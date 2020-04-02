package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cbt.pojo.Admuser;
import com.importExpress.pojo.BFOrderDetail;
import com.importExpress.pojo.BFOrderDetailSku;
import com.importExpress.pojo.BFOrderInfo;
import com.importExpress.pojo.ZoneBean;

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
	/**回复备注
	 * @param id
	 * @param remark
	 * @return
	 */
	int updateOrdersDetailsRemark(@Param("id")int id,@Param("remark")String remark);
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
	
	List<Admuser> lstAdms();
	
	int updateOrderDetailsSkuWeight(@Param("weight")String weight,@Param("bfdid")int bfdid);
	
	int updateDeliveryTime(@Param("orderNo")String orderNo,@Param("time")String time,@Param("feight")String feight,@Param("method")String method);
	int insertRemark(@Param("orderNo")String orderNo,@Param("remark")String remark);
	List<Map<String,String>> getRemark(String orderNo);
	
	List<Map<String,String>> getTransport();
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
	
	/**国家列表
	 * @return
	 */
	List<ZoneBean> lstCountry();
}
