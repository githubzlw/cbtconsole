package com.importExpress.pojo;

import java.util.Date;

public class TabCouponType {

	private Long id;

	// 券的分类码
	private String typeCode;

	// 券的使用说明
	private String typeNote;

	// 1-有效可用；0-无效
	private Long state;

	private Date createtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeNote() {
		return typeNote;
	}

	public void setTypeNote(String typeNote) {
		this.typeNote = typeNote;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "TabCouponType [id=" + id + ", typeCode=" + typeCode + ", typeNote=" + typeNote + ", state=" + state
				+ ", createtime=" + createtime + "]";
	}
	
}