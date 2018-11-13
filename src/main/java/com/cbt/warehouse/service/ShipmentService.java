package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.Shipment;
import com.cbt.website.bean.DataGridResult;

import java.util.List;
import java.util.Map;

/**   
 * @Title: ShipmentService.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年7月5日
 * @version V1.0   
 */
public interface ShipmentService {
	
	
	int insertShipment(Shipment shipment);

	List<Shipment> insertShipment(List<Shipment> list);
	
	/**
	 * 校验运单号
	 * @param list
	 * @return
	 */
	List<Shipment> validateShipmentList(List<Shipment> list, String uuid);

	/**
	 * 根据id列表获取运单信息列表
	 * @param ids
	 * @return
	 */
	List<Shipment> selectShipmentList(List<Integer> list);


	/**
	 * 加载运单信息
	 * @param company
	 * @param date
	 * @param choiseType
	 * @return
	 */
	DataGridResult loadShipment(Map<String, String> map);

	/**
	 * 查询待导出运单信息
	 * @param company
	 * @param senttimeBegin
	 * @param senttimeEnd
	 * @param choiseType
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<Shipment> loadExportShipment(String company, String senttimeBegin, String senttimeEnd, String choiseType);


	/**
	 * 查询运单总条数
	 * @param company
	 * @param date
	 * @param choiseType
	 * @return
	 */
	Map<?, ?> selectCountShipment(String company, String senttimeBegin, String senttimeEnd, String choiseType, double exchageRate);

	/**
	 * 查询不在系统中运单总金额
	 * @param company
	 * @param date
	 * @param choiseType
	 * @return
	 */
	String getNoTolPrice(String company, String senttimeBegin, String senttimeEnd, String choiseType);



	int deleteShipment(List<Integer> idList);

	/**
	 * 录入某个月的赔偿款
	 * @param amounts
	 * @param datas
	 * @return
	 */
	int insertSources(String amounts, String datas, String comps);

	/**
	 * 人工判断运输公司给的运费可以支付
	 * @param sm_id
	 * @return
	 */
	int updateShipMentFlag(List<Integer> idList, String flag_remark, String totalprice);
	/**
	 * 采购出库时间统计
	 * @Title PurchaseStatisticsInquiry
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<String[]>
	 */
	List<String[]> PurchaseStatisticsInquiry(Map<Object, Object> map);


	/**
	 * 过去30天完成出货的订单的 出货时间
	 * @return
	 */
	List<String[]> shipmentTimeCount(String paytimestart, String paytimeend, int days);
	/***
	 * 获取最近4个月订单中商品数量
	 * @Title getDetailsCount
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getDetailsCount();
	/**
	 * 获取最近4个月订单数量
	 * @Title getOrderCount
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getOrderCount();
	/**
	 * 获取最近4个月订单平均采购时间
	 * @Title getOrderBuyTime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getOrderBuyTime();
	/**
	 * 获取最近4个月商品平均采购时间
	 * @Title getGoodsbuytime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getGoodsbuytime();
	/**
	 * 获取最近4个月订单平均出货时间
	 * @Title getChTime
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int getChTime();

	/**
	 * 过去三个月运费统计
	 * @param time
	 * @return
	 */
	List<Shipment> showCalculFreight(String time);

	int check(String start, String end);
	
}
