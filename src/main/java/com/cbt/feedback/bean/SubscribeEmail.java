package com.cbt.feedback.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订阅邮箱bean
 * 
 * @author kirin
 *
 */
public class SubscribeEmail implements Serializable {

	private static final long serialVersionUID = -5538426584221L;

	private int id;
	private String email;// 邮箱
	private Timestamp createTime; // 创建时间
	private String label;// 客户标签

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "SubscribeEmail [id=" + id + ", email=" + email + ", createTime=" + createTime + ", label=" + label
				+ "]";
	}

}
