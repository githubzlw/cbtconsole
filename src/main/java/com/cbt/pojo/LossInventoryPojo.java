package com.cbt.pojo;

import java.io.Serializable;

public class LossInventoryPojo implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int in_id;//
	private String new_remaining;
	private String old_remaining;
	private String loss_inventory;
	private String old_barcode;
	private String new_barcode;
	private String loss_amount;
	private String loss_price;
	private String createtime;
	private String admName;
	private String car_img;
	private String goods_p_url;
	private String remark;
	private String sku;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGoods_p_url() {
		return goods_p_url;
	}
	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}
	public String getCar_img() {
		return car_img;
	}
	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIn_id() {
		return in_id;
	}
	public void setIn_id(int in_id) {
		this.in_id = in_id;
	}
	public String getNew_remaining() {
		return new_remaining;
	}
	public void setNew_remaining(String new_remaining) {
		this.new_remaining = new_remaining;
	}
	public String getOld_remaining() {
		return old_remaining;
	}
	public void setOld_remaining(String old_remaining) {
		this.old_remaining = old_remaining;
	}
	public String getLoss_inventory() {
		return loss_inventory;
	}
	public void setLoss_inventory(String loss_inventory) {
		this.loss_inventory = loss_inventory;
	}
	public String getOld_barcode() {
		return old_barcode;
	}
	public void setOld_barcode(String old_barcode) {
		this.old_barcode = old_barcode;
	}
	public String getNew_barcode() {
		return new_barcode;
	}
	public void setNew_barcode(String new_barcode) {
		this.new_barcode = new_barcode;
	}
	public String getLoss_amount() {
		return loss_amount;
	}
	public void setLoss_amount(String loss_amount) {
		this.loss_amount = loss_amount;
	}
	public String getLoss_price() {
		return loss_price;
	}
	public void setLoss_price(String loss_price) {
		this.loss_price = loss_price;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
  
}
