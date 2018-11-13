package com.cbt.auto.ctrl;

public class AutoToSalePojo {
	private int userid;//用户id
    private String email;//用户邮箱
    private String username;
	private String orderAdmin;
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    public String getOrderAdmin() {
		return orderAdmin;
	}
	public void setOrderAdmin(String orderAdmin) {
		this.orderAdmin = orderAdmin;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
