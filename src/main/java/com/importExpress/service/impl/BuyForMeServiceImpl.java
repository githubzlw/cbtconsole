package com.importExpress.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cbt.pojo.Buy4MeCusotme;
import com.cbt.pojo.BuyForMeStatistic;
import com.cbt.util.GetConfigureInfo;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.google.gson.Gson;
import com.importExpress.utli.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Admuser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.mapper.BuyForMeMapper;
import com.importExpress.pojo.*;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

@Service
@Slf4j
public class BuyForMeServiceImpl implements com.importExpress.service.BuyForMeService {

	private static final String getFreightCostUrl = GetConfigureInfo.getValueByCbt("getMinFreightUrl");

	private static UrlUtil instance = UrlUtil.getInstance();

	@Autowired
	private BuyForMeMapper buyForMemapper;

	@Override
	public List<BFOrderInfo> getOrders(Map<String, Object> map) {
		List<BFOrderInfo> orders = buyForMemapper.getOrders(map);
		if(orders == null) {
			return orders;
		}
//		订单状态：-1 取消，0申请，1处理中 2销售处理完成 3已支付;
		orders.stream().forEach(o->{
			String content = o.getState() == -1 ? "取消" : o.getState() == 0 ?
					"申请中":o.getState() == 1 ?"处理中":o.getState() == 2 ?
							"销售处理完成":o.getState() == 3 ?"已支付":"";
			o.setStateContent(content);
		});
		return orders;
	}

	@Override
	public int getOrdersCount(Map<String, Object> map) {
		return buyForMemapper.getOrdersCount(map);
	}

	@Override
	public List<BFOrderDetail> getOrderDetails(String orderNo, String bfId) {
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
		if(detailSku.getId() == 0) {
			result = buyForMemapper.addOrderDetailsSku(detailSku);
		}else {
			result = buyForMemapper.updateOrderDetailsSku(detailSku);
		}
		buyForMemapper.updateOrdersDetailsState(detailSku.getBfDetailsId(),1);
		buyForMemapper.updateOrdersState(detailSku.getBfId(),1);
		
		return result;
	}
	@Override
	public int updateOrderDetailsSkuState(int id, int state) {
		return buyForMemapper.updateOrderDetailsSkuState(id, state);
	}
	@Override
	public int finshOrder(int id) {
		return buyForMemapper.updateOrdersState(id,2);
	}

