package com.cbt.bean;

public class OrderAddress {
	private String country;
	private String statename;
	private String address2;
	private String address;
	private String zipcode;
	private String street;
	/**Drop Ship订单拆分字段*/
	private int id;
	private String addressId;
	private String orderNo;
	private String phoneNumber;
	private String adstatus;
	private String updatetimr;
	private String admUserID;
	private String recipients;
	private Integer flag;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAdstatus() {
		return adstatus;
	}
	public void setAdstatus(String adstatus) {
		this.adstatus = adstatus;
	}
	public String getUpdatetimr() {
		return updatetimr;
	}
	public void setUpdatetimr(String updatetimr) {
		this.updatetimr = updatetimr;
	}
	public String getAdmUserID() {
		return admUserID;
	}
	public void setAdmUserID(String admUserID) {
		this.admUserID = admUserID;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}	
}
