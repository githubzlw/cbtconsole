package com.cbt.service;

import com.cbt.bean.*;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.GoodsEditBean;
import com.importExpress.pojo.GoodsParseBean;
import com.importExpress.pojo.OnlineGoodsCheck;

import java.util.List;

public interface CustomGoodsService {

    /**
     * 所有类别
     *
     * @return
     * @date 2016年12月22日
     * @author abc
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
     * 查询各类别下静态页面总数统计
     *
     * @param queryBean
     * @return
     */
    public List<CategoryBean> queryStaticizeCateroryByParam();

    /**
     * 产品列表
     *
     * @param catid  类别
     * @param page   页码
     * @param sttime 上传时间
     * @param edtime 上传时间
     * @param state  状态 2-产品下架 3-发布失败 4-发布成功
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public List<CustomGoodsBean> getGoodsList(String catid, int page, String sttime, String edtime, int state);

    /**
     * 获取指定产品
     *
     * @param pid  产品id
     * @param type 0-本地 1-线上
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public CustomGoodsPublish getGoods(String pid, int type);

    /**
     * 获取产品集合
     *
     * @param pidList 产品id列表
     * @return
     * @date 2017年3月21日
     * @author abc
     */
    public List<CustomGoodsBean> getGoodsList(String pidList);

    /**
     * 获取产品集合
     *
     * @param catid 类别id
     * @return
     * @date 2017年3月21日
     * @author abc
     */
    public List<CustomGoodsBean> getGoodsListByCatid(String catid);

    /**
     * 获取指定产品详情
     *
     * @param pid 产品id
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public String getGoodsInfo(String pid);

    /**
     * 更新指定产品详情数据
     *
     * @param bean 产品数据
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public int updateInfo(CustomGoodsBean bean);

    /**
     * 批量更新产品详情数据
     *
     * @param list 产品数据
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public int updateInfoList(List<CustomGoodsBean> list);

    /**
     * 发布数据到线上
     *
     * @param bean
     * @return
     * @date 2016年12月22日
     * @author abc
     */
    public int publish(CustomGoodsPublish bean);


    /**
     * 更新本地产品状态
     *
     * @param pid     产品id
     * @param state   2-产品下架 3-发布失败 4-发布成功
     * @param adminid 操作人id
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public int updateState(int state, String pid, int adminid);

    /**
     * 批量更新本地产品状态
     *
     * @param pids    产品id列表
     * @param state   2-产品下架 3-发布失败 4-发布成功
     * @param adminid 操作人id
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public boolean updateStateList(int state, String pids, int adminid);

    /**
     * 更新线上产品状态
     *
     * @param pid   产品id
     * @param valid 0-下架 1-在线
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public int updateValid(int valid, String pid);

    /**
     * 更新线上产品状态
     *
     * @param pids  产品id
     * @param valid 0-下架 1-在线
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public int updateValidList(int valid, String pids);

    /**
     * 添加操作记录
     *
     * @param pid   产品id
     * @param admin 操作人
     * @param state 状态
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public int insertRecord(String pid, String admin, int state, String record);

    /**
     * 批量添加操作记录
     *
     * @param pids  产品id列表
     * @param admin 操作人
     * @param state 状态
     * @return
     * @date 2017年3月14日
     * @author abc
     */
    public int insertRecordList(List<String> pids, String admin, int state, String record);

    /**
     * 获取产品状态记录
     *
     * @param pid 产品id号
     * @return
     * @date 2017年3月14日
     * @author abc
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
    public int setGoodsValid(String pid, String adminName, int adminId, int type, String remark);


    /**
     * @param pid
     * @param goodsState
     * @return int
     * @Title updateGoodsState
     * @Description 根据PID更新发布状态
     */
    public int updateGoodsState(String pid, int goodsState);

    /**
     * @param pidLst
     * @param adminid
     * @return boolean
     * @Title updateBmFlagByPids
     * @Description 批量更新人为对标falg
     */
    public boolean updateBmFlagByPids(String[] pidLst, int adminid);


    /**
     * @param shopId
     * @return ShopManagerPojo
     * @Title queryByShopId
     * @Description 根据shopid获取店铺信息
     */
    public ShopManagerPojo queryByShopId(String shopId);

    /**
     * @param mainPid     主商品pid
     * @param similarPids 相似商品pids
     * @param adminId     创建人id
     * @param existPids   已经存在的pids
     * @return boolean
     * @Title batchInsertSimilarGoods
     * @Description 批量插入相似商品
     */
    public boolean batchInsertSimilarGoods(String mainPid, String similarPids, int adminId, List<String> existPids);

    /**
     * @param mainPid
     * @return List<SimilarGoods>
     * @Title querySimilarGoodsByMainPid
     * @Description 根据主商品pid查询相似商品数据
     */
    public List<SimilarGoods> querySimilarGoodsByMainPid(String mainPid);

