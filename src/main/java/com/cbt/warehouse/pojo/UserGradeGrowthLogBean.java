package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserGradeGrowthLogBean implements Serializable {
	private static final long serialVersionUID = 4793490272209668526L;
	private int id;// int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	private int userId;// int(11) NOT NULL COMMENT '用户id',
	private int grade;// int(2) DEFAULT '1' COMMENT '等级',
	private int beforeGrowthValue;// int(11) DEFAULT '0' COMMENT '更改之前的积分',
	private int afterGrowthValue;// int(11) DEFAULT '0' COMMENT '更改后的积分',
	private int gapGrowthValue;
	private int backGrowthValue;
	private int orderGrowthValue;// int(11) DEFAULT '0' COMMENT '本次订单积分',
	private String orderNo;// varchar(255) DEFAULT NULL COMMENT '订单号',
	private double orderPrice;// double(11,2) DEFAULT '0.00' COMMENT '订单金额',
	private int times;// int(4) DEFAULT '0' COMMENT '剩余使用次数',
	private String comment;// varchar(1000) DEFAULT NULL COMMENT '备注',
	private int isOldOrder;//是否就是旧的订单操作
	private Date createTime;// datetime DEFAULT NULL COMMENT '时间',
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getBeforeGrowthValue() {
		return beforeGrowthValue;
	}
	public void setBeforeGrowthValue(int beforeGrowthValue) {
		this.beforeGrowthValue = beforeGrowthValue;
	}
	public int getAfterGrowthValue() {
		return afterGrowthValue;
	}
	public void setAfterGrowthValue(int afterGrowthValue) {
		this.afterGrowthValue = afterGrowthValue;
	}
	public int getOrderGrowthValue() {
		return orderGrowthValue;
	}
	public void setOrderGrowthValue(int orderGrowthValue) {
		this.orderGrowthValue = orderGrowthValue;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getGapGrowthValue() {
		return gapGrowthValue;
	}
	public void setGapGrowthValue(int gapGrowthValue) {
		this.gapGrowthValue = gapGrowthValue;
	}
	public int getBackGrowthValue() {
		return backGrowthValue;
	}
	public void setBackGrowthValue(int backGrowthValue) {
		this.backGrowthValue = backGrowthValue;
	}
	public int getIsOldOrder() {
		return isOldOrder;
	}
	public void setIsOldOrder(int isOldOrder) {
		this.isOldOrder = isOldOrder;
	}
	
}
