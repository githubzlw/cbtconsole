package com.cbt.warehouse.util;

import com.cbt.bean.OrderInfoPrint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPrintInfoUtil {
	private String userid;
	private int count;
	private List<String> orders = new ArrayList<String>();
	private Map<String,Integer> map = new HashMap<String,Integer>();
	private Map<String,List<OrderInfoPrint>> maplist = new HashMap<String,List<OrderInfoPrint>>();
	private int orderLength;
	private String createTime;
	private String orderAddress;
	private String  userName; //用户名

	private int  isDropshipFlag ; //

	private int remark;
	public int getRemark() {
		return remark;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}
	public Map<String, List<OrderInfoPrint>> getMaplist() {
		return maplist;
	}
	public void setMaplist(Map<String, List<OrderInfoPrint>> maplist) {
		this.maplist = maplist;
	}
	public int getOrderLength() {
		return orderLength;
	}
	public void setOrderLength(int orderLength) {
		this.orderLength = orderLength;
	}
	public Map<String, Integer> getMap() {
		return map;
	}
	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getOrderAddress() {
		return orderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public List<String> getOrders() {
		return orders;
	}
	public void setOrders(List<String> orders) {
		this.orders = orders;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getIsDropshipFlag() {
		return isDropshipFlag;
	}
	public void setIsDropshipFlag(int isDropshipFlag) {
		this.isDropshipFlag = isDropshipFlag;
	}
	
	
}
