package com.cbt.util;

import com.cbt.bean.TypeBean;
import com.cbt.warehouse.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	public  static final  double PERGRAMSPECIAL = 0.08;
	public static final String CHINESE_CHAR = "([\\一-\\龥]+)",NUM_CHAR=",\\s*",
			CHAR_ONE=",\\s*\\[",CHAR_TWO="[',\";#\\:]",CHAT_FOUR="&lt;",CHAR_FIVE="&gt;",CHAT_SIX="(\\s+)",
			CHAR_SEV="(\\[.*\\])",CHAR_W="(\\W+)",CHAR_A="(\\(.*\\))",CHAR_B="(\\.\\s*)",CHAR_C="\\{";
	public static final String PIC_IP="104.247.194.50";
	public static final String PIC_PASS="importftp@123";
	public static final String PIC_USER="importweb";
	public static final String PIC_PATH="remotePath";
	public static final String PIC_URL="https://img.import-express.com/importcsvimg/inspectionImg/";
    //单位g 运费
    public static final double PERGRAM = 0.054;
	// 美元汇率
	public static final double EXCHANGE_RATE = 6.3;
	public static int str2Ip(String ip) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ip);
		byte[] bytes = address.getAddress();
		int a, b, c, d;
		a = byte2int(bytes[0]);
		b = byte2int(bytes[1]);
		c = byte2int(bytes[2]);
		d = byte2int(bytes[3]);
		int result = (a << 24) | (b << 16) | (c << 8) | d;
		return result;
	}

	public static int byte2int(byte b) {
		int l = b & 0x07f;
		if (b < 0) {
			l |= 0x80;
		}
		return l;
	}
	// 日期转换
	public static String beforNumDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, day);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}



	public static long ip2long(String ip) throws UnknownHostException {
		int ipNum = str2Ip(ip);
		return int2long(ipNum);
	}

	public static String getItemid(String u) {
		if (u.length() < 12) {// http://aaa&123
			return "0";
		}
		String ret = "";
		Pattern p = Pattern.compile("\\d{2,}");
		String maxStr = "";
		Matcher m = p.matcher(u);
		int i = 0;
		while (m.find()) {
			String temp = m.group();
			int c = u.indexOf(temp);
			int len = c + m.group().length() + 5;
			if (len > u.length()) {
				len = c + m.group().length();
			}
			temp = u.substring(c - 4, len);
			if (temp.indexOf("?id=") != -1 || temp.indexOf("&id=") != -1
					|| temp.indexOf(".html") != -1) {
				if (m.group().length() > maxStr.length()) {
					maxStr = m.group();
				}
			}
			i++;
		}
		ret = maxStr;
		return ret;
	}


	public static long int2long(int i) {
		long l = i & 0x7fffffffL;
		if (i < 0) {
			l |= 0x080000000L;
		}
		return l;
	}

	/**
	 * 从字符串单位中提取单位
	 * @param str
	 * @return
	 */
	public static int getNumberForStr(String str){
		int num=1;
		try{
			String regEx="[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			num=Integer.valueOf(m.replaceAll("").trim());
		}catch (Exception e){
			num=1;
		}
		return num;
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(ip2long("222.95.255.255"));
		System.out.println(getNumberForStr("piece"));
	}
	
	public static String currencyChange(String currency){
		if(currency!="" && !currency.equals("")){
			if(currency.equals("USD")){
				return "$";
			}else if(currency.equals("GBP")){
				return "￡";
			}else if(currency.equals("RMB")){
				return "￥";
			}else if(currency.equals("EUR")){
				return "€";
			}else if(currency.equals("CAD")){
				return "CAN$";
			}else if(currency.equals("AUD")){
				return "A$";
			}else{
				return "Unknowen Currency";
			}
	}
		return "EmptyParam";
	}


	public  static List<Map<String, String>> genQueryDate(String yearStr, String monthStr) {
		List<Map<String, String>> monthLst = new ArrayList<Map<String, String>>();
		int qyear = Integer.valueOf(yearStr);
		if (monthStr == null || "".equals(monthStr.trim()) || "-1".equals(monthStr.trim())) {
			// 有年份无月份的，创建当年的12个月,当前年的最大月份
			Calendar cal = Calendar.getInstance();
			int nyear = cal.get(Calendar.YEAR);
			int nmonth = cal.get(Calendar.MONTH);
			int total = 12;
			if (nyear == qyear) {
				total = nmonth + 1;
			}
			for (int i = 1; i <= total; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("year", yearStr);
				dataMap.put("month", String.valueOf(i));
				if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
					if (i < 10) {
						dataMap.put("beginDate", yearStr + "-0" + i + "-01 00:00:00");
						dataMap.put("endDate", yearStr + "-0" + i + "-31 23:59:59");
					} else {
						dataMap.put("beginDate", yearStr + "-" + i + "-01 00:00:00");
						dataMap.put("endDate", yearStr + "-" + i + "-31 23:59:59");
					}
				} else if (i == 2) {
					int year = Integer.valueOf(yearStr);
					if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
						// 是闰年
						dataMap.put("beginDate", yearStr + "-02-01 00:00:00");
						dataMap.put("endDate", yearStr + "-02-29 23:59:59");
					} else {
						// 不是闰年
						dataMap.put("beginDate", yearStr + "-02-01 00:00:00");
						dataMap.put("endDate", yearStr + "-02-28 23:59:59");
					}
				} else if (i == 4 || i == 6 || i == 9 || i == 11) {
					if (i < 10) {
						dataMap.put("beginDate", yearStr + "-0" + i + "-01 00:00:00");
						dataMap.put("endDate", yearStr + "-0" + i + "-30 23:59:59");
					} else {
						dataMap.put("beginDate", yearStr + "-" + i + "-01 00:00:00");
						dataMap.put("endDate", yearStr + "-" + i + "-30 23:59:59");
					}
				}
				monthLst.add(dataMap);
			}
		} else {
			int month = Integer.valueOf(monthStr);
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("year", yearStr);
			dataMap.put("month", monthStr);
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				if (month < 10) {
					dataMap.put("beginDate", yearStr + "-0" + month + "-01 00:00:00");
					dataMap.put("endDate", yearStr + "-0" + month + "-31 23:59:59");
				} else {
					dataMap.put("beginDate", yearStr + "-" + month + "-01 00:00:00");
					dataMap.put("endDate", yearStr + "-" + month + "-31 23:59:59");
				}
			} else if (month == 2) {
				int year = Integer.valueOf(yearStr);
				if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
					// 是闰年
					dataMap.put("beginDate", yearStr + "-02-01 00:00:00");
					dataMap.put("endDate", yearStr + "-02-29 23:59:59");
				} else {
					// 不是闰年
					dataMap.put("beginDate", yearStr + "-02-01 00:00:00");
					dataMap.put("endDate", yearStr + "-02-28 23:59:59");
				}
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				if (month < 10) {
					dataMap.put("beginDate", yearStr + "-0" + month + "-01 00:00:00");
					dataMap.put("endDate", yearStr + "-0" + month + "-30 23:59:59");
				} else {
					dataMap.put("beginDate", yearStr + "-" + month + "-01 00:00:00");
					dataMap.put("endDate", yearStr + "-" + month + "-30 23:59:59");
				}
			}
			monthLst.add(dataMap);
		}
		return monthLst;
	}

	/**
	 * 解析1688商品规格
	 * @Title getTypeList
	 * @Description TODO
	 * @param entype
	 * @return
	 * @return List<TypeBean>
	 */
	public static List<TypeBean> getTypeList(String entype, String rePath){
		List<TypeBean> list=new ArrayList<TypeBean>();
		entype=entype.replace("[", "");
		String str[]=entype.split("],");
		for (String s : str) {
			String type[]=s.trim().replace("]", "").split(",");
			TypeBean t=new TypeBean();
			for (String st : type) {
				String strs[]=st.split("=");
				if("id".equals(strs[0].trim()) && strs.length>1){
					t.setId(!com.cbt.common.StringUtils.isStrNull(strs[1])?strs[1]:null);
				}else if("type".equals(strs[0].trim()) && strs.length>1){
					t.setType(!com.cbt.common.StringUtils.isStrNull(strs[1])?strs[1]:null);
				}else if("value".equals(strs[0].trim()) && strs.length>1){
					t.setValue(!com.cbt.common.StringUtils.isStrNull(strs[1])?strs[1]:null);
				}else if("img".equals(strs[0].trim()) && strs.length>1){
					t.setImg(!com.cbt.common.StringUtils.isStrNull(strs[1])?(rePath+strs[1]):null);
				}
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * 数组去重
	 * @return
	 */
	public static List<String> getStr(String expressno){
		List<String> noList=new ArrayList<String>();
		if(StringUtil.isBlank(expressno)){
			return noList;
		}
		String []nos=expressno.split(",");
		for(String n:nos){
			if(!noList.contains(n)){
				noList.add(n);
			}
		}
		return noList;
	}

	/**
	 * @param catId
	 * @return 判断当前商品是否是带电 或者彩妆
	 */
	public  static boolean getThisCatIdIsSpecialStr(String catId){
		boolean isSpecial = false;
		String[] catIds = {"10204,1047893,123610009,727,728,123754001,82101,1042634,1043162,1043498,121702001,126182004,50906,126144003,126178001,1550,124186012,124188008,124936001,10331,122700010,122708008,122698011,1047903,1047904,1047905,1047996,122698010,124188009,124952002,124952003,124952005,124952007,125012003,704,720,724,725,3414,1032233,1032781,1032782,122314006,124000001,50903,1033984,1037149,702,10256,1042047,124736031,124742035,10246,1048186,122704001,123608002,124734039,726,122396005,122398004,1046691,121624001,123648002,123650001,123736004,123736005,123736006,123736007,123736008,123752002,123752003,123752004,123754004,123754005,123754006,123756001,123756003,123758003,123758004,123758005,123758006,123760002,123760003,123760004,123860004,124580001,124582001,124734030,124734031,124736033,124736056,124740027,124740028,124824005,124902001,124904002,124906001,124908001,122178001,122180001,122310007,122374007,123736003,123754002,123754003,123756002,123758001,123758002,124272012,124812002,1034778,1034779,1034780,1034782,1034783,1034784,1034785,1036812,1040898,1040900,1043093,1043094,1043095,1043504,1046942,124186007,1043175,1034758,1041576,1033966,1033967,1033971,1033973,1033975,123854005,123864004,123864005,126144004,126178002,126180001,123860006,126180002"};
		if(StringUtils.isBlank(catId)){
			//老的购物车商品没存这个字段，给个默认
			catId = "0";
		}
		String[] source_catids = catId.split(",");
		for(String catid:source_catids){
			if(org.apache.commons.lang.StringUtils.isNotBlank(catid)&& ArrayUtils.contains(catIds,catid)){
				isSpecial = true;
				break;
			}
		}
		return isSpecial;
	}


}