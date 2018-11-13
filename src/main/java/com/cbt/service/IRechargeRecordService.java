package com.cbt.service;


import com.cbt.bean.RechargeRecord;
import com.cbt.common.BaseService;

import java.util.List;

public interface IRechargeRecordService extends BaseService<RechargeRecord> {
	
	public List<RechargeRecord> findRecordByUid(Integer uid);
	
	public List<RechargeRecord> findRefundRecordsByUid(Integer uid);
	
}
