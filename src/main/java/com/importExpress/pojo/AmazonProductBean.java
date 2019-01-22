package com.importExpress.pojo;

public class AmazonProductBean {
    private Integer id;
    private String amazonPid;//产品id
    private String amazonName;//产品名称
    private String amazonImg;//产品图片
    private String amazonPrice;//产品价格
    private String amazonUrl;//产品链接
    private String amazonUnit;//单位
    private String keyword;
    private String amazonCatid;
    private String amazonWeight;
    private Integer adminId;
    private String adminName;
    private String createTime;
    private Integer dealState;
    private String matchPid;
    private String updateTime;

    private int startNum;
    private int limitNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAmazonPid() {
        return amazonPid;
    }

    public void setAmazonPid(String amazonPid) {
        this.amazonPid = amazonPid;
    }

    public String getAmazonName() {
        return amazonName;
    }

    public void setAmazonName(String amazonName) {
        this.amazonName = amazonName;
    }

    public String getAmazonImg() {
        return amazonImg;
    }

    public void setAmazonImg(String amazonImg) {
        this.amazonImg = amazonImg;
    }

    public String getAmazonPrice() {
        return amazonPrice;
    }

    public void setAmazonPrice(String amazonPrice) {
        this.amazonPrice = amazonPrice;
    }

    public String getAmazonUrl() {
        return amazonUrl;
    }

    public void setAmazonUrl(String amazonUrl) {
        this.amazonUrl = amazonUrl;
    }

    public String getAmazonUnit() {
        return amazonUnit;
    }

    public void setAmazonUnit(String amazonUnit) {
        this.amazonUnit = amazonUnit;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAmazonCatid() {
        return amazonCatid;
    }

    public void setAmazonCatid(String amazonCatid) {
        this.amazonCatid = amazonCatid;
    }

    public String getAmazonWeight() {
        return amazonWeight;
    }

    public void setAmazonWeight(String amazonWeight) {
        this.amazonWeight = amazonWeight;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
    }

    public String getMatchPid() {
        return matchPid;
    }

    public void setMatchPid(String matchPid) {
        this.matchPid = matchPid;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "AmazonProductBean{" +
                "id=" + id +
                ", amazonPid='" + amazonPid + '\'' +
                ", amazonName='" + amazonName + '\'' +
                ", amazonImg='" + amazonImg + '\'' +
                ", amazonPrice='" + amazonPrice + '\'' +
                ", amazonUrl='" + amazonUrl + '\'' +
                ", amazonUnit='" + amazonUnit + '\'' +
                ", keyword='" + keyword + '\'' +
                ", amazonCatid='" + amazonCatid + '\'' +
                ", amazonWeight='" + amazonWeight + '\'' +
                ", adminId=" + adminId +
                ", createTime='" + createTime + '\'' +
                ", dealState=" + dealState +
                ", matchPid='" + matchPid + '\'' +
                '}';
    }
}
