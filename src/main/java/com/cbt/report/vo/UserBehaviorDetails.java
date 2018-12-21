package com.cbt.report.vo;

/**
 * @author Jxw
 * @version 1.0.0
 * @ClassName UserBehaviorDetails
 * @Description 用户行为分析明细
 * @date 2018年5月14日
 */
public class UserBehaviorDetails {
    private int userId;
    private String email;
    private String createTime;
    private String pid;
    private int carNum;
    private String orderAmount;
    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createtime) {
        this.createTime = createtime;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    @Override
    public String toString() {
        return "UserBehaviorDetails{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", createTime='" + createTime + '\'' +
                ", pid='" + pid + '\'' +
                ", carNum=" + carNum +
                ", orderAmount=" + orderAmount +
                '}';
    }
}
