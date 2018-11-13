package com.cbt.dao;

import com.cbt.bean.LogBean;

public interface LogDao {

	/**
	 * 保存日志
	 */	
	public int saveLog(LogBean log);
	
}
