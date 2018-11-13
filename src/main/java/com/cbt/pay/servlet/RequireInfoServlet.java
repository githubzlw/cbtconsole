package com.cbt.pay.servlet;

import com.cbt.bean.ClassDiscount;
import com.cbt.bean.SpiderBean;
import com.cbt.pay.service.ISpidersServer;
import com.cbt.pay.service.SpidersServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.processes.utils.Processes;
import com.cbt.util.Application;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation class RequireInfoServerlet
 */
public class RequireInfoServlet extends HttpServlet {
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(RequireInfoServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequireInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * 获取选中的商品item
	 */
	protected void getSelectedItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String info = request.getParameter("info");
		HttpSession session = request.getSession();
		Object infos = session.getAttribute(info);
		if(infos == null){
			response.sendRedirect("processesServlet?action=getShopCar&className=Goods");
			return;
		}
		Map<String, String> mapx = (Map<String, String>) infos;
		String[] userinfo = WebCookie.getUser(request); 
		IUserServer userServer = new UserServer();
		int userid=0;
		if(userinfo != null){
			userid=Integer.parseInt(userinfo[0]);
		}else{
			response.sendRedirect("cbt/geton.jsp");
			return;
		}
		ISpidersServer is = new SpidersServer();
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		if(userid!=0){
			spiderlist = is.getSelectedItem(userid, mapx.get("itemId"),Integer.parseInt(mapx.get("state")));
		}else{
			LOG.warn("没有userid");
		}
		double price_e = 0;double feePrice_e = 0;
		double price_s = 0;double feePrice_s = 0;
		String days = "";
		String days_free_e = "";
		String days_free_s = "";
		String pay_express_e = "",pay_express_s = "";
		double sum_v_ff_e = 0;
		double sum_w_ff_e = 0;
		double max_w_ff_e = 0;
		double sum_v_ff_s = 0;
		double sum_w_ff_s = 0;
		double max_w_ff_s = 0;
		String guid_s = "";
		String guid_e = "";
		String itemid_ = "";
		String itemid_e = "";
		String itemid_s = "";
		double extra_freight_s = 0;
		double extra_freight = 0;
		String regEx="[^0-9]";
		int isOk = 0;//国内交期时间和国际交期时间都大于10的,1=不显示，存在大于10
		int delivery_long = 0;
		//获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		Map<String, Double> mms = Currency.getMaphl(request);
		Pattern p = Pattern.compile(regEx);
		int cost_number = 0;
		int free_e_number = 0;
		int free_s_number = 0;
		int is_long_deliveryTime = 0;
		if(spiderlist.size() > 0){
			double ex = mms.get(spiderlist.get(0).getCurrency())/mms.get("USD");
			for (int i = 0; i < spiderlist.size(); i++) {
				SpiderBean spiderBean = spiderlist.get(i);
				String delivery_time = spiderBean.getDelivery_time();
				delivery_time = delivery_time == null ? "" : delivery_time;
				int delivery_time_ = 0;
				Matcher m = p.matcher(delivery_time);   
				int match1=Integer.parseInt(m.replaceAll("").trim());
				if((delivery_time).indexOf("week")>=0){
					delivery_time_=match1*7;
				}else if((delivery_time).indexOf("month")>=0){
					delivery_time_=match1*30;
				}else{
					delivery_time_=match1;
				}
				/* array[i]=delivery_time; */
				if(spiderBean.getUrl().indexOf("taobao")!=-1||spiderBean.getUrl().indexOf("tmall")!=-1){
					delivery_time_=5;
				}
				if(delivery_time_ > 10){
					is_long_deliveryTime = 1;
				}
				spiderBean.setDelivery_time(delivery_time_ != 0 ? delivery_time_+"" : "");
				String freePrice = "0";
				if(spiderBean.getFreight_free() != 0){
					String days_free = spiderBean.getFree_sc_days();
					String free_shopping_company = spiderBean.getFree_shopping_company();
					if(Utility.getStringIsNull(spiderBean.getFeeprice()) && Utility.getStringIsNull(spiderBean.getPrice()) && !spiderBean.getFeeprice().equals("0.00")){
						freePrice = Processes.getFreeToFreight(spiderBean.getPrice(), ex, spiderBean.getFeeprice(), spiderBean.getSource_url(), spiderBean.getpWprice(), spiderBean.getNumber());
					}
					if(free_shopping_company.indexOf("Express") > -1){
						price_e += Double.parseDouble(spiderBean.getPrice()) * spiderBean.getNumber();
						feePrice_e += Double.parseDouble(freePrice) * spiderBean.getNumber();
						days_free_e = days_free;
						sum_w_ff_e += Utility.getIsDouble(spiderBean.getTotal_weight())?Double.parseDouble(spiderBean.getTotal_weight()) : 0;
						sum_v_ff_e += Utility.getIsDouble(spiderBean.getBulk_volume())?Double.parseDouble(spiderBean.getBulk_volume()) : 0;
						double perwight = Utility.getIsDouble(spiderBean.getPerWeight())?Double.parseDouble(spiderBean.getPerWeight()) : 0;
						if(perwight > max_w_ff_e){
							max_w_ff_e = perwight;
						}
						pay_express_e = free_shopping_company;
						guid_e = guid_e + "@" + spiderBean.getGuId() + "@";
						itemid_e += spiderBean.getId() + "@";
						free_e_number ++;
					}else{
						price_s += Double.parseDouble(spiderBean.getPrice()) * spiderBean.getNumber();
						feePrice_s += Double.parseDouble(freePrice) * spiderBean.getNumber();
						days_free_s = days_free;
						sum_w_ff_s += Utility.getIsDouble(spiderBean.getTotal_weight())?Double.parseDouble(spiderBean.getTotal_weight()) : 0;
						sum_v_ff_s += Utility.getIsDouble(spiderBean.getBulk_volume())?Double.parseDouble(spiderBean.getBulk_volume()) : 0;
						double perwight = Utility.getIsDouble(spiderBean.getPerWeight())?Double.parseDouble(spiderBean.getPerWeight()) : 0;
						if(perwight > max_w_ff_s){
							max_w_ff_s = perwight;
						}
						pay_express_s = free_shopping_company;
						guid_s = guid_s + "@" + spiderBean.getGuId() + "@";
						itemid_s += spiderBean.getId() + "@";
						extra_freight_s += spiderBean.getExtra_freight();
						free_s_number ++;
					}
				}else{
					cost_number ++;
					itemid_ += spiderBean.getId() + "@";
					extra_freight += spiderBean.getExtra_freight();
					if(delivery_time_ > delivery_long){
						delivery_long = delivery_time_;
					}
					if(Integer.parseInt(delivery_time) > 10 && isOk == 0){
						isOk = 1;
					}
				}
				freePrice = "0";
			}
		}
		request.setAttribute("extra_freight", extra_freight);
		request.setAttribute("extra_freight_s", extra_freight_s);
		request.setAttribute("userId", userid);
		request.setAttribute("itemid", mapx.get("itemId"));
		request.setAttribute("pay_express", mapx.get("express"));
		String[] pay_express_ = mapx.get("express").split("@");
		String country = "";
		String method = "";
		if(pay_express_.length > 1){
			days = pay_express_[1];
			method = pay_express_[0];
			if(pay_express_.length > 2){
				country = pay_express_[2];
			}
		}
		String delivery_ = ""; 
		if(Utility.getStringIsNull(days)){
			String dayss = days.split("-")[0];
			int days_ = Utility.getIsInt(dayss)?Integer.parseInt(dayss):0;
			if(days_ > 10){
				isOk = 1;
			}
			delivery_ = (days_ + delivery_long) + "";
			if(days.indexOf("-") > -1){
				delivery_ = delivery_ +"-" + (Integer.parseInt(days.split("-")[1])+delivery_long);
			}
		}
		request.setAttribute("method", method);
		request.setAttribute("delivery_c", delivery_long);//国际交期时间+国内交期最长时间
		request.setAttribute("delivery_", delivery_);//国际交期时间+国内交期最长时间
		request.setAttribute("days", days);
		request.setAttribute("isOk", itemid_.equals("")?1:isOk);
		request.setAttribute("sf", Utility.getIsDouble(mapx.get("sf"))?Double.parseDouble(mapx.get("sf")):0);
		request.setAttribute("sum_v", Utility.getIsDouble(mapx.get("sum_v"))?Double.parseDouble(mapx.get("sum_v")):0);
		request.setAttribute("sum_w", Utility.getIsDouble(mapx.get("sum_w"))?new BigDecimal(mapx.get("sum_w")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue():0);
		request.setAttribute("max_w", mapx.get("max_w"));
		request.setAttribute("itemid_", itemid_);
		request.setAttribute("cost_number", cost_number);
		request.setAttribute("free_e_number", free_e_number);
		request.setAttribute("free_s_number", free_s_number);
		if(Utility.getStringIsNull(itemid_)){
			mapx.put("itemid", itemid_);
		}
		if(Utility.getStringIsNull(itemid_e)){
			mapx.put("itemid_e", itemid_e);
			mapx.put("express_e", pay_express_e + "@" + days_free_e + "@" +country);
		}
		if(Utility.getStringIsNull(itemid_s)){
			mapx.put("itemid_s", itemid_s);
			mapx.put("express_s", pay_express_s + "@" + days_free_s + "@" +country);
		}
		if(sum_w_ff_e != 0){
			request.setAttribute("days_free_e", days_free_e);
			request.setAttribute("pay_express_e", mapx.get("express_e"));
			request.setAttribute("sum_v_ff_e", sum_v_ff_e);
			request.setAttribute("sum_w_ff_e", new BigDecimal(sum_w_ff_e).setScale(2, BigDecimal.ROUND_HALF_UP));
			request.setAttribute("max_w_ff_e", max_w_ff_e);
			request.setAttribute("guid_e", guid_e);
			request.setAttribute("price_e", new BigDecimal(price_e).setScale(2, BigDecimal.ROUND_HALF_UP));
			request.setAttribute("feePrice_e", new BigDecimal(feePrice_e).setScale(2, BigDecimal.ROUND_HALF_UP));
			mapx.put("sum_v_ff_e", sum_v_ff_e+"");
			mapx.put("sum_w_ff_e", sum_w_ff_e+"");
			mapx.put("max_w_ff_e", max_w_ff_e+"");
		}
		if(sum_w_ff_s != 0){
			request.setAttribute("days_free_s", days_free_s);
			request.setAttribute("pay_express_s", mapx.get("express_s"));
			request.setAttribute("sum_v_ff_s", sum_v_ff_s);
			request.setAttribute("sum_w_ff_s", new BigDecimal(sum_w_ff_s).setScale(2, BigDecimal.ROUND_HALF_UP));
			request.setAttribute("max_w_ff_s", max_w_ff_s);
			request.setAttribute("guid_s", guid_s);
			request.setAttribute("price_s", new BigDecimal(price_s).setScale(2, BigDecimal.ROUND_HALF_UP));
			request.setAttribute("feePrice_s", new BigDecimal(feePrice_s).setScale(2, BigDecimal.ROUND_HALF_UP));
			mapx.put("sum_v_ff_s", sum_v_ff_s+"");
			mapx.put("sum_w_ff_s", sum_w_ff_s+"");
			mapx.put("max_w_ff_s", max_w_ff_s+"");
		}
		request.setAttribute("countryid", mapx.get("countryId"));
		session.setAttribute(info,mapx);
		request.setAttribute("info", info);
		 
		double credit = 0;
		if(userinfo != null){
			credit = userServer.getUserApplicableCredit(Integer.parseInt(userinfo[0]));
		}
		request.setAttribute("is_long_deliveryTime", is_long_deliveryTime);
		request.setAttribute("currency", currency1);
		request.setAttribute("credit", credit);
		request.setAttribute("spiderlist", spiderlist);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/DeliveryTime.jsp");
		homeDispatcher.forward(request, response);
	}

	public static void main(String[] args) {
		String string = "dw3.e";
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(string);   
		System.out.println( m.replaceAll("").trim());
	}
	
	protected void getSelectedItemrequiredinfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String info = request.getParameter("info");
		HttpSession session = request.getSession();
		Object infos = session.getAttribute(info);
		if(infos == null){
			response.sendRedirect("processesServlet?action=getShopCar&className=Goods");
			return;
		}
		Map<String, String> mapx = (Map<String, String>) infos;

		String itemid = mapx.get("itemId");
		String pay_express = mapx.get("express");
		String sf = mapx.get("sf");
		int state = Integer.parseInt(mapx.get("state"));
		String[] userinfo = WebCookie.getUser(request); 
		int userid=0;
		if(userinfo != null){
			userid=Integer.parseInt(userinfo[0]);
		}else{
			response.sendRedirect("cbt/geton.jsp");
		}
		ISpidersServer is = new SpidersServer();
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		if(userid!=0){
			spiderlist = is.getSelectedItem(userid, itemid,state);
		}else{
			LOG.warn("没有userid");
		}
		 //获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		request.setAttribute("currency", currency1);
		request.setAttribute("itemid", itemid);
		request.setAttribute("spiderlist", spiderlist);
		request.setAttribute("pay_express", pay_express);
		request.setAttribute("sf", sf);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/RequiredInfo.jsp");
		homeDispatcher.forward(request, response);
	}
	
