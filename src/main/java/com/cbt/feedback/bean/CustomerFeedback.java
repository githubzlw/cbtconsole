package com.cbt.feedback.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustomerFeedback implements Serializable {
	private static final long serialVersionUID = -902925523644L;
	
	private int id;
	private int type;// 进入类型：1、订阅进入；2、注册进入
	private String needsProducts;// 需要的产品
	private String sales;// 销售额
	private String email;// 邮箱
	private int userId;// 用户邮箱
	private Timestamp createTime;// 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNeedsProducts() {
		return needsProducts;
	}

	public void setNeedsProducts(String needsProducts) {
		this.needsProducts = needsProducts;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "CustomerFeedback [id=" + id + ", type=" + type + ", needsProducts=" + needsProducts + ", sales=" + sales
				+ ", email=" + email + ", userId=" + userId + ", createTime=" + createTime + "]";
	}

}
