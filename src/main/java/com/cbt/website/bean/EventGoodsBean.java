package com.cbt.website.bean;
/**
 * 做活动单个商品实体类
 * @author admin
 *
 */
public class EventGoodsBean {

	private int goodsid;
	private int goodssampleid;
	private String goodsurl;
	private String name;
	private String img;
	private double factory_price;
	private String discount;
	private double discount_price;
	private String type;
	private int avilibleStock;
	private int sold;
	private String flag;
	private String weight;
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getGoodssampleid() {
		return goodssampleid;
	}
	public void setGoodssampleid(int goodssampleid) {
		this.goodssampleid = goodssampleid;
	}
	public String getGoodsurl() {
		return goodsurl;
	}
	public void setGoodsurl(String goodsurl) {
		this.goodsurl = goodsurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public double getFactory_price() {
		return factory_price;
	}
	public void setFactory_price(double factory_price) {
		this.factory_price = factory_price;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public double getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(double discount_price) {
		this.discount_price = discount_price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAvilibleStock() {
		return avilibleStock;
	}
	public void setAvilibleStock(int avilibleStock) {
		this.avilibleStock = avilibleStock;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
