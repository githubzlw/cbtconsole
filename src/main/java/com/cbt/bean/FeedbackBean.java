package com.cbt.bean;

import java.io.Serializable;

public class FeedbackBean implements Serializable {

	private static final long serialVersionUID = -565855236482L;

	private Integer id;
	private Integer type;// 反馈类别,1:没有找到想要的,2:价格太高
	private String content;// 反馈内容
	private String otherComment;// 其他留言
	private String searchUrl;// 搜索的url
	private String searchKeywords;// 搜索关键词

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
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

	@Override
	public String toString() {
		return "FeedbackBean [id=" + id + ", type=" + type + ", content=" + content + ", otherComment=" + otherComment
				+ ", searchUrl=" + searchUrl + ", searchKeywords=" + searchKeywords + "]";
	}

}
