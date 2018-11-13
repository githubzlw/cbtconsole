package com.cbt.bean;

import java.util.List;


/**
 * 替换商品Bean
 */
public class ProductChangeBean {


	//订单NO
	private String orderNo;
	//原商品ID
	private String goodId;
	//原图片
	private String aliImg;
	//原URL
	private String aliUrl;
	//原goodsCarURL
	private String aliGoodsCarUrl;
	//原goodsCar图片
	private String aliGoodsCarImg;
	//原价格
	private String aliPrice;
	//商品详情价格
	private String goodsPrice;
	//商品详情名字
	private String goodsName;
	//商品详情购物车id
	private String goodsCarId;
	//商品详情购物车id
	private String purchaseState;
	//购物车规格
	private String goodsType;
	//原价格
	private String aliName;
	//替换商品图片
	private String changeImg;
	//替换商品URL
	private String changeUrl;
	private String changePid;
	//替换商品价格
	private String changePrice;
	//替换商品价格
	private String changeName;
	//goodsdata:Pid
	private String pId;
	//替换商品标识
	private int changeFlag;
	//goodsdata新商品id
	private String gdId;
	//订单币种
	private String currency;
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the gdId
	 */
	public String getGdId() {
		return gdId;
	}
	/**
	 * @param gdId the gdId to set
	 */
	public void setGdId(String gdId) {
		this.gdId = gdId;
	}
	/**
	 * @return the aliGoodsCarUrl
	 */
	public String getAliGoodsCarUrl() {
		return aliGoodsCarUrl;
	}
	/**
	 * @param aliGoodsCarUrl the aliGoodsCarUrl to set
	 */
	public void setAliGoodsCarUrl(String aliGoodsCarUrl) {
		this.aliGoodsCarUrl = aliGoodsCarUrl;
	}
	/**
	 * @return the aliGoodsCarImg
	 */
	public String getAliGoodsCarImg() {
		return aliGoodsCarImg;
	}
	/**
	 * @param aliGoodsCarImg the aliGoodsCarImg to set
	 */
	public void setAliGoodsCarImg(String aliGoodsCarImg) {
		this.aliGoodsCarImg = aliGoodsCarImg;
	}
	
	/**
	 * @return the goodsType
	 */
	public String getGoodsType() {
		return goodsType;
	}
	/**
	 * @param goodsType the goodsType to set
	 */
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	
	/**
	 * @return the changePid
	 */
	public String getChangePid() {
		return changePid;
	}
	/**
	 * @param changePid the changePid to set
	 */
	public void setChangePid(String changePid) {
		this.changePid = changePid;
	}
	
	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * @param goodsName the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	/**
	 * @return the changeFlag
	 */
	public int getChangeFlag() {
		return changeFlag;
	}
	/**
	 * @param changeFlag the changeFlag to set
	 */
	public void setChangeFlag(int changeFlag) {
		this.changeFlag = changeFlag;
	}
	private List<ProductChangeBean> aliList;
	
	private List<ProductChangeBean> changeList;
	
	/**
	 * @return the goodsCarId
	 */
	public String getGoodsCarId() {
		return goodsCarId;
	}
	/**
	 * @param goodsCarId the goodsCarId to set
	 */
	public void setGoodsCarId(String goodsCarId) {
		this.goodsCarId = goodsCarId;
	}
	/**
	 * @return the purchaseState
	 */
	public String getPurchaseState() {
		return purchaseState;
	}
	/**
	 * @param purchaseState the purchaseState to set
	 */
	public void setPurchaseState(String purchaseState) {
		this.purchaseState = purchaseState;
	}
	
	/**
	 * @return the goodsPrice
	 */
	public String getGoodsPrice() {
		return goodsPrice;
	}
	/**
	 * @param goodsPrice the goodsPrice to set
	 */
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	/**
	 * @return the goodId
	 */
	public String getGoodId() {
		return goodId;
	}
	/**
	 * @param goodId the goodId to set
	 */
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
	/**
	 * @return the aliList
	 */
	public List<ProductChangeBean> getAliList() {
		return aliList;
	}
	/**
	 * @param aliList the aliList to set
	 */
	public void setAliList(List<ProductChangeBean> aliList) {
		this.aliList = aliList;
	}
	/**
	 * @return the changeList
	 */
	public List<ProductChangeBean> getChangeList() {
		return changeList;
	}
	/**
	 * @param changeList the changeList to set
	 */
	public void setChangeList(List<ProductChangeBean> changeList) {
		this.changeList = changeList;
	}
	/**
	 * @return the pId
	 */
	public String getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId) {
		this.pId = pId;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the aliImg
	 */
	public String getAliImg() {
		return aliImg;
	}
	/**
	 * @param aliImg the aliImg to set
	 */
	public void setAliImg(String aliImg) {
		this.aliImg = aliImg;
	}
	/**
	 * @return the aliUrl
	 */
	public String getAliUrl() {
		return aliUrl;
	}
	/**
	 * @param aliUrl the aliUrl to set
	 */
	public void setAliUrl(String aliUrl) {
		this.aliUrl = aliUrl;
	}
	/**
	 * @return the aliPrice
	 */
	public String getAliPrice() {
		return aliPrice;
	}
	/**
	 * @param aliPrice the aliPrice to set
	 */
	public void setAliPrice(String aliPrice) {
		this.aliPrice = aliPrice;
	}
	/**
	 * @return the aliName
	 */
	public String getAliName() {
		return aliName;
	}
	/**
	 * @param aliName the aliName to set
	 */
	public void setAliName(String aliName) {
		this.aliName = aliName;
	}
	/**
	 * @return the changeImg
	 */
	public String getChangeImg() {
		return changeImg;
	}
	/**
	 * @param changeImg the changeImg to set
	 */
	public void setChangeImg(String changeImg) {
		this.changeImg = changeImg;
	}
	/**
	 * @return the changeUrl
	 */
	public String getChangeUrl() {
		return changeUrl;
	}
	/**
	 * @param changeUrl the changeUrl to set
	 */
	public void setChangeUrl(String changeUrl) {
		this.changeUrl = changeUrl;
	}
	/**
	 * @return the changePrice
	 */
	public String getChangePrice() {
		return changePrice;
	}
	/**
	 * @param changePrice the changePrice to set
	 */
	public void setChangePrice(String changePrice) {
		this.changePrice = changePrice;
	}
	/**
	 * @return the changeName
	 */
	public String getChangeName() {
		return changeName;
	}
	/**
	 * @param changeName the changeName to set
	 */
	public void setChangeName(String changeName) {
		this.changeName = changeName;
	}
	
}
