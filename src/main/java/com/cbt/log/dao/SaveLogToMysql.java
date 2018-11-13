package com.cbt.log.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.log.jdbc.DBHelperLog;
import com.cbt.util.AppConfig;
import com.cbt.util.AppConfig.Enum_log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveLogToMysql {
	public static String LOG_TABLE;
	 
	    public static void insert(int userId,String sessionid,String keywords,String seachwords,String ip,String view_url,int view_url_count,String action,String pruduct_url, String device,String placement)
	    {
	    	Connection conn;
	    	if(AppConfig.Log_action == Enum_log.OPEN){
		    	conn = DBHelperLog.getConnection();
	    	}else if(AppConfig.Log_action == Enum_log.TEST){
		    	conn = DBHelper.getInstance().getConnection();
	    	}else{
	    		return;
	    	}
	    	String sql="insert into behavior_record(userid,sessionid,keywords,seachwords,ip,view_url,view_url_count,view_date_year,view_date_month,view_date_day,view_date_time,action,producturl,device,placement) values (?,?,?,?,?,?,?,year(now()),DATE_FORMAT(now(),'%Y-%m'),DATE_FORMAT(now(),'%Y-%m-%d'),now(),?,?,?,?)";
	    	PreparedStatement stmt = null;
	    	try {
	    		stmt = conn.prepareStatement(sql);
	    		stmt.setInt(1, userId);
	    		stmt.setString(2, sessionid);
	    		stmt.setString(3, keywords);
	    		stmt.setString(4, seachwords);
	    		stmt.setString(5, ip);
	    		stmt.setString(6, view_url);
	    		stmt.setInt(7, view_url_count);
	    		stmt.setString(8, action);
	    		stmt.setString(9, pruduct_url);
	    		stmt.setString(10, device);
	    		stmt.setString(11, placement);
	    		stmt.executeUpdate();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	} finally {
	    		if (stmt != null) {
	    			try {
	    				stmt.close();
	    			} catch (SQLException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		DBHelper.getInstance().closeConnection(conn);
	    	}
	    }
}
