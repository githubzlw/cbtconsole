package com.importExpress.pojo;

import java.security.SecureRandom;
import java.util.UUID;

public class IdGen {

	 private static SecureRandom random = new SecureRandom();

	    private IdGen() {
	    }

	    /**
	     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	     */
	    public static String uuid() {
	        return UUID.randomUUID().toString().replaceAll("-", "");
	    }

	    /**
	     * 使用SecureRandom随机生成Long. 
	     */
	    public static long randomLong() {
	        return Math.abs(random.nextLong());
	    }

	    private static String createZeroString(String s) {
	        if(s.length() > 4) return s;
	        StringBuffer sb = new StringBuffer();
	        for(int i = 0 ; i < 4 - s.length() ; i++) {
	            sb.append("0");
	        }

	        return sb.append(s).toString();
	    }

	    public static void main(String[] args) {
	        IdGen id=new IdGen();
	        System.out.println(id.uuid());
	    }
}
