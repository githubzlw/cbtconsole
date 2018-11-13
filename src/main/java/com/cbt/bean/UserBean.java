package com.cbt.bean;

import java.util.Date;

public class UserBean {

	private int id;
	private String sessionId;
	private String email;
	private String name;
	private String pass;
	private String token;
	private String sequence;
	private String activationCode;//������
	private int activationState;//����״̬
	private String picture;//�û�ͷ���ַ
	private Date createtime;
	private Date activationTime;
	private Date activationPassTime;
	private String activationPassCode;
	private String currency;//货币类型
	private int logReg;//登录还是注册
	private String count;//购物车商品数
	private double applicable_credit;
	private String businessName;
	private String available_m;//用户余额
	
	
	
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", sessionId\":\"%s\", email\":\"%s\", name\":\"%s\", pass\":\"%s\", token\":\"%s\", sequence\":\"%s\", createtime\":\"%s\"}",
						id, sessionId, email, name, pass, token, sequence, createtime);
	}
	public UserBean(String email, String name, String pass, String token,
			String sequence, Date createtime) {
		super();
		this.email = email;
		this.name = name;
		this.pass = pass;
		this.token = token;
		this.sequence = sequence;
		this.createtime = createtime;
	}
	
	
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	public String getAvailable_m() {
		return available_m;
	}
	public void setAvailable_m(String available_m) {
		this.available_m = available_m;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	
	public void setActivationState(int activationState) {
		this.activationState = activationState;
	}
	public int getActivationState() {
		return activationState;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPicture() {
		return picture;
	}
	
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}
	public Date getActivationTime() {
		return activationTime;
	}
	public void setActivationPassTime(Date activationPassTime) {
		this.activationPassTime = activationPassTime;
	}
	public Date getActivationPassTime() {
		return activationPassTime;
	}
	public String getActivationPassCode() {
		return activationPassCode;
	}
	public void setActivationPassCode(String activationPassCode) {
		this.activationPassCode = activationPassCode;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public int getLogReg() {
		return logReg;
	}
	public void setLogReg(int logReg) {
		this.logReg = logReg;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	public double getApplicable_credit() {
		return applicable_credit;
	}
	public void setApplicable_credit(double applicable_credit) {
		this.applicable_credit = applicable_credit;
	}
}
