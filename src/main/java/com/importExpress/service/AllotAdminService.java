package com.importExpress.service;


/**
 * @author 王宏杰
 *自动分配销售人员
 *2016-10-08
 */
public interface AllotAdminService {
	
	/**
	 * 自动分配销售人员
	 * @param type是否是订单，是-1
	 * 
	 */
	public void allotAdminSaler(int type, int userId, String sessionId, String email, String orderAdmin, String username);

}
