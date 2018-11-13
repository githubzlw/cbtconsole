package com.cbt.customer.dao;

import com.cbt.bean.*;

import java.util.List;
import java.util.Map;

public interface IShopUrlDao {

    ShopUrl findById(int id);

    List<ShopUrl> findAll(String shopId, String shopUserName, String date, int start, int end,
                          String timeFrom, String timeTo, int isOn, int state, int isAuto, int readyDel, int shopType,
                          int authorizedFlag, int authorizedFileFlag, String shopids);

    int total(String shopId, String shopUserName, String date, String timeFrom, String timeTo, int isOn, int state,
              int isAuto, int readyDel, int shopType, int authorizedFlag, int authorizedFileFlag, String shopids);

    int delById(int id);

    public String getShopList(String admName, String days);


    List<ShopGoods> findAllGoods(String shopId, int start, int end);

    int goodsTotal(String shopId);

    int insertOrUpdate(ShopUrl su, String[] urls);

    /**
     * @param shopId
     * @return List<ShopGoods>
     * @Title query1688GoodsByShopId
     * @Description 查询店铺下面已经抓取1688的商品数据
     */
    public List<ShopGoodsInfo> query1688GoodsByShopId(String shopId);

    /**
     * @param shopId
     * @return List<ShopGoods>
     * @Title queryDealGoodsByShopId
     * @Description 查询店铺下面已经处理的商品数据
     */
    public List<ShopGoodsInfo> queryDealGoodsByShopId(String shopId);

    /**
     * @param infos
     * @return boolean
     * @Title insertShopInfos
     * @Description 插入店铺商品类别统计数据
     */
    public boolean insertShopInfos(List<ShopInfoBean> infos);

    /**
     * @param infos
     * @return boolean
     * @Title updateShopInfos
     * @Description 更新店铺商品类别统计数据
     */
    public boolean updateShopInfos(List<ShopInfoBean> infos);

    /**
     * @param infos
     * @return boolean
     * @Title insertShopCatidAvgWeight
     * @Description 插入店铺类别商品关键词重量
     */
    public boolean insertShopCatidAvgWeight(List<ShopInfoBean> infos);

    /**
     * @param shopId
     * @param catid
     * @return List<ShopInfoBean>
     * @Title queryInfoByShopId
     * @Description 根据店铺ID查询店铺下的类别等信息
     */
    public List<ShopInfoBean> queryInfoByShopId(String shopId, String catid);

    /**
     * @param shopId
     * @return List<GoodsProfitReference>
     * @Title queryGoodsProfitReferences
     * @Description 根据店铺id查询存在的线上的商品
     */
    public List<GoodsProfitReference> queryGoodsProfitReferences(String shopId);


    /**
     * @param shopInfos
     * @return boolean
     * @Title saveCategoryInfos
     * @Description 根据shopId保存设定值的信息
     */
    public boolean saveCategoryInfos(List<ShopInfoBean> shopInfos);


    /**
     * @param shopInfos
     * @return boolean
     * @Title batchUpdateGoodsInfo
     * @Description 批量更新商品的重量和价格等信息
     */
    public boolean batchUpdateGoodsInfo(List<ShopInfoBean> shopInfos);


    /**
     * @param shopId
     * @param pids
     * @return boolean
     * @Title deleteShopReadyGoods
     * @Description 批量删除店铺下清洗的商品数据
     */
    public boolean deleteShopReadyGoods(String shopId, String pids);


    /**
     * @param shopId
     * @param pids
     * @return boolean
     * @Title deleteShopOfferGoods
     * @Description 批量删除店铺下商品的原始表数据
     */
    public boolean deleteShopOfferGoods(String shopId, String pids);


    /**
     * @param shopId
     * @return List<ShopGoodsEnInfo>
     * @Title queryDealGoodsWithInfoByShopId
     * @Description 查询店铺下面已经处理的商品数据(含详情数据)
     */
    public List<ShopGoodsEnInfo> queryDealGoodsWithInfoByShopId(String shopId);


