package com.cbt.website.bean;

import java.util.List;

public class outIdBean {

	private int id;//客户id
	private String email;//客户Email
	private List<PurchaseBean> purchaseBean;//客户订单
	
	public outIdBean() {
		super();
	}
	
	public outIdBean(int id, String email, List<PurchaseBean> purchaseBean) {
		super();
		this.id = id;
		this.email = email;
		this.purchaseBean = purchaseBean;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<PurchaseBean> getPurchaseBean() {
		return purchaseBean;
	}
	public void setPurchaseBean(List<PurchaseBean> purchaseBean) {
		this.purchaseBean = purchaseBean;
	}
	
}
