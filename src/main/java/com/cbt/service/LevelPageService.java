package com.cbt.service;

import com.cbt.bean.LevelPageBean;

import java.util.List;

public interface LevelPageService {
	
	/**获取所有二级页面列表
	 * @date 2017年3月22日
	 * @author abc
	 * @return  
	 */
	List<LevelPageBean> getList(int page);
	
	/**更新二级页面
	 * @date 2017年3月22日
	 * @author abc
	 * @param id 索引id
	 * @return  
	 */
	int update(LevelPageBean bean);
	
	/**新增二级页面
	 * @date 2017年3月22日
	 * @author abc
	 * @param bean 二级页面
	 * @return  
	 */
	int insert(LevelPageBean bean);
	
	/**
	 * @date 2017年3月22日
	 * @author abc
	 * @param id
	 * @return  
	 */
	int delete(int id);

}
