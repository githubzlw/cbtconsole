package com.cbt.warehouse.service;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import b.b.b.c.l;

import com.cbt.pojo.Admuser;
import com.cbt.warehouse.dao.LookReturnOrderServiceNewMapper;
import com.cbt.warehouse.pojo.orderJson;
import com.cbt.warehouse.pojo.returnbill;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.website.util.EasyUiJsonResult;

@Service
public class LookReturnOrderServiceNewImpl implements LookReturnOrderServiceNew {
    @Autowired
    private LookReturnOrderServiceNewMapper lookReturnOrderServiceNewMapper;

	@Override
	public EasyUiJsonResult FindReturndisplay(String applyUser, String state,
			String a1688Shipno, String optTimeStart, String optTimeEnd, int page,int mid) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		String nameString=null;
		if ("".equals(applyUser)||"全部".equals(applyUser)||"-1".equals(applyUser)) {
			nameString=null;
		}else {
			Admuser us=this.lookReturnOrderServiceNewMapper.findUserName(applyUser);
			 nameString=us.getAdmName();	
		}
		if ("".equals(state)) {
			state=null;
		}
		if ("".equals(a1688Shipno)) {
			a1688Shipno=null;
		}
		if ("".equals(optTimeStart)||"列:2019-01-17".equals(optTimeStart)) {
			optTimeStart=null;
		}else {
			optTimeStart=optTimeStart.replaceAll("列:", "");	
		}
		if ("".equals(optTimeEnd)||"列:2019-01-17".equals(optTimeEnd)) {
			optTimeEnd=null;
		}else {
			optTimeEnd=optTimeEnd.replaceAll("列:", "");
			optTimeEnd+=" 23:59:59";
		}
		
		
		List<returndisplay> list = new ArrayList<returndisplay>();
		
		list=this.lookReturnOrderServiceNewMapper.FindReturndisplay(nameString,state,a1688Shipno,optTimeStart,optTimeEnd,page);
		if (list.size()==0) {
			json.setTotal(0);
		return json;	
		}
		int total=this.lookReturnOrderServiceNewMapper.selectCount(nameString,state,a1688Shipno,optTimeStart,optTimeEnd,page);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setOrderInfo("订单号：<a>"+list.get(i).getA1688Order()+"</a><br>运单号：<a href='/cbtconsole/website/newtrack.jsp?shipno="+list.get(i).getA1688Shipno()+"&barcode="+list.get(i).getBarcode()+"' target='_blank'>"+list.get(i).getA1688Shipno()+"</a>"
					+ "<br>下单："+list.get(i).getPlaceDate()+"<br>签收："+list.get(i).getSigntime());
			list.get(i).setCustomerorder("<a href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+list.get(i).getCustomerorder()+"' target='_blank'>"+list.get(i).getCustomerorder()+"</a>");
            list.get(i).setItem(list.get(i).getItem()+"<br>数量："+list.get(i).getItemNumber());
            if (list.get(i).getChangeShipno()==null||"".equals(list.get(i).getChangeShipno())) {
				list.get(i).setChangeShipno("<input class='but_color' type='button' value='输入换货运单号' onclick='UpShip("+list.get(i).getId()+")'>");
			} else {
				list.get(i).setChangeShipno(list.get(i).getChangeShipno()+"<input type='button' value='修改' onclick='UpShip("+list.get(i).getId()+")'>");	
			}
            if (list.get(i).getShipno()==null||"".equals(list.get(i).getShipno())) {
				list.get(i).setShipno("<input class='but_color' type='button' value='输入退货运单号' onclick='UpShipH("+list.get(i).getId()+")'>");
			} else {
				list.get(i).setShipno(list.get(i).getShipno()+"<input  type='button' value='修改' onclick='UpShipH("+list.get(i).getId()+")'>");	
			}
//            list.get(i).setChangeShipno("<input class='but_color' type='button' value='"+list.get(i).getChangeShipno()==null?"输入换货运单号":"修改"+"' onclick='"+list.get(i).getChangeShipno()==null?"UpShip("+list.get(i).getId()+")":"onclick='UpShip("+list.get(i).getId()+")'>");
//            list.get(i).setShipno("<input class='but_color' type='button' value='"+list.get(i).getShipno()==null?"输入退货运单号":"修改"+"' onclick='"+list.get(i).getShipno()==null?"UpShipH("+list.get(i).getId()+")":"onclick='UpShipH("+list.get(i).getId()+")'>");
            String stat="";
            if (list.get(i).getState()==0) {
				stat="待处理";
			}
            if (list.get(i).getState()==1) {
				stat="已处理";
			}
            if (list.get(i).getState()==2) {
				stat="已到账<br><input class='but_color' type='button' value='确认完结' onclick='upState("+list.get(i).getId()+")'>";
			}
            if (list.get(i).getState()==3) {
				stat="换货签收<br><input class='but_color' type='button' value='确认完结' onclick='upState("+list.get(i).getId()+")'>";
			}
            if (list.get(i).getState()==4) {
				stat="完结";
				
			}
            list.get(i).setStateShow(stat);
            list.get(i).setPepoInfo("申请人："+list.get(i).getApplyUser()+"<br>退货数量："+list.get(i).getReturnNumber());
            if (mid==1) {
            	 list.get(i).setPepoInfo("<input class='but_color' type='button' value='同意退货' onclick='Agreed("+list.get(i).getId()+")'>/<input class='but_color' type='button' value='驳回' onclick='rejected("+list.get(i).getId()+")'");	
			}
		}
		
