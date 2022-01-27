package com.cbt.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

public class StrUtils {
	

	
	/**Object转数字String
	 * @param object
	 * @return
	 */
	public static String object2NumStr(Object object){
		if(object == null){
			return "0";
		}
		if(!isMatch(object.toString(), "(\\d+)")){
			return "0";
		}
		return object.toString();
	}
	/**Object转小数String
	 * @param object
	 * @return
	 */
	public static String object2DotNumStr(Object object){
		if(object == null){
			return "0";
		}
		if(!isMatch(object.toString(), "(\\d+\\.\\d+)")){
			return "0";
		}
		return object.toString();
	}
	/**Object转String
	 * @param object
	 * @return
	 */
	public static String object2Str(Object object){
		
		return object == null ? "" : object.toString();
	}
	/**Object转价格String
	 * @param object
	 * @return
	 */
	public static String object2PriceStr(Object object){
		if(object == null){
			return "0";
		}
		if(!isMatch(object.toString(), "(\\d+\\.*\\d*)")){
			return "0";
		}
		
		return object.toString();
	}
	
	
	
	
	
	/**获取当前时间之前几天的时间
	 * @date 2016年3月1日
	 * @author abc
	 * @param days
	 * @return  
	 */
	public static String getTimeBefore(double days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 Date date = new Date();
	     return df.format(new Date((long) (date.getTime() - days * 24 * 60 * 60 * 1000)));
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
	
	public static double getRate(String catid){
		if(StringUtils.isBlank(catid)){
			return 3.5;
		}
		catid = catid.startsWith(",") ? catid.substring(1) : catid;
		catid = catid.endsWith(",")?catid.substring(0,catid.length()-1) : catid;
		String[] catids = catid.split(",");
		int catidsLength = catids.length;
		if(catidsLength>2&&("1509".equals(catids[2]) || "26".equals(catids[2])|| "200002086".equals(catids[2])
				||"200000293".equals(catids[2])||"200000408".equals(catids[2])||"1704".equals(catids[2])
				||"200020001".equals(catids[2])||"40601".equals(catids[2])||"39050502".equals(catids[2])
				||"1504".equals(catids[2])||"200002296".equals(catids[2]) ||"200003045".equals(catids[2])
				||"200154001".equals(catids[2]))){
			return 3.5;
		}
		if(catidsLength>1&&("1509".equals(catids[1]) || "26".equals(catids[1])|| "200002086".equals(catids[1])
				||"200000293".equals(catids[1])||"200000408".equals(catids[1])||"1704".equals(catids[1])
				||"200020001".equals(catids[1])||"40601".equals(catids[1])||"39050502".equals(catids[1])
				||"1504".equals(catids[1])||"200002296".equals(catids[1]) ||"200003045".equals(catids[1])
				||"200154001".equals(catids[1]))){
			return 3.5;
		}
		if(catidsLength>0&&("1509".equals(catids[0]) || "26".equals(catids[0])|| "200002086".equals(catids[0])
				||"200000293".equals(catids[0])||"200000408".equals(catids[0])||"1704".equals(catids[0])
				||"200020001".equals(catids[0])||"40601".equals(catids[0])||"39050502".equals(catids[0])
				||"1504".equals(catids[0])||"200002296".equals(catids[0]) ||"200003045".equals(catids[0])
				||"200154001".equals(catids[0]))){
			return 3.5;
		}
		return 3.5;
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
		return Pattern.compile("(\\d+)").matcher(str.trim()).matches();
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
	
	/**匹配满足正则的字符串
	 * @date 2016年5月9日
	 * @author abc
	 * @param str
	 * @param reg
	 * @return  
	 */
	public static String matchStr(String str,String reg){
		if( StringUtils.isBlank(str) || StringUtils.isBlank(reg) )
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
    	if(str != null && !str.isEmpty()){
    		Pattern p = Pattern.compile(reg,Pattern.DOTALL); 
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
	
	/**  
     * 该方法主要使用正则表达式来判断字符串中是否包含字母  
     * @author 2018年4月25日   
     * @param cardNum 待检验的原始卡号  
     * @return 返回是否包含  
     */  
    public static boolean judgeContainsStr(String cardNum) {  
        String regex=".*[a-zA-Z]+.*";  
        Matcher m=Pattern.compile(regex).matcher(cardNum);  
        return m.matches();  
    }
    
    /**去除中文字符与符号
	 * @return
	 */
	public static String removeChineseCode(String str) {
		String result = str;
		if(StringUtils.isNotBlank(str)) {
			return str.replaceAll("[\u4e00-\u9fa5]+", " ").replaceAll("[\u2e80－\ua4cf]+", " ")
					.replaceAll("[\uf900-\ufaff]+", " ").replaceAll("[\ufe30-\ufe4f]+"," ")
					.replaceAll("[。，《》？、（）……￥！·——‘’“”｛｝【】：；{}\\+\\=\\?\\:;\\^#@\\!~`><\\\t\\\n,\\[\\]]", " ");
		}
		return result;
	}
	
	/**去除英文符号
	 * @param str
	 * @return
	 */
	public static String removeSpecialCode(String str) {
		String result = str;
		if(StringUtils.isNotBlank(str)) {
			return str.replaceAll("[\\[\\]\\!\\@#\\$%\\^&\\*\\(\\)_\\+\\-\\=\\|\\?/>,<\\:;'\"{}~`。，《》？、（）……￥！·——‘’“”｛｝【】：；\\\t\\\n]+", " ");
		}
		return result;//\\.
	}
	/**针对批发价格特殊处理字符
	 * @param str
	 * @return
	 */
	public static String removeSpecialCodeForWprice(String str) {
		String result = str;
		if(StringUtils.isNotBlank(str)) {
			return str.replaceAll("[\\!\\@#%\\^&\\*\\(\\)_\\+\\=\\|\\?/><\\:;'\"{}~`。，《》？、（）……￥！·——‘’“”｛｝【】：；\\\t\\\n]+", " ");
		}
		return result;//\\.
	}

	public static <T> List<T> getPersons(String jsonString, Class cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
		}
		return list;
	}
	public static void main(String[] args) {
		
		System.out.println(removeSpecialCodeForWprice("[1-2 $ 4.00,3-10 $ 3.80,≥11 $ 3.00]"));
	}
}