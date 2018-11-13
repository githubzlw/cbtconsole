package com.cbt.website.bean;

public class GoodsArrivalInfo {
	private String orderid;//商品对应的订单id
	private int goodsid;//商品id
	private String expresstrackid;//商品对应的快递跟踪号
	private String warehouseid;//商品对应的库位号
	private int goodsarrivecount;//商品的到货数量
	private int goodscount;//商品的实际数量
	private int arrivalstatus;//商品的到货状态
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getExpresstrackid() {
		return expresstrackid;
	}
	public void setExpresstrackid(String expresstrackid) {
		this.expresstrackid = expresstrackid;
	}
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	public int getGoodsarrivecount() {
		return goodsarrivecount;
	}
	public void setGoodsarrivecount(int goodsarrivecount) {
		this.goodsarrivecount = goodsarrivecount;
	}
	public int getGoodscount() {
		return goodscount;
	}
	public void setGoodscount(int goodscount) {
		this.goodscount = goodscount;
	}
	public int getArrivalstatus() {
		return arrivalstatus;
	}
	public void setArrivalstatus(int arrivalstatus) {
		this.arrivalstatus = arrivalstatus;
	}
	
	
}
