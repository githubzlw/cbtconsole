package com.cbt.processes.servlet;

import com.cbt.bean.ClassDiscount;
import com.cbt.bean.PreferentialWeb;
import com.cbt.bean.SpiderBean;
import com.cbt.processes.service.IPreferentialServer;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.PreferentialServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.util.Application;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Preferential {

	//保存用户申请优惠折扣信息
	public void savePreferential(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] userinfos = WebCookie.getUser(request);
		String url = request.getParameter("purl"); 
		String country = request.getParameter("country");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String quantity = request.getParameter("quantity");
		String sprice = request.getParameter("sprice");
		String note = request.getParameter("note");
		String pGoodsUnit = request.getParameter("pGoodsUnit");
		String discount = request.getParameter("discount");
		String discountedUnitPrice = request.getParameter("discountedUnitPrice");
		String totalprice = request.getParameter("totalprice");
		String shipping = request.getParameter("shipping");
		int userid=0;
		PrintWriter out = response.getWriter();
		String sessionId = null;
		if(userinfos != null){
			userid=Integer.parseInt(userinfos[0]);
			username = userinfos[1];
		}else{
		    sessionId = Goods.getSessionId(request, response);
		}
		com.cbt.bean.Preferential preferential = new com.cbt.bean.Preferential();
		preferential.setNumber(Integer.parseInt(quantity));
		preferential.setUserid(userid);
		preferential.setCountry(country);
		preferential.setEmail(email);
		preferential.setUsername(username);
		preferential.setNote(note);
		preferential.setSessionid(sessionId);
		preferential.setpGoodsUnit(pGoodsUnit);
		preferential.setDiscount(discount+"%");
		preferential.setDiscountedUnitPrice(discountedUnitPrice);
		preferential.setTotalprice(totalprice);
		preferential.setShipping(shipping);
		double sprice_ = 0;
		if(Utility.getStringIsNull(sprice)){
			if(sprice.indexOf("-") > 0){
				sprice_ = Double.parseDouble(sprice.split("-")[1]);
			}else{
				sprice_ = Double.parseDouble(sprice);
			}
		}
		preferential.setSprice(sprice_);
		IPreferentialServer preferentialServer = new PreferentialServer();
		int res = preferentialServer.savePreferential(preferential,url);
		out.print(res);
		out.flush();
		out.close();
	}
	 
	 //取消优惠交互信息
   	protected void  cancelPainteracted(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		int pid = Integer.parseInt(request.getParameter("pid"));
   		String cancel_reason = request.getParameter("cancel_reason");
		String[] userinfos = WebCookie.getUser(request);
		if(userinfos == null){
			response.sendRedirect("cbt/geton.jsp");
			return ;
		}
   		IPreferentialServer server = new PreferentialServer();
   		int res = server.delPaprestrain(pid, cancel_reason);
   		PrintWriter out = response.getWriter();
   		out.print(res);
   		out.flush();
   		out.close();
   	}
   	
    //获取优惠交互信息
   	protected void  getPainteracteds(HttpServletRequest request,
   			HttpServletResponse response) throws ServletException, IOException, ParseException {
   		String stateString = request.getParameter("state");
   		String pageString = request.getParameter("page");
   		int userid=0;
		String[] userinfos = WebCookie.getUser(request);
		if(userinfos != null){
			userid=Integer.parseInt(userinfos[0]);
		}else{
			response.sendRedirect("cbt/geton.jsp");
			return ;
		}
		int state = 2;
		int page = 1;
		if (Utility.getStringIsNull(pageString)) {
			page = Integer.parseInt(pageString);
		}
		if (Utility.getStringIsNull(stateString)) {
			state = Integer.parseInt(stateString);
		}
   		IPreferentialServer server = new PreferentialServer();
   		List<PreferentialWeb> res = server.getPreferentials(userid, state, page);
   		int count = server.getPreferentials(userid, state);
   		int counts = (int)Math.ceil(count/8);
   		request.setAttribute("pre", res);
   		request.setAttribute("state", state);
   		request.setAttribute("page", page);
   		request.setAttribute("count", counts == 0? 1:counts);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/preferential.jsp");
		homeDispatcher.forward(request, response);
   	}
   	
   	/**
	 * 用户邮件中批量优惠折扣点击链接
	 */
	protected void sendcart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		request.setAttribute("uid", uid);
		String[] user =  WebCookie.getUser(request);
		if(user == null){
			 response.sendRedirect("Geton?uid="+uid);
			 return;
		}
		IPreferentialServer server = new PreferentialServer();
   		List<PreferentialWeb> res = server.getPreferentials(uid,Integer.parseInt(user[0]));
   		request.setAttribute("pre", res);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/sendtocart.jsp");
		homeDispatcher.forward(request, response);
	}
	
	/**
	 * 批量优惠商品添加到用户购物车-链接进入
	 */
	protected void savetocart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		request.setAttribute("uid", uid);
		PrintWriter out = response.getWriter();
		IPreferentialServer spiderServer = new PreferentialServer();
		String[] userinfo =  WebCookie.getUser(request);
		if(userinfo == null){
			 response.sendRedirect("Geton?uid="+uid);
			 return;
		}
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		//获取汇率
		Map<String, Double> maphl = Currency.getMaphl(request);
		int res = spiderServer.addPa_Googs_car(uid, Integer.parseInt(userinfo[0]),0,currency1,maphl);
		out.print(res);
		out.flush();
		out.close();
	}
	
	/**
	 * 批量优惠商品添加到用户购物车-个人中心进入
	 */
	protected void savetocart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("ids");
		PrintWriter out = response.getWriter();
		IPreferentialServer spiderServer = new PreferentialServer();
		String[] userinfo =  WebCookie.getUser(request);
		if(userinfo == null){
			 response.sendRedirect("Geton?uid="+uid);
			 return;
		}
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		//获取汇率
		Map<String, Double> maphl = Currency.getMaphl(request);
		int res = spiderServer.addPa_Googs_car(uid, Integer.parseInt(userinfo[0]),1,currency1,maphl);
		out.print(res);
		out.flush();
		out.close();
	}
	
	/**
	 * 获取类别混批折扣页面数据显示 spider.jsp
	 */
	protected void getClass_discount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object shopcar = session.getAttribute("shopcar");
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		if(shopcar != null){
			list = (List<SpiderBean>) shopcar;
		}else{
			 String[] ub = WebCookie.getUser(request);
			 ISpiderServer is = new SpiderServer();
			 if(ub != null){
				 list = is.getGoogs_cars(null, Integer.parseInt(ub[0]), 0);
			 }else{
				 list = is.getGoogs_cars(Goods.getSessionId(request, response), 0, 0);
			 }
			 session.setAttribute("shopcar",list);
		}
		//获取混批折扣率
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		list_cd = Application.getClassDiscount(request);
		List<ClassDiscount> list_cd1 = new ArrayList<ClassDiscount>();//免邮
		for (ClassDiscount classDiscount : list_cd) {
			ClassDiscount cd = new ClassDiscount();
			cd.setClassname(classDiscount.getClassname());
			cd.setDeposit_rate(classDiscount.getDeposit_rate());
			cd.setG_price(classDiscount.getG_price());
			cd.setId(classDiscount.getId());
			cd.setPrice(classDiscount.getPrice());
			cd.setShowname(classDiscount.getShowname());
			cd.setSum_price(classDiscount.getSum_price());
			list_cd1.add(cd);
		}
		boolean f = false;
		double s_price = 0; 
		for (int i = 0; i < list.size(); i++) {
			SpiderBean sb = list.get(i);
			for (int j = 0; j < list_cd.size(); j++) {
				ClassDiscount cd = list_cd.get(j);
				if(cd.getId() == sb.getGoods_class() && Double.parseDouble(sb.getPrice()) <= 150){
					f = true;
					BigDecimal b2 = new BigDecimal(cd.getSum_price()+sb.getNumber()*Double.parseDouble(sb.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP);
					cd.setSum_price(b2.doubleValue());
					break;
				}else{
					s_price += sb.getNumber()*Double.parseDouble(sb.getPrice());
				}
			}
		}
 		for (int k = 0; k < list_cd.size(); k++) {
			ClassDiscount cd = list_cd.get(k);
			if(cd.getPrice() < cd.getSum_price()){
				BigDecimal b2 = new BigDecimal(cd.getSum_price()*(1-cd.getDeposit_rate())).setScale(2, BigDecimal.ROUND_HALF_UP);
				cd.setG_price(b2.doubleValue());
			}
		}
		request.setAttribute("classDiscount", f?list_cd:null);
		BigDecimal b1 = new BigDecimal(s_price).setScale(2, BigDecimal.ROUND_HALF_UP);
		request.setAttribute("s_price", b1);
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		request.setAttribute("currency", currency1);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/class_discount.jsp");
		homeDispatcher.forward(request, response);
	}
	
	/**
	 * 获取类别混批折扣页面数据显示shop_car.jsp
	 */
	protected void getClass_discount_car(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] classprice = request.getParameter("classprice").split("@");
		//获取混批折扣率
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		list_cd = Application.getClassDiscount(request);
		
		for (int k = 0; k < list_cd.size(); k++) {
			ClassDiscount cd = list_cd.get(k);
			double sum_price = Double.parseDouble(classprice[k]);
			cd.setSum_price(sum_price);
			if(cd.getPrice() < sum_price){
				BigDecimal b2 = new BigDecimal(sum_price-sum_price*cd.getDeposit_rate()).setScale(2, BigDecimal.ROUND_HALF_UP);
				cd.setG_price(b2.doubleValue());
			}
		}
		request.setAttribute("classDiscount", list_cd);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/class_discount.jsp");
		homeDispatcher.forward(request, response);
	}
}
