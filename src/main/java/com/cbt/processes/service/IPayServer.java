package com.cbt.processes.service;

import com.cbt.bean.Payment;

public interface IPayServer {
	/*wanyang添加付款记录*/
	public void addPayment(Payment pay);
	/*wanyang获取返回的成功付款记录*/
	public Payment getPayment(int userid, String orderid, String paymentid);
	/*查询是否已付完款 ylm*/
	public int getPayment(String paymentid);
}
