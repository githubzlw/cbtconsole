package com.cbt.service.impl;

import com.cbt.bean.LogBean;
import com.cbt.dao.LogDao;
import com.cbt.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService{
	
	@Autowired
	private LogDao logDao;

	@Override
	public int saveLog(LogBean log) {
		// TODO Auto-generated method stub
		return logDao.saveLog(log);
	}
	
}
