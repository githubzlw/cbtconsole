package com.cbt.warehouse.util;

import com.cbt.util.Md5Util;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**判断url类型（即从哪个网站来解析数据）
 * @author abc
 *
 */
public class TypeUtils {
	//public static final String path = "E:/workdesk/soft/apache-tomcat-6.0.43/webapps";
	public static final String home = "http://www.import-express.com/";
	public static final String myhome = "http://192.168.1.58:8080/";
	public static final String path = "/usr/wwwRoot";
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TypeUtils.class);
	private static final String[] url_items = new String[]{ 
			"bestway020.en.alibaba.com",
			"en.alibaba.com",
			".alibaba.com",
			"www.dhgate.com",
			"item.taobao.com",
			".yiwugou.com",
			"www.aliexpress.com",
			"www.globalsources.com",
			"en.jd.com",
			"hero-tech.en.made-in-china.com",
			"www.madeinchina.com",
			"www.lightinthebox.com",
			"www.ebay.com",
			"www.ecvv.com",
			"www.dx.com",
			"www.tinydeal.com",
			"www.amazon.com",
			"detail.tmall.com",
			"detail.1688.com",
			"small-order.hktdc.com",
			"www.eelly.com"
			};
	
	
	private static final int[] type_items = new int[]{3,2,1,4,5,6,7,8,9,10,11,
			12,13,14,15,16,17,18,19,20,21};
	/**分类网址
	 * @param url
	 * @return  网址类型
	 */
	public static int getType(String url){
		String url_result = getUrlPat(url);
		int result = 0;
		int count = url_items.length;
		for(int i=0;i<count;i++){
			if(StrUtils.isFind(url_result, "("+url_items[i]+")")){
				result = type_items[i];
				break;
			}
		}
		url_result  =null;
		return result;
	}
	
	/**解析网址，得到主要域名
	 * @param url 商品链接url
	 * @return 返回网站名（例如www.alibaba.com）
	 */
	public static String getUrlPat(String url){
		String pat = "(?:https*\\://)(.*?)(?:/)";
		Pattern p = Pattern.compile(pat);
    	Matcher m = p.matcher(url);
    	if(m.find()){  
    		return m.group(1).toString();
    	}else{
    		LOG.warn("	---the url not in the list ---");
    		return "";
    	}	
	}
	
	/**链接校正
	 * @param flag 0:search;1:goods
	 * @return
	 */
	public static String modefindUrl(String url,int flag){
		if(url==null||url.isEmpty()){
			return url;
		}
		if (StrUtils.isFind(url, "(www.aliexpress.com)")) {
			url = url.startsWith("//") ? "https:" + url : url;
			url = url.replaceAll("https\\:", "http://");
		}
		String result = null;
		url = url.replaceAll("http\\:/+", "http://")
				.replaceAll("https\\:/+", "https://");
		if(flag==1){
			if(!StrUtils.isFind(url, "(taobao)|(tmall)")){
				//商品链接
				if(StrUtils.isFind(url, "(www\\.aliexprwwws\\.com)")){
					url = url.replace("www.aliexprwwws.com", "www.aliexpress.com");
				}
				if(StrUtils.isFind(url, "(\\.htm)")){
					url = url.split("(\\.htm)")[0]+".html";
				}
				if(StrUtils.isFind(url, "(aliexpress)")){
					url= url.replaceAll("store/product/", "item/").replaceAll("/\\d+_", "/");
				}
			}else{
				url = url.replace("#detail", "");
			}
		}else{
			
			if(StrUtils.isFind(url, "(aliexpress)")){
				
				if(!StrUtils.isFind(url, "((&g=y)|(/store/))")){
					
					url = StrUtils.isFind(url, "(\\?)")? url+"&g=y" : url+"?&g=y";
				}
			}else{
				
				if(StrUtils.isFind(url,"(wholesale.*alibaba)")){
					url = url.replace("/www.alibaba.com/wholesaler.alibaba.com/", "/wholesaler.alibaba.com/");
				}
			}
		}
		String urlPat = getUrlPat(url);
		if(urlPat!=null&&!urlPat.isEmpty()){
			Boolean alibaba = StrUtils.isMatch(urlPat, "(.*(en)*\\.alibaba.com)");
			String firstPat = urlPat.split("(\\.)")[0];
			String pat = "(www)|(bestway020)|(item)|(en)|(hero-tech)|(small-order)|(detail)|(ny)|(import-express)|(newcloud)";
			result = alibaba&&!StrUtils.isMatch(firstPat, pat) ? url.replaceAll(firstPat, "www") : url;
			result = result.trim();
		}
		return result;
	}
	
	/**链接特殊字符编码
	 * @date 2016年4月9日
	 * @author abc
	 * @param url
	 * @return  
	 */
	public static String encodeUrl(String url){
		if(url==null){
			 return null;
		}
		String spurl =  url.replace("http://", "")
				.replace("https://", "")
				.replace(".html", "9DT5")
				.replace("www.", "2CR5")
				.replace(".com", "0RB5")
				.replaceAll("%20", "9ST5")
				.replaceAll("\\s", "9ST5")
				.replaceAll("&amp;", "&")
				.replaceAll("alibaba", "3DL5")
				.replaceAll("aliexpress", "2DL5")
				.replaceAll("wholesale", "6DL5")
				.replaceAll("&", "7WT5")
				.replaceAll("#", "7BY5")
				.replaceAll("\\?", "9WR5")
				.replaceAll("=", "3QY5")
				.replaceAll("-", "4JK5")
				.replaceAll("\\+", "8OK5")
				.replaceAll("_", "3HF5")
				.replaceAll("/", "2YK5")
				.replaceAll("\\.", "6AF5");
		return spurl;
		
	}
	/**链接解码
	 * @date 2016年4月9日
	 * @author abc
	 * @param url
	 * @return  
	 */
	public static String decodeUrl(String url){
		if(url==null){
			return null;
		}
		String spurl =  url.replace("2CR5", "www.")
				.replace("0RB5", ".com")
				.replaceAll("9DT5", ".html")
				.replaceAll("2DL5", "aliexpress")
				.replaceAll("3DL5", "alibaba")
				.replaceAll("6DL5", "wholesale")
				.replaceAll("9ST5", "%20")
				.replaceAll("7WT5", "&")
				.replaceAll("7BY5", "#")
				.replaceAll("9WR5", "?")
				.replaceAll("3QY5", "=")
				.replaceAll("4JK5", "-")
				.replaceAll("8OK5", "+")
				.replaceAll("3HF5", "_")
				.replaceAll("2YK5", "/")
				.replaceAll("6AF5", ".");
		return spurl;
		
	}
		
	/**encode the search url
	 * @param url
	 * @return
	 */
	public static String encodeSearch(String url){
		String en = encodeUrl(url);
		if(en==null){
			return null;
		}
		int u=en.length()>>2;
		StringBuffer sb = new StringBuffer();
		sb.append("&k0=")
		.append(en.substring(u,u*2))
		.append("&k1=")
		.append(en.substring(u*3,en.length()))
		.append("&k2=")
		.append(en.substring(0,u))
		.append("&k3=")
		.append(en.substring(2*u,u*3));
		return sb.toString();
		
	}
	
	/**decode the search url
	 * @param url
	 * @return
	 */
	public static String decodeSearch(String url){
		String[] split = url.split("&");
		if(split!=null&&split.length>4){
			String[] k0s = split[1].split("=");
			String[] k1s = split[2].split("=");
			String[] k2s = split[3].split("=");
			String[] k3s = split[4].split("=");
			
			String k0 = k0s.length>1?k0s[1]:null;
			String k1 = k1s.length>1?k1s[1]:null;
			String k2 = k2s.length>1?k2s[1]:null;
			String k3 = k3s.length>1?k3s[1]:null;
			if(k0!=null&&k1!=null&&k2!=null&&k3!=null){
				String re = k2+k0+k3+k1;
				String en = decodeUrl(re);
				if(en.startsWith("//")){
					en = "http:" + en;
				}else if(!en.startsWith("http://")){
					en = "http://" + en;
				}
				re = null;
				split = null;
				k0 = null;
				k1 = null;
				k2 = null;
				k3 = null;
				return en;
			}
		}
		return null;
	}
	
	/**encode the goods url
	 * @param url
	 * @return
	 */
	public static String encodeGoods(String url){
		String en = encodeUrl(url);
		if(en!=null){
			int u=en.length()>>2;
			StringBuffer sb = new StringBuffer();
			sb.append("&u0=")
			  .append(en.substring(2*u,u*3))
			  .append("&u1=")
			  .append(en.substring(0,u))
			  .append("&u2=")
			  .append(en.substring(u*3,en.length()))
			  .append("&u3=")
			  .append(en.substring(u,u*2));
			return sb.toString();
		}
		return null;
	}
	
	/**decode the goods url
	 * @param url
	 * @return
	 */
	public static String decodeGoods(String url){
		String[] split = url.split("&");
		if(split.length<4){
			return null;
		}
		String u0 = split[1].split("=")[1];
		String u1 = split[2].split("=")[1];
		String u2 = split[3].split("=")[1];
		String u3 = split[4].split("=")[1];
		String re = u1+u3+u0+u2;
		String en = decodeUrl(re);
		if(en.startsWith("//")){
			en = "http:" + en;
		}else if(!en.startsWith("http://")){
			en = "http://" + en;
		}
		split = null;
		u0 = null;
		u1 = null;
		u2 = null;
		u3 = null;
		return en;
	}
	
	/**比较时间
	 * @date 2016年3月9日
	 * @author abc
	 * @param timeOld
	 * @param days
	 * @return  
	 */
	public static Boolean isNew(String oldtime,double days){
		
		if(StringUtils.isEmpty(oldtime) || !StrUtils.isFind(oldtime, "(\\d+-\\d+-\\d+\\s*\\d+\\:\\d+\\:\\d+\\.*\\d*)")){
			return false;
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		oldtime = oldtime.replaceAll("\\.\\d+", "").trim();
		Boolean result = false;
		try {
			Date old_time = sdf.parse(oldtime);
			double daysBetween=(new Date().getTime()-old_time.getTime())/(24*60*60*1000);
			result = (daysBetween>0||daysBetween==0)&&daysBetween < days?true:false;
			old_time = null;
		} catch (ParseException e) {
			result  = false;
		}
		return result;
	}
	
	/**获取当前时间  年-月-日-时-分-秒
	 * @return
	 */
	public static String getTime(){
	     SimpleDateFormat dateForamt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	     return dateForamt.format(new Date());
	}
	
	/**获取当前时间之前几天的时间
	 * @date 2016年3月1日
	 * @author abc
	 * @param days
	 * @return  
	 */
	public static String getTimeBefore(double days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	     return df.format(new Date((long) (System.currentTimeMillis() - days * 24 * 60 * 60 * 1000)));
	}
	/**获取当前日期之前几天的时日期
	 * @date 2016年3月1日
	 * @author abc
	 * @param days 间隔天数
	 * @return  
	 */
	public static String getDateBefore(double days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		return df.format(new Date((long) (System.currentTimeMillis() - days * 24 * 60 * 60 * 1000)));
	}
      
	
	
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
	// 字符串在编译时会被转码一次,所以是 "\\b"    
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
	private static final String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry"    
											+"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp|laystation portable)|nokia|fennec|htc[-_]"    
											+"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
	private static final String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
	/**检测是否为移动端设备访问 
	 * @param userAgent
	 * @return
	 */
	public static boolean checkAgent(HttpServletRequest req){
		
		String userAgent = req.getHeader("USER-AGENT");
		userAgent = userAgent==null?"":userAgent.toLowerCase();
        // 匹配    
		//移动设备正则匹配：手机端、平板  
		LOG.warn("checkAgent:"+userAgent);
        Matcher matcherPhone = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE).matcher(userAgent);    
        Matcher matcherTable = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE).matcher(userAgent);    
        if(matcherPhone.find() || matcherTable.find()){    
            return true;    
        }   
        return false;    
	}
	
	
	/**产品单页短连接转换正常网站请求链接
	 * @param sourceType 长度32位为aliexpress数据,
	 * 					   以A开头为aliexpress ,
	 * 					   以M开头为亚马逊,
	 * 					 D开头为1688上传,
	 * 					 T开头为货源替换数据
	 * 
	 * @param itemId 产品id
	 * @return
	 */
	public static String shortToUrl(String sourceType,String itemId){
		String result = "";
		if(StringUtils.isEmpty(itemId) || StringUtils.isEmpty(sourceType)){
			return  result;
		}
		if(sourceType.length()==32 || sourceType.startsWith("A")){
			//aliexpress网站数据
			result = "http://www.aliexpress.com/item/a/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "M")){
			//amazon抓取数据
			result = "http://www.amazon.com/item/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "D")){
			//1688上传数据
			result = "https://www.local.com/item/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "T")){
			//货源替换数据
			result = "https://www.change.com/item/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "I")){
			//图片搜索2.0
			result = "https://www.imageSearch.com/offer/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "S")){
			//图片搜索1.0
			result = "https://detail.search.com/offer/"+itemId+".html";
			
		}else if(StringUtils.startsWith(sourceType, "N")){
			//新品云
			result = "https://newcloud.com/offer/"+itemId+".html";
		}
		return result;
	}
	/**产品单页正常网站请求链接转换短连接
	 * @param url 产品链接
	 * @param itemId  产品id
	 * @return
	 */
	public static String urlToShort(String url,String itemId){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append("&source=");
		if(url.indexOf("aliexpress") > -1){
			//aliexpress网站数据
			if(StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(/\\d+\\.htm)").replaceAll("\\D+", "").trim();
			}
			result.append("A");
		}else if(url.indexOf("//www.amazon.com/cido/") > -1){
			//amazon抓取数据
			result.append("M");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(/pid=.*)");
				itemId = itemId.replaceAll("/pid=", "").trim();
			}
		}else if(url.indexOf("//www.local.com/cido") > -1){
			//1688上传数据
			result.append("D");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(/pid=.*)");
				itemId = itemId.replaceAll("/pid=", "").trim();
			}
		}else if(url.indexOf("taobao.com") > -1  && StringUtils.startsWith(itemId, "T") ){
			//货源替换数据item.taobao.com
			result.append("T");
			
		}else if(url.indexOf("detail.1688.com") > -1  && StringUtils.startsWith(itemId, "D")){
			//货源替换数据detail.1688.com
			result.append("T");
			
		}else if(url.indexOf("detail.tmall") > -1 && StringUtils.startsWith(itemId, "L")){
			//货源替换数据detail.tmall.com
			result.append("T");
		}else if(url.indexOf("www.imageSearch.com/offer") > -1){
			//图片搜索1.0
			result.append("I");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(offer/\\d+)");
				itemId = itemId.replaceAll("offer/", "").trim();
			}
		}else if(url.indexOf("detail.search.com/offer") > -1){
			//图片搜索2.0
			result.append("S");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(offer/\\d+)");
				itemId = itemId.replaceAll("offer/", "").trim();
			}
		}else if(url.indexOf("newcloud.com/offer") > -1){
			//新品云
			result.append("N");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(offer/\\d+)");
				itemId = itemId.replaceAll("offer/", "").trim();
			}
		}
		
		if(StringUtils.isEmpty(itemId)){
			return "";
		}
		result.append(Md5Util.encoder(url)).append("&item=").append(itemId);
		
		return result.toString();
	}
	
	/**产品单页正常网站请求链接获取产品id
	 * @param url  产品链接
	 * @return
	 */
	public static String urlToItemId(String url){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String itemId = "";
		if(url.indexOf("aliexpress") > -1){
			//aliexpress网站数据
			itemId = StrUtils.matchStr(url, "(/\\d+\\.htm)").replaceAll("\\D+", "").trim();
		}else if(url.indexOf("//www.amazon.com/cido/") > -1){
			//amazon抓取数据
			itemId = StrUtils.matchStr(url, "(/pid=.*)");
			itemId = itemId.replaceAll("/pid=", "").trim();
		}else if(url.indexOf("//www.local.com/cido") > -1){
			//1688上传数据
			itemId = StrUtils.matchStr(url, "(/pid=.*)");
			itemId = itemId.replaceAll("/pid=", "").trim();
		}else if(url.indexOf("www.imageSearch.com/offer") > -1){
			//图片搜索1.0
			itemId = StrUtils.matchStr(url, "(offer/\\d+)");
			itemId = itemId.replaceAll("offer/", "").trim();
		}else if(url.indexOf("detail.search.com/offer") > -1){
			//图片搜索2.0
			itemId = StrUtils.matchStr(url, "(offer/\\d+)");
			itemId = itemId.replaceAll("offer/", "").trim();
		}else if(url.indexOf("newcloud.com/offer") > -1){
			//新品云
			itemId = StrUtils.matchStr(url, "(offer/\\d+)");
			itemId = itemId.replaceAll("offer/", "").trim();
		}
		
		return itemId;
	}
	
	
}
