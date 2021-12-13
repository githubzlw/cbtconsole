package com.cbt.bean;

public class ImportExSkuShow {
    private String ppIds;
    private String skuAttrs;
    private float price;
    private double fianlWeight;
    private String enType;
    private String chType;
    private String specId;


    private float freePrice;
    private float costPrice;

    private String skuId;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(float costPrice) {
		this.costPrice = costPrice;
	}

    public float getFreePrice() {
        return freePrice;
    }

    public void setFreePrice(float freePrice) {
        this.freePrice = freePrice;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getChType() {
        return chType;
    }

    public void setChType(String chType) {
        this.chType = chType;
    }

    /**
     * 体积重量
     */
    private double volumeWeight;

    public double getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(double volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

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

    public String getEnType() {
        return enType;
    }

    public void setEnType(String enType) {
        this.enType = enType;
    }

    @Override
    public String toString() {
        return "{\"ppIds\":\"" + ppIds + "\", \"skuAttrs\":\"" + skuAttrs + "\", \"price\":\"" + price
                + "\", \"fianlWeight\":\"" + fianlWeight + "\"}";
    }

}
