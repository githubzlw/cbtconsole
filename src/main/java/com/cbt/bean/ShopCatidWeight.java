package com.cbt.bean;

/**
 * 
 * @ClassName ShopCatidWeight
 * @Description 店铺类别的平均重量
 * @author Jxw
 * @date 2018年3月29日
 */
public class ShopCatidWeight {
	private int id;
	private String shopId;
	private String catid;
	private Double avgWeight;
	private String keyword;
	private int adminId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public Double getAvgWeight() {
		return avgWeight;
	}

	public void setAvgWeight(Double avgWeight) {
		this.avgWeight = avgWeight;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"shopId\":\"" + shopId + "\", \"catid\":\"" + catid + "\", \"avgWeight\":\""
				+ avgWeight + "\", \"keyword\":\"" + keyword + "\", \"adminId\":\"" + adminId + "\"}";
	}

}
