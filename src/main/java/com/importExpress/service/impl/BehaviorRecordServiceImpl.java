package com.importExpress.service.impl;

import com.cbt.website.bean.UserBehavior;
import com.importExpress.mapper.BehaviorRecordMapper;
import com.importExpress.service.BehaviorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviorRecordServiceImpl implements BehaviorRecordService {

	@Autowired
	private BehaviorRecordMapper behaviorRecordMapper;

	@Override
	public List<UserBehavior> recordUserBehavior(int userid, int page,
			int pagesize, String viewDateTime) {
		page = page > 0 ? (page-1) * pagesize : 0;
		return behaviorRecordMapper.recordUserBehavior(userid, page, pagesize, viewDateTime);
	}

	@Override
	public int recordUserBehaviorCount(int userid, String viewDateTime) {
		
		return behaviorRecordMapper.recordUserBehaviorCount(userid, viewDateTime);
	}
	

}
