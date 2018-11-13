package com.cbt.report.vo;

import java.util.Date;

public class OrderProductVo {

    private Integer odId;

    private Date addtime;

    private String orderid;

    private Double goodsPrice;

    private String currency;

    private String goodsPPrice;

    private Integer usecount;

    private Integer buycount;
    
    private String category;

	public Integer getOdId() {
		return odId;
	}

	public void setOdId(Integer odId) {
		this.odId = odId;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGoodsPPrice() {
		return goodsPPrice;
	}

	public void setGoodsPPrice(String goodsPPrice) {
		this.goodsPPrice = goodsPPrice;
	}

	public Integer getUsecount() {
		return usecount;
	}

	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}

	public Integer getBuycount() {
		return buycount;
	}

	public void setBuycount(Integer buycount) {
		this.buycount = buycount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}