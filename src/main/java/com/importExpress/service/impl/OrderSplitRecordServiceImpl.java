package com.importExpress.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.warehouse.dao.ZoneMapper;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;
import com.importExpress.mapper.OrderSplitRecordMapper;
import com.importExpress.pojo.OrderSplitChild;
import com.importExpress.pojo.OrderSplitMain;
import com.importExpress.service.OrderSplitRecordService;
import com.importExpress.utli.FreightUtlity;
@Service
public class OrderSplitRecordServiceImpl implements OrderSplitRecordService {
	@Autowired
	private OrderSplitRecordMapper orderSplitRecordMapper;
	@Autowired
	private ZoneMapper zoneMapper;
	
	private IOrderSplitDao splitDao = new OrderSplitDaoImpl();
	private DecimalFormat format = new DecimalFormat("#0.000");

	@Override
	public int insertMainOrder(OrderSplitMain order) {
		int countMainOrder = orderSplitRecordMapper.countMainOrder(order.getOrderid());
		
		if(countMainOrder == 0) {
			int idFormCountry = zoneMapper.getIdFormCountry(order.getCountryName());
			order.setCountry(idFormCountry);
			double freightByWeight = FreightUtlity.getFreightByWeight(idFormCountry, format.format(order.getWeight()), order.getModeTransport());
			order.setFeight(freightByWeight);
			return orderSplitRecordMapper.insertMainOrder(order);
		}
		return countMainOrder;
	}

	@Override
	public int insertChildOrder(OrderSplitMain orderMain, String orderid) {
		double orderMainWeight = orderMain.getWeight();
		
		OrderSplitMain mainOrder = orderSplitRecordMapper.getMainOrder(orderid.split("_")[0]);
		int mainId = mainOrder.getId();
		int country = mainOrder.getCountry();
		String modeTransport = mainOrder.getModeTransport();
		
		//获取子单订单商品详情
		List<OrderDetailsBean> orderDetails = splitDao.getOrdersDetails_split(orderid);
		double orderChildCost = 0;// 订单商品总价
        double orderChildWeight = 0;// 订单商品总重量
		for(OrderDetailsBean d : orderDetails) {
			orderChildCost += Double.valueOf(d.getGoodsprice()) * d.getYourorder();
			orderChildWeight += Double.valueOf(d.getActual_weight());
		}
		
		OrderSplitChild orderChild = new OrderSplitChild();
		orderChild.setOrderid(orderid);
		orderChild.setMainid(mainId);
		orderChild.setWeight(orderChildWeight);
		orderChild.setCost(orderChildCost);
		orderChild.setCountry(country);
		orderChild.setModeTransport(modeTransport);
		
		double orderChildFeigth = FreightUtlity.getFreightByWeight(country, format.format(orderChildWeight),modeTransport) ;
		
		//计算总单运费
		double orderMainFeight = FreightUtlity.getFreightByWeight(country, format.format(orderMainWeight),modeTransport);
		
		//剩余子单重量
		double otherWeight = orderMainWeight - orderChildWeight;
		//运费
		double otherfeight = FreightUtlity.getFreightByWeight(country, format.format(otherWeight),modeTransport);
		
		double feight = otherfeight + orderChildFeigth - orderMainFeight;
		int recommend = feight < orderChildCost * 0.3 ? 1 : 2;
		
		orderChild.setRecommend(recommend);
		orderChild.setFeight(orderChildFeigth);
		
		int insertChildOrder = orderSplitRecordMapper.insertChildOrder(orderChild );
		return insertChildOrder;
	}

	@Override
	public OrderSplitChild getOrder(String splitOrerId) {
		// TODO Auto-generated method stub
		return orderSplitRecordMapper.getOrder(splitOrerId);
	}

	
	

}
