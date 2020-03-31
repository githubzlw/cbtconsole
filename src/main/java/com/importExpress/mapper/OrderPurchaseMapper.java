package com.importExpress.mapper;

import com.importExpress.pojo.OrderPurchase;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.mapper
 * @date:2020/1/9
 */
public interface OrderPurchaseMapper {

    int getCurrentOrder(OrderPurchase orderPurchase);

    List<OrderPurchase> queryForList(OrderPurchase orderPurchase);

    int queryForListCount(OrderPurchase orderPurchase);

    List<OrderPurchase> taobaoList(OrderPurchase orderPurchase);

    int taobaoListCount(OrderPurchase orderPurchase);
}
