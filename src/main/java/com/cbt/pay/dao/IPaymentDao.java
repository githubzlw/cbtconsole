package com.cbt.pay.dao;

import com.cbt.bean.Payment;
import com.cbt.bean.RechargeRecord;

import java.util.List;

public interface IPaymentDao {
	/*付款成功后添加一条记录到数据库--wanyang*/
	public void addPayment(Payment pay);
	/*显示当前的付款记录---wanyang*/
	public Payment getPayment(int userid, String orderid);
	/*查询是否已付完款 ylm*/
	public int getPayment(String paymentid);
	/*向paylog表中插入交易信息(IPN记录)-wanyang*/
	public void insertintoPaylog(Payment pay, String paymentDate, int paybtype);
	/*向paylog表中插入交易信息(付款时记录)-wanyang*/
	/*userid:用户id;
	 *orderid:订单id;
	 *paySID:本地交易申请号
	 *payment_amount:付款金额
	 *username:用户名
	 *paymentDate:付款日期*/
	public void insertintoPaylog(int userid, String orderid, String paySID, float payment_amount, String username, String paylogdesc, String paybtype);
	/*后台处理页面的付款到账确认功能-wanyang-20150722*/
	public void insertintoPayconfirm(String orderid, String name);
	/**
	 * 订单重复付款后往充值记录表中加入一条记录
	 *
	 * @param user
	 * 	用户信息
	 */
	public void addRechargeRecord(RechargeRecord rr);
	public Payment getPayment(int userid, String orderid, String payflag);

	/**
	 * 付款成功后添加一条记录到数据库
	 *
	 * @param user
	 * 	用户信息
	 */
	public void addPayments(List<Payment> pay);
	/**
	 * 查询paylog中是否存在数据
	 *
	 * @param user
	 * 	用户信息
	 */
	public String getPayLog(String orderNo, String sid);
	/**
	 * 关联到账的信息
	 *
	 */
	public List<Payment> getOrdersPayList(String orderNo, int paystatus);
}
