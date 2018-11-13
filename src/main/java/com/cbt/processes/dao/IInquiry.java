package com.cbt.processes.dao;

import com.cbt.bean.InquiryDetail;

import java.util.List;


/**
 * @author ylm
 * 询价商品接口
 */
public interface IInquiry {

	/**
	 * 保存询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int saveInquiry(int userid, String payno, double server_price, int pay_state);

	/**
	 * 保存询价详情表
	 *
	 * @param user
	 * 	用户信息
	 */
	public int saveInquiryDetail(List<String> goodsid, String inquiryid);

	/**
	 * 修改询价表的付款状态
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryPay(String payno, int state);


	/**
	 * 修改询价表状态：0-未询价，1-已询到价格，2-商品无效,3-用户不显示
	 * id订单详情ID
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryDetail(int id, int state, double price);


	/**
	 * 查找询价表
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<InquiryDetail> getInquiry(int state, int userid);
	
	/**
	 * 查找询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public List<InquiryDetail> getInquiry(String inqId);
	
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
