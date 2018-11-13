package com.cbt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherPhoneNumber {
	/**
	 * 提取电话号码,返回结果是逗号隔开的多个电话号码
	 * 
	 * @param str
	 * @return
	 */
	public static String getAllTel(String str) {
		StringBuffer tel = new StringBuffer();
		String date = "\\d{4}[\\.|/|-]\\d{1,2}[\\.|/|-]\\d{1,2}";
		String time = "\\d{1,2}:\\d{1,2}:?\\d{1,2}?";
		// 去掉 相邻两个数字相加
		String add = "\\d{1,3}\\+\\d{1,3}?";
		str = str.replaceAll(date, "");
		str = str.replaceAll(time, "");
		str = str.replaceAll(add, "");
		String res = "";

		res = getTelFromString(str);
		while (res != null && !"".equals(res)) {
			tel.append(res + ";");
			String res2 = res;
			str = str.replace(res, "");
			res = getTelFromString(str);
			if (res2.equals(res)) {
				res = null;
			}
		}
		return tel.toString();
	}

	public static String getTelFromString(String str) {
		String pos = str; // 寻找当前数字段位置专用
		String tel = "";
		int ind = -1;
		int i = 0;
		String reg1 = "[\\(]?\\d+( / )?[\\)]?[\\.|/|\\-| ]?";
		StringBuffer bf = new StringBuffer();
		Pattern pattern = Pattern.compile(reg1);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			try {
				String m = matcher.group();
				String num = m;
				if (i == 0) {
					ind = str.indexOf(m);
				} else {
					ind = str.indexOf(bf.toString()) + bf.length(); // 已拼接数字末位置
				}
				int now_index = pos.indexOf(m); // 当前截取数字所在位置
				if (i == 0) {
					bf.append(m);
					// 把拼接过的数字段替换掉，避免后面出现相同数字段时获取错索引
					String str_star = "";
					for (int star = 0; star < m.length(); star++) {
						str_star += "a";
					}
					// 把数字段里的特殊符号替换掉，以免被识别为正则符号
					num = num.replace("(", "\\(");
					num = num.replace(")", "\\)");
					num = num.replace("-", "\\-");
					num = num.replace(".", "\\.");
					pos = pos.replaceFirst(num, str_star);
				} else {
					if (ind == now_index || ind == now_index - 1) {
						bf.append(m);
						String str_star = "";
						for (int star = 0; star < m.length(); star++) {
							m.replace("(", "z");
							str_star += "a";
						}
						num = num.replace("(", "\\(");
						num = num.replace(")", "\\)");
						num = num.replace("-", "\\-");
						num = num.replace(".", "\\.");
						pos = pos.replaceFirst(num, str_star);
					} else if (bf.toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "")
							.replace("/", "").length() > 7) {
						break;
					} else {
						i = 0;
						str = str.substring(ind);
						bf.delete(0, bf.length());
						bf.append(m);
						pos = str;
						String str_star = "";
						for (int star = 0; star < m.length(); star++) {
							str_star += "a";
						}
						num = num.replace("(", "\\(");
						num = num.replace(")", "\\)");
						num = num.replace("-", "\\-");
						num = num.replace(".", "\\.");
						pos = pos.replaceFirst(num, str_star);
					}
				}
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return tel.trim();
	}
}
