package com.cbt.change.bean;

import java.sql.Timestamp;

/**
 * 订单变更记录bean
 * 
 * @author jxw
 *
 */
public class OrderChangeRecords {
	private int id;
	private String orderNo;// 订单号
	private int userId;// 用户id
	private int adminId;// 操作人id
	private int operationType;// 操作类型，1:采购,2:入库,3:出运,4:完结
	private String productCost;// 商品费用
	private String payPrice;// 订单生成后用户的支付费用
	private String remainingPrice;// 订单所剩费用
	private String status;// 订单状态：-1:后台取消订单,0:等待付款,1:购买中,2:已到仓库,3:出运中,4:完结,5:确认价格中,6:客户取消订单,7:预订单
	private Timestamp createTime;// 创建时间

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

	public String getProductCost() {
		return productCost;
	}

	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getRemainingPrice() {
		return remainingPrice;
	}

	public void setRemainingPrice(String remainingPrice) {
		this.remainingPrice = remainingPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "OrderChangeRecords [id=" + id + ", orderNo=" + orderNo + ", userId=" + userId + ", adminId=" + adminId
				+ ", operationType=" + operationType + ", productCost=" + productCost + ", payPrice=" + payPrice
				+ ", remainingPrice=" + remainingPrice + ", status=" + status + ", createTime=" + createTime + "]";
	}

}
