package com.cbt.warehouse.pojo;
import java.io.Serializable;
import java.util.Date;

/**
 * 注册页面、购物车页面、支付页面反馈弹框bean
 * @author tb
 * @time 2017.3.9
 */
public class ProblemFeedBackBean implements Serializable{

	private static final long serialVersionUID = -1162035188425044268L;
	
	private int id;//主键
	
	private int uid;//用户id;如果用户是登陆的则记录;否则不记录;
	
	private String email;//反馈问题的email，不一定是用户登录的Email;
	
	private String problem;//反馈提交的问题;
	
	private Date createtime;//提交反馈问题的时间;

	private int type;//提交问题所出的页面,1、注册页面；2、购物车页面；3、支付页面;
	
	private String admName;//销售姓名 
	
	//=================王宏杰新增 客户反馈内容实体字段
	private int userid;
	private String userName;//用户名
	private String userEmail;//用户邮箱
	private String sale_email;//对应的销售邮箱
	private String qustion;//反馈问题
	
	private String reply_content;
	private String reply_time;
	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public String getReply_time() {
		return reply_time;
	}

	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSale_email() {
		return sale_email;
	}

	public void setSale_email(String sale_email) {
		this.sale_email = sale_email;
	}

	public String getQustion() {
		return qustion;
	}

	public void setQustion(String qustion) {
		this.qustion = qustion;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Date getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	@Override
	public String toString() {
		return "ProblemFeedBackBean [id=" + id + ", uid=" + uid + ", email=" + email + ", problem=" + problem
				+ ", createtime=" + createtime + ", type=" + type + ", admName=" + admName + "]";
	}

	
	
	
	
}
