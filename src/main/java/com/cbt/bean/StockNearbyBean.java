package com.cbt.bean;

import java.util.Date;

/**
 * StockNearbyBean
 */
public class StockNearbyBean {
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
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 订单频率
	 */
	private String orderFrequency;
	
	/**
	 * 每次订量
	 */
	private String orderQuantity;
	
	/**
	 * 公司名
	 */
	private String companyName;
	
	
	/**
	 * 年销售
	 */
	private int annualTurnover;
	
	/**
	 * 时间
	 */
	private Date createTime;
	
	/**
	 * 商品url
	 */
	private String purl;
	
	/**
	 * 商品名
	 */
	private String pname;
	
	/**
	 * 商品价格
	 */
	private String fprice;
	
	/**
	 * 商品最小订量
	 */
	private String minOrder;
	
	/**
	 * 商品货币
	 */
	private String currency;
	
	/**
	 * 图片
	 */
	private String img;
	
	
	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}
	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * @return the purl
	 */
	public String getPurl() {
		return purl;
	}
	/**
	 * @param purl the purl to set
	 */
	public void setPurl(String purl) {
		this.purl = purl;
	}
	/**
	 * @return the pname
	 */
	public String getPname() {
		return pname;
	}
	/**
	 * @param pname the pname to set
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}
	/**
	 * @return the fprice
	 */
	public String getFprice() {
		return fprice;
	}
	/**
	 * @param fprice the fprice to set
	 */
	public void setFprice(String fprice) {
		this.fprice = fprice;
	}
	/**
	 * @return the minOrder
	 */
	public String getMinOrder() {
		return minOrder;
	}
	/**
	 * @param minOrder the minOrder to set
	 */
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the orderFrequency
	 */
	public String getOrderFrequency() {
		return orderFrequency;
	}
	/**
	 * @param orderFrequency the orderFrequency to set
	 */
	public void setOrderFrequency(String orderFrequency) {
		this.orderFrequency = orderFrequency;
	}
	/**
	 * @return the orderQuantity
	 */
	public String getOrderQuantity() {
		return orderQuantity;
	}
	/**
	 * @param orderQuantity the orderQuantity to set
	 */
	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the annualTurnover
	 */
	public int getAnnualTurnover() {
		return annualTurnover;
	}
	/**
	 * @param annualTurnover the annualTurnover to set
	 */
	public void setAnnualTurnover(int annualTurnover) {
		this.annualTurnover = annualTurnover;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
