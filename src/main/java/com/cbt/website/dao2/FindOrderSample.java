package com.cbt.website.dao2;

import com.cbt.website.util.IOrderOnlineSampleBasic;
import fpx.api.client.OrderOnlineDelegate;
import fpx.api.orderonline.entity.Error;
import fpx.api.orderonline.entity.FindOrderRequest;
import fpx.api.orderonline.entity.FindOrderResponse;
import fpx.api.orderonline.entity.OrderItem;

import java.rmi.RemoteException;

/**
 * @author Terence chen 2012-7-3 上午9:37:33
 * @version 1.0
 */
public class FindOrderSample {
	/**
	 * 测试入口
	 * 
	 * @param args
	public static void main(String[] args) {
		FindOrderSample sample = new FindOrderSample();
		sample.findOrderService("2126871715122400005<br/>");
	}
	 */

	/**
	 * 查询订单API
	 */
	public int findOrderService(String strOrder) {
		/**
		 * 封装请求参数
		 */
		FindOrderRequest objRequest = getFindOrderRequest(strOrder);
		/**
		 * 创建4PX 提供的SDK调用方式代理类 - 在线订单操作代理类
		 */
		OrderOnlineDelegate objDelegate = new OrderOnlineDelegate();
		/**
		 * 远程调用查询订单API
		 */
		FindOrderResponse objResponse = null;
		try {
			objResponse = objDelegate.findOrderService(IOrderOnlineSampleBasic.PRODUCT_AUTHTOKEN, objRequest);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		/**
		 * 打印返回结果
		 printResponse(objResponse);
		 */
		int tt = 1;
		if((objResponse.getAck()).equals("Success")){
			OrderItem[] dd = objResponse.getOrderItem();
			if (dd == null) {
				tt = 1;
			} else {
				for (int t = 0; t < objResponse.getOrderItem().length; t++) {
					System.out.println(objResponse.getOrderItem(t).getTrackingNumber());
					tt++;
				}
//				System.out.println(tt);
			}
		} else {
			tt = 0;//查询失败
		}
		return tt;
	}

	/**
	 * 封装请求参数
	 * 
	 * @return
	 */
	public FindOrderRequest getFindOrderRequest(String strOrderID) {
		FindOrderRequest objRequest = new FindOrderRequest();
		String[] astrOrderNo = strOrderID.split("<br/>");
		objRequest.setOrderNo(astrOrderNo);
		return objRequest;
	}

	/**
	 * 打印返回结果
	 */
	public void printResponse(FindOrderResponse objResponse) {
		if (objResponse != null) {
			// 服务器响应时间
			System.out.println("Timestamp = " + objResponse.getTimestamp());
			// 操作 成功: Success 失败: Failure
			System.out.println("Ack = " + objResponse.getAck());
			// 错误信息
			Error[] aobjError = objResponse.getErrors();
			if (aobjError != null && aobjError.length > 0) {
				for (int j = 0, iErrorLength = aobjError.length; j < iErrorLength; j++) {
					Error objError = aobjError[j];
					// 错识代码
					System.out.println("Code = " + objError.getCode());
					// 错误详细内容中文描述
					System.out.println("CnMessage = " + objError.getCnMessage());
					// 错误的处理方法中文描述
					System.out.println("CnAction = " + objError.getCnAction());
					// 错误详细内容英文描述
					System.out.println("EnMessage = " + objError.getEnMessage());
					// 错误的处理方法英文描述
					System.out.println("EnAction = " + objError.getEnAction());
					// 错误信息补充说明
					System.out.println("DefineMessage = " + objError.getDefineMessage());
					System.out.println("*********************我是华丽的分割线****************************");
				}
			}
			OrderItem[] aobjOrderItem = objResponse.getOrderItem();
			if (aobjOrderItem != null && aobjOrderItem.length > 0) {
				for (int i = 0, iLength = aobjOrderItem.length; i < iLength; i++) {
					OrderItem objOrderItem = aobjOrderItem[i];
					System.out.println("OrderNo = " + objOrderItem.getOrderNo());
					System.out.println("TrackingNumber = " + objOrderItem.getTrackingNumber());
					System.out.println("ProductCode = " + objOrderItem.getProductCode());
					System.out.println("DestinationCountryCode = " + objOrderItem.getDestinationCountryCode());
					System.out.println("-------------------------------我是华丽的分割线----------------------------------");
				}
			}
		}
	}

}
