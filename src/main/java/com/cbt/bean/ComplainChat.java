package com.cbt.bean;

import java.io.Serializable;
import java.util.Date;

public class ComplainChat implements Serializable {

	private static final long serialVersionUID = 9977586222L;

	private int id;
	private int complainid;
	private String chatText;
	private Date chatTime;
	private String chatAdmin;
	private int chatAdminid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComplainid() {
		return complainid;
	}

	public void setComplainid(int complainid) {
		this.complainid = complainid;
	}

	public String getChatText() {
		return chatText;
	}

	public void setChatText(String chatText) {

		this.chatText = chatText;
	}

	public Date getChatTime() {
		return chatTime;
	}

	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}

	public String getChatAdmin() {
		return chatAdmin;
	}

	public void setChatAdmin(String chatAdmin) {
		this.chatAdmin = chatAdmin;
	}

	public int getChatAdminid() {
		return chatAdminid;
	}

	public void setChatAdminid(int chatAdminid) {
		this.chatAdminid = chatAdminid;
	}

	@Override
	public String toString() {
		return "ComplainChat [id=" + id + ", complainid=" + complainid + ", chatText=" + chatText + ", chatTime="
				+ chatTime + ", chatAdmin=" + chatAdmin + ", chatAdminid=" + chatAdminid + "]";
	}

}
