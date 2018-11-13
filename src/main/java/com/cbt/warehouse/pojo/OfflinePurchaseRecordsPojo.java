package com.cbt.warehouse.pojo;

public class OfflinePurchaseRecordsPojo {
	private int id;
	   private String shipno;
	   private String createtime;
	   private String admuserid;
	   private String goods_p_url;
	   private 	String tb_orderid;
	   private String goodsid;
	   private String orderid;
   public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getAdmuserid() {
		return admuserid;
	}
	public void setAdmuserid(String admuserid) {
		this.admuserid = admuserid;
	}
	public String getGoods_p_url() {
		return goods_p_url;
	}
	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}
	public String getTb_orderid() {
		return tb_orderid;
	}
	public void setTb_orderid(String tb_orderid) {
		this.tb_orderid = tb_orderid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
}
