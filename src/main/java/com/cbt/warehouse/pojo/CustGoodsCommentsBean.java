package com.cbt.warehouse.pojo;

import java.io.Serializable;

/**
 * 自定义评论实体类,扩展
 * @ClassName CustGoodsCommentsBean 
 * @Description TODO
 * @author Administrator
 * @date 2018年1月27日 下午4:01:13
 */
public class CustGoodsCommentsBean extends GoodsCommentsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String goodsname;
	private String goodsprice;
	private String goods_url;
	private String goods_img;
	
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	
	public String getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getGoods_img() {
		return goods_img;
	}
	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}
	
	

}
