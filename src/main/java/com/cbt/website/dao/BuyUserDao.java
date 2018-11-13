package com.cbt.website.dao;

import com.cbt.website.bean.OrderBuy;

import java.util.ArrayList;


public interface BuyUserDao {
	public int add(String orderid, String buyuser, int buyid);
	public int update(String orderid, String buyuser, int buyid);
	
	public ArrayList<OrderBuy> query(String orderid);

}
