package com.cbt.onlinesql.bean;

import java.io.Serializable;

public class OnlineDataInfo implements Serializable {

	private static final long serialVersionUID = 9972351823L;

	private int id;
	private int uniqueId;// 执行序列号
	private String businessType;// 业务类型
	private String tableName;// 执行表名称
	private int userId; // 用户id
	private String orderId; // 订单id
	private String sqlStr; // 执行sql体
	private int flag;// 成功标识,0未执行，1执行成功，2执行失败

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "OnlineDataInfo [id=" + id + ", uniqueId=" + uniqueId + ", businessType=" + businessType + ", tableName="
				+ tableName + ", userId=" + userId + ", orderId=" + orderId + ", sqlStr=" + sqlStr + ", flag=" + flag
				+ "]";
	}

}
