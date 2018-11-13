package com.cbt.dao;

import com.cbt.bean.DiscountBean;

import java.util.List;

public interface ClassDiscountDao {

	
	/**查询
	 * @date 2016年11月5日
	 * @author abc
	 * @param catid 类别id
	 * @param price 金额
	 * @param desopite 折扣
	 * @return  
	 */
	public List<DiscountBean> getDiscount(String catid, String price, String desopite);
	
	/**增加一条数据
	 * @date 2016年11月5日
	 * @author abc
	 * @param bean
	 * @return  
	 */
	public int add(DiscountBean bean);
	
	/**删除
	 * @date 2016年11月5日
	 * @author abc
	 * @param id
	 * @return  
	 */
	public int delete(int id);
	
	/**更新
	 * @date 2016年11月5日
	 * @author abc
	 * @param bean
	 * @return  
	 */
	public int update(DiscountBean bean);
	
	/**是否有记录
	 * @date 2016年11月15日
	 * @author abc
	 * @param catid
	 * @return  
	 */
	public int isExsis(String catid);
}
