package com.cbt.parse.servlet;

import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.SearchUtils;
import com.cbt.processes.servlet.Currency;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Servlet implementation class GoodsSearch
 */
public class GoodsSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String website = request.getParameter("site");
		if(website==null||"undefind".equals(website)||website.isEmpty()){
			website = null;
		}
		String pid = request.getParameter("pid");
		if(pid==null||"undefind".equals(pid)||pid.isEmpty()){
			pid = null;
		}
		String cid = request.getParameter("cid");
		if(cid==null||"undefind".equals(cid)||cid.isEmpty()){
			cid = null;
		}
		String html_session = request.getParameter("session");
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(html_session!=null&&"false".equals(html_session)){
			currency1 = "USD";
		}
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		String[] user =  WebCookie.getUser(request);
		String userName = "游客";
    	if(user != null){
        	userName = user[1];
        	request.setAttribute("username", userName);
        	request.setAttribute("email", user[2]);
    	}
     try{
    	 GoodsBean data = new GoodsBean();
    	 data = ParseGoodsUrl.goodsDriver(pid, cid);
		 //妞妞车价格
		 double exchange_rate = 1;
		 if(data != null){
			 String psprice = data.getpSprice();
			 if((SearchUtils.key(data.getpName()) && Utility.getStringIsNull(psprice))){
				 if(psprice.indexOf("-") == -1){
					 double psprice1 = Double.parseDouble(psprice);
					 if(psprice1>60 && psprice1<170){
						 data.setpSprice("170.00");
					 }
				 }
			 }
			 if(data.getTitle() != null){
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
			 if(priceUnit != null){
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
					 double h1 = 0;
					 if(pSprice.indexOf("-")>-1){
						 double h2 = Double.parseDouble(pSprice.split("-")[0].trim()) * exchange_rate;
						 h1 = Double.parseDouble(pSprice.split("-")[1].trim()) * exchange_rate;
						 pSprice = df.format(h2) + "-" + df.format(h1);
					 }else{
						 h1 = Double.parseDouble(pSprice) * exchange_rate;
						 pSprice = df.format(h1);
					 }
					 if(h1 < 15 && h1 > 0){
						 //服装，鞋，和首饰 类别，最小金额降低到 3 USD.
						if(Utility.getIsInt(data.getMinOrder())){
							int k = 3;
							int minorder = (int) Math.ceil(k/h1);
							int minorder_ = Utility.getIsInt(data.getMinOrder())? Integer.parseInt(data.getMinOrder()):1;
							data.setMinOrder((minorder_ > minorder ? minorder_ : minorder)+"");
						}
					 }
				 }
				 if(Utility.getStringIsNull(fPrice)){
					 if(fPrice.indexOf("-")>-1){
						 double hl = Double.parseDouble(fPrice.split("-")[0].trim()) * exchange_rate;
						 double h2 = Double.parseDouble(fPrice.split("-")[1].trim()) * exchange_rate;
						 fPrice = df.format(hl)+"-"+df.format(h2);
					 }else{
						 double h1 = Double.parseDouble(fPrice) * exchange_rate;
						 fPrice = df.format(h1);
					 }
				 }
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
			 data.setpSprice(pSprice);
			 String shopId= data.getpID();
			 if(shopId == null){
				 data.setsID("cbt");
			 }else if(shopId.equals("")){
				 data.setsID("cbt");
			 }
			 request.setAttribute("country_fz", 0);
			 Cookie cookie = WebCookie.getCookieByName(request, "expressType");
			 String idsString = "";
			 String country = "USA";
			 String countryid = "36";
			 if(cookie != null){
				 idsString = cookie.getValue();
				 String[] expresssString = idsString.split("@");
				 country = expresssString[3];
				 countryid = expresssString[0];
			 }
			 if(Utility.getStringIsNull(data.getFree())){
				 if(data.getFree().equals("1")||data.getFree().equals("2")){
					 if(countryid.equals("29") || countryid.equals("37")){
						 request.setAttribute("country_fz", 1);
					 }
				 }
			 }
			 request.setAttribute("country", country);
			 request.setAttribute("countryid", countryid);
			 request.setAttribute("applicable_credit", applicable_credit);
			 request.setAttribute("currency", currency1);
			 request.setAttribute("spider_pWprice", pWprice1.size() == 0 && !Utility.getStringIsNull(data.getpWprice()) ? data.getpWprice() : pWprice1);
			 String time = data.getTime();
			 if(Utility.getStringIsNull(time)){
				 if(time.indexOf("-") > -1){
					 time = time.split("-")[1];
				 }
				 int time_ = Integer.parseInt(time);
				 if(time_ <= 15){
					 data.setTime("8-12");
					 data.setMethod("China Post Express");
				 }else{
					 data.setTime("20-45");
					 data.setMethod("China Post SAL");
				 }
			 }
		 }
		 request.setAttribute("spider", data);
		 if(data.getType() != null && data.getType().size() > 0){
			 request.setAttribute("stype", JSONArray.fromObject(data.getType()));
		 }
		 request.setAttribute("exchange_rate", exchange_rate);
		 data = null;
		 }
 } catch (Exception e) {
		e.printStackTrace();
}finally{
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/spider.jsp");
		homeDispatcher.forward(request, response);
	}
	}
}