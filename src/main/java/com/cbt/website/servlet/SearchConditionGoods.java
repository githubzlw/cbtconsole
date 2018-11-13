package com.cbt.website.servlet;
/**
 * 条件查询
 */

import com.cbt.bean.Goods;
import com.cbt.util.Utility;
import com.cbt.website.service.GoodsServerImpl;
import com.cbt.website.service.IGoodsServer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SearchConditionGoods extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SearchConditionGoods() {
        super();
    }
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		int searchType=Integer.parseInt(request.getParameter("searchType"));
		int order=Integer.parseInt(request.getParameter("order"));
		String minpriceStr=request.getParameter("minprice");
		String maxpriceStr=request.getParameter("maxprice");
		double minprice=0;
		double maxprice=0;
		if(Utility.getStringIsNull(minpriceStr)){
			minprice=Double.parseDouble(minpriceStr);
		}else{
			minpriceStr="";
		}
		if(Utility.getStringIsNull(maxpriceStr)){
			maxprice=Double.parseDouble(maxpriceStr);
		}else{
			maxpriceStr="";
		}
		String condition=request.getParameter("condition");
		
		
		IGoodsServer goodsServer=new GoodsServerImpl();
		List<Goods> goodsList=null;
		if(searchType==1){
			//关键字
			goodsList=goodsServer.getSearchConditionList(condition.trim());
		}
		if(searchType==2){
			//升序 降序
			goodsList=goodsServer.getSearchOrderList(order, condition);
			
		}
		if(searchType==3){
			//价格范围
			goodsList=goodsServer.getSearchScopeList(order, minprice, maxprice, condition);
		}
		
		int goodTotal=0;
		if(goodsList!=null){
			for(int i=0;i<goodsList.size();i++){
				Goods goods = goodsList.get(i);
				request.setAttribute("id"+goodTotal, goods.getId());
				request.setAttribute("goodName"+goodTotal, goods.getGoodName());
				request.setAttribute("keyWord"+goodTotal, goods.getKeywords());
				request.setAttribute("price"+goodTotal, goods.getPrice());
				request.setAttribute("address"+goodTotal, goods.getAddress());
				goodTotal++;
			}
		}
		request.setAttribute("order", order);
		request.setAttribute("minprice", minpriceStr);
		request.setAttribute("condition", condition.trim());
		request.setAttribute("maxprice", maxpriceStr);
		request.setAttribute("goodsTotal", goodTotal);
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("view/goodslist.jsp");
		homeDispatcher.forward(request, response);
		goodsList=null;
		goodsServer=null;
	}
}
