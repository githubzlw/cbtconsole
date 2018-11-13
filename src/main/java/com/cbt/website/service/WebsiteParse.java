package com.cbt.website.service;

import com.cbt.parse.bean.ParamBean;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.driver.SearchEngine;
import com.cbt.parse.service.DownloadMain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class WebsiteParse {
	
	public ArrayList<SearchGoods>  ParseSearch(String url,String keyword,String page){
		
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		int tem = 0;
		String url_page = DownloadMain.getJsoup(url, tem,null);
		while(tem<3&&(url_page==null||url_page.isEmpty()||"httperror".equals(url_page))){
			tem++;
			url_page = DownloadMain.getJsoup(url,tem,null);
		}
		Element body =null;
		if(url_page!=null&&!url_page.isEmpty()&&!"httperror".equals(url_page)){
			//JSoup解析HTML生成document
			body = Jsoup.parse(url_page).body();
			url_page = null;
		}
		if(body==null){
//			System.err.println("--------body--null--------");
			return list;
		}
//		DownloadMain.write2file(body.toString(), "C:/Users/abc/Desktop/166.txt");
		//获取页码  分页
		String page_url =  body.select("div[class=sm-pagination]").attr("data-async-url");
//		System.err.println("-1---page_url----"+page_url);
		page_url = page_url.isEmpty()?body.select("div[id=sw_mod_pagination]").attr("data-async-url"):page_url;
		page_url = page_url.isEmpty()?body.select("div[class=sm-offer-trigger]").attr("data-mod-config"):page_url;
		
		if(page_url!=null&&!page_url.isEmpty()){
//			System.err.println("-2-page_url----"+page_url);
			page_url = DownloadMain.getSpiderContext(page_url, "http.*token=\\d+");
			if(!page_url.isEmpty()){
				page_url = page_url.replace("&amp;", "&");
				page_url = page_url+"&callback=jQuery";
				page_url = page_url.replaceAll("asyncCount=\\d+", "asyncCount=65");
				page_url = page_url.replaceAll("startIndex=\\d+", "startIndex=0");
				page_url = page_url.replaceAll("beginPage=%7Bpage%7D", "beginPage=1");
				page_url = page_url.replaceAll("beginPage=\\d+", "beginPage=1");
			}
		}
//		System.out.println("page_url:"+page_url);
		
		if(page_url==null||page_url.isEmpty()){
			return localPage(body);
		}
		
		int max=0;
		//1.起始链接开始   需要先parse总页数   pagination-max
		String mount = body.select("div[class=sm-pagination]").attr("data-total-page");
		if(mount==null||mount.isEmpty()){
			mount = body.select("div[id=sw_mod_pagination]").attr("data-total-page");
		}
		if(!mount.isEmpty()&&Pattern.compile("(\\d+)").matcher(mount).matches()){
			max = Integer.valueOf(mount);
		}
		ParamBean bean = new ParamBean();
		bean.setCurrent(Integer.valueOf(page));
		bean.setAmount(max);
		bean.setKeyword(keyword);
		bean.setCom("goodswebsite");
		bean.setWebsite("o");
		ArrayList<SearchGoods> page_list = SearchEngine.page(bean );
		//2.拼接分页链接  http://xxxx/2.html?xxxx
		String p_url = page_url.replaceAll("beginPage=1", "beginPage="+page);
		
		list = goodsParse(p_url);
		if(!list.isEmpty()){
			list.addAll(page_list);
		}
		body = null;
	   return list;	
	}
	
	private ArrayList<SearchGoods> goodsParse(String url){
		ArrayList<SearchGoods> list  = new ArrayList<SearchGoods>();
		//保存商品数据
		String client = DownloadMain.getContentClient(url, null);
		if(client!=null){
			client = client.replaceAll("\\\\\"", "\"");
			client = client.replaceAll("\\\\n", "");
			client = client.replaceAll("\\\\/", "/");
			client = client.replaceAll("\\\\'", "'");
			client = DownloadMain.getSpiderContext(client, "(<li\\s*.*</script>\\s*(</div>)*)");
			client = client.replaceAll("sm-offer-item sw-dpl-offer-item\\s*", "sm-offer-item-sw-dpl-offer-item");
			client = client.replaceAll("sm-offer-trade sw-dpl-offer-trade\\s*", "sm-offer-trade sw-dpl-offer-trade");
			Document document = Jsoup.parse(client);
			//System.out.println("==================================");
			Element body = document.body();
//			DownloadMain.write2file(body.toString(), "C:/Users/abc/Desktop/15.txt");
			if(body!=null){
				Elements goods = body.select("li[class=sm-offerShopwindow-item]");
				if(goods.isEmpty()){
					goods = body.select("li[class=sm-offer-item-sw-dpl-offer-item]");
				}
				String goods_url = null;
				String goods_name = null;
				String goods_solder = null;
				String goods_price = null;
				Elements good  = null;
				int goods_num  = goods.size();
				String goods_img = null;
				SearchGoods sg = null;
				for(int i=0;i<goods_num;i++){
					good = goods.get(i).select("a[class=sm-offer-photoLink sw-dpl-offer-photoLink]");
					if(good.isEmpty()){
						good = goods.get(i).select("a[trace-click_item=title]");
					}
					goods_url = good.attr("href");
					if(!Pattern.compile("(detail.1688.com)").matcher(goods_url).find()){
						continue;
					}
					
					goods_solder = goods.get(i).select("span[class=sm-offer-trade sw-dpl-offer-trade]").select("em").text().replaceAll("\\D+", "").trim();
					if(goods_solder.isEmpty()){
						goods_solder = goods.get(i).select("div[class=price-other]").select("em").text().replaceAll("\\D+", "").trim();
					}
					if(goods_solder==null||goods_solder.isEmpty()){
						goods_solder = "0";
					}
					goods_img = goods.get(i).select("a[trace-click_item=pic]").select("img").attr("src");
					if(goods_img.isEmpty()){
						goods_img = goods.get(i).select("a[offer-stat=pic]").select("img").attr("src");
					}
					if(goods_img.isEmpty()){
						goods_img = goods.get(i).select("a[trace-click_item=pic]").select("img").attr("data-lazy-src");
					}
					if(goods_img.isEmpty()){
						goods_img = goods.get(i).select("a[offer-stat=pic]").select("img").attr("data-lazy-src");
					}
					goods_name = good.text();
					if(goods_name.isEmpty()){
						goods_name = good.attr("title");
					}
					goods_price = goods.get(i).select("span[class=sm-offer-priceNum sw-dpl-offer-priceNum]").text().replace("¥", "").trim();
					if(goods_price.isEmpty()){
						goods_price = goods.get(i).select("span[class=price fd-left]").text().replace("¥", "").trim();
					}
					goods_price = DownloadMain.getSpiderContext(goods_price, "\\d+\\.*\\d*"); 
					goods_price = goods_price.isEmpty()?"0":goods_price;
					
					sg = new SearchGoods();
					sg.setGoods_name(goods_name);
					sg.setGoods_price(goods_price);
					sg.setGoods_solder(goods_solder);
					sg.setGoods_url(goods_url);
					sg.setGoods_image(goods_img);
					
					sg.setKey_type("goods");
					list.add(sg);
//					System.out.println("-------------2222--------------------");
//					System.out.println(goods_url);
//					System.out.println(goods_solder);
//					System.out.println(goods_price);
//					System.out.println(goods_name);
//					System.out.println(goods_img);
//					System.out.println("----------2222-----------------------");
					goods_url = null;
					goods_name = null;
					goods_solder = null;
					goods_price = null;
				}
			}
		}else{
//			System.out.println("444444444444444444444444444444444444444");
		}
		return list;
	}
	
	
	
	private  ArrayList<SearchGoods> localPage(Element body){
		ArrayList<SearchGoods> list  = new ArrayList<SearchGoods>();
		if(body!=null){
			SearchGoods  sg = null;
			Elements goods = body.select("li[class=sm-offerShopwindow-item]");
			String goods_url = null;
			String goods_name = null;
			String goods_solder = null;
			String goods_price = null;
			int goods_num  = goods.size();
			String goods_img = null;
			Elements good = null;
			for(int i=0;i<goods_num;i++){
				
				good = goods.get(i).select("a[trace-click_item=title]");
				goods_url = good.attr("href");
				if(!Pattern.compile("(detail.1688.com)").matcher(goods_url).find()){
					continue;
				}
				goods_solder = goods.get(i).select("span[class=volume fd-right]").select("em").text().replaceAll("\\D+", "").trim();
				if(goods_solder.isEmpty()){
					goods_solder = goods.get(i).select("div[class=price-other]").select("em").text().replaceAll("\\D+", "").trim();
				}
				if(goods_solder==null||goods_solder.isEmpty()){
					goods_solder = "0";
				}
				goods_name = good.text();
				if(goods_name.isEmpty()){
					goods_name = good.attr("title");
				}
				goods_img = goods.get(i).select("a[trace-click_item=pic]").select("img").attr("src");
				if(goods_img.isEmpty()){
					goods_img = goods.get(i).select("a[trace-click_item=pic]").select("img").attr("data-lazy-src");
				}
				goods_price = goods.get(i).select("span[class=sm-offer-priceNum sw-dpl-offer-priceNum]").text().replace("¥", "").trim();
				if(goods_price.isEmpty()){
					goods_price = goods.get(i).select("span[class=price fd-left]").text().replace("¥", "").trim();
				}
				goods_price = DownloadMain.getSpiderContext(goods_price, "\\d+\\.*\\d*"); 
				goods_price = goods_price.isEmpty()?"0":goods_price;
				
				sg = new SearchGoods();
				sg.setGoods_name(goods_name);
				sg.setGoods_price(goods_price);
				sg.setGoods_solder(goods_solder);
				sg.setGoods_url(goods_url);
				sg.setGoods_image(goods_img);
				sg.setKey_type("goods");
				list.add(sg);
				
//				System.out.println("-------------llllll--------------------");
//				System.out.println(goods_url);
//				System.out.println(goods_solder);
//				System.out.println(goods_price);
//				System.out.println(goods_name);
//				System.out.println(goods_img);
//				System.out.println("----------llllll-----------------------");
				goods_url = null;
				goods_name = null;
				goods_solder = null;
				goods_price = null;
		}
		}
		return list;
	}

}
