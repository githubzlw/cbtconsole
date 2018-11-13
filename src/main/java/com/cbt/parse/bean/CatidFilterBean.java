package com.cbt.parse.bean;

public class CatidFilterBean {
	private String weight;//类别默认重量
	private double minPrice;//类别默认重量
	private String catid;//类别id
	private String keyword;//过滤关键词
	
	/*catid的过滤类型
	 *  type=1.指定类别搜索跳转到1688, 
	 *  type=2.指定类别默认重量
	 *  type=3.混合型（即 既有默认重量又有类别跳转）
	 *  type=4 指定类别 限定最小价格  aliexpress用于价格排序
	 *  */
	private int type;
	private int valid;
	private String time;
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minprice) {
		this.minPrice = minprice;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	

}
