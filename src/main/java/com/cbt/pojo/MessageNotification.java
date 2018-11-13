package com.cbt.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 消息提醒bean
 */
public class MessageNotification implements Serializable {

	private static final long serialVersionUID = -750125009816131838L;
	private int id;
	private String orderNo;// 订单No
	private int senderId;// 接受人id
	private int sendType;// 消息的发送类型
	private String sendContent;// 发送内容
	private String linkUrl;// 链接地址
	private Timestamp createTime;// 消息的创建时间
	private String isRead;// 标记是否已读
	private int refundId;// 退款表id
	private String reservation1;// 备用字段1,发送人id使用
	private String reservation2;// 备用字段2
	private String reservation3;// 备用字段3
	private int message_id;

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public int getRefundId() {
		return refundId;
	}

	public void setRefundId(int refundId) {
		this.refundId = refundId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp time) {
		this.createTime = time;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getReservation1() {
		return reservation1;
	}

	public void setReservation1(String reservation1) {
		this.reservation1 = reservation1;
	}

	public String getReservation2() {
		return reservation2;
	}

	public void setReservation2(String reservation2) {
		this.reservation2 = reservation2;
	}

	public String getReservation3() {
		return reservation3;
	}

	public void setReservation3(String reservation3) {
		this.reservation3 = reservation3;
	}

	@Override
	public String toString() {
		return "MessageNotification [id=" + id + ", orderNo=" + orderNo + ", senderId=" + senderId + ", sendType="
				+ sendType + ", sendContent=" + sendContent + ", linkUrl=" + linkUrl + ", createTime=" + createTime
				+ ", isRead=" + isRead + ", refundId=" + refundId + ", reservation1=" + reservation1 + ", reservation2="
				+ reservation2 + ", reservation3=" + reservation3 + "]";
	}

}
