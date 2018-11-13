package com.cbt.bean;

public class OrderPaymentBean {

	private String orderNo;//订单号
	
	private String dateTime;//日期
	
	private String paymentAmount;//订单总金额(paypal+wiretransfer+余额)
	
	private String  payPrice;//订单金额
	
	private String  payment;//实际进账金额(paypal+wiretransfer)
	
	private String  additionalBalance;//余额补偿
	
	private String  cancelPrice;//订单取消金额
	
	private String cancelOrder;//订单取消商品金额
	
	private String refund;//paypal退款金额
	
	private String  balance;//余额
	
	private String userid;//用户id
	
	private int countTotal;
	
	private int isBalance;//I1实际下单总金额-R总的退款金额 =O总的实际完成订单金额
	

	
	public String getCancelOrder() {
		return cancelOrder;
	}

	public void setCancelOrder(String cancelOrder) {
		this.cancelOrder = cancelOrder;
	}

	public int getIsBalance() {
		return isBalance;
	}

	public void setIsBalance(int isBalance) {
		this.isBalance = isBalance;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getAdditionalBalance() {
		return additionalBalance;
	}

	public void setAdditionalBalance(String additionalBalance) {
		this.additionalBalance = additionalBalance;
	}

	public String getCancelPrice() {
		return cancelPrice;
	}

	public void setCancelPrice(String cancelPrice) {
		this.cancelPrice = cancelPrice;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
