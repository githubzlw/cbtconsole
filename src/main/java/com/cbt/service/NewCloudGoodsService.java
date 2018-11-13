package com.cbt.service;

import com.cbt.bean.*;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;

import java.util.List;

public interface NewCloudGoodsService {

	/**
	 * 所有类别
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @return
	 */
	public List<CategoryBean> getCaterory();

	/**
	 * 查询参数下类别和商品总数统计
	 * 
	 * @param queryBean
	 * @return
	 */
	public List<CategoryBean> queryCateroryByParam(CustomGoodsQuery queryBean);

	/**
	 * 产品列表
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param catid
	 *            类别
	 * @param page
	 *            页码
	 * @param sttime
	 *            上传时间
	 * @param edtime
	 *            上传时间
	 * @param state
	 *            状态 2-产品下架 3-发布失败 4-发布成功
	 * @return
	 */
	public List<CustomGoodsBean> getGoodsList(String catid, int page, String sttime, String edtime, int state);

	/**
	 * 获取指定产品
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param type
	 *            0-本地 1-线上
	 * @return
	 */
	public CustomGoodsPublish getGoods(String pid, int type);

	/**
	 * 获取产品集合
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param pidList
	 *            产品id列表
	 * @return
	 */
	public List<CustomGoodsBean> getGoodsList(String pidList);

	/**
	 * 获取产品集合
	 * 
	 * @date 2017年3月21日
	 * @author abc
	 * @param catid
	 *            类别id
	 * @return
	 */
	public List<CustomGoodsBean> getGoodsListByCatid(String catid);

	/**
	 * 获取指定产品详情
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @return
	 */
	public String getGoodsInfo(String pid);

	/**
	 * 更新指定产品详情数据
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
	 *            产品数据
	 * @return
	 */
	public int updateInfo(CustomGoodsBean bean);

	/**
	 * 批量更新产品详情数据
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param list
	 *            产品数据
	 * @return
	 */
	public int updateInfoList(List<CustomGoodsBean> list);

	/**
	 * 发布数据到线上
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
	 * @return
	 */
	public int publish(CustomGoodsPublish bean);


	/**
	 * 更新本地产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param state
	 *            2-产品下架 3-发布失败 4-发布成功
	 * @param adminid
	 *            操作人id
	 * @return
	 */
	public int updateState(int state, String pid, int adminid);

	/**
	 * 批量更新本地产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pids
	 *            产品id列表
	 * @param state
	 *            2-产品下架 3-发布失败 4-发布成功
	 * @param adminid
	 *            操作人id
	 * @return
	 */
	public boolean updateStateList(int state, String pids, int adminid);

	/**
	 * 更新线上产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param valid
	 *            0-下架 1-在线
	 * @return
	 */
	public int updateValid(int valid, String pid);

	/**
	 * 更新线上产品状态
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pids
	 *            产品id
	 * @param valid
	 *            0-下架 1-在线
	 * @return
	 */
	public int updateValidList(int valid, String pids);

	/**
	 * 添加操作记录
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id
	 * @param admin
	 *            操作人
	 * @param state
	 *            状态
	 * @return
	 */
	public int insertRecord(String pid, String admin, int state, String record);

	/**
	 * 批量添加操作记录
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pids
	 *            产品id列表
	 * @param admin
	 *            操作人
	 * @param state
	 *            状态
	 * @return
	 */
	public int insertRecordList(List<String> pids, String admin, int state, String record);

	/**
	 * 获取产品状态记录
	 * 
	 * @date 2017年3月14日
	 * @author abc
	 * @param pid
	 *            产品id号
	 * @return
	 */
	public List<CustomRecord> getRecordList(String pid, int page);

	/**
	 * 查询产品、翻译和对应ali商品的数据
	 * 
	 * @param queryBean
	 * @return
	 */
	public List<CustomGoodsPublish> queryGoodsInfos(CustomGoodsQuery queryBean);

	/**
	 * 查询参数下的总数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int queryGoodsInfosCount(CustomGoodsQuery queryBean);

	/**
	 * 批量更新商品的翻译名称
	 * 
	 * @param user
	 * @param cgLst
	 * @return
	 */
	public void batchSaveEnName(Admuser user, List<CustomGoodsBean> cgLst);

	/**
	 * 根据pids批量删除
	 * 
	 * @param pidLst
	 * @return
	 */
	public boolean batchDeletePids(String[] pidLst);

	/**
	 * 根据pid查询本地的1688商品以及对于翻译和ali商品的数据
	 * 
	 * @param pid
	 * @return
	 */
	public CustomGoodsPublish queryGoodsDetails(String pid, int type);

	/**
	 * 保存编辑后的1688对应电商网站的详情信息
	 * 
	 * @param cgp
	 * @param adminName
	 * @param adminId
	 * @param type
	 * @return
	 */
	public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type);

	/**
	 * 根据pid查询1688货源主图筛选数量
	 * 
	 * @param pid
	 * @return
	 */
	public GoodsPictureQuantity queryPictureQuantityByPid(String pid);

	/**
	 * 设置pid商品是否有效(上架或者下架)
	 * 
	 * @param pid
	 * @param adminName
	 * @param adminId
	 * @param type
	 * @return
	 */
	public int setGoodsValid(String pid, String adminName, int adminId, int type);
	
	
	/**
	 * 
	 * @Title updateGoodsState 
	 * @Description 根据PID更新发布状态
	 * @param pid
	 * @param goodsState
	 * @return int
	 */
	public int updateGoodsState(String pid, int goodsState);

	/**
	 *
	 * @Title updateBmFlagByPids
	 * @Description 批量更新人为对标falg
	 * @param pidLst
	 * @param adminid
	 * @return boolean
	 */
	public boolean updateBmFlagByPids(String[] pidLst, int adminid);


	/**
	 *
	 * @Title queryByShopId
	 * @Description 根据shopid获取店铺信息
	 * @param shopId
	 * @return  店铺信息pojo
	 * @return ShopManagerPojo
	 */
	public ShopManagerPojo queryByShopId(String shopId);

	/**
	 *
	 * @Title batchInsertSimilarGoods
	 * @Description 批量插入相似商品
	 * @param mainPid 主商品pid
	 * @param similarPids 相似商品pids
	 * @param adminId 创建人id
	 * @param existPids 已经存在的pids
	 * @return
	 * @return boolean
	 */
	public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId, List<String> existPids);
	
	/**
	 * 
	 * @Title querySimilarGoodsByMainPid 
	 * @Description 根据主商品pid查询相似商品数据
	 * @param mainPid
	 * @return
	 * @return List<SimilarGoods>
	 */
	public List<SimilarGoods> querySimilarGoodsByMainPid(String mainPid);
}
