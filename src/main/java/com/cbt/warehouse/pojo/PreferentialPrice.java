package com.cbt.warehouse.pojo;

/**
 * 采购批量优惠价格实体类
 * 
 * @author whj
 *
 */
public class PreferentialPrice {
	private int id;
	private String orderid;
	private int goodsid;
	private String goods_url;
	private String goods_p_url;
	private int begin;
	private int end;
	private double price;
	private String createtime;
	private String uuid;
	private String goods_p_itemid;
	private String goods_pid;

	public String getGoods_p_itemid() {
		return goods_p_itemid;
	}

	public void setGoods_p_itemid(String goods_p_itemid) {
		this.goods_p_itemid = goods_p_itemid;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getGoods_p_url() {
		return goods_p_url;
	}

	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
