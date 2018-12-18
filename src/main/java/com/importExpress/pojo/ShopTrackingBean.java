package com.importExpress.pojo;

public class ShopTrackingBean {
    private int userId;
    private String followTime;
    private String orderNo;
    private double orderPayAmount;
    private String orderPayTime;
    private String orderPayEndTime;
    private int adminId;
    private String adminName;
    private int startNum;
    private int limitNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getOrderPayAmount() {
        return orderPayAmount;
    }

    public void setOrderPayAmount(double orderPayAmount) {
        this.orderPayAmount = orderPayAmount;
    }

    public String getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(String orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public String getOrderPayEndTime() {
        return orderPayEndTime;
    }

    public void setOrderPayEndTime(String orderPayEndTime) {
        this.orderPayEndTime = orderPayEndTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    @Override
    public String toString() {
        return "ShopTrackingBean{" +
                "userId=" + userId +
                ", followTime='" + followTime + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderPayTime='" + orderPayTime + '\'' +
                ", adminId=" + adminId +
                ", adminName=" + adminName +
                '}';
    }
}
