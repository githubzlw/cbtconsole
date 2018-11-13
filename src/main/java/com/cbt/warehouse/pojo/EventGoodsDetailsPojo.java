package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class EventGoodsDetailsPojo implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String id;//							
	private String goodssampleid;//	基本样品表id					
	private String goodsid;//		产品id					
	private String goodsname;//		产品名字	
	private String goodsurl;//我们自己的网站上产品单页对应的链接						
	private String goodsimg;// 商品图片					
	private String goodsprice;//原价							
	private String cid;//							
	private String cidpath;//		分类路径，对应alicategory表path					
	private String category;//	
	private String type;
	private String weight;
	private String originalprice;
	private String flag;
	private String discount;
	private int avilibleStock;//库存数量
	private int sold;//已售出数量
	
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
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOriginalprice() {
		return originalprice;
	}
	public void setOriginalprice(String originalprice) {
		this.originalprice = originalprice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodssampleid() {
		return goodssampleid;
	}
	public void setGoodssampleid(String goodssampleid) {
		this.goodssampleid = goodssampleid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoodsurl() {
		return goodsurl;
	}
	public void setGoodsurl(String goodsurl) {
		this.goodsurl = goodsurl;
	}
	public String getGoodsimg() {
		return goodsimg;
	}
	public void setGoodsimg(String goodsimg) {
		this.goodsimg = goodsimg;
	}
	public String getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCidpath() {
		return cidpath;
	}
	public void setCidpath(String cidpath) {
		this.cidpath = cidpath;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
