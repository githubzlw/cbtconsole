package com.cbt.processes.service;

import com.cbt.bean.InquiryDetail;

import java.util.List;

/**
 * @author ylm
 * 询价商品接口
 */
public interface IInquiryServer {

	/**
	 * 保存询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int saveInquiry(int userid, String payno, double server_price, int pay_state, String[] goodsid);

	/**
	 * 修改询价表状态：0-未询价，1-已询到价格，2-商品无效
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryDetail(int userid, int id, int state, double price);


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
	 * 修改询价表的付款状态
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upInquiryPay(String payno, int state);
	
	/**
	 * 修改询价表的删除状态
	 * ylm
	 * @param 
	 */
	public int delInquiry(String inquiryId);
}
