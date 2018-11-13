package com.cbt.warehouse.service;

import com.cbt.bean.OrderBuyBean;
import com.cbt.warehouse.dao.IOrderbuySSMDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBuyServiceImpl implements IOrderBuyService {
	
	@Autowired
	private IOrderbuySSMDao orderbuySSMDAO;
	
	@Override
	public List<OrderBuyBean> getOrderBuyById(Integer uid) {
		// TODO Auto-generated method stub
		return orderbuySSMDAO.getOrderBuyById(uid);
	}

	@Override
	public OrderBuyBean getById(Integer id) {
		// TODO Auto-generated method stub
		return orderbuySSMDAO.getById(id);
	}

	@Override
	public Integer delById(Integer id) {
		// TODO Auto-generated method stub
		return orderbuySSMDAO.delById(id);
	}

	@Override
	public Integer update(OrderBuyBean t) {
		// TODO Auto-generated method stub
		return orderbuySSMDAO.update(t);
	}

	@Override
	public Integer add(OrderBuyBean t) {
		// TODO Auto-generated method stub
		return orderbuySSMDAO.add(t);
	}

}
