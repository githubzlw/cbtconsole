package com.importExpress.pojo;

import java.util.List;

public class AliProductBean {
    private Integer id;
    private String aliPid;//产品id
    private String aliName;//产品名称
    private String aliImg;//产品图片
    private String aliPrice;//产品价格
    private String aliUrl;//产品链接
    private String aliUnit;//单位
    private String keyword;
    private String aliCatid;
    private String aliWeight;
    private Integer adminId;
    private String createTime;
    private int startNum;
    private int limitNum;
    private List<ImportProductBean> productListLire;
    private List<ImportProductBean> productListPython;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliName() {
        return aliName;
    }

    public void setAliName(String aliName) {
        this.aliName = aliName;
    }

    public String getAliImg() {
        return aliImg;
    }

    public void setAliImg(String aliImg) {
        this.aliImg = aliImg;
    }

    public String getAliPrice() {
        return aliPrice;
    }

    public void setAliPrice(String aliPrice) {
        this.aliPrice = aliPrice;
    }

    public String getAliUrl() {
        return aliUrl;
    }

    public void setAliUrl(String aliUrl) {
        this.aliUrl = aliUrl;
    }

    public String getAliUnit() {
        return aliUnit;
    }

    public void setAliUnit(String aliUnit) {
        this.aliUnit = aliUnit;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAliCatid() {
        return aliCatid;
    }

    public void setAliCatid(String aliCatid) {
        this.aliCatid = aliCatid;
    }

    public String getAliWeight() {
        return aliWeight;
    }

    public void setAliWeight(String aliWeight) {
        this.aliWeight = aliWeight;
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

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public List<ImportProductBean> getProductListLire() {
        return productListLire;
    }

    public void setProductListLire(List<ImportProductBean> productListLire) {
        this.productListLire = productListLire;
    }

    public List<ImportProductBean> getProductListPython() {
        return productListPython;
    }

    public void setProductListPython(List<ImportProductBean> productListPython) {
        this.productListPython = productListPython;
    }

    @Override
    public String toString() {
        return "AliProductBean{" +
                "id=" + id +
                ", aliPid='" + aliPid + '\'' +
                ", aliName='" + aliName + '\'' +
                ", aliImg='" + aliImg + '\'' +
                ", aliPrice='" + aliPrice + '\'' +
                ", aliUrl='" + aliUrl + '\'' +
                ", aliUnit='" + aliUnit + '\'' +
                ", keyword='" + keyword + '\'' +
                ", aliCatid='" + aliCatid + '\'' +
                ", aliWeight='" + aliWeight + '\'' +
                ", adminId=" + adminId +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
