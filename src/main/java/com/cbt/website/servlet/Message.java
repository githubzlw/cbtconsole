package com.cbt.website.servlet;

import com.cbt.util.WebCookie;
import com.cbt.website.service.IMessageServer;
import com.cbt.website.service.MessageServer;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 用户消息
 */
public class Message extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Message() {
        super();	
    }

    //获取用户消息
	protected void getMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String state_req = request.getParameter("state");
		 IMessageServer server = new MessageServer();
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo != null){
			int state = -1;
			if(state_req!=null && !state_req.equals("")){
				state = Integer.parseInt(state_req);
			}
			int userid = Integer.parseInt(userinfo[0]);
			List<com.cbt.bean.Message> mes = server.getMessage(userid, state);
			request.setAttribute("count", server.getMessageSize(userid));
			request.setAttribute("message", mes);
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/SystemMessage.jsp");
			homeDispatcher.forward(request, response);
		}else{
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}
	}

	 //获取用户未读消息数量
	protected void getMessageNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
		 IMessageServer server = new MessageServer();
		if(userinfo != null){ 
			int mes = server.getMessageSize(Integer.parseInt(userinfo[0]));
			request.setAttribute("count", mes);
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}else{
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}
	}
	

	/**
	 * 删除用户消息
	 * 
	 * @param messgeId
	 * 		消息ID
	 */
	public void delMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String messageID = request.getParameter("messageID");
		String[] userinfo = WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		 IMessageServer server = new MessageServer();
		if(userinfo != null){ 
			int res = server.delMessage(Integer.parseInt(messageID));
			String exist = WebCookie.cookie(request, "message");
			if(exist != null  && res>0){
				Cookie cookie=new Cookie("message", (Integer.parseInt(exist)-1)+"");
				cookie.setMaxAge(3600*24*2);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			out.print(res);
		}else{
			out.print(-1);
		}
		
	}
	/**
	 * 添加用户消息
	 * 
	 * @param userid,content
	 * 		用户ID,消息内容
	 */
	public void addMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String count = request.getParameter("count");
		String codid = request.getParameter("codid");
		String[] userinfo = WebCookie.getUser(request);
		 IMessageServer server = new MessageServer();
		if(userinfo != null){ 
			server.addMessage(Integer.parseInt(userinfo[0]), count,codid);
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}else{
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}
	}
	
	/**
	 * 修改用户消息状态
	 * 
	 * @param userid,content
	 * 		用户ID,消息内容
	 */
	public void upMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String messageId = request.getParameter("messageId");
		 IMessageServer server = new MessageServer();
		int res = server.upMessage(Integer.parseInt(messageId));
		String message = WebCookie.cookie(request, "message");
		if(message != null){
			int mes = Integer.parseInt(message);
			if(mes>0){
				Cookie cookie=new Cookie("message", (mes-1)+"");
				cookie.setMaxAge(3600*24*2);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		
	}
}
