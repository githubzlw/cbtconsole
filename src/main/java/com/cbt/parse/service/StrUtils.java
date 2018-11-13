package com.cbt.parse.service;


import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(StrUtils.class);
	

	/**URL编码
	 * @date 2016年6月3日
	 * @author abc
	 * @param cntext
	 * @return  
	 */
	public static String urlEncoder(String cntext){
		if(cntext==null){
			return "";
		}
		System.err.println("cntext----------------"+cntext.replace(" ", ""));
		//%最先转换
		cntext = cntext.replace("%", "%25");
		cntext = cntext.replace(" ", "%20").replace("!", "%21");
		cntext = cntext.replace("　", "%20");
		cntext = cntext.replace("\"", "%22");
		cntext = cntext.replace("#", "%23").replace("$", "%24");
		cntext = cntext.replace("&", "%26").replace("'", "%27");
		cntext = cntext.replace("(", "%28").replace(")", "%29");
		cntext = cntext.replace("*", "%2A").replace("+", "%2B");
		cntext = cntext.replace(",", "%2C").replace("-", "%2D");
		cntext = cntext.replace(".", "%2E").replace("/", "%2F");
		cntext = cntext.replace(":", "%3A").replace(";", "%3B");
		cntext = cntext.replace("<", "%3C").replace("=", "%3D");
		cntext = cntext.replace(">", "%3E").replace("?", "%3F");
		cntext = cntext.replace("@", "%40");
		cntext = cntext.replace("[", "%5B").replace("\\", "%5C");
		cntext = cntext.replace("]", "%5D").replace("^", "%5E");
		cntext = cntext.replace("_", "%5F");
		cntext = cntext.replace("`", "%60");
		cntext = cntext.replace("{", "%7B").replace("}", "%7D");
		cntext = cntext.replace("|", "%7C").replace("~", "%7E");
		cntext = cntext.replace("『", "").replace("』", "");
		cntext = cntext.replace(" ", "");
		
		cntext = cntext.replaceAll("\\s+", "%20");
		return cntext;
		
	}
	
	/**URL解码
	 * @date 2016年6月3日
	 * @author abc
	 * @param entext
	 * @return  
	 */
	public static String urlDecoder(String entext){
		if(entext==null){
			return "";
		}
		//%25最后转换
		
		entext = entext.replace("%20"," ").replace( "%21","!");
		entext = entext.replace( "%22","\"");
		entext = entext.replace( "%23","#").replace( "%24","$");
		entext = entext.replace( "%26","&").replace( "%27","'");
		entext = entext.replace( "%28","(").replace( "%29",")");
		entext = entext.replace("%2A","*").replace( "%2B","+");
		entext = entext.replace("%2C",",").replace("%2D","-");
		entext = entext.replace("%2E",".").replace( "%2F","/");
		entext = entext.replace("%3A",":").replace( "%3B",";");
		entext = entext.replace("%3C","<").replace("%3D","=");
		entext = entext.replace("%3E",">").replace( "%3F","?");
		entext = entext.replace( "%40","@");
		entext = entext.replace( "%5B","[").replace( "%5C","\\");
		entext = entext.replace( "%5D","]").replace( "%5E","^");
		entext = entext.replace( "%5F","_");
		entext = entext.replace( "%60","`");
		entext = entext.replace( "%7B","{").replace( "%7D","}");
		entext = entext.replace( "%7C","|").replace( "%7E","~");
		entext = entext.replace("%25", "%");
		return entext;
		
	}
	
	/**字符串为null，或为空
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean isNullOrEmpty(String str){
		return str == null || str.isEmpty();
	}
	/**字符串不为null，不为空
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean isNotNullEmpty(String str){
		return str != null && !str.isEmpty();
	}
	/**字符串不为null的情况下，是否为空
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean isEmpty(String str){
		return str != null && str.isEmpty();
	}
	/**字符串是否为Null
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean isNull(String str){
		return str == null;
	}
	
	
	
	/**字符串是否为数字字符串
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean  isNum(String str){
		if(str == null){
			return false;
		}
		return Pattern.compile("(\\d+)").matcher(str).matches();
	}
	
	/**是否为带有小数点的数字字符串
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return  
	 */
	public static boolean isDotNum(String str){
		if(str == null)
		{
			return false;
		}
		return Pattern.compile("(\\d+\\.*\\d*)").matcher(str).matches();
		
	}
	
	/**输入的字符串是否匹配正则表达式
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @param reg
	 * @return  
	 */
	public static boolean isMatch(String str,String reg){
		if(str == null || reg == null || reg.isEmpty())
		{
			return false;
		}
		return Pattern.compile(reg).matcher(str).matches();
	}
	/**输入的字符串中是否包含满足正则表达式的字符串
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @param reg
	 * @return  
	 */
	public static boolean isFind(String str,String reg){
		if( str == null || isNullOrEmpty(reg) )
		{
			return false;
		}
		return Pattern.compile(reg).matcher(str).find();
	}
	
	/**
	 * 字符串是否为产品区间价格
	 * 
	 * @date 2016年4月28日
	 * @author abc
	 * @param str
	 * @return
	 */
	public static boolean isRangePrice(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		str = str.replaceAll("(\\s*-\\s*)", "-");
		return Pattern.compile("(\\d+(\\.\\d+){0,1}(-\\d+(\\.\\d+){0,1}){0,1})")
				.matcher(str).matches();
	}
	/**匹配满足正则的字符串
	 * @date 2016年5月9日
	 * @author abc
	 * @param str
	 * @param reg
	 * @return  
	 */
	public static String matchStr(String str,String reg){
		if( isNull(str) || isNull(reg) )
		{
			return "";
		}
		if( reg.indexOf("(") == -1 )
		{
			reg = "("+reg;
		}
		if( reg.indexOf(")") == -1 )
		{
			reg = reg+")";
		}
		Pattern p = Pattern.compile(reg); 
		Matcher m = p.matcher(str);
		if( m.find() )
		{  
			return m.group(1).toString();
		}
    	return "";
	}
	
    /**正则匹配相应格式的内容
     * @date 2016年5月9日
     * @author abc
     * @param pattern
     * @param page
     * @return  
     */
    public static List<String> matchStrList(String reg,String str){
    	List<String> mobj = new ArrayList<String>();
    	if(str!=null&&!str.isEmpty()){
    		Pattern p = Pattern.compile(reg); 
    		Matcher m = p.matcher(str);  
    		while(m.find()){ 
    			mobj.add(m.group(1));
    		}
    	}
    	return mobj;
    }
    
    public static int GetCharInStringCount(String text, String content) {
		if(!StrUtils.isNotNullEmpty(content)){
			return 0;
		}
		String str = content.replace(text, "");
		return (content.length() - str.length()) / text.length();

	}

}
