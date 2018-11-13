package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable {

	private static final long serialVersionUID = -358458436351860319L;
	
	private int id;
	private String sessionId;
	private String email;
	private String name;
	private String pass;
	private String token;
	private String sequence;
	//激活码
	private String activationCode;
	//����״激活装填
	private int activationState;
	//头像
	private String picture;
	private Date createtime;
	private Date activationTime;
	private Date activationPassTime;
	private String activationPassCode;
	private String currency;//货币类型
	private int logReg;//登录还是注册
	private String count;//购物车商品数
	private double applicable_credit;
	private double availableM;//用户余额
	private String businessName;
	private int countryId;
	private String countryName;
	//用户标识  1表示该用户为商家   2 表示为个人  0 默认 ，4 新的个人标识：2018年4月20日
	private String userCategory;
	private String area;
	private int isTogether;//登录是否存在合并购物车
	//api key
	private String signkey;

	/**
	 * login 错误信息
	 */
	private String errorMessage;

	/**
	 * 跳转的页面
	 */
	private String jumpUrl;
	/**
	 * 用户记录信息
	 */
	private String info;
	private String dropshipping1;
	private String proPurl;
	private String purl;
	private String uid;
	private String gbid;
	private String pre;
	private String rfq;
	private String custype;
	private String bfm;
	private String searchUrl;
	private String redirect;
	private String pc_flag;
	private String bind;
	private String type;
	private String google_email;
	/**
	 * 标识 如果是购物车页面跳转到登陆页面则显示contione to check out
	 */
	private int mark;

	/**
	 * 0：老用户默认0，1:快速注册未激活，2：快速注册激活
	 */
	private int isfastregistered;
	/**
	 * 商业信息
	 */
	private String businessIntroduction;

	public int getIsfastregistered() {
		return isfastregistered;
	}

	public void setIsfastregistered(int isfastregistered) {
		this.isfastregistered = isfastregistered;
	}

	public String getBusinessIntroduction() {
		return businessIntroduction;
	}

	public void setBusinessIntroduction(String businessIntroduction) {
		this.businessIntroduction = businessIntroduction;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getPc_flag() {
		return pc_flag;
	}

	public void setPc_flag(String pc_flag) {
		this.pc_flag = pc_flag;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGoogle_email() {
		return google_email;
	}

	public void setGoogle_email(String google_email) {
		this.google_email = google_email;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDropshipping1() {
		return dropshipping1;
	}

	public void setDropshipping1(String dropshipping1) {
		this.dropshipping1 = dropshipping1;
	}

	public String getProPurl() {
		return proPurl;
	}

	public void setProPurl(String proPurl) {
		this.proPurl = proPurl;
	}

	public String getPurl() {
		return purl;
	}

	public void setPurl(String purl) {
		this.purl = purl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGbid() {
		return gbid;
	}

	public void setGbid(String gbid) {
		this.gbid = gbid;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public String getRfq() {
		return rfq;
	}

	public void setRfq(String rfq) {
		this.rfq = rfq;
	}

	public String getCustype() {
		return custype;
	}

	public void setCustype(String custype) {
		this.custype = custype;
	}

	public String getBfm() {
		return bfm;
	}

	public void setBfm(String bfm) {
		this.bfm = bfm;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public double getAvailableM() {
		return availableM;
	}
	public void setAvailableM(double availableM) {
		this.availableM = availableM;
	}
	
	private double cashBack;
	private double firstDiscount;

	public double getFirstDiscount() {
		return firstDiscount;
	}

	public void setFirstDiscount(double firstDiscount) {
		this.firstDiscount = firstDiscount;
	}

	public double getCashBack() {
		return cashBack;
	}
	public void setCashBack(double cashBack) {
		this.cashBack = cashBack;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
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
	public void setArea(String area) {
		this.area = area;
	}
	public String getArea() {
		return area;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public int getIsTogether() {
		return isTogether;
	}
	public void setIsTogether(int isTogether) {
		this.isTogether = isTogether;
	}
	public String getSignkey() {
		return signkey;
	}
	public void setSignkey(String signkey) {
		this.signkey = signkey;
	}

	@Override
	public String toString() {
		return "UserBean{" + "id=" + id + ", sessionId='" + sessionId + '\'' + ", email='" + email + '\'' + ", name='" + name + '\'' + ", pass='" + pass + '\'' + ", token='" + token + '\'' + ", sequence='" + sequence + '\'' + ", activationCode='" + activationCode + '\'' + ", activationState=" + activationState + ", picture='" + picture + '\'' + ", createtime=" + createtime + ", activationTime=" + activationTime + ", activationPassTime=" + activationPassTime + ", activationPassCode='" + activationPassCode + '\'' + ", currency='" + currency + '\'' + ", logReg=" + logReg + ", count='" + count + '\'' + ", applicable_credit=" + applicable_credit + ", availableM=" + availableM + ", businessName='" + businessName + '\'' + ", countryId=" + countryId + ", countryName='" + countryName + '\'' + ", userCategory='" + userCategory + '\'' + ", area='" + area + '\'' + ", isTogether=" + isTogether + ", signkey='" + signkey + '\'' + ", errorMessage='" + errorMessage + '\'' + ", jumpUrl='" + jumpUrl + '\'' + ", info='" + info + '\'' + ", dropshipping1='" + dropshipping1 + '\'' + ", proPurl='" + proPurl + '\'' + ", purl='" + purl + '\'' + ", uid='" + uid + '\'' + ", gbid='" + gbid + '\'' + ", pre='" + pre + '\'' + ", rfq='" + rfq + '\'' + ", custype='" + custype + '\'' + ", bfm='" + bfm + '\'' + ", searchUrl='" + searchUrl + '\'' + ", redirect='" + redirect + '\'' + ", pc_flag='" + pc_flag + '\'' + ", bind='" + bind + '\'' + ", type='" + type + '\'' + ", google_email='" + google_email + '\'' + ", mark=" + mark + ", isfastregistered=" + isfastregistered + ", businessIntroduction='" + businessIntroduction + '\'' + ", cashBack=" + cashBack + ", firstDiscount=" + firstDiscount + '}';
	}
}
