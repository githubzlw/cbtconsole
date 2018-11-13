package com.cbt.website.bean;

public class PurchaseDetailsBean {

	private int goodsid;
	private int goodsdata_id;
	private String googs_img;
	private String goods_url;
	private String goods_type;
	private String goods_title;
	private String goods_price;
	private String currency;
	private int googs_number;
	private String remark;
	private String oldValue;
	private String newValue;
	private String remarkpurchase;
	private String issure;
	private int purchaseCount;
	private String addtime;
	private String purchaseSure;
	private String puechaseTime;
	
	
	public PurchaseDetailsBean() {
		super();
	}
	
	public PurchaseDetailsBean(int goodsid,int goodsdata_id, String googs_img,
			String goods_url, String goods_type, String goods_title,int purchaseCount,
			String goods_price, String currency, int googs_number,String issure,
			String remark, String oldValue, String newValue,String remarkpurchase,
			String addtime,String purchaseSure,String puechaseTime) {
		super();
		this.goodsid = goodsid;
		this.goodsdata_id = goodsdata_id;
		this.googs_img = googs_img;
		this.goods_url = goods_url;
		this.goods_type = goods_type;
		this.goods_title = goods_title;
		this.goods_price = goods_price;
		this.currency = currency;
		this.googs_number = googs_number;
		this.remark = remark;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.remarkpurchase = remarkpurchase;
		this.issure = issure;
		this.purchaseCount = purchaseCount;
		this.addtime = addtime;
		this.purchaseSure = purchaseSure;
		this.puechaseTime = puechaseTime;
	}

	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getGoodsdata_id() {
		return goodsdata_id;
	}
	public void setGoodsdata_id(int goodsdata_id) {
		this.goodsdata_id = goodsdata_id;
	}
	public String getGoogs_img() {
		return googs_img;
	}
	public void setGoogs_img(String googs_img) {
		this.googs_img = googs_img;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public String getGoods_title() {
		return goods_title;
	}
	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getGoogs_number() {
		return googs_number;
	}
	public void setGoogs_number(int googs_number) {
		this.googs_number = googs_number;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getRemarkpurchase() {
		return remarkpurchase;
	}
	public void setRemarkpurchase(String remarkpurchase) {
		this.remarkpurchase = remarkpurchase;
	}
	public String getIssure() {
		return issure;
	}
	public void setIssure(String issure) {
		this.issure = issure;
	}
	public int getPurchaseCount() {
		return purchaseCount;
	}
	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getPurchaseSure() {
		return purchaseSure;
	}
	public void setPurchaseSure(String purchaseSure) {
		this.purchaseSure = purchaseSure;
	}
	public String getPuechaseTime() {
		return puechaseTime;
	}
	public void setPuechaseTime(String puechaseTime) {
		this.puechaseTime = puechaseTime;
	}
	
}
