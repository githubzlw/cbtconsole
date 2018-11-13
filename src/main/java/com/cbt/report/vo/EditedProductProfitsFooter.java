package com.cbt.report.vo;

public class EditedProductProfitsFooter {
	private String adminName = "<b style='color:red;'>总毛利润：</b>";
	private float orderPrice;

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

}