    /**
     * @param goodsList
     * @return boolean
     * @Title updateGoodsEninfo
     * @Description 更新商品的eninfo数据
     */
    public boolean updateGoodsEninfo(List<ShopGoodsEnInfo> goodsList);

    /**
     * @param shopId
     * @param pid
     * @return CustomGoodsPublish
     * @Title queryGoodsInfo
     * @Description 根据shopId和pid查询店铺商品
     */
    public CustomGoodsPublish queryGoodsInfo(String shopId, String pid);


    /**
     * @param cgp
     * @param shopId
     * @param adminId
     * @return boolean
     * @Title saveEditGoods
     * @Description 保存编辑后的商品信息
     */
    public boolean saveEditGoods(CustomGoodsPublish cgp, String shopId, int adminId);

    /**
     * @param shopId
     * @return List<CustomOnlineGoodsBean>
     * @Title queryReadyDealGoods
     * @Description 查询店铺下已经处理好的商品
     */
    public List<CustomOnlineGoodsBean> queryReadyDealGoods(String shopId);

    /**
     * @param shopId
     * @param pid
     * @return CustomOnlineGoodsBean
     * @Title queryGoodsByShopIdAndPid
     * @Description 查询店铺商品全部信息
     */
    public CustomOnlineGoodsBean queryGoodsByShopIdAndPid(String shopId, String pid);

    /**
     * @param goods
     * @param shopId
     * @return boolean
     * @Title syncSingleGoodsToOnline
     * @Description 单个商品同步线上
     */
    public boolean syncSingleGoodsToOnline(CustomOnlineGoodsBean goods, String shopId);

    /**
     * @param goods
     * @return boolean
     * @Title updateGoodsImg
     * @Description 更新下载好的图片
     */
    public boolean updateGoodsImg(CustomOnlineGoodsBean goods);

    /**
     * @param goods
     * @return boolean
     * @Title updateGoodsImgError
     * @Description 下载图片失败更新pid标识
     */
    public boolean updateGoodsImgError(CustomOnlineGoodsBean goods);

    /**
     * @param shopId
     * @return Map<String       ,       String>
     * @Title queryShopDealState
     * @Description 查询店铺的处理状态
     */
    public Map<String, String> queryShopDealState(String shopId);

    /**
     * @param shopId
     * @param state
     * @return boolean
     * @Title updateShopState
     * @Description 更新店铺发布状态
     */
    public boolean updateShopState(String shopId, int state);

    /**
     * @return List<Category1688Bean>
     * @Title queryAll1688Category
     * @Description 查询全部的1688类别数据
     */
    public List<Category1688Bean> queryAll1688Category();

    /**
     * @param shopId
     * @param goodsPid
     * @return String
     * @Title queryGoodsLocalPath
     * @Description 查询店铺商品的本地路径
     */
    public String queryGoodsLocalPath(String shopId, String goodsPid);

    /**
     * @param shopId
     * @return Map<String       ,       Integer>
     * @Title queryDealState
     * @Description 根据店铺ID查询店铺数据清洗状态
     */
    public Map<String, Integer> queryDealState(String shopId);


    /**
     * @param shopId
     * @return Map<String       ,       Integer>
     * @Title queryShopGoodsSync
     * @Description 统计店铺商品上线
     */
    public Map<String, Integer> queryShopGoodsSync(String shopId);

    /**
     * @param shopId
     * @return List<ShopCatidWeight>
     * @Title queryShopCatidWeightListByShopId
     * @Description 根据店铺ID查询类别关键词重量数据
     */
    public List<ShopCatidWeight> queryShopCatidWeightListByShopId(String shopId);


    /**
     * @param list
     * @return boolean
     * @Title batchInsertCatidWeight
     * @Description 批量插入店铺类别关键词重量数据
     */
    public boolean batchInsertCatidWeight(List<ShopCatidWeight> list);

