package com.cbt.website.dao;

import com.cbt.bean.OrderBean;

import java.util.Map;

public interface OrderInfoDao {

	
	/**
	 * 获取订单信息
	 * @param orderNo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public OrderBean getOrderInfo(String orderNo, String userId) throws Exception;

	/**
	 * 更新订单表中的ipn国家简称
	 * @param orderNo
	 * @return
	 */
	public int updateOrderinfoIpnAddress(String orderNo);


	/**
	 * 更新订单状态
	 * @param userId
	 * @param orderNo
	 * @param price
	 * @throws Exception
	 */
	public void updateOrderStatu(Integer userId, String orderNo) throws Exception;


	/**
	 * 更新订单信息
	 * @param userId
	 * @param orderNo
	 * @param price
	 * @throws Exception
	 */
	public int updateOrder(OrderBean order, String payPrice, boolean isPayFreight) throws Exception;


	/**
	 * 订单信息确认
	 * @param orderNo
	 * @param confirmname
	 * @param confirmtime
	 * @param paytype
	 * @param tradingencoding
	 * @param wtprice
	 * @param userId
	 * @throws Exception
	 */
	public int confirmOrder(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId) throws Exception;


	/**
	 * 订单信息补录
	 * @param orderNo
	 * @param confirmname
	 * @param confirmtime
	 * @param paytype
	 * @param tradingencoding
	 * @param wtprice
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int addPaymentConfirm(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId) throws Exception;



	/*
	 * 添加payment支付信息
	 */
	public int addpayment(String orderNo, String paytype, String tradingencoding, int userId)throws Exception;
	public String[] getPayMentInfo(String orderNo);

	/**
	 * 获取支付信息
	 * @param orderNo
	 * @return
	 */
	Map<String,Object> queryPaymentInfoByOrderNo(String orderNo);
}
