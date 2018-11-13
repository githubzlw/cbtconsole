package com.cbt.bean;

/**
 * 
 * @ClassName GoodsInputWeight
 * @Description 类别重量数据
 * @author Jxw
 * @date 2018年4月10日
 */
public class CatidInputWeight {
	private String shopId;
	private String catid;
	private double weight;
	private double freight;

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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	@Override
	public String toString() {
		return "{\"shopId\":\"" + shopId + "\", \"catid\":\"" + catid + "\", \"weight\":\"" + weight
				+ "\", \"freight\":\"" + freight + "\"}";
	}

}
