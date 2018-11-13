package com.cbt.util;

public class RandomUtil {
	
	public static Integer getRandom6(){
		
		return (int)((Math.random()*9+1)*100000);
	}
	
	public static Integer getRandom4(){
		
		return (int)((Math.random()*9+1)*1000);
	}
	
	public static Integer getRandom5(){
		
		return (int)((Math.random()*9+1)*10000);
	}
}
