package com.importExpress.pojo;

import java.io.Serializable;


public class Address implements Serializable {
	
	private static final long serialVersionUID = 3603539991948026554L;
	
	private int id;
	private int userid;
	/**
	 * Recipients 收件人
	 */
	private String recipients;
	/**
	 * street1
	 */
	private String address;
	/**
	 * street2
	 */
	private String country;
	private String phone_number;
	private String zip_code;
	private String countryname;
	/**
	 * city
	 */
	private String address2;
	private String statename;
	private String createtime;
	private String modifytime;
	private int defaultaddress;
	private String street;
//	private String shorthand;//国家缩写,发起paypal支付传入的国家
	/**
	 *用户邮箱
	 * @return
	 */
	private String email;

	/**
	 * 商业名字
	 * @return
	 */
	private String businessName;

	/**
	 * 从是行业
	 * @return
	 */
	private String businessIntroduction;
	/**
	 * cookie id
	 */
	private String userCookieId;
	/**
	 * 是否存在的条数
	 */
	private int count;
	/**
	 * 1:第一次添加，2，新增，3，修改
	 */
	private int type;
	/**
	 * 1:不一样 1 一样
	 */
	private int showHihts;

	public int getShowHihts() {
		return showHihts;
	}

	public void setShowHihts(int showHihts) {
		this.showHihts = showHihts;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUserCookieId() {
		return userCookieId;
	}

	public void setUserCookieId(String userCookieId) {
		this.userCookieId = userCookieId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessIntroduction() {
		return businessIntroduction;
	}

	public void setBusinessIntroduction(String businessIntroduction) {
		this.businessIntroduction = businessIntroduction;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public String getRecipients() {
		return recipients;
	}
	
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getModifytime() {
		return modifytime;
	}
	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}	
	public int getDefaultaddress() {
		return defaultaddress;
	}
	public void setDefaultaddress(int defaultaddress) {
		this.defaultaddress = defaultaddress;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	
	/*public String getShorthand() {
		return shorthand;
	}
	public void setShorthand(String shorthand) {
		this.shorthand = shorthand;
	}*/

	@Override
	public String toString() {
		return "Address{" + "id=" + id + ", userid=" + userid + ", recipients='" + recipients + '\'' + ", address='" + address + '\'' + ", country='" + country + '\'' + ", phone_number='" + phone_number + '\'' + ", zip_code='" + zip_code + '\'' + ", countryname='" + countryname + '\'' + ", address2='" + address2 + '\'' + ", statename='" + statename + '\'' + ", createtime='" + createtime + '\'' + ", modifytime='" + modifytime + '\'' + ", defaultaddress=" + defaultaddress + ", street='" + street + '\'' + ", email='" + email + '\'' + ", businessName='" + businessName + '\'' + ", businessIntroduction='" + businessIntroduction + '\'' + ", userCookieId='" + userCookieId + '\'' + ", count=" + count + ", type=" + type + ", showHihts=" + showHihts + '}';
	}
}