    /**
     * @param list
     * @return boolean
     * @Title batchUpdateCatidWeight
     * @Description 批量更新店铺类别关键词重量数据
     */
    public boolean batchUpdateCatidWeight(List<ShopCatidWeight> list);


    /**
     * @param shopId
     * @param catid
     * @return double
     * @Title calculateAvgWeightByCatid
     * @Description 计算非当前店铺下同类别ID的平均重量
     */
    public float calculateAvgWeightByCatid(String shopId, String catid);

    /**
     * @param shopId
     * @return int
     * @Title batchUpdateCatidPath
     * @Description 批量更新CatidPath值
     */
    public int batchUpdateCatidPath(String shopId);

    /**
     * @return List<String>
     * @Title queryErrorClearShopList
     * @Description 查询清洗错误的店铺集合
     */
    public List<String> queryErrorClearShopList();


    /**
     * @param shopId
     * @return List<GoodsOfferBean>
     * @Title queryOriginalGoodsInfo
     * @Description 根据店铺id查询原始商品的重量等原始数据
     */
    public List<GoodsOfferBean> queryOriginalGoodsInfo(String shopId);

    /**
     * @param goodsErrInfos :重量异常标识 0正常 1特例 2异常已经处理 3异常未处理
     * @return boolean
     * @Title batchUpdateErrorWeightGoods
     * @Description 批量更新商品重量异常的标识
     */
    public boolean batchUpdateErrorWeightGoods(List<GoodsOfferBean> goodsErrInfos);

    /**
     * @param shopId
     * @param pids
     * @return boolean
     * @Title deleteOriginalGoodsOffer
     * @Description 批量删除原始店铺商品数据
     */
    public boolean deleteOriginalGoodsOffer(String shopId, String pids);


    /**
     * @param shopGoodsInfos
     * @param adminId
     * @return boolean
     * @Title insertShopGoodsDeleteImgs
     * @Description 保存店铺商品被删除的信息
     */
    public boolean insertShopGoodsDeleteImgs(List<ShopGoodsInfo> shopGoodsInfos, int adminId);


    /**
     * @param shopId
     * @return boolean
     * @Title updateWeightFlag
     * @Description 更新店铺商品的重量异常标识，恢复默认
     */
    public boolean updateWeightFlag(String shopId);


    /**
     * @param shopId
     * @return List<CustomGoodsPublish>
     * @Title queryOnlineGoodsByShopId
     * @Description 查询产品表已经存在的商品信息
     */
    public List<CustomGoodsPublish> queryOnlineGoodsByShopId(String shopId);

    /**
     * 保存问题店铺的问题信息
     *
     * @param shopId
     * @param type
     * @param remark
     * @return
     */
    boolean saveReadyDeleteShop(String shopId, int type, String remark);

    /**
     * 根据类别删除店铺商品
     * @param shopId
     * @param catids
     * @return
     */
    boolean deleteCatidGoods(String shopId, String catids);

    /**
     * 转换店铺录入状态为手动录入
     * @param shopId
     * @return
     */
    boolean changShopToManually(String shopId);

    /**
     * 检查是否是黑名单店铺
     * @param shopId
     * @return
     */
    boolean checkIsBlackShopByShopId(String shopId);


    /**
     * 设置店铺类型
     * @param shopId
     * @param type
     * @return
     */
    boolean setShopType(String shopId, int type);

    /**
     * 设置店铺已授权
     * @param shopId
     * @return
     */
    boolean setAuthorizedFlag(String shopId, Integer authorizedFlag);


    /**
     * 分页查询需要下架商品信息
     * @param offShelf
     * @return
     */
    List<NeedOffShelfBean>  queryNeedOffShelfByParam(NeedOffShelfBean offShelf);


    /**
     * 统计查询需要下架商品信息总数
     * @param offShelf
     * @return
     */
    int queryNeedOffShelfByParamCount(NeedOffShelfBean offShelf);

    /**
     * 查询精品商品的数据
     * @param pids
     * @return
     */
    Map<String,Integer> queryCompetitiveFlag(List<String> pids);

}
