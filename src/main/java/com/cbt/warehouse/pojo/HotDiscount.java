package com.cbt.warehouse.pojo;

public class HotDiscount {

    private int id;
    private int hotId;
    private String goodsPid;
    private double percentage;
    private String beginTime;
    private String endTime;
    private String createTime;
    private int sort;
    private int adminId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotId() {
        return hotId;
    }

    public void setHotId(int hotId) {
        this.hotId = hotId;
    }

    public String getGoodsPid() {
        return goodsPid;
    }

    public void setGoodsPid(String goodsPid) {
        this.goodsPid = goodsPid;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "HotDiscount{" +
                "id=" + id +
                ", hotId=" + hotId +
                ", goodsPid='" + goodsPid + '\'' +
                ", percentage=" + percentage +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sort=" + sort +
                ", adminId=" + adminId +
                '}';
    }
}
