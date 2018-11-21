package com.importExpress.pojo;

public class WebhookPaymentBean {
	private String id;
	private String createTime;
	private String amount;//金额
	private String paymentMode;
	private String transactionFee;//手续
	private String profit;//利润
	private String type;//类型  贸易 ebay  importexpress
	private String userid;
	private String email;
	private String orderNO;
	private String payType;//支付类型 stripe paypal
	private String findType;//查询类型 mysql  webhook
	private long time;
	private String receiverID;
	private String trackID;
	
	private double mcGross;//金额
	private double refundAmount;
	
	
	public String getTrackID() {
		return trackID;
	}
	public void setTrackID(String trackID) {
		this.trackID = trackID;
	}
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public double getMcGross() {
		return mcGross;
	}
	public void setMcGross(double mcGross) {
		this.mcGross = mcGross;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getTransactionFee() {
		return transactionFee;
	}
	public void setTransactionFee(String transactionFee) {
		this.transactionFee = transactionFee;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"createTime\":\"" + createTime + "\", \"amount\":\"" + amount
				+ "\", \"paymentMode\":\"" + paymentMode + "\", \"transactionFee\":\"" + transactionFee
				+ "\", \"type\":\"" + type + "\", \"userid\":\"" + userid + "\", \"email\":\"" + email
				+ "\", \"orderNO\":\"" + orderNO + "\"}";
	}
	
	
	
	

}
