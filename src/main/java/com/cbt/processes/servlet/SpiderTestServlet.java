package com.cbt.processes.servlet;

import com.cbt.bean.ClassDiscount;
import com.cbt.bean.GuestBookBean;
import com.cbt.bean.RecentViewBean;
import com.cbt.customer.logintb.GetValidateCode;
import com.cbt.customer.logintb.TaobaoHttpLogin;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.customer.service.IRecentViewService;
import com.cbt.customer.service.RencentViewServiceImpl;
import com.cbt.customer.servlet.RecentViewServlet;
import com.cbt.parse.service.*;
import com.cbt.util.Application;
import com.cbt.util.DownloadPage;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 抓取网页数据
 */
public class SpiderTestServlet extends HttpServlet {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SpiderTestServlet.class);
	
	private static final long serialVersionUID = 1L;
	
    public SpiderTestServlet() {
        super();
    }
    
    public void getSpider(HttpServletRequest request, HttpServletResponse response) throws Throwable {
    	long lzjst = new Date().getTime();
    	long st1 = new Date().getTime();
    	String requrl = request.getParameter("url");
    	//String freeShipping = request.getParameter("FreeShipping");
    	String u0 = request.getParameter("u0");
    	String u1 = request.getParameter("u1");
    	String u2 = request.getParameter("u2");
    	String u3 = request.getParameter("u3");
    	String sp = request.getParameter("p");
    	String site = request.getParameter("site");
    	String html_session = request.getParameter("session");
		String spurl = u1+u3+u0+u2;
		spurl = TypeUtils.decodeUrl(spurl);
		spurl = "http://"+spurl;
		if(site!=null&&site.isEmpty()){
			site = null;
		}
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(html_session!=null&&"false".equals(html_session)){
			currency1 = "USD";
		}
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		
    	if(requrl==null||requrl.isEmpty()){
    		requrl = spurl;
    	}
		if (requrl.indexOf("http://") < 0 && requrl.indexOf("https://") < 0){
			requrl = "http://" + requrl;
		}
		
		requrl = TypeUtils.modefindUrl(requrl,1);
		
		HttpSession session = request.getSession();
//		ISpiderServer iss = new SpiderServer();
		String[] user =  WebCookie.getUser(request);
		//---lizhanjun start-----
		int userId = 0;
		//---lizhanjun end-----
		String userName = "游客";
    	if(user != null){
    		//---lizhanjun start-----
    		userId = Integer.parseInt(user[0]);
    		//---lizhanjun end-----
        	userName = user[1];
        	request.setAttribute("username", userName);
//        	if(user.length == 3){
        		request.setAttribute("email", user[2]);
//        	}
    	}
    	
    	/**lizhanjun - start*/
    	boolean b = true; //用于判断finally里面中的语句的执行（页面跳转）
    	/**lizhanjun - end*/
     try{
    	 long st = new Date().getTime();
    	 LOG.warn("(0).ParseGoodsUrl："+(st-st1));
    	 LOG.warn("requrl:"+requrl);
    	 GoodsBean data = new GoodsBean();
//		if(Pattern.compile("(taobao)|(tmall)").matcher(requrl).find()){
//			data = ParseGoodsUrl.parseGoods(requrl,userName,0);
//		}else{
//		}
//		System.out.println(new Date().getTime());
    		 HttpServletRequest hrequest = (HttpServletRequest) request;
    		 String com_ip = Utility.getIpAddress(hrequest);
    		 
    		 data = ParseGoodsUrl.parseGoods2(requrl,com_ip,site);
//		LOG.warn("data:"+data);
    		 //妞妞车价格
    		 double exchange_rate = 1;
    		 /**lizhanjun 当不能访问淘宝商品详情页时  增加后台登陆淘宝逻辑的开始---*/
    		 //若要测试后台登陆 请把第一个if注释掉  解开第二个if即可测试
    		 if(data != null){
    			 String freeShipping = data.getFree();
    			 //当传过来的参数是免邮的，获取运输方式是否存在，不存在则变为不免邮
    			 /*if(Utility.getStringIsNull(freeShipping)){
				LOG.warn("expresee_method:"+data.getMethod());
				String express = AppConfig.express.get(data.getMethod());
				if(express == null){
					freeShipping = null;
					data.setMethod(null);
					data.setTime(null);
					data.setFree("0");
				}
			}*/
    			 String psprice = data.getpSprice();
    			 if(Utility.getStringIsNull(sp)||(SearchUtils.key(data.getpName()) && Utility.getStringIsNull(psprice))){
    				 if(psprice.indexOf("-") == -1){
    					 double psprice1 = Double.parseDouble(psprice);
    					 if(psprice1>60 && psprice1<170){
    						 data.setpSprice("170.00");
    					 }
    				 }
    			 }
    			 
    			 if(data.getTitle() != null){
    				 if(Pattern.compile("(淘宝网 - 淘！我喜欢)").matcher(data.getTitle()).find()
    						 ||Pattern.compile("(对不起，您访问的页面不存在！ -尚天猫，就购了)").matcher(data.getTitle()).find()){//
//		if(!Pattern.compile("(淘宝网 - 淘！我喜欢)").matcher(data.getTitle()).find()){//这个时候说明登陆淘宝失败 需要实现后台登陆淘宝  这时候需要跳转到显示登录淘宝验证码图片上
    					 //获取淘宝生成验证码图片的链接
    					 String codeUrl = GetValidateCode.getCodeUrl();
    					 if(codeUrl == null) { //说明这个时候是不需要验证码登陆的
    						 if(request.getSession().getAttribute("httpClient")!=null) {//说明之前已经登陆过了
    							 CloseableHttpClient httpClient = (CloseableHttpClient) request.getSession().getAttribute("httpClient");
    							 data = TaobaoHttpLogin.sessionLogin(request, response, httpClient, requrl);
    						 }else {
    							 String page = TaobaoHttpLogin.login(requrl,request);
    							 //JSoup解析HTML生成document
    							 Document doc = Jsoup.parse(page);
    							 //获取element
    							 Element body = doc.body();
    							 String url_title = doc.title();
    							 
    							 //根据url 来获取数据类型
    							 int type = TypeUtils.getType(requrl);
    							 data = ParseGoodsUrl.spiderFromDoc(data,type, body,doc.head());
    							 //解析商品链接数据，获取商品信息
    							 data.setTitle(url_title);
    							 LOG.warn(url_title);     
    							 /*if(type == 0){
						iss.addURL(userName, requrl,0);
					}else{
						iss.addURL(userName, requrl,1);
					}*/
    							 data = ParseGoodsUrl.modefiGoodsData(data);
    						 }
    					 }else {//需要输入验证码登陆
    						 /**lizhanjun*/
    						 if(request.getSession().getAttribute("httpClient")!=null) {//说明之前已经登陆过了
    							 CloseableHttpClient httpClient = (CloseableHttpClient) request.getSession().getAttribute("httpClient");
    							 data = TaobaoHttpLogin.sessionLogin(request, response, httpClient, requrl);
    						 }else {//第一次登陆
    							 b = false;
    							 //request.getRequestDispatcher("/getCode?codeUrl="+codeUrl+"&askurl="+requrl+"").forward(request, response);
    							 request.getRequestDispatcher("/getCode?askurl="+URLEncoder.encode(requrl,"UTF-8")+"").forward(request, response);
    							 return;
    						 }
    						 //}
    						 /**lizhanjun*/
    					 }
    				 }
    			 }
    			 /**lizhanjun 当不能访问淘宝商品详情页时  增加后台登陆淘宝逻辑的结束---*/
    			 //没有设置代理情况下
//		GoodsBean data = com.gr.test.ParseGoodsUrl.parseGoods(requrl, userName, 0);
    			 long ed = new Date().getTime();
    			 LOG.warn("10.ParseGoodsUrl："+(ed-st));
    			 if(data.getpName()==null){
    				 data.setpName(data.getTitle());
    			 }
    			 data.setpUrl(requrl);
    			 //运费换算
    			 String fee = data.getpFreight();
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
    					 data.setpFreight(feere);
    				 }else if(Pattern.compile("alibaba").matcher(data.getpUrl()).find()){
    					 feen = Double.parseDouble(data.getpFreight());
    					 if(feen!=0.0){
    						 if(feen < 300){
    							 data.setpFreightChange(true);
    							 /*Double cfee = feen/7;
    					st_fee = String.valueOf(cfee);
    					if(st_fee.length()<4){
    						end = st_fee.length();
    					}else{
    						end = st_fee.indexOf(".")+3;
    					}
    					feere = st_fee.substring(0,end);*/
    						 }else{
    							 feere = "45.0";
    						 }
    					 }else{
    						 feere = "0";
    					 }
    					 data.setpFreight(feere);
    				 }
    			 }else{
    				 feere = "10";
    				 data.setpFreight(feere);
    			 }
    			 //汇率换算
    			 String priceUnit = data.getpPriceUnit();
    			 String pOprice = data.getpOprice();
    			 String pSprice = data.getpSprice();
    			 String fPrice = data.getFprice();
    			 String pSprice_ = data.getpSprice();
    			 ArrayList<String> pWprice = data.getpWprice();//汇率换算后的批发价
    			 List<String> pWprice1 = new ArrayList<String>();//美元批发价
    			 String applicable_credit = "50";
    			 //商品折扣
//    			 double discount = PropertiesFile.getDiscount();
    			 if(priceUnit != null){
//			if(priceUnit.equals("RMB") || !currency1.equals("USD")){
    				 //获取汇率
    				 Map<String, Double> maphl = Currency.getMaphl(request);
    				 exchange_rate = maphl.get(currency1);
    				 if(priceUnit.equals("RMB")){
    					 exchange_rate = exchange_rate/maphl.get("RMB");
    				 }
    				 //换算
    				 DecimalFormat df=new DecimalFormat("#0.##");
    				 applicable_credit = df.format(exchange_rate*50);
    				 double exchange_rate1 = exchange_rate;
    				 //40 -99 RMB 80.00 , 100 -299 RMB 78.00 , ≥ 300 RMB 75.00 
    				 //[40 -99  $12.9, 100 -299  $12.58, ≥ 300  $12.1]
    				 //[4 - 9 $77.49, 10 - 49 $73.3, 50 + $71.73]
    				 int j = 0;
    				 if(pWprice != null){
    					 if(pWprice.size()>0){
    						 if(pWprice.get(0).indexOf("RMB") > -1){
    							 j = 1;
    							 double usd_rmb = maphl.get("USD")/maphl.get("RMB");
    							 for (int i = 0; i < pWprice.size(); i++) {
    								 String pw = pWprice.get(i);
    								 String pwn=pw.split("RMB")[0];
    								 double pri=Double.parseDouble(pw.split("RMB")[1]);
    								 if(pwn.indexOf("≥") > -1){
    									 pwn = pwn.replace("≥", "")+" +";
    								 }
    								 pWprice.set(i,pwn+" $"+df.format(pri * exchange_rate));
    								 pWprice1.add(i,pwn+" $"+df.format(pri * usd_rmb));
    								 if(i==0){
    									 pOprice = df.format(pri);
    									 data.setpOprice(pw.split("-", 0)[0]);
    								 }
    							 }
    							 data.setpWprice(pWprice);
    						 }else{
    							 j = 1;
    							 for (int i = 0; i < pWprice.size(); i++) {
    								 String pw = pWprice.get(i);
    								 String pwn=pw.split("\\$")[0];
    								 double pri=Double.parseDouble(pw.split("\\$")[1]);
    								 if(pwn.indexOf("≥") > -1){
    									 pwn = pwn.replace("≥", "")+" +";
    								 }
    								 pWprice.set(i,pwn+" $"+df.format(pri * exchange_rate));
    								 pWprice1.add(i,pwn+" $"+df.format(pri));
    								 if(i==0){
    									 pOprice = df.format(pri);
    									 data.setpOprice(pri+"");
    								 }
    							 }
    							 data.setpWprice(pWprice);
    						 }
    					 }
    				 }
    				 if(j == 0){
    					 if(pOprice!=null){
    						 if(pOprice.indexOf("-")>-1){
    							 double hl = Double.parseDouble(pOprice.split("-")[0].trim()) * exchange_rate1;
    							 double h2 = Double.parseDouble(pOprice.split("-")[1].trim()) * exchange_rate1;
    							 pOprice = df.format(hl)+"-"+df.format(h2);
    						 }else{
    							 double hl = Double.parseDouble(pOprice) * exchange_rate1;
    							 pOprice = df.format(hl);
    						 }
    					 }
    				 }
    				 
    				 if(Utility.getStringIsNull(pSprice)){
    					 if(pSprice.indexOf("-")>-1){
    						 double hl = Double.parseDouble(pSprice.split("-")[0].trim()) * exchange_rate;
    						 double h2 = Double.parseDouble(pSprice.split("-")[1].trim()) * exchange_rate;
    						 pSprice = df.format(hl)+"-"+df.format(h2);
    					 }else{
    						 double h1 = Double.parseDouble(pSprice) * exchange_rate;
    						 pSprice = df.format(h1);
    					 }
    				 }
    				 if(fPrice != null){
    					 if(fPrice.indexOf("-")>-1){
    						 double hl = Double.parseDouble(fPrice.split("-")[0].trim()) * exchange_rate;
    						 double h2 = Double.parseDouble(fPrice.split("-")[1].trim()) * exchange_rate;
    						 fPrice = df.format(hl)+"-"+df.format(h2);
    					 }else{
    						 double h1 = Double.parseDouble(fPrice) * exchange_rate;
    						 fPrice = df.format(h1);
    					 }
    				 }
//			}
    			 }
    			 request.setAttribute("fPrice", pSprice_);
    			 data.setFprice(fPrice);
    			 data.setpOprice(pOprice);
    			 data.setMinOrder(Utility.getStringIsNull(data.getMinOrder())?data.getMinOrder():"1");
    			 if(!Utility.getStringIsNull(pSprice)){
    				 if(Utility.getStringIsNull(pOprice)){
    					 pSprice = pOprice;
    				 }else if(pWprice != null){
    					 pSprice = "0";
    				 }
    			 }
    			 if(Utility.getStringIsNull(pSprice)){
    				 if(pWprice != null){
    					 if(pWprice.size()>0){
    						 String wprice = pWprice.get(0);
    						 if(wprice.indexOf("$") != -1){
    							 pSprice = wprice.substring(wprice.indexOf("$")+1);
    						 }
    					 }
    				 }
    			 }
    			 /*
    			  * 2015/07/31-去除
    			  * if(pSprice != null){
	    	if(freeShipping != null &&pSprice.indexOf("-") == -1 && !pSprice.equals("")){
	    		DecimalFormat df1 = new DecimalFormat("0.00");
	    		double pSprices = Double.parseDouble(pSprice);
	    		if(Pattern.compile("aliexpress").matcher(data.getpUrl()).find()){
	        		pSprice = (df1.format(pSprices - pSprices * 0.05))+"";
	    		}else{
	        		pSprice = (df1.format(pSprices * 0.05+pSprices))+"";
	    		}
	    	}
    	}*/
    			 data.setpSprice(pSprice);
    			 String shopId= data.getpID();
    			 if(shopId == null){
    				 data.setsID("cbt");
    			 }else if(shopId.equals("")){
    				 data.setsID("cbt");
    			 }
    			 request.setAttribute("country_fz", 0);
    			 if(Utility.getStringIsNull(data.getFree())){
    				 if(data.getFree().equals("1")||data.getFree().equals("2")){
    					 Cookie cookie = WebCookie.getCookieByName(request, "expressType");
    					 String idsString = "";
    					 String country = "USA";
    					 String countryid = "";
    					 if(cookie != null){
    						 idsString = cookie.getValue();
    						 String[] expresssString = idsString.split("@");
    						 country = expresssString[3];
    						 countryid = expresssString[0];
    					 }
    					 if(countryid.equals("29") || countryid.equals("37")){
    						 request.setAttribute("country_fz", 1);
    					 }else{
    						 request.setAttribute("country", country);
    					 }
    				 }
    			 }
    			 request.setAttribute("applicable_credit", applicable_credit);
    			 request.setAttribute("currency", currency1);
    			 request.setAttribute("spider_pWprice", pWprice1.size() == 0 && !Utility.getStringIsNull(data.getpWprice()) ? data.getpWprice() : pWprice1);
    		 }
    		 request.setAttribute("spider", data);
    		 //获取混批折扣率
    		 List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
    		 list_cd = Application.getClassDiscount(hrequest);
    		 request.setAttribute("ClassDiscount", list_cd);
    		 double pSprice_ = Utility.getStringIsNull(data.getpSprice())?Double.parseDouble(data.getpSprice().split("-")[0]):150+1;
    		 if(Utility.getStringIsNull(data.getCategory()) && pSprice_ <= 150){
    			 String type = data.getCategory().toLowerCase().trim();
    			 DecimalFormat df=new DecimalFormat("#0");
    			 ok:
    				 for (int k = 0; k < list_cd.size(); k++) {
    					 String[] classname = list_cd.get(k).getClassname().split(",");
    					 for (int i = 0; i < classname.length; i++) {
    						 if(type.indexOf(classname[i].toLowerCase()) > -1){
    							 if(type.indexOf("women") > -1){
    								 if(classname[i].toLowerCase().indexOf("women") > -1){
        								 request.setAttribute("ClassDiscount_cn", list_cd.get(k).getId());
            							 request.setAttribute("ClassDiscount_sn", list_cd.get(k).getShowname());
            							 request.setAttribute("ClassDiscount_p", df.format(list_cd.get(k).getPrice()));
            							 break ok;
    								 }
    							 }else{
    								 request.setAttribute("ClassDiscount_cn", list_cd.get(k).getId());
        							 request.setAttribute("ClassDiscount_sn", list_cd.get(k).getShowname());
        							 request.setAttribute("ClassDiscount_p", df.format(list_cd.get(k).getPrice()));
        							 break ok;
    							 }
    						 }
    					 }
    					 
    				 }
    		 }
    		 
    		 if(data.getType() != null && data.getType().size() > 0){
    			 request.setAttribute("stype", JSONArray.fromObject(data.getType()));
    		 }
    		 request.setAttribute("exchange_rate", exchange_rate);
    		 
    		 //----lizhanjun如果访问的商品详情页可正常访问 就设置最近浏览和现实留言及回复信息----end
    		 long lzjen = new Date().getTime();
    		 LOG.warn("spider.jsp GoodsBean time:"+(lzjen - lzjst));
    		 /**  lizhanjun  test  翻译--start*/
    		 /* long lzjt1 = new Date().getTime(); 
	     	if(Pattern.compile("taobao").matcher(data.getpUrl()).find() 
	     		|| Pattern.compile("tmall").matcher(data.getpUrl()).find()){//如果是淘宝的商品详情就进行goolge翻译
	     		try{
	     			GoodsBean gb = TransHelp.TransBean(data);
	     			request.setAttribute("spider", gb);
	     		}catch(Exception e) {
	     			request.setAttribute("spider",data );
	     		}
	     	}
	     	long lzjt2 = new Date().getTime();
	     	System.out.println("调用翻译class总共花费的时间:"+(lzjt2-lzjt1));*/
    		 /**  lizhanjun  test  翻译--end*/
    		 data = null;
     } catch (Exception e) {
    	 LOG.debug(e+"-url"+requrl);
			e.printStackTrace();
			/*iss.addURL(userName, requrl,-1);*/
	}finally{
			if(b) {
				javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/spidertest.jsp");
				homeDispatcher.forward(request, response);
			}
		}
