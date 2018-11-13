package com.cbt.util;

import java.util.ResourceBundle;

public class SysParamUtil {
	
	private static String SYSPARAM_FILE = "cbt";
	
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle(SYSPARAM_FILE);
	
	
	/**
	 * 获得系统参数
	 * @param key
	 * @return
	 */
	public static String getParam(String key) {
		String param = null;
		try {
			param = BUNDLE.getString(key);
		} catch (Exception e) {
			//找不到直接返回null
			return param;
		}
		return param;
	}
}
