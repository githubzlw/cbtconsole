package com.cbt.processes.servlet;

import com.cbt.bean.Forwarder;
import com.cbt.bean.TrackBean;
import com.cbt.util.WebCookie;
import com.cbt.website.dao2.CargoTrackingSample;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zlw
 * 订单操作
 */
public class TrackInfo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	//订单跟踪信息
	public void getTrackInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String orderNo = request.getParameter("orderNo");
		 List<TrackBean> trackList = new ArrayList<TrackBean>();
		 String[] user =  WebCookie.getUser(request);
		 if(user != null){
			
			IOrderwsServer serverws = new OrderwsServer();
			Forwarder forwarder = serverws.getForwarder(orderNo);
			String apiKey ="f97a462b9c6cc71f";
			String typeCom = forwarder.getLogistics_name();
			String typeNu = forwarder.getExpress_no();
				
			int isneed = forwarder.getIsneed();
			String ytime = forwarder.getCreatetime().toString();
			String ycontext = "[SHANG HAI - SHANG HAI]FLY TO HONG KONG";
			if(isneed==1 ){
				TrackBean trackBean = new TrackBean();
	            
	            try {
		            CargoTrackingSample cs = new CargoTrackingSample();
		            String jsonArray = cs.cargoTrackingService(typeNu);
		            if("".equals(jsonArray)){
		            	trackBean.setTime(ytime);
			            trackBean.setContext(ycontext);
			            trackList.add(trackBean);
		            }
					try {
						JSONObject json= new JSONObject(jsonArray.substring(1, jsonArray.length()-1));
						JSONArray details = json.getJSONArray("data"); 
			            for(int i=0;i<details.length();i++){
				            JSONObject jObject=(JSONObject) details.get(i);  
				            String time=(String)jObject.get("time");
				            String context=(String)jObject.getString("context");
				            TrackBean trackBean2 = new TrackBean();
				            trackBean2.setTime(time);
				            trackBean2.setContext(context);
				            trackList.add(trackBean2);
				        }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }catch (net.sf.json.JSONException e) {
					request.setAttribute("errorInfo","No result found for your query. Please try later.");
					e.printStackTrace();
				}
	            
			}else if(!"".equals(typeNu)){
				
				String url = "http://api.kuaidi100.com/api?id="+apiKey+"&com="+typeCom+"&nu="+typeNu+"&show=0&muti=1&order=desc";
		        
		        String jsons = loadJson(url);
		        
		        try {
			        JSONObject json= new JSONObject(jsons);  
			        JSONArray jsonArray=json.getJSONArray("data");  
			        
			        for(int i=0;i<jsonArray.length();i++){  
			            JSONObject jObject=(JSONObject) jsonArray.get(i);  
			            String time=(String) jObject.get("time");
			            String context=(String) jObject.get("context");
			            TrackBean trackBean = new TrackBean();
			            trackBean.setTime(time);
			            trackBean.setContext(context);
			            trackList.add(trackBean);
			        }
				} catch (JSONException e) {
					request.setAttribute("errorInfo","No result found for your query. Please try later.");
					e.printStackTrace();
				}
		        		
			}
			//运输方式
			request.setAttribute("typeCom", typeCom.replace("en", ""));
			//快递跟踪号
			request.setAttribute("typeNu", typeNu);
			request.setAttribute("trackList",trackList);
			
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/track.jsp");
			homeDispatcher.forward(request, response);
	        
			
		 }else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
		
	}
    
	public static String loadJson (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return json.toString();
    }
	
}