//    	out.print(spider.toString());
	}
    public static void main(String[] args) {
    	String string = "a$b";
		System.out.println(string.split("\\$")[0]);
	}
    /**
     * 方法描述:获取浏览记录和留言
     * author:lizhanjun
     * date:2015年4月21日
     * @param request
     * @param response
     * @param data
     * @throws IOException 
     * @throws ServletException
     */
    public void getGBBS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String pid = request.getParameter("pid");
    	String pname = request.getParameter("pname");
    	String purl = request.getParameter("purl");
    	String pimage = request.getParameter("pimage");
    	String psprice = request.getParameter("psprice");
    	String minorder = request.getParameter("minorder");
    	String keywords = request.getParameter("keywords");
    	String seachwords = request.getParameter("seachwords");
    	RecentViewBean rvb = new RecentViewBean();
		rvb.setPid(pid);
		rvb.setPname(pname);
		rvb.setPurl(purl);
		rvb.setImgUrl(pimage);
		rvb.setPrice(psprice);
		rvb.setMinOrder(minorder);
		rvb.setKeywords(keywords);
		rvb.setSeachwords(seachwords);
		String ip = Utility.getIpAddress(request);
		rvb.setIp(ip);
    	String[] user =  WebCookie.getUser(request);
    	//留言
		//---lizhanjun start-----
		int userId = 0;
		//---lizhanjun end-----
		List<GuestBookBean> gbbs = new ArrayList<GuestBookBean>();
    	if(user != null){
    		userId = Integer.parseInt(user[0]);
    		//根据商品id查询出客户曾经对该商品的留言以及回答
    		IGuestBookService ibs = new GuestBookServiceImpl();
    		gbbs = ibs.findByPid(pid,userId,0,3);
    	}else{
    		rvb.setSessionid(WebCookie.cookie(request, "sessionId"));
    	}
    	rvb.setUid(userId);
    	//浏览记录
   	 	RecentViewServlet.saveCookie(request, response, rvb);
   	 	List<RecentViewBean> rViewBeans = RecentViewServlet.getRencent(request, response);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("gbbs", gbbs);
    	map.put("rview", rViewBeans);
    	PrintWriter out = response.getWriter();
    	out.print(JSONArray.fromObject(map));
    	out.flush();
    	out.close();
    }
    /**
     * 方法描述:将浏览记录保存到cookie中
     * author:lizhanjun
     * date:2015年4月21日
     * @param request
     * @param response
     * @param data
     */
    public static void saveCookie(HttpServletRequest request, HttpServletResponse response, RecentViewBean rvb) {
    		//将刚浏览过的商品保存到数据库中
    		IRecentViewService service = new RencentViewServiceImpl();
    		service.addRecode(rvb);
    		//保存到cookie中的名字 它是名值对的 值是商品的id
    		String coName = "rencentView";
    		//从服务器端获取之前的最近浏览记录
    			Cookie[] cookies = request.getCookies();
    			String value = "";  //cookie 中保存的value是商品的id 
    			if(cookies != null && cookies.length > 0) {
	    			//遍历这个cookies
	    			for (Cookie c: cookies) {
	    				//判断cookie中有木有这个商品 通过名字获取对应的值
	    				if(c.getName().equals(coName)) { //根据名字来判断是否为浏览同一个商品 即判断重复
	    					value = c.getValue(); //有这个就通过名字获取value
	    					break;
	    				}
	    			}
	    			//System.out.println("value = " + value);
	    			//将新浏览的商品id保存到cookie中 并处理重复
	    			String[] ids = value.split(",");
	    			StringBuffer sb = new StringBuffer();//用来拼接新的cookie中的商品id字符串
	    			sb.append(rvb.getPid());  //将当前浏览的商品的id添加到第一个
	    			int productNum = 1; //Cookie中的商品数量
	    			//遍历cooki中保存的id值数组  保存的是五个商品浏览信息
	    			for (int i = 0; i < ids.length && productNum < 6; i++) {
	    				if( !ids[i].equals(rvb.getPid())) {//判断cookie中没有此商品id 重复处理
	    				   if(i==0) {
				            sb.append(ids[i]);
				           }
				           else{
				            sb.append(",").append(ids[i]);
				           }
	    					productNum++;
	    				}
	    			}
	    			//将cookie保存到浏览器中 名字是定义的常量  值是商品的id
	    			Cookie cookie = new Cookie(coName,sb.toString());
	    			cookie.setMaxAge(60 * 60 *24); //设置cookie的过期时间
	    			//向客户端添加该cookie；
	    			response.addCookie(cookie);
    			}
    }
 
     
    public void details(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String url = request.getParameter("url"); 
    	String desc = DownloadPage.getContentFormUrl(url);
    	PrintWriter out = response.getWriter();
    	if(desc.length()>15){
    		desc = desc.substring(10, desc.length()-2);
    	}else{
    		desc = "";
    	}
    	out.print(desc);
    	out.flush();
    	out.close();
    }
    
    /**
     * 方法描述:获取商品用户反馈
     * author: sj
     * date:2015年8月6日
     * @return
     */
    public void getFeedBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String url = request.getParameter("url");
    	String sBuffer = ParseGoodsUrl.getNote(url);
    	PrintWriter out = response.getWriter();
    	if(sBuffer != null){
    		out.print(sBuffer);
    	}else{
    		out.print("");
    	}
    	out.flush();
    	out.close();
    }
    /**
     * 方法描述:获取商品详情
     * author: ylm
     * date:2015年8月6日
     * @return
     */
    public void getGoodInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	LOG.info("getGoodInfo start");
    	request.setCharacterEncoding("utf-8");
    	response.setCharacterEncoding("utf-8");
    	response.setContentType("text/html");
    	String url = request.getParameter("url"); 
    	String ip = request.getParameter("ip"); 
    	String sBuffer = DownloadMain.getContentClient(url, null);
    	PrintWriter out = response.getWriter();
    	LOG.warn("get GoodInfo test  ---------");
    	if(sBuffer != null){
    		out.print(sBuffer);
    		LOG.warn("get GoodInfo test  :"+sBuffer.length());
    	}else{
    		out.print("");
    	}
    	out.flush();
    	out.close();
    }
    
    /**
     * 方法描述:获取商品免邮的运输方式
     * author: ylm
     * date:2015年8月6日
     * @return
     */
    public void getGoodFree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	LOG.info("getGoodFree start");
    	request.setCharacterEncoding("utf-8");
    	response.setCharacterEncoding("utf-8");
    	response.setContentType("text/html");
    	String url = request.getParameter("url");
    	String pid = request.getParameter("pid");
    	String perWeight = request.getParameter("perWeight");
    	String width = request.getParameter("width");
    	String weight = request.getParameter("weight");
    	String sprice = request.getParameter("sprice");
    	String sBuffer =  ParseGoodsUrl.getFee(request, url, pid, sprice, perWeight, width, weight);
    	PrintWriter out = response.getWriter();
    	out.print(sBuffer);
    	out.flush();
    	out.close();
    	LOG.info("getGoodFree end:"+sBuffer);
    }
}
