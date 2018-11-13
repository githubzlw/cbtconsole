package com.cbt.bean;

public class CustomRecord {
	private String goodsPid;//产品id
	private String admin;//操作人
	private String updateTime;//操作时间
	private String goodsState;//状态
	private String record;//备注
	
	
	private int count;
	
	
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getGoodsPid() {
		return goodsPid;
	}
	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getGoodsState() {
		return goodsState;
	}
	public void setGoodsState(String goodsState) {
		this.goodsState = goodsState;
	}
	
	
	

}
