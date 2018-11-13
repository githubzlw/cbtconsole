package com.cbt.website.servlet;

import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 8小时后台发送邮件给客户
 * Servlet implementation class SendMailWithAttachmentServlet
 */
public class SendMailWithAttachmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMailWithAttachmentServlet() {
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
		String emailInfo=request.getParameter("emailInfo");
		String emailaddress=request.getParameter("emailaddress");
		String copyEmail=request.getParameter("copyEmail");
		String fileName=request.getParameter("fileName");
		String customId = request.getParameter("customId");
		int res = 0;
		if(Utility.getStringIsNull(emailInfo)){
			String sendemail = null;
    	    String pwd = null;
			 if(Utility.getStringIsNull(copyEmail)){
    		    IUserDao userDao = new com.cbt.processes.dao.UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(0, copyEmail, 0);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
	    	  }
			 //附件 
			 String fileSrc = "D:\\pdf\\"+fileName;
			 File file = new  File(fileSrc);
			res = SendEmail.sendMailAndAttachment(sendemail,pwd,emailInfo,emailaddress,copyEmail,file,customId);
		}else{
			 
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}

}
