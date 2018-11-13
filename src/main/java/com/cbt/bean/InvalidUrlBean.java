package com.cbt.bean;

public class InvalidUrlBean {
	private int type;//1-surl,0-gurl
	private String url;//
	private String createtime;//
	
	private int count;//
	private int id;
	private String aliUrl;//import-express链接
	
	
	public String getAliUrl() {
		return aliUrl;
	}
	public void setAliUrl(String aliUrl) {
		this.aliUrl = aliUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public InvalidUrlBean() {
		super();
	}
	public InvalidUrlBean(int type, String url, String createtime) {
		super();
		this.type = type;
		this.url = url;
		this.createtime = createtime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
	
	

}
