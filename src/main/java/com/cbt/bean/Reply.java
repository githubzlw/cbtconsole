package com.cbt.bean;

import java.util.Date;

/**
 * @author lizhanjun
 * 留言回复
 */
public class Reply {
	/**
	 * 回复id
	 */
	private int id;
	/**
	 * 留言id
	 */
	private int guestbookId;
	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 回复内容
	 */
	private String replyContent;
	/**
	 * 回复时间
	 */
	private Date replyTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGuestbookId() {
		return guestbookId;
	}
	public void setGuestbookId(int guestbookId) {
		this.guestbookId = guestbookId;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "Reply [id=" + id + ", guestbookId=" + guestbookId + ", userId="
				+ userId + ", userName=" + userName + ", replyContent="
				+ replyContent + ", replyTime=" + replyTime + "]";
	}
	
}
