package com.cbt.userinfo.service;

import com.cbt.pojo.UserEx;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.userinfo.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserInfoService implements IUserInfoService {
	@Autowired
	private UserMapper mapper;
	@Override
	public Map<String, Object> getUserCount(int userid) {
		return mapper.getUserCount(userid);
	}
	@Override
	public List<String> getPaypal(int userID) {
		return mapper.getPaypal(userID);
	}
	
	/**
	 * 获取扩展用户信息
	 */
	@Override
	public UserEx getUserEx(int userID){
		return mapper.getUserEx(userID);
	}

	@Override
	public List<String> getAllAdmuser(String admName) {
		return mapper.getAllAdmuser(admName);
	}

	@Override
	public String exitEmail(String email) {
		return mapper.exitEmail(email);
	}
	
	@Override
	public String exitPhone(String phone,int userid) {
		return mapper.exitPhone(phone,userid);
	}
	
	@Override
	public int upEmail(String email, int uid, String oldemail, int adminid) {
		Random random = new Random();
		int password = random.nextInt(899999);
		String passwordNew = com.cbt.util.Md5Util.encoder(email + password);
		int res = mapper.upEmail(passwordNew, email, uid);
		if(res > 0){
			mapper.saveupemail_log(oldemail, email, adminid);
			String sendemail = null;
    	    String pwd = null;
    		    IUserDao userDao = new UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(adminid, "", 0);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
    		    StringBuffer sb = new StringBuffer("You have reset your email address in ImportExpress.com! </br>");
    			sb.append("New email address: "+email);
    			sb.append("<br>Passward: " + password + "<br>");
    			sb.append("Please login to your <a href='http://www.import-express.com/cbt/uppass.jsp?email=" + email + "'>Import-express Account</a> to reset your passward.<br> ");
    			SendEmail.send(sendemail,pwd,email,SendEmail.SetContent(email, sb).toString(),"Your ImportExpress login email address has been reset!","","", 1);
//			SendEmail.send(sendemail,pwd,email, emailInfo,"Your ImportExpress Split Order details, please Advise.","","", 1);
		}
		return res;
	}
	
	@Override
	public int upPhone(String newPhone, int userid, String oldPhone,int admuserid) {
		return mapper.upPhone(newPhone, userid, oldPhone);
	}
	
	@Override
	public int InPhone(String newPhone, int userid) {
		return mapper.InPhone(newPhone, userid);
	}
public static void main(String[] args) {
	StringBuffer sb = new StringBuffer("You have reset your email address in ImportExpress.com! </br>");
	sb.append("New email address: 1351535753@qq.com");
	sb.append("<br>Passward: " + 11111 + "<br>");
	sb.append("Please login to your <a href='http://www.import-express.com/cbt/uppass.jsp?email=1351535753@qq.com'>Import-express Account</a> to reset your passward.<br> ");
	SendEmail.send(null,null,"1351535753@qq.com", SendEmail.SetContent("1351535753@qq.com", sb).toString(),"Your ImportExpress login email address has been reset!","","", 1);
}

    @Override
    public List<String> queryUserRemark(int userid) {
        return mapper.queryUserRemark(userid);
    }

    @Override
    public void insertUserRemark(int userid, String remark) {
        mapper.insertUserRemark(userid, remark);
    }
}
