package com.cbt.bean;

public class TransitPricecost {

	private int id;
	private String shippingmethod;//运输方式名称
	private String shippingmethod_en;//
	private int countryId;//国家ID
	private String countrycname;//国家ID
	private String countryname;//国家ID
	private String delivery_time;//运输时间段
	private int days;//速度档次
	private double under;//首重价格
	private double over;//续重价格
	private double divisionweight;//整除重量KG
	private String battery;//有无电池
	private double minweight;//最小重量区间
	private double maxweight;//最大重量区间
	private double weight;//重量区间1-100
	private double freight;//运费
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShippingmethod() {
		return shippingmethod;
	}
	public void setShippingmethod(String shippingmethod) {
		this.shippingmethod = shippingmethod;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public double getUnder() {
		return under;
	}
	public void setUnder(double under) {
		this.under = under;
	}
	public double getOver() {
		return over;
	}
	public void setOver(double over) {
		this.over = over;
	}
	public double getDivisionweight() {
		return divisionweight;
	}
	public void setDivisionweight(double divisionweight) {
		this.divisionweight = divisionweight;
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	public double getMinweight() {
		return minweight;
	}
	public void setMinweight(double minweight) {
		this.minweight = minweight;
	}
	public double getMaxweight() {
		return maxweight;
	}
	public void setMaxweight(double maxweight) {
		this.maxweight = maxweight;
	}
	public String getCountrycname() {
		return countrycname;
	}
	public void setCountrycname(String countrycname) {
		this.countrycname = countrycname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCountryname() {
		return countryname;
	}
	
	public void setShippingmethod_en(String shippingmethod_en) {
		this.shippingmethod_en = shippingmethod_en;
	}
	public String getShippingmethod_en() {
		return shippingmethod_en;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public double getFreight() {
		return freight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getWeight() {
		return weight;
	}
}
