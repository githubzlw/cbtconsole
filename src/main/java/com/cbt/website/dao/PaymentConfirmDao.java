package com.cbt.website.dao;

import com.cbt.website.bean.PaymentConfirm;

import java.util.Map;

public interface PaymentConfirmDao {
	//获取产品列表
	public Map<String,Object> selectPaymentConfirm(String orderno);
	
	public int insertPaymentConfirm(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId);
	
	
	public PaymentConfirm getPaymentConfirmBean(String orderNo);
	
	public int addPaymentConfirm(PaymentConfirm paymentconfirm);
	
	public int updatePaymentConfirm(PaymentConfirm paymentconfirm);
}
