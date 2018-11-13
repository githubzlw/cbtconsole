package com.cbt.website.bean;

/**
 * @author Administrator
 * 客户信息综合查询对象类
 */
public class ApplicationSummary {
	private int id;
	private int userid;
	private String username;
	private String useremail;
	private String tmpid;
	private int leave_message;
	private int batch_application;
	private int postage_discount;
	private int business_inquiries;
	private String AppDate;
	private String AppDate_Month;
	private String AppDate_Year;
	private String confirmDate;
	private String confirmUser;
	private String CreateDate;
	private String confirmUsername;
	private int status;
	private int count;//用户的下单数
	private int adminId;
	private int total;//总条数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getTmpid() {
		return tmpid;
	}
	public void setTmpid(String tmpid) {
		this.tmpid = tmpid;
	}
	public int getLeave_message() {
		return leave_message;
	}
	public void setLeave_message(int leave_message) {
		this.leave_message = leave_message;
	}
	public int getBatch_application() {
		return batch_application;
	}
	public void setBatch_application(int batch_application) {
		this.batch_application = batch_application;
	}
	public int getPostage_discount() {
		return postage_discount;
	}
	public void setPostage_discount(int postage_discount) {
		this.postage_discount = postage_discount;
	}
	public int getBusiness_inquiries() {
		return business_inquiries;
	}
	public void setBusiness_inquiries(int business_inquiries) {
		this.business_inquiries = business_inquiries;
	}
	public String getAppDate() {
		return AppDate;
	}
	public void setAppDate(String appDate) {
		AppDate = appDate;
	}
	public String getAppDate_Month() {
		return AppDate_Month;
	}
	public void setAppDate_Month(String appDate_Month) {
		AppDate_Month = appDate_Month;
	}
	public String getAppDate_Year() {
		return AppDate_Year;
	}
	public void setAppDate_Year(String appDate_Year) {
		AppDate_Year = appDate_Year;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getConfirmUsername() {
		return confirmUsername;
	}
	public void setConfirmUsername(String confirmUsername) {
		this.confirmUsername = confirmUsername;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public int getAdminId() {
		return adminId;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal() {
		return total;
	}
}
