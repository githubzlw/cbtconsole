package com.cbt.onlinesql.dao;

import com.cbt.onlinesql.bean.OnlineDataInfo;

public interface OnlineDataInfoDao {

	/**
	 * 插入需要执行SQL的到OnlineDataInfo表
	 * 
	 * @param info
	 * @return
	 */
	public int insertOnlineDataInfo(OnlineDataInfo info) throws Exception;

}
