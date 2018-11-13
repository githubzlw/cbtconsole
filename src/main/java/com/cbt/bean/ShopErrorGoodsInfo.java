package com.cbt.bean;

import java.util.List;

/**
 * 
 * @ClassName ShopErrorGoodsInfo
 * @Description 店铺异常商品信息
 * @author Jxw
 * @date 2018年2月23日
 */
public class ShopErrorGoodsInfo {
	private String shopId;// 店铺ID
	private String categoryId;// 类别ID
	private String categoryName;// 类别名称
	private float weightVal;// 重量设定
	private int totalNum;//
	private List<GoodsOfferBean> gdOfLs;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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

	public float getWeightVal() {
		return weightVal;
	}

	public void setWeightVal(float weightVal) {
		this.weightVal = weightVal;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<GoodsOfferBean> getGdOfLs() {
		return gdOfLs;
	}

	public void setGdOfLs(List<GoodsOfferBean> gdOfLs) {
		this.gdOfLs = gdOfLs;
	}

	@Override
	public String toString() {
		return "{\"shopId\":\"" + shopId + "\", \"categoryId\":\"" + categoryId + "\", \"categoryName\":\""
				+ categoryName + "\", \"weightVal\":\"" + weightVal + "\", \"totalNum\":\"" + totalNum
				+ "\", \"gdOfLs\":\"" + gdOfLs + "\"}";
	}

}
