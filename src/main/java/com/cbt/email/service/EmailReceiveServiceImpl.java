package com.cbt.email.service;

import com.cbt.email.dao.EmailReceiveDaoImpl;
import com.cbt.email.dao.IEmailReceiveDao;
import com.cbt.email.entity.EmailReceive;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.Utility;

import java.sql.Date;
import java.util.List;


public class EmailReceiveServiceImpl implements  IEmailReceiveService{
	IEmailReceiveDao dao = new EmailReceiveDaoImpl();

	@Override
	public int add(EmailReceive receive) {
		
		return dao.add(receive);
	}

	@Override
	public List<EmailReceive> getall(EmailReceive er) {
		
		return dao.getall(er);
	}
	@Override
	public int getalltotal(EmailReceive er) {
		
		return dao.getalltotal(er);
	}

	

	@Override
	public EmailReceive getEmail(int id) {
		
		return dao.getEmail(id);
	}

	@Override
	public int reply(int id,int userId, String content, Date date, String email,
			String title) {
		if(Utility.getStringIsNull(email)){
		    	   String sendemail = null;
		    	   String pwd = null;
		    	   if(userId != 0){
		    		   IUserDao userDao = new UserDao();
		    		   String[] adminEmail =  userDao.getAdminUser(0, null, userId);
		    		   if(adminEmail != null){
		    			   sendemail = adminEmail[0];
		    			   pwd = adminEmail[1];
		    		   }
		    	   }
				        SendEmail.send(sendemail,pwd,email, content,title,"", 1);
		       }
			return dao.reply(id, content,title,date);
		}

	@Override
	public int add1(EmailReceive receive) {
		
		return dao.add1(receive);
	}

	@Override
	public List<EmailReceive> getall1(int id, EmailReceive er) {
		
		return dao.getall1(id,er);
	}

	@Override
	public int getalltotal1(int id, EmailReceive er) {
		
		return dao.getalltotal1(id,er);
	}

	@Override
	public List<EmailReceive> getall2(EmailReceive er) {
		
		return dao.getall2(er);
	}

	@Override
	public int getalltotal2(EmailReceive er) {
		
		return dao.getalltotal2(er);
	}

	@Override
	public List<EmailReceive1> getall(String orderNo) {
		
		return dao.getall(orderNo);
	}

	
	}

