package com.cbt.pojo;

import java.io.Serializable;

/**
 * 付款审批的相关财务统计实体类
 * 
 * @author 王宏杰 2017-08-23
 *
 */
public class OrderDetetailsSalePojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String times;// 日期

	private String totalSum;// 非取消订单总进账(TT+PayPal)

	private String titalRefund;// 总退款

	private String balancePayment;// 余额支付

	private String balancePrepaid;// 余额充值

	private String salesAmount;// 进账对应销售额

	private String cancelBeforeOrderAmount;// 取消的非当日订单对应的销售额
	//==============================
	private String order_no;//订单号
	
	private String originalAmount;//订单最初到账金额
	
	private String estimateBuyAmount;//预估采购成本
	
	private String estimateFright;//预估运费成本
	
	private String estimateProfits;//预估毛利润
	
	private String orderactualAmount;//订单实际金额
	
	private String orderActualBuyAmount;//实际采购成本
	
	private String orderActualFright;//实际运费成本
	
	private String orderActualProfits;//实际毛利润
	
	private String shipmentno;//出运号
	public String getShipmentno() {
		return shipmentno;
	}

	public void setShipmentno(String shipmentno) {
		this.shipmentno = shipmentno;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getEstimateBuyAmount() {
		return estimateBuyAmount;
	}

	public void setEstimateBuyAmount(String estimateBuyAmount) {
		this.estimateBuyAmount = estimateBuyAmount;
	}

	public String getEstimateFright() {
		return estimateFright;
	}

	public void setEstimateFright(String estimateFright) {
		this.estimateFright = estimateFright;
	}

	public String getEstimateProfits() {
		return estimateProfits;
	}

	public void setEstimateProfits(String estimateProfits) {
		this.estimateProfits = estimateProfits;
	}

	public String getOrderactualAmount() {
		return orderactualAmount;
	}

	public void setOrderactualAmount(String orderactualAmount) {
		this.orderactualAmount = orderactualAmount;
	}

	public String getOrderActualBuyAmount() {
		return orderActualBuyAmount;
	}

	public void setOrderActualBuyAmount(String orderActualBuyAmount) {
		this.orderActualBuyAmount = orderActualBuyAmount;
	}

	public String getOrderActualFright() {
		return orderActualFright;
	}

	public void setOrderActualFright(String orderActualFright) {
		this.orderActualFright = orderActualFright;
	}

	public String getOrderActualProfits() {
		return orderActualProfits;
	}

	public void setOrderActualProfits(String orderActualProfits) {
		this.orderActualProfits = orderActualProfits;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getTitalRefund() {
		return titalRefund;
	}

	public void setTitalRefund(String titalRefund) {
		this.titalRefund = titalRefund;
	}

	public String getBalancePayment() {
		return balancePayment;
	}

	public void setBalancePayment(String balancePayment) {
		this.balancePayment = balancePayment;
	}

	public String getBalancePrepaid() {
		return balancePrepaid;
	}

	public void setBalancePrepaid(String balancePrepaid) {
		this.balancePrepaid = balancePrepaid;
	}

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getCancelBeforeOrderAmount() {
		return cancelBeforeOrderAmount;
	}

	public void setCancelBeforeOrderAmount(String cancelBeforeOrderAmount) {
		this.cancelBeforeOrderAmount = cancelBeforeOrderAmount;
	}
}
