package com.cbt.messages.vo;

import java.util.Date;


public class MessagesCountVo {

    /**
     * 消息类型英文
     */
    private String type;

    /**
     * 类型中文
     */
    private String typeName;
    
    /**
     * 所有数量
     */
    private Integer countAll;

    /**
     * 已完结数量
     */
    private Integer isDeleteCount;
    
    /**
     * 未完结数量
     */
    private Integer noDeleteCount;
    
    /**
     * 所有数量
     */
    private Integer noArrgCount;
    
    /**
     * 开始时间
     */
    private Date timeFrom;
    
    /**
     * 结束时间
     */
	private Date timeTo;
	
	/**
     * 客服id
     */
    private Integer adminid;
    
    private Integer state;

    public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAdminid() {
		return adminid;
	}

	public void setAdminid(Integer adminid) {
		this.adminid = adminid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getCountAll() {
		return countAll;
	}

	public void setCountAll(Integer countAll) {
		this.countAll = countAll;
	}

	public Integer getIsDeleteCount() {
		return isDeleteCount;
	}

	public void setIsDeleteCount(Integer isDeleteCount) {
		this.isDeleteCount = isDeleteCount;
	}

	public Integer getNoDeleteCount() {
		return noDeleteCount;
	}

	public void setNoDeleteCount(Integer noDeleteCount) {
		this.noDeleteCount = noDeleteCount;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}

	public Integer getNoArrgCount() {
		return noArrgCount;
	}

	public void setNoArrgCount(Integer noArrgCount) {
		this.noArrgCount = noArrgCount;
	}

}