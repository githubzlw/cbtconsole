package com.cbt.util;

import java.util.Map;

public class AppConfig {

	// 此处设置ip，并且facebook中的回调地址也需要配置https://developers.facebook.com/apps/
	public static String ip_email = "http://www.import-express.com";

	//新增发送邮件访问域名地址
	public static String server_path = "http://www.importx.com";
	//个人中心地址
	public static String center_path = "https://www.import-express.com/individual/getCenter";

	
//	public static String ips = "http://192.168.1.220:8084/cbtconsole";
	public static String ips = "http://192.168.1.55:8180/cbtconsole";
	//public static String ips = "http://192.168.1.29:8899/cbtconsole";
	//public static String ips = "http://27.115.38.42:8086/cbtconsole";
	
	public static String ip = ips;
	public static String path = ips;
	// 入库图片保存路径
	public static String product = "http://127.0.0.1:90";
//	public static String product = "http://192.168.1.220:90";
//	public static String product = "http://116.228.150.218:90";
	//public static String product = "http://27.115.38.42:90";

	public static enum Enum_log {
		TEST, CLOSE, OPEN;
	}

	// 日志记录,CLOSE-关闭，OPEN-正式库，TEST-测试库
	public static final Enum_log Log_action = AppConfig.Enum_log.CLOSE;
	/* paypal付款沙盒测试环境配置 */
	public static String paypal_action = "https://www.sandbox.paypal.com/cgi-bin/webscr";
	public static String paypal_business = "wanyangnumberonebusiness@163.com";
	public static String paypal_notifyurl = "http://116.228.150.218:10001/cbtconsole/paysServlet?action=validatePayment&className=PaymentServerlet";
	/* paypal付款正式环境配置 */
	// public static String
	// paypal_action="https://www.paypal.com/cgi-bin/webscr";
	// public static String paypal_business="584JZVFU6PPVU";
	// public static String
	// paypal_notifyurl=ip+"/paysServlet?action=validatePayment&className=PaymentServerlet";
	// facebook
	public static final String APP_ID = "1582187542069067";
	public static final String APP_SECRET = "5ec4e4b51e5d716e91b3ef47747e2a81";
	public static final String redirect_uri = ip + "/FacebookCallback";// facebook网站中配置的界面回调地址
	// 汇率
	public static Map<String, String> hl = null;
	// 注册邮箱
	public static final String emaill_url = ip;
	// 寄件人信息
	public static final String adminname = "ZHENGLING";
	public static final String admincompany = "IMPORT-EXPRESS";
	public static final String adminzone = "CHINA";
	public static final String admincode = "200062";
	public static final String adminaddress = "ROOM 605, BUILDING NO.1, HUI YIN MING ZUN BUILDING, NO.609 ，EAST YUN LING ROAD, PUTUO DISTRICT，SHANGHAI,CHINA.200062";
	public static final String adminphone = "02161504007-835";
	public static final String adminprovince = "SHANGHAI";
	public static final String admincity = "SHANGHAI";
	
	// 控制是否自动分配采购人员开关--whj
	public static final boolean isAuto = true;

}
