package com.importExpress.service;

import com.cbt.website.bean.UserBehavior;

import java.util.List;

public interface BehaviorRecordService {
	
	/**获取行为记录
	 * @date 2018年4月2日
	 * @author user4
	 * @param userid 用户id
	 * @param page 页码
	 * @param pagesize 页码数量
	 * @param view_date_time 时间
	 * @return  
	 */
	List<UserBehavior> recordUserBehavior(int userid, int page, int pagesize, String view_date_time);
	/**获取行为记录数量
	 * @date 2018年4月2日
	 * @author user4
	 * @param userid 用户id
	 * @param view_date_time 时间
	 * @return  
	 */
	int recordUserBehaviorCount(int userid, String view_date_time);

}
