package com.cbt.bean;

import java.util.Date;

public class Evaluate {

	private int id;
	private int userid;
	private String orderNo;
	private int service;
	private int products;
	private String evaluate;
	private Date createtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getService() {
		return service;
	}
	public void setService(int service) {
		this.service = service;
	}
	public int getProducts() {
		return products;
	}
	public void setProducts(int products) {
		this.products = products;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	
}
