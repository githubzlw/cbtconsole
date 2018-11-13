package com.cbt.website.servlet;
/**
 * 获取商品列表
 */

import com.cbt.bean.Goods;
import com.cbt.website.service.GoodsServerImpl;
import com.cbt.website.service.IGoodsServer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetGoodsList extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public GetGoodsList() {
        super();
    }
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		IGoodsServer goodsServer=new GoodsServerImpl();
		List<Goods> goodsList = goodsServer.getGoodsList();
		int goodTotal=0;
		for(int i=0;i<goodsList.size();i++){
			Goods goods = goodsList.get(i);
			request.setAttribute("id"+goodTotal, goods.getId());
			request.setAttribute("goodName"+goodTotal, goods.getGoodName());
			request.setAttribute("keyWord"+goodTotal, goods.getKeywords());
			request.setAttribute("price"+goodTotal, goods.getPrice());
			request.setAttribute("address"+goodTotal, goods.getAddress());
			goodTotal++;
		}
		request.setAttribute("condition", "");
//		order =0 升序
		request.setAttribute("order", -1);
		request.setAttribute("minprice", "");
		request.setAttribute("maxprice", "");
		request.setAttribute("goodsTotal", goodTotal);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("view/goodslist.jsp");
		homeDispatcher.forward(request, response);
		goodsList=null;
		goodsServer=null;
	}

}
