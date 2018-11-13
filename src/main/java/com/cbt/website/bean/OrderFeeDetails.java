package com.cbt.website.bean;

public class OrderFeeDetails {
	
	private String yfhNum; //原飞航单号
	private int  uid;//用户id
	private String  order;//订单号
	private String  delivery_time;//交期天数
	private String  currency;//货币单位
	private double  packagefee;//额外包装费
	private double  actfee;//实际运费
	private double  actcgetfee;//实收运费
	private String  volume;//体积
	private double  weight;//重量
	private String  country;//国家//收货地址
	private double  feetrans;//未支付运费
	private double  feecount;//总的未支付费用
	private String  transport;//4PX,YuanFeiHang 
	private String  zone;//4px对应的国家代码
	private String  order_area;//QuYu
	private String  trans;//4px运输方式
	private String  admin;//操作人员
	private int idts;  //余额，运费余额抵扣
	private double  app_credit;//总的未支付费用
	private double  deduction;//运费余额抵扣费用
	private String  cargoType;//货物类型
	
	
	
	
	public String getYfhNum() {
		return yfhNum;
	}
	public void setYfhNum(String yfhNum) {
		this.yfhNum = yfhNum;
	}
	public String getCargoType() {
		return cargoType;
	}
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getPackagefee() {
		return packagefee;
	}
	public void setPackagefee(double packagefee) {
		this.packagefee = packagefee;
	}
	public double getActfee() {
		return actfee;
	}
	public void setActfee(double actfee) {
		this.actfee = actfee;
	}
	public double getActcgetfee() {
		return actcgetfee;
	}
	public void setActcgetfee(double actcgetfee) {
		this.actcgetfee = actcgetfee;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getFeetrans() {
		return feetrans;
	}
	public void setFeetrans(double feetrans) {
		this.feetrans = feetrans;
	}
	public double getFeecount() {
		return feecount;
	}
	public void setFeecount(double feecount) {
		this.feecount = feecount;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getOrder_area() {
		return order_area;
	}
	public void setOrder_area(String order_area) {
		this.order_area = order_area;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getTrans() {
		return trans;
	}
	public void setTrans(String trans) {
		this.trans = trans;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public int getIdts() {
		return idts;
	}
	public void setIdts(int idts) {
		this.idts = idts;
	}
	public double getApp_credit() {
		return app_credit;
	}
	public void setApp_credit(double app_credit) {
		this.app_credit = app_credit;
	}
	public double getDeduction() {
		return deduction;
	}
	public void setDeduction(double deduction) {
		this.deduction = deduction;
	}
	
	
}
