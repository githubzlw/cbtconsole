package com.cbt.bean;

import java.util.Date;

public class OrderBuyBean {
	private int id;
	private String orderid;
	private String buyuser;
	private int buyid;
	private Date time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getBuyuser() {
		return buyuser;
	}
	public void setBuyuser(String buyuser) {
		this.buyuser = buyuser;
	}
	public int getBuyid() {
		return buyid;
	}
	public void setBuyid(int buyid) {
		this.buyid = buyid;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
