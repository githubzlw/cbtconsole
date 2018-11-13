package com.cbt.customer.service;

import ceRong.tools.bean.DorpDwonBean;
import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.DropShipBean;
import com.cbt.bean.StockNearbyBean;
import com.cbt.customer.dao.IMoreActionDao;
import com.cbt.customer.dao.MoreActionDaoImpl;

import java.util.List;

public class MoreActionServiceImpl implements IMoreActionService {

	IMoreActionDao dao = new MoreActionDaoImpl();
	
	@Override
	public int addCustomOrder(CustomOrderBean cob) {
		return dao.addCustomOrder(cob);
	}
	
	@Override
	public int addStockNearby(StockNearbyBean cnb) {
		return dao.addStockNearby(cnb);
	}
	
	@Override
	public int addDropShip(DropShipBean dsb) {
		return dao.addDropShip(dsb);
	}
	
	@Override
	public List<StockNearbyBean> findAllStockNearby(int userId, String userName, int start, int end, String useremail) {
		return dao.findAllStockNearby(userId, userName, start, end,useremail);
	}
	
	@Override
	public List<DropShipBean> findAllDropShip(int userId, String userName, int start, int end, String useremail) {
		return dao.findAllDropShip(userId, userName, start, end,useremail);
	}

	@Override
	public List<CustomOrderBean> findAllCustomOrder(String startDate, String endDate, int start, int end, String useremail, String state) {
		return dao.findAllCustomOrder(startDate, endDate, start, end,useremail,state);
	}
	
	@Override
	public List<CustomOrderBean> findAllPriceMatch(String startDate, String endDate, int start, int end, String useremail, String state) {
		return dao.findAllPriceMatch(startDate, endDate, start, end,useremail,state);
	}
	
	@Override
	public List<CustomOrderBean> findAllNameYourPrice(String startDate, String endDate, int start, int end, String useremail, String state) {
		return dao.findAllNameYourPrice(startDate, endDate, start, end,useremail,state);
	}
	
	@Override
	public List<CustomOrderBean> findAllCustomRfq(String startDate, String endDate, int start, int end, String useremail, String state) {
		return dao.findAllCustomRfq(startDate, endDate, start, end,useremail,state);
	}
	
	@Override
	public List<CustomOrderBean> findSearchKey(String searchKey) {
		return dao.findSearchKey(searchKey);
	}
	
	
	
	@Override
	public List<CustomOrderBean> findAllCustomorSearch(String startDate, String endDate, int start, int end, String useremail) {
		return dao.findAllCustomorSearch(startDate, endDate, start, end,useremail);
	}
	
	@Override
	public List<DorpDwonBean> getLargeIndexInfo() {
		return dao.getLargeIndexInfo();
	}
	
	@Override
	public int total(int userId, String userName, int start, int end, String useremail) {
		return dao.total(userId, userName, start, end,useremail);
	}

	@Override
	public int dropShiptotal(int userId, String userName, int start, int end, String useremail) {
		return dao.dropShiptotal(userId, userName, start, end,useremail);
	}
	
	@Override
	public int customOrdertotal(int userId, String userName, int start, int end, String useremail) {
		return dao.customOrdertotal(userId, userName, start, end,useremail);
	}
	
	@Override
	public int updateCustomState(int userId,int type) {
		return dao.updateCustomState(userId,type);
	}
	
	
}
