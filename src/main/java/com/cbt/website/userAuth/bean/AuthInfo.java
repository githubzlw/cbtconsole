package com.cbt.website.userAuth.bean;

import java.io.Serializable;

/**
 * 权限实体类
 * 
 * @author admins
 *
 */
public class AuthInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int authId;
	private String authName;
	private String url;
	private String reMark;
	private int del; // 是否删除(启用) 0：未删除(启用) 1：已删除(禁用)
	private int orderNo; // 排序编号
	private int moduleType;// 模块类别 -1:暂不用区,0:暂未分类,1:常用,2:客户信息,3:仓库管理,4:财务模块,5:统计,6:配置模块

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReMark() {
		return reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getModuleType() {
		return moduleType;
	}

	public void setModuleType(int moduleType) {
		this.moduleType = moduleType;
	}

	@Override
	public String toString() {
		return "AuthInfo [authId=" + authId + ", authName=" + authName + ", url=" + url + ", reMark=" + reMark
				+ ", del=" + del + ", orderNo=" + orderNo + ", moduleType=" + moduleType + "]";
	}

}
