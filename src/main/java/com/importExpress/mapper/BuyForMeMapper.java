package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;

public interface BuyForMeMapper {
	/**更新order状态
	 * @param id
	 * @return
	 */
	int updateOrdersState(@Param("id")int id,@Param("state")int state);
	/**更新details状态
	 * @param id
	 * @return
	 */
	int updateOrdersDetailsState(@Param("id")int id,@Param("state")int state);
	/**更新details sku状态
	 * @param id
	 * @return
	 */
	int updateOrderDetailsSkuState(@Param("id")int id,@Param("state")int state);
	
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
	List<BFOrderDetail> getOrderDetails(String orderNo);
	
	
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
	
	Map<String,Object> getOrder(String orderNo);
	
	int updateOrderDetailsSkuWeight(@Param("weight")String weight,@Param("bfdid")int bfdid);
	
	int updateDeliveryTime(@Param("orderNo")String orderNo,@Param("time")String time);
    
}
