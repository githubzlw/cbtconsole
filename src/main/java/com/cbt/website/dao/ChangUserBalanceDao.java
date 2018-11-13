package com.cbt.website.dao;

import com.cbt.website.util.JsonResult;

/**
 * 修改客户余额DAO
 * 
 * @author JXW
 *
 */
public interface ChangUserBalanceDao {

	/**
	 * 修改客户余额
	 * 
	 * @param userId
	 *            ： 客户id(必填)
	 * @param amount
	 *            ： 金额(必填)
	 * @param operationType
	 *            ： 操作类型(必填)，1增加余额，-1减去余额
	 * @param operationSource
	 *            ： 操作来源(必填)，1系统拆单取消 2系统整单取消 3客户取消 4客户余额提现 5余额补偿 6客户多支付
	 * @param orderNo
	 *            ： 订单号(可为空)
	 * @param remark
	 *            ：备注(可为空)
	 * @param adminId
	 *            ： 操作人(必填)
	 * @return
	 */
	public JsonResult changeBalance(int userId, float amount, int operationType, int operationSource, String orderNo,
                                    String remark, int adminId);

}
