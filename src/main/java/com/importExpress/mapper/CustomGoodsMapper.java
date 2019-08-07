package com.importExpress.mapper;

import com.cbt.bean.CategoryBean;
import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.CustomGoodsQuery;
import com.cbt.website.bean.PurchasesBean;
import com.importExpress.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CustomGoodsMapper {


    /**
     * 插入已经编辑商品信息
     *
     * @param shopId
     * @param pid
     * @param adminId
     * @return
     */
    int insertPidIsEdited(@Param("shopId") String shopId, @Param("pid") String pid, @Param("adminId") int adminId);

    /**
     * 根据PID检查是否存在已经编辑信息
     *
     * @param pid
     * @return
     */
    int checkIsEditedByPid(@Param("pid") String pid);

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
    int queryTypeinGoodsTotal(@Param("adminId") int adminId, @Param("valid") int valid);

    /**
     * 查询在线商品数量
     *
     * @param valid
     * @return
     */
    int queryOnlineGoodsTotal(@Param("valid") int valid);

    /**
     * 查询已经编辑的商品数量
     *
     * @param adminId
     * @return
     */
    int queryIsEditOnlineGoodsTotal(@Param("adminId") int adminId, @Param("valid") int valid);

    List<String> queryStaticizeList(@Param("catid") String catid);


    /**
     * 设置对标信息
     *
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @return
     */
    int saveBenchmarking(@Param("pid") String pid, @Param("aliPid") String aliPid, @Param("aliPrice") String aliPrice);


    /**
     * 插入编辑商品标识
     *
     * @param editBean
     * @return
     */
    int insertIntoGoodsEditBean(GoodsEditBean editBean);

    /**
     * 查询商品标识
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
     * 产品库数据分页查询
     *
     * @param goodsQuery
     * @return
     */
    List<CustomGoodsPublish> queryForListByParam(CustomGoodsQuery goodsQuery);

    /**
     * 产品库数据总数统计
     *
     * @param goodsQuery
     * @return
     */
    int queryForListByParamCount(CustomGoodsQuery goodsQuery);

    /**
     * 产品库类别数据查询
     *
     * @param goodsQuery
     * @return
     */
    List<CategoryBean> queryCategoryByParam(CustomGoodsQuery goodsQuery);

    /**
     * 添加商品评论
     *
     * @param map
     * @return
     */
    public int addReviewRemark(@Param("map") Map<String, String> map);

    /**
     * 编辑商品评论
     *
     * @param map
     * @return
     */
    public int updateReviewRemark(@Param("map") Map<String, String> map);


    /**
     * 根据PID查询商品详情
     *
     * @param pid
     * @return
     */
    CustomGoodsPublish queryGoodsDetailsByPid(@Param("pid") String pid);


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
    List<GoodsParseBean> queryCustomGoodsByLimit(@Param("minId") int minId, @Param("maxId") int maxId);

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
    int checkIsExistsGoods(@Param("pid") String pid);

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
    int updateGoodsSearchNum(@Param("pid") String pid);

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
    int updateGoodsWeightByPid(@Param("pid") String pid, @Param("newWeight") double newWeight, @Param("oldWeight") double oldWeight, @Param("weightIsEdit") int weightIsEdit);

    /**
     * 更新商品详情信息
     *
     * @param goodsPublish
     * @return
     */
    int updateGoodsDetailsByInfo(CustomGoodsPublish goodsPublish);

    /**
     * 查询商品评论内容
     *
     * @param pid
     * @return
     */
    public List<CustomGoodsPublish> getAllReviewByPid(@Param("pid") String pid);


    /**
     * 更新和锁定利润率
     *
     * @param pid
     * @param type
     * @param editProfit
     * @return
     */
    int editAndLockProfit(@Param("pid") String pid, @Param("type") int type, @Param("editProfit") double editProfit);

    List<OnlineGoodsCheck> queryOnlineGoodsForList(OnlineGoodsCheck queryPm);

    int queryOnlineGoodsForListCount(OnlineGoodsCheck queryPm);

    List<CategoryBean> queryCategoryList(OnlineGoodsCheck queryPm);

    /**
     * 查询根据链接获取的md5相同的数量
     *
     * @param pid
     * @param url
     * @return
     */
    int queryMd5ImgByUrlCount(@Param("pid") String pid, @Param("url") String url, @Param("shopId") String shopId);

    /**
     * 查询根据链接获取的md5相同的bean
     *
     * @param pid
     * @param url
     * @return
     */
    List<GoodsMd5Bean> queryMd5ImgByUrlList(@Param("pid") String pid, @Param("url") String url, @Param("shopId") String shopId);


    /**
     * 根据PID查询数据信息
     * @param pidList
     * @return
     */
    List<CustomGoodsPublish>  queryGoodsByPidList(@Param("pidList") List<String> pidList);


    /**
     * 更新详情数据
     * @param gd
     * @return
     */
    int updatePidEnInfo(CustomGoodsPublish gd);

    /**
     * 更新MD5删除的图片标记
     * @param pid
     * @param url
     * @return
     */
    int updateMd5ImgDeleteFlag(@Param("pid") String pid, @Param("url") String url, @Param("shopId") String shopId);

    /**
     * 删除同店铺MD5临时表数据
     * @param pid
     * @param url
     * @param shopId
     * @return
     */
    int deleteMd5ImgSameTempFlag(@Param("pid") String pid, @Param("url") String url, @Param("shopId") String shopId);



    /**
     * 获取相同店铺下的商品信息
     * @param shopMd5Bean
     * @return
     */
	List<ShopMd5Bean> queryForMd5List(ShopMd5Bean shopMd5Bean);

    /**
     *获取相同店铺下的商品信息总数
     * @param shopMd5Bean
     * @return
     */
	int queryForMd5ListCount(ShopMd5Bean shopMd5Bean);

    /**
     * 跟进店铺ID查询商品的MD5数据
     * @param shopId
     * @return
     */
	List<GoodsMd5Bean> queryShopGoodsByMd5(@Param("md5Val") String shopId);

    /**
     * 根据MD5数据标识已删除
     * @param goodsMd5
     * @return
     */
	int updateImgDeleteByMd5(@Param("goodsMd5") String goodsMd5);


	/**
     * 删除非同店铺MD5临时表数据
     * @param goodsMd5
     * @return
     */
    int deleteMd5ImgNoSameTempFlag(@Param("goodsMd5") String goodsMd5);

	/**
     * 获取不同店铺下的商品信息
     * @param shopMd5Bean
     * @return
     */
	List<ShopMd5Bean> queryMd5NoSameList(ShopMd5Bean shopMd5Bean);

    /**
     *获取不同店铺下的商品信息总数
     * @param shopMd5Bean
     * @return
     */
	int queryMd5NoSameListCount(ShopMd5Bean shopMd5Bean);


	/**
     * 设置产品对标信息
     * @param pid
     * @param aliPid
     * @param aliPrice
     * @return
     */
	int setNewAliPidInfo(@Param("pid") String pid, @Param("aliPid") String aliPid, @Param("aliPrice") String aliPrice);


	/**
     * 根据状态获取PID数据
     * @param state
     * @return
     */
    List<String> queryPidListByState(@Param("state") int state);

    /**
     * 插入价格或者重量记录
     * @param editBean
     * @return
     */
    int insertIntoGoodsPriceOrWeight(GoodsEditBean editBean);

    /**
     * 更新WeightFlag状态
     * @param pid
     * @param flag
     * @return
     */
    int updateWeightFlag(@Param("pid") String pid, @Param("flag") int flag);

    /**
     * 更新sku数据
     * @param pid
     * @param sku
     * @return
     */
    int updateSkuInfo(@Param("pid") String pid, @Param("sku") String sku);

    /**
     * 插入sku更新日志
     * @param pid
     * @param oldSku
     * @param newSku
     * @param adminId
     * @return
     */
    int insertIntoSkuLog(@Param("pid") String pid, @Param("oldSku") String oldSku, @Param("newSku") String newSku,
                         @Param("adminId") int adminId);


    /**
     * 标记商品软下架
     * @param pid
     * @param reason
     * @return
     */
    int remarkSoftGoodsValid(@Param("pid") String pid, @Param("reason") int reason);

    /**
     * 根据店铺ID查询PID数据
     * @param shopId : 店铺ID
     * @return
     */
    List<String> queryPidByShopId(@Param("shopId") String shopId);

    /**
     * 更新体积重量
     * @param pid : pID
     * @param newWeight : 新的重量
     * @return
     */
    int updateVolumeWeight(@Param("pid") String pid, @Param("newWeight") String newWeight);

    List<CustomGoodsPublish> queryGoodsShowInfos(CustomGoodsQuery goodsQuery);

    int queryGoodsShowInfosCount(CustomGoodsQuery goodsQuery);


    /**
     * 查询全部店铺售卖金额
     * @return
     */
    List<ShopGoodsSalesAmount> queryShopGoodsSalesAmountAll();

    /**
     * 查询单个店铺售卖金额
     * @param shopId
     * @return
     */
    ShopGoodsSalesAmount queryShopGoodsSalesAmountByShopId(String shopId);


    /**
     * 插入商品压缩和上传图片失败日志
     * @param pid
     * @param imgUrl
     * @param adminId
     * @param remark
     * @return
     */
    int insertIntoGoodsImgUpLog(@Param("pid") String pid, @Param("imgUrl") String imgUrl, @Param("adminId") int adminId, @Param("remark") String remark);
    @Select("SELECT shipno as shipnoid,tborderid from id_relationtable WHERE orderid=#{purchasesBean.orderNo} AND odid=#{purchasesBean.od_id} AND goodid=#{purchasesBean.goodsid} LIMIT 1")
    PurchasesBean FindShipnoByOdid(@Param("purchasesBean") PurchasesBean purchasesBean);
    @Select("SELECT shipno from taobao_1688_order_history WHERE orderid=#{tborderid} LIMIT 1")
    String FindShipnoByTbor(@Param("tborderid") String tborderid);

    /**
     * 根据shopId查询数据信息
     *
     * @param shopId
     * @return
     */
    List<CustomGoodsPublish> queryGoodsByShopId(String shopId);

    /**
     * 查询kids下类别信息
     * @return
     */
    List<String> queryKidsCanUploadCatid();

    List<Map<String, String>> queryAllOffLineReason();

    /**
     * 根据skuId查询中文规格信息
     * @param skuId
     * @return
     */
    String queryChTypeBySkuId(String skuId);
}