		json.setRows(list);
		json.setTotal(total);
		return json;
	}

	@Override
	public EasyUiJsonResult UpdaeReturnOrder(String ship) {
		// TODO Auto-generated method stub
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo;	
		bo=this.lookReturnOrderServiceNewMapper.UpdaeReturnOrder(ship);	
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult RemReturnOrder(String ship) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo=this.lookReturnOrderServiceNewMapper.RemReturnOrder(ship);
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult SetReturnOrder(String ship,Double number) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo=this.lookReturnOrderServiceNewMapper.SetReturnOrder(ship);
		returnbill re=this.lookReturnOrderServiceNewMapper.FindOrdetByship(ship);
		re.setEndTime(new Date());
		re.setChangAmount(number);
		bo=this.lookReturnOrderServiceNewMapper.AddMoney(re);
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult UpdateReturnOrder(int ship, String number,int mid,String name) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo = false;
		if (mid==0) {
			 bo=this.lookReturnOrderServiceNewMapper.UpdateReturnOrder(ship,number,name);
			 this.lookReturnOrderServiceNewMapper.UpdaeReturnOrderBy1(ship);
		}else if (mid==1) {
			 bo=this.lookReturnOrderServiceNewMapper.UpdateReturnOrderH(ship,number,name);
			 this.lookReturnOrderServiceNewMapper.UpdaeReturnOrderBy1(ship);
		}
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult AddOrder(int number, int orid,String cusorder,String returnNO,String id) {
		 EasyUiJsonResult json=new EasyUiJsonResult();
		returndisplay re=new returndisplay();
		if (orid !=0) {
		re=this.lookReturnOrderServiceNewMapper.FindReturndisplayByOrid(orid);
		}
		
		re.setReturnNumber(number);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		re.setApplyTime(dateString);
		re.setCustomerorder(cusorder);
		re.setReturnReason(returnNO);		
		re.setApplyUser(id);
		re.setState(5);
		Boolean bo=this.lookReturnOrderServiceNewMapper.AddOrder(re);
		if (bo) {
		json.setRows(1);	
		}else {
			json.setRows(0);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult LookOrder(String tborid) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		List<returndisplay> list = new ArrayList<returndisplay>();
		list=this.lookReturnOrderServiceNewMapper.LookOrder(tborid);
		System.out.println("客户item+"+list.size());
		json.setRows(list);
		json.setTotal(5);
		return json;
	}

	@Override
	public orderJson getAllOrder(String cusOrder,String tbOrder, int mid) {
		orderJson json=new orderJson();
		List<returndisplay> listOr = new ArrayList<returndisplay>();
		List<returndisplay> listItem = new ArrayList<returndisplay>();
		listOr=this.lookReturnOrderServiceNewMapper.getAllOrder(cusOrder);		
		json.setRows1(listOr);
		for (int i = 0; i < listOr.size(); i++) {
			if (tbOrder !=null&&tbOrder.equals(listOr.get(i).getA1688Order())) {
				listItem=this.lookReturnOrderServiceNewMapper.getAllItem(listOr.get(i).getA1688Order());
				for (int j = 0; j < listItem.size(); j++) {
					returndisplay itnum=this.lookReturnOrderServiceNewMapper.Finditnum(listItem.get(j).getTbId());
					if (itnum !=null) {
						int itemNum=itnum.getItemNumber()-itnum.getReturnNumber();
						if (itemNum<=0) {
							listItem.remove(j);
							j--;
						}else {
							listItem.get(j).setItemNumber(itemNum);
							listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
							listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
						}
					}else {
						listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
						listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
					}					
				}
				json.setRows(listItem);
				return json;
			}else if(mid==1) {
				listItem=this.lookReturnOrderServiceNewMapper.getAllItem(listOr.get(0).getA1688Order());
				for (int j = 0; j < listItem.size(); j++) {
					returndisplay itnum=this.lookReturnOrderServiceNewMapper.Finditnum(listItem.get(j).getTbId());
					if (itnum !=null) {
						int itemNum=itnum.getItemNumber()-itnum.getReturnNumber();
						if (itemNum<=0) {
							listItem.remove(j);
							j--;
							}else {
								listItem.get(j).setItemNumber(itemNum);	
								listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
								listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
							}			
				}else {
					listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
					listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
				}
				}
				if (listItem.size()==0) {
					json.setRows(5);
					return json;
				}
				json.setRows(listItem);
				return json;
			}			
		}
		return json;
		
	}

	@Override
	public orderJson getAllOrderByCu(String cusOrder) {
		orderJson json=new orderJson();
		List<returndisplay> list = new ArrayList<returndisplay>();
		list=this.lookReturnOrderServiceNewMapper.getAllOrder(cusOrder);
		json.setRows(list);
		System.err.println("客户单："+list.get(0).getA1688Order());
		return json;
	}

	@Override
	public EasyUiJsonResult AddAllOrder(List<returndisplay> re) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo = false;
		List<returndisplay> list=this.lookReturnOrderServiceNewMapper.getAllItem(re.get(0).getA1688Order());
		for (int i = 0; i < list.size(); i++) {
			returndisplay ls=this.lookReturnOrderServiceNewMapper.Finditnum(list.get(i).getTbId());
			if (ls !=null) {
				list.remove(i);
				i--;
			}
		}

		for (int i = 0; i < re.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (j*3==re.get(i).getItemNumber()) {
				list.get(j).setCustomerorder(re.get(i).getCustomerorder());	
				list.get(j).setApplyUser(re.get(i).getApplyUser());
				returndisplay itnum=this.lookReturnOrderServiceNewMapper.Finditnum(list.get(j).getTbId());
				if (itnum !=null) {
					int itemNum=itnum.getItemNumber()-itnum.getReturnNumber();
					if (re.get(i).getReturnNumber()>itemNum) {
						json.setRows(4);
						return json;
					}
				}else{
					if (list.get(j).getItemNumber()<re.get(i).getReturnNumber()) {
					json.setRows(4);
					return json;
				}
				}
				list.get(j).setReturnNumber(re.get(i).getReturnNumber());
				list.get(j).setReturnReason(re.get(i).getReturnReason());
				list.get(j).setState(5);
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(currentTime);
				
				list.get(j).setApplyTime(dateString);
				bo=this.lookReturnOrderServiceNewMapper.AddOrder(list.get(j));
				}
			}	
		}
		if (bo) {
			json.setRows(1);
		}else {
			json.setRows(0);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult AddRetAllOrder(String cusorder, String tbOrder,
			String returnNO,String id) {
		List<returndisplay> list = new ArrayList<returndisplay>();
		list=this.lookReturnOrderServiceNewMapper.getAllItem(tbOrder);
		Boolean bo=false;
		try {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setCustomerorder(cusorder);
				list.get(i).setReturnReason(returnNO);
				list.get(i).setApplyUser(id);
				list.get(i).setState(0);
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(currentTime);
				list.get(i).setApplyTime(dateString);
				bo=this.lookReturnOrderServiceNewMapper.AddOrder(list.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			bo=false;
		}
		EasyUiJsonResult json=new EasyUiJsonResult();
		if (bo) {
		json.setRows(1);	
		}else {
			json.setRows(0);		
		}
		return json;
	}

	@Override
	public orderJson getAllOrderByitem(String cusOrder, String orid) {
		orderJson json=new orderJson();
		List<returndisplay> listItem = new ArrayList<returndisplay>();
		listItem=this.lookReturnOrderServiceNewMapper.getAllOrderByitem(orid);
		for (int i = 0; i < listItem.size(); i++) {
			listItem.get(i).setReturnReason("<input type='text' id='retu"+i+"' value='' >");
			listItem.get(i).setChangeShipno("<input type='text' id='num"+i+"' value='' >");	
			listItem.get(i).setCustomerorder(cusOrder);
		}
		json.setRows(listItem);
		return json;
	}

	@Override
	public EasyUiJsonResult AddOrderByOdid(int number, String odid,
			String cusorder, String returnNO, String admName, int num) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		returndisplay re=new returndisplay();
		int orid=Integer.parseInt(odid);
		re=this.lookReturnOrderServiceNewMapper.FindReturndisplayByOrid(orid);
		if (re==null) {
			re=this.lookReturnOrderServiceNewMapper.FindOrderdetailsByOrid(orid);
		}else {
			returndisplay itnum=this.lookReturnOrderServiceNewMapper.Finditnum(re.getTbId());
			int itemNum=itnum.getItemNumber()-itnum.getReturnNumber();
			if (itemNum <=0) {
				json.setRows(4);
				return json;
			}
		}
		
		if (re==null) {
		json.setRows("3");
		return json;
		}
		re.setCustomerorder(cusorder);
		re.setApplyUser(admName);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		re.setApplyTime(dateString);
		re.setReturnNumber(num);
		re.setReturnReason(returnNO);
		Boolean bo=false;
		try {
			bo = this.lookReturnOrderServiceNewMapper.AddOrder(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setRows(1);
			return json;
		}
		if (bo) {
		json.setRows(0);	
		}else {
		json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult FindOdid(String cusorder) {
		EasyUiJsonResult jsonResult=new EasyUiJsonResult();
		List<returndisplay> list=new ArrayList<returndisplay>();
		list=this.lookReturnOrderServiceNewMapper.FindOdid(cusorder);
		jsonResult.setRows(list);
		return jsonResult;
	}

	@Override
	public EasyUiJsonResult getAllOrderByOrid(String odid) {
		EasyUiJsonResult jsonResult=new EasyUiJsonResult();
		if (odid==null||"".equals(odid)) {
			odid="0";
		}
		int orid=Integer.parseInt(odid);
		returndisplay re=this.lookReturnOrderServiceNewMapper.FindReturndisplayByOrid(orid);
		if (re == null) {
			jsonResult.setRows(2);
			return jsonResult;
		}
		returndisplay itnum=this.lookReturnOrderServiceNewMapper.Finditnum(re.getTbId());
		if (itnum !=null) {
			int num=itnum.getItemNumber()-itnum.getReturnNumber();
			if (num<=0) {
				jsonResult.setRows(1);
				return jsonResult;
			}else {
				re.setItemNumber(num);
			}
		}
		jsonResult.setRows(re);
		return jsonResult;
	}
	
	
}
