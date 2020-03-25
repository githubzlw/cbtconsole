package com.importExpress.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.importExpress.mapper.BuyForMeMapper;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.DetailsSku;
import com.importExpress.service.BuyForMeService;

@Service
public class BuyForMeServiceImpl implements BuyForMeService {
	@Autowired
	private BuyForMeMapper buyForMemapper;

	@Override
	public List<BFOrderInfo> getOrders(Map<String, Object> map) {
		
		return buyForMemapper.getOrders(map);
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
	public int updateDeliveryTime(String orderNo,String time) {
		return buyForMemapper.updateDeliveryTime(orderNo,time);
	}

}
