package com.cbt.processes.dao;

import com.cbt.bean.Payment;
import com.cbt.bean.RechargeRecord;

public interface ProcessPaymentDao {
	
	/*付款成功后添加一条记录到数据库--wanyang*/
	public void addPayment(Payment pay) throws Exception;
	/*显示当前的付款记录---wanyang*/
	public Payment getPayment(int userid, String orderid, String paymentid);
	/*查询是否已付完款 ylm*/
	public int getPayment(String paymentid);
	

	/**
	 * 添加充值记录
	 * 
	 * @param user
	 * 	用户信息
	 */
	public void addRechargeRecord(RechargeRecord rr);
}
