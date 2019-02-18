package com.cbt.warehouse.ctrl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cbt.util.Redis;
import com.cbt.warehouse.pojo.ReturnRes;
import com.cbt.warehouse.pojo.Returndetails;
import com.cbt.warehouse.pojo.Returnresult;
import com.cbt.warehouse.service.AddReturnOrderService;
import com.cbt.warehouse.service.LookReturnOrderService;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.EasyUiJsonResult;
import com.ibm.icu.text.SimpleDateFormat;

@Controller
@RequestMapping("/AddReturnOrder")
public class AddReturnOrder {
@Autowired
private AddReturnOrderService addReturnOrderService;
@Autowired
private LookReturnOrderService lookReturnOrderService;
@RequestMapping(value = "/AddOrder",method = RequestMethod.POST)
public Returnresult AddOrder(HttpServletRequest request,HttpServletResponse response){
	 Returndetails  returndetails=new Returndetails();
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 String cusorder=request.getParameter("cusorder");//获取客户订单
	 String purNum=request.getParameter("purNum");//获取采购订单数量
	 String tborder=request.getParameter("tborder");//获取采购订单号
	 String purSou=request.getParameter("purSou");//获取采购订单来源： 
	 String Waybill=request.getParameter("Waybill");//获取采购运单号
	 String purManey=request.getParameter("purManey");//获取采购金额
	 String returnOrder=request.getParameter("returnOrder");//获取退货运单号
	 String Warehouse=request.getParameter("Warehouse");//获取所在仓库
	 String returnNum=request.getParameter("returnNum");//获取退货数量
	 String returnMoney=request.getParameter("returnMoney");//获取退货金额
	 String ordeerPeo=request.getParameter("ordeerPeo");//获取订单采购人
	 String orderSale=request.getParameter("orderSale");//获取订单关联销售
	 String returnApply=request.getParameter("returnApply");//获取退货申请人
	 String returnReason=request.getParameter("returnReason");//获取退货原因说明
	 String PlaceDate=request.getParameter("PlaceDate");//获取下单时间
	 String deliveryDate=request.getParameter("deliveryDate");//获取发货时间
	 returndetails.setCusorder(cusorder);
	 returndetails.setPlaceDate(PlaceDate);
	 returndetails.setDeliveryDate(deliveryDate);
	
	 returndetails.setOrdeerPeo(ordeerPeo);
	 returndetails.setOrderSale(orderSale);
	 returndetails.setPurManey(Double.parseDouble(purManey));
	 returndetails.setPurNum(Integer.parseInt(purNum));
	 returndetails.setPurSou(purSou);
	 returndetails.setReturnApply(returnApply);
	 returndetails.setReturnMoney(Double.parseDouble(returnMoney));
	 returndetails.setReturnNum(Integer.parseInt(returnNum));
	 returndetails.setReturnOrder(returnOrder);
	 returndetails.setReturnReason(returnReason);
	 returndetails.setReturnState("申请退货");
	 returndetails.setTborder(tborder);
	 returndetails.setWarehouse(Warehouse);
	 returndetails.setWaybill(Waybill);
	Returnresult returnresult=new Returnresult();
	Boolean b=this.lookReturnOrderService.AddOrder(returndetails);
//	String ste="";
//	if (b) {
//		ste="保存失败";
//	}else {
//		ste="保存成功";
//	}
	try {
		response.sendRedirect("/cbtconsole/AddReturnOrder/LookReturnOrder?state="+returndetails.getReturnState());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return returnresult;
}
@RequestMapping(value="/LookReturnOrder", method = RequestMethod.GET)
public ModelAndView LookReturnOrder(HttpServletRequest request,HttpServletResponse response){
	 String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
	 System.err.println("用户名是："+admuserJson);
	  ModelAndView mv = new ModelAndView("ReturnSystem");
	  ModelAndView mvApply = new ModelAndView("ReturnApply");
	  
     if (StringUtil.isBlank(admuserJson)) {
         mv.addObject("success", 0);
         mv.addObject("message", "请登录后操作");
         mvApply.addObject("success", 0);
         mvApply.addObject("message", "请登录后操作");
         return mv;
     }
	String mid=request.getParameter("mid");
	String admuserid=request.getParameter("admuserid");
	String state=request.getParameter("state");
	String orderNum=request.getParameter("orderNum");
	String pa=request.getParameter("page");
	String ste=request.getParameter("ste");
	if (pa==null||"".equals(pa)) {
		pa="1";
	}
	System.err.println("当前是"+pa);
	int page = Integer.parseInt(pa);
	
	 int page1=page>0?((page - 1) * 50):page;
	 List<ReturnRes> rets=this.lookReturnOrderService.SelectReturnOrder(admuserid,state,orderNum,page1,mid);
	 int total=this.lookReturnOrderService.countPage(admuserid,state,orderNum,page1,mid);
	 int totalpage=(total-1)/50+1;
	 System.err.println("当前是"+totalpage);
	mv.addObject("rets",rets);
	mv.addObject("ste",ste);
	mv.addObject("state",state);
	mv.addObject("totalpage",totalpage);
	mv.addObject("total",total);
	mv.addObject("page",page);
	mv.addObject("admuserid",admuserid);
	mvApply.addObject("page",page);
	mvApply.addObject("admuserid",admuserid);
	mvApply.addObject("totalpage",totalpage);
	mvApply.addObject("total",total);
	mvApply.addObject("rets",rets);
	mvApply.addObject("ste",ste);
	mvApply.addObject("state",state);
	if ("退货申请".equals(state)) {
		return mvApply;
	}
	return mv;
}
@RequestMapping("/UpdataReturnOrder/{order}/{returnState}/{sta}/{returnOrder}/{company}")
public ModelAndView UpdataReturnOrder(@PathVariable("order")String order,@PathVariable("returnState")String returnState,@PathVariable("sta")int sta,
		@PathVariable("returnOrder")String returnOrder,@PathVariable("company")String company){
	
	 ModelAndView mvApply = new ModelAndView("ReturnApply");
	Boolean f=this.lookReturnOrderService.UpdataReturnOrder(order,returnState,sta,returnOrder,company);
	String string="";
	if (f) {
		string="操作成功";
	}else {		
		string="操作失败";
	}

	String orderNum=null;
	String admuserid=null;
	String mid=null;
	int page = 1;
	 List<ReturnRes> rets=this.lookReturnOrderService.SelectReturnOrder(admuserid,returnState,orderNum,page,mid);
	 mvApply.addObject("string",string);
	 mvApply.addObject("rets",rets);
	 mvApply.addObject("state",returnState);
	 return mvApply;
}
@RequestMapping("/RemReturnOrder/{order}/{returnState}/{sta}")
public ModelAndView RemReturnOrder(@PathVariable("order")String order,@PathVariable("returnState")String returnState,@PathVariable("sta")int sta){
	 ModelAndView mvApply = new ModelAndView("ReturnApply");
	Boolean f=this.lookReturnOrderService.RemReturnOrder(order,sta);
	String string="";
	if (f) {
		string="操作成功";
	}else {
		string="操作失败";
	}
	returnState="退货申请";
	String admuserid=null;
	String orderNum=null;
	String mid=null;
	int page = 1;
	 List<ReturnRes> rets=this.lookReturnOrderService.SelectReturnOrder(admuserid,returnState,orderNum,page,mid);
	 mvApply.addObject("string",string);
	 mvApply.addObject("rets",rets);
	 mvApply.addObject("state",returnState);
	 return mvApply;
}
@RequestMapping("/FindReturnOrder/{tborder}")
public ModelAndView FindReturnOrder(@PathVariable("tborder")String tborder,HttpServletRequest request,HttpServletResponse response){
	 
	  ModelAndView mv = new ModelAndView("ReturnAU");
	  ReturnRes rets=this.lookReturnOrderService.FindReturnOrder(tborder);
	  if (rets==null) {
		mv.addObject("addString","该客户没有可退定单或订单已发起退货");
	}
	mv.addObject("rets",rets);
	return mv;
}
@RequestMapping("/AddtbOrder/{cusorder}/{returnApply}/{returnReason}/{returnMoney}/{tborder}/{warehouse}/{ordeerPeo}")
public ModelAndView AddReturntbOrder(@PathVariable("cusorder")String cusorder,@PathVariable("returnApply")String returnApply,
		@PathVariable("returnReason")String returnReason,@PathVariable("returnMoney")Double returnMoney,@PathVariable("tborder")String tborder,
		@PathVariable("warehouse")String warehouse,@PathVariable("ordeerPeo")String ordeerPeo){
	  ModelAndView mv = new ModelAndView("ReturnAU");
	  Boolean bo=this.lookReturnOrderService.AddReturntbOrder(cusorder,returnApply,returnReason,returnMoney,tborder,ordeerPeo,warehouse);
	  System.err.println("新增状态"+bo);
	  String addString="订单不存在或者订单已发起退货";
	  if (bo) {	  
		  addString="退货申请成功";
	}
	  ReturnRes rets=this.lookReturnOrderService.FindReturnOrder(cusorder);
	  mv.addObject("addString", addString);
	  mv.addObject("rets",rets);
	return mv;
}

}
