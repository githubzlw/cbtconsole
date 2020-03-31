package com.importExpress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.SynonymsCategoryWrap;

public interface SynonymsCategoryMapper {
	
	/**获取列表
	 * @param catid
	 * @param page
	 * @return
	 */
	List<SynonymsCategoryWrap> getCategoryList(@Param("catid")String catid,@Param("page")int page);
	
	
	/**列表数量
	 * @param catid
	 * @return
	 */
	int categoryListCount(@Param("catid")String catid);

}
