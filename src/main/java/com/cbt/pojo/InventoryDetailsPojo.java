package com.cbt.pojo;

import java.io.Serializable;

public class InventoryDetailsPojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String createtime;
	private int od_id;
	private int inventory_acount;
	private double inventory_price;
	private double invetory_amount;
	private String inventory_barcode;
	private String goods_pid;
    private String car_type;
    private String car_urlMD5;
    private String car_img;
    private String goodName;
    //================
    private String orderno;
	private String goodsid;
    private int lock_remaining;
    private double lock_inventory_amount;
    private String is_use;
    private String flag;
    private String is_delete;
    public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public int getLock_remaining() {
		return lock_remaining;
	}
	public void setLock_remaining(int lock_remaining) {
		this.lock_remaining = lock_remaining;
	}
	public double getLock_inventory_amount() {
		return lock_inventory_amount;
	}
	public void setLock_inventory_amount(double lock_inventory_amount) {
		this.lock_inventory_amount = lock_inventory_amount;
	}
	public String getIs_use() {
		return is_use;
	}
	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getOd_id() {
		return od_id;
	}
	public void setOd_id(int od_id) {
		this.od_id = od_id;
	}
	public int getInventory_acount() {
		return inventory_acount;
	}
	public void setInventory_acount(int inventory_acount) {
		this.inventory_acount = inventory_acount;
	}
	public double getInventory_price() {
		return inventory_price;
	}
	public void setInventory_price(double inventory_price) {
		this.inventory_price = inventory_price;
	}
	public double getInvetory_amount() {
		return invetory_amount;
	}
	public void setInvetory_amount(double invetory_amount) {
		this.invetory_amount = invetory_amount;
	}
	public String getInventory_barcode() {
		return inventory_barcode;
	}
	public void setInventory_barcode(String inventory_barcode) {
		this.inventory_barcode = inventory_barcode;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public String getCar_urlMD5() {
		return car_urlMD5;
	}
	public void setCar_urlMD5(String car_urlMD5) {
		this.car_urlMD5 = car_urlMD5;
	}
	public String getCar_img() {
		return car_img;
	}
	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
    
	

}
