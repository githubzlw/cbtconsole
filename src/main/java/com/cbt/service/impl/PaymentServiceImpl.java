package com.cbt.service.impl;

import com.cbt.bean.Payment;
import com.cbt.dao.IPaymentSSMDao;
import com.cbt.dao.IRechargeRecordSSMDao;
import com.cbt.refund.bean.RefundBeanExtend;
import com.cbt.service.IPaymentService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements IPaymentService {

	@Autowired
	private IPaymentSSMDao paymentSSMDao;
	@Autowired
	private IRechargeRecordSSMDao rechargerecordDao;

	@Override
	public Integer delById(Integer id) {
		return paymentSSMDao.delById(id);
	}

	@Override
	public Integer update(Payment t) {
		return paymentSSMDao.update(t);
	}

	@Override
	public Integer add(Payment t) {
		return paymentSSMDao.add(t);
	}

	@Override
	public Payment getById(Integer id) {
		Payment payment =paymentSSMDao.getById(id);
		if(payment!=null){
			String strjs="";
			strjs=JSONArray.fromObject(payment).toString();
			System.out.println(strjs);
		}
		return paymentSSMDao.getById(id);
	}

	@Override
	public List<Payment> getRefundAblePaymentByUid(Integer uid) {
		return paymentSSMDao.getRefundAblePaymentByUid(uid);
	}

	@Override
	public List<RefundBeanExtend> getUserCoustomRecordsByUid(Integer uid) {
		return paymentSSMDao.getUserCoustomRecordsByUid(uid);
	}

	@Override
	public List<Object[]> getUserRealChargeMoneyByUid(Integer uid) {
		return paymentSSMDao.getUserRealChargeMoneyByUid(uid);
	}


}
