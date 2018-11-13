package com.cbt.warehouse.pojo;

/**
 * 出库  库存明细表
 * @author admin
 *
 */
public class GoodsInventory {

	private String  goods_url;
	private int  inGoodNum;
	private String  inGoodTime;
	private  String  remark;
	private  String itemid;
	private  String barcode;
	private  String  goodsname;
	private  String  sku ;
	private  String itemprice ;
	private  String  new_barcode;
	
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	
	public int getInGoodNum() {
		return inGoodNum;
	}
	public void setInGoodNum(int inGoodNum) {
		this.inGoodNum = inGoodNum;
	}
	public String getInGoodTime() {
		return inGoodTime;
	}
	public void setInGoodTime(String inGoodTime) {
		this.inGoodTime = inGoodTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getItemprice() {
		return itemprice;
	}
	public void setItemprice(String itemprice) {
		this.itemprice = itemprice;
	}
	public String getNew_barcode() {
		return new_barcode;
	}
	public void setNew_barcode(String new_barcode) {
		this.new_barcode = new_barcode;
	}
	
	
}
