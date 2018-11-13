package com.cbt.dao;

import com.cbt.bean.CustomOnlineGoodsBean;

/**
 * 
 * @ClassName SyncPidToOnlineDao 
 * @Description 同步PID商品Dao
 * @author Jxw
 * @date 2018年3月8日
 */
public interface SyncPidToOnlineDao {
	
	/**
	 * 
	 * @Title queryGoodsInfoByPid 
	 * @Description 根据表名和PID查询商品信息
	 * @param goodsPid
	 * @param dealTable : 处理后的1688商品数据表
	 * @return
	 * @return CustomOnlineGoodsBean
	 */
	public CustomOnlineGoodsBean queryGoodsInfoByPid(String goodsPid, String dealTable);


	/**
	 *
	 * @Title queryGoodsLocalPath
	 * @Description 查询店铺商品的本地路径
	 * @param crawlTable
	 * @param goodsPid
	 * @return
	 * @return String
	 */
	public String queryGoodsLocalPath(String crawlTable, String goodsPid);


	/**
	 *
	 * @Title queryGoodsByTableAndPid
	 * @Description 根据表名和PID查询商品全部信息
	 * @param dealTable
	 * @param goodsPid
	 * @return
	 * @return CustomOnlineGoodsBean
	 */
	public CustomOnlineGoodsBean queryGoodsByTableAndPid(String dealTable, String goodsPid);

	/**
	 *
	 * @Title syncSingleGoodsToOnline
	 * @Description 同步商品数据到线上
	 * @param goods
	 * @param dealTable
	 * @return
	 * @return boolean
	 */
	public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods, String dealTable);

}
