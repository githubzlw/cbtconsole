package com.cbt.bean;

public class IntensveBean {

	public IntensveBean() {
	}

	private String keyword;//关键词
	private String catid;//类别
	private String category;//类别名称
	private String type;//类型
	private String status;//状态
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public IntensveBean(String keyword, String catid, String type, String status) {
		super();
		this.keyword = keyword;
		this.catid = catid;
		this.type = type;
		this.status = status;
	}
}
