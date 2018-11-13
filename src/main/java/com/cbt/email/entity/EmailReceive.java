package com.cbt.email.entity;

/**
 * 邮件收件原版
 */
public class EmailReceive {
	/** 邮件id*/
	private int id;
	/** 客户id*/
	private int userId;
	/** 销售id*/
	private int adminId;
	/** 发件人*/
	private String send;
	/** 邮箱*/
	private String email;
	/** 问题id*/
	private int questionid;
	/** 邮件标题*/
	private String title;
	/** 邮件内容*/
	private String content;
	/** 邮件发送日期*/
	private String sendDate;
	/** 回复邮件标题*/
	private String retitle;
	/** 回复邮件内容*/
	private String recontent;
	/** 回复邮件发送日期*/
	private String reDate;
	/** 是否包含附件(0未包含,1已包含)*/
	private int include;
	/** 是否回复 0未回复 1，已回复*/
	private int status;
   
	/** 分页的开始*/
	private int start;
	/** 分页的结束*/
	private int end;
	/** 查看标识(0未查看，1已查看)*/
	private int isLook;
	/** 原始邮件名*/
	private String originalmessage;
	
	private  String  userName ;
	private  int   count  ; //总数
	 
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getOriginalmessage() {
		return originalmessage;
	}
	public void setOriginalmessage(String originalmessage) {
		this.originalmessage = originalmessage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRetitle() {
		return retitle;
	}
	public void setRetitle(String retitle) {
		this.retitle = retitle;
	}
	public String getRecontent() {
		return recontent;
	}
	public void setRecontent(String recontent) {
		this.recontent = recontent;
	}
	public String getReDate() {
		return reDate;
	}
	public void setReDate(String reDate) {
		this.reDate = reDate;
	}
	public String getSend() {
		return send;
	}
	public void setSend(String send) {
		this.send = send;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public int getInclude() {
		return include;
	}
	public void setInclude(int include) {
		this.include = include;
	}

	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getIsLook() {
		return isLook;
	}
	public void setIsLook(int isLook) {
		this.isLook = isLook;
	}
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int string) {
		this.adminId = string;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "EmailReceive [id=" + id + ", userId=" + userId + ", adminId="
				+ adminId + ", send=" + send + ", email=" + email
				+ ", questionid=" + questionid + ", title=" + title
				+ ", content=" + content + ", sendDate=" + sendDate
				+ ", retitle=" + retitle + ", recontent=" + recontent
				+ ", reDate=" + reDate + ", include=" + include + ", status="
				+ status + ", start=" + start + ", end=" + end + ", isLook="
				+ isLook + ", originalmessage=" + originalmessage + "]";
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	}
	

