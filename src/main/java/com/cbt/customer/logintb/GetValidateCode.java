package com.cbt.customer.logintb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GetValidateCode extends HttpServlet {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GetValidateCode.class);
	private static final long serialVersionUID = 1L;
	
	private static CookieStore sslcookies = new BasicCookieStore();
	private static CookieStore cookies = new BasicCookieStore();
	
	static String TPL_username="3053568516@qq.com"; 
	
	static String path = null;
	public static CloseableHttpClient createSSLClientDefault(boolean isSSL){
		if(isSSL){
			SSLContext sslContext = null;
			/*try {
				sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					//信任所有
					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						return true;
					}
				}).build();
			} catch (KeyManagementException e) { 
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}*/
			if(null != sslContext){
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(sslcookies).build();
			}else{
				return  HttpClients.custom().setDefaultCookieStore(cookies).build();
			}
		}else{
			return  HttpClients.custom().setDefaultCookieStore(cookies).build();
		}
	}
	
	@Override
	public void init() throws ServletException {
		path = getServletContext().getRealPath("/");
		//System.out.println(path);
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getParameter("askurl");
		//String codeUrl = request.getParameter("codeUrl");
		String Friendship_remind = request.getParameter("Friendship_remind");
		//if(getCodeUrl() == null) {
			
		//}else {
			//handleVilidateCode(codeUrl);
			handleVilidateCode(getCodeUrl());
			request.setAttribute("askurl", url);
			request.setAttribute("Friendship_remind", Friendship_remind);
			request.getRequestDispatcher("/view/showVCode.jsp").forward(request, response);
		//}
	}
	
	public static JSONObject String2Json(String string){
		if(string == null){
			return null;
		}
		String jsonString = string.trim();
		if(jsonString.startsWith("{") && jsonString.endsWith("}")){
			JSONObject jb = null;
			try {
				jb = new JSONObject(jsonString);
				return jb;
			}catch (Exception e) {
				return null;
			}
		}else if(jsonString.startsWith("[") && jsonString.endsWith("]")){
			JSONArray ja = null;
			try {
				ja = new JSONArray(jsonString);
				return (JSONObject)ja.get(0);
			}catch(Exception e){
				return null;
			} 
		}
		return null;
	}
	
	public static void headerWrapper(AbstractHttpMessage methord){
		methord.setHeader("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0");
		methord.setHeader("accept", "*/*");
		methord.setHeader("accept-language", "zh-CN");
		methord.setHeader("Accept-Encoding", "gzip, deflate");
		
	}
	
	public static String getCodeUrl(){
		CloseableHttpClient httpClient = createSSLClientDefault(true);
		//验证登陆的用户是否需要验证码登陆
		HttpPost hp = new HttpPost("https://login.taobao.com/member/request_nick_check.do?_input_charset=utf-8");
		headerWrapper(hp);
		hp.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", TPL_username));
		HttpResponse httpresponse;
		try {
			hp.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			httpresponse = httpClient.execute(hp);
			HttpEntity entity = httpresponse.getEntity(); 
			//body返回是否需要验证码（true/false)和淘宝生成验证码图片的url
			String body = EntityUtils.toString(entity);  
			EntityUtils.consume(entity);
			JSONObject J_obj = String2Json(body);
			boolean isNeed = (Boolean) J_obj.get("needcode");
			LOG.warn("needcode:" + isNeed);
			if(isNeed){ //需要登陆验证码 
				String code_url = (String) J_obj.get("url");
				LOG.warn("code_url:" + code_url);
				//返回淘宝生成验证码图片的url
				return code_url;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}  
		return null;
	}

	public static void handleVilidateCode(String url){
		CloseableHttpClient httpClient = createSSLClientDefault(true);
		HttpGet hg = new HttpGet(url);
		HttpResponse httpresponse;
		int n = 0;
		try {
			httpresponse = httpClient.execute(hg);
			HttpEntity entity = httpresponse.getEntity();
			InputStream content = entity.getContent();
			byte[] b = IOUtils.toByteArray(content);
			FileUtils.writeByteArrayToFile(new File(path+"//1.jpeg"), b);
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
