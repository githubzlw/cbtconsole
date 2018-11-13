package com.cbt.warehouse.pojo;

/**
 * @author ylm
 * 运费关于国家对应的参数
 */
public class CountryEpacketjcexBean {

	private int id;
	private int countryid;
	private int exist_epacket;//该国家是否存在E邮宝，0-存在，1-不存在
	private double jcex_ratio;//快递运费=epacket*该系数
	private double jcex_add;//快递运费=epacket*该系数+改字段
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
	public int getExist_epacket() {
		return exist_epacket;
	}
	public void setExist_epacket(int exist_epacket) {
		this.exist_epacket = exist_epacket;
	}
	public double getJcex_ratio() {
		return jcex_ratio;
	}
	public void setJcex_ratio(double jcex_ratio) {
		this.jcex_ratio = jcex_ratio;
	}
	public double getJcex_add() {
		return jcex_add;
	}
	public void setJcex_add(double jcex_add) {
		this.jcex_add = jcex_add;
	}

	
}
