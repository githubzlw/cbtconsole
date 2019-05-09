package com.importExpress.service.impl;

import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.controller.NewCustomersFollowController;
import com.importExpress.mapper.NewCustomersFollowMapper;
import com.importExpress.pojo.ShopCarUserStatistic;
import com.importExpress.service.NewCustomersFollowService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NewCustomersFollowServiceImpl implements NewCustomersFollowService {
    private static final Log logger = LogFactory.getLog(NewCustomersFollowController.class);
   @Autowired
    private NewCustomersFollowMapper newCustomersFollowMapper;
    @Override
    public EasyUiJsonResult FindCustomList(ShopCarUserStatistic statistic) {
        EasyUiJsonResult json=new EasyUiJsonResult();
        try {
            List<ShopCarUserStatistic> list=this.newCustomersFollowMapper.FindCustomList(statistic);
            int count =this.newCustomersFollowMapper.FindCustomCount(statistic);
            json.setRows(list);
            json.setTotal(count);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败，原因 :" + e.getMessage());
            json.setSuccess(false);
            json.setMessage("查询失败，原因:" + e.getMessage());
        }
        return json;
    }

    @Override
    public  List<ShopCarUserStatistic>  queryNewCustomByUserId(int userId,int page) {
        try {
            List<ShopCarUserStatistic> list=this.newCustomersFollowMapper.queryNewCustomByUserId(userId,(page-1)*10);
            for (ShopCarUserStatistic shop:list){
                int valid=this.newCustomersFollowMapper.FindValidByPid(shop.getShippingName());
                if (valid==0) {
                    shop.setCurrency("该商品已下架");
                }
                if (valid==2){
                    shop.setCurrency("该商品软下架");
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int queryNewCustomByUserIdCount(int userId, int page) {
        try {
            int total=this.newCustomersFollowMapper.queryNewCustomByUserIdCount(userId);
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public List<ConfirmUserInfo> queryAllSale() {
        try {
            List<ConfirmUserInfo> total=this.newCustomersFollowMapper.queryAllSale();
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
