package com.cbt.warehouse.util;

import com.cbt.util.AppConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utility {
	
	public static String datePattern1 = "yyyy-MM-dd HH:mm:ss";
	
	public static boolean getStringIsNull(String str){
		if(str!=null&&!str.trim().equals("")){
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
                String strIp = ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }   
    
    /** 
     * orderNo订单号
     * amount支付金额
     * sign=Md5Util.encoder(AppConfig.paypal_business + userid + orderNo + amount)
     * custom=userid + "@" + paySID
     * payglag支付说明：运费or订单支付
     * type额外参数
     * currency货币单位
     */  
    public static String  getForm(String username, String orderNo, String amount, String sign, String custom, String payglag,String type,String currency, String balancePay){
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
		if(type.indexOf(orderNo) > -1){
			orderNo = type.substring(type.lastIndexOf("@")+1, type.length());
		}
		//custom:userinfo[0]+"@"+paySID@支付说明N@是否余额支付@商品总金额
		sb.append("<input id='return' type='hidden' name='return' value='"+AppConfig.path+"/cbt/paysuccess.jsp?orderid="+orderNo+"&amount="+amount+"&currency="+currency+"'>");
		sb.append("<input id='cancel_return' type='hidden' name='cancel_return' value='"+AppConfig.path+"/cbt/payfailed.jsp'>");
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
		/*map.put("EUR", "€");
		map.put("CAD", "C$");
		map.put("GBP", "UK ￡");
		map.put("AUD", "AU $");
		map.put("USD", "US $");*/
		map.put("EUR", "€");
		map.put("CAD", "$");
		map.put("GBP", "￡");
		map.put("AUD", "$");
		map.put("USD", "$");
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
		if(str == null)return false;
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
	public static boolean getIsDouble(Object str){
		try {
			 Double.parseDouble(str.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/*public static double getService_fee(double product_cost){
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
	}*/
	
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
	
	public static String currencyChange(String currency){
		if(currency!=null&&currency!="" && !currency.equals("")){
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
	
	public static BigDecimal mul(double v1, double v2) {  
		  BigDecimal b1 = new BigDecimal(Double.toString(v1));
		  BigDecimal b2 = new BigDecimal(Double.toString(v2));  
		  return b1.multiply(b2).setScale(2,   BigDecimal.ROUND_HALF_UP);  
	}  
	
	/**
	 * 比较两个时间大小
	 * @return
	 */
    public static int compareDate(String smdate,String bdate){  
    	
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		try {
			Date now = sdf.parse(smdate);
	        Date endDate= sdf.parse(bdate);
	        if (now.getTime() > endDate.getTime()) {
	        	return 1;
	        }else if (now.getTime() < endDate.getTime()) {
	            return -1;
	        }else{
	        	return 0;
	        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    } 
    
    
    
    /**
     * 根据URL 获取网页数据
     * 
     */
    public static  String fun(String urlString) {
 		urlString = urlString.replaceAll(" ", "");
 		StringBuilder json = new StringBuilder();
 		try {
 			URL urlObject = new URL(urlString);
 			URLConnection uc = urlObject.openConnection();

 			BufferedReader in = new BufferedReader(new InputStreamReader(
 					uc.getInputStream(), "GBK"));
 			String inputLine = null;
 			while ((inputLine = in.readLine()) != null) {
 				json.append(inputLine);
 			}
 			in.close();
 			uc=null;
 			urlObject=null;
 		} catch (MalformedURLException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}

 		// Document doc=null;
 		String str = json.toString();
 		return str;
 	}
    
    
    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static  int daysBetween(Date smdate,Date bdate) throws Exception    
    {    
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }  
}	
