package com.cbt.bean;

import java.util.Date;

public class BusiessBean {
	private Integer id;
	private String company;
	private String email;
	private String ordervalue;
	private String needs;
	private String createtime;
	private Integer userid;
	private String userphone;
	private String admName;
	private int status;
	private int adminid;
	private Date updatetime;
	// start add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
	private Integer customizedId;
	// end add by yang_tao for 关联服装定制信息字段添加_2017年1月7日
	private Integer feedbackId;// 客户反馈id
	private int state;
	private String startdate;
	private String enddate;
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public int getAdminid() {
		return adminid;
	}

	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrdervalue() {
		return ordervalue;
	}

	public void setOrdervalue(String ordervalue) {
		this.ordervalue = ordervalue;
	}

	public String getNeeds() {
		return needs;
	}

	public void setNeeds(String needs) {
		this.needs = needs;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public Integer getCustomizedId() {
		return customizedId;
	}

	public void setCustomizedId(Integer customizedId) {
		this.customizedId = customizedId;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

}
