package com.cbt.warehouse.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.warehouse.pojo.orderJson;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.website.util.EasyUiJsonResult;

public interface LookReturnOrderServiceNew {
	EasyUiJsonResult FindReturndisplay(String applyUser, String state,
			String a1688Shipno, String optTimeStart, String optTimeEnd,int page,int mid,String user,String a1688order,String pid,String skuid);

	EasyUiJsonResult UpdaeReturnOrder(String ship,String ch,String money,Double freight);

	EasyUiJsonResult RemReturnOrder(String ship,String cusorder);

	EasyUiJsonResult SetReturnOrder(String ship,String number);

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
			String returnNO, String admName, int num,String goodsid,String pid);

	EasyUiJsonResult FindOdid(String cusorder);

	EasyUiJsonResult getAllOrderByOrid(String orid);


	EasyUiJsonResult getpid(String cusOrder, String tbOrder);

    String getAllOrderCount();

	orderJson getOrderByship(String shipno);

	EasyUiJsonResult SetUpMoney(String ship, String number);

	List<returndisplay> FindAllByTborder(String tborder);

    EasyUiJsonResult AddOtherOrder(List<returndisplay> list, String admName);

    EasyUiJsonResult AddOnlyRefund(String orid, Double odmany, String admName);

	EasyUiJsonResult Lookstatement(String optTimeStart, String optTimeEnd, String applyUser, int page);

    Double gettatolmaney(String optTimeStart, String optTimeEnd, String applyUser);
}
