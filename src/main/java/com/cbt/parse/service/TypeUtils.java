package com.cbt.parse.service;

import com.cbt.util.AppConfig;
import com.cbt.util.Md5Util;
import com.cbt.util.SearchConfig;

import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**鍒ゆ柇url绫诲瀷锛堝嵆浠庡摢涓綉绔欐潵瑙ｆ瀽鏁版嵁锛�
 * @author abc
 *
 */
public class TypeUtils {
	//public static final String path = "E:/workdesk/soft/apache-tomcat-6.0.43/webapps";
	public static final String home = "http://www.import-express.com/";
	public static final String myhome = "http://192.168.1.58:8080/";
	public static final String path = "/usr/local/apache2/htdocs";
	/**
	 * 产品单页静态化文件中的名字，
	 */
	private static final String[] goodsNameWords={"hotsale","aliexpress","free-shipping","new","shipping","HOT","fashion","Hot sale","Hot Worldwide"};
	
	
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
	/**鍒嗙被缃戝潃
	 * @param url
	 * @return  缃戝潃绫诲瀷
	 */
	public static int getType(String url){
		String url_result = getUrlPat(url);
		int result = 0;
		int count = url_items.length;
		for(int i=0;i<count;i++){
			if(Pattern.compile("("+url_items[i]+")").matcher(url_result).find()){
				result = type_items[i];
				break;
			}
		}
		url_result  =null;
		return result;
	}
	
	/**瑙ｆ瀽缃戝潃锛屽緱鍒颁富瑕佸煙鍚�
	 * @param url 鍟嗗搧閾炬帴url
	 * @return 杩斿洖缃戠珯鍚嶏紙渚嬪www.alibaba.com锛�
	 */
	public static String getUrlPat(String url){
		String url_result;
		String pat = "(?:https*\\://)(.*?)(?:/)";
		Pattern p = Pattern.compile(pat);
    	Matcher m = p.matcher(url);
    	if(m.find()){  
    		url_result = m.group(1).toString();
//    		LOG.warn("		insert锛�+url_result);
    	}else{
    		LOG.warn("	---the url not in the list 杩欎釜閾句笉绗﹀悎瑙勫畾 褰曞叆涓嶈繘鍘�1.24 ---");
    		url_result = null;
    	}	
    	p = null;
    	m = null;
    	pat = null;
		return url_result;
	}
	
	/**閾炬帴鍘婚櫎澶氳瑷�摼鎺�
	 * @param flag 0:search;1:goods
	 * @return
	 */
	public static String modefindUrl(String url,int flag){
		String result = null;
		if(url!=null){
			url = url.replaceAll("http\\:/+", "http://")
			         .replaceAll("https\\:/+", "https://");
			if(flag==1){
				if(!Pattern.compile("(taobao)|(tmall)").matcher(url).find()){
					//鍟嗗搧閾炬帴
					if(Pattern.compile("(www\\.aliexprwwws\\.com)").matcher(url).find()){
						url = url.replace("www.aliexprwwws.com", "www.aliexpress.com");
					}
					if(Pattern.compile("(\\.htm)").matcher(url).find()){
						url = url.split("(\\.htm)")[0]+".html";
					}
					if(Pattern.compile("(aliexpress)").matcher(url).find()){
						url= url.replaceAll("store/product/", "item/").replaceAll("/\\d+_", "/");
					}
				}else{
					url = url.replace("#detail", "");
				}
			}else{
				if(Pattern.compile("(aliexpress)").matcher(url).find()){
					if(!Pattern.compile("(&g=y)").matcher(url).find()&&!Pattern.compile("(/store/)").matcher(url).find()){
						if(Pattern.compile("(\\?)").matcher(url).find()){
							url +="&g=y";
						}else{
							url +="?&g=y";
						}
					}
				}else{
					if(Pattern.compile("(wholesale.*alibaba)").matcher(url).find()){
						url = url.replace("/www.alibaba.com/wholesaler.alibaba.com/", "/wholesaler.alibaba.com/");
					}
				}
			}
			String urlPat = getUrlPat(url);
			if(urlPat!=null&&!urlPat.isEmpty()){
				Boolean alibaba = true;
				if(Pattern.compile("(.*(en)*\\.alibaba.com)").matcher(urlPat).matches()){
					alibaba = false;
				}
				String firstPat = urlPat.split("(\\.)")[0];
				String pat = "(www)|(bestway020)|(item)|(en)|(hero-tech)|(small-order)|(detail)|(ny)";
				result = url;
				if(alibaba&&!Pattern.compile(pat).matcher(firstPat).matches()){
					result = url.replaceAll(firstPat, "www");
				}
			}
			result = result.trim();
		}
		
		return result;
	}
	
