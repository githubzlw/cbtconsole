package com.cbt.bean;

public class NeedOffShelfBean {

    private int id;
    private String pid;
    private String imgUrl;
    private int isOffShelf;//上下架标识
    private int reason;//下架原因
    private String catid;
    private String catidName;
    private int competitiveFlag;//精品状态
    private int neverOffFlag;//永不下架标识
    private int updateFlag;
    private String updateTime;
    private String beginTime;
    private String endTime;
    private int startNum;
    private int limitNum;
    private int soldFlag;
    private int soldFlag2;
    private int soldFlag3;
    private Integer sourceFlag;
    private String noShelfInfo;

    public String getNoShelfInfo() {
        return noShelfInfo;
    }

    public void setNoShelfInfo(String noShelfInfo) {
        this.noShelfInfo = noShelfInfo;
    }

    public Integer getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(Integer sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    public int getSoldFlag3() {
        return soldFlag3;
    }

    public void setSoldFlag3(int soldFlag3) {
        this.soldFlag3 = soldFlag3;
    }

    public int getSoldFlag2() {
        return soldFlag2;
    }

    public void setSoldFlag2(int soldFlag2) {
        this.soldFlag2 = soldFlag2;
    }

    public int getSoldFlag() {
        return soldFlag;
    }

    public void setSoldFlag(int soldFlag) {
        this.soldFlag = soldFlag;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsOffShelf() {
        return isOffShelf;
    }

    public void setIsOffShelf(int isOffShelf) {
        this.isOffShelf = isOffShelf;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatidName() {
        return catidName;
    }

    public void setCatidName(String catidName) {
        this.catidName = catidName;
    }

    public int getCompetitiveFlag() {
        return competitiveFlag;
    }

    public void setCompetitiveFlag(int competitiveFlag) {
        this.competitiveFlag = competitiveFlag;
    }

    public int getNeverOffFlag() {
        return neverOffFlag;
    }

    public void setNeverOffFlag(int neverOffFlag) {
        this.neverOffFlag = neverOffFlag;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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
        return "NeedOffShelfBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", isOffShelf=" + isOffShelf +
                ", reason=" + reason +
                ", catid='" + catid + '\'' +
                ", catidName='" + catidName + '\'' +
                ", competitiveFlag=" + competitiveFlag +
                ", updateFlag=" + updateFlag +
                ", updateTime='" + updateTime + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startNum=" + startNum +
                ", limitNum=" + limitNum +
                '}';
    }
}
