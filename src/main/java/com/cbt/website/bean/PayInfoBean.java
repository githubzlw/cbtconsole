package com.cbt.website.bean;

public class PayInfoBean {
	private String userEmail;
	private String userId;
	private String paymentAll;//支付总金额（paypal + wiretransfer+余额）
	private String orderPriceAll;//订单支出总金额 paypal + wiretransfer
	private String orderPayPrice;//订单付款总金额
	private String currencyBalance;//当前余额
	private String appRefund;//申请退款金额
	private String appPaypal;//paypal申请退款金额
	private String refund;//已退款金额
	private String balance;//应有金额
	private String  total;
	private int orderNum;//订单数量
	private String paypal;//paypal支付金额
	private String wireTransfer;//tt支付金额
	
	
	public String getPaypal() {
		return paypal;
	}
	public void setPaypal(String paypal) {
		this.paypal = paypal;
	}
	public String getWireTransfer() {
		return wireTransfer;
	}
	public void setWireTransfer(String wireTransfer) {
		this.wireTransfer = wireTransfer;
	}
	public String getPaymentAll() {
		return paymentAll;
	}
	public void setPaymentAll(String paymentAll) {
		this.paymentAll = paymentAll;
	}
	private String paypalAmount;//paypal申诉金额
	
	private String addBalance;//余额奖励
	
	
	private int pageTotal;
	private int pageCurrent;
	
	
	
	public String getAppPaypal() {
		return appPaypal;
	}
	public void setAppPaypal(String appPaypal) {
		this.appPaypal = appPaypal;
	}
	public String getPaypalAmount() {
		return paypalAmount;
	}
	public void setPaypalAmount(String paypalAmount) {
		this.paypalAmount = paypalAmount;
	}
	public String getAddBalance() {
		return addBalance;
	}
	public void setAddBalance(String addBalance) {
		this.addBalance = addBalance;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public int getPageCurrent() {
		return pageCurrent;
	}
	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderPriceAll() {
		return orderPriceAll;
	}
	public void setOrderPriceAll(String orderPriceAll) {
		this.orderPriceAll = orderPriceAll;
	}
	public String getOrderPayPrice() {
		return orderPayPrice;
	}
	public void setOrderPayPrice(String orderPayPrice) {
		this.orderPayPrice = orderPayPrice;
	}
	public String getCurrencyBalance() {
		return currencyBalance;
	}
	public void setCurrencyBalance(String currencyBalance) {
		this.currencyBalance = currencyBalance;
	}
	public String getAppRefund() {
		return appRefund;
	}
	public void setAppRefund(String appRefund) {
		this.appRefund = appRefund;
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
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return String
				.format("{\"userEmail\"=\"%s\", \"userId\"=\"%s\", \"orderPriceAll\"=\"%s\", "
						+ "\"orderPayPrice\"=\"%s\", \"currencyBalance\"=\"%s\", "
						+ "\"appRefund\"=\"%s\", \"refund\"=\"%s\", \"balance\"=\"%s\", "
						+ "\"total\"=\"%s\", \"orderNum\"=\"%s\", \"paypalAmount\"=\"%s\", "
						+ "\"addBalance\"=\"%s\", \"pageTotal\"=\"%s\", \"pageCurrent\"=\"%s\"}",
						userEmail, userId, orderPriceAll, orderPayPrice,
						currencyBalance, appRefund, refund, balance, total,
						orderNum, paypalAmount, addBalance, pageTotal,
						pageCurrent);
	}
	
	

}
