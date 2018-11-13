package com.cbt.website.bean;

import java.util.List;

public class PurchaseBean {

	private int userid;
	private String name;
	private int orderid;
	private String orderNo;
	private String orderaddress;//订单地址
	private String deliveryTime;//交期天数
	private String ordertime;//下单时间
	private String paytime;//付款时间
	private int purchase_number;//已采购数量
	private int details_number;//需采购数量
	private List<PurchaseDetailsBean> purchaseDetailsBean;//采购详情
	private String ofState;   //order_fee 状态
	
	private String orderarr;//合并了几个订单
	
	
	
	
	
	public String getOrderarr() {
		return orderarr;
	}

	public void setOrderarr(String orderarr) {
		this.orderarr = orderarr;
	}

	public String getOfState() {
		return ofState;
	}

	public void setOfState(String ofState) {
		this.ofState = ofState;
	}

	public PurchaseBean() {
		super();
	}
	
	public PurchaseBean(int userid, String name, String orderNo, String deliveryTime,String ordertime,
			int orderid,String paytime, int purchase_number,int details_number,String orderaddress) {
		super();
		this.userid = userid;
		this.name = name;
		this.orderid = orderid;
		this.orderNo = orderNo;
		this.deliveryTime = deliveryTime;
		this.ordertime = ordertime;
		this.paytime = paytime;
		this.purchase_number = purchase_number;
		this.details_number = details_number;
		this.orderaddress = orderaddress;
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public int getPurchase_number() {
		return purchase_number;
	}
	public void setPurchase_number(int purchase_number) {
		this.purchase_number = purchase_number;
	}
	public int getDetails_number() {
		return details_number;
	}
	public void setDetails_number(int details_number) {
		this.details_number = details_number;
	}
	public String getOrderaddress() {
		return orderaddress;
	}
	public void setOrderaddress(String orderaddress) {
		this.orderaddress = orderaddress;
	}
	public List<PurchaseDetailsBean> getPurchaseDetailsBean() {
		return purchaseDetailsBean;
	}
	public void setPurchaseDetailsBean(List<PurchaseDetailsBean> purchaseDetailsBean) {
		this.purchaseDetailsBean = purchaseDetailsBean;
	}
}
