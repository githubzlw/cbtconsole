package com.importExpress.service;

import com.importExpress.pojo.ShopCarInfo;
import com.importExpress.pojo.ShopCarMarketing;
import com.importExpress.pojo.ShopCarMarketingExample;
import com.importExpress.pojo.ShopCarUserStatistic;

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
     * @param userId
     * @param adminId
     * @param content
     * @return
     */
    int updateAndInsertUserFollowInfo(int userId,int adminId,String content);

    /**
     * 获取购物车营销列表
     * @param statistic
     * @return
     */
    List<ShopCarUserStatistic> queryForList(ShopCarUserStatistic statistic);

    /**
     * 物车营销列表总数统计
     * @param statistic
     * @return
     */
    int queryForListCount(ShopCarUserStatistic statistic);


}
