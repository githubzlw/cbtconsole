package com.cbt.dao;

import com.cbt.bean.Payment;
import com.cbt.common.BaseDao;
import com.cbt.refund.bean.RefundBeanExtend;

import java.util.List;

public interface IPaymentSSMDao extends BaseDao<Payment>{
	public List<Payment> getRefundAblePaymentByUid(Integer uid);    //根据用户ID查询可退款的到账
	
	public List<RefundBeanExtend> getUserCoustomRecordsByUid(Integer uid);   //查询用户的消费记录
	
	public List<Object[]> getUserRealChargeMoneyByUid(Integer uid);  //查询用户通过PayPal和wire transfer消费的记录
	
}