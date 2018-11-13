package com.cbt.parse.bean;

import java.sql.Date;

/**搜索词跳转二级页面
 * @author abc
 *
 */
public class KeysPageBean {
	private int id;
	private String rectName;
	private String pageName;
	private String pageUrl;
	private String keyWords;
	private String valid;
	private Date time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRectName() {
		return rectName;
	}
	public void setRectName(String rectName) {
		this.rectName = rectName;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	
	

}
