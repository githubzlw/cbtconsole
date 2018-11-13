package com.cbt.Specification.bean;

import java.io.Serializable;

public class AliCategory implements Serializable {

	private static final long serialVersionUID = -5362561755L;
	private String id;
	private String cid;
	private String path;
	private String category;
	private String time;
	private String lv;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

	@Override
	public String toString() {
		return "AliCategory [id=" + id + ", cid=" + cid + ", path=" + path + ", category=" + category + ", time=" + time
				+ ", lv=" + lv + "]";
	}

}
