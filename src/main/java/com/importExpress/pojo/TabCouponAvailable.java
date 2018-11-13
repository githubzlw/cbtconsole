package com.importExpress.pojo;

import java.util.Date;

public class TabCouponAvailable {

	private Long id;

	private String couponCode;

	// 券的规则id
	private Long rulesId;

	// 券的所属分类码；目前分为三种：折扣券；代金券；满减券
	private String typeCode;

	// 优惠券的数量，0-该券不可用，因为可领取的数量为0
	private Long couponNumber;

	// 0-无可用金额；折扣上限；优惠金额
	private Long couponAmount;

	// 折扣率 使用 百分比乘以100，0 /1 都无折扣。默认为0
	private Long couponDiscount;

	// 可用截止时间
	private Date availableTime;

	// 领取截止时间
	private Date receiveTime;

	// 创建时间
	private Date createTime;

	// 0-系统分发；其他为运营人员id编号，表示此优惠券的分发人
	private long createAdmid = 0;
	
	private String createAdmidStr;
	
	public String getCreateAdmidStr() {
		return createAdmidStr;
	}

	public void setCreateAdmidStr(String createAdmidStr) {
		this.createAdmidStr = createAdmidStr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Long getRulesId() {
		return rulesId;
	}

	public void setRulesId(Long rulesId) {
		this.rulesId = rulesId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Long getCouponNumber() {
		return couponNumber;
	}

	public void setCouponNumber(Long couponNumber) {
		this.couponNumber = couponNumber;
	}

	public Long getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Long couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Long getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(Long couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public Date getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(Date availableTime) {
		this.availableTime = availableTime;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateAdmid() {
		return createAdmid;
	}

	public void setCreateAdmid(long createAdmid) {
		this.createAdmid = createAdmid;
	}

	@Override
	public String toString() {
		return "TabCouponAvailable [id=" + id + ", couponCode=" + couponCode + ", rulesId=" + rulesId + ", typeCode="
				+ typeCode + ", couponNumber=" + couponNumber + ", couponAmount=" + couponAmount + ", couponDiscount="
				+ couponDiscount + ", availableTime=" + availableTime + ", receiveTime=" + receiveTime
				+ ", createTime=" + createTime + ", createAdmid=" + createAdmid + ", createAdmidStr=" + createAdmidStr
				+ "]";
	}

}