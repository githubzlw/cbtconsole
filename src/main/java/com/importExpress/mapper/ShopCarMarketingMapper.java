package com.importExpress.mapper;

import com.importExpress.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCarMarketingMapper {
    int countByExample(ShopCarMarketingExample example);

    int deleteByExample(ShopCarMarketingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShopCarMarketing record);

    int insertSelective(ShopCarMarketing record);

    List<ShopCarMarketing> selectByExample(ShopCarMarketingExample example);

    ShopCarMarketing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShopCarMarketing record, @Param("example") ShopCarMarketingExample example);

    int updateByExample(@Param("record") ShopCarMarketing record, @Param("example") ShopCarMarketingExample example);

    int updateByPrimaryKeySelective(ShopCarMarketing record);

    int updateByPrimaryKey(ShopCarMarketing record);


    int updateGoodsCarPrice(@Param("goodsId") int goodsId, @Param("userId") int userId, @Param("goodsPrice") double goodsPrice, @Param("newPrice") double newPrice);

    int deleteMarketingByUserId(@Param("userId") int userId);


    int insertShopCarBatch(List<ShopCarMarketing> recordList);

    int updateShopCarBatch(List<ShopCarMarketing> recordList);

    int deleteShopCarBatch(List<ShopCarMarketing> recordList);

    List<ShopCarInfo> queryShopCarInfoByUserId(@Param("userId") int userId);

    ShopCarUserStatistic queryUserInfo(@Param("userId") int userId);


    /**
     * 更新客户的跟进信息
     *
     * @param userId
     * @return
     */
    int updateUserFollowTime(@Param("userId") int userId, @Param("adminId") int adminId);

    /**
     * 插入跟进日志
     *
     * @param userId
     * @param adminId
     * @param content
     * @return
     */
    int insertIntoShopCarFollow(@Param("userId") int userId, @Param("adminId") int adminId, @Param("content") String content);

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
     * 获取EDM跟踪列表
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
    List<ShopCarMarketing> selectByUserIdAndType(@Param("userId") int userId, @Param("website") int website);
}