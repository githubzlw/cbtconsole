package com.cbt.website.dao2;

import com.cbt.website.util.IOrderOnlineSampleBasic;
import fpx.api.client.OrderOnlineDelegate;
import fpx.api.orderonline.entity.Error;
import fpx.api.orderonline.entity.RemoveOrderResponse;

import java.rmi.RemoteException;

/**
 * @author Terence chen 2012-7-3 上午9:39:14
 * @version 1.0
 */
public class RemoveOrderSample {
	/**
	 * 删除订单API
	 */
	public void removeOrderService(String ordernumber) {
		/**
		 * 单号
		 */
		String[] astrOrderNo = new String[1];
		astrOrderNo[0] = ordernumber;
//		astrOrderNo[1] = "T20121017002";
		/**
		 * 创建4PX 提供的SDK调用方式代理类 - 在线订单操作代理类
		 */
		OrderOnlineDelegate objDelegate = new OrderOnlineDelegate();
		/**
		 * 远程调用删除订单API
		 */
		RemoveOrderResponse[] aobjResponse = null;
		try {
			aobjResponse = objDelegate.removeOrderService(
					IOrderOnlineSampleBasic.PRODUCT_AUTHTOKEN, astrOrderNo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		/**
		 * 打印返回结果
		 */
		printResponse(aobjResponse);
	}

	/**
	 * 打印返回结果
	 */
	public void printResponse(RemoveOrderResponse[] aobjResponse) {
		if (aobjResponse != null && aobjResponse.length > 0) {
			for (int i = 0, iLength = aobjResponse.length; i < iLength; i++) {
				RemoveOrderResponse objResponse = aobjResponse[i];
				// 服务器响应时间
				System.out.println("Timestamp = " + objResponse.getTimestamp());
				// 操作 成功: Success 失败: Failure
				System.out.println("Ack = " + objResponse.getAck());
				// 引用单号，一般为客户单号
				System.out.println("ReferenceNumber = "
						+ objResponse.getReferenceNumber());
				// 错误信息
				Error[] aobjError = objResponse.getErrors();
				if (aobjError != null && aobjError.length > 0) {
					for (int j = 0, iErrorLength = aobjError.length; j < iErrorLength; j++) {
						Error objError = aobjError[j];
						// 错识代码
						System.out.println("Code = " + objError.getCode());
						// 错误详细内容中文描述
						System.out.println("CnMessage = "
								+ objError.getCnMessage());
						// 错误的处理方法中文描述
						System.out.println("CnAction = "
								+ objError.getCnAction());
						// 错误详细内容英文描述
						System.out.println("EnMessage = "
								+ objError.getEnMessage());
						// 错误的处理方法英文描述
						System.out.println("EnAction = "
								+ objError.getEnAction());
						// 错误信息补充说明
						System.out.println("DefineMessage = "
								+ objError.getDefineMessage());
						System.out
								.println("**************************我是华丽的分割线***********************");
					}
				}
				System.out
						.println("------------------------------------我是华丽的分割线-----------------------------");
			}
		}
	}

}
