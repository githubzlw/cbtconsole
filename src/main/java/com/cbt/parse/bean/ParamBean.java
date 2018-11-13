package com.cbt.parse.bean;

public class ParamBean {
	private String valid;//valid
	private int amount;//总页数
	private int current;//当前页数
	private String keyword;//关键词
	private String minq;//最小订量
	private String maxq;//最大订量
	private String sort;//排序 
	private String minprice;//最小价格
	private String maxprice;//最大价格
	private String catid;//类别
	private String pvid;//类型id
	private String website;//网站来源
	private String sid;//商店id
	private String com;//接口入口
	private String goodId;//
	/**
	 * @return the goodId
	 */
	public String getGoodId() {
		return goodId;
	}
	/**
	 * @param goodId the goodId to set
	 */
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMinq() {
		return minq;
	}
	public void setMinq(String minq) {
		this.minq = minq;
	}
	public String getMaxq() {
		return maxq;
	}
	public void setMaxq(String maxq) {
		this.maxq = maxq;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getMinprice() {
		return minprice;
	}
	public void setMinprice(String minprice) {
		this.minprice = minprice;
	}
	public String getMaxprice() {
		return maxprice;
	}
	public void setMaxprice(String maxprice) {
		this.maxprice = maxprice;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getPvid() {
		return pvid;
	}
	public void setPvid(String pvid) {
		this.pvid = pvid;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}

}
