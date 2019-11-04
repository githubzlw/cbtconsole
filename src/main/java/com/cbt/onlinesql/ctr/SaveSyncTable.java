package com.cbt.onlinesql.ctr;

import com.cbt.onlinesql.bean.OnlineDataInfo;
import com.cbt.onlinesql.dao.OnlineDataInfoDao;
import com.cbt.onlinesql.dao.OnlineDataInfoDaoImpl;
import com.cbt.util.UniqueIdUtil;

import org.slf4j.LoggerFactory;

public class SaveSyncTable {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SaveSyncTable.class);

	/**
	 * 插入更新SQL语句到OnlineDataInfo表中
	 * 
	 * @param userId
	 *            : 用户id
	 * @param orderId
	 *            : 订单号,没有订单号的设置为""值,不能为null
	 * @param businessType
	 *            : 业务类型:1、分配销售 2、取消商品3、取消订单4、拆单（普通拆单和dropship拆单）
	 *            5、改价操作6、录入货源（线上进入采购状态）7、商品替换8、商品入库操作（更新order-details和orderinfo)
	 *            9、商品出库操作(实际出库) 10、确认到账操作(现在存在问题。仅操作了本地) 11、修改余额  12、退款处理（申诉处理）
	 * @param tableName
	 *            : 执行表名称
	 * @param sqlStr
	 *            : 执行SQL体
	 */
	public static boolean InsertOnlineDataInfo(int userId, String orderId, String businessType, String tableName,
			String sqlStr) {
		int count = 0;
		boolean success = false;
		// 三次重试
		while (count < 3 && !success) {
			try {
				OnlineDataInfo info = new OnlineDataInfo();
				info.setUniqueId(UniqueIdUtil.queryByDb());
				info.setUserId(userId);
				info.setOrderId(orderId);
				info.setBusinessType(businessType);
				info.setSqlStr(sqlStr);
				info.setTableName(tableName);

				OnlineDataInfoDao dao = new OnlineDataInfoDaoImpl();
				dao.insertOnlineDataInfo(info);
				success = true;
				break;
			} catch (Exception e) {
				count++;
				//e.printStackTrace();
				LOG.error("InsertOnlineDataInfo,tableName:" + tableName + ",businessType :" + businessType
						+ " error,reason:" + e.getMessage());
			}
		}

		return success;

	}

}
