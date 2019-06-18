package com.importExpress.mapper;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.OrderSplitChild;
import com.importExpress.pojo.OrderSplitMain;

public interface OrderSplitRecordMapper {
   
    /**插入主单数据
     * @param Order
     * @return
     */
    int insertMainOrder(OrderSplitMain order);
    
    /**查询订单
     * @param orderId
     * @return
     */
    int countMainOrder(@Param("orderid")String orderid);
    /**查询订单
     * @param orderId
     * @return
     */
    OrderSplitMain getMainOrder(@Param("orderid")String orderid);
    /**查询订单
     * @param orderId
     * @return
     */
    OrderSplitChild getChildOrder(@Param("orderid")String orderid);
    
    /**插入子单
     * @return
     */
    int insertChildOrder(OrderSplitChild order);
    /**更新子单的子单
     * @param orderChild1
     * @param orderidChild2
     * @return
     */
    int updateChildOrder(@Param("orderChild1")String orderChild1,@Param("orderChild2")String orderidChild2);
    
    
    /**
     * @param orderid
     * @return
     */
    OrderSplitChild getOrder(String orderid);
    
}