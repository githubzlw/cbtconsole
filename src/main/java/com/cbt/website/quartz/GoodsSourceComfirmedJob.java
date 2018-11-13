package com.cbt.website.quartz;

import com.cbt.util.IpSelect;
import com.cbt.warehouse.service.OrderService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.importExpress.service.IPurchaseService;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GoodsSourceComfirmedJob extends QuartzJobBean{
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsSourceComfirmedJob.class);
	
	@Autowired
    private OrderService orderService;
	@Autowired
	private IPurchaseService iPurchaseService;
	public void execute() {
		System.out.println("=============================test execute!==============");
		
		List<Map<String,Object>> orderBeanMapList = orderService.getNeedComfirmSourceGoods();
		if(orderBeanMapList!=null&&orderBeanMapList.size()>0) {
			for(Map<String,Object> orderBeanMap : orderBeanMapList) {
				String goodspid = (String)orderBeanMap.get("goods_pid");
				String specId = (String)orderBeanMap.get("specId");
				if(StringUtils.isBlank(specId)||StringUtils.isBlank(goodspid)) {
					continue;
				}
				int number = Integer.parseInt(String.valueOf(orderBeanMap.get("number")));
				Map<String,Object> map_1688info = get1688GoodsInfo(goodspid,specId,number);
				int success = Integer.parseInt(String.valueOf(map_1688info.get("success")));
				if(success==1) {
				    double goodsprice = Double.parseDouble(String.valueOf(orderBeanMap.get("goodsprice")));
				    
				    int stock = Integer.parseInt(String.valueOf(map_1688info.get("canBookCount")));
				    double maxprice = (double)map_1688info.get("maxPrice");
				    if(number<=stock&&goodsprice<=maxprice) {
				    	//可以进行自动货源确认
				    	String url_1688 = "https://detail.1688.com/offer/"+goodspid+".html";
				    	/*************自动录入货源start*************/
				    	Map<String,String> map=new HashMap<String, String>();
						map.put("type","1");
						map.put("admid","0.00");
						map.put("userid",String.valueOf(orderBeanMap.get("userid")));
						map.put("goodsdata_id","0.00");
						map.put("goods_url",String.valueOf(orderBeanMap.get("car_url")));
						map.put("googs_img",String.valueOf(orderBeanMap.get("car_img")));
						map.put("goods_price",String.valueOf(orderBeanMap.get("goodsprice")));
						map.put("goods_title",String.valueOf(orderBeanMap.get("goodstitle")));
						map.put("googs_number",String.valueOf(orderBeanMap.get("number")));
						map.put("orderNo",String.valueOf(orderBeanMap.get("orderid")));
						map.put("goodid","0");
						map.put("cGoodstype",String.valueOf(orderBeanMap.get("car_type")));
						map.put("od_id",String.valueOf(orderBeanMap.get("id")));
						map.put("reason","");
						map.put("state_flag","1");
						map.put("resource",url_1688);
						map.put("price",String.valueOf(maxprice));
						iPurchaseService.AddRecource(map);
				    	/*************自动录入货源end*************/
				    	
						/*************自动确认货源start*************/
						iPurchaseService.PurchaseComfirmTwoHyqr(Integer.parseInt(String.valueOf(orderBeanMap.get("userid"))), 
								String.valueOf(orderBeanMap.get("orderid")), Integer.parseInt(String.valueOf(orderBeanMap.get("id"))), 
								0, 0, 0, String.valueOf(orderBeanMap.get("car_url")),
								String.valueOf(orderBeanMap.get("car_img")), String.valueOf(orderBeanMap.get("goodsprice")), 
								String.valueOf(orderBeanMap.get("goodstitle")), 
								Integer.parseInt(String.valueOf(orderBeanMap.get("number"))), 
								"https://detail.1688.com/offer/"+goodspid+".html", url_1688, 
								Integer.parseInt(String.valueOf(orderBeanMap.get("number"))), String.valueOf(orderBeanMap.get("dropshipid")),
						String.valueOf(orderBeanMap.get("isDropshipOrder")));
						/*************自动确认货源end*************/
						
						System.out.println(String.valueOf(orderBeanMap.get("orderid"))+"订单的商品："+goodspid +",完成自动货源确认，规格"+String.valueOf(orderBeanMap.get("car_type")));
				    }
				}
			}
		}
		return;
	}
	
	public Map<String,Object> get1688GoodsInfo(String pid,String spec_id,int good_number){
		Map<String,Object> map = new HashMap<String,Object>();
		String url = "https://detail.1688.com/offer/"+pid+".html";
		String page = getContentByUrl(url);
		
		int tem=0;
		while(StringUtils.isBlank(page) && tem < 3){
			tem++;
			page = getContentByUrl(url);
		}
		
		if(StringUtils.isBlank(page)) {
			map.put("success", 0);//抓取失败
		}else if("httperror".equals(page)){//请求出现错误
			map.put("success", 0);//抓取失败
		}else{
			//JSoup解析HTML生成document
			Document doc = Jsoup.parse(page);
			Element body = doc.body();
			String script = body.select("script").toString().replaceAll("(\\s*)", "");
			String iDetailData = null;
			try {
				int index1 = script.indexOf("\"sku\"")-1;
				int index2 = script.indexOf("iDetailData.allTagIds")-1;
				iDetailData = script.substring(index1, index2);
				JsonObject productSkuObj = new JsonParser().parse(iDetailData).getAsJsonObject();
				JsonObject skuMapObj = productSkuObj.getAsJsonObject("sku").getAsJsonObject("skuMap");
				JsonArray arrJson = productSkuObj.getAsJsonObject("sku").getAsJsonArray("priceRange");
				double maxPrice = getPrice(good_number,arrJson);//获取对应数量的1688区间价格
				
				Set<Entry<String, JsonElement>> ssMap = skuMapObj.entrySet();
				Iterator<Entry<String, JsonElement>> it = ssMap.iterator();
				while(it.hasNext()) {
					String key = it.next().getKey();
					JsonObject skuJson = skuMapObj.getAsJsonObject(key);
					String specid = skuJson.get("specId").getAsString();
					if(specid.replaceAll("\\s*", "").equals(spec_id.replaceAll("\\s*", ""))) {
						String canBookCount = skuJson.get("canBookCount").getAsString();
						map.put("canBookCount", canBookCount);
						if(arrJson==null){//区间价获取价格
							String sku_price = skuJson.get("price").getAsString();
							maxPrice = Double.parseDouble(sku_price);
						}
						break;
					}
				}
				map.put("maxPrice", maxPrice);
				map.put("success", 1);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("GoodsSourceComfirmedJob error,"+"iDetailData:"+iDetailData);
				map.put("success", 0);
			}
		}
		return map;
	}
	
	public double getPrice(int good_number,JsonArray arrJson) {
		double maxPrice = 0.00;
		if(arrJson!=null){//区间批发价获取价格
			Iterator<JsonElement> it_arr = arrJson.iterator();
			int count = 0;
			int size = arrJson.size();
			if(size==1) {//一个区间
				JsonElement je = it_arr.next();
				String priceArr = je.getAsJsonArray().get(1).toString();
				double price1 = Double.parseDouble(priceArr);//当前批发价格
				maxPrice = price1;
			}else if(size==2) {//两个区间
				double before_price = 0;
				while(it_arr.hasNext()) {
					count++;
					JsonElement je = it_arr.next();
					String priceArr = je.getAsJsonArray().get(1).toString();
					int buy_number = Integer.parseInt(je.getAsJsonArray().get(0).toString().replaceAll("\\s*", ""));//当前批发数量
					double price1 = Double.parseDouble(priceArr);//当前批发价格
					if(count==1) {
						before_price = price1;
						continue;
					}else {
						if(good_number<buy_number) {
							maxPrice = before_price;
						}else {
							maxPrice = price1;
						}
					}
				}
			}else if(size==3){//三个区间
				double before_price = 0;
				
				int before_number1 = 1;
				double before_price1 = 0;
				while(it_arr.hasNext()) {
					count++;
					JsonElement je = it_arr.next();
					String priceArr = je.getAsJsonArray().get(1).toString();
					int buy_number = Integer.parseInt(je.getAsJsonArray().get(0).toString().replaceAll("\\s*", ""));//当前批发数量
					double price1 = Double.parseDouble(priceArr);//当前批发价格
					if(count==1) {
						before_price = price1;
						continue;
					}else if(count==2){
						before_number1 = buy_number;
						before_price1 = price1;
						continue;
					}else {
						if(good_number<before_number1) {
							maxPrice = before_price;
						}else if(good_number<buy_number){
							maxPrice = before_price1;
						}else{
							maxPrice = price1;
						}
					}
				}
			}else if(size==4){//4个区间
                double before_price = 0;
				
				int before_number1 = 1;
				double before_price1 = 0;
				
				int before_number2 = 1;
				double before_price2 = 0;
				while(it_arr.hasNext()) {
					count++;
					JsonElement je = it_arr.next();
					String priceArr = je.getAsJsonArray().get(1).toString();
					int buy_number = Integer.parseInt(je.getAsJsonArray().get(0).toString().replaceAll("\\s*", ""));//当前批发数量
					double price1 = Double.parseDouble(priceArr);//当前批发价格
					if(count==1) {
						before_price = price1;
						continue;
					}else if(count==2){
						before_number1 = buy_number;
						before_price1 = price1;
						continue;
					}else if(count==3){
						before_number2 = buy_number;
						before_price2 = price1;
						continue;
					}else{
						if(good_number<before_number1) {
							maxPrice = before_price;
						}else if(good_number<before_number2){
							maxPrice = before_price1;
						}else if(good_number<buy_number){
							maxPrice = before_price2;
						}else {
							maxPrice = price1;
						}
					}
				}
			}else {
				JsonElement je = it_arr.next();
				String priceArr = je.getAsJsonArray().get(1).toString();
				double price1 = Double.parseDouble(priceArr);//当前批发价格
				maxPrice = price1;
			}
		}
		return maxPrice;
	}
	
	public String getContentByUrl(String url) {
        String page = null;
		WebClient wc = new WebClient(BrowserVersion.CHROME);
		try {
			WebRequest request=new WebRequest(new URL(url)); 
			request.setCharset("UTF-8");
			String ip = IpSelect.ip();
			request.setAdditionalHeader("X-Forwarded-For", ip);
			request.setAdditionalHeader("HTTP_CLIENT_IP", ip);
			request.setAdditionalHeader("HTTP_X_FORWARDED_FOR", ip);
			request.setAdditionalHeader("WL-Proxy-Client-IP", ip);
			request.setAdditionalHeader("Proxy-Client-IP", ip);
			
			wc.getOptions().setUseInsecureSSL(true);
	        wc.getOptions().setDoNotTrackEnabled(false);
	        wc.getOptions().setJavaScriptEnabled(false); //启用JS解释器，默认为true  
	        wc.getOptions().setCssEnabled(false); //禁用css支持  
	        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常  
	        wc.getOptions().setTimeout(2*1000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
	        wc.waitForBackgroundJavaScript(1*1000);
	        wc.setJavaScriptTimeout(1*1000);
	        NicelyResynchronizingAjaxController ctr = new NicelyResynchronizingAjaxController();
	        wc.setAjaxController(ctr);
	        HtmlPage pagehtml = wc.getPage(request);
			page = pagehtml.asXml().toString(); //以xml的形式获取响应文本
			
			///wc.closeAllWindows();
			
			page = page.toString()
 				      .replace("锛", "")
 					  .replace("&nbsp;", " ")
 					  .replace("&", "&")
 				      .replace("&yen;", "RMB");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 匹配满足正则的字符串
	 * 
	 * @date 2016年5月9日
	 * @author abc
	 * @param str
	 * @param reg
	 * @return
	 */
	public static String matchStr(String str, String reg) {
		if (reg.indexOf("(") == -1) {
			reg = "(" + reg;
		}
		if (reg.indexOf(")") == -1) {
			reg = reg + ")";
		}
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return m.group(1).toString();
		}
		return "";
	}
}
