package com.cbt.warehouse.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.pojo.TrackInfoPojo;
import com.cbt.warehouse.pojo.TrankBean;
import com.cbt.warehouse.service.TrackService;
import com.cbt.website.util.EasyUiJsonResult;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/trackingController")
public class TrackingController {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TrackingController.class);
	
	private   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
 	@Autowired
	private TrackService trackService;
	
	//抓取JCEX物流信息
 	@RequestMapping("trackJcexInfo")
 	@ResponseBody
	public String  trackJcexInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 String company = request.getParameter("company");
		 String senttimeBegin = request.getParameter("senttimeBegin");
		 String senttimeEnd = request.getParameter("senttimeEnd");
		 String result="";
		 String json ="";
		 if(StrUtils.isNullOrEmpty(senttimeBegin)){
			 result="false";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		 if(StrUtils.isNotNullEmpty(senttimeEnd)){
			 senttimeEnd =senttimeEnd+" 23:59:59";
		 }
		 senttimeEnd = senttimeEnd.length()>0?senttimeEnd:null;
		//获取指定网址的页面内容
//		 org.jsoup.nodes.Document  document = null;
		//获取JCEX运单号
		List<TrankBean>  expressnoList = trackService.getAllJCEX(company,senttimeBegin,senttimeEnd);
		 if(expressnoList.size()==0){
			 result ="此区间没有运单信息";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		 if(company.equalsIgnoreCase("JCEX")){
//			 //异步
			 JcexThread  thread = new JcexThread(expressnoList,company);
			 thread.start();
			 result = "JCEX物流跟踪数据抓取完后自动存入数据库,请稍后查看";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		 if(company.equalsIgnoreCase("emsinten")){
//			 //异步
			 YZThread  thread = new YZThread(expressnoList,company);
			 thread.start();
			 result = "emsinten物流跟踪数据抓取完后自动存入数据库,请稍后查看";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		 if(company.equalsIgnoreCase("原飞航")){
//			 //异步
			 YFHThread  thread = new YFHThread(expressnoList,company);
			 thread.start();
			 result = "原飞航物流跟踪数据抓取完后自动存入数据库,请稍后查看";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		 if(company.equalsIgnoreCase("shunfeng")){
//			 //异步
			 SFThread  thread = new SFThread(expressnoList,company);
			 thread.start();
			 result = "shunfeng物流跟踪数据抓取完后自动存入数据库,请稍后查看";
			 json = JSON.toJSONString(result);
			 return json;
		 }
		return null;
		 
	}
	
	
	
	   
    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public  int daysBetween(Date smdate,Date bdate) throws Exception    
    {    
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }  
    
    
    public  String fun(String urlString) {
		urlString = urlString.replaceAll(" ", "");
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(urlString);
			URLConnection uc = urlObject.openConnection();
			// uc.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "GBK"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Document doc=null;
		String str = json.toString();
		return str;
	}
    
    
    public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
//    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
//    	Date date = new Date();
//    	System.out.println("date"+sdf.format(date));
//    	Date  smdate=sdf.parse("2016-11-30 16:03");  
//    	Date  bdate=sdf.parse(sdf.format(date));  
//         Calendar cal = Calendar.getInstance();    
//         cal.setTime(smdate);    
//         long time1 = cal.getTimeInMillis();                 
//         cal.setTime(bdate);    
//         long time2 = cal.getTimeInMillis();         
//         long between_days=(time2-time1)/(1000*3600*24);  
//         System.out.println(between_days);
    	
    	String url = "http://api.kuaidi100.com/api?id=f97a462b9c6cc71f&com=jcex&nu=984902833&show=0&muti=1&order=desc";
        String jsons = loadJson(url);
        try {
	        JSONObject json= JSONObject.parseObject(jsons);
	        JSONArray jsonArray=json.getJSONArray("data");
	        
	        for(int i=0;i<jsonArray.size();i++){
	            JSONObject jObject=(JSONObject) jsonArray.get(i);  
	            String time=(String) jObject.get("time");
	            String context=(String) jObject.get("context");
	            System.out.println(context);
	            if(context.contains("绛炬�")){
	            	System.out.println(1);
	            }
	        }
		} catch (JSONException e) {
        	e.printStackTrace();
		}
	}
    
    
   	public static   String loadJson (String url) {
           StringBuilder json = new StringBuilder();
           try {
               URL urlObject = new URL(url);
               URLConnection uc = urlObject.openConnection();
               BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
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
   	
   	
   	class JcexThread extends Thread{
   		private List<TrankBean>  expressnoList ;
   		private String company ;
   		
   	    public JcexThread(List<TrankBean> expressnoList, String company) {
			// TODO Auto-generated constructor stub
   	    	this.expressnoList=expressnoList;
   	    	this.company = company;
		}
   	    
   	    @Override
   	    public void run() {
   	     List<TrankBean>  list = new ArrayList<TrankBean>();
   	     org.jsoup.nodes.Document  document = null;
   	    	// TODO Auto-generated method stub
   	    	for(TrankBean row:expressnoList){
		        if(!row.getExpressno().matches(".*\\d+.*")){
		        	 continue;
		        }
				//JCEX
				if(company.equalsIgnoreCase("JCEX")){
					try {
					document= Jsoup.connect("http://www.jcex.cn/Tracking-Result2.asp?WLYD_NUM="+row.getExpressno()+"&act=detail").timeout(50000).get();
					Elements  es = document.select("div[class=RightContentText]").select("div[class=TextControl]").select("table"); 
					if(!es.isEmpty()){
				    	  Elements trs = document.select("div[class=RightContentText]").select("table").get(1).select("tr"); 
				          int size = trs.size();
				          if(size>2){
				        	  //顶头 的最新跟踪数据  
					    	  Elements tds = trs.get(1).select("td");
					            String s1 = tds.get(0).text(); //时间
					            String s2 = tds.get(1).text();  // 站点
					            String s3 = tds.get(2).text();  // 状态
					           //最开始的第一天数据
					          Elements tr = trs.get(size-1).select("td");
					            String td1 = tr.get(0).text(); //时间
					            String td2 = tr.get(1).text();  // 站点
					            String td3 = tr.get(2).text();  // 状态
					           
					            //先判断寄件有没有超过十天
					            if(!s3.equals("签收")){
					            	 //从寄件开始到现在的时间间隔
					            	 int moreTime= daysBetween(sdf.parse(td1.substring(1).trim()),new Date());
					            	 if(moreTime>10){
					            		 TrankBean bean = new TrankBean();
					            		 bean.setExpressno(row.getExpressno());
					            		 bean.setFlag(1);
					            		 bean.setCompany(company);
					            		 bean.setRemarks(row.getRemarks()); //订单
					            		 bean.setAdmName(row.getAdmName()); //销售
					            		 bean.setCreatetime(row.getCreatetime());
					            		 list.add(bean);
					            	 }
					            }
					            if(!s3.equals("签收")){
					            	//最新运单状态到现在的时间间隔
					            	 int days = daysBetween(sdf.parse(s1.substring(1).trim()),new Date());
					            	 if(days>3){
					            		 TrankBean bean = new TrankBean();
					            		 bean.setExpressno(row.getExpressno());
					            		 bean.setFlag(2);
					            		 bean.setCompany(company);
					            		 bean.setRemarks(row.getRemarks()); //订单
					            		 bean.setAdmName(row.getAdmName()); //销售
					            		 bean.setCreatetime(row.getCreatetime());
					            		 list.add(bean);
					            	 }
					            }
				          }
				          
				          if(size==1){
				        	//顶头 的最新跟踪数据  
					    	  Elements tds = trs.get(1).select("td");
					            String s1 = tds.get(0).text(); //时间
					            String s3 = tds.get(2).text();  // 状态
				        	  if(!s3.equals("签收")){
					            	//最新运单状态到现在的时间间隔
					            	 int days = daysBetween(sdf.parse(s1.substring(1).trim()),new Date());
					            	 if(days>3){
					            		 TrankBean bean = new TrankBean();
					            		 bean.setExpressno(row.getExpressno());
					            		 bean.setFlag(2);
					            		 bean.setCompany(company);
					            		 bean.setRemarks(row.getRemarks()); //订单
					            		 bean.setAdmName(row.getAdmName()); //销售
					            		 bean.setCreatetime(row.getCreatetime());
					            		 list.add(bean);
					            	 }
					            }
				          }
				    	 
				      }
				} catch (Exception e) {
					// TODO: handle exception
				}
			 }
   	    }
   	        //将数据插入数据库
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
    		String date = sdf.format(new Date());
	    	if(list.size()>0){
	    		trackService.saveTrackInfo(list);
	    	}else{
	    		List<TrankBean>  list1 = new ArrayList<TrankBean>();
	    		TrankBean bean = new TrankBean();
	    		bean.setFlag(3);
	    		bean.setCompany(company);
	    		list1.add(bean);
	    		trackService.saveTrackInfo(list1);
	    	}
   	    }
   	}
   	
   	
   	class  YFHThread extends Thread{
   		private List<TrankBean>  expressnoList ;
   		private String company ;
   		
   	    public YFHThread(List<TrankBean> expressnoList, String company) {
			// TODO Auto-generated constructor stub
   	    	this.expressnoList=expressnoList;
   	    	this.company = company;
		}
   	    
   	    @Override
   	    public void run() {
   	     List<TrankBean>  list = new ArrayList<TrankBean>();
   	     org.jsoup.nodes.Document  document = null;
   	    	// TODO Auto-generated method stub
   	    	for(TrankBean row:expressnoList){
		        if(!row.getExpressno().matches(".*\\d+.*")){
		        	 continue;
		        }
				//JCEX
				if(company.equalsIgnoreCase("原飞航")){
					try {
					
					String urlString = "http://www.yfhex.com/ServicePlatform/track?num="+row.getExpressno();
					urlString = urlString.replaceAll(" ", "");
					StringBuilder json = new StringBuilder();
					try {
						URL urlObject = new URL(urlString);
						URLConnection uc = urlObject.openConnection();
	
						BufferedReader in = new BufferedReader(
								new InputStreamReader(uc.getInputStream(),
										"UTF-8"));
						String inputLine = null;
						while ((inputLine = in.readLine()) != null) {
							json.append(inputLine);
						}
						in.close();
					} catch (MalformedURLException e) {
	//					e.printStackTrace();
					} catch (IOException e) {
	//					e.printStackTrace();
					}
	
					String retStr = json.toString();
					
					System.out.println(retStr);
	
					JSONArray jsonArray = JSONArray
							.parseArray(retStr);
	
					List<Map<String, String>> mapListJson = (List) jsonArray;
					
					if(mapListJson.size()>1){
						//最新的快件信息
						Map<String, String> obj = mapListJson.get(0);
						String t1 = obj.get("ProcessTime") ; // 时间
						String s1 =obj.get("FlowStep");  // 状态
						//最开始的快件信息
						Map<String, String> obj2 = mapListJson.get(mapListJson.size()-1);
						String t2 = obj2.get("ProcessTime") ; // 时间
						String s2 =obj2.get("FlowStep");  // 状态
		
			            //先判断寄件有没有超过十天
			            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
			            	 //从寄件开始到现在的时间间隔
								int	moreTime = daysBetween(sdf.parse(t2.trim()),new Date());
    							 if(moreTime>10){
				            		 TrankBean bean = new TrankBean();
				            		 bean.setExpressno(row.getExpressno());
				            		 bean.setFlag(1);
				            		 bean.setCompany(company);
				            		 bean.setAdmName(row.getAdmName());
				            		 bean.setRemarks(row.getRemarks());
				            		 bean.setCreatetime(row.getCreatetime());
				            		 list.add(bean);
				            	 }
			            }
			            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
			            	//最新运单状态到现在的时间间隔
			            	 int days = daysBetween(sdf.parse(t1.trim()),new Date());
			            	 if(days>3){
			            		 TrankBean bean = new TrankBean();
			            		 bean.setExpressno(row.getExpressno());
			            		 bean.setFlag(2);
			            		 bean.setCompany(company);
			            		 bean.setAdmName(row.getAdmName());
			            		 bean.setRemarks(row.getRemarks());
			            		 bean.setCreatetime(row.getCreatetime());
			            		 list.add(bean);
			            	 }
			            }
					}
					if(mapListJson.size()==1){
						//最新的快件信息
						Map<String, String> obj = mapListJson.get(0);
						String t1 = obj.get("ProcessTime") ; // 时间
						String s1 =obj.get("FlowStep");  // 状态
						if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
			            	//最新运单状态到现在的时间间隔
			            	 int days = daysBetween(sdf.parse(t1.trim()),new Date());
			            	 if(days>3){
			            		 TrankBean bean = new TrankBean();
			            		 bean.setExpressno(row.getExpressno());
			            		 bean.setFlag(2);
			            		 bean.setCompany(company);
			            		 bean.setAdmName(row.getAdmName());
			            		 bean.setRemarks(row.getRemarks());
			            		 bean.setCreatetime(row.getCreatetime());
			            		 list.add(bean);
			            	 }
			            }
					}
					} catch (Exception e) {
						// TODO: handle exception
					}
			 }
   	    }
   	        //将数据插入数据库
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
    		String date = sdf.format(new Date());
	    	if(list.size()>0){
	    		trackService.saveTrackInfo(list);
	    	}else{
	    		List<TrankBean>  list1 = new ArrayList<TrankBean>();
	    		TrankBean bean = new TrankBean();
	    		bean.setFlag(3);
	    		bean.setCompany(company);
	    		list1.add(bean);
	    		trackService.saveTrackInfo(list1);
	    	}
   	    }
   	}
   	
   	
   	//邮政
   	class YZThread  extends Thread{
   		private List<TrankBean>  expressnoList ;
   		private String company ;
   		
   	    public YZThread(List<TrankBean> expressnoList, String company) {
			// TODO Auto-generated constructor stub
   	    	this.expressnoList=expressnoList;
   	    	this.company = company;
		}
   	    
   	    @Override
   	    public void run() {
   	     List<TrankBean>  list = new ArrayList<TrankBean>();
   	     org.jsoup.nodes.Document  document = null;
   	    	// TODO Auto-generated method stub
   	    	for(TrankBean row:expressnoList){
		        if(!row.getExpressno().matches(".*\\d+.*")){
		        	 continue;
		        }
				// 
				if(company.equalsIgnoreCase("emsinten")){
					try {
						String urlstr = "http://track.api.cnexps.com/cgi-bin/GInfo.dll?EmsApiTrack&cno="+row.getExpressno();
				    	String str = fun(urlstr);
				    	if("-102".equals(str) || "-9".equals(str)){
				    		continue;
						}else{
							if (str.indexOf("<TRACK_DATA>") != -1) {
								str = str.substring(str.indexOf("<TRACK_DATA>"),
										str.indexOf("</TRACK_DATA>") + "</TRACK_DATA>".length());
							}
							SAXBuilder sb = new SAXBuilder(false);
							StringReader read = new StringReader(str);
							Document doc = null;
							System.out.println(str);
		
							try {
								doc = sb.build(read);
							} catch (JDOMException e) {
								// TODO Auto-generated catch block
		//						e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
		//						e.printStackTrace();
							}
							Element books = doc.getRootElement();
							List datatime = books.getChildren("DATETIME");
							List place = books.getChildren("PLACE");
							List info = books.getChildren("INFO");
		
							Iterator iterdatatime = datatime.iterator();
							Iterator iterplace = place.iterator();
							Iterator iterInfo = info.iterator();
							int count = 0;
							String sj="";
							String xinxi="";
							String zt="";
							
							String sj2="";
							String xinxi2="";
							String zt2="";
							while (iterdatatime.hasNext()) {
								count ++;
								Element book = (Element) iterdatatime.next();
								Element book2 = (Element) iterplace.next();
								Element book3 = (Element) iterInfo.next();
								if(count==1){
									  sj = book.getText();
									  xinxi = book2.getText();
									  zt = book3.getText();
								}else{
									 if(!iterdatatime.hasNext()) {//最后一个元素
										  sj2 = book.getText();
										  xinxi2 = book2.getText();
										  zt2 = book3.getText();
										 
								      }
								}
								iterdatatime.hasNext();
							}
							
							System.out.println("state"+zt2);
							if(StrUtils.isNotNullEmpty(sj2)){
								//先判断寄件有没有超过十天
					            if(!(zt2.contains("妥投")||zt2.contains("投递并签收"))){
					            	if(StrUtils.isNotNullEmpty(sj)){
					            	 //从寄件开始到现在的时间间隔
					            	 int moreTime= daysBetween(sdf.parse(sj.trim()),new Date());
					            	 if(moreTime>10){
					            		 TrankBean bean = new TrankBean();
					            		 bean.setFlag(1);
					            		 bean.setExpressno(row.getExpressno());
					            		 bean.setCompany(company);
					            		 bean.setAdmName(row.getAdmName());
					            		 bean.setRemarks(row.getRemarks());
					            		 bean.setCreatetime(row.getCreatetime());
					            		 list.add(bean);
					            	 }
					            	}
					            }
					            
					            if(!(zt2.contains("妥投")||zt2.contains("投递并签收"))){
					            	  if(StrUtils.isNotNullEmpty(sj2)){
					            		//最新运单状态到现在的时间间隔
							            	 int days = daysBetween(sdf.parse(sj2.trim()),new Date());
							            	 if(days>3){
							            		 TrankBean bean = new TrankBean();
							            		 bean.setFlag(2);
							            		 bean.setExpressno(row.getExpressno());
							            		 bean.setCompany(company);
							            		 bean.setAdmName(row.getAdmName());
							            		 bean.setRemarks(row.getRemarks());
							            		 bean.setCreatetime(row.getCreatetime());
							            		 list.add(bean);
							            	 }
					            	  }
					            }
							}
							//
							if(StrUtils.isNullOrEmpty(sj2)){
								if(!(zt.contains("妥投")||zt.contains("投递并签收"))){
					            	  if(StrUtils.isNotNullEmpty(sj)){
					            		//最新运单状态到现在的时间间隔
							            	 int days = daysBetween(sdf.parse(sj.trim()),new Date());
							            	 if(days>3){
							            		 TrankBean bean = new TrankBean();
							            		 bean.setFlag(2);
							            		 bean.setExpressno(row.getExpressno());
							            		 bean.setCompany(company);
							            		 bean.setAdmName(row.getAdmName());
							            		 bean.setRemarks(row.getRemarks());
							            		 bean.setCreatetime(row.getCreatetime());
							            		 list.add(bean);
							            	 }
					            	  }
					            }
							}
							
						}
				    	
						} catch (Exception e) {
							// TODO: handle exception
						}
			 }
   	    }
   	     //将数据插入数据库
    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
    		String date = sdf.format(new Date());
   	    	if(list.size()>0){
   	    		trackService.saveTrackInfo(list);
   	    	}else{
   	    		List<TrankBean>  list1 = new ArrayList<TrankBean>();
   	    		TrankBean bean = new TrankBean();
   	    		bean.setFlag(3);
   	    		bean.setCompany(company);
   	    		list1.add(bean);
   	    		trackService.saveTrackInfo(list1);
   	    	}
   	    }
   	}
   	
   	
   	class  SFThread extends Thread{
   		private List<TrankBean>  expressnoList ;
   		private String company ;
   		
   	    public SFThread(List<TrankBean> expressnoList, String company) {
			// TODO Auto-generated constructor stub
   	    	this.expressnoList=expressnoList;
   	    	this.company = company;
		}
   	    
   	    @Override
   	    public void run() {
   	     List<TrankBean>  list = new ArrayList<TrankBean>();
   	     org.jsoup.nodes.Document  document = null;
   	    	// TODO Auto-generated method stub
   	    	for(TrankBean row:expressnoList){
		        if(!row.getExpressno().matches(".*\\d+.*")){
		        	 continue;
		        }

		        if(company.equalsIgnoreCase("shunfeng")){
					  try {
					      //获取指定网址的页面内容  shunfeng
						  Response res = Jsoup.connect("https://m.kuaidi100.com/query?type=shunfeng&postid="+row.getExpressno()+"&id=1&valicode=&temp=0.8255025921389461").timeout(50000).execute();
						  String body = res.body();
						  JSONObject jsonObjct = JSONObject.parseObject(body);
						  String data = jsonObjct.getString("data");
						  System.out.println(data);
						  JSONArray jsonArray = JSONArray.parseArray(data);
						  List<Map<String, String>> mapListJson = (List) jsonArray;
						  String time  ="";
						  String context ="";
								  
						  String time2 ="";
						  String context2 ="";
						  if(mapListJson.size()>1){
							 //最新跟踪物流信息
							   time = mapListJson.get(0).get("time");
							   context = mapListJson.get(0).get("context");
						  
							  //刚开始物流信息
							  time2 = mapListJson.get(mapListJson.size()-1).get("time");
							  context2 = mapListJson.get(mapListJson.size()-1).get("context");
							  
							//先判断寄件有没有超过十天
					            if(!context.contains("已签收")){
					            	 //从寄件开始到现在的时间间隔
					            	 int moreTime;
									try {
										moreTime = daysBetween(sdf.parse(time2.trim()),new Date());
										 if(moreTime>10){
						            		 TrankBean bean = new TrankBean();
						            		 bean.setFlag(1);
						            		 bean.setExpressno(row.getExpressno());
						            		 bean.setCompany(company);
						            		 bean.setAdmName(row.getAdmName());
						            		 bean.setRemarks(row.getRemarks());
						            		 bean.setCreatetime(row.getCreatetime());
						            		 list.add(bean);
						            	 }
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					            	
					            }
					            
				            if(!context.contains("已签收")){
				            	//最新运单状态到现在的时间间隔
				            	 int days;
								try {
									days = daysBetween(sdf.parse(time.trim()),new Date());
									 if(days>3){
					            		 TrankBean bean = new TrankBean();
					            		 bean.setExpressno(row.getExpressno());
					            		 bean.setFlag(2);
					            		 bean.setCompany(company);
					            		 bean.setAdmName(row.getAdmName());
					            		 bean.setRemarks(row.getRemarks());
					            		 bean.setCreatetime(row.getCreatetime());
					            		 list.add(bean);
					            	 }
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				            	
				            }
						  }
						  
						  if(mapListJson.size()==1){
							   time = mapListJson.get(0).get("time");
							   context = mapListJson.get(0).get("context");
							   if(!context.contains("已签收")){
					            	//最新运单状态到现在的时间间隔
					            	 int days;
									try {
										days = daysBetween(sdf.parse(time.trim()),new Date());
										 if(days>3){
						            		 TrankBean bean = new TrankBean();
						            		 bean.setExpressno(row.getExpressno());
						            		 bean.setFlag(2);
						            		 bean.setCompany(company);
						            		 bean.setAdmName(row.getAdmName());
						            		 bean.setRemarks(row.getRemarks());
						            		 bean.setCreatetime(row.getCreatetime());
						            		 list.add(bean);
						            	 }
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					            }
						  }
						  System.out.println(time+"----"+context);
					  } catch (IOException e) {
					      e.printStackTrace();
					  }
				}	
   	    }
   	   //将数据插入数据库
   	    	if(list.size()>0){
   	    		trackService.saveTrackInfo(list);
   	    	}else{
   	    		List<TrankBean>  list1 = new ArrayList<TrankBean>();
   	    		TrankBean bean = new TrankBean();
   	    		bean.setFlag(3);
   	    		bean.setCompany(company);
   	    		list1.add(bean);
   	    		trackService.saveTrackInfo(list1);
   	    	}
   	    }
   	}
   	
    @RequestMapping("select")
    @ResponseBody
   	public String  selectInfo(HttpServletRequest request, HttpServletResponse response){
          String company = request.getParameter("company");
          String admName = request.getParameter("admName");
          admName = admName.equals("all")?null:admName;
          List<TrankBean>  list = trackService.selectByCompany(company);
          String result ="";
          String json = "";
          if(list.size()==1&&list.get(0).getFlag()==3){
        	  result ="noproblem";
        	  json = JSON.toJSONString(result);
        	  return json;
          } else if(list.size()==0){
        	  result ="undown";
        	  json = JSON.toJSONString(result);
        	  return json;
          }else{
        	  list = trackService.selectByCompany1(company,admName);
        	  json = JSON.toJSONString(list);
        	  return json;
          }
   	}
    
    
    
    //根据
    @RequestMapping(value = "/trackinfoByExpressnoAndCompany")
    public String  trackinfoByExpressnoAndCompany(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
    	String companyAndExpressno = request.getParameter("companyAndExpressno") ;
    	String company = companyAndExpressno.split("@")[0].trim();
    	String expressno = companyAndExpressno.split("@")[1].trim();
    	String  id  = request.getParameter("id");
    	List<TrankBean> trackList = new ArrayList<TrankBean>();
    	TrankBean bean = new TrankBean();
    	bean.setExpressno(expressno);
    	bean.setCompany(company);
    	if(StrUtils.isNotNullEmpty(expressno)){
    		//JCEX
    		 if(company.equalsIgnoreCase("JCEX")){
    			 StringBuffer sb = new StringBuffer("http://api.jcex.com/JcexJson/api/notify/sendmsg?service=track&data_body=");
			    	String  js ="{\"customerid\":\"-1\",\"waybillnumber\":\""+expressno.trim()+"\",\"isdisplaydetail\":\"true\"}";
			    	sb.append(Base64.encode(js.getBytes("UTF-8")));
			    	StringBuilder json = new StringBuilder();
					try {
						URL urlObject = new URL(sb.toString());
						URLConnection uc = urlObject.openConnection();

						BufferedReader in = new BufferedReader(
								new InputStreamReader(uc.getInputStream(),
										"UTF-8"));
						String inputLine = null;
						while ((inputLine = in.readLine()) != null) {
							json.append(inputLine);
						}
						in.close();
					} catch (MalformedURLException e) {
						 LOG.error("对接JCEX API获取物流信息有误【运单号:"+expressno+"】", e);
						 bean.setErrorInfo("No result found for your query. Please try later.");
					} catch (IOException e) {
						 LOG.error("对接JCEX API获取物流信息有误【运单号:"+expressno+"】", e);
						 bean.setErrorInfo("No result found for your query. Please try later.");
					}

					try {
						String retStr = json.toString();
						retStr = retStr.substring(retStr.indexOf("statusdetail")+14,retStr.length()-3).trim();
						System.out.println(retStr);
						JSONArray jsonArray = JSONArray
								.parseArray(retStr);
						
						List<Map<String, String>> mapListJson = (List) jsonArray;
						for(int i = mapListJson.size()-1; i>=0;i--){
							Map<String, String> obj = mapListJson.get(i);
							System.out.println(obj.get("time") + "----【"+obj.get("locate")+"】    "+
									  obj.get("status"));
								TrankBean trackBean = new TrankBean();
					            trackBean.setCreatetime(obj.get("time"));
					            String ProcessNotes ="【"+obj.get("locate")+"】  "+ obj.get("status");
					            trackBean.setContext(ProcessNotes);
					            trackBean.setExpressno(expressno);
					            trackBean.setCompany(company);
					            trackList.add(trackBean);
						}
					} catch (Exception e) {
						 LOG.error("对接JCEX API获取物流信息有误【运单号:"+expressno+"】", e);
						 bean.setErrorInfo("No result found for your query. Please try later.");
					}
    		 }
    		//原飞航
              if(company.equalsIgnoreCase("yfh")){
            	  String urlString = "http://www.yfhex.com/ServicePlatform/track?num="+expressno;
					urlString = urlString.replaceAll(" ", "");
					StringBuilder json = new StringBuilder();
					try {
						URL urlObject = new URL(urlString);
						URLConnection uc = urlObject.openConnection();

						BufferedReader in = new BufferedReader(
								new InputStreamReader(uc.getInputStream(),
										"UTF-8"));
						String inputLine = null;
						while ((inputLine = in.readLine()) != null) {
							json.append(inputLine);
						}
						in.close();
					} catch (MalformedURLException e) {
						LOG.error("原飞航获取物流信息有误【运单号:"+expressno+"】【运输方式:"+company+"】", e);
						 bean.setErrorInfo("No result found for your query. Please try later.");
					} catch (IOException e) {
						LOG.error("原飞航获取物流信息有误【运单号:"+expressno+"】【运输方式:"+company+"】", e);
						bean.setErrorInfo("No result found for your query. Please try later.");
					}
					try {
						String retStr = json.toString();

						JSONArray jsonArray = JSONArray
								.parseArray(retStr);

						List<Map<String, String>> mapListJson = (List) jsonArray;
						for (int i =0;i<mapListJson.size(); i++) {
							Map<String, String> obj = mapListJson.get(i);
							System.out.println(obj.get("ProcessTime") + "----"+obj.get("FlowStep")+"------"
									+ obj.get("ProcessNotes"));
							
							   TrankBean trackBean = new TrankBean();
					            String ProcessNotes = obj.get("ProcessNotes");
					            String FlowStep = obj.get("FlowStep");
					            trackBean.setCreatetime(obj.get("ProcessTime"));
						        trackBean.setContext(FlowStep+"-----"+ProcessNotes);
						        trackBean.setExpressno(expressno);
						        trackBean.setCompany(company);
					            trackList.add(trackBean);               
						}
					} catch (Exception e) {
						LOG.error("原飞航获取物流信息有误【运单号:"+expressno+"】【运输方式:"+company+"】", e);
						 bean.setErrorInfo("No result found for your query. Please try later.");
					}
    		 }
    		//邮政
              if(company.equalsIgnoreCase("emsinten")){
            	  String urlstr = "http://track.api.cnexps.com/cgi-bin/GInfo.dll?EmsApiTrack&cno="+expressno;
			    	String str = fun(urlstr);
			    	if("-102".equals(str) || "-9".equals(str)){
			    		bean.setErrorInfo("No result found for your query. Please try later.");
				    	trackList.add(bean);
					}else{
						if (str.indexOf("<TRACK_DATA>") != -1) {
							str = str.substring(str.indexOf("<TRACK_DATA>"),
									str.indexOf("</TRACK_DATA>") + "</TRACK_DATA>".length());
						}
						if(str.contains("<TRACK_DATA>")){
						SAXBuilder sb = new SAXBuilder(false);
						StringReader read = new StringReader(str);
						Document doc = null;
						System.out.println(str);
						try {
							doc = sb.build(read);
							Element books = doc.getRootElement();
							List datatime = books.getChildren("DATETIME");
							List place = books.getChildren("PLACE");
							List info = books.getChildren("INFO");
							Iterator iterdatatime = datatime.iterator();
							Iterator iterplace = place.iterator();
							Iterator iterInfo = info.iterator();
							List<TrankBean> templist = new ArrayList<TrankBean>();
							while (iterdatatime.hasNext()) {
								Element book = (Element) iterdatatime.next();
								Element book2 = (Element) iterplace.next();
								Element book3 = (Element) iterInfo.next();
								String sj = book.getText();
								String xinxi = book2.getText();
								String zt = book3.getText();
								
								TrankBean trackBean = new TrankBean();
						        trackBean.setCreatetime(sj);
						        trackBean.setContext(xinxi+"-----"+zt);
						        trackBean.setExpressno(expressno);
						        trackBean.setCompany(company);
						        trackList.add(trackBean);
								iterdatatime.hasNext();
							}
							Collections.reverse(trackList);
						} catch (JDOMException e) {
							LOG.error("对接邮政 api 获取物流信息有误【运单号:"+expressno+"】【运输方式:"+company+"】", e);
						} catch (IOException e) {
							LOG.error("对接邮政 api 获取物流信息有误【运单号:"+expressno+"】【运输方式:"+company+"】", e);
						}
						}
					}
     		 }
    		//顺丰
              if(company.equalsIgnoreCase("shunfeng")){
            	  org.jsoup.nodes.Document document = null;
				  try {
				      //获取指定网址的页面内容  shunfeng
					  Response res = Jsoup.connect("https://m.kuaidi100.com/query?type=shunfeng&postid="+expressno+"&id=1&valicode=&temp=0.8255025921389461").timeout(50000).execute();
					  String body = res.body();
					 
					  JSONObject jsonObjct = JSONObject.parseObject(body);
					  String data = jsonObjct.getString("data");
					  System.out.println(data);
					  JSONArray jsonArray = JSONArray
								.parseArray(data);
					  List<Map<String, String>> mapListJson = (List) jsonArray;
					  for(int i=0; i<mapListJson.size(); i++){
						  String time = mapListJson.get(i).get("time");
						  String location = mapListJson.get(i).get("location");
						  String context = mapListJson.get(i).get("context");
						        
						  if(location!=null){
							  location = location.replaceAll("【", "");
							  location =  location.replaceAll("】", "");
						  }else{
							  location="";
						  }
						  
						  context = context.replaceAll("【", "");                                        
						  context = context.replaceAll("】", "");
						  
						  TrankBean trackBean = new TrankBean();
				          trackBean.setCreatetime(time);
				          trackBean.setContext(location+"  "+context);
				          trackBean.setExpressno(expressno);
				          trackBean.setCompany(company);
				          trackList.add(trackBean);
						//  System.out.println(time+"---"+location+"---"+context);
					  }
					 
				  } catch (IOException e) {
					  bean.setErrorInfo("No result found for your query. Please try later.");
				      e.printStackTrace();
				  }
     		 }
    	}
    	if(trackList.size()>0){
    		bean.settList(trackList);
    		//更新状态已读
    		trackService.updateReadFlag(expressno);
    	}
    	request.setAttribute("trackBean", bean);
    	return "wuliu";
    }
    @RequestMapping(value ="/saveBzByExpressnoAndCompany")
    @ResponseBody
    public String saveBzByExpressnoAndCompany(HttpServletRequest request, HttpServletResponse response){
    	String bz = request.getParameter("bz");
    	String id = request.getParameter("id");
    	int ret = trackService.saveBzByExpressnoAndCompany(bz,Integer.parseInt(id));
    	String result = "false";
    	if(ret==1){
    		result = "success";
    	}
    	String json = JSON.toJSONString(result);
    	return json;
    }

    
    @RequestMapping("respider")
    @ResponseBody
    public String respider(HttpServletRequest request, HttpServletResponse response){
    	String company = request.getParameter("company");
    	String admName = null;
    	List<TrankBean>  tlist = trackService.selectByCompany1(company,admName);
    	List<TrankBean>  list = new ArrayList<TrankBean>();
    	org.jsoup.nodes.Document document = null;
        for(TrankBean bean:tlist){
        	if(!bean.getExpressno().matches(".*\\d+.*")){
	        	 continue;
	        }
			//JCEX
			if(!company.equalsIgnoreCase("JCEX")){
				try {
				float ti = System.currentTimeMillis();
				document= Jsoup.connect("http://www.jcex.cn/Tracking-Result2.asp?WLYD_NUM="+bean.getExpressno()+"&act=detail").timeout(50000).get();
				Elements  es = document.select("div[class=RightContentText]").select("div[class=TextControl]").select("table"); 
				if(!es.isEmpty()){
			    	  Elements trs = document.select("div[class=RightContentText]").select("table").get(1).select("tr"); 
			          int size = trs.size();
			          if(size>2){
			        	  //顶头 的最新跟踪数据  
				    	  Elements tds = trs.get(1).select("td");
				            String s1 = tds.get(0).text(); //时间
				            String s2 = tds.get(1).text();  // 站点
				            String s3 = tds.get(2).text();  // 状态
				           //最开始的第一天数据
				          Elements tr = trs.get(size-1).select("td");
				            String td1 = tr.get(0).text(); //时间
				            String td2 = tr.get(1).text();  // 站点
				            String td3 = tr.get(2).text();  // 状态
				           
				            //先判断寄件有没有超过十天
				            if(!s3.equals("签收")){
				            	 //从寄件开始到现在的时间间隔
				            	 int moreTime= daysBetween(sdf.parse(td1.substring(1).trim()),new Date());
				            	 if(moreTime>10){
				            		 list.add(bean);
				            	 }
				            }
				            if(!s3.equals("签收")){
				            	//最新运单状态到现在的时间间隔
				            	 int days = daysBetween(sdf.parse(s1.substring(1).trim()),new Date());
				            	 if(days>3){
				            		 list.add(bean);
				            	 }
				            }
			          }
			          
			          if(size==1){
			        	//顶头 的最新跟踪数据  
				    	  Elements tds = trs.get(1).select("td");
				            String s1 = tds.get(0).text(); //时间
				            String s2 = tds.get(1).text();  // 站点
				            String s3 = tds.get(2).text();  // 状态
			        	  if(!s3.equals("签收")){
				            	//最新运单状态到现在的时间间隔
				            	 int days = daysBetween(sdf.parse(s1.substring(1).trim()),new Date());
				            	 if(days>3){
				            		 list.add(bean);
				            	 }
				            }
			          }
			    	 
			      }
			} catch (Exception e) {
				// TODO: handle exception
			}
			}
			//原飞航
			if(company.equalsIgnoreCase("原飞航")){
				String urlString = "http://www.yfhex.com/ServicePlatform/track?num="+bean.getExpressno();
				urlString = urlString.replaceAll(" ", "");
				StringBuilder json = new StringBuilder();
				try {
					URL urlObject = new URL(urlString);
					URLConnection uc = urlObject.openConnection();

					BufferedReader in = new BufferedReader(
							new InputStreamReader(uc.getInputStream(),
									"UTF-8"));
					String inputLine = null;
					while ((inputLine = in.readLine()) != null) {
						json.append(inputLine);
					}
					in.close();
				} catch (MalformedURLException e) {
//					e.printStackTrace();
				} catch (IOException e) {
//					e.printStackTrace();
				}

				String retStr = json.toString();
				
				System.out.println(retStr);

				JSONArray jsonArray = JSONArray
						.parseArray(retStr);

				List<Map<String, String>> mapListJson = (List) jsonArray;
				
				if(mapListJson.size()>1){
					//最新的快件信息
					Map<String, String> obj = mapListJson.get(0);
					String t1 = obj.get("ProcessTime") ; // 时间
					String s1 =obj.get("FlowStep");  // 状态
					//最开始的快件信息
					Map<String, String> obj2 = mapListJson.get(mapListJson.size()-1);
					String t2 = obj2.get("ProcessTime") ; // 时间
					String s2 =obj2.get("FlowStep");  // 状态
	
		            //先判断寄件有没有超过十天
		            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
		            	 //从寄件开始到现在的时间间隔
		            	 int moreTime;
						try {
							moreTime = daysBetween(sdf.parse(t2.trim()),new Date());
							if(moreTime>10){
			            		 list.add(bean);
			            	 }
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
		            	//最新运单状态到现在的时间间隔
		            	 int days;
						try {
							days = daysBetween(sdf.parse(t1.trim()),new Date());
							 if(days>3){
			            		 list.add(bean);
			            	 }
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            }
				}
				if(mapListJson.size()==1){
					//最新的快件信息
					Map<String, String> obj = mapListJson.get(0);
					String t1 = obj.get("ProcessTime") ; // 时间
					String s1 =obj.get("FlowStep");  // 状态
					if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("貨件完成送達"))){
		            	//最新运单状态到现在的时间间隔
		            	 int days;
						try {
							days = daysBetween(sdf.parse(t1.trim()),new Date());
							if(days>3){
			            		 list.add(bean);
			            	 }
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	 
		            }
				}
				
			}
			//邮政 
			if("emsinten".equalsIgnoreCase(company)){
				try {
				String urlstr = "http://track.api.cnexps.com/cgi-bin/GInfo.dll?EmsApiTrack&cno="+bean.getExpressno();
		    	String str = fun(urlstr);
		    	if("-102".equals(str) || "-9".equals(str)){
		    		continue;
				}else{
					if (str.indexOf("<TRACK_DATA>") != -1) {
						str = str.substring(str.indexOf("<TRACK_DATA>"),
								str.indexOf("</TRACK_DATA>") + "</TRACK_DATA>".length());
					}
					SAXBuilder sb = new SAXBuilder(false);
					StringReader read = new StringReader(str);
					Document doc = null;
					System.out.println(str);

					try {
						doc = sb.build(read);
					} catch (JDOMException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}
					Element books = doc.getRootElement();
					List datatime = books.getChildren("DATETIME");
					List place = books.getChildren("PLACE");
					List info = books.getChildren("INFO");

					Iterator iterdatatime = datatime.iterator();
					Iterator iterplace = place.iterator();
					Iterator iterInfo = info.iterator();
					int count = 0;
					String sj="";
					String xinxi="";
					String zt="";
					
					String sj2="";
					String xinxi2="";
					String zt2="";
					while (iterdatatime.hasNext()) {
						count ++;
						Element book = (Element) iterdatatime.next();
						Element book2 = (Element) iterplace.next();
						Element book3 = (Element) iterInfo.next();
						if(count==1){
							  sj = book.getText();
							  xinxi = book2.getText();
							  zt = book3.getText();
						}else{
							 if(!iterdatatime.hasNext()) {//最后一个元素
								  sj2 = book.getText();
								  xinxi2 = book2.getText();
								  zt2 = book3.getText();
								 
						      }
						}
						iterdatatime.hasNext();
					}
					
					System.out.println("state"+zt2);
					//先判断寄件有没有超过十天
		            if(!(zt2.contains("妥投")||zt2.contains("投递并签收"))){
		            	if(StrUtils.isNotNullEmpty(sj)){
		            	 //从寄件开始到现在的时间间隔
		            	 int moreTime= daysBetween(sdf.parse(sj.trim()),new Date());
		            	 if(moreTime>10){
		            		 list.add(bean);
		            	 }
		            	}
		            }
		            
		            if(!(zt2.contains("妥投")||zt2.contains("投递并签收"))){
		            	  if(StrUtils.isNotNullEmpty(sj2)){
		            		//最新运单状态到现在的时间间隔
				            	 int days = daysBetween(sdf.parse(sj2.trim()),new Date());
				            	 if(days>3){
				            		 list.add(bean);
				            	 }
		            	  }
		            }
				}
		    	
				} catch (Exception e) {
					// TODO: handle exception
				}
				
		}
	     //顺丰
		if(company.equalsIgnoreCase("shunfeng")){
			  try {
			      //获取指定网址的页面内容  shunfeng
				  Response res = Jsoup.connect("https://m.kuaidi100.com/query?type=shunfeng&postid="+bean.getExpressno()+"&id=1&valicode=&temp=0.8255025921389461").timeout(50000).execute();
				  String body = res.body();
				  JSONObject jsonObjct = JSONObject.parseObject(body);
				  String data = jsonObjct.getString("data");
				  System.out.println(data);
				  JSONArray jsonArray = JSONArray.parseArray(data);
				  List<Map<String, String>> mapListJson = (List) jsonArray;
				  String time  ="";
				  String context ="";
						  
				  String time2 ="";
				  String context2 ="";
				  if(mapListJson.size()>1){
					 //最新跟踪物流信息
					   time = mapListJson.get(0).get("time");
					   context = mapListJson.get(0).get("context");
				  
					  //刚开始物流信息
					  time2 = mapListJson.get(mapListJson.size()-1).get("time");
					  context2 = mapListJson.get(mapListJson.size()-1).get("context");
					  
					//先判断寄件有没有超过十天
			            if(!context.contains("已签收")){
			            	 //从寄件开始到现在的时间间隔
			            	 int moreTime;
							try {
								moreTime = daysBetween(sdf.parse(time2.trim()),new Date());
								 if(moreTime>10){
				            		 list.add(bean);
				            	 }
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }
			            
		            if(!context.contains("已签收")){
		            	//最新运单状态到现在的时间间隔
		            	 int days;
						try {
							days = daysBetween(sdf.parse(time.trim()),new Date());
							if(days>3){
			            		 list.add(bean);
			            	 }
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
				  }
				  
				  if(mapListJson.size()==1){
					   time = mapListJson.get(0).get("time");
					   context = mapListJson.get(0).get("context");
					   if(!context.contains("已签收")){
			            	//最新运单状态到现在的时间间隔
			            	 int days;
							try {
								days = daysBetween(sdf.parse(time.trim()),new Date());
								if(days>3){
				            		 list.add(bean);
				            	 }
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	 
			            }
				  }
				  System.out.println(time+"----"+context);
				 
				 
			  } catch (IOException e) {
			      e.printStackTrace();
			  }
		}
        }
        
        List<TrankBean>  newList = new ArrayList<TrankBean>();
        for(TrankBean bean:tlist){
        	 int flag = 0;
        	 for(TrankBean row:list){
        		 if(bean.getExpressno().equals(row.getExpressno())){
        			 flag = 1;
        			 break;
        		 }
        	 }
        	 if(flag ==0){
        		 newList.add(bean);
        	 }
        }
        
        //更新track
        int ret = 0;
        if(newList.size()>0){
        	ret= trackService.deleteInfoList(newList);
        }
        String result = ret==newList.size()?"true":"false";
        String json  = JSON.toJSONString(result);
    	return json;
    }
    
    @RequestMapping("getadmInfo")
    @ResponseBody
    public String getadmInfo(HttpServletRequest request , HttpServletResponse response){
    	List<String>  list = trackService.getadmInfo();
    	String json = JSON.toJSONString(list);
    	return json;
    }
    
    
    
    
    /**
     * 物流反馈页面显示延迟物流信息
     * 
     */
    @RequestMapping("showAllTrackInfo")
    @ResponseBody
    public EasyUiJsonResult  showAllTrackInfo(HttpServletRequest request, HttpServletResponse response){
    	EasyUiJsonResult json = new EasyUiJsonResult();
    	String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
//		if(adm == null){
//			return "main_login";
//		}
		String  pagen = request.getParameter("page");
		String  transportcompany = request.getParameter("transportcompany");
		transportcompany=transportcompany==""?null:transportcompany;
		if(transportcompany!=null){
			if(transportcompany.equals("yfh")){
				transportcompany ="原飞航";
			}
		}
		if(StrUtils.isNullOrEmpty(pagen)){
			pagen="1";
		}
		int page = Integer.parseInt(pagen);
		String  readFlag = request.getParameter("readFlag");
		if(readFlag==null||readFlag==""){
			readFlag = null;
		}
		int  admId = adm.getId();
		if(admId==56){
			 admId = 1;
		}
		List<TrackInfoPojo> list1=new ArrayList<TrackInfoPojo>();
		List<Map<String,String>>   list =  trackService.selectTrackInfoByAdmId(admId,readFlag,page,transportcompany);
		for (Map<String, String> map : list) {
			TrackInfoPojo t=new TrackInfoPojo();
			t.setOrder_no(map.get("order_no"));
			t.setLink_url(map.get("link_url"));
			t.setSend_content(map.get("send_content"));
			t.setReservation3("<input type='text'  value='"+(map.get("reservation3")==null || "".equals(map.get("reservation3"))?"":map.get("reservation3"))+"'  disabled='disabled'>");
			t.setIs_read("N".equals(map.get("is_read"))?"未读":"已读");
			t.setOperation("<button onclick=\"reply('"+map.get("order_no")+"','"+String.valueOf(map.get("id"))+"')\">新增备注</button>");
			list1.add(t);
		}
		int  Allcount = 0 ;
		int  totalCount = 0;
		if(list.size()>0){
			 totalCount = Integer.parseInt(list.get(0).get("count"));
			 Allcount = Integer.parseInt(list.get(0).get("count"));
		     Allcount = Allcount%40==0?Allcount/40:Allcount/40+1; 
		}
		
//		request.setAttribute("readFlag", readFlag);
//		request.setAttribute("transportcompany", transportcompany);
//		request.setAttribute("currentpage", page);
//		request.setAttribute("totalpage", Allcount);
//		request.setAttribute("totalCount", totalCount);
//		request.setAttribute("pageCount", 40);
//		request.setAttribute("list", list);
//		request.setAttribute("readFlag", readFlag);
		json.setRows(list1);
		json.setTotal(totalCount);
		return json;
    }

    /**
     * 更新备注信息
     */
    @RequestMapping("updateBz")
    @ResponseBody
    public String  updateBz(HttpServletRequest request, HttpServletResponse response){
        int  id= Integer.parseInt(request.getParameter("id"));
        String result = "false";
        String bz = request.getParameter("bz");
        String expressno = request.getParameter("expressno");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null){
			return "main_login";
		}
		bz = adm.getAdmName()+":"+bz+";" ;
        int ret = trackService.updateBzById(id,bz,expressno);
        if(ret>0){
        	result ="success";
        }
        String json = JSON.toJSONString(result);
    	return  json;
    }
    
}
    
