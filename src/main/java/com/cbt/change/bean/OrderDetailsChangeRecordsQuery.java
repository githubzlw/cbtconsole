package com.cbt.change.bean;

public class OrderDetailsChangeRecordsQuery {

	private int id;// OrderDetailsChangeRecords的id
	private String orderNo;// 订单号
	private int odsId;// 订单详情id
	private int goodsId;// 商品id
	private String odsStatus;// 订单详情状态
	private String purchaseStatus;// 商品采购状态
	private int userId;// 用户id
	private int adminId;// 操作人id
	private String beginDate;// 创建时间
	private String endDate;// 创建时间
	private int startNo;// 查询开始数
	private int limitCount;//查询数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOdsId() {
		return odsId;
	}

	public void setOdsId(int odsId) {
		this.odsId = odsId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getOdsStatus() {
		return odsStatus;
	}

	public void setOdsStatus(String odsStatus) {
		this.odsStatus = odsStatus;
	}

	public String getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(String purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getStartNo() {
		return startNo;
	}

	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	@Override
	public String toString() {
		return "OrderDetailsChangeRecordsQuery [id=" + id + ", orderNo=" + orderNo + ", odsId=" + odsId + ", goodsId="
				+ goodsId + ", odsStatus=" + odsStatus + ", purchaseStatus=" + purchaseStatus + ", userId=" + userId
				+ ", adminId=" + adminId + ", beginDate=" + beginDate + ", endDate=" + endDate + ", startNo=" + startNo
				+ ", limitCount=" + limitCount + "]";
	}



}
