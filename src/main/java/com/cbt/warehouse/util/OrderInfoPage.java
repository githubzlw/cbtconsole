package com.cbt.warehouse.util;

import com.cbt.bean.Forwarder;
import com.cbt.bean.OrderInfoPrint;
import com.cbt.bean.StorageLocationBean;
import com.cbt.warehouse.pojo.ExpressRecord;
import com.cbt.warehouse.pojo.OrderFeePojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class OrderInfoPage {
	private int pageNum; // 当前页
	private int pageSize; // 也大小
	private int pageSum; // 总记录数
	private int roleType;

	private List<StorageLocationBean> pagelist;
	private List<Forwarder> forwarderlist; //已出库列表
	private List<OrderInfoPrint> oipList;//库存列表
	private List<OrderFeePojo> orderfeelist;//运费统计报表
	private List<ExpressRecord> recordlist;//运费统计报表
	private List<String> list_tbOrderid;//运费统计报表

	private List<String> list_orderid;//运费统计报表
	
	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	public List<ExpressRecord> getRecordlist() {
		return recordlist;
	}



	public void setRecordlist(List<ExpressRecord> recordlist) {
		this.recordlist = recordlist;
	}

	//查询条件
	private String orderid;
	private String userid;
	private String goodid;
	private String orderstruts;  // 0 所有 1未审核 2已通过 3有问题
	private String day; //   时间查询
	private String express_no;
	private String express_code;
	
	private String ckStartTime; //   入库开始时间
	private String ckEndTime; //   入库结束时间
	
	
	private String trans_method;// 运费统计报表  出货方式
	
//	private String saleOrPurchase ; //只看销售或者采购
	
	//库存查询条件
	private String goodsname;

	
	public String getExpress_code() {
		return express_code;
	}



	public void setExpress_code(String express_code) {
		this.express_code = express_code;
	}

	public List<String> getList_tbOrderid() {
		return list_tbOrderid;
	}



	public void setList_tbOrderid(List<String> list_tbOrderid) {
		this.list_tbOrderid = list_tbOrderid;
	}



	public List<String> getList_orderid() {
		return list_orderid;
	}



	public void setList_orderid(List<String> list_orderid) {
		this.list_orderid = list_orderid;
	}

	//获得订单
	public String[] getAllOrders(){
		//没有查询出来订单
		if(pagelist == null){
			return null;
		}
		
		//保存所有的订单 不包含重复 用来作为页面第一个循次数
		String strOrders = "";
		String [] arrayOrders = null;
		HashSet<String> set = new HashSet<String>();  
		for (int i = 0; i < pagelist.size(); i++) {
			String temp = pagelist.get(i).getOrderid();
			if(strOrders != null && temp !=null){
//				if(strOrders.indexOf(temp) == -1){
//					strOrders += pagelist.get(i).getOrderid()+",";
//				}
				set.add(temp);
			}
		}  
		Iterator<String> it = set.iterator();  
		while (it.hasNext()) {  
		  String str = it.next();  
		  strOrders += str+",";
		}  
		if(strOrders != ""){  //有订单 去结尾逗号
			strOrders = strOrders.substring(0,strOrders.length()-1);
			
			if(strOrders.indexOf(",") == -1)  //如果只有一个订单
			{
				arrayOrders = new String[1];
				arrayOrders[0] = strOrders;
				
			}else{ //多个订单
				//分割出来每一个订单 保存数组
				arrayOrders = strOrders.split(",");
			}	
		}
		return arrayOrders;
	}
	
	public static void main(String[] args) {
		List list=new ArrayList();
		list.add("Q423372253642988_1");
		list.add("Q423372253642988_2");
		list.add("Q423372253642988");
		String strOrders = "";
		HashSet<String> set = new HashSet<String>();  
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			if(strOrders != null && temp !=null){
				set.add(temp);
			}
		}  
		System.out.println(set.toString());
	}
	
	
	
	public String getCkStartTime() {
		return ckStartTime;
	}



//	public String getSaleOrPurchase() {
//		return saleOrPurchase;
//	}
//
//
//
//	public void setSaleOrPurchase(String saleOrPurchase) {
//		this.saleOrPurchase = saleOrPurchase;
//	}



	public void setCkStartTime(String ckStartTime) {
		this.ckStartTime = ckStartTime;
	}



	public String getExpress_no() {
		return express_no;
	}



	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}



	public String getGoodid() {
		return goodid;
	}



	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}



	public List<OrderFeePojo> getOrderfeelist() {
		return orderfeelist;
	}



	public void setOrderfeelist(List<OrderFeePojo> orderfeelist) {
		this.orderfeelist = orderfeelist;
	}



	public String getTrans_method() {
		return trans_method;
	}



	public void setTrans_method(String trans_method) {
		this.trans_method = trans_method;
	}



	public String getCkEndTime() {
		return ckEndTime;
	}



	public void setCkEndTime(String ckEndTime) {
		this.ckEndTime = ckEndTime;
	}



	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public List<OrderInfoPrint> getOipList() {
		return oipList;
	}

	public void setOipList(List<OrderInfoPrint> oipList) {
		this.oipList = oipList;
	}

	//获得userid
	public String[] getAllUserids(){
		//没有查询出来订单
		if(pagelist == null){
			return null;
		}
		
		//保存所有的订单 不包含重复 用来作为页面第一个循次数
		String userids = "";
		String [] arrayUsers = null;
		
		for (int i = 0; i < pagelist.size(); i++) {
			if(userids.indexOf(pagelist.get(i).getUser_id()) == -1){
				userids += pagelist.get(i).getUser_id()+",";
			}
		}
		if(userids != ""){  //有订单 去结尾逗号
			userids = userids.substring(0,userids.length()-1);
			
			if(userids.indexOf(",") == -1)  //如果只有一个订单
			{
				arrayUsers = new String[1];
				arrayUsers[0] = userids;
				
			}else{ //多个订单
				//分割出来每一个订单 保存数组
				arrayUsers = userids.split(",");
			}	
		}
		return arrayUsers;
	}
	
	//一共多少页
	public int getPageCount() {
		int pageCount = 0;
		 
		if(pageSum==0){  //记录数 ==0  直接返回0
			return pageCount;
		}
		
		pageCount = pageSum / pageSize;
		if (pageSum % pageSize != 0) {
			pageCount++;
		}
		return pageCount;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	//下一页
	public int getNextPage() {
		int nextNnum = pageNum;
		if (nextNnum == getPageCount()) {
			nextNnum = 1;
		}else{
			nextNnum += 1;
		}
		return nextNnum;
	}

	//上一页
	public int getPreviousPage() {
		int previous = pageNum;
		if (previous == 1) {
			previous = 1;
		}else{
			previous -= 1;;
		}
		return previous;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSum() {
		return pageSum;
	}

	public void setPageSum(int pageSum) {
		this.pageSum = pageSum;
	}

	

	public List<StorageLocationBean> getPagelist() {
		return pagelist;
	}

	public void setPagelist(List<StorageLocationBean> pagelist) {
		this.pagelist = pagelist;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUserid() {
		return userid;
	}

	public List<Forwarder> getForwarderlist() {
		return forwarderlist;
	}

	public void setForwarderlist(List<Forwarder> forwarderlist) {
		this.forwarderlist = forwarderlist;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderstruts() {
		return orderstruts;
	}

	public void setOrderstruts(String orderstruts) {
		this.orderstruts = orderstruts;
	}


	
	
}
