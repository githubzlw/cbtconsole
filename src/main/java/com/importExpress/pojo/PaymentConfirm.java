package com.importExpress.pojo;

import java.io.Serializable;

public class PaymentConfirm implements Serializable {
		
	private static final long serialVersionUID = 8915258901511502223L;
	
	private int id;
	private String orderno;
	private String confirmname;
	private String confirmtime;
	private String paytype;
	private String paymentid;
	
	private String orderno_new;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getConfirmname() {
		return confirmname;
	}
	public void setConfirmname(String confirmname) {
		this.confirmname = confirmname;
	}
	public String getConfirmtime() {
		return confirmtime;
	}
	public void setConfirmtime(String confirmtime) {
		this.confirmtime = confirmtime;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getOrderno_new() {
		return orderno_new;
	}
	public void setOrderno_new(String orderno_new) {
		this.orderno_new = orderno_new;
	}
	
	
}
