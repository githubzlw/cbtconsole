package com.cbt.warehouse.service;

import com.cbt.parse.service.StrUtils;
import com.cbt.warehouse.dao.TrackDao;
import com.cbt.warehouse.pojo.TrankBean;
import com.cbt.warehouse.util.Utility;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TrackServiceImpl  implements  TrackService {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger("track");
	
	@Autowired
	private TrackDao  trackDao;
	
	@Override
	public void saveTrackInfo(List<TrankBean> list) {
		// TODO Auto-generated method stub
		List<TrankBean>  newList = new ArrayList<TrankBean>();
		//插入数据前先对数据进行判断
		for(TrankBean bean:list){
			String company = bean.getCompany();
			String expressno = bean.getExpressno();
			int count  = trackDao.selectByExpressnoAndCompany(company,expressno);
			if(count>0){
				continue;
			}
			newList.add(bean);
		}
		if(newList.size()>0){
			trackDao.saveTrackInfo(newList);
		}
	}
	
	@Override
	public List<TrankBean> getAllJCEX(String company,String senttimeBegin ,String senttimeEnd) {
		// TODO Auto-generated method stub
		return trackDao.getAllJCEX(company,senttimeBegin,senttimeEnd);
	}


	@Override
	public void deleteInfoByJCEX(String company) {
		// TODO Auto-generated method stub
		trackDao.deleteInfoByJCEX(company);
	}


	@Override
	public List<TrankBean> selectByCompany(String company) {
		// TODO Auto-generated method stub
		return trackDao.selectByCompany(company);
	}


	@Override
	public List<TrankBean> selectByCompany1(String company,String admName) {
		// TODO Auto-generated method stub
		return trackDao.selectByCompany1(company,admName);
	}


	@Override
	public int  saveBzByExpressnoAndCompany(String bz, int id) {
		// TODO Auto-generated method stub
		return trackDao.saveBzByExpressnoAndCompany(bz,id);
	}


	@Override
	public int deleteInfoList(List<TrankBean> newList) {
		// TODO Auto-generated method stub
		return trackDao.deleteInfoList(newList);
	}

	@Override
	public List<String> getadmInfo() {
		// TODO Auto-generated method stub
		return trackDao.getadmInfo();
	}

	/**
     * 定时抓取前三个月未完结订单的物流状态,并做判断
     * description 条件:(三天不更新状态和15天以上没有签收   通知销售、仓库)
     */
	@Override
	public void getAllTracks() {
		// TODO Auto-generated method stub
		//根据条件要求获取所有的出运中订单
    	//当前时间的前三个月的订单 
    	Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例  
    	ca.setTime(new Date()); // 设置时间为当前时间  
    	ca.add(Calendar.MONTH, -3);// 月份减3  
    	Date resultDate = ca.getTime(); // 结果  
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
    	System.out.println(sdf.format(resultDate));
    	String  date = sdf.format(resultDate);
		List<TrankBean>  list =  trackDao.getAllTracks(date);
		//根据订单的出运方式选择不同运输公司的抓取物流信息方式,并判断满足条件的订单信息,统计起来

    	String transportcompany ="";
    	
    	List<TrankBean>  emsList = new ArrayList<TrankBean>();
    	List<TrankBean>  jecxList = new ArrayList<TrankBean>();
    	List<TrankBean>  yfhList = new ArrayList<TrankBean>();
    	List<TrankBean>  ztoList = new ArrayList<TrankBean>();
    	List<TrankBean>  otherList = new ArrayList<TrankBean>();
    	for(TrankBean  bean:list){
    		transportcompany = bean.getCompany();
    		if(transportcompany==null){
    			continue;
    		}
    	    //判断该运单号属于哪个运输方式
    		 if(transportcompany.equals("emsinten")){ //邮政
    			 emsList.add(bean); 
    		 }else if(transportcompany.equals("JCEX")){ // 佳成
    			 jecxList.add(bean);
    		 }else if(transportcompany.equals("原飞航")){   //原飞航
    			 yfhList.add(bean);
    		 }else if(transportcompany.equals("zto")){
    			 ztoList.add(bean);
    		 }/*else{  //其他  TNT、DHL 、UPS
    			 otherList.add(bean);
    		 }*/
    	}
    	//异步线程处理
    	if(emsList.size()>0){
    		YZThreads  th = new YZThreads(emsList, "emsinten");
    		th.start();
    	}
    	if(jecxList.size()>0){
    		 JCEXThreads  thread = new JCEXThreads(jecxList,"JCEX");
    		 thread.start();
    	}
    	if(yfhList.size()>0){
    		YFHThreads  thread = new YFHThreads(yfhList,"原飞航");
    		thread.start();
    	}
    	if(ztoList.size()>0){
    		ZTOThreads  thread = new ZTOThreads(ztoList,"中通");
    		thread.start();
    	}
	}

	@Override
	public void saveBugTrack(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		//推送消息给销售
		trackDao.saveBugTrack(list);
		// 推送消息给ling
		for(Map<String, Object>  map:list){
			String reservation2 = map.get("order_no")+"/1/"+map.get("type");
			map.put("reservation2", reservation2);
		}
		trackDao.saveBugTrack1(list);
		for(Map<String, Object>  map:list){
			String reservation2 = map.get("order_no")+"/15/"+map.get("type");
			map.put("reservation2", reservation2);
		}
		//推送消息给eric
		trackDao.saveBugTrack2(list);
	}

	@Override
	public List<Map<String,String>>  selectTrackInfoByAdmId(Integer id,String readFlag,int page,String transportcompany) {
		int start = (page-1)*40 ;
		List<Map<String,String>>  list = trackDao.selectTrackInfoByAdmId(id,readFlag,start,transportcompany);
		int count = 0 ;
		if(list.size()>0){
			count = trackDao.countSelectTrackInfoByAdmId(id,readFlag,transportcompany);
			list.get(0).put("count", String.valueOf(count));
		}
		return list;
	}
	
	
	
  /*** ---------------------------------------------------------------------------------------***/
    
    class YZThreads  extends Thread{

		private List<TrankBean>  expressnoList ;
		private String company ;
		
		private   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    public YZThreads(List<TrankBean> expressnoList,String company) {
		// TODO Auto-generated constructor stub
	    	this.expressnoList=expressnoList;
	    	this.company = company;
	    }
	    
	    @Override
	    public void run() {
	   
	     List<Map<String,Object>>	list = new ArrayList<Map<String,Object>>();
	     Map<String,Object>  map  = null; 
	    	// TODO Auto-generated method stub
		for(TrankBean row:expressnoList){
		        if(!row.getExpressno().matches(".*\\d+.*")){
		        	 continue;
		        }
				// 
				if(company.equalsIgnoreCase("emsinten")){
					try {
						String urlstr = "http://track.api.cnexps.com/cgi-bin/GInfo.dll?EmsApiTrack&cno="+row.getExpressno();
				    	String str = Utility.fun(urlstr);
				    	if("-102".equals(str) || "-9".equals(str)){
				    		continue;
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
								} catch (JDOMException e) {
									LOG.error("邮政对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
								} catch (IOException e) {
									LOG.error("邮政对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
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
								
								if(StrUtils.isNotNullEmpty(sj2)){
									//先判断寄件有没有超过十天 Delivered
						            if(!(zt2.contains("派送完成")||zt2.contains("妥投")||zt2.contains("投递并签收")||zt2.contains("送达")||zt2.toLowerCase().contains("delivered")||zt2.contains("已签收")||zt2.contains("已自提")||zt2.contains("已派送并签收"))){
						            	if(StrUtils.isNotNullEmpty(sj)){
						            	 //从寄件开始到现在的时间间隔
						            		String tracktime = sj.trim();
						            		  if(tracktime.contains(":")){
						            			  tracktime = tracktime.substring(0, tracktime.indexOf(" ")).trim()+" 00:00:00";
						            		  }
						            	 int moreTime= Utility.daysBetween(sdf.parse(tracktime),new Date());
						            	 if(moreTime>15){
						            		 map = new HashMap<String,Object>();
						            		 map.put("order_no", row.getExpressno());
						            		 map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
						            		 map.put("sender_id",row.getAdmId());             //接收人
						            		 map.put("send_type", 10);                        //类型
						            		 map.put("type", 15);
						            		 map.put("send_content","物流反馈,订单号["+row.getOrderid()+"],运单号["+row.getExpressno()+"]十五天没签收 ,最新物流信息:"+zt2 );
						            		 map.put("create_time", sdf.format(new Date()));  //创建时间
						            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+15);
						            		 list.add(map);
						            	 }
						            	}
						            }
						            
						            if(!(zt2.contains("派送完成")||zt2.contains("妥投")||zt2.contains("投递并签收")||zt2.contains("送达")||zt2.toLowerCase().contains("delivered")||zt2.contains("已签收")||zt2.contains("已自提")||zt2.contains("已派送并签收"))){
						            	  if(StrUtils.isNotNullEmpty(sj2)){
						            		//最新运单状态到现在的时间间隔
							            		  String tracktime = sj2.trim();
							            		  if(tracktime.contains(":")){
							            			  tracktime = tracktime.substring(0, tracktime.indexOf(" ")).trim()+" 00:00:00";
							            		  }
								            	 int days = Utility.daysBetween(sdf.parse(tracktime),new Date());
								            	 if(days>3){
								            		 map = new HashMap<String,Object>();
								            		 map.put("order_no", row.getExpressno());
								            		  map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
								            		 map.put("sender_id",row.getAdmId());             //接收人
								            		 map.put("type", 3);
								            		 map.put("send_type", 10);                        //类型
								            		 map.put("send_content","物流反馈,订单号["+row.getOrderid()+"],运单号["+row.getExpressno()+"]三天未更新状态 ,最新物流信息:"+zt2 );
								            		 map.put("create_time", sdf.format(new Date()));  //创建时间
								            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
								            		 list.add(map);
								            	 }
						            	  }
						            }
								}
								if(StrUtils.isNullOrEmpty(sj2)){
						            if(!(zt2.contains("派送完成")||zt2.contains("妥投")||zt2.contains("投递并签收")||zt2.contains("送达")||zt2.toLowerCase().contains("delivered")||zt2.contains("已签收")||zt2.contains("已自提")||zt2.contains("已派送并签收"))){
						            	  if(StrUtils.isNotNullEmpty(sj)){
						            		//最新运单状态到现在的时间间隔
						            		      String tracktime = sj.trim();
							            		  if(tracktime.contains(":")){
							            			  tracktime = tracktime.substring(0, tracktime.indexOf(" ")).trim()+" 00:00:00";
							            		  }
								            	 int days = Utility.daysBetween(sdf.parse(tracktime),new Date());
								            	 if(days>3){
								            		 map = new HashMap<String,Object>();
								            		 map.put("order_no", row.getExpressno());
								            		  map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
								            		 map.put("sender_id",row.getAdmId());             //接收人
								            		 map.put("send_type", 10);                        //类型
								            		 map.put("type", 3);
								            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】三天未更新状态 ,最新物流信息:"+zt2 );
								            		 map.put("create_time", sdf.format(new Date()));  //创建时间
								            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
								            		 list.add(map);
								            	 }
						            	  }
						            }
								}
							}
						}
				    	
						} catch (Exception e) {
							LOG.error("邮政对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
						}
			 }
		    }
		
		if(list.size()>0){
			   saveBugTrack(list);
		}
	    }
}
    
    class 	ZTOThreads extends Thread{
    	private List<TrankBean> expressnoList;
    	private String company;
    	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	public ZTOThreads(List<TrankBean> expressnoList, String company) {
    		// TODO Auto-generated constructor stub
    		this.expressnoList = expressnoList;
    		this.company = company;
    	}
    	@Override
    	public void run() {
    		List<Map<String,Object>>	list = new ArrayList<Map<String,Object>>();
		    Map<String,Object>  map  = null; 
		    URL urlObject=null;
		    URLConnection uc=null;
		    for (TrankBean row : expressnoList) {
		    	StringBuilder json = new StringBuilder();
    			StringBuffer sb = new StringBuffer("http://www.17track.net/zh-cn/track?nums="+row.getExpressno()+"");
    			try {
					urlObject = new URL(sb.toString());
					uc = urlObject.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
					String inputLine = null;
					while ((inputLine = in.readLine()) != null) {
						json.append(inputLine);
					}
					in.close();
					String retStr = json.toString();
				} catch (MalformedURLException e) {
					LOG.error("ZTO对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
				} catch (IOException e) {
					LOG.error("ZTO对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
				}finally{
					uc=null;
					urlObject=null;
				}
    		}
    	}
    }
    
    
    
    class JCEXThreads extends Thread {

    	private List<TrankBean> expressnoList;
    	private String company;

    	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	public JCEXThreads(List<TrankBean> expressnoList, String company) {
    		// TODO Auto-generated constructor stub
    		this.expressnoList = expressnoList;
    		this.company = company;
    	}

    	@Override
    	public void run() {
		   List<Map<String,Object>>	list = new ArrayList<Map<String,Object>>();
		    Map<String,Object>  map  = null; 
		    URL urlObject=null;
		    URLConnection uc=null;
    		// TODO Auto-generated method stub
    		for (TrankBean row : expressnoList) {
    			if (!row.getExpressno().matches(".*\\d+.*")) {
    				continue;
    			}
    			// JCEX
    			if (company.equalsIgnoreCase("JCEX")) {
    					StringBuffer sb = new StringBuffer("http://api.jcex.com/JcexJson/api/notify/sendmsg?service=track&data_body=");
				    	String  js ="{\"customerid\":\"-1\",\"waybillnumber\":\""+row.getExpressno()+"\",\"isdisplaydetail\":\"true\"}";
				    	try {
							sb.append(Base64.encode(js.getBytes("UTF-8")));
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							LOG.error("JCEX对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e1);
						}
				    	StringBuilder json = new StringBuilder();
						try {
							urlObject = new URL(sb.toString());
							uc = urlObject.openConnection();

							BufferedReader in = new BufferedReader(
									new InputStreamReader(uc.getInputStream(),
											"UTF-8"));
							String inputLine = null;
							while ((inputLine = in.readLine()) != null) {
								json.append(inputLine);
							}
							in.close();
						} catch (MalformedURLException e) {
							LOG.error("JCEX对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
						} catch (IOException e) {
							LOG.error("JCEX对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
						}finally{
							uc=null;
							urlObject=null;
						}
						try {
							String retStr = json.toString();
							//对于还没物流信息的情况下
							if(retStr.contains("statusdetail")){
								retStr = retStr.substring(retStr.indexOf("statusdetail")+14,retStr.length()-3).trim();
								System.out.println(retStr);
								net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(retStr);
								
								List<Map<String, String>> mapListJson = (List) jsonArray;
								int size = mapListJson.size();
								if(mapListJson.size()>2){
									// 顶头 的最新跟踪数据
									String  s1= mapListJson.get(size-1).get("time"); //时间
									String  s2 =mapListJson.get(size-1).get("locate");   //站点
									String  s3 = mapListJson.get(size-1).get("status");  //状态
									// 最开始的第一天数据
									String  td1= mapListJson.get(0).get("time"); //时间
									String  td2 =mapListJson.get(0).get("locate");   //站点
									String  td3 = mapListJson.get(0).get("status");  //状态
									
									
									// 先判断寄件有没有超过十天  Delivery Received
	    							if (!(s3.toLowerCase().contains("delivery")||s3.toLowerCase().contains("delivered")||s3.toLowerCase().contains("received"))) {
	    								// 从寄件开始到现在的时间间隔
	    								int moreTime = Utility.daysBetween(
	    										sdf.parse(td1.trim()),
	    										new Date());
	    								if (moreTime > 15) {
	    									map = new HashMap<String,Object>();
	    									map.put("order_no", row.getExpressno());
						            		  map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
						            		 map.put("sender_id",row.getAdmId());             //接收人
						            		 map.put("send_type", 10);                        //类型
						            		 map.put("type", 15);
						            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】十五天未签收, 最新物流信息:"+s3 );
						            		 map.put("create_time", sdf.format(new Date()));  //创建时间
						            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+15);
						            		 list.add(map);
	    								}
	    							}
	    							if (!(s3.toLowerCase().contains("delivery")||s3.toLowerCase().contains("delivered")||s3.toLowerCase().contains("received"))) {
	    								// 最新运单状态到现在的时间间隔
	    								int days = Utility.daysBetween(
	    										sdf.parse(s1.trim()),
	    										new Date());
	    								if (days > 3) {
	    									map = new HashMap<String,Object>();
	    									map.put("order_no", row.getExpressno());
						            		  map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
						            		 map.put("sender_id",row.getAdmId());             //接收人
						            		 map.put("send_type", 10);                        //类型
						            		 map.put("type", 3);
						            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】三天未更新状态, 最新物流信息:"+s3 );
						            		 map.put("create_time", sdf.format(new Date()));  //创建时间
						            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
						            		 list.add(map);
	    								}
	    							}
								}
								//
								if (size == 1) {
									// 顶头 的最新跟踪数据
	    							String s1 = mapListJson.get(0).get("time"); // 时间
	    							String s3 = mapListJson.get(0).get("status");// 状态
	    							if (!(s3.toLowerCase().contains("delivery")||s3.toLowerCase().contains("delivered")||s3.toLowerCase().contains("received"))) {
	    								// 最新运单状态到现在的时间间隔
	    								int days = Utility.daysBetween(
	    										sdf.parse(s1.trim()),
	    										new Date());
	    								if (days > 3) {
	    									map = new HashMap<String,Object>();
	    									map.put("order_no", row.getExpressno());
						            		  map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
						            		 map.put("sender_id",row.getAdmId());             //接收人
						            		 map.put("send_type", 10);                        //类型
						            		 map.put("type", 3);
						            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】三天未更新状态, 最新物流信息:"+s3);
						            		 map.put("create_time", sdf.format(new Date()));  //创建时间
						            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
						            		 list.add(map);
	    								}
	    							}
								}
								
							}
						} catch (Exception e) {
							LOG.error("JCEX对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
						}
						
    		}
    		if (list.size() > 0) {
    			saveBugTrack(list);
    		} 
    	}
    		
    }
    }
    
    
    
    
    class YFHThreads  extends Thread{

		private List<TrankBean>  expressnoList ;
		private String company ;
		
		private   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    public YFHThreads(List<TrankBean> expressnoList,String company) {
		// TODO Auto-generated constructor stub
	    	this.expressnoList=expressnoList;
	    	this.company = company;
	}
	    
	    public void run() {

		 List<Map<String,Object>>	list = new ArrayList<Map<String,Object>>();
		 Map<String,Object>  map  = null;
		 URLConnection uc=null;
		 URL urlObject=null;
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
					urlObject = new URL(urlString);
					uc = urlObject.openConnection();

					BufferedReader in = new BufferedReader(
							new InputStreamReader(uc.getInputStream(),
									"UTF-8"));
					String inputLine = null;
					while ((inputLine = in.readLine()) != null) {
						json.append(inputLine);
					}
					in.close();
				} catch (MalformedURLException e) {
					LOG.error(" 原飞航 对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
				} catch (IOException e) {
					LOG.error(" 原飞航 对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
				}finally{
					uc=null;
					urlObject=null;
				}

				String retStr = json.toString();
				
				System.out.println(retStr);

				net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
						.fromObject(retStr);

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
		            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("送达")||s1.contains("貨件完成送達"))){
		            	 //从寄件开始到现在的时间间隔
							int	moreTime = Utility.daysBetween(sdf.parse(t2.trim()),new Date());
							 if(moreTime>15){
								 map = new HashMap<String,Object>();
								 map.put("order_no", row.getExpressno());
			            		 map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
			            		 map.put("sender_id",row.getAdmId());             //接收人
			            		 map.put("send_type", 10);                        //类型
			            		 map.put("type", 15);
			            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】 十五天没签收, 最新物流信息:"+s1 );
			            		 map.put("create_time", sdf.format(new Date()));  //创建时间
			            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+15);
			            		 list.add(map);
			            	 }
		            }
		            if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("送达")||s1.contains("貨件完成送達"))){
		            	//最新运单状态到现在的时间间隔
		            	 int days = Utility.daysBetween(sdf.parse(t1.trim()),new Date());
		            	 if(days>3){
		            		 map = new HashMap<String,Object>();
		            		 map.put("order_no", row.getExpressno());
		            		 map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
		            		 map.put("sender_id",row.getAdmId());             //接收人
		            		 map.put("send_type", 10);                        //类型
		            		 map.put("type", 3);
		            		 map.put("send_content", "物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】 三天未更新状态, 最新物流信息:"+s1 );
		            		 map.put("create_time", sdf.format(new Date()));  //创建时间
		            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
		            		 list.add(map);
		            	 }
		            }
				}
				if(mapListJson.size()==1){
					//最新的快件信息
					Map<String, String> obj = mapListJson.get(0);
					String t1 = obj.get("ProcessTime") ; // 时间
					String s1 =obj.get("FlowStep");  // 状态
					if(!(s1.contains("已派送并签收")||s1.contains("已送达")||s1.contains("送达")||s1.contains("貨件完成送達"))){
		            	//最新运单状态到现在的时间间隔
		            	 int days = Utility.daysBetween(sdf.parse(t1.trim()),new Date());
		            	 if(days>3){
		            		 map = new HashMap<String,Object>();
		            		 map.put("order_no", row.getExpressno());
		            		 map.put("link_url", row.getCompany()+"@"+row.getExpressno());           //orderno
		            		 map.put("sender_id",row.getAdmId());             //接收人
		            		 map.put("send_type", 10);                        //类型
		            		 map.put("send_content","物流反馈,订单号:【"+row.getOrderid()+"】,运输方式:【"+company+"】,运单号:【"+row.getExpressno()+"】三天未更新状态, 最新物流信息:"+s1 );
		            		 map.put("create_time", sdf.format(new Date()));  //创建时间
		            		 map.put("reservation2", row.getExpressno()+"/"+row.getAdmId()+"/"+3);
		            		 list.add(map);
		            	 }
		            }
				}
				} catch (Exception e) {
					LOG.error(" 原飞航 对接API 获取物流信息失败【运单号:"+row.getExpressno()+"】", e);
				}
		 }
	    }
    	if(list.size()>0){
    		saveBugTrack(list);
    	} 
	    }
    }


	@Override
	public int updateBzById(int id ,String bz,String expressno) {
		// TODO Auto-generated method stub
		trackDao.updateReadFlag(expressno);
		return   trackDao.updateBzById(bz,expressno);
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		 
	}

	@Override
	public void updateReadFlag(String expressno) {
		// TODO Auto-generated method stub
		trackDao.updateReadFlag(expressno);
	}
}
