package com.cbt.bean;

/**
 * 
 * @ClassName ShopInfoBean
 * @Description 店铺信息
 * @author Jxw
 * @date 2018年2月23日
 */
public class ShopInfoBean {
	private String shopId;// 店铺ID
	private String categoryId;// 类别ID
	private String categoryName;// 类别名称
	private int goodsNum;// 商品数量
	private String weightInterval = "0.00-0.00";// 重量情况(区间)
	private float weightVal;// 重量设定
	private float firstIntervalRate;// 第1区间利润率
	private double suggestRate;// 第1区间建议利润率
	private double totalRate;// cid总利润率
	private int totalNum;// cid总商品数量
	private float otherIntervalRate;// 第2,3区间利润率
	private int adminId;// 操作人ID
	private float minWeight;// 最小重量
	private float maxWeight;// 最大重量
	private int isChoose;// 是否选择原始重量 0平均重量 1原始重量
	private String keyWeight;//关键词重量
	private int isForbid;// 0不禁止 1禁止

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getWeightInterval() {
		return weightInterval;
	}

	public void setWeightInterval(String weightInterval) {
		if(!(weightInterval == null || "".equals(weightInterval))){
			this.weightInterval = weightInterval;
		}	
	}

	public float getWeightVal() {
		return weightVal;
	}

	public void setWeightVal(float weightVal) {
		this.weightVal = weightVal;
	}

	public float getFirstIntervalRate() {
		return firstIntervalRate;
	}

	public void setFirstIntervalRate(float firstIntervalRate) {
		this.firstIntervalRate = firstIntervalRate;
	}

	public double getSuggestRate() {
		return suggestRate;
	}

	public void setSuggestRate(double suggestRate) {
		this.suggestRate = suggestRate;
	}

	public double getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(double totalRate) {
		this.totalRate = totalRate;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public float getOtherIntervalRate() {
		return otherIntervalRate;
	}

	public void setOtherIntervalRate(float otherIntervalRate) {
		this.otherIntervalRate = otherIntervalRate;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public float getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(float minWeight) {
		this.minWeight = minWeight;
	}

	public float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}

	public int getIsChoose() {
		return isChoose;
	}

	public void setIsChoose(int isChoose) {
		this.isChoose = isChoose;
	}
	
	

	public String getKeyWeight() {
		return keyWeight;
	}

	public void setKeyWeight(String keyWeight) {
		this.keyWeight = keyWeight;
	}

	public int getIsForbid() {
		return isForbid;
	}

	public void setIsForbid(int isForbid) {
		this.isForbid = isForbid;
	}

	@Override
	public String toString() {
		return "ShopInfoBean{" +
				"shopId='" + shopId + '\'' +
				", categoryId='" + categoryId + '\'' +
				", categoryName='" + categoryName + '\'' +
				", goodsNum=" + goodsNum +
				", weightInterval='" + weightInterval + '\'' +
				", weightVal=" + weightVal +
				", firstIntervalRate=" + firstIntervalRate +
				", suggestRate=" + suggestRate +
				", totalRate=" + totalRate +
				", totalNum=" + totalNum +
				", otherIntervalRate=" + otherIntervalRate +
				", adminId=" + adminId +
				", minWeight=" + minWeight +
				", maxWeight=" + maxWeight +
				", isChoose=" + isChoose +
				", keyWeight='" + keyWeight + '\'' +
				", isForbid=" + isForbid +
				'}';
	}
}
