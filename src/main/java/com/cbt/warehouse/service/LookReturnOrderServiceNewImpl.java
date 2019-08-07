package com.cbt.warehouse.service;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cbt.warehouse.util.StringUtil;
import org.apache.tools.ant.taskdefs.condition.And;
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
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	@Override
	public EasyUiJsonResult FindReturndisplay(String applyUser, String state,
			String a1688Shipno, String optTimeStart, String optTimeEnd, int page,int mid,String user,String a1688order) {
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
		if ("".equals(a1688order)){
			a1688order=null;
		}
		String userOther=null;

			if ("mindy".equalsIgnoreCase(user)){
				userOther="策融BY1";
			}
			if ("helen".equalsIgnoreCase(user)){
				userOther="策融BY2";
			}
			if ("camille".equalsIgnoreCase(user)){
				userOther="策融BY3";
			}

		if ("-2".equals(state)){
			state=null;
		}
		List<returndisplay> list = new ArrayList<returndisplay>();
		
		list=this.lookReturnOrderServiceNewMapper.FindReturndisplay(nameString,state,a1688Shipno,optTimeStart,optTimeEnd,page,userOther,user,a1688order);
		if (list.size()==0) {
            json.setRows("");
            json.setTotal(0);
		return json;	
		}
		int total=this.lookReturnOrderServiceNewMapper.selectCount(nameString,state,a1688Shipno,optTimeStart,optTimeEnd,page,userOther,user,a1688order);

		for (int i = 0; i < list.size(); i++) {
			String ship=list.get(i).getShipno();
			list.get(i).setOrderInfo("订单号：<a >"+list.get(i).getA1688Order()+"</a><br>运单号：<a href='/cbtconsole/website/newtrack.jsp?shipno="+list.get(i).getA1688Shipno()+"&barcode="+list.get(i).getBarcode()+"' target='_blank'>"+list.get(i).getA1688Shipno()+"</a>"
					+ "<br>下单："+list.get(i).getPlaceDate()+"<br>签收："+list.get(i).getSigntime());
			list.get(i).setCustomerorder("<a href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+list.get(i).getCustomerorder()+"' target='_blank'>"+list.get(i).getCustomerorder()+"</a>");
            list.get(i).setItem("产品名：<a href='https://www.importx.com/goodsinfo/122916001-121814002-1"+list.get(i).getItem()+".html' target='_blank'>"+list.get(i).getItemname()+"</a><br>pid:<a href='https://detail.1688.com/offer/"+list.get(i).getItem()+".html' target='_blank'>"+list.get(i).getItem()+"</a><br>规格："+list.get(i).getSku()+"<br>采购数量："+list.get(i).getItemNumber());
			if (list.get(i).getReason()==null) {
            if (list.get(i).getChangeShipno()==null||"".equals(list.get(i).getChangeShipno())) {
				list.get(i).setChangeShipno("<input id=\"cid"+list.get(i).getId()+"\" class='but_color' type='button' value='输入换货运单号' onclick='UpShip("+list.get(i).getId()+")'>");
			} else {
				list.get(i).setChangeShipno(list.get(i).getChangeShipno()+"<input id=\"cid"+list.get(i).getId()+"\" type='button' value='修改' onclick='UpShip("+list.get(i).getId()+")'>");
			}

				if (list.get(i).getShipno() == null || "".equals(list.get(i).getShipno())) {
					list.get(i).setShipno("<input id=\"did"+list.get(i).getId()+"\" class='but_color' type='button' value='输入退货运单号' onclick='UpShipH(" + list.get(i).getId() + ")'>");
				} else {
					list.get(i).setShipno(list.get(i).getShipno() + "<input id=\"did"+list.get(i).getId()+"\" type='button' value='修改' onclick='UpShipH(" + list.get(i).getId() + ")'>");
				}
			}
//            list.get(i).setChangeShipno("<input class='but_color' type='button' value='"+list.get(i).getChangeShipno()==null?"输入换货运单号":"修改"+"' onclick='"+list.get(i).getChangeShipno()==null?"UpShip("+list.get(i).getId()+")":"onclick='UpShip("+list.get(i).getId()+")'>");
//            list.get(i).setShipno("<input class='but_color' type='button' value='"+list.get(i).getShipno()==null?"输入退货运单号":"修改"+"' onclick='"+list.get(i).getShipno()==null?"UpShipH("+list.get(i).getId()+")":"onclick='UpShipH("+list.get(i).getId()+")'>");

            String stat="";
			if (list.get(i).getRefund_time()==null){
				list.get(i).setRefund_time("");
			}

            if (list.get(i).getState()==0) {
				stat="待处理<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额："+list.get(i).getActual_money()+"</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'>";
			}
            if (list.get(i).getState()==1) {
				stat="已处理<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额："+list.get(i).getActual_money()+"</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'>";
			}

            if (list.get(i).getState()==2) {
				if (!"".equals(list.get(i).getActual_money()) && !"".equals(list.get(i).getRefund_money())) {
						if (list.get(i).getActual_money() == list.get(i).getRefund_money() && list.get(i).getRefund_money()!=0 &&list.get(i).getActual_money() !=0) {
							stat = "已到账<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额：" + list.get(i).getActual_money() +"</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'> <br/>退款时间：" + list.get(i).getRefund_time() + "<br><a style=\"color: red\">1688退款金额：" + list.get(i).getRefund_money()+"</a>";
						} else if (StringUtil.isNotBlank(list.get(i).getDifferences()) ){
							stat = "已到账<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额：" + list.get(i).getActual_money() + "</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'> <br/>退款时间：" + list.get(i).getRefund_time() + "<br><a style=\"color: red\">1688退款金额：" + list.get(i).getRefund_money() + "</a><br/>差异原因:"+list.get(i).getDifferences();

						}else {
							stat = "已到账<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额：" + list.get(i).getActual_money() + "</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'> <br/>退款时间：" + list.get(i).getRefund_time() + "<br><a style=\"color: red\">1688退款金额：" + list.get(i).getRefund_money() + "</a><input class='but_color' id=\"oid"+list.get(i).getId()+"\" type='button' value='差异原因' onclick='upState(\"" + ship + "\",this)'>" ;


						}
					}else {
						stat = "已到账<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额：" + list.get(i).getActual_money() + "</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'> <br/>退款时间：" + list.get(i).getRefund_time() + "<br><a style=\"color: red\">1688退款金额：" + list.get(i).getRefund_money()+"</a>";
					}

			}
            if (list.get(i).getState()==3) {
				stat="换货签收<br><input class='but_color' type='button' value='确认完结' onclick='upState("+ship+")'>";
			}
            if (list.get(i).getState()==4) {
				stat = "完结<br><a id=\"pid"+list.get(i).getId()+"\" style=\"color: red\">采购录入金额：" + list.get(i).getActual_money() + "</a><input  type='button' value='修改' onclick='UpMoney(\"" + ship + "\",\""+list.get(i).getId()+"\")'> <br/>退款时间：" + list.get(i).getRefund_time() + "<br><a style=\"color: red\">1688退款金额：" + list.get(i).getRefund_money()+"</a>";
				if (StringUtil.isNotBlank(list.get(i).getDifferences())&&list.get(i).getActual_money()!=list.get(i).getRefund_money()){
					stat+="<br/>差异原因："+list.get(i).getDifferences();
				}
				if (list.get(i).getReason()!=null ){
					stat += "<br>退货方式："+list.get(i).getReason()+"<br>同意退货时间：" + list.get(i).getReturntime();
				}
			}
				if (list.get(i).getReturntime()!=null  ){
					stat += "<br>同意退货时间：" + list.get(i).getReturntime();
			}

			if (list.get(i).getState()==5 ){
					stat = "完结<br>驳回理由："+list.get(i).getReason()+"<br>驳回退货时间：" + list.get(i).getReturntime();
			}
			if (list.get(i).getState()==-1) {
				list.get(i).setShipno("");
				stat="<input class='but_color' type='button' value='同意退货' onclick='Agreed("+list.get(i).getId()+")'><br> / <br><input class='but_color' type='button' value='驳回' onclick='rejected("+list.get(i).getId()+")'/>";
			}

            list.get(i).setStateShow(stat);
            list.get(i).setPepoInfo("申请人："+list.get(i).getApplyUser()+"<br>退货数量："+list.get(i).getReturnNumber());
		}

		json.setRows(list);
		json.setTotal(total);
		return json;
	}

	@Override
	public EasyUiJsonResult UpdaeReturnOrder(String ship,String ch,String money) {
		// TODO Auto-generated method stub
		EasyUiJsonResult json=new EasyUiJsonResult();
		double moneyd=Double.parseDouble(money.replaceAll(" ",""));
		Boolean bo;
		if ("1".equals(ch)){
			ch="线下补发";
			bo=this.lookReturnOrderServiceNewMapper.UpdaeReturnOrderCh(ship,ch);
		}else {
			bo = this.lookReturnOrderServiceNewMapper.UpdaeReturnOrder(ship,moneyd);
		}
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);	
		}
		return json;
	}

	@Override
	public EasyUiJsonResult RemReturnOrder(String ship,String cusorder) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo=this.lookReturnOrderServiceNewMapper.RemReturnOrder(ship,cusorder);
		if(bo){
			json.setMessage("0");
		}else {
			json.setMessage("0");
		}
		return json;
	}

	@Override
	public EasyUiJsonResult SetReturnOrder(String ship,String number) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		Boolean bo=this.lookReturnOrderServiceNewMapper.SetReturnOrder(ship,number);
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
		returndisplay listItemCount = new returndisplay();
		listOr=this.lookReturnOrderServiceNewMapper.getAllOrder(cusOrder);
		if (listOr.size()==0&&StringUtil.isNotBlank(tbOrder)){
			listItem=this.lookReturnOrderServiceNewMapper.getAllItem(tbOrder);
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
			returndisplay display=new returndisplay();
			display.setA1688Order(tbOrder);
			display.setCustomerorder(cusOrder);
			listOr.add(display);
			json.setRows1(listOr);
			json.setRows(listItem);
			return json;
		}
		for (int i = 0; i < listOr.size(); i++) {
			listItemCount=this.lookReturnOrderServiceNewMapper.FindItemCount(listOr.get(i).getA1688Order());
			if (listItemCount !=null&&listItemCount.getItemNumber()-listItemCount.getReturnNumber()<=0){
				listOr.remove(i);
				i--;
			}
		}
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
						System.out.println(listItem.size());
					}else {
						listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
						listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
					}					
				}
				json.setRows(listItem);
				return json;
			}else if(mid==1) {
				for (int g = 0; g < listOr.size(); g++) {
					listItem=this.lookReturnOrderServiceNewMapper.getAllItem(listOr.get(g).getA1688Order());
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
						listOr.remove(g);
						g--;
						
					}else {
						break;
					}
				}
				if (listOr.size()==0) {
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
			if (ls !=null && ls.getItemNumber()-ls.getReturnNumber()<=0) {
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
				list.get(j).setState(-1);
				Date currentTime = new Date();
				list.get(j).setApplyTime(df.format(new Date()));
				bo=this.lookReturnOrderServiceNewMapper.AddOrder(list.get(j));
				}
			}	
		}
		if (bo) {
			json.setRows(1);
			json.setFooter(df.format(new Date()));

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
				list.get(i).setState(-1);
				list.get(i).setReturnNumber(list.get(i).getItemNumber());
				Date currentTime = new Date();
				list.get(i).setApplyTime(df.format(new Date()));
				bo=this.lookReturnOrderServiceNewMapper.AddOrder(list.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			bo=false;
		}
		EasyUiJsonResult json=new EasyUiJsonResult();
		if (bo) {
		json.setRows(1);
		json.setFooter(df.format(new Date()));
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
	public EasyUiJsonResult AddOrderByOdid(int number, String tbId,
			String cusorder, String returnNO, String admName, int num,String goodsid,String pid) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		returndisplay re=new returndisplay();
		int orid=Integer.parseInt(tbId);
        returndisplay itnum=new returndisplay();
		 itnum=this.lookReturnOrderServiceNewMapper.FinditnumByPid(cusorder,pid);
		if (itnum !=null) {
			int itemNum=itnum.getItemNumber()-itnum.getReturnNumber();
			if (itemNum ==0) {
				json.setRows(1);
				return json;
			}
			if (itemNum<0) {
				json.setRows(4);
				return json;
			}
		}
		
		re=this.lookReturnOrderServiceNewMapper.FindReturndisplayBypid(cusorder,pid);
		if (re==null) {
		json.setRows("3");
		return json;
		}
		if (re.getTbId()==null) {
			re.setTbId(tbId);
		}
		
		re.setCustomerorder(cusorder);
		re.setApplyUser(admName);
		re.setApplyTime(df.format(new Date()));
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

	@Override
	public EasyUiJsonResult getpid(String cusorder, String pid) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		List<returndisplay> re = new ArrayList<returndisplay>();
		returndisplay itnum = new returndisplay();
		itnum = this.lookReturnOrderServiceNewMapper.FinditnumByPid(cusorder, pid);
		if (itnum != null) {
			int itemNum = itnum.getItemNumber() - itnum.getReturnNumber();
			if (itemNum <= 0) {
				json.setRows(0);
				return json;
			}
		}
		re=this.lookReturnOrderServiceNewMapper.FindReturndisplayByCusorder(cusorder,pid);
		if (re.size()==0) {
			json.setRows("1");
			return json;
		}
			return null;
		}

	@Override
	public String getAllOrderCount() {
		try {
			int count=this.lookReturnOrderServiceNewMapper.getAllOrderCount();
			return String.valueOf(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public orderJson getOrderByship(String shipno) {
        try {
        orderJson json=new orderJson();
        List<returndisplay> listItem = new ArrayList<returndisplay>();
        listItem=this.lookReturnOrderServiceNewMapper.getOrderByship(shipno);
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
                System.out.println(listItem.size());
            }else {
                listItem.get(j).setReturnReason("<input type='text' id='retu"+j+"' value='' >");
                listItem.get(j).setChangeShipno("<input type='text' id='num"+j+"' value='' >");
            }
        }
        json.setRows(listItem);
        return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public EasyUiJsonResult SetUpMoney(String ship, String number) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		double moneyd=Double.parseDouble(number.replaceAll(" ",""));
		Boolean bo=this.lookReturnOrderServiceNewMapper.SetUpMoney(ship,moneyd);
		if(bo){
			json.setRows(0);
		}else {
			json.setRows(1);
		}
		return json;
	}

	@Override
	public List<returndisplay> FindAllByTborder(String tborder) {
		try {
			List<returndisplay> item=this.lookReturnOrderServiceNewMapper.FindAllByTborder(tborder);
			for(int i=0;i<item.size();i++) {
                int re=0;
                try {
                    re=this.lookReturnOrderServiceNewMapper.FindRetuByTbid(item.get(i).getTbId());
                } catch (Exception e) {

                }
                if (re !=0){
                    int retu=item.get(i).getItemNumber()-re;
                    if (retu<=0){
                        item.remove(item.get(i));
                        i--;
                    }else {
                        item.get(i).setItemNumber(retu);
                    }
                }
            }
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public EasyUiJsonResult AddOtherOrder(List<returndisplay> list, String admName) {
		Boolean bo=false;
		try {
			for (int i = 0; i < list.size(); i++) {
				returndisplay re=this.lookReturnOrderServiceNewMapper.FindOrderByTbid(list.get(i).getTbId());
                re.setReturnNumber(list.get(i).getReturnNumber());
                re.setReturnReason(list.get(i).getReturnReason());
                re.setApplyUser(admName);
                re.setApplyTime(df.format(new Date()));
                re.setState(-1);
                bo=this.lookReturnOrderServiceNewMapper.AddOrder(re);
            }
		} catch (Exception e) {
			e.printStackTrace();
			bo=false;
		}
		EasyUiJsonResult json=new EasyUiJsonResult();
		if (bo) {
			json.setRows(1);
			json.setFooter(df.format(new Date()));
		}else {
			json.setRows(0);
		}
		return json;
	}

}
