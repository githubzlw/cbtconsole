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
    public ShopRecommendInfo queryRecommendInfoByShopId(String shopId) {
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
    public int updateShopMainImg(ShopRecommendInfo shopRecommendInfo) {
        return shopRecommendMapper.updateShopMainImg(shopRecommendInfo);
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
    public int insertShopRecommendGoods(List<ShopRecommendGoods> shopRecommendGoodsList) {
        return shopRecommendMapper.insertShopRecommendGoods(shopRecommendGoodsList);
    }

    @Override
    public int updateShopRecommendGoods(List<ShopRecommendGoods> shopRecommendGoodsList) {
        return shopRecommendMapper.updateShopRecommendGoods(shopRecommendGoodsList);
    }

    @Override
    public int deleteShopRecommendGoods(String shopId) {
        return shopRecommendMapper.deleteShopRecommendGoods(shopId);
    }
}
