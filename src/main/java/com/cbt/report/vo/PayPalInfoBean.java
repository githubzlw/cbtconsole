package com.cbt.report.vo;

import java.sql.Timestamp;

/**
 * PayPal总收入详情
 * 
 * @author JXW
 *
 */
public class PayPalInfoBean {

	private String year;
	private String month;
	private String orderid;// 订单号
	private int userId;// 客户id
	private float paymentAmount;// 付款金额
	private String paymentCc;// 付款币种
	private String payFlag;// 支付标识 O是订单支付，Y是运费支付，N修改商品价格后的订单支付金额
	private String payType;// 支付类别 0是paypal支付，1是WireTransfer 支付 2 余额支付 3订单拆分
							// 4合并支付 5stripe支付
	private Timestamp createTime;// 创建时间
	private String payNo;//支付交易号

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

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentCc() {
		return paymentCc;
	}

	public void setPaymentCc(String paymentCc) {
		this.paymentCc = paymentCc;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	@Override
	public String toString() {
		return "PayPalInfoBean{" +
				"year='" + year + '\'' +
				", month='" + month + '\'' +
				", orderid='" + orderid + '\'' +
				", userId=" + userId +
				", paymentAmount=" + paymentAmount +
				", paymentCc='" + paymentCc + '\'' +
				", payFlag='" + payFlag + '\'' +
				", payType='" + payType + '\'' +
				", createTime=" + createTime +
				", payNo='" + payNo + '\'' +
				'}';
	}
}
