package com.cbt.bean;

/**
 * 查询类别是否带电(用来计算运费)               
 * 		 
 */
public class TransitType {

	private int id;
	private String transit_type;//运输类别
	private String aliExpress_type;//AliExpress 小类别
	private String battery;//是否带电池
	private String transit_type_name;// 运输类别显示名称
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTransit_type() {
		return transit_type;
	}
	public void setTransit_type(String transit_type) {
		this.transit_type = transit_type;
	}
	public String getAliExpress_type() {
		return aliExpress_type;
	}
	public void setAliExpress_type(String aliExpress_type) {
		this.aliExpress_type = aliExpress_type;
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	public String getTransit_type_name() {
		return transit_type_name;
	}
	public void setTransit_type_name(String transit_type_name) {
		this.transit_type_name = transit_type_name;
	}
	
	
}
