package com.cbt.website.servlet;

import com.cbt.bean.*;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.pay.dao.IOrderDao;
import com.cbt.pay.dao.OrderDao;
import com.cbt.pay.service.IOrderServer;
import com.cbt.pay.service.OrderServer;
import com.cbt.processes.service.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
		List<SpiderBean> list = new ArrayList<SpiderBean>();
		ISpiderServer is = new SpiderServer();
		Map<String, Object> map = new HashMap<String, Object>();
		String userName = "";
		if(userid_str != null){
			int userid = 0;
			userid = Integer.parseInt(userid_str);
			list = is.getGoogs_cars(null,userid,0);
			
		}else{
			String sessionId = null;
			String[] userinfo = WebCookie.getUser(request);
			if(userinfo != null){
				list = is.getGoogs_cars(null,Integer.parseInt(userinfo[0]),0);
				userName = userinfo[1];
	        	request.setAttribute("username", userName);
	        	if(userinfo.length == 3){
	        		request.setAttribute("email", userinfo[2]);
	        	}
			}else{
			    sessionId = this.getSessionId(request, response);
			    list = is.getGoogs_cars(sessionId,0,0);
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
		//免邮商品
		List<SpiderBean> listF = new ArrayList<SpiderBean>();
		//正常商品
		List<SpiderBean> listP = new ArrayList<SpiderBean>();
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		String express = "";
		if(cookie != null){
			express = cookie.getValue();
			map.put("express", express);
		}
		List<ClassDiscount> list_cd = new ArrayList<ClassDiscount>();
		//获取混批折扣率
		list_cd = Application.getClassDiscount(request);
		for (int i = 0; i < list.size(); i++) {
			SpiderBean spider = list.get(i);
			if(spider.getNorm_least() != null){
				list.get(i).setNorm_least1(spider.getNorm_least().split(" ")[0]);
			}
			String id = "+"+spider.getId()+"+";
			if(spider.getPrice() == null || spider.getPrice().equals("0") || spider.getPrice().equals("0.0")){
				listU.add(spider);
			}else if(spider.getFreight_free() == 1 && express.indexOf(id) < 0){
				listF.add(spider);
			}else{
				if(spider.getWidth() != "" && spider.getWidth() != null){
					sum_v = sum_v + Float.parseFloat(spider.getBulk_volume());
					sum_w = sum_w + Float.parseFloat(spider.getTotal_weight());
				}
				listP.add(spider);
			}
		}
		map.put("sum_w", sum_w);
		map.put("sum_v", sum_v);
		
		map.put("spider", listP);
		map.put("spider_un", listU);
		map.put("spider_ff", listF);
		request.setAttribute("goods_car", map);
		request.setAttribute("classDiscount", list_cd);
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
	    	
			for (int i = 0; i < list.size(); i++) {
				SpiderBean spider = list.get(i);
				int preferential = spider.getPreferential();//是否是优惠申请的
				email = spider.getGoods_email();
				float  weight = Float.parseFloat(spider.getTotal_weight());float widths = Float.parseFloat(spider.getBulk_volume());
				
				String price = spider.getPrice();
				if(!Utility.getStringIsNull(price)){
					price = "0";
				}
				float  sprice = spider.getNumber() * Float.parseFloat(price);
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
		    	float norm_least_f = 0;
		    	if(Utility.getStringIsNull(spider.getNorm_least())){
		    		norm_least_f = Float.parseFloat(spider.getNorm_least().split(" ")[0]);
		    	}
		    	if(idsString.indexOf("+"+res+"+") > -1){
		    		html2.append("<tr  title='");
					html2.append(name);
					html2.append("' class='div_info_goods_tr'><td class='div_info_goods_td' colspan='3'><div  class='spider_cart_div_title'>&nbsp;");
					html2.append(name);
					html2.append("</div></td></tr><tr><td class='div_info_goods_td_img' rowspan='");
					html2.append(3);
					html2.append("'><img class='div_info_goods_td_img_img' alt='' src='");
					html2.append(Utility.getStringIsNull(img) ? img : "/cbtconsole/img/1.png");
					html2.append("'></td><td class='div_info_goods_td_price'><img src='img/w.jpg'/><div>");
					html2.append("<input type='text' class='cj cji' readonly='readonly' onclick='fnUpNumber_car(this,1,");
					html2.append(res);
					html2.append(",");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(")'/><input id='goods_car_number");
					html2.append(res);
					html2.append("' onBlur='fnUpNumber_car(this,0,");
					html2.append(res);
					html2.append(",");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(")'onkeydown='fnNumberInput();' maxlength='5' value='");
					html2.append(spider.getNumber());
					html2.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
					html2.append(res);
					html2.append(",");
					html2.append(spider.getPreferential());
					html2.append(",");
					html2.append(fnum.format(norm_least_f));
					html2.append(");' >");
					html2.append("</div></td><td class='td_info_goods_td_price1' ><div class='div_info_goods_td_price1'>");
					html2.append(price_);
					html2.append("</div><td></tr><tr><td class='div_info_goods_td_size' >");
					html2.append(spider.getColor());
					html2.append("</td><td>");
					html2.append(spider.getSize());
					html2.append("</td></tr><tr><td class='div_info_goods_td_kg'><div style='display:block'><em style='display:none;'>");
					html2.append(widths);
					html2.append("</em> <em >");
					html2.append(weight);
					html2.append("</em>KG<input type='hidden' value='");
					html2.append(spider.getWeight());
					html2.append("'/></div></td><td class='div_info_goods_td_sprice'><div class='rmb_smart'  id='goods_id");
					html2.append(res);
					html2.append("'>$<em>");
					html2.append(sprice_);
					html2.append("</em> </div></td></tr>");
					sum_v2 = sum_v2 + widths;
					sum_w2 = sum_w2 + weight;
					float ww = Float.parseFloat((spider.getWeight()));
					if(max_w2 == 0){
						max_w2 = ww;
					}else if(ww > max_w){
						max_w2 = ww;
					}
					sum_ww += weight;
					continue;
		    	}
				if(spider.getFreight_free() == 1){
					if(i == 0){
						j = 3;
						html.append("<tr class='spidershopcartr' id='");
						html.append(spider.getFree_shopping_company().replaceAll(" ", "_"));
						html.append("'><td colspan='3'  class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree'>Free Shipping:</em>by <em>");
						html.append(spider.getFree_shopping_company());
						html.append("</em>&nbsp;<em>");
						html.append(spider.getFree_sc_days());
						html.append("</em>&nbsp;Days&nbsp;<a  class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'>▼</em></a></div></td>");
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
						html.append("'><td colspan='3'  class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree'>Free Shipping:</em>by <em>");
						html.append(spider.getFree_shopping_company());
						html.append("</em>&nbsp;<em>");
						html.append(spider.getFree_sc_days());
						html.append("</em>&nbsp;Days&nbsp;<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'>▼</em></a></div></td>");
						shopping_companyString = spider.getFree_shopping_company();
						sum_w = 0; sum_v = 0; max_w=0;
	    			}
		    	}else{
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
	    				html.append("<tr class='spidershopcartr1'  id='spidershopcar1'><td colspan='3' class='spidershopcarfree2'><div class='spidscarfrdiv'><input type='hidden' value=''><em class='spidershopcarfree1'>Shipping:</em>by <em id='show_express'>");
						html.append(express);
						html.append("</em>&nbsp;<em>");
						html.append(day);
						html.append("</em>&nbsp;Days&nbsp;<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'>▼</em></a></div></td>");
						shopping_companyString = spider.getFree_shopping_company();
						sum_w = 0; sum_v = 0; max_w=0;
	    			}
		    	}
				html.append("<tr  title='");
				html.append(name);
				html.append("' class='div_info_goods_tr'><td class='div_info_goods_td' colspan='3'><div  class='spider_cart_div_title'>&nbsp;");
				html.append(name);
				html.append("</div></td></tr><tr><td class='div_info_goods_td_img' rowspan='");
				html.append(3);
				html.append("'>");
				if(spider.getFreight_free() == 1){
					html.append("<img src='/cbtconsole/img/free.png' class='spiderfreeimg'>");
				}
				html.append("<img class='div_info_goods_td_img_img' alt='' src='");
				html.append(Utility.getStringIsNull(img) ? img : "/cbtconsole/img/1.png");
				html.append("'></td><td class='div_info_goods_td_price'><img src='img/w.jpg'/><div>");
				html.append("<input type='text' class='cj cji' readonly='readonly' onclick='fnUpNumber_car(this,1,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'/><input id='goods_car_number");
				html.append(res);
				html.append("' onBlur='fnUpNumber_car(this,0,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'onkeydown='fnNumberInput();' maxlength='5' value='");
				html.append(spider.getNumber());
				html.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(");' >");
				html.append("</div></td><td class='td_info_goods_td_price1' ><div class='div_info_goods_td_price1'>");
				html.append(price_);
				html.append("</div><td></tr><tr><td class='div_info_goods_td_size' >");
				html.append(spider.getColor());
				html.append("</td><td>");
				html.append(spider.getSize());
				html.append("</td></tr><tr><td class='div_info_goods_td_kg'><div style='display:block'><em style='display:none;'>");
				html.append(widths);
				html.append("</em> <em >");
				html.append(weight);
				html.append("</em>KG<input type='hidden' value='");
				html.append(spider.getWeight());
				html.append("'/></div></td><td class='div_info_goods_td_sprice'><div class='rmb_smart'  id='goods_id");
				html.append(res);
				html.append("'>$<em>");
				html.append(sprice_);
				html.append("</em> </div></td></tr>");
				sum_v = sum_v + widths;
				sum_w = sum_w + weight;
				float ww = 0;
		    	if(Utility.getStringIsNull(spider.getWeight())){
		    		ww = Float.parseFloat(spider.getWeight());
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
				html.append("<tr class='spidershopcartr1'  id='spidershopcar1'><td colspan='3' class='spidershopcarfree2'><div style='width: 273px;'><input type='hidden' value='" + sum_w2 + "_" + sum_v2 + "_" + max_w2 + "'><em class='spidershopcarfree1'>Shipping:</em>by <em id='show_express'>");
				html.append(express);
				html.append("</em>&nbsp;<em>");
				html.append(day);
				html.append("</em>&nbsp;Days&nbsp;<a class='change_expree' onclick='fnGetFreight(this,0)'><em style='color: #00B1FF;cursor: pointer;color: #00B1FF;font-size: 15px'>▼</em></a></div></td>");
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
		map.put("sum_w", sum_w);
		map.put("sum_v", sum_v);
		map.put("spider", html.toString());
		map.put("carNumber", list.size());
		map.put("email", email);
		map.put("countryId", countryId == "" ? "36" : countryId );
		map.put("sum_ww", sum_ww);
		html = null;
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		if(cookie != null){
			map.put("express", cookie.getValue());
		}else{
			map.put("express", null);
		}
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
				html.append("<div></td><td class='div_info_goods_td_price'><img src='img/w.jpg'/><div>");
				html.append("<input type='text' class='cj cji' readonly='readonly' onclick='fnUpNumber_car(this,1,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'/><input id='goods_car_number");
				html.append(res);
				html.append("' onBlur='fnUpNumber_car(this,0,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(")'onkeydown='fnNumberInput();' maxlength='5' value='");
				html.append(spider.getNumber());
				html.append("' class='tb_text1'/><input type='text' class='cj' readonly='readonly' onclick='fnUpNumber_car(this,2,");
				html.append(res);
				html.append(",");
				html.append(spider.getPreferential());
				html.append(",");
				html.append(fnum.format(norm_least_f));
				html.append(");' >");
				html.append("</div></td></tr><tr><td class='div_info_goods_td_img' >");
				if(spider.getFreight_free() == 1){
					html.append("<img src='/cbtconsole/img/free.png' class='spiderfreeimg'>");
				}
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
				html.append("</em> </div></td></tr>");
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
		String userid_str = request.getParameter("userid");//app接口参数
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
		String freeShopping = request.getParameter("freeShopping");//是否增加行
		String types = request.getParameter("types");//规格
		String orderid=request.getParameter("orderid");
//		String sum_vv = request.getParameter("sum_vv");//购物车总体积
//		String sum_ww = request.getParameter("sum_ww");//购物车总重量
		int countryId = Integer.parseInt(request.getParameter("countryId"));//国家
		
		int freightFree = 0;
		if(freight_free!=null){
			if(freight_free.equals("1")){
				freightFree = 1;
			}
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
		float shopping_cost = 0;
//		spider.setCountryid(countryId);
		ISpiderServer iss = new SpiderServer();
		Map<String, String> map = new HashMap<String, String>();
		int userid=0;
		if(userid_str != null){
			spider.setUserId(Integer.parseInt(userid_str));
//			map = iss.addGoogs_car(spider);
		}else{
			String[] user =  WebCookie.getUser(request);
			if (user != null) {
				userid = Integer.parseInt(user[0]);
				spider.setUserId(userid);
//				map = iss.addGoogs_car(spider);
			}else {
				String sessionId = this.getSessionId(request, response);
				spider.setSessionId(sessionId);
//				map = iss.addGoogs_car(spider);
			}
		}
		IOrderServer os = new OrderServer();
		IOrderDao io =new OrderDao();
		os.updateGoodscarState(userid, map.get("res"));	
		List<Address> addresslist = new ArrayList<Address>();
		List<OrderDetailsBean> odb= new ArrayList<OrderDetailsBean>();
		OrderDetailsBean temp= new OrderDetailsBean();
		int addressid=0;
		if(userid!=0){
			addresslist=os.getUserAddr(userid);
			LOG.warn("address list count:"+addresslist.size());
			if(addresslist.size()>0&&addresslist!=null){
				addressid=addresslist.get(0).getId();
			}
		}
		
		temp.setGoodsprice(price);
		temp.setYourorder(Integer.parseInt(number));
		temp.setDelivery_time(delivery_time);
		temp.setGoodsid(Integer.parseInt(map.get("res")));
		temp.setUserid(userid);
		temp.setGoodsname(name);
		temp.setOrderid(orderid);
		temp.setCheckprice_fee(50);//询价费用写死
		temp.setCheckproduct_fee(100);
		temp.setState(0);//订单详细信息状态
		temp.setFileupload("/temp/pic");
		temp.setFreight("0");
		temp.setGoods_img(img_url);
		temp.setGoods_url(url);
		temp.setGoods_type(types);
		odb.add(temp);
		os.add(odb);
		
		float product_cost=Float.parseFloat(io.getOrderProductcost(orderid));
		float service_fee=0;
		DecimalFormat df = new DecimalFormat(".00");
		if(product_cost<1000){
			service_fee=(float) (product_cost*0.08);
		}else if(product_cost<3000){
			service_fee=(float) (product_cost*0.06);
		}else if(product_cost<5000){
			service_fee=(float) (product_cost*0.04);
		}else if(product_cost<10000){
			service_fee=(float) (product_cost*0.03);
		}else{
			service_fee=(float) (product_cost*0.025);
		}
		io.updateOrderinfo(product_cost+"", service_fee+"", orderid);
	}
	
	//添加商品新建不同运输方式的行时调用，获取运输方式和运输费用
	private ShippingBean getExpress(int countryid,Float weight,Float volume,Float singleweightmax,String expressType){
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
	
	//选择国家显示不同运输方式
	public void getExpress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		//修改
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
	//价格根据数据改变
	public static String getPrice(String wprice, int number){
		//wprice = "[1 - 2 $135.87, 3 - 4 $130.9, 5 - 19 $115.19, 20 + $108.91]";
		String temp = wprice.replace("[", "").replace("]", "").replaceAll(" ", "");
		String[] wp = temp.split(",");
		String priceString = "0.0";
		try {
			for (int i = 0; i < wp.length; i++) {
				String qj = wp[i].split("\\$")[0];
				int min = 1;int max = 1;
				if(qj.indexOf("-") > -1){
					min = Integer.parseInt(qj.split("-")[0]);
				    max = Integer.parseInt(qj.split("-")[1]);
				}else{
					String regEx="[^0-9]";
					Pattern p = Pattern.compile(regEx);   
					Matcher m = p.matcher(qj);   
					min = Integer.parseInt(m.replaceAll("").trim());  
					max = min;
				}
				if(min > number){
					return wp[i].split("\\$")[1];
				}
				if(min <= number && max >= number){
					return wp[i].split("\\$")[1];
				}
			}
			priceString =  wp[wp.length-1].split("\\$")[1];
		} catch (Exception e) {
			LOG.debug("",e);
			e.printStackTrace();
		}
		return priceString;
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
		//String itemId = request.getParameter("itemId");
		String number = request.getParameter("number");
		/*String country = request.getParameter("country");
		String sum_vv = request.getParameter("sum_vv");//除去该商品的购物车总体积
		String sum_ww = request.getParameter("sum_ww");//除去该商品的购物车总重量
*/		String[] userinfo = WebCookie.getUser(request);
		String sessionId =  null;
		ISpiderServer is = new SpiderServer();
		Map<String, String> map = new HashMap<String, String>();
		/*if(userinfo != null){
			int userid = Integer.parseInt(userinfo[0]);
			map = is.upGoogs_car(Integer.parseInt(carId), Integer.parseInt(number), userid);
		}else{
			sessionId = this.getSessionId(request, response);
			map = is.upGoogs_car(Integer.parseInt(carId), Integer.parseInt(number), sessionId);
		}*/
		/*float shipping_cost = 0;
		float sum_w = Float.parseFloat(map.get("totalweight")) + Float.parseFloat(sum_ww);
		float sum_v = Float.parseFloat(map.get("totalvalume")) + Float.parseFloat(sum_vv);*/
		/*String key = "fedexie";
		if(sum_v != 0 && sum_w != 0 ){
			IZoneServer zs= new ZoneServer();
			int type = 0;
			if(sum_w <= 2){
				type = 2;key = "epacket";
			}else if(sum_w > 2 && sum_w <= 15){
				type = 4;key = "chinapostsal";
			}else if(sum_w > 15 && sum_w <= 67){
				type = 1;key = "fedexie";
			}else{
				type = 3;key = "dhlfba";
			}
			shipping_cost = zs.getCost(Integer.parseInt(country), sum_w, type, sum_v);
		}
		map.put("shipping_cost", shipping_cost+"");
		map.put("express_type", key);*/
		out.print(JSONArray.fromObject(map));
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
		
		String[] goodsid = goodsids.split("@");
		int res = is.delGoogs_car(goodsid,"");

		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		if(cookie != null){
			String id =  "";
			String val = cookie.getValue();
			for (int i = 0; i < goodsid.length; i++) {
				id = "+"+goodsid[i]+"+";
				if(val.indexOf(id) > -1){
					val = val.replace(id, "");
				}
			}
			cookie.setValue(val);
			cookie.setMaxAge(3600*24);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber") ;
		if(cartNumber != null){
			cartNumber.setValue((Integer.parseInt(cartNumber.getValue())-goodsid.length)+"");
			cartNumber.setMaxAge(3600*24*2);
			cartNumber.setPath("/");
			response.addCookie(cartNumber);
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
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
		String days = request.getParameter("days");
		Cookie cookie = WebCookie.getCookieByName(request, "expressType");
		String coString = country+"@"+express+"@"+days+"@"+shipping_cost+"@";
		if(cookie != null){
			String val = cookie.getValue();
			String id =  "";
			if(val.indexOf("+") > -1){
				id = val.substring(val.indexOf("+"),val.length());
			}
			cookie.setValue(coString+id+ids);
			cookie.setMaxAge(3600*24);
			cookie.setPath("/");
			response.addCookie(cookie);
		}else{
			Cookie cookie1=new Cookie("expressType", coString+ids);
			cookie1.setMaxAge(3600*24);
			cookie1.setPath("/");
			response.addCookie(cookie1);
		}
	}
	public static void main(String[] args) {
		String val = " Pickup(Melbourne)@23 Days@305@+1651++1637+";
		String id= "+"+1651+"+";
		System.err.println(val.indexOf("+")+","+val.length());
		System.err.println(val.substring(val.indexOf("+"),val.length()));
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
