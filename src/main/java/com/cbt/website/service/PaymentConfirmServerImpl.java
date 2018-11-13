package com.cbt.website.service;

import com.cbt.website.dao.PaymentConfirmDao;
import com.cbt.website.dao.PaymentConfirmDaoImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentConfirmServerImpl implements PaymentConfirmServer {

//	@Autowired
//	private PaymentConfirmDao paymentConfirmDao;
	private PaymentConfirmDao paymentConfirmDao = new PaymentConfirmDaoImpl();
	
	
	@Override
	public Map<String, Object> getPaymentConfirm(String orderno) {
		return paymentConfirmDao.selectPaymentConfirm(orderno);
	}
	@Override
	public int addPaymentConfirm(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId) {
		return paymentConfirmDao.insertPaymentConfirm(orderNo, confirmname, confirmtime, paytype, tradingencoding, wtprice, userId);
	}
	
	//确认订单
	@Override
	public int confirmOrder(String orderNo, String confirmname,String confirmtime, String paytype, String tradingencoding,String wtprice, int userId) throws Exception {
		return paymentConfirmDao.insertPaymentConfirm(orderNo, confirmname, confirmtime, paytype, tradingencoding, wtprice, userId);
	}
	

}
