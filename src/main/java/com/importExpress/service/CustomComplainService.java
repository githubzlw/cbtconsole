package com.importExpress.service;

import java.util.List;

public interface CustomComplainService {
	
	/**批量插入
	 * @param pidList
	 * @return
	 */
	Integer insertPidList(List<String> pidList);
	
	/**批量更新投诉次数
	 * @param pidList
	 * @return
	 */
	Integer updateComplainCount(List<String> pidList);
	
	
	/**获取列表
	 * @param pidList
	 * @return
	 */
	List<String> selectByPidList(List<String> pidList);

}
