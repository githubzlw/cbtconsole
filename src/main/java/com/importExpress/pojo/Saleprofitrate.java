package com.importExpress.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Saleprofitrate {
    private Integer id;

    private Integer userid;

    private String goodid;

    private Integer salesnum;

    private BigDecimal saleprice;

    private BigDecimal sourceprice;

    private String profitrate;

    private Date creattime;

    private Date updatatime;

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

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid == null ? null : goodid.trim();
    }

    public Integer getSalesnum() {
        return salesnum;
    }

    public void setSalesnum(Integer salesnum) {
        this.salesnum = salesnum;
    }

    public BigDecimal getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(BigDecimal saleprice) {
        this.saleprice = saleprice;
    }

    public BigDecimal getSourceprice() {
        return sourceprice;
    }

    public void setSourceprice(BigDecimal sourceprice) {
        this.sourceprice = sourceprice;
    }

    public String getProfitrate() {
        return profitrate;
    }

    public void setProfitrate(String profitrate) {
        this.profitrate = profitrate == null ? null : profitrate.trim();
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Date getUpdatatime() {
        return updatatime;
    }

    public void setUpdatatime(Date updatatime) {
        this.updatatime = updatatime;
    }
}