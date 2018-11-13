package com.cbt.warehouse.pojo;
/**
 * 每个国家的 建议单包最大重量 和 单包免查报关金额(用来计算运费)               
 * 		 
 */
public class TransitCheckfree {

	private int id;
	private int countryid;
	private String countryname;
	private double price;//最大免查金额
	private double weight;//最大免查重量
	private double tariff;//最大免关税 货值
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCountryid() {
		return countryid;
	}
	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getTariff() {
		return tariff;
	}
	public void setTariff(double tariff) {
		this.tariff = tariff;
	}
	
	
}
