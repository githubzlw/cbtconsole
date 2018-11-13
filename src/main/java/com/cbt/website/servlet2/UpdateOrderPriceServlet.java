package com.cbt.website.servlet2;

import com.cbt.website.dao2.IWebsiteOrderDetailDao;
import com.cbt.website.dao2.WebsiteOrderDetailDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Servlet implementation class UpdateOrderStateServlet
 */
public class UpdateOrderPriceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateOrderPriceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String orderid=request.getParameter("orderid");
		double price2=Double.valueOf(request.getParameter("price"));//修改后的价格
		double price1=Double.valueOf(request.getParameter("price1"));//订单原价格
		double extra_discount=Double.valueOf(request.getParameter("extra_discount"));//手动优惠的原来的优惠额度
		double price3=price1-price2+extra_discount;
		 BigDecimal p = new BigDecimal(price3);
		 double price = p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
		int res=dao.websiteUpdateOrderPrice(orderid, price);
		PrintWriter out=response.getWriter();
		out.print(res);
		out.close();
	}

}