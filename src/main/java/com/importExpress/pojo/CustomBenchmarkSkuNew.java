package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

public class CustomBenchmarkSkuNew implements Serializable{

    private static final long serialVersionUID = -4423370471811080151L;

    private Integer id;

    private String pid;

    private String skuAttr;

    private String skuPropIds;

    private SkuValPO skuVal;

    private Date createTime;

    private Date updateTime;

    // 28数据库做匹配1688原id 处理中数据标记：0-默认;1-正常处理 没关联1688id;2-处理异常;4-无entype数据;5-entype不为空 sku为空;',
    // 6-单规格正常处理且匹配1688id；7-多规格正常处理且匹配1688id；
    // 8-定时更新sku时候从清洗来的数据同步；9-定时sku更新时候无8的数据，使用拆分的sku
    private Integer flag;

    private String specId;

    private String skuId;

    private String wprice;

    private String finalWeight;

    public String getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(String finalWeight) {
        this.finalWeight = finalWeight;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        this.wprice = wprice;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public SkuValPO getSkuVal() {
        return skuVal;
    }

    public void setSkuVal(SkuValPO skuVal) {
        this.skuVal = skuVal;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(String skuAttr) {
        this.skuAttr = skuAttr;
    }

    public String getSkuPropIds() {
        return skuPropIds;
    }

    public void setSkuPropIds(String skuPropIds) {
        this.skuPropIds = skuPropIds;
    }

    public CustomBenchmarkSkuNew() {

    }

    public CustomBenchmarkSkuNew(String pid, Integer flag) {
        this.pid = pid;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "CustomBenchmarkSkuNew{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", skuAttr='" + skuAttr + '\'' +
                ", skuPropIds='" + skuPropIds + '\'' +
                ", skuVal=" + skuVal +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", flag=" + flag +
                ", specId='" + specId + '\'' +
                ", skuId='" + skuId + '\'' +
                '}';
    }

}