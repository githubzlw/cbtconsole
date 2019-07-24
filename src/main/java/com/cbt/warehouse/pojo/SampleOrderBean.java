package com.cbt.warehouse.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleOrderBean implements Serializable {

    private static final long serialVersionUID = 1258635548L;

    private Integer id;
    private Integer userId;
    private String orderNo;
    private String pid;
    private String imgUrl;
    private String skuId;
    private String enType;
    private int isChoose;
    private String createTime;
    private String updateTime;
    private int goodsNum;
    private String enName;
    private String onlineUrl;
    private String catid;
    private String urlMd5;
    private String weight;
    private double volume;
    private double volumeWeight;
    private String sellUnit;
    private int isSoldFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getEnType() {
        return enType;
    }

    public void setEnType(String enType) {
        this.enType = enType;
    }

    public int getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(int isChoose) {
        this.isChoose = isChoose;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(double volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public String getSellUnit() {
        return sellUnit;
    }

    public void setSellUnit(String sellUnit) {
        this.sellUnit = sellUnit;
    }

    public int getIsSoldFlag() {
        return isSoldFlag;
    }

    public void setIsSoldFlag(int isSoldFlag) {
        this.isSoldFlag = isSoldFlag;
    }

    public SampleOrderBean() {
    }

    public SampleOrderBean(Integer userId, String orderNo, String pid, String imgUrl, String skuId, String enType, int goodsNum) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.pid = pid;
        this.imgUrl = imgUrl;
        this.skuId = skuId;
        this.enType = enType;
        this.goodsNum = goodsNum;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"pid\":\"")
                .append(pid).append('\"');
        sb.append(",\"imgUrl\":\"")
                .append(imgUrl).append('\"');
        sb.append(",\"enName\":\"")
                .append(enName).append('\"');
        sb.append(",\"onlineUrl\":\"")
                .append(onlineUrl).append('\"');
        sb.append(",\"catid\":\"")
                .append(catid).append('\"');
        sb.append(",\"weight\":\"")
                .append(weight).append('\"');
        sb.append(",\"sellUnit\":\"")
                .append(sellUnit).append('\"');
        sb.append(",\"isSoldFlag\":")
                .append(isSoldFlag);
        sb.append(",\"goodsNum\":")
                .append(goodsNum);
        sb.append('}');
        return sb.toString();
    }
}
