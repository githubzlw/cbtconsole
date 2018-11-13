package com.importExpress.pojo;

import java.io.Serializable;

public class OrderAddress implements Serializable {

	private static final long serialVersionUID = -1421514785832424336L;
	
	private int id;
	
	private int AddressID;
	
	private String orderno;
	
	private String Country;
	
	private String statename;
	
	private String address;
	
	private String address2;
	
	private String phoneNumber;
	
	private String zipcode;
	
	private String Adstatus;
	
	private String Updatetimr;
	
	private String admUserID;
	
	private String street;
	
	private String recipients;
	
	private String orderno_new;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAddressID() {
		return AddressID;
	}

	public void setAddressID(int addressID) {
		AddressID = addressID;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAdstatus() {
		return Adstatus;
	}

	public void setAdstatus(String adstatus) {
		Adstatus = adstatus;
	}

	public String getUpdatetimr() {
		return Updatetimr;
	}

	public void setUpdatetimr(String updatetimr) {
		Updatetimr = updatetimr;
	}

	public String getAdmUserID() {
		return admUserID;
	}

	public void setAdmUserID(String admUserID) {
		this.admUserID = admUserID;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getOrderno_new() {
		return orderno_new;
	}

	public void setOrderno_new(String orderno_new) {
		this.orderno_new = orderno_new;
	}

}
