package com.importExpress.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cbt.common.UrlUtil;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Admuser;
import com.cbt.util.GetConfigureInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.importExpress.mapper.BuyForMeMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.BuyForMeService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BuyForMeServiceImpl implements BuyForMeService {
    private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getMinFreightUrl");
     private static com.cbt.common.UrlUtil instance = UrlUtil.getInstance();
    @Autowired
    private BuyForMeMapper buyForMemapper;

    @Override
    public List<BFOrderInfo> getOrders(Map<String, Object> map) {
        List<BFOrderInfo> orders = buyForMemapper.getOrders(map);
        if (orders == null) {
            return orders;
        }
//		订单状态：-1 取消，0申请，1处理中 2销售处理完成 3已支付;
		orders.stream().forEach(o->{
			String content = o.getState() == -1 ? "取消" : o.getState() == 0 ?
					"申请中":o.getState() == 1 ?"处理中":o.getState() == 2 ?
							"销售处理完成":o.getState() == 3 ?"待支付":o.getState() == 4 ?"已支付":"";
			o.setStateContent(content);
		});
		return orders;
	}

    @Override
    public int getOrdersCount(Map<String, Object> map) {
        return buyForMemapper.getOrdersCount(map);
    }

	@Override
	public List<BFOrderDetail> getOrderDetails(String orderNo,String bfId) {
		List<BFOrderDetail> orderDetails = buyForMemapper.getOrderDetails(orderNo);
		if(orderDetails == null || orderDetails.isEmpty()) {
			return Lists.newArrayList();
		}
		List<BFOrderDetailSku> orderDetailsSku = getOrderDetailsSku(bfId);
		if(orderDetailsSku == null || orderDetailsSku.isEmpty()) {
			return orderDetails;
		}
		Map<Integer,List<DetailsSku>> detailsIdSku = Maps.newHashMap();
		orderDetailsSku.stream().forEach(o->{
			int bfDetailsId = o.getBfDetailsId();
			List<DetailsSku> list = detailsIdSku.get(bfDetailsId);
			list = list == null ? Lists.newArrayList() : list;
			DetailsSku detailsSku = DetailsSku.builder().num(o.getNum()).skuid(o.getSkuid())
			.price(o.getPrice()).url(o.getProductUrl()).sku(o.getSku()).id(o.getId())
			.priceBuy(o.getPriceBuy()).priceBuyc(o.getPriceBuyc()).shipFeight(o.getShipFeight())
			.weight(o.getWeight())
			.state(o.getState())
			.unit(o.getUnit())
            .imgUrl(o.getImgUrl())
			.build();
			list.add(detailsSku);
			detailsIdSku.put(bfDetailsId, list);
		});
		orderDetails.stream().forEach(o->{
			List<DetailsSku> list = detailsIdSku.get(o.getId());
			o.setSkus(list);
			if(list != null && !list.isEmpty()) {
				o.setSkuCount(list.size());
				o.setWeight(list.get(0).getWeight());
				o.setCount(list.stream().filter(s->s.getState()>0).mapToInt(DetailsSku::getNum).sum());
			}
		});
		
		return orderDetails;
	}

    @Override
    public List<BFOrderDetailSku> getOrderDetailsSku(String bfId) {
        return buyForMemapper.getOrderDetailsSku(bfId);
    }

    @Override
    public int addOrderDetailsSku(BFOrderDetailSku detailSku) {
        int result = 0;
        if (detailSku.getId() == 0) {
            // result = buyForMemapper.addOrderDetailsSku(detailSku);
            // 使用MQ插入sku数据
            List<String> lstValues = Lists.newArrayList();
            String sql2 = "insert into buyforme_details_sku(sku,product_url,num,price,price_buy,price_buy_c,ship_feight,weight,unit,state,id,bf_id,bf_details_id,num_iid,skuid,remark)" +
                    "  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            lstValues.add(detailSku.getSku());
            lstValues.add(detailSku.getProductUrl());
            lstValues.add(String.valueOf(detailSku.getNum()));
            lstValues.add(detailSku.getPrice());
            lstValues.add(detailSku.getPriceBuy());
            lstValues.add(detailSku.getPriceBuyc());
            lstValues.add(detailSku.getShipFeight());
            lstValues.add(detailSku.getWeight());
            lstValues.add(detailSku.getUnit());
            lstValues.add(String.valueOf(detailSku.getState()));
            lstValues.add(String.valueOf(detailSku.getId()));
            lstValues.add(String.valueOf(detailSku.getBfId()));
            lstValues.add(String.valueOf(detailSku.getBfDetailsId()));
            lstValues.add(detailSku.getNumIid());
            lstValues.add(detailSku.getSkuid());
            lstValues.add(detailSku.getRemark());
            lstValues.add(String.valueOf(detailSku.getState()));
            SendMQ.sendMsg(new RunSqlModel(DBHelper.covertToSQL(sql2, lstValues)));
            result = 99;
        } else {
            result = buyForMemapper.updateOrderDetailsSku(detailSku);
        }
        buyForMemapper.updateOrdersDetailsState(detailSku.getBfDetailsId(), 1);
        buyForMemapper.updateOrdersState(detailSku.getBfId(), 1);

        return result;
    }

    @Override
    public int updateOrderDetailsSkuState(int id, int state) {
        return buyForMemapper.updateOrderDetailsSkuState(id, state);
    }

    @Override
    public int finshOrder(int id) {
        return buyForMemapper.updateOrdersState(id, 2);
    }

    @Override
    public Map<String, Object> getOrder(String orderNo) {
        return buyForMemapper.getOrder(orderNo);
    }

    @Override
    public int updateOrderDetailsSkuWeight(String weight, int bfdid) {
        return buyForMemapper.updateOrderDetailsSkuWeight(weight, bfdid);
    }

    @Override
    public int updateDeliveryTime(String orderNo, String time, String feight, String method) {
        int updateDeliveryTime = buyForMemapper.updateDeliveryTime(orderNo, time, feight, method);
        if (updateDeliveryTime > 0) {
            String sql = "update buyforme_orderinfo set delivery_time =?,ship_feight=?,delivery_method=? where order_no=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(time);
            lstValue.add(feight);
            lstValue.add(method);
            lstValue.add(orderNo);
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return updateDeliveryTime;
    }

    @Override
    public int insertRemark(String orderNo, String remark) {
        return buyForMemapper.insertRemark(orderNo, remark);
    }

    @Override
    public List<Map<String, String>> getRemark(String orderNo) {
        return buyForMemapper.getRemark(orderNo);
    }

    @Override
    public List<TransportMethod> getTransport() {
        Map<String, List<String>> result = Maps.newHashMap();
        List<Map<String, String>> transport = buyForMemapper.getTransport();
        transport.stream().forEach(t -> {
            String key = t.get("shipping_time");
            List<String> value = result.get(key);
            value = value == null ? Lists.newArrayList() : value;
            value.add(t.get("transport_mode"));
            result.put(key, value);
        });
        List<TransportMethod> list = Lists.newArrayList();
        result.entrySet().forEach(r -> {
            list.add(TransportMethod.builder().time(r.getKey()).method(r.getValue()).build());
        });
        return list;
    }

    @Override
    public int updateOrdersDetailsRemark(int id, String remark) {
        int update = buyForMemapper.updateOrdersDetailsRemark(id, remark);
        if (update > 0) {
            String sql = "update buyforme_details set remark_replay=? where id=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(remark);
            lstValue.add(String.valueOf(id));
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return update;
    }

    @Override
    public int updateOrdersAddress(Map<String, String> map) {
        int update = buyForMemapper.updateOrdersAddress(map);
        if (update > 0) {
            String sql = "update buyforme_address set address=?,address2=?," +
                    "country=?,phone_number=?,zip_code=?,statename=?,street=?,recipients=?  where id=?";
            List<String> lstValue = Lists.newArrayList();
            lstValue.add(map.get("address"));
            lstValue.add(map.get("address2"));
            lstValue.add(map.get("country"));
            lstValue.add(map.get("phone"));
            lstValue.add(map.get("code"));
            lstValue.add(map.get("statename"));
            lstValue.add(map.get("street"));
            lstValue.add(map.get("recipients"));
            lstValue.add(map.get("id"));
            String covertToSQL = DBHelper.covertToSQL(sql, lstValue);
            SendMQ.sendMsg(new RunSqlModel(covertToSQL));
        }
        return update;
    }

    @Override
    public List<Admuser> lstAdms() {
        return buyForMemapper.lstAdms();
    }

    @Override
    public int deleteProduct(int bfdid) {
        int deleteProduct = buyForMemapper.deleteProduct(bfdid);
        if (deleteProduct > 0) {
            String sql = "update buyforme_details a left join  buyforme_details_sku b on a.id=b.bf_details_id " +
                    "set a.state=-1,b.state=-1 where a.id=" + bfdid;
            SendMQ.sendMsg(new RunSqlModel(sql));
        }
        return deleteProduct;
    }

    @Override
    public List<ZoneBean> lstCountry() {
        return buyForMemapper.lstCountry();
    }

    @Override
    public int insertBFChat(BFChat bfChat) {
        return buyForMemapper.insertBFChat(bfChat);
    }

    @Override
    public List<BFChat> queryBFChatList(BFChat bfChat) {
        return buyForMemapper.queryBFChatList(bfChat);
    }

    @Override
    public List<BuyForMeSearchLog> querySearchList(BuyForMeSearchLog searchLog) {
        List<BuyForMeSearchLog> buyForMeSearchLogs = buyForMemapper.querySearchList(searchLog);
        buyForMeSearchLogs.stream().forEach(buyForMeSearchLog ->{
            String ip = buyForMeSearchLog.getIp();
            if(ip.indexOf("192.168")>-1|| ip.indexOf("127.0")>-1){
                ip = "2.24.0.0";
            }
             String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId","queryNameByIp?ip="+ip);
            CommonResult commonResult =null;
            try {
                String requestUrl = url;
                JSONObject jsonObject = instance.doGet(requestUrl);
                commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
            } catch (IOException e) {
                log.error("CartController refresh ",e);
            }
            if(commonResult.getCode() == 200){
                String data = (String) commonResult.getData();
                buyForMeSearchLog.setCountryName(data);
            }
            });
        return buyForMeSearchLogs;
    }

    @Override
    public int querySearchListCount(BuyForMeSearchLog searchLog) {
        return buyForMemapper.querySearchListCount(searchLog);
    }

    @Override
    public List<BFSearchStatic> queryStaticList(BFSearchStatic searchStatic) {
        return buyForMemapper.queryStaticList(searchStatic);
    }

    @Override
    public int queryStaticListCount(BFSearchStatic searchStatic) {
        return buyForMemapper.queryStaticListCount(searchStatic);
    }

    @Override
    public int insertIntoSearchStatic(BFSearchStatic searchStatic) {
        return buyForMemapper.insertIntoSearchStatic(searchStatic);
    }

    @Override
    public int updateSearchStatic(BFSearchStatic searchStatic) {
        return buyForMemapper.updateSearchStatic(searchStatic);
    }

    @Override
    public int deleteSearchStatic(BFSearchStatic searchStatic) {
        return buyForMemapper.deleteSearchStatic(searchStatic);
    }

    @Override
    public List<BFSearchPid> queryPidByStaticId(int staticId) {
        return buyForMemapper.queryPidByStaticId(staticId);
    }

    @Override
    public int insertIntoStaticPid(BFSearchPid searchPid) {
        return buyForMemapper.insertIntoStaticPid(searchPid);
    }

    @Override
    public int updateStaticPid(BFSearchPid searchPid) {
        return buyForMemapper.updateStaticPid(searchPid);
    }

    @Override
    public int deleteStaticPid(BFSearchPid searchPid) {
        return buyForMemapper.deleteStaticPid(searchPid);
    }

    @Override
    public int cancelOrders(int id) {
        int update = buyForMemapper.updateOrderAllState(id);
        if (update > 0) {
            String sql = "update  buyforme_orderinfo a left join " +
                    " buyforme_details_sku b on a.id=b.bf_id " +
                    " left join  buyforme_details c on c.order_no=a.order_no " +
                    "set a.state=-1,b.state=-1,c.state=-1  where a.id=" + id;
            SendMQ.sendMsg(new RunSqlModel(sql));
        }
        return update;
    }


}
