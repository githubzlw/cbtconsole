package com.cbt.dc.servlet;

import com.cbt.dc.service.DcServer;
import com.cbt.dc.service.DcServerImpl;
import com.cbt.parse.bean.SearchGoods;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@SuppressWarnings("serial")
public class PopProducts extends HttpServlet {
	
	public void getPopPro (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		DcServer dcserver= new DcServerImpl();
		String keywords = request.getParameter("keywords");
		String catList = request.getParameter("catList");
		String catid = request.getParameter("catid");
		List<SearchGoods> bgList = dcserver.getPopProducts(keywords, catList, catid);
		request.setAttribute("bgList", bgList);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/popProTest2.jsp");
		homeDispatcher.forward(request, response);
	}
	
	
}
