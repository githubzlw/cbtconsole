package com.importExpress.service;

import java.util.List;
import java.util.Map;

import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.TransportMethod;

public interface BuyForMeService {
	/**申请单列表
	 * @return
	 */
	List<BFOrderInfo> getOrders(Map<String,Object> map);
	
	/**申请单列表数量
	 * @return
	 */
	int getOrdersCount(Map<String,Object> map);
	
	/**订单明细
	 * @return
	 */
	List<BFOrderDetail> getOrderDetails(String orderNo,String bfId);
	
	/**获取订单
	 * @param orderNo
	 * @return
	 */
	Map<String,Object> getOrder(String orderNo);
	
	
	/**获取sku
	 * @return
	 */
	List<BFOrderDetailSku> getOrderDetailsSku(String bfId);
	

	/**增加sku
	 * @param detailSku
	 * @return
	 */
	int addOrderDetailsSku(BFOrderDetailSku detailSku);
	/**更新sku状态
	 * @param id
	 * @return
	 */
	int updateOrderDetailsSkuState(int id,int state);
	/**
	 * @param detailSku
	 * @return
	 */
	int finshOrder(int bfId);
	
	/**更新重量
	 * @param weight
	 * @param bfdid
	 * @return
	 */
	int updateOrderDetailsSkuWeight(String weight,int bfdid);
	
	/**更新交期
	 * @param orderNo
	 * @param time
	 * @return
	 */
	int updateDeliveryTime(String orderNo,String time);
	/**对内备注
	 * @param orderNo
	 * @param remark
	 * @return
	 */
	int insertRemark(String orderNo,String remark);
	/**获取对内备注
	 * @param orderNo
	 * @return
	 */
	List<Map<String,String>> getRemark(String orderNo);
	
	List<TransportMethod>  getTransport();
	
}
