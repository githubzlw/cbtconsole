package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

public class MessageError implements Serializable {
	
	private static final long serialVersionUID = 8132583440545279046L;
	
	public int id;
	public String email;
	public String error;
	public String info;
	public Date createtime;
	
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
		
}
