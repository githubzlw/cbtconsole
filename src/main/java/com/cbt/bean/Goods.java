package com.cbt.bean;

public class Goods {
	private int id;
	private String goodName;
	private String keywords;
	private int moneyType;
	private double price;
	private String address;
	public Goods(){}
	public Goods(int id,String goodName,String keywords,int moneyType,double price,String address){
		this.id=id;
		this.goodName=goodName;
		this.keywords=keywords;
		this.moneyType=moneyType;
		this.price=price;
		this.address=address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
