package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class OldCustom implements Serializable {
	
	private String deliveredTime;//最后下单时间user_id,order_no,delivered_time,username,createdate,useremail,admName,senddate
	private String orderNo;//客户订单
	private String userId;//客户id
	private String userName;//客户名称
	private String createDate;//注册时间
	private String admiName;//销售名称
	private String senddate;//最后跟进时间
	private String userEmail;//客户邮箱
	public String getDeliveredTime() {
		return deliveredTime;
	}
	public void setDeliveredTime(String deliveredTime) {
		this.deliveredTime = deliveredTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAdmiName() {
		return admiName;
	}
	public void setAdmiName(String admiName) {
		this.admiName = admiName;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	

}
