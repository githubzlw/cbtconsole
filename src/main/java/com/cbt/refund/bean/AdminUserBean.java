package com.cbt.refund.bean;

public class AdminUserBean {

	private int id;
	private String admName;
	private String email;
	private String password;
	private String title;
	private int roleType;
	private int state;

	public AdminUserBean() {
		super();
	}

	public AdminUserBean(int id, String admName, String email, String password, String title, int roleType, int state) {
		super();
		this.id = id;
		this.admName = admName;
		this.email = email;
		this.password = password;
		this.title = title;
		this.roleType = roleType;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "AdminUserBean [id=" + id + ", admName=" + admName + ", email=" + email + ", password=" + password
				+ ", title=" + title + ", roleType=" + roleType + ", state=" + state + "]";
	}

}
