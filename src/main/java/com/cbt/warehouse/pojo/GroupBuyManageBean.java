package com.cbt.warehouse.pojo;

/**
 * @author Jxw
 * @version 1.0.0
 * @ClassName GroupBuyManageBean
 * @Description 团购管理bean
 * @date 2018年5月21日
 */
public class GroupBuyManageBean {
    private int id;
    private String pid;
    private String goodsName;
    private String imgUrl;
    private int finalPriceNeedNum;
    private double finalPrice;
    private int initVirtualNum;
    private int effectiveDay;
    private String activeBeginTime;
    private String activeEndTime;
    private String activeDesc;
    private String shopId;
    private String createTime;
    private int isOn;
    private int adminId;
    private String adminName;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getFinalPriceNeedNum() {
        return finalPriceNeedNum;
    }

    public void setFinalPriceNeedNum(int finalPriceNeedNum) {
        this.finalPriceNeedNum = finalPriceNeedNum;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getInitVirtualNum() {
        return initVirtualNum;
    }

    public void setInitVirtualNum(int initVirtualNum) {
        this.initVirtualNum = initVirtualNum;
    }

    public int getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(int effectiveDay) {
        this.effectiveDay = effectiveDay;
    }

    public String getActiveBeginTime() {
        return activeBeginTime;
    }

    public void setActiveBeginTime(String activeBeginTime) {
        this.activeBeginTime = activeBeginTime;
    }

    public String getActiveEndTime() {
        return activeEndTime;
    }

    public void setActiveEndTime(String activeEndTime) {
        this.activeEndTime = activeEndTime;
    }

    public String getActiveDesc() {
        return activeDesc;
    }

    public void setActiveDesc(String activeDesc) {
        this.activeDesc = activeDesc;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsOn() {
        return isOn;
    }

    public void setIsOn(int isOn) {
        this.isOn = isOn;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GroupBuyManageBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", finalPriceNeedNum=" + finalPriceNeedNum +
                ", finalPrice=" + finalPrice +
                ", initVirtualNum=" + initVirtualNum +
                ", effectiveDay=" + effectiveDay +
                ", activeBeginTime='" + activeBeginTime + '\'' +
                ", activeEndTime='" + activeEndTime + '\'' +
                ", activeDesc='" + activeDesc + '\'' +
                ", shopId='" + shopId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isOn=" + isOn +
                ", adminId=" + adminId +
                '}';
    }
}
