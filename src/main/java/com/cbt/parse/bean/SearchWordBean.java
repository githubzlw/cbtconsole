package com.cbt.parse.bean;

public class SearchWordBean {
	private String website;//来源网站
	private String type;//类型
	private String url;//链接
	private String name;//名称
	
	
	
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return String.format("{\"website\"=\"%s\";\"type\"=\"%s\";\"url\"=\"%s\";\"name\"=\"%s\"}",
				website,type, url, name);
	}
	
	

}
