package com.cbt.service;

import com.cbt.bean.Payment;
import com.cbt.common.BaseService;
import com.cbt.refund.bean.RefundBeanExtend;

import java.util.List;


public interface IPaymentService extends BaseService<Payment> {
	
	public List<Payment> getRefundAblePaymentByUid(Integer uid);   //根据用户ID查询可退款的到账

	public List<RefundBeanExtend> getUserCoustomRecordsByUid(Integer uid);   //根据用户ID查询可退款的到账

	public List<Object[]> getUserRealChargeMoneyByUid(Integer uid);
}
