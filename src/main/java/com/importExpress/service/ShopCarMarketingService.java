package com.importExpress.service;

import com.importExpress.pojo.*;

import java.util.List;

public interface ShopCarMarketingService {

    int countByExample(ShopCarMarketingExample example);

    int deleteByExample(ShopCarMarketingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShopCarMarketing record);

    int insertSelective(ShopCarMarketing record);

    List<ShopCarMarketing> selectByExample(ShopCarMarketingExample example);

    ShopCarMarketing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(ShopCarMarketing record, ShopCarMarketingExample example);

    int updateByExample(ShopCarMarketing record, ShopCarMarketingExample example);

    int updateByPrimaryKeySelective(ShopCarMarketing record);

    int updateByPrimaryKey(ShopCarMarketing record);

    int updateGoodsCarPrice(int goodsId, int userId, double goodsPrice, double newPrice);

    int deleteMarketingByUserId(int userId);

    int insertShopCarBatch(List<ShopCarMarketing> recordList);

    int updateShopCarBatch(List<ShopCarMarketing> recordList);

    int deleteShopCarBatch(List<ShopCarMarketing> recordList);

    List<ShopCarInfo> queryShopCarInfoByUserId(int userId);


    ShopCarUserStatistic queryUserInfo(int userId);


    /**
     * 更新客户的跟进信息
     *
     * @param userId
     * @param adminId
     * @param content
     * @return
     */
    int updateAndInsertUserFollowInfo(int userId, int adminId, String content);

    /**
     * 获取购物车营销列表
     *
     * @param statistic
     * @return
     */
    List<ShopCarUserStatistic> queryForList(ShopCarUserStatistic statistic);

    /**
     * 物车营销列表总数统计
     *
     * @param statistic
     * @return
     */
    int queryForListCount(ShopCarUserStatistic statistic);


    List<ShopTrackingBean> queryTrackingList(ShopTrackingBean param);

    int queryTrackingListCount(ShopTrackingBean param);

    List<ZoneBean> queryAllCountry();


    /**
     * 分页获取EDM跟踪列表
     *
     * @param logBean
     * @return
     */
    List<FollowLogBean> queryFollowLogList(FollowLogBean logBean);


    /**
     * 获取EDM跟踪列表的总数
     *
     * @param logBean
     * @return
     */
    int queryFollowLogListCount(FollowLogBean logBean);


    /**
     * 更加userId和website 查询数据
     * @param userId
     * @param website
     * @return
     */
    List<ShopCarMarketing> selectByUserIdAndType(int userId, int website);

    /**
     * 查询所有需要更新的数据
     * @return
     */
    List<Integer> queryReloadConfigUserId();
}
