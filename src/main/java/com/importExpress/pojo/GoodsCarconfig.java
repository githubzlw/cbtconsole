package com.importExpress.pojo;

import java.util.Date;

public class GoodsCarconfig {
    private Integer id;

    private Integer userid;

    private String shippingname;

    private Double shippingcost;

    private String shippingdays;

    private Integer changeexpress;

    private Double saveFreight;

    private Integer addflag;

    private String usercookieid;

    private String sumTypePrice;

    private Double totalFreight;

    private Date updatetime;

    private Double fastshipbalance;

    private String usermark;

    private Integer needcheck;

    private int isNew;

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getShippingname() {
        return shippingname;
    }

    public void setShippingname(String shippingname) {
        this.shippingname = shippingname == null ? null : shippingname.trim();
    }

    public Double getShippingcost() {
        return shippingcost;
    }

    public void setShippingcost(Double shippingcost) {
        this.shippingcost = shippingcost;
    }

    public String getShippingdays() {
        return shippingdays;
    }

    public void setShippingdays(String shippingdays) {
        this.shippingdays = shippingdays == null ? null : shippingdays.trim();
    }

    public Integer getChangeexpress() {
        return changeexpress;
    }

    public void setChangeexpress(Integer changeexpress) {
        this.changeexpress = changeexpress;
    }

    public Double getSaveFreight() {
        return saveFreight;
    }

    public void setSaveFreight(Double saveFreight) {
        this.saveFreight = saveFreight;
    }

    public Integer getAddflag() {
        return addflag;
    }

    public void setAddflag(Integer addflag) {
        this.addflag = addflag;
    }

    public String getUsercookieid() {
        return usercookieid;
    }

    public void setUsercookieid(String usercookieid) {
        this.usercookieid = usercookieid == null ? null : usercookieid.trim();
    }

    public String getSumTypePrice() {
        return sumTypePrice;
    }

    public void setSumTypePrice(String sumTypePrice) {
        this.sumTypePrice = sumTypePrice == null ? null : sumTypePrice.trim();
    }

    public Double getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(Double totalFreight) {
        this.totalFreight = totalFreight;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Double getFastshipbalance() {
        return fastshipbalance;
    }

    public void setFastshipbalance(Double fastshipbalance) {
        this.fastshipbalance = fastshipbalance;
    }

    public String getUsermark() {
        return usermark;
    }

    public void setUsermark(String usermark) {
        this.usermark = usermark == null ? null : usermark.trim();
    }

    public Integer getNeedcheck() {
        return needcheck;
    }

    public void setNeedcheck(Integer needcheck) {
        this.needcheck = needcheck;
    }
}