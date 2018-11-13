package com.cbt.pojo;

public class OrderSalesAmountPojo {
	private String id;

	private String orderAmount;// 订单总金额之和

	private String grabAmount;// 采购价之和

	private String forecastAmount;// 预估运费

	private String forecastProfits;// 预估利润
	
	private String times;//日期
	
	private String forecastProfitsMargin;//预估利润率
	//用户月利润统计
	private String email;
	private String grade;
	private String orderCount;
	private String salesAmount;
	private String buyAmount;
	private String freight;
	private String Profits;//利润率

	private String esprofits;//预估利润率
	private double pid_amount;
	private String estimateFreight;//预估运费
	private String estimateProfit;//用户利润预估
	private String ac_weight;//用户月订单中实际重量
	private String es_weight;//用户月订单中预估重量
	private String custom_freight;//客户付的运费
	private String orderids;
	private String shipnos;
	private int flag;
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}


	public String getGetAmount() {
		return getAmount;
	}

	public void setGetAmount(String getAmount) {
		this.getAmount = getAmount;
	}

	private String getAmount;
	public String getEsBuyPrice() {
		return esBuyPrice;
	}

	public void setEsBuyPrice(String esBuyPrice) {
		this.esBuyPrice = esBuyPrice;
	}

	private String esBuyPrice;
	public String getEndProfits() {
		return endProfits;
	}

	public void setEndProfits(String endProfits) {
		this.endProfits = endProfits;
	}

	private String endProfits;
	public String getTotalPriceFreight() {
		return totalPriceFreight;
	}

	public void setTotalPriceFreight(String totalPriceFreight) {
		this.totalPriceFreight = totalPriceFreight;
	}

	private String totalPriceFreight;

	public String getShipnos() {
		return shipnos;
	}

	public void setShipnos(String shipnos) {
		this.shipnos = shipnos;
	}

	public String getOrderids() {
		return orderids;
	}

	public void setOrderids(String orderids) {
		this.orderids = orderids;
	}


	public String getEsFreight() {
		return esFreight;
	}

	public void setEsFreight(String esFreight) {
		this.esFreight = esFreight;
	}

	private String esFreight;
	public String getCustom_freight() {
		return custom_freight;
	}

	public void setCustom_freight(String custom_freight) {
		this.custom_freight = custom_freight;
	}

	public String getAc_weight() {
		return ac_weight;
	}

	public void setAc_weight(String ac_weight) {
		this.ac_weight = ac_weight;
	}

	public String getEs_weight() {
		return es_weight;
	}

	public void setEs_weight(String es_weight) {
		this.es_weight = es_weight;
	}
	public String getEstimateFreight() {
		return estimateFreight;
	}
	public String getEsprofits() {
		return esprofits;
	}

	public void setEsprofits(String esprofits) {
		this.esprofits = esprofits;
	}
	public void setEstimateFreight(String estimateFreight) {
		this.estimateFreight = estimateFreight;
	}

	public String getEstimateProfit() {
		return estimateProfit;
	}

	public void setEstimateProfit(String estimateProfit) {
		this.estimateProfit = estimateProfit;
	}

	public double getPid_amount() {
		return pid_amount;
	}

	public void setPid_amount(double pid_amount) {
		this.pid_amount = pid_amount;
	}

	public String getProfits() {
		return Profits;
	}

	public void setProfits(String profits) {
		Profits = profits;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(String buyAmount) {
		this.buyAmount = buyAmount;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}


	public String getForecastProfitsMargin() {
		return forecastProfitsMargin;
	}

	public void setForecastProfitsMargin(String forecastProfitsMargin) {
		this.forecastProfitsMargin = forecastProfitsMargin;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getForecastAmount() {
		return forecastAmount;
	}

	public void setForecastAmount(String forecastAmount) {
		this.forecastAmount = forecastAmount;
	}

	public String getForecastProfits() {
		return forecastProfits;
	}

	public void setForecastProfits(String forecastProfits) {
		this.forecastProfits = forecastProfits;
	}

}
