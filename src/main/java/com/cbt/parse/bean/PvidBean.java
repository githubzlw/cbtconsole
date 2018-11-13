package com.cbt.parse.bean;

public class PvidBean {
	private String keyword;//1688类别与类型提取用
	private String catid;//1688类别与类型提取用
	private int sort;//1688排序
	
	//ali_pvidlist.sql表
	private String name;//参数名称
	private String value;//参数对应的值
	private String img;//图片
	private String pvid;//id
	
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPvid() {
		return pvid;
	}
	public void setPvid(String pvid) {
		this.pvid = pvid;
	}
	

}
