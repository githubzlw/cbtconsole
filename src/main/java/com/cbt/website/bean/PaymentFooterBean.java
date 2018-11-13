package com.cbt.website.bean;

public class PaymentFooterBean {

	private String orderNo;// 订单号
	private String paymentTime;// 支付时间
	private String currency;// 货币单位
	private int isShow = 0;// 是否在页面显示订单号标识

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	@Override
	public String toString() {
		return "PaymentFooterBean [orderNo=" + orderNo + ", paymentTime=" + paymentTime + ", currency=" + currency
				+ ", isShow=" + isShow + "]";
	}

}
