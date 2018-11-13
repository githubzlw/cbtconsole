package com.cbt.bean;

public class UserGradeBean {
	private int gid;//名称
	private String grade;//名称
	
	private double discount;
	private int valid ;//数据有效性
	private String createtime;//更新时间
	
	
	
	public UserGradeBean() {
		super();
	}
	public UserGradeBean(int gid, String grade) {
		super();
		this.gid = gid;
		this.grade = grade;
	}
	
	
	public UserGradeBean(int gid, String grade, double discount) {
		super();
		this.gid = gid;
		this.grade = grade;
		this.discount = discount;
	}
	public double getDiscount() {
		return discount;
	}
	
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	

}
