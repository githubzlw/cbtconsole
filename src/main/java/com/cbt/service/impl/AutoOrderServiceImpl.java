package com.cbt.service.impl;

import com.cbt.bean.Address;
import com.cbt.bean.AutoOrderBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.service.AutoOrderService;
import com.cbt.website.dao.PaymentDao;
import com.cbt.website.dao.PaymentDaoImp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AutoOrderServiceImpl implements AutoOrderService {
	private IOrderDao dao = new OrderDao();
	private PaymentDaoImp paymentDao = new PaymentDao();

	@Override
	public int addOrderInfo(List<OrderBean> OrderBean, int address, int odcount) {

		return dao.addOrderInfo(OrderBean, address, odcount);
	}

	@Override
	public int addOrderDetail(List<OrderDetailsBean> orderdetails) {

		return dao.addOrderDetail(orderdetails);
	}

	@Override
	public int addOrderAddress(Map<String, Object> map) {

		return dao.addOrderAddress(map);
	}

	@Override
	public List<Address> getUserAddr(int userid) {

		return dao.getUserAddr(userid);
	}

	@Override
	public String getOrderNo() {

		return dao.getOrderNo();
	}

	@Override
	public String getExchangeRate() {
		return dao.getExchangeRate();
	}

	@Override
	public List<AutoOrderBean> getOrderList(String orderid, String userid,
			String page) {
		int int_page = Integer.valueOf(page);
		int_page = (int_page-1)*40;
		return paymentDao.getOrderList(orderid, userid, int_page);
	}


	@Override
	public int cancelOrder(String orderid) {
		
		return dao.cancelOrder(orderid);
	}
	@Override
	public int cancelPayment(String pid) {
		
		return paymentDao.cancelPayment(pid);
	}

}
