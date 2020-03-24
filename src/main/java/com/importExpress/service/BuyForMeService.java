package com.importExpress.service;

import java.util.List;
import java.util.Map;

import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;

public interface BuyForMeService {
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
	List<BFOrderDetail> getOrderDetails(String orderNo,String bfId);
	
	Map<String,Object> getOrder(String orderNo);
	
	
	/**
	 * @return
	 */
	List<BFOrderDetailSku> getOrderDetailsSku(String bfId);
	

	/**
	 * @param detailSku
	 * @return
	 */
	int addOrderDetailsSku(BFOrderDetailSku detailSku);
	/**更新sku
	 * @param id
	 * @return
	 */
	int updateOrderDetailsSkuState(int id,int state);
	/**
	 * @param detailSku
	 * @return
	 */
	int finshOrder(int bfId);
	
	int updateOrderDetailsSkuWeight(String weight,int bfdid);
	
}
