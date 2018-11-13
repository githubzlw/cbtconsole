package com.cbt.pojo;

public class ReportDetailOrder {
	private Integer id;
	private String orderno;
	private Double purchasePrice;
	private Double salesPrice;
	private Double averagePrice;
	private Integer salesVolumes;
	private Integer  genralReportId ;
	private Integer buycount;
	private Double buyAveragePrice;
	private Double profitLoss;
	private Double freight;//实收运费
	private Double od_weight;//预估重量
	private Double fee_weight;//录入重量
	private Double fw_weight;//实收重量
	private Double inputFreight; //录入运费
	private String typeship; //出货类型（合并出货、正常出货）
	
	private int datacount;
	
	private String  expressno ; //运单号
	private String  remarks ;   //合并订单号
	
	public int getDatacount() {
		return datacount;
	}
	public void setDatacount(int datacount) {
		this.datacount = datacount;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public Double getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(Double averagePrice) {
		this.averagePrice = averagePrice;
	}
	public Integer getSalesVolumes() {
		return salesVolumes;
	}
	public void setSalesVolumes(Integer salesVolumes) {
		this.salesVolumes = salesVolumes;
	}
	public Integer getGenralReportId() {
		return genralReportId;
	}
	public void setGenralReportId(Integer genralReportId) {
		this.genralReportId = genralReportId;
	}
	public Integer getBuycount() {
		return buycount;
	}
	public void setBuycount(Integer buycount) {
		this.buycount = buycount;
	}
	public Double getBuyAveragePrice() {
		return buyAveragePrice;
	}
	public void setBuyAveragePrice(Double buyAveragePrice) {
		this.buyAveragePrice = buyAveragePrice;
	}
	public Double getProfitLoss() {
		return profitLoss;
	}
	public void setProfitLoss(Double profitLoss) {
		this.profitLoss = profitLoss;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Double getOd_weight() {
		return od_weight;
	}
	public void setOd_weight(Double od_weight) {
		this.od_weight = od_weight;
	}
	public Double getFee_weight() {
		return fee_weight;
	}
	public void setFee_weight(Double fee_weight) {
		this.fee_weight = fee_weight;
	}
	public Double getFw_weight() {
		return fw_weight;
	}
	public void setFw_weight(Double fw_weight) {
		this.fw_weight = fw_weight;
	}
	public Double getInputFreight() {
		return inputFreight;
	}
	public void setInputFreight(Double inputFreight) {
		this.inputFreight = inputFreight;
	}
	public String getTypeship() {
		return typeship;
	}
	public void setTypeship(String typeship) {
		this.typeship = typeship;
	}
	public String getExpressno() {
		return expressno;
	}
	public void setExpressno(String expressno) {
		this.expressno = expressno;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
}
