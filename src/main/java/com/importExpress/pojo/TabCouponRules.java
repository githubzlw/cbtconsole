package com.importExpress.pojo;

public class TabCouponRules {

	private Long id;

	// 最低消费金额；0-无最低消费金额限制
	private Long minAmount;

	// 适用的产品类别id,最多3个分类 0 不限制
	private String category;

	// 0-不限制；领用之后，限制几天内使用
	private Long availableTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Long minAmount) {
		this.minAmount = minAmount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(Long availableTime) {
		this.availableTime = availableTime;
	}

	@Override
	public String toString() {
		return "tab_coupon_rules [id=" + id + ", minAmount=" + minAmount + ", category=" + category
				+ ", availableTime=" + availableTime + "]";
	}

}