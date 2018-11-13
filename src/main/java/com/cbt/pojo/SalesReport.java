package com.cbt.pojo;

public class SalesReport {
	private Integer user_id; //用户id
	private String admName; //管理员名称
	private Double payprice; //月 消费
	private String orderpaytime; //月首次订单支付时间
	private int sl; //月订单量
	private Double zje; // 从第一次订单起到本月底总消费金额
	private int zsl; // 从第一次订单起到本月底订单总数
	private String firstbuy; //首单支付时间
	private String scgm; // 订单支付时间
	private String newsign; //新旧标识
	
	private int datacount;
	
	public int getDatacount() {
		return datacount;
	}
	public void setDatacount(int datacount) {
		this.datacount = datacount;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	public Double getPayprice() {
		return payprice;
	}
	public void setPayprice(Double payprice) {
		this.payprice = payprice;
	}
	public String getOrderpaytime() {
		return orderpaytime;
	}
	public void setOrderpaytime(String orderpaytime) {
		this.orderpaytime = orderpaytime;
	}
	public int getSl() {
		return sl;
	}
	public void setSl(int sl) {
		this.sl = sl;
	}
	public Double getZje() {
		return zje;
	}
	public void setZje(Double zje) {
		this.zje = zje;
	}
	public int getZsl() {
		return zsl;
	}
	public void setZsl(int zsl) {
		this.zsl = zsl;
	}
	public String getFirstbuy() {
		return firstbuy;
	}
	public void setFirstbuy(String firstbuy) {
		this.firstbuy = firstbuy;
	}
	public String getScgm() {
		return scgm;
	}
	public void setScgm(String scgm) {
		this.scgm = scgm;
	}
	public String getNewsign() {
		return newsign;
	}
	public void setNewsign(String newsign) {
		this.newsign = newsign;
	}
	
}
