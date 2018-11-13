package com.cbt.auto.ctrl;

import java.sql.Timestamp;

public class OrderAutoBean {
	private int orderid;//订单id
	
	private String orderno;//订单号
	
	private int staus;//订单状态
	
	private Timestamp paytime;//支付时间
	
	private String goodscatid;//商品类别ID
	//商品明细
	private String goodsid;
	//order_details id
	private int odid;
	//购物车商品链接
	private String carUrl;
	//goodsdataid
	private int goodsdataid;
	
	private int adminid;
	
	private String admname;
	
	private Timestamp logday;
	
	private int goodscount;
	
	int userid;
	private String goods_pid;
	private String car_urlMD5;
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

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getGoodscatid() {
		return goodscatid;
	}

	public void setGoodscatid(String goodscatid) {
		this.goodscatid = goodscatid;
	}
	public int getAdminid() {
		return adminid;
	}

	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}

	public String getAdmname() {
		return admname;
	}

	public void setAdmname(String admname) {
		this.admname = admname;
	}

	public Timestamp getLogday() {
		return logday;
	}

	public void setLogday(Timestamp date) {
		this.logday = date;
	}

	public int getGoodscount() {
		return goodscount;
	}

	public void setGoodscount(int goodscount) {
		this.goodscount = goodscount;
	}

	public int getGoodsdataid() {
		return goodsdataid;
	}

	public void setGoodsdataid(int goodsdataid) {
		this.goodsdataid = goodsdataid;
	}

	public int getStaus() {
		return staus;
	}

	public void setStaus(int staus) {
		this.staus = staus;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public int getOdid() {
		return odid;
	}

	public void setOdid(int odid) {
		this.odid = odid;
	}

	public String getCarUrl() {
		return carUrl;
	}

	public void setCarUrl(String carUrl) {
		this.carUrl = carUrl;
	}

	
	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}


	public Timestamp getPaytime() {
		return paytime;
	}

	public void setPaytime(Timestamp paytime) {
		this.paytime = paytime;
	}


}
