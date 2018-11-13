package com.cbt.warehouse.pojo;

import java.sql.Date;
import java.util.List;

/**
 * 批量优惠邮件详情
 * 
 * @author JXW
 *
 */
public class BatchDiscountEmailDetails {
	private int id;
	private int btDsEmId;// 批量折扣邮件表id
	private int goodsId;// 商品id
	private String goodsUuid;// 产品原始链接md5取值
	private String goodsType;// 商品规格
	private String goodsImg;// 商品图片链接
	private String goodsImportUrl;// 电商网站链接
	private String goodsUrl;// 商品链接
	private float goodsPrice;// 客户成交价格
	private float freeShippingPrice;// 商品免邮价格
	private String goodsName;// 商品名称
	private int orderNumber;// 订单数量
	private int odDtId;// 订单详情id
	private Date createTime;// 创建时间
	private String valid;// 是否有效 0:无效,1有效
	private List<BatchDiscountPurchasPrice> purchasPriceList;// 商品的批量价格

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBtDsEmId() {
		return btDsEmId;
	}

	public void setBtDsEmId(int btDsEmId) {
		this.btDsEmId = btDsEmId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsUuid() {
		return goodsUuid;
	}

	public void setGoodsUuid(String goodsUuid) {
		this.goodsUuid = goodsUuid;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public String getGoodsImportUrl() {
		return goodsImportUrl;
	}

	public void setGoodsImportUrl(String goodsImportUrl) {
		this.goodsImportUrl = goodsImportUrl;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public float getFreeShippingPrice() {
		return freeShippingPrice;
	}

	public void setFreeShippingPrice(float freeShippingPrice) {
		this.freeShippingPrice = freeShippingPrice;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOdDtId() {
		return odDtId;
	}

	public void setOdDtId(int odDtId) {
		this.odDtId = odDtId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public List<BatchDiscountPurchasPrice> getPurchasPriceList() {
		return purchasPriceList;
	}

	public void setPurchasPriceList(List<BatchDiscountPurchasPrice> purchasPriceList) {
		this.purchasPriceList = purchasPriceList;
	}

	@Override
	public String toString() {
		return "BatchDiscountEmailDetails [id=" + id + ", btDsEmId=" + btDsEmId + ", goodsId=" + goodsId
				+ ", goodsUuid=" + goodsUuid + ", goodsType=" + goodsType + ", goodsImg=" + goodsImg
				+ ", goodsImportUrl=" + goodsImportUrl + ", goodsUrl=" + goodsUrl + ", goodsPrice=" + goodsPrice
				+ ", freeShippingPrice=" + freeShippingPrice + ", goodsName=" + goodsName + ", orderNumber="
				+ orderNumber + ", odDtId=" + odDtId + ", createTime=" + createTime + ", valid=" + valid
				+ ", purchasPriceList=" + purchasPriceList + "]";
	}

}
