package com.importExpress.mapper;

import com.importExpress.pojo.ShopCarMarketing;
import com.importExpress.pojo.ShopCarMarketingExample;
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
}