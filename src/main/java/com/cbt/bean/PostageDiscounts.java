package com.cbt.bean;

/**
 * @author ylm
 * 邮费折扣申请
 */
public class PostageDiscounts {

	private int id;
	private int userid;
	private String sessionid;
	private String name;
	private String email;
	private String phone;
	private int countryid;
	private String country;
	private String shopping_company;//运输方式
	private String shopping_price;//运输金额
	private String createtime;
	private String ip;
	private int handlestate;//处理状态
	private String handleman;//处理人员
	private String handletime;//处理时间
	private int coi;//申请公司或者个人
	private int page;
	private int count;
	private String weight;//重量
	private String admname;//负责人
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
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getCountryid() {
		return countryid;
	}
	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}
	public String getShopping_company() {
		return shopping_company;
	}
	public void setShopping_company(String shopping_company) {
		this.shopping_company = shopping_company;
	}
	public String getShopping_price() {
		return shopping_price;
	}
	public void setShopping_price(String shopping_price) {
		this.shopping_price = shopping_price;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHandleman() {
		return handleman;
	}
	public void setHandleman(String handleman) {
		this.handleman = handleman;
	}
	public String getHandletime() {
		return handletime;
	}
	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}
	public int getCoi() {
		return coi;
	}
	public void setCoi(int coi) {
		this.coi = coi;
	}
	public void setHandlestate(int handlestate) {
		this.handlestate = handlestate;
	}
	public int getHandlestate() {
		return handlestate;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWeight() {
		return weight;
	}
	public String getAdmname() {
		return admname;
	}
	public void setAdmname(String admname) {
		this.admname = admname;
	}
}
