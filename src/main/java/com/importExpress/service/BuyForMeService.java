package com.importExpress.service;

import com.cbt.pojo.Admuser;
import com.importExpress.pojo.*;

import java.util.List;
import java.util.Map;

public interface BuyForMeService {
    /**
     * 更新order状态
     *
     * @param id
     * @return
     */
    int cancelOrders(int id);

    /**
     * 申请单列表
     *
     * @return
     */
    List<BFOrderInfo> getOrders(Map<String, Object> map);

    /**
     * 申请单列表数量
     *
     * @return
     */
    int getOrdersCount(Map<String, Object> map);

    /**
     * 订单明细
     *
     * @return
     */
    List<BFOrderDetail> getOrderDetails(String orderNo, String bfId);

    /**
     * 获取订单
     *
     * @param orderNo
     * @return
     */
    Map<String, Object> getOrder(String orderNo);


    /**
     * 获取sku
     *
     * @return
     */
    List<BFOrderDetailSku> getOrderDetailsSku(String bfId);


    /**
     * 增加sku
     *
     * @param detailSku
     * @return
     */
    int addOrderDetailsSku(BFOrderDetailSku detailSku);

    /**
     * 更新sku状态
     *
     * @param id
     * @return
     */
    int updateOrderDetailsSkuState(int id, int state, int bfId);

    /**
     * @param detailSku
     * @return
     */
    int finshOrder(int bfId);

    /**
     * 更新重量
     *
     * @param weight
     * @param bfdid
     * @return
     */
    int updateOrderDetailsSkuWeight(String weight, int bfdid);

    /**
     * 更新交期
     *
     * @param orderNo
     * @param time
     * @return
     */
    int updateDeliveryTime(String orderNo, String time, String feight, String method);

    /**
     * 对内备注
     *
     * @param orderNo
     * @param remark
     * @return
     */
    int insertRemark(String orderNo, String remark);

    /**
     * 获取对内备注
     *
     * @param orderNo
     * @return
     */
    List<Map<String, String>> getRemark(String orderNo);

    List<TransportMethod> getTransport();

    /**
     * 回复备注
     *
     * @param id
     * @param remark
     * @return
     */
    int updateOrdersDetailsRemark(int id, String remark);

    /**
     * 修改地址
     *
     * @param id
     * @param remark
     * @return
     */
    int updateOrdersAddress(Map<String, String> map);

    /**
     * 删除商品
     *
     * @param id
     * @param remark
     * @return
     */
    int deleteProduct(int bfdid);

    List<Admuser> lstAdms();

    /**
     * 国家列表
     *
     * @return
     */
    List<ZoneBean> lstCountry();

    /**
     * 商品聊天
     *
     * @param bfChat
     * @return
     */
    int insertBFChat(BFChat bfChat);

    List<BFChat> queryBFChatList(BFChat bfChat);

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
}
