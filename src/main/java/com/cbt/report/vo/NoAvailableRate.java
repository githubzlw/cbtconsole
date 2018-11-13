package com.cbt.report.vo;

/**
 * 无货率统计bean
 * 
 * @author JiangXianwei
 *
 */
public class NoAvailableRate {

	private int order;// 排序
	private String dateStr;// 日期
	private float rate;// 比率

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "NoAvailableRate [order=" + order + ", dateStr=" + dateStr + ", rate=" + rate + "]";
	}

}
