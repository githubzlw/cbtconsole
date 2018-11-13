package com.importExpress.service;

import com.importExpress.pojo.OrderBean;

import java.util.List;

public interface OrderSplitService {
	
	/**2015-11-13 ylm***
	 * 订单拆分
	 * @param goods
	 * @return
	 */
	public int splitOrder(String orderNo, String odids);
	
	/**2015-11-17 ylm***
	 * 获取进行过分割的订单
	 * @param goods
	 * @return
	 */
	public List<OrderBean> getSplitOrder(String orderNo);
	
	/**2015-11-17 ylm***
	 * 获取进行过分割的订单详情
	 * @param goods
	 * @return
	 */
	public List<Object[]> getSplitOrderDetails(String orderNo);
	
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
	
	public int addMessage_error(String email, String error, String info);
	
}
