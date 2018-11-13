package com.cbt.website.bean;
/**
 * 用来记录客户访问网站的轨迹行为                
 * wanyang
 * 		 
 */
public class UserBehavior {
	private int id;
	private int userid;
	private String keywords;
	private String view_url;
	private String action;
	private String view_date_day;
	private String view_date_time;
	private int total;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getView_url() {
		return view_url;
	}
	public void setView_url(String view_url) {
		this.view_url = view_url;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getView_date_day() {
		return view_date_day;
	}
	public void setView_date_day(String view_date_day) {
		this.view_date_day = view_date_day;
	}
	public String getView_date_time() {
		return view_date_time;
	}
	public void setView_date_time(String view_date_time) {
		this.view_date_time = view_date_time;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
