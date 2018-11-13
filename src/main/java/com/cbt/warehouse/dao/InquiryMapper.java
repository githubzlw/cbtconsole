/**
 * @ClassName:     InquiryMapper.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         Administrator
 * @Date           2017年4月17日09:37:49
 */
package com.cbt.warehouse.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 * 询价商品接口
 */
public interface InquiryMapper {

	/**
	 * 保存询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int saveInquiry(@Param("userid") int userid, @Param("payno") String payno, @Param("server_price") double server_price, @Param("pay_state") int pay_state);

	/**
	 * 保存询价详情表
	 *
	 * @param user
	 * 	用户信息
	 */
	public int saveInquiryDetail(@Param("goodsid") List<String> goodsid, @Param("inquiryid") String inquiryid);

	/**
	 * 修改询价表的付款状态
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryPay(@Param("payno") String payno, @Param("state") int state);


	/**
	 * 修改询价表状态：0-未询价，1-已询到价格，2-商品无效,3-用户不显示
	 * id订单详情ID
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryDetail(@Param("id") int id, @Param("state") int state, @Param("priceprice") double price);


	/**
	 * 查找询价表
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<Map<String, Object>> getInquiry(@Param("state") int state, @Param("userid") int userid);
	
	/**
	 * 查找询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public List<Map<String, Object>> getInquiryByInquiryid(String inqId);
	
	/**
	 * 查询已回复询价状态
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int getInquiryNumber(int userid);
	
	/**
	 * 修改询价表的删除状态
	 * 
	 * @param 
	 */
	public int delInquiry(String payno);
	
	/**
	 * 查找询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public List<String> getInquiryExist(String[] goodsId);


}
