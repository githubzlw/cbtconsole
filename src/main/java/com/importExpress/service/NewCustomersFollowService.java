package com.importExpress.service;

import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.ShopCarUserStatistic;

import java.util.List;

public interface NewCustomersFollowService {
    EasyUiJsonResult FindCustomList(ShopCarUserStatistic statistic, Admuser admuser);

    List<ShopCarUserStatistic> queryNewCustomByUserId(int userId,int page);

    int queryNewCustomByUserIdCount(int userId, int page);

    List<ConfirmUserInfo> queryAllSale();

    JsonResult queryCustomByUserId(String userid);

}
