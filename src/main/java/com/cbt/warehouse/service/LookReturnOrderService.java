package com.cbt.warehouse.service;

import java.util.List;

import com.cbt.warehouse.pojo.ReturnRes;
import com.cbt.warehouse.pojo.Returndetails;

public interface LookReturnOrderService {

	List<ReturnRes> SelectReturnOrder(String admuserid, String state,
			String orderNum,int page,String mid);

	Boolean UpdataReturnOrder(String tborder,String returnState,int sta,String returnOrder,String company);

	Boolean AddOrder(Returndetails returndetails);

	ReturnRes FindReturnOrder(String tborder);

	Boolean RemReturnOrder(String order,int sta);

	Boolean AddReturntbOrder(String cusorder, String returnApply,
			String returnReason, Double returnMoney, String tborder,String ordeerPeo,String warehouse);

	int countPage(String admuserid, String state, String orderNum, int page,
			String mid);

}
