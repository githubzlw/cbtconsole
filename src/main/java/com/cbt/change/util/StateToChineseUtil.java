package com.cbt.change.util;

import com.cbt.util.OrderDetailsConstantUtil;
import com.cbt.util.OrderInfoConstantUtil;

/**
 * 转换订单/订单详情的状态数据到中文
 * 
 * @author Administrator
 *
 */
public class StateToChineseUtil {

	/**
	 * orderInfo的state转换
	 * 
	 * @param state
	 *            状态
	 * @return
	 */
	public static String orderInfoState(String state) {
		// 订单状态：5-确认价格中,1-购买中,2-已到仓库,0-等待付款,3-出运中,4-完结,-1-后台取消订单,6-客户取消订单,7-预订单
		String result = "";
		if (state == null || "".equals(state)) {
			result = "";
		} else {
			if (OrderInfoConstantUtil.OFFLINECANCEL.equals(state)) {
				result = "后台取消订单";
			} else if (OrderInfoConstantUtil.CUSTOMERCANCEL.equals(state)) {
				result = "客户取消订单";
			} else if (OrderInfoConstantUtil.FINISH.equals(state)) {
				result = "完结";
			} else if (OrderInfoConstantUtil.SHIPMENT.equals(state)) {
				result = "出运中";
			} else if (OrderInfoConstantUtil.WAREHOUSE.equals(state)) {
				result = "已到仓库";
			} else if (OrderInfoConstantUtil.PROCURMENT.equals(state)) {
				result = "购买中";
			} else if (OrderInfoConstantUtil.REVIEW.equals(state)) {
				result = "确认价格中";
			}
		}
		return result;
	}

	/**
	 * orderDetails的state转换
	 * 
	 * @param state
	 *            状态
	 * @return
	 */
	public static String orderDetailsState(int state) {
		// 订单详细状态 0-正常 1-已到仓库 2-取消
		String result = "";
		if (state == OrderDetailsConstantUtil.NORMAL) {
			result = "正常";
		} else if (state == OrderDetailsConstantUtil.WAREHOUSE) {
			result = "已到仓库";
		} else if (state == OrderDetailsConstantUtil.CANCEL) {
			result = "取消";
		}
		return result;
	}

}
