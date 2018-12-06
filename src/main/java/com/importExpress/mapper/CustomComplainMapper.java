package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

public interface CustomComplainMapper {

	/**批量插入
	 * @param pidList
	 * @return
	 */
	Integer insertPidList(List<String> list);
	
	/**批量更新投诉次数
	 * @param pidList
	 * @return
	 */
	Integer updateComplainCount(List<String> list);
	
	
	/**获取列表
	 * @param pidList
	 * @return
	 */
	List<String> selectByPidList(List<String> list);
	
}
