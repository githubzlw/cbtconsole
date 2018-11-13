package com.cbt.parse.bean;

/**关键词的提示词
 * @author abc
 *
 */
public class DataReturn {
	private String keyword;//提示词
	private String catId;//提示词所在的类别ud
	private String catName;//提示词所在的类别名称
	private String count;//符合提示词的搜索结果数

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("{\"dat\":\"%s\"}", keyword);
	}
	
}
