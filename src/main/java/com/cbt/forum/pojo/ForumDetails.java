package com.cbt.forum.pojo;

import java.sql.Date;
import java.sql.Timestamp;

public class ForumDetails {
	private Integer id;
	private Integer postClassId;
	private String  postTitle;
	private String  postUser;
	private String  postDetails;
	private String  postPictrue;
	private String  auditUser;
	private Date creatime;
	private int auditState;
	private int detailsState;
	private int postOtherId;
	private ForumClassification forumClass;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPostClassId() {
		return postClassId;
	}

	public void setPostClassId(Integer postClassId) {
		this.postClassId = postClassId;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostUser() {
		return postUser;
	}

	public void setPostUser(String postUser) {
		this.postUser = postUser;
	}

	public String getPostDetails() {
		return postDetails;
	}

	public void setPostDetails(String postDetails) {
		this.postDetails = postDetails;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public Date getCreatime() {
		return creatime;
	}

	public void setCreatime(Date creatime) {
		this.creatime = creatime;
	}

	public int getDetailsState() {
		return detailsState;
	}

	public void setDetailsState(int detailsState) {
		this.detailsState = detailsState;
	}

	public int getPostOtherId() {
		return postOtherId;
	}

	public void setPostOtherId(int postOtherId) {
		this.postOtherId = postOtherId;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public String getPostPictrue() {
		return postPictrue;
	}

	public void setPostPictrue(String postPictrue) {
		this.postPictrue = postPictrue;
	}

	public ForumClassification getForumClass() {
		return forumClass;
	}

	public void setForumClass(ForumClassification forumClass) {
		this.forumClass = forumClass;
	}
}