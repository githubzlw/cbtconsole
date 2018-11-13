package com.cbt.warehouse.pojo;

public class OrderProductSurcePojo {
	private String odcar_img;
	private String goods_price;  //价格
	private String goodscount;  //商品数量
	private String admName;  //采购人员
	private String orderid; //订单号
	private String buycount;//代买数量
	private String goodsid; //商品id
	
	
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getOrderid() {
		return orderid;
	}
	public String getBuycount() {
		return buycount;
	}
	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOdcar_img() {
		return odcar_img;
	}
	public void setOdcar_img(String odcar_img) {
		this.odcar_img = odcar_img;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoodscount() {
		return goodscount;
	}
	public void setGoodscount(String goodscount) {
		this.goodscount = goodscount;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	
	
}
