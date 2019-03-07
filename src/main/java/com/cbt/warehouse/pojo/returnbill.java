package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

public class returnbill implements Serializable{
private int id;
private int appyId;//获取退货人id
private Double changAmount;//获取到账金额
private String optUser;//获取实际退货人员
private Date optTime;//获取实际退单时间
private Date endTime;//获取完结时间
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getAppyId() {
	return appyId;
}
public void setAppyId(int appyId) {
	this.appyId = appyId;
}
public Double getChangAmount() {
	return changAmount;
}
public void setChangAmount(Double changAmount) {
	this.changAmount = changAmount;
}
public String getOptUser() {
	return optUser;
}
public void setOptUser(String optUser) {
	this.optUser = optUser;
}
public Date getOptTime() {
	return optTime;
}
public void setOptTime(Date optTime) {
	this.optTime = optTime;
}
public Date getEndTime() {
	return endTime;
}
public void setEndTime(Date endTime) {
	this.endTime = endTime;
}

}
