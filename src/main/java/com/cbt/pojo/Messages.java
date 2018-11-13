package com.cbt.pojo;

import java.util.Date;

public class Messages {
	private Integer id;

	private String type;

	private Integer userid;

	private String title;

	private Date createTime;

	private String content;

	private String hrefName;

	private byte isDelete;

	private String typeName;

	private Integer adminid;

	private Date applytime;

	private Date complettime;

	private Integer eventid;

	public Integer getEventid() {
		return eventid;
	}

	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	public Date getComplettime() {
		return complettime;
	}

	public void setComplettime(Date complettime) {
		this.complettime = complettime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getHrefName() {
		return hrefName;
	}

	public void setHrefName(String hrefName) {
		this.hrefName = hrefName == null ? null : hrefName.trim();
	}

	public byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getAdminid() {
		return adminid;
	}

	public void setAdminid(Integer adminid) {
		this.adminid = adminid;
	}

	@Override
	public String toString() {
		return "Messages [id=" + id + ", type=" + type + ", userid=" + userid + ", title=" + title + ", createTime="
				+ createTime + ", content=" + content + ", hrefName=" + hrefName + ", isDelete=" + isDelete
				+ ", typeName=" + typeName + ", adminid=" + adminid + ", applytime=" + applytime + ", complettime="
				+ complettime + ", eventid=" + eventid + "]";
	}

}