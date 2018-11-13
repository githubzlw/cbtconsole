package com.cbt.method.servlet;

import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class CanBuyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cart=request.getParameter("cart");
//		String admJson = Redis.hget(request.getSession().getId(), "admuser");//获取登录用户
// 	    Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
// 	    int adminid=user.getId();
 	   Map map=new HashMap();
		if(cart!=null && !cart.equals("") && cart.equals("taobao")){//获取淘宝货源
			map.put("523892333968", "4");
			map.put("533886200156", "3");
			map.put("531227252284", "7");
		}else if(cart!=null && !cart.equals("") && cart.equals("1688")){//获取1688货源
			map.put("1182832578", "2");
			map.put("535586321512", "4");
		}else{
			//错误
		}
		PrintWriter out;
		out = response.getWriter();
		out.print(JSONArray.fromObject(map).toString());
	}

}
