package com.cbt.bean;

import java.util.List;


/**
 * GoodsCheckBean
 */
public class GoodsCheckBean {
	
	
	private int id;
	
	private String sj_goods_pid;//实际采购订单的商品号
	
	private String yj_goods_pid;//预估采样订单商品号
	
	public String getYj_goods_pid() {
		return yj_goods_pid;
	}

	public void setYj_goods_pid(String yj_goods_pid) {
		this.yj_goods_pid = yj_goods_pid;
	}

	public String getSj_goods_pid() {
		return sj_goods_pid;
	}

	public void setSj_goods_pid(String sj_goods_pid) {
		this.sj_goods_pid = sj_goods_pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**			
	 * tbPath			
	 */			
	private String tbPath;			
				
	public String getTbPath() {			
		return tbPath;		
	}			
				
	public void setTbPath(String tbPath) {			
		this.tbPath = tbPath;		
	}			

	/**			
	 * tbPath1			
	 */			
	private String tbPath1;	
	public String getTbPath1() {
		return tbPath1;
	}

	public void setTbPath1(String tbPath1) {
		this.tbPath1 = tbPath1;
	}

	public Double getScore1() {
		return score1;
	}

	public void setScore1(Double score1) {
		this.score1 = score1;
	}

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
	
	private String currency;
	
	private String goodsWeight;
	
	private String catid1;

	public int getSamplCount() {
		return samplCount;
	}

	public void setSamplCount(int samplCount) {
		this.samplCount = samplCount;
	}

	private int samplCount;

	public String getAuthorizedFlag() {
		return authorizedFlag;
	}

	public void setAuthorizedFlag(String authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}

	private String authorizedFlag;

	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	private String exchange_rate;
	
	public String getCatid1() {
		return catid1;
	}

	public void setCatid1(String catid1) {
		this.catid1 = catid1;
	}

	public String getGoodsWeight() {
		return goodsWeight;
	}

	public void setGoodsWeight(String goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	private String companyState;//工厂状态
	public String getCompanyState() {
		return companyState;
	}

	public void setCompanyState(String companyState) {
		this.companyState = companyState;
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
	
	private String goodsName0;
	private String goodsName1;
	private String goodsName2;
	private String goodsName3;
	private String goodsName4;
	private String goodsName5;
	private String goodsName6;
	private String goodsName7;
	private String goodsName8;
	private String goodsName9;
	private String goodsName10;
	private String goodsName11;
	
	private String minMoq0;
	private String minMoq1;
	private String minMoq2;
	private String minMoq3;
	private String minMoq4;
	private String minMoq5;
	private String minMoq6;
	private String minMoq7;
	private String minMoq8;
	private String minMoq9;
	private String minMoq10;
	private String minMoq11;
	
	public String getGoodsName0() {
		return goodsName0;
	}

	public void setGoodsName0(String goodsName0) {
		this.goodsName0 = goodsName0;
	}

	public String getGoodsName1() {
		return goodsName1;
	}

	public void setGoodsName1(String goodsName1) {
		this.goodsName1 = goodsName1;
	}

	public String getGoodsName2() {
		return goodsName2;
	}

	public void setGoodsName2(String goodsName2) {
		this.goodsName2 = goodsName2;
	}

	public String getGoodsName3() {
		return goodsName3;
	}

	public void setGoodsName3(String goodsName3) {
		this.goodsName3 = goodsName3;
	}

	public String getGoodsName4() {
		return goodsName4;
	}

	public void setGoodsName4(String goodsName4) {
		this.goodsName4 = goodsName4;
	}

	public String getGoodsName5() {
		return goodsName5;
	}

	public void setGoodsName5(String goodsName5) {
		this.goodsName5 = goodsName5;
	}

	public String getGoodsName6() {
		return goodsName6;
	}

	public void setGoodsName6(String goodsName6) {
		this.goodsName6 = goodsName6;
	}

	public String getGoodsName7() {
		return goodsName7;
	}

	public void setGoodsName7(String goodsName7) {
		this.goodsName7 = goodsName7;
	}

	public String getGoodsName8() {
		return goodsName8;
	}

	public void setGoodsName8(String goodsName8) {
		this.goodsName8 = goodsName8;
	}

	public String getGoodsName9() {
		return goodsName9;
	}

	public void setGoodsName9(String goodsName9) {
		this.goodsName9 = goodsName9;
	}

	public String getGoodsName10() {
		return goodsName10;
	}

	public void setGoodsName10(String goodsName10) {
		this.goodsName10 = goodsName10;
	}

	public String getGoodsName11() {
		return goodsName11;
	}

	public void setGoodsName11(String goodsName11) {
		this.goodsName11 = goodsName11;
	}

	public String getMinMoq0() {
		return minMoq0;
	}

	public void setMinMoq0(String minMoq0) {
		this.minMoq0 = minMoq0;
	}

	public String getMinMoq1() {
		return minMoq1;
	}

	public void setMinMoq1(String minMoq1) {
		this.minMoq1 = minMoq1;
	}

	public String getMinMoq2() {
		return minMoq2;
	}

	public void setMinMoq2(String minMoq2) {
		this.minMoq2 = minMoq2;
	}

	public String getMinMoq3() {
		return minMoq3;
	}

	public void setMinMoq3(String minMoq3) {
		this.minMoq3 = minMoq3;
	}

	public String getMinMoq4() {
		return minMoq4;
	}

	public void setMinMoq4(String minMoq4) {
		this.minMoq4 = minMoq4;
	}

	public String getMinMoq5() {
		return minMoq5;
	}

	public void setMinMoq5(String minMoq5) {
		this.minMoq5 = minMoq5;
	}

	public String getMinMoq6() {
		return minMoq6;
	}

	public void setMinMoq6(String minMoq6) {
		this.minMoq6 = minMoq6;
	}

	public String getMinMoq7() {
		return minMoq7;
	}

	public void setMinMoq7(String minMoq7) {
		this.minMoq7 = minMoq7;
	}

	public String getMinMoq8() {
		return minMoq8;
	}

	public void setMinMoq8(String minMoq8) {
		this.minMoq8 = minMoq8;
	}

	public String getMinMoq9() {
		return minMoq9;
	}

	public void setMinMoq9(String minMoq9) {
		this.minMoq9 = minMoq9;
	}

	public String getMinMoq10() {
		return minMoq10;
	}

	public void setMinMoq10(String minMoq10) {
		this.minMoq10 = minMoq10;
	}

	public String getMinMoq11() {
		return minMoq11;
	}

	public void setMinMoq11(String minMoq11) {
		this.minMoq11 = minMoq11;
	}

	/**
	 * goodsnamecn
	 */
	private String goodsnamecn;
	
	public String getGoodsnamecn() {
		return goodsnamecn;
	}

	public void setGoodsnamecn(String goodsnamecn) {
		this.goodsnamecn = goodsnamecn;
	}

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
	 * ylFtList
	 */
	private List<GoodsCheckBean> ylFtList;
	
	/**
	 * ylLocList
	 */
	private List<GoodsCheckBean> ylLocList;
	
	/**
	 * sqLocList
	 */
	private List<GoodsCheckBean> sqLocList;
	
	public List<GoodsCheckBean> getSqLocList() {
		return sqLocList;
	}

	public void setSqLocList(List<GoodsCheckBean> sqLocList) {
		this.sqLocList = sqLocList;
	}

	public List<GoodsCheckBean> getYlFtList() {
		return ylFtList;
	}

	public void setYlFtList(List<GoodsCheckBean> ylFtList) {
		this.ylFtList = ylFtList;
	}

	public List<GoodsCheckBean> getYlLocList() {
		return ylLocList;
	}

	public void setYlLocList(List<GoodsCheckBean> ylLocList) {
		this.ylLocList = ylLocList;
	}

	/**
	 * aliSourceUrl
	 */
	private String aliSourceUrl;

	public String getAligSourceUrlPd() {
		return aligSourceUrlPd;
	}

	public String getUrlPd() {
		return urlPd;
	}

	public void setUrlPd(String urlPd) {
		this.urlPd = urlPd;
	}

	public void setAligSourceUrlPd(String aligSourceUrlPd) {
		this.aligSourceUrlPd = aligSourceUrlPd;
	}

	/**

	 * aligSourceUrl
	 */
	private String aligSourceUrl;
	private String aligSourceUrlPd;
	
	/**
	 * aliSourceOrderNo
	 */
	private String aliSourceOrderNo;
	
	/**
	 * yLTbimg
	 */
	private String yLTbimg;
	
	/**
	 * yLLocimg
	 */
	private String yLLocimg;
	
	/**
	 * yLLocurl
	 */
	private String yLLocurl;
	
	/**
	 * yLLocprice
	 */
	private String yLLocprice;
	
	public String getyLLocimg() {
		return yLLocimg;
	}

	public void setyLLocimg(String yLLocimg) {
		this.yLLocimg = yLLocimg;
	}

	public String getyLLocurl() {
		return yLLocurl;
	}

	public void setyLLocurl(String yLLocurl) {
		this.yLLocurl = yLLocurl;
	}

	public String getyLLocprice() {
		return yLLocprice;
	}

	public void setyLLocprice(String yLLocprice) {
		this.yLLocprice = yLLocprice;
	}

	/**
	 * yLTburl
	 */
	private String yLTburl;
	
	public String getyLTbimg() {
		return yLTbimg;
	}

	public void setyLTbimg(String yLTbimg) {
		this.yLTbimg = yLTbimg;
	}

	public String getyLTburl() {
		return yLTburl;
	}

	public void setyLTburl(String yLTburl) {
		this.yLTburl = yLTburl;
	}

	public String getyLTbprice() {
		return yLTbprice;
	}

	public void setyLTbprice(String yLTbprice) {
		this.yLTbprice = yLTbprice;
	}

	/**
	 * yLTbprice
	 */
	private String yLTbprice;
	
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
	 * aliSourcePrice
	 */
	private String aliSourcePrice;
	
	/**
	 * ePPrice预计采购金额
	 */
	private double ePPrice;
	
	private double sjPrice;
	
	private int sjCount;
	
	public double getSjPrice() {
		return sjPrice;
	}

	public void setSjPrice(double sjPrice) {
		this.sjPrice = sjPrice;
	}

	public int getSjCount() {
		return sjCount;
	}

	public void setSjCount(int sjCount) {
		this.sjCount = sjCount;
	}

	public double getePPrice() {
		return ePPrice;
	}

	public void setePPrice(double ePPrice) {
		this.ePPrice = ePPrice;
	}

	/**
	 * 建议拿样的核心商品数量
	 */
	private int recommendCount;
	
	public int getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(int recommendCount) {
		this.recommendCount = recommendCount;
	}

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
	private String urlPd;
	
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
	 * tbFlag4
	 */
	private String tbFlag4;

	/**
	 * tbFlag5
	 */
	private String tbFlag5;
	public String getTbFlag4() {
		return tbFlag4;
	}

	public void setTbFlag4(String tbFlag4) {
		this.tbFlag4 = tbFlag4;
	}

	public String getTbFlag5() {
		return tbFlag5;
	}

	public void setTbFlag5(String tbFlag5) {
		this.tbFlag5 = tbFlag5;
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
	 * imgpath
	 */
	private String imgpath;
	
	private int buyCount;
	
	private String buyPrice;
	
	
	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public String getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

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
	 * tbName4
	 */
	private String tbName4;
	/**
	 * tbName5
	 */
	private String tbName5;
	
	/**
	 * change:goodsPurl
	 */
	private String goodsPurl;
	
	/**
	 * change:goodsPrice
	 */
	private String goodsPrice;
	
	/**
	 * 核心产品标识
	 */
	private int priorityFlag;
	
	/**
	 * 同店铺标识
	 */
	private int sourceProFlag;
	
	public String getpUtil() {
		return pUtil;
	}

	public void setpUtil(String pUtil) {
		this.pUtil = pUtil;
	}

	private String pUtil;
	
	private String types;
	
	private String sku;
	
	private String showTypeImg;
	
	private String showTypeNum;
	public String getShowTypeImg() {
		return showTypeImg;
	}

	public void setShowTypeImg(String showTypeImg) {
		this.showTypeImg = showTypeImg;
	}

	public String getShowTypeNum() {
		return showTypeNum;
	}

	public void setShowTypeNum(String showTypeNum) {
		this.showTypeNum = showTypeNum;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

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

	/**
	 * 产品id
	 */
	private long offerId;
	
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
	 * 相似分数
	 */
	private Double score1;
	
	/**
	 * 新价格
	 */
	private String newPrice;
	
	/**
	 * 新名字
	 */
	private String newName;
	
	/**
	 * 类别ID
	 */
	private int catId;
	public String getTbName4() {
		return tbName4;
	}

	public void setTbName4(String tbName4) {
		this.tbName4 = tbName4;
	}

	public String getTbName5() {
		return tbName5;
	}

	public void setTbName5(String tbName5) {
		this.tbName5 = tbName5;
	}
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

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
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
	
	public String getAliCatName() {
		return aliCatName;
	}

	public void setAliCatName(String aliCatName) {
		this.aliCatName = aliCatName;
	}

	private String aliCatName;
	
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


	private Double dScore;
	
	/**
	 * imgCheck
	 */
	private int imgCheck0;
	
	public Double getdScore() {
		return dScore;
	}

	public void setdScore(Double dScore) {
		this.dScore = dScore;
	}

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
	 * useKcCount
	 */
	private int useKcCount;
	
	public int getUseKcCount() {
		return useKcCount;
	}

	public void setUseKcCount(int useKcCount) {
		this.useKcCount = useKcCount;
	}

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
	/**
	 * tbprice4
	 */
	private String tbprice4;
	
	public String getTbprice4() {
		return tbprice4;
	}

	public void setTbprice4(String tbprice4) {
		this.tbprice4 = tbprice4;
	}

	public String getTbprice5() {
		return tbprice5;
	}

	public void setTbprice5(String tbprice5) {
		this.tbprice5 = tbprice5;
	}

	/**
	 * tbprice5
	 */
	private String tbprice5;
	
	
	/**
	 * tbImg4
	 */
	private String tbImg4;
	
	public String getTbImg4() {
		return tbImg4;
	}

	public void setTbImg4(String tbImg4) {
		this.tbImg4 = tbImg4;
	}

	public String getTbUrl4() {
		return tbUrl4;
	}

	public void setTbUrl4(String tbUrl4) {
		this.tbUrl4 = tbUrl4;
	}

	public String getTbImg5() {
		return tbImg5;
	}

	public void setTbImg5(String tbImg5) {
		this.tbImg5 = tbImg5;
	}

	public String getTbUrl5() {
		return tbUrl5;
	}

	public void setTbUrl5(String tbUrl5) {
		this.tbUrl5 = tbUrl5;
	}

	/**
	 * tbUrl4
	 */
	private String tbUrl4;
	
	private String tbUrl6;
	private String tbUrl7;
	private String tbUrl8;
	private String tbUrl9;
	private String tbUrl10;
	private String tbUrl11;
	/**
	 * tbImg5
	 */
	private String tbImg5;
	public String getTbUrl6() {
		return tbUrl6;
	}

	public void setTbUrl6(String tbUrl6) {
		this.tbUrl6 = tbUrl6;
	}

	public String getTbUrl7() {
		return tbUrl7;
	}

	public void setTbUrl7(String tbUrl7) {
		this.tbUrl7 = tbUrl7;
	}

	public String getTbUrl8() {
		return tbUrl8;
	}

	public void setTbUrl8(String tbUrl8) {
		this.tbUrl8 = tbUrl8;
	}

	public String getTbUrl9() {
		return tbUrl9;
	}

	public void setTbUrl9(String tbUrl9) {
		this.tbUrl9 = tbUrl9;
	}

	public String getTbUrl10() {
		return tbUrl10;
	}

	public void setTbUrl10(String tbUrl10) {
		this.tbUrl10 = tbUrl10;
	}

	public String getTbUrl11() {
		return tbUrl11;
	}

	public void setTbUrl11(String tbUrl11) {
		this.tbUrl11 = tbUrl11;
	}

	public String getTbImg6() {
		return tbImg6;
	}

	public void setTbImg6(String tbImg6) {
		this.tbImg6 = tbImg6;
	}

	public String getTbImg7() {
		return tbImg7;
	}

	public void setTbImg7(String tbImg7) {
		this.tbImg7 = tbImg7;
	}

	public String getTbImg8() {
		return tbImg8;
	}

	public void setTbImg8(String tbImg8) {
		this.tbImg8 = tbImg8;
	}

	public String getTbImg9() {
		return tbImg9;
	}

	public void setTbImg9(String tbImg9) {
		this.tbImg9 = tbImg9;
	}

	public String getTbImg10() {
		return tbImg10;
	}

	public void setTbImg10(String tbImg10) {
		this.tbImg10 = tbImg10;
	}

	public String getTbImg11() {
		return tbImg11;
	}

	public void setTbImg11(String tbImg11) {
		this.tbImg11 = tbImg11;
	}

	public String getShopId6() {
		return shopId6;
	}

	public void setShopId6(String shopId6) {
		this.shopId6 = shopId6;
	}

	public String getShopId7() {
		return shopId7;
	}

	public void setShopId7(String shopId7) {
		this.shopId7 = shopId7;
	}

	public String getShopId8() {
		return shopId8;
	}

	public void setShopId8(String shopId8) {
		this.shopId8 = shopId8;
	}

	public String getShopId9() {
		return shopId9;
	}

	public void setShopId9(String shopId9) {
		this.shopId9 = shopId9;
	}

	public String getShopId10() {
		return shopId10;
	}

	public void setShopId10(String shopId10) {
		this.shopId10 = shopId10;
	}

	public String getShopId11() {
		return shopId11;
	}

	public void setShopId11(String shopId11) {
		this.shopId11 = shopId11;
	}

	public String getMoqPrice() {
		return moqPrice;
	}

	public void setMoqPrice(String moqPrice) {
		this.moqPrice = moqPrice;
	}

	public String getMoqPrice1() {
		return moqPrice1;
	}

	public void setMoqPrice1(String moqPrice1) {
		this.moqPrice1 = moqPrice1;
	}

	public String getMoqPrice2() {
		return moqPrice2;
	}

	public void setMoqPrice2(String moqPrice2) {
		this.moqPrice2 = moqPrice2;
	}

	public String getMoqPrice3() {
		return moqPrice3;
	}

	public void setMoqPrice3(String moqPrice3) {
		this.moqPrice3 = moqPrice3;
	}

	public String getMoqPrice4() {
		return moqPrice4;
	}

	public void setMoqPrice4(String moqPrice4) {
		this.moqPrice4 = moqPrice4;
	}

	public String getMoqPrice5() {
		return moqPrice5;
	}

	public void setMoqPrice5(String moqPrice5) {
		this.moqPrice5 = moqPrice5;
	}

	public String getMoqPrice6() {
		return moqPrice6;
	}

	public void setMoqPrice6(String moqPrice6) {
		this.moqPrice6 = moqPrice6;
	}

	public String getMoqPrice7() {
		return moqPrice7;
	}

	public void setMoqPrice7(String moqPrice7) {
		this.moqPrice7 = moqPrice7;
	}

	public String getMoqPrice8() {
		return moqPrice8;
	}

	public void setMoqPrice8(String moqPrice8) {
		this.moqPrice8 = moqPrice8;
	}

	public String getMoqPrice9() {
		return moqPrice9;
	}

	public void setMoqPrice9(String moqPrice9) {
		this.moqPrice9 = moqPrice9;
	}

	public String getMoqPrice10() {
		return moqPrice10;
	}

	public void setMoqPrice10(String moqPrice10) {
		this.moqPrice10 = moqPrice10;
	}

	public String getMoqPrice11() {
		return moqPrice11;
	}

	public void setMoqPrice11(String moqPrice11) {
		this.moqPrice11 = moqPrice11;
	}

	private String tbImg6;
	private String tbImg7;
	private String tbImg8;
	private String tbImg9;
	private String tbImg10;
	private String tbImg11;
	
	/**
	 * tbUrl5
	 */
	private String tbUrl5;
	
	private String shopId;
	private String shopId1;
	private String shopId2;
	private String shopId3;
	private String shopId4;
	private String shopId5;
	private String shopId6;
	private String shopId7;
	private String shopId8;
	private String shopId9;
	private String shopId10;
	private String shopId11;
	
	private String shopLevel;
	private String shopLevel1;
	private String shopLevel2;
	private String shopLevel3;
	private String shopLevel4;
	private String shopLevel5;
	private String shopLevel6;
	private String shopLevel7;
	private String shopLevel8;
	private String shopLevel9;
	private String shopLevel10;
	private String shopLevel11;
	
	private String moqPrice;
	private String moqPrice1;
	private String moqPrice2;
	private String moqPrice3;
	private String moqPrice4;
	private String moqPrice5;
	private String moqPrice6;
	private String moqPrice7;
	private String moqPrice8;
	private String moqPrice9;
	private String moqPrice10;
	private String moqPrice11;
	
	private int imgCheck;
	
	private String shopGuarantee;
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSaleService() {
		return saleService;
	}

	public void setSaleService(String saleService) {
		this.saleService = saleService;
	}

	private String address;
	private String saleService;
	
	public String getShopGuarantee() {
		return shopGuarantee;
	}

	public void setShopGuarantee(String shopGuarantee) {
		this.shopGuarantee = shopGuarantee;
	}

	public String getShopLevel() {
		return shopLevel;
	}

	public void setShopLevel(String shopLevel) {
		this.shopLevel = shopLevel;
	}

	public String getShopLevel1() {
		return shopLevel1;
	}

	public void setShopLevel1(String shopLevel1) {
		this.shopLevel1 = shopLevel1;
	}

	public String getShopLevel2() {
		return shopLevel2;
	}

	public void setShopLevel2(String shopLevel2) {
		this.shopLevel2 = shopLevel2;
	}

	public String getShopLevel3() {
		return shopLevel3;
	}

	public void setShopLevel3(String shopLevel3) {
		this.shopLevel3 = shopLevel3;
	}

	public String getShopLevel4() {
		return shopLevel4;
	}

	public void setShopLevel4(String shopLevel4) {
		this.shopLevel4 = shopLevel4;
	}

	public String getShopLevel5() {
		return shopLevel5;
	}

	public void setShopLevel5(String shopLevel5) {
		this.shopLevel5 = shopLevel5;
	}

	public String getShopLevel6() {
		return shopLevel6;
	}

	public void setShopLevel6(String shopLevel6) {
		this.shopLevel6 = shopLevel6;
	}

	public String getShopLevel7() {
		return shopLevel7;
	}

	public void setShopLevel7(String shopLevel7) {
		this.shopLevel7 = shopLevel7;
	}

	public String getShopLevel8() {
		return shopLevel8;
	}

	public void setShopLevel8(String shopLevel8) {
		this.shopLevel8 = shopLevel8;
	}

	public String getShopLevel9() {
		return shopLevel9;
	}

	public void setShopLevel9(String shopLevel9) {
		this.shopLevel9 = shopLevel9;
	}

	public String getShopLevel10() {
		return shopLevel10;
	}

	public void setShopLevel10(String shopLevel10) {
		this.shopLevel10 = shopLevel10;
	}

	public String getShopLevel11() {
		return shopLevel11;
	}

	public void setShopLevel11(String shopLevel11) {
		this.shopLevel11 = shopLevel11;
	}

public int getImgCheck() {
		return imgCheck;
	}

	public void setImgCheck(int imgCheck) {
		this.imgCheck = imgCheck;
	}

	public int getImgCheck4() {
		return imgCheck4;
	}

	public void setImgCheck4(int imgCheck4) {
		this.imgCheck4 = imgCheck4;
	}

	public int getImgCheck5() {
		return imgCheck5;
	}

	public void setImgCheck5(int imgCheck5) {
		this.imgCheck5 = imgCheck5;
	}

	public int getImgCheck6() {
		return imgCheck6;
	}

	public void setImgCheck6(int imgCheck6) {
		this.imgCheck6 = imgCheck6;
	}

	public int getImgCheck7() {
		return imgCheck7;
	}

	public void setImgCheck7(int imgCheck7) {
		this.imgCheck7 = imgCheck7;
	}

	public int getImgCheck8() {
		return imgCheck8;
	}

	public void setImgCheck8(int imgCheck8) {
		this.imgCheck8 = imgCheck8;
	}

	public int getImgCheck9() {
		return imgCheck9;
	}

	public void setImgCheck9(int imgCheck9) {
		this.imgCheck9 = imgCheck9;
	}

	public int getImgCheck10() {
		return imgCheck10;
	}

	public void setImgCheck10(int imgCheck10) {
		this.imgCheck10 = imgCheck10;
	}

	public int getImgCheck11() {
		return imgCheck11;
	}

	public void setImgCheck11(int imgCheck11) {
		this.imgCheck11 = imgCheck11;
	}

	//	private int imgCheck1;
//	private int imgCheck2;
//	private int imgCheck3;
	private int imgCheck4;
	private int imgCheck5;
	private int imgCheck6;
	private int imgCheck7;
	private int imgCheck8;
	private int imgCheck9;
	private int imgCheck10;
	private int imgCheck11;
	
	private String categoryid;
	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	private String categoryid1;
	private String categoryid2;
	private String categoryid3;
	private String categoryid4;
	private String categoryid5;
	private String categoryid6;
	private String categoryid7;
	private String categoryid8;
	private String categoryid9;
	private String categoryid10;
	
	
	private String goodsSold;
	private String goodsSold1;
	private String goodsSold2;
	private String goodsSold3;
	private String goodsSold4;
	private String goodsSold5;
	private String goodsSold6;
	private String goodsSold7;
	private String goodsSold8;
	private String goodsSold9;
	private String goodsSold10;
	private String goodsSold11;
	
	private int goodsSoldFlag;
	private int goodsSoldFlag1;
	private int goodsSoldFlag2;
	private int goodsSoldFlag3;
	private int goodsSoldFlag4;
	private int goodsSoldFlag5;
	private int goodsSoldFlag6;
	private int goodsSoldFlag7;
	private int goodsSoldFlag8;
	private int goodsSoldFlag9;
	private int goodsSoldFlag10;
	private int goodsSoldFlag11;
	
	public int getGoodsSoldFlag() {
		return goodsSoldFlag;
	}

	public void setGoodsSoldFlag(int goodsSoldFlag) {
		this.goodsSoldFlag = goodsSoldFlag;
	}

	public int getGoodsSoldFlag1() {
		return goodsSoldFlag1;
	}

	public void setGoodsSoldFlag1(int goodsSoldFlag1) {
		this.goodsSoldFlag1 = goodsSoldFlag1;
	}

	public int getGoodsSoldFlag2() {
		return goodsSoldFlag2;
	}

	public void setGoodsSoldFlag2(int goodsSoldFlag2) {
		this.goodsSoldFlag2 = goodsSoldFlag2;
	}

	public int getGoodsSoldFlag3() {
		return goodsSoldFlag3;
	}

	public void setGoodsSoldFlag3(int goodsSoldFlag3) {
		this.goodsSoldFlag3 = goodsSoldFlag3;
	}

	public int getGoodsSoldFlag4() {
		return goodsSoldFlag4;
	}

	public void setGoodsSoldFlag4(int goodsSoldFlag4) {
		this.goodsSoldFlag4 = goodsSoldFlag4;
	}

	public int getGoodsSoldFlag5() {
		return goodsSoldFlag5;
	}

	public void setGoodsSoldFlag5(int goodsSoldFlag5) {
		this.goodsSoldFlag5 = goodsSoldFlag5;
	}

	public int getGoodsSoldFlag6() {
		return goodsSoldFlag6;
	}

	public void setGoodsSoldFlag6(int goodsSoldFlag6) {
		this.goodsSoldFlag6 = goodsSoldFlag6;
	}

	public int getGoodsSoldFlag7() {
		return goodsSoldFlag7;
	}

	public void setGoodsSoldFlag7(int goodsSoldFlag7) {
		this.goodsSoldFlag7 = goodsSoldFlag7;
	}

	public int getGoodsSoldFlag8() {
		return goodsSoldFlag8;
	}

	public void setGoodsSoldFlag8(int goodsSoldFlag8) {
		this.goodsSoldFlag8 = goodsSoldFlag8;
	}

	public int getGoodsSoldFlag9() {
		return goodsSoldFlag9;
	}

	public void setGoodsSoldFlag9(int goodsSoldFlag9) {
		this.goodsSoldFlag9 = goodsSoldFlag9;
	}

	public int getGoodsSoldFlag10() {
		return goodsSoldFlag10;
	}

	public void setGoodsSoldFlag10(int goodsSoldFlag10) {
		this.goodsSoldFlag10 = goodsSoldFlag10;
	}

	public int getGoodsSoldFlag11() {
		return goodsSoldFlag11;
	}

	public void setGoodsSoldFlag11(int goodsSoldFlag11) {
		this.goodsSoldFlag11 = goodsSoldFlag11;
	}

	public String getGoodsSold() {
		return goodsSold;
	}

	public void setGoodsSold(String goodsSold) {
		this.goodsSold = goodsSold;
	}

	public String getGoodsSold1() {
		return goodsSold1;
	}

	public void setGoodsSold1(String goodsSold1) {
		this.goodsSold1 = goodsSold1;
	}

	public String getGoodsSold2() {
		return goodsSold2;
	}

	public void setGoodsSold2(String goodsSold2) {
		this.goodsSold2 = goodsSold2;
	}

	public String getGoodsSold3() {
		return goodsSold3;
	}

	public void setGoodsSold3(String goodsSold3) {
		this.goodsSold3 = goodsSold3;
	}

	public String getGoodsSold4() {
		return goodsSold4;
	}

	public void setGoodsSold4(String goodsSold4) {
		this.goodsSold4 = goodsSold4;
	}

	public String getGoodsSold5() {
		return goodsSold5;
	}

	public void setGoodsSold5(String goodsSold5) {
		this.goodsSold5 = goodsSold5;
	}

	public String getGoodsSold6() {
		return goodsSold6;
	}

	public void setGoodsSold6(String goodsSold6) {
		this.goodsSold6 = goodsSold6;
	}

	public String getGoodsSold7() {
		return goodsSold7;
	}

	public void setGoodsSold7(String goodsSold7) {
		this.goodsSold7 = goodsSold7;
	}

	public String getGoodsSold8() {
		return goodsSold8;
	}

	public void setGoodsSold8(String goodsSold8) {
		this.goodsSold8 = goodsSold8;
	}

	public String getGoodsSold9() {
		return goodsSold9;
	}

	public void setGoodsSold9(String goodsSold9) {
		this.goodsSold9 = goodsSold9;
	}

	public String getGoodsSold10() {
		return goodsSold10;
	}

	public void setGoodsSold10(String goodsSold10) {
		this.goodsSold10 = goodsSold10;
	}

	public String getGoodsSold11() {
		return goodsSold11;
	}

	public void setGoodsSold11(String goodsSold11) {
		this.goodsSold11 = goodsSold11;
	}

	public String getCategoryid6() {
		return categoryid6;
	}

	public void setCategoryid6(String categoryid6) {
		this.categoryid6 = categoryid6;
	}

	public String getCategoryid7() {
		return categoryid7;
	}

	public void setCategoryid7(String categoryid7) {
		this.categoryid7 = categoryid7;
	}

	public String getCategoryid8() {
		return categoryid8;
	}

	public void setCategoryid8(String categoryid8) {
		this.categoryid8 = categoryid8;
	}

	public String getCategoryid9() {
		return categoryid9;
	}

	public void setCategoryid9(String categoryid9) {
		this.categoryid9 = categoryid9;
	}

	public String getCategoryid10() {
		return categoryid10;
	}

	public void setCategoryid10(String categoryid10) {
		this.categoryid10 = categoryid10;
	}

	public String getCategoryid11() {
		return categoryid11;
	}

	public void setCategoryid11(String categoryid11) {
		this.categoryid11 = categoryid11;
	}

	private String categoryid11;
	public String getCategoryid1() {
		return categoryid1;
	}

	public void setCategoryid1(String categoryid1) {
		this.categoryid1 = categoryid1;
	}

	public String getCategoryid2() {
		return categoryid2;
	}

	public void setCategoryid2(String categoryid2) {
		this.categoryid2 = categoryid2;
	}

	public String getCategoryid3() {
		return categoryid3;
	}

	public void setCategoryid3(String categoryid3) {
		this.categoryid3 = categoryid3;
	}

	public String getCategoryid4() {
		return categoryid4;
	}

	public void setCategoryid4(String categoryid4) {
		this.categoryid4 = categoryid4;
	}

	public String getCategoryid5() {
		return categoryid5;
	}

	public void setCategoryid5(String categoryid5) {
		this.categoryid5 = categoryid5;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopId1() {
		return shopId1;
	}

	public void setShopId1(String shopId1) {
		this.shopId1 = shopId1;
	}

	public String getShopId2() {
		return shopId2;
	}

	public void setShopId2(String shopId2) {
		this.shopId2 = shopId2;
	}

	public String getShopId3() {
		return shopId3;
	}

	public void setShopId3(String shopId3) {
		this.shopId3 = shopId3;
	}

	public String getShopId4() {
		return shopId4;
	}

	public void setShopId4(String shopId4) {
		this.shopId4 = shopId4;
	}

	public String getShopId5() {
		return shopId5;
	}

	public void setShopId5(String shopId5) {
		this.shopId5 = shopId5;
	}

	/**
	 * goodsPid
	 */
	private String goodsPid;
	
	public String getGoodsPid() {
		return goodsPid;
	}

	public void setGoodsPid(String goodsPid) {
		this.goodsPid = goodsPid;
	}
	
	
	private String goodsImg;
	
	private String ylPid;
	
	private String ylPrice;
	
	private String ylImg;
	
	private String ylUrl;
	
	private String shipState;

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getYlUrl() {
		return ylUrl;
	}

	public void setYlUrl(String ylUrl) {
		this.ylUrl = ylUrl;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
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

	public String getYlImg() {
		return ylImg;
	}

	public void setYlImg(String ylImg) {
		this.ylImg = ylImg;
	}
	//卖家在我司有多少产品
	private int proCon;
	private int proCon1;
	private int proCon2;
	private int proCon3;
	private int proCon4;
	private int proCon5;
	private int proCon6;
	//卖家质量水准
	private int shopQuality;
	private int shopQuality1;
	private int shopQuality2;
	private int shopQuality3;
	private int shopQuality4;
	private int shopQuality5;
	private int shopQuality6;
	//商品评分
	private int proScore;
	private int proScore1;
	private int proScore2;
	private int proScore3;
	private int proScore4;
	private int proScore5;
	private int proScore6;
	//1688深度验厂
	private int deepFactory;
	private int deepFactory1;
	private int deepFactory2;
	private int deepFactory3;
	private int deepFactory4;
	private int deepFactory5;
	private int deepFactory6;
	//销量
	private int proSold;
	private int proSold1;
	private int proSold2;
	private int proSold3;
	private int proSold4;
	private int proSold5;
	private int proSold6;

	public int getProCon() {
		return proCon;
	}

	public void setProCon(int proCon) {
		this.proCon = proCon;
	}

	public int getProCon1() {
		return proCon1;
	}

	public void setProCon1(int proCon1) {
		this.proCon1 = proCon1;
	}

	public int getProCon2() {
		return proCon2;
	}

	public void setProCon2(int proCon2) {
		this.proCon2 = proCon2;
	}

	public int getProCon3() {
		return proCon3;
	}

	public void setProCon3(int proCon3) {
		this.proCon3 = proCon3;
	}

	public int getProCon4() {
		return proCon4;
	}

	public void setProCon4(int proCon4) {
		this.proCon4 = proCon4;
	}

	public int getProCon5() {
		return proCon5;
	}

	public void setProCon5(int proCon5) {
		this.proCon5 = proCon5;
	}

	public int getProCon6() {
		return proCon6;
	}

	public void setProCon6(int proCon6) {
		this.proCon6 = proCon6;
	}

	public int getShopQuality() {
		return shopQuality;
	}

	public void setShopQuality(int shopQuality) {
		this.shopQuality = shopQuality;
	}

	public int getShopQuality1() {
		return shopQuality1;
	}

	public void setShopQuality1(int shopQuality1) {
		this.shopQuality1 = shopQuality1;
	}

	public int getShopQuality2() {
		return shopQuality2;
	}

	public void setShopQuality2(int shopQuality2) {
		this.shopQuality2 = shopQuality2;
	}

	public int getShopQuality3() {
		return shopQuality3;
	}

	public void setShopQuality3(int shopQuality3) {
		this.shopQuality3 = shopQuality3;
	}

	public int getShopQuality4() {
		return shopQuality4;
	}

	public void setShopQuality4(int shopQuality4) {
		this.shopQuality4 = shopQuality4;
	}

	public int getShopQuality5() {
		return shopQuality5;
	}

	public void setShopQuality5(int shopQuality5) {
		this.shopQuality5 = shopQuality5;
	}

	public int getShopQuality6() {
		return shopQuality6;
	}

	public void setShopQuality6(int shopQuality6) {
		this.shopQuality6 = shopQuality6;
	}

	public int getProScore() {
		return proScore;
	}

	public void setProScore(int proScore) {
		this.proScore = proScore;
	}

	public int getProScore1() {
		return proScore1;
	}

	public void setProScore1(int proScore1) {
		this.proScore1 = proScore1;
	}

	public int getProScore2() {
		return proScore2;
	}

	public void setProScore2(int proScore2) {
		this.proScore2 = proScore2;
	}

	public int getProScore3() {
		return proScore3;
	}

	public void setProScore3(int proScore3) {
		this.proScore3 = proScore3;
	}

	public int getProScore4() {
		return proScore4;
	}

	public void setProScore4(int proScore4) {
		this.proScore4 = proScore4;
	}

	public int getProScore5() {
		return proScore5;
	}

	public void setProScore5(int proScore5) {
		this.proScore5 = proScore5;
	}

	public int getProScore6() {
		return proScore6;
	}

	public void setProScore6(int proScore6) {
		this.proScore6 = proScore6;
	}

	public int getDeepFactory() {
		return deepFactory;
	}

	public void setDeepFactory(int deepFactory) {
		this.deepFactory = deepFactory;
	}

	public int getDeepFactory1() {
		return deepFactory1;
	}

	public void setDeepFactory1(int deepFactory1) {
		this.deepFactory1 = deepFactory1;
	}

	public int getDeepFactory2() {
		return deepFactory2;
	}

	public void setDeepFactory2(int deepFactory2) {
		this.deepFactory2 = deepFactory2;
	}

	public int getDeepFactory3() {
		return deepFactory3;
	}

	public void setDeepFactory3(int deepFactory3) {
		this.deepFactory3 = deepFactory3;
	}

	public int getDeepFactory4() {
		return deepFactory4;
	}

	public void setDeepFactory4(int deepFactory4) {
		this.deepFactory4 = deepFactory4;
	}

	public int getDeepFactory5() {
		return deepFactory5;
	}

	public void setDeepFactory5(int deepFactory5) {
		this.deepFactory5 = deepFactory5;
	}

	public int getDeepFactory6() {
		return deepFactory6;
	}

	public void setDeepFactory6(int deepFactory6) {
		this.deepFactory6 = deepFactory6;
	}

	public int getProSold() {
		return proSold;
	}

	public void setProSold(int proSold) {
		this.proSold = proSold;
	}

	public int getProSold1() {
		return proSold1;
	}

	public void setProSold1(int proSold1) {
		this.proSold1 = proSold1;
	}

	public int getProSold2() {
		return proSold2;
	}

	public void setProSold2(int proSold2) {
		this.proSold2 = proSold2;
	}

	public int getProSold3() {
		return proSold3;
	}

	public void setProSold3(int proSold3) {
		this.proSold3 = proSold3;
	}

	public int getProSold4() {
		return proSold4;
	}

	public void setProSold4(int proSold4) {
		this.proSold4 = proSold4;
	}

	public int getProSold5() {
		return proSold5;
	}

	public void setProSold5(int proSold5) {
		this.proSold5 = proSold5;
	}

	public int getProSold6() {
		return proSold6;
	}

	public void setProSold6(int proSold6) {
		this.proSold6 = proSold6;
	}
	
}
