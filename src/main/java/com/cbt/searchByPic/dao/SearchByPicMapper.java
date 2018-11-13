package com.cbt.searchByPic.dao;

import com.cbt.bean.SearchIndex;
import com.cbt.bean.SearchResults;
import com.cbt.searchByPic.bean.CustomerRequireBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SearchByPicMapper {

	List<CustomerRequireBean> selectAll(@Param("parseInt") int parseInt);

	int count();

	CustomerRequireBean selectByPrimaryKey(@Param("parseInt") int parseInt);

	int countByEnName(String enName);

	void insertSelective(SearchIndex searchIndex);

	int updateByEnName(SearchIndex searchIndex);

	List<SearchIndex> selectList(@Param("parseInt") int parseInt, @Param("indexId") int indexId);

	int countIndex(@Param("indexId") int indexId);

	List<SearchResults> selectByIndexIdAndPrice(@Param("indexId") int indexId, @Param("start") int start, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

	int countByIndexIdAndPrce(@Param("indexId") int indexId, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

	SearchIndex selectKeyWords(int id);

	List<SearchResults> selectByIndexId(@Param("indexId") int indexId, @Param("start") int start);

	int countByIndexId(int id);

	int updateValidByPids(@Param("indexId") Integer indexId, @Param("pids") List<String> pids, @Param("valid") Integer valid);

	//更新 search_result 表
	/**删除临时表
	 * @date 2016年7月18日
	 * @author abc
	 * @return
	 */
	int dropTempTable();

	/**创建临时表
	 * @date 2016年7月18日
	 * @author abc
	 * @return
	 */
	int createTempTable();

	/**数据插入临时表
	 * @date 2016年7月18日
	 * @author abc
	 * @param list
	 * @return
	 */
	int insertTempTable(List<SearchResults> list);

	/**更新临时表数据
	 * @date 2016年7月18日
	 * @author abc
	 * @return
	 */
	int updateTempTable();

	/**临时表中数据插入总表中
	 * @date 2016年7月18日
	 * @author abc
	 * @return
	 */
	int addByTempTable();

	List<SearchResults> selectResultByIndexId(@Param("indexId") int indexId, @Param("start") int start);
	 /**更新翻译时间
     * @date 2016年7月19日
     * @author abc
     * @param id
     * @return
     */
	int updateTranslationTime(int id);

	int updateValidByPids1(@Param("indexId") Integer indexId, @Param("pids") List<String> pids, @Param("valid") Integer valid);

	/**
	 * 图片2.0  根据indexid 查询search_results 数据
	 * @param indexId
	 * @return
	 */
	List<SearchResults> selectByIndexIdAll(Integer indexId);

	void deleteByIndexId(Integer indexId);

	int updateSyncFlag(@Param("indexId") Integer indexId, @Param("i") int i);

	void updateCustomByIndexId(@Param("parseInt") int parseInt, @Param("index_id") int index_id);

	

}
