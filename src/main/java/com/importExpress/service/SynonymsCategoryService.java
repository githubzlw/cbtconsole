package com.importExpress.service;

import java.util.List;

import com.importExpress.pojo.SynonymsCategoryWrap;

public interface SynonymsCategoryService {
	/**获取列表
	 * @param catid
	 * @param page
	 * @return
	 */
	List<SynonymsCategoryWrap> getCategoryList(String catid,int page);
	
	
	/**列表数量
	 * @param catid
	 * @return
	 */
	int categoryListCount(String catid);
	
	/**新增
	 * @param wrap
	 * @return
	 */
	int addCategory(SynonymsCategoryWrap wrap);
	
	
	/**更新类别同义词
	 * @param catid
	 * @param content
	 * @return
	 */
	int updateCategory(String catid,String content);
	
	/**删除类别
	 * @param catid
	 * @return
	 */
	int delete(String catid);

}
