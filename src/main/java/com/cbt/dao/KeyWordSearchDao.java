package com.cbt.dao;

import java.util.List;
import java.util.Map;

public interface KeyWordSearchDao {

	/**
	 * 直接查商品表
	 */	
	public List<Map<String,Object>> searchByKeyWords(String keyword, String title) throws Exception;
	
	/**
	 * 查询表
	 */	
	public List<Map<String,Object>> search(String sql) throws Exception;
	
	/**
	 * 缓存查
	 */	
	public List<Map<String, Object>> searchGoodsByCache(String keywords, String price1, String price2, String minq, String maxq,
                                                        String sort, String cid, String page, String pid) throws Exception;

	/**
	 * 更新同义词表
	 */
	public int updateSimilarwords(String zc, String tyc, String time) throws Exception;

	/**
	 * 插入同义词表
	 */
	public int insertSimilarwords(String zc, String tyc, String time) throws Exception;

	/**
	 * 更新词表
	 */
	public int updateKwsynonymstb(String zc, String tablename) throws Exception;

	/**
	 * 插词表
	 */
	public int insertKwsynonymstb(String zc, String tablename) throws Exception;

	/**
	 * 查产品表
	 */
	public List<Map<String, Object>> selectGoodsInfo(String tyc) throws Exception;

	/**
	 * 更新表
	 */
	public int update(String sql) throws Exception;

	/**
	 * 创建表
	 */
	public int create(String tablename) throws Exception;

	/**
	 * 插新表
	 */
	public void insertNewTb(List<Map<String, Object>> list, String tablename) throws Exception;

	/**
	 * 查询总页数
	 */
	public int searchGoodsCountByCache(String keywords, String price1, String price2, String minq, String maxq,
                                       String sort, String cid, String page, String pid) throws Exception;
	
}
