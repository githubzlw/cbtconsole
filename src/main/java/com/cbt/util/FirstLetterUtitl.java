package com.cbt.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
public class FirstLetterUtitl {
	
	/**
	 * 
	 * @Title compilePriceListStr 
	 * @Description 过滤掉价格中的非法字符  有时间价格信息从页面取，会有乱七八糟的玩意，这里只取需要的字符类型 qiqing  2019/01/02
	 * @param str
	 * @return
	 * @return String
	 */
	public  static String compilePriceListStr(String str) {
		StringBuilder pricestr = new StringBuilder();
		Pattern compile = Pattern.compile("[(\\d+\\.\\d)|\\d|\\$|\\-|\\,]");
		Matcher matcher = compile.matcher(str);
		while (matcher.find()){
			pricestr.append(matcher.group());
		}
		return pricestr.toString();
	}

	/**产品名称首字母大写
	 * @param name
	 * @return
	 */
	public static String upperCaseProductName(String name){
		if(StringUtils.isBlank(name)){
			return name;
		}
		String[] names = name.split("(\\s+)");
		StringBuilder sb = new StringBuilder();
		for(String str : names){
			//小写字母开头的单词转为首字母大写
			if(StrUtils.isMatch(str, "([a-z]+.*)")){
				char[] charArray = str.toCharArray();  
				charArray[0] -= 32;  
				sb.append(String.valueOf(charArray)).append(" ");
			}else{
				sb.append(str).append(" ");
			}
		}
		return sb.toString().trim();
	}
public static String getNameNew(String enName, String categoryName) {
    	
		String chineseChar = "([\\一-\\龥]+)";//()表示匹配字符串，[]表示在首尾字符范围  从 \\一 到 \\龥字符之间，+号表示至少出现一次
		String signChar = "~`!@#%\\^\\*_-=\\+\\\\\\|\\]\\}\\[\\{\\\\\\|'\";\\?\\/";//去掉各种特殊字符
		String signChar2 = "([.><,!])";//匹配特殊符号 .><,!
		
		String enNameNew = enName
				.replaceAll(chineseChar, " ")
				.replaceAll(signChar, " ")
				.replaceAll(signChar2, " ")
				.replaceAll("\\s+"," ")
				.trim();
		// 标题短 就 在标题里面 加上 这个产品的 类别名1级2级3级
		if(enNameNew.split("(\\s+)").length < 6 && StringUtils.isNotBlank(enNameNew)){
			enNameNew = categoryName+" "+enNameNew;
		}
		
		//产品名称首字母大写
		enNameNew = FirstLetterUtitl.upperCaseProductName(enNameNew);
		
    	return enNameNew;
    }
}
