package com.cbt.report.vo;

public class UserBehaviorTotal {

    private String queryTime;
    private int addCarNoRegisterNum;//有添加购物车的客户数量 （未注册的客户）
    private int addCarAllUserNum;//有添加购物车的客户数量（已注册）
    private int registerUserNum;//注册用户数量
    private int firstAddAdressNum;//第一次录入收货地址的用户
    private int markOrderTotalNum;//下单的总客户数量
    private int markOrderNewNum;//下单的新客户数量
    private int payOrderUserNum;//日付款按钮的用户数量
    private int recentViewNum;//当日产品单页浏览总次数
    private int payLogNum;//8Pay按钮点击独特人数
    private int payLogCat;//点击添加购物车按钮数量
    private int AllCatCount;//本日有添加购物车的客户数量 （所有客户）
    private int addCarWithOldUserNum;//3.3本日有添加购物车的客户数量 （老客户 （以前购买过））

    public int getPayOrderUserNum() {
        return payOrderUserNum;
    }

    public void setPayOrderUserNum(int payOrderUserNum) {
        this.payOrderUserNum = payOrderUserNum;
    }

    public int getRecentViewNum() {
        return recentViewNum;
    }

    public void setRecentViewNum(int recentViewNum) {
        this.recentViewNum = recentViewNum;
    }

    public int getPayLogNum() {
        return payLogNum;
    }

    public void setPayLogNum(int payLogNum) {
        this.payLogNum = payLogNum;
    }

    public int getPayLogCat() {
        return payLogCat;
    }

    public void setPayLogCat(int payLogCat) {
        this.payLogCat = payLogCat;
    }

    public int getAllCatCount() {
        return AllCatCount;
    }

    public void setAllCatCount(int allCatCount) {
        AllCatCount = allCatCount;
    }

    public int getAddCarWithOldUserNum() {
        return addCarWithOldUserNum;
    }

    public void setAddCarWithOldUserNum(int addCarWithOldUserNum) {
        this.addCarWithOldUserNum = addCarWithOldUserNum;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public int getAddCarNoRegisterNum() {
        return addCarNoRegisterNum;
    }

    public void setAddCarNoRegisterNum(int addCarNoRegisterNum) {
        this.addCarNoRegisterNum = addCarNoRegisterNum;
    }

    public int getAddCarAllUserNum() {
        return addCarAllUserNum;
    }

    public void setAddCarAllUserNum(int addCarAllUserNum) {
        this.addCarAllUserNum = addCarAllUserNum;
    }

    public int getRegisterUserNum() {
        return registerUserNum;
    }

    public void setRegisterUserNum(int registerUserNum) {
        this.registerUserNum = registerUserNum;
    }

    public int getFirstAddAdressNum() {
        return firstAddAdressNum;
    }

    public void setFirstAddAdressNum(int firstAddAdressNum) {
        this.firstAddAdressNum = firstAddAdressNum;
    }

    public int getMarkOrderTotalNum() {
        return markOrderTotalNum;
    }

    public void setMarkOrderTotalNum(int markOrderTotalNum) {
        this.markOrderTotalNum = markOrderTotalNum;
    }

    public int getMarkOrderNewNum() {
        return markOrderNewNum;
    }

    public void setMarkOrderNewNum(int markOrderNewNum) {
        this.markOrderNewNum = markOrderNewNum;
    }

    @Override
    public String toString() {
        return "UserBehaviorTotal{" +
                "queryTime='" + queryTime + '\'' +
                ", addCarNoRegisterNum=" + addCarNoRegisterNum +
                ", addCarAllUserNum=" + addCarAllUserNum +
                ", registerUserNum=" + registerUserNum +
                ", firstAddAdressNum=" + firstAddAdressNum +
                ", markOrderTotalNum=" + markOrderTotalNum +
                ", markOrderNewNum=" + markOrderNewNum +
                '}';
    }
}
