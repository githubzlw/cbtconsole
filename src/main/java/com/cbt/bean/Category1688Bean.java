package com.cbt.bean;

/**
 * 
 * @ClassName Category1688Bean
 * @Description 1688类别
 * @author Jxw
 * @date 2018年3月2日
 */
public class Category1688Bean {
	private int id;
	private String categoryId;
	private String categoryCName;
	private String categoryName;
	private int lv;
	private String childIds;
	private String path;
	private String parentId;

	public String getCategoryCName() {
		return categoryCName;
	}

	public void setCategoryCName(String categoryCName) {
		this.categoryCName = categoryCName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public String getChildIds() {
		return childIds;
	}

	public void setChildIds(String childIds) {
		this.childIds = childIds;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"categoryId\":\"" + categoryId + "\", \"categoryName\":\"" + categoryName
				+ "\", \"lv\":\"" + lv + "\", \"childIds\":\"" + childIds + "\", \"path\":\"" + path
				+ "\", \"parentId\":\"" + parentId + "\"}";
	}

}
