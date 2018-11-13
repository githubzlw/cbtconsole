package com.cbt.bean;

import java.util.List;

/**
 * @author zlw master
 */
public class CategoryBean {

	private int id;
	private String cid;
	private String path;
	private String categoryName;
	private int total;// 此类别下商品数量
	private int lv;// 类别等级
	private String pCid;// 当前类别的父cid

	private String category_id;
	private String name;
	private String childids;
	private String enName;

	private List<CategoryBean> threelist;
	private List<CategoryBean> towList;



	public List<CategoryBean> getThreelist() {
		return threelist;
	}

	public void setThreelist(List<CategoryBean> threelist) {
		this.threelist = threelist;
	}

	public List<CategoryBean> getTowList() {
		return towList;
	}

	public void setTowList(List<CategoryBean> towList) {
		this.towList = towList;
	}



	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChildids() {
		return childids;
	}

	public void setChildids(String childids) {
		this.childids = childids;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}



	public int getId() {
		return id;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid
	 *            the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public String getpCid() {
		return pCid;
	}

	public void setpCid(String pCid) {
		this.pCid = pCid;
	}

}
