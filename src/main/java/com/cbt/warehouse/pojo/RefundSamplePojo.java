package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class RefundSamplePojo implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private String goods_name;//ali名称
	private String goods_p_name;//1688名称
	private String img;//1688图片
	private String price;//1688价格
	private String profit_rate;//利润率
	private  String inventory_count;//库存数量
	private String id_time;//入库时间
	private String last_return_time;//最晚退货时间
	private String shop_id;//供应商名称
	private String shop_id_socre;//供应商评分
	private String state;//当前状态
	private String shipno;//退货快递号
	private String operating;//操作
	private String goods_p_url;
	private String goods_pid;
	private String preferential;
	private String t_id;//进货商品记录ID
	private String t_orderid;
	private String remark;
	private String in_id;//库存ID
	private String od_orderid;
	private String goodsid;
	private String flag;//库存是否盘点过
	private String itemqty;//商品采购数量
	private String goodsAttr;
	public String getGoodsAttr() {
		return goodsAttr;
	}

	public void setGoodsAttr(String goodsAttr) {
		this.goodsAttr = goodsAttr;
	}

	public String getItemqty() {
		return itemqty;
	}
	public void setItemqty(String itemqty) {
		this.itemqty = itemqty;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOd_orderid() {
		return od_orderid;
	}
	public void setOd_orderid(String od_orderid) {
		this.od_orderid = od_orderid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getIn_id() {
		return in_id;
	}
	public void setIn_id(String in_id) {
		this.in_id = in_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getT_orderid() {
		return t_orderid;
	}
	public void setT_orderid(String t_orderid) {
		this.t_orderid = t_orderid;
	}
	public String getT_id() {
		return t_id;
	}
	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	public String getPreferential() {
		return preferential;
	}
	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}
	public String getGoods_pid() {
		return goods_pid;
	}
	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}
	public String getGoods_p_url() {
		return goods_p_url;
	}
	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}
	public String getGoods_p_name() {
		return goods_p_name;
	}
	public void setGoods_p_name(String goods_p_name) {
		this.goods_p_name = goods_p_name;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getProfit_rate() {
		return profit_rate;
	}
	public void setProfit_rate(String profit_rate) {
		this.profit_rate = profit_rate;
	}
	public String getInventory_count() {
		return inventory_count;
	}
	public void setInventory_count(String inventory_count) {
		this.inventory_count = inventory_count;
	}
	public String getId_time() {
		return id_time;
	}
	public void setId_time(String id_time) {
		this.id_time = id_time;
	}
	public String getLast_return_time() {
		return last_return_time;
	}
	public void setLast_return_time(String last_return_time) {
		this.last_return_time = last_return_time;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getShop_id_socre() {
		return shop_id_socre;
	}
	public void setShop_id_socre(String shop_id_socre) {
		this.shop_id_socre = shop_id_socre;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getOperating() {
		return operating;
	}
	public void setOperating(String operating) {
		this.operating = operating;
	}
}
