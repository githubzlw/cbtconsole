package com.cbt.pojo;

public class Admuser {
	private Integer id;

	private String admname;

	private String email;

	private String password;

	private String title;

	private Integer roletype;

	private Integer status;

	private String emailpass;

	private String admName; // 之前的命名和 redis 的不一致

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdmname() {
		return admname;
	}

	public void setAdmname(String admname) {
		this.admname = admname == null ? null : admname.trim();
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
		this.email = email == null ? null : email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Integer getRoletype() {
		return roletype;
	}

	public void setRoletype(Integer roletype) {
		this.roletype = roletype;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmailpass() {
		return emailpass;
	}

	public void setEmailpass(String emailpass) {
		this.emailpass = emailpass == null ? null : emailpass.trim();
	}

	@Override
	public String toString() {
		return "Admuser [id=" + id + ", admname=" + admname + ", email=" + email + ", password=" + password + ", title="
				+ title + ", roletype=" + roletype + ", status=" + status + ", emailpass=" + emailpass + ", admName="
				+ admName + "]";
	}

}