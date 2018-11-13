package com.cbt.processes.servlet;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.ShippingBean;
import com.cbt.bean.SpiderBean;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.dao.IPreferentialDao;
import com.cbt.processes.dao.PreferentialDao;
import com.cbt.processes.service.*;
import com.cbt.processes.thread.GoodsAddThread;
import com.cbt.processes.utils.Processes;
import com.cbt.util.AppConfig;
import com.cbt.util.Application;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Goods extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SpiderServer.class);
	
	/**
	 * 获取购物车中的商品shop-car.jsp
	 */
	protected void getShopCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		/*HttpSession session = request.getSession(); 
		UserBean user = (UserBean) session.getAttribute("userInfo");*/
		String userid_str = request.getParameter("userid");
		String showFlag = request.getParameter("showFlag");
		int showFlag_ = Utility.getStringIsNull(showFlag)?Integer.parseInt(showFlag):0;
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		ISpiderServer is = new SpiderServer();
		Map<String, Object> map = new HashMap<String, Object>();
		String userName = "";
		if(userid_str != null){
			int userid = 0;
			userid = Integer.parseInt(userid_str);
			list = is.getGoogs_cars(null,userid,showFlag_);
		}else{
			String sessionId = null;
			String[] userinfo = WebCookie.getUser(request);
			if(userinfo != null){
				list = is.getGoogs_cars(null,Integer.parseInt(userinfo[0]),showFlag_);
				userName = userinfo[1];
				request.setAttribute("userid", userinfo[0]);
	        	request.setAttribute("username", userName);
	        	if(userinfo.length == 3){
	        		request.setAttribute("email", userinfo[2]);
	        	}
			}else{
			    sessionId = this.getSessionId(request, response);
			    list = is.getGoogs_cars(sessionId,0,showFlag_);
			}
			Cookie cookie = WebCookie.getCookieByName(request, "cartNumber") ;
			if(cookie != null){
				cookie.setValue(SpiderServer.cartNumber+"");
				cookie.setMaxAge(3600*24*2);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		float sum_w = 0;
		float sum_v = 0;
		//询价商品
		List<SpiderBean> listU = new ArrayList<SpiderBean>();
		//免邮商品s
		List<SpiderBean> listF_s = new ArrayList<SpiderBean>();
		//免邮商品e
		List<SpiderBean> listF_e = new ArrayList<SpiderBean>();
		//正常商品
		List<SpiderBean> listP = new ArrayList<SpiderBean>();
//		Object cookie =request.getSession().getAttribute("expressType");
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		String express = "";
		if(cookie != null){
			express = cookie.getValue();
			map.put("express", express);
		}
		double extra_freight_e = 0;
    	double extra_freight_s = 0;
    	double extra_freight = 0;
		HttpSession session = request.getSession();
		session.setAttribute("shopcar",list);
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		Map<String, Double> mms = Currency.getMaphl(request);
		if(list.size() > 0){
			double ex = mms.get(currency1)/mms.get("USD");
			for (int i = 0; i < list.size(); i++) {
				SpiderBean spider = list.get(i);
				if(spider.getNorm_least() != null){
					list.get(i).setNorm_least1(spider.getNorm_least().split(" ")[0]);
				}
				String id = "+"+spider.getGuId()+"+";
				if(spider.getPrice() == null || spider.getPrice().equals("0") || spider.getPrice().equals("0.0")){
					listU.add(spider);
				}else if(spider.getFreight_free() == 1 && express.indexOf(id) < 0){
					spider.setFree_price(Processes.getFreeToFreight(spider.getPrice(), ex, spider.getFeeprice(), spider.getSource_url(), spider.getpWprice(), spider.getNumber()));
					if(spider.getFree_shopping_company().indexOf("SAL") > -1){
						listF_s.add(spider);
						extra_freight_s += spider.getExtra_freight();
					}else{
						listF_e.add(spider);
						extra_freight_e += spider.getExtra_freight();
					}
				}else{
					if(Utility.getStringIsNull(spider.getFeeprice()) && Utility.getStringIsNull(spider.getPrice()) && !spider.getFeeprice().equals("0.00")){
						String freePrice = Processes.getFreightToFree(spider.getPrice(), ex, spider.getFeeprice(), spider.getSource_url(), spider.getpWprice(), spider.getNumber());
						spider.setFree_price(freePrice);
					}
					if(spider.getWidth() != "" && spider.getWidth() != null){
						sum_v = sum_v + Float.parseFloat(spider.getBulk_volume());
						sum_w = sum_w + Float.parseFloat(spider.getTotal_weight());
					}
					listP.add(spider);
					extra_freight += spider.getExtra_freight();
				}
			}
		}
		
		
		map.put("sum_w", sum_w);
		map.put("sum_v", sum_v);
		
		map.put("spider", listP);
		map.put("spider_un", listU);
		map.put("spider_ff_s", listF_s);
		map.put("spider_ff_e", listF_e);
		
		request.setAttribute("currency", Utility.getCurrency(currency1));
		request.setAttribute("goods_car", map);
		request.setAttribute("extra_freight_e", extra_freight_e);
		request.setAttribute("extra_freight_s", extra_freight_s);
		request.setAttribute("extra_freight", extra_freight);
		request.setAttribute("cart_number", listP.size()+listF_e.size()+listF_s.size());
		String class_dis = JSONArray.fromObject(Application.getClassDiscount(request)).toString().replaceAll("'", "\\\\'");
		request.setAttribute("class_discount", class_dis);
		map = null;
		list = null;
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/shop-car.jsp");
		homeDispatcher.forward(request, response);
	}

	/**
	 * 获取购物车中的商品spider.jsp
	 */
	protected void getSpiderCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		HttpSession session = request.getSession();
		Object shopcar = session.getAttribute("shopcar");
		if(shopcar != null){
			list = (List<SpiderBean>) shopcar;
		}else{
			 String[] ub = WebCookie.getUser(request);
			 ISpiderServer is = new SpiderServer();
			 if(ub != null){
				 list = is.getGoogs_cars(null,Integer.parseInt(ub[0]),0);
			 }else{
				 list = is.getGoogs_cars(this.getSessionId(request, response),0,0);
			 }
			 session.setAttribute("shopcar",list);
		}
		float sum_w = 0;
		float sum_v = 0;
		float sum_ww = 0;
		float sum_w2 = 0;
		float sum_v2 = 0;
		float max_w2 = 0;
		DecimalFormat   fnum  =   new  DecimalFormat("##0.00");
		StringBuffer html = new StringBuffer();
		StringBuffer html2 = new StringBuffer();
		String email = "";
		//运输总费用
		String countryId = "36";
		String currency1 = WebCookie.cookie(request, "currency");
		if(!Utility.getStringIsNull(currency1)){
			currency1 = "USD";
		}
		String currencyfh = currency1 + "&nbsp;";
    	double extra_freight_e = 0;
    	double extra_freight_s = 0;
    	double extra_freight = 0;
		if(list.size() > 0){
			email = list.get(0).getGoods_email();
			int j = 0;
			String shopping_companyString = "";
			float max_w = 0;
			Cookie cookie = WebCookie.getCookieByName(request, "expressType");
	    	String idsString = "";
	    	String express = "";
	    	String day = "";
	    	if(cookie != null){
		    	idsString = cookie.getValue();
		    	String[] expresssString = idsString.split("@");
		    	express = expresssString[1];
		    	countryId = expresssString[0];
		    	day = expresssString[2];
	    	}
			Map<String, Double> mms = Currency.getMaphl(request);
			double ex = mms.get(currency1)/mms.get("USD");
			for (int i = 0; i < list.size(); i++) {
				SpiderBean spider = list.get(i);
//				int preferential = spider.getPreferential();//是否是优惠申请的
				email = spider.getGoods_email();
				String tw = spider.getTotal_weight();
				String bv = spider.getBulk_volume();
				float  weight = 0;
				float widths = 0;
				if(Utility.getStringIsNull(tw)){
					weight = Float.parseFloat(tw);
				}
				if(Utility.getStringIsNull(bv)){
					widths = Float.parseFloat(bv);
				}
				
				String price = spider.getPrice();
				if(!Utility.getStringIsNull(price)){
					price = "0";
				}
				price = price.replaceAll(",", "").trim();
				float  sprice = spider.getNumber() * Float.parseFloat(price);
				 String sprice_ = "";
				 String price_ = "";
		    	if(sprice == 0){
		    		price_ = "unkown";
		    		sprice_ = "unkown";
		    	}else{
		    		price_ = price;
		    		sprice_ = fnum.format(sprice);
		    	}
		    	String img = spider.getImg_url();
		    	String name = spider.getName();
		    	String res = spider.getGuId();
		    	float norm_least_f = 0;
		    	if(Utility.getStringIsNull(spider.getNorm_least())){
		    		norm_least_f = Float.parseFloat(spider.getNorm_least().split(" ")[0]);
		    	}
		    	if(idsString.indexOf("+"+res+"+") > -1){
		    		html2.append("<tr  title='");
					html2.append(name);
					html2.append("' class='div_info_goods_tr'><td class='div_info_goods_td' colspan='3'><div  class='spider_cart_div_title'>&nbsp;");
					html2.append(name);
					html2.append("</div><img class='spider_cart_div_img' onclick='fnDel("+spider.getId()+",\""+spider.getGuId()+"\");' title='Deleting' src='/cbtconsole/img/del.png'></td></tr><tr><td class='div_info_goods_td_img' rowspan='");
					html2.append(3);
					html2.append("'><img class='div_info_goods_td_img_img' alt='' src='");
					html2.append(Utility.getStringIsNull(img) ? img : "/cbtconsole/img/1.png");
					html2.append("'></td><td class='div_info_goods_td_price'><img src='/cbtconsole/img/w.jpg'/><div>");
					html2.append("<input type='text' class='cj cji' readonly='readonly' onclick='fnUpNumber_car(this,1,");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(")'/><input id='");
					html2.append(spider.getGuId());
					html2.append("' onBlur='fnUpNumber_car(this,0,");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(")'onkeydown='fnNumberInput();' maxlength='5' value='");
					html2.append(spider.getNumber());
					html2.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(");' >");
					html2.append("</div></td><td class='td_info_goods_td_price1' ><div class='div_info_goods_td_price1'>"+currencyfh+"<em>");
					html2.append(price_);
					html2.append("</em></div><td></tr><tr><td colspan='2' class='div_info_goods_td_size' >");
					if(Utility.getStringIsNull(spider.getColor())){
						html2.append("<em style='border: 1px solid #c8c9cd'>");
						html2.append(spider.getColor());
						html2.append("</em>");
					}
					html2.append("</td><td>");
					if(Utility.getStringIsNull(spider.getSize())){
						html2.append("&nbsp;<em style='border: 1px solid #c8c9cd'>");
						html2.append(spider.getSize());
						html2.append("</em>");
					}

					if(Utility.getStringIsNull(spider.getTypes())){
						String[] typeStrings = spider.getTypes().split(",");
						for (int k = 0; k < typeStrings.length-1; k++) {
							html2.append("&nbsp;<div class='spider_spec'>");
							html2.append(typeStrings[i].split(":")[1].split("@")[0]);
							html2.append("</div>");
						}
					}
					if(Utility.getStringIsNull(spider.getImg_type())){
						String[] typeStrings = spider.getImg_type().split("@");
						for (int k = 0; k < typeStrings.length-1; k++) {
							if(typeStrings[k].indexOf("http") > -1){
								html2.append("&nbsp;<div class='spider_spec'>");
								html2.append("<img  src='" + typeStrings[k] + "' style='cursor: pointer;width: 20px;height: 20px;'>&nbsp;");
								html2.append("</div>");
							}
						}
					}
					html2.append("</td></tr><tr><td class='div_info_goods_td_kg'><div style='display:block'><em style='display:none;'>");
					html2.append(widths);
					html2.append("</em> <em style='font-size: 12px;'>");
					html2.append(weight == 0?"Weight Unknown":weight+"KG");
					html2.append("</em><input type='hidden' value='");
					html2.append(spider.getWeight());
					html2.append("'/></div></td><td class='div_info_goods_td_sprice'><div class='rmb_smart'  id='goods_id");
					html2.append(res);
					html2.append("'>"+currencyfh+"<em>");
					html2.append(sprice_);
					html2.append("</em> <input type='hidden' value='"+spider.getGoods_class()+"'/></div></td></tr>");
					sum_v2 = sum_v2 + widths;
					sum_w2 = sum_w2 + weight;
					String weightString = spider.getWeight();
					float ww = 0;
					if(Utility.getStringIsNull(weightString)){
						ww = Float.parseFloat(weightString);
					}
					if(max_w2 == 0){
						max_w2 = ww;
					}else if(ww > max_w){
						max_w2 = ww;
					}
					sum_ww += weight;
					continue;
		    	}
				if(spider.getFreight_free() == 1){
					if(spider.getFree_shopping_company().indexOf("SAL") > -1){
						extra_freight_s += spider.getExtra_freight();
					}
					else if(spider.getFree_shopping_company().indexOf("Express") > -1){
						extra_freight_e += spider.getExtra_freight();
					}
					if(i == 0){
						j = 3;
						html.append("<tr class='spidershopcartr' id='");
						html.append(spider.getFree_shopping_company().replaceAll(" ", "_"));
						html.append("'><td colspan='3'  class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree'>FreeShipping:</em><em>");
						html.append(spider.getFree_shopping_company());
						html.append("</em>&nbsp;<em>");
						html.append(spider.getFree_sc_days());
						html.append("</em>Days<a  class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'><img src='/cbtconsole/img/shopcar/newicon/bt1-1.png'></em></a></div></td>");
						shopping_companyString = spider.getFree_shopping_company();
					}
					else if(!shopping_companyString.equals(list.get(i).getFree_shopping_company())){
						j = 3;
	    				String hiddenString = "<input type='hidden' value=''>";
	    				int ss = html.indexOf(hiddenString);
	    				if(ss > -1){
	    					html.replace(ss, ss + hiddenString.length(), "<input type='hidden' value='" + fnum.format(sum_w) + "_" + sum_v + "_" + max_w + "'>");
	    				}
	    				html.append("<tr class='spidershopcartr' id='");
						html.append(spider.getFree_shopping_company().replaceAll(" ", "_"));
						html.append("'><td colspan='3'  class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree'>Free Shipping:</em><em>");
						html.append(spider.getFree_shopping_company());
						html.append("</em>&nbsp;<em>");
						html.append(spider.getFree_sc_days());
						html.append("</em>&nbsp;Days&nbsp;<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'><img src='/cbtconsole/img/shopcar/newicon/bt1-1.png'></em></a></div></td>");
						shopping_companyString = spider.getFree_shopping_company();
						sum_w = 0; sum_v = 0; max_w=0;
	    			}
		    	}else{
			    	extra_freight += spider.getExtra_freight();
		    		sum_ww += weight;
					if(j==0 || j == 3){
						String hiddenString = "<input type='hidden' value=''>";
	    				int ss = html.indexOf(hiddenString);
	    				if(ss > -1){
	    					//查询并修改运输方式
//	    		    		ShippingBean sbBean = getExpress(countryId, sum_w, sum_v, max_w , spider.getShopping_company());
	    					html.replace(ss, ss + hiddenString.length(), "<input type='hidden' value='" + fnum.format(sum_w) + "_" + sum_v + "_" + max_w + "'>");
	    				}
						j = 2;
	    				html.append("<tr class='spidershopcartr1'  id='spidershopcar1'><td colspan='3' class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree1'>Shipping:</em><em id='show_express'>");
						html.append(express);
						html.append("</em>&nbsp;<em>");
						html.append(day);
						html.append("</em>Days<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'><img src='/cbtconsole/img/shopcar/newicon/bt1-1.png'></em></a></div></td>");
						shopping_companyString = spider.getFree_shopping_company();
						sum_w = 0; sum_v = 0; max_w=0;
	    			}
		    	}
				html.append("<tr  title='");
				html.append(name);
				html.append("' class='div_info_goods_tr'><td class='div_info_goods_td' colspan='3'><div  class='spider_cart_div_title'>&nbsp;");
				html.append(name);
				html.append("</div><img class='spider_cart_div_img' title='Deleting' src='/cbtconsole/img/del.png' onclick='fnDel("+spider.getId()+",\""+spider.getGuId()+"\");'></td></tr><tr><td class='div_info_goods_td_img' rowspan='");
				html.append(3);
				html.append("'>");
				/*if(spider.getFreight_free() == 1){
					html.append("<img src='/cbtconsole/img/free.png' class='spiderfreeimg'>");
				}*/
				html.append("<img class='div_info_goods_td_img_img' alt='' src='");
				html.append(Utility.getStringIsNull(img) ? img : "/cbtconsole/img/1.png");
				html.append("'></td><td class='div_info_goods_td_price'><img src='/cbtconsole/img/w.jpg'/><div>");
				html.append("<input type='text' class='cj cji' readonly='readonly' numid= '"+spider.getGuId()+"' onclick='fnUpNumber_car(this,1,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'/><input id='");
				html.append(spider.getGuId());
				html.append("' onBlur='fnUpNumber_car(this,0,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'onkeydown='fnNumberInput();' numid= '"+spider.getGuId()+"' maxlength='5' value='");
				html.append(spider.getNumber());
				html.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(");' >");
				html.append("</div></td><td class='td_info_goods_td_price1' ><div class='div_info_goods_td_price1'>"+currencyfh+"<em>");
				html.append(price_);
				html.append("</em></div>");
				if(spider.getFreight_free() == 0){
					if(Utility.getStringIsNull(spider.getFeeprice()) && !spider.getFeeprice().equals("0.00")){
						String freePrice = Processes.getFreightToFree(spider.getPrice(), ex, spider.getFeeprice(), spider.getSource_url(), spider.getpWprice(), spider.getNumber());
						html.append("<div class='div_info_goods_td_free_price'>"+currencyfh);
						html.append("<em>"+freePrice);
						html.append("</em></div>");
					}
				}
				html.append("<td></tr><tr><td colspan='2' class='div_info_goods_td_size' >");
				 if(Utility.getStringIsNull(spider.getColor())){
					html.append("<em style='border: 1px solid #c8c9cd'>");
					html.append(spider.getColor());
					html.append("</em>");
				}
				if(Utility.getStringIsNull(spider.getSize())){
					html.append("&nbsp;<em style='border: 1px solid #c8c9cd'>");
					html.append(spider.getSize());
					html.append("</em>");
				} 
				if(Utility.getStringIsNull(spider.getTypes())){
					String[] typeStrings = spider.getTypes().split(",");
					for (int k = 0; k < typeStrings.length; k++) {
						html.append("&nbsp;<div class='spider_spec'>");
						html.append(typeStrings[k].split(":")[1].split("@")[0]);
						html.append("</div>");
					}
				}
				if(Utility.getStringIsNull(spider.getImg_type())){
					String[] typeStrings1 = spider.getImg_type().split("@");
					for (int k = 0; k < typeStrings1.length; k++) {
						if(typeStrings1[k].indexOf("http") > -1){
							html.append("&nbsp;");
							html.append("<img  src='" + typeStrings1[k] + "' style='cursor: pointer;width: 20px;height: 20px;'>&nbsp;");
							html.append("");
						}
					}
				}
				
				html.append("</td></tr><tr><td class='div_info_goods_td_kg'><div style='display:block'><em style='display:none;'>");
				html.append(widths);
				html.append("</em> <em style='font-size: 12px;'>");
				html.append(weight == 0?"Weight Unknown":weight+"KG");
				html.append("</em><input type='hidden' value='");
				html.append(spider.getWeight());
				html.append("'/></div></td><td class='div_info_goods_td_sprice'><div class='rmb_smart'  id='goods_id");
				html.append(res);
				html.append("'>"+currencyfh+"<em>");
				html.append(sprice_);
				html.append("</em> <input type='hidden' value='"+spider.getGoods_class()+"'/></div></td></tr>");
				sum_v = sum_v + widths;
				sum_w = sum_w + weight;
				float ww = 0;
		    	if(Utility.getStringIsNull(spider.getWeight())){
		    		ww = Float.parseFloat(spider.getWeight().replaceAll("kg", ""));
		    	}
				if(max_w == 0){
					max_w = ww;
				}else if(ww > max_w){
					max_w = ww;
				}
			}
			if(j == 3){
				String hiddenString = "<input type='hidden' value=''>";
				int ss = html.indexOf(hiddenString);
				if(ss > -1){
					html.replace(ss, ss + hiddenString.length(), "<input type='hidden' value='" + sum_w + "_" + sum_v + "_" + max_w + "'>");
				}
			}
			if( j != 2 && idsString.indexOf("+") > -1){
				sum_w = sum_w2;
				sum_v = sum_v2;
				html.append("<tr class='spidershopcartr1'  id='spidershopcar1'><td colspan='3' class='spidershopcarfree2'><div style='width: 273px;'><input type='hidden' value='" + sum_w2 + "_" + sum_v2 + "_" + max_w2 + "'><em class='spidershopcarfree1'>Shipping:</em><em id='show_express'>");
				html.append(express);
				html.append("</em>&nbsp;<em>");
				html.append(day);
				html.append("</em>Days<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'><img src='/cbtconsole/img/shopcar/newicon/bt1-1.png'></em></a></div></td>");
			}else if(j == 2){
				String hiddenString = "<input type='hidden' value=''>";
				int ss = html.indexOf(hiddenString);
				if(ss > -1){
					if(max_w2 > max_w){
						max_w = max_w2;
					}
					html.replace(ss, ss + hiddenString.length(), "<input type='hidden' value='" + (sum_w+sum_w2) + "_" + (sum_v+sum_v2) + "_" + max_w + "'>");
				}
			}
		}
		html.append(html2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sum_w", fnum.format(sum_w));
		map.put("sum_v", fnum.format(sum_v));
		map.put("spider", html.toString());
		map.put("carNumber", list.size());
		map.put("email", email);
		map.put("countryId", countryId == "" ? "36" : countryId );
		map.put("sum_ww", fnum.format(sum_ww));
		map.put("extra_freight_e", extra_freight_e);
		map.put("extra_freight_s", extra_freight_s);
		map.put("extra_freight", extra_freight);
		html = null;
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		if(cookie != null){
			map.put("express", cookie.getValue());
		}else{
			map.put("express", null);
		}
		IPreferentialDao pre = new PreferentialDao();
		map.put("class_discount", pre.getClass_discount());
		out.print(net.sf.json.JSONArray.fromObject(map));
		out.flush();
		list = null;
		map = null;
		out.close();
	}
	
	protected void getSpiderCar2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		/*HttpSession session = request.getSession(); 
		UserBean user = (UserBean) session.getAttribute("userInfo");*/
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		String sessionId = null;
		String[] userinfo = WebCookie.getUser(request);
		ISpiderServer is = new SpiderServer();
		if(userinfo != null){
			list = is.getGoogs_cars("",Integer.parseInt(userinfo[0]),0);
		}else{
		    sessionId = this.getSessionId(request, response);
		    list = is.getGoogs_cars(sessionId,0,0);
		}
		float sum_w = 0;
		float sum_v = 0;
		DecimalFormat   fnum  =   new  DecimalFormat("##0.00");
		StringBuffer html = new StringBuffer();
		String email = "";
		if(list.size() > 0){
			email = list.get(0).getGoods_email();
			for (int i = 0; i < list.size(); i++) {
				SpiderBean spider = list.get(i);
				email = spider.getGoods_email();
				if(spider.getFreight_free() != 1 && spider.getPrice() != "0" && spider.getWidth() != "" && spider.getWidth() != null){
					sum_v = sum_v + Float.parseFloat(spider.getBulk_volume());
					sum_w = sum_w + Float.parseFloat(spider.getTotal_weight());
				}
				String price = spider.getPrice();
				if(!Utility.getStringIsNull(price)){
					price = "0";
				}
				float  sprice = (spider.getNumber()*Float.parseFloat(price));
				 String sprice_ = "";
				 String price_ = "";
				 
		    	if(sprice == 0){
		    		price_ = "unkown";
		    		sprice_ = "unkown";
		    	}else{
		    		price_ = "$"+price;
		    		sprice_ = fnum.format(sprice);
		    	}
		    	String img = spider.getImg_url();
		    	String name = spider.getName();
		    	int res = spider.getId();
		    	String widths = spider.getTotal_weight();String freights = spider.getBulk_volume();
		    	String display = "none";
		    	//免邮产品和询价商品不显示商品的重量和体积
		    	if(spider.getFreight_free() != 1 && !price.equals("")){
		    		display = "block";
		    	}
		    	float norm_least_f = Float.parseFloat(spider.getNorm_least().split(" ")[0]);
				html.append("<tr  title='");
				html.append(name);
				html.append("' class='div_info_goods_tr'><td class='div_info_goods_td' colspan='2'><div  class='spider_cart_div_title'>&nbsp;");
				html.append(name);
				html.append("<div></td><td class='div_info_goods_td_price'><img src='/cbtconsole/img/w.jpg'/><div>");
				html.append("<input type='text' class='cj cji' readonly='readonly' onclick='fnUpNumber_car(this,1,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'/><input id='goods_car_number");
				html.append(res);
				html.append("' onBlur='fnUpNumber_car(this,0,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'onkeydown='fnNumberInput();' maxlength='5' value='");
				html.append(spider.getNumber());
				html.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(");' >");
				html.append("</div></td></tr><tr><td class='div_info_goods_td_img' >");
				/*if(spider.getFreight_free() == 1){
					html.append("<img src='/cbtconsole/img/free.png' class='spiderfreeimg'>");
				}*/
				html.append("<img class='div_info_goods_td_img_img'  alt='' src='");
				html.append(img == "" ? "/cbtconsole/img/1.png":img);
				html.append("'></td><td class='div_info_goods_td_size' ><div>");
				html.append(spider.getColor());
				html.append("&nbsp;&nbsp;");
				html.append(spider.getSize());
				html.append("</div><div style='display:");
				html.append(display);
				html.append("'><em style='display:none;'>");
				html.append(widths);
				html.append("</em> <em >");
				html.append(freights);
				html.append("</em>KG<input type='hidden' value='");
				html.append(spider.getWeight());
				html.append("'/></div></td><td class='div_info_goods_td_sprice'><div class='rmb_smart'  id='goods_id");
				html.append(res);
				html.append("'>$<em>");
				html.append(sprice_);
				html.append("</em><input type='hidden' value='"+spider.getGoods_class()+"'/> </div></td></tr>");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sum_w", sum_w);
		map.put("sum_v", sum_v);
		map.put("spider", html.toString());
		map.put("carNumber", list.size());
		map.put("email", email);
		html = null;
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		if(cookie != null){
			map.put("express", cookie.getValue());
		}else{
			map.put("express", null);
		}
		out.print(net.sf.json.JSONArray.fromObject(map));
		map = null;
		list = null;
		out.flush();
		out.close();
	}
	
	public static String getSessionId(HttpServletRequest request,
			HttpServletResponse response){
		Cookie[] c =  request.getCookies();
		if(c !=null){
			for (Cookie cookie2 : c) {
				if(cookie2.getName().equals("sessionId")){
					return cookie2.getValue();
				}
			}
		}
		
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Cookie cookie=new Cookie("sessionId", sessionId);
		cookie.setMaxAge(3600*24*2);
		cookie.setPath("/");
		response.addCookie(cookie);
		return sessionId;
	}

	/**
	 * 添加商品到购物车
	 */
	public void addSpider(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		String userid_str = request.getParameter("userid");//app接口参数
		String itemID = request.getParameter("itemID");
		String shopID = request.getParameter("shopID");
		String price = request.getParameter("price");
		String number = request.getParameter("number");
		String size = request.getParameter("size");
		String color = request.getParameter("color");
		String freight = request.getParameter("freight");
		String img_url = request.getParameter("img_url");
		String name = request.getParameter("titles").replace("\"", "");
		String url = request.getParameter("url");
		String remark = request.getParameter("remark");
		String norm_most = request.getParameter("norm_most");
		String norm_least = request.getParameter("norm_least");
		String delivery_time = request.getParameter("delivery_time");
		String true_shipping = request.getParameter("true_shipping");
		String freight_free = request.getParameter("freight_free");//免运费
		String weight = request.getParameter("weight");//重量
		String sellUnits = request.getParameter("sellUnits");//
		String pGoodsUnit = request.getParameter("pGoodsUnit");//
		String width = request.getParameter("width");//体积
		String perWeight = request.getParameter("perWeight");//单个产品重量
		String shopping_company = request.getParameter("method");//免邮快递方式
		String sc_days = request.getParameter("time");//免邮快递方式所需时间
		String types = request.getParameter("types");//规格
		String feeprice = request.getParameter("feeprice");
		String currency = request.getParameter("currency");//货币单位
		String goods_class = request.getParameter("goods_class");//商品类别
		String img_type = request.getParameter("img_type");//商品类别图片
		String extra_freight = request.getParameter("extra_freight");//额外运费
		String source_url = request.getParameter("source_url");//来源网址,1->1688网址->feeprice免邮转非免邮价格减去该字段,否则计算
		String goods_catid = request.getParameter("goods_catid");//商品最小类别ID
		int freightFree = 0;
		if(freight_free!=null){
			if(freight_free.equals("1")){
				freightFree = 1;
			}
		}
		String price_combine = request.getParameter("price_combine");//非免邮价格
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		String idsString = "";
    	String countryid = "";
		if(cookie != null){
	    	idsString = cookie.getValue();
	    	String[] expresssString = idsString.split("@");
	    	countryid = expresssString[0];
    	}
		boolean isNum = price_combine.matches("^\\d+\\.\\d+$");
		if((countryid.equals("29") || countryid.equals("37")) && isNum){
			price = price_combine;
			freightFree = 0;
		}
		String wprice = request.getParameter("wprice");//批发价
		SpiderBean spider = new SpiderBean(0, 0, itemID, shopID, 0, name, url,
				"dd", price, Integer.parseInt(number), freight, img_url, color,
				size, remark, norm_most, norm_least, delivery_time,true_shipping);
		spider.setpWprice(wprice);
		spider.setFreight_free(freightFree);
		spider.setWidth(width);
		spider.setPerWeight(weight);
		spider.setSeilUnit(sellUnits);
		spider.setGoodsUnit(pGoodsUnit);
		spider.setWeight(perWeight);
		spider.setFree_shopping_company(shopping_company);
		spider.setFree_sc_days(sc_days);
		spider.setTypes(types);
		spider.setFeeprice(Utility.getStringIsNull(feeprice)?feeprice:"0");
		spider.setCurrency(currency);
		spider.setImg_type(img_type);
		spider.setExtra_freight(Utility.getIsDouble(extra_freight)?Double.parseDouble(extra_freight):0);
		spider.setGoods_class(Utility.getStringIsNull(goods_class)?Integer.parseInt(goods_class):0);
		spider.setSource_url(Utility.getIsInt(source_url)?Integer.parseInt(source_url):0);
		spider.setGoods_catid(goods_catid);
			HttpSession session = request.getSession();
			Object shopcar = session.getAttribute("shopcar");
			List<SpiderBean> list = (List<SpiderBean>) shopcar;
//			Object userInfo = session.getAttribute("userInfo");
			String[] userInfo = WebCookie.getUser(request);
			int userid = 0;
			String sessionId = "";
			if(userInfo != null){
				userid = Integer.parseInt(userInfo[0]);
			}else{
				sessionId = this.getSessionId(request, response);
			}
			spider.setUserId(userid);
			spider.setSessionId(sessionId);
			int cartnumber = 0;
			int up = 0;
			Map<String, Double> mms = Currency.getMaphl(request);
			int insert = 0;
			//获取用户中的货币
			String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
			}
			if(list != null && list.size() >0){
				for (int i = 0; i < list.size(); i++) {
					SpiderBean spdBean = list.get(i);
					if(Utility.getStringIsNull(shopping_company)&& shopping_company.equals(spdBean.getFree_shopping_company()) && spdBean.getFreight_free()==1){
						insert = i;
					}
					if(spdBean.getUrl().equals(url) && spdBean.getColor().equals(color) && spdBean.getSize().equals(size) && spdBean.getTypes().equals(types)){
						int number1 = spdBean.getNumber()+spider.getNumber();
						String price1 = "";
						boolean isPwprice = Utility.getStringIsNull(spider.getpWprice());
						if(isPwprice){
							double mm = mms.get(currency1)/mms.get("USD");
							double feeprice_cost = 0;
							if(spdBean.getFreight_free() == 0 && Utility.getStringIsNull(spider.getFeeprice())){
								feeprice_cost = Double.parseDouble(spdBean.getFeeprice());
							}
							price1 = Processes.getWPrice(spider.getpWprice(), number1,mm, feeprice_cost);
							spdBean.setPrice(price1);
						}else{
							double ma = mms.get(currency);
							double mm = mms.get(currency1)/ma;
							DecimalFormat df = new DecimalFormat("#0.##");
							price1 = df.format(Double.parseDouble(spdBean.getPrice())*mm);
						}
						String volume = ParseGoodsUrl.calculateVolume(number1, spider.getWidth(), spider.getSeilUnit(), spider.getGoodsUnit());
						String weight1 = ParseGoodsUrl.calculateWeight(number1,spider.getPerWeight(), spider.getSeilUnit(), spider.getGoodsUnit());
						spider.setBulk_volume(volume);
						spider.setTotal_weight(weight1);
						spdBean.setTotal_weight(weight1);
						spdBean.setBulk_volume(volume);
						spdBean.setNumber(number1);
						up = 1;
						GoodsAddThread goodsAddThread = new GoodsAddThread(1, number1, price1, weight1, volume, spdBean.getGuId(),userid , sessionId);
						goodsAddThread.start();
						break;
					}
				}
			}
			if(up == 0){
				
				int number1 = spider.getNumber();
				String price1 = spider.getPrice();
				boolean isPwprice = Utility.getStringIsNull(spider.getpWprice());
				if(isPwprice){
					double mm = mms.get(currency1)/mms.get("USD");
					price1 = Processes.getWPrice(spider.getpWprice(), number1,mm, 0);
				}else{
					double ma = mms.get(currency1);
					double mm = ma/mms.get(currency);
					DecimalFormat df = new DecimalFormat("#0.##");
					price1 = df.format(Double.parseDouble(spider.getPrice())*mm);
				}
				String volume = ParseGoodsUrl.calculateVolume(number1, spider.getWidth(), spider.getSeilUnit(), spider.getGoodsUnit());
				String weight1 = ParseGoodsUrl.calculateWeight(number1,spider.getPerWeight(), spider.getSeilUnit(), spider.getGoodsUnit());
				spider.setBulk_volume(volume);
				spider.setTotal_weight(weight1);
				spider.setPrice(price1);
				if(list == null){
					list = new ArrayList<SpiderBean>();
				}
				if(freightFree == 1){
					list.add(insert, spider);
				}else{
					list.add(spider);
				}
				String guId = UUID.randomUUID().toString().replaceAll("-", "");
				spider.setGuId(guId);
				GoodsAddThread goodsAddThread = new GoodsAddThread(spider, 0);
				goodsAddThread.start();
				cartnumber = cartnumber + 1;
				session.setAttribute("shopcar",list);
				session.setMaxInactiveInterval(3600*24);
//				map.put("spider", JSONArray.fromObject(spider).toString());
			}
			Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber");
			if(cartNumber != null){
				cartNumber.setValue((Integer.parseInt(cartNumber.getValue())+cartnumber)+"");
			}else {
				cartNumber=new Cookie("cartNumber", 1+"");
			}
			cartNumber.setMaxAge(3600*24*2);
			cartNumber.setPath("/");
			response.addCookie(cartNumber);
			getSpiderCar(request, response);
	}
	
	/**
	 * 批量优惠申请的商品到购物车
	 */
	public void addGoods_preferential(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		String userid_str = request.getParameter("userid");//app接口参数
		String itemID = request.getParameter("itemID");
		String shopID = request.getParameter("shopID");
		String price = request.getParameter("price");
		String number = request.getParameter("number");
		String size = request.getParameter("size");
		String color = request.getParameter("color");
		String freight = request.getParameter("freight");
		String img_url = request.getParameter("img_url");
		String name = request.getParameter("titles").replace("\"", "");
		String url = request.getParameter("url");
		String remark = request.getParameter("remark");
		String norm_most = request.getParameter("norm_most");
		String norm_least = request.getParameter("norm_least");
		String delivery_time = request.getParameter("delivery_time");
		String true_shipping = request.getParameter("true_shipping");
		String weight = request.getParameter("weight");//重量
		String sellUnits = request.getParameter("sellUnits");//
		String pGoodsUnit = request.getParameter("pGoodsUnit");//
		String width = request.getParameter("width");//体积
		String perWeight = request.getParameter("perWeight");//单个产品重量
		String shopping_company = request.getParameter("method");//免邮快递方式
		String sc_days = request.getParameter("time");//免邮快递方式所需时间
		String types = request.getParameter("types");//规格
		String feeprice = request.getParameter("feeprice");
		String pid = request.getParameter("pid");
		String discount = request.getParameter("discount");//折扣率
		String currency = request.getParameter("currency");//货币单位
//		String wprice = request.getParameter("wprice");//批发价
		String img_type = request.getParameter("img_type");//商品类别图片
		SpiderBean spider = new SpiderBean(0, 0, itemID, shopID, 0, name, url,
				"dd", price, Integer.parseInt(number), freight, img_url, color,
				size, remark, norm_most, norm_least, delivery_time,true_shipping);
		/*spider.setpWprice(wprice);*/
		spider.setFreight_free(0);
		spider.setWidth(width);
		spider.setPerWeight(weight);
		spider.setSeilUnit(sellUnits);
		spider.setGoodsUnit(pGoodsUnit);
		spider.setWeight(perWeight);
		spider.setFree_shopping_company(shopping_company);
		spider.setFree_sc_days(sc_days);
		spider.setTypes(types);
		spider.setFeeprice(feeprice);
		spider.setPreferential(1);
		if(Utility.getStringIsNull(discount)){
			spider.setDeposit_rate(Integer.parseInt(discount));
		}
		spider.setImg_type(img_type);
		HttpSession session = request.getSession();
		Object shopcar = session.getAttribute("shopcar");
		List<SpiderBean> list = (List<SpiderBean>) shopcar;
		String[] userInfo = WebCookie.getUser(request);
		int userid = 0;
		String sessionId = "";
		if(userInfo != null){
			userid = Integer.parseInt(userInfo[0]);
		}else{
			sessionId = this.getSessionId(request, response);
		}
		spider.setUserId(userid);
		spider.setSessionId(sessionId);
		spider.setCurrency(currency);
		int cartnumber = 0;
	 
		int number1 = spider.getNumber();
		String price1 = spider.getPrice();
		/*boolean isPwprice = Utility.getStringIsNull(spider.getpWprice());
		if(isPwprice){
			price1 = Goods.getPrice(spider.getpWprice(), number1,1);
		}*/
		String volume = ParseGoodsUrl.calculateVolume(number1, spider.getWidth(), spider.getSeilUnit(), spider.getGoodsUnit());
		String weight1 = ParseGoodsUrl.calculateWeight(number1,spider.getPerWeight(), spider.getSeilUnit(), spider.getGoodsUnit());
		spider.setBulk_volume(volume);
		spider.setTotal_weight(weight1);
		spider.setPrice(price1);
		if(list == null){
			list = new ArrayList<SpiderBean>();
		}
			list.add(spider);
		String guId = UUID.randomUUID().toString().replaceAll("-", "");
		spider.setGuId(guId);
		GoodsAddThread goodsAddThread = new GoodsAddThread(spider, 0, Integer.parseInt(pid));
		goodsAddThread.start();
		cartnumber = cartnumber + 1;
		session.setAttribute("shopcar",list);
		session.setMaxInactiveInterval(3600*24);
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber");
		if(cartNumber != null){
			cartNumber.setValue((Integer.parseInt(cartNumber.getValue())+cartnumber)+"");
		}else {
			cartNumber=new Cookie("cartNumber", 1+"");
		}
		cartNumber.setMaxAge(3600*24*2);
		cartNumber.setPath("/");
		response.addCookie(cartNumber);
		getSpiderCar(request, response);
	}
	
	
	//添加商品新建不同运输方式的行时调用，获取运输方式和运输费用
	private ShippingBean getExpress(int countryid, Float weight, Float volume, Float singleweightmax, String expressType){
		IZoneServer zs= new ZoneServer();
		List<ShippingBean> list = zs.getShippingList(countryid, weight, volume, singleweightmax, 16);
		ShippingBean sbBean = null;
		for (int i = 0; i < list.size(); i++) {
			sbBean = list.get(i);
			if(sbBean.getResult() == 0){list.remove(i);}
			if(expressType.equals(sbBean)){
				return sbBean;
			}
		}
		
		return list.get(0);
	}
	
	public void upExpress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//spider.jsp修改购物车数量
		String goodsIds = request.getParameter("goodsIds");
		int countryId = Integer.parseInt(request.getParameter("countryId"));
		float weight = Float.parseFloat(request.getParameter("weight"));
		float volume = Float.parseFloat(request.getParameter("volume"));
		float singleweightmax = Float.parseFloat(request.getParameter("singleweightmax"));
		String expressType = request.getParameter("expressType");
		//根据重量获取运输方式
		ShippingBean sBean = getExpress(countryId, weight, volume, singleweightmax, expressType);
		//改变运输方式同级下的其他商品的运输方式
		/*ISpiderServer spiderServer = new SpiderServer();
		spiderServer.upExpreeType(goodsIds, sBean.getName(), sBean.getDays(), countryId);*/
		PrintWriter out = response.getWriter();
		out.print(sBean.getName() + "&nbsp;" + sBean.getDays() + "&nbsp;" + "_" + sBean.getResult());
		out.flush();
		out.close();
	}
	
	/**
	 * 修改购物车的数量
	 */
	protected void spiderUpNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String carId = request.getParameter("carId");
		String guid = request.getParameter("guid");
		//String itemId = request.getParameter("itemId");
		String number = request.getParameter("number");
		String[] userinfo = WebCookie.getUser(request);
		String sessionId =  null;
		ISpiderServer is = new SpiderServer();
		Map<String, String> map = null;
		int userid = 0;
		if(userinfo != null){
			userid = Integer.parseInt(userinfo[0]);
		}else{
			sessionId = this.getSessionId(request, response);
		}
		HttpSession session = request.getSession();
		Object shopcar = session.getAttribute("shopcar");
		int res = 0;
		int number1 = 0;
		String freePrice = "0";//由免邮转非免邮的商品，显示原先免邮价格
		String freePriceToCostPrice = "0";//免邮转非免邮隐藏值，合并运输弹出提示
		if(shopcar != null){
			List<SpiderBean> list = (List<SpiderBean>) shopcar;
			Map<String, Double> mms = Currency.getMaphl(request);
			String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
			}
			double mm = mms.get(currency1)/mms.get("USD");
			for (int i = 0; i < list.size(); i++) {
				SpiderBean spdBean = list.get(i);
				if(spdBean.getGuId().equals(guid)){
					number1 = Integer.parseInt(number);
					String price = spdBean.getPrice();
					boolean isPwprice = Utility.getStringIsNull(spdBean.getpWprice());
					if(isPwprice){
						double feePrice_cost = 0;
						if(spdBean.getFreight_free() != 1){
							feePrice_cost = Utility.getIsDouble(spdBean.getFeeprice()) ? Double.parseDouble(spdBean.getFeeprice()) : 0;
						}
						price = Processes.getWPrice(spdBean.getpWprice(), number1,mm, feePrice_cost);
						freePrice = Processes.getWPrice(spdBean.getpWprice(), number1,mm, 0);
						spdBean.setFree_price(freePrice);
						if(Utility.getStringIsNull(spdBean.getFeeprice()) && !spdBean.getFeeprice().equals("0.00")){
							feePrice_cost = Utility.getIsDouble(spdBean.getFeeprice()) ? Double.parseDouble(spdBean.getFeeprice()) : 0;
							freePriceToCostPrice = Processes.getWPrice(spdBean.getpWprice(), number1,mm, feePrice_cost);
						}
					}
					DecimalFormat   fnum  =   new  DecimalFormat("##0.00");  
					String weightString = ParseGoodsUrl.calculateWeight(number1, spdBean.getPerWeight(), spdBean.getSeilUnit(), spdBean.getGoodsUnit());
					String volume = ParseGoodsUrl.calculateVolume(number1,spdBean.getWidth(), spdBean.getSeilUnit(), spdBean.getGoodsUnit());
					double weight_d = Double.parseDouble(weightString);
					weightString = fnum.format(weight_d);
					spdBean.setBulk_volume(volume);
					spdBean.setTotal_weight(weightString);
					spdBean.setNumber(number1);
					spdBean.setPrice(price);
					map = new HashMap<String, String>();
					map.put("price", price);
					map.put("number", number);
					map.put("totalweight", weightString);
					map.put("totalvalume", volume);
					map.put("freePrice", freePrice);
					map.put("freePriceToCostPrice", freePriceToCostPrice);
					res = 1;
					break;
				}
			}
		}
		int sid = 0;
		if(carId != null){
			sid = Integer.parseInt(carId);
		}
		if(res == 1){
			is.upGoogs_car(guid, sid, number1, userid, sessionId, map.get("price"), map.get("totalvalume"), map.get("totalweight"));
		}else{
			map = is.upGoogs_car(guid, sid, Integer.parseInt(number), userid, sessionId, Currency.getMaphl(request));
		}
		out.print(map == null? null : JSONArray.fromObject(map));
		out.flush();
		out.close();
	}
	
	//修改商品信息
	public void upGoodInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String goodsid = request.getParameter("goodsid");
		//String itemId = request.getParameter("itemId");
		/*String seller = request.getParameter("seller");*/
		String title = request.getParameter("title");
		/*String price = request.getParameter("price");*/
		String freight = request.getParameter("freight");
		String remark = request.getParameter("remark");
		/*UserBean user = (UserBean) session.getAttribute("userInfo");*/
		List<SpiderBean> map = new ArrayList<SpiderBean>();
		ISpiderServer is = new SpiderServer();
		String sessionId = this.getSessionId(request, response);
		if(goodsid !=null){
			is.upGoogs_car(Integer.parseInt(goodsid), title, freight, remark);
		}
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo != null){
			map = is.getGoogs_cars(null,Integer.parseInt(userinfo[0]),0);
		}else{
		    sessionId = this.getSessionId(request, response);
			map = is.getGoogs_cars(sessionId,0,0);
		}
		/*Cookie[] c =  request.getCookies();
		for (Cookie cookie2 : c) {
			if(cookie2.getName().equals("cartNumber")){
				cookie2.setValue(SpiderServer.cartNumber+"");
			}
		} */
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber") ;
		if(cartNumber != null){
			cartNumber.setValue(SpiderServer.cartNumber+"");
			cartNumber.setMaxAge(3600*24*2);
			cartNumber.setPath("/");
			response.addCookie(cartNumber);
		}
		
		out.print(net.sf.json.JSONArray.fromObject(map));
		out.flush();
		out.close();
	}
	
	/**
	 * 修改购物车的商品价格
	 */
	public  void upGoodsprice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String goodsid = request.getParameter("goodsid");
		String price = request.getParameter("price");
		String[] userinfo = WebCookie.getUser(request);
		String sessionId = this.getSessionId(request, response);
		ISpiderServer is = new SpiderServer();
		int userid = 0; 
		if(userinfo != null){
			userid = Integer.parseInt(userinfo[0]);
		}
		int res = is.upGoodsprice(Integer.parseInt(goodsid), price, sessionId, userid);
		out.print(res);
		out.flush();
		out.close();
	}
	
	//删除购物车商品
	public void delGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String goodsids = request.getParameter("goodsid");
		String guids = request.getParameter("guids");
		
		String[] goodsid = goodsids.split("@");
		String[] guid = guids.split("@");
		int res = is.delGoogs_car(goodsid,"");

		int goodsid_len = goodsid.length;
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		HttpSession session = request.getSession();
//		Object cookie = session.getAttribute("expressType");
		if(cookie != null){
			String id =  "";
			String val = cookie.getValue();
			for (int i = 0; i < goodsid_len; i++) {
				id = "+"+guid[i]+"+";
				if(val.indexOf(id) > -1){
					val = val.replace(id, "");
				}
			}
		}
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber") ;
		if(cartNumber != null){
			cartNumber.setValue((Integer.parseInt(cartNumber.getValue())-goodsid.length)+"");
			cartNumber.setMaxAge(3600*24*2);
			cartNumber.setPath("/");
			response.addCookie(cartNumber);
		}
		Object shopcar = session.getAttribute("shopcar");
		if(shopcar != null){
			List<SpiderBean> lists = (List<SpiderBean>) shopcar;
			int list_len = lists.size();
			for (int j = 0; j < goodsid_len; j++) {
				OK: 
				for (int i = 0; i < list_len; i++) {
					SpiderBean spdBean = lists.get(i);
					if(spdBean.getGuId().equals(guid[j])){
						lists.remove(i);
						 break OK ;  
					}
				}
			}
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//删除购物车商品spider.jsp
	public void delGoods_spider(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String goodsids = request.getParameter("goodsid");
		String guids = request.getParameter("guids");
		
		String[] goodsid = {goodsids};
		is.delGoogs_car(goodsid,guids);

		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		HttpSession session = request.getSession();
		if(cookie != null){
			String id =  "";
			String val = cookie.getValue();
			id = "+"+guids+"+";
			if(val.indexOf(id) > -1){
				val = val.replace(id, "");
			}
		}
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber") ;
		if(cartNumber != null){
			cartNumber.setValue((Integer.parseInt(cartNumber.getValue())-goodsid.length)+"");
			cartNumber.setMaxAge(3600*24*2);
			cartNumber.setPath("/");
			response.addCookie(cartNumber);
		}
		Object shopcar = session.getAttribute("shopcar");
		if(shopcar != null){
			List<SpiderBean> lists = (List<SpiderBean>) shopcar;
			for (int i = 0; i < lists.size(); i++) {
				SpiderBean spdBean = lists.get(i);
				if(spdBean.getGuId().equals(guids)){
					lists.remove(i);
				}
			}
		}
		getSpiderCar(request, response);
	}
	
	/**
	 * ylm
	 * 收藏商品
	 */
	public void addCollection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String url = request.getParameter("url");
		String titile = request.getParameter("titile");
		String img = request.getParameter("img");
		String price = request.getParameter("price");
		String moq = request.getParameter("seller");
		String[] userinfo = WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		if(userinfo != null){
			CollectionBean collection = new CollectionBean();
			collection.setImg(img);
			collection.setPrice(price);
			//商品连接加密
			if(!Pattern.compile("(&u0=)").matcher(url.substring(0,5)).find()){
				url = TypeUtils.encodeGoods(url);
			}
			collection.setUrl(url);
			collection.setUserid(Integer.parseInt(userinfo[0]));
			collection.setTitile(titile);
			collection.setMoq(moq);
			int res = is.addCollection(collection);
			out.print(res);
		}else{
			out.print(-1);
		}
		out.flush();
		out.close();
	}
	/**
	 * ylm
	 * 查询是否收藏商品
	 */
	public void getCollection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String url = request.getParameter("url");
		String[] userinfo = WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		if(userinfo != null){
			if(!Pattern.compile("(&u0=)").matcher(url.substring(0,5)).find()){
				url = TypeUtils.encodeGoods(url);
			}
			int res = is.getCollection(url, Integer.parseInt(userinfo[0]));
			out.print(res);
		}else{
			out.print(-1);
		}
		out.flush();
		out.close();
	}
	/**
	 * ylm
	 * 删除收藏商品
	 */
	public void delCollection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String id = request.getParameter("id");
		id = id.substring(0, id.length()-1);
		String[] userinfo = WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		if(userinfo != null){
			int res = is.deleteCollection(id,Integer.parseInt(userinfo[0]));
			out.print(res);
		}else{
			out.print(-1);
		}
		out.flush();
		out.close();
	}
	
	public void delCollectionByUrl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String url = request.getParameter("url");
		String[] userinfo = WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		if(userinfo != null){
			if(url!=null&&!Pattern.compile("(&u0=)").matcher(url.substring(0, 5)).find()){
				url = TypeUtils.encodeGoods(url);
			}
			int res = is.delCollectionByUrl(url,Integer.parseInt(userinfo[0]));
			out.print(res);
		}else{
			out.print(-1);
		}
		out.flush();
		out.close();
	}
	
	/**
	 * ylm
	 * 获取用户收藏的商品
	 */
	public void getCollections(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ISpiderServer is = new SpiderServer();
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo != null){
			List<CollectionBean> collections = is.getCollection(Integer.parseInt(userinfo[0]));
			request.setAttribute("collections", collections);
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/collection.jsp");
			homeDispatcher.forward(request, response);
		}else{
	    	response.sendRedirect(AppConfig.ip+"Geton");
		}
	}
	
	/**
	 * ylm
	 * 保存快递方式和国家
	 */
	public void saveExpress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String country = request.getParameter("country");
		String express = request.getParameter("express");
		String ids = request.getParameter("ids");
		if(ids == null)ids = "";
		if(ids.equals("@")){ids = "";}else{
			ids = ids.replaceAll("@", "+");
		}
		String shipping_cost = request.getParameter("shipping_cost");
		HttpSession session = request.getSession();
		if(shipping_cost != null){
			String countryname = request.getParameter("countryname");
			String days = request.getParameter("days");
			Cookie cookie = WebCookie.getCookieByName(request, "expressType");
//			Object cookie = session.getAttribute("expressType");
			String coString = country+"@"+express+"@"+days+"@"+countryname+"@"+shipping_cost+"@";
			if(cookie != null){
				cookie.setValue(coString);
				cookie.setMaxAge(3600*24);
				cookie.setPath("/");
				response.addCookie(cookie);
			}else{
				cookie  = new Cookie("expressType", coString);
				cookie.setMaxAge(3600*24);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		Object shopcar = session.getAttribute("shopcar");
		//重新排序
		String guidPrice = "";
		if(shopcar != null && ids.indexOf("+") > -1){
			Map<String, Double> mms = Currency.getMaphl(request);
			List<SpiderBean> lists = (List<SpiderBean>) shopcar;
			String[] goodsid = ids.replace("++", "+").split("\\+");
			int goodsid_len = goodsid.length;
			int list_len = lists.size();
			List<SpiderBean> lists_up = new ArrayList<SpiderBean>();
			
			for (int j = 1; j < goodsid_len; j++) {
				OK:
				for (int i = 0; i < list_len; i++) {
					SpiderBean spdBean = lists.get(i);
					if(spdBean.getGuId().equals(goodsid[j])){
						String price = spdBean.getPrice();
						if(spdBean.getFreight_free() == 1){
							if(Utility.getStringIsNull(price) && Utility.getStringIsNull(spdBean.getFeeprice())){
								spdBean.setFree_price(price);
								double ex = mms.get(spdBean.getCurrency())/mms.get("USD");
								price = Processes.getFreeToFreight(price, ex, spdBean.getFeeprice(), spdBean.getSource_url(), spdBean.getpWprice(), spdBean.getNumber());
								guidPrice += spdBean.getGuId()+":"+price+"@"+spdBean.getPrice()+",";
							}
						}
						spdBean.setFreight_free(0);
						spdBean.setPrice(price);
						lists_up.add(spdBean);
						lists.add(list_len, spdBean);
						lists.remove(i);
						break OK ;
					}
				}
			}
			ISpiderServer server = new SpiderServer();
			server.upGoogs_carFree(lists_up);
		}
		PrintWriter out = response.getWriter();
		out.print(guidPrice);
		out.flush();
		out.close();
	}
	
	//保存用户申请邮费折扣
	public void saveGoodsemail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String pd_name = request.getParameter("pd_name");
		String pd_phone = request.getParameter("pd_phone");
		String pd_coi = request.getParameter("pd_coi");
		String countryid = request.getParameter("countryid");
		String shopping_price = request.getParameter("shopping_price");
		String shopping_company = request.getParameter("shopping_company");
		String weight = request.getParameter("weight");
		
		String[] userinfo = WebCookie.getUser(request);
		IPreferentialServer server = new PreferentialServer();
		PostageDiscounts pd = new PostageDiscounts();
		pd.setEmail(email);
		pd.setCoi(Integer.parseInt(pd_coi));
		pd.setCountryid(Utility.getStringIsNull(countryid)?Integer.parseInt(countryid):0);
		pd.setIp(Utility.getIpAddress(request));
		pd.setName(pd_name);
		pd.setPhone(pd_phone);
		String sessionid = null;
		int userid = 0;
		if(userinfo == null){
			sessionid = WebCookie.cookieValue(request, "sessionId");
//			res = is.saveGoodsEmail(0, sessionId, email);
		}else{
			userid = Integer.parseInt(userinfo[0]);
//			res = is.saveGoodsEmail(Integer.parseInt(userinfo[0]), null, email);
		}
		pd.setSessionid(sessionid);
		pd.setUserid(userid);
		pd.setShopping_company(shopping_company);
		pd.setShopping_price(shopping_price);
		pd.setWeight(weight);
		int res = server.savePostageD(pd);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//保存用户申请邮费折扣
	public void upGoodsemail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String pd_name = request.getParameter("pd_name");
		String pd_phone = request.getParameter("pd_phone");
		String pd_coi = request.getParameter("pd_coi");
		String countryid = request.getParameter("countryid");
		String shopping_price = request.getParameter("shopping_price");
		String shopping_company = request.getParameter("shopping_company");
		String pdid = request.getParameter("pdid");
		
		String[] userinfo = WebCookie.getUser(request);
		IPreferentialServer server = new PreferentialServer();
		PostageDiscounts pd = new PostageDiscounts();
		pd.setEmail(email);
		pd.setCoi(Integer.parseInt(pd_coi));
		pd.setCountryid(Utility.getStringIsNull(countryid)?Integer.parseInt(countryid):0);
		pd.setIp(Utility.getIpAddress(request));
		pd.setName(pd_name);
		pd.setPhone(pd_phone);
		pd.setId(Integer.parseInt(pdid));
		String sessionid = null;
		int userid = 0;
		if(userinfo == null){
			sessionid = WebCookie.cookieValue(request, "sessionId");
//				res = is.saveGoodsEmail(0, sessionId, email);
		}else{
			userid = Integer.parseInt(userinfo[0]);
//				res = is.saveGoodsEmail(Integer.parseInt(userinfo[0]), null, email);
		}
		pd.setSessionid(sessionid);
		pd.setUserid(userid);
		pd.setShopping_company(shopping_company);
		pd.setShopping_price(shopping_price);
		int res = server.savePostageD(pd);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//获取用户申请邮费折扣
	public void getPostageDiscounts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
		IPreferentialServer server = new PreferentialServer();
		String sessionid = null;
		int userid = 0;
		if(userinfo == null){
			sessionid = WebCookie.cookieValue(request, "sessionId");
//				res = is.saveGoodsEmail(0, sessionId, email);
		}else{
			userid = Integer.parseInt(userinfo[0]);
//				res = is.saveGoodsEmail(Integer.parseInt(userinfo[0]), null, email);
		}
		PostageDiscounts pd = server.getPostageD(userid,sessionid);
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(pd));
		out.flush();
		out.close();
	}
	//获取用户运费津贴
	public void getUserAC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
		IUserServer userServer = new UserServer();
		double res = 0;
		if(userinfo != null){
			res = userServer.getUserApplicableCredit(Integer.parseInt(userinfo[0]));
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
}
