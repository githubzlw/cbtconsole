package com.cbt.pojo;

import java.io.Serializable;

/**
 * 消息提醒类别数量统计
 * 
 * @author JiangXianwei
 *
 */
public class MessageNotificationStatistical implements Serializable {

	private static final long serialVersionUID = 5555223253L;

	private int sendType;// 消息的发送类型
	private int count;// 类别数量
	private int senderId;// 接收人id

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	@Override
	public String toString() {
		return "MessageNotificationStatistical [sendType=" + sendType + ", count=" + count + ", senderId=" + senderId
				+ "]";
	}

}
