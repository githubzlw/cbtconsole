package com.importExpress.mapper;

import com.cbt.website.bean.UserBehavior;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *用户行为表behavior_record
 *@author user4
 *@date 2018年4月2日
 *
 */
public interface BehaviorRecordMapper {
	/**获取行为记录
	 * @date 2018年4月2日
	 * @author user4
	 * @param userid 用户id
	 * @param page 页码
	 * @param pagesize 页码数量
	 * @param view_date_time 时间
	 * @return  
	 */
	List<UserBehavior> recordUserBehavior(@Param("userid") int userid, @Param("page") int page, @Param("pagesize") int pagesize, @Param("viewDateTime") String viewDateTime);
	/**获取行为记录数量
	 * @date 2018年4月2日
	 * @author user4
	 * @param userid 用户id
	 * @param view_date_time 时间
	 * @return
	 */
	int recordUserBehaviorCount(@Param("userid") int userid, @Param("viewDateTime") String viewDateTime);


}
