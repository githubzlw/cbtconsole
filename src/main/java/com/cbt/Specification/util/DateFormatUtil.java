package com.cbt.Specification.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化
 * 
 * @author JiangXianwei
 *
 */
public class DateFormatUtil {

	/**
	 * 获取 yyyy-MM-dd HH:mm:ss:SSS 格式的时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getWithMicroseconds(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		return df.format(date);
	}

	/**
	 * 获取 yy/MM/dd HH:mm 格式的时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getWithMinutes(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm");
		return df.format(date);
	}

	/**
	 * 获取 yyyy-MM-dd HH:mm:ss 格式的时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String getWithSeconds(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	
	/**
	 * 获取 yyyy-MM-dd 格式的时间字符串
	 * @param date
	 * @return
	 */
	public static String getWithDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	/**
	 * 转换 yyyy-MM-dd 字符串到date类型
	 * @param dateStr
	 * @return
	 */
	public static Date formatStringToDate(String dateStr){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCurrentYearAndMonth() {
		String str;
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.MONTH) + 1 < 10) {
			str = cal.get(Calendar.YEAR) + "-0" + (cal.get(Calendar.MONTH) + 1);
		} else {
			str = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1);
		}
		return str;
	}

	public static void main(String[] args) {
		System.err.println(getCurrentYearAndMonth());
	}

}
