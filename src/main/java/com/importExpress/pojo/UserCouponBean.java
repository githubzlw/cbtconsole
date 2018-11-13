package com.importExpress.pojo;

import java.io.Serializable;


public class UserCouponBean implements Serializable {
	
	private static final long serialVersionUID = 34748509347128907L;
	private int id;
	private int userid;//用户id
	private String starttime;//起效时间
	private String endtime;//终止时间
	private String percentage;//折扣比率
	private String maxdis;//折扣金额
	private int state;//状态（0 未使用  1 已使用  2 失效）
	private int usetype;//使用方式（0 一次使用，多次使用，需要保证金额不容许保存小数）
	private String goodsclass;//商品分类限制（默认0  不限制，限制需要添加catid）
	private int type;//卷分类(0 运费卷  1 通用卷  2 销售赠送运费卷 3 销售赠送通用卷)
	private String orderid;
	private String comment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getMaxdis() {
		return maxdis;
	}
	public void setMaxdis(String maxdis) {
		this.maxdis = maxdis;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getUsetype() {
		return usetype;
	}
	public void setUsetype(int usetype) {
		this.usetype = usetype;
	}
	public String getGoodsclass() {
		return goodsclass;
	}
	public void setGoodsclass(String goodsclass) {
		this.goodsclass = goodsclass;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