	@Override
	public Map<String, Object> getOrder(String orderNo) {
		return buyForMemapper.getOrder(orderNo);
	}
	@Override
	public int updateOrderDetailsSkuWeight(String weight, int bfdid) {
		return buyForMemapper.updateOrderDetailsSkuWeight(weight,bfdid);
	}
	@Override
	public int updateDeliveryTime(String orderNo, String time, String feight, String method){
		int updateDeliveryTime = buyForMemapper.updateDeliveryTime(orderNo,time,feight,method);
		if(updateDeliveryTime > 0) {
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
		return buyForMemapper.insertRemark(orderNo,remark);
	}

	@Override
	public List<Map<String,String>> getRemark(String orderNo) {
		return buyForMemapper.getRemark(orderNo);
	}
	@Override
	public List<TransportMethod> getTransport() {
		Map<String,List<String>> result = Maps.newHashMap();
		List<Map<String,String>> transport = buyForMemapper.getTransport();
		transport.stream().forEach(t->{
			String key = t.get("shipping_time");
			List<String> value = result.get(key);
			value = value == null ? Lists.newArrayList() : value;
			value.add(t.get("transport_mode"));
			result.put(key, value);
		});
		List<TransportMethod> list =  Lists.newArrayList();
		result.entrySet().forEach(r->{
			list.add(TransportMethod.builder().time(r.getKey()).method(r.getValue()).build());
		});
		return list;
	}

	@Override
	public int updateOrdersDetailsRemark(int id, String remark) {
		int update = buyForMemapper.updateOrdersDetailsRemark(id, remark);
		if(update > 0) {
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
		if(update > 0) {
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
		if(deleteProduct > 0) {
			String sql = "update buyforme_details a left join  buyforme_details_sku b on a.id=b.bf_details_id " + 
					"set a.state=-1,b.state=-1 where a.id="+bfdid;
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
	public int cancelOrders(int id) {
		int update = buyForMemapper.updateOrderAllState(id);
		if(update > 0) {
			String sql = "update  buyforme_orderinfo a left join " + 
					" buyforme_details_sku b on a.id=b.bf_id " + 
					" left join  buyforme_details c on c.order_no=a.order_no " + 
					"set a.state=-1,b.state=-1,c.state=-1  where a.id="+id;
			SendMQ.sendMsg(new RunSqlModel(sql));
		}
		return update;
	}
	@Override
	public EasyUiJsonResult queryCustomers(ShopCarUserStatistic statistic){
		EasyUiJsonResult json=new EasyUiJsonResult();
		CommonResult commonResult = new CommonResult();
		String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId","buy4me/queryAll");

		com.alibaba.fastjson.JSONObject jsonObject;
		try {
			String requestUrl = url;
			jsonObject = instance.doGet(requestUrl);
			commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
		} catch (IOException e) {
			log.error("CartController refresh ",e);
		}
		if(commonResult.getCode() == 200){

			int limitNum = statistic.getLimitNum();
			int num = statistic.getNum();
			List<String> list = (List<String>)commonResult.getData();
			int length = 0;
			if (list == null) {
				list = new ArrayList<>();
			} else {
				length = list.size();
				if (length > 1) {
					// 分页
					list = list.stream().skip(num).limit(limitNum).collect(Collectors.toList());
				}
			}
			List<Buy4MeCusotme> list1 = new ArrayList<>();
			String s = "car:";
			list.stream().forEach(e ->{
				if(e.indexOf(s) > -1){
					e = e.split(s)[1];
				}
				Buy4MeCusotme buy4MeCusotme = new Buy4MeCusotme();
				buy4MeCusotme.setUserId(e);
				buy4MeCusotme.setJumpLink(e);
				list1.add(buy4MeCusotme);
			});
			json.setRows(list1);
			json.setTotal(length);
		}else {
			json.setSuccess(false);
		}
		return json;
	}
	@Override
	public JsonResult getCustomerCartDetails(String userId){

		JsonResult jsonResult = new JsonResult();
		CommonResult commonResult = new CommonResult();
		String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId","buy4me/"+userId);
		jsonResult.setErrorInfo("error!");
		com.alibaba.fastjson.JSONObject jsonObject;
		try {
			String requestUrl = url;
			jsonObject = instance.doGet(requestUrl);
			commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
		} catch (IOException e) {
			log.error("CartController refresh ",e);
		}
		if(commonResult.getCode() == 200){
			String data1 = (String)commonResult.getData();
			BuyForMeStatistic data = new Gson().fromJson(data1, BuyForMeStatistic.class);
			jsonResult = JsonResult.success(data);
		}
		return jsonResult;
	}

	@Override
	public CommonResult putMsg(String userId,String itemid,String msg){

		JsonResult jsonResult = new JsonResult();
		CommonResult commonResult = new CommonResult();
		String url = getFreightCostUrl.replace("shopCartMarketingCtr/getMinFreightByUserId","buy4me/"+userId+"/"+itemid);
		jsonResult.setErrorInfo("error!");
		com.alibaba.fastjson.JSONObject jsonObject;
		try {
			String requestUrl = url;
			Map<String,Object> map = new HashMap<>();
			map.put("msg",msg);
			jsonObject = instance.doPost (requestUrl,map);
			commonResult = new Gson().fromJson(jsonObject.toJSONString(), CommonResult.class);
		} catch (IOException e) {
			log.error("CartController refresh ",e);
		}
		return commonResult;
	}

}
