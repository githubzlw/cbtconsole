package com.cbt.warehouse.pojo;

import java.sql.Timestamp;

/**
 * 批量优惠邮件
 * 
 * @author JXW
 *
 */
public class BatchDiscountEmail {
	private int id;
	private String orderNo;// 订单号
	private int userId;// 客户id
	private String userEmail;// 客户邮箱
	private float productAmount;// 产品金额
	private int productCategoryNum;// 订单产品种类数
	private int batchDiscountNum;// 批量优惠种类数
	private Timestamp payTime;// 支付时间
	private Timestamp shipmentTime;// 出运时间
	private String orderCountry;// 订货国家
	private int currentOrderStatus;// 当前订单状态
	private int adminId;// 销售id
	private String adminName;// 销售名称
	private String flag;// 邮件推送状态:-3废弃不发;-2:未满足发送条件;-1:发送失败;0:待发送;1:已发送(已读);2:已发送(未读)
	private Timestamp sendTime;// 发送时间
	private Timestamp createTime;// 创建时间
	private int errorNum;// 发送错误次数

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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public float getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(float productAmount) {
		this.productAmount = productAmount;
	}

	public int getProductCategoryNum() {
		return productCategoryNum;
	}

	public void setProductCategoryNum(int productCategoryNum) {
		this.productCategoryNum = productCategoryNum;
	}

	public int getBatchDiscountNum() {
		return batchDiscountNum;
	}

	public void setBatchDiscountNum(int batchDiscountNum) {
		this.batchDiscountNum = batchDiscountNum;
	}

	public Timestamp getPayTime() {
		return payTime;
	}

	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}

	public Timestamp getShipmentTime() {
		return shipmentTime;
	}

	public void setShipmentTime(Timestamp shipmentTime) {
		this.shipmentTime = shipmentTime;
	}

	public String getOrderCountry() {
		return orderCountry;
	}

	public void setOrderCountry(String orderCountry) {
		this.orderCountry = orderCountry;
	}

	public int getCurrentOrderStatus() {
		return currentOrderStatus;
	}

	public void setCurrentOrderStatus(int currentOrderStatus) {
		this.currentOrderStatus = currentOrderStatus;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	@Override
	public String toString() {
		return "BatchDiscountEmail [id=" + id + ", orderNo=" + orderNo + ", userId=" + userId + ", userEmail="
				+ userEmail + ", productAmount=" + productAmount + ", productCategoryNum=" + productCategoryNum
				+ ", batchDiscountNum=" + batchDiscountNum + ", payTime=" + payTime + ", shipmentTime=" + shipmentTime
				+ ", orderCountry=" + orderCountry + ", currentOrderStatus=" + currentOrderStatus + ", adminId="
				+ adminId + ", adminName=" + adminName + ", flag=" + flag + ", sendTime=" + sendTime + ", createTime="
				+ createTime + ", errorNum=" + errorNum + "]";
	}

}
