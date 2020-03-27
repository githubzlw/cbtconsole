package com.importExpress.pojo;

import lombok.Data;

@Data
public class BFOrderInfo {
	private int id;
	private String orderNo;
	private int state;
	private int userId;
	private String payPrice;
	private String productCost;
	private String deliveryTime;
	private String addressId;
	private String createTime;
	private String updateTime;
	private String ip;
	private String remark;
	private int adminId;
	private String stateContent;
	private String method;
	private String feight;
}
