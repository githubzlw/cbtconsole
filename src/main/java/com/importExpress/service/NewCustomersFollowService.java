package com.importExpress.service;

import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.ShopCarUserStatistic;

import java.util.List;

public interface NewCustomersFollowService {
    EasyUiJsonResult FindCustomList(ShopCarUserStatistic statistic);

    List<ShopCarUserStatistic> queryNewCustomByUserId(int userId,int page);

    int queryNewCustomByUserIdCount(int userId, int page);

}
