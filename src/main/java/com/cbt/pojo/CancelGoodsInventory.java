package com.cbt.pojo;

import java.io.Serializable;

public class CancelGoodsInventory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String orderid;// 取消商品的订单号

	private String goodsid;// 取消商品的商品号

	private String order_barcode;// 取消商品存放库位

	private String inventory_qty;// 取消商品使用库存数量

	private String inventory_barcode;// 库存存放位置

	private String od_id;

	private String car_img;// 取消商品图片

	private String createtime;// 创建时间

	private String dealtime;// 处理时间

	private String operation;// 操作

	private String username;// 操作人

	private int flag;// 是否处理

	private int in_id;

	private int i_flag;
	
	private double goods_p_price;

	public double getGoods_p_price() {
		return goods_p_price;
	}

	public void setGoods_p_price(double goods_p_price) {
		this.goods_p_price = goods_p_price;
	}

	public int getIn_id() {
		return in_id;
	}

	public void setIn_id(int in_id) {
		this.in_id = in_id;
	}

	public int getI_flag() {
		return i_flag;
	}

	public void setI_flag(int i_flag) {
		this.i_flag = i_flag;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
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

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getOrder_barcode() {
		return order_barcode;
	}

	public void setOrder_barcode(String order_barcode) {
		this.order_barcode = order_barcode;
	}

	public String getInventory_qty() {
		return inventory_qty;
	}

	public void setInventory_qty(String inventory_qty) {
		this.inventory_qty = inventory_qty;
	}

	public String getInventory_barcode() {
		return inventory_barcode;
	}

	public void setInventory_barcode(String inventory_barcode) {
		this.inventory_barcode = inventory_barcode;
	}

	public String getOd_id() {
		return od_id;
	}

	public void setOd_id(String od_id) {
		this.od_id = od_id;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDealtime() {
		return dealtime;
	}

	public void setDealtime(String dealtime) {
		this.dealtime = dealtime;
	}
}
