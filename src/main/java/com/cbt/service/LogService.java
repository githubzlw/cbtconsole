package com.cbt.service;

import com.cbt.bean.LogBean;

public interface LogService {

	/**
	 * 保存日志
	 */	
	public int saveLog(LogBean log);
	
}
