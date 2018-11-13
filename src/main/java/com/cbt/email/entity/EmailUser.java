package com.cbt.email.entity;

import java.util.Date;

public class EmailUser {
	
	/** 用户id*/
	private int id;
	/** 用户角色编号(1-翻译 2-数据录入 3-老外销售 4-跟单销售 5-其他销售 6-采购 7-物流 8-质检)*/
	private int roleNo;
	/** 用户名称*/
	private String userName;
	/** 用户密码*/
	private String pwd;
	/** 员工真实姓名*/
	private String trueName;
	/** 员工职位*/
	private String job;
	/** 用户邮件地址*/
	private String emailAddress;
	/** 出生日期*/
	private String birthday;
	/** 入职时间*/
	private String worktime;
	/** 英语水平*/
	private String englishLVL;
	/** 联系方式*/
	private String phone;
	/** 住址*/
	private String liveAddress;
	/** 工作年限*/
	private int workmonth;
	/** skape邮件地址*/
	private String skapeemail;
	/** msn邮件地址*/
	private String Msn;
	/** 公司电话*/
	private String CompanyPhone;
	/** 移动电话*/
	private String Mobile;
	/** 邮箱密码*/
	private String emailPWD;
	/** 是否需要翻译的标识（0不需要，1需要)*/
	private int flag;
	/** 是否需要在职的标识（0在职，1离职)*/
	private int dimission;
	/** 注册日期 */
	private Date registDate;
	/** 登录Ip */
	private String loginIp ;
	/** 登录错误次数 */
	private String wrongNumber ;
	/** 登录错误时间 */
	private Long wrongTime ;
	/** 错误Ip*/
	private Long wrongIp ;
	/** 权限*/
	private int qualification ;
	/** 登录Ip */
	private String loginIp1 ;
	/** 登录Ip */
	private String loginIp2 ;
	/** 登录Ip */
	private String loginIp3 ;
	/** 登录Ip */
	private String loginIp4 ;
	/** 登录Ip */
	private String loginIp5 ;
	/** 登录Ip */
	private String loginIp6 ;
	/** 登录Ip */
	private String loginIp7 ;
	/** 登录Ip */
	private String loginIp8 ;
	/** 登录Ip */
	private String loginIp9 ;
	/** 登录Ip */
	private String loginIp10 ;
	/** 登录Ip */
	private String loginIp11 ;
	/** 登录Ip */
	private String loginIp12 ;
	/** 登录Ip */
	private String loginIp13 ;
	/** 登录Ip */
	private String loginIp14 ;
	/** 登录Ip */
	private String loginIp15 ;
	/** 登录Ip */
	private String loginIp16 ;
	/** 登录Ip */
	private String loginIp17 ;
	/** 登录Ip */
	private String loginIp18 ;
	/** 登录Ip */
	private String loginIp19 ;
	/** 登录Ip */
	private String loginIp20 ;
	/** 登录Ip */
	private String loginIp21 ;
	/** 登录Ip */
	private String loginIp22 ;
	/** 登录Ip */
	private String loginIp23 ;
	/** 登录Ip */
	private String loginIp24 ;
	/** 登录Ip */
	private String loginIp25 ;
	/** 登录Ip */
	private String loginIp26 ;
	/** 登录Ip */
	private String loginIp27 ;
	/** 登录Ip */
	private String loginIp28 ;
	/** 登录Ip */
	private String loginIp29 ;
	/** 登录Ip */
	private String loginIp30 ;
	/** 用户邮件地址*/
	private String emailAddress1;
	
	
	public String getEmailAddress1() {
		return emailAddress1;
	}
	public void setEmailAddress1(String emailAddress1) {
		this.emailAddress1 = emailAddress1;
	}
	public EmailUser() {
		
	}
	public EmailUser(String userName, String pwd, String emailAddress) {
		super();
		this.userName = userName;
		this.pwd = pwd;
		this.emailAddress = emailAddress;
	}
	public EmailUser(String userName2, String newPWD) {
		super();
		this.userName = userName;
		this.pwd = pwd;
	}
	/*public EmailUser(String userName2) {
		super();
		this.userName = userName;
		
	}*/
	
	
	
