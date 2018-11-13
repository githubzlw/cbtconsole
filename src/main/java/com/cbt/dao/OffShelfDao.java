package com.cbt.dao;

import java.util.List;


public interface OffShelfDao {

	Integer updateValidAndUnsellableReasonByPid(String pid, Integer valid, Integer unsellableReason);

	Integer querySameProductIds(String pid);

	Integer queryRemainingByPid(String pid);
	
	Integer updateValidAndUnsellableReasonByPidJDBC(String pid, Integer valid, Integer unsellableReason);

	List<String> queryGoodsSoftOffShelf();


}
