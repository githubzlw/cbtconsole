package com.cbt.processes.dao;

/**
 * @author ylm
 * 货币单位改变
 */
public interface ICurrencyDao {

	/**
	 * 修改用户表中的余额和货币单位
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int upUserCurrency(int userid, String currency, double exchange_rate);

	/**
	 * 修改购物车的单价和货币单位
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upGoodCurrency(int userid, String sessionId, String currency, double exchange_rate);
}
