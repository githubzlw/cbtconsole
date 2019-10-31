package com.importExpress.service;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.website.util.JsonResult;

import java.util.List;
import java.util.Map;

public interface ReorderService {
    /**
     * @Title: getGoodsInfoByOrderNo
     * @Author: cjc
     * @Despricetion:TODO 获取订单购物车表数据
     * @Date: 2018/11/10 16:21:27
     * @Param: [orderNo, goodsId]
     * @Return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getGoodsInfoByOrderNo(String orderNo, String goodsId);
    List<Map<String, Object>> getGoodsInfoByUserId(String goodsId);

    String reorder(String orderNo, String userId);
    String returnGoodsCarByUserId(String userId);

    /**
     * 新版reorder
     * @param orderNo
     * @param userId
     * @param odbs
     * @return
     */
    JsonResult reOrderNew(String orderNo, String userId,List<OrderDetailsBean> odbs);
}
