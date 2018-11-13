package com.cbt.pojo;

import java.io.Serializable;

/**
 * 采购添加商品评论实体类
 */
public class BuyerCommentPojo implements Serializable{
    private static final long serialVersionUID = -7612963429664006443L;
    private String orderid;
    private String goodsid;
    private String id;
    private String admuserid;
    private String countryid;
    private String email;
    private String uid;
    private String car_type;
    private String goods_pid;
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmuserid() {
        return admuserid;
    }

    public void setAdmuserid(String admuserid) {
        this.admuserid = admuserid;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }


}
