package com.cbt.bean;
/**
 * @author ylm
 * 优惠申请
 */
public class Preferential {

	private int id;
	private String pano;//优惠申请号
	private int userid;
	private int goodsid;
	private String sessionid;
	private String url;
	private int number;//申请数量
	private double sprice;//原价格
	private String price;//现价格
	private String email;
	private String country;
	private String username;
	private int state;
	private String note;//备注
	private String createtime;
	private String handletime;//处理时间
	private String effectivetime;//有效期
	private int effectiveend;//是否失效
	private String syseffectivetime;//系统有效时间
	private String pGoodsUnit;
	private String discount;//折扣率
	private String discountedUnitPrice;
	private String totalprice;
	private String shipping;
	private String goods_types;//
	private String currency;//货币单位
	private String admname;//负责人
	private int adminid;
	private String itemId;//分组ID

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPano() {
		return pano;
	}
	public void setPano(String pano) {
		this.pano = pano;
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getHandletime() {
		return handletime;
	}
	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getSprice() {
		return sprice;
	}
	public void setSprice(double sprice) {
		this.sprice = sprice;
	}
	public String getEffectivetime() {
		return effectivetime;
	}
	public void setEffectivetime(String effectivetime) {
		this.effectivetime = effectivetime;
	}
	public int getEffectiveend() {
		return effectiveend;
	}
	public void setEffectiveend(int effectiveend) {
		this.effectiveend = effectiveend;
	}
	public void setSyseffectivetime(String syseffectivetime) {
		this.syseffectivetime = syseffectivetime;
	}
	public String getSyseffectivetime() {
		return syseffectivetime;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public void setpGoodsUnit(String pGoodsUnit) {
		this.pGoodsUnit = pGoodsUnit;
	}
	public String getpGoodsUnit() {
		return pGoodsUnit;
	}
	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	/**
	 * @return the discountedUnitPrice
	 */
	public String getDiscountedUnitPrice() {
		return discountedUnitPrice;
	}
	/**
	 * @param discountedUnitPrice the discountedUnitPrice to set
	 */
	public void setDiscountedUnitPrice(String discountedUnitPrice) {
		this.discountedUnitPrice = discountedUnitPrice;
	}
	/**
	 * @return the totalprice
	 */
	public String getTotalprice() {
		return totalprice;
	}
	/**
	 * @param totalprice the totalprice to set
	 */
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	/**
	 * @return the shipping
	 */
	public String getShipping() {
		return shipping;
	}
	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	
	public String getGoods_types() {
		return goods_types;
	}
	public void setGoods_types(String goods_types) {
		this.goods_types = goods_types;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return price;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public String getAdmname() {
		return admname;
	}
	public void setAdmname(String admname) {
		this.admname = admname;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemId() {
		return itemId;
	}
	public int getAdminid() {
		return adminid;
	}
	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	
}
