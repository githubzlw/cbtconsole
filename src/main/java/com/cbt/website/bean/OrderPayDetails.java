package com.cbt.website.bean;

public class OrderPayDetails {
	private String order_no;//订单号
	private String user_id;//用户ID
	private String address_id;//订单地址ID
	private String country;//订单国家
	private String delivery_time;//国内交期天数
	private String delivery_time_abroad;//国外交期
	private String mode_transport;//运输方式
	private double service_fee;//服务费用
	private double product_cost;//产品费用
	private String domestic_freight;//国内运费
	private String foreign_freight;//国外运费
	private String actual_allincost;//国际总费用
	private double pay_price;//订单生成后用户的支付费用
	private double pay_price_tow;//已支付运费金额
	private String pay_price_three;//余额抵扣费用
	private String actual_ffreight;//实际国际运费
	private String remaining_price;//订单所剩费用（订单欠费-|订单剩余费用）
	private String actual_volume;//整体长宽高
	private String actual_weight;//整体重量
	private String transport_time;//国际运输时间
	private String cancel_obj;//取消订单对象(0-客户，1-官方)
	private String expect_arrive_time;//预计到货时间
	private String create_time;//
	private String currency;//货币单位
	private String discount_amount;//折扣金额
	private String actual_lwh;//实际体积
	private String actual_weight_estimate;//预估重量
	private String actual_freight_c;//实际成本国际运费
	
	private String available_m;//账户余额
	private String applicable_credit;//赠送运费余额
	private String ucurrency;//账户余额货币单位
	private String buyuser;//订单负责人

	private double fpxFee;//4PX费用
	private double yfhFee;//原飞航费用
	private String admname;//销售负责人
	
	//order_fee
	private String  ofweight;         //重量
	private String  ofvolume_lwh;     //体积
	private String  ofcountry_code;    //国家
	private String  orderarr;
	private String  ofdelivery_time;  // 查询出来的运输天数
	private String  ofacture_get_fee; //真的运费
	private String  ofacture_fee; //  真的运费
	private String  oftrans_method; //  运输方式 4px  嘉城 原飞航
	private String  oftrans_details; //  运输方式明细
	private String  ofcargo_type; //    包裹类型
	private String  ofunpay; //    差的钱
	private String  oforder_area;  //原飞航区域
	private String  ofyfhNum;  //原飞航单号
	
	
//	private String  of; //    原飞航单号
//	private String  of; //    
//	private String  of; //
//	private String  of; //
//	private String  of; // 
//	private String  of;// 
//	private String  of; //
	 
	
	
	
	
