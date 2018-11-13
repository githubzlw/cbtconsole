package com.cbt.bean;

public class DiscountBean {
	private String showName;//展示类名
	private String className;//类名
	private int price;//价格
	private double depositRate;//折扣率
	private int classType;//类型
	private String catid;//类别id
	private int id;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public double getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(double depositRate) {
		this.depositRate = depositRate;
	}
	public int getClassType() {
		return classType;
	}
	public void setClassType(int classType) {
		this.classType = classType;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	

}
