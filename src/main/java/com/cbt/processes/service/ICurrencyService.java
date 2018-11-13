package com.cbt.processes.service;

/**
 * @author ylm
 * 货币单位改变
 */
public interface ICurrencyService {

	/**
	 * 修改用户表中的余额和货币单位&修改购物车的单价和货币单位
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int upUserCurrency(int userid, String sessionId, String currency, double exchange_rate);
	
	
	/**
	 * 根据货币查找汇率
	 * @param cur
	 * @return
	 */
	public double currencyConverter(String cur);
}
