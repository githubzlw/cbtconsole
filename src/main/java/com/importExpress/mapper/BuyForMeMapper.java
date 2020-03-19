package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;

public interface BuyForMeMapper {
	
	/**申请单列表
	 * @return
	 */
	List<BFOrderInfo> getOrders(Map<String,Object> map);
	
	/**申请单列表数量
	 * @return
	 */
	int getOrdersCount(Map<String,Object> map);
	
	/**
	 * @return
	 */
	List<BFOrderDetail> getOrderDetails(String bfId);
	
	
	/**
	 * @return
	 */
	List<BFOrderDetailSku> getOrderDetailsSku(String bfId);
	

	/**
	 * @param detailSku
	 * @return
	 */
	int addOrderDetailsSku(BFOrderDetailSku detailSku);
	
	/**
	 * @param detailSku
	 * @return
	 */
	int updateOrderDetailsSku(BFOrderDetailSku detailSku);
    
}
