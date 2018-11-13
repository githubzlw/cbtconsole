package com.cbt.report.vo;

import java.sql.Timestamp;

/**
 * 本月实际销售额详情和未启动订单总金额详情
 * 
 * @author JXW
 *
 */
public class OrderInfoBean {

	private String orderNo;// 订单号
	private String userId;// 客户id
	private float payPrice;// 实际支付金额
	private Timestamp createTime;// 订单创建时间
	private String state;// 订单状态
	private Timestamp orderPayTime;// 订单支付时间
	private String userEmail;//客户邮箱地址

	private double buyAmount;//订单录入采购金额
	private double frightAmount;//订单运费
	private String expressNo;//出运单号
	
	private double estimatefreight;//预估运费
	private String exchange_rate;
	private double acAmount;//订单实际的采购金额
	public double getAcAmount() {
		return acAmount;
	}

	public void setAcAmount(double acAmount) {
		this.acAmount = acAmount;
	}

	public double getEstimatefreight() {
		return estimatefreight;
	}

	public void setEstimatefreight(double estimatefreight) {
		this.estimatefreight = estimatefreight;
	}

	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public double getPid_amount() {
		return pid_amount;
	}

	public void setPid_amount(double pid_amount) {
		this.pid_amount = pid_amount;
	}

	private double pid_amount;
	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	private double profit;//订单利润
	public double getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(double buyAmount) {
		this.buyAmount = buyAmount;
	}

	public double getFrightAmount() {
		return frightAmount;
	}

	public void setFrightAmount(double frightAmount) {
		this.frightAmount = frightAmount;
	}
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public float getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(float payPrice) {
		this.payPrice = payPrice;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Timestamp getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Timestamp orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	@Override
	public String toString() {
		return "OrderInfoBean [orderNo=" + orderNo + ", userId=" + userId + ", payPrice=" + payPrice + ", createTime="
				+ createTime + ", state=" + state + "]";
	}

}
