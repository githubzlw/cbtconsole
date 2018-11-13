package com.cbt.website.bean;

/**
 * @author wanyang
 * 订单仓库信息
 */
public class OrderWarehouseInfo {
	private String OrderId;//订单号
	private String WarehouseId;//库位号
	private int OrderCount;//订单有效商品数
	private int InWarehouseCount;//订单入库商品数
	private String ConfirmUser;//入库确认人
	
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getWarehouseId() {
		return WarehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		WarehouseId = warehouseId;
	}
	public int getOrderCount() {
		return OrderCount;
	}
	public void setOrderCount(int orderCount) {
		OrderCount = orderCount;
	}
	public int getInWarehouseCount() {
		return InWarehouseCount;
	}
	public void setInWarehouseCount(int inWarehouseCount) {
		InWarehouseCount = inWarehouseCount;
	}
	public String getConfirmUser() {
		return ConfirmUser;
	}
	public void setConfirmUser(String confirmUser) {
		ConfirmUser = confirmUser;
	}
}
