package com.cbt.website.dao2;

/**
 * @author wanyang
 * 针对订单详情统一的数据库操作接口类
 * 以后订单详情数据库操作统一放入该类
 * 2015-11-06
 */
public interface IWebsiteOrderDetailDao {
	/*统计订单采购金额数据库操作
	 *orderid:订单id
	 */
	public float PurchaseAmountOfOrder(String orderid);
	/*修改订单状态--提供给销售进行操作的数据库dao
	 *orderid:订单id
	 */
	public int websiteUpdateOrderState(String orderid, int state);
	/*修改未付款的金额
	 *orderid:订单id
	 */
	public int websiteUpdateOrderPrice(String orderid, double price);
	
}
