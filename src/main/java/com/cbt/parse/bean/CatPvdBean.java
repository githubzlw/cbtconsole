package com.cbt.parse.bean;

/**ali_catpvd.sql表
 * @author abc
 *
 */
public class CatPvdBean {
	private String keyword;//关键词
	private String catid;//类别id
	private String catidlist;//类别id的list
	private String pvidlist;//规格id的list
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getCatidlist() {
		return catidlist;
	}
	public void setCatidlist(String catidlist) {
		this.catidlist = catidlist;
	}
	public String getPvidlist() {
		return pvidlist;
	}
	public void setPvidlist(String pvidlist) {
		this.pvidlist = pvidlist;
	}
	
	

}
