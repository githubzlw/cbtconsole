package com.cbt.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TabTrackInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;

    private String orderNo;

    private String orderList;

    private Integer packageNo;

    private String trackNo;

    private String trackCompany;

    private String tarckAddress;

    private Date orderPaytime;

    private Date orderDeliverDate;

    private Integer trackState; //1-备货中；2-已发货；3-已签收；4-退回；5-异常(海关扣押等)

    private Date addTime;

    private Date trackUpdate;

    private Date updatDate;

    private Date deliveredTime;

    private String trackUser;

    private int grabState;

    private String sourceType;

    private List<TabTrackDetails> tabTrackDetailsList;

    private Date senttime;// 新增 发货时间
    
    private String trackNote;
    
    @Override
	public String toString() {
		return "TabTrackInfo [id=" + id + ", orderNo=" + orderNo + ", orderList=" + orderList + ", packageNo="
				+ packageNo + ", trackNo=" + trackNo + ", trackCompany=" + trackCompany + ", tarckAddress="
				+ tarckAddress + ", orderPaytime=" + orderPaytime + ", orderDeliverDate=" + orderDeliverDate
				+ ", trackState=" + trackState + ", addTime=" + addTime + ", trackUpdate=" + trackUpdate
				+ ", updatDate=" + updatDate + ", deliveredTime=" + deliveredTime + ", trackUser=" + trackUser
				+ ", grabState=" + grabState + ", sourceType=" + sourceType + ", tabTrackDetailsList="
				+ tabTrackDetailsList + ", senttime=" + senttime + "]";
	}
    
	public Date getSenttime() {
		return senttime;
	}

	public void setSenttime(Date senttime) {
		this.senttime = senttime;
	}

	public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(Integer packageNo) {
        this.packageNo = packageNo;
    }

    public Integer getTrackState() {
        return trackState;
    }

    public void setTrackState(Integer trackState) {
        this.trackState = trackState;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<TabTrackDetails> getTabTrackDetailsList() {
        return tabTrackDetailsList;
    }

    public void setTabTrackDetailsList(List<TabTrackDetails> tabTrackDetailsList) {
        this.tabTrackDetailsList = tabTrackDetailsList;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderList() {
        return this.orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getTrackNo() {
        return this.trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getTrackCompany() {
        return this.trackCompany;
    }

    public void setTrackCompany(String trackCompany) {
        this.trackCompany = trackCompany;
    }

    public String getTarckAddress() {
        return this.tarckAddress;
    }

    public void setTarckAddress(String tarckAddress) {
        this.tarckAddress = tarckAddress;
    }

    public Date getOrderPaytime() {
        return this.orderPaytime;
    }

    public void setOrderPaytime(Date orderPaytime) {
        this.orderPaytime = orderPaytime;
    }

    public Date getAddTime() {
        return this.addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getTrackUpdate() {
        return this.trackUpdate;
    }

    public void setTrackUpdate(Date trackUpdate) {
        this.trackUpdate = trackUpdate;
    }

    public Date getUpdatDate() {
        return this.updatDate;
    }

    public void setUpdatDate(Date updatDate) {
        this.updatDate = updatDate;
    }

    public Date getDeliveredTime() {
        return this.deliveredTime;
    }

    public void setDeliveredTime(Date deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getTrackUser() {
        return this.trackUser;
    }

    public void setTrackUser(String trackUser) {
        this.trackUser = trackUser;
    }

    public int getGrabState() {
        return this.grabState;
    }

    public void setGrabState(int grabState) {
        this.grabState = grabState;
    }

    public Date getOrderDeliverDate() {
        return orderDeliverDate;
    }

    public void setOrderDeliverDate(Date orderDeliverDate) {
        this.orderDeliverDate = orderDeliverDate;
    }

	public String getTrackNote() {
		return trackNote;
	}

	public void setTrackNote(String trackNote) {
		this.trackNote = trackNote;
	}

	public TabTrackInfo() {
		super();
	}
//TabTrackInfo(trackNo, trackState, trackNote)
	public TabTrackInfo(String trackNo, Integer trackState, String trackNote) {
		super();
		this.trackNo = trackNo;
		this.trackState = trackState;
		this.trackNote = trackNote;
	}
	
	//添加转单信息查询  2018/08/07 15:16
	private String forwardNo;//转单单号
	
	private String forwardCompany; //转单物流公司
	
	private String forwardSourceType; //转单物流抓取地址

	public String getForwardNo() {
		return forwardNo;
	}

	public void setForwardNo(String forwardNo) {
		this.forwardNo = forwardNo;
	}

	public String getForwardCompany() {
		return forwardCompany;
	}

	public void setForwardCompany(String forwardCompany) {
		this.forwardCompany = forwardCompany;
	}

	public String getForwardSourceType() {
		return forwardSourceType;
	}

	public void setForwardSourceType(String forwardSourceType) {
		this.forwardSourceType = forwardSourceType;
	}
	
	
    
}