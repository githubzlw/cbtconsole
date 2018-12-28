package com.cbt.pojo;

import java.io.Serializable;

/**
 * 广东直发列表实体类
 * @ClassName StraightHairPojo 
 * @Description TODO
 * @author 王宏杰
 * @date 2018年3月23日 下午5:04:28
 */
public class StraightHairPojo implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private String orderid;//订单号
	private String userid;//用户ID
	private String goodsid;//商品号
	private String buyer;//采购人
	private String sku;//规格
	private String img;//图片
	private String state;//状态
	private String times;//确认直发时间
	private String car_url;
	private String goodsname;
	private String shipno;//直发时发货单号
	private String operating;
	private String remark;
	private String states;
	private String odid;

	public String getTborderid() {
		return tborderid;
	}

	public void setTborderid(String tborderid) {
		this.tborderid = tborderid;
	}

	private String tborderid;
	public String getOdid() {
		return odid;
	}

	public void setOdid(String odid) {
		this.odid = odid;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperating() {
		return operating;
	}
	public void setOperating(String operating) {
		this.operating = operating;
	}
	public String getShipno() {
		return shipno;
	}
	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
	public String getCar_url() {
		return car_url;
	}
	public void setCar_url(String car_url) {
		this.car_url = car_url;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}

}
