package com.cbt.report.vo;

import java.sql.Timestamp;

/**
 * PayPal退款统计
 * 
 * @author JXW
 *
 */
public class RefundInfoBean {
	private String year;// 年份
	private String month;// 月份
	private String orderNo;// 订单号
	private Timestamp payTime;// 订单支付时间
	private float payAmount;// 订单实际支付金额
	private int userId;// 客户id
	private Timestamp refundTime;// 退款时间
	private float refundAmount;// 退款金额

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Timestamp getPayTime() {
		return payTime;
	}

	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	public float getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(float payAmount) {
		this.payAmount = payAmount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Timestamp refundTime) {
		this.refundTime = refundTime;
	}

	public float getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(float refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Override
	public String toString() {
		return "RefundInfoBean [year=" + year + ", month=" + month + ", orderNo=" + orderNo + ", payTime=" + payTime
				+ ", payAmount=" + payAmount + ", userId=" + userId + ", refundTime=" + refundTime + ", refundAmount="
				+ refundAmount + "]";
	}

}
