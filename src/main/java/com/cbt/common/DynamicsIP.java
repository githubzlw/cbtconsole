package com.cbt.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 *动态外网ip
 *@author abc
 *@date 2016年12月6日
 *
 */
public class DynamicsIP {
	
	 public  String getMyIP() {  
	        InputStream ins = null;  
	        String result = "";
	        try {  
	            URL url = new URL("http://1212.ip138.com/ic.asp");  
	            URLConnection con = url.openConnection();  
	            ins = con.getInputStream();  
	            InputStreamReader isReader = new InputStreamReader(ins, "GB2312");  
	            BufferedReader bReader = new BufferedReader(isReader);  
	            StringBuffer webContent = new StringBuffer();  
	            String str = null;  
	            while ((str = bReader.readLine()) != null) {  
	                webContent.append(str);  
	            }  
	            System.err.println(webContent);
	            int start = webContent.indexOf("[") + 1;  
	            int end = webContent.indexOf("]"); 
	            result = webContent.substring(start, end);
	        }catch(Exception e){
	        	e.printStackTrace();
	        } finally {  
	            if (ins != null) {  
	                try {
						ins.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}  
	            }  
	        } 
	        return result;
	    }  
}
