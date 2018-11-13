package com.cbt.service.impl;

import com.cbt.bean.RechargeRecord;
import com.cbt.dao.IPaymentSSMDao;
import com.cbt.dao.IRechargeRecordSSMDao;
import com.cbt.service.IRechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechargeRecordServiceImpl implements IRechargeRecordService {

	@Autowired
	private IRechargeRecordSSMDao rechargeRecordDao;
	@Autowired
	private IPaymentSSMDao paymentDao;
	
	@Override
	public RechargeRecord getById(Integer id) {
		return rechargeRecordDao.getById(id);
	}

	@Override
	public Integer delById(Integer id) {
		return rechargeRecordDao.delById(id);
	}

	@Override
	public Integer update(RechargeRecord t) {
		return rechargeRecordDao.update(t);
	}

	@Override
	public Integer add(RechargeRecord t) {
		return rechargeRecordDao.add(t);
	}

	@Override
	public List<RechargeRecord> findRecordByUid(Integer uid) {
		return rechargeRecordDao.findRecordByUid(uid);
	}

	@Override
	public List<RechargeRecord> findRefundRecordsByUid(Integer uid) {
		// TODO Auto-generated method stub
		return rechargeRecordDao.findRefundRecordsByUid(uid);
	}


}
