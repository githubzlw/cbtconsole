package com.cbt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPasswordUtil {

	public static boolean doCheck(String password) {
		// 判断密码长度
		if (password.length() < 8) {
			return false;
		}
		// 判断是否含有特殊字符
		String regExSpecial = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern ptSpecial = Pattern.compile(regExSpecial);
		Matcher mtSpecial = ptSpecial.matcher(password);
		if (!mtSpecial.find()) {
			return false;
		}
		// 判断是否含有英文字符
		String regExEnglish = "[a-zA-Z]";
		Pattern ptEnglish = Pattern.compile(regExEnglish);
		Matcher mtEnglish = ptEnglish.matcher(password);
		if (!mtEnglish.find()) {
			return false;
		}

		// 判断是否含有数字
		String regExNumber = "[0-9]";
		Pattern ptNumber = Pattern.compile(regExNumber);
		Matcher mtNumber = ptNumber.matcher(password);
		if (!mtNumber.find()) {
			return false;
		}
		return true;
	}

}
