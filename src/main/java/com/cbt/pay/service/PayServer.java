package com.cbt.pay.service;

import com.cbt.bean.Payment;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;

import java.util.List;

public class PayServer implements IPayServer {

	IPaymentDao dao= new PaymentDao();
	@Override
	public void addPayment(Payment pay) {
		// TODO Auto-generated method stub
		dao.addPayment(pay);
	}
	@Override
	public Payment getPayment(int userid, String orderid) {
		// TODO Auto-generated method stub
		return dao.getPayment(userid, orderid);
	}
	@Override
	public int getPayment(String paymentid) {
		// TODO Auto-generated method stub
		return dao.getPayment(paymentid);
	}
	@Override
	public int validateOrder(int userid, String orderid) {
		// TODO Auto-generated method stub
		int flag=1;//1代表重复付款的订单记录，0代表首次支付的订单记录
		Payment pay=new Payment();
		pay =  dao.getPayment(userid, orderid);
		if(pay.getPaymentid()==null){
			flag=0;
		}
		return flag;
	}
	@Override
	public int validateOrder2(int userid, String orderid,String payflag) {
		// TODO Auto-generated method stub
		int flag=1;//1代表重复付款的订单记录，0代表首次支付的订单记录
		Payment pay =  dao.getPayment(userid, orderid, payflag);
		if(pay.getOrderid()== null){
			flag=0;
		}
		return flag;
	}
	/*
	 * 关联到账的信息
	 * @see com.cbt.pay.service.IPayServer#getOrdersPayList(java.lang.String)
	 */
	@Override
	public List<Payment> getOrdersPayList(String orderNo, int paystatus) {
		
		return dao.getOrdersPayList(orderNo,paystatus);
	}
}
