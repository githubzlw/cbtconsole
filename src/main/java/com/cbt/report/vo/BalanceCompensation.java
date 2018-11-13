package com.cbt.report.vo;

/**
 * 客户余额补偿详情
 * 
 * @author JXW
 *
 */
public class BalanceCompensation {
	private String year;// 年份
	private String month;// 月份
	private int userId;// 客户id
	private double amount;// 补偿金额

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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "BalanceCompensation [year=" + year + ", month=" + month + ", userId=" + userId + ", amount=" + amount
				+ "]";
	}

}
