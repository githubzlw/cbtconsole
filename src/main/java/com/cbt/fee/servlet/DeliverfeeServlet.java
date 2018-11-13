package com.cbt.fee.servlet;

import com.cbt.fee.service.DeliveryFeeServer;
import com.cbt.fee.service.IDeliveryFeeServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DeliverfeeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void displayFee(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		IDeliveryFeeServer idfs = new DeliveryFeeServer();
//		/*获取用户id*/
		//UserBean us = (UserBean) req.getSession().getAttribute("userInfo");
		//System.out.println(req.getSession().getAttribute("userInfo"));
		/*获取的数据返回页面*/
		PrintWriter out = resp.getWriter();
		out.print(idfs.getAllDeliveryFee());
	}	
}