	protected void getPayproduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String info = request.getParameter("info");
		String addressInfo= request.getParameter("addressInfo");
		String currency = null;
		Object ordersession = session.getAttribute(info);
		String itemId = "", sf = "", pay_express = "",sum_w = "",sum_v = "";
		int state = 0;
		 //获取用户中的货币
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		String itemid_ = null;
		double sf_e = 0;
		double sf_s = 0;
		int sf_res = 0;
		if(ordersession != null){
			Map<String, String> mapx = (Map<String, String>) ordersession;
			itemId = mapx.get("itemId");
			pay_express = mapx.get("express");
			double sf_ = Utility.getIsDouble(mapx.get("sf"))?Double.parseDouble(mapx.get("sf")):0;
			sf_e = Utility.getIsDouble(mapx.get("sf_e"))?Double.parseDouble(mapx.get("sf_e")):0;
			sf_s = Utility.getIsDouble(mapx.get("sf_s"))?Double.parseDouble(mapx.get("sf_s")):0;
			sf = new BigDecimal(sf_ + sf_e + sf_s).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			sum_v = mapx.get("sum_v");
			sum_w = mapx.get("sum_w");
			currency = mapx.get("currency");
			state = Integer.parseInt(mapx.get("state"));
			itemid_ = Utility.getStringIsNull(mapx.get("itemid"))?mapx.get("itemid"):null;
			if(Utility.getStringIsNull(sf) && Utility.getStringIsNull(currency)){
				//获取汇率
				Map<String, Double> maphl = Currency.getMaphl(request);
				double exchange_rate = maphl.get(currency1)/maphl.get(currency);
				if(exchange_rate != 1){
					int sf_1 = (int)Math.rint((sf_*exchange_rate));
					int sf_e1 = (int)Math.rint((sf_e*exchange_rate));
					int sf_s1 = (int)Math.rint((sf_s*exchange_rate));
					mapx.put("sf",sf_1+"");
					mapx.put("sf_e",sf_e1+"");
					mapx.put("sf_s",sf_s1+"");
					mapx.put("currency",currency1);
					request.setAttribute("sf", sf_1+sf_e1+sf_s1);
					sf_res = sf_1+sf_e1+sf_s1; 
				}else{
					sf_res = (int)Math.rint((Double.parseDouble(sf)*exchange_rate));
				}
			}
			if(Utility.getStringIsNull(addressInfo)){
				mapx.put("addressInfo", addressInfo);
			}
		}else{
			response.sendRedirect("processesServlet?action=getShopCar&className=Goods");
			return ;
		}
		String[] userinfo = WebCookie.getUser(request); 
		int userid=0;
		if(userinfo != null){
			userid=Integer.parseInt(userinfo[0]);
		}else{
			response.sendRedirect("cbt/geton.jsp");
			return;
		}
		ISpidersServer is = new SpidersServer();
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		if(userid!=0){
			spiderlist = is.getSelectedItem(userid, itemId,state);
		}else{
			LOG.warn("没有userid");
		}
		IUserServer userServer = new UserServer();
		double[] balance_credit = userServer.getBalance(userid);
	
