package com.importExpress.service;

import java.util.List;
import java.util.Map;

import com.cbt.pojo.Admuser;
import com.importExpress.pojo.BFChat;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.TransportMethod;
import com.importExpress.pojo.ZoneBean;
import com.importExpress.pojo.BuyForMeSearchLog;

public interface BuyForMeService {
	/**更新order状态
	 * @param id
	 * @return
	 */
	int cancelOrders(int id);
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

	List<BuyForMeSearchLog> querySearchList(BuyForMeSearchLog searchLog);

	int querySearchListCount(BuyForMeSearchLog searchLog);

	/**更新交期
	 * @param orderNo
	 * @param time
	 * @return
	 */
	int updateDeliveryTime(String orderNo,String time,String feight,String method);
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
	/**回复备注
	 * @param id
	 * @param remark
	 * @return
	 */
	int updateOrdersDetailsRemark(int id,String remark);
	/**修改地址
	 * @param id
	 * @param remark
	 * @return
	 */
	int updateOrdersAddress(Map<String,String> map);
	/**删除商品
	 * @param id
	 * @param remark
	 * @return
	 */
	int deleteProduct(int bfdid);

	List<Admuser> lstAdms();
	/**国家列表
	 * @return
	 */
	List<ZoneBean> lstCountry();

	/**
	 * 商品聊天
	 * @param bfChat
	 * @return
	 */
	int insertBFChat(BFChat bfChat);

	List<BFChat> queryBFChatList(BFChat bfChat);
}
