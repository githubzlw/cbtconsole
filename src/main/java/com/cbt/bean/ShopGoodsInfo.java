package com.cbt.bean;

public class ShopGoodsInfo {

	private String shopId;
	private String categoryId;
	private String pid;
	private String imgUrl;
	private String price;
	private String wprice;
	private String rangePrice;
	private String showPrice;
	private String remotePath;
	private String enName;
	private String goodsUrl;
	private String weight;
	private String enInfo;
	private int valid;
	private int syncFlag;
	private String syncDescribe;
	private String syncRemark;
	private int enInfoNum;
	private String localPath;
	private int onlineFlag;// 线上标识，0非线上 1线上
	private int onlineValid;
	private int onlineEdit;

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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getWprice() {
		return wprice;
	}

	public void setWprice(String wprice) {
		this.wprice = wprice;
	}

	public String getRangePrice() {
		return rangePrice;
	}

	public void setRangePrice(String rangePrice) {
		this.rangePrice = rangePrice;
	}

	public String getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(String showPrice) {
		this.showPrice = showPrice;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getEnInfo() {
		return enInfo;
	}

	public void setEnInfo(String enInfo) {
		this.enInfo = enInfo;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
		if (syncFlag == 0) {
			this.syncDescribe = "未同步";
		} else if (syncFlag == 1) {
			this.syncDescribe = "同步成功";
		} else if (syncFlag == 2) {
			this.syncDescribe = "同步失败";
		}
	}

	public String getSyncDescribe() {
		return syncDescribe;
	}

	public void setSyncDescribe(String syncDescribe) {
		this.syncDescribe = syncDescribe;
	}

	public String getSyncRemark() {
		return syncRemark;
	}

	public void setSyncRemark(String syncRemark) {
		this.syncRemark = syncRemark;
	}

	public int getEnInfoNum() {
		return enInfoNum;
	}

	public void setEnInfoNum(int enInfoNum) {
		this.enInfoNum = enInfoNum;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public int getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(int onlineFlag) {
		this.onlineFlag = onlineFlag;
	}

	public int getOnlineValid() {
		return onlineValid;
	}

	public void setOnlineValid(int onlineValid) {
		this.onlineValid = onlineValid;
	}

	public int getOnlineEdit() {
		return onlineEdit;
	}

	public void setOnlineEdit(int onlineEdit) {
		this.onlineEdit = onlineEdit;
	}

	@Override
	public String toString() {
		return "{\"shopId\":\"" + shopId + "\", \"pid\":\"" + pid + "\", \"enInfo\":\"" + enInfo + "\"}";
	}

}
