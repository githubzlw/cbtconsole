package com.cbt.website.bean;

/**
 * 无货源商品推送实体类
* @author lyb
* @date 2016年8月26日
*
 */
public class NoGoodsPushBean {
	private int goodsid; //商品id
	private String goodsName; //商品名称
	private String carUrl; //商品链接地址
	private String img; //商品图片
	private Double price; //商品价格
	private int goodsCount; //缺少数量
	
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getCarUrl() {
		return carUrl;
	}
	public void setCarUrl(String carUrl) {
		this.carUrl = carUrl;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	
}
