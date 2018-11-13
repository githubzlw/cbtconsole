package com.cbt.bean;

public class StateZip {
	private String state;//名称
	private String zipCode;//邮编
	private float volumeFreight;//体积海运费  美元/立方米
	private float weightFreight;//重量海运费  美元/吨
	private float deliveryTime;//运费
	public StateZip(){}
	public StateZip(String state,String zipCode){
		this.state=state;
		this.zipCode=zipCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public float getVolumeFreight() {
		return volumeFreight;
	}
	public void setVolumeFreight(float volumeFreight) {
		this.volumeFreight = volumeFreight;
	}
	public float getWeightFreight() {
		return weightFreight;
	}
	public void setWeightFreight(float weightFreight) {
		this.weightFreight = weightFreight;
	}
	public float getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(float deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
}
