package com.cbt.website.servlet;

import com.cbt.website.bean.NoGoodsPushBean;
import com.cbt.website.service.INoGoodsPushService;
import com.cbt.website.service.NoGoodsPushService;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 无货源商品信息推送
* @author lyb
* @date 2016年8月25日
 */
public class NoGoodsPushServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private INoGoodsPushService gservice = new NoGoodsPushService();
	
	/**
	 * 查询无货源信息的商品  lyb  
	 */
	public void selectNoGoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<NoGoodsPushBean> list = gservice.selectNoGoods();
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONArray json = JSONArray.fromObject(list);
		out.write(json.toString());
		out.close();
	}
	
	/**
	 * 推送商品
	 */
	public void pushGoods(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String carUrl = request.getParameter("carUrl");
		int i = gservice.pushGoods(carUrl);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(i+"");
		out.close();
	}
	
}
