package com.cbt.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化
 * 
 * @author JiangXianwei
 *
 */
public class DateFormatUtil {

	/**
     * 获取给定时间的年月
     * @param dateTime
     * @return
     */
    public static String formatDateToYearAndMonthString(LocalDateTime dateTime) {
        return format(dateTime, "yyyy-MM");
    }


    /**
     * 根据规格格式化日式为字符串
     * @param dateTime
     * @param patternStr
     * @return
     */
    private static String format(LocalDateTime dateTime, String patternStr) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(patternStr, Locale.US);
        return pattern.format(dateTime);
    }

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
	    if (date == null){
	        return "";
        }
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
	 * 获取 yyyy/MM/dd 格式的时间字符串
	 * @param date
	 * @return
	 */
	public static String getWithDaySkim(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
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

	public static String formatDateToStringByYear(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		return df.format(date);
	}


	/**
	 * 根据字符串获取时间
	 * @param dateStr
	 * @return
	 */
	public static LocalDateTime getTimeWithStr(String dateStr){
		return getTimeByStr(dateStr,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据给出规格格式化字符串为时间
	 * @param dateStr
	 * @param patternStr
	 * @return
	 */
	 public static LocalDateTime getTimeByStr(String dateStr,String patternStr){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patternStr);
     return LocalDateTime.parse(dateStr, dtf);


	 }
	public static void main(String[] args) {
		System.err.println(formatDateToStringByYear(new Date()));
	}

	public static Date getDateByTimeStr(String dateStr) throws ParseException {
	 	if(StringUtils.isNotBlank(dateStr)){
	 		if(dateStr.length() > 10){
	 			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 			return df.parse(dateStr);
			}else{
	 			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 			return df.parse(dateStr);
			}
		}else{
	 		return null;
		}
	}

}
