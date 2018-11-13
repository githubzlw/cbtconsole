package com.cbt.website.userAuth.bean;

import java.io.Serializable;


/**
 * 管理员实体类
 * @author admins
 *
 */
public class Admuser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String admName;
	private String password;
	private String roletype;
	private String status;
	private String Email;
	private String title;
	private String emialpass;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmialpass() {
		return emialpass;
	}
	public void setEmialpass(String emialpass) {
		this.emialpass = emialpass;
	}
	@Override
	public String toString() {
		return String
				.format("Admuser [id=%s, admName=%s, password=%s, roletype=%s, status=%s, Email=%s, title=%s, emialpass=%s]",
						id, admName, password, roletype, status, Email, title,
						emialpass);
	}
	
	
	
	
	
	
	

}
