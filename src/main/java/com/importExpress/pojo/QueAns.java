package com.importExpress.pojo;

import java.io.Serializable;


public class QueAns implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private int questionid;
	private String userid;
	private String user_name;
	private String pid;
	private String spider_mincatid;
	private String pname;
	private String purl;
	private String question_content;
	private String create_time;
	private String reply_status;
	private String reply_name;
	private String reply_content;
	private String reply_time;
	private int isShow;
	private int parentqid;
	private int questionIsShow;
	private String email;//客户邮箱
	private String sale_email;//销售邮箱
	private String shop_id;//产品店铺名称
	private String c_shop_id;
	private String admName;//销售
	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getC_shop_id() {
		return c_shop_id;
	}
	public void setC_shop_id(String c_shop_id) {
		this.c_shop_id = c_shop_id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSale_email() {
		return sale_email;
	}
	public void setSale_email(String sale_email) {
		this.sale_email = sale_email;
	}
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSpider_mincatid() {
		return spider_mincatid;
	}
	public void setSpider_mincatid(String spider_mincatid) {
		this.spider_mincatid = spider_mincatid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getQuestion_content() {
		return question_content;
	}
	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getReply_status() {
		return reply_status;
	}
	public void setReply_status(String reply_status) {
		this.reply_status = reply_status;
	}
	public String getReply_name() {
		return reply_name;
	}
	public void setReply_name(String reply_name) {
		this.reply_name = reply_name;
	}
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
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getParentqid() {
		return parentqid;
	}
	public void setParentqid(int parentqid) {
		this.parentqid = parentqid;
	}
	public int getQuestionIsShow() {
		return questionIsShow;
	}
	public void setQuestionIsShow(int questionIsShow) {
		this.questionIsShow = questionIsShow;
	}
	
}