	public String getOfyfhNum() {
		return ofyfhNum;
	}
	public void setOfyfhNum(String ofyfhNum) {
		this.ofyfhNum = ofyfhNum;
	}
	public String getOforder_area() {
		return oforder_area;
	}
	public void setOforder_area(String oforder_area) {
		this.oforder_area = oforder_area;
	}
	public String getOfunpay() {
		return ofunpay;
	}
	public void setOfunpay(String ofunpay) {
		this.ofunpay = ofunpay;
	}
	public String getOfweight() {
		return ofweight;
	}
	public String getOfdelivery_time() {
		return ofdelivery_time;
	}
	public void setOfdelivery_time(String ofdelivery_time) {
		this.ofdelivery_time = ofdelivery_time;
	}
	public String getOfacture_get_fee() {
		return ofacture_get_fee;
	}
	public void setOfacture_get_fee(String ofacture_get_fee) {
		this.ofacture_get_fee = ofacture_get_fee;
	}
	public String getOfacture_fee() {
		return ofacture_fee;
	}
	public void setOfacture_fee(String ofacture_fee) {
		this.ofacture_fee = ofacture_fee;
	}
	public String getOftrans_method() {
		return oftrans_method;
	}
	public void setOftrans_method(String oftrans_method) {
		this.oftrans_method = oftrans_method;
	}
	public String getOftrans_details() {
		return oftrans_details;
	}
	public void setOftrans_details(String oftrans_details) {
		this.oftrans_details = oftrans_details;
	}
	public String getOfcargo_type() {
		return ofcargo_type;
	}
	public void setOfcargo_type(String ofcargo_type) {
		this.ofcargo_type = ofcargo_type;
	}
	public void setOfweight(String ofweight) {
		this.ofweight = ofweight;
	}
	public String getOfvolume_lwh() {
		return ofvolume_lwh;
	}
	public void setOfvolume_lwh(String ofvolume_lwh) {
		this.ofvolume_lwh = ofvolume_lwh;
	}
	public String getOfcountry_code() {
		return ofcountry_code;
	}
	public void setOfcountry_code(String ofcountry_code) {
		this.ofcountry_code = ofcountry_code;
	}
	public String getOrderarr() {
		return orderarr;
	}
	public void setOrderarr(String orderarr) {
		this.orderarr = orderarr;
	}
	public double getFpxFee() {
		return fpxFee;
	}
	public void setFpxFee(double fpxFee) {
		this.fpxFee = fpxFee;
	}
	public double getYfhFee() {
		return yfhFee;
	}
	public void setYfhFee(double yfhFee) {
		this.yfhFee = yfhFee;
	}
	public String getAvailable_m() {
		return available_m;
	}
	public void setAvailable_m(String available_m) {
		this.available_m = available_m;
	}
	public String getApplicable_credit() {
		return applicable_credit;
	}
	public void setApplicable_credit(String applicable_credit) {
		this.applicable_credit = applicable_credit;
	}
	public String getUcurrency() {
		return ucurrency;
	}
	public void setUcurrency(String ucurrency) {
		this.ucurrency = ucurrency;
	}
	public String getBuyuser() {
		return buyuser;
	}
	public void setBuyuser(String buyuser) {
		this.buyuser = buyuser;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getMode_transport() {
		return mode_transport;
	}
	public void setMode_transport(String mode_transport) {
		this.mode_transport = mode_transport;
	}
	public String getDomestic_freight() {
		return domestic_freight;
	}
	public void setDomestic_freight(String domestic_freight) {
		this.domestic_freight = domestic_freight;
	}
	public String getForeign_freight() {
		return foreign_freight;
	}
	public void setForeign_freight(String foreign_freight) {
		this.foreign_freight = foreign_freight;
	}
	public String getActual_allincost() {
		return actual_allincost;
	}
	public void setActual_allincost(String actual_allincost) {
		this.actual_allincost = actual_allincost;
	}
	public double getService_fee() {
		return service_fee;
	}
	public void setService_fee(double service_fee) {
		this.service_fee = service_fee;
	}
	public double getProduct_cost() {
		return product_cost;
	}
	public void setProduct_cost(double product_cost) {
		this.product_cost = product_cost;
	}
	public double getPay_price() {
		return pay_price;
	}
	public void setPay_price(double pay_price) {
		this.pay_price = pay_price;
	}
	public double getPay_price_tow() {
		return pay_price_tow;
	}
	public void setPay_price_tow(double pay_price_tow) {
		this.pay_price_tow = pay_price_tow;
	}
	public String getPay_price_three() {
		return pay_price_three;
	}
	public void setPay_price_three(String pay_price_three) {
		this.pay_price_three = pay_price_three;
	}
	public String getActual_ffreight() {
		return actual_ffreight;
	}
	public void setActual_ffreight(String actual_ffreight) {
		this.actual_ffreight = actual_ffreight;
	}
	public String getRemaining_price() {
		return remaining_price;
	}
	public void setRemaining_price(String remaining_price) {
		this.remaining_price = remaining_price;
	}
	public String getActual_volume() {
		return actual_volume;
	}
	public void setActual_volume(String actual_volume) {
		this.actual_volume = actual_volume;
	}
	public String getActual_weight() {
		return actual_weight;
	}
	public void setActual_weight(String actual_weight) {
		this.actual_weight = actual_weight;
	}
	public String getTransport_time() {
		return transport_time;
	}
	public void setTransport_time(String transport_time) {
		this.transport_time = transport_time;
	}
	public String getCancel_obj() {
		return cancel_obj;
	}
	public void setCancel_obj(String cancel_obj) {
		this.cancel_obj = cancel_obj;
	}
	public String getExpect_arrive_time() {
		return expect_arrive_time;
	}
	public void setExpect_arrive_time(String expect_arrive_time) {
		this.expect_arrive_time = expect_arrive_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(String discount_amount) {
		this.discount_amount = discount_amount;
	}
	public String getActual_lwh() {
		return actual_lwh;
	}
	public void setActual_lwh(String actual_lwh) {
		this.actual_lwh = actual_lwh;
	}
	public String getActual_weight_estimate() {
		return actual_weight_estimate;
	}
	public void setActual_weight_estimate(String actual_weight_estimate) {
		this.actual_weight_estimate = actual_weight_estimate;
	}
	public String getActual_freight_c() {
		return actual_freight_c;
	}
	public void setActual_freight_c(String actual_freight_c) {
		this.actual_freight_c = actual_freight_c;
	}
	public String getDelivery_time_abroad() {
		return delivery_time_abroad;
	}
	public void setDelivery_time_abroad(String delivery_time_abroad) {
		this.delivery_time_abroad = delivery_time_abroad;
	}
	public String getAdmname() {
		return admname;
	}
	public void setAdmname(String admname) {
		this.admname = admname;
	}

}
