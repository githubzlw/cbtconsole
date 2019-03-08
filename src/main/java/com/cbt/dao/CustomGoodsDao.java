package com.cbt.dao;

import com.cbt.bean.*;
import com.cbt.website.bean.OrderProductSourceLogBean;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.importExpress.pojo.CustomBenchmarkSkuNew;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.ShopMd5Bean;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.UpdateTblModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomGoodsDao {
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
	
	public List<CategoryBean> queryStaticizeCateroryByParam();

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
	 * 更新数据
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
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
	public int publish(CustomGoodsPublish bean,int isOnline);

	/**
	 * 批量发布数据到线上
	 * 
	 * @date 2016年12月22日
	 * @author abc
	 * @param bean
	 * @return
	 */
	public int publishList(List<CustomGoodsBean> list);

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

	public int queryStaticizeGoodsInfosCount();
	
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
	 * @param type
	 *            0本地 1线上
	 * @return
	 */
	public CustomGoodsPublish queryGoodsDetails(String pid, int type);

	/**
	 * 保存编辑后的1688对应电商网站的详情信息
	 * 
	 * @param cgp
	 * @param adminName
	 * @param type
	 * @return
	 */
	public int saveEditDetalis(CustomGoodsPublish cgp, String adminName, int adminId, int type);
	
	
	/**
	 * 根据pid查询1688货源主图筛选数量
	 * @param pid
	 * @return
	 */
	public GoodsPictureQuantity queryPictureQuantityByPid(String pid);
	
	
	/**
	 * 设置pid商品是否有效(上架或者下架)
	 * @param pid
	 * @param adminName
	 * @param adminId
	 * @param type
	 * @param reason
	 * @return
	 */
	public int setGoodsValid(String pid, String adminName, int adminId, int type, int reason, String remark);

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
	 * @Title publishTo28
	 * @Description 发布数据到28
	 * @param bean
	 * @return int
	 */
	public int publishTo28(CustomGoodsPublish bean);


	/**
	 *
	 * @Title updateBmFlagByPids
	 * @Description 批量更新人为对标falg
	 * @param pidLst
	 * @param adminid
	 * @return int
	 */
	public int updateBmFlagByPids(String[] pidLst, int adminid);

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

	/**
	 *
	 * @Title queryOffShelfPids
	 * @Description 查询需要下架商品的pid集合
	 * @return
	 * @return List<String>
	 */
	public List<String> queryOffShelfPids();

	/**
	 *
	 * @Title updateOffShelfByPid
	 * @Description 根据pid更新下架标识
	 * @param pid
	 * @param flag 1失败 2成功
	 * @return void
	 */
	public void updateOffShelfByPid(String pid, int flag);

	/**
	 *
	 * @Title setGoodsFlagByPid
	 * @Description 设置商品标识
	 * @param editBean
	 * @return
	 * @return boolean
	 */
	public boolean setGoodsFlagByPid(GoodsEditBean editBean);

	/**
	 *
	 * @Title checkIsHotGoods
	 * @Description 判断是否是热卖的商品
	 * @param pid
	 * @return
	 * @return boolean
	 */
	public boolean checkIsHotGoods(String pid);

	/**
	 *
	 * @Title queryUnsellableReason
	 * @Description 查询当前时间一天前的所有unsellableReason = 3的数据
	 * @return
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> queryUnsellableReason();

	/**
	 *
	 * @Title updateOnlineUnsellableReason
	 * @Description 更新线上UnsellableReason值
	 * @param pid
	 * @param flag : 商品下架原因：1-1688货源下架；2-不满足库存条件；3-销量无变化；4-页面404；5-重复验证合格；
	 * @return
	 * @return boolean
	 */
	public boolean updateOnlineUnsellableReason(String pid, int flag);


	/**
	 * 查询线上非删除的文字尺码表信息
	 * @param pid
	 * @return
	 */
	String getWordSizeInfoByPid(String pid);

	/**
	 * 删除文字尺码表信息
	 * @param pid
	 * @return
	 */
	boolean deleteWordSizeInfoByPid(String pid);

	/**
	 *
	 * @Title querySoldUnsellableReason
	 * @Description 取出 unsellableReason 中销量验证 有变化的数据
	 * @return
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> querySoldUnsellableReason();

	/**
	 *
	 * @Title querySoldUnsellableReason
	 * @Description 更新 unsellableReason 中销量验证 有变化的数据
	 * @return
	 * @return booldean
	 */
	public boolean updateOnlineSoldUnsellableReason(String key, int value, UpdateTblModel model);

	/**
	 *
	 * @Title queryOrderProductSourceLog
	 * @Description 查询order_product_source_log表中未同步到线上的数据
	 * @return
	 * @return 未同步数据集合
	 */
	public List<OrderProductSourceLogBean> queryOrderProductSourceLog();

	/**
	 *
	 * @Title queryOrderProductSourceLog
	 * @Description 同步order_product_source_log表中数据到线上
	 * @return
	 * @return 更新条数
	 */
	public int updateOrderProductSourceLog(OrderProductSourceLogBean orderProductSourceLogBean, RunSqlModel runSqlModel);


	/**
	 * 查询侵权商品
	 * @param limitNum
	 * @return
	 */
	List<String> queryInfringingGoodsByLimit(int limitNum);

	/**
	 * 更新后台产品编辑PID标识
	 * @param pid
	 * @return
	 */
	boolean updateInfringingGoodsByPid(String pid);



	/**
	 * 更新商品为非对标商品
	 * @param pid
	 * @param finalWeight
	 * @return
	 */
	boolean setNoBenchmarking(String pid, double finalWeight);

    /**
     * 更新对标关联数据 
     * 精准对标更新产品表ali_pid,ali_price,bm_flag=1,isBenchmark=1,
     *
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @param bmFlag
     * @param isBenchmark
     * @return
     */
    boolean upCustomerReady(String pid,String aliPid,String aliPrice,int bmFlag, int isBenchmark,String edName,String rwKeyword,int flag);
    
	/**
	 * 设置永不下架标识
	 * @param pid
	 * @return
	 */
	boolean setNeverOff(String pid);

	public Set<String> queryHotSellingGoods();

	public Set<String> queryShelvesGoods();

	public List<String> queryPermanentGoods();

	boolean updateCustomBenchmarkSkuNew(String pid, List<CustomBenchmarkSkuNew> insertList);


	/**
     * 设置对标商品标识
     *
     * @param pid
     * @param moq
     * @param rangePrice
     * @param priceContent
     * @param isSoldFlag
     * @param benchmarkingFlag
     * @return
     */
    boolean updateGoodsFlag(String pid, int moq, String rangePrice, String priceContent, int isSoldFlag, int benchmarkingFlag);


    String getCatxs(String catId, String factoryPrice);


    int checkSkuGoodsOffers(String pid);

    int updateSkuGoodsOffers(String pid, double finalWeight);

    int updateSourceProFlag(String pid, String finalWeight);

    int insertIntoSingleOffersChild(String pid, double finalWeight);

    List<CustomBenchmarkSkuNew> querySkuByPid(String pid);

    int deleteSkuByPid(String pid);

    int insertIntoSkuToOnline(List<CustomBenchmarkSkuNew> insertList);

	/**
	 * 更新28促销flag
	 * @param pid
	 * @return
	 */
	int updatePromotionFlag(String pid);


	/**
     * 更新详情数据
     * @param gd
     * @return
     */
    int updatePidEnInfo(CustomGoodsPublish gd);

    /**
     * 检查店铺公共图片删除表是否存在此图片
     * @param shopMd5Bean
     * @return
     */
	int checkShopGoodsImgIsMarkByParam(ShopMd5Bean shopMd5Bean);


	/**
     * 设置产品对标信息(28数据库)
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @return
     */
	int setNewAliPidInfo(String pid, String aliPid, String aliPrice);

}
