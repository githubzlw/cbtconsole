package com.importExpress.pojo;

import java.util.List;
import java.util.Map;

public class TabSeachPagesDetailBean {
	private Integer id;
	/**
	 * tab_seach_pages.id
	 */
	private Integer sid;
	
	/**
	 * 主商品名
	 */
	private String name;
	/**
	 * 搜索URL
	 */
	private String seachUrl;
	/**
	 * 
	 */
	private String keyword;
	/**
	 * 搜索类别
	 */
	private Integer catid;
	/**
	 * 反关键字
	 */
	private String antiWords;
	/**
	 * 0 正常  1 删除
	 */
	private Integer sate;
	
	
	private String category;
	
	private Integer sort;
	
	private String relateKeyWordUrl;
	
	
	private String bannerImgName;//活动图片本地访问路径
	
	private String bannerImgUrl;//活动图片线上访问路径
	
	private String bannerName;//活动名称
	
	private String bannerDescribe;//活动描述
	
	public String getBannerImgName() {
		return bannerImgName;
	}
	public void setBannerImgName(String bannerImgName) {
		this.bannerImgName = bannerImgName;
	}
	public String getBannerImgUrl() {
		return bannerImgUrl;
	}
	public void setBannerImgUrl(String bannerImgUrl) {
		this.bannerImgUrl = bannerImgUrl;
	}
	public String getBannerName() {
		return bannerName;
	}
	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}
	public String getBannerDescribe() {
		return bannerDescribe;
	}
	public void setBannerDescribe(String bannerDescribe) {
		this.bannerDescribe = bannerDescribe;
	}
	public String getRelateKeyWordUrl() {
		return relateKeyWordUrl;
	}
	public void setRelateKeyWordUrl(String relateKeyWordUrl) {
		this.relateKeyWordUrl = relateKeyWordUrl;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	private List<Map<String,Object>> goodslist;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getSeachUrl() {
		return seachUrl;
	}
	public void setSeachUrl(String seachUrl) {
		this.seachUrl = seachUrl;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getCatid() {
		return catid;
	}
	public void setCatid(Integer catid) {
		this.catid = catid;
	}
	public String getAntiWords() {
		return antiWords;
	}
	public void setAntiWords(String antiWords) {
		this.antiWords = antiWords;
	}
	public Integer getSate() {
		return sate;
	}
	public void setSate(Integer sate) {
		this.sate = sate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Map<String, Object>> getGoodslist() {
		return goodslist;
	}
	public void setGoodslist(List<Map<String, Object>> goodslist) {
		this.goodslist = goodslist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
