package com.cbt.warehouse.pojo;


import java.util.Date;

public class IdRelationTable {
    private Integer id;

    private String orderid;

    private String goodid;

    private String goodurl;

    private Integer goodstatus;

    private Date createtime;

    private String picturepath;

    private String barcode;

    private String tborderid;

    private String position;

    private String username;

    private String userid;

    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid == null ? null : goodid.trim();
    }

    public String getGoodurl() {
        return goodurl;
    }

    public void setGoodurl(String goodurl) {
        this.goodurl = goodurl == null ? null : goodurl.trim();
    }

    public Integer getGoodstatus() {
        return goodstatus;
    }

    public void setGoodstatus(Integer goodstatus) {
        this.goodstatus = goodstatus;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPicturepath() {
        return picturepath;
    }

    public void setPicturepath(String picturepath) {
        this.picturepath = picturepath == null ? null : picturepath.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getTborderid() {
        return tborderid;
    }

    public void setTborderid(String tborderid) {
        this.tborderid = tborderid == null ? null : tborderid.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }
}
