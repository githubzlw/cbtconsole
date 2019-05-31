package com.importExpress.pojo;

import lombok.Data;

@Data
public class UserOtherInfoBean {

    private Integer id;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String webFacebookUrl;
    /**
     * What kind of business services are beneficial to you:
     * [ 1] Combine Shipping (ocean freight, cheapest shipping rate)
     * [ 2] Combine Shipping (air freight, better price than shipping individually)
     * [3 ] Quality Control
     * [ 4] Custom Packaging
     */
    private String userType;
    private String userTypeDesc;

    /**
     * tell us what you want
     */
    private String remarks;

    private String createTime;
    private String followTime;

    private Integer adminId;

    private String beginTime;
    private String endTime;
    private int limitNum;
    private int startNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getWebFacebookUrl() {
        return webFacebookUrl;
    }

    public void setWebFacebookUrl(String webFacebookUrl) {
        this.webFacebookUrl = webFacebookUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserTypeDesc() {
        return userTypeDesc;
    }

    public void setUserTypeDesc(String userTypeDesc) {
        this.userTypeDesc = userTypeDesc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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
}
