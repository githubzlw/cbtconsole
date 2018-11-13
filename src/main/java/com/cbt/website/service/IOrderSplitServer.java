package com.cbt.website.service;

import com.cbt.bean.OrderBean;

import java.util.List;
import java.util.Map;

public interface IOrderSplitServer {

	/**2015-11-13 ylm***
	 * 订单拆分
	 * @param goods
	 * @return
	 */
//	public String splitOrder(String orderNo, String odids,int state,String adminUserName,Map<String, String> oddSMap);
	public String splitOrder(String orderNo, String odids, int state, String adminUserName);

	/**2015-11-17 ylm***
	 * 获取进行过分割的订单
	 * @param goods
	 * @return
	 */
	public List<OrderBean> getSplitOrder(String[] orderNos);

	/**2015-11-17 ylm***
	 * 获取进行过分割的订单详情
	 * @param goods
	 * @return
	 */
	public List<Object[]> getSplitOrderDetails(String[] orderNo);

	public String getUserEmailByUserName(int userId);

	/**
	 * 获取发送邮件的错误信息
	 * ylm
	 * @param
	 */
	public List<Object[]> getMessage_error(String time, String endtime, int page);

	/**
	 * 获取发送邮件的错误数量
	 * ylm
	 * @param
	 */
	public int getMessage_error(String time, String endtime);
    /**
     * Drop Ship订单拆分功能
     * @param orderno
     * @param odids
     * @param state
     * @param admName
     * @return
     */
	public Map<String, String> splitOrderShip(String orderNo, String odids, int userId, String state);
	
	/**
	 * 根据订单号获取订单详情信息
	 * @param orderNos
	 * @return
	 */
	public List<Object[]> getOrderDetails(String[] orderNos);

	
}