    /**
     * @param editBean
     * @return boolean
     * @Title setGoodsFlagByPid
     * @Description 设置商品标识
     */
    public boolean setGoodsFlagByPid(GoodsEditBean editBean);

    /**
     * @param pid
     * @return boolean
     * @Title checkIsHotGoods
     * @Description 判断是否是热卖的商品
     */
    public boolean checkIsHotGoods(String pid);

    /**
     * 查询线上非删除的
     *
     * @param pid
     * @return
     */
    String getWordSizeInfoByPid(String pid);


    /**
     * 删除文字尺码表信息
     *
     * @param pid
     * @return
     */
    boolean deleteWordSizeInfoByPid(String pid);

    /**
     * 更新商品为非对标商品
     *
     * @param pid
     * @param finalWeight
     * @return
     */
    boolean setNoBenchmarking(String pid, double finalWeight);


    /**
     * 设置永不下架标识
     *
     * @param pid
     * @return
     */
    boolean setNeverOff(String pid);

    /**
     * 插入已经编辑商品信息
     *
     * @param shopId
     * @param pid
     * @param adminId
     * @return
     */
    int insertPidIsEdited(String shopId, String pid, int adminId);

    /**
     * 根据PID检查是否存在已经编辑信息
     *
     * @param pid
     * @return
     */
    int checkIsEditedByPid(String pid);

    /**
     * 更新PID操作人信息
     *
     * @param editBean
     * @return
     */
    int updatePidIsEdited(GoodsEditBean editBean);


    /**
     * 根据用户ID查询已经录入的商品数量
     *
     * @param adminId
     * @param valid
     * @return
     */
    int queryTypeinGoodsTotal(int adminId, int valid);

    /**
     * 查询在线商品数量
     *
     * @param valid
     * @return
     */
    int queryOnlineGoodsTotal(int valid);

    /**
     * 查询已经编辑的商品数量
     *
     * @param adminId
     * @return
     */
    int queryIsEditOnlineGoodsTotal(int adminId, int valid);


    /**
     * 设置对标信息
     *
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @return
     */
    int saveBenchmarking(String pid, String aliPid, String aliPrice);

    public List<String> queryStaticizeList(String catid);


    /**
     * 插入编辑商品标识
     *
     * @param editBean
     * @return
     */
    int insertIntoGoodsEditBean(GoodsEditBean editBean);

    /**
     * 分页查询商品编辑日志
     *
     * @param editBean
     * @return
     */
    List<GoodsEditBean> queryGoodsEditBean(GoodsEditBean editBean);


    /**
     * 查询商品编辑日志总数
     *
     * @param editBean
     * @return
     */
    int queryGoodsEditBeanCount(GoodsEditBean editBean);


    /**
     * 获取产品表最大ID
     *
     * @return
     */
    int queryMaxIdFromCustomGoods();

    /**
     * 分页查询商品信息
     *
     * @param minId
     * @param maxId
     * @return
     */
    List<GoodsParseBean> queryCustomGoodsByLimit(int minId, int maxId);

    /**
     * 更新价格和统计信息
     *
     * @param goodsParseBean
     * @return
     */
    int updateCustomGoodsStatistic(GoodsParseBean goodsParseBean);

    /**
     * 确认是否存在标识的商品
     *
     * @param pid
     * @return
     */
    int checkIsExistsGoods(String pid);

    /**
     * 插入价格和统计信息
     *
     * @param goodsParseBean
     * @return
     */
    int insertCustomGoodsStatistic(GoodsParseBean goodsParseBean);


    /**
     * 更新商品搜索次数
     *
     * @return
     */
    int updateGoodsSearchNum(String pid);

    /**
     * 更新商品的点击次数
     *
     * @return
     */
    int updateGoodsClickNum();

    /**
     * 修改商品重量
     *
     * @param pid
     * @param newWeight
     * @param oldWeight
     * @param weightIsEdit
     * @return
     */
    int updateGoodsWeightByPid(String pid, double newWeight,double oldWeight,int weightIsEdit);

    /**
     * 更新和锁定利润率
     *
     * @param pid
     * @param type
     * @param editProfit
     * @return
     */
    int editAndLockProfit(String pid, int type, double editProfit);

    JsonResult setGoodsWeightByWeigher(String pid, String newWeight);

    JsonResult setGoodsWeightByWeigherNew(String pid, String newWeight);

    List<OnlineGoodsCheck> queryOnlineGoodsForList(OnlineGoodsCheck queryPm);

    int queryOnlineGoodsForListCount(OnlineGoodsCheck queryPm);

    List<CategoryBean> queryCategoryList(OnlineGoodsCheck queryPm);

    boolean refreshPriceRelatedData(CustomGoodsPublish bean);
}
