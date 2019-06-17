package com.importExpress.service;

import com.importExpress.pojo.OrderSplitMain;

public interface OrderSplitRecordService {
	/**插入主单数据
     * @param Order
     * @return
     */
    int insertMainOrder(OrderSplitMain order);
    
    
    /**插入子单
     * @return
     */
    int insertChildOrder(String orderId);
    
    
    /**
     * @param orderid
     * @return
     */
    int getOrder(String orderid);
    
    
}
