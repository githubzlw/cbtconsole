package com.cbt.method.servlet;

import com.cbt.method.service.OrderDetailsService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 测量仪器数据录入
 * @author 王宏杰
 * 2016-10-08
 *
 */
public class PackageParametersServlet extends HttpServlet {
	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(PackageParametersServlet.class);
	private static final long serialVersionUID = 1L;
       

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OrderDetailsService ods=new OrderDetailsServiceImpl();
		request.setCharacterEncoding("utf-8");
		String length1=request.getParameter("length1")!=null?request.getParameter("length1"):"";
		String length2=request.getParameter("length2")!=null?request.getParameter("length2"):"";
		String length3=request.getParameter("length3")!=null?request.getParameter("length3"):"";
		String weight=request.getParameter("weight")!=null?request.getParameter("weight"):"";
		String bagenum=request.getParameter("bagenum")!=null?request.getParameter("bagenum"):"";
		String orderid=request.getParameter("orderid")!=null?request.getParameter("orderid"):"";
		LOG.info("length1="+length1);
		LOG.info("length2="+length2);
		LOG.info("length3="+length3);
		LOG.info("weight="+weight);
		LOG.info("bagenum="+bagenum);
		LOG.info("orderid="+orderid);
		int row=ods.addPackgeParameters(length1, length2, length3, weight, bagenum, orderid);
		PrintWriter out = response.getWriter();
		if(row>0){
			LOG.info("-------------更新测量数据成功---------------");
			out.print("success");
		}else{
			LOG.info("-------------更新测量数据失败---------------");
			out.print("fail");
		}
		out.flush();
		out.close();
	}

}
