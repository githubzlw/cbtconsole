package com.cbt.report.vo;

import java.sql.Timestamp;

/**
 * 
 * @ClassName EditedProductProfits
 * @Description 人工编辑产品利润bean
 * @author JXW
 * @date 2017年12月18日 上午9:51:47
 */
public class EditedProductProfits {
	private String year;// 年份
	private String month;// 月份
	private Timestamp createTime;// 订单创建时间
	private String pid;// 商品pid
	private int userId;// 客户id
	private String orderNo;// 商品所属订单号
	private int adminId;// 商品编辑人id
	private String adminName;// 商品编辑人名称
	private float orderPrice;// 商品下单金额
	private float purchasePrice;// 商品采购金额
	private float intenetFreight;// 商品国际运费
	private float grossProfit;// 商品毛利润

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

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

	public float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public float getIntenetFreight() {
		return intenetFreight;
	}

	public void setIntenetFreight(float intenetFreight) {
		this.intenetFreight = intenetFreight;
	}

	public float getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(float grossProfit) {
		this.grossProfit = grossProfit;
	}

	@Override
	public String toString() {
		return "EditedProductProfits [year=" + year + ", month=" + month + ", createTime=" + createTime + ", pid=" + pid
				+ ", userId=" + userId + ", orderNo=" + orderNo + ", adminId=" + adminId + ", adminName=" + adminName
				+ ", orderPrice=" + orderPrice + ", purchasePrice=" + purchasePrice + ", intenetFreight="
				+ intenetFreight + ", grossProfit=" + grossProfit + "]";
	}

}
