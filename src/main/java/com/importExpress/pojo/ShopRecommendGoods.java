package com.importExpress.pojo;

import lombok.Data;

@Data
public class ShopRecommendGoods {
    private Integer id;
    private String shopId;
    private Integer recoId;
    private String pid;

    private String onlineUrl;
    private String goodsImg;
    private String goodsPrice;
    private String priceShow;
    private String goodsUnit;
    private String rangePrice;
    private String price1688;
    private String moq;
    private String remotePath;
    private String mainImg;
    private String enName;

    private int isOn;
    private String createTime;
    private int createAdminId;
    private String updateTime;
    private int updateAdminId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"shopId\":\"")
                .append(shopId).append('\"');
        sb.append(",\"pid\":\"")
                .append(pid).append('\"');
        sb.append(",\"enName\":\"")
                .append(enName).append('\"');
        sb.append(",\"onlineUrl\":\"")
                .append(onlineUrl).append('\"');
        sb.append(",\"goodsImg\":\"")
                .append(goodsImg).append('\"');
        sb.append(",\"priceShow\":\"")
                .append(priceShow).append('\"');
        sb.append(",\"goodsUnit\":\"")
                .append(goodsUnit).append('\"');
        sb.append(",\"rangePrice\":\"")
                .append(rangePrice).append('\"');
        sb.append(",\"mainImg\":\"")
                .append(mainImg).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
