package com.cbt.warehouse.pojo;

/**
 * 商品替换日志实体类
 * 王宏杰 2018-12-11
 */
public class ChangeGoodsLogPojo {

	private String orderid;
	private String old_odid;
	private String old_goodsid;
	private String old_url;
	private String old_goodsPrice;
	private String new_url;
	private String new_goodsPrice;
	private String admuserid;
	private String createtime;
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOld_odid() {
		return old_odid;
	}

	public void setOld_odid(String old_odid) {
		this.old_odid = old_odid;
	}

	public String getOld_goodsid() {
		return old_goodsid;
	}

	public void setOld_goodsid(String old_goodsid) {
		this.old_goodsid = old_goodsid;
	}

	public String getOld_url() {
		return old_url;
	}

	public void setOld_url(String old_url) {
		this.old_url = old_url;
	}

	public String getOld_goodsPrice() {
		return old_goodsPrice;
	}

	public void setOld_goodsPrice(String old_goodsPrice) {
		this.old_goodsPrice = old_goodsPrice;
	}

	public String getNew_url() {
		return new_url;
	}

	public void setNew_url(String new_url) {
		this.new_url = new_url;
	}

	public String getNew_goodsPrice() {
		return new_goodsPrice;
	}

	public void setNew_goodsPrice(String new_goodsPrice) {
		this.new_goodsPrice = new_goodsPrice;
	}

	public String getAdmuserid() {
		return admuserid;
	}

	public void setAdmuserid(String admuserid) {
		this.admuserid = admuserid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}