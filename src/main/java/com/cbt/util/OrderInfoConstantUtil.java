package com.cbt.util;

/**
 * 订单状态常量获取
 * 
 * @author JiangXianwei
 *
 */
public class OrderInfoConstantUtil {

	/**
	 * 后台取消
	 */
	public static final String OFFLINECANCEL = "-1";// 后台取消
	/**
	 * 客户取消
	 */
	public static final String CUSTOMERCANCEL = "6";// 客户取消
	/**
	 * 完结
	 */
	public static final String FINISH = "4";// 完结
	/**
	 * 出运
	 */
	public static final String SHIPMENT = "3";// 出运
	/**
	 * 到库
	 */
	public static final String WAREHOUSE = "2";// 到库
	/**
	 * 采购中
	 */
	public static final String PROCURMENT = "1";// 采购中
	/**
	 * 审核中
	 */
	public static final String REVIEW = "5";// 审核中

}
