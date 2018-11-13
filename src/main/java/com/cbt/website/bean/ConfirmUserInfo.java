package com.cbt.website.bean;

public class ConfirmUserInfo {
	private int id;
	private String confirmusername;
	private int role;

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConfirmusername() {
		return confirmusername;
	}

	public void setConfirmusername(String confirmusername) {
		this.confirmusername = confirmusername;
	}

	@Override
	public String toString() {
		return "ConfirmUserInfo [id=" + id + ", confirmusername=" + confirmusername + ", role=" + role + "]";
	}

}
