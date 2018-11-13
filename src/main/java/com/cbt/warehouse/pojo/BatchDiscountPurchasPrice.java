package com.cbt.warehouse.pojo;

import java.sql.Date;

/**
 * 批量采购价表
 * 
 * @author JXW
 *
 */
public class BatchDiscountPurchasPrice {

	private int id;
	private int goodsId;// 商品id
	private String goodsUuid;// 产品原始链接md5取值
	private int minQuantify;// 最小定量
	private int maxQuantify;// 最多定量
	private float purchasePrice;// 采购价格
	private float discountPrice;// 优惠价格
	private float discountAmount;// 单品优惠金额
	private float discountRate;// 单品优惠率
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private int adminId;// 更改人id
	private String adminName;// 更新销售名称
	private String valid;// 是否有效 0:无效,1有效

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getMinQuantify() {
		return minQuantify;
	}

	public void setMinQuantify(int minQuantify) {
		this.minQuantify = minQuantify;
	}

	public int getMaxQuantify() {
		return maxQuantify;
	}

	public void setMaxQuantify(int maxQuantify) {
		this.maxQuantify = maxQuantify;
	}

	public float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(float discountPrice) {
		this.discountPrice = discountPrice;
	}

	public float getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}

	public float getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(float discountRate) {
		this.discountRate = discountRate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "BatchDiscountPurchasPrice [id=" + id + ", goodsId=" + goodsId + ", goodsUuid=" + goodsUuid
				+ ", minQuantify=" + minQuantify + ", maxQuantify=" + maxQuantify + ", purchasePrice=" + purchasePrice
				+ ", discountPrice=" + discountPrice + ", discountAmount=" + discountAmount + ", discountRate="
				+ discountRate + ", createTime=" + createTime + ", updateTime=" + updateTime + ", adminId=" + adminId
				+ ", adminName=" + adminName + ", valid=" + valid + "]";
	}

}
