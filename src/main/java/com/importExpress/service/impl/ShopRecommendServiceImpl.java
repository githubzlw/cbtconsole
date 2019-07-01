package com.importExpress.service.impl;

import com.importExpress.mapper.ShopRecommendMapper;
import com.importExpress.pojo.ShopRecommendGoods;
import com.importExpress.pojo.ShopRecommendInfo;
import com.importExpress.service.ShopRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopRecommendServiceImpl implements ShopRecommendService {

    @Autowired
    private ShopRecommendMapper shopRecommendMapper;

    @Override
    public List<ShopRecommendInfo> queryShopRecommendInfoList() {
        return shopRecommendMapper.queryShopRecommendInfoList();
    }

    @Override
    public List<ShopRecommendInfo> queryRecommendInfoByShopId(String shopId) {
        return shopRecommendMapper.queryRecommendInfoByShopId(shopId);
    }

    @Override
    public int checkRecommendInfoByShopId(String shopId) {
        return shopRecommendMapper.checkRecommendInfoByShopId(shopId);
    }

    @Override
    public int insertShopRecommendInfo(ShopRecommendInfo shopRecommendInfo) {
        return shopRecommendMapper.insertShopRecommendInfo(shopRecommendInfo);
    }

    @Override
    public int updateShopRecommendInfo(ShopRecommendInfo shopRecommendInfo) {
        return shopRecommendMapper.updateShopRecommendInfo(shopRecommendInfo);
    }

    @Override
    public int deleteShopRecommendInfoByShopId(String shopId) {
        return shopRecommendMapper.deleteShopRecommendInfoByShopId(shopId);
    }

    @Override
    public List<ShopRecommendGoods> queryShopRecommendGoodsList() {
        return shopRecommendMapper.queryShopRecommendGoodsList();
    }

    @Override
    public List<ShopRecommendGoods> queryShopRecommendGoodsByShopId(String shopId) {
        return shopRecommendMapper.queryShopRecommendGoodsByShopId(shopId);
    }

    @Override
    public int insertShopRecommendGoods(ShopRecommendGoods shopRecommendGoods) {
        return shopRecommendMapper.insertShopRecommendGoods(shopRecommendGoods);
    }

    @Override
    public int updateShopRecommendGoods(ShopRecommendGoods shopRecommendGoods) {
        return shopRecommendMapper.updateShopRecommendGoods(shopRecommendGoods);
    }

    @Override
    public int deleteShopRecommendGoods(ShopRecommendGoods shopRecommendGoods) {
        return shopRecommendMapper.deleteShopRecommendGoods(shopRecommendGoods);
    }
}
