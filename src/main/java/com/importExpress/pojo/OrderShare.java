package com.importExpress.pojo;

import lombok.Data;

@Data
public class OrderShare {

    private Long id;

    //图片
    private String goodsImg;

    //价格
    private String goodsPrice;

    //价格
    private String goodsPid;

    //店铺类型
    private String shopType;

    //订单号
    private String orderNo;
    //商品数量
    private String yourorder;

    public String getYourorder() {
        return yourorder;
    }

    public void setYourorder(String yourorder) {
        this.yourorder = yourorder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPid() {
        return goodsPid;
    }

    public void setGoodsPid(String goodsPid) {
        this.goodsPid = goodsPid;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }




}