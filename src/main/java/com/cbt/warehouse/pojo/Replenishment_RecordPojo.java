package com.cbt.warehouse.pojo;

public class Replenishment_RecordPojo {
	private int id;
	private int acount;  //补货数量
	private double price;//补货价格
	private String goods_p_url;//补货商品链接
	private String remark;//补货备注
	private String createtime;//补货时间
	private String admuserid;//补货人ID
	private int goodsid;//商品编号
	private String orderid;//销售订单号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAcount() {
		return acount;
	}
	public void setAcount(int acount) {
		this.acount = acount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getGoods_p_url() {
		return goods_p_url;
	}
	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getAdmuserid() {
		return admuserid;
	}
	public void setAdmuserid(String admuserid) {
		this.admuserid = admuserid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

}
