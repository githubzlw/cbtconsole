package com.cbt.change.util;

import com.cbt.util.OrderInfoConstantUtil;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;

import org.slf4j.LoggerFactory;

/**
 * 线上订单/订单详情状态更新校验
 * 
 * @author JiangXianwei
 *
 */
public class CheckCanUpdateUtil {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CheckCanUpdateUtil.class);

	/**
	 * 线下更改线上订单状态校检
	 * 
	 * @param orderNo
	 *            订单号
	 * @param operationType
	 *            订单状态：-1 6 订单取消 4 订单完结 3 订单出运 2 到库 1 采购中 5 审核中
	 * @param adminId
	 *            操作人ID
	 * @return
	 */
	public static boolean updateOnlineOrderInfoByLocal(String orderNo, int operationType, int adminId)
			throws Exception {
		
		//2017-5-3 ED说屏蔽校检接口，代码现在不校检，直接返回true 
		return true;
		
		/*boolean isCheck = false;
		// TODO
		// 传递参数判断有效性
		if (orderNo == null || "".equals(orderNo) || operationType == 0 || adminId == 0) {
			LOG.error("获取传递订单号、更改状态或操作人id数据为空,终止继续判断,结果不通过");
			throw new Exception("获取传递订单号、更改状态或操作人id数据为空,终止继续判断,结果不通过");
		} else {
			LOG.info("开始线上orderinfo数据的判断，订单号：" + orderNo + ",操作人：" + adminId);
			OnlineOrderInfoDao onlineDao = new OnlineOrderInfoDao();
			// 获取线上数据
			Orderinfo orderinfo = onlineDao.queryOrderInfoByOrderNo(orderNo);
			// 判断获取的线上订单信息是否为空
			if (orderinfo != null) {
				InsertMessageNotification messageDao = new InsertMessageNotification();
				// 当线上为 -1/4/6 时候，本地更新全部终止，并记录终止原因
				String onlineState = orderinfo.getState();
				// 订单状态为0的说明获取失败
				if (onlineState.equals("0")) {//
					String recordStr = "订单[" + orderNo + "],获取状态失败,请确认线上订单状态";
					LOG.error(recordStr);
					// 插入到消息提醒记录中
					messageDao.orderChangeError(orderNo, adminId, recordStr);
					return isCheck;
				}
				// 如果线上状态为-1(后台取消)、6(客户取消)、4(完结状态)的,不需要再执行更新状态操作
				if (onlineState.equals(OrderInfoConstantUtil.OFFLINECANCEL)
						|| onlineState.equals(OrderInfoConstantUtil.FINISH)
						|| onlineState.equals(OrderInfoConstantUtil.CUSTOMERCANCEL)) {
					// 插入到更新错误日志中
					String sqlStr = "order_no:" + orderNo;
					String remark = "订单[" + orderNo + "]线上状态为:[" + StateToChineseUtil.orderInfoState(onlineState)
							+ "],要更新状态:[" + StateToChineseUtil.orderInfoState(String.valueOf(operationType))
							+ "],不符合更新规则,拒绝更新!";
					// 更新错误日志记录
					ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, adminId, 2, remark);
					LOG.error(sqlStr + " error:" + remark);
					// 消息提醒添加错误记录
					messageDao.orderChangeError(orderNo, adminId, remark);
					isCheck = false;
				} else {// 3出运、2出库、1采购中、5审核中
					if (checkOrderInfoPriority(orderNo, onlineState, String.valueOf(operationType), adminId)) {
						// 判断状态优先级通过，执行更新操作
						isCheck = true;
					} else {
						// 添加到更新错误日志中
						String nwSqlStr = "orderinfo,order_no:" + orderNo + ",operationType:" + operationType;
						String nwRemark = "订单[" + orderNo + "]线上状态为:[" + StateToChineseUtil.orderInfoState(onlineState)
								+ "],要更新状态:[" + StateToChineseUtil.orderInfoState(String.valueOf(operationType))
								+ "],不符合更新规则,拒绝更新!";
						ErrorLogDao.insertErrorInfo("orderinfo", nwSqlStr, adminId, 2, nwRemark);
						LOG.error(nwSqlStr + " error:" + nwRemark);
						// 如果更新的状态相同，不给出消息提醒信息
						if (!onlineState.equals(String.valueOf(operationType))) {
							messageDao.orderChangeError(orderNo, adminId, nwRemark);
						}
						isCheck = false;
					}
				}
			} else {
				LOG.error("当前订单号：" + orderNo + "获取线上订单数据为空,终止继续判断,结果不通过");
				throw new Exception("当前订单号：" + orderNo + "获取线上订单数据为空,终止继续判断,结果不通过");
			}
		}
		return isCheck;*/
	}

	/**
	 * 线下更改线上订单详情状态校检
	 * 
	 * @param orderNo
	 *            订单号
	 * @param goodsid
	 *            商品id
	 * @param operationType
	 *            详情状态
	 * @param adminId
	 *            操作人id
	 * @return
	 */
	public static boolean updateOnlineOrderDetailsByLocal(String orderNo, int goodsid, int operationType, int adminId)
			throws Exception {
		//2017-5-3 ED说屏蔽校检接口，代码现在不校检，直接返回true 
		return true;
		
		/*boolean isCheck = false;
		// TODO
		if (orderNo == null || "".equals(orderNo) || goodsid == 0 || operationType == 0 || adminId == 0) {
			LOG.error("当前订单详情的订单号、商品id、更改状态、adminId数据为空,终止继续判断,结果不通过!");
			throw new Exception("当前订单详情的订单号、商品id、更改状态、adminId数据为空,终止继续判断,结果不通过!");
		} else {
			LOG.info("开始线上order_details数据的判断,订单号:" + orderNo + ",商品id:" + goodsid + ",操作人:" + adminId);
			OnlineOrderInfoDao onlineDao = new OnlineOrderInfoDao();
			// 获取线上数据
			OrderDetailsBean orderDetails = onlineDao.queryOrderDetailsByOrderNoAndGoodsid(orderNo, goodsid);
			// 判断获取的线上订单详情信息是否为空,或者adminId为0
			if (orderDetails != null) {
				InsertMessageNotification messageDao = new InsertMessageNotification();
				// 添加判断管理员权限,
				if (isAdminiRightsWithOrderDetails(orderNo, goodsid, operationType, adminId)) {
					// 拥有管理员权限的可进行逆序操作
					// 正序:符合优先级2(取消)>1(入库)>0(采购);逆序:1(入库)->0(采购)->2(取消)
					int onlineState = orderDetails.getState();
					if (onlineState == OrderDetailsConstantUtil.CANCEL) {
						// 线上状态为2(取消),无更新操作
						isCheck = false;
						// 添加到更新错误日志中
						String sqlStr = "order_details,orderid:" + orderNo + ",goodsid:" + goodsid + ",operationType:"
								+ operationType;
						String remark = "订单[" + orderNo + "]的商品(" + goodsid + ")线上状态:[取消],不符合更新规则,拒绝更新!";
						// 更新错误日志记录
						ErrorLogDao.insertErrorInfo("order_details", sqlStr, adminId, 2, remark);
						LOG.error(sqlStr + " error:" + remark);
						// 如果更新的状态相同，不给出消息提醒信息
						if (onlineState != operationType) {
							// 消息提醒添加错误记录
							messageDao.orderChangeError(orderNo, adminId, remark);
						}
					} else if (onlineState == OrderDetailsConstantUtil.WAREHOUSE) {
						// 线上状态为1(入库),操作正序流程为2(取消),逆序流程为0(采购)
						isCheck = operationType == OrderDetailsConstantUtil.NORMAL
								|| operationType == OrderDetailsConstantUtil.CANCEL;
					} else if (onlineState == OrderDetailsConstantUtil.NORMAL) {
						// 线上状态为0(采购),无操作正序流程,逆序流程为2(取消)
						isCheck = operationType == OrderDetailsConstantUtil.CANCEL;
					}
				} else {
					// 无管理员权限的,不能回退,按照正常流程走下去
					// 符合优先级2>1>0 2 取消 1 到库 0 正常
					int onlineState = orderDetails.getState();
					if (onlineState == OrderDetailsConstantUtil.CANCEL) {
						// 线上状态为2(取消),无更新操作
						isCheck = false;
						// 添加到更新错误日志中
						String sqlStr = "order_details,orderid:" + orderNo + ",goodsid:" + goodsid + ",operationType:"
								+ operationType;
						String remark = "订单[" + orderNo + "]的商品(" + goodsid + ")线上状态:[取消],不符合更新规则,拒绝更新!";
						// 更新错误日志记录
						ErrorLogDao.insertErrorInfo("order_details", sqlStr, adminId, OrderDetailsConstantUtil.CANCEL,
								remark);
						LOG.error(sqlStr + " error:" + remark);
						// 如果更新的状态相同，不给出消息提醒信息
						if (onlineState != operationType) {
							// 消息提醒添加错误记录
							messageDao.orderChangeError(orderNo, adminId, remark);
						}
					} else {
						// 必须符合前进的步骤,即0-->1-->2
						if (onlineState + 1 == operationType) {
							// 判断状态优先级通过，执行更新操作
							isCheck = true;
						} else {
							// 添加到更新错误日志中
							String sqlStr = "order_details,orderid:" + orderNo + ",goodsid:" + goodsid
									+ ",operationType:" + operationType;
							String remark = "订单[" + orderNo + "]的商品(" + goodsid + ")线上状态:["
									+ StateToChineseUtil.orderDetailsState(onlineState) + "],要更新状态:["
									+ StateToChineseUtil.orderDetailsState(operationType) + "],不符合更新规则,拒绝更新!";
							// 更新错误日志记录
							ErrorLogDao.insertErrorInfo("order_details", sqlStr, adminId, 2, remark);
							LOG.error(sqlStr + " error:" + remark);
							// 如果更新的状态相同，不给出消息提醒信息
							if (onlineState != operationType) {
								// 消息提醒添加错误记录
								messageDao.orderChangeError(orderNo, adminId, remark);
							}
							isCheck = false;
						}
					}
				}
			} else {
				LOG.error("当前订单详情的订单号:" + orderNo + ",商品id:" + goodsid + ",获取线上订单详情数据为空,终止继续判断,结果不通过!");
				throw new Exception("当前订单详情的订单号:" + orderNo + ",商品id:" + goodsid + ",获取线上订单详情数据为空,终止继续判断,结果不通过!");
			}
		}

		return isCheck;*/
	}

	/**
	 * 判断状态优先级别-1=6>4>3>2>1>5
	 * 
	 * @param onlineState
	 *            线上状态
	 * @param operationType
	 *            操作状态
	 * @return
	 */
	private static boolean checkOrderInfoPriority(String orderNo, String onlineState, String operationType,
			int adminId) {
		boolean isCheck = false;
		// TODO
		// 添加判断管理员权限,
		if (isAdminiRightsWithOrderInfo(orderNo, operationType, adminId)) {
			// 拥有管理员权限的可进行逆序操作
			isCheck = isPositiveAndNegativeSequence(operationType, onlineState);
		} else {
			// 无管理员权限的,不能回退,按照正常流程走下去
			isCheck = isPositiveSequence(operationType, onlineState);
		}
		return isCheck;
	}

	/**
	 * 判断此adminId是否有管理员权限
	 * 
	 * @param orderNo
	 *            订单号
	 * @param operationType
	 *            更改状态
	 * @param adminId
	 *            后台用户id
	 * @return
	 */
	private static boolean isAdminiRightsWithOrderInfo(String orderNo, String operationType, int adminId) {
		boolean isAdm = false;
		AdmUserDao admDao = new AdmUserDaoImpl();
		try {
			Admuser adm = admDao.getAdmUserById(adminId);
			if (adm == null) {
				LOG.error("订单信息,订单号:" + orderNo + ",操作状态:" + operationType + "adminId: " + adminId
						+ ",获取的用户信息为空,校检终止,不通过");
			} else {
				if (adm.getRoletype() == null || "".equals(adm.getRoletype())) {
					LOG.error("订单信息,订单号:" + orderNo + ",操作状态:" + operationType + "adminId: " + adminId
							+ ",当前用户权限为空,校检终止,不通过");
				} else {
					isAdm = "0".equals(adm.getRoletype());// 管理员权限为0
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("订单信息,订单号:" + orderNo + ",操作状态:" + operationType + "adminId: " + adminId
					+ ",执行查询admin用户信息失败,校检终止,不通过");
		}
		// 判断机制在,但目前不做判断,直接返回true
		isAdm = true;
		return isAdm;
	}

	/**
	 * 订单状态更改正序流程判断
	 * 
	 * @param operationType
	 *            更改状态
	 * @param onlineState
	 *            线上状态
	 * @return
	 */
	private static boolean isPositiveSequence(String operationType, String onlineState) {
		boolean isCheck = false;
		// 当线上为 -1/4/6 时候终止更新
		if (onlineState.equals(OrderInfoConstantUtil.OFFLINECANCEL) || onlineState.equals(OrderInfoConstantUtil.FINISH)
				|| onlineState.equals(OrderInfoConstantUtil.CUSTOMERCANCEL)) {
			isCheck = false;
		} else {
			// 其他状态符合优先级-1=6>4>3>2>1>5的判断通过,并且只能是前进步骤通过
			if (operationType.equals(OrderInfoConstantUtil.OFFLINECANCEL)) {
				// 操作状态是-1(后台取消),线上订单状态只能够是5(审核中)或者1(采购中)
				isCheck = onlineState.equals(OrderInfoConstantUtil.PROCURMENT)
						|| onlineState.equals(OrderInfoConstantUtil.REVIEW);
			} else if (operationType.equals(OrderInfoConstantUtil.CUSTOMERCANCEL)) {
				// 操作状态是6(客户取消),线上订单状态只能够是5(审核中)
				isCheck = onlineState.equals(OrderInfoConstantUtil.REVIEW);
			} else if (operationType.equals(OrderInfoConstantUtil.FINISH)) {
				// 操作状态是4(完结),线上订单状态只能够是3(出运)
				isCheck = onlineState.equals(OrderInfoConstantUtil.SHIPMENT);
			} else if (operationType.equals(OrderInfoConstantUtil.SHIPMENT)) {
				// 操作状态是3(出运),线上订单状态只能够是2(到库)
				isCheck = onlineState.equals(OrderInfoConstantUtil.WAREHOUSE);
			} else if (operationType.equals(OrderInfoConstantUtil.WAREHOUSE)) {
				// 操作状态是2(到库),线上订单状态只能够是1(采购中)
				isCheck = onlineState.equals(OrderInfoConstantUtil.PROCURMENT);
			} else if (operationType.equals(OrderInfoConstantUtil.PROCURMENT)) {
				// 操作状态是1(采购中),线上订单状态只能够是5(审核中)
				isCheck = onlineState.equals(OrderInfoConstantUtil.REVIEW);
			}
		}
		return isCheck;
	}

	private static boolean isPositiveAndNegativeSequence(String operationType, String onlineState) {
		boolean isCheck = false;
		// 当线上为 -1(后台取消)/6(客户取消) 时候终止更新
		if (onlineState.equals(OrderInfoConstantUtil.OFFLINECANCEL)
				|| onlineState.equals(OrderInfoConstantUtil.CUSTOMERCANCEL)) {
			isCheck = false;
		} else {
			if (onlineState.equals(OrderInfoConstantUtil.FINISH)) {
				// 线上订单状态是4(完结),无操作状态顺序流程,逆序流程是3(出运)
				isCheck = operationType.equals(OrderInfoConstantUtil.SHIPMENT);
			} else if (onlineState.equals(OrderInfoConstantUtil.SHIPMENT)) {
				// 线上订单状态是3(出运),操作状态顺序流程是4(完结),逆序流程是2(到库)
				isCheck = operationType.equals(OrderInfoConstantUtil.FINISH)
						|| operationType.equals(OrderInfoConstantUtil.WAREHOUSE);
			} else if (onlineState.equals(OrderInfoConstantUtil.WAREHOUSE)) {
				// 线上订单状态是2(到库),操作状态顺序流程是3(出运),逆序流程是1(采购中)
				isCheck = operationType.equals(OrderInfoConstantUtil.SHIPMENT)
						|| operationType.equals(OrderInfoConstantUtil.PROCURMENT);
			} else if (onlineState.equals(OrderInfoConstantUtil.PROCURMENT)) {
				// 线上订单状态是1(采购中),操作状态顺序流程是2(到库),无逆序流程
				isCheck = operationType.equals(OrderInfoConstantUtil.WAREHOUSE);
			} else if (onlineState.equals(OrderInfoConstantUtil.REVIEW)) {
				// 线上订单状态是5(审核中),操作状态顺序流程是1(采购中)或-1(后台取消),无逆序流程
				isCheck = operationType.equals(OrderInfoConstantUtil.PROCURMENT)
						|| operationType.equals(OrderInfoConstantUtil.OFFLINECANCEL);
			}
		}
		return isCheck;
	}

	private static boolean isAdminiRightsWithOrderDetails(String orderNo, int goodsid, int operationType, int adminId) {
		boolean isAdm = false;
		AdmUserDao admDao = new AdmUserDaoImpl();
		try {
			Admuser adm = admDao.getAdmUserById(adminId);
			if (adm == null) {
				LOG.error("订单详情,订单号:" + orderNo + ",商品id:," + goodsid + ",操作状态:" + operationType + "adminId: " + adminId
						+ ",获取的用户信息为空,校检终止,不通过");
			} else {
				if (adm.getRoletype() == null || "".equals(adm.getRoletype())) {
					LOG.error("订单详情,订单号:" + orderNo + ",商品id:," + goodsid + ",操作状态:" + operationType + "adminId: "
							+ adminId + ",当前用户权限为空,校检终止,不通过");
				} else {
					isAdm = "0".equals(adm.getRoletype());// 管理员权限为0
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("订单详情,订单号:" + orderNo + ",商品id:," + goodsid + ",操作状态:" + operationType + "adminId: " + adminId
					+ ",执行查询admin用户信息失败,校检终止,不通过");
		}
		// 判断机制在,但目前不做判断,直接返回true
		isAdm = true;
		return isAdm;
	}

}
