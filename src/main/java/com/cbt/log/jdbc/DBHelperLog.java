package com.cbt.log.jdbc;
/**
 * 业务服务器使用
 * @author chenhaishen
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

public class DBHelperLog {
	private static final Log LOG = LogFactory.getLog(DBHelperLog.class);
	public static ComboPooledDataSource pool = null;
//	public static int serverId=0;
	public static void init() {
		try {
			long  st = new Date().getTime();
			destory();
			LOG.warn("初始化数据库");
			synchronized (DBHelperLog.class) {
				InputStream ins = DBHelperLog.class
						.getResourceAsStream("../../../../jdbc.properties");
				Properties p = new Properties();
				try {
					p.load(ins);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pool = new ComboPooledDataSource();
				pool.setDriverClass(p.getProperty("driver")); // loads the jdbc
																// driver
				pool.setJdbcUrl(p.getProperty("url_log")
						+ "?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull");
				pool.setUser(p.getProperty("userName_log"));
				pool.setPassword(p.getProperty("userPass_log"));
				pool.setMaxIdleTime(300);
				pool.setIdleConnectionTestPeriod(1800);
				pool.setAcquireIncrement(3);
				pool.setMaxPoolSize(3050);
				long  stt = new Date().getTime();
				LOG.warn("初始化数据库:"+(stt-st));
//				serverId=Integer.parseInt(p.getProperty("serverId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 销毁
	 */
	public static void destory() {
		try {
			if (pool != null) {
				DataSources.destroy(pool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			if(pool==null){
				init();
			}
			return pool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void returnConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		init();
	}
}
