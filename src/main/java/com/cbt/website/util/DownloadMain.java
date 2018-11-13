package com.cbt.website.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**网页源码获取实现类
 * @author abc
 *
 */
public class DownloadMain {
	private static int count = 0;//链接超时
	public static final String TYPE = "TYPE_A";
	private static final Log LOG = LogFactory.getLog(DownloadMain.class);   												
	

	/**随机数生成函数
	 * @param Max
	 * @param min
	 * @return
	 */
	public static int toRodom(int Max, int min) {
		int i = (int) (min + Math.random() * (Max - min));
		return i;
	}
	
	public static  String getAgent(){
		//伪装客户端
		String[] userAgent = new String[]{"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)",
															  "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT6.0)",
															  "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT5.2)",
															  "Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/42.0.2311.90 Safari/537.36 OPR/29.0.1795.47(Edition Campaign 56)",
															  "Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/31.0.1650.63 Safari/537.36",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.2)Gecko/2008070208 Firefox/3.0.1",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070309 Firefox/2.0.0.3",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070803 Firefox/1.5.0.12 ",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.2)AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1Safari/525.13",
															 // "Mozilla/5.0 (iPhone; U; CPU like Mac OS X)AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93Safari/419.3",
															  "Opera/9.27 (Windows NT 5.2; U; zh-cn)",
															  "Opera/8.0 (Macintosh; PPC Mac OS X; U; en)",
															  "Links/0.9.1 (Linux 2.4.24; i386;)",
															  "Links (2.1pre15; FreeBSD 5.3-RELEASE i386; 196x84)",
															  "Mozilla/4.8 [en] (X11; U; SunOS; 5.7 sun4u)",
															  "Gulper Web Bot 0.2.4 (www.ecsl.cs.sunysb.edu/~maxim/cgi-bin/Link/GulperBot)",
															  "OmniWeb/2.7-beta-3 OWF/1.0",
															  "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)"};
		if(count>=userAgent.length){
			count  = 0;
		}
		String agent = userAgent[count];
		count++;
		userAgent = null;
		return agent;
	}
	
	
	
	/**写入文件
	 * @param page
	 * @throws Throwable
	 */
	public static void write2file(String page,String filepath){
		
		File file = new File(filepath); 
		if (!file.exists()) {//测试此抽象路径名表示的文件或目录是否存在。 
	        file.getParentFile().mkdirs();//返回此抽象路径名父目录的抽象路径名；创建
	        try {
				file.createNewFile();
			} catch (IOException e) {
				LOG.info("writeStringToFile error0");
			}//不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件
	        
	    }
	    try {
			FileUtils.writeStringToFile(file, page, "gbk");
		} catch (IOException e) {
			LOG.info("writeStringToFile error");
		} 
	}
	
	
	
 
	
public static Boolean isValid(String agent,String ip,String port){
	Boolean result;
	String url = "http://1111.ip138.com/ic.asp";
	String page = "";
	setAddress(ip, port);
	try {
		Response con = connectUrl(agent, url, 0);
		page = con.body();
		String pag = DownloadMain.getSpiderContext(page, "(?:<center>)(.*?)(</center>)");
		LOG.warn(pag);
		if(Pattern.compile("("+ip+")").matcher(pag).find()){
			result = true;
		}else{
			result = false;
		}
	} catch (IOException e) {
		result = false;
		LOG.warn("校验代理服务器异常:"+e);
	}
	return result;
}

	
 

