package com.cbt.email.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsoupGetData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 获取网页的内容
	 * @throws IOException 
	 \*/
	public static String getContent(String html) throws IOException {
		Document doc = Jsoup.parse(html);
		System.out.println("html:"+doc.html());
		Elements ets=doc.getElementsByTag("script");
		String jsoupResult=ets.get(ets.size()-1).html();
		/*Iterator<Element> it=ets.iterator();
		while(it.hasNext()){
			Element et=it.next();
			System.out.println("text:"+et.html());
		}*/
		return jsoupResult;
	}
	
	//截取数字  
	public static String getNumbers(String content) {  
	    Pattern pattern = Pattern.compile("\\d+");  
	    Matcher matcher = pattern.matcher(content);  
	    while (matcher.find()) {  
	       return matcher.group(0);  
	    }  
	    return "";  
	}
	
	//截取字符串  
	public static String getNumbers2(String result) {  
		String regex = "+-*";
		Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(result);
		while(matcher.find()){
		    return matcher.group();
		}
	    return "";  
	}
	//截取字符串  
	public static String getNumbers4(String result) {  
		String regex = "Success";
		Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(result);
		while(matcher.find()){
			return matcher.group();
		}
		return "";  
	}
	
	//截取字符串  
	public static String getNumbers3(String result) {  
		String strProjectId=null;
		strProjectId = result.substring(41,result.length()-3);
		
		return strProjectId;  
	}
	//截取字符串  
	public static String getNumbers5(String email) {  
		String firstName=null;
		firstName = email.substring(0,email.lastIndexOf("@"));
		
		return firstName;  
	}
	//截取字符串  
	public static String getNumbers7(String email) {  
		String firstName=null;
		firstName = email.substring(email.lastIndexOf('@'));
		
		return firstName;  
	}
	
	public static String getContent2(String html) throws IOException {
		Document doc = Jsoup.parse(html);
		System.out.println("html:"+doc.html());
		Elements ets=doc.getElementsByTag("script");
		String jsoupResult=ets.get(ets.size()-2).html();
		/*Iterator<Element> it=ets.iterator();
		while(it.hasNext()){
			Element et=it.next();
			System.out.println("text:"+et.html());
		}*/
		return jsoupResult;
	}

	public static String getNumbers6(String email1) {
		String email=null;
		email = email1.substring(4,email1.length());
		
		return email;  
	}

	public static String getNumbers8(String fileName1) {
		String filename=null;
		filename = fileName1.substring(fileName1.lastIndexOf('@'));
		return filename;
	}
	//截取字符串  
		public static String getNumbers9(String result) {  
			String regex = "MAILER-DAEMON";
			Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(result);
			while(matcher.find()){
				return matcher.group();
			}
			return "";  
		}
		public static String getNumbers10(String list) {
			String list1=null;
			list1 = list.substring(list.length()-3,list.length());
			return list1;
		}
		public static String getNumbers11(String list) {
			String list1=null;
			list1 = list.substring(5,list.length());
			return list1;
		}
		public static String getNumbers12(String list) {
		String list1=list.replace("<", "").replace(">", "");
		return list1;
		}
	

	

}
