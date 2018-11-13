package com.cbt.share.pojo;

public class SharePojo {
	private String id;
	private Integer ip1; //起始段
	private Integer ip2; //结束段
	private String addr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIp1() {
		return ip1;
	}
	public void setIp1(Integer ip1) {
		this.ip1 = ip1;
	}
	public Integer getIp2() {
		return ip2;
	}
	public void setIp2(Integer ip2) {
		this.ip2 = ip2;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
}
