package com.cbt.bean;

/**
 * 
 * @ClassName GoodsProfitReference
 * @Description 商品利润率参考
 * @author Jxw
 * @date 2018年2月23日
 */
public class GoodsProfitReference {
	private String pid;
	private String imgUrl;
	private String goodsName;
	private String price;
	private String firstPrice;
	private String finalWeight;
	private String wholesalePrice;
	private String wprice;
	private String rangePrice;
	private double rate;
	private double freight;
	private double freight5Gd;// 5件商品的平均运费
	private String categoryId;
	private String aliPrice;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(String firstPrice) {
		this.firstPrice = firstPrice;
	}

	public String getFinalWeight() {
		return finalWeight;
	}

	public void setFinalWeight(String finalWeight) {
		this.finalWeight = finalWeight;
	}

	public String getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(String wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public String getWprice() {
		return wprice;
	}

	public void setWprice(String wprice) {
		this.wprice = wprice;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public double getFreight5Gd() {
		return freight5Gd;
	}

	public void setFreight5Gd(double freight5Gd) {
		this.freight5Gd = freight5Gd;
	}

	public String getAliPrice() {
		return aliPrice;
	}

	public void setAliPrice(String aliPrice) {
		this.aliPrice = aliPrice;
	}

	@Override
	public String toString() {
		return "{\"pid\":\"" + pid + "\", \"imgUrl\":\"" + imgUrl + "\", \"goodsName\":\"" + goodsName
				+ "\", \"price\":\"" + price + "\", \"firstPrice\":\"" + firstPrice + "\", \"finalWeight\":\""
				+ finalWeight + "\", \"wholesalePrice\":\"" + wholesalePrice + "\", \"wprice\":\"" + wprice
				+ "\", \"rangePrice\":\"" + rangePrice + "\", \"rate\":\"" + rate + "\", \"freight\":\"" + freight
				+ "\", \"freight5Gd\":\"" + freight5Gd + "\", \"categoryId\":\"" + categoryId + "\", \"aliPrice\":\""
				+ aliPrice + "\"}";
	}

}
