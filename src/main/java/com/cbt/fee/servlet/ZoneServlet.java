package com.cbt.fee.servlet;

import com.cbt.bean.ShippingBean;
import com.cbt.bean.SpiderBean;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.MisceServerImpl;
import com.cbt.fee.service.ZoneServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.util.Redis;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZoneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(MisceServerImpl.class);
	
	//获取zone分区表的数据
	public void getAllZone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		IZoneServer os = new ZoneServer();
		PrintWriter out = response.getWriter();
		out.print(os.getAllZone());
	}
	
	//获取州名的数据
	public void getStateName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		IZoneServer os = new ZoneServer();
		out.print(os.getStateName());
	}
	//根据重量和zone计算运费
	public void Estimate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		double weight=Double.parseDouble(request.getParameter("weight"));
		int zone=Integer.parseInt(request.getParameter("value"));
		double ecost=0.0,acost=0.0,scost=0.0;
		double ecost_a=0.0,ecost_b=0.0,ecost_c=0.0;//快递费用计算系数A,B,C
		double acost_a=0.0;//空用费用计算系数A
		double scost_a=0.0;//海运费用计算系数A
		double temp_1=800;
		double temp_2=600;
		switch(zone){
		case 3:
			ecost_a=101.2;ecost_b=26.8;ecost_c=22.6;
			acost_a=16.0;
			scost_a=0.0;
			break;
		case 5:
			ecost_a=114.8;ecost_b=36.0;ecost_c=31.8;
			acost_a=29.0;
			scost_a=0.0;
			break;
		case 6:
			ecost_a=117.2;ecost_b=38.8;ecost_c=33.4;
			acost_a=29.0;
			scost_a=0.0;
			break;
		case 7:
			ecost_a=150.8;ecost_b=39.6;ecost_c=36.8;
			acost_a=29.0;
			scost_a=0.0;
			break;
		case 8:
			ecost_a=192.4;ecost_b=50.8;ecost_c=43.8;
			acost_a=33.0;
			scost_a=30.0;
			break;
		case 9:
			ecost_a=289.2;ecost_b=64.8;ecost_c=59.4;
			acost_a=37.0;
			scost_a=40.0;
			break;
		}
		if(weight<=30){
			ecost=(ecost_a+(weight*2-1)*ecost_b)/6.2*1.05;
			out.print(ecost_a/6.2*1.05+"?"+(weight*2-1)+"?"+ecost_b/6.2*1.05+":"+acost+":"+scost);
		}
		else if(weight>30&&weight<=50){
			ecost=(ecost_a+(weight*2-1)*ecost_c)/6.2*1.05;
			out.print(ecost_a/6.2*1.05+"?"+(weight*2-1)+"?"+ecost_c/6.2*1.05+":"+acost+":"+scost);
		}
		else if(weight>50&&weight<=300){
			ecost=(ecost_a+(weight*2-1)*ecost_c)/6.2*1.05;
			acost=(temp_1+weight*acost_a/6.2)*1.05;
			scost=temp_2+weight*scost_a/1000;
			String s1=ecost_a/6.2*1.05+"?"+(weight*2-1)+"?"+ecost_c/6.2*1.05+":";
			String s2=temp_1 + "?" + weight+"?"+acost_a/6.2 *1.05+":";
			String s3=temp_2 + "?" + weight/1000+"?"+scost_a;
			out.print(s1+s2+s3);
		}
		else if(weight>300){
			acost=(temp_1+weight*acost_a/6.2)*1.05;
			scost=temp_2+weight*scost_a/1000;
			String s1 = temp_1 + "?" + weight+"?"+acost_a/6.2 *1.05+":";
			String s2 = temp_2 + "?" + weight/1000+"?"+scost_a;
			out.print(ecost+":"+s1+s2);
		}
	
		
	}
	
	/*获取不同国家在不同运输方式下的运费*/
	public void getCost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		int countryid=Integer.parseInt(request.getParameter("countryid"));
		int count=16;//运费方式数量
		float weight=Float.parseFloat(request.getParameter("weight"));
		float volume=Float.parseFloat(request.getParameter("volume"));
		LOG.warn("singleweightmax:"+request.getParameter("singleweightmax"));
		String singleweightmax_ = request.getParameter("singleweightmax");
		String currency = request.getParameter("currency");
		float singleweightmax = 0 ;
		if(Utility.getIsDouble(singleweightmax_)){
			singleweightmax = Float.parseFloat(request.getParameter("singleweightmax"));
		}
		IZoneServer zs= new ZoneServer();
		List<ShippingBean> list=zs.getShippingList(countryid, weight, volume, singleweightmax, count);
		//获取用户中的货币
		if(!Utility.getStringIsNull(currency)){
			currency = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency)){
				currency = "USD";
			}
		}
		//获取汇率
		Map<String, Double> maphl = Currency.getMaphl(request);
		for(int i=0;i<list.size();i++){
			BigDecimal   b   =   new   BigDecimal(list.get(i).getResult()*maphl.get(currency));
			float   f   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
			list.get(i).setResult(f);
			LOG.warn(list.get(i).getId()+"---"+list.get(i).getName()+"---"+list.get(i).getDays()+"---"+list.get(i).getResult()+"---"+list.get(i).getResult1());
		}
		LOG.warn(list.toString());
		
		out.print(net.sf.json.JSONArray.fromObject(list));
		out.close();
	}
	
	/*获取不同国家在不同运输方式下的运费*/
	public void getCost_two(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		int countryid=Integer.parseInt(request.getParameter("countryid"));
		String currency = request.getParameter("currency");
		String type = request.getParameter("type");//具体页面,spider-从购物车session中取所有数据，
		List<ShippingBean> list = new ArrayList<ShippingBean>();
		IZoneServer zs= new ZoneServer();
		//获取用户中的货币
		if(!Utility.getStringIsNull(currency)){
			currency = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency)){
				currency = "USD";
			}
		}
		Map<String, Double> maphl = Currency.getMaphl(request);
		BigDecimal   b1   =   new   BigDecimal(maphl.get("USD")/maphl.get(currency));
		float   hl_   =   b1.setScale(3,   BigDecimal.ROUND_HALF_UP).floatValue();
		
		if("spider".equals(type)){//spider-从购物车session中取所有数据
			int calculate = Integer.parseInt(request.getParameter("calculate"));//查询非免邮0，还是免邮SAL3，Express4
			List<SpiderBean> spiderBeans = new ArrayList<SpiderBean>();
//			HttpSession session = request.getSession();
//			Object shopcar = session.getAttribute("shopcar");
			String shopcar = Redis.hget(request.getSession().getId(), "shopcar");
			if(shopcar != null){
				shopcar = shopcar.replace("\"[", "\"[[");
				spiderBeans =  (List) net.sf.json.JSONArray.toCollection(JSONArray.fromObject(shopcar),  
						SpiderBean.class);
//				spiderBeans = (List<SpiderBean>) shopcar;
			}
			List<Object[]> obj_s = new ArrayList<Object[]>();
			for (int i = 0; i < spiderBeans.size(); i++) {
				SpiderBean spiderBean = spiderBeans.get(i);
				String free_shopping_company = spiderBean.getFree_shopping_company();
				int number = spiderBean.getNumber();
				double weight = Double.parseDouble(spiderBean.getWeight());
				if(weight == 0)continue;
				if(calculate == 3){
					if((free_shopping_company.indexOf("SAL") > -1 && spiderBean.getFreight_free() == 1) || spiderBean.getFreight_free() == 0){
						obj_s.add(new Object[]{spiderBean.getNumber(),Double.parseDouble(spiderBean.getWeight())*number,Double.parseDouble(spiderBean.getPrice())*number,Utility.getStringIsNull(spiderBean.getGoods_catid()) ? spiderBean.getGoods_catid() : "0",Double.parseDouble(spiderBean.getBulk_volume())});
					}
				}else if(calculate == 4){
					if((free_shopping_company.indexOf("Express") > -1 && spiderBean.getFreight_free() == 1) || spiderBean.getFreight_free() == 0){
						obj_s.add(new Object[]{spiderBean.getNumber(),Double.parseDouble(spiderBean.getWeight())*number,Double.parseDouble(spiderBean.getPrice())*number,Utility.getStringIsNull(spiderBean.getGoods_catid()) ? spiderBean.getGoods_catid() : "0",Double.parseDouble(spiderBean.getBulk_volume())});
					}
				}else{
					if(spiderBean.getFreight_free() == 0){
						obj_s.add(new Object[]{spiderBean.getNumber(),Double.parseDouble(spiderBean.getWeight())*number,Double.parseDouble(spiderBean.getPrice())*number,Utility.getStringIsNull(spiderBean.getGoods_catid()) ? spiderBean.getGoods_catid() : "0",Double.parseDouble(spiderBean.getBulk_volume())});
					}
				}
			}
			double[] weights = new double[obj_s.size()];
			double[] prices = new double[obj_s.size()];
			double[] volumes = new double[obj_s.size()];
			String[] types = new String[obj_s.size()];
			int[] numbers = new int[obj_s.size()];
			for (int j = 0; j < obj_s.size(); j++) {
				Object[] obj = obj_s.get(j);
				numbers[j] = (Integer) obj[0];
				weights[j] = (Double) obj[1];
				prices[j] = (Double) obj[2] * hl_;
				types[j] =  (String) obj[3];
				volumes[j] = (Double) obj[4];
			}
			list = zs.getShippingBeans(countryid, weights, prices, volumes, types, numbers);
		}else {//shop-car-从guIds购物车session中取所有数据
			//weights prices numbers types volumes
			String[] weights_str = request.getParameter("weights").split("@");
			String[] prices_str = request.getParameter("prices").split("@");
			String[] number_str = request.getParameter("numbers").split("@");
			String[] types_str = request.getParameter("types").split("@");
			String[] volumes_str = request.getParameter("volumes").split("@");
			double[] weights = new double[weights_str.length];
			double[] prices = new double[weights_str.length];
			double[] volumes = new double[weights_str.length];
			String[] types = new String[weights_str.length];
			int[] number = new int[weights_str.length];
			for (int i = 0; i < weights_str.length; i++) {
				weights[i] = Double.parseDouble(weights_str[i]);
				prices[i] = Double.parseDouble(prices_str[i]) * hl_;
				number[i] = Integer.parseInt(number_str[i]);
				types[i] = types_str[i];
				volumes[i] = Double.parseDouble(volumes_str[i]);
			}
			list = zs.getShippingBeans(countryid, weights, prices, volumes, types, number);
		}
		
		//获取汇率
		for(int i=0;i<list.size();i++){
			BigDecimal   b   =   new   BigDecimal(list.get(i).getResult()*maphl.get(currency));
			float   f   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
			list.get(i).setResult(f);
			LOG.warn(list.get(i).getId()+"---"+list.get(i).getName()+"---"+list.get(i).getDays()+"---"+list.get(i).getResult());
		}
		LOG.warn(list.toString());
		out.print(net.sf.json.JSONArray.fromObject(list));
		out.close();
		
	}
	
	/*获取不同国家在不同运输方式下的运费*/
	public void getCost_deliverytime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		int countryid=Integer.parseInt(request.getParameter("countryid"));
		String zoneInfoList = request.getParameter("zoneInfoList");
		List<Object[]> obj_s = new ArrayList<Object[]>();
		if(Utility.getStringIsNull(zoneInfoList)){
			JSONArray ja = JSONArray.fromObject(zoneInfoList);
			for (int i = 0; i < ja.size(); i++) {
				JSONArray json = (JSONArray) ja.get(i);
				obj_s.add(new Object[]{json.get(0),json.get(1),json.get(2),json.get(3),json.get(4)});
			}
		}
		String zoneInfoList_sal = request.getParameter("zoneInfoList_sal");
		if(Utility.getStringIsNull(zoneInfoList_sal)){
			JSONArray ja = JSONArray.fromObject(zoneInfoList_sal);
			for (int i = 0; i < ja.size(); i++) {
				JSONArray json = (JSONArray) ja.get(i);
				obj_s.add(new Object[]{json.get(0),json.get(1),json.get(2),json.get(3),json.get(4)});
			}
		}
		String zoneInfoList_express = request.getParameter("zoneInfoList_express");
		if(Utility.getStringIsNull(zoneInfoList_express)){
			JSONArray ja = JSONArray.fromObject(zoneInfoList_express);
			for (int i = 0; i < ja.size(); i++) {
				JSONArray json = (JSONArray) ja.get(i);
				obj_s.add(new Object[]{json.get(0),json.get(1),json.get(2),json.get(3),json.get(4)});
			}
		}
		String currency = request.getParameter("currency");
		List<ShippingBean> list = new ArrayList<ShippingBean>();
		IZoneServer zs= new ZoneServer();
		double[] weights = new double[obj_s.size()];
		double[] prices = new double[obj_s.size()];
		double[] volumes = new double[obj_s.size()];
		String[] types = new String[obj_s.size()];
		int[] numbers = new int[obj_s.size()];
		for (int j = 0; j < obj_s.size(); j++) {
			Object[] obj = obj_s.get(j);
			numbers[j] = (Integer) obj[0];
			weights[j] =  Double.parseDouble(obj[1]+"");
			prices[j] = Double.parseDouble(obj[2]+"");
			types[j] =  Utility.getStringIsNull(obj[3]+"") ? obj[3]+"" : "0";
			volumes[j] = Double.parseDouble(obj[4]+"");
		}
		list = zs.getShippingBeans(countryid, weights, prices, volumes, types, numbers);
		//获取用户中的货币
		if(!Utility.getStringIsNull(currency)){
			currency = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency)){
				currency = "USD";
			}
		}
		//获取汇率
		Map<String, Double> maphl = Currency.getMaphl(request);
		for(int i=0;i<list.size();i++){
			BigDecimal   b   =   new   BigDecimal(list.get(i).getResult()*maphl.get(currency));
			float   f   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();
			list.get(i).setResult(f);
			LOG.warn(list.get(i).getId()+"---"+list.get(i).getName()+"---"+list.get(i).getDays()+"---"+list.get(i).getResult());
		}
		LOG.warn(list.toString());
		out.print(net.sf.json.JSONArray.fromObject(list));
		out.close();
		
	}
	
	/**
	 * 获取运费接口订单后台调用
	 * @param weight  重量
	 * @param countryid 国家
	 * @param volume 体积
	 * @return
	 */
	public void getOrderFreight(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IZoneServer zs= new ZoneServer();
		PrintWriter out = response.getWriter();
		String countryid_req = request.getParameter("countryid");
		int countryid = 0;
		if(Utility.getIsInt(countryid_req)){
			countryid = Integer.parseInt(countryid_req);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":1}");
			out.flush();
			out.close();
		}
		String volume = request.getParameter("volume");
		double volume_ = 0;
		if(Utility.getIsDouble(volume)){
			volume_ = Double.parseDouble(volume);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":2}");
			out.flush();
			out.close();
		}
		String weigth = request.getParameter("weigth");
		double weigth_ = 0;
		if(Utility.getIsDouble(weigth)){
			weigth_ = Double.parseDouble(weigth);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":3}");
			out.flush();
			out.close();
		}
		List<ShippingBean> list = zs.getShippingBeans(countryid, new double[]{weigth_}, new double[]{1}, new double[]{volume_}, new String[]{"0"}, new int[]{1});
		out.println(JSONArray.fromObject(list));
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		int countryid=29;
		float weight=10f;//总重量
		int count=15;//运费方式数量
		float volume=0;//特殊处理ems:体积/8000
		float singleweightmax=0;//单件最大重量
		IZoneServer zs= new ZoneServer();
		String str="";
		
		float temp=zs.getCost(countryid, weight, 16,volume,singleweightmax);
		System.out.println(temp);
		 
	}
}
