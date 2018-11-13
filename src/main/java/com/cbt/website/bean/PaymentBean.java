package com.cbt.website.bean;

public class PaymentBean {
	private int userid;
	private String orderid;
	private String paymentid;
	private String payment_other;
	private String payment_amount;//该订单支付金额
	private String payment_cc;
	private String orderdesc;
	private String username;
	private String paystatus;
	private String createtime;
	private String paySID;
	private String payflag;
	private String paytype;//支付类型
	private String total;//总页数
	private String page;//本次查询页码
	private double total_money;
	private double allTotalMoney;
	private String orderSplit;//拆单情况
	private String payAmount;//paypal支付总金额
	private String balancePay;//余额支付
	private Integer balancePayFlag;//余额支付
	private String payAll;//总付款
	
	private int orderSum;//订单数
	
	private int count;//统计
	
	private int isMainOrder;//是否合并订单 1-是 0-否
	
	
	public int getIsMainOrder() {
		return isMainOrder;
	}
	public void setIsMainOrder(int isMainOrder) {
		this.isMainOrder = isMainOrder;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(int orderSum) {
		this.orderSum = orderSum;
	}
	public String getPayAll() {
		return payAll;
	}
	public void setPayAll(String payAll) {
		this.payAll = payAll;
	}
	public Integer getBalancePayFlag() {
		return balancePayFlag;
	}
	public void setBalancePayFlag(Integer balancePayFlag) {
		this.balancePayFlag = balancePayFlag;
	}
	public String getBalancePay() {
		return balancePay;
	}
	public void setBalancePay(String balancePay) {
		this.balancePay = balancePay;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getOrderSplit() {
		return orderSplit;
	}
	public void setOrderSplit(String orderSplit) {
		this.orderSplit = orderSplit;
	}
	public double getAllTotalMoney() {
		return allTotalMoney;
	}
	public void setAllTotalMoney(double allTotalMoney) {
		this.allTotalMoney = allTotalMoney;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public double getTotal_money() {
		return total_money;
	}
	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
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
	public String getPayment_other() {
		return payment_other;
	}
	public void setPayment_other(String payment_other) {
		this.payment_other = payment_other;
	}
	public String getPayment_amount() {
		return payment_amount;
	}
	public void setPayment_amount(String payment_amount) {
		this.payment_amount = payment_amount;
	}
	public String getPayment_cc() {
		return payment_cc;
	}
	public void setPayment_cc(String payment_cc) {
		this.payment_cc = payment_cc;
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
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
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

}
