package com.cbt.bean;

import java.math.BigDecimal;

public class TabTransitFreightinfoUniteNew {
	private Integer id;

	private String transportMode;

	private String shippingTime;

	private Integer countryid;

	private Double normalBaseWeight;

	private BigDecimal normalBasePrice;

	private BigDecimal normalRatioPrice;

	private BigDecimal normalBigWeightPrice;

	private Double specialBaseWeight;
	private BigDecimal specialBasePrice;


	private BigDecimal specialRatioPrice;

	private BigDecimal specialBigWeightPrice;

	private Integer del;

	private Integer split;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode == null ? null : transportMode.trim();
	}

	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime == null ? null : shippingTime.trim();
	}

	public Integer getCountryid() {
		return countryid;
	}

	public void setCountryid(Integer countryid) {
		this.countryid = countryid;
	}

	public Double getNormalBaseWeight() {
		return normalBaseWeight;
	}

	public void setNormalBaseWeight(Double normalBaseWeight) {
		this.normalBaseWeight = normalBaseWeight;
	}

	public BigDecimal getNormalBasePrice() {
		return normalBasePrice;
	}

	public void setNormalBasePrice(BigDecimal normalBasePrice) {
		this.normalBasePrice = normalBasePrice;
	}

	public BigDecimal getNormalRatioPrice() {
		return normalRatioPrice;
	}

	public void setNormalRatioPrice(BigDecimal normalRatioPrice) {
		this.normalRatioPrice = normalRatioPrice;
	}

	public BigDecimal getNormalBigWeightPrice() {
		return normalBigWeightPrice;
	}

	public void setNormalBigWeightPrice(BigDecimal normalBigWeightPrice) {
		this.normalBigWeightPrice = normalBigWeightPrice;
	}

	public Double getSpecialBaseWeight() {
		return specialBaseWeight;
	}

	public void setSpecialBaseWeight(Double specialBaseWeight) {
		this.specialBaseWeight = specialBaseWeight;
	}

	public BigDecimal getSpecialBasePrice() {
		return specialBasePrice;
	}

	public void setSpecialBasePrice(BigDecimal specialBasePrice) {
		this.specialBasePrice = specialBasePrice;
	}

	public BigDecimal getSpecialRatioPrice() {
		return specialRatioPrice;
	}

	public void setSpecialRatioPrice(BigDecimal specialRatioPrice) {
		this.specialRatioPrice = specialRatioPrice;
	}

	public BigDecimal getSpecialBigWeightPrice() {
		return specialBigWeightPrice;
	}

	public void setSpecialBigWeightPrice(BigDecimal specialBigWeightPrice) {
		this.specialBigWeightPrice = specialBigWeightPrice;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public Integer getSplit() {
		return split;
	}

	public void setSplit(Integer split) {
		this.split = split;
	}
}
