package com.cbt.website.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static boolean getStringIsNull(String str){
		if(str!=null&&!str.trim().equals("")){
			return true;
		}
		return false;
	}
	public static DateFormat getDateFormatYMD(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		return format;
	}
	public static Date getDate(String dateStr){
		Date date=null;
		try {
			date=getDateFormatYMD().parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static void openExe(){
		Runtime run=Runtime.getRuntime();
		Process p=null;
		try {
			p=run.exec("\"D:/WindowsFormsApplication2.exe\"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 去除商品规格多余字符
	 * @param type
	 * @return
	 */
	public static String TypeMatch(String type){
		String str = "";
		String[] strArr = type.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String tmp = strArr[i];
			int idx = tmp.indexOf("@");
			if(idx <= 0){
				str += tmp;
			}else{
				if (i == (strArr.length - 1)) {
					str += tmp.substring(0, idx);
				}else {
					str += tmp.substring(0, idx)+",";
				}
			}
		}
		return str;
	}

	public static String[] ImgMatch(String img){
		if (img != null && !"".equals(img.trim())) {
			String[] str = null;
			str = img.replace("undefined@", "").split("@");
			return str;
		}else{
			return null;
		}
	}

}	
