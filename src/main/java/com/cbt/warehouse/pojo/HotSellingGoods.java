package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.sql.Date;

/**
 * 热卖类别商品
 * 
 * @author jxw
 *
 */
public class HotSellingGoods implements Serializable {
	private static final long serialVersionUID = -22775418633L;

	private int id;
	private int hotSellingId;// 热卖类别id
	private String goodsPid;// 产品pid
	private String goodsName;// 产品名称
	private String showName;// 显示名称
	private String goodsUrl;// 商品链接
	private String goodsImg;// 商品图片链接
	private double goodsPrice;// 商品原价
	private String isOn;// 状态: 0:关闭 1:启用
	private double profitMargin;// 利润率
	private double sellingPrice;// 售价
	private double wholesalePrice_1;// 2件批发价
	private double wholesalePrice_2;// 5件批发价
	private double wholesalePrice_3;// 10件批发价
	private double wholesalePrice_4;// 批发价备用1
	private double wholesalePrice_5;// 批发价备用2
	private int createAdmid;// 创建人id
	private Date createTime;// 创建时间
	private int updateAdmid;// 更新人id
	private Date updateTime;// 更新时间
	private String rangePrice;
	private String wprice;
	private String feeprice;
	private int isNewCloud;
	private String goodsUnit;
	private String showPrice;
	private double amazonPrice;// 亚马逊价格
	private String asinCode;// 亚马逊asin码

	private int discountId;
	private double discountPercentage;
	private String discountBeginTime;
	private String discountEndTime;
	private int discountSort;

	private int evaluationId;
	private String evaluationSkuId;
	private int evaluationUserId;
	private String evaluationContent;
	private int evaluationLevel;
	private int evaluationServiceLevel;
	private String evaluationTime;

	private int isSoldFlag;
	private String maxPrice;
	private double virtualOldPrice;
	private int promotionFlag;//促销flag: 0不促销，1促销

	private String goodsPath;

	/**
	 * 1688货源异常但属于热卖区 = 26
	 */
	private int unsellableReason ;

	/**
	 * 美加可售卖标识
	 */
	private int salable;

	public int getSalable() {
		return salable;
	}

	public void setSalable(int salable) {
		this.salable = salable;
	}

	public int getUnsellableReason() {
		return unsellableReason;
	}

	public void setUnsellableReason(int unsellableReason) {
		this.unsellableReason = unsellableReason;
	}

	public String getGoodsPath() {
		return goodsPath;
	}

