package com.cbt.bean;

public class StateName {
	private String statecode;
	private String statename;
	public String getStatecode() {
		return statecode;
	}
	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}
	@Override
	public String toString() {
		return String.format("{\"statecode\":\"%s\", \"statename\":\"%s\"}",
				statecode, statename);
	}
	
}
