package com.importExpress.pojo;

import lombok.Data;

@Data
public class SampleOrderBean {
    private Integer id;
    private Integer userId;
    private String orderNo;
    private String pid;
    private String imgUrl;
    private String skuId;
    private String enType;
    private String createTime;
    private String updateTime;
    private int isChoose;
    private String enName;
    private String onlineUrl;
    private String catid;
    private String urlMd5;
    private String weight;
    private double volume;
    private double volumeWeight;
    private String sellUnit;
    private int isSoldFlag;

    public SampleOrderBean() {
    }

    public SampleOrderBean(Integer userId, String orderNo, String pid, String imgUrl, String skuId, String enType) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.pid = pid;
        this.imgUrl = imgUrl;
        this.skuId = skuId;
        this.enType = enType;
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
        sb.append('}');
        return sb.toString();
    }
}
