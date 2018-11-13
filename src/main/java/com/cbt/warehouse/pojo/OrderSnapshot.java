package com.cbt.warehouse.pojo;

import java.io.Serializable;

/**
 * 
 * @ClassName OrderSnapshot
 * @Description 订单快照bean
 * @author jxw
 * @date 2018年1月10日
 */
public class OrderSnapshot implements Serializable {

	private static final long serialVersionUID = 856235896527L;

	private int id;
	private int odId;// order_details表id
	private int userid;
	private int goodsid;
	private String goodsname;
	private String goods_url;
	private String goods_img;
	private String goods_type;
	private String orderid;
	private String dropshipid;// dropshiporder订单表id
	private int yourorder;
	private String goodsprice;
	private String delivery_time;// 交期
	private String product_cost;// 商品总费用
	private int goods_class;// 商品类型
	private String car_urlMD5;// goods_url的MD5数字指纹
	private String goods_pid;// 产品id号，对应goods_car的itemId
	private String goods_details;// 1688描述明细
	private String goods_eninfo;// 产品详情
	private int deal_flag;// 图片处理标识 1已经处理，0未处理
	private String down_img_url;// 图片下载地址
	private String remotpath;
	private String goodsUnit;//商品单位

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOdId() {
		return odId;
	}

	public void setOdId(int odId) {
		this.odId = odId;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
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

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getDropshipid() {
		return dropshipid;
	}

	public void setDropshipid(String dropshipid) {
		this.dropshipid = dropshipid;
	}

	public int getYourorder() {
		return yourorder;
	}

	public void setYourorder(int yourorder) {
		this.yourorder = yourorder;
	}

	public String getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

	public String getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}

	public int getGoods_class() {
		return goods_class;
	}

	public void setGoods_class(int goods_class) {
		this.goods_class = goods_class;
	}

	public String getCar_urlMD5() {
		return car_urlMD5;
	}

	public void setCar_urlMD5(String car_urlMD5) {
		this.car_urlMD5 = car_urlMD5;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getGoods_details() {
		return goods_details;
	}

	public void setGoods_details(String goods_details) {
		this.goods_details = goods_details;
	}

	public String getGoods_eninfo() {
		return goods_eninfo;
	}

	public void setGoods_eninfo(String goods_eninfo) {
		this.goods_eninfo = goods_eninfo;
	}

	public int getDeal_flag() {
		return deal_flag;
	}

	public void setDeal_flag(int deal_flag) {
		this.deal_flag = deal_flag;
	}

	public String getDown_img_url() {
		return down_img_url;
	}

	public void setDown_img_url(String down_img_url) {
		this.down_img_url = down_img_url;
	}

	public String getRemotpath() {
		return remotpath;
	}

	public void setRemotpath(String remotpath) {
		this.remotpath = remotpath;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	
	

}
