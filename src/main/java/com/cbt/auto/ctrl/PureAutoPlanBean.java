package com.cbt.auto.ctrl;

import java.sql.Timestamp;

public class PureAutoPlanBean {
	
    private int id;
	
	private int autoState;
	
	private Timestamp autitime;
	
	private String orderid;
	
	private String orderno;
	
	private int paystatus;
	
	private Timestamp paytime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAutoState() {
		return autoState;
	}

	public void setAutoState(int autoState) {
		this.autoState = autoState;
	}

	public Timestamp getAutitime() {
		return autitime;
	}

	public void setAutitime(Timestamp autitime) {
		this.autitime = autitime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public int getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}

	public Timestamp getPaytime() {
		return paytime;
	}

	public void setPaytime(Timestamp paytime) {
		this.paytime = paytime;
	}


}
