package com.cbt.website.servlet2;

import com.cbt.website.server.GoodsServer;
import com.cbt.website.server.IGoodsServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 修改支付状态和订单状态
 */
public class UPOrdersPayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UPOrdersPayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderno = request.getParameter("orderno");
		IGoodsServer goodsServer = new GoodsServer();
		String update = goodsServer.upOrdersPay(orderno);
		PrintWriter out = response.getWriter();
		out.print(update);
		out.flush();
		out.close();
	}

}
