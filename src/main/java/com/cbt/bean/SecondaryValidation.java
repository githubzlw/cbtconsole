package com.cbt.bean;

import java.util.Date;

public class SecondaryValidation {
	private int id;
	private int userid;
	private String password;
	private Date createTime;
	private String updateName;
	private Date updateTime;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "SecondaryValidation [id=" + id + ", userid=" + userid + ", password=" + password + ", createTime="
				+ createTime + ", updateName=" + updateName + ", updateTime=" + updateTime + ", remark=" + remark + "]";
	}

}
