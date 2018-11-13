package com.cbt.bean;

public class AutoOrderBean {
	private int id;//payment_invoice表id
	private String index;//orderinfo表id
	private String  orderid;//订单号
	private String userid;//用户
	private String orderAdmin;//订单生成处理人
	private String paymentAdmin;//进账记录生成处理人
	private String payPrice;//订单付款金额
	private String currency;//货币
	private String orderState;//订单状态
	private String payStatus;//支付状态
	private String paymentid;//交易流水号
	private int payId;//payment表id
	private String  payType;//付款方式
	private String createTime;//时间

	public String getPaymentstr() {
		return paymentstr;
	}

	public void setPaymentstr(String paymentstr) {
		this.paymentstr = paymentstr;
	}

	private String paymentstr;
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	private String oid;
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private int state;
	
	private int count;//计数
	
	
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOrderAdmin() {
		return orderAdmin;
	}
	public void setOrderAdmin(String orderAdmin) {
		this.orderAdmin = orderAdmin;
	}
	public String getPaymentAdmin() {
		return paymentAdmin;
	}
	public void setPaymentAdmin(String paymentAdmin) {
		this.paymentAdmin = paymentAdmin;
	}
	public String getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
