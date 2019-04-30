package com.cbt.bean;

public class ImportExSkuShow {
    private String ppIds;
    private String skuAttrs;
    private float price;
    private double fianlWeight;

    public double getFianlWeight() {
        return fianlWeight;
    }

    public void setFianlWeight(double fianlWeight) {
        this.fianlWeight = fianlWeight;
    }

    public String getPpIds() {
        return ppIds;
    }

    public void setPpIds(String ppIds) {
        this.ppIds = ppIds;
    }

    public String getSkuAttrs() {
        return skuAttrs;
    }

    public void setSkuAttrs(String skuAttrs) {
        this.skuAttrs = skuAttrs;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{\"ppIds\":\"" + ppIds + "\", \"skuAttrs\":\"" + skuAttrs + "\", \"price\":\"" + price
                + "\", \"fianlWeight\":\"" + fianlWeight + "\"}";
    }

}
