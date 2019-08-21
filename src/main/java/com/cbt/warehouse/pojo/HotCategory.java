package com.cbt.warehouse.pojo;

import java.io.Serializable;

/**
 * 热卖商品类别
 *
 * @author jxw
 */
public class HotCategory implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -9988442356L;

    private int id;
    private String categoryId;// 商品分类id
    private String categoryName;// 商品分类名称
    private String showName;// 显示名称
    private String showImg;// 显示图片
    private int isOn;// 状态: 0:关闭 1:启用
    private int sorting;// 排序号
    private int hotType;// 热卖类别，1热卖区、2今日折扣、3新品
    private String remark;// 备注
    private int adminId;// 创建人id
    private String adminName;//创建人
    private String createTime;// 创建时间
    private int updateAdminId;// 更新人id
    private String updateTime;// 更新时间
    private int startNum;
    private int limitNum;
    /**
     * 网站类别 1import 2 kids 3 pets 4 restaurant
     */
    private int webSite;

    public int getWebSite() {
        return webSite;
    }

    public void setWebSite(int webSite) {
        this.webSite = webSite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public int getIsOn() {
        return isOn;
    }

    public void setIsOn(int isOn) {
        this.isOn = isOn;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public int getHotType() {
        return hotType;
    }

    public void setHotType(int hotType) {
        this.hotType = hotType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUpdateAdminId() {
        return updateAdminId;
    }

    public void setUpdateAdminId(int updateAdminId) {
        this.updateAdminId = updateAdminId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
        return "HotCategory{" +
                "id=" + id +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", showName='" + showName + '\'' +
                ", showImg='" + showImg + '\'' +
                ", isOn=" + isOn +
                ", sorting=" + sorting +
                ", hotType=" + hotType +
                ", remark='" + remark + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateAdminId=" + updateAdminId +
                ", updateTime='" + updateTime + '\'' +
                ", startNum=" + startNum +
                ", limitNum=" + limitNum +
                '}';
    }
}
