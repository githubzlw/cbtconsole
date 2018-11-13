package com.cbt.bean;


public class Payment {
	private int id;
	private int userid;//用户id
	private String orderid;//订单流水号
	private String paymentid;//付款流水号
	private String orderdesc;//订单描述
	private String username;//用户名
	private int paystatus;//付款状态
	private float payment_amount;//付款金额
	private String payment_cc;//付款币种
	private String createtime;
	private String paySID;//本地交易申请号
	private String payflag;
	private String paytype; //交易方式     渠道
	private float payment_other;//合并扣除费用
	private String orderSplit;//订单拆分情况     0未拆单|1前台自动拆单|2后台手动拆单
	private String payAll;//总付款
	private String balancePay;//余额付款
	
	public String getPayAll() {
		return payAll;
	}
	public void setPayAll(String payAll) {
		this.payAll = payAll;
	}
	public String getBalancePay() {
		return balancePay;
	}
	public void setBalancePay(String balancePay) {
		this.balancePay = balancePay;
	}
	public String getOrderSplit() {
		return orderSplit;
	}
	public void setOrderSplit(String orderSplit) {
		this.orderSplit = orderSplit;
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
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getOrderdesc() {
		return orderdesc;
	}
	public void setOrderdesc(String orderdesc) {
		this.orderdesc = orderdesc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}
	public float getPayment_amount() {
		return payment_amount;
	}
	public void setPayment_amount(float payment_amount) {
		this.payment_amount = payment_amount;
	}
	public String getPayment_cc() {
		return payment_cc;
	}
	public void setPayment_cc(String payment_cc) {
		this.payment_cc = payment_cc;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPaySID() {
		return paySID;
	}
	public void setPaySID(String paySID) {
		this.paySID = paySID;
	}
	public String getPayflag() {
		return payflag;
	}
	public void setPayflag(String payflag) {
		this.payflag = payflag;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public void setPayment_other(float payment_other) {
		this.payment_other = payment_other;
	}
	public float getPayment_other() {
		return payment_other;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"userid\":\"%s\", \"orderid\":\"%s\", \"paymentid\":\"%s\", \"orderdesc\":\"%s\", \"username\":\"%s\", \"paystatus\":\"%s\", \"payment_amount\":\"%s\", \"payment_cc\":\"%s\", \"createtime\":\"%s\"}",
						id, userid, orderid, paymentid, orderdesc, username,
						paystatus, payment_amount, payment_cc, createtime);
	}
	
}
