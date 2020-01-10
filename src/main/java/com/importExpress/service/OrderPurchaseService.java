package com.importExpress.service;

import com.importExpress.pojo.OrderPurchase;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.service
 * @date:2020/1/9
 */
public interface OrderPurchaseService {

    List<OrderPurchase> queryForList(OrderPurchase orderPurchase);

    int queryForListCount(OrderPurchase orderPurchase);
}
