package com.cbt.email.entity;

import java.util.Date;


public class AdminUser {
	/** 客户编号*/
	private int id;
	
	/** 客户id*/
	private int userId;
	/** 客户姓名*/
	private String userName;
	
	/** 客户邮件地址*/
	private String useremail;
	
	/** 销售id*/
	private int adminId;
	/** 创建时间*/
	private Date createdate;

	/** 销售名字*/
	private String admName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	@Override
	public String toString() {
		return "Admin_User [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", useremail=" + useremail + ", adminId="
				+ adminId + ", createdate=" + createdate + ", admName="
				+ admName + "]";
	}
	
	
}
