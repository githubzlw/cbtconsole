package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @ClassName GoodsCommentsBean
 * @Description 商品评论bean
 * @author Administrator
 * @date 2018年1月20日
 */
public class GoodsCommentsBean implements Serializable {

	private static final long serialVersionUID = 60957798915263L;

	private Integer id;
	private Integer countryId;// 国家id
	private String orderNo;// 订单号
	private Integer userId;// 用户ID
	private String userName;// 用户名
	private String goodsPid;// 产品pid
	private Integer goodsSource;// 产品来源 0:无 1:1688 2:阿里 3:新品云
	private String commentsContent;// 评论内容
	private Timestamp commentsTime;// 评论时间
	private Integer adminId;// 是否由后台销售评论的，0不是;>0是销售id
	private int showFlag;// 显示标志（0不显示 1显示）
	
	private Integer orderDetailId;//订单详情的id
	private String carType;	//商品的规格



	private String picPath;//评论图片   王宏杰 2018-06-26
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGoodsPid() {
		return goodsPid;
	}
	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}
	public Integer getGoodsSource() {
		return goodsSource;
	}
	public void setGoodsSource(Integer goodsSource) {
		this.goodsSource = goodsSource;
	}
	public String getCommentsContent() {
		return commentsContent;
	}
	public void setCommentsContent(String commentsContent) {
		this.commentsContent = commentsContent;
	}
	public Timestamp getCommentsTime() {
		return commentsTime;
	}
	public void setCommentsTime(Timestamp commentsTime) {
		this.commentsTime = commentsTime;
	}
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public int getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(int showFlag) {
		this.showFlag = showFlag;
	}

	

}
