package com.cbt.warehouse.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.orderJson;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.warehouse.service.LookReturnOrderServiceNew;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;

@Controller
@RequestMapping("/Look")
public class ReturnsManagement {
	@Autowired
	private LookReturnOrderServiceNew lookReturnOrderServiceNew;
	@RequestMapping(value = "/LookReturnOrder")
	@ResponseBody
	public EasyUiJsonResult LookReturnOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		String applyUser=request.getParameter("location_type");
		String State=request.getParameter("State");
		String a1688Shipno=request.getParameter("shipno");
		String optTimeStart=request.getParameter("optTimeStart");
		String optTimeEnd=request.getParameter("optTimeEnd");
		int mid=Integer.parseInt(request.getParameter("mid"));
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		if (mid==1) {
		State="5";	
		}
		json=this.lookReturnOrderServiceNew.FindReturndisplay(applyUser,State,a1688Shipno,optTimeStart,optTimeEnd,page,mid);
		 
		return json;
}
	@RequestMapping(value = "/UpdaeReturnOrder")
	@ResponseBody
	public EasyUiJsonResult UpdaeReturnOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		String ship= request.getParameter("ship");
		System.err.println("运单号"+ship);
		json=this.lookReturnOrderServiceNew.UpdaeReturnOrder(ship);
		 return json;
	}
	@RequestMapping(value = "/RemReturnOrder")
	@ResponseBody
	public EasyUiJsonResult RemReturnOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		String ship= request.getParameter("ship");
		System.err.println("运单号"+ship);
		json=this.lookReturnOrderServiceNew.RemReturnOrder(ship);
		 return json;
	}
	@RequestMapping(value = "/SetReturnOrder")
	@ResponseBody
	public EasyUiJsonResult SetReturnOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		String ship= request.getParameter("ship");
		Double number= Double.parseDouble(request.getParameter("number"));
		System.err.println("运单号"+ship);
		json=this.lookReturnOrderServiceNew.SetReturnOrder(ship,number);
		 return json;
	}
	@RequestMapping(value = "/UpdateReturnOrder")
	@ResponseBody
	public EasyUiJsonResult UpdateReturnOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		int ship=Integer.parseInt( request.getParameter("ship"));
		String number= request.getParameter("number");
		int mid=Integer.parseInt(request.getParameter("mid"));
		System.err.println("运单号"+number);
		json=this.lookReturnOrderServiceNew.UpdateReturnOrder(ship,number,mid,adm.getAdmName());
		 return json;
	}
	@RequestMapping(value = "/AddOrder")
	@ResponseBody
	public EasyUiJsonResult AddOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		int number;
		try {
			number = Integer.parseInt( request.getParameter("number"));
		} catch (NumberFormatException e) {
			number=0;
		}
		String orString=request.getParameter("orid");
		if (orString==null||"".equals(orString)) {
			orString="0";
		}
		int orid=Integer.parseInt(orString);
		System.err.println("订单号："+orid);
		String cusorder=request.getParameter("cusorder");
		String returnNO=request.getParameter("returnNO");
		if (returnNO==null||"".equals(returnNO)||number==0) {
			json.setRows(3);
			return json;
		}
		System.err.println("运单号"+number);
		json=this.lookReturnOrderServiceNew.AddOrder(number,orid,cusorder,returnNO,adm.getAdmName());
		 return json;
	}
	@RequestMapping(value = "/AddOrderByOdid")
	@ResponseBody
	public EasyUiJsonResult AddOrderByOdid(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		int number;
		try {
			number = Integer.parseInt( request.getParameter("number"));
		} catch (NumberFormatException e) {
			json.setRows(2);
			return json;
		}
		String goodsid=request.getParameter("goodsid");
		String cusorder=request.getParameter("cusorder");
		String odid=request.getParameter("odid");
		int num=Integer.parseInt(request.getParameter("num"));
		String returnNO=request.getParameter("returnNO");
		if (returnNO==null||"".equals(request)) {
			json.setRows(2);
			return json;
		}
		json=this.lookReturnOrderServiceNew.AddOrderByOdid(number,odid,cusorder,returnNO,adm.getAdmName(),num,goodsid);
		 return json;
	}
	@RequestMapping(value = "/LookOrder")
	@ResponseBody
	public EasyUiJsonResult LookOrder(HttpServletRequest request,HttpServletResponse response){
		 EasyUiJsonResult json=new EasyUiJsonResult();
		String tborid=request.getParameter("orderid");
		System.err.println("客户订单号——"+tborid);
		json=this.lookReturnOrderServiceNew.LookOrder(tborid);
		 return json;
	}
	@RequestMapping(value = "/getAllOrder")
	@ResponseBody
	public orderJson getAllOrder(HttpServletRequest request,HttpServletResponse response){
		orderJson json=new orderJson();
		String cusOrder=request.getParameter("cusOrder");
		String tbOrder=request.getParameter("tbOrder");
		int mid=Integer.parseInt(request.getParameter("mid"));
		System.err.println("客户订单号——"+cusOrder);
		List<returndisplay> result = new ArrayList<returndisplay>();
		json=this.lookReturnOrderServiceNew.getAllOrder(cusOrder,tbOrder,mid);		
		 return json;
	}
	@RequestMapping(value = "/getAllOrderByCu")
	@ResponseBody
	public orderJson getAllOrderByCu(HttpServletRequest request,HttpServletResponse response){
		orderJson json=new orderJson();
		String cusOrder=request.getParameter("cusOrder");
		System.err.println("单号："+cusOrder);
		json=this.lookReturnOrderServiceNew.getAllOrderByCu(cusOrder);		
		 return json;
	}

	@RequestMapping(value = "/AddAllOrder")
	@ResponseBody
	public EasyUiJsonResult AddAllOrder(@RequestParam("cusOrder") String re,HttpServletRequest request,HttpServletResponse response){
		EasyUiJsonResult json=new EasyUiJsonResult(); 
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String[] strArray = null;   
        strArray = re.split(",");
        String tbOrder=strArray[strArray.length-2];
        String cusOrder=strArray[strArray.length-1];
        List<String>list=new ArrayList<String>();
        List<returndisplay>items=new ArrayList<returndisplay>();
        int f=1;
        for (int i = 0; i < strArray.length-2; i++) {
        	returndisplay rep=new returndisplay();
         if (f%3==1&&"1".equals(strArray[i])) {
        	 rep.setA1688Order(tbOrder);
        	 if("".equals(strArray[i+2])||"".equals(strArray[i+1])){
         		json.setRows(3); 
         		return json;
         	 }
        	 rep.setReturnNumber(Integer.parseInt(strArray[i+2]));
        	 rep.setReturnReason(strArray[i+1]);
        	
        	 rep.setCustomerorder(cusOrder);
        	 rep.setItemNumber(i);
        	 rep.setApplyUser(adm.getAdmName());
        	 items.add(rep);
			}	
         f++;
		}
        if (items.size()<1) {
		json.setRows(2);
		return json;
		}
		List<returndisplay> result = new ArrayList<returndisplay>();
		json=this.lookReturnOrderServiceNew.AddAllOrder(items);		
		 return json;
	}
	@RequestMapping(value = "/AddRetAllOrder")
	@ResponseBody
	public EasyUiJsonResult AddRetAllOrder(@RequestParam("cusorder")String cusorder,@RequestParam("tbOrder")String tbOrder,@RequestParam("returnNO")String returnNO,HttpServletRequest request,HttpServletResponse response){
		EasyUiJsonResult json=new EasyUiJsonResult();
		 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		json=this.lookReturnOrderServiceNew.AddRetAllOrder(cusorder,tbOrder,returnNO,adm.getAdmName());		
		 return json;
	}	
	@RequestMapping(value = "/FindOdid")
	@ResponseBody
	public EasyUiJsonResult FindOdid(@RequestParam("cusorder")String cusorder){
		EasyUiJsonResult json=new EasyUiJsonResult();
		json=this.lookReturnOrderServiceNew.FindOdid(cusorder);		
		 return json;
	}	
	@RequestMapping(value = "/getAllOrderByOrid")
	@ResponseBody
	public EasyUiJsonResult getAllOrderByOrid(@RequestParam("orid")String orid){
		EasyUiJsonResult json=new EasyUiJsonResult();
		json=this.lookReturnOrderServiceNew.getAllOrderByOrid(orid);		
		 return json;
	}	
}