package com.cbt.util;

import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.Syncdatainfo;
import com.cbt.warehouse.service.SyncdatainfoService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**   
 * @Title : SqlErrorUtil.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年11月18日
 * @version : V1.0   
 */


//执行异常sql处理
public class SqlErrorUtil {
	
	private static SyncdatainfoService syncdatainfoService = null;
	
	static {
		syncdatainfoService = (SyncdatainfoService) SpringContextUtil.getBean("SyncdatainfoService");
        
	}
	
	/**
	 * 
	 * @param sql sql语句
	 * @param flag 1:本地操作失败,2:远程操作失败
	 * @param loopNum 重试次数:填0默认执行1次
	 */
	public static int executeSqlError(String sql,int flag,int loopNum){
		if (sql!=null && !"".equals(sql)) {
			//本地链接
			Connection conn = DBHelper.getInstance().getConnection();
			//远程链接
			Connection conn1 = DBHelper.getInstance().getConnection2();
			PreparedStatement stmt = null;
			PreparedStatement stmt1 = null;
			int result = 0;
			try {
				//1重试本地
				if (loopNum <= 0) {
					return result;
				}
				if (flag == 1) {
					stmt=conn.prepareStatement(sql);
					result=stmt.executeUpdate();
					loopNum -= 1;
					//sql重试结果小于0和循环次数大于0的情况下继续重试
					if (result > 0) {
						return result;
					}else if (result <= 0 && loopNum <= 0) {
						Syncdatainfo syncdatainfo = new Syncdatainfo();
						syncdatainfo.setSqlstr(sql);
						syncdatainfo.setCreattime(new Date());
						syncdatainfo.setFlag(flag);
						syncdatainfoService.addSyncdatainfo(syncdatainfo);
						return 0;
					}else if (result <= 0 && loopNum > 0) {
						executeSqlError(sql, flag, loopNum);
					}
				}
				//重试远程
				if (flag == 2) {
					stmt1=conn1.prepareStatement(sql);
					result=stmt1.executeUpdate();
					loopNum -= 1;
					//sql重试结果小于0和循环次数大于0的情况下继续重试
					if (result > 0) {
						return result;
					}else if (result <= 0 && loopNum <= 0) {
						Syncdatainfo syncdatainfo = new Syncdatainfo();
						syncdatainfo.setSqlstr(sql);
						syncdatainfo.setCreattime(new Date());
						syncdatainfo.setFlag(flag);
						syncdatainfoService.addSyncdatainfo(syncdatainfo);
						return 0;
					}else if (result <= 0 && loopNum > 0) {
						executeSqlError(sql, flag, loopNum);
					}
				}
			} catch (SQLException e) {
//				e.printStackTrace();
				loopNum -= 1;
				if (loopNum <= 0) {
					Syncdatainfo syncdatainfo = new Syncdatainfo();
					syncdatainfo.setSqlstr(sql);
					syncdatainfo.setCreattime(new Date());
					syncdatainfo.setFlag(flag);
					syncdatainfoService.addSyncdatainfo(syncdatainfo);
				}else{
					executeSqlError(sql, flag, loopNum);
				}
			}finally {
				DBHelper.getInstance().closePreparedStatement(stmt);
				DBHelper.getInstance().closePreparedStatement(stmt1);
				DBHelper.getInstance().closeConnection(conn);
				DBHelper.getInstance().closeConnection(conn1);
			}
		}
		return 0;
	}

}
