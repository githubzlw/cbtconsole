package com.importExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;
import com.importExpress.mapper.OrderSplitRecordMapper;
import com.importExpress.pojo.OrderSplitChild;
import com.importExpress.pojo.OrderSplitMain;
import com.importExpress.service.OrderSplitRecordService;
@Service
public class OrderSplitRecordServiceImpl implements OrderSplitRecordService {
	@Autowired
	private OrderSplitRecordMapper orderSplitRecordMapper;
	private IOrderSplitDao splitDao = new OrderSplitDaoImpl();

	@Override
	public int insertMainOrder(OrderSplitMain order) {
		int countMainOrder = orderSplitRecordMapper.countMainOrder(order.getOrderid());
		if(countMainOrder == 0) {
			return orderSplitRecordMapper.insertMainOrder(order);
		}
		return countMainOrder;
	}

	@Override
	public int insertChildOrder(String orderid) {
		String mainId = "";
		double orderMainWeight = 0;
		int lastIndexOf = orderid.lastIndexOf("_");
		if(lastIndexOf > orderid.indexOf("_")) {
			String orderParent = orderid.substring(0, lastIndexOf);
			orderSplitRecordMapper.updateChildOrder(orderParent,orderid);
			OrderSplitChild mainOrder = orderSplitRecordMapper.getChildOrder(orderParent);
			mainId = mainOrder.getMainid();
			orderMainWeight = mainOrder.getWeight();
		}else {
			OrderSplitMain mainOrder = orderSplitRecordMapper.getMainOrder(orderid.split("_")[0]);
			mainId = String.valueOf(mainOrder.getId());
			orderMainWeight = mainOrder.getWeight();
		}
		
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
		double orderChildFeigth = orderChildWeight * 3;
		
		//计算总单运费
		double orderMainFeight = orderMainWeight * 5;
		
		//剩余子单重量
		double otherWeight = orderMainWeight - orderChildWeight;
		//运费
		double otherfeight = otherWeight * 4;
		
		
		double feight = otherfeight + orderChildFeigth - orderMainFeight;
		int recommend = feight < orderChildCost * 0.3 ? 1 : 2;
		
		orderChild.setRecommend(recommend);
		
		int insertChildOrder = orderSplitRecordMapper.insertChildOrder(orderChild );
		return insertChildOrder;
	}

	@Override
	public OrderSplitChild getOrder(String splitOrerId) {
		// TODO Auto-generated method stub
		return orderSplitRecordMapper.getOrder(splitOrerId);
	}

	
	

}
