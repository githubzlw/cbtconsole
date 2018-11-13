package com.cbt.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadPage {
	 /**  
     * 根据URL抓取网页内容  
     *   
     * @param url  
     * @return  
	 * @throws  Exception 
     */
    public static String getContentFormUrl(String url)  
    {  
    	  StringBuffer content = new StringBuffer();  
        /* 实例化一个HttpClient客户端 */
    	long start = new Date().getTime();
     
    	  URL u;
		try {
			u = new URL(url);
    	   InputStream in = new BufferedInputStream(u.openStream());
    	   InputStreamReader theHTML = new InputStreamReader(in);
    	   int c;
    	   while ((c = theHTML.read()) != -1) {
    	    content.append((char) c);
    	   }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	long end = new Date().getTime();
        return content.toString().replace("\n", "").replace("\r", "");  
    }  
    public static void main(String[] args) throws Exception {
    	String url = "http://list.tmall.com/search_product.htm?q=%CB%D1%B2%D8&type=p&vmarket=&spm=3.7396704.a2227oh.d100&from=mallfp..pc_1_searchbutton";
    	//String url = "http://www.baidu.com";
    	String testStr = " data-title=\"蝴蝶结雪地靴羊皮毛一体女中筒靴冬季磨砂加厚加绒真皮保暖雪地棉\">     蝴蝶结雪地靴羊皮毛一体女中筒靴冬季磨砂加厚加绒真皮保暖雪地棉   </h3>   <p class=\"tb-subtitle\"> 淘宝独家爆款，采用澳洲100%羊皮毛一体；[6大卖点]:1.加厚超长纯天然澳洲羊毛内里，更保暖，轻松抵御-38℃寒冬；2：精选优质磨砂羊皮，更透气，更保暖；3：高纯度专用牛筋鞋底，更防滑，耐穿；4：纯手工制作，十年做鞋经验，100%品质保障；5:120天超长售后，给您一种实体店的感觉！6：待亲如贵宾 </p> ";  
    	
    	String title_pat = "<title>(.*?)</title>";//标题正则
    	String abstract_pat = "class=\"tb-subtitle\">(.*?)</p>";//简介正则
    	String img_pat = "itemId:\"(.*?)\",";//获取图片集
    	String price_pat = "price:(.*), bnow";//价格
    	String color_pat = "no-repeat;\"><span>(.+?)</span></a>";//颜色
    	String colorURL_pat = "style=\"background:url\\((.*?)\\) center no-repeat;";//颜色连接
    	String sizi_pat = "<a href=\"#\"><span>(.*?)</span></a>";//尺寸
    	String shop_pat = "itemId:\"(.*?)\",";//商品ID
    	String item_pat = "<a href=\"#\"><span>(.*?)</span></a>";//商店ID
    	String sname = "taobao.com\" title=\"(.*?)\"";//商家名称
    	String apiItemDesc = "apiItemDesc\":\"(.*?)\",";//宝贝详情
    	String apiItemDesc1 = "pre-wrap;\">(.*?)</pre>";//宝贝详情
    	String apiItemDesc2 = "src=  \"(.*?)\" />";//宝贝详情
    	Pattern p = Pattern.compile(img_pat);   
    	String page = DownloadPage.getContentFormUrl(url); 
//    	Object s =getSpiderContext(color_pat, page, "1");
//		System.out.println(s);
	}
    
  /*  //正则匹配相应格式的内容
    public static Object getSpiderContext(String pattern,String page,String type){
    	Pattern p = Pattern.compile(pattern); 
    	Matcher m = p.matcher(page);   
    	String res = ""; 
    	System.out.println(type);
//    		System.out.println(m.group(1).replace("\"", ""));  
    	if(type == null || type.equals("")){
    		System.out.println("ddddddddddddd");
    		if(m.find()){
        		return m.group(1);
    		}
    		return "";
    	}else{
    		
    		System.out.println("ddddddddddddd1："+m.groupCount());
    		String[] mobj = new String[m.groupCount()];
    		 int i = 0;
    		while(m.find()){  
    			mobj[i] =m.group(1);
    			i++;
    		}
    		return mobj;
    	}
    	 
    }*/
    
    
    //正则匹配相应格式的内容
    public static Object getSpiderContext(String pattern,String page){
    	Pattern p = Pattern.compile(pattern); 
    	Matcher m = p.matcher(page);
    	while(m.find()){  
            return m.group(1);
    	}
    	 return "";
    }
    
    //正则匹配相应格式的内容
    public static List<String> getSpiderContextList(String pattern,String page){
    	Pattern p = Pattern.compile(pattern); 
    	Matcher m = p.matcher(page);  
		List<String> mobj = new ArrayList<String>();
		while(m.find()){  
			mobj.add(m.group(1));
		}
		return mobj;
    	 
    }
    
}
