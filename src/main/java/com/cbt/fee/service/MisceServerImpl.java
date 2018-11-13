package com.cbt.fee.service;

import com.cbt.bean.State;
import com.cbt.bean.StateZip;
import com.cbt.fee.dao.IMisceDao;
import com.cbt.fee.dao.MisceDaoImpl;
import com.cbt.util.Utility;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MisceServerImpl implements IMisceServer {
	private IMisceDao misceDao=new MisceDaoImpl();
	private static final Log LOG = LogFactory.getLog(MisceServerImpl.class);
	@Override
	public String getZipResult(String zipcode){
		BufferedReader in = null;
		String result = "";
		try{
        	String url="http://ziptasticapi.com/"+zipcode;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            //设置User Agent
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 建立实际的连接
            connection.setConnectTimeout(1000);
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            } 
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(in!=null){
				 try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		return result;
	}
	
	public Map<String, String>  getFreight(String zipcode, float productweightkg,float cubicfoot,
			int furniture, int insidedelivery,float productValue,float dutyrate,String zone) {
		Map<String, String> resultMap=new HashMap<String, String>();	
        BufferedReader in = null;
        String result ="";
       
        try {
        	
        	if(zone.indexOf("6+USA")>=0||zone.indexOf("6+CANADA")>=0){
        		System.out.println(zone);
        		result=getZipResult(zipcode);
        	}
            //千克转换成磅
            float productweight=productweightkg*2.2f;
            
            if(Utility.getStringIsNull(result)){
            	JSONObject parseObject = JSONObject.fromObject(result);
            	String state = parseObject.getString("state");
            	Map<String, State> stateList = misceDao.getStateList(state);
            	if(stateList==null||stateList.size()<1){
            		return null;
            	}
            	
            	//
//            	Map<String, StateZip> stateZipMap = misceDao.getStateZipMap(state);
            	String origins="";
//            	for(Entry<String, StateZip> entry:stateZipMap.entrySet()){
//            		String key = entry.getKey();
//            		origins=origins+key+"|";
//            	}
            	for(String entry:stateList.keySet()){
            		origins=origins+entry+"|";
            	}
            	if(origins.endsWith("|")){
            		origins=origins.substring(0, origins.length()-1);
            	}
//            	Map<String, Object> distance = getDistance(zipcode,origins);
            	LOG.warn(origins+"====="+zipcode);
            	
            	Map<String, Object> distance = getDistance(origins, zipcode);
            	JSONObject fromObject = JSONObject.fromObject(distance);
            	LOG.warn("83====="+fromObject.toString());
            	String add = (String) distance.get("add");
//            	Set<String> keySet = stateZipMap.keySet();
//            	StateZip stateZip=null;
//            	for(String  s:keySet){
//            		if(add.indexOf(s)>0){
//            			stateZip=stateZipMap.get(s);
//            			break;
//            		}
//            	}
//            	State state2 = stateList.get(add);
//            	if(state2==null){
//            		return null;
//            	}
            	StateZip stateZip=null;
            	for(String  s:stateList.keySet()){
            		if(add.indexOf(s)>0){
//            			stateZip=stateZipMap.get(s);
            			State state2 = stateList.get(s);
            			stateZip=misceDao.getStateZip(state2.getDestinationPort());
            			break;
            		}
            	}
            	if(stateZip==null){
            		return null;
            	}
            	//海运费
            	float ocean_freight = Math.max(stateZip.getVolumeFreight()*cubicfoot/35,stateZip.getWeightFreight()*productweightkg/1000);
            	float seaFreightTime=stateZip.getDeliveryTime();
            	
            	//距离
            	int distancemiles=0;
            	if(distance.size()>0){
            		distancemiles = (Integer) distance.get("distance");
            	}
            	
            	LOG.warn("118=="+distancemiles);
            	
            	float pcf=productweight/cubicfoot;
            	float pcfClass=0;
            	if(pcf>1000000){
            		pcfClass=50;
            	}else{
            		pcfClass = misceDao.getFreightClass(pcf);
            	}
            	
            	
            	
//            	Freight Rate with Fuel Surcharge= ((0.35 * 距离 * Class * 0.0215) * 重量 (lb)/1000 * 0.0569+130.41)*1.22
//            	Limited Access Fee (usd)= 0.0952*重量（lb)
//            	Truck Broker Service fee = (Freight Rate with Fuel Surcharge+Limited Access Fee)*10%
            	
//            	按下列公式计算卡车运费
            	float frwfs=(float) (((0.35 * distancemiles/1000 * pcfClass * 0.0215) * productweight/1000 * 0.0569+130.41)*1.22);
            	LOG.warn(frwfs);
            	float laf= (float) (0.0952*productweight);
            	LOG.warn(productweight);
            	LOG.warn(laf);
            	//卡车运费
            	float tbsf=(float) ((frwfs+laf)*0.1);
            	
            	float idelivery=insidedelivery;
//            	if(insidedelivery==1){
//            		idelivery=120;
//            	}
//            	LiftGate Service Fee = 0.0775*重量（lb)
            	float lgsf= (float) (0.0775*productweight);
//            	总 trucking cost = Freight Rate with Fuel Surcharge + Limited Access Fee + Truck Broker Service fee + 如果有（Inside delivery+LiftGate Service Fee））
            	//总费用
            	float truckCost=frwfs+laf+tbsf+(idelivery+lgsf);
            	
//            	清关运期 （Clear Custom) = 3
//            	卡车运期 （Trucking) = 2
//            	Total Transit Time 总运期 = 海运运期+清关运期+卡车运期
            	
            	float totalTransitTime=seaFreightTime+3+2;
            	
//            	Duty 关税 = Duty Rate * 产品货值（Product Value)
            	//关税
            	float duty=dutyrate*productValue;
            	
            	
//            	Other Processing Fees in USA				
//            	如果是美国				
//            	AMS： 			25	USD
//            	ISF： 			25	USD
//            	CUSTOMS Clearance Fee：  			125	USD
//            	Document Transfer：      			65	USD
//            	CUSTOMS Bond：  			100	USD

            	
//            	Other Processing Fees = 340 USD
            	int otherprofee=340;
            	
//            	1立方米 = 35 立方英尺
//            	Warehouse Cost '= minmum(18*产品体积/35 +203 , 550)
	            
            	float warehousecost=Math.min(18*cubicfoot/35+203, 550);
            	
//            	总运费 = 海运费 Ocean Freight Cost + 总 trucking cost + Other Processing Fees + Warehouse Cost + Duty
            	
            	float totallogisticsCost=ocean_freight+truckCost+otherprofee+warehousecost;
            	DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            	resultMap.put("add", add.split(",")[0]+","+add.split(",")[1]);
            	if(pcfClass==50){
            		pcfClass=Math.round(pcfClass);
            	}
            	resultMap.put("pcfclass", pcfClass+"");      	
            	resultMap.put("distance",   Math.round(distancemiles/1000)+"  km");
            	resultMap.put("danjia", decimalFormat.format(ocean_freight/productweight) +" USD/pound");
            	if(insidedelivery>0){
            		resultMap.put("insidedelivery", "Inside delivery");
            	}else{
            		resultMap.put("insidedelivery", "");
            	}
            	
            	resultMap.put("ofc", decimalFormat.format(ocean_freight));
            	resultMap.put("ams", "25");
            	resultMap.put("isf", "25");
            	resultMap.put("ccf", "125");
            	resultMap.put("dt", "65");
            	resultMap.put("cb", "100");
            	DecimalFormat df = new DecimalFormat(".00");
            	resultMap.put("duty", decimalFormat.format(duty));
            	resultMap.put("wc", decimalFormat.format(warehousecost));
            	resultMap.put("tc", decimalFormat.format(truckCost));
            	resultMap.put("tlc", df.format(totallogisticsCost));
            	resultMap.put("ttt", totalTransitTime+"");
            }else{
//            	resultMap
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if(in!=null){
				 try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
		return resultMap;
	}
	@SuppressWarnings("deprecation")
	public Map<String, Object> getDistance(String origins,String destinations){
		Map<String, Object> resultMap=new HashMap<String, Object>();
		BufferedReader in =null;
		try {
			LOG.warn("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+origins+"&destinations="+destinations+"&mode=driving&language=fr-FR&sensor=true");
			String urls="http://maps.googleapis.com/maps/api/distancematrix/json?origins="+origins+"&destinations="+destinations+"&mode=driving&language=fr-FR&sensor=true";
//			url=url.replaceAll("|", "%124");
			URL url=new URL(urls);
			URI ui = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//			URI ui = new URI(urls);
			HttpClient hc = new DefaultHttpClient();
//			org.apache.http.HttpHost proxy = new org.apache.http.HttpHost("127.0.0.1", 3213);
//			hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
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
			LOG.warn(result);
			if(Utility.getStringIsNull(result)&&result.endsWith("}")&&result.startsWith("{")){
//				JSONObject parseObject = JSON.parseObject(result);
				JSONObject parseObject = JSONObject.fromObject(result);
				
				String status0 = parseObject.getString("status");
				if(status0.equals("OK")){
					//如果多对多  要多层循环
					//多个目的地
					/*JSONArray destinationAdd = parseObject.getJSONArray("destination_addresses");
					//多个起点
//					JSONArray destinationAdd = parseObject.getJSONArray("origin_addresses");
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
					}*/
					//多个起点 多个终点
					//终点内容
//					JSONArray destinationAdd = parseObject.getJSONArray("destination_addresses");
					//起点内容
					JSONArray originAdd = parseObject.getJSONArray("origin_addresses");
					JSONArray rows = parseObject.getJSONArray("rows");
					for(int i=0;i<originAdd.size();i++){
						String sAdd = (String)originAdd.get(i);
						JSONObject jsonObject = rows.getJSONObject(i);
						JSONArray eleDistance = jsonObject.getJSONArray("elements");
						for(int j=0;j<eleDistance.size();j++){
							JSONObject ele = eleDistance.getJSONObject(j);
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
	}
}
