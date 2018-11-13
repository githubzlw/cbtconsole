package com.cbt.bean;

import java.io.Serializable;
import java.util.Date;

public class TabTrackDetails implements Serializable {

    private long id;

    private String trackNo;

    private Date actionDate;

    private String actionInfo;
    
    //添加显示详细物流信息  2018/08/07 16:19
    private String actionInfoCn;
    
    private Integer flag;
    
    public String getActionInfoCn() {
		return actionInfoCn;
	}

	public void setActionInfoCn(String actionInfoCn) {
		this.actionInfoCn = actionInfoCn;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackNo() {
        return this.trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public Date getActionDate() {
        return this.actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionInfo() {
        return this.actionInfo;
    }

    public void setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
    }

    @Override
    public String toString() {
        return "TabTrackDetails{" +
                "id=" + id +
                ", trackNo='" + trackNo + '\'' +
                ", actionDate=" + actionDate +
                ", actionInfo='" + actionInfo + '\'' +
                '}';
    }
}