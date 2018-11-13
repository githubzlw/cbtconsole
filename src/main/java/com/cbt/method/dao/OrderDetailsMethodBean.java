package com.cbt.method.dao;

public class OrderDetailsMethodBean {
	private int odid;
	private String carType;//规格
	private String goodsPrice;//价格
	private String carImg;//图片
	private int yourOrder;//数量
	private String orderid;
	private String goodscatid;
	private String goodsid;
	private String seilUnit;
	public String getSeilUnit() {
		return seilUnit;
	}
	public void setSeilUnit(String seilUnit) {
		this.seilUnit = seilUnit;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getGoodscatid() {
		return goodscatid;
	}
	public void setGoodscatid(String goodscatid) {
		this.goodscatid = goodscatid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getOdid() {
		return odid;
	}
	public void setOdid(int odid) {
		this.odid = odid;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getCarImg() {
		return carImg;
	}
	public void setCarImg(String carImg) {
		this.carImg = carImg;
	}
	public int getYourOrder() {
		return yourOrder;
	}
	public void setYourOrder(int yourOrder) {
		this.yourOrder = yourOrder;
	}

}
