package com.cbt.warehouse.pojo;

import java.io.Serializable;

public class DisplayBuyInfo implements Serializable {
	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private String car_url;// 采购链接
	private String goods_p_price;// 采购价格
	private String buycount;// 采购数量
	private String admName;// 采购人
	private String createtime;// 采购时间
	private String company;// 采样公司
	private String goodsname;//商品名称
	private String od_id;
	private String remark;//采样商品反馈
	private String goods_pid;
	private String level;//工厂级别
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOd_id() {
		return od_id;
	}

	public void setOd_id(String od_id) {
		this.od_id = od_id;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getCar_url() {
		return car_url;
	}

	public void setCar_url(String car_url) {
		this.car_url = car_url;
	}

	public String getGoods_p_price() {
		return goods_p_price;
	}

	public void setGoods_p_price(String goods_p_price) {
		this.goods_p_price = goods_p_price;
	}

	public String getBuycount() {
		return buycount;
	}

	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}

	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
