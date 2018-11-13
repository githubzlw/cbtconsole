package com.cbt.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 
 * @author 余秋月
 *	工具类
 */
public class WebTool {

	
	/**
	 * 格式化输出json
	 * @param result
	 * @param response
	 */
	public static void writeJson(String result,HttpServletResponse response){
		PrintWriter writer = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			writer = response.getWriter();
			writer.write(result);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 判断字符是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || ("").equals(str.trim())) {
			return true;
		}
		return false;
	}
}
