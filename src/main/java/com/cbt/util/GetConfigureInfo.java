package com.cbt.util;


import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.util.JsonResult;
import org.slf4j.LoggerFactory;

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
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GetConfigureInfo.class);

	private static Properties cbtProperties = new Properties();
	private static Properties paypalProperties = new Properties();

	static {
		initCbt();
		initPayPal();
	}

	static void initCbt() {
		InputStream ins = null;
		try {
			ins = GetConfigureInfo.class.getClassLoader().getResourceAsStream("cbt.properties");
			cbtProperties.load(ins);
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("init error : " + e.getMessage());
			LOG.error("init error : ", e);
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
			LOG.error("init error : ", e);
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



	public static String getValueByCbt(String key){
		String rsVal = "";
		try{
			if(cbtProperties == null){
				initCbt();
			}
			rsVal = cbtProperties.getProperty(key);
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
			LOG.error("get openSync error : ", e);
		}
		return is;
	}


	/**
	 * 获取定时任务开启配置
	 * @return
	 */
	public static boolean openJob() {
		return false;
	}

	/**
	 * 获取谷歌插件存放数据的路径
	 * @return
	 */
	public static String getAddGoodsPath() {
		String path = "/data/cbtconsole/cbtimg/editimg/addGoodsNo.properties";
		try {
			if (cbtProperties == null) {
				initCbt();
			}
			path = String.valueOf(cbtProperties.getProperty("adGoodsPath"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getAddGoodsPath error : " + e.getMessage());
			LOG.error("getAddGoodsPath error : ", e);
		}
		return path;
	}



	public static String getAdgoodsPath() {
		String path="/data/cbtconsole/cbtimg/editimg/addGoodsNo.properties";
		try {
			if (cbtProperties == null) {
				initCbt();
			}
			path = String.valueOf(cbtProperties.getProperty("adGoodsPath"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get openJob error : " + e.getMessage());
			LOG.error("get openJob error : ", e);
		}
		return path;
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
			LOG.error("get getFtpConfig error : ", e);
		}
		return cfg;
	}

	public static void checkFtpConfig(FtpConfig ftpConfig, JsonResult json) {
        json.setOk(true);
        // 判断获取的配置信息是否有效
        if (ftpConfig == null || !ftpConfig.isOk()) {
            json.setOk(false);
            json.setMessage("获取配置文件失败");
        } else {
            if (StringUtil.isBlank(ftpConfig.getFtpURL())) {
                json.setOk(false);
                json.setMessage("获取ftpURL失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpPort())) {
                json.setOk(false);
                json.setMessage("获取ftpPort失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpUserName())) {
                json.setOk(false);
                json.setMessage("获取ftpUserName失败");
            } else if (StringUtil.isBlank(ftpConfig.getFtpPassword())) {
                json.setOk(false);
                json.setMessage("获取ftpPassword失败");
            } else if (StringUtil.isBlank(ftpConfig.getRemoteShowPath())) {
                json.setOk(false);
                json.setMessage("获取remoteShowPath失败");
            } else if (StringUtil.isBlank(ftpConfig.getLocalDiskPath())) {
                json.setOk(false);
                json.setMessage("获取localDiskPath失败");
            } else if (StringUtil.isBlank(ftpConfig.getLocalShowPath())) {
                json.setOk(false);
                json.setMessage("获取localShowPath失败");
            }
        }
    }

}
