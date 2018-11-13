package com.cbt.bean;

import java.io.Serializable;

public class OrderChange implements Serializable {
	private static final long serialVersionUID = 998910089034000945L;
	private int ropType;
	private String oldValue;
	private String newValue;
	private int del_state;
	private String goodId;
	private String dateline;
	public int getRopType() {
		return ropType;
	}

	public void setRopType(int ropType) {
		this.ropType = ropType;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public int getDel_state() {
		return del_state;
	}

	public void setDel_state(int del_state) {
		this.del_state = del_state;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

}
