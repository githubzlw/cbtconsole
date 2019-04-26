package com.cbt.bean;

/**
 * 电商网站Sku
 *
 * @author JXW
 */
public class ImportExSku {
    private String skuAttr;
    private String skuPropIds;
    private String specId;
    private String skuId;
    private double fianlWeight;
    private ImportExSkuVal skuVal;

    public double getFianlWeight() {
        return fianlWeight;
    }

    public void setFianlWeight(double fianlWeight) {
        this.fianlWeight = fianlWeight;
    }

    public String getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(String skuAttr) {
        this.skuAttr = skuAttr;
    }

    public String getSkuPropIds() {
        return skuPropIds;
    }

    public void setSkuPropIds(String skuPropIds) {
        this.skuPropIds = skuPropIds;
    }

    public ImportExSkuVal getSkuVal() {
        return skuVal;
    }

    public void setSkuVal(ImportExSkuVal skuVal) {
        this.skuVal = skuVal;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"skuAttr\":\"")
                .append(skuAttr).append('\"');
        sb.append(",\"skuPropIds\":\"")
                .append(skuPropIds).append('\"');
        sb.append(",\"specId\":\"")
                .append(specId).append('\"');
        sb.append(",\"skuId\":\"")
                .append(skuId).append('\"');
        sb.append(",\"fianlWeight\":")
                .append(fianlWeight);
        sb.append(",\"skuVal\":")
                .append(skuVal);
        sb.append('}');
        return sb.toString();
    }
}
