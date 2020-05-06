package com.importExpress.service;

import com.cbt.pojo.Admuser;
import com.importExpress.pojo.*;

import com.cbt.pojo.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.importExpress.pojo.*;

import java.util.List;
import java.util.Map;

import com.cbt.pojo.Admuser;
import com.importExpress.pojo.*;

public interface BuyForMeService {
    List<BFOrderInfo> getOrders(Map<String, Object> map);

    int getOrdersCount(Map<String, Object> map);

    List<BFOrderDetail> getOrderDetails(String orderNo, String bfId);

    List<BFOrderDetailSku> getOrderDetailsSku(String bfId);

    int addOrderDetailsSku(BFOrderDetailSku detailSku);

    int updateOrderDetailsSkuState(int id, int state);

    int finshOrder(int id);

    Map<String, Object> getOrder(String orderNo);

    int updateOrderDetailsSkuWeight(String weight, int bfdid);

    int updateDeliveryTime(String orderNo, String time, String feight, String method);

    int insertRemark(String orderNo, String remark);

    List<Map<String,String>> getRemark(String orderNo);

    List<TransportMethod> getTransport();

    int updateOrdersDetailsRemark(int id, String remark);

    int updateOrdersAddress(Map<String, String> map);

    List<Admuser> lstAdms();

    int deleteProduct(int bfdid);

    List<ZoneBean> lstCountry();

    int insertBFChat(BFChat bfChat);

    List<BFChat> queryBFChatList(BFChat bfChat);

    int cancelOrders(int id);

    EasyUiJsonResult queryCustomers(ShopCarUserStatistic statistic);

    JsonResult getCustomerCartDetails(String userId);

    CommonResult putMsg(String userId, String itemid, String msg);

	List<BuyForMeSearchLog> querySearchList(BuyForMeSearchLog searchLog);
    List<BuyForMeSearchLog> querySearchList(BuyForMeSearchLog searchLog);

    int querySearchListCount(BuyForMeSearchLog searchLog);

    List<BFSearchStatic> queryStaticList(BFSearchStatic searchStatic);

    int queryStaticListCount(BFSearchStatic searchStatic);

    int insertIntoSearchStatic(BFSearchStatic searchStatic);

    int updateSearchStatic(BFSearchStatic searchStatic);

    int deleteSearchStatic(BFSearchStatic searchStatic);

    List<BFSearchPid> queryPidByStaticId(int staticId);

    int insertIntoStaticPid(BFSearchPid searchPid);

    int updateStaticPid(BFSearchPid searchPid);

    int deleteStaticPid(BFSearchPid searchPid);

    int setJsonState(int flag, String ids);


    List<BuyForMePidLog> pidLogList(BuyForMePidLog pidLog);

    int pidLogListCount(BuyForMePidLog pidLog);

    /**
     * 更新国家
     * @param searchLogList
     * @return
     */
    int updateSearchLogList(List<BuyForMeSearchLog> searchLogList);
}
