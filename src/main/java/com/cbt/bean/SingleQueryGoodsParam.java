package com.cbt.bean;

public class SingleQueryGoodsParam {
	private String pid;
	private String sttime;
	private String edtime;
	private int admid;
	private int state;
	private int startNum;
	private int limitNum;
	private int drainageFlag;
	private int goodsType;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSttime() {
		return sttime;
	}

	public void setSttime(String sttime) {
		this.sttime = sttime;
	}

	public String getEdtime() {
		return edtime;
	}

	public void setEdtime(String edtime) {
		this.edtime = edtime;
	}

	public int getAdmid() {
		return admid;
	}

	public void setAdmid(int admid) {
		this.admid = admid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	public int getDrainageFlag() {
		return drainageFlag;
	}

	public void setDrainageFlag(int drainageFlag) {
		this.drainageFlag = drainageFlag;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
}
