package com.cbt.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取HttpServletRequest中的远程访问IP，判断是否是内部网络访问的
 * @author Administrator
 *
 */
public class IpCheckUtil {
	
	/**
	 * 判断远程访问IP是在网段192.168下的所有IP
	 * @param request
	 * @return
	 */
	public static boolean checkIsIntranet(HttpServletRequest request){
		String ipStr= request.getRemoteAddr();
		if(ipStr.indexOf("192.168") >=0){
			return true;
		} else{
			return false;
		}
	} 

}
