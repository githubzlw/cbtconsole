package com.cbt.bean;

public class LevelPageBean {
	private int id;
	private String name;//关键词
	private String page;//二级页面
	private String catid;//类别
	private int valid;//0-失效 1-有效
	private String createTime;//更新时间
	private int count;//统计
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return String
				.format("LevelPageBean [id=%s, name=%s, page=%s, catid=%s, valid=%s, createTime=%s, count=%s]",
						id, name, page, catid, valid, createTime, count);
	}
	

}
