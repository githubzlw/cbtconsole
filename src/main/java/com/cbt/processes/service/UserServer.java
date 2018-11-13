package com.cbt.processes.service;

import com.cbt.bean.UserBean;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.util.AppConfig;
import com.cbt.util.Md5Util;
import com.cbt.util.ResCode;
import com.cbt.util.UUIDUtil;

import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class UserServer implements IUserServer {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(UserServer.class);
	IUserDao userDao = new UserDao();
	
	@Override
	public int regUser(UserBean user) {
		String passwordNew = Md5Util.encoder(user.getName() + user.getPass());
		user.setPass(passwordNew);
		int userid = userDao.regUser(user);
		return userid;
	}
	
	@Override
	public void regSendEmail(String email,String name,String pass){
		String uuid = UUIDUtil.getEffectiveUUID(0, email);
		
		StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
        sb.append("<a href='"+AppConfig.ip_email+"'><img style='cursor: pointer' src='"+AppConfig.ip_email+"/img/logo.png' ></img></a>");
//        String path = "";
//        try {
//			path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//        sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
//        sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
//        sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip_email+path+"'>here</a>.");
//        sb.append("</div></div></div>");
        
        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear " + name + ",</div><br><div style='font-size: 13px;'>"); 

		StringBuffer sb2=new StringBuffer();
		sb2.append(sb);
		sb2.append("Thank you for registering on <a style='color: #0070C0' href='"+AppConfig.ip_email+"'>Import Express</a>. You are now ready to enjoy simple shopping and import experiences and unbelievable deals with us.</div>"); 
		sb2.append("<div style='margin-bottom: 10px;'>Your User ID:"+name+"</div>");
		sb2.append("<div style='margin-bottom: 10px;'>Password:"+pass+"</div>");
        

		StringBuffer sb1=new StringBuffer();
        sb1.append("<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
        sb1.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
        sb1.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
        //自动登录路径
        String path = "";
        try {
			path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        sb1.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip_email+path+"'>here</a>.");
        sb1.append("</div></div></div>");
        sb2.append(sb1);
        
        //发送注册邮件
	    SendEmail.send(null,null,email, sb2.toString(),"You've successfully created a ImportExpress account. We Welcome You!","", 1);
	    //发送运费赠送邮件邮件
		StringBuffer sb3=new StringBuffer();
		sb3.append(sb);
		sb3.append("For every new customer, we offer a <label style='font-weight: bold;'>USD$50</label> credit which can be applied to shipping fee.");
		sb3.append("<br><div>Enjoy your shopping with <a style='color: #0070C0' href='http://www.import-express.com'>www.Import-Express.com<a></div>");
		sb3.append(sb1);
	    SendEmail.send(null,null,email, sb3.toString(),"You have USD$50 credit in your ImportExpress Account!","", 1);
	}
	@Override
	public int getNameEmail(String name, String email){
		return userDao.getNameEmail(name, email);
	}
	@Override
	public boolean getUserName(String name) {
		 
		return userDao.getUserName(name)>0?true:false;
	}
	@Override
	public boolean getUserEmail(String name) {
		 
		return userDao.getUserEmail(name) == null ? false:true;
	}

	@Override
	public String getUserEmail(String email,String validateCode) {
		UserBean user = userDao.getUserEmail(email);
		 long diff = new Date().getTime() - user.getActivationTime().getTime();
		 long day2 = 1000*60*60*48;
		 String code = "";
		if(diff<day2){
			if(validateCode.equals(user.getActivationCode())){
				boolean res = userDao.upUserState(email);
				if(res){
					code = "code=1&info=已激活";
				}else{
					code =  "code=0&info=激活失败";
				}
			}else{
				code =  "code="+ResCode.VALIDATECODE+"&info=激活失败";
			}
		}else{
			code =  "code="+ResCode.EMAIL_TIMEOUT+"&info=激活码超时";
		}
		return code;
	}

	@Override
	public String upUserState(String email,String validateCode) {
		String code = "";
		 UserBean user=userDao.getUserEmail(email);
		 long diff = new Date().getTime() - user.getActivationTime().getTime();
		 long day2 = 1000*60*60*48;
	        //验证用户是否存在 
	        if(user!=null) {  
	            //验证用户激活状态  
	            if(user.getActivationState()==0) { 
	              //激活码是否过期
	                if(diff<day2){ 
	                    //验证激活码是否正确  
	                    if(validateCode.equals(user.getActivationCode())) {  
	                        //激活成功， //并更新用户的激活状态，为已激活 
	                        user.setActivationState(1);//把状态改为激活
	                        userDao.upUserState(email);
	                        code = "code=1&info=激活成功";
	                    } else {  
	                    	code = "code="+ResCode.VALIDATECODE+"&info=激活码错误";
	                    }  
	                } else { 
	                	code = "code="+ResCode.VALIDATECODE_PVERDUE+"&info=激活码已过期";
	                	String activationCode = Md5Util.encoder(email+new Date().getTime());
	                	userDao.upUserActivationCode(email, activationCode, 1);
	                }  
	            } else {
	            	code = "code="+ResCode.EMAIL_EXIST+"&info=邮箱已激活，请登录！";
	            	LOG.warn("邮箱已激活，请登录！"); 
	            }  
	        } else {
	        	code = "code="+ResCode.EMAIL_NOEXIST+"&info=该邮箱未注册（邮箱地址不存在）！";
	        }  
		return code;
	}

	/*@Override
	public String sendEmail(String email) {
		UserBean user = userDao.getUserEmail(email);
		
		 StringBuffer sb=new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
	        sb.append("<a href=\""+AppConfig.emaill_url+"/processesServlet?action=regActivate&className=RegServlet&email=");
	        sb.append(user.getEmail()); 
	        sb.append("&validateCode="); 
	        sb.append(user.getActivationCode());
	        sb.append("\">" + AppConfig.emaill_url + "/processesServlet?action=regActivate&className=RegServlet&email="); 
	        sb.append(user.getEmail()); 
	        sb.append("&validateCode=");
	        sb.append(user.getActivationCode());
	        sb.append("</a>");

	        //发送邮件
	        try {
		        SendEmail.send(user.getEmail(), sb.toString(),"ImportExpress Account Update");
		        LOG.warn("发送邮件："+user.getEmail());
			} catch (Exception e) {
				return "code="+ResCode.EMAIL_SDENDFAIL+"&info=邮件发送失败";
			}
		return "code=1&info=发送成功";
	}*/

	@Override
	public String sendEmailfind(String email) {
		UserBean user = userDao.getUserEmail(email);
		String uuid = UUIDUtil.getEffectiveUUID(0, user.getEmail());
		LOG.warn("user:"+user);
		String activationPassCode = Md5Util.encoder(email+new Date().getTime());
		userDao.upUserActivationCode(email, activationPassCode,2);
		String path = "";
		try {
			 path = UUIDUtil.getAutoLoginPath("/processesServlet?action=passActivate&className=ForgotPassword&email="+user.getEmail()+"&validateCode="+activationPassCode, uuid);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		 StringBuffer sb=new StringBuffer("Please click the below link to set your new password</br>");
		 	sb.append("<a href=\""+AppConfig.emaill_url+path);
/*	        sb.append("<a href=\""+AppConfig.emaill_url+"/processesServlet?action=passActivate&className=ForgotPassword&email=");
	        sb.append(user.getEmail()); 
	        sb.append("&validateCode="); 
	        sb.append(activationPassCode);*/
		 	sb.append("\">" + AppConfig.emaill_url + path);
/*	        sb.append("\">" + AppConfig.emaill_url + "/processesServlet?action=passActivate&className=ForgotPassword&email="); 
	        sb.append(user.getEmail()); 
	        sb.append("&validateCode=");
	        sb.append(activationPassCode);*/
	        sb.append("</a>");

	        //发送邮件
	        try {
		        SendEmail.send(null,null,user.getEmail(), sb.toString(),"ImportExpress Account Update","", 1);
		        LOG.warn("发送邮件："+user.getEmail());
			} catch (Exception e) {
				return "code="+ResCode.EMAIL_SDENDFAIL+"&info=邮件发送失败";
			}
		return "code=1&info=注册成功";
	}
	@Override
	public UserBean login(String name, String pass) {
		String username = name;
		if(name.indexOf("@")>-1){
			username = userDao.getUserEmailName(name);
		}
		String passt = Md5Util.encoder(username + pass);
		UserBean user = userDao.getUser(name, passt);
		return user;
	}

	@Override
	public UserBean loginFacebook(String facebookID, UserBean user) {
		//查看用户是否绑定
		UserBean ub = userDao.getFacebookUser(facebookID);
		if(ub == null){
			//查询邮箱是否存在，如果存在则直接将facebookID与现有邮箱绑定
			UserBean userBean = userDao.getUserEmail(user.getEmail());
			
			if(userBean !=null ){
				//绑定facebook用户
				boolean boo = userDao.facebookbound(userBean.getId() , facebookID);
				if(!boo){
					 return null;
				}
				userBean.setName(user.getName());
				userBean.setLogReg(1);
				return userBean;
			}
			//查询facebook中的用户名称是否存在，存在则自动增加字符，使其唯一
			int userid = userDao.getUserName(user.getName());
			if(userid != 0 ){
				user.setName(user.getName()+userid);
			}
			//添加用户
			int res = userDao.regUser(user);
			if(res>0){
				//绑定facebook用户
				boolean boo = userDao.facebookbound(res , facebookID);
				if(boo){
					ub = new UserBean();
					ub.setId(res);
					ub.setName(user.getName());
				}else{
					ub = null;
				}
				ub.setLogReg(1);
			}
		}
		return ub;
	}

	@Override
	public boolean getNameEmial(String name, String email) {
		return userDao.getUserName(name)>0?true:false;
	}

	@Override
	public boolean passActivate(String email, String validateCode) {
		UserBean user = userDao.getUserEmail(email);
		 long diff = new Date().getTime() - user.getActivationPassTime().getTime();
		 long day2 = 1000*60*60*48;
		 boolean code = false;
		if(diff<day2){
			if(validateCode.equals(user.getActivationPassCode())){
				return true;
			}
		}
		return code;
	}

	@Override
	public int upPassword(String email,String password) {
		String name = userDao.getUserEmailName(email);
		String passwordNew = Md5Util.encoder(name + password);
		return userDao.upPassword(passwordNew, email);
	}

	@Override
	public double[] getBalance(int id) {
		
		return userDao.getUserPrice(id);
	}

	@Override
	public int upPasswordName(String password, String username) {
		String passwordNew = Md5Util.encoder(username + password);
		return userDao.upPasswordName(passwordNew, username);
	}

	@Override
	public double getUserApplicableCredit(int userId) {
		return userDao.getUserApplicableCredit(userId);
	}

	@Override
	public int upUserApplicableCredit(int userId, double acprice) {
		return userDao.upUserApplicableCredit(userId, acprice);
	}

	@Override
	public int upUserPrice(int userId, double price) {
		// TODO Auto-generated method stub
		return userDao.upUserPrice(userId, price);
	}

	@Override
	public int upUserPrice(int userId, double price, double acprice) {
		// TODO Auto-generated method stub
		return userDao.upUserPrice(userId, price, acprice);
	}
	
	@Override
	public UserBean getUserInfo(int userId) {
		UserBean user = userDao.getUserInfo(userId);
		return user;
	}
	

	@Override
	public String[] getBalance_currency(int id) {
		// TODO Auto-generated method stub
		return userDao.getBalance_currency(id);
	}
}
