package com.cbt.pojo;

public class ReportSalesInfo {
	private Integer id;
	private Integer years; //年份
	private Integer months; //月份
	private Integer userCount; //用户数量
	private Double cost;//本月消费
	private Integer orderCount;//本月订单数
	private Double allCost;//历史消费
	private Integer allOrder; //历史订单数
	private Integer newUser;//本月新下单用户
	private Integer oldUser;//老用户
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
	}
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public Double getAllCost() {
		return allCost;
	}
	public void setAllCost(Double allCost) {
		this.allCost = allCost;
	}
	public Integer getAllOrder() {
		return allOrder;
	}
	public void setAllOrder(Integer allOrder) {
		this.allOrder = allOrder;
	}
	public Integer getNewUser() {
		return newUser;
	}
	public void setNewUser(Integer newUser) {
		this.newUser = newUser;
	}
	public Integer getOldUser() {
		return oldUser;
	}
	public void setOldUser(Integer oldUser) {
		this.oldUser = oldUser;
	}
	
}
