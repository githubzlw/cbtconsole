package com.importExpress.mapper;

import com.importExpress.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderSplitMapper {
	
	/**
	 * 获取订单详情和是否已有采购链接
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<Map<String, Object>> getOrdersDetails_split(String orderNo);
	
	/**
	 * 获取订单详情
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<Map<String, Object>> getOrdersDetails(String orderNo);
	
	/**
	 * 获取订单对象
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<OrderBean> getOrders(@Param("ordernos") String[] ordernos) ;

	/**
	 * 修改订单金额
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int upOrder(OrderBean ob);

	/**
	 * 复制订单地址到拆分订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int cpOrder_address(OrderAddress orderAddress);

	/**
	 * 拆分生成新订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int addOrderInfo(OrderBean orderBean);

	/**
	 * 拆分生成新订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int upOrderDetails(@Param("orderid") String orderid, @Param("gids") String gids);

	/**
	 * payment表插入
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public void addPayment(Payment pay);

	/**
	 * 添加拆分日志，记录原始订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public void addOrderInfoLog(@Param("orderno") String orderno, @Param("orderinfo") String orderinfo);

	/**
	 * 添加发送邮件的错误信息
	 * ylm
	 * @param
	 */
	public int addMessage_error(MessageError messageError);

	/**
	 * 获取发送邮件的错误信息
	 * ylm
	 * @param
	 */
	public List<MessageError> getMessage_errorByPage(@Param("time") String time, @Param("endtime") String endtime, @Param("page") int page, @Param("endpage") int endpage);

	/**
	 * 获取发送邮件的错误数量
	 * ylm
	 * @param
	 */
	public int getMessage_error(@Param("time") String time, @Param("endtime") String endtime);
	
	/**
	 * 修改order_change表对应的订单为拆分后的订单
	 * ylm
	 * @param 
	 */
	public int upOrder_change(String orderid);
	
	/**
	 * 复制确认人员到拆分订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int cpOrder_Paymentconfirm(PaymentConfirm paymentConfirm);
	
	/**
	 * 复制采购人员到拆分订单
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int cpOrder_Buy(OrderBuy orderBuy);
	
	
	public int upOrder_remarkByProduct(String orderid);
	
	public int upOrder_remarkByGooddata(String orderid);
	
}
