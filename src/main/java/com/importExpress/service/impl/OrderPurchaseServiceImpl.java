package com.importExpress.service.impl;

import com.importExpress.mapper.OrderPurchaseMapper;
import com.importExpress.pojo.OrderPurchase;
import com.importExpress.service.OrderPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.service.impl
 * @date:2020/1/9
 */
@Service
public class OrderPurchaseServiceImpl implements OrderPurchaseService {

    @Autowired
    private OrderPurchaseMapper orderPurchaseMapper;

    @Override
    public List<OrderPurchase> queryForList(OrderPurchase orderPurchase) {
        return orderPurchaseMapper.queryForList(orderPurchase);
    }

    @Override
    public int queryForListCount(OrderPurchase orderPurchase) {
        return orderPurchaseMapper.queryForListCount(orderPurchase);
    }
}
