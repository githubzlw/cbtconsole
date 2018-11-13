package com.cbt.processes.servlet;

import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 选择金额进行变更
 */
public class Currency {

	 
	@SuppressWarnings("unchecked")
	public static Map<String, Double> getMaphl(HttpServletRequest request){
//		HttpSession session = request.getSession();
//		//获取汇率
//		Map<String, Double> maphl = null;
//		Object obj = session.getAttribute("exchangeRate");
		
		Map<String, Double> maphl = null;
		ServletContext application=request.getSession().getServletContext(); 
		Object obj = application.getAttribute("exchangeRate");
		if(obj == null){
			ISpiderServer spider = new SpiderServer();
			maphl = spider.getExchangeRate();
//			request.getSession().setAttribute("exchangeRate", maphl);
			request.getSession().getServletContext().setAttribute("exchangeRate", maphl);
		}else{
			maphl = (Map<String, Double>) obj;
		}
		maphl.put(null, (double) 1);
		return maphl;
	}
}
