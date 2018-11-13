package com.cbt.feedback.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Questionnaire implements Serializable {
	private static final long serialVersionUID = -902925523644L;

	private int id;
	private int type;// 反馈类别,1:没有找到想要的,2:价格太高
	private String content;// 反馈内容
	private String otherComment;// 其他留言
	private int userId;// 用户邮箱
	private String userEmail;// 用户邮箱
	private String searchUrl;// 搜索的url
	private Timestamp createTime;// 创建时间
	private String searchKeywords;// 搜索关键词

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOtherComment() {
		return otherComment;
	}

	public void setOtherComment(String otherComment) {
		this.otherComment = otherComment;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Questionnaire [id=" + id + ", type=" + type + ", content=" + content + ", otherComment=" + otherComment
				+ ", userId=" + userId + ", userEmail=" + userEmail + ", searchUrl=" + searchUrl + ", createTime="
				+ createTime + ", searchKeywords=" + searchKeywords + "]";
	}

}
