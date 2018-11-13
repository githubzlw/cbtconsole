package com.cbt.parse.daoimp;

import java.util.List;
import java.util.Map;

public interface ISearchCacheDao {
	/**
	 * 缓存查
	 */	
	public List<Map<String, Object>> searchGoodsByCache(String key, int param_id, int sort, String page, int sold);

	/**
	 * 创建表
	 */
	public int create(String tablename);
	/**
	 * 创建表
	 */
	public int create2(String tablename);
	/**
	 * 删除表
	 */
	public int delete(String tablename);


	/**
	 * 更新表
	 */
	public int update(String sql);

	/**
	 * 查询表
	 */
	public List<Map<String,Object>> search(String sql);

	/**
	 * 插新表
	 */
	public void insertNewTb(List<Map<String, Object>> list, String tableName);
	/**
	 * 插新表
	 */
	public void insertNewTb2(List<Map<String, Object>> list, String tableName, String param_id);

	/**
	 * 查询总页数
	 */
	public int searchGoodsCountByCache(String key, int param_id, int sold);
	
	
	/**查询表是否存在
	 * @date 2016年3月1日
	 * @author abc
	 * @param tableName
	 * @return  
	 */
	public int queryTable(String tableName);
	
}
