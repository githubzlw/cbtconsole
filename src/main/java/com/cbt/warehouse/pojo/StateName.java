package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class StateName implements Serializable {
	
	private static final long serialVersionUID = -4637991872102824604L;
	
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