		request.setAttribute("currency", currency1);
		request.setAttribute("balance", balance_credit[0]);
		request.setAttribute("credit", balance_credit[1]);
		request.setAttribute("itemId", itemId);
		request.setAttribute("result", JSONArray.fromObject(spiderlist).toString());
		request.setAttribute("spiderlist", spiderlist);
		request.setAttribute("express", pay_express);
		request.setAttribute("sum_w", sum_w);
		request.setAttribute("sum_v", sum_v);
		request.setAttribute("itemid_", itemid_);
		//服务费  2016-1-7 remove(sj)
		/*for (int i = 0; i < spiderlist.size(); i++) {
			double spider_price = Double.parseDouble(spiderlist.get(i).getPrice()) * spiderlist.get(i).getNumber();
			int gid = spiderlist.get(i).getId();
			if(itemid_ != null){
				for (int j = 0; j < itemid_.length; j++) {
					if(Utility.getStringIsNull(itemid_[j])){
						if(Integer.parseInt(itemid_[j]) == gid){
							service_fee += spider_price;
							break;
						}
					}
				}
			}
			if(itemid_e != null && sf_e != 0){
				for (int j = 0; j < itemid_e.length; j++) {
					if(Utility.getStringIsNull(itemid_e[j])){
						if(Integer.parseInt(itemid_e[j]) == gid){
							service_fee += spider_price;
							break;
						}
					}
				}
			}
			if(itemid_s != null && sf_s != 0){
				for (int j = 0; j < itemid_s.length; j++) {
					if(Utility.getStringIsNull(itemid_s[j])){
						if(Integer.parseInt(itemid_s[j]) == gid){
							service_fee += spider_price;
							break;
						}
					}
				}
			}
		}*/
		//获取混批折扣率
		double extra_freight = 0;
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		list_cd = Application.getClassDiscount(request);
		for (int i = 0; i < spiderlist.size(); i++) {
			SpiderBean sb = spiderlist.get(i);
			if(sb.getFreight_free() == 0){
				extra_freight += sb.getExtra_freight();
			}
			for (int j = 0; j < list_cd.size(); j++) {
				ClassDiscount cd = list_cd.get(j);
				int typeString = sb.getGoods_class();
				if(cd.getId() == typeString && Double.parseDouble(sb.getPrice().toString()) <= 150){
					cd.setSum_price( cd.getSum_price()+sb.getNumber()*Double.parseDouble(sb.getPrice()));
					break;
				}
			}
		}
		double g_price = 0;
		for (int k = 0; k < list_cd.size(); k++) {
			ClassDiscount cd = list_cd.get(k);
			if(cd.getPrice() < cd.getSum_price()){
				g_price += cd.getSum_price()-cd.getSum_price()*cd.getDeposit_rate();
			}
		}
		request.setAttribute("sf", sf_res+extra_freight);
		request.setAttribute("g_price", g_price);//折扣金额
		request.setAttribute("express", pay_express);
		request.setAttribute("info", info);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/Settlement.jsp");
		homeDispatcher.forward(request, response);
	}
}
