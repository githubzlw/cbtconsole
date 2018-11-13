package com.cbt.fee.servlet;

import com.cbt.bean.ShippingBean;
import com.cbt.fee.service.IMisceServer;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.MisceServerImpl;
import com.cbt.fee.service.ZoneServer;
import com.cbt.util.Utility;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetFright extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void getZipResult(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		IMisceServer misceServer=new MisceServerImpl();
		String zipcode=request.getParameter("zipcode");
		String result= misceServer.getZipResult(zipcode);
		out.print(result);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
//		PrintWriter out = response.getWriter();
//      测试区号     94596
		String zipcode=request.getParameter("zipcode");
		String zone=request.getParameter("shiptocountry");
		String flag=request.getParameter("flag");
		String userid=request.getParameter("userid");
		String itemid=request.getParameter("itemid");
		float productweight=Float.parseFloat(request.getParameter("productweight"));
		float maxweight =Float.parseFloat(request.getParameter("maxweight"));//最大单个商品重量
		float cubicfoot=Float.parseFloat(request.getParameter("cubicfoot"));
//		int furniture=Integer.parseInt(request.getParameter("furniture"));
//		String insidedeliveryStrs=request.getParameter("insidedelivery");
		String[] insidedeliveryStr = request.getParameterValues("insidedelivery");
		int insidedelivery=0;
		if(insidedeliveryStr!=null && insidedeliveryStr.length>0){
			for(String s:insidedeliveryStr){
				if(s.equals("1")){
					insidedelivery=120;
				}
			}
		}
		float productValue=0;
		String productValueStr=request.getParameter("productValue");
		if(Utility.getStringIsNull(productValueStr)){
			productValue=Float.parseFloat(productValueStr);
		}
		String dutyrateStr=request.getParameter("dutyrate");
		float dutyrate=0;
		if(Utility.getStringIsNull(dutyrateStr)){
			dutyrateStr=dutyrateStr.trim().replaceAll("%", "");
			dutyrate=Float.parseFloat(dutyrateStr)/100;
		}
		IMisceServer misceServer=new MisceServerImpl();
		Map<String, String> freight = misceServer.getFreight(zipcode, productweight,cubicfoot, 0, insidedelivery,productValue,dutyrate,zone);
		request.setAttribute("freight", freight);
		request.setAttribute("zone", zone);
		request.setAttribute("productweight", productweight);
		request.setAttribute("cubicfoot", cubicfoot);
		request.setAttribute("productValue", productValue);
		request.setAttribute("zipcode", zipcode);
		request.setAttribute("duty", dutyrateStr);
		request.setAttribute("flag", flag);
		request.setAttribute("total",freight.get("tlc"));
		request.setAttribute("pcfclass", freight.get("pcfclass"));
		request.setAttribute("ttt", freight.get("ttt"));
		if(userid.equals("")&&itemid.equals("")){
			request.setAttribute("userid", "null");
			request.setAttribute("itemid", "null");
		}else{
			request.setAttribute("userid", userid);
			request.setAttribute("itemid", itemid);
		}
		IZoneServer zs= new ZoneServer();
		List<ShippingBean> list = zs.getShippingList(Integer.parseInt(zone.substring(0,zone.indexOf("+"))), productweight, cubicfoot, maxweight, 16);
		ShippingBean sbBean = null;
		for (int i = 0; i < list.size(); i++) {
			sbBean = list.get(i);
			if(sbBean.getResult() == 0){list.remove(i);}
		}
		request.setAttribute("express", list);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/Fee.jsp");
		homeDispatcher.forward(request, response);
		//		out.println(JSONObject.fromObject(freight));
//		out.flush();
//		out.close();
	}
	
	/**
	 * 获取运费接口沈静调用
	 * @param weight  重量
	 * @param price 价格
	 * @param volume 体积
	 * @return
	 */
	public void getFreight(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IZoneServer zs= new ZoneServer();
		int countryid = 36;
		PrintWriter out = response.getWriter();
		String weights = request.getParameter("weight");
		double weights_ = 0;
		if(Utility.getIsDouble(weights)){
			weights_ = Double.parseDouble(weights);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":1}");
			out.flush();
			out.close();
			return ;
		}
		
		double prices_ = 0;
		/*String prices = request.getParameter("price");
		if(Utility.getIsDouble(prices)){
			prices_ = Double.parseDouble(prices);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":2}");
			out.flush();
			out.close();
		}*/
		

		String volume = request.getParameter("volume");
		double volume_ = 0;
		if(Utility.getIsDouble(volume)){
			volume_ = Double.parseDouble(volume);
			
		}
		//int countryid,double weights[],double prices[],double volumes[],String types[], int[] number 
		List<ShippingBean> list = zs.getShippingBeans(countryid, new double[]{weights_}, new double[]{prices_}, new double[]{volume_}, new String[]{"0"}, new int[]{1});
		out.println(JSONArray.fromObject(list));
		out.flush();
		out.close();
	}
	
	/**
	 * 获取运费接口周鹏调用-包裹运费
	 * @param weight  重量
	 * @param volume 体积
	 * @param countryid 国家
	 * @return
	 */
	public void getFreight_package(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IZoneServer zs= new ZoneServer();
		int countryid = 36;
		PrintWriter out = response.getWriter();
		String weights = request.getParameter("weight");
		double weights_ = 0;
		if(Utility.getIsDouble(weights)){
			weights_ = Double.parseDouble(weights);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":1}");
			out.flush();
			out.close();
			return ;
		}

		String volume = request.getParameter("volume");
		double volume_ = 0;
		if(Utility.getIsDouble(volume)){
			volume_ = Double.parseDouble(volume);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":2}");
			out.flush();
			out.close();
			return ;
		}

		String countryid_ = request.getParameter("countryid");
		if(Utility.getIsInt(countryid_)){
			countryid = Integer.parseInt(countryid_);
		}else{
			out.print("{\"result\":\"Fail\",\"code\":3}");
			out.flush();
			out.close();
			return ;
		}
		List<ShippingBean> list = zs.getShippingBeans(countryid, new double[]{weights_}, new double[]{0}, new double[]{volume_}, new String[]{"0"}, new int[]{1});
		out.println(JSONArray.fromObject(list));
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		IZoneServer zs= new ZoneServer();
		List<ShippingBean> list = zs.getShippingBeans(36, new double[]{5}, new double[]{53}, new double[]{0.09}, new String[]{"0"}, new int[]{1});
		System.out.println(JSONArray.fromObject(list));
	}
	/**
	 * 一个起点 多个目的 解析
	 * @param origins  起点
	 * @param destinations 目的地
	 * @return
	 *//*
	@SuppressWarnings("deprecation")
	public Map<String, Object> getDistance(String origins,String destinations){
		Map<String, Object> resultMap=new HashMap<String, Object>();
		BufferedReader in =null;
		try {
			System.out.println("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+origins+"&destinations="+destinations+"&mode=driving&language=fr-FR&sensor=true");
			String url="http://maps.googleapis.com/maps/api/distancematrix/json?origins="+origins+"&destinations="+destinations+"&mode=driving&language=fr-FR&sensor=true";
			URI ui = new URI(url);
			HttpClient hc = new DefaultHttpClient();
			org.apache.http.HttpHost proxy = new org.apache.http.HttpHost("127.0.0.1", 3213);
			hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			HttpGet hg = new HttpGet(ui);
			HttpResponse hr = null;
			hr = hc.execute(hg);
			HttpEntity en = hr.getEntity();
			InputStream ins = en.getContent();
			in = new BufferedReader(new InputStreamReader(ins,"UTF-8"));
			String line1=null;
			String result="";
			while ((line1 = in.readLine()) != null) {
				result += line1;
			}
			System.out.println(result);
			if(Utility.getStringIsNull(result)){
//				JSONObject parseObject = JSON.parseObject(result);
				JSONObject parseObject = JSONObject.fromObject(result);
				
				String status0 = parseObject.getString("status");
				if(status0.equals("OK")){
					//如果多对多  要多层循环
					//多个目的地
					JSONArray destinationAdd = parseObject.getJSONArray("destination_addresses");
					//多个起点
//					JSONArray originAdd = parseObject.getJSONArray("origin_addresses");
					JSONArray rows = parseObject.getJSONArray("rows");
					JSONObject jsonObject = rows.getJSONObject(0);
					JSONArray eleDistance = jsonObject.getJSONArray("elements");
					for(int i=0;i<destinationAdd.size();i++){
						String sAdd = (String)destinationAdd.get(i);
						JSONObject ele = eleDistance.getJSONObject(i);
						String status = ele.getString("status");
						if(status.equals("OK")){
							JSONObject distanceS = ele.getJSONObject("distance");
							int distance = distanceS.getInt("value");
							Integer dis = (Integer)resultMap.get("distance");
							if(dis==null){
								resultMap.put("add", sAdd);
								resultMap.put("distance", distance);
							}else{
								if(distance<dis){
									resultMap.put("add", sAdd);
									resultMap.put("distance", distance);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
				 try {
					 if(in!=null){
						 in.close();
					 }
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return resultMap;
	}*/
}
