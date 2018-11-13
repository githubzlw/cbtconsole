package com.cbt.website.bean;

/**
 * 支付统计
 * 
 * @author JXW
 *
 */
public class PaymentStatistics {

	private int userId;// 客户ID
	private int orderNum;// 订单数量
	private String orderNo;// 订单号
	private float paymentAmount;// 支付总额
	private String paymentTime;// 支付时间
	private int splitFlag;// 拆单标识 0非拆单订单 1:拆单订单
	private String currency;// 货币单位
	private int isShow = 1;// 是否在页面显示订单号标识
	private int orderState;//订单状态
	private String orderStateDesc;//订单状态描述
	private String payId;//交易号
	private String payType;//

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public int getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(int splitFlag) {
		this.splitFlag = splitFlag;
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

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
		if(orderState == -1){
			this.orderStateDesc = "后台取消";
		}else if(orderState == -1){
			this.orderStateDesc = "后台取消";
		}else if(orderState == 0){
			this.orderStateDesc = "未支付";
		}else if(orderState == 1){
			this.orderStateDesc = "采购中";
		}else if(orderState == 2){
			this.orderStateDesc = "入库";
		}else if(orderState == 3){
			this.orderStateDesc = "出运";
		}else if(orderState == 4){
			this.orderStateDesc = "完结";
		}else if(orderState == 5){
			this.orderStateDesc = "审核中";
		}else if(orderState == 6){
			this.orderStateDesc = "客户取消";
		}
	}

	public String getOrderStateDesc() {
		return orderStateDesc;
	}

	public void setOrderStateDesc(String orderStateDesc) {
		this.orderStateDesc = orderStateDesc;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
		if ("3".equals(payType) || "4".equals(payType)) {
			this.splitFlag = 1;
		}
	}

	@Override
	public String toString() {
		return "PaymentStatistics [userId=" + userId + ", orderNum=" + orderNum + ", orderNo=" + orderNo
				+ ", paymentAmount=" + paymentAmount + ", paymentTime=" + paymentTime + ", splitFlag=" + splitFlag
				+ ", currency=" + currency + ", isShow=" + isShow + "]";
	}

}
