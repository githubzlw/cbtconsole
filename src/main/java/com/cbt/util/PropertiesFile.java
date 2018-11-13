package com.cbt.util;

import com.cbt.jdbc.DBHelper;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {
    
	public static double getDiscount(){
		InputStream ins = DBHelper.class
				.getResourceAsStream("../../../cbt.properties");
		Properties p = new Properties();
		double discount = 1;
		try {
			p.load(ins);
			discount = Double.parseDouble(p.get("discount").toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return discount;
	}
}