	public String getEmailPWD() {
		return emailPWD;
	}
	public int getQualification() {
		return qualification;
	}
	public void setQualification(int qualification) {
		this.qualification = qualification;
	}
	public String getWorktime() {
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public Long getWrongIp() {
		return wrongIp;
	}
	public void setWrongIp(Long wrongIp) {
		this.wrongIp = wrongIp;
	}
	public Long getWrongTime() {
		return wrongTime;
	}
	public void setWrongTime(Long wrongTime) {
		this.wrongTime = wrongTime;
	}
	public String getWrongNumber() {
		return wrongNumber;
	}
	public void setWrongNumber(String wrongNumber) {
		this.wrongNumber = wrongNumber;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public void setEmailPWD(String emailPWD) {
		this.emailPWD = emailPWD;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getRoleNo() {
		return roleNo;
	}
	public void setRoleNo(int roleNo) {
		this.roleNo = roleNo;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	
	public int getDimission() {
		return dimission;
	}
	public void setDimission(int dimission) {
		this.dimission = dimission;
	}
	
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getEnglishLVL() {
		return englishLVL;
	}
	public void setEnglishLVL(String englishLVL) {
		this.englishLVL = englishLVL;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLiveAddress() {
		return liveAddress;
	}
	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}
	public int getWorkmonth() {
		return workmonth;
	}
	public void setWorkmonth(int workmonth) {
		this.workmonth = workmonth;
	}
	public String getSkapeemail() {
		return skapeemail;
	}
	public void setSkapeemail(String skapeemail) {
		this.skapeemail = skapeemail;
	}
	public String getMsn() {
		return Msn;
	}
	public void setMsn(String msn) {
		Msn = msn;
	}
	public String getCompanyPhone() {
		return CompanyPhone;
	}
	public void setCompanyPhone(String companyPhone1) {
		CompanyPhone = companyPhone1;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	
	public String getLoginIp1() {
		return loginIp1;
	}
	public void setLoginIp1(String loginIp1) {
		this.loginIp1 = loginIp1;
	}
	public String getLoginIp2() {
		return loginIp2;
	}
	public void setLoginIp2(String loginIp2) {
		this.loginIp2 = loginIp2;
	}
	public String getLoginIp3() {
		return loginIp3;
	}
	public void setLoginIp3(String loginIp3) {
		this.loginIp3 = loginIp3;
	}
	public String getLoginIp4() {
		return loginIp4;
	}
	public void setLoginIp4(String loginIp4) {
		this.loginIp4 = loginIp4;
	}
	public String getLoginIp5() {
		return loginIp5;
	}
	public void setLoginIp5(String loginIp5) {
		this.loginIp5 = loginIp5;
	}
	public String getLoginIp6() {
		return loginIp6;
	}
	public void setLoginIp6(String loginIp6) {
		this.loginIp6 = loginIp6;
	}
	public String getLoginIp7() {
		return loginIp7;
	}
	public void setLoginIp7(String loginIp7) {
		this.loginIp7 = loginIp7;
	}
	public String getLoginIp8() {
		return loginIp8;
	}
	public void setLoginIp8(String loginIp8) {
		this.loginIp8 = loginIp8;
	}
	public String getLoginIp9() {
		return loginIp9;
	}
	public void setLoginIp9(String loginIp9) {
		this.loginIp9 = loginIp9;
	}
	public String getLoginIp10() {
		return loginIp10;
	}
	public void setLoginIp10(String loginIp10) {
		this.loginIp10 = loginIp10;
	}
	public String getLoginIp11() {
		return loginIp11;
	}
	public void setLoginIp11(String loginIp11) {
		this.loginIp11 = loginIp11;
	}
	public String getLoginIp12() {
		return loginIp12;
	}
	public void setLoginIp12(String loginIp12) {
		this.loginIp12 = loginIp12;
	}
	public String getLoginIp13() {
		return loginIp13;
	}
	public void setLoginIp13(String loginIp13) {
		this.loginIp13 = loginIp13;
	}
	public String getLoginIp14() {
		return loginIp14;
	}
	public void setLoginIp14(String loginIp14) {
		this.loginIp14 = loginIp14;
	}
	public String getLoginIp15() {
		return loginIp15;
	}
	public void setLoginIp15(String loginIp15) {
		this.loginIp15 = loginIp15;
	}
	public String getLoginIp16() {
		return loginIp16;
	}
	public void setLoginIp16(String loginIp16) {
		this.loginIp16 = loginIp16;
	}
	public String getLoginIp17() {
		return loginIp17;
	}
	public void setLoginIp17(String loginIp17) {
		this.loginIp17 = loginIp17;
	}
	public String getLoginIp18() {
		return loginIp18;
	}
	public void setLoginIp18(String loginIp18) {
		this.loginIp18 = loginIp18;
	}
	public String getLoginIp19() {
		return loginIp19;
	}
	public void setLoginIp19(String loginIp19) {
		this.loginIp19 = loginIp19;
	}
	public String getLoginIp20() {
		return loginIp20;
	}
	public void setLoginIp20(String loginIp20) {
		this.loginIp20 = loginIp20;
	}
	public String getLoginIp21() {
		return loginIp21;
	}
	public void setLoginIp21(String loginIp21) {
		this.loginIp21 = loginIp21;
	}
	public String getLoginIp22() {
		return loginIp22;
	}
	public void setLoginIp22(String loginIp22) {
		this.loginIp22 = loginIp22;
	}
	public String getLoginIp23() {
		return loginIp23;
	}
	public void setLoginIp23(String loginIp23) {
		this.loginIp23 = loginIp23;
	}
	public String getLoginIp24() {
		return loginIp24;
	}
	public void setLoginIp24(String loginIp24) {
		this.loginIp24 = loginIp24;
	}
	public String getLoginIp25() {
		return loginIp25;
	}
	public void setLoginIp25(String loginIp25) {
		this.loginIp25 = loginIp25;
	}
	public String getLoginIp26() {
		return loginIp26;
	}
	public void setLoginIp26(String loginIp26) {
		this.loginIp26 = loginIp26;
	}
	public String getLoginIp27() {
		return loginIp27;
	}
	public void setLoginIp27(String loginIp27) {
		this.loginIp27 = loginIp27;
	}
	public String getLoginIp28() {
		return loginIp28;
	}
	public void setLoginIp28(String loginIp28) {
		this.loginIp28 = loginIp28;
	}
	public String getLoginIp29() {
		return loginIp29;
	}
	public void setLoginIp29(String loginIp29) {
		this.loginIp29 = loginIp29;
	}
	public String getLoginIp30() {
		return loginIp30;
	}
	public void setLoginIp30(String loginIp30) {
		this.loginIp30 = loginIp30;
	}
	@Override
	public String toString() {
		return "EmailUser [id=" + id + ", roleNo=" + roleNo + ", userName="
				+ userName + ", pwd=" + pwd + ", trueName=" + trueName
				+ ", job=" + job + ", emailAddress=" + emailAddress
				+ ", birthday=" + birthday + ", worktime=" + worktime
				+ ", englishLVL=" + englishLVL + ", phone=" + phone
				+ ", liveAddress=" + liveAddress + ", workmonth=" + workmonth
				+ ", skapeemail=" + skapeemail + ", Msn=" + Msn
				+ ", CompanyPhone=" + CompanyPhone + ", Mobile=" + Mobile
				+ ", emailPWD=" + emailPWD + ", flag=" + flag + ", dimission="
				+ dimission + ", registDate=" + registDate + ", loginIp="
				+ loginIp + ", wrongNumber=" + wrongNumber + ", wrongTime="
				+ wrongTime + ", wrongIp=" + wrongIp + ", qualification="
				+ qualification + ", loginIp1=" + loginIp1 + ", loginIp2="
				+ loginIp2 + ", loginIp3=" + loginIp3 + ", loginIp4="
				+ loginIp4 + ", loginIp5=" + loginIp5 + ", loginIp6="
				+ loginIp6 + ", loginIp7=" + loginIp7 + ", loginIp8="
				+ loginIp8 + ", loginIp9=" + loginIp9 + ", loginIp10="
				+ loginIp10 + ", loginIp11=" + loginIp11 + ", loginIp12="
				+ loginIp12 + ", loginIp13=" + loginIp13 + ", loginIp14="
				+ loginIp14 + ", loginIp15=" + loginIp15 + ", loginIp16="
				+ loginIp16 + ", loginIp17=" + loginIp17 + ", loginIp18="
				+ loginIp18 + ", loginIp19=" + loginIp19 + ", loginIp20="
				+ loginIp20 + ", loginIp21=" + loginIp21 + ", loginIp22="
				+ loginIp22 + ", loginIp23=" + loginIp23 + ", loginIp24="
				+ loginIp24 + ", loginIp25=" + loginIp25 + ", loginIp26="
				+ loginIp26 + ", loginIp27=" + loginIp27 + ", loginIp28="
				+ loginIp28 + ", loginIp29=" + loginIp29 + ", loginIp30="
				+ loginIp30 + ", emailAddress1=" + emailAddress1 + "]";
	}
	
	
	
	
	
	
	
	
}
