package com.cbt.change.bean;

import java.sql.Timestamp;

/**
 * 订单详情更改记录bean
 * 
 * @author jxw
 *
 */
public class OrderDetailsChangeRecords {
	private int id;
	private String orderNo;// 订单号
	private int odsId;// 订单详情id
	private int goodsId;// 商品id
	private String goodsPrice;// 商品价格
	private int goodsNumber;// 商品数量
	private String odsStatus;// 订单详情状态1-已到仓库，2-已删除
	private String purchaseStatus;// 商品采购状态 0-没有确认，1-确认货源 3-确认购买
	private int userId;// 用户id
	private int adminId;// 操作人id
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

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "OrderDetailsChangeRecords [id=" + id + ", orderNo=" + orderNo + ", odsId=" + odsId + ", goodsId="
				+ goodsId + ", goodsPrice=" + goodsPrice + ", goodsNumber=" + goodsNumber + ", odsStatus=" + odsStatus
				+ ", purchaseStatus=" + purchaseStatus + ", userId=" + userId + ", adminId=" + adminId + ", createTime="
				+ createTime + "]";
	}

}
