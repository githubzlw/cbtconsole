package com.importExpress.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.jdbc.DBHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.mapper.BuyForMeMapper;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.DetailsSku;
import com.importExpress.pojo.TransportMethod;
import com.importExpress.service.BuyForMeService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

@Service
public class BuyForMeServiceImpl implements BuyForMeService {
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
	public int updateOrderDetailsSkuState(int id,int state) {
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
	public int updateOrderDetailsSkuWeight(String weight,int bfdid) {
		return buyForMemapper.updateOrderDetailsSkuWeight(weight,bfdid);
	}
	@Override
	public int updateDeliveryTime(String orderNo,String time,String feight,String method){
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
	public int insertRemark(String orderNo,String remark) {
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
	

}
