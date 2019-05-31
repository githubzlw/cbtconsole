package com.importExpress.service.impl;

import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.controller.NewCustomersFollowController;
import com.importExpress.mapper.NewCustomersFollowMapper;
import com.importExpress.pojo.ShopCarUserStatistic;
import com.importExpress.pojo.UserOtherInfoBean;
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
    public EasyUiJsonResult FindCustomList(ShopCarUserStatistic statistic, Admuser admuser) {
        EasyUiJsonResult json=new EasyUiJsonResult();
        try {
            List<ShopCarUserStatistic> list=this.newCustomersFollowMapper.FindCustomList(statistic);
            for (ShopCarUserStatistic shopCarUserStatistic:list){
                shopCarUserStatistic.setAdmname(admuser.getAdmName());
                shopCarUserStatistic.setPassword(admuser.getPassword());
            }
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

    @Override
    public JsonResult queryCustomByUserId(String userid) {
        JsonResult jsonResult=new JsonResult();
        try {
            UserOtherInfoBean userOtherInfoBean= this.newCustomersFollowMapper.queryCustomByUserId(userid);
            if (userOtherInfoBean!=null && userOtherInfoBean.getUserType()!=null ){
                String body="";
                char arr[]=userOtherInfoBean.getUserType().toCharArray();
                for (int i=0;i<arr.length;i++){
                    if ('1'==arr[i]) {
                        body+="Combine Shipping (ocean freight, cheapest shipping rate);  ";
                    } else if ('2'==arr[i]) {
                        body+="Combine Shipping (air freight, better price than shipping individually);  ";
                    } else if ('3'==arr[i]) {
                        body+="Quality Control;  ";
                    } else if ('4'==arr[i]) {
                        body+="Custom Packaging  ";
                    }

                }
                userOtherInfoBean.setUserTypeDesc(body);

            }
            jsonResult.setData(userOtherInfoBean);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setData(1);
        }

        return jsonResult;
    }
}
