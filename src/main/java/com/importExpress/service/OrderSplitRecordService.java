package com.importExpress.service;

import com.importExpress.pojo.OrderSplitChild;
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
    int insertChildOrder(OrderSplitMain orderMain,String orderId);
    
    
    /**
     * @param orderid
     * @return
     */
    OrderSplitChild getOrder(String orderid);
    
    
}
