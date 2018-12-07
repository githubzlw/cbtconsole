package com.importExpress.pojo;

public class CustomerDisputeBean {
	private int id;
	private String disputeID;//申诉事件号
	private String orderNo;//订单号
	private String userid;//用户id
	private String transactionID;//交易号
	private String remark;//备注
	private String status;//状态 0-待退款  1-同意 2 退款成功  -1-拒绝
	private String refundType;//退款类型  0-接受paypal买家索赔   1-paypal卖家提议退款
	private String returnShippingAddress;//退货地址
	private String merchantID;//merchant_id
	private String grossAmount;//订单金额
	private String buyerRequestAmount;//买家申请退款金额
	private String sellerOfferedAmount;//卖家提议退款金额
	private String amountRefunded;//最终退款金额
	private String currencyCode;//货币
	private String offerType;//提议类型
	private String acceptClaimReason;//接受索赔理由
	private String apiType;//申诉源  paypal  stripe
	private String refuseReason;//退款拒绝理
	private String createTime;
	private String updateTime;
	private String oprateAdm;//操作人
	
	private String amount;//金额+货币
	private String adminRolyType;//登录人员权限
	
	public String getAdminRolyType() {
		return adminRolyType;
	}
	public void setAdminRolyType(String adminRolyType) {
		this.adminRolyType = adminRolyType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDisputeID() {
		return disputeID;
	}
	public void setDisputeID(String disputeID) {
		this.disputeID = disputeID;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	public String getReturnShippingAddress() {
		return returnShippingAddress;
	}
	public void setReturnShippingAddress(String returnShippingAddress) {
		this.returnShippingAddress = returnShippingAddress;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}
	public String getBuyerRequestAmount() {
		return buyerRequestAmount;
	}
	public void setBuyerRequestAmount(String buyerRequestAmount) {
		this.buyerRequestAmount = buyerRequestAmount;
	}
	public String getSellerOfferedAmount() {
		return sellerOfferedAmount;
	}
	public void setSellerOfferedAmount(String sellerOfferedAmount) {
		this.sellerOfferedAmount = sellerOfferedAmount;
	}
	public String getAmountRefunded() {
		return amountRefunded;
	}
	public void setAmountRefunded(String amountRefunded) {
		this.amountRefunded = amountRefunded;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getAcceptClaimReason() {
		return acceptClaimReason;
	}
	public void setAcceptClaimReason(String acceptClaimReason) {
		this.acceptClaimReason = acceptClaimReason;
	}
	public String getApiType() {
		return apiType;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOprateAdm() {
		return oprateAdm;
	}
	public void setOprateAdm(String oprateAdm) {
		this.oprateAdm = oprateAdm;
	}

}
