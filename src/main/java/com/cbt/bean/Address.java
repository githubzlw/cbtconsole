package com.cbt.bean;


public class Address {
	private int id;
	private int userid;
	private String recipients;//暂时存放支付总金额
	private String address;
	private String country;
	private String phone_number;
	private String zip_code;
	private String countryname;
	private String address2;
	private String statename;
	private String createtime;
	private String modifytime;
	private int defaultaddress;
    private String delflag;
	private String street;

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
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
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"userid\":\"%s\", \"recipients\":\"%s\", \"address\":\"%s\",\"street\":\"%s\", \"country\":\"%s\", \"phone_number\":\"%s\", \"zip_code\":\"%s\", \"countryname\":\"%s\", \"address2\":\"%s\", \"statename\":\"%s\"}",
						id, userid, recipients, address, street, country, phone_number,
						zip_code, countryname, address2, statename);
	}
}
