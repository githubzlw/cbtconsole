package com.cbt.bean;

import java.util.Date;
import java.util.List;

/**
 * 用户留言
 */
public class GuestBookBean {
	private int id;
	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 用户名字
	 */
	private String userName;
	/**
	 * 商品id
	 */
	private String pid;
	/**
	 * 商品链接地址
	 */
	private String purl;
	/**
	 * 图片链接地址
	 */
	private String pimg;
	/**
	 * 留言内容
	 */
	private String content;

	/**
	 * 留言时间
	 */
	private Date createTime;
	/**
	 * 回复状态
	 */
	private int status;
	/**
	 * 商品名称
	 */
	private String pname;
	/**
	 * 商品价格
	 */
	private String price;

	/**
	 * 回复内容
	 */
	private String replyContent;

	/**
	 * 回复时间
	 */
	private Date replyTime;
	/**
	 * 一个留言问题会有多个回复
	 */
	private List<Reply> reps;
	private String email;

	/**
	 * 仅仅为了显示页面时间
	 */
	private String showTime;

	private String admname;//负责人
	private String bname;//公司名字
	private int eid;//回复邮件id
	


	private String onlineUrl;// 线上商品链接

	//------------------------
	/**
	 * 客户信息留言	 
	 **/
	private  String orderQuantity ;  //订单数量
	private  String targetPrice;     //目标价格
	private  String customizationNeed;  //定制需求
	private  int  questionType;   //留言类型
	private String userInfos;
	private String statusinfo;
	private String sale_email;//客户对应的销售邮箱
	private String picPath;
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getSale_email() {
		return sale_email;
	}
	public void setSale_email(String sale_email) {
		this.sale_email = sale_email;
	}
	public String getStatusinfo() {
		return statusinfo;
	}
	public void setStatusinfo(String statusinfo) {
		this.statusinfo = statusinfo;
	}
	public String getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(String userInfos) {
		this.userInfos = userInfos;
	}
	public String getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getTargetPrice() {
		return targetPrice;
	}
	public void setTargetPrice(String targetPrice) {
		this.targetPrice = targetPrice;
	}
	public String getCustomizationNeed() {
		return customizationNeed;
	}
	public void setCustomizationNeed(String customizationNeed) {
		this.customizationNeed = customizationNeed;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPurl() {
		return purl;
	}

	public void setPurl(String purl) {
		this.purl = purl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Reply> getReps() {
		return reps;
	}

	public void setReps(List<Reply> reps) {
		this.reps = reps;
	}

	public String getPimg() {
		return pimg;
	}

	public void setPimg(String pimg) {
		this.pimg = pimg;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdmname() {
		return admname;
	}

	public void setAdmname(String admname) {
		this.admname = admname;
	}

	public String getOnlineUrl() {
		return onlineUrl;
	}

	public void setOnlineUrl(String onlineUrl) {
		this.onlineUrl = onlineUrl;
	}

	@Override
	public String toString() {

		return "GuestBookBean [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", pid=" + pid + ", purl=" + purl + ", pimg="
				+ pimg + ", content=" + content + ", createTime=" + createTime
				+ ", status=" + status + ", pname=" + pname + ", price="
				+ price + ", replyContent=" + replyContent + ", replyTime="
				+ replyTime + ", reps=" + reps + ", email=" + email
				+ ", showTime=" + showTime + ", admname=" + admname
				+ ", bname=" + bname + ", eid=" + eid + "]";

		

	}

	
}
