package com.cbt.bean;

/**
 * @author JiangXianwei
 * @version 1.0.0
 * @Description 团购商品bean
 * @date 2018年6月8日
 */
public class GroupBuyGoodsBean extends ShopGoodsInfo {
    private int isMain;
    private int adminId;
    private int isOn;
    private int gbId;

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getIsOn() {
        return isOn;
    }

    public void setIsOn(int isOn) {
        this.isOn = isOn;
    }

    public int getGbId() {
        return gbId;
    }

    public void setGbId(int gbId) {
        this.gbId = gbId;
    }

    @Override
    public String toString() {
        return "GroupBuyGoodsBean{" +
                "isMain=" + isMain +
                ", adminId=" + adminId +
                ", isOn=" + isOn +
                ", gbId=" + gbId +
                "} " + super.toString();
    }
}
