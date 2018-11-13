package com.cbt.bean;

public class ShippingBean{
	private int id;
	private String name;
	private String days;
	private double result;
	private double feight;
	private int result1;//包裹数量
	private int battery;//是否带电,1-是
	private double result_notfree;//合并运输时，之前非免邮对应的运费价格
	private double promote_shipping;
	private double volumeweights;//总的体积重
	private int isJCEX;//是否是佳成运输
	private int exist_vw;//产品 包含了 “会算体积重量的 门类”
	private String actual_freight;//订单实际出运运费
	private String estimatefreight;

	public String getAc_weight() {
		return ac_weight;
	}

	public void setAc_weight(String ac_weight) {
		this.ac_weight = ac_weight;
	}

	private String ac_weight;
	public String getEstimatefreight() {
		return estimatefreight;
	}

	public void setEstimatefreight(String estimatefreight) {
		this.estimatefreight = estimatefreight;
	}

	public String getTransportcompany() {
		return transportcompany;
	}

	public void setTransportcompany(String transportcompany) {
		this.transportcompany = transportcompany;
	}

	public String getShippingtype() {
		return shippingtype;
	}

	public void setShippingtype(String shippingtype) {
		this.shippingtype = shippingtype;
	}

	private String transportcompany;
	private String shippingtype;
	public String getActual_freight() {
		return actual_freight;
	}

	public void setActual_freight(String actual_freight) {
		this.actual_freight = actual_freight;
	}

	public int getIsJCEX() {
		return isJCEX;
	}
	public void setIsJCEX(int isJCEX) {
		this.isJCEX = isJCEX;
	}
	public int getExist_vw() {
		return exist_vw;
	}
	public void setExist_vw(int exist_vw) {
		this.exist_vw = exist_vw;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	private String deliveryTime;//国内处理时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public int getResult1() {
		return result1;
	}
	public void setResult1(int result1) {
		this.result1 = result1;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	public int getBattery() {
		return battery;
	}
	public double getResult_notfree() {
		return result_notfree;
	}
	public void setResult_notfree(double result_notfree) {
		this.result_notfree = result_notfree;
	}
	public void setVolumeweights(double volumeweights) {
		this.volumeweights = volumeweights;
	}
	public double getVolumeweights() {
		return volumeweights;
	}
	
	public void setFeight(double feight) {
		this.feight = feight;
	}
	public double getFeight() {
		return feight;
	}
	public void setPromote_shipping(double promote_shipping) {
		this.promote_shipping = promote_shipping;
	}
	public double getPromote_shipping() {
		return promote_shipping;
	}
	
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"name\":\"%s\", \"days\":\"%s\", \"result\":\"%s\", \"result1\":\"%s\", \"result_notfree\":\"%s\"}",
						id, name, days, result, result1,result_notfree);
	}
	public ShippingBean(){
		
	}
	public ShippingBean(int id,String name,String days,double result,int result1){
		this.id=id;
		this.name=name;
		this.days=days;
		this.result=result;
		this.result1=result1;
	}
}
