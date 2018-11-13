package com.cbt.pojo;

public class AddBalanceInfo {
    private int userId;
    private double money;
    private String admin;
    private String remark;
    private String createtime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "AddBalanceInfo{" +
                "userId=" + userId +
                ", money=" + money +
                ", admin='" + admin + '\'' +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
