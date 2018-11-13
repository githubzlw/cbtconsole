package com.cbt.website.bean;

/**
 * 用户等级
 * @author Administrator
 *
 */
public class GradeDiscount {
	  private int id;
	  private int gid;
	  private String gname;
  public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
}
