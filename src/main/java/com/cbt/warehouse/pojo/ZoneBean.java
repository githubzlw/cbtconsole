package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class ZoneBean implements Serializable {
	
	private static final long serialVersionUID = 4500299423924684710L;
	
	private int id;
	private String country;
	private int zone;
	private String fedexie;
	private String area;
	private int isFree;//是否存在免邮价格
	private String chinapostbig;//国家名称中文
	public String getChinapostbig() {
		return chinapostbig;
	}
	public void setChinapostbig(String chinapostbig) {
		this.chinapostbig = chinapostbig;
	}
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
	public String getFedexie() {
		return fedexie;
	}
	public void setFedexie(String fedexie) {
		this.fedexie = fedexie;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getIsFree() {
		return isFree;
	}
	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}
	@Override
	public String toString() {
		return String.format("{\"id\":\"%s\",\"country\":\"%s\", \"zone\":\"%s\", \"isFree\":%d}",id, country,
				zone, isFree);
	}
	
}