	public static String encodeUrl(String url){
		if(url!=null){
			String spurl =  url.replace("http://", "")
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
		 return null;
	}
	public static String decodeUrl(String url){
		if(url!=null){
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
		return null;
	}
	
	
	/**encode the search url
	 * @param url
	 * @return
	 */
	public static String encodeSearch(String url){
		String en = encodeUrl(url);
		if(en!=null){
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
		return null;
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
				String en = "http://"+decodeUrl(re);
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
		if(split.length>3){
			String u0 = split[1].split("=")[1];
			String u1 = split[2].split("=")[1];
			String u2 = split[3].split("=")[1];
			String u3 = split[4].split("=")[1];
			String re = decodeUrl(u1+u3+u0+u2);
			re = re.startsWith("//")?"http:"+re:"http://"+re;
			split = null;
			return re;
		}
		return null;
	}
	
	public static Boolean isNew(String timeOld,double days){
		String oldtime = timeOld;
		Boolean result = false;
		
		if(oldtime!=null&&!oldtime.isEmpty()&&Pattern.compile("(\\d+-\\d+-\\d+\\s*\\d+\\:\\d+\\:\\d+\\.*\\d*)").matcher(oldtime).find()){
			String newtime = getTime();
			oldtime = oldtime.replaceAll("\\.\\d+", "").trim();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date old_time = sdf.parse(oldtime);
				Date new_time =sdf.parse(newtime);
				double daysBetween=(new_time.getTime()-old_time.getTime())/(24*60*60*1000*1.0);
				if((daysBetween>0||daysBetween==0)&&daysBetween < days){
					result = true;
				}else{
					result  =false;
				}
				old_time = null;
				new_time = null;
			} catch (ParseException e) {
				result  = false;
			}
			oldtime = null;
			newtime = null;
			sdf = null;
		}else{
			result  = false;
		}
		return result;
	}
	
	/**鑾峰彇褰撳墠鏃堕棿  骞�鏈�鏃�鏃�鍒�绉�
	 * @return
	 */
	public static String getTime(){
		 Date date = new Date();
	     SimpleDateFormat dateForamt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	     return dateForamt.format(date);
	}
	
	/**鑾峰彇褰撳墠鏃堕棿涔嬪墠鍑犲ぉ鐨勬椂闂�
	 * @date 2016骞�鏈�鏃�
	 * @author abc
	 * @param days
	 * @return  
	 */
	public static String getTimeBefore(double days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 Date date = new Date();
	     return df.format(new Date((long) (date.getTime() - days * 24 * 60 * 60 * 1000)));
	}
	/**鑾峰彇鏃堕棿涔嬪悗鍑犲ぉ鐨勬椂闂�
	 * @date 2016骞�鏈�鏃�
	 * @author abc
	 * @param days
	 * @return  
	 */
	public static String getTimeAfter(Date time,double days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return df.format(new Date((long) (time.getTime() + days * 24 * 60 * 60 * 1000)));
	}
      
	
	
	// \b 鏄崟璇嶈竟鐣�杩炵潃鐨勪袱涓�瀛楁瘝瀛楃 涓�闈炲瓧姣嶅瓧绗� 涔嬮棿鐨勯�杈戜笂鐨勯棿闅�,    
	// 瀛楃涓插湪缂栬瘧鏃朵細琚浆鐮佷竴娆�鎵�互鏄�"\\b"    
	// \B 鏄崟璇嶅唴閮ㄩ�杈戦棿闅�杩炵潃鐨勪袱涓瓧姣嶅瓧绗︿箣闂寸殑閫昏緫涓婄殑闂撮殧)    
	private static final String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry"    
											+"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp|laystation portable)|nokia|fennec|htc[-_]"    
											+"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
	private static final String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
	/**妫�祴鏄惁涓虹Щ鍔ㄧ璁惧璁块棶 
	 * @param userAgent
	 * @return
	 */
	public static boolean checkAgent(String userAgent){
		userAgent = userAgent==null?"":userAgent.toLowerCase();
        // 鍖归厤    
		//绉诲姩璁惧姝ｅ垯鍖归厤锛氭墜鏈虹銆佸钩鏉� 
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
	 * 					 W开头为wholesaler数据
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
		}else if(StringUtils.startsWith(sourceType, "W")){
			//wholesaler.alibaba.com
			result = "http://wholesaler.alibaba.com/product-detail/a_"+itemId+".html";
		}
		return result;
	}
	/**产品单页正常网站请求链接转换短连接
	 * @param url 产品链接
	 * @param itemId  产品id
	 * @return
	 */
	public static String urlToShort(String url,String itemId){
		if(StringUtils.isBlank(url)){
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
				itemId = StrUtils.matchStr(url, "(offer/.*htm)");
				itemId = itemId.replaceAll("offer/", "").replace(".htm", "").trim();
			}
		}else if(url.indexOf("wholesaler.alibaba.com") > -1){
			//wholesaler.alibaba.com
			result.append("W");
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(_/\\d+\\.htm)");
				itemId = itemId.replaceAll("(\\D+)", "").trim();
			}
		}else if(url.indexOf("&source=") > -1){
			//source
			result.append(StrUtils.matchStr(url, "(&source=.)").replace("&source=", ""));
			if( StringUtils.isEmpty(itemId)){
				itemId = StrUtils.matchStr(url, "(&item=.*)");
				itemId = itemId.replace("&item=", "").trim();
			}
		}
		
