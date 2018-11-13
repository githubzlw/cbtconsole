package com.cbt.processes.service;

import com.cbt.bean.Payment;
import com.cbt.processes.dao.PaymentDao;
import com.cbt.processes.dao.ProcessPaymentDao;

public class PayServer implements IPayServer {

	ProcessPaymentDao dao= new PaymentDao();
	@Override
	public void addPayment(Payment pay) {
		// TODO Auto-generated method stub
		try {
			dao.addPayment(pay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Payment getPayment(int userid, String orderid, String paymentid) {
		// TODO Auto-generated method stub
		return dao.getPayment(userid, orderid, paymentid);
	}
	@Override
	public int getPayment(String paymentid) {
		// TODO Auto-generated method stub
		return dao.getPayment(paymentid);
	}
}

