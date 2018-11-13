package com.cbt.website.bean;

import java.util.List;

public class ExpressTrackInfo {
	private String expressTrackOrderid;//订单号
	private String expressTrackRelationId;//订单号对应的单号id
	private String expressTrackId;//快递跟踪号
	private int type;//单号类型，1:淘宝单号2:1688单号3:直接的快递单号
	private int status;//0:未生成快递跟踪号1:已生成
	private List<String> goodsImgurl;//商品的图片url
	private List<String> goodsId;//商品id
	private List<String> goodsName;//商品名称
	private List<String> goodsType;//商品规格
	private List<String> goodsPrice;//商品价格=商品价格+货币单位+商品单位
	private List<Integer> goodsQuantity;//商品数量
	private OrderWarehouseInfo warehouseInfo;//订单仓库信息
	private int[] arrivalstatus;//商品的到货状态
	
	public String getExpressTrackOrderid() {
		return expressTrackOrderid;
	}
	public void setExpressTrackOrderid(String expressTrackOrderid) {
		this.expressTrackOrderid = expressTrackOrderid;
	}
	public String getExpressTrackRelationId() {
		return expressTrackRelationId;
	}
	public void setExpressTrackRelationId(String expressTrackRelationId) {
		this.expressTrackRelationId = expressTrackRelationId;
	}
	public String getExpressTrackId() {
		return expressTrackId;
	}
	public void setExpressTrackId(String expressTrackId) {
		this.expressTrackId = expressTrackId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<String> getGoodsImgurl() {
		return goodsImgurl;
	}
	public void setGoodsImgurl(List<String> goodsImgurl) {
		this.goodsImgurl = goodsImgurl;
	}
	public List<String> getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(List<String> goodsId) {
		this.goodsId = goodsId;
	}
	public OrderWarehouseInfo getWarehouseInfo() {
		return warehouseInfo;
	}
	public void setWarehouseInfo(OrderWarehouseInfo warehouseInfo) {
		this.warehouseInfo = warehouseInfo;
	}
	public int[] getArrivalstatus() {
		return arrivalstatus;
	}
	public void setArrivalstatus(int[] arrivalstatus) {
		this.arrivalstatus = arrivalstatus;
	}
	public List<String> getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(List<String> goodsName) {
		this.goodsName = goodsName;
	}
	public List<String> getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(List<String> goodsType) {
		this.goodsType = goodsType;
	}
	public List<String> getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(List<String> goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public List<Integer> getGoodsQuantity() {
		return goodsQuantity;
	}
	public void setGoodsQuantity(List<Integer> goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}
	
}
