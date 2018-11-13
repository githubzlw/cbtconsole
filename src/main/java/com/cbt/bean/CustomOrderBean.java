package com.cbt.bean;

import java.util.Date;

/**
 * CustomOrderBean
 */
public class CustomOrderBean {
	
	/**
	 * catId
	 */
	private int catId;
	
	/**
	 * catName
	 */
	private String catName;
	/**
	 * 原url
	 */
	private String sourceUrl;
	
	/**
	 * 原img
	 */
	private String sourceImg;
	
	public String getNewImg() {
		return newImg;
	}
	public void setNewImg(String newImg) {
		this.newImg = newImg;
	}

	/**
	 * 新img路径
	 */
	private String newImg;
	
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getSourceImg() {
		return sourceImg;
	}
	public void setSourceImg(String sourceImg) {
		this.sourceImg = sourceImg;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	private int id;
	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 用户名字
	 */
	private String userName;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 订单频率
	 */
	private String quantity;
	
	private String productName;
	
	private String photo;
	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	private String targetPrice;
	
	private String item;
	
	public String getTargetPrice() {
		return targetPrice;
	}
	public void setTargetPrice(String targetPrice) {
		this.targetPrice = targetPrice;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}

	private String customChange;
	
	private String questions;
	
	private int state;
	
	public String getCustomChange() {
		return customChange;
	}
	public void setCustomChange(String customChange) {
		this.customChange = customChange;
	}
	public String getQuestions() {
		return questions;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}

	/**
	 * 商品url
	 */
	private String purl;
	
	private String otherUrl;
	
	private String fromPrice;
	
	public String getOtherUrl() {
		return otherUrl;
	}
	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}
	public String getFromPrice() {
		return fromPrice;
	}
	public void setFromPrice(String fromPrice) {
		this.fromPrice = fromPrice;
	}

	/**
	 * 商品名
	 */
	private String pname;
	
	
	/**
	 * 图片
	 */
	private String img;
	
	/**
	 * 图片名
	 */
	private String imgName;
	
	private String keyWord;
	
	public int getTotal() {
		return total;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	private int total;
	
	private String indexNameCn;//索引名称中文
	
	
	public String getIndexNameCn() {
		return indexNameCn;
	}
	public void setIndexNameCn(String indexNameCn) {
		this.indexNameCn = indexNameCn;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	/**
	 * 相似分数
	 */
	private Double score;
	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}
	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}
	
	/**
	 * @return the purl
	 */
	public String getPurl() {
		return purl;
	}
	/**
	 * @param purl the purl to set
	 */
	public void setPurl(String purl) {
		this.purl = purl;
	}
	/**
	 * @return the pname
	 */
	public String getPname() {
		return pname;
	}
	/**
	 * @param pname the pname to set
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}
	/**
	 * @return the fprice
	 */
	public String getFprice() {
		return fprice;
	}
	/**
	 * @param fprice the fprice to set
	 */
	public void setFprice(String fprice) {
		this.fprice = fprice;
	}
	/**
	 * @return the minOrder
	 */
	public String getMinOrder() {
		return minOrder;
	}
	/**
	 * @param minOrder the minOrder to set
	 */
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
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
	 * 商品价格
	 */
	private String fprice;
	
	/**
	 * 商品最小订量
	 */
	private String minOrder;
	
	/**
	 * 商品货币
	 */
	private String currency;
	
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * 注释
	 */
	private String comment;
	
	/**
	 * 时间
	 */
	private Date createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
