package com.importExpress.pojo;

import java.util.Date;

public class TabCouponUser {

	private Long id;

	private Long userid;

	// table_coupon_available行id
	private Long couponId;

	// 用户领取时间
	private Date createtime;

	// 1-用户可用此券；0-已失效；2 使用
	private Integer couponState = 1;

	// 劵领取的源URL
	private String getPUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getCouponState() {
		return couponState;
	}

	public void setCouponState(Integer couponState) {
		this.couponState = couponState;
	}

	public String getGetPUrl() {
		return getPUrl;
	}

	public void setGetPUrl(String getPUrl) {
		this.getPUrl = getPUrl;
	}

	@Override
	public String toString() {
		return "TabCouponUser [id=" + id + ", userid=" + userid + ", couponId=" + couponId + ", createtime="
				+ createtime + ", couponState=" + couponState + ", getPUrl=" + getPUrl + "]";
	}

}