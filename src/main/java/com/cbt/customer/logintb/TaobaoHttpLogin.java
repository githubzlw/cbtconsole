package com.cbt.customer.logintb;

import com.cbt.bean.UserBean;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.servlet.Currency;

import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaobaoHttpLogin extends HttpServlet {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TaobaoHttpLogin.class);
	private static final long serialVersionUID = 1L;
	
	private static CookieStore sslcookies = new BasicCookieStore();
	private static CookieStore cookies = new BasicCookieStore();
	
	static String TPL_username="3053568516@qq.com"; 
	static String TPL_password="3053568516lizhanjun";  
	static CloseableHttpClient httpClient;
	public static CloseableHttpClient createSSLClientDefault(boolean isSSL){
		if(isSSL){
			SSLContext sslContext = null;
//			try {
//				//sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//					//信任所有
//					public boolean isTrusted(X509Certificate[] chain,
//							String authType) throws CertificateException {
//						return true;
//					}
//				}).build();
//			} catch (KeyManagementException e) {
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (KeyStoreException e) {
//				e.printStackTrace();
//			}
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
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.warn("==============第一次登陆获取页面开始==================");
		long t1 = new Date().getTime();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String url = request.getParameter("askurl");
		//url = url.replace("////", "//");
		String TPL_checkcode = request.getParameter("TPL_checkcode");
		params.add(new BasicNameValuePair("TPL_checkcode", TPL_checkcode)); 
	    httpClient = GetValidateCode.createSSLClientDefault(true);
		//CloseableHttpClient httpClient = createSSLClientDefault(true);
		HttpPost httpPost = new HttpPost("https://login.taobao.com/member/login.jhtml");
		headerWrapper(httpPost);
		httpPost.setHeader("accept-Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		params.add(new BasicNameValuePair("TPL_password", TPL_password));  
		params.add(new BasicNameValuePair("TPL_username", TPL_username));
		params.add(new BasicNameValuePair("newlogin", "1"));   
		params.add(new BasicNameValuePair("callback", "1"));  
		httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));  
		// 发送请求  
		HttpResponse httpresponse = httpClient.execute(httpPost);  
		HttpEntity entity = httpresponse.getEntity();  
		String body = EntityUtils.toString(entity);  
		//System.out.println(body);  
		String sessionid = body.substring(body.indexOf("token")+8, body.length()-3);
		LOG.warn(sessionid);
		
		
		HttpGet hg1 = null;
		try {
			 hg1 = new HttpGet("https://passport.alipay.com/mini_apply_st.js?site=0&token="+sessionid+"&callback=vstCallback65");
		} catch (Exception e) {//说明验证码登陆错误 提醒用户重新输入
			//String codeUrl = GetValidateCode.getCodeUrl();
			//设置友情提示语
			String Friendship_remind ="Friendship remind:Entered an incorrect verification code, please enter again";
			//获取淘宝生成验证码图片的链接
			//String codeUrl = GetValidateCode.getCodeUrl();
			//request.getRequestDispatcher("/getCode?codeUrl="+codeUrl+"&Friendship_remind="+Friendship_remind+"&askurl="+url+"").forward(request, response);
			request.getRequestDispatcher("/getCode?Friendship_remind="+Friendship_remind+"&askurl="+url+"").forward(request, response);
			return;
		}
		headerWrapper(hg1);
		HttpResponse httpresponse1 = httpClient.execute(hg1);  
		HttpEntity entity1 = httpresponse1.getEntity();  
		String body1 = EntityUtils.toString(entity1);
		
		String st = "";
		String regex = "vstCallback65\\((.*)\\)";
		Pattern compile = Pattern.compile(regex);
		Matcher m = compile.matcher(body1);
		while(m.find()){
			String group = m.group(1);
			JSONObject string2Json = String2Json(group);
			try {
				JSONObject object = (JSONObject) string2Json.get("data");
				st = (String) object.get("st");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		HttpGet hg2 = new HttpGet("https://login.taobao.com/member/vst.htm?st="+st+"&params=style%3Dminisimple%26sub%3Dtrue%26TPL_username%3D"+TPL_username+"%26loginsite%3D0%26from_encoding%3D%26not_duplite_str%3D%26guf%3D%26full_redirect%3D%26isIgnore%3D%26need_sign%3D%26sign%3D%26from%3Ddatacube%26TPL_redirect_url%3Dhttp%25253A%25252F%25252Fmofang.taobao.com%25252Fs%25252Flogin%26css_style%3D%26allp%3D&_ksTS=1404787873165_78&callback=jsonp79");
		headerWrapper(hg2);
		HttpResponse httpresponse2 = httpClient.execute(hg2);  
		HttpEntity entity2 = httpresponse2.getEntity();  
		String body2 = EntityUtils.toString(entity2);  
		//System.out.println(body2);  
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60*60*24);//以秒为单位
		session.setAttribute("httpClient", httpClient);
		//HttpGet hg4 = new HttpGet("http://trade.taobao.com/trade/itemlist/list_bought_items.htm?spm=1.7274553.1997525045.2.C6QtVd");
		HttpGet hg4 = new HttpGet(url);
		headerWrapper(hg4);
		HttpResponse httpresponse4 =httpClient.execute(hg4);
		//HttpResponse httpresponse4 = commonClient.execute(hg4);  
		HttpEntity entity4 = httpresponse4.getEntity();  
		String page = EntityUtils.toString(entity4);  
		//System.out.println(page);  
		GoodsBean goods = pasePage(request, response, page, url);
		long ed = new Date().getTime();
//    	System.out.println("dddd======2:"+data.toString());
    	if(goods.getpName()==null){
    		goods.setpName(goods.getTitle());
    	}
    	goods.setpUrl(url);
//    	System.out.println("dddd======1:"+data.toString());
    	//运费换算
    	String fee = goods.getpFreight();
    	Double feen=0.0;
    	String feere = null;
    	if(fee!=null&&!fee.isEmpty()){
    		String st_fee;
    		int end;
    		if(Pattern.compile("taobao").matcher(fee).find()){
    			fee = fee.replace("taobao", "");
    			feen = Double.parseDouble(fee)/7;
    			if(feen!=0.0){
    				st_fee = String.valueOf(feen);
    				end = st_fee.indexOf(".")+3;
    				feere = st_fee.substring(0,end);
    			}else{
    				feere = "0";
    			}
    			goods.setpFreight(feere);
    		}
    	}
    	//汇率换算
		String priceUnit = goods.getpPriceUnit();
		String pOprice = goods.getpOprice();
		String pSprice = goods.getpSprice();
		//HttpSession session = request.getSession(); 
		if(priceUnit != null){
			if(priceUnit.equals("RMB")){
				//获取汇率
				Map<String, Double> maphl = Currency.getMaphl(request);
				//换算
				DecimalFormat df=new DecimalFormat(".##");
				List<String> pWprice = goods.getpWprice();
				//40 -99 RMB 80.00 , 100 -299 RMB 78.00 , ≥ 300 RMB 75.00 
				//[40 -99  $12.9, 100 -299  $12.58, ≥ 300  $12.1]
				//[4 - 9 $77.49, 10 - 49 $73.3, 50 + $71.73]
				int j = 0;
				if(pWprice != null){
				if(pWprice.size()>0){
					if(pWprice.get(0).indexOf("RMB") > -1){
						j = 1;
						ArrayList<String> pwList = new ArrayList<String>();
						for (int i = 0; i < pWprice.size(); i++) {
							String pw = pWprice.get(i);
								String pwn=pw.split("RMB")[0];
								double pri=Double.parseDouble(pw.split("RMB")[1]) * maphl.get("RMB"); 
								if(pwn.indexOf("≥") > -1){
									pwn = pwn.replace("≥", "")+" +";
								}
								pwList.add(pwn+" $"+df.format(pri));
								if(i==0){
									pOprice = df.format(pri);
									goods.setMinOrder(pw.split("-", 0)[0]);
								}
						}
						goods.setpWprice(pwList);
					}
				}
				}
				if(j == 0){
					if(maphl.get("RMB")!=null && pOprice!=null){
						if(pOprice.indexOf("-")>-1){
							double hl = Double.parseDouble(pOprice.split("-")[0].trim()) * maphl.get("RMB"); 
							double h2 = Double.parseDouble(pOprice.split("-")[1].trim()) * maphl.get("RMB"); 
							pOprice = df.format(hl)+"-"+df.format(h2);
						}else{
							double hl = Double.parseDouble(pOprice) * maphl.get("RMB"); 
							pOprice = df.format(hl);
						}
					}
				}
				 
				if(pSprice != null){
					if(pSprice.indexOf("-")>-1){
						double hl = Double.parseDouble(pSprice.split("-")[0].trim()) * maphl.get("RMB"); 
						double h2 = Double.parseDouble(pSprice.split("-")[1].trim()) * maphl.get("RMB");  
						pSprice = df.format(hl)+"-"+df.format(h2);
					}else{
						double h1 = Double.parseDouble(pSprice) * maphl.get("RMB"); 
						pSprice = df.format(h1);
					}
				}

			}
		}
		goods.setpOprice(pOprice);
		goods.setpSprice(pSprice);
		String shopId= goods.getpID();
    	if(shopId == null){
    		goods.setsID("cbt");
    	}else if(shopId.equals("")){
    		goods.setsID("cbt");
    	}
    	 //----lizhanjun如果访问的商品详情页可正常访问 就设置最近浏览和现实留言及回复信息----strt
   	 if(goods != null && !Pattern.compile("(淘宝网 - 淘！我喜欢)").matcher(goods.getTitle()).find()
   			 && !Pattern.compile("(对不起，您访问的页面不存在！ -尚天猫，就购了)").matcher(goods.getTitle()).find()) {//将浏览的记录保存到cookie中
//   		 SpiderServlet.saveCookie(request, response, goods);
   		 UserBean user = (UserBean) session.getAttribute("userInfo");
   		 if(user != null) {
//   			 List<GuestBookBean> gbbs = SpiderServlet.getGBBS(goods.getpID(),user.getId());
//   			LOG.warn("size:"+gbbs.size());
//      		 request.setAttribute("gbbs", gbbs);
   		 }
   		 
   	 }
   	 //----lizhanjun如果访问的商品详情页可正常访问 就设置最近浏览和现实留言及回复信息----end
   	 
   	 /**  lizhanjun  test  翻译--start*/
	     	/*if(Pattern.compile("taobao").matcher(goods.getpUrl()).find() 
	     		|| Pattern.compile("tmall").matcher(goods.getpUrl()).find()){//如果是淘宝的商品详情就进行goolge翻译
	     		try{
	     			GoodsBean gb = TransHelp.TransBean(goods);
	     			request.setAttribute("spider", gb);
	     		}catch(Exception e) {
	     			request.setAttribute("spider",goods );
	     		}
	     	}*/
    	/**  lizhanjun  test  翻译--end*/
		request.setAttribute("spider", goods);
		long t2 = new Date().getTime();
		LOG.warn("==============第一次登陆获取页面结束所花时间"+(t2-t1)+"==================");
   	 	//System.out.println("data:"+goods);
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/spider.jsp");
			homeDispatcher.forward(request, response);
	}
	
	public static String login(String url,HttpServletRequest request)
			throws ServletException, IOException {
		LOG.warn("================不用验证码登陆获取页面的开始=====================");
		long t1 = new Date().getTime();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    httpClient = GetValidateCode.createSSLClientDefault(true);
		HttpPost httpPost = new HttpPost("https://login.taobao.com/member/login.jhtml");
		headerWrapper(httpPost);
		httpPost.setHeader("accept-Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		params.add(new BasicNameValuePair("TPL_password", TPL_password));  
		params.add(new BasicNameValuePair("TPL_username", TPL_username));
		params.add(new BasicNameValuePair("newlogin", "1"));   
		params.add(new BasicNameValuePair("callback", "1"));  
		httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));  
		// 发送请求  
		HttpResponse httpresponse = httpClient.execute(httpPost);  
		HttpEntity entity = httpresponse.getEntity();  
		String body = EntityUtils.toString(entity);  
		//System.out.println(body);  
		String sessionid = body.substring(body.indexOf("token")+8, body.length()-3);
		LOG.warn("sss:"+sessionid);
		
		
	/*	HttpGet hg1 = new HttpGet("https://passport.alipay.com/mini_apply_st.js?site=0&token="+sessionid+"&callback=vstCallback65");
		headerWrapper(hg1);
		HttpResponse httpresponse1 = httpClient.execute(hg1);  
		HttpEntity entity1 = httpresponse1.getEntity();  
		String body1 = EntityUtils.toString(entity1);
		
		String st = "";
		String regex = "vstCallback65\\((.*)\\)";
		Pattern compile = Pattern.compile(regex);
		Matcher m = compile.matcher(body1);
		while(m.find()){
			String group = m.group(1);
			JSONObject string2Json = String2Json(group);
			try {
				JSONObject object = (JSONObject) string2Json.get("data");
				st = (String) object.get("st");
				System.out.println(st); 
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(body1);  
		
		HttpGet hg2 = new HttpGet("https://login.taobao.com/member/vst.htm?st="+st+"&params=style%3Dminisimple%26sub%3Dtrue%26TPL_username%3D"+TPL_username+"%26loginsite%3D0%26from_encoding%3D%26not_duplite_str%3D%26guf%3D%26full_redirect%3D%26isIgnore%3D%26need_sign%3D%26sign%3D%26from%3Ddatacube%26TPL_redirect_url%3Dhttp%25253A%25252F%25252Fmofang.taobao.com%25252Fs%25252Flogin%26css_style%3D%26allp%3D&_ksTS=1404787873165_78&callback=jsonp79");
		headerWrapper(hg2);
		HttpResponse httpresponse2 = httpClient.execute(hg2);  
		HttpEntity entity2 = httpresponse2.getEntity();  
		String body2 = EntityUtils.toString(entity2);  
		System.out.println(body2);  */
		//HttpGet hg4 = new HttpGet("http://trade.taobao.com/trade/itemlist/list_bought_items.htm?spm=1.7274553.1997525045.2.C6QtVd");
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60*60*24);//以秒为单位
		session.setAttribute("httpClient", httpClient);
		HttpGet hg4 = new HttpGet(url);
		headerWrapper(hg4);
		HttpResponse httpresponse4 =httpClient.execute(hg4);
		//HttpResponse httpresponse4 = commonClient.execute(hg4);  
		HttpEntity entity4 = httpresponse4.getEntity();  
		String page = EntityUtils.toString(entity4);  
		//System.out.println(page);  
		//GoodsBean goods = pasePage(request, response, page, url);
		//request.setAttribute("spider", goods);
		long t2 = new Date().getTime();
		LOG.warn("==============不用验证码登陆获取页面结束所花时间"+(t2-t1)+"==================");
		return page;
   	 	//System.out.println("data:"+goods);
			//javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/spider.jsp");
			//homeDispatcher.forward(request, response);
	}
	
	public static GoodsBean pasePage (HttpServletRequest request, HttpServletResponse response, String page, String url) throws ServletException, IOException {
		//JSoup解析HTML生成document
        Document doc = Jsoup.parse(page);
        //获取element
        Element gbody = doc.body();
        String url_title = doc.title();
        GoodsBean goods = new GoodsBean();
        //根据url 来获取数据类型
        int type = TypeUtils.getType(url);
        //long se = new Date().getTime();
        // System.out.println("4.解析HTML生成document:"+(se-ss));
        try {
			goods = ParseGoodsUrl.spiderFromDoc(goods,type, gbody,doc.head());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //解析商品链接数据，获取商品信息
        goods.setTitle(url_title);
        LOG.warn(url_title);     
        //ISpiderServer iss = new SpiderServer();
		goods = ParseGoodsUrl.modefiGoodsData(goods);
    	/**  lizhanjun  test  translate*/
    	//if(Pattern.compile("taobao").matcher(goods.getpUrl()).find() 
    		//|| Pattern.compile("tmall").matcher(goods.getpUrl()).find()){//如果是淘宝的商品详情就进行goolge翻译
    		//goods = TransHelp.TransBean(goods);
    //	}
    	return goods;
    	 
	}
	
	public static GoodsBean sessionLogin(HttpServletRequest request, HttpServletResponse response, CloseableHttpClient httpClient, String url) {
		LOG.warn("==============通过session获取页面开始==================");
		System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(100000));// （单位：毫秒） 
		long t1 = new Date().getTime();
		HttpGet targetUrl = new HttpGet(url);
		headerWrapper(targetUrl);
		HttpResponse hresponse;
		GoodsBean goods = null;
		try {
			hresponse = httpClient.execute(targetUrl);
			HttpEntity entity4 = hresponse.getEntity();  
			String page = EntityUtils.toString(entity4);  
			//System.out.println(page);  
			goods = pasePage(request, response, page, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long t2 = new Date().getTime();
		LOG.warn("==============通过session获取页面结束所花时间"+(t2-t1)+"==================");
		return goods;
	}
}