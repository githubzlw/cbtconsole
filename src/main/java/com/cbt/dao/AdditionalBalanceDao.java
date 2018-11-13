package com.cbt.dao;

import com.cbt.bean.BalanceBean;

import java.util.List;
import java.util.Map;

public interface AdditionalBalanceDao {
	/**插入数据
	 * @date 2016年9月23日
	 * @author abc
	 * @param bean
	 * @return  
	 */
	public int insert(BalanceBean bean);
	/**插入数据 邮件投诉+用户投诉
	 * @date 2016年9月23日
	 * @author abc
	 * @param bean
	 * @return  
	 */
	public int insertForRefund(BalanceBean bean);
	/**更新状态
	 * @date 2016年10月8日
	 * @author abc
	 * @param id
	 * @param state
	 * @return  
	 */
	public int updateStateById(int id, int state);

	/**用户奖励余额列表
	 * @date 2016年9月23日
	 * @author abc
	 * @param userId
	 * @return
	 */
	public List<BalanceBean> getBalanceByUserId(Integer userId, int page);


	/**用户奖励总额
	 * @date 2016年9月23日
	 * @author abc
	 * @param userId
	 * @return
	 */
	public double getMoneyAmount(int userId);

	/**用户奖励余额列表
	 * @date 2016年9月23日
	 * @author abc
	 * @param userId
	 * @return
	 */
	public Map<String, String> getBalanceByUserIds(List<Integer> userIds);

	/**获取指定投诉id的余额补偿款
	 * @date 2016年10月12日
	 * @author abc
	 * @param cid
	 * @return
	 */
	public double getMoneyAmountByCid(String cid);
	
	/**获取指定投诉id列表的余额补偿款
	 * @date 2016年10月13日
	 * @author abc
	 * @param cidList
	 * @return  
	 */
	public Map<String, Double> getMoneyAmountByCids(String cidList);
}
