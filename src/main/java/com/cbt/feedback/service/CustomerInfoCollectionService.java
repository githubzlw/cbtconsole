package com.cbt.feedback.service;

import com.cbt.feedback.bean.CustomerFeedback;
import com.cbt.feedback.bean.CustomerInfoCollection;
import com.cbt.feedback.bean.Questionnaire;

import java.util.List;

public interface CustomerInfoCollectionService {

	/**
	 * 查询所有的客户信息数据
	 * 
	 * @param type
	 *            : 来源类型
	 * @param sales
	 *            : 销售额
	 * @param beginDate
	 *            ： 开始时间
	 * @param endDate
	 *            ： 结束时间
	 * @param comment
	 *            : 留言
	 * @param pageNo
	 * @return
	 */
	public List<Questionnaire> queryForList(int type, String sales, String beginDate, String endDate, String comment,
                                            int pageNo);

	/**
	 * 查询所有符合的客户信息总数
	 * 
	 * @param type
	 * @param sales
	 * @param beginDate
	 * @param endDate
	 * @param comment
	 * @return
	 */
	public Long queryCount(int type, String sales, String beginDate, String endDate, String comment);

	/**
	 * 根据type查询客户信息数据
	 * 
	 * @param type
	 *            : 类别
	 * @return
	 */
	public List<CustomerInfoCollection> queryByType(int type);

	/**
	 * 插入客户信息数据
	 * 
	 * @param customerInfo
	 */
	public void insertCustomerInfo(CustomerInfoCollection customerInfo);

	/**
	 * 修改客户信息数据
	 * 
	 * @param CustomerInfoCollection
	 */
	public void updateCustomerInfo(CustomerInfoCollection customerInfo);

	/**
	 * 根据id删除客户信息数据
	 * 
	 * @param id
	 */
	public void deleteCustomerInfo(int id);

	/**
	 * 查询全部客户反馈数据
	 * 
	 * @return
	 */
	public List<CustomerFeedback> queryForAllList();
}
