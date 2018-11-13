package com.cbt.pojo;

import java.io.Serializable;

/**
 * 库存商品对应的出入库明细
 * @ClassName StorageOutboundDetailsPojo 
 * @Description TODO
 * @author Administrator
 * @date 2018年4月2日 下午4:02:06
 */
public class StorageOutboundDetailsPojo implements Serializable{
	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String orderid;
	private String goodsid;
	private String car_img;
	private String yourorder;//商品总验货数量
	private String car_type;//当次验货数量

	public String getAdd_inventory() {
		return add_inventory;
	}

	public void setAdd_inventory(String add_inventory) {
		this.add_inventory = add_inventory;
	}

	private String add_inventory;
	public String getWhen_count() {
		return when_count;
	}

	public void setWhen_count(String when_count) {
		this.when_count = when_count;
	}

	private String when_count;
	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	private String types; //1入库 2出库
	private String createtime;
	private String counts;
	private String goods_pid;
	private String admName;
	private String barcode;
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

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	public String getYourorder() {
		return yourorder;
	}

	public void setYourorder(String yourorder) {
		this.yourorder = yourorder;
	}

	public String getCar_type() {
		return car_type;
	}

	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}


	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCounts() {
		return counts;
	}

	public void setCounts(String counts) {
		this.counts = counts;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
