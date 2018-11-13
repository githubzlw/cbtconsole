package com.cbt.warehouse.pojo;

public class JcexPrintInfo {
	private String userid;    //
	private String orderno;//
	private String weight;//重量
	private String volume_lwh;//体积
	private String cargo_type;//包装
	private String express_no;//运单条码
	private String productname;//英文品名
	private String producenglishtname;//中文品名
	private String productnum;//内件数
	private String productprice;//单价金额 申报价值
	private String productcurreny;//申报币种
	private String productremark;//备注
//	private UserOrderDetails uod;
	private String useridAndOrderid;
	private String adminname;
	private String admincompany;
	private String admincode;
	private String recipients;
	private String address;
	private String zone;
	private String address2;
	private String statename;
	private String zipcode;
	private String phone;
	private String payType;
	private String pay_curreny;
	private String hscode;
	public String getHscode() {
		return hscode;
	}
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	public String getPay_curreny() {
		return pay_curreny;
	}
	public void setPay_curreny(String pay_curreny) {
		this.pay_curreny = pay_curreny;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getAdmincompany() {
		return admincompany;
	}
	public void setAdmincompany(String admincompany) {
		this.admincompany = admincompany;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
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
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getUseridAndOrderid() {
		return this.useridAndOrderid;       
	}
	public void setUseridAndOrderid(String useridAndOrderid) {
		this.useridAndOrderid = useridAndOrderid;
	}
//	public UserOrderDetails getUod() {
//		return uod;
//	}
//	public void setUod(UserOrderDetails uod) {
//		this.uod = uod;
//	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getVolume_lwh() {
		return volume_lwh;
	}
	public void setVolume_lwh(String volume_lwh) {
		this.volume_lwh = volume_lwh;
	}
	public String getCargo_type() {
		return cargo_type;
	}
	public void setCargo_type(String cargo_type) {
		this.cargo_type = cargo_type;
	}
	public String getExpress_no() {
		return express_no;
	}
	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProducenglishtname() {
		return producenglishtname;
	}
	public void setProducenglishtname(String producenglishtname) {
		this.producenglishtname = producenglishtname;
	}
	public String getProductnum() {
		return productnum;
	}
	public void setProductnum(String productnum) {
		this.productnum = productnum;
	}
	public String getProductprice() {
		return productprice;
	}
	public void setProductprice(String productprice) {
		this.productprice = productprice;
	}
	public String getProductcurreny() {
		return productcurreny;
	}
	public void setProductcurreny(String productcurreny) {
		this.productcurreny = productcurreny;
	}
	public String getProductremark() {
		return productremark;
	}
	public void setProductremark(String productremark) {
		this.productremark = productremark;
	}
}
