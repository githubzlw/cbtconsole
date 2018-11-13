package com.cbt.service;

import com.cbt.bean.Address;
import com.cbt.bean.AutoOrderBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;

import java.util.List;
import java.util.Map;

public interface AutoOrderService {
	/*用户添加订单记录
	 * wanyang*/
	public int addOrderInfo(List<OrderBean> OrderBean, int address, int odcount);
	/*添加订单详细信息*/
	public int addOrderDetail(List<OrderDetailsBean> orderdetails);
	/*新增地址*/
	public int addOrderAddress(Map<String, Object> map);
	/*获取用户地址*/
	public List<Address> getUserAddr(int userid);
	/**
	 * 获取订单号
	 * author:ylm
	 * 2015-09-16
	 */
	public String getOrderNo();

	/**
	 * 获取实时汇率
	 * @return
	 */
	public String getExchangeRate();

	/**自生成订单列表
	 * @date 2016年11月18日
	 * @author abc
	 * @param orderid  订单号
	 * @param userid	用户id
	 * @param page	页
	 * @return
	 */
	public  List<AutoOrderBean> getOrderList(String orderid, String userid, String page);
	
	
	/**取消自生成订单
	 * @date 2016年11月21日
	 * @author abc
	 * @param orderid	订单号
	 * @return  
	 */
	public  int cancelOrder(String orderid);
	/**取消自生成进账记录
	 * @date 2016年11月21日
	 * @author abc
	 * @param pid  payment表id
	 * @return  
	 */
	public  int cancelPayment(String pid);
	
	

}
