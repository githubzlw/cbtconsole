package com.cbt.Specification.bean;

public class GoodsDataInfo {
	private int goodsid;// goods的id
	private String cid;// goods的类别id
	private String detail;// goods的详情
	private String types;// goods的选择区

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "GoodsDataInfo [goodsid=" + goodsid + ", cid=" + cid + ", detail=" + detail + ", types=" + types + "]";
	}

}
