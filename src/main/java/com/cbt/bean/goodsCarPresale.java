package com.cbt.bean;

import java.math.BigDecimal;
import java.util.Date;

public class goodsCarPresale {
	
	private int id;
	
	/**
	 * 用户id
	 */
	private int userId;
	
	/**
	 * 购物车id
	 */
	private String goodsCarId;
	
	/**
	 * 购物车原始总价：工厂价+运费
	 */
	private BigDecimal originalPrice;
	
	/**
	 * 成本价：货源成本价+运费
	 */
	private BigDecimal costPrice;
	
	/**
	 * 优惠价
	 */
	private BigDecimal discountPrice;
	
	private Date createTime;

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

	public String getGoodsCarId() {
		return goodsCarId;
	}

	public void setGoodsCarId(String goodsCarId) {
		this.goodsCarId = goodsCarId;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
