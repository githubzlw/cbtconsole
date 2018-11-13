package com.cbt.report.vo;

/**
 * 客户余额变更
 * 
 * @author JXW
 *
 */
public class CustomerBalanceChangeBean {
	private String year;// 年份
	private String month;// 月份
	private int userId;// 客户id
	private float changeBalance;// 变更余额=月末余额-月初余额
	private float currentBalance;// 当前余额

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public float getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(float currentBalance) {
		this.currentBalance = currentBalance;
	}

	public float getChangeBalance() {
		return changeBalance;
	}

	public void setChangeBalance(float changeBalance) {
		this.changeBalance = changeBalance;
	}

	@Override
	public String toString() {
		return "CustomerBalanceChangeBean [year=" + year + ", month=" + month + ", userId=" + userId
				+ ", changeBalance=" + changeBalance + ", currentBalance=" + currentBalance + "]";
	}

}
