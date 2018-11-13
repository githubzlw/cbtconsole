package com.cbt.bean;


public class ComplainFile {
	private int id;
	private int complainid;
	private int complainChatid;
	private String imgUrl;
	private int delState;
	private int flag;
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

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
	public int getComplainChatid() {
		return complainChatid;
	}
	public void setComplainChatid(int complainChatid) {
		this.complainChatid = complainChatid;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getDelState() {
		return delState;
	}
	public void setDelState(int delState) {
		this.delState = delState;
	}
	
}
