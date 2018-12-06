package com.cbt.bean;

/**
 * 
 * @ClassName SameTypeGoodsBean
 * @Description 同款商品bean
 * @author Jxw
 * @date 2018年1月24日
 */
public class SameTypeGoodsBean {
	private int id;
	private String goodsPid;// pid
	private String goodsName;// 名称
	private String goodsImg;// 主图
	private double goodsPrice;// 商品价格
	private int minOrderNum;// 最小定量
	private String goodUrl;// 商品链接
	private String expressUrl;// 电商网站链接
	private String createTime;
	private int sourceFlag;// 产品来源 1:1688,2:ali,3:新品云
	private int adminId;// 输入人id
	private String adminName;// 输入人
	private int typeFlag;// 类别 0未分类 1女装 2男装 3童装 4包包 5鞋子
	private int totalNum;// 同款商品总数
	private int successNum;// 同款商品成功数量
	private int crawlFlag;// 流程第1步 --抓取标识 0未抓完 1抓完 4抓取错误
	private int clearFlag;// 流程第2步--商品数据是否清洗 0未清洗 1已清洗
	private int valid;// 流程第2步--清洗标识 0数据无效 1数据有效
	private int imgDownFlag;// 流程第3步--图片是否下载标识 0未下载 1已下载
	private int syncFlag;// 流程第4步--同步，0：未同步 1同步成功 2同步失败
	private String syncRemark;// 同步失败原因
	private int delFlag;// 删除标识
	private int isOn;// 开启标识
	private double aveWeight;// 平均重量
	private int opFlag;//操作标识 0 1 2
	private int drainageFlag;//引流标识 0非引流 1引流
	private int goodsType;//对标类型 0默认 1阿里对标商品 2亚马逊对标商品
	private String shopId;// 店铺ID
	private int shopGoodsNum;// 店铺数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getMinOrderNum() {
		return minOrderNum;
	}

	public void setMinOrderNum(int minOrderNum) {
		this.minOrderNum = minOrderNum;
	}

	public String getGoodUrl() {
		return goodUrl;
	}

	public void setGoodUrl(String goodUrl) {
		this.goodUrl = goodUrl;
	}

	public String getExpressUrl() {
		return expressUrl;
	}

	public void setExpressUrl(String expressUrl) {
		this.expressUrl = expressUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(int sourceFlag) {
		this.sourceFlag = sourceFlag;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
		if (adminId == 0) {
			this.adminName = "";
		}
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public int getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(int typeFlag) {
		this.typeFlag = typeFlag;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getCrawlFlag() {
		return crawlFlag;
	}

	public void setCrawlFlag(int crawlFlag) {
		this.crawlFlag = crawlFlag;
	}

	public int getClearFlag() {
		return clearFlag;
	}

	public void setClearFlag(int clearFlag) {
		this.clearFlag = clearFlag;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getImgDownFlag() {
		return imgDownFlag;
	}

	public void setImgDownFlag(int imgDownFlag) {
		this.imgDownFlag = imgDownFlag;
	}

	public int getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(int syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getSyncRemark() {
		return syncRemark;
	}

	public void setSyncRemark(String syncRemark) {
		this.syncRemark = syncRemark;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public int getIsOn() {
		return isOn;
	}

	public void setIsOn(int isOn) {
		this.isOn = isOn;
	}

	public double getAveWeight() {
		return aveWeight;
	}

	public void setAveWeight(double aveWeight) {
		this.aveWeight = aveWeight;
	}

	public int getOpFlag() {
		return opFlag;
	}

	public void setOpFlag(int opFlag) {
		this.opFlag = opFlag;
	}

	public int getDrainageFlag() {
		return drainageFlag;
	}

	public void setDrainageFlag(int drainageFlag) {
		this.drainageFlag = drainageFlag;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public int getShopGoodsNum() {
		return shopGoodsNum;
	}

	public void setShopGoodsNum(int shopGoodsNum) {
		this.shopGoodsNum = shopGoodsNum;
	}
}
