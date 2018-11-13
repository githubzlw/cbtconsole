package com.cbt.website.bean;

import java.io.Serializable;
import java.util.Map;

public class QualityResult implements Serializable {

    private int id;
    private String orderid;
    private String goodsid;
    private String admName;
    private Map<String,String> result;
    private String catid;
    private String createtime;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getAdmName() {
        return admName;
    }

    public void setAdmName(String admName) {
        this.admName = admName;
    }

    public Map<String,String> getResult() {
        return result;
    }

    public void setResult(Map<String,String> result) {
        this.result = result;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}
