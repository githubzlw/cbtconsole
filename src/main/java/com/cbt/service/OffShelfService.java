package com.cbt.service;

public interface OffShelfService {

	/**
	 * @param pid
	 * @param unsellableReason
	 * @return 返回0 更新失败，返回1更新成功，返回2 unsellableReason值问题
	 */
	Integer updateByPid(String pid, Integer unsellableReason);
	Integer updateByPidJDBC(String pid, Integer unsellableReason);
	
	/**
	 * 软下架转硬下架
	 * */
	void goodsSoftOffShelf();

}
