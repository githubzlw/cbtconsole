package com.cbt.website.servlet;

import com.cbt.bean.GuestBookBean;
import com.cbt.bean.RecentViewBean;
import com.cbt.bean.UserBean;
import com.cbt.customer.logintb.GetValidateCode;
import com.cbt.customer.logintb.TaobaoHttpLogin;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.customer.service.IRecentViewService;
import com.cbt.customer.service.RencentViewServiceImpl;
import com.cbt.customer.servlet.RecentViewServlet;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.servlet.Currency;
import com.cbt.util.DownloadPage;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 抓取网页数据
 */
public class SpiderServlet extends HttpServlet {
	private static final Log LOG = LogFactory.getLog(SpiderServlet.class);
	
	private static final long serialVersionUID = 1L;
	
    public SpiderServlet() {
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
    	String site = request.getParameter("site");
    	String username=request.getParameter("username");
    	int userid= Integer.parseInt(request.getParameter("userid"));
//    	System.out.println(u3);
		String spurl = u1+u3+u0+u2;
//		System.out.println("u:"+u0+u1+u2+u3);
		spurl = TypeUtils.decodeUrl(spurl);
		spurl = "http://"+spurl;
//		System.out.println("spurl:"+spurl);
		if(site!=null&&site.isEmpty()){
			site = null;
		}
    	if(requrl==null||requrl.isEmpty()){
    		requrl = spurl;
    	}
		if (requrl.indexOf("http://") < 0 && requrl.indexOf("https://") < 0){
			requrl = "http://" + requrl;
		}
		UserBean userbean= new UserBean();
		userbean.setId(userid);
		userbean.setName(username);
		request.getSession().setAttribute("userInfo", userbean);
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
        	if(user.length == 3){
        		request.setAttribute("email", user[2]);
        	}
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
		List<String> pWprice = data.getpWprice();
		if(priceUnit != null){
			if(priceUnit.equals("RMB")){
				//获取汇率
				Map<String, Double> maphl = Currency.getMaphl(request);
				//换算
				DecimalFormat df=new DecimalFormat(".##");
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
									data.setpOprice(pw.split("-", 0)[0]);
								}
						}
						data.setpWprice(pwList);
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
    		if(pSprice.indexOf("-")>-1){
    			pSprice = pSprice.split("-")[1];
    		}else if(pWprice != null){
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
		}
    	 request.setAttribute("spider", data);
    	 //----lizhanjun如果访问的商品详情页可正常访问 就设置最近浏览和现实留言及回复信息----strt
    	/* if(data != null){
    		 if(data.getTitle() != null){
	    	 if( !Pattern.compile("(淘宝网 - 淘！我喜欢)").matcher(data.getTitle()).find()
	    			 && !Pattern.compile("(对不起，您访问的页面不存在！ -尚天猫，就购了)").matcher(data.getTitle()).find()) {//将浏览的记录保存到cookie中
//	    		 RecentViewServlet.saveCookie(request, response, data);
	    		 if( userId != 0) {
		    		 List<GuestBookBean> gbbs = getGBBS(data.getpID(),userId);
		    		 LOG.warn("size:"+gbbs.size());
		    		 request.setAttribute("gbbs", gbbs);
	    		 }
	    		 //request.setAttribute("lyts", request.getParameter("lyts"));
	    	 }
    		 }
    	 }*/
    	 request.setAttribute("recentProducts", RecentViewServlet.getRencent(request, response));
    	 
    	 //----lizhanjun如果访问的商品详情页可正常访问 就设置最近浏览和现实留言及回复信息----end
    	 long lzjen = new Date().getTime();
    	 LOG.warn("请求商品详情页以及解析成GoodsBean所花的时间:"+(lzjen - lzjst));
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
			e.printStackTrace();
			/*iss.addURL(userName, requrl,-1);*/
		}finally{
			if(b) {
				javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/spider.jsp");
				homeDispatcher.forward(request, response);
			}
		}
//    	out.print(spider.toString());
	}

    
    /**
     * 方法描述:将浏览记录保存到cookie中
     * author:lizhanjun
     * date:2015年4月21日
     * @param request
     * @param response
     * @param data
     */
    public static void saveCookie(HttpServletRequest request, HttpServletResponse response, GoodsBean data) {
    		//将刚浏览过的商品保存到数据库中
    		RecentViewBean rvb = new RecentViewBean();
    		rvb.setPid(data.getpID());
    		rvb.setPname(data.getpName());
    		rvb.setPurl(data.getpUrl());
    		if(data.getpImage() != null && data.getpImage().size() > 0) {
    			rvb.setImgUrl(data.getpImage().get(0));
    		}
    		rvb.setPrice(data.getpSprice());
    		rvb.setMinOrder(data.getMinOrder());
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

    /**
     * 方法描述:根据商品id查询出客户曾经对该商品的留言以及回答
     * author: lizhanjun
     * date:2015年4月21日
     * @return
     */
    public static List<GuestBookBean> getGBBS(String pid,int userId){
    	IGuestBookService ibs = new GuestBookServiceImpl();
    	//IReplyService is = new ReplyService();
    	//显示最近答复的前三个
    	List<GuestBookBean> gbbList = ibs.findByPid(pid,userId,0,3);
    	for (GuestBookBean gb : gbbList) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy",Locale.ENGLISH);
			try {
				//gb.setCreateTime(dateFormat.parse(dateFormat.format(gb.getCreateTime())));
				gb.setShowTime(dateFormat.format(gb.getCreateTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	return gbbList;
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
     * 方法描述:获取商品详情
     * author: ylm
     * date:2015年8月6日
     * @return
     */
    public void getGoodInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String url = request.getParameter("url"); 
    	String ip = request.getParameter("ip");
    	String sBuffer = ParseGoodsUrl.getInfo(url,ip);
    	PrintWriter out = response.getWriter();
    	out.print(sBuffer);
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
    	String url = request.getParameter("url");
    	String pid = request.getParameter("pid");
    	String perWeight = request.getParameter("perWeight");
    	String width = request.getParameter("width");
    	String weight = request.getParameter("weight");
    	String sprice = request.getParameter("sprice");
    	String sBuffer = ParseGoodsUrl.getFee(request,url, pid,sprice,weight,width,perWeight);
    	PrintWriter out = response.getWriter();
    	out.print(sBuffer);
    	out.flush();
    	out.close();
    	LOG.info("getGoodFree end:"+sBuffer);
    }
}
