package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;

public class OrderProductSourceLogBean implements Serializable {

    private static final long serialVersionUID = 408217964610342129L;

    private Integer id;
    private Integer odId;
    private Date updatetime;
    private Integer purchaseState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOdId() {
        return odId;
    }

    public void setOdId(Integer odId) {
        this.odId = odId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(Integer purchaseState) {
        this.purchaseState = purchaseState;
    }
}
