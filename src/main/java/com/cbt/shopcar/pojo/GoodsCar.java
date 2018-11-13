package com.cbt.shopcar.pojo;

import java.util.Date;

public class GoodsCar {
    private Integer id;

    private Integer goodsdataId;

    private Integer userid;

    private String sessionid;

    private String guid;

    private String itemid;

    private String shopid;

    private String goodsUrl;

    private String goodsTitle;

    private String googsSeller;

    private String googsImg;

    private String googsPrice;

    private Integer googsNumber;

    private String googsSize;

    private String goodsType;

    private String googsColor;

    private String freight;

    private String deliveryTime;

    private String normMost;

    private String normLeast;

    private Integer state;

    private String remark;

    private Date datatime;

    private Integer flag;

    private String pwprice;

    private String trueShipping;

    private Integer freightFree;

    private String width;

    private String perweight;

    private String seilunit;

    private String goodsunit;

    private String bulkVolume;

    private String totalWeight;

    private String perWeight;

    private String goodsEmail;

    private String freeShoppingCompany;

    private String freeScDays;

    private Integer preferential;

    private Integer depositRate;

    private String feeprice;

    private Integer goodsClass;

    private String currency;

    private Double extraFreight;

    private Integer buyForMe;

    private Integer sourceUrl;

    private double price1;//修改后的价格

    private double totalPrice;//总价

    private String goodsCatid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsdataId() {
        return goodsdataId;
    }

    public void setGoodsdataId(Integer goodsdataId) {
        this.goodsdataId = goodsdataId;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid == null ? null : sessionid.trim();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid == null ? null : itemid.trim();
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl == null ? null : goodsUrl.trim();
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle == null ? null : goodsTitle.trim();
    }

    public String getGoogsSeller() {
        return googsSeller;
    }

    public void setGoogsSeller(String googsSeller) {
        this.googsSeller = googsSeller == null ? null : googsSeller.trim();
    }

    public String getGoogsImg() {
        return googsImg;
    }

    public void setGoogsImg(String googsImg) {
        this.googsImg = googsImg == null ? null : googsImg.trim();
    }

    public String getGoogsPrice() {
        return googsPrice;
    }

    public void setGoogsPrice(String googsPrice) {
        this.googsPrice = googsPrice == null ? null : googsPrice.trim();
    }

    public Integer getGoogsNumber() {
        return googsNumber;
    }

    public void setGoogsNumber(Integer googsNumber) {
        this.googsNumber = googsNumber;
    }

    public String getGoogsSize() {
        return googsSize;
    }

