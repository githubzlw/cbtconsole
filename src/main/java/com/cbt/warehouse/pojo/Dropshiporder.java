package com.cbt.warehouse.pojo;

import java.util.Date;

public class Dropshiporder {
    private Integer orderid;

    private String parentOrderNo;

    private String childOrderNo;

    private Integer userId;

    private Integer addressId;

    private String deliveryTime;

    private Integer packagStyle;

    private String modeTransport;

    private String serviceFee;

    private String productCost;

    private String domesticFreight;

    private String foreignFreight;

    private Double actualAllincost;

    private String payPrice;

    private String payPriceTow;

    private String payPriceThree;

    private String actualFfreight;

    private Double remainingPrice;

    private String actualVolume;

    private String actualWeight;

    private String customDiscussOther;

    private String customDiscussFright;

    private Date transportTime;

    private String state;

    private Integer cancelObj;

    private Date expectArriveTime;

    private Date arriveTime;

    private Date createTime;

    private Integer clientUpdate;

    private Integer serverUpdate;

    private String ip;

    private Double orderAc;

    private Integer purchaseNumber;

    private Integer detailsNumber;

    private String ipnaddress;

    private String currency;

    private Double discountAmount;

    private Integer purchaseDays;

    private String actualLwh;

    private Double actualWeightEstimate;

    private Double actualFreightC;

    private Double extraFreight;

    private Integer orderShow;

    private Integer packagNumber;

    private String orderremark;

    private Date orderpaytime;

    private Double cashback;
    
    //运单号
    private String expressNo;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo == null ? null : parentOrderNo.trim();
    }

    public String getChildOrderNo() {
        return childOrderNo;
    }

    public void setChildOrderNo(String childOrderNo) {
        this.childOrderNo = childOrderNo == null ? null : childOrderNo.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime == null ? null : deliveryTime.trim();
    }

    public Integer getPackagStyle() {
        return packagStyle;
    }

    public void setPackagStyle(Integer packagStyle) {
        this.packagStyle = packagStyle;
    }

    public String getModeTransport() {
        return modeTransport;
    }

    public void setModeTransport(String modeTransport) {
        this.modeTransport = modeTransport == null ? null : modeTransport.trim();
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee == null ? null : serviceFee.trim();
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost == null ? null : productCost.trim();
    }

    public String getDomesticFreight() {
        return domesticFreight;
    }

    public void setDomesticFreight(String domesticFreight) {
        this.domesticFreight = domesticFreight == null ? null : domesticFreight.trim();
    }

    public String getForeignFreight() {
        return foreignFreight;
    }

    public void setForeignFreight(String foreignFreight) {
        this.foreignFreight = foreignFreight == null ? null : foreignFreight.trim();
    }

    public Double getActualAllincost() {
        return actualAllincost;
    }

    public void setActualAllincost(Double actualAllincost) {
        this.actualAllincost = actualAllincost;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice == null ? null : payPrice.trim();
    }

    public String getPayPriceTow() {
        return payPriceTow;
    }

    public void setPayPriceTow(String payPriceTow) {
        this.payPriceTow = payPriceTow == null ? null : payPriceTow.trim();
    }

    public String getPayPriceThree() {
        return payPriceThree;
    }

    public void setPayPriceThree(String payPriceThree) {
        this.payPriceThree = payPriceThree == null ? null : payPriceThree.trim();
    }

    public String getActualFfreight() {
        return actualFfreight;
    }

    public void setActualFfreight(String actualFfreight) {
        this.actualFfreight = actualFfreight == null ? null : actualFfreight.trim();
    }

    public Double getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(Double remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public String getActualVolume() {
        return actualVolume;
    }

    public void setActualVolume(String actualVolume) {
        this.actualVolume = actualVolume == null ? null : actualVolume.trim();
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight == null ? null : actualWeight.trim();
    }

    public String getCustomDiscussOther() {
        return customDiscussOther;
    }

    public void setCustomDiscussOther(String customDiscussOther) {
        this.customDiscussOther = customDiscussOther == null ? null : customDiscussOther.trim();
    }

    public String getCustomDiscussFright() {
        return customDiscussFright;
    }

    public void setCustomDiscussFright(String customDiscussFright) {
        this.customDiscussFright = customDiscussFright == null ? null : customDiscussFright.trim();
    }

    public Date getTransportTime() {
        return transportTime;
    }

    public void setTransportTime(Date transportTime) {
        this.transportTime = transportTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Integer getCancelObj() {
        return cancelObj;
    }

    public void setCancelObj(Integer cancelObj) {
        this.cancelObj = cancelObj;
    }

    public Date getExpectArriveTime() {
        return expectArriveTime;
    }

    public void setExpectArriveTime(Date expectArriveTime) {
        this.expectArriveTime = expectArriveTime;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getClientUpdate() {
        return clientUpdate;
    }

    public void setClientUpdate(Integer clientUpdate) {
        this.clientUpdate = clientUpdate;
    }

    public Integer getServerUpdate() {
        return serverUpdate;
    }

    public void setServerUpdate(Integer serverUpdate) {
        this.serverUpdate = serverUpdate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Double getOrderAc() {
        return orderAc;
    }

    public void setOrderAc(Double orderAc) {
        this.orderAc = orderAc;
    }

    public Integer getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(Integer purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public Integer getDetailsNumber() {
        return detailsNumber;
    }

    public void setDetailsNumber(Integer detailsNumber) {
        this.detailsNumber = detailsNumber;
    }

    public String getIpnaddress() {
        return ipnaddress;
    }

    public void setIpnaddress(String ipnaddress) {
        this.ipnaddress = ipnaddress == null ? null : ipnaddress.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getPurchaseDays() {
        return purchaseDays;
    }

    public void setPurchaseDays(Integer purchaseDays) {
        this.purchaseDays = purchaseDays;
    }

    public String getActualLwh() {
        return actualLwh;
    }

    public void setActualLwh(String actualLwh) {
        this.actualLwh = actualLwh == null ? null : actualLwh.trim();
    }

    public Double getActualWeightEstimate() {
        return actualWeightEstimate;
    }

    public void setActualWeightEstimate(Double actualWeightEstimate) {
        this.actualWeightEstimate = actualWeightEstimate;
    }

    public Double getActualFreightC() {
        return actualFreightC;
    }

    public void setActualFreightC(Double actualFreightC) {
        this.actualFreightC = actualFreightC;
    }

    public Double getExtraFreight() {
        return extraFreight;
    }

    public void setExtraFreight(Double extraFreight) {
        this.extraFreight = extraFreight;
    }

    public Integer getOrderShow() {
        return orderShow;
    }

    public void setOrderShow(Integer orderShow) {
        this.orderShow = orderShow;
    }

    public Integer getPackagNumber() {
        return packagNumber;
    }

    public void setPackagNumber(Integer packagNumber) {
        this.packagNumber = packagNumber;
    }

    public String getOrderremark() {
        return orderremark;
    }

    public void setOrderremark(String orderremark) {
        this.orderremark = orderremark == null ? null : orderremark.trim();
    }

    public Date getOrderpaytime() {
        return orderpaytime;
    }

    public void setOrderpaytime(Date orderpaytime) {
        this.orderpaytime = orderpaytime;
    }

    public Double getCashback() {
        return cashback;
    }

    public void setCashback(Double cashback) {
        this.cashback = cashback;
    }

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
}