package com.cbt.website.util;

import com.cbt.parse.service.TypeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * @author 作者 E-mail: saycjc@outlook.com
 * @version 创建时间：2017年3月22日 下午1:15:49 
 * 类说明  通过录入的货源获得商家的名字
 */
public class GetCompanyName {
	private static int count = 0;//链接超时
	public static String getCompanyName(String Url) {
		String content  =null;//网页内容
		String agent = getAgent();
		String companyName =null;//获取网页工厂的名字
		Connection conn = Jsoup.connect(Url)//
				.header("Connection", "keep-alive")
				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") 
				.userAgent(agent)
				.timeout(3*1000)
				.followRedirects(true);
				try {
					if (conn.execute().body() != null && !conn.execute().body().equals("")) {						
						content = conn.execute().body();
					} else {
						GetCompanyName.getCompanyName("http://item.taobao.com/item.htm?id=542215211967");
					}
					Document doc = Jsoup.parse(content);
					Element body = doc.body();
					//1688商品
					companyName=body.select("div[class=d-tab-company]").text();
					if(companyName==null||companyName.isEmpty()){
						//淘宝商品
						companyName=body.select("div[class=tb-shop-name]").text();
						//天猫商品
						if (companyName==null||companyName.isEmpty()) {
							companyName=body.select("div[class=slogo] ").text();
						}
					}else {
						System.out.println("1688商家名字是"+companyName);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			return companyName;
	}
	public static  String getAgent(){
		//伪装客户端
		String[] userAgent = new String[]{"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)",
															  "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT6.0)",
															  "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT5.2)",
															  "Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/42.0.2311.90 Safari/537.36 OPR/29.0.1795.47(Edition Campaign 56)",
															  "Mozilla/5.0 (Windows NT 6.1;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/31.0.1650.63 Safari/537.36",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.2)Gecko/2008070208 Firefox/3.0.1",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070309 Firefox/2.0.0.3",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.1)Gecko/20070803 Firefox/1.5.0.12 ",
															  "Mozilla/5.0 (Windows; U; Windows NT 5.2)AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1Safari/525.13",
															 // "Mozilla/5.0 (iPhone; U; CPU like Mac OS X)AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93Safari/419.3",
															  "Opera/9.27 (Windows NT 5.2; U; zh-cn)",
															  "Opera/8.0 (Macintosh; PPC Mac OS X; U; en)",
															  "Links/0.9.1 (Linux 2.4.24; i386;)",
															  "Links (2.1pre15; FreeBSD 5.3-RELEASE i386; 196x84)",
															  "Mozilla/4.8 [en] (X11; U; SunOS; 5.7 sun4u)",
															  "Gulper Web Bot 0.2.4 (www.ecsl.cs.sunysb.edu/~maxim/cgi-bin/Link/GulperBot)",
															  "OmniWeb/2.7-beta-3 OWF/1.0",
															  "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)"};
		if(count>=userAgent.length){
			count  = 0;
		}
		String agent = userAgent[count];
		count++;
		userAgent = null;
		return agent;
	}
	public static void main(String[] args) {
		String name =GetCompanyName.getCompanyName("https://detail.1688.com/offer/532705002059.html?tracelog=p4p");
		System.out.println(name);
	}
	public static String OurLink(String url) {
		String ourUrl = "https://www.import-express.com/spider/getSpider?url=" + TypeUtils.encodeGoods(url);
		return ourUrl;
		
	}
}
 