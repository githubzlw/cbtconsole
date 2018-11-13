package com.cbt.email.entity;

/**
 * 抄送人
 */
public class EmailCC {
	/** 抄送人ID*/
	private int id;
	/** 客户id(对应Customer)*/
	private int cid;
	/** 收发件的邮件id*/
	private int eid;
	/** 客户的名字*/
	private String name;
	/** 分页的开始*/
	private int start;
	/** 分页的结束*/
	private int end;
	/** 抄送人名字*/
	private String cName;
	/** 抄送人邮箱地址*/
	private String emailAddress;
	/** 是否是本公司员工(是否为本公司员工（0不是，1是）默认不是*/
	private int isOurCompany;
	/** 是收件的抄送人还是发件的抄送人（1表示发件，2表示收件）*/
	private int isSR;
	/** 添加到用户 ，0未添加到客户，1，已添加到客户 2，客户已存在*/
	private String is_cus;
	/** 项目id*/
	public String projectId;
	/** 客户邮箱*/
	public String email;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getIs_cus() {
		return is_cus;
	}
	public void setIs_cus(String is_cus) {
		this.is_cus = is_cus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public int getIsOurCompany() {
		return isOurCompany;
	}
	public void setIsOurCompany(int isOurCompany) {
		this.isOurCompany = isOurCompany;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public int getIsSR() {
		return isSR;
	}
	public void setIsSR(int isSR) {
		this.isSR = isSR;
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
	@Override
	public String toString() {
		return "EmailCC [id=" + id + ", cid=" + cid + ", eid=" + eid
				+ ", name=" + name + ", start=" + start + ", end=" + end
				+ ", cName=" + cName + ", emailAddress=" + emailAddress
				+ ", isOurCompany=" + isOurCompany + ", isSR=" + isSR
				+ ", is_cus=" + is_cus + ", projectId=" + projectId + "]";
	}
	
	
	
}
