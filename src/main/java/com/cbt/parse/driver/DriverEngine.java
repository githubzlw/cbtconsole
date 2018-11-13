package com.cbt.parse.driver;

import com.cbt.parse.bean.ProxyAddress;
import com.cbt.parse.dao.ServerDao;
import com.cbt.parse.service.DownloadMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

/**aliexpress商品搜索引擎
 * @author abc
 *
 */
public class DriverEngine {
	
	/**返回页面主题内容
	 * @param url  链接
	 * @param count 设置最大重试次数（即若得到内容为null，是否重试）最大为3次
	 * @param flag 标志  1代表获取商品单页  0位获取搜索页面
	 * @return
	 */
	public static Element pageBody(String url,int count,int flag){
		int tem = count;
		String page = null;
		String temUrl = url.replaceAll("(&amp;)+", "&");
		if(temUrl!=null&&!temUrl.isEmpty()&&Pattern.compile("(http://.+)").matcher(temUrl).matches()){
			page = DownloadMain.getJsoup(temUrl, flag,null);
			while(page==null||page.isEmpty()||"httperror".equals(page)){
				if(tem<3){
					tem++;
					page = DownloadMain.getJsoup(temUrl,flag,null);
				}else{
					break;
				}
			}
		}
		temUrl = null;
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			if(Pattern.compile("(www\\.aliexpress\\.com)").matcher(url).find()){
				page = page.replace("<script type=\"text/x-handlebars-template\" id=\"lazy-render\" class=\"lazy-render\">", "");
				page = page.replace("</ul>     			  			</script>", "</ul>");
				page = page.replaceAll("li>\\s*</script>\\s*</div>", "li></ul></div>");
			}
			//JSoup解析HTML生成document
			Document doc = Jsoup.parse(page);
			if("Buy Products Online from China Wholesalers at Aliexpress.com".equals(doc.title())){
				
			}
			page = null;
			//获取element
			return doc.body();
		}
		  return null;
	}
	/**返回页面主题内容
	 * @param url  链接
	 * @param count 设置最大重试次数（即若得到内容为null，是否重试）最大为3次
	 * @param flag 标志  1代表获取商品单页  0位获取搜索页面
	 * @return
	 */
	/*public static Element pageBody2(String url,int count,int flag,Map<String, String> cookies){
		int tem = count;
		String page = null;
		String temUrl = url.replaceAll("(&amp;)+", "&");
		if(temUrl!=null&&!temUrl.isEmpty()&&Pattern.compile("(http://.+)").matcher(temUrl).matches()){
//			page = DownloadMain.getJsoup(temUrl, flag,null);
			page = DownloadMain.getJsoup2(temUrl,flag,null,cookies);
//			page = DownloadMain.getContentClient(temUrl, null);
			while(page==null||page.isEmpty()||"httperror".equals(page)){
				if(tem<3){
					tem++;
					page = DownloadMain.getJsoup2(temUrl,flag,null,cookies);
//					page = DownloadMain.getContentClient(temUrl, null);
					//page = DownloadMain.getJsoup(temUrl,flag,null);
				}else{
					break;
				}
			}
		}
		temUrl = null;
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			if(Pattern.compile("(www\\.aliexpress\\.com)").matcher(url).find()){
				page = page.replace("<script type=\"text/x-handlebars-template\" id=\"lazy-render\" class=\"lazy-render\">", "");
				page = page.replace("</ul>     			  			</script>", "</ul>");
				page = page.replaceAll("li>\\s*</script>\\s*</div>", "li></ul></div>");
			}
			//JSoup解析HTML生成document
			Document doc = Jsoup.parse(page);
			page = null;
			System.out.println("doc.title():"+doc.title());
			if("1688.com,阿里巴巴打造的全球最大的采购批发平台".equals(doc.title())){
				return null;
			}else{
				return doc.body();
			}
			//获取element
		}
		return null;
		
	}*/
	/**得到网页的body数据
	 * flag  1为商品 单页   0 为搜索页面
	 */
	public static Element pageBodyProxy(String url,int count,int flag){
		int tem = count;
		String page = null;
		String temUrl = url.replaceAll("(&amp;)+", "&");
		if(temUrl!=null&&!temUrl.isEmpty()&&Pattern.compile("(http://.+)").matcher(temUrl).matches()){
//			Setip set = new Setip();
			ServerDao sd = new ServerDao();
			ProxyAddress.active++;
			if(ProxyAddress.active > sd.queryData().size()){
				ProxyAddress.active = 1;
			}
			int id = ProxyAddress.active-1;
			
			page= DownloadMain.getContentJsoup(url,id,flag);
			while(page==null||page.isEmpty()||Pattern.compile("(<title>Source)").matcher(page).find()
					||Pattern.compile("(错误-未找到该页)").matcher(page).find()
					||Pattern.compile("(Serv-U - Error Occurred)").matcher(page).find()
					||page.length()<1000){
					if(tem<3){
						tem ++ ;
						id ++;
						System.out.println("-----------------other page--------------------");
						page = DownloadMain.getContentJsoup(url,id,flag);
					}
				}
//			while(page==null||page.isEmpty()){
//				if(tem<3){
//					tem++;
//					id ++;
//					page= DownloadMain.getContentJsoup(url,id,flag);
//				}else{
//					break;
//				}
//			}
		}
		temUrl = null;
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			//JSoup解析HTML生成document
			Document doc = Jsoup.parse(page);
			page = null;
			//获取element
			return doc.body();
		}
		return null;
	}
	
}
