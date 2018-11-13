package com.cbt.parse.bean;

public class ProxyAddress {
	public static int count = 0;
//	public static int count = 606;
	public static int active = 0;
	private String id;
	private String ip;
	private String port;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public ProxyAddress(String id, String ip, String port) {
		super();
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

}
