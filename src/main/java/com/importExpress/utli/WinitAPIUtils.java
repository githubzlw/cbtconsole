package com.importExpress.utli;

public class WinitAPIUtils {
	private static String sign = "";
	static {
		init();
	}
	
	private static void init() {
		PropertiesUtils propertiesUtils = new PropertiesUtils("cbt.properties");
		sign = propertiesUtils.getProperty("sign");
	}
	
	/**
	 * 开始同步库存
	 */
	public static void syncStock() {
		//生成签名
		
		//请求仓库，返回仓库ID 和仓库Code
		
		
		//查询库存明细
		
		
		
		
		
	}
	
	
	
	
	
	

}
