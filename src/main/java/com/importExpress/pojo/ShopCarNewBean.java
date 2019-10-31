package com.importExpress.pojo;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2019/10/23
 */
public class ShopCarNewBean {

    private Integer id;
    private Integer userId;
    private String importData;
    private String otherData;
    private int isNew;
    private int isTest;
    private int site;

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

    public String getImportData() {
        return importData;
    }

    public void setImportData(String importData) {
        this.importData = importData;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsTest() {
        return isTest;
    }

    public void setIsTest(int isTest) {
        this.isTest = isTest;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"importData\":\"")
                .append(importData).append('\"');
        sb.append(",\"otherData\":\"")
                .append(otherData).append('\"');
        sb.append(",\"isNew\":")
                .append(isNew);
        sb.append('}');
        return sb.toString();
    }
}
