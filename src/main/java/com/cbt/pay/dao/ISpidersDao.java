package com.cbt.pay.dao;

import com.cbt.bean.SpiderBean;

import java.util.List;

public interface ISpidersDao {
	/**
	 * wanyang
	 * 
	 * @param spider
	 * 获取购物车商品的其他信息（交期，订量等等）
	 */
	public  List<SpiderBean> getSelectedItem(int userid, String itemid, int state);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取购物车商品的其他信息（价格，订量等等）
	 */
	public List<Object[]> getSelectedItemPrice(int userid, String itemid, int state);
	
	/**
	 * ylm
	 * 
	 * @param spider
	 * 获取订单信息，地址，交期
	 */
	public List<String[]> getOrderInfo(String orderNo);
	
	/**
	 * ylm
	 * 
	 * @param spider
	 * 获取订单详情信息，产品名称，总价
	 */
	public List<Object[]> getOrderDetails(String orderNo);
}
