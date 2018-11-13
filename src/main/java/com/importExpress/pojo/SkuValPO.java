package com.importExpress.pojo;

import java.io.Serializable;

public class SkuValPO implements Serializable{

    private static final long serialVersionUID = 4687806514480941065L;

    private Double actSkuCalPrice;

    private Double actSkuMultiCurrencyCalPrice;

    private Double actSkuMultiCurrencyDisplayPrice;

    //库存数量
    private Integer availQuantity = 0;

    //0
    private Integer inventory;

    //0-false;1-true
    private Boolean isActivity; //和1688原sku数据对应

    private Integer isActivityInt; //和数据库对应 暂未使用

    private Double skuCalPrice;

    private Double skuMultiCurrencyCalPrice;

    private Double skuMultiCurrencyDisplayPrice;

    public Double getActSkuCalPrice() {
        return actSkuCalPrice;
    }

    public void setActSkuCalPrice(Double actSkuCalPrice) {
        this.actSkuCalPrice = actSkuCalPrice;
    }

    public Double getActSkuMultiCurrencyCalPrice() {
        return actSkuMultiCurrencyCalPrice;
    }

    public Boolean getActivity() {
        return isActivity;
    }

    public void setActivity(Boolean activity) {
        isActivity = activity;
    }

    public Integer getIsActivityInt() {
        return isActivityInt;
    }

    public void setIsActivityInt(Integer isActivityInt) {
        this.isActivityInt = isActivityInt;
    }

    public void setActSkuMultiCurrencyCalPrice(Double actSkuMultiCurrencyCalPrice) {
        this.actSkuMultiCurrencyCalPrice = actSkuMultiCurrencyCalPrice;
    }

    public Double getActSkuMultiCurrencyDisplayPrice() {
        return actSkuMultiCurrencyDisplayPrice;
    }

    public void setActSkuMultiCurrencyDisplayPrice(Double actSkuMultiCurrencyDisplayPrice) {
        this.actSkuMultiCurrencyDisplayPrice = actSkuMultiCurrencyDisplayPrice;
    }

    public Integer getAvailQuantity() {
        return availQuantity;
    }

    public void setAvailQuantity(Integer availQuantity) {
        this.availQuantity = availQuantity;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Boolean getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(Boolean isActivity) {
        this.isActivity = isActivity;
    }

    public Double getSkuCalPrice() {
        return skuCalPrice;
    }

    public void setSkuCalPrice(Double skuCalPrice) {
        this.skuCalPrice = skuCalPrice;
    }

    public Double getSkuMultiCurrencyCalPrice() {
        return skuMultiCurrencyCalPrice;
    }

    public void setSkuMultiCurrencyCalPrice(Double skuMultiCurrencyCalPrice) {
        this.skuMultiCurrencyCalPrice = skuMultiCurrencyCalPrice;
    }

    public Double getSkuMultiCurrencyDisplayPrice() {
        return skuMultiCurrencyDisplayPrice;
    }

    public void setSkuMultiCurrencyDisplayPrice(Double skuMultiCurrencyDisplayPrice) {
        this.skuMultiCurrencyDisplayPrice = skuMultiCurrencyDisplayPrice;
    }

    @Override
    public String toString() {
        return "SkuValPO{" +
                "actSkuCalPrice=" + actSkuCalPrice +
                ", actSkuMultiCurrencyCalPrice=" + actSkuMultiCurrencyCalPrice +
                ", actSkuMultiCurrencyDisplayPrice=" + actSkuMultiCurrencyDisplayPrice +
                ", availQuantity=" + availQuantity +
                ", inventory=" + inventory +
                ", isActivity=" + isActivity +
                ", skuCalPrice=" + skuCalPrice +
                ", skuMultiCurrencyCalPrice=" + skuMultiCurrencyCalPrice +
                ", skuMultiCurrencyDisplayPrice=" + skuMultiCurrencyDisplayPrice +
                '}';
    }
}
