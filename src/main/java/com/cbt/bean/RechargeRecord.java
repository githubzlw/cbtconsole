package com.cbt.bean;

/**
 * @author ylm
 * 充值记录表
 */
public class RechargeRecord {

	private int id;
	private int userid;
	private double price;
	private double balanceAfter;
	private String datatime;
	private int type;//充值类型:1.取消订单，2多余金额，3重复支付，4手动，5其他 7-余额抵扣 8-申请提现  9-提现取消 10 提现拒绝
	private String remark;
	private String remark_id;//注备ID：orderno
	private int usesign;//收入or支付
	private String currency;  
	protected int paytype;
	protected String paymentId;
	protected String afterBalanceshow;
	protected String currencyshow;
	private String adminUser;//操作员
	
	private int count;
	
	
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark_id() {
		return remark_id;
	}
	public void setRemark_id(String remark_id) {
		this.remark_id = remark_id;
	}
	public void setUsesign(int usesign) {
		this.usesign = usesign;
	}
	public int getUsesign() {
		return usesign;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getBalanceAfter() {
		return balanceAfter;
	}
	public void setBalanceAfter(double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	public int getPaytype() {
		return paytype;
	}
	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getAfterBalanceshow() {
		return afterBalanceshow;
	}
	public void setAfterBalanceshow(String afterBalanceshow) {
		this.afterBalanceshow = afterBalanceshow;
	}
	public String getCurrencyshow() {
		return currencyshow;
	}
	public void setCurrencyshow(String currencyshow) {
		this.currencyshow = currencyshow;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\"=\"%s\", \"userid\"=\"%s\", \"price\"=\"%s\", "
						+ "\"balanceAfter\"=\"%s\", \"datatime\"=\"%s\", \"type\"=\"%s\", "
						+ "\"remark\"=\"%s\", \"remark_id\"=\"%s\", \"usesign\"=\"%s\", "
						+ "\"currency\"=\"%s\", \"paytype\"=\"%s\", "
						+ "\"paymentId\"=\"%s\", \"afterBalanceshow\"=\"%s\", "
						+ "\"currencyshow\"=\"%s\", \"adminUser\"=\"%s\", \"count\"=\"%s\"}",
						id, userid, price, balanceAfter, datatime, type,
						remark, remark_id, usesign, currency, paytype,
						paymentId, afterBalanceshow, currencyshow, adminUser,
						count);
	}
	
	
}
