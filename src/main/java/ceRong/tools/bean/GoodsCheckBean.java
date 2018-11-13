package ceRong.tools.bean;

import java.util.List;


/**
 * GoodsCheckBean
 */
public class GoodsCheckBean {
	
	
	/**
	 * orderNo
	 */
	private String orderNo;
	
	/**
	 * userName
	 */
	private String userName;
	
	/**
	 * email
	 */
	private String email;
	
	/**
	 * createtime
	 */
	private String createtime;
	
	/**
	 * productCost
	 */
	private String productCost;
	
	/**
	 * 利润
	 */
	private String profit;
	
	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public int getAmzSales() {
		return amzSales;
	}

	public void setAmzSales(int amzSales) {
		this.amzSales = amzSales;
	}

	/**
	 * 亚马逊销量
	 */
	private int amzSales;
	
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the createtime
	 */
	public String getCreatetime() {
		return createtime;
	}

	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	/**
	 * @return the productCost
	 */
	public String getProductCost() {
		return productCost;
	}

	/**
	 * @param productCost the productCost to set
	 */
	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}

	/**
	 * pId
	 */
	private int pId;
	
	/**
	 * goodsName
	 */
	private String goodsName;
	
	/**
	 * styId
	 */
	private String styId;
	
	/**
	 * @return the styId
	 */
	public String getStyId() {
		return styId;
	}

	/**
	 * @param styId the styId to set
	 */
	public void setStyId(String styId) {
		this.styId = styId;
	}

	/**
	 * @return the styImg
	 */
	public String getStyImg() {
		return styImg;
	}

	/**
	 * @param styImg the styImg to set
	 */
	public void setStyImg(String styImg) {
		this.styImg = styImg;
	}

	/**
	 * @return the styType
	 */
	public String getStyType() {
		return styType;
	}

	/**
	 * @param styType the styType to set
	 */
	public void setStyType(String styType) {
		this.styType = styType;
	}

	/**
	 * @return the styValue
	 */
	public String getStyValue() {
		return styValue;
	}

	/**
	 * @param styValue the styValue to set
	 */
	public void setStyValue(String styValue) {
		this.styValue = styValue;
	}

	/**
	 * aliStyImg
	 */
	private String aliStyImg;
	
	/**
	 * @return the aliStyImg
	 */
	public String getAliStyImg() {
		return aliStyImg;
	}

	/**
	 * @param aliStyImg the aliStyImg to set
	 */
	public void setAliStyImg(String aliStyImg) {
		this.aliStyImg = aliStyImg;
	}

	/**
	 * @return the aliStyType
	 */
	public String getAliStyType() {
		return aliStyType;
	}

	/**
	 * @param aliStyType the aliStyType to set
	 */
	public void setAliStyType(String aliStyType) {
		this.aliStyType = aliStyType;
	}

	/**
	 * @return the aliStyValue
	 */
	public String getAliStyValue() {
		return aliStyValue;
	}

	/**
	 * @param aliStyValue the aliStyValue to set
	 */
	public void setAliStyValue(String aliStyValue) {
		this.aliStyValue = aliStyValue;
	}

	/**
	 * aliStyType
	 */
	private String aliStyType;
	
	/**
	 * aliStyValue
	 */
	private String aliStyValue;
	
	/**
	 * styImg
	 */
	private String styImg;
	
	/**
	 * styType
	 */
	private String styType;
	
	/**
	 * styType
	 */
	private String styValue;
	
	/**
	 * styImg1
	 */
	private String styImg1;
	
	/**
	 * styType1
	 */
	private String styType1;
	
	/**
	 * styType1
	 */
	private String styValue1;
	
	/**
	 * styImg2
	 */
	private String styImg2;
	
	/**
	 * styType2
	 */
	private String styType2;
	
	/**
	 * styType2
	 */
	private String styValue2;
	
	/**
	 * styImg3
	 */
	private String styImg3;
	
	/**
	 * styType3
	 */
	private String styType3;
	
	/**
	 * styType3
	 */
	private String styValue3;
	
	/**
	 * pictureList
	 */
	private List<GoodsCheckBean> pictureList;
	
	/**
	 * aliSourceList
	 */
	private List<GoodsCheckBean> aliSourceList;
	
	/**
	 * aliSourceUrl
	 */
	private String aliSourceUrl;
	
	/**
	 * aligSourceUrl
	 */
	private String aligSourceUrl;
	
	/**
	 * aliSourceOrderNo
	 */
	private String aliSourceOrderNo;
	
	/**
	 * @return the aligSourceUrl
	 */
	public String getAligSourceUrl() {
		return aligSourceUrl;
	}

	/**
	 * @return the aliSourceOrderNo
	 */
	public String getAliSourceOrderNo() {
		return aliSourceOrderNo;
	}

	/**
	 * @param aliSourceOrderNo the aliSourceOrderNo to set
	 */
	public void setAliSourceOrderNo(String aliSourceOrderNo) {
		this.aliSourceOrderNo = aliSourceOrderNo;
	}

	/**
	 * @param aligSourceUrl the aligSourceUrl to set
	 */
	public void setAligSourceUrl(String aligSourceUrl) {
		this.aligSourceUrl = aligSourceUrl;
	}

	/**
	 * aliSourceImgUrl
	 */
	private String aliSourceImgUrl;
	
	/**
	 * amzUrl
	 */
	private String amzUrl;
	
	/**
	 * amzImg
	 */
	private String amzImg;
	
	public String getAmzUrl() {
		return amzUrl;
	}

	public void setAmzUrl(String amzUrl) {
		this.amzUrl = amzUrl;
	}

	public String getAmzImg() {
		return amzImg;
	}

	public void setAmzImg(String amzImg) {
		this.amzImg = amzImg;
	}

	public String getAmzPrice() {
		return amzPrice;
	}

	public void setAmzPrice(String amzPrice) {
		this.amzPrice = amzPrice;
	}

	/**
	 * amzPrice
	 */
	private String amzPrice;
	
	public String getAmzName() {
		return amzName;
	}

	public void setAmzName(String amzName) {
		this.amzName = amzName;
	}

	/**
	 * amzName
	 */
	private String amzName;
	
	/**
	 * aliSourcePrice
	 */
	private String aliSourcePrice;
	
	/**
	 * aliSourceName
	 */
	private String aliSourceName;
	
	/**
	 * @return the aliSourceList
	 */
	public List<GoodsCheckBean> getAliSourceList() {
		return aliSourceList;
	}

	/**
	 * @param aliSourceList the aliSourceList to set
	 */
	public void setAliSourceList(List<GoodsCheckBean> aliSourceList) {
		this.aliSourceList = aliSourceList;
	}

	/**
	 * @return the aliSourceUrl
	 */
	public String getAliSourceUrl() {
		return aliSourceUrl;
	}

	/**
	 * @param aliSourceUrl the aliSourceUrl to set
	 */
	public void setAliSourceUrl(String aliSourceUrl) {
		this.aliSourceUrl = aliSourceUrl;
	}

	/**
	 * @return the aliSourceImgUrl
	 */
	public String getAliSourceImgUrl() {
		return aliSourceImgUrl;
	}

	/**
	 * @param aliSourceImgUrl the aliSourceImgUrl to set
	 */
	public void setAliSourceImgUrl(String aliSourceImgUrl) {
		this.aliSourceImgUrl = aliSourceImgUrl;
	}

	/**
	 * @return the aliSourcePrice
	 */
	public String getAliSourcePrice() {
		return aliSourcePrice;
	}

	/**
	 * @param aliSourcePrice the aliSourcePrice to set
	 */
	public void setAliSourcePrice(String aliSourcePrice) {
		this.aliSourcePrice = aliSourcePrice;
	}

	/**
	 * @return the aliSourceName
	 */
	public String getAliSourceName() {
		return aliSourceName;
	}

	/**
	 * @param aliSourceName the aliSourceName to set
	 */
	public void setAliSourceName(String aliSourceName) {
		this.aliSourceName = aliSourceName;
	}


	
	/**
	 * @return the pictureList
	 */
	public List<GoodsCheckBean> getPictureList() {
		return pictureList;
	}

	/**
	 * @param pictureList the pictureList to set
	 */
	public void setPictureList(List<GoodsCheckBean> pictureList) {
		this.pictureList = pictureList;
	}

	/**
	 * styImgList
	 */
	private List<GoodsCheckBean> styImgList;
	
	/**
	 * styImgList1
	 */
	private List<GoodsCheckBean> styImgList1;
	
	/**
	 * styImgList2
	 */
	private List<GoodsCheckBean> styImgList2;
	
	/**
	 * styImgList3
	 */
	private List<GoodsCheckBean> styImgList3;
	
	/**
	 * @return the aliStyleList
	 */
	public List<GoodsCheckBean> getAliStyleList() {
		return aliStyleList;
	}

	/**
	 * @param aliStyleList the aliStyleList to set
	 */
	public void setAliStyleList(List<GoodsCheckBean> aliStyleList) {
		this.aliStyleList = aliStyleList;
	}

	/**
	 * aliStyleList
	 */
	private List<GoodsCheckBean> aliStyleList;
	
	/**
	 * @return the styImgList
	 */
	public List<GoodsCheckBean> getStyImgList() {
		return styImgList;
	}

	/**
	 * @param styImgList the styImgList to set
	 */
	public void setStyImgList(List<GoodsCheckBean> styImgList) {
		this.styImgList = styImgList;
	}

	/**
	 * @return the styTypeList
	 */
	public List<String> getStyTypeList() {
		return styTypeList;
	}

	/**
	 * @param styTypeList the styTypeList to set
	 */
	public void setStyTypeList(List<String> styTypeList) {
		this.styTypeList = styTypeList;
	}

	/**
	 * @return the styValueList
	 */
	public List<GoodsCheckBean> getStyValueList() {
		return styValueList;
	}

	/**
	 * @param styValueList the styValueList to set
	 */
	public void setStyValueList(List<GoodsCheckBean> styValueList) {
		this.styValueList = styValueList;
	}

	/**
	 * styTypeList
	 */
	private List<String> styTypeList;
	
	/**
	 * styValueList
	 */
	private List<GoodsCheckBean> styValueList;
	
	/**
	 * @return the styImg1
	 */
	public String getStyImg1() {
		return styImg1;
	}

	/**
	 * @param styImg1 the styImg1 to set
	 */
	public void setStyImg1(String styImg1) {
		this.styImg1 = styImg1;
	}

	/**
	 * @return the styType1
	 */
	public String getStyType1() {
		return styType1;
	}

	/**
	 * @param styType1 the styType1 to set
	 */
	public void setStyType1(String styType1) {
		this.styType1 = styType1;
	}

	/**
	 * @return the styValue1
	 */
	public String getStyValue1() {
		return styValue1;
	}

	/**
	 * @param styValue1 the styValue1 to set
	 */
	public void setStyValue1(String styValue1) {
		this.styValue1 = styValue1;
	}

	/**
	 * @return the styImg2
	 */
	public String getStyImg2() {
		return styImg2;
	}

	/**
	 * @param styImg2 the styImg2 to set
	 */
	public void setStyImg2(String styImg2) {
		this.styImg2 = styImg2;
	}

	/**
	 * @return the styType2
	 */
	public String getStyType2() {
		return styType2;
	}

	/**
	 * @param styType2 the styType2 to set
	 */
	public void setStyType2(String styType2) {
		this.styType2 = styType2;
	}

	/**
	 * @return the styValue2
	 */
	public String getStyValue2() {
		return styValue2;
	}

	/**
	 * @param styValue2 the styValue2 to set
	 */
	public void setStyValue2(String styValue2) {
		this.styValue2 = styValue2;
	}

	/**
	 * @return the styImg3
	 */
	public String getStyImg3() {
		return styImg3;
	}

	/**
	 * @param styImg3 the styImg3 to set
	 */
	public void setStyImg3(String styImg3) {
		this.styImg3 = styImg3;
	}

	/**
	 * @return the styType3
	 */
	public String getStyType3() {
		return styType3;
	}

	/**
	 * @param styType3 the styType3 to set
	 */
	public void setStyType3(String styType3) {
		this.styType3 = styType3;
	}

	/**
	 * @return the styValue3
	 */
	public String getStyValue3() {
		return styValue3;
	}

	/**
	 * @param styValue3 the styValue3 to set
	 */
	public void setStyValue3(String styValue3) {
		this.styValue3 = styValue3;
	}

	/**
	 * @return the styImgList1
	 */
	public List<GoodsCheckBean> getStyImgList1() {
		return styImgList1;
	}

	/**
	 * @param styImgList1 the styImgList1 to set
	 */
	public void setStyImgList1(List<GoodsCheckBean> styImgList1) {
		this.styImgList1 = styImgList1;
	}

	/**
	 * @return the styImgList2
	 */
	public List<GoodsCheckBean> getStyImgList2() {
		return styImgList2;
	}

	/**
	 * @param styImgList2 the styImgList2 to set
	 */
	public void setStyImgList2(List<GoodsCheckBean> styImgList2) {
		this.styImgList2 = styImgList2;
	}

	/**
	 * @return the styImgList3
	 */
	public List<GoodsCheckBean> getStyImgList3() {
		return styImgList3;
	}

	/**
	 * @param styImgList3 the styImgList3 to set
	 */
	public void setStyImgList3(List<GoodsCheckBean> styImgList3) {
		this.styImgList3 = styImgList3;
	}

	/**
	 * @return the styValueList1
	 */
	public List<GoodsCheckBean> getStyValueList1() {
		return styValueList1;
	}

	/**
	 * @param styValueList1 the styValueList1 to set
	 */
	public void setStyValueList1(List<GoodsCheckBean> styValueList1) {
		this.styValueList1 = styValueList1;
	}

	/**
	 * @return the styValueList2
	 */
	public List<GoodsCheckBean> getStyValueList2() {
		return styValueList2;
	}

	/**
	 * @param styValueList2 the styValueList2 to set
	 */
	public void setStyValueList2(List<GoodsCheckBean> styValueList2) {
		this.styValueList2 = styValueList2;
	}

	/**
	 * @return the styValueList3
	 */
	public List<GoodsCheckBean> getStyValueList3() {
		return styValueList3;
	}

	/**
	 * @param styValueList3 the styValueList3 to set
	 */
	public void setStyValueList3(List<GoodsCheckBean> styValueList3) {
		this.styValueList3 = styValueList3;
	}

	/**
	 * styValueList1
	 */
	private List<GoodsCheckBean> styValueList1;
	
	/**
	 * styValueList2
	 */
	private List<GoodsCheckBean> styValueList2;
	
	/**
	 * styValueList3
	 */
	private List<GoodsCheckBean> styValueList3;
	
	/**
	 * url
	 */
	private String url;
	
	/**
	 * imgurl
	 */
	private String imgurl;
	
	/**
	 * tbFlag
	 */
	private String tbFlag;
	
	/**
	 * @return the tbflagname
	 */
	public String getTbflagname() {
		return tbflagname;
	}

	/**
	 * @param tbflagname the tbflagname to set
	 */
	public void setTbflagname(String tbflagname) {
		this.tbflagname = tbflagname;
	}

	/**
	 * tbflagname
	 */
	private String tbflagname;
	
	/**
	 * tbFlag1
	 */
	private String tbFlag1;
	
	/**
	 * tbFlag2
	 */
	private String tbFlag2;
	
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

	/**
	 * @return the tbFlag1
	 */
	public String getTbFlag1() {
		return tbFlag1;
	}

	/**
	 * @param tbFlag1 the tbFlag1 to set
	 */
	public void setTbFlag1(String tbFlag1) {
		this.tbFlag1 = tbFlag1;
	}

	/**
	 * @return the tbFlag2
	 */
	public String getTbFlag2() {
		return tbFlag2;
	}

	/**
	 * @param tbFlag2 the tbFlag2 to set
	 */
	public void setTbFlag2(String tbFlag2) {
		this.tbFlag2 = tbFlag2;
	}

	/**
	 * @return the tbFlag3
	 */
	public String getTbFlag3() {
		return tbFlag3;
	}

	/**
	 * @param tbFlag3 the tbFlag3 to set
	 */
	public void setTbFlag3(String tbFlag3) {
		this.tbFlag3 = tbFlag3;
	}

	/**
	 * tbFlag3
	 */
	private String tbFlag3;
	
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
	 * imgpath
	 */
	private String imgpath;
	
	/**
	 * aliExpressprice
	 */
	private String price;
	
	/**
	 * priceRmb
	 */
	private String priceRmb;
	
	/**
	 * @return the priceRmb
	 */
	public String getPriceRmb() {
		return priceRmb;
	}

	/**
	 * @param priceRmb the priceRmb to set
	 */
	public void setPriceRmb(String priceRmb) {
		this.priceRmb = priceRmb;
	}

	/**
	 * sell
	 */
	private String sell;
	
	/**
	 * tbImg
	 */
	private String tbImg;
	
	/**
	 * tbUrl
	 */
	private String tbUrl;
	
	/**
	 * tbName
	 */
	private String tbName;
	
	/**
	 * tbName1
	 */
	private String tbName1;
	/**
	 * tbName2
	 */
	private String tbName2;
	/**
	 * tbName3
	 */
	private String tbName3;
	
	/**
	 * change:goodsPurl
	 */
	private String goodsPurl;
	
	/**
	 * change:goodsPrice
	 */
	private String goodsPrice;
	
	/**
	 * 产品id
	 */
	private long offerId;
	
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 原图片
	 */
	private String sourceImg;
	
	/**
	 * 原链接
	 */
	private String sourceUrl;
	
	/**
	 * 新图片
	 */
	private String newImg;
	
	/**
	 * 新链接
	 */
	private String newUrl;
	
	/**
	 * 相似分数
	 */
	private Double score;
	
	/**
	 * 新价格
	 */
	private Double newPrice;
	
	/**
	 * 新名字
	 */
	private String newName;
	
	/**
	 * 类别ID
	 */
	private int catId;
	
	public long getOfferId() {
		return offerId;
	}

	public void setOfferId(long offerId) {
		this.offerId = offerId;
	}

	public String getSourceImg() {
		return sourceImg;
	}

	public void setSourceImg(String sourceImg) {
		this.sourceImg = sourceImg;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getNewImg() {
		return newImg;
	}

	public void setNewImg(String newImg) {
		this.newImg = newImg;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	public Double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
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

	/**
	 * 新名字
	 */
	private String catName;
	
	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
	/**
	 * @return the goodsPurl
	 */
	public String getGoodsPurl() {
		return goodsPurl;
	}

	/**
	 * @param goodsPurl the goodsPurl to set
	 */
	public void setGoodsPurl(String goodsPurl) {
		this.goodsPurl = goodsPurl;
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
	 * @return the chaGoodsName
	 */
	public String getChaGoodsName() {
		return chaGoodsName;
	}

	/**
	 * @param chaGoodsName the chaGoodsName to set
	 */
	public void setChaGoodsName(String chaGoodsName) {
		this.chaGoodsName = chaGoodsName;
	}

	/**
	 * @return the goodsImgUrl
	 */
	public String getGoodsImgUrl() {
		return goodsImgUrl;
	}

	/**
	 * @param goodsImgUrl the goodsImgUrl to set
	 */
	public void setGoodsImgUrl(String goodsImgUrl) {
		this.goodsImgUrl = goodsImgUrl;
	}

	/**
	 * change:chaGoodsName
	 */
	private String chaGoodsName;
	
	/**
	 * change:goodsImgUrl
	 */
	private String goodsImgUrl;
	
	
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
	 * @return the tbName1
	 */
	public String getTbName1() {
		return tbName1;
	}

	/**
	 * @param tbName1 the tbName1 to set
	 */
	public void setTbName1(String tbName1) {
		this.tbName1 = tbName1;
	}

	/**
	 * @return the tbName2
	 */
	public String getTbName2() {
		return tbName2;
	}

	/**
	 * @param tbName2 the tbName2 to set
	 */
	public void setTbName2(String tbName2) {
		this.tbName2 = tbName2;
	}

	/**
	 * @return the tbName3
	 */
	public String getTbName3() {
		return tbName3;
	}

	/**
	 * @param tbName3 the tbName3 to set
	 */
	public void setTbName3(String tbName3) {
		this.tbName3 = tbName3;
	}

	/**
	 * tbImg1
	 */
	private String tbImg1;
	
	private String style0;
	
	private String style1;
	
	/**
	 * @return the style0
	 */
	public String getStyle0() {
		return style0;
	}

	/**
	 * @param style0 the style0 to set
	 */
	public void setStyle0(String style0) {
		this.style0 = style0;
	}

	/**
	 * @return the style1
	 */
	public String getStyle1() {
		return style1;
	}

	/**
	 * @param style1 the style1 to set
	 */
	public void setStyle1(String style1) {
		this.style1 = style1;
	}

	/**
	 * @return the style2
	 */
	public String getStyle2() {
		return style2;
	}

	/**
	 * @param style2 the style2 to set
	 */
	public void setStyle2(String style2) {
		this.style2 = style2;
	}

	/**
	 * @return the style3
	 */
	public String getStyle3() {
		return style3;
	}

	/**
	 * @param style3 the style3 to set
	 */
	public void setStyle3(String style3) {
		this.style3 = style3;
	}

	private String style2;
	
	private String style3;
	
	private String alStyle;
	
	private String imgcheck0;
	
	private String imgcheck1;
	
	private String imgcheck2;
	
	/**
	 * @return the imgcheck0
	 */
	public String getImgcheck0() {
		return imgcheck0;
	}

	/**
	 * @param imgcheck0 the imgcheck0 to set
	 */
	public void setImgcheck0(String imgcheck0) {
		this.imgcheck0 = imgcheck0;
	}

	/**
	 * @return the imgcheck1
	 */
	public String getImgcheck1() {
		return imgcheck1;
	}

	/**
	 * @param imgcheck1 the imgcheck1 to set
	 */
	public void setImgcheck1(String imgcheck1) {
		this.imgcheck1 = imgcheck1;
	}

	/**
	 * @return the imgcheck2
	 */
	public String getImgcheck2() {
		return imgcheck2;
	}

	/**
	 * @param imgcheck2 the imgcheck2 to set
	 */
	public void setImgcheck2(String imgcheck2) {
		this.imgcheck2 = imgcheck2;
	}

	/**
	 * @return the imgcheck3
	 */
	public String getImgcheck3() {
		return imgcheck3;
	}

	/**
	 * @param imgcheck3 the imgcheck3 to set
	 */
	public void setImgcheck3(String imgcheck3) {
		this.imgcheck3 = imgcheck3;
	}

	private String imgcheck3;
	
	/**
	 * @return the alStyle
	 */
	public String getAlStyle() {
		return alStyle;
	}

	/**
	 * @param alStyle the alStyle to set
	 */
	public void setAlStyle(String alStyle) {
		this.alStyle = alStyle;
	}

	/**
	 * tbUrl1
	 */
	private String tbUrl1;
	
	/**
	 * tbImg2
	 */
	private String tbImg2;
	
	/**
	 * tbUrl2
	 */
	private String tbUrl2;
	
	/**
	 * tbImg3
	 */
	private String tbImg3;
	
	/**
	 * tbUrl3
	 */
	private String tbUrl3;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the imgurl
	 */
	public String getImgurl() {
		return imgurl;
	}

	/**
	 * @param imgurl the imgurl to set
	 */
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	/**
	 * @return the imgpath
	 */
	public String getImgpath() {
		return imgpath;
	}

	/**
	 * @param imgpath the imgpath to set
	 */
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the sell
	 */
	public String getSell() {
		return sell;
	}

	/**
	 * @param sell the sell to set
	 */
	public void setSell(String sell) {
		this.sell = sell;
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
	 * @return the tbUrl1
	 */
	public String getTbUrl1() {
		return tbUrl1;
	}

	/**
	 * @param tbUrl1 the tbUrl1 to set
	 */
	public void setTbUrl1(String tbUrl1) {
		this.tbUrl1 = tbUrl1;
	}

	/**
	 * @return the tbUrl2
	 */
	public String getTbUrl2() {
		return tbUrl2;
	}

	/**
	 * @param tbUrl2 the tbUrl2 to set
	 */
	public void setTbUrl2(String tbUrl2) {
		this.tbUrl2 = tbUrl2;
	}

	/**
	 * @return the tbUrl3
	 */
	public String getTbUrl3() {
		return tbUrl3;
	}

	/**
	 * @param tbUrl3 the tbUrl3 to set
	 */
	public void setTbUrl3(String tbUrl3) {
		this.tbUrl3 = tbUrl3;
	}

	/**
	 * @return the pId
	 */
	public int getpId() {
		return pId;
	}

	/**
	 * @param pId the pId to set
	 */
	public void setpId(int pId) {
		this.pId = pId;
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

	/**
	 * @return the tbImg1
	 */
	public String getTbImg1() {
		return tbImg1;
	}

	/**
	 * @param tbImg1 the tbImg1 to set
	 */
	public void setTbImg1(String tbImg1) {
		this.tbImg1 = tbImg1;
	}

	/**
	 * @return the tbImg2
	 */
	public String getTbImg2() {
		return tbImg2;
	}

	/**
	 * @param tbImg2 the tbImg2 to set
	 */
	public void setTbImg2(String tbImg2) {
		this.tbImg2 = tbImg2;
	}

	/**
	 * @return the tbImg3
	 */
	public String getTbImg3() {
		return tbImg3;
	}

	/**
	 * @param tbImg3 the tbImg3 to set
	 */
	public void setTbImg3(String tbImg3) {
		this.tbImg3 = tbImg3;
	}


	/**
	 * imgCheck
	 */
	private int imgCheck0;
	
	/**
	 * @return the imgCheck0
	 */
	public int getImgCheck0() {
		return imgCheck0;
	}

	/**
	 * @param imgCheck0 the imgCheck0 to set
	 */
	public void setImgCheck0(int imgCheck0) {
		this.imgCheck0 = imgCheck0;
	}

	/**
	 * @return the imgCheck1
	 */
	public int getImgCheck1() {
		return imgCheck1;
	}

	/**
	 * @param imgCheck1 the imgCheck1 to set
	 */
	public void setImgCheck1(int imgCheck1) {
		this.imgCheck1 = imgCheck1;
	}

	/**
	 * @return the imgCheck2
	 */
	public int getImgCheck2() {
		return imgCheck2;
	}

	/**
	 * @param imgCheck2 the imgCheck2 to set
	 */
	public void setImgCheck2(int imgCheck2) {
		this.imgCheck2 = imgCheck2;
	}

	/**
	 * @return the imgCheck3
	 */
	public int getImgCheck3() {
		return imgCheck3;
	}

	/**
	 * @param imgCheck3 the imgCheck3 to set
	 */
	public void setImgCheck3(int imgCheck3) {
		this.imgCheck3 = imgCheck3;
	}

	/**
	 * imgCheck
	 */
	private int imgCheck1;
	/**
	 * imgCheck
	 */
	private int imgCheck2;
	
	/**
	 * imgCheck
	 */
	private int imgCheck3;
	
	/**
	 * @return the useCount
	 */
	public int getUseCount() {
		return useCount;
	}

	/**
	 * @param useCount the useCount to set
	 */
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	/**
	 * useCount
	 */
	private int useCount;
	
	/**
	 * goodscarid
	 */
	private int goodscarid;
	private int order_details_id;//order_details 表的ID  hongtu ADD;
	private int goodsDataId;
	
	
	public int getGoodsDataId() {
		return goodsDataId;
	}

	public void setGoodsDataId(int goodsDataId) {
		this.goodsDataId = goodsDataId;
	}

	/**
	 * @return the goodscarid
	 */
	public int getGoodscarid() {
		return goodscarid;
	}

	/**
	 * @param goodscarid the goodscarid to set
	 */
	public void setGoodscarid(int goodscarid) {
		this.goodscarid = goodscarid;
	}

	public int getOrder_details_id() {
		return order_details_id;
	}

	public void setOrder_details_id(int order_details_id) {
		this.order_details_id = order_details_id;
	}

	/**
	 * @return the minImgCheck
	 */
	public int getMinImgCheck() {
		return minImgCheck;
	}

	/**
	 * @param minImgCheck the minImgCheck to set
	 */
	public void setMinImgCheck(int minImgCheck) {
		this.minImgCheck = minImgCheck;
	}

	/**
	 * minImgCheck
	 */
	private int minImgCheck;
	
	/**
	 * minPrice
	 */
	private String minPrice;
	
	/**
	 * @return the minPrice
	 */
	public String getMinPrice() {
		return minPrice;
	}

	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	/**
	 * tbprice
	 */
	private String tbprice;
	
	/**
	 * tbprice1
	 */
	private String tbprice1;
	
	/**
	 * @return the tbprice
	 */
	public String getTbprice() {
		return tbprice;
	}

	/**
	 * @param tbprice the tbprice to set
	 */
	public void setTbprice(String tbprice) {
		this.tbprice = tbprice;
	}

	/**
	 * @return the tbprice1
	 */
	public String getTbprice1() {
		return tbprice1;
	}

	/**
	 * @param tbprice1 the tbprice1 to set
	 */
	public void setTbprice1(String tbprice1) {
		this.tbprice1 = tbprice1;
	}

	/**
	 * @return the tbprice2
	 */
	public String getTbprice2() {
		return tbprice2;
	}

	/**
	 * @param tbprice2 the tbprice2 to set
	 */
	public void setTbprice2(String tbprice2) {
		this.tbprice2 = tbprice2;
	}

	/**
	 * @return the tbprice3
	 */
	public String getTbprice3() {
		return tbprice3;
	}

	/**
	 * @param tbprice3 the tbprice3 to set
	 */
	public void setTbprice3(String tbprice3) {
		this.tbprice3 = tbprice3;
	}

	/**
	 * tbprice2
	 */
	private String tbprice2;
	
	/**
	 * tbprice3
	 */
	private String tbprice3;
	
	
	

	
}
