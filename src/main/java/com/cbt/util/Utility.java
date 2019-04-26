package com.cbt.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	
	public static String datePattern1 = "yyyy-MM-dd HH:mm:ss";
	
	public static boolean getStringIsNull(String str){
		if(str!=null&&!str.trim().equals("")&&!"null".equals(str)){
			return true;
		}
		return false;
	}
	public static boolean getStringIsNull(List<String> str){
		if(str!=null&&str.size()>0){
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
	
	/** 
     * 使用参数Format格式化Date成字符串 
     */  
    public static String format(Date date, String pattern)  
    {  
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);  
    } 
    
    public final static String getIpAddress(HttpServletRequest request) throws IOException {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");   
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");   
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");    
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }

	/**
	 * 商品下架原因
	 * @param type
	 * @param unsellableReason
	 * @return
	 */
	public static String getUnsellableReason(String type,String unsellableReason){
		if("1".equals(type)){
			unsellableReason="1688货源下架";
		}else if("4".equals(type)){
			unsellableReason="页面404";
		}else if("6".equals(type)){
			unsellableReason="IP问题或运营直接下架";
		}else if("8".equals(type)){
			unsellableReason="采样不合格";
		}else if("9".equals(type)){
			unsellableReason="有质量问题";
		}else if("10".equals(type)){
			unsellableReason="商品侵权";
		}else if("11".equals(type)){
			unsellableReason="店铺侵权";
		}else if("14".equals(type)){
			unsellableReason="1688商品货源变更";
		}else{
			unsellableReason="其他原因";
		}
		return unsellableReason;
	}

	public static double getMaxPrice(String[] prices) {
		double maxPrice = 0.00;
		for (int i = 0; i < prices.length; i++) {
			double price = Double.valueOf(prices[i].toString());
			if (maxPrice < price) {
				maxPrice = price;
			}
		}
		return maxPrice;
	}

	public static String getItemid(String u) {
		if (u.length() < 12) {// http://aaa&123
			return "0";
		}
		String ret = "";
		Pattern p = Pattern.compile("\\d{2,}");// 这个2是指连续数字的最少个数
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
			if (temp.indexOf("?id=") != -1 || temp.indexOf("&id=") != -1 || temp.indexOf(".html") != -1) {
				if (m.group().length() > maxStr.length()) {
					maxStr = m.group();
				}
			}
			i++;
		}
		ret = maxStr;
		return ret;
	}

	/**
	 * 根据简写的库位拼装新的库位
	 *
	 * @param barcode
	 *            简写库位
	 * @return
	 */
	public static String getBarcode(String barcode) {
		StringBuffer bar = new StringBuffer("sh");
		if (barcode.length() == 5) {
			for (int i = 0; i < barcode.length(); i++) {
				char item = barcode.charAt(i);
				if (i < 2) {
					bar.append(item);
				} else {
					bar.append("00").append(item);
				}
			}
		} else if (barcode.length() == 6) {
			for (int i = 0; i < barcode.length(); i++) {
				char item = barcode.charAt(i);
				if (i < 2) {
					bar.append(item);
				} else if (i == 2 || i == 3) {
					bar.append(item);
				} else {
					bar.append("00").append(item);
				}
				if (i == 1) {
					bar.append("0");
				}
			}
		}
		return bar.toString();
	}
    
    public static String  getForm(String username, String orderNo, String amount, String sign, String custom, String payglag,String type,String currency){
    	StringBuffer sb=new StringBuffer();
    	sb.append("<form id='pay2' action='"+AppConfig.paypal_action+"' method='post'>");
		sb.append("<input type='hidden' name='cmd' value='_xclick'>");
		sb.append("<input type='hidden' name='business' value='"+AppConfig.paypal_business+"'>");
		sb.append("<input type='hidden' name='lc' value='US'>");
		sb.append("<input id='item_name' type='hidden' name='item_name' value='"+username+"'>");
		sb.append("<input id='item_number' type='hidden' name='item_number' value='"+orderNo+"'>");
		sb.append("<input id='amount' type='hidden' name='amount' value='"+amount+"'>");
		sb.append("<input type='hidden' name='currency_code' value='"+currency+"'>");
		sb.append("<input type='hidden' name='button_subtype' value='services'>");
		sb.append("<input type='hidden' name='no_note' value='3'>");
		sb.append("<input type='hidden' name='no_shipping' value='3'>");
		sb.append("<input type='hidden' name='rm' value='3'>");
		/*String[] orderNews = Utility.getStringIsNull(type) ? type.split("@"):null;
		String orderNew = orderNews.length > 2 ? orderNews[2] : orderNo;*/
		if(type.indexOf(orderNo) > -1){
			orderNo = type.substring(type.lastIndexOf("@")+1, type.length());
		}
		sb.append("<input id='return' type='hidden' name='return' value='"+AppConfig.ip+"/cbt/paysuccess.jsp?orderid="+orderNo+"&amount="+amount+"&currency="+currency+"'>");
		sb.append("<input id='cancel_return' type='hidden' name='cancel_return' value='"+AppConfig.ip+"/cbt/payfailed.jsp'>");
		sb.append("<input type='hidden' name='notify_url' value='"+AppConfig.paypal_notifyurl+"'>");
		sb.append("<input id='custom' type='hidden' name='custom' value='"+custom+"@"+sign+"@"+payglag+"@"+type+"'>");
		sb.append("<input type='hidden' name='shipping' value='0.00'>");
		sb.append("<input type='hidden' name='bn' value='PP-BuyNowBF:btn_buynowCC_LG.gif:NonHosted'>");
		sb.append("</form>");
		return sb.toString();
    }
    
    //根据自增id获取订单号
    public static final String generateOrderNumber(long seqNo){
		Calendar c		= Calendar.getInstance();
		char year		= (char)('A' + (c.get(Calendar.YEAR) % 26));
		int m			= c.get(Calendar.MONTH) + 1;
		char month		= (char)(m < 10 ? '0' + m : 'A' + m - 10);
		String day		= String.format("%02d", c.get(Calendar.DAY_OF_MONTH));
		String seq		= String.format("%02d", seqNo % 100);
		String serial	= String.format("%08d", c.getTimeInMillis() % 100000000);
		String random	= String.format("%02d", new Random(seqNo).nextInt(100));

		return new StringBuilder(16)
					.append(year)
					.append(month)
					.append(day)
					.append(seq)
					.append(serial)
					.append(random)
					.toString();
	}
	
	public static String formatPrice(String price){
		if(price == null || "".equals(price) || "null".equals(price)){
			return "0.00";
		}
		return price;
	}
	
	public static List<String> getState(String key){
		List<Map<String,List<String>>>list=setState();
		for(Map<String,List<String>> map:list){
			for(String keystate : map.keySet()){
				if(key.equals(keystate)){
					return map.get(keystate);
				}
			}
		}
		return null;
	}
	
	public static List<Map<String,List<String>>> setState(){
		List<Map<String,List<String>>>list=new ArrayList<Map<String, List<String>>>();
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		List<String> liststate=new ArrayList<String>();
		liststate.add("Australian Capital Territory");
		liststate.add("New South Wales");
		liststate.add("Northern Territory");
		liststate.add("Queensland");
		liststate.add("South Australia");
		liststate.add("Tasmania");
		liststate.add("Victoria");
		liststate.add("Western Australia");
		map.put("2", liststate);
		List<String> liststate2=new ArrayList<String>();
		liststate2.add("Eastern Cape");
		liststate2.add("Free State");
		liststate2.add("Gauteng");
		liststate2.add("KwaZulu-Natal");
		liststate2.add("Limpopo");
		liststate2.add("Mpumalanga");
		liststate2.add("North West");
		liststate2.add("Northern Cape");
		liststate2.add("Western Cape");
		map.put("29", liststate2);
		List<String> liststate3=new ArrayList<String>();
		liststate3.add("England");
		liststate3.add("Northern Ireland");
		liststate3.add("Scotland");
		liststate3.add("Wales (Cymru)");
		map.put("35", liststate3);
		list.add(map);
		return list;
	}
	
	public static Map<String, String> getCountry(){
		Map<String,String> map=new TreeMap<String,String>();
		map.put("2", "Australia");
		map.put("29", "South Africa");
		map.put("35", "United Kingdom");
		return map;
	}
	
	public static String getCurrency(String fh){
		Map<String,String> map=new HashMap<String,String>();
		map.put("EUR", "€");
		map.put("CAD", "C$");
		map.put("GBP", "UK ￡");
		map.put("AUD", "AU $");
		map.put("USD", "US $");
		return map.get(fh);
	}
	
	/*public static String getGoodsType(String type){
		String goodstype = "";
		String[] types = {"clothing","bags","watches","shoes","mobile phone","jewelry"};
		for (int i = 0; i < types.length; i++) {
			if(type.toLowerCase().indexOf(types[i]) > -1){
				goodstype = types[i];
				if(i == 4){
					goodstype = "mobile_phone";
				}
			}
		}
		return goodstype;
	}
	*/
	public static String getString(String str){
		if(str!=null && !("").equals(str)){
			return str;
		}
		return "0";
	}
	
	public static Integer getInt(String str){
		if(str!=null && !("").equals(str)){
			return Integer.parseInt(str);
		}
		return null;
	}
	
	public static String formatObject(Object ob){
		if(ob == null){
			return "";
		}
		return ob.toString();
	}
	
	public static boolean getIsInt(String str){
		boolean result=str.matches("[0-9]+");
		return result;
	}
	 
	public static boolean getIsDouble(String str){
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static double getService_fee(double product_cost){
		double service_fee = 0;
		if (product_cost < 1000) {
			service_fee = (float) (product_cost * 0.08);
		} else if (product_cost < 3000) {
			service_fee = (float) (product_cost * 0.06);
		} else if (product_cost < 5000) {
			service_fee = (float) (product_cost * 0.04);
		} else if (product_cost < 10000) {
			service_fee = (float) (product_cost * 0.03);
		} else {
			service_fee = (float) (product_cost * 0.025);
		}
		BigDecimal service_fee_ = new BigDecimal(service_fee).setScale(2, BigDecimal.ROUND_HALF_UP);
		return service_fee_.doubleValue();
	}
	
	/**
	 * 生成数字型长度为【length】的随机数
	 * @param length
	 * @return
	 */
	public static String genNumericalRandom(int length) {
		Random rm = new Random();
		// 获得随机数
		double pross = rm.nextDouble();
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(2, length + 2);
	}
	/**
	 * @Title: ipToLong
	 * @Author: cjc
	 * @Despricetion: ip 转 long
	 * @Date: 2019/4/26 11:09:36
	 * @Param: [ip]
	 * @Return: long
	 */
	public static long ipToLong(String ip) {
		String[] ipArray = ip.split("\\.");
		List ipNums = new ArrayList();
		for (int i = 0; i < 4; ++i) {
			ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
		}
		long ZhongIPNumTotal = ((Long) ipNums.get(0)).longValue() * 256L * 256L * 256L
				+ ((Long) ipNums.get(1)).longValue() * 256L * 256L + ((Long) ipNums.get(2)).longValue() * 256L
				+ ((Long) ipNums.get(3)).longValue();

		return ZhongIPNumTotal;
	}
	/**
	 * @Title: getIP
	 * @Author: cjc
	 * @Despricetion: long 转 ip
	 * @Date: 2019/4/26 11:26:28
	 * @Param: [ipaddr]
	 * @Return: java.lang.String
	 */
	public static String getIP(long ipaddr) {
		long y = ipaddr % 256;
		long m = (ipaddr - y) / (256 * 256 * 256);
		long n = (ipaddr - 256 * 256 *256 * m - y) / (256 * 256);
		long x = (ipaddr - 256 * 256 *256 * m - 256 * 256 *n - y) / 256;
		return m + "." + n + "." + x + "." + y;
	}

}	
