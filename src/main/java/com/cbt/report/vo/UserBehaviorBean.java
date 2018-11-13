package com.cbt.report.vo;

/**
 * 
 * @ClassName UserBehaviorBean
 * @Description 用户行为bean
 * @author Jxw
 * @date 2018年5月9日
 * @version 1.0.0
 */
public class UserBehaviorBean {
	private String typeDesc;
	private int statisticsNum;
	private int typeFlag;
	private int isShow = 1;
	private String recordDate;
	private int isExport = 1;

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public int getStatisticsNum() {
		return statisticsNum;
	}

	public void setStatisticsNum(int statisticsNum) {
		this.statisticsNum = statisticsNum;
	}

	public int getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(int typeFlag) {
		this.typeFlag = typeFlag;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public int getIsExport() {
		return isExport;
	}

	public void setIsExport(int isExport) {
		this.isExport = isExport;
	}

	@Override
	public String toString() {
		return "{\"typeDesc\":\"" + typeDesc + "\", \"statisticsNum\":\"" + statisticsNum + "\", \"typeFlag\":\""
				+ typeFlag + "\", \"isShow\":\"" + isShow + "\", \"recordDate\":\"" + recordDate + "\", \"isExport\":\""
				+ isExport + "\"}";
	}

}
