package com.cbt.parse.service;

import com.cbt.parse.bean.ParseBean;
import com.cbt.util.AppConfig;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryUtils {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CategoryUtils.class);
	
	
	public  static Boolean gategory(String name,String cID,List<HashMap<String, String>> list,String keywords,String tem_path){
		java.util.logging.Logger.getLogger("com.cbt.parse.service.DownloadMain").setLevel(java.util.logging.Level.OFF);
		LOG.warn("creat html........");
		Boolean result = false;
		if(cID!=null&&!cID.isEmpty()&&name!=null&&!name.isEmpty()){
			System.out.println("*****************1");
			String spath  = tem_path;
			String path = AppConfig.ips.replace("/cbtconsole", "")+"/cbtconsole/apa/template.html";
//			File in = new File(path);
			try {
//				Document doc = Jsoup.parse(in, "utf-8", "");
				URL u= new URL(path);
				Document doc = Jsoup.parse(u, 5*1000);
				
				//页面标题
				Element titleNode = doc.getElementsByTag("title").get(0);
				titleNode.text(name);
				
				//类别标题
				Element hdivNode = doc.getElementById("hdiv");
				hdivNode.text(name.toUpperCase());
				
				//关键字数据添加
				if(keywords!=null&&!keywords.isEmpty()){
					Element wordsNode = doc.getElementById("category");
					String[] words = keywords.split("\\s*[,，；;]+\\s*");
					Element trNode = null;
					Element tdNode = null;
					String href;
					for(int i=0;i<words.length;i++){
						if(words.length<3||i%3==0){
							trNode = doc.createElement("tr");
							wordsNode.appendChild(trNode);
						}
						if(!words[i].isEmpty()){
							tdNode = doc.createElement("td");
							tdNode.attr("class", "wordsNode");
							tdNode.attr("width", "320px");
							
							Element aNode = doc.createElement("a");
							href  = "/cbtconsole/goodsTypeServerlet?keyword="+words[i]+"&price1=&price2=&moq=&website=w&srt=default&k0=&k1=&k2=&k3=";
							aNode.attr("href",href);
							aNode.text(words[i]);
							tdNode.appendChild(aNode);
							trNode.appendChild(tdNode);
						}
						
					}
					
					trNode = null;
					wordsNode = null;
					words = null;
					href  =null;
				}
				
				//商品数据添加
				int size = list.size();
				if(size>0){
					System.out.println("*********************2");
					Element goodsNode = doc.getElementById("goods");
					Element genNode= null;
					Element geeNode= null;
					Element listNode= null;
					Element cateNode= null;
					Element gInfoNode= null;
					Element rowNode= null;
					Element productNode= null;
					Element imgNode= null;
					Element nameNode= null;
					Element priceNode= null;
					Element orderNode= null;
					Element spanNode= null;
					Element temNode= null;
					Element teeNode= null;
					Element tmmNode= null;
					
					String href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet";
					String cate;
					String urls = null;
					for(int i=0;i<size;i++){
						if(size<2||i%2==0){
							listNode = doc.createElement("div");
							listNode.attr("class", "listNode");
							goodsNode.appendChild(listNode);
						}
						genNode = doc.createElement("div");
						genNode.attr("class", "genNode");
						listNode.appendChild(genNode);
						
						if(i%2==0){
							geeNode = doc.createElement("div");
							geeNode.attr("class", "genNode");
							tmmNode = doc.createElement("div");
							tmmNode.attr("style", "width:30px;");
							geeNode.appendChild(tmmNode);
							listNode.appendChild(geeNode);
						}
						
						
						if(i%2==1){
							temNode = doc.createElement("div");
							temNode.attr("style", "clear:both;");
							listNode.appendChild(temNode);
							
						}
						
						cate = list.get(i).get("cate");
						cateNode = doc.createElement("div");
						cateNode.attr("class", "cateNode");
						cateNode.text(cate);
						genNode.appendChild(cateNode);
						
						gInfoNode = doc.createElement("div");
						genNode.appendChild(gInfoNode);
						
						urls = list.get(i).get("urls");
						String[] url = urls.split("\\s*[,，；;]+\\s*");
						for(int j=0;j<url.length;j++){
							
							if(url.length<2||j%2==0){
								rowNode = doc.createElement("div");
								rowNode.attr("class", "rowNode");
								gInfoNode.appendChild(rowNode);
							}
							if(url[j]!=null&&!url[j].isEmpty()){
								ParseBean data = getDatab(url[j]);
								if(data!=null){
									productNode = doc.createElement("div");
									productNode.attr("class", "productNode");
									rowNode.appendChild(productNode);
									//图片
									imgNode = doc.createElement("img");
									imgNode.attr("class","imgNode");
									imgNode.attr("src",data.getImg());
									if(data.getUrl()!=null&&!data.getUrl().isEmpty()){
										imgNode.attr("onclick","window.open(\'"+href+TypeUtils.encodeGoods(data.getUrl())+"\',\'_blank\')");
									}else{
										LOG.warn("url is null");
									}
									productNode.appendChild(imgNode);
									
									//名称
									nameNode = doc.createElement("div");
									nameNode.attr("class", "nameNode");
									spanNode = doc.createElement("span");
									spanNode.attr("title", data.getName());
									spanNode.text(data.getName());
									nameNode.appendChild(spanNode);
									productNode.appendChild(nameNode);
									
									//价格
									priceNode = doc.createElement("span");
									priceNode.attr("class", "priceNode");
									priceNode.text("$"+data.getPrice().replace("$", "").trim());
									productNode.appendChild(priceNode);
									//订单
									orderNode = doc.createElement("span");
									orderNode.attr("class", "orderNode");
									orderNode.text(" Min.Order:"+data.getMorder());
									productNode.appendChild(orderNode);
									
									if(j%2==1){
										teeNode = doc.createElement("div");
										teeNode.attr("style", "clear:both;");
										rowNode.appendChild(teeNode);
									}
								}else{
									System.out.println("8888888888888");
								}
								data = null;
							}
						}
						productNode= null;
						imgNode= null;
						nameNode= null;
						priceNode= null;
						orderNode= null;
						spanNode= null;
						urls = null;
						cate = null;
						
					}
					goodsNode = null;
					listNode= null;
					cateNode= null;
					gInfoNode= null;
					rowNode = null;
					teeNode = null;
					temNode = null;
					genNode = null;
					geeNode = null;
					
				}
				
				
				DownloadMain.write2file(doc.toString(), spath);
				doc = null;
			LOG.warn("success......creat........");
			result = true;
			} catch (IOException e) {
				result = false;
				LOG.warn("fail......creat........1");
			}
		}
		return result;
	}
	
	
	private static ParseBean getDatab(String url){
		ParseBean pb = null;
		String tem_url = url.replaceAll("(\\s+)", "").trim();
		GoodsBean gb = ParseGoodsUrl.parseGoods2(tem_url,null,null);
		if(gb!=null&&!gb.isEmpty()){
			pb = new ParseBean();
			pb.setUrl(tem_url);
			pb.setName(gb.getpName());
			
			String price = gb.getpSprice();
			if(price==null||price.isEmpty()){
				price = gb.getpOprice();
				if(price==null||price.isEmpty()){
					ArrayList<String> list = gb.getpWprice();
					if(list!=null&&!list.isEmpty()){
						price = list.get(0).split("\\$")[1];
					}
					list  =null;
				}
			}
			if(price!=null&&price.indexOf("-")>0){
				price = price.split("-")[0];
			}
			pb.setPrice(price);
			price = null;
			
			String morder = gb.getMinOrder();
			if(morder!=null&&!morder.isEmpty()){
				String pat = DownloadMain.getSpiderContext(morder, "(\\d+)").trim();
				if("1".equals(pat)){
					morder = morder.replace("(s)", "").replace("(es)", "").trim();
				}else{
					morder = morder.replace("(", "").replace(")", "").trim();
				}
			}
			pb.setMorder(morder);
			morder  =null;
			
			ArrayList<String> image = gb.getpImage();
			String img =  "";
			if(image!=null&&!image.isEmpty()){
				img = image.get(0);
				String[] imgSize = gb.getImgSize();
				if(imgSize!=null&&imgSize.length>0){
					img = new StringBuilder().append(img).append(imgSize[1]).toString();
				}
				imgSize = null;
			}
			pb.setImg(img);
			img = null;
			image = null;
		}
		gb = null;
		return pb;
	}
	
	
	
	
	
	/**获取名称 价格 图片
	 * @param url
	 * @return
	 */
	/*
	private   static ParseBean getData(String url){
		ParseBean pb = null;
		if(url!=null&&!url.isEmpty()){
			if(!url.isEmpty()&&Pattern.compile("(http://.+)").matcher(url).matches()){
				String page = DownloadMain.getJsoupForCheck(url,0);
//				while ((page==null||page.isEmpty())&&count<2) {
//					count++;
//					page = DownloadMain.getJsoupForCheck(url,0);
//				}
				if(page!=null&&!page.isEmpty()){
					//JSoup解析HTML生成document
					Document doc = Jsoup.parse(page);
					page = null;
					//获取element
					Element body = doc.body();
					pb = parseDate(body);
					pb.setUrl(url);
				}
			}
		}
		return pb;
	}
	*/
	/**获取名称 价格 图片
	 * @param body
	 * @return
	 */
	/*
	private static ParseBean  parseDate(Element body){
		ParseBean pb = new ParseBean();
		int flag;
		//1.name
		//①alibaba  wholesale
		String name=body.select("div[class=title]").select("h1").text();
		flag = 0;
		if(name==null||name.isEmpty()){
			//② alibaba
			name=body.select("h1[class=title fn]").select("h1").text();
			flag = 1;
			//③ aliexpress
			if(name==null||name.isEmpty()){
				name=body.select("h1[class=product-name]").text();
				flag = 2;
			}
		}
		pb.setName(name);
		
		
		//2.price
		String price = "";
		String pricex = "";
		String pricey="";
		String tem;
		String minOrder="1 piece";
		if(flag==0){
			//①alibaba  wholesale
			price = body.select("span[id=J-price]").text().trim();
			if(price==null||price.isEmpty()){
				Elements prices = body.select("div[class=brief-info]")
									 .select("dl[class=util-clearfix]");
				if(prices!=null&&!prices.isEmpty()){
					for(Element ind:prices){
						if(Pattern.compile("(Promotion Price)").matcher(ind.select("dt").text()).find()){
							tem = ind.select("span[class=measurement]").text().replace("/", "").trim();
							pricex = ind.select("span").text()
									.replace("/", "")
									.replace(tem, "").trim();
							tem = null;
						}else if(Pattern.compile("(Wholesale Price)").matcher(ind.select("dt").text()).find()){
							tem = ind.select("dd").select("span[class=measurement]").text();
							pricey = ind.select("dd").text()
									.replace("US", "").replace("$", "")
									.replace(tem, "").trim();
							tem = null;
						}
					}
					price = pricex.isEmpty()?pricey:pricex;
				}else{
					Elements wprice = body.select("dl[class=ladder-price-item util-clearfix]");
					if(wprice!=null&&!wprice.isEmpty()){
						price = wprice.get(0).select("span[class=priceVal]")
								.text().replace("US", "").replace("$", "").trim();
					}}
				prices = null;
			}
		}else if(flag==1){
			//② alibaba
			Elements prices = body.select("div[class=ls-icon ls-brief]")
					.select("table")
					.select("tr");
			for(Element t:prices){
				if(Pattern.compile("Promotional Price").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pricex = tem.length()>4?tem.substring(4).replace("/", "")
							.replace("Piece", "").trim():"";
							tem = null;
				}else if(Pattern.compile("Wholesale Price").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pricey = tem.length()>4?tem.substring(4)
							.replace("/", "")
							.replace("Piece", "").trim():"";
							tem = null;
							
				}else if(Pattern.compile("FOB").matcher(t.select("th").text()).find()){
					tem = t.select("td").text();
					pricey = tem.length() - 22 > 0?tem.substring(4,tem.length() - 22)
							.replace("/", "")
							.replace("Piece", "").trim():"";
							tem = null;
				}else if(Pattern.compile("Min.").matcher(t.select("th").text()).find()){
					minOrder = t.select("td").text();
					if(minOrder.indexOf("/")>0){
						minOrder = minOrder.substring(0,minOrder.indexOf("/"));
						minOrder = minOrder+"(s)";
					}
				}
				if(pricex.isEmpty()){
					pricex = pricey;
					pricey = null;
				}
				if(pricex!=null&&!pricex.isEmpty()){
					pricex = DownloadMain.getSpiderContext(pricex, "(\\d+(.+)\\d+)");
				}
				price = pricex;
			}
			prices = null;
			
		}else if(flag==2){
			//③ aliexpress
			price= body.select("span[id=sku-discount-price]").text().trim();
			if(price==null||price.isEmpty()){
				price = body.select("span[id=sku-price]").text().trim();
			}
		}
		
		pricey = null;
		pricex = null;
		tem = null;
		pb.setPrice(price);
		
		
		//3.img
		//①alibaba  wholesale
		String img = null;
		Elements productImag = body.getElementsByClass("thumb").select("img"); 
        if(productImag.size() == 0){
        	productImag = body.select("div[class=ione J-item]").select("img"); 
        	if(productImag.size() == 0){
        		productImag = body.select("div[class=J-item]").select("img"); 
        		//③ aliexpress
        		if(productImag.size() == 0){
        			productImag = body.select("ul[class=image-nav util-clearfix]").select("li"); 
            		if(productImag.size() == 0){
            			productImag = body.select("a[class=ui-image-viewer-thumb-frame]"); 
            		}
        		}
        	}
        }
        if(productImag!=null&&!productImag.isEmpty()){
        	img = DownloadMain.getSpiderContext(productImag.get(0).toString(),"(?:src=\")(.*?)(\")");
        }
		pb.setImg(img);
		
		//4.morder
		if(flag==0){
			//①wholesale
			Elements info_data = body.select("div[class=ls-brief]")
					.select("dl[class=min-order util-clearfix]")
					.select("dd");
			minOrder = info_data.text();
			Elements delete = info_data.select("span[class=brief-mark]");
			String tem_qunit;
			if(minOrder.isEmpty()){
				Elements mmind = body.select("div[class=order-attr util-clearfix]");
				for(Element m:mmind){
					if(Pattern.compile("(Min)").matcher(m.text()).find()){
						minOrder = m.select("div[class=value]").text();
					}
				}
			}
			if(!delete.text().isEmpty()){
				tem_qunit = minOrder.replace(delete.text(), "");
			}else{
				tem_qunit = minOrder;
			}
			String qunit = DownloadMain.getSpiderContext(tem_qunit, "(\\D+)");
			if(minOrder.isEmpty()){
				minOrder =  DownloadMain.getSpiderContext(body.select("script").toString(), "(?:moq: ')(.*?)(?:',)");
			}
		}else if(flag==2){
			//③ aliexpress
			Elements quantity = body.select("dl[class=product-info-quantity]").select("input");
			minOrder = DownloadMain.getSpiderContext(quantity.toString(), "(?:value=\")(.*?)(?:\")");
		}
		
		pb.setMorder(minOrder);
		return pb;
	}
	*/

}
