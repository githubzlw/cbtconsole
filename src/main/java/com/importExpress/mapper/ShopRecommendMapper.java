package com.importExpress.mapper;

import com.importExpress.pojo.ShopRecommendGoods;
import com.importExpress.pojo.ShopRecommendInfo;

import java.util.List;

public interface ShopRecommendMapper {

    List<ShopRecommendInfo> queryShopRecommendInfoList();

    List<ShopRecommendInfo> queryRecommendInfoByShopId(String shopId);

    int checkRecommendInfoByShopId(String shopId);

    int insertShopRecommendInfo(ShopRecommendInfo shopRecommendInfo);

    int updateShopRecommendInfo(ShopRecommendInfo shopRecommendInfo);

    int deleteShopRecommendInfoByShopId(String shopId);

    List<ShopRecommendGoods> queryShopRecommendGoodsList();

    List<ShopRecommendGoods> queryShopRecommendGoodsByShopId(String shopId);

    int insertShopRecommendGoods(ShopRecommendGoods shopRecommendGoods);

    int updateShopRecommendGoods(ShopRecommendGoods shopRecommendGoods);

    int deleteShopRecommendGoods(ShopRecommendGoods shopRecommendGoods);
}
