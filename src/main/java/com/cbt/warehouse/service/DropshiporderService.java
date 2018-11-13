package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.website.bean.DataGridResult;

import java.util.List;
import java.util.Map;


/**   
 * @Title : DropshiporderService.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年10月26日
 * @version : V1.0   
 */
public interface DropshiporderService {
	
	int addDropshiporder(Dropshiporder dropshiporder);
	
	/**
	 * 支付成功后调用
	 * @param orderno
	 * @return
	 */
	int updateDropshiporder(String orderno);
	
	
	//List<Dropshiporder> getDropShipOrderList(String ParentOrderNo,int userId);
	
	/**
	 * 获取子订单列表
	 * @param ParentOrderNo
	 * @param userId
	 * @param page
	 * @return
	 */
	DataGridResult getDropShipOrderList(String ParentOrderNo, Integer userId, Integer page);

	/**
	 * 获取正在确认价格的订单
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getDropShipOrderInfo(String orderNo, int userId);

	/**
	 * 获取用户总付款金额
	 * @param orderNo
	 * 		用户ID,订单状态
	 */
	public Object[] getOrdersPay(String orderNo);

	/**
	 * 根据子订单号查找drop ship订单信息
	 * @param childOrderNo
	 * @param userId
	 * @return
	 */
	public Dropshiporder getDropShipOrder(String childOrderNo, int userId);


	public int updateDropShipOrder(Dropshiporder dropshiporder);


	public List<Dropshiporder> getDropshiporderList(String parentOrderNo, int userId, String state);
	
}
