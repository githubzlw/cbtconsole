package com.cbt.parse.bean;

/**对应cn_en.sql
 * @author abc
 *
 */
public class CnEnDaoBean {
	private int id;
	private String cntext;//中文
	private String entext;//英文
	private int cnlength;//中文长度
	private int sport;//排序
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCntext() {
		return cntext;
	}
	public void setCntext(String cntext) {
		this.cntext = cntext;
	}
	public String getEntext() {
		return entext;
	}
	public void setEntext(String entext) {
		this.entext = entext;
	}
	public int getCnlength() {
		return cnlength;
	}
	public void setCnlength(int cnlength) {
		this.cnlength = cnlength;
	}
	public int getSport() {
		return sport;
	}
	public void setSport(int sport) {
		this.sport = sport;
	}
	
	

}
