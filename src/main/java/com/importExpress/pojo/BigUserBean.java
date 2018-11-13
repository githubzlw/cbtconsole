package com.importExpress.pojo;

public class BigUserBean {

    private int userId;
    private String orderNo;
    private String lastOrderPayTime;
    private int salesId;
    private String salesName;
    private double orderTotalAmount;
    private int limitNum;
    private int startNum;

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

    public String getLastOrderPayTime() {
        return lastOrderPayTime;
    }

    public void setLastOrderPayTime(String lastOrderPayTime) {
        this.lastOrderPayTime = lastOrderPayTime;
    }

    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    @Override
    public String toString() {
        return "BigUserBean{" +
                "userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", lastOrderPayTime='" + lastOrderPayTime + '\'' +
                ", salesId=" + salesId +
                ", salesName='" + salesName + '\'' +
                ", orderTotalAmount=" + orderTotalAmount +
                '}';
    }
}
