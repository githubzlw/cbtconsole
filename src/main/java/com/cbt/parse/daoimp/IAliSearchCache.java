package com.cbt.parse.daoimp;

import com.cbt.parse.bean.SearchGoods;

import java.util.ArrayList;
import java.util.Map;

public interface IAliSearchCache {
	/**添加一条数据param表
	 * @param bean
	 * @return
	 */
	public int addParam(String keyword, String catid, int sort);
	/**param表
	 * @param bean
	 * @return
	 */
	public int updateParam(String keyword, String catid, int sort, int param_id);

	/**获取数据id， param表
	 * @param bean
	 * @return
	 */
	public int getParamId(String keyword, String catid, int sort);

	/**批量添加datas表
	 * @param list
	 * @param param_id
	 * @return
	 */
	public int addData(ArrayList<SearchGoods> list, int param_id);
	/**批量更新datas表
	 * @param list
	 * @param param_id
	 * @return
	 */
	public int updateData(ArrayList<SearchGoods> list);

	/**获取商品集合  datas表
	 * @param param_id
	 * @return
	 */
	public ArrayList<SearchGoods> getDatas(String param_id, int sort, int sold, String page);
	/**删除商品集合  datas表
	 * @param param_id
	 * @return
	 */
	public  int deleteDatas();

	/**查询数据列表所有数据
	 * @param param_id
	 * @return
	 */
	public ArrayList<SearchGoods> getDatasLast(String param_id, String time);

	/**数据迁移至log表
	 * @param list
	 * @param param_id
	 * @return
	 */
	public int addLog();

	/**更新 当前缓存的页面数，比如当前缓存到第5页
	 * @param page
	 * @return
	 */
	public int updateParamCachePage(int page, int param_id);

	/**更新缓存标志    param_cache_flag=0，即停止后面页面的缓存
	 * @param flag
	 * @param param_id
	 * @return
	 */
	public int updateParamCacheFlag(int flag, int param_id);


	/**获取缓存标志
	 * @param param_id
	 * @return
	 */
	public int getParamCacheFlag(int param_id);

	/**获取缓存页数
	 * @param param_id
	 * @return
	 */
	public int getParamCachePage(int param_id);


	/**更新过滤后的商品页数
	 * @date 2016年2月26日
	 * @author abc
	 * @param param_id
	 * @return
	 */
	public int updateParamDataCount(int datas_count, int param_id);

	/**获取过滤后的商品数量
	 * @date 2016年2月26日
	 * @author abc
	 * @param param_id
	 * @return
	 */
	public int getDataCount(int param_id);
	/**获取过滤后的商品页数
	 * @date 2016年2月26日
	 * @author abc
	 * @param param_id
	 * @return
	 */
	public int getParamDataCount(int param_id);


	/**
	 * @date 2016年2月29日
	 * @author abc
	 * @param param_id
	 * @return
	 */
	public Map<String, Object>  getParam(int param_id);
	/**获取搜有param集合
	 * @date 2016年2月29日
	 * @author abc
	 * @return
	 */
	public ArrayList<Map<String, Object>> getParamAll();


	/**更新内存表名称
	 * @date 2016年2月29日
	 * @author abc
	 * @param param_id
	 * @return
	 */
	public int updateParamTabName(String table_name, int param_id);
	
	/**获取内存表
	 * @date 2016年2月29日
	 * @author abc
	 * @param param_id
	 * @return  
	 */
	public String getParamTabName(int param_id);
	
	/**获取大于某个时间的param数据
	 * @date 2016年3月1日
	 * @author abc
	 * @param time
	 * @return  
	 */
	public ArrayList<Map<String, String>>  getParamLatest(String time);

}
