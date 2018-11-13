package com.cbt.parse.service;

import com.cbt.parse.bean.ProxyAddress;
import com.cbt.parse.bean.Set;
import com.cbt.parse.dao.ServerDao;
import com.cbt.util.AppConfig;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
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
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**网页源码获取实现类
 * @author abc
 *
 */
public class DownloadMain {
	private static int count = 0;//链接超时
	public static final String TYPE = "TYPE_A";
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DownloadMain.class);
	

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
	
	
	
	
/**代理服务器轮换
 * @param url
 * @param flag
 * @param sd
 * @param id
 * @param error
 * @return
 */
public static int getid(String url,int flag,ServerDao sd,int id){
	int temp = id;
	if(flag==1&&Pattern.compile("(alibaba)").matcher(url).find()){
		ProxyAddress.active++;
		if(ProxyAddress.active > sd.queryData().size()){
			ProxyAddress.active = 1;
		}
		temp = ProxyAddress.active-1;
	}else{
		ProxyAddress.count++;
		if(ProxyAddress.count > sd.query().size()){
			ProxyAddress.count = 1;
		}
		temp = ProxyAddress.count-1;
	}
	return temp;
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

	

/**没有代理服务器，使用Jsoup来获取网页源码
 * @param flag 0:search;1:goods
 * @return
 */
public static String getJsoup(String urls,int flag,Set set){
	String content  =null;//网页内容
	if(urls!=null&&!urls.isEmpty()){
		String url = urls.replaceAll("\\s", "%20").replaceAll("&amp;", "&");
		long st = new Date().getTime();
		boolean find = Pattern.compile("(alibaba)|(aliexpress)").matcher(url).find();
		boolean tbfind = Pattern.compile("(taobao)|(tmall)").matcher(url).find();
		String agent;//伪客户端
		agent = getAgent();
		String ip = IpSelect.ip();
		if(set!=null){
			set.setIp(ip);
		}
		Map<String, String> cookies = null;
		if(!Pattern.compile("(www.import-express.com)|(198.38.90.14)").matcher(AppConfig.ip).find()){
			try {//www.aliexpress.com/
				String host_url =DownloadMain.getSpiderContext(url, "(http\\://(.*\\.)+((com)|(cn)))")+"/";
				if(!host_url.isEmpty()&&Pattern.compile("http\\://.*/").matcher(host_url).find()){
					cookies = Jsoup.connect(host_url).execute().cookies();
				}
			} catch (IOException e1) {
				cookies = null;
			}
		}
		Connection conn = null;
		try {
			conn = Jsoup.connect(url)
						.header("Connection", "keep-alive")
						.header("Accept-Encoding", "gzip, deflate")
						.header("Accept-Language", "en")
						.header("Content-Language", "en-US")
						.header("Cache-Control", "no-cache")
						.header("Content-Type", "text/html;charset=UTF-8")
						.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") 
						.header("X-Forwarded-For", ip)
						.header("HTTP_CLIENT_IP", ip)
						.header("HTTP_X_FORWARDED_FOR", ip)
						.header("WL-Proxy-Client-IP", ip)
						.header("Proxy-Client-IP", ip)
						.userAgent(agent).followRedirects(true);
			
			if(conn!=null){
				//加cookies
				if(cookies!=null&&!cookies.isEmpty()){
					conn = conn.cookies(cookies);
				}
				//分情况设置超时系数
				if(flag==0&&tbfind){
					//1.淘宝搜索超时
					conn = conn.timeout(2*1000+500);
				}else if(flag==1&&tbfind){
					//2.淘宝单页商品超时
					conn = conn.timeout(3*1000);
				}else if(flag==0&&find){
					//3.alibaba搜索超时
					conn = conn.timeout(2*1000);
				}else{
					//4.alibaba 以及其他单页商品超时
					if(flag==1){
						conn = conn.timeout(3*1000);
					}else{
						conn = conn.timeout(2*1000+500);
					}
				}
				content = conn.execute().body();
				content = content.replaceAll("锛", "").replaceAll("&yen;", "RMB");
				String p = "<title>Alibaba.*Manufacturer.*Directory.*-.*Suppliers,.*Manufacturers,.*Exporters.*Importers.*</title>";
				if(content!=null&&Pattern.compile("("+p+")").matcher(content).find()){
					content = null;
					LOG.error("error-ip:"+ip+"--url:"+url);
				}
			}
		} catch (HttpStatusException e) {
			content = "httperror";
		}catch (Exception e) {
			content = null;
			LOG.warn("Exception url:"+url+"--error:"+e.toString());
		}finally{
			conn = null;
			ip = null;
			agent = null;
		}	
		long ed = new Date().getTime();
		LOG.warn("	jsoup get page:"+(ed-st));
	}
	return content;
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


	
	/**使用Jsoup来获取网页源码
	 * @param url
	 * @return
	 */
	public static String getContentJsoup(String url,int id,int flag){
		long st = new Date().getTime();
		String content  ="";//网页内容
		String agent;//伪客户端
		String ip;//代理服务器ip
		String port;//代理服务器端口
//		Boolean valid;
		ServerDao sd = new ServerDao();
		//所有数据
		ArrayList<HashMap<String,String>> query = sd.query();
		//状态为valid数据(即代理服务器是有效的)
//		ArrayList<HashMap<String,String>> queryValid = sd.queryValid();
		//状态为active数据
		ArrayList<HashMap<String,String>> queryData = sd.queryData();
		int size = queryData.size();
		if(size<10){
			LOG.warn("please add proxy......");
		}
		if(size > 0){
			boolean find = Pattern.compile("(alibaba)").matcher(url).find();
			//查询数据库 获取代理服务器可用ip
			if(flag==1&&find){
				ProxyAddress.active = id+1;
				ip = queryData.get(id).get("ip");
				port = queryData.get(id).get("port");
			}else{
				ProxyAddress.count = id+1;
				ip = query.get(id).get("ip");
				port = query.get(id).get("port");
			}
			/*
			if(Pattern.compile("(taobao)").matcher(url).find()){
				agent = getAgent();
				LOG.warn("agent:"+"0."+agent);
			}else if(Pattern.compile("(tmall)").matcher(url).find()){
				agent = getAgent();
				LOG.warn("agent:"+"0."+agent);
			}else{
				count++;
				LOG.warn("agent:"+(count-1)+"."+agent);
			}
			*/
			agent = getAgent();
			
			LOG.warn("proxy："+ip+"---"+port);
	//		if(flag==1){
	//			//判断当前的代理服务器是否有效
	//			valid = isValid(agent,ip, port);
	//		}else{
	//		}
	//		valid = true;
			if(true){
				//设置代理
				setAddress(ip, port);
				LOG.warn("valid proxy:"+ip+"---"+port);
				//链接网页
				Response response;
				try {
						long st1 = new Date().getTime();
						response = connectUrl(agent, url,flag);
						long st2 = new Date().getTime();
						LOG.warn("1.Jsoup connect url："+(st2-st1));
						//链接网页成功后获取网页源码
						content = response.body();
						
					} catch (IOException e) {
						long st3 = new Date().getTime();
						//注意： 只针对alibaba和wholesale的单个商品获取数据时，才做代理服务器的质量判断									
						if(flag==1&&find){
							String error = e.toString();
							if(e.toString().length()>100){
							error = e.toString().substring(0,100);
							}
							sd.addError(ip, port, "inactive", error);
						}
						//若代理服务器连接超时 或者 有 其他错误（比如找不到网页）  就改状态为 ”inactive"										
	//					/*
						if(e instanceof SocketTimeoutException){
				        	//出现超时异常时，重新链接
				        		content = "timeout";
				        		LOG.warn("Readtime out......");
				        }else{ 
				        	LOG.warn("http error...........");
				        	//网络链接出现异常（使用代理时服务器无法连接、服务器拒绝访问等）
				        	content ="httperror";
				        }
	//			        */
						long st4 = new Date().getTime();
						LOG.warn("Exception handling time："+(st4-st3));
					}
			}
			/*else{
				//代理服务器无效
				sd.updateValid(ip, 0);
				id++;
				if(id>=sd.queryValid().size()){
					id = 0;
				}
				if(flag==1&&find){
					ProxyAddress.active = id+1;
				}else{
					ProxyAddress.count = id+1;
				}
				content = getContentJsoup(url, id, flag);
			}
	*/		
			long ed = new Date().getTime();
			LOG.warn("2.Jsoup get page："+(ed-st));
			
			//页面错误处理
			if(flag==1&&find){
				if(Pattern.compile("(<title>Source)").matcher(content).find()){
					sd.addError(ip, port, "inactive", "Other Page content");
				}else if(Pattern.compile("(错误-未找到该页)").matcher(content).find()){
					sd.addError(ip, port, "inactive", "Page not found");
				}else if(Pattern.compile("(Serv-U - Error Occurred)").matcher(content).find()){
					sd.addError(ip, port, "inactive", "Serv-U - Error Occurred");
				}else if(content.length()>10&&content.length()<1000){
					sd.addError(ip, port, "inactive", "insufficient content");
				}
			}
		}else{
			content = "add proxy";
			LOG.warn("ERROR:have no available proxy.");
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
	
	
   	/**利用htmlunit获取网页数据
	 * @param url
	 * @return
	 */
//	public static String getContentUnit(String url){
//		 /* turn off annoying htmlunit warnings */
//        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
//		long st = new Date().getTime();
//		String page ="";
//        WebClient wc = new WebClient(BrowserVersion.CHROME);
//        wc.getOptions().setUseInsecureSSL(true);
//        wc.getOptions().setDoNotTrackEnabled(false);
//        wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true
//        wc.getOptions().setCssEnabled(false); //禁用css支持
//        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
//        wc.getOptions().setTimeout(1*1000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
//        wc.waitForBackgroundJavaScript(3*1000);
//        wc.setJavaScriptTimeout(1*1000);
//        NicelyResynchronizingAjaxController ctr = new NicelyResynchronizingAjaxController();
//        wc.setAjaxController(ctr);
//        HtmlPage pagehtml;
//		try {
//			pagehtml = wc.getPage(url);
//			page = pagehtml.asXml().toString(); //以xml的形式获取响应文本
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String content = "";
//		if(Pattern.compile("(tmall)|(taobao)").matcher(url).find()){
//			long st2 = new Date().getTime();
//			content = HtmlUtils.htmlUnescape(HtmlUtils.htmlEscape(page).replace("&yen;", "RMB"));
//			long ed2 = new Date().getTime();
//			LOG.warn("getContentUnit里对html特殊字符转换："+(ed2-st2));
//		}else{
//			content = page;
//		}
//		long ed = new Date().getTime();
//			LOG.warn("getContentUnit里使用htmlunit获取网页"+(ed - st));
//
//		wc.closeAllWindows();
//		return content.replace("锛", "")
//					  .replace("&nbsp;", " ")
//					  .replace("&", "&"); //其换掉html文档中的空格符 ;
//	}
    
    
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
						if(urls.indexOf("192.168.1.48") > -1){
							in = new InputStreamReader(entity.getContent(), "utf-8");
							
						}else if(Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()){
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
	
	
	

	
}
