package com.cbt.bean;

public class Complain {
	private int id;
	private int userid;
	private String userEmail;
	private String complainType;
	private String complainText;
	private String creatTime;
	private String closeTime;
	private String refOrderId;
	private String refGoodsId;
	private int complainState;
	private String dealAdmin;
	private int dealAdminid;

	private String saleAdmin;// 关联的销售
	private int isRefund;// 是否申请退款

	public String getRefGoodsId() {
		return refGoodsId;
	}

	public void setRefGoodsId(String refGoodsId) {
		this.refGoodsId = refGoodsId;
	}

	public int getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(int isRefund) {
		this.isRefund = isRefund;
	}

	public String getSaleAdmin() {
		return saleAdmin;
	}

	public void setSaleAdmin(String saleAdmin) {
		this.saleAdmin = saleAdmin;
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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getComplainType() {
		return complainType;
	}

	public void setComplainType(String complainType) {
		this.complainType = complainType;
	}

	public String getComplainText() {
		return complainText;
	}

	public void setComplainText(String complainText) {
		this.complainText = complainText;
	}

	public String getRefOrderId() {
		return refOrderId;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}

	public int getComplainState() {
		return complainState;
	}

	public void setComplainState(int complainState) {
		this.complainState = complainState;
	}

	public String getDealAdmin() {
		return dealAdmin;
	}

	public void setDealAdmin(String dealAdmin) {
		this.dealAdmin = dealAdmin;
	}

	public int getDealAdminid() {
		return dealAdminid;
	}

	public void setDealAdminid(int dealAdminid) {
		this.dealAdminid = dealAdminid;
	}

	@Override
	public String toString() {
		return "Complain [id=" + id + ", userid=" + userid + ", userEmail=" + userEmail + ", complainType="
				+ complainType + ", complainText=" + complainText + ", creatTime=" + creatTime + ", closeTime="
				+ closeTime + ", refOrderId=" + refOrderId + ", complainState=" + complainState + ", dealAdmin="
				+ dealAdmin + ", dealAdminid=" + dealAdminid + ", saleAdmin=" + saleAdmin + ", isRefund=" + isRefund
				+ "]";
	}

}
