package com.cbt.website.service;

import java.util.Map;

public interface PaymentConfirmServer {
	//获取产品列表
	public Map<String,Object> getPaymentConfirm(String orderno);
	
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
	 */
	public int addPaymentConfirm(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId);
	
	
	
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
	
	
	
	
	
}
