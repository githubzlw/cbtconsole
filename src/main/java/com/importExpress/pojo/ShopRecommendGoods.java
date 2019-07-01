package com.importExpress.pojo;

import lombok.Data;

@Data
public class ShopRecommendGoods {
    private Integer id;
    private String shopId;
    private Integer recoId;
    private String pid;

    private String goodsImportUrl;
    private String goodsImg;
    private String goodsPrice;
    private String priceShow;
    private String goodsUnit;
    private String rangePrice;
    private String price1688;
    private String moq;

    private int isOn;
    private String createTime;
    private int createAdminId;
    private String updateTime;
    private int updateAdminId;
}
