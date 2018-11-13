package com.importExpress.pojo;

import java.io.Serializable;

public class OrderBuy implements Serializable {
	
	private static final long serialVersionUID = 240390245698021841L;
	
	private String orderid;
	private String buyUser;
	private  int buyId;
	
	private String orderno_new;
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getBuyUser() {
		return buyUser;
	}
	public void setBuyUser(String buyUser) {
		this.buyUser = buyUser;
	}
	public int getBuyId() {
		return buyId;
	}
	public void setBuyId(int buyId) {
		this.buyId = buyId;
	}
	
	public String getOrderno_new() {
		return orderno_new;
	}
	public void setOrderno_new(String orderno_new) {
		this.orderno_new = orderno_new;
	}
		

}
