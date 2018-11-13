package com.cbt.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取配置信息
 * 
 * @author Jxw
 *
 */
public class GetConfigureInfo {
	private static final Log LOG = LogFactory.getLog(GetConfigureInfo.class);

	private static Properties cbtProperties = new Properties();
	private static Properties paypalProperties = new Properties();

	static {
		initCbt();
		initPayPal();
	}

	static void initCbt() {
		InputStream ins = null;
		try {
			ins = GetConfigureInfo.class.getResourceAsStream("../../../cbt.properties");
			cbtProperties.load(ins);
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("init error : " + e.getMessage());
			LOG.error("init error : " + e.getMessage());
		}finally {
			if(ins != null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (cbtProperties != null) {
			System.out.println("current openSync is: " + Boolean.valueOf(cbtProperties.getProperty("openSync")));
		} else {
			System.out.println("init error");
		}
	}

	static void initPayPal() {
		InputStream ins = null;
		try {
			ins = GetConfigureInfo.class.getResourceAsStream("../../../paypal.properties");
			paypalProperties.load(ins);
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("init error : " + e.getMessage());
			LOG.error("init error : " + e.getMessage());
		}finally {
			if(ins != null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getValueByPapPal(String key){
		String rsVal = "";
		try{
			if(paypalProperties == null){
				initPayPal();
			}
			rsVal = paypalProperties.getProperty(key);
		}catch (Exception e){
			e.printStackTrace();
		}

		return rsVal;
	}

	/**
	 * 获取cbt.properties是否开启线下同步线上配置
	 * 
	 * @return
	 */
	public static boolean openSync() {
		boolean is = false;
		try {
			if (cbtProperties == null) {
				initCbt();
			}
			is = Boolean.valueOf(cbtProperties.getProperty("openSync"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get openSync error : " + e.getMessage());
			LOG.error("get openSync error : " + e.getMessage());
		}
		return is;
	}


	/**
	 * 获取定时任务开启配置
	 * @return
	 */
	public static boolean openJob() {
		boolean is = false;
		try {
			if (cbtProperties == null) {
				initCbt();
			}
			is = Boolean.valueOf(cbtProperties.getProperty("openJob"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get openJob error : " + e.getMessage());
			LOG.error("get openJob error : " + e.getMessage());
		}
		return is;
	}



	public static FtpConfig getFtpConfig() {
		FtpConfig cfg = new FtpConfig();
		try {
			if (cbtProperties == null) {
				initCbt();
			}

			cfg.setFtpURL(String.valueOf(cbtProperties.getProperty("ftpUrl")));
			cfg.setFtpPort(String.valueOf(cbtProperties.getProperty("ftpPort")));
			cfg.setFtpUserName(String.valueOf(cbtProperties.getProperty("ftpUserName")));
			cfg.setFtpPassword(String.valueOf(cbtProperties.getProperty("ftpPassword")));
			cfg.setLocalDiskPath(String.valueOf(cbtProperties.getProperty("localDiskPath")));
			cfg.setLocalShowPath(String.valueOf(cbtProperties.getProperty("localShowPath")));
			cfg.setRemoteShowPath(String.valueOf(cbtProperties.getProperty("remoteShowPath")));
			cfg.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get getFtpConfig error : " + e.getMessage());
			LOG.error("get getFtpConfig error : " + e.getMessage());
		}
		return cfg;
	}

}
