package com.cbt.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**   
 * @Title: TranslationUtil.java 
 * @Description: TODO
 * @Company : www.importExpress.com
 * @author Administrator
 * @date 2016年8月3日
 * @version V1.0   
 */

@Component
public class TranslationUtil {
	
	
	public static String translation(String catId,String str){
		//return urlDecoder(HttpClientUtil.doGet(TRANSLATION+"?catid="+catId+"&cntext="+urlEncoder(str)));
		List <NameValuePair> params = new ArrayList<NameValuePair>(); 
	    params.add(new BasicNameValuePair("catid", catId));
	    params.add(new BasicNameValuePair("cntext", str));
		String text = getContentClientPost("http://192.168.1.27:9089/translation/gettrans",params);
		return text;
	}

	
	/**URL编码
	 * @date 2016年6月3日
	 * @author abc
	 * @param cntext
	 * @return  
	 */
	public String urlEncoder(String cntext){
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

	
	@SuppressWarnings("deprecation")
	public static  String getContentClientPost(String urls,List <NameValuePair> params){ 
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol")
						.setLevel(java.util.logging.Level.OFF);
		if(urls==null||urls.isEmpty()){
			return "";
		}
		String co = "";
		//HttpClient4.1的调用与之前的方式不同     
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			String url = urls.replaceAll("\\s", "%20");
			//链接超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5*60*1000); 
			//读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5*60*1000);
			HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
			
			HttpPost httpPost = new HttpPost(url); 
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			
			response = client.execute(httpPost);
			
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {  
				HttpEntity entity = response.getEntity();     
				if (entity != null) { 
					InputStreamReader in = null;
					if(Pattern.compile("(taobao)|(tmall)|(1688)").matcher(urls).find()){
						in = new InputStreamReader(entity.getContent(), "gbk");
					}else{
						in = new InputStreamReader(entity.getContent(), HTTP.UTF_8);
					}
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
			co = "";
		} catch (IOException e) {
			co = "";
		}finally{
			if(client!=null){
				client.close();
			}
			response = null;
		}
		return co;     
	}
	
	
	
	public static void main(String[] args) {
		List <NameValuePair> params = new ArrayList<NameValuePair>(); 
	    params.add(new BasicNameValuePair("catid", "1"));
	    params.add(new BasicNameValuePair("cntext", "GOOD DSDFSD"));
		String text = getContentClientPost("http://192.168.1.27:8083/translation/gettrans",params);
		System.out.println(text);
		String[] split = text.split("/;/");
		for (String string : split) {
			System.out.println("---");
			String[] split2 = string.split("/!/");
			for (String string2 : split2) {
				System.out.println(string2+"-");
			}
		}
	}
	
}
