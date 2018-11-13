package com.cbt.searchByPic.service;

import com.cbt.bean.SearchIndex;
import com.cbt.bean.SearchResults;
import com.cbt.searchByPic.bean.CustomerRequireBean;

import java.util.List;

public interface SearchByPicService {

	List<CustomerRequireBean> selectAll(int parseInt);

	CustomerRequireBean selectByPrimaryKey(int parseInt);

	int insertSelective(SearchIndex searchIndex);

	int updateByPrimaryKey(SearchIndex searchIndex);

	List<SearchIndex> selectList(int parseInt, int indexId);

	List<SearchResults> selectByIndexIdAndPrice(int indexId, int start, Double minprice, Double maxprice);


	/**
	 * 根据indexid 查询 search_index 表数据
	 * @param indexid
	 * @return
	 */
	SearchIndex selectKeyWords(int id);

	List<SearchResults> selectByIndexId(int id, int paged);

	int updateValidByPids(int id, String pids, int i);
	
	int updateValidByPids1(int id, String pids, int i);

	/**批量插入
	 * @date 2016年7月14日
	 * @author abc
	 * @param list
	 * @return  
	 */
	int saveListByTempTable(List<SearchResults> list);

	List<SearchResults> selectResultByIndexId(int indexId, int start);
	 /**更新翻译时间
     * @date 2016年7月19日
     * @author abc
     * @param id
     * @return  
     */
	int updateTranslationTime(int id);

	/**
	 * 图片2.0 根据indexid 查询search_result数据
	 * @param indexId
	 * @return
	 */
	List<SearchResults> selectByIndexIdAll(Integer indexId);

	/**
	 * 根据indexid
	 * 删除search_result数据
	 * @param indexId
	 */
	void deleteByIndexId(Integer indexId);

	/**
	 * 更新search_index  syncflag
	 * @param indexId
	 * @param i
	 * @return
	 */
	int updateSyncFlag(Integer indexId, int i);

	//更新custom_search  indexId
	void updateCustomByIndexId(int parseInt, int index_id);

}
