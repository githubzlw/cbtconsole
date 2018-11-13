package com.cbt.customer.dao;

import ceRong.tools.bean.DorpDwonBean;
import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.DropShipBean;
import com.cbt.bean.StockNearbyBean;

import java.util.List;

public interface IMoreActionDao {
	
	/**
	 * 方法描述:添加顾客订单
	 * author:zlw
	 * date:2015年11月03日
	 * @param cob
	 * @return
	 */
	public int addCustomOrder(CustomOrderBean cob);
	
	/**
	 * 方法描述:添加StockNearby
	 * author:zlw
	 * date:2015年11月03日
	 * @param cob
	 * @return
	 */
	public int addStockNearby(StockNearbyBean cnb);
	
	
	/**
	 * 方法描述:添加StockNearby
	 * author:zlw
	 * date:2015年11月03日
	 * @param cob
	 * @return
	 */
	public int addDropShip(DropShipBean dsb);
	
	/**
	 * 方法描述:查看所有Stock Nearby
	 * author:zlw
	 * date:2015年11月04日
	 * @return
	 */
	public List<StockNearbyBean> findAllStockNearby(int userId, String userName, int start, int end, String useremail);

	/**
	 * 方法描述:查看所有DropShip
	 * author:zlw
	 * date:2015年11月04日
	 * @return
	 */
	public List<DropShipBean> findAllDropShip(int userId, String userName, int start, int end, String useremail);

	/**
	 * 方法描述:查看所有CustomOrder
	 * @return
	 */
	public List<CustomOrderBean> findAllCustomOrder(String startDate, String endDate, int start, int end, String useremail, String state);

	public List<CustomOrderBean> findAllPriceMatch(String startDate, String endDate, int start, int end, String useremail, String state);

	public List<CustomOrderBean> findAllNameYourPrice(String startDate, String endDate, int start, int end, String useremail, String state);

	public List<CustomOrderBean> findAllCustomRfq(String startDate, String endDate, int start, int end, String useremail, String state);

	public List<CustomOrderBean> findAllCustomorSearch(String startDate, String endDate, int start, int end, String useremail);

	public List<DorpDwonBean> getLargeIndexInfo();

	public List<CustomOrderBean> findSearchKey(String searchKey);

	/**
	 * 方法描述:统计所有的stocknearby数量
	 * author:zlw
	 * date:2015年11月04日
	 * @return
	 */
	public int total(int userId, String userName, int start, int end, String useremail);

	/**
	 * 方法描述:统计所有的dropShip数量
	 * author:zlw
	 * date:2015年11月04日
	 * @return
	 */
	public int dropShiptotal(int userId, String userName, int start, int end, String useremail);

	/**
	 * 方法描述:统计所有的customOrder数量
	 * author:zlw
	 * date:2015年11月04日
	 * @return
	 */
	public int customOrdertotal(int userId, String userName, int start, int end, String useremail);

	/**
	 * 方法描述:更新客户发信状态
	 */
	public int updateCustomState(int userId, int type);
	
}