		if(StringUtils.isBlank(itemId)){
			return "";
		}
		result.append(Md5Util.encoder(itemId)).append("&item=").append(itemId);
		
		return result.toString();
	}
	
	
	/**产品ID转换UUID
	 * @param itemId 产品ID
	 * @param type  数据来源
	 * @return
	 */
	public static String itemIDToUUID(String itemId,String type){
		if(StringUtils.isBlank(itemId)){
			return "";
		}
		return type+Md5Util.encoder(itemId);
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
		if(url.indexOf("aliexpress") > -1 || url.indexOf("https:wholesaleServer") > -1){
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
			itemId = StrUtils.matchStr(url, "(offer/.*html)").replace(".html", "");
			itemId = itemId.replaceAll("offer/", "").trim();
		}else if(url.indexOf("wholesaler.alibaba.com") > -1){
			//wholesaler
			itemId = StrUtils.matchStr(url, "(_/\\d+\\.htm)");
			itemId = itemId.replaceAll("(\\D+)", "").trim();
		}else if(url.indexOf("&source=") > -1){
			//wholesaler
			itemId = StrUtils.matchStr(url, "(&item=.*)");
			itemId = itemId.replaceAll("(&item=)", "").trim();
		}else if(url.indexOf("/import-express.com/") > -1){
			//线下订单生成商品
			itemId = "88888888";
		}
		
		return itemId;
	}
	/**产品单页正常网站请求链接获取产品id
	 * @param url  产品链接
	 * @return
	 */
	public static String urlToSourceType(String url){
		if(StringUtils.isEmpty(url)){
			return "D";
		}
		if(url.indexOf("aliexpress") > -1){
			//aliexpress网站数据
			return "A";
		}else if(url.indexOf("//www.amazon.com/cido/") > -1){
			//amazon抓取数据
			return "M";
		}else if(url.indexOf("//www.local.com/cido") > -1){
			//1688上传数据
			return "D";
		}else if(url.indexOf("www.imageSearch.com/offer") > -1){
			//图片搜索1.0
			return "I";
		}else if(url.indexOf("newcloud.com/offer") > -1){
			//新品云
			return "N";
		}else if(url.indexOf("wholesaler.alibaba.com") > -1){
			//wholesaler
			return "W";
		}else if(url.indexOf("&source=") > -1){
			//source
			String type = StrUtils.matchStr(url, "(&source=.)");
			type = type.replace("&source=", "");
			return type;
		}else if(url.indexOf("/import-express.com/") > -1){
			//source
			return "E";
		}
		return "D";
	}
	
	/**通过uuid itemID得到我司产品链接
	 * @param uuid 33位:数据来源位+32位uuid
	 * @param itemID 产品id
	 * @return
	 */
	public static String UUidToUrl(String uuid,String itemID){
		if(StringUtils.isBlank(itemID) || StringUtils.isBlank(uuid)){
			return "";
		}
		uuid = uuid.toUpperCase();
		if(AppConfig.ip.indexOf("import-express") > -1){
			if(AppConfig.ip.endsWith("/")){
				return AppConfig.ip+"spider/detail?&source="+uuid+"&item="+itemID;
			}
			return AppConfig.ip+"/spider/detail?&source="+uuid+"&item="+itemID;
		}
		return "192.168.1.29:8086/spider/detail?&source="+uuid+"&item="+itemID;
	}
	
	/**全站兼容数据
	 * @param uuid
	 * @param itemid
	 * @param url
	 * @return
	 */
	public static String compatibleUrl(String uuid,String itemid,String url){
		if(StringUtils.isBlank(itemid)){
			itemid = TypeUtils.urlToItemId(url);
		}
		if(StringUtils.isBlank(uuid) || StringUtils.length(uuid) < 33){
			String urlToSourceType = TypeUtils.urlToSourceType(url);
			uuid = TypeUtils.itemIDToUUID(itemid, urlToSourceType);
		}
		return TypeUtils.UUidToUrl(uuid, itemid);
	}
	
	/**
	 * @param itemId
	 * @param type
	 * @return
	 */
	public static String itemIDToUrl(String itemId,String type){
		return UUidToUrl(itemIDToUUID(itemId, type), itemId);
	}
	 /**
     * 
     * @Title uUidToStaticUrl_cart 
     * @Description 生成产品页面伪静态链接（qiqing  优化上面那个接口，不要再for循环中读配置文件）
     * @param uuid
     * @param itemID
     * @param pname
     * @param urlHead
     * @return
     * @return String
     */
    public static String uUidToStaticUrl_cart(String uuid,String itemID,String pname,String urlHead,String catid1,String catid2) {
    	String type = StringUtils.length(uuid) > 0 ? uuid.substring(0, 1) : "D";
		return pseudoStaticUrl(itemID, type , pname, catid1, catid2);
    }
    
	 /**伪静态化产品链接
     * @param itemid 产品id
     * @param type 类型 
     * @param name 产品名称
     * @param catid1 类别1
     * @param catid2 类别2
     * @return
     */
    public static String pseudoStaticUrl(String itemid,String type,String name,String catid1,String catid2) {
    	String dataType = "1";
    	switch (type) {
		case SearchConfig.UPLOAD_PRODUCT:
			dataType = "1";
			break;
		case SearchConfig.ALIEXPRESS_PRODUCT:
			dataType = "2";
			break;
		case SearchConfig.NEWCLOUD_PRODUCT:
			dataType = "3";
			break;
		case SearchConfig.AMAZON_PRODUCT:
			dataType = "4";
			break;
		case SearchConfig.IMAGE_SEARCH_PRODUCT:
			dataType = "5";
			break;
		case SearchConfig.CHANGE_PRODUCT:
			dataType = "6";
			break;
		case SearchConfig.IMPORT_EXPRESS_PRODUCT:
			dataType = "7";
			break;
		default:
			dataType = "1";
			break;
		}
    	name = name.toLowerCase().replaceAll("[^a-zA-Z0-9]"," ").trim();
		//去除静态页名字中一些不需要的词
    	name=removeGoodsNameWords(name);
		String[] nameArr = name.split("\\s+");
		StringBuffer goodnameNew=new StringBuffer();
		if(nameArr.length>10){
			for (int i = 0; i < 10; i++) {
				if(StringUtils.isNotBlank(nameArr[i])){
					goodnameNew.append(nameArr[i]+"-");
				}
			} 
		}else{
			goodnameNew.append(name.replaceAll("(\\s+)", "-"));
		}
		String url  = "";
		if(StringUtils.isNotBlank(goodnameNew.toString())){
			if(StringUtils.isBlank(catid1) || StringUtils.isBlank(catid2)) {
				url = "/goodsinfo/"+goodnameNew.deleteCharAt(goodnameNew.length()-1)+"-"+dataType+ itemid + ".html";
			}else {
				url = "/goodsinfo/"+goodnameNew.deleteCharAt(goodnameNew.length()-1) +"-"+catid1+"-"+catid2+"-"+dataType+ itemid + ".html";
			}
		} else {
			url = "/spider/getSpider?item="+itemid+"&source="+itemIDToUUID(itemid, type);
		}
    	
		return url;
    }
	
    /**
	 * 移除产品单页名字中一些乱七八糟的词
	 * @param goodsName
	 * @return
	 */
	public static String removeGoodsNameWords(String goodsName){
		
		if(goodsName!=null&&!"".equals(goodsName)){
			for (String name : goodsNameWords) {
//				goodsName=goodsName.replace(name.trim(), "");
				//替换忽略大小写
				String newName="(?i)"+name;
				goodsName=goodsName.replaceAll(newName, "");
			}
			
//			int nowYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
			/*delete by lhao 2018.06.29
			for(int year=1949;year<=9999;year++){
				goodsName = goodsName.replace(String.valueOf(year), "");
			}*/
		 return goodsName;
		} 
		return goodsName;
	}
}
