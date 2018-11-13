package com.cbt.customer.servlet;

import ceRong.tools.bean.DorpDwonBean;
import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.DropShipBean;
import com.cbt.bean.StockNearbyBean;
import com.cbt.customer.service.IMoreActionService;
import com.cbt.customer.service.MoreActionServiceImpl;
import com.cbt.util.SplitPage;
import com.cbt.util.TranslationUtil;
import com.cbt.util.WebCookie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MoreActionServlet extends HttpServlet {
	private final static int PAGESIZE = 30;
	private static final long serialVersionUID = 1L;
	
	/**
	 * 方法描述:添加一条顾客订单记录
	 * author:zlw
	 * date:2015年11月03日
	 * @param request
	 * @param response
	 */
	public void addCustomOrder(HttpServletRequest request, HttpServletResponse response) {
		IMoreActionService ims = new MoreActionServiceImpl();
		String[] user =  WebCookie.getUser(request);
		String quantity = request.getParameter("quantity");
		String comment = request.getParameter("comment");
		String purl = request.getParameter("purl");
		String pname = request.getParameter("pname");
		String fprice = request.getParameter("fprice");
		String minOrder = request.getParameter("minOrder");
		String currency = request.getParameter("currency");
		try {
			PrintWriter out = response.getWriter();
			if(user != null && user.length > 0){//已经登陆过的用户
				CustomOrderBean cob = new CustomOrderBean();

				cob.setUserId(Integer.parseInt(user[0]));
				cob.setUserName(user[1]);
				cob.setEmail(user[2]);
				cob.setQuantity(quantity);
				cob.setComment(comment);
				cob.setPurl(purl);
				cob.setPname(pname);
				cob.setFprice(fprice);
				cob.setMinOrder(minOrder);
				cob.setCurrency(currency);
				int count = ims.addCustomOrder(cob);
				if(count > 0) {
					out.print(count);
				}
			}else {//没有登陆跳转到登陆的页面
				out.print(-1);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * 方法描述:添加一条顾客订单记录
	 * author:zlw
	 * date:2015年11月03日
	 * @param request
	 * @param response
	 */
	public void addStockNearby(HttpServletRequest request, HttpServletResponse response) {
		IMoreActionService ims = new MoreActionServiceImpl();
		String[] user =  WebCookie.getUser(request);
		String orderFrequency = request.getParameter("orderFrequency");
		String orderQuantity = request.getParameter("orderQuantity");
		String companyName = request.getParameter("companyName");
		String annualTurnover = request.getParameter("annualTurnover");
		String purl = request.getParameter("purl");
		String pname = request.getParameter("pname");
		String fprice = request.getParameter("fprice");
		String minOrder = request.getParameter("minOrder");
		String currency = request.getParameter("currency");
		if("".equals(annualTurnover) || null == annualTurnover){
			annualTurnover = "0";
		}
		try {
			PrintWriter out = response.getWriter();
			if(user != null && user.length > 0){//已经登陆过的用户
				StockNearbyBean snb = new StockNearbyBean();

				snb.setUserId(Integer.parseInt(user[0]));
				snb.setUserName(user[1]);
				snb.setEmail(user[2]);
				snb.setOrderFrequency(orderFrequency);
				snb.setOrderQuantity(orderQuantity);
				snb.setCompanyName(companyName);
				snb.setAnnualTurnover(Integer.parseInt(annualTurnover));
				snb.setPurl(purl);
				snb.setPname(pname);
				snb.setFprice(fprice);
				snb.setMinOrder(minOrder);
				snb.setCurrency(currency);
				int count = ims.addStockNearby(snb);
				if(count > 0) {
					out.print(count);
				}
			}else {//没有登陆跳转到登陆的页面
				out.print(-1);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * 方法描述:添加一条DropShip
	 * author:zlw
	 * date:2015年11月03日
	 * @param request
	 * @param response
	 */
	public void addDropShip(HttpServletRequest request, HttpServletResponse response) {
		IMoreActionService ims = new MoreActionServiceImpl();
		String[] user =  WebCookie.getUser(request);
		String purl = request.getParameter("purl");
		String pname = request.getParameter("pname");
		String fprice = request.getParameter("fprice");
		String minOrder = request.getParameter("minOrder");
		String currency = request.getParameter("currency");
		try {
			PrintWriter out = response.getWriter();
			if(user != null && user.length > 0){//已经登陆过的用户
				DropShipBean dsb = new DropShipBean();

				dsb.setUserId(Integer.parseInt(user[0]));
				dsb.setUserName(user[1]);
				dsb.setEmail(user[2]);
				dsb.setPurl(purl);
				dsb.setPname(pname);
				dsb.setFprice(fprice);
				dsb.setMinOrder(minOrder);
				dsb.setCurrency(currency);
				int count = ims.addDropShip(dsb);
				if(count > 0) {
					out.print(count);
				}
			}else {//没有登陆跳转到登陆的页面
				out.print(-1);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * 方法描述:查询StockNearby
	 * author:zlw
	 * date:2015年4月24日
	 */
	public void findAllStockNearby(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		String su = request.getParameter("userId");
		int userId = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("userId", su);
			userId = Integer.parseInt(su);
		}
		String userName = request.getParameter("userName");
		if(userName != null && !"".equals(userName)) {
			request.setAttribute("userName", userName);
		}
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<StockNearbyBean> findAll = ibs.findAllStockNearby(userId, userName, start, PAGESIZE,useremail);
		int total = ibs.total(userId, userName, start, PAGESIZE,useremail);
		SplitPage.buildPager(request, total, PAGESIZE, page);
		request.setAttribute("gbbs", findAll);
		try {
			request.getRequestDispatcher("/website/stocknearby.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询DropShip
	 * author:zlw
	 * date:2015年4月24日
	 */
	public void findAllDropShip(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		String su = request.getParameter("userId");
		int userId = 0;
		if(su != null && !"".equals(su)) {
			request.setAttribute("userId", su);
			userId = Integer.parseInt(su);
		}
		String userName = request.getParameter("userName");
		if(userName != null && !"".equals(userName)) {
			request.setAttribute("userName", userName);
		}
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<DropShipBean> findAll = ibs.findAllDropShip(userId, userName, start, PAGESIZE,useremail);
		int total = ibs.dropShiptotal(userId, userName, start, PAGESIZE,useremail);
		SplitPage.buildPager(request, total, PAGESIZE, page);
		request.setAttribute("gbbs", findAll);
		try {
			request.getRequestDispatcher("/website/dropship.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询CustomOrder
	 */
	public void findAllCustomOrder(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String type=request.getParameter("type");
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<CustomOrderBean> findAll = ibs.findAllCustomOrder(startdate, enddate, start, PAGESIZE,useremail,type);
		if(!findAll.isEmpty()){
			SplitPage.buildPager(request, findAll.get(0).getTotal(), PAGESIZE, page);
		}
		request.setAttribute("gbbs", findAll);
		request.setAttribute("type", type);
		request.setAttribute("useremail", useremail);
		request.setAttribute("startdate", startdate);
		request.setAttribute("enddate", enddate);
		try {
			request.getRequestDispatcher("/website/customorder.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询PriceMatch
	 */
	public void findAllPriceMatch(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String type=request.getParameter("type");
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<CustomOrderBean> findAll = ibs.findAllPriceMatch(startdate, enddate, start, PAGESIZE,useremail,type);
		if(!findAll.isEmpty()){
			SplitPage.buildPager(request, findAll.get(0).getTotal(), PAGESIZE, page);
		}
		request.setAttribute("gbbs", findAll);
		request.setAttribute("type", type);
		request.setAttribute("useremail", useremail);
		request.setAttribute("startdate", startdate);
		request.setAttribute("enddate", enddate);
		try {
			request.getRequestDispatcher("/website/pricematch.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询findAllNameYourPrice
	 */
	public void findAllNameYourPrice(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String type=request.getParameter("type");
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<CustomOrderBean> findAll = ibs.findAllNameYourPrice(startdate, enddate, start, PAGESIZE,useremail,type);
		if(!findAll.isEmpty()){
			SplitPage.buildPager(request, findAll.get(0).getTotal(), PAGESIZE, page);
		}
		request.setAttribute("gbbs", findAll);
		request.setAttribute("type", type);
		request.setAttribute("useremail", useremail);
		request.setAttribute("startdate", startdate);
		request.setAttribute("enddate", enddate);
		try {
			request.getRequestDispatcher("/website/nameyourprice.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询findAllCustomRfq
	 */
	public void findAllCustomRfq(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String type=request.getParameter("type");
		String useremail=request.getParameter("useremail");
		if(useremail!=null&&!useremail.equals("")){
			request.setAttribute("useremail", useremail);
		}
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<CustomOrderBean> findAll = ibs.findAllCustomRfq(startdate, enddate, start, PAGESIZE,useremail,type);
		if(!findAll.isEmpty()){
			SplitPage.buildPager(request, findAll.get(0).getTotal(), PAGESIZE, page);
		}
		request.setAttribute("gbbs", findAll);
		request.setAttribute("type", type);
		request.setAttribute("useremail", useremail);
		request.setAttribute("startdate", startdate);
		request.setAttribute("enddate", enddate);
		try {
			request.getRequestDispatcher("/website/customrfq.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:查询photo search
	 */
	public void customorSearchInfo(HttpServletRequest request, HttpServletResponse response) {
		String str = request.getParameter("page");
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//大类索引取得
  		List<DorpDwonBean> largeIndexList = ibs.getLargeIndexInfo();
  		request.setAttribute("largeIndexList", largeIndexList);
  		
		String type=request.getParameter("type");
		if(type!=null&&!type.equals("")){
			request.setAttribute("maxSelectId", type);
		}
		
		int page = 1;
		if(str != null) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGESIZE;
		List<CustomOrderBean> findAll = ibs.findAllCustomorSearch(startdate, enddate, start, PAGESIZE,type);
		if(!findAll.isEmpty()){
			SplitPage.buildPager(request, findAll.get(0).getTotal(), PAGESIZE, page);
		}
		request.setAttribute("cusSrearchList", findAll);
		request.setAttribute("startdate", startdate);
		request.setAttribute("enddate", enddate);
		try {
			request.getRequestDispatcher("/website/upload_search_info.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:更新客户发信状态
	 */
	public void customoreState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		
		ibs.updateCustomState(Integer.parseInt(userId),Integer.parseInt(type));
		
		PrintWriter out = response.getWriter();
		out.print("1");
		out.flush();
		out.close();

	}
	
	/**
	 * 
	 */
	public void findSearchKey(HttpServletRequest request, HttpServletResponse response) {
		
		IMoreActionService ibs = new MoreActionServiceImpl();
		
		String searchKey=request.getParameter("searchKey");
		
		List<CustomOrderBean> findAll = ibs.findSearchKey(searchKey);
		if(!findAll.isEmpty()){
			for(int i=0;i<findAll.size();i++){
				//翻译结果
				String enname = TranslationUtil.translation("0", findAll.get(i).getCatName());
				findAll.get(i).setProductName(enname);
			}
			
		}
		
		request.setAttribute("gbbs", findAll);
		request.setAttribute("searchKey", searchKey);
		try {
			request.getRequestDispatcher("/website/customorder.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
