package com.cbt.change.bean;

public class OrderChangeRecordsQuery {

	private int id;// OrderChangeRecords的id
	private String orderNo;// 订单号
	private int userId;// 用户id
	private int adminId;// 操作人id
	private int operationType;// 操作类型
	private String status;// 订单状态
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

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "OrderChangeRecordsQuery [id=" + id + ", orderNo=" + orderNo + ", userId=" + userId + ", adminId="
				+ adminId + ", operationType=" + operationType + ", status=" + status + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", startNo=" + startNo + ", limitCount=" + limitCount + "]";
	}

	

}
