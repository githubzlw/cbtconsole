package com.cbt.website.bean;

public class EmailSendInfo {
	/*
	 * 用户id
	 */
	private int userid;
	/*
	 * 用户email
	 */
	private String email;
	/*
	 * 订单
	 */
	private String orderid;
	/*
	 * 订单状态
	 */
	private int orderstate;
	/*
	 * 发送方式
	 */
	private int type;
	/*
	 * 发送状态
	 */
	private int result;
	/*
	 * 备注信息
	 */
	private String info;
	/*
	 * 创建时间
	 */
	private String createtime;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getOrderstate() {
		return orderstate;
	}
	public void setOrderstate(int orderstate) {
		this.orderstate = orderstate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
	
	
	
}
