package com.cbt.bean;

import java.util.Date;

public class Tb1688OrderHistory {
	private int  id;
	private int  tbOr1688;   //0： 淘宝  1：1688
	private String orderid;  
	private Date orderdate;
	private String seller;
	private String totalprice;
	private String orderstatus;
	private String itemname;
	private String itemid;
	private double itemprice;
	private String itemqty;
	private String sku;
	private String shipno;
	private String shipper;
	private String shipstatus;
	private String itemurl;
	private String imgurl;
	private String username;
	private Date creatTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTbOr1688() {
		return tbOr1688;
	}
	public void setTbOr1688(int tbOr1688) {
		this.tbOr1688 = tbOr1688;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public double getItemprice() {
		return itemprice;
	}
	public void setItemprice(double itemprice) {
		this.itemprice = itemprice;
	}
	public String getItemqty() {
		return itemqty;
	}
	public void setItemqty(String itemqty) {
		this.itemqty = itemqty;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getShipstatus() {
		return shipstatus;
	}
	public void setShipstatus(String shipstatus) {
		this.shipstatus = shipstatus;
	}
	public String getItemurl() {
		return itemurl;
	}
	public void setItemurl(String itemurl) {
		this.itemurl = itemurl;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

}
