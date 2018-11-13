package com.cbt.warehouse.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 网页源码获取实现类
 * 
 * @author abc
 *
 */
public class DownloadMainUtil {
	private static int count = 0;// 链接超时
	public static final String TYPE = "TYPE_A";
	private static final Log LOG = LogFactory.getLog(DownloadMainUtil.class);

	/**
	 * 随机数生成函数
	 * 
	 * @param Max
	 * @param min
	 * @return
	 */
	public static int toRodom(int Max, int min) {
		int i = (int) (min + Math.random() * (Max - min));
		return i;
	}

	public static String getAgent() {
		// 伪装客户端
		String[] userAgent = new String[] { "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT6.0)",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT5.2)",
				"Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/42.0.2311.90 Safari/537.36 OPR/29.0.1795.47(Edition Campaign 56)",
				"Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/31.0.1650.63 Safari/537.36",
				"Mozilla/5.0 (Windows; U; Windows NT 5.2)Gecko/2008070208 Firefox/3.0.1",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070309 Firefox/2.0.0.3",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070803 Firefox/1.5.0.12 ",
				"Mozilla/5.0 (Windows; U; Windows NT 5.2)AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1Safari/525.13",
				// "Mozilla/5.0 (iPhone; U; CPU like Mac OS X)AppleWebKit/420.1
				// (KHTML, like Gecko) Version/3.0 Mobile/4A93Safari/419.3",
				"Opera/9.27 (Windows NT 5.2; U; zh-cn)", "Opera/8.0 (Macintosh; PPC Mac OS X; U; en)",
				"Links/0.9.1 (Linux 2.4.24; i386;)", "Links (2.1pre15; FreeBSD 5.3-RELEASE i386; 196x84)",
				"Mozilla/4.8 [en] (X11; U; SunOS; 5.7 sun4u)",
				"Gulper Web Bot 0.2.4 (www.ecsl.cs.sunysb.edu/~maxim/cgi-bin/Link/GulperBot)",
				"OmniWeb/2.7-beta-3 OWF/1.0",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)" };
		if (count >= userAgent.length) {
			count = 0;
		}
		String agent = userAgent[count];
		count++;
		userAgent = null;
		return agent;
	}

	/**
	 * 写入文件
	 * 
	 * @param page
	 * @throws Throwable
	 */
	public static void write2file(String page, String filepath) {

		File file = new File(filepath);
		if (!file.exists()) {// 测试此抽象路径名表示的文件或目录是否存在。
			file.getParentFile().mkdirs();// 返回此抽象路径名父目录的抽象路径名；创建
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOG.info("writeStringToFile error0");
			} // 不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件

		}
		try {
			FileUtils.writeStringToFile(file, page, "gbk");
		} catch (IOException e) {
			LOG.info("writeStringToFile error");
		}
	}

	public static Boolean isValid(String agent, String ip, String port) {
		Boolean result;
		String url = "http://1111.ip138.com/ic.asp";
		String page = "";
		setAddress(ip, port);
		try {
			Response con = connectUrl(agent, url, 0);
			page = con.body();
			String pag = StrUtils.matchStr(page, "(?:<center>)(.*?)(</center>)");
			LOG.warn(pag);
			if (Pattern.compile("(" + ip + ")").matcher(pag).find()) {
				result = true;
			} else {
				result = false;
			}
		} catch (IOException e) {
			result = false;
			LOG.warn("校验代理服务器异常:" + e);
		}
		return result;
	}

	/**
	 * 没有代理服务器，
	 * 
	 * @param flag
	 *            0:search;1:goods
	 * @return
	 */
	public static String getJsoup(String urls, int flag, Set set) {
		String content = null;// 网页内容
		if (urls != null && !urls.isEmpty()) {
			String url = urls.replaceAll("\\s", "%20").replaceAll("&amp;", "&");
			url = url.replace("http://www.aliexpress.com/", "https://www.aliexpress.com/");
			// long st = new Date().getTime();

			if (flag == 1 && url.indexOf("www.aliexpress.com") > -1) {
				content = getContentUnit(url, set);
			} else {
				content = getByJsoup(url, flag, set);
			}
			// content = getAjaxCotnent(url);

			// LOG.warn(" jsoup get page:"+(new Date().getTime()-st));
		}
		return content;
	}

	/**
	 * 使用Jsoup来获取网页源码
	 * 
	 * @date 2016年10月10日
	 * @author abc
	 * @param urls
	 * @param flag
	 *            0-搜索用 1-单页
	 * @param set
	 * @return
	 */
	public static String getByJsoup(String urls, int flag, Set set) {
		String content = null;// 网页内容
		if (StringUtil.isBlank(urls)) {
			return null;
		}
		String url = urls.replaceAll("\\s", "%20").replaceAll("&amp;", "&");
		url = url.replace("http://www.aliexpress.com/", "https://www.aliexpress.com/");
		long st = new Date().getTime();
		boolean find = StrUtils.isFind(url, "(alibaba)|(aliexpress)");
		boolean tbfind = StrUtils.isFind(url, "(taobao)|(tmall)");
		String ip = IpSelect.ip();
		if (set != null) {
			set.setIp(ip);
		}
		// Map<String, String> cookies = null;
		// if(!Pattern.compile("(www.import-express.com)|(198.38.90.14)").matcher(AppConfig.ip).find()){
		// try {//www.aliexpress.com/
		// String host_url = StrUtils.matchStr(url,
		// "(http\\://(.*\\.)+((com)|(cn)))")+"/";
		// if(!host_url.isEmpty()&&Pattern.compile("http\\://.*/").matcher(host_url).find()){
		// cookies = Jsoup.connect(host_url).execute().cookies();
		// }
		// } catch (IOException e1) {
		// cookies = null;
		// }
		// }
		Connection conn = null;
		try {
			conn = Jsoup.connect(url).header("Connection", "keep-alive").header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "en").header("Content-Language", "en-US")
					.header("Cache-Control", "no-cache").header("Content-Type", "text/html;charset=UTF-8")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("X-Forwarded-For", ip).header("HTTP_CLIENT_IP", ip).header("HTTP_X_FORWARDED_FOR", ip)
					.header("WL-Proxy-Client-IP", ip).header("Proxy-Client-IP", ip).userAgent(getAgent())
					.followRedirects(true);

			if (conn != null) {
				// 加cookies
				// if(cookies!=null&&!cookies.isEmpty()){
				// conn = conn.cookies(cookies);
				// }
				// 分情况设置超时系数
				if (flag == 0 && tbfind) {
					// 1.淘宝搜索超时
					conn = conn.timeout(2 * 1000 + 500);
				} else if (flag == 1 && tbfind) {
					// 2.淘宝单页商品超时
					conn = conn.timeout(3 * 1000);
				} else if (flag == 0 && find) {
					// 3.alibaba搜索超时
					conn = conn.timeout(5 * 1000);
				} else {
					// 4.alibaba 以及其他单页商品超时
					conn = conn.timeout(flag == 1 ? 5 * 1000 : 3 * 1000 + 500);
				}
				// conn.wait(300);
				content = conn.execute().body();
				// conn.
				content = content.replaceAll("锛", "").replaceAll("&yen;", "RMB");
				String p = "<title>Alibaba.*Manufacturer.*Directory.*-.*Suppliers,.*Manufacturers,.*Exporters.*Importers.*</title>";
				if (flag == 1 && StrUtils.isMatch(content, "(" + p + ")")) {
					content = null;
					LOG.error("error-ip:" + ip + "--url:" + url);
				}
			}
		} catch (HttpStatusException e) {
			LOG.warn("Exception url:" + url + "--error:" + e.toString());
			content = "httperror";
		} catch (Exception e) {
			// e.printStackTrace();
			content = null;
			LOG.warn("Exception url:" + url + "--error:" + e.toString());
		} finally {
			conn = null;
			ip = null;
		}
		LOG.warn("	jsoup get page:" + (new Date().getTime() - st));
		return content;
	}

	/**
	 * 设置系统代理服务器
	 * 
	 * @param ip
	 * @param port
	 */
	public static void setAddress(String ip, String port) {
		System.getProperties().setProperty("proxySet", "true");
		// 代理服务器
		System.getProperties().setProperty("http.proxyHost", ip.trim());
		// 代理端口
		System.getProperties().setProperty("http.proxyPort", port.trim());
	}

	/**
	 * 链接网页
	 * 
	 * @param agent
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Response connectUrl(String agent, String url, int flag) throws IOException {
		long st = new Date().getTime();
		boolean find = Pattern.compile("(alibaba)").matcher(url).find();

		boolean tbfind = Pattern.compile("(taobao)|(tmall)").matcher(url).find();

		long ste = new Date().getTime();
		Connection conn = Jsoup.connect(url).header("CLIENT-IP", "64.186.47.179")
				.header("X-FORWARDED-FOR", "64.186.47.179").header("Connection", "keep-alive")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("User-Agent", agent).userAgent(agent).followRedirects(true);

		// 分情况设置超时系数
		if (flag == 0 && tbfind) {
			// 1.淘宝搜索超时
			conn = conn.timeout(2 * 1000 + 500);
			LOG.warn("TB search  timeout coefficient：2.5*1000");
		} else if (flag == 1 && tbfind) {
			// 2.淘宝单页商品超时
			LOG.warn("TB goods timeout coefficient：3*1000");
			conn = conn.timeout(3 * 1000);
		} else if (flag == 0 && find) {
			// 3.alibaba搜索超时
			LOG.warn("alibaba search timeout coefficient：2*1000");
			conn = conn.timeout(2 * 1000);
		} else {
			// 4.alibaba 以及其他单页商品超时
			if (flag == 1) {
				LOG.warn("alibaba goods timeout coefficient：2*1000");
				conn = conn.timeout(2 * 1000);
			}
		}

		// conn = conn.method(Method.POST);
		long stt = new Date().getTime();
		LOG.warn("（0）Jsoup connect(): " + (stt - ste));
		// 执行连接
		Response execu = conn.execute();
		long ed = new Date().getTime();

		LOG.warn("（1）Jsoup execute(): " + (ed - stt));

		LOG.warn("（2）Jsoup connect: " + (ed - st));
		return execu;
	}

	/**
	 * 根据URL抓取网页内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @author yinlimei
	 */
	public static String getUrl(String url) {
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
			con.setConnectTimeout(1 * 1000);
			InputStream in = new BufferedInputStream(con.getInputStream());
			theHTML = new InputStreamReader(in);
			long ed = new Date().getTime();
			LOG.warn("url:" + (ed - st));
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
		LOG.warn("获取url信息所用时间为：" + (end - start));
		return content.toString();
	}

	/**
	 * 直接通过读取网页数据流来抓取网页内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getContentUrl(String url, String charset) {
		long st = new Date().getTime();
		HttpURLConnection con = null;
		StringBuffer content = new StringBuffer();
		/* 实例化一个HttpClient客户端 */
		URL u, realURL;
		InputStreamReader theHTML;
		// 对于tinydeal 与amazon 需要用BufferedReader读取网页源码
		String pattern = "(tinydeal)|(amazon)";
		if (Pattern.compile(pattern).matcher(url).find()) {
			try {
				long st3 = new Date().getTime();
				u = new URL(url);
				con = (HttpURLConnection) u.openConnection();

				con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");// IE代理进行下载
				// con.setInstanceFollowRedirects(true);
				con.setConnectTimeout(3000);
				con.setReadTimeout(3000);
				// con.setRequestMethod("POST");

				while (con.getResponseCode() == 302) {
					// System.out.println("响应码：" + con.getResponseCode());
					String realUrl = con.getHeaderField("Location");
					con.disconnect();
					realURL = new URL(realUrl);
					con = (HttpURLConnection) realURL.openConnection();
				}

				if (con.getResponseCode() == 200) {
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
				LOG.warn("getContentOld��HttpURLConnection��ȡ��ҳ" + (ed1 - st3));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
		} else {// 其他的可以用InputStreamReader读取网页源码
			try {
				long st4 = new Date().getTime();
				u = new URL(url);
				InputStream in = new BufferedInputStream(u.openStream());
				theHTML = new InputStreamReader(in, charset);
				int c;
				long st2 = new Date().getTime();
				LOG.warn("getContentOld里链接url:" + (st2 - st4));
				while ((c = theHTML.read()) != -1) {
					content.append((char) c);
				}
				in.close();
				long ed = new Date().getTime();
				LOG.warn("getContentOld里URL读取时间：" + (ed - st2));
				LOG.warn("getContentOld里URL直接获取网页" + (ed - st));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content.toString().replace("锛", "").replace("&nbsp;", " ").replace("&yen;", "RMB"); // 其换掉html文档中的空格符
	}

	/**
	 * 调用curl命令行，通过代理服务器获取网页源码
	 * 
	 * @param url
	 *            网页链接
	 * @param charset
	 *            网页编码格式
	 * @return String 网页源码
	 */
	public static String getContentCurl(String url, String charset) {
		String url_r = url;// .replace("&","%26" ).replace("#",
							// "%23").replace("=", "%3D");
		// 代理服务器
		// String user_agent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT;
		// DigExt)";
		String user_agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11";
		// 拼写命令行
		String cmd = "curl " + "-A " + "\"" + user_agent + "\" " + " -L " + url_r;
		String line = null;
		StringBuffer content = new StringBuffer();
		BufferedReader br = null;
		Process p = null;
		try {
			// 执行curl命令
			p = Runtime.getRuntime().exec(cmd);

			br = new BufferedReader(new InputStreamReader(p.getInputStream(), charset));
			while ((line = br.readLine()) != null) {
				content.append(line);
				// content.append("\n");
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

		return content.toString().replace("锛", "").replace("&nbsp;", " ").replace("&", "&").replace("&yen;", "RMB"); // 其换掉html文档中的空格符
																														// ;
	}

	/**
	 * 利用htmlunit获取网页数据
	 * 
	 * @param url
	 * @return
	 */
	public static String getContentUnit(String url, Set set) {
		/* turn off annoying htmlunit warnings */
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		long st = new Date().getTime();
		String page = "";
		WebClient wc = new WebClient(BrowserVersion.CHROME);
		try {
			WebRequest request = new WebRequest(new URL(url));
			request.setCharset("UTF-8");
			String ip = IpSelect.ip();
			if (set != null) {
				set.setIp(ip);
			}
			request.setAdditionalHeader("X-Forwarded-For", ip);
			request.setAdditionalHeader("HTTP_CLIENT_IP", ip);
			request.setAdditionalHeader("HTTP_X_FORWARDED_FOR", ip);
			request.setAdditionalHeader("WL-Proxy-Client-IP", ip);
			request.setAdditionalHeader("Proxy-Client-IP", ip);
			wc.getOptions().setUseInsecureSSL(true);
			wc.getOptions().setDoNotTrackEnabled(false);
			wc.getOptions().setJavaScriptEnabled(false); // 启用JS解释器，默认为true
			wc.getOptions().setCssEnabled(false); // 禁用css支持
			wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
			wc.getOptions().setTimeout(2 * 1000); // 设置连接超时时间
													// ，这里是10S。如果为0，则无限期等待
			wc.waitForBackgroundJavaScript(1 * 1000);
			wc.setJavaScriptTimeout(1 * 1000);
			NicelyResynchronizingAjaxController ctr = new NicelyResynchronizingAjaxController();
			wc.setAjaxController(ctr);
			HtmlPage pagehtml = wc.getPage(request);
			page = pagehtml.asXml().toString(); // 以xml的形式获取响应文本

		} catch (Exception e) {
			page = "httperror";
			// e.printStackTrace();
			LOG.warn(url + "---" + e);
		}
		String content = "";
		if (StrUtils.isFind(url, "((tmall)|(taobao))")) {
			long st2 = new Date().getTime();
			content = HtmlUtils.htmlUnescape(HtmlUtils.htmlEscape(page).replace("&yen;", "RMB"));
			LOG.warn(url + "----getContentUnit's html:" + (new Date().getTime() - st2));
		} else {
			content = page;
		}
		LOG.warn(url + "-getContent by htmlunit-" + (new Date().getTime() - st));

		// wc.closeAllWindows();
		return content.replace("锛", "").replace("&nbsp;", " ").replace("&", "&"); // 其换掉html文档中的空格符
																					// ;
	}

	@SuppressWarnings("deprecation")
	public static String getContentClient(String urls, String ip) {
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol").setLevel(java.util.logging.Level.OFF);
		// long st = new Date().getTime();
		String co = "";
		if (urls != null && !urls.isEmpty()) {
			String url = urls.replaceAll("\\s", "%20");
			if (url.startsWith("//")) {
				// 开头加上http:
				url = "http:" + url;
			}
			// HttpClient4.1的调用与之前的方式不同
			DefaultHttpClient client = new DefaultHttpClient();
			if (urls.indexOf("http://119.148.161.83:8080") > -1) {
				// 链接超时
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 60 * 10);
				// 读取超时
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 60 * 10);
			} else {
				// 链接超时
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
				// 读取超时
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
			}

			CookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie cookie = new BasicClientCookie("intl_locale", "en_US");
			cookieStore.addCookie(cookie);
			client.setCookieStore(cookieStore);
			HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			HttpResponse response;
			try {
				HttpGet httpGet = new HttpGet(url);
				if (ip != null && !ip.isEmpty() && Pattern.compile("(\\d+\\.){3}\\d+").matcher(ip).matches()) {
					httpGet.setHeader("X-Forwarded-For", ip);
					httpGet.setHeader("HTTP_CLIENT_IP", ip);
					httpGet.setHeader("HTTP_X_FORWARDED_FOR", ip);
					httpGet.setHeader("WL-Proxy-Client-IP", ip);
					httpGet.setHeader("Proxy-Client-IP", ip);
				}
				// httpGet.setHeader("Referer",
				// "https://item.taobao.com/item.htm?&id=527041229261");
				// String agent = getAgent();
				// httpGet.setHeader("Connection", "keep-alive");
				// httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
				// httpGet.setHeader("Accept-Language",
				// "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
				// httpGet.setHeader("Content-Language", "en-US");
				// httpGet.setHeader("Cache-Control", "no-cache");
				// httpGet.setHeader("Content-Type", "text/html;charset=gbk");
				// httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				// ;
				// httpGet.setHeader("User-Agent", agent);
				response = client.execute(httpGet);
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStreamReader in = null;
						if (Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()) {
							in = new InputStreamReader(entity.getContent(), "gbk");
						} else {
							in = new InputStreamReader(entity.getContent(), "utf-8");
						}
						BufferedReader reader = new BufferedReader(in);
						co = IOUtils.toString(reader);
						in.close();
						reader.close();
						in = null;
						reader = null;
						/*
						 * String line = null; if (entity.getContentLength() >
						 * 0) { strBuf = new StringBuffer((int)
						 * entity.getContentLength()); while ((line =
						 * reader.readLine()) != null) { strBuf.append(line); }
						 * } } if (entity != null) { entity.consumeContent();
						 */
					}
					entity = null;
					httpGet = null;
				}
			} catch (ClientProtocolException e) {
				co = "";
			} catch (IOException e) {
				co = "";
			} finally {
				if (client != null) {
					client.close();
				}
				response = null;
			}
		}
		return co.replaceAll("鈥�", ""); // 其换掉html文档中的空格符 ;
	}

	/**
	 * phantomjs
	 * 
	 * @date 2016年10月10日
	 * @author abc
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getAjaxCotnent(String url) {
		Runtime rt = Runtime.getRuntime();
		Process p;
		StringBuffer sbf = new StringBuffer();
		try {
			p = rt.exec("C:/phantomjs.exe C:/codes.js " + url);
			// 这里我的codes.js是保存在c盘下面的phantomjs目录
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				sbf.append(tmp);
			}
			// System.out.println(sbf.toString());
			is.close();
			br.close();
			p.destroy();
		} catch (IOException e) {
			LOG.error("url--" + url + "---getAjaxCotnent--" + e);
		}
		return sbf.toString();
	}

	/**
	 * 启用js解析器的HtmlUnit爬虫方法
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String getJSContent(String url) throws Exception {

		WebRequest request = new WebRequest(new URL(url));
		request.setCharset("UTF-8");
		String ip = IpSelect.ip();
		request.setAdditionalHeader("X-Forwarded-For", ip);
		request.setAdditionalHeader("HTTP_CLIENT_IP", ip);
		request.setAdditionalHeader("HTTP_X_FORWARDED_FOR", ip);
		request.setAdditionalHeader("WL-Proxy-Client-IP", ip);
		request.setAdditionalHeader("Proxy-Client-IP", ip);

		WebClient wc = new WebClient(BrowserVersion.CHROME);
		wc.getOptions().setUseInsecureSSL(true);
		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
		wc.setAjaxController(new NicelyResynchronizingAjaxController());// 设置支持AJAX
		wc.getOptions().setCssEnabled(false); // 禁用css支持
		wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
		wc.getOptions().setRedirectEnabled(true);
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);// 当HTTP的状态非200时是否抛出异常
		wc.getOptions().setTimeout(3000); // 设置请求超时时间为3秒 ，如果为0，则无限期等待
		wc.getOptions().setDoNotTrackEnabled(false);

		HtmlPage page = wc.getPage(request);
		wc.waitForBackgroundJavaScript(3 * 1000l);// 等待js执行
		wc.setJavaScriptTimeout(1 * 1000l);

		return page.asXml();
	}
}
