package com.cbt.warehouse.pojo;

public class OrderInfoPojo {
	private String user_id;
	private String ipnaddress;//Paypal地址
	private String mergeorders; //合并的订单
	private String orderarrs;
	
	//收货地址
	private String statename;
	private String address2;
	private String street;
	private String address;
	private String zip_code;  //邮编
	private String phone_number;
	private String country; //国家
	
	private String currency;//货币单位
	private String pay_price_tow;//已支付运费
	private String order_ac;// 赠送运费
	private String available_m;//用户余额
	private String applicable_credit; //赠送用户的运费
	
	private String order_no;// 差钱补齐的订单
	
	//国家  
	private String country_code;
	private String chinese_name;
	private String english_name;
	
	//4px运输方式 
	private String productcode;
	private String chinesename;
	private String englishname;
	
	//出库数量
	private String outType ;
	private String outCount;
	
	//订单差的钱
	private String remaining_price;
	
	//其他出货方式
	private String code_id;
	private String code_name;
	private String logistics_name; //物流公司名字
	
	//产品总金额  已支付运费金额  已支付产品费用
	private String sumProduct_cost; //产品总金额
	private String sumPay_price_tow; // 已支付运费金额
	private String sumPayment_amount; // 已支付产品费用
	private String sumOrder_ac;    //订单赠送的总费用
	private String sumForeign_freight; // 预估费用   国外运费
	private String sumDiscount_amount;// 折扣金额

	private String sumPayment_amountRMB; // 已支付产品费用
	
	private String useremail;
	
	
	
	public String getSumPayment_amountRMB() {
		return sumPayment_amountRMB;
	}
	public void setSumPayment_amountRMB(String sumPayment_amountRMB) {
		this.sumPayment_amountRMB = sumPayment_amountRMB;
	}


	private String mode_transport; //用户选择的运输  包含时间
	//上次输入的信息
	//查出保存的信息

	private String yfhNum; 	//快递单号
	private double actual_lwh;//质检费

	public double getMemberFee() {
		return memberFee;
	}

	public void setMemberFee(double memberFee) {
		this.memberFee = memberFee;
	}

	private double memberFee;//会员费
	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	private String exchange_rate;
	public double getActual_lwh() {
		return actual_lwh;
	}

	public void setActual_lwh(double actual_lwh) {
		this.actual_lwh = actual_lwh;
	}

	public String getLogistics_name() {
		return logistics_name;
	}
	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}
	
	
	public String getSumDiscount_amount() {
		return sumDiscount_amount;
	}
	public void setSumDiscount_amount(String sumDiscount_amount) {
		this.sumDiscount_amount = sumDiscount_amount;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}


	public String getSumForeign_freight() {
		return sumForeign_freight;
	}
	public void setSumForeign_freight(String sumForeign_freight) {
		this.sumForeign_freight = sumForeign_freight;
	}
	public String getRemaining_price() {
		return remaining_price;
	}
	public void setRemaining_price(String remaining_price) {
		this.remaining_price = remaining_price;
	}
	public String getApplicable_credit() {
		return applicable_credit;
	}
	public void setApplicable_credit(String applicable_credit) {
		this.applicable_credit = applicable_credit;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMode_transport() {
		return mode_transport;
	}
	public void setMode_transport(String mode_transport) {
		this.mode_transport = mode_transport;
	}


	public String getSumOrder_ac() {
		return sumOrder_ac;
	}
	public void setSumOrder_ac(String sumOrder_ac) {
		this.sumOrder_ac = sumOrder_ac;
	}


	private String fpxCocountry_codeuntryCode ; 	//国家
	private String unpay ; 	//未支付费用
	private String state ;
	private String trans_method;					//出货方式
	private String trans_details ; 	//运输方式详细
	private String package_fee ; 	//包装费
	private String acture_fee; 	//实际运费
	private String volume_lwh ; 			//产品体积
	private String weight ; 					// 整体重量
	private String order_area;   //区域
	
	
	
	public String getSumProduct_cost() {
		return sumProduct_cost;
	}
	public void setSumProduct_cost(String sumProduct_cost) {
		this.sumProduct_cost = sumProduct_cost;
	}
	public String getSumPay_price_tow() {
		return sumPay_price_tow;
	}
	public void setSumPay_price_tow(String sumPay_price_tow) {
		this.sumPay_price_tow = sumPay_price_tow;
	}
	public String getSumPayment_amount() {
		return sumPayment_amount;
	}
	public void setSumPayment_amount(String sumPayment_amount) {
		this.sumPayment_amount = sumPayment_amount;
	}
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public String getOutCount() {
		return outCount;
	}
	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}
	public String getCode_id() {
		return code_id;
	}
	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}
	public String getCode_name() {
		return code_name;
	}
	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}
	public String getOrderarrs() {
		return orderarrs;
	}
	public void setOrderarrs(String orderarrs) {
		this.orderarrs = orderarrs;
	}
	public String getOrder_area() {
		return order_area;
	}
	public void setOrder_area(String order_area) {
		this.order_area = order_area;
	}
	public String getYfhNum() {
		return yfhNum;
	}
	public void setYfhNum(String yfhNum) {
		this.yfhNum = yfhNum;
	}
	public String getFpxCocountry_codeuntryCode() {
		return fpxCocountry_codeuntryCode;
	}
	public void setFpxCocountry_codeuntryCode(String fpxCocountry_codeuntryCode) {
		this.fpxCocountry_codeuntryCode = fpxCocountry_codeuntryCode;
	}
	public String getUnpay() {
		return unpay;
	}
	public void setUnpay(String unpay) {
		this.unpay = unpay;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTrans_method() {
		return trans_method;
	}
	public void setTrans_method(String trans_method) {
		this.trans_method = trans_method;
	}
	public String getTrans_details() {
		return trans_details;
	}
	public void setTrans_details(String trans_details) {
		this.trans_details = trans_details;
	}
	public String getPackage_fee() {
		return package_fee;
	}
	public void setPackage_fee(String package_fee) {
		this.package_fee = package_fee;
	}
	public String getActure_fee() {
		return acture_fee;
	}
	public void setActure_fee(String acture_fee) {
		this.acture_fee = acture_fee;
	}
	public String getVolume_lwh() {
		return volume_lwh;
	}
	public void setVolume_lwh(String volume_lwh) {
		this.volume_lwh = volume_lwh;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getChinesename() {
		return chinesename;
	}
	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}
	public String getEnglishname() {
		return englishname;
	}
	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getChinese_name() {
		return chinese_name;
	}
	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}
	public String getEnglish_name() {
		return english_name;
	}
	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPay_price_tow() {
		return pay_price_tow;
	}
	public void setPay_price_tow(String pay_price_tow) {
		this.pay_price_tow = pay_price_tow;
	}
	public String getOrder_ac() {
		return order_ac;
	}
	public void setOrder_ac(String order_ac) {
		this.order_ac = order_ac;
	}
	public String getAvailable_m() {
		return available_m;
	}
	public void setAvailable_m(String available_m) {
		this.available_m = available_m;
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
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getMergeorders() {
		return mergeorders;
	}
	public void setMergeorders(String mergeorders) {
		this.mergeorders = mergeorders;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIpnaddress() {
		return ipnaddress;
	}
	public void setIpnaddress(String ipnaddress) {
		this.ipnaddress = ipnaddress;
	}
	
	
}
