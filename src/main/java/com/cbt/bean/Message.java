package com.cbt.bean;

import java.util.Date;

public class Message {

	public int id;
	public int userid;
	public String codid;//关联ID
	public String content;
	public int state;
	public Date createtime;
	
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"userid\":\"%s\", \"codid\":\"%s\", \"content\":\"%s\", \"state\":\"%s\", \"createtime\":\"%s\"}",
						id, userid,codid, content, state, createtime);
	}

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

	public void setCodid(String codid) {
		this.codid = codid;
	}
	public String getCodid() {
		return codid;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
}
