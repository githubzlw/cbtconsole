package com.cbt.processes.servlet;

import com.cbt.bean.InquiryDetail;
import com.cbt.bean.SpiderBean;
import com.cbt.processes.service.IInquiryServer;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.InquiryServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.util.WebCookie;

import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author ylm
 * 询价操作
 *
 */
public class Inquiry extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(Inquiry.class);

	/**
	 * ylm
	 * 添加询价支付信息
	 */
	protected void saveInquiry(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(req);
		PrintWriter out = resp.getWriter();
		int userid = 0;
		if(userinfo != null){
			userid = Integer.parseInt(userinfo[0]);
		}
		String goodsid_req = req.getParameter("inquiryid");
		LOG.warn(goodsid_req);
		String[] goodsid = null;
		if(goodsid_req != null){
			goodsid = goodsid_req.split("@");
		}
		String payno = req.getParameter("payno");
		LOG.warn("询价支付金额："+goodsid.length*50);
		double server_ = goodsid.length*50;
		IInquiryServer server = new InquiryServer();
		int res = server.saveInquiry(userid,payno,server_,2, goodsid);
		if(res>0){
			//修改购物车的数量、
			/*Cookie cartNumber = WebCookie.getCookieByName(req, "cartNumber") ;
			if(cartNumber != null){
				int cartNumbers = Integer.parseInt(cartNumber.getValue())-goodsid.length;
				cartNumber.setValue(cartNumbers+"");
			}*/
			Cookie cookie = WebCookie.getCookieByName(req, "cartNumber") ;
			if(cookie != null){
				int cartNumbers = Integer.parseInt(cookie.getValue())-goodsid.length;
				cookie.setValue(cartNumbers+"");
			}
		}
		out.print(res>0?payno:"0");
		out.flush();
		out.close();
		
	}
	
	/**
	 * ylm
	 * 获取商品信息
	 */
	protected void getInquiryGoods(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo == null){
			resp.sendRedirect("cbt/geton.jsp");
			return ; 
		}
		String goodsid_req = request.getParameter("id");
		LOG.warn(goodsid_req);
		String[] goodsid = null;
		if(goodsid_req != null){
			goodsid = goodsid_req.split("@");
		}
		ISpiderServer spiderServer = new SpiderServer();
		List<SpiderBean> res = spiderServer.getInquiryGoods(goodsid);
		request.setAttribute("spiderBean", res);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/inquiry.jsp");
		homeDispatcher.forward(request, resp);
		
	}
	
	/**
	 * 修改询价表状态：0-未询价，1-已询到价格，2-商品无效
	 * 
	 * @param user
	 * 	用户信息
	 */
	public void upInquiryDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userid = req.getParameter("userid");
		String id = req.getParameter("id");
		String state = req.getParameter("state");
		String price = req.getParameter("price");
		IInquiryServer server = new InquiryServer();
		double prices = 0;
		if(price != null){
			prices = Double.parseDouble(price);
		}
		int res = server.upInquiryDetail(Integer.parseInt(userid),Integer.parseInt(id), Integer.parseInt(state), prices);
		PrintWriter out = resp.getWriter();
		out.print(res > 0 ? true : false);
		out.flush();
		out.close();
	}

	/**
	 * 购物车跳到询价支付界面
	 * 
	 * @param user
	 * 	用户信息
	 */
	public void getToCartInquiryInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String inquiryId = request.getParameter("id");
		IInquiryServer server = new InquiryServer();
		List<InquiryDetail> lisDetails = server.getInquiry(inquiryId);
		request.setAttribute("inquiry", lisDetails);
			RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/inquiry.jsp");
			homeDispatcher.forward(request, response);
	}
	
	/**
	 * 查找询价表
	 * 
	 * @param user
	 * 	用户信息
	 */
	public void getInquiryInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userid_req = request.getParameter("userid");
		String state_req = request.getParameter("state");
		IInquiryServer server = new InquiryServer();
		int userid = 0,state = 0;
		
		if(userid_req != null){
			userid = Integer.parseInt(userid_req);
		}
		if(state_req != null){
			state = Integer.parseInt(state_req);
		}
		List<InquiryDetail> lisDetails = server.getInquiry(state, userid);
		request.setAttribute("individual", lisDetails);
			RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/individual-inquiry.jsp");
			homeDispatcher.forward(request, response);
	}
	
	public void getInquiryInfoWebsite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String state_req = request.getParameter("state");
		IInquiryServer server = new InquiryServer();
		int userid = 0,state = 0;
		if(state_req != null){
			state = Integer.parseInt(state_req);
		}
		List<InquiryDetail> lisDetails = server.getInquiry(state, userid);
		request.setAttribute("inquiry", lisDetails);
		request.setAttribute("state", state);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/inquiry.jsp");
		homeDispatcher.forward(request, response);
		
	}
	
	//删除询价表ylm
	public void delOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String payno = request.getParameter("payno");
		IInquiryServer server = new InquiryServer();
		int res = server.delInquiry(payno);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
}
