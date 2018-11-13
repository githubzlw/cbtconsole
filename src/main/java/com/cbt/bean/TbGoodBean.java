package com.cbt.bean;


/**
 * @author zlw
 * 
 */
public class TbGoodBean {

	private String goodId;
	private String tbUrl;
	private String tbPrice;
	private String tbName;
	private String tbImg;
	private String shopId;
	//是人工判断的货源
	private String bmFlag;
	//货源对标情况 ：（1）精确对标 （2）近似对标 （3） 没找到对标 （理论上 线上 商品 不会有此状态）（4）成功卖过  (5) 未对标直接上传 （一般是同店商品上传）
	private String markFlag;
	//1688类别
	private String catId;
	private String ylUrl;
	private int pidSource;
	public int getPidSource() {
		return pidSource;
	}
	public void setPidSource(int pidSource) {
		this.pidSource = pidSource;
	}
	public String getYlImg() {
		return ylImg;
	}
	public void setYlImg(String ylImg) {
		this.ylImg = ylImg;
	}
	private String ylPid;
	private String sourceYlpid;
	private String ylImg;
	private String ylWeight;
	private String lotUnit;
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLotUnit() {
		return lotUnit;
	}
	public void setLotUnit(String lotUnit) {
		this.lotUnit = lotUnit;
	}
	public String getYlWeight() {
		return ylWeight;
	}
	public void setYlWeight(String ylWeight) {
		this.ylWeight = ylWeight;
	}
	/**
	 * 核心产品标识
	 */
	private int priorityFlag;
	
	/**
	 * 同店铺标识
	 */
	private int sourceProFlag;
	
	public int getPriorityFlag() {
		return priorityFlag;
	}
	public void setPriorityFlag(int priorityFlag) {
		this.priorityFlag = priorityFlag;
	}
	public int getSourceProFlag() {
		return sourceProFlag;
	}
	public void setSourceProFlag(int sourceProFlag) {
		this.sourceProFlag = sourceProFlag;
	}
	public String getSourceYlpid() {
		return sourceYlpid;
	}
	public void setSourceYlpid(String sourceYlpid) {
		this.sourceYlpid = sourceYlpid;
	}
	public String getYlUrl() {
		return ylUrl;
	}
	public void setYlUrl(String ylUrl) {
		this.ylUrl = ylUrl;
	}
	public String getYlPid() {
		return ylPid;
	}
	public void setYlPid(String ylPid) {
		this.ylPid = ylPid;
	}
	public String getYlPrice() {
		return ylPrice;
	}
	public void setYlPrice(String ylPrice) {
		this.ylPrice = ylPrice;
	}
	private String ylPrice;
	
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getBmFlag() {
		return bmFlag;
	}
	public void setBmFlag(String bmFlag) {
		this.bmFlag = bmFlag;
	}
	public String getMarkFlag() {
		return markFlag;
	}
	public void setMarkFlag(String markFlag) {
		this.markFlag = markFlag;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	/**
	 * @return the tbImg
	 */
	public String getTbImg() {
		return tbImg;
	}
	/**
	 * @param tbImg the tbImg to set
	 */
	public void setTbImg(String tbImg) {
		this.tbImg = tbImg;
	}
	private String tbFlag;
	/**
	 * @return the tbFlag
	 */
	public String getTbFlag() {
		return tbFlag;
	}
	/**
	 * @param tbFlag the tbFlag to set
	 */
	public void setTbFlag(String tbFlag) {
		this.tbFlag = tbFlag;
	}
	private int delFlag;
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
	 * @return the tbUrl
	 */
	public String getTbUrl() {
		return tbUrl;
	}
	/**
	 * @param tbUrl the tbUrl to set
	 */
	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}
	/**
	 * @return the tbPrice
	 */
	public String getTbPrice() {
		return tbPrice;
	}
	/**
	 * @param tbPrice the tbPrice to set
	 */
	public void setTbPrice(String tbPrice) {
		this.tbPrice = tbPrice;
	}
	/**
	 * @return the tbName
	 */
	public String getTbName() {
		return tbName;
	}
	/**
	 * @param tbName the tbName to set
	 */
	public void setTbName(String tbName) {
		this.tbName = tbName;
	}
	/**
	 * @return the delFlag
	 */
	public int getDelFlag() {
		return delFlag;
	}
	/**
	 * @param delFlag the delFlag to set
	 */
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	

}
