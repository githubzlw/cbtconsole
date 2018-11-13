package com.cbt.pay.service;

import com.cbt.bean.Payment;

import java.util.List;

public interface IPayServer {
	/*wanyang添加付款记录*/
	public void addPayment(Payment pay);
	/*wanyang获取返回的成功付款记录*/
	public Payment getPayment(int userid, String orderid);
	/*查询是否已付完款 ylm*/
	public int getPayment(String paymentid);
	/*验证订单是否重复支付*/
	public int validateOrder(int userid, String orderid);
	
	public int validateOrder2(int userid, String orderid, String payflag);
	/**
	 * 关联到账的信息
	 */
	public List<Payment> getOrdersPayList(String orderNo, int paystatus);
}
