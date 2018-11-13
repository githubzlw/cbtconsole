package com.cbt.pay.service;

import com.cbt.bean.SpiderBean;

import java.util.List;

public interface ISpidersServer {

	/*获取商品的其他信息*/
	public List<SpiderBean> getSelectedItem(int userid, String itemid, int state);

	/*获取购物车的其他信息*/
	public List<Object[]> getSelectedItemPrice(int userid, String itemid, int state);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取订单信息，地址，交期
	 */
	public int sendEmail(String orderNo, int useId);
}
