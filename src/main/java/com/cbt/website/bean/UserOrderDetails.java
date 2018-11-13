package com.cbt.website.bean;

import java.util.List;

public class UserOrderDetails {

	//2/26 新加 
	private String transMethod; //货运方式
	private String cargoType; //货物类型
	//收件人信息
	private String countryCode;
	private String userid;
	private String userName;
	private String email;
	private String zone;//国家
	private String address;//地址
	private String address2;
	private String phone;
	private String statename;
	private String zipcode;
//	private Float goodsPrice;//购物车总额
	private String orderno;
	private String goodsnum;
	private List<ProductCodeBean> productCodeBean;
	private List<CountryCodeBean> countryCodeBean;
	private List<ProductBean> productBean;
	private String mark;//备注
	
	private String outmethod;//出货方式,0:4PX;1:原飞航
	private String transport;
	private String weight;
	private String goodstype;
	private String usercompany;
	private String userzone;
	private String usercode;
	private String userstate;
	private String usercity;
	private String userstreet;
	private String useraddress;
	private String userphone;
	private String orderlist;//合并订单
	private String recipients; //收件人
	//寄件人信息
	private String adminname;
	private String admincompany;
	private String adminzone;
	private String admincode;
	private String adminaddress;
	private String adminphone;
	private String adminprovince;
	private String admincity;
	private String shipmentno;
	
	
	
	public String getShipmentno() {
		return shipmentno;
	}
	public void setShipmentno(String shipmentno) {
		this.shipmentno = shipmentno;
	}
	public String getCargoType() {
		return cargoType;
	}
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTransMethod() {
		return transMethod;
	}
	public void setTransMethod(String transMethod) {
		this.transMethod = transMethod;
	}
	public String getAdminprovince() {
		return adminprovince;
	}
	public void setAdminprovince(String adminprovince) {
		this.adminprovince = adminprovince;
	}
	public String getAdmincity() {
		return admincity;
	}
	public void setAdmincity(String admincity) {
		this.admincity = admincity;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public List<ProductCodeBean> getProductCodeBean() {
		return productCodeBean;
	}
	public void setProductCodeBean(List<ProductCodeBean> productCodeBean) {
		this.productCodeBean = productCodeBean;
	}
	public List<CountryCodeBean> getCountryCodeBean() {
		return countryCodeBean;
	}
	public void setCountryCodeBean(List<CountryCodeBean> countryCodeBean) {
		this.countryCodeBean = countryCodeBean;
	}
	public List<ProductBean> getProductBean() {
		return productBean;
	}
	public void setProductBean(List<ProductBean> productBean) {
		this.productBean = productBean;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
	/////////////wuliuxinxi
	public String getOutmethod() {
		return outmethod;
	}
	public void setOutmethod(String outmethod) {
		this.outmethod = outmethod;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(String goodstype) {
		this.goodstype = goodstype;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getUsercompany() {
		return usercompany;
	}
	public void setUsercompany(String usercompany) {
		this.usercompany = usercompany;
	}
	public String getAdmincompany() {
		return admincompany;
	}
	public void setAdmincompany(String admincompany) {
		this.admincompany = admincompany;
	}
	public String getUserzone() {
		return userzone;
	}
	public void setUserzone(String userzone) {
		this.userzone = userzone;
	}
	public String getAdminzone() {
		return adminzone;
	}
	public void setAdminzone(String adminzone) {
		this.adminzone = adminzone;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getUserstate() {
		return userstate;
	}
	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}
	public String getUsercity() {
		return usercity;
	}
	public void setUsercity(String usercity) {
		this.usercity = usercity;
	}
	public String getUserstreet() {
		return userstreet;
	}
	public void setUserstreet(String userstreet) {
		this.userstreet = userstreet;
	}
	public String getUseraddress() {
		return useraddress;
	}
	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}
	public String getAdminaddress() {
		return adminaddress;
	}
	public void setAdminaddress(String adminaddress) {
		this.adminaddress = adminaddress;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getAdminphone() {
		return adminphone;
	}
	public void setAdminphone(String adminphone) {
		this.adminphone = adminphone;
	}
	public String getOrderlist() {
		return orderlist;
	}
	public void setOrderlist(String orderlist) {
		this.orderlist = orderlist;
	}
	
}
