package com.cbt.pojo;

import java.io.Serializable;

/**
 * 月统计采购对账实体类
 * 
 * @author 王宏杰 2017-08-16
 *
 */
public class BuyReconciliationPojo implements Serializable{

	private static final long serialVersionUID = 4365457054419851012L;
	private String times; // 统计时间

	private String grabAmount;// 支付宝抓取采购金额

	private String normalAmount;// 对应正常采购订单采购金额

	private String cancelAmount;// 对应取消订单采购金额

	private String noMatchingOrder;// 没有匹配到销售订单的采购金额

	private String noStorage;// 未入库采购订单金额

	private String forecastAmount;// 系统预估运费金额

	private String actualAmount;// 物流公司实际运费金额

	private String sale_inventory;// 本月销售出去的库存

	private String lastMonth;// 对应上月订单采购金额

	private String inventory_amount;// 库存金额
	
	private String beginBlance;//月初支付宝余额
	
	private String endBlance;//月末支付宝余额
	
	private String transfer;//银行转账支付宝金额
	
	private String payFreight;//实际支付的运费
	
	private String grabAmounts;//根据公司计算出来的D1=月初支付宝余额+银行转账支付宝金额-月末支付宝余额
	private String grabAmountsCopy;
	
	private String zfbPayAmount;//支付宝付款（D2）=对应正常采购订单采购金额+对应取消订单采购金额+没有匹配到销售订单的采购金额+实际支付的运费
	private String zfbPayAmountCopy;

	private String ebayAmount;//亚马逊公司支付宝支出金额
	
	private String materialsAmount;//电商物料
	
	private String zfbFright;//支付宝支出运费
	private String zfbFrightCopy;
	private String profit;//月度利润

	private String order_sales;//销售额
	private String balance_compensation;//余额补偿
	private String orderFreight;//当月订单运费汇总金额
	public String getOrderFreight() {
		return orderFreight;
	}

	public String getGrabAmountsCopy() {
		return grabAmountsCopy;
	}

	public String getZfbFrightCopy() {
		return zfbFrightCopy;
	}

	public void setZfbFrightCopy(String zfbFrightCopy) {
		this.zfbFrightCopy = zfbFrightCopy;
	}

	public void setGrabAmountsCopy(String grabAmountsCopy) {
		this.grabAmountsCopy = grabAmountsCopy;
	}

	public String getZfbPayAmountCopy() {
		return zfbPayAmountCopy;
	}

	public void setZfbPayAmountCopy(String zfbPayAmountCopy) {
		this.zfbPayAmountCopy = zfbPayAmountCopy;
	}

	public void setOrderFreight(String orderFreight) {
		this.orderFreight = orderFreight;
	}

	public String getOrder_sales() {
		return order_sales;
	}

	public void setOrder_sales(String order_sales) {
		this.order_sales = order_sales;
	}

	public String getBalance_compensation() {
		return balance_compensation;
	}

	public void setBalance_compensation(String balance_compensation) {
		this.balance_compensation = balance_compensation;
	}
	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getZfbFright() {
		return zfbFright;
	}

	public void setZfbFright(String zfbFright) {
		this.zfbFright = zfbFright;
	}

	public String getEbayAmount() {
		return ebayAmount;
	}

	public void setEbayAmount(String ebayAmount) {
		this.ebayAmount = ebayAmount;
	}

	public String getMaterialsAmount() {
		return materialsAmount;
	}

	public void setMaterialsAmount(String materialsAmount) {
		this.materialsAmount = materialsAmount;
	}

	
	public String getPayFreight() {
		return payFreight;
	}

	public void setPayFreight(String payFreight) {
		this.payFreight = payFreight;
	}

	public String getZfbPayAmount() {
		return zfbPayAmount;
	}

	public void setZfbPayAmount(String zfbPayAmount) {
		this.zfbPayAmount = zfbPayAmount;
		this.zfbPayAmountCopy = zfbPayAmount;
	}

	public String getGrabAmounts() {
		return grabAmounts;
	}

	public void setGrabAmounts(String grabAmounts) {
		this.grabAmounts = grabAmounts;
	}

	public String getBeginBlance() {
		return beginBlance;
	}

	public void setBeginBlance(String beginBlance) {
		this.beginBlance = beginBlance;
	}

	public String getEndBlance() {
		return endBlance;
	}

	public void setEndBlance(String endBlance) {
		this.endBlance = endBlance;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}


	public String getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}

	public String getInventory_amount() {
		return inventory_amount;
	}

	public void setInventory_amount(String inventory_amount) {
		this.inventory_amount = inventory_amount;
	}

	public String getSale_inventory() {
		return sale_inventory;
	}

	public void setSale_inventory(String sale_inventory) {
		this.sale_inventory = sale_inventory;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getGrabAmount() {
		return grabAmount;
	}

	public void setGrabAmount(String grabAmount) {
		this.grabAmount = grabAmount;
	}

	public String getNormalAmount() {
		return normalAmount;
	}

	public void setNormalAmount(String normalAmount) {
		this.normalAmount = normalAmount;
	}

	public String getCancelAmount() {
		return cancelAmount;
	}

	public void setCancelAmount(String cancelAmount) {
		this.cancelAmount = cancelAmount;
	}

	public String getNoMatchingOrder() {
		return noMatchingOrder;
	}

	public void setNoMatchingOrder(String noMatchingOrder) {
		this.noMatchingOrder = noMatchingOrder;
	}

	public String getNoStorage() {
		return noStorage;
	}

	public void setNoStorage(String noStorage) {
		this.noStorage = noStorage;
	}

	public String getForecastAmount() {
		return forecastAmount;
	}

	public void setForecastAmount(String forecastAmount) {
		this.forecastAmount = forecastAmount;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

}
