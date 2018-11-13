package com.cbt.bean;

public class LocationTracking {
	
	private int id;
	private String barcode;
	private String short_term;
	private String admuser;
	private String createtime;
	private int acount;
	
	public int getAcount() {
		return acount;
	}
	public void setAcount(int acount) {
		this.acount = acount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getShort_term() {
		return short_term;
	}
	public void setShort_term(String short_term) {
		this.short_term = short_term;
	}
	public String getAdmuser() {
		return admuser;
	}
	public void setAdmuser(String admuser) {
		this.admuser = admuser;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

}
