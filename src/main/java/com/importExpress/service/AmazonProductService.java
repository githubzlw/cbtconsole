package com.importExpress.service;

import com.importExpress.pojo.AmazonOrderBean;
import com.importExpress.pojo.AmazonOrderParam;
import com.importExpress.pojo.AmazonProductBean;

import java.util.List;

public interface AmazonProductService {

    /**
     * 分页查询速卖通对标信息
     *
     * @param amazonProduct
     * @return
     */
    List<AmazonProductBean> queryForList(AmazonProductBean amazonProduct);

    /**
     * 速卖通对标信息总数
     *
     * @param amazonProduct
     * @return
     */
    int queryForListCount(AmazonProductBean amazonProduct);


    /**
     * 设置匹配PID和标识
     *
     * @param dealState
     * @param amazonPid
     * @param adminId
     * @param matchPid
     * @return
     */
    int setAmazonDealFlag(String amazonPid,int dealState, int adminId, String matchPid);


    /**
     * 检查亚马逊产品是否存在
     *
     * @param amazonPid
     * @return
     */
    int checkAmazonProductIsExists(String amazonPid);


    List<AmazonOrderBean> queryAmazonOrderList(AmazonOrderParam param);

    int queryAmazonOrderListCount(AmazonOrderParam param);

    int insertAmazonOrder(AmazonOrderBean orderBean);

    int insertAmazonOrderList(List<AmazonOrderBean> orderBeanList);
}
