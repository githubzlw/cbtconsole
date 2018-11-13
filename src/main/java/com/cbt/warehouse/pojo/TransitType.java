package com.cbt.warehouse.pojo;

/**
 * 查询类别是否带电(用来计算运费)               
 * 		 
 */
public class TransitType {

	private int id;
	private String aliExpress_type;//AliExpress 小类别
	private String battery;//是否带电池
	private int isvolumeweight;//是否抛货,1抛货，0不是
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setIsvolumeweight(int isvolumeweight) {
		this.isvolumeweight = isvolumeweight;
	}
	public int getIsvolumeweight() {
		return isvolumeweight;
	}
	
}