	public void setGoodsPath(String goodsPath) {
		this.goodsPath = goodsPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHotSellingId() {
		return hotSellingId;
	}

	public void setHotSellingId(int hotSellingId) {
		this.hotSellingId = hotSellingId;
	}

	public String getGoodsPid() {
		return goodsPid;
	}

	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getIsOn() {
		return isOn;
	}

	public void setIsOn(String isOn) {
		this.isOn = isOn;
	}

	public double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public double getWholesalePrice_1() {
		return wholesalePrice_1;
	}

	public void setWholesalePrice_1(double wholesalePrice_1) {
		this.wholesalePrice_1 = wholesalePrice_1;
	}

	public double getWholesalePrice_2() {
		return wholesalePrice_2;
	}

	public void setWholesalePrice_2(double wholesalePrice_2) {
		this.wholesalePrice_2 = wholesalePrice_2;
	}

	public double getWholesalePrice_3() {
		return wholesalePrice_3;
	}

	public void setWholesalePrice_3(double wholesalePrice_3) {
		this.wholesalePrice_3 = wholesalePrice_3;
	}

	public double getWholesalePrice_4() {
		return wholesalePrice_4;
	}

	public void setWholesalePrice_4(double wholesalePrice_4) {
		this.wholesalePrice_4 = wholesalePrice_4;
	}

	public double getWholesalePrice_5() {
		return wholesalePrice_5;
	}

	public void setWholesalePrice_5(double wholesalePrice_5) {
		this.wholesalePrice_5 = wholesalePrice_5;
	}

	public int getCreateAdmid() {
		return createAdmid;
	}

	public void setCreateAdmid(int createAdmid) {
		this.createAdmid = createAdmid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getUpdateAdmid() {
		return updateAdmid;
	}

	public void setUpdateAdmid(int updateAdmid) {
		this.updateAdmid = updateAdmid;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public String getWprice() {
		return wprice;
	}

	public void setWprice(String wprice) {
		this.wprice = wprice;
	}

	public int getIsNewCloud() {
		return isNewCloud;
	}

	public void setIsNewCloud(int isNewCloud) {
		this.isNewCloud = isNewCloud;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(String showPrice) {
		this.showPrice = showPrice;
	}

	public double getAmazonPrice() {
		return amazonPrice;
	}

	public void setAmazonPrice(double amazonPrice) {
		this.amazonPrice = amazonPrice;
	}

	public String getAsinCode() {
		return asinCode;
	}

	public void setAsinCode(String asinCode) {
		if(asinCode == null ){
			this.asinCode = "";
		}else{
			this.asinCode = asinCode;
		}	
	}

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getDiscountBeginTime() {
		return discountBeginTime;
	}

	public void setDiscountBeginTime(String discountBeginTime) {
		this.discountBeginTime = discountBeginTime;
	}

	public String getDiscountEndTime() {
		return discountEndTime;
	}

	public void setDiscountEndTime(String discountEndTime) {
		this.discountEndTime = discountEndTime;
	}

	public int getDiscountSort() {
		return discountSort;
	}

	public void setDiscountSort(int discountSort) {
		this.discountSort = discountSort;
	}

	public int getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(int evaluationId) {
		this.evaluationId = evaluationId;
	}

	public String getEvaluationSkuId() {
		return evaluationSkuId;
	}

	public void setEvaluationSkuId(String evaluationSkuId) {
		this.evaluationSkuId = evaluationSkuId;
	}

	public int getEvaluationUserId() {
		return evaluationUserId;
	}

	public void setEvaluationUserId(int evaluationUserId) {
		this.evaluationUserId = evaluationUserId;
	}

	public String getEvaluationContent() {
		return evaluationContent;
	}

	public void setEvaluationContent(String evaluationContent) {
		this.evaluationContent = evaluationContent;
	}

	public int getEvaluationLevel() {
		return evaluationLevel;
	}

	public void setEvaluationLevel(int evaluationLevel) {
		this.evaluationLevel = evaluationLevel;
	}

	public int getEvaluationServiceLevel() {
		return evaluationServiceLevel;
	}

	public void setEvaluationServiceLevel(int evaluationServiceLevel) {
		this.evaluationServiceLevel = evaluationServiceLevel;
	}

	public String getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(String evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public int getIsSoldFlag() {
		return isSoldFlag;
	}

	public void setIsSoldFlag(int isSoldFlag) {
		this.isSoldFlag = isSoldFlag;
	}

	public String getFeeprice() {
		return feeprice;
	}

	public void setFeeprice(String feeprice) {
		this.feeprice = feeprice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getVirtualOldPrice() {
		return virtualOldPrice;
	}

	public void setVirtualOldPrice(double virtualOldPrice) {
		this.virtualOldPrice = virtualOldPrice;
	}

	public int getPromotionFlag() {
		return promotionFlag;
	}

	public void setPromotionFlag(int promotionFlag) {
		this.promotionFlag = promotionFlag;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"hotSellingId\":\"" + hotSellingId + "\", \"goodsPid\":\"" + goodsPid
				+ "\", \"goodsName\":\"" + goodsName + "\", \"showName\":\"" + showName + "\", \"goodsUrl\":\""
				+ goodsUrl + "\", \"goodsImg\":\"" + goodsImg + "\", \"goodsPrice\":\"" + goodsPrice + "\", \"isOn\":\""
				+ isOn + "\", \"profitMargin\":\"" + profitMargin + "\", \"sellingPrice\":\"" + sellingPrice
				+ "\", \"createAdmid\":\"" + createAdmid + "\", \"createTime\":\"" + createTime
				+ "\", \"updateAdmid\":\"" + updateAdmid + "\", \"updateTime\":\"" + updateTime
				+ "\", \"rangePrice\":\"" + rangePrice + "\", \"wprice\":\"" + wprice + "\", \"isNewCloud\":\""
				+ isNewCloud + "\", \"goodsUnit\":\"" + goodsUnit + "\", \"showPrice\":\"" + showPrice
				+ "\", \"amazonPrice\":\"" + amazonPrice + "\", \"asinCode\":\"" + asinCode + "\"}";
	}

}
