package com.cbt.email.servlet;

import com.cbt.email.entity.EmailReceive;
import com.cbt.email.service.EmailReceiveServiceImpl;
import com.cbt.email.service.IEmailReceiveService;
import com.cbt.email.util.SplitPage;
import com.cbt.messages.service.MessagesService;
import com.cbt.util.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EmailReceiveServlet extends HttpServlet {
	IEmailReceiveService service = new EmailReceiveServiceImpl();
	private static final long serialVersionUID = 1L;
	private MessagesService messagesService = null;
	private static final int  PAGE_SIZE=30;
	private static final Log LOG = LogFactory.getLog(EmailReceiveServlet.class);
	/**
	 * 方法描述:获取收件
	 * author:wy
	 * date:2017年3月7日
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void replyrecord(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String str = request.getParameter("page");
		int page = 1; //默认是第一页
		if(str != null && !"".equals(str)) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGE_SIZE;
	    String s = request.getParameter("id");
		int id = 0;
		if(s != null && !"".equals(s)) {
			id = Integer.parseInt(s);
		}
		
		 String s1 = request.getParameter("adminid");
			int adminid = 0;
			if(s1 != null && !"".equals(s1)) {
				adminid = Integer.parseInt(s1);
			}
		
		EmailReceive  er=new EmailReceive();
		er.setQuestionid(id);
		er.setStart(start);
		er.setEnd(PAGE_SIZE);
		List<EmailReceive> cuslist=service.getall(er);
		int total=service.getalltotal(er);
		SplitPage.buildPager(request, total, PAGE_SIZE, page);
		request.setAttribute("cuslist", cuslist);
		request.setAttribute("adminid", adminid);
		try {
			request.getRequestDispatcher("/website/replyrecord.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:获取其他客户留言
	 * author:wy
	 * date:2017年3月7日
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void replyrecord1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    String s = request.getParameter("userid");
		int id = 0;
		if(s != null && !"".equals(s)) {
			id = Integer.parseInt(s);
		}
		String str = request.getParameter("page");
		int page = 1; //默认是第一页
		if(str != null && !"".equals(str)) {
			page = Integer.parseInt(str);
		}
		int start = (page-1) * PAGE_SIZE;
		EmailReceive  er=new EmailReceive();
		er.setStart(start);
		er.setEnd(PAGE_SIZE);
		if(id!=1){ 
	    List<EmailReceive> cuslist=service.getall1(id,er);
		request.setAttribute("cuslist", cuslist);
		request.setAttribute("adminid", id);
		int total=service.getalltotal1(id,er);
		SplitPage.buildPager(request, total, PAGE_SIZE, page);
		}else{
			List<EmailReceive> cuslist=service.getall2(er);
			request.setAttribute("cuslist", cuslist);
			request.setAttribute("adminid", id);
			int total=service.getalltotal2(er);
			SplitPage.buildPager(request, total, PAGE_SIZE, page);
		}
		try {
			request.getRequestDispatcher("/website/replyrecord1.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法描述:获取其他客户留言
	 * author:wy
	 * date:2017年3月7日
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void reply(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    String s = request.getParameter("id");
		int id = 0;
		if(s != null && !"".equals(s)) {
			id = Integer.parseInt(s);
		}
		String s1 = request.getParameter("adminid");
		int adminid = 0;
		if(s1 != null && !"".equals(s1)) {
			adminid = Integer.parseInt(s1);
		}
		//获取登录用户
      /*String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
	  Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
	  System.out.print(adm);
      int adminid = adm.getId();*/
		EmailReceive er=service.getEmail(id);
		request.setAttribute("er", er);
		request.setAttribute("userId", adminid);
		try {
			request.getRequestDispatcher("/website/reply_question1.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 方法描述:给客户发邮件
	 * author:wy
	 * date:2017年3月8日
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void reply1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String s = request.getParameter("id");
		int id = 0;
		if(s != null && !"".equals(s)) {
			id = Integer.parseInt(s);
		}
		
		String content = request.getParameter("replyContent");
		String title = request.getParameter("title");
		String email = request.getParameter("email");
		String userId = request.getParameter("userId");
		messagesService = SpringContextUtil.getBean("MessagesService",MessagesService.class);
		try {
			PrintWriter out = response.getWriter();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			Date now = new Date();
			dateFormat.setLenient(false);
			Date parse = dateFormat.parse(dateFormat.format(now));
			java.sql.Date date = new java.sql.Date(parse.getTime());
			int count = service.reply(id,Integer.parseInt(userId), content,date,email,title);
			String res = date.toString();
			if(count > 0) {
					//同时将消息列表的信息改为已完结
				   /* Messages messages = new Messages();
				    messages.setType(CommonConstants.PROPAGEMESSAGE);
				    messages.setEventid(id);
		   			int rows = messagesService.deleteByPrimaryKey(messages);
		   			LOG.info("消息列表修改状态："+rows);*/
					out.println(res);
					out.flush();
					out.close();
			}else {
				out.println(-1);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	}
	/**
	 * 方法描述:获取其他客户留言
	 * author:wy
	 * date:2017年3月7日
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 *//*
	public void delreply(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String s = request.getParameter("userid");
		int id = 0;
		if(s != null && !"".equals(s)) {
			id = Integer.parseInt(s);
		}
		if(id!=1){ 
			EmailReceive  er=new EmailReceive();
			
			List<EmailReceive> cuslist=service.getall1(id);
			request.setAttribute("cuslist", cuslist);
		}else{
			List<EmailReceive> cuslist=service.getall2();
			request.setAttribute("cuslist", cuslist);	
		}
		try {
			request.getRequestDispatcher("/website/replyrecord1.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	
	

