package com.cbt.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ListToJson {
	public static Map<String, Object> formatToJson(Object t) throws IllegalArgumentException, IllegalAccessException{
		Class cls = t.getClass();
		Field[] fields = cls.getDeclaredFields();  
		Map<String, Object> objMap = new HashMap<String, Object>();
		for(int i=0; i<fields.length; i++){  
            Field f = fields[i];  
            f.setAccessible(true);  
            System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(t));  
            objMap.put(f.getName(), f.get(t));
        } 
		return objMap;
	}
}
