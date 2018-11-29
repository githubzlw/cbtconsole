package com.importExpress.service.impl;

import com.importExpress.mapper.ShopCarMarketingMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.ShopCarMarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shopCarMarketingService")
public class ShopCarMarketingServiceImpl implements ShopCarMarketingService {
    @Autowired
    private ShopCarMarketingMapper shopCarMarketingMapper;

    @Override
    public int countByExample(ShopCarMarketingExample example) {
        return shopCarMarketingMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ShopCarMarketingExample example) {
        return shopCarMarketingMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return shopCarMarketingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ShopCarMarketing record) {
        return shopCarMarketingMapper.insert(record);
    }

    @Override
    public int insertSelective(ShopCarMarketing record) {
        return shopCarMarketingMapper.insertSelective(record);
    }

    @Override
    public List<ShopCarMarketing> selectByExample(ShopCarMarketingExample example) {
        return shopCarMarketingMapper.selectByExample(example);
    }

    @Override
    public ShopCarMarketing selectByPrimaryKey(Integer id) {
        return shopCarMarketingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ShopCarMarketing record, ShopCarMarketingExample example) {
        return shopCarMarketingMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(ShopCarMarketing record, ShopCarMarketingExample example) {
        return shopCarMarketingMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(ShopCarMarketing record) {
        return shopCarMarketingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ShopCarMarketing record) {
        return shopCarMarketingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateGoodsCarPrice(int goodsId, int userId, double goodsPrice,double newPrice) {
        return shopCarMarketingMapper.updateGoodsCarPrice(goodsId, userId, goodsPrice,newPrice);
    }

    @Override
    public int deleteMarketingByUserId(int userId) {
        return shopCarMarketingMapper.deleteMarketingByUserId(userId);
    }

    @Override
    public int insertShopCarBatch(List<ShopCarMarketing> recordList) {
        return shopCarMarketingMapper.insertShopCarBatch(recordList);
    }

    @Override
    public int updateShopCarBatch(List<ShopCarMarketing> recordList) {
        return shopCarMarketingMapper.updateShopCarBatch(recordList);
    }

    @Override
    public int deleteShopCarBatch(List<ShopCarMarketing> recordList) {
        return shopCarMarketingMapper.deleteShopCarBatch(recordList);
    }

    @Override
    public List<ShopCarInfo> queryShopCarInfoByUserId(int userId) {
        List<ShopCarInfo> resultList = shopCarMarketingMapper.queryShopCarInfoByUserId(userId);
        if(!(resultList == null  || resultList.isEmpty())){
            for(ShopCarInfo carInfo : resultList){
                if(!(carInfo.getIsBenchmark() == 1 && carInfo.getBmFlag() == 1)){
                    carInfo.setAliPid(null);
                }
            }
        }
        return resultList;
    }

    @Override
    public ShopCarUserStatistic queryUserInfo(int userId) {
        return shopCarMarketingMapper.queryUserInfo(userId);
    }

    @Override
    public int updateAndInsertUserFollowInfo(int userId, int adminId, String content) {
        shopCarMarketingMapper.insertIntoShopCarFollow(userId, adminId, content);
        return shopCarMarketingMapper.updateUserFollowTime(userId);
    }

    @Override
    public List<ShopCarUserStatistic> queryForList(ShopCarUserStatistic statistic) {
        return shopCarMarketingMapper.queryForList(statistic);
    }

    @Override
    public int queryForListCount(ShopCarUserStatistic statistic) {
        return shopCarMarketingMapper.queryForListCount(statistic);
    }

    @Override
    public List<ShopTrackingBean> queryTrackingList(ShopTrackingBean param) {
        return shopCarMarketingMapper.queryTrackingList(param);
    }

    @Override
    public int queryTrackingListCount(ShopTrackingBean param) {
        return shopCarMarketingMapper.queryTrackingListCount(param);
    }

    @Override
    public List<ZoneBean> queryAllCountry() {
        return shopCarMarketingMapper.queryAllCountry();
    }

}