    public void setGoogsSize(String googsSize) {
        this.googsSize = googsSize == null ? null : googsSize.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public String getGoogsColor() {
        return googsColor;
    }

    public void setGoogsColor(String googsColor) {
        this.googsColor = googsColor == null ? null : googsColor.trim();
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight == null ? null : freight.trim();
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime == null ? null : deliveryTime.trim();
    }

    public String getNormMost() {
        return normMost;
    }

    public void setNormMost(String normMost) {
        this.normMost = normMost == null ? null : normMost.trim();
    }

    public String getNormLeast() {
        return normLeast;
    }

    public void setNormLeast(String normLeast) {
        this.normLeast = normLeast == null ? null : normLeast.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getDatatime() {
        return datatime;
    }

    public void setDatatime(Date datatime) {
        this.datatime = datatime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getPwprice() {
        return pwprice;
    }

    public void setPwprice(String pwprice) {
        this.pwprice = pwprice == null ? null : pwprice.trim();
    }

    public String getTrueShipping() {
        return trueShipping;
    }

    public void setTrueShipping(String trueShipping) {
        this.trueShipping = trueShipping == null ? null : trueShipping.trim();
    }

    public Integer getFreightFree() {
        return freightFree;
    }

    public void setFreightFree(Integer freightFree) {
        this.freightFree = freightFree;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width == null ? null : width.trim();
    }

    public String getPerweight() {
        return perweight;
    }

    public void setPerweight(String perweight) {
        this.perweight = perweight == null ? null : perweight.trim();
    }

    public String getSeilunit() {
        return seilunit;
    }

    public void setSeilunit(String seilunit) {
        this.seilunit = seilunit == null ? null : seilunit.trim();
    }

    public String getGoodsunit() {
        return goodsunit;
    }

    public void setGoodsunit(String goodsunit) {
        this.goodsunit = goodsunit == null ? null : goodsunit.trim();
    }

    public String getBulkVolume() {
        return bulkVolume;
    }

    public void setBulkVolume(String bulkVolume) {
        this.bulkVolume = bulkVolume == null ? null : bulkVolume.trim();
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight == null ? null : totalWeight.trim();
    }

    public String getPerWeight() {
        return perWeight;
    }

    public void setPerWeight(String perWeight) {
        this.perWeight = perWeight == null ? null : perWeight.trim();
    }

    public String getGoodsEmail() {
        return goodsEmail;
    }

    public void setGoodsEmail(String goodsEmail) {
        this.goodsEmail = goodsEmail == null ? null : goodsEmail.trim();
    }

    public String getFreeShoppingCompany() {
        return freeShoppingCompany;
    }

    public void setFreeShoppingCompany(String freeShoppingCompany) {
        this.freeShoppingCompany = freeShoppingCompany == null ? null : freeShoppingCompany.trim();
    }

    public String getFreeScDays() {
        return freeScDays;
    }

    public void setFreeScDays(String freeScDays) {
        this.freeScDays = freeScDays == null ? null : freeScDays.trim();
    }

    public Integer getPreferential() {
        return preferential;
    }

    public void setPreferential(Integer preferential) {
        this.preferential = preferential;
    }

    public Integer getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(Integer depositRate) {
        this.depositRate = depositRate;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFeeprice(String feeprice) {
        this.feeprice = feeprice == null ? null : feeprice.trim();
    }

    public Integer getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(Integer goodsClass) {
        this.goodsClass = goodsClass;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Double getExtraFreight() {
        return extraFreight;
    }

    public void setExtraFreight(Double extraFreight) {
        this.extraFreight = extraFreight;
    }

    public Integer getBuyForMe() {
        return buyForMe;
    }

    public void setBuyForMe(Integer buyForMe) {
        this.buyForMe = buyForMe;
    }

    public Integer getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(Integer sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsCatid() {
        return goodsCatid;
    }

    public void setGoodsCatid(String goodsCatid) {
        this.goodsCatid = goodsCatid;
    }

    public GoodsCar() {
    }

    public GoodsCar(Integer goodsdataId, Integer userid, String sessionid, String guid, String itemid, String shopid,
                    String goodsUrl, String goodsTitle, String googsSeller, String googsImg, String googsPrice,
                    Integer googsNumber, String googsSize, String goodsType, String googsColor, String freight,
                    String deliveryTime, String normMost, String normLeast, Integer state, String remark, Date datatime,
                    Integer flag, String pwprice, String trueShipping, Integer freightFree, String width, String perweight,
                    String seilunit, String goodsunit, String bulkVolume, String totalWeight, String perWeight, String goodsEmail,
                    String freeShoppingCompany, String freeScDays, Integer preferential, Integer depositRate, String feeprice,
                    Integer goodsClass, String currency, Double extraFreight, Integer buyForMe, Integer sourceUrl,String goodsCatid) {
        this.goodsdataId = goodsdataId;
        this.userid = userid;
        this.sessionid = sessionid;
        this.guid = guid;
        this.itemid = itemid;
        this.shopid = shopid;
        this.goodsUrl = goodsUrl;
        this.goodsTitle = goodsTitle;
        this.googsSeller = googsSeller;
        this.googsImg = googsImg;
        this.googsPrice = googsPrice;
        this.googsNumber = googsNumber;
        this.googsSize = googsSize;
        this.goodsType = goodsType;
        this.googsColor = googsColor;
        this.freight = freight;
        this.deliveryTime = deliveryTime;
        this.normMost = normMost;
        this.normLeast = normLeast;
        this.state = state;
        this.remark = remark;
        this.datatime = datatime;
        this.flag = flag;
        this.pwprice = pwprice;
        this.trueShipping = trueShipping;
        this.freightFree = freightFree;
        this.width = width;
        this.perweight = perweight;
        this.seilunit = seilunit;
        this.goodsunit = goodsunit;
        this.bulkVolume = bulkVolume;
        this.totalWeight = totalWeight;
        this.perWeight = perWeight;
        this.goodsEmail = goodsEmail;
        this.freeShoppingCompany = freeShoppingCompany;
        this.freeScDays = freeScDays;
        this.preferential = preferential;
        this.depositRate = depositRate;
        this.feeprice = feeprice;
        this.goodsClass = goodsClass;
        this.currency = currency;
        this.extraFreight = extraFreight;
        this.buyForMe = buyForMe;
        this.sourceUrl = sourceUrl;
        this.goodsCatid = goodsCatid;
    }

    @Override
	public String toString() {
		return "GoodsCar [id=" + id + ", goodsdataId=" + goodsdataId
				+ ", userid=" + userid + ", sessionid=" + sessionid + ", guid="
				+ guid + ", itemid=" + itemid + ", shopid=" + shopid
				+ ", goodsUrl=" + goodsUrl + ", goodsTitle=" + goodsTitle
				+ ", googsSeller=" + googsSeller + ", googsImg=" + googsImg
				+ ", googsPrice=" + googsPrice + ", googsNumber=" + googsNumber
				+ ", googsSize=" + googsSize + ", goodsType=" + goodsType
				+ ", googsColor=" + googsColor + ", freight=" + freight
				+ ", deliveryTime=" + deliveryTime + ", normMost=" + normMost
				+ ", normLeast=" + normLeast + ", state=" + state + ", remark="
				+ remark + ", datatime=" + datatime + ", flag=" + flag
				+ ", pwprice=" + pwprice + ", trueShipping=" + trueShipping
				+ ", freightFree=" + freightFree + ", width=" + width
				+ ", perweight=" + perweight + ", seilunit=" + seilunit
				+ ", goodsunit=" + goodsunit + ", bulkVolume=" + bulkVolume
				+ ", totalWeight=" + totalWeight + ", perWeight=" + perWeight
				+ ", goodsEmail=" + goodsEmail + ", freeShoppingCompany="
				+ freeShoppingCompany + ", freeScDays=" + freeScDays
				+ ", preferential=" + preferential + ", depositRate="
				+ depositRate + ", feeprice=" + feeprice + ", goodsClass="
				+ goodsClass + ", currency=" + currency + ", extraFreight="
				+ extraFreight + ", buyForMe=" + buyForMe + ", sourceUrl="
				+ sourceUrl + "]";
	}
    
    
    
    
    
    
    
}