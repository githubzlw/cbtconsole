package com.cbt.warehouse.pojo;

public class UserCouponBean {

    private int id;
    private int userId;
    private String orderNo;
    private double couponValue;
    private int goodsId;//goods_car 表ID
    private String goodsPid;
    private int type;//类型 0默认 1团购活动
    private int sourceFlag;//来源标识 0收入，1支出


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(double couponValue) {
        this.couponValue = couponValue;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsPid() {
        return goodsPid;
    }

    public void setGoodsPid(String goodsPid) {
        this.goodsPid = goodsPid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(int sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    @Override
    public String toString() {
        return "UserCouponBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", couponValue=" + couponValue +
                ", goodsId=" + goodsId +
                ", goodsPid='" + goodsPid + '\'' +
                ", type=" + type +
                ", sourceFlag=" + sourceFlag +
                '}';
    }
}
