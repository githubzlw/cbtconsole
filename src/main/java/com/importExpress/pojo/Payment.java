package com.importExpress.pojo;

import java.io.Serializable;


public class Payment implements Serializable {
	
	private static final long serialVersionUID = -1947764997021064091L;
	
	private int id;
	private int userid;//用户id
	private String orderid;//订单流水号
	private String paymentid;//付款流水号
	private String orderdesc;//订单描述
	private String username;//用户名
	private int paystatus;//付款状态
	private float payment_amount;//付款金额
	private String payment_cc;//付款币种
	private String createtime;
	private String paySID;//本地交易申请号
	private String payflag;
	private String paytype; //交易方式     渠道
	private float payment_other;//合并扣除费用
	private String paypalAccount; //PayPal
	private String paymentNo;//支付ID
	private double transaction_fee;//paypal手续费
	private String paypalid;//paypalid
	
	private String paymentDate;
	private String paybtype;
	
	private double balance;
	private double orderAmount;
	private String exchange_rate;//订单支付时的汇率
	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getOrderdesc() {
		return orderdesc;
	}
	public void setOrderdesc(String orderdesc) {
		this.orderdesc = orderdesc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}
	public float getPayment_amount() {
		return payment_amount;
	}
	public void setPayment_amount(float payment_amount) {
		this.payment_amount = payment_amount;
	}
	public String getPayment_cc() {
		return payment_cc;
	}
	public void setPayment_cc(String payment_cc) {
		this.payment_cc = payment_cc;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPaySID() {
		return paySID;
	}
	public void setPaySID(String paySID) {
		this.paySID = paySID;
	}
	public String getPayflag() {
		return payflag;
	}
	public void setPayflag(String payflag) {
		this.payflag = payflag;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public void setPayment_other(float payment_other) {
		this.payment_other = payment_other;
	}
	public float getPayment_other() {
		return payment_other;
	}
	
	public String getPaypalAccount() {
		return paypalAccount;
	}
	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}
	
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaybtype() {
		return paybtype;
	}
	public void setPaybtype(String paybtype) {
		this.paybtype = paybtype;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public double getTransaction_fee() {
		return transaction_fee;
	}
	public void setTransaction_fee(double transaction_fee) {
		this.transaction_fee = transaction_fee;
	}
	public String getPaypalid() {
		return paypalid;
	}
	public void setPaypalid(String paypalid) {
		this.paypalid = paypalid;
	}
	
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"userid\":\"%s\", \"orderid\":\"%s\", \"paymentid\":\"%s\", \"orderdesc\":\"%s\", \"username\":\"%s\", \"paystatus\":\"%s\", \"payment_amount\":\"%s\", \"payment_cc\":\"%s\", \"createtime\":\"%s\"}",
						id, userid, orderid, paymentid, orderdesc, username,
						paystatus, payment_amount, payment_cc, createtime);
	}
	
}
