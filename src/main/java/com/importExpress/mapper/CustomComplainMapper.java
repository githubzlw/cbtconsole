package com.importExpress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CustomComplainMapper {

	/**批量插入
	 * @param pidList
	 * @return
	 */
	Integer insertPidList(@Param("list")List<String> list,@Param("complainId")String complainId);
	
	/**批量更新投诉次数
	 * @param pidList
	 * @return
	 */
	Integer updateComplainCount(@Param("list")List<String> list,@Param("complainId")String complainId);
	
	
	/**获取列表
	 * @param pidList
	 * @return
	 */
	List<String> selectByPidList(List<String> list);
	
}
