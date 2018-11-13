package com.cbt.report.vo;

/**
 * 订单取消(全部或部分)详情
 *
 * @author JXW
 */
public class OrderCancelBean {

    private String year;// 年份
    private String month;// 月份
    private int userId;// 客户id
    private double amount;// 取消商品金额
    private String remarkId;//备注ID一般是订单号
    private String remark;//备注信息

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderCancelBean{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", userId=" + userId +
                ", amount=" + amount +
                ", remarkId='" + remarkId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
