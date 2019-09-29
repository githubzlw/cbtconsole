package com.importExpress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.RecommendCatalog;

public interface RecommendCatalogMapper {

    /**
     * 插入数据
     * @param cataLog
     * @return
     */
    int insertCatalog(RecommendCatalog cataLog);

    /**更新
     * @param cataLog
     * @return
     */
    int updateCatalog(RecommendCatalog cataLog);
    
    /**删除目录
     * @param cataLog
     * @return
     */
    int deleteCatalog(int id);
    
    /**目录列表
	 * @param page
	 * @param template
	 * @param catalog_name
	 * @return
	 */
	List<RecommendCatalog> catalogList(@Param("page")int page,@Param("template")int template,@Param("catalogName")String catalogName);
	
	
	/**统计目录列表数量
	 * @param template
	 * @param catalog_name
	 * @return
	 */
	int catalogCount(@Param("template")int template,@Param("catalogName")String catalogName);
	
	/**获取数据
     * @param id
     * @return
     */
    RecommendCatalog catalogById(int id);

}