public static String getJsoupForCheck(String url,int flag){
	int tem = flag;
	String content  = null;//网页内容
	if(url!=null&&!url.isEmpty()){
		//伪客户端
		String agent = getAgent();
//		String ip = IpSelect.ip(url,flag);
		Connection conn = Jsoup.connect(url)
//				.header("CLIENT-IP", ip)
//				.header("X-FORWARDED-FOR", ip)
				.header("Connection", "keep-alive")
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") 
				.userAgent(agent)
				.timeout(3*1000)
				.followRedirects(true);
		try {
			content = conn.execute().body();
		}catch (SocketTimeoutException e) {
			if(tem<3){
				tem++;
				content = getJsoupForCheck(url,tem);
			}else{
				content = null;
			}
		} catch (HttpStatusException e) {
			content = null;
		} catch (IOException e) {
			content = null;
		}finally{
			conn = null;
		}
//		ip = null;
		agent = null;
	}
	return content;
}


	 
	
	
	/**设置系统代理服务器
	 * @param ip
	 * @param port
	 */
	private static void setAddress(String ip,String port){
			System.getProperties().setProperty("proxySet", "true");  
			//代理服务器    
			System.getProperties().setProperty("http.proxyHost", ip.trim());  
			//代理端口    
			System.getProperties().setProperty("http.proxyPort", port.trim()); 
	}
	
	/**链接网页
	 * @param agent
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	private static Response connectUrl(String agent,String url,int flag) throws IOException{
		long st = new Date().getTime();
		boolean find = Pattern.compile("(alibaba)").matcher(url).find();
		
		boolean tbfind = Pattern.compile("(taobao)|(tmall)").matcher(url).find();
		
		long ste = new Date().getTime();
		Connection conn = Jsoup.connect(url)
								.header("CLIENT-IP", "64.186.47.179")
								.header("X-FORWARDED-FOR", "64.186.47.179")
								.header("Connection", "keep-alive")
								.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") 
								.header("User-Agent", agent)  
								.userAgent(agent)
								.followRedirects(true);
		
		//分情况设置超时系数
		if(flag==0&&tbfind){
			//1.淘宝搜索超时
			conn = conn.timeout(2*1000+500);
			LOG.warn("TB search  timeout coefficient：2.5*1000");
		}else if(flag==1&&tbfind){
			//2.淘宝单页商品超时
			LOG.warn("TB goods timeout coefficient：3*1000");
			conn = conn.timeout(3*1000);
		}else if(flag==0&&find){
			//3.alibaba搜索超时
			LOG.warn("alibaba search timeout coefficient：2*1000");
			conn = conn.timeout(2*1000);
		}else{
			//4.alibaba 以及其他单页商品超时
			if(flag==1){
				LOG.warn("alibaba goods timeout coefficient：2*1000");
				conn = conn.timeout(2*1000);
			}
		}
		
//		conn = conn.method(Method.POST);
		long stt = new Date().getTime();
		LOG.warn("（0）Jsoup connect(): "+(stt-ste));
		//执行连接
		Response execu = conn.execute();
		long ed = new Date().getTime();
		
		
		
		LOG.warn("（1）Jsoup execute(): "+(ed-stt));
		
		LOG.warn("（2）Jsoup connect: "+(ed-st));
		return execu;
	}
	
	 /**  
     * 根据URL抓取网页内容  
     * @param url  
     * @return  
	 * @throws  Exception 
	 * @author yinlimei
     */
    public static String getUrl(String url)  
    {  
    	long start = new Date().getTime();
    	StringBuffer content = new StringBuffer();  
        /* 实例化一个HttpClient客户端 */
    	  URL u;
    	  InputStreamReader theHTML;
		try {
			
			long st = new Date().getTime();
			u = new URL(url);
			URLConnection con = u.openConnection();
			con.connect();
			con.setConnectTimeout(1*1000);
    	    InputStream in = new BufferedInputStream(con.getInputStream());
    		theHTML = new InputStreamReader(in);
    		long ed = new Date().getTime();
    		LOG.warn("url:"+(ed - st));
    	   int c;
		   while ((c = theHTML.read()) != -1) {
			   content.append((char) c);
    	   }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	long end = new Date().getTime();
    	LOG.warn("获取url信息所用时间为："+(end-start));
        return content.toString();  
    }  
	
	
	
	 /**  
     * 直接通过读取网页数据流来抓取网页内容  
     * @param url  
     * @return  
     * @throws  Exception 
     */
    public static String getContentUrl(String url,String charset)  
    {  
    	long st = new Date().getTime();
    	HttpURLConnection con = null;  
    	StringBuffer content = new StringBuffer();  
    	/* 实例化一个HttpClient客户端 */
    	URL u,realURL;
    	InputStreamReader theHTML;
    	//对于tinydeal 与amazon 需要用BufferedReader读取网页源码
    	String pattern = "(tinydeal)|(amazon)";
    	if(Pattern.compile(pattern).matcher(url).find()){
    		try{
    			long st3 = new Date().getTime();
    			u = new URL(url);
    			con = (HttpURLConnection) u.openConnection(); 
    			
    			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// IE代理进行下载  
//    			con.setInstanceFollowRedirects(true);
    			con.setConnectTimeout(3000);  
    			con.setReadTimeout(3000); 
//    			con.setRequestMethod("POST");
    			
    			while(con.getResponseCode()==302){
//	    			System.out.println("响应码：" + con.getResponseCode());
	    			String realUrl = con.getHeaderField("Location");
	    			con.disconnect();
	    			realURL = new URL(realUrl);
	    			con = (HttpURLConnection)realURL.openConnection();
	    		}
    				
    			 if(con.getResponseCode() == 200){
    				InputStream inStr = con.getInputStream(); 
    				InputStreamReader istreamReader = new InputStreamReader(inStr, charset);
    				BufferedReader buffStr = new BufferedReader(istreamReader); 
    				String str = null; 
    				while ((str = buffStr.readLine()) != null) {
    					content.append(str);
    					content.append("\n");
    				}
    				inStr.close();
    				istreamReader.close();
    				buffStr.close();
    			}
    			 long ed1 = new Date().getTime();
    			 LOG.warn("getContentOld��HttpURLConnection��ȡ��ҳ"+(ed1-st3));
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			if(con!=null){
    				con.disconnect();
    			}
	    	}
    	}else{//其他的可以用InputStreamReader读取网页源码
	    	try {
	    		long st4 = new Date().getTime();
	    		u = new URL(url);
	    		InputStream in = new BufferedInputStream(u.openStream());
    			theHTML = new InputStreamReader(in,charset);
	    		int c;
	    		long st2 = new Date().getTime();
	    		LOG.warn("getContentOld里链接url:"+(st2-st4));
	    		while ((c = theHTML.read()) != -1) {
	    			content.append((char) c);
	    		}
	    		in.close();
	    		long ed = new Date().getTime();
	    		LOG.warn("getContentOld里URL读取时间："+(ed-st2));
	    		LOG.warn("getContentOld里URL直接获取网页"+(ed-st));
	    	} catch (MalformedURLException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} 
    	}
    	
    	return content.toString()
			      .replace("锛", "")
				  .replace("&nbsp;", " ")
				  .replace("&yen;", "RMB"); //其换掉html文档中的空格符 
    }  
    
    /**调用curl命令行，通过代理服务器获取网页源码
   	 * @param url  网页链接
   	 * @param charset  网页编码格式
   	 * @return String 网页源码
   	 */
   	public static String getContentCurl(String url,String charset){
   		String url_r = url;//.replace("&","%26" ).replace("#", "%23").replace("=", "%3D");
   		//代理服务器
//   		String user_agent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
   		String user_agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11";
   		//拼写命令行
   		String cmd = "curl "+"-A " + "\"" + user_agent + "\" "+" -L " +url_r;
   		String line = null;
   		StringBuffer content = new StringBuffer();
           BufferedReader br = null;
           Process p = null;
           try {
           	//执行curl命令
               p = Runtime.getRuntime().exec(cmd);
               
               br = new BufferedReader(new InputStreamReader(p.getInputStream(),charset));
               while ((line = br.readLine()) != null) {
                   content.append(line);
//                   content.append("\n");
               }
           } catch (Exception e) {
               e.printStackTrace();
           } finally {
               if (br != null) {
                   try {
                       br.close();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }
           
        // 这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
           try {
                p.waitFor();
           } catch (InterruptedException e) {
               throw new RuntimeException("Curl下载过程被打断！");
           }
           
   		return content.toString()
   				      .replace("锛", "")
   					  .replace("&nbsp;", " ")
   					  .replace("&", "&")
   				      .replace("&yen;", "RMB"); //其换掉html文档中的空格符 ;
   	}
	
 
    
	 /**正则匹配相应格式的内容
     * @param page   内容
     * @param pattern  正则
     * @return  满足正则的字符串
     */
    public static String getSpiderContext(String page,String pattern){
    	if(page!=null&&!page.isEmpty()&&pattern!=null){
			if(!"(".equals(pattern.substring(0,1))){
				pattern = "("+pattern;
			}
			if(!")".equals(pattern.substring(pattern.length()-1))){
				pattern = pattern+")";
			}
    		Pattern p = Pattern.compile(pattern); 
    		Matcher m = p.matcher(page);
    		if(m.find()){  
    			return m.group(1).toString();
    		}else{
    			return "";
    		}
    	}
    	return "";
    }
    
  //正则匹配相应格式的内容
    public static List<String> getSpiderContextList1(String pattern,String page){
    	List<String> mobj = new ArrayList<String>();
    	if(page!=null&&!page.isEmpty()){
    		Pattern p = Pattern.compile(pattern); 
    		Matcher m = p.matcher(page);  
    		while(m.find()){ 
    			mobj.add(m.group(1));
    		}
    	}
    	return mobj;
    }

	//正则匹配相应格式的内容
	public static List<String> getSpiderContextList2(String pattern,String page){
		List<String> mobj = new ArrayList<String>();
		if(page!=null&&!page.isEmpty()){
			Pattern p = Pattern.compile(pattern,Pattern.DOTALL); 
			Matcher m = p.matcher(page);  
			while(m.find()){  
				mobj.add(m.group(1));
			}
		}
		return mobj;
	}
	
	@SuppressWarnings("deprecation")
	public static  String getContentClient(String urls,String ip){ 
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol").setLevel(java.util.logging.Level.OFF);
//		long st = new Date().getTime();
		String co = "";
		if(urls!=null&&!urls.isEmpty()){
			String url = urls.replaceAll("\\s", "%20");
			//HttpClient4.1的调用与之前的方式不同     
			DefaultHttpClient client = new DefaultHttpClient();
			//链接超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 200000); 
			CookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie  cookie = new BasicClientCookie("intl_locale", "en_US");
			cookieStore.addCookie(cookie);
			client.setCookieStore(cookieStore);
			//读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 200000);
			HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			HttpResponse response;
			try {
				HttpGet httpGet = new HttpGet(url); 
				if(ip!=null&&!ip.isEmpty()&&Pattern.compile("(\\d+\\.){3}\\d+").matcher(ip).matches()){
					httpGet.setHeader("X-Forwarded-For", ip);
					httpGet.setHeader("HTTP_CLIENT_IP", ip);
					httpGet.setHeader("HTTP_X_FORWARDED_FOR", ip);
					httpGet.setHeader("WL-Proxy-Client-IP", ip);
					httpGet.setHeader("Proxy-Client-IP", ip);
				}
//				String agent = getAgent();
//				httpGet.setHeader("Connection", "keep-alive");
//				httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
//				httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
//				httpGet.setHeader("Content-Language", "en-US");
//				httpGet.setHeader("Cache-Control", "no-cache");
//				httpGet.setHeader("Content-Type", "text/html;charset=gbk");
//				httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8") ;
//				httpGet.setHeader("User-Agent", agent);   
		        response = client.execute(httpGet);
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {  
					HttpEntity entity = response.getEntity();     
					if (entity != null) { 
						InputStreamReader in = null;
						if(Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()){
							in = new InputStreamReader(entity.getContent(), "gbk");
						}else{
						    in = new InputStreamReader(entity.getContent(), "utf-8");
						}
						BufferedReader reader = new BufferedReader(in);     
						co = IOUtils.toString(reader);
						in.close();
						reader.close();
						in = null;
						reader = null;
						/*
		            String line = null;     
		            if (entity.getContentLength() > 0) { 
		                strBuf = new StringBuffer((int) entity.getContentLength());     
		                while ((line = reader.readLine()) != null) {     
		                    strBuf.append(line);     
		                }     
		            }  
		        }     
		        if (entity != null) {     
		            entity.consumeContent();     
						 */
					}  
					entity = null;
					httpGet = null;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				co = "";
			} catch (IOException e) {
				e.printStackTrace();
				co = "";
			}finally{
				if(client!=null){
					client.close();
				}
				response = null;
			}
		}
			return co.replaceAll("鈥�", ""); //其换掉html文档中的空格符 ;       
	}
	
	
	  public static  String sendUrl(String strUrl){
	    	StringBuffer bufferRes = new StringBuffer();
	    	 try{  
	             URL url = new URL(strUrl);  
	             HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();  
	             //POST Request Define:   
	             urlConnection.setRequestMethod("GET");   
	             urlConnection.connect();  
	             
	             InputStream in = urlConnection.getInputStream();
	             BufferedReader read = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	             String valueString = null;
	             while ((valueString=read.readLine())!=null){
	                     bufferRes.append(valueString);
	             }
	             read.close();
	             in.close();
	             in = null;
	             if (urlConnection != null) {
	                 // 关闭连接
	            	 urlConnection.disconnect();
	             }
	         } catch (Exception e) {
	         	e.printStackTrace();
	         }
	    	 
	    	 return bufferRes.toString();
	     }
	  
	  @SuppressWarnings("deprecation")
		public static  String postContentClient(String urls,List<NameValuePair> nvps){ 
			java.util.logging.Logger.getLogger("org.apache.http.client.protocol").setLevel(java.util.logging.Level.OFF);
//			long st = new Date().getTime();
			String co = "";
			if(urls!=null&&!urls.isEmpty()){
				String url = urls.replaceAll("\\s", "%20");
				//HttpClient4.1的调用与之前的方式不同     
				DefaultHttpClient client = new DefaultHttpClient();
				//链接超时
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
				CookieStore cookieStore = new BasicCookieStore();
				BasicClientCookie  cookie = new BasicClientCookie("intl_locale", "en_US");
				cookieStore.addCookie(cookie);
				client.setCookieStore(cookieStore);
				//读取超时
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
				HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
				HttpResponse response;
				try {
					HttpPost httpPost = new HttpPost(url); 
			        
			        httpPost.setEntity(new UrlEncodedFormEntity(nvps)); 
					response = client.execute(httpPost);
					
					if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {  
						HttpEntity entity = response.getEntity();     
						if (entity != null) { 
							InputStreamReader in = new InputStreamReader(entity.getContent(), "utf-8");
							BufferedReader reader = new BufferedReader(in);     
							co = IOUtils.toString(reader);
							in.close();
							reader.close();
							in = null;
							reader = null;
						}  
						entity = null;
						httpPost = null;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					co = "";
				} catch (IOException e) {
					e.printStackTrace();
					co = "";
				}finally{
					if(client!=null){
						client.close();
					}
					response = null;
				}
			}
			return co;        
		}
	
}
