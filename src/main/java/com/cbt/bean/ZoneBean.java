package com.cbt.bean;

public class ZoneBean {
	private int id;
	private String country;
	private int zone;
	private String area;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getZone() {
		return zone;
	}
	public void setZone(int zone) {
		this.zone = zone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	@Override
	public String toString() {
		return String.format("{\"id\":\"%s\",\"country\":\"%s\", \"zone\":\"%s\", \"area\":\"%s\"}",id, country,
				zone,area);
	}
	
}
