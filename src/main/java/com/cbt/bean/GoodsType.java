package com.cbt.bean;
/*������ʾ��Ʒ*/
public class GoodsType {
	private int id;
	private int type1_id;
	private String type1;
	private int type2_id;
	private String type2;
	private String goodsname;
	private String img_url;
	private String goods_url;
	private float price;
	
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType1() {
		return type1;
	}
	public void setType1(String type1) {
		this.type1 = type1;
	}
	public String getType2() {
		return type2;
	}
	public void setType2(String type2) {
		this.type2 = type2;
	}
	public int getType1_id() {
		return type1_id;
	}
	public void setType1_id(int type1_id) {
		this.type1_id = type1_id;
	}
	public int getType2_id() {
		return type2_id;
	}
	public void setType2_id(int type2_id) {
		this.type2_id = type2_id;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"type1_id\":\"%s\", \"type1\":\"%s\", \"type2_id\":\"%s\", \"type2\":\"%s\", \"goodsname\":\"%s\", \"img_url\":\"%s\", \"goods_url\":\"%s\", \"price\":\"%s\"}",
						id, type1_id, type1, type2_id, type2, goodsname,
						img_url, goods_url, price);
	}
	
}
