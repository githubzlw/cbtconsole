package com.cbt.website.bean;

/**
 * @author ylm
 * AliExpress 搜索结果
 */
public class AliExpressTop240Bean {
	private int total;
	private int id;
	private int results_typeid;
	private String keyword;
	private String typeid;
	private String sort;
	private String aliexpress_url;//原网站链接
	private String gimgurl;//商品原图片链接
	private String img;//商品显示图片
	private String gname;
	private String price;
	private String gfree;
	private String minOrder;
	private int sales;

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getAliexpress_url() {
		return aliexpress_url;
	}
	public void setAliexpress_url(String aliexpress_url) {
		this.aliexpress_url = aliexpress_url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	public void setGfree(String gfree) {
		this.gfree = gfree;
	}
	public String getGfree() {
		return gfree;
	}
	public String getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
	public void setGimgurl(String gimgurl) {
		this.gimgurl = gimgurl;
	}
	public String getGimgurl() {
		return gimgurl;
	}
	public void setResults_typeid(int results_typeid) {
		this.results_typeid = results_typeid;
	}
	public int getResults_typeid() {
		return results_typeid;
	}
}
