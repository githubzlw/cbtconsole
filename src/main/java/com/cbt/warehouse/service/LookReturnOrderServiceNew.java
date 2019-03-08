package com.cbt.warehouse.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.warehouse.pojo.orderJson;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.website.util.EasyUiJsonResult;

public interface LookReturnOrderServiceNew {
	EasyUiJsonResult FindReturndisplay(String applyUser, String state,
			String a1688Shipno, String optTimeStart, String optTimeEnd,int page,int mid);

	EasyUiJsonResult UpdaeReturnOrder(String ship);

	EasyUiJsonResult RemReturnOrder(String ship);

	EasyUiJsonResult SetReturnOrder(String ship,Double number);

	EasyUiJsonResult UpdateReturnOrder(int ship, String number,int mid,String name);

	EasyUiJsonResult AddOrder(int number, int orid, String cusorder,
			String returnNO, String id);

	EasyUiJsonResult LookOrder(String tborid);

	orderJson getAllOrder(String cusOrder, String tbOrder, int mid);

	orderJson getAllOrderByCu(String cusOrder);

	EasyUiJsonResult AddAllOrder(List<returndisplay> re);

	EasyUiJsonResult AddRetAllOrder(String cusorder, String tbOrder,
			String returnNO, String admName);

	orderJson getAllOrderByitem(String cusOrder, String orid);

	EasyUiJsonResult AddOrderByOdid(int number, String odid, String cusorder,
			String returnNO, String admName, int num,String goodsid);

	EasyUiJsonResult FindOdid(String cusorder);

	EasyUiJsonResult getAllOrderByOrid(String orid);

	

	

	

	

}