package com.cbt.share.util;

import com.cbt.share.pojo.SharePojo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShareUtil {
	/**
	 * 获得ip地址
	 * @param request
	 * @return 返回ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){ 
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr(); 
	    }
	    return ip; 
	}
	
    public static int halfSearch(List<SharePojo> allIpAddress, int key)//折半查找函数
    {
        int min,max,mid;
        min=0;
        max=allIpAddress.size()-1;
        mid=(min+max)/2;
 
        while (!(allIpAddress.get(min).getIp1()>=key && key<=allIpAddress.get(min).getIp2()))
        {
            if(key>allIpAddress.get(min).getIp2())
            {
                min=mid+1;
            }else if(key<allIpAddress.get(min).getIp1())
            {
                max=mid-1;
            }
            if(min>max)           //如果数组中没有所求的数，返回-1
                return -1;
            mid=(min+max)/2;     //保证函数继续
        }
        return mid;
    }
}
