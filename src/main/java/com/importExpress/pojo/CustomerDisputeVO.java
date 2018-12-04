package com.importExpress.pojo;

public class CustomerDisputeVO extends CustomerDisputeBean {
	private long time;//时间
	private String email;//用户邮箱
	private String value;//申诉订单金额
	private String reason;//申诉理由
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	
}
