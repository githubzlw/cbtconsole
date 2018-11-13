package com.cbt.common;


import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;


/**
 * 字符串处理工具类
 * @author Tommy
 *
 */
public class StringUtils extends org.springframework.util.StringUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * JSON转Map
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> json2Map(String jsonStr){
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> map = null;
		try {
			map = objectMapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			logger.error("JSON转Map异常", e);
		}
		return map;
	}
	
	 public static boolean equals(String str1, String str2) {
	        return str1 == null ? str2 == null : str1.equals(str2);
	    }
	
	/**
	 * 用于实体类的toString方法
	 * @param object
	 * @return
	 */
	public static String objectToString(Object object){
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error("转JSON异常", e);
		}
		return json;
	}
	/**
	 * 使字符串第一个字符小写
	 * @param target
	 * @return
	 */
	public static String firstCharToLow(String target){
		String firstChar = null;
		if(target != null && !"".equalsIgnoreCase(target)){
			firstChar = target.substring(0, 1);
			target = firstChar.toLowerCase() + target.substring(1);
		}
		return target;
	}
	
	/**
	 * 使字符串数组转为list<String>
	 * @param target
	 * @return
	 */
	public static List<String> stringArr2List(String[] strArr){
		List<String> list = new ArrayList<String>();
		if(strArr != null){
			for(String str:strArr){
				list.add(str);
			}
		}
		return list;
	}
	
	/**
	 * 判断字符串是否为NULL或""
	 * @param str
	 * @return
	 */
	public static boolean isStrNull (String str){
		if(null == str || "".equals(str) || "null".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
     * 产生随机字符串
     */
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;

	/**
	 * 产生随机字符串
	 * @param length 长度
	 * @return
	 */
	public static final String randomString(int length) {
	         if (length < 1) {
	             return null;
	         }
	         if (randGen == null) {
	                randGen = new Random();
	                numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
	                   "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	                  //numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	                 }
	         char [] randBuffer = new char[length];
	         for (int i=0; i<randBuffer.length; i++) {
	             randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
	          //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
	         }
	         return new String(randBuffer);
	}
	
	
	  /**
	   * 左补齐一个特殊字符.
	   *
	   * Pad to a size of <code>size</code>.
	   *
	   * <pre>
	   * StringUtils.leftPad(null, *, *)     = null
	   * StringUtils.leftPad("", 3, 'z')     = "zzz"
	   * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	   * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	   * StringUtils.leftPad("bat", 1, 'z')  = "bat"
	   * StringUtils.leftPad("bat", -1, 'z') = "bat"
	   * </pre>
	   *
	   * @param str  the String to pad out, may be null
	   * @param size  the size to pad to
	   * @param padChar  the character to pad with
	   * @return left padded String or original String if no padding is necessary,
	   *  <code>null</code> if null String input
	   * @since 2.0
	   */
	  public static String leftPad(String str, int size, char padChar) {
	      if (str == null) {
	          return null;
	      }
	      int pads = size - str.length();
	      if (pads <= 0) {
	          return str; // returns original String when possible
	      }
	      return padding(pads, padChar).concat(str);
	  }

	  /**
	   * 左补齐字符串
	   *
	   * Pad to a size of <code>size</code>.
	   *
	   * <pre>
	   * StringUtils.leftPad(null, *, *)      = null
	   * StringUtils.leftPad("", 3, "z")      = "zzz"
	   * StringUtils.leftPad("bat", 3, "yz")  = "bat"
	   * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
	   * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
	   * StringUtils.leftPad("bat", 1, "yz")  = "bat"
	   * StringUtils.leftPad("bat", -1, "yz") = "bat"
	   * StringUtils.leftPad("bat", 5, null)  = "  bat"
	   * StringUtils.leftPad("bat", 5, "")    = "  bat"
	   * </pre>
	   *
	   * @param str  the String to pad out, may be null
	   * @param size  the size to pad to
	   * @param padStr  the String to pad with, null or empty treated as single space
	   * @return left padded String or original String if no padding is necessary,
	   *  <code>null</code> if null String input
	   */
	  public static String leftPad(String str, int size, String padStr) {
	      if (str == null) {
	          return null;
	      }
	      if (isStrNull(padStr)) {
	          padStr = " ";
	      }
	      int padLen = padStr.length();
	      int strLen = str.length();
	      int pads = size - strLen;
	      if (pads <= 0) {
	          return str; // returns original String when possible
	      }
	      if (padLen == 1) {
	          return leftPad(str, size, padStr.charAt(0));
	      }

	      if (pads == padLen) {
	          return padStr.concat(str);
	      } else if (pads < padLen) {
	          return padStr.substring(0, pads).concat(str);
	      } else {
	          char[] padding = new char[pads];
	          char[] padChars = padStr.toCharArray();
	          for (int i = 0; i < pads; i++) {
	              padding[i] = padChars[i % padLen];
	          }
	          return new String(padding).concat(str);
	      }
	  }
	  
	  /**
	   * 右补齐一个特殊字符.
	   *
	   * Pad to a size of <code>size</code>.
	   *
	   * <pre>
	   * StringUtils.rightPad(null, *, *)     = null
	   * StringUtils.rightPad("", 3, 'z')     = "zzz"
	   * StringUtils.rightPad("bat", 3, 'z')  = "bat"
	   * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
	   * StringUtils.rightPad("bat", 1, 'z')  = "bat"
	   * StringUtils.rightPad("bat", -1, 'z') = "bat"
	   * </pre>
	   *
	   */
	  public static String rightPad(String str, int size, char padChar) {
	      if (str == null) {
	          return null;
	      }
	      int pads = size - str.length();
	      if (pads <= 0) {
	          return str; // returns original String when possible
	      }
	      return str.concat(padding(pads, padChar));
	  }

	  /**
	   * 右补齐字符串
	   *
	   * Pad to a size of <code>size</code>.
	   *
	   * <pre>
	   * StringUtils.rightPad(null, *, *)      = null
	   * StringUtils.rightPad("", 3, "z")      = "zzz"
	   * StringUtils.rightPad("bat", 3, "yz")  = "bat"
	   * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
	   * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
	   * StringUtils.rightPad("bat", 1, "yz")  = "bat"
	   * StringUtils.rightPad("bat", -1, "yz") = "bat"
	   * StringUtils.rightPad("bat", 5, null)  = "  bat"
	   * StringUtils.rightPad("bat", 5, "")    = "  bat"
	   * </pre>
	   *
	   */
	  public static String rightPad(String str, int size, String padStr) {
	      if (str == null) {
	          return null;
	      }
	      if (isStrNull(padStr)) {
	          padStr = " ";
	      }
	      int padLen = padStr.length();
	      int strLen = str.length();
	      int pads = size - strLen;
	      if (pads <= 0) {
	          return str; // returns original String when possible
	      }
	      if (padLen == 1) {
	          return rightPad(str, size, padStr.charAt(0));
	      }

	      if (pads == padLen) {
	          return str.concat(padStr);
	      } else if (pads < padLen) {
	          return str.concat(padStr.substring(0, pads));
	      } else {
	          char[] padding = new char[pads];
	          char[] padChars = padStr.toCharArray();
	          for (int i = 0; i < pads; i++) {
	              padding[i] = padChars[i % padLen];
	          }
	          return  str.concat(new String(padding));
	      }
	  }
	  
	  /**
	   * 返回一个给定次数的重复字符串
	   * to a given length.
	   *
	   * <pre>
	   * StringUtils.padding(0, 'e')  = ""
	   * StringUtils.padding(3, 'e')  = "eee"
	   * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
	   * </pre>
	   *
	   * Note: this method doesn't not support padding with
	   * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
	   * as they require a pair of <code>char</code>s to be represented.
	   * If you are needing to support full I18N of your applications
	   * consider using {@link #repeat(String, int)} instead. 
	   * 
	   *
	   * @param repeat  number of times to repeat delim
	   * @param padChar  character to repeat
	   * @return String with repeated character
	   * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
	   * @see #repeat(String, int)
	   */
	  private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
	      if (repeat < 0) {
	          throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
	      }
	      final char[] buf = new char[repeat];
	      for (int i = 0; i < buf.length; i++) {
	          buf[i] = padChar;
	      }
	      return new String(buf);
	  }	
	
	public static void main(String[] args) {
		List<String> list1 = new ArrayList<String>();
		list1.add("张三");
		list1.add("李四");
		list1.add("王五");
		List<String> list2 = new ArrayList<String>();
		list2.add("小六");
		list2.add("小七");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list1", list1);
		map.put("list2",list2);
//		System.out.println(objectToString(map));		
//		System.out.println(leftPad("1", 3, "0"));
		
/*		String json = "{\"w2_expires_in\": 1800,\"taobao_user_id\": \"3605353356\",\"taobao_user_nick\": \"sandbox_zj_001\",\"w1_expires_in\": 12960000,\"re_expires_in\": 15552000,\"r2_expires_in\": 259200,\"expires_in\": 12960000,\"token_type\": \"Bearer\",\"refresh_token\": \"620062270f5caed005a2bc3d763953ZZeea105af7211d473605353356\",\"access_token\": \"6200922f7e28e1a09768ae23f8cd08ZZ51bbc85257dbf8d3605353356\",\"r1_expires_in\": 12960000} ";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String,String> map = objectMapper.readValue(json, Map.class);
			System.out.println(map.get("taobao_user_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 替换html标签特殊字符
	 * @param str
	 * @return str
	 */
	public static String replaceHtmlCh(String str){
		
		str = str.replace("&","&amp;");   
        str = str.replace("<","&lt;");   
        str = str.replace(">","&gt;");   
//        str = str.replace("\\","&#39;");   
//        str = str.replace("\"","&quot;");
		return str;
	}
	
	/**
	 * 将html标签特殊字符还原
	 * @param str
	 * @return str
	 */
	public static String reverseReplaceHtmlCh(String str){
//		str = str.replace("&amp;","&");   
//        str = str.replace("&lt;","<");   
//        str = str.replace("&gt;",">");   
//        str = str.replace("&#39;","\\");   
//        str = str.replace("&quot;","\"");
		return str;
	}
	
	/**
	 * 去除字符串中除中间空格外的特殊字符
	 * @author 吴鹏
	 * @param str 需要处理的字符串
	 * @return 处理后返回的字符串
	 */
	public static String deleteWhitespace(String str)
	 {
	      if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
	        return str;
	      }else {
	    	  str = str.trim();
	      }
	      
	      int sz = str.length();
	      char[] chs = new char[sz];
	      int count = 0;
	      for (int i = 0; i < sz; ++i) {
	      if (!(Character.isWhitespace(str.charAt(i))) || str.charAt(i) == 0x0020) {
	          chs[(count++)] = str.charAt(i);
	        }
	      }
	      
	      if (count == sz) {
	       return str;
	      }
	      return new String(chs, 0, count);
	  }
	
	public static String formatPrice(Integer places, BigDecimal bigDecimal){
		if(bigDecimal == null){
			return "";
		}
		bigDecimal = bigDecimal.setScale(places, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.toString();
	}
	
	/**
	 * 默认设置null的字符串为空字符串
	 * @author 吴鹏
	 * @param str 传入的字符串
	 * @return 处理过后的字符串
	 */
	public static String defaultEmpty(String str){
		return (str == null) ? "" : str;
	}
	
	
	public static String getFileName(String filename){
		Date date = new Date(System.currentTimeMillis());
		int mon = date.getMonth() + 1;
		int day = date.getDate();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();
		filename=mon < 10?(filename += "0" + mon):(filename += mon);
		filename=day < 10?(filename += "0" + day):(filename += day);
		filename=hour < 10?(filename += "0" + hour):(filename += hour);
		filename=min < 10?(filename += "0" + min):(filename += min);
		filename=sec < 10?(filename += "0" + sec):(filename += sec);
		filename += ".xls";
		return filename;
	}
}
