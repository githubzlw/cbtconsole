package com.cbt.parse.service;

import com.cbt.parse.bean.*;
import com.cbt.parse.dao.CatPvdDao;
import com.cbt.parse.dao.PvidDao;
import com.cbt.parse.dao.ServerDao;
import com.cbt.parse.daoimp.ICatPvdDao;
import com.cbt.parse.daoimp.IPvidDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**解析搜索链接
 * @author abc
 *
 */
public class ParseSearchUrl {
	private static final Log LOG = LogFactory.getLog(ParseSearchUrl.class);
	//不使用代理服务器
	public static ArrayList<SearchGoods> parseSearch(String turl, int cycle, String keyword, String catid, boolean page_flag){
		long st1 = new Date().getTime();
		int tem = cycle;
		String page=null;
		String url = TypeUtils.modefindUrl(turl.replaceAll("(&amp;)+", "&"), 0);
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		Boolean source = false;
		boolean url_flag_ali = Pattern.compile("(\\.aliexpress\\.com)").matcher(url).find();
		if(url_flag_ali){
			source = Pattern.compile("(/store/)").matcher(url).find();
		}
		
		boolean key = SearchUtils.key(keyword);
		//针对有时候搜索得到是非英文字符设置   用于获取ip
		Set set = new Set();
		LOG.warn("search3: Got original page,url=" +url);
		if(url!=null&&!url.isEmpty()&&Pattern.compile("(http://.+)").matcher(url).matches()){
			page = DownloadMain.getJsoup(url,0,set);
			if(url_flag_ali&&source&&page!=null&&Pattern.compile("(title>Buy Products Online from China Wholesalers at Aliexpress.com</title)").matcher(page).find()){
				if(!Pattern.compile("(/search/\\d+\\.html)").matcher(url).find()){
					url = url+"/search/1.html";
				}
				page = null;
			}
			while((page==null||page.isEmpty())&&tem<2){
				tem++;
				page = DownloadMain.getJsoup(url,0,set);
			}
		}else{
			LOG.warn("The url is unvalid");
		}
		long s = new Date().getTime();
		
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			if(Pattern.compile("(taobao)").matcher(url).find()){
				List<String> tepp = DownloadMain.getSpiderContextList1("(?:nav-cat)(.*?)(?:\")", page);
				for(String t:tepp){
					page = page.replaceAll(t, t.trim());
				}
				tepp = null;
			}
			//针对aliexpress搜索  商品集合无法直接通过标签解析的情况
			if(url_flag_ali){
				page = page.replace("<script type=\"text/x-handlebars-template\" id=\"lazy-render\" class=\"lazy-render\">", "");
				page = page.replace("</ul>     			  			</script>", "</ul>");
				page = page.replaceAll("li>\\s*</script>\\s*</div>", "li></ul></div>");
			}
			//解析搜索url  
			Document doc_s = Jsoup.parse(page);
			//获取element  
			Element body_s = doc_s.body(); 
			if(!source){
				//1.一般搜索
				//aliexpress存在大类情况
				if(url_flag_ali){
					Elements select = body_s.select("div[class=broad-box]");
					if(select.size()>0){
						String rUrl = DownloadMain.getSpiderContext(select.get(0).select("a[class=view-more]").toString(), "(?:href=\")(.*?)(?:\")");
						if(!rUrl.isEmpty()&&Pattern.compile("(http://.+)").matcher(rUrl).matches()){
							page = DownloadMain.getJsoup(rUrl,0,set);
							if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
								page = page.replace("<script type=\"text/x-handlebars-template\" id=\"lazy-render\" class=\"lazy-render\">", "");
								page = page.replace("</ul>     			  			</script>", "</ul>").replaceAll("li>\\s*</script>\\s*</div>", "li></ul></div>");
								body_s = Jsoup.parse(page).body();
							}
						}
					}
				}
				
				String title = doc_s.title();
				doc_s = null;
				if(Pattern.compile("(taobao)").matcher(url).find()&&!Pattern.compile("(我喜欢)").matcher(title).find()){
					list = parseTBSearch(body_s,page);
				}else if(url_flag_ali){
					list = parseExSearch(body_s,key);
				}else if(Pattern.compile("(.en\\.alibaba\\.com)").matcher(url).find()){
					list = parseAliSearchS(body_s,url,key);
				}else if(Pattern.compile("(wholesale.*alibaba\\.com)").matcher(url).find()){
					list = parseAliSearchW(body_s,key);
					list = list.isEmpty()?parseAliSearchM(body_s,key):list;
				}else if(Pattern.compile("(alibaba)").matcher(url).find()){
					list = parseAliSearchA(body_s,key);
					if(list.isEmpty()){
						list = parseAliSearchM(body_s,key);
					}
				}else if(Pattern.compile("(list\\.tmall\\.com)").matcher(url).find()){
					list = parseTMSearch(body_s);
				}
				//相关链接  relate search  页码  类别  类型等
				CatPvdBean bean = null;
				if(keyword!=null&&!keyword.isEmpty()&&catid!=null&&!catid.isEmpty()){
					keyword = keyword.replaceAll("\\s*\\+\\s*", " ");
					bean = new CatPvdBean();
					bean.setKeyword(keyword);
					bean.setCatid(catid);
				}
				if(list!=null&&!list.isEmpty()){
					ArrayList<SearchGoods> data = RelatedData(url, body_s,page,bean,page_flag);
					if(data!=null&&!data.isEmpty()){
						list.addAll(data);
					}
				}
				title = null;
			}else{
				//2.商店的商品搜索
				doc_s = null;
				if(Pattern.compile("(\\.en\\.alibaba\\.com)").matcher(url).find()){
					list = parseAliSearchS(body_s,url,key);
				}else if(url_flag_ali){
					list = parseExSearchS(body_s);
				}
			}
			body_s = null;
		}
		LOG.warn("search4: got a list,url= "+url);
		LOG.warn("parse:"+(new Date().getTime() - s));
		LOG.warn("search total:"+(new Date().getTime() - st1));
		page = null;
		if(list!=null&&!list.isEmpty()){
			list.get(0).setIp(set.getIp());
		}
		return list;
	}
	
	
	/**代理服务器使用去请求搜索数据
	 * @param url
	 * @param cycle
	 * @return
	 * @throws Throwable
	 */
	public static ArrayList<SearchGoods> parseSearchGoods(String url, int cycle, CatPvdBean bean) throws Throwable{
		int temp = cycle;
		ServerDao sd = new ServerDao();
		int id;
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		ProxyAddress.count++;
		if(ProxyAddress.count > sd.query().size()){
			ProxyAddress.count = 1;
		}
		id = ProxyAddress.count-1;
		String page = DownloadMain.getContentJsoup(url,id,0);
		if(Pattern.compile("(Serv-U - Error Occurred)").matcher(page).find()
			||page.length()<1000){
			if(temp<2){
				temp++;
				id ++;
				page = DownloadMain.getContentJsoup(url,id,0);
			}
		}
		long st1 = new Date().getTime();
		boolean find = Pattern.compile("(taobao)").matcher(url).find();
		if(find){
			List<String> tepp = DownloadMain.getSpiderContextList1("(?:nav-cat)(.*?)(?:\")", page);
			for(String t:tepp){
				String result = t.trim();
				page = page.replaceAll(t, result);
			}
		}
		//解析搜索url
		Document doc_s = Jsoup.parse(page);
		//获取element
		Element body_s = doc_s.body(); 
		String title = doc_s.title();
		boolean find2 = Pattern.compile("(我喜欢)").matcher(title).find();
		if(find){
			if(!find2){
				list = parseTBSearch(body_s,page);
				long ed = new Date().getTime();
				LOG.warn("parseSearchGoods get TB keyword："+(ed-st1));
			}
		}else if(Pattern.compile("(alibaba)").matcher(url).find()){
			list = parseAliSearchA(body_s,false);
			if(list.isEmpty()){
				list = parseAliSearchW(body_s,false);
				LOG.warn("2........");
			}
			long ed = new Date().getTime();
			LOG.warn("parseSearchGoods get alibaba keyword："+(ed-st1));
		}else if(Pattern.compile("(list.tmall.com)").matcher(url).find()){
			list = parseTMSearch(body_s);
			long ed = new Date().getTime();
			LOG.warn("parseSearchGoods get tmall keyword："+(ed-st1));
		}
		if(list!=null&&!list.isEmpty()){
			ArrayList<SearchGoods> data = RelatedData(url, body_s,page,bean,false);
			if(data!=null&&!data.isEmpty()){
				list.addAll(data);
			}
		}
		//同一个url最多3次获取搜索结果数据
		if(list.isEmpty()){
			if(temp<2){
				temp++;
				list = parseSearchGoods(url, temp,bean);
			}
		}
		return list;
	}
	
	
	
	/**parse Aliexpress 商店搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseExSearchS(Element body_s){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		if(list!=null){
			list.clear();
		}
		Elements Goods = body_s.select("li[class=item]");
		Elements Goods3 = body_s.select("li[class=item item-simple]");
		if(Goods!=null&&Goods3!=null&&!Goods3.isEmpty()){
			Goods.addAll(Goods3);
		}
		String img = "div[class=img]";
		String name = "h3";
		String price = "div[class=cost]";
		String solder = "span[class=recent-order]";
		String min = "span[class=min-order]";
		String discount = "span[class=rate]";
		String oprice = "div[class=cost-old]";
		list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		
		if(list==null||list.isEmpty()){
			Goods = body_s.select("li[class=lin4]");
			oprice = "em[class=yjn]";
			discount = "div[class=off]";
			img = "div[class=img]";
			name = "div[class=desc]";
			price = "div[class=pr1]";
			solder = "div[class=order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("div[class=partbd partbdleft partbdtop partbd-buynow0 css3bd]");
			Elements Goods2 = body_s.select("div[class=partbd  partbdtop partbd-buynow0 css3bd]");
			Elements Goods5 = body_s.select("div[class=partbd partbdleft  partbd-buynow0 css3bd]");
			Elements Goods4 = body_s.select("div[class=partbd   partbd-buynow0 css3bd]");
			Goods.addAll(Goods2);
			Goods.addAll(Goods4);
			Goods.addAll(Goods5);
			oprice = "em[class=yjn]";
			discount = "div[class=zk]";
			img = "a[class=img-b]";
			name = "div[class=bbxx bbxx-zk1]";
			price = "em[class=jgn]";
			solder = "em[class=xln]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		
		if(list==null||list.isEmpty()){
			Goods = body_s.select("li[class=itemlist]");
			img = "a";
			name = "a[class=titbox]";
			price = "div[class=prbox]";
			solder = "span[class=order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("li[class=item]");
			img = "a";
			name = "div[class=title]";
			price = "a[class=price]";
			solder = "span[class=recent-order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("ul[class=um-proList]").select("li");
			img = "div[class=picBox]";
			name = "div[class=names]";
			price = "div[class=price]";
			solder = "span[class=recent-order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("div[class=part]");
			Elements Goods2 = body_s.select("div[class=bbmiddle]");
			if(Goods2!=null&&!Goods2.isEmpty()){
				Goods.addAll(Goods2);
			}
			img = "div[class=bbpic]";
			name = "div[class=bbxx]";
			price = "em[class=jgn]";
			solder = "em[class=xln]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
			
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("li[class=rn_itm]");
			img = "div[class=rn_itm_img]";
			name = "p[title=title title_2]";
			price = "p[class=price]";
			solder = "p[class=soldcount]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("ul[class=fn_con_lin fn_con_lin_fst]").select("li");
			img = "div[class=fn_con_itm_inn_img]";
			name = "p[title=title title_2]";
			price = "p[class=price]";
			solder = "p[class=soldcount]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("ul[class=item]").select("li");
			img = "a[class=imgwrap]";
			name = "div[class=title]";
			price = "span[class=price]";
			solder = "span[class=order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
			
		}
		if(list==null||list.isEmpty()){
			Goods = body_s.select("div[class=m8_item]");
			img = "span";
			name = "div[class=title]";
			price = "div[class=pri1]";
			solder = "span[class=order]";
			min = "span[class=min-order]";
			list = parseExStore(Goods, img, name, price, solder, min,discount,oprice);
		}
		LOG.warn("get aliexpress store:"+(new Date().getTime()-start));
		return list;
	}
	
	
	
	
	
	/**parse Aliexpress 基础店铺
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	private static ArrayList<SearchGoods> parseExStore(Elements Goods, String imgselect,
                                                       String nameselect, String priceselect, String solderselect, String minselect,
                                                       String discountselect, String opriceselect){
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image = null;
		String image_url = null;
		String name_url = null;
		String goods_name = null;
		String goods_minOrder = null;
		String goods_price = null;
		String goods_solder = null;
		String goods_dispatch = null;
		String goods_fee = null;
		String goods_shop = null;
		String goods_unit = null;
		String goods_price_orgin = null;
		String goods_price_min = null;
		String goods_price_max = null;
		String goods_minorder_min= null;
		String goods_discount = null;
		if(Goods!=null&&!Goods.isEmpty()){
			Elements imgs;
			StringBuilder sb = new StringBuilder();
			DecimalFormat format = new DecimalFormat("#0.00");
			for(Element goods:Goods){
				sg = new SearchGoods();
				//获取商品图片
				imgs = goods.select(imgselect);
				goods_image = DownloadMain.getSpiderContext(imgs.select("img").toString(),"(?:src=\")(.*?)(\")");
				if(goods_image.isEmpty()){
					goods_image = DownloadMain.getSpiderContext(imgs.toString(),"(?:url\\()(.*?)(\\))");
				}
				//获取商品图片附带的商品链接
				image_url = DownloadMain.getSpiderContext(imgs.select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品名称
				goods_name = goods.select(nameselect).text();
				
				if(goods_name==null||goods_name.isEmpty()){
					goods_name = goods.select("div[class=title]").text();
					goods_name = goods_name.isEmpty()?goods.select("p[class=title title_3]").text():goods_name;
					if(goods_name==null||goods_name.isEmpty()){
						goods_name = DownloadMain.getSpiderContext(goods.select("a").toString(), "(?:title=\")(.*?)(?:\")");
					}
				}
				if(Pattern.compile("(\\d+)").matcher(goods_name).matches()){
					goods_name  ="";
				}
				//获取商品名称附带的链接
				name_url = DownloadMain.getSpiderContext(goods.select(nameselect).select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				if(image_url.isEmpty()&&name_url.isEmpty()){
					name_url = DownloadMain.getSpiderContext(goods.toString(), "(?:href=\")(.*?)(\")");
				}
				//商品价格
				goods_price = goods.select(priceselect).text().trim();
				if("span[class=price]".equals(priceselect)&&(goods_price==null||goods_price.isEmpty())){
					goods_price = goods.select("span[class=num]").text().trim();
				}
				if(goods_price==null||goods_price.isEmpty()){
					goods_price = goods.select("div[class=price]").text().trim();
					goods_price = goods_price.isEmpty()?goods.select("div[class=detail]").text().trim():goods_price;
				}
				goods_price = DownloadMain.getSpiderContext(goods_price, "(\\d+,*\\d*\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
				if(goods_price!=null&&!goods_price.isEmpty()){
					if(goods_price.indexOf("-")>0){
						String[] split = goods_price.split("-");
						goods_price_min = DownloadMain.getSpiderContext(split[0], "(\\d+\\.*\\d*)");
						goods_price_max = DownloadMain.getSpiderContext(split[1], "(\\d+\\.*\\d*)");
					}else{
						goods_price_min = DownloadMain.getSpiderContext(goods_price, "(\\d+\\.*\\d*)");
						goods_price_max = null;
					}
				}
				//商品折扣  只有当限时促销价格 比销售价格(正常sales price)低 30% 时，才 销售价格(正常sales price), 否则就用限时促销价-20151230
				goods_price_orgin = goods.select(opriceselect).text().replace("US", "").replace("$", "").trim();
				goods_price_orgin = goods_price_orgin.isEmpty()?goods.select("div[class=oldprice]").text().replace("US", "").replace("$", "").trim():goods_price_orgin;
				goods_discount = goods.select(discountselect).text();
				goods_discount = goods_discount.isEmpty()?goods.select("div[class=destitle]").text():goods_discount;
				goods_discount = DownloadMain.getSpiderContext(goods_discount, "(\\d+)");
				if(!goods_discount.isEmpty()&&(Double.valueOf(goods_discount)>30||Math.abs(30-Double.valueOf(goods_discount))<0.0001)){
					if(goods_price_min!=null&&!goods_price_min.isEmpty()){
						goods_price =format.format(Double.valueOf(goods_price_min)*1.2) ;
					}
					if(goods_price_max!=null&&!goods_price_max.isEmpty()){
						goods_price +="-"+format.format(Double.valueOf(goods_price_max)*1.2) ;
					}
				}
				//商品最小订量
				goods_minOrder = goods.select(minselect).text();
				if(goods_minOrder==null||goods_minOrder.isEmpty()){
					goods_minOrder = "1 piece";
				}
				//3美元为每个商品的最小定量 -20151230
				if(goods_price_max!=null&&!goods_price_max.isEmpty()){
					goods_price_min = goods_price_max;
				}
				goods_minorder_min = DownloadMain.getSpiderContext(goods_minOrder, "(\\d+)");
				goods_unit = goods_minOrder.replace(goods_minorder_min, "").trim();
				if(goods_minOrder.indexOf("/")>0){
					goods_minorder_min = "1";
					goods_unit = goods_minOrder.substring(goods_minOrder.indexOf("/")+1).trim();
				}
				if(goods_price_min!=null&&(!goods_price_min.isEmpty())&&goods_minorder_min!=null&&!goods_minorder_min.isEmpty()){
					if(Double.valueOf(goods_price_min)*Integer.valueOf(goods_minorder_min)<3){
						goods_minorder_min = String.valueOf(3/(Double.valueOf(goods_price_min)));
						goods_minorder_min = Math.ceil(Double.valueOf(goods_minorder_min))+"";
						goods_unit = "1.0".equals(goods_minorder_min)?goods_unit:goods_unit+"s";
						goods_minOrder = goods_minorder_min.replace(".0", "") +" "+ goods_unit;
					}
				}
				goods_solder = goods.select(solderselect).text().replace("Order", "").replace("s", "").replace("(", "").replace(")", "").trim();
				goods_solder = goods_solder.isEmpty()?goods.select("span[class=order-num]").text().replaceAll("\\D+", "").trim():goods_solder;
				
				//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
				if(name_url!=null&&Pattern.compile("(aliexpress\\.com/store/product)").matcher(name_url).find()){
					name_url = name_url.replace("store/product", "item").replaceAll("/\\d+_", "/");
				}
				if(image_url!=null&&Pattern.compile("(aliexpress\\.com/store/product)").matcher(image_url).find()){
					image_url = image_url.replace("store/product", "item").replaceAll("/\\d+_", "/");
				}
				
				if(image_url!=null&&image_url.length() == 0){
					sg.setGoods_url(TypeUtils.encodeGoods(name_url));
				}else{
					sg.setGoods_url(TypeUtils.encodeGoods(image_url));
				}
				if(goods_image!=null&&!goods_image.isEmpty()&&(!image_url.isEmpty()||!name_url.isEmpty())&&image_url.length()>15){
					sg.setGoods_image(goods_image);
					sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
					sg.setGoods_minOrder(goods_minOrder);
					goods_price = goods_price.isEmpty()?null:goods_price;
					sg.setGoods_price(goods_price);
					sg.setGoods_solder(goods_solder);
					sg.setGoods_dispatch(goods_dispatch);
					sg.setGoods_free(goods_fee);
					sg.setGoods_shop(goods_shop);
					sg.setKey_type("goods");
					sg = modefindPrice(sg,false);
					list.add(sg);
				}
				goods_fee = null;
				goods_shop = null;
				goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				goods_solder = null;
				goods_dispatch = null;
				goods_price_max = null;
				goods_price_min = null;
				sg = null;
				sb.delete(0, sb.length());
			}
		}
		return list;
	}

	
	/**parse Aliexpress 商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseExSearch(Element body_s, Boolean key){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image = null;//商品图片
		String image_url = null;//图片上附带的商品链接
		String name_url = null;//商品名上附带的链接
		String goods_name = null;//商品名称
		String goods_minOrder = null;//商品最小订量
		String goods_price = null;//商品价格
		String goods_unit = null;//商品售卖单位
//		String goods_price_orgin = null;//
		double morder_price_min = 0.0;//用于确定最小订量的基准价格
		double goods_price_min = 0.0;//价格最低价
		double goods_price_max = 0.0;//最高价
		String goods_minorder_min= null;//商品最小订量
		String goods_discount = null;//商品折扣率
		double goods_discount_d = 0.0;//商品折扣率（double）
		String goods_solder = null;//已经售出数量
		String goods_dispatch = null;//48小时派件
		String goods_fee = null;//运费
		double goods_fee_d = 0;//运费（double）
		String goods_shop = null;//商店
		String online = null;//店家在线atm16 atm-link
		String goods_similar=null;//相似商品数量tkhb-more-summ tkhb-bottom
		
		Elements Goods = body_s.select("div[class=item]");
		Elements category = body_s.select("div[id=breadView]");
		if(list!=null){
			list.clear();
		}
		if(Goods!=null&&!Goods.isEmpty()){
			int goods_num = Goods.size();
			Element goods = null;
			DecimalFormat format = new DecimalFormat("#0.00");
			for(int i=0;i<goods_num;i++){
				goods=Goods.get(i);
				sg = new SearchGoods();
				//获取商品图片
				goods_image = DownloadMain.getSpiderContext(goods.select("div[class=img img-border]").select("img").toString(),"(?:src=\")(.*?)(\")");
				//获取商品图片附带的商品链接
				image_url = DownloadMain.getSpiderContext(goods.select("div[class=pic]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品名称
				goods_name = goods.select("h3").text();
				//获取商品名称附带的链接
				name_url = DownloadMain.getSpiderContext(goods.select("h3").select("a").toString(), "(?:href=\")(.*?)(\")");
				//价格
				Elements prices = goods.select("span[class=lot-price]").select("span[itemprop=price]");
				if(prices.size()>0){
					goods_price = prices.text().replace("US", "").replace("$", "").trim();
					goods_unit = goods.select("span[class=lot-price]").select("span[class=unit]").text().replaceAll("\\(.*\\)", "").trim();
				}else{
					goods_price = goods.select("span[itemprop=price]").text().replace("US", "").replace("$", "").trim();
				}
				goods_price = goods_price.replaceAll(",", "").replaceAll("\\s+", "").trim();
				goods_price = DownloadMain.getSpiderContext(goods_price, "(\\d+\\.*\\d*-*\\d*\\.*\\d*)");
				if(goods_price!=null&&!goods_price.isEmpty()){
					if(goods_price.indexOf("-")>0){
						String[] split = goods_price.split("-");
						goods_price_min = Double.valueOf(DownloadMain.getSpiderContext(split[0], "(\\d+\\.*\\d*)"));
						goods_price_max = Double.valueOf(DownloadMain.getSpiderContext(split[1], "(\\d+\\.*\\d*)"));
					}else{
						goods_price_min = Double.valueOf(DownloadMain.getSpiderContext(goods_price, "(\\d+\\.*\\d*)"));
						goods_price_max = 0.0;
					}
				}
				//商品折扣  只有当限时促销价格 比销售价格(正常sales price)低 30% 时，显示限时促销价格*1.2, 否则就用限时促销价-20151230
				goods_discount = goods.select("span[class=new-discount-rate]").text();
				goods_discount = DownloadMain.getSpiderContext(goods_discount, "(\\d+)");
				if(goods_price!=null&&!goods_price.isEmpty()&&!goods_discount.isEmpty()){
					goods_discount_d = Double.valueOf(goods_discount);
					if(goods_discount_d>30||30-goods_discount_d<0.0001){
						if(goods_price_min!=0){
							goods_price_min =goods_price_min*1.2 ;
							goods_price =format.format(goods_price_min) ;
						}
						if(goods_price_max!=0){
							goods_price_max = goods_price_max*1.2;
							goods_price +="-"+format.format(goods_price_max);
						}
					}
				}
				//最小订量
				goods_minOrder = goods.select("span[class=min-order]").text();
				if(goods_minOrder==null||goods_minOrder.isEmpty()){
					if(goods_unit==null||goods_unit.isEmpty()){
						Elements units = goods.select("span[class=unit]");
						goods_unit = units.size()>0?units.get(0).text():goods_unit;
					}
					goods_minOrder = "1";
				}
				
				//3美元为每个商品的最小定量 -2015-12-30
				morder_price_min = goods_price_min;
				if(goods_price_max!=0){
					morder_price_min = goods_price_max;
				}
				goods_minorder_min = DownloadMain.getSpiderContext(goods_minOrder, "(\\d+)");
				if(goods_unit==null||goods_unit.isEmpty()){
					goods_unit = goods_minOrder.replace(goods_minorder_min, "").trim();
				}
				if(goods_minOrder.indexOf("/")>0){//100 piece/lot
					goods_minorder_min = "1";
					goods_unit = goods_minOrder.substring(goods_minOrder.indexOf("/")+1).trim();
				}
				if(morder_price_min!=0&&goods_minorder_min!=null&&!goods_minorder_min.isEmpty()){
					if(Double.valueOf(goods_price_min)*Integer.valueOf(goods_minorder_min)<3){
						goods_minorder_min = String.valueOf(3/morder_price_min);
						goods_minorder_min = Math.ceil(Double.valueOf(goods_minorder_min))+"";
						goods_unit = "1.0".equals(goods_minorder_min)?goods_unit:goods_unit+"s";
						goods_minOrder = goods_minorder_min.replace(".0", "");
					}
				}
				
				//已经售出商品数量
				goods_solder = goods.select("em[title=Total Orders]").text().trim();
				goods_solder = DownloadMain.getSpiderContext(goods_solder, "(\\d+)");
				goods_solder = goods_solder.isEmpty()?"0":goods_solder;
				//运费
				goods_fee = goods.select("dd[class=price]").select("span[class=value]").text();
				goods_fee = DownloadMain.getSpiderContext(goods_fee, "(\\d+\\.*\\d*)");
				//商品不是免邮商品
				if(goods_fee!=null&&!goods_fee.isEmpty()){
					goods_fee_d = Double.valueOf(goods_fee);
					//若商品价格大于20，且商品运费大于价格的50%  商品价格修正为  商品价格+运费
					if(goods_price_min>20&&goods_fee_d>goods_price_min*0.5){
						goods_price_min = goods_price_min+goods_fee_d;
						goods_price = format.format(goods_price_min);
						if(goods_price_max!=0){
							goods_price_max = goods_price_max+goods_fee_d;
							goods_price +="-"+format.format(goods_price_max);
						}
					}
				}
				//免邮  freeshipping
				goods_fee = goods.select("strong[class=free-s]").text();
				//店铺
				goods_shop = goods.select("div[class=store-name util-clearfix]").text();
				//店家在线
				online = goods.select("a[class=atm16 atm-link]").attr("title");
				online = online.isEmpty()?"0":"1";
				//相似商品数量
				goods_similar=goods.select("div[class=tkhb-more-summ tkhb-bottom]").select("a").text().trim();
				goods_similar = DownloadMain.getSpiderContext(goods_similar, "(\\d+)");
				goods_similar = goods_similar.isEmpty()?"1":goods_similar;
				//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
				//(\\(*[sS]hip\\s*[fF]rom\\s*[uU][sS]\\)*,*!*\\:*)
				if(goods_name!=null&&!Pattern.compile("(\\(*[sS]hip.*\\s*[fF]rom\\s*[uU][sS]\\)*,*!*\\:*)").matcher(goods_name).find()){
					name_url = image_url.length()!= 0?TypeUtils.modefindUrl(image_url, 1):TypeUtils.modefindUrl(name_url, 1);
					name_url = TypeUtils.encodeGoods(name_url);
					name_url =goods_fee!=null&&!goods_fee.isEmpty()?"&s=y"+name_url:name_url;
					sg.setGoods_url(name_url);
					goods_name = modefindName(goods_name);
					sg.setGoods_image(goods_image);
					sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
					goods_minOrder = goods_minOrder!=null?goods_minOrder.replaceAll("\\D+", "").trim():"1";
					goods_minOrder = goods_minOrder==null||goods_minOrder.isEmpty()?"1":goods_minOrder;
					sg.setGoods_minOrder(goods_minOrder);
					sg.setGoods_price(goods_price);
					sg.setGoods_solder(goods_solder);
					sg.setGoods_dispatch(goods_dispatch);
					sg.setGoods_free(goods_fee);
					sg.setGoods_shop(goods_shop);
					sg.setSeller_online(online);
					sg.setGoods_similar(goods_similar);
					sg.setKey_type("goods");
					//扭扭车价格特殊处理
					sg = modefindPrice(sg,key);
					list.add(sg);
				}
				goods_fee = null;
				goods_shop = null;
				goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				goods_price_min = 0.0;
				goods_price_max = 0.0;
				goods_minorder_min = null;
				goods_solder = null;
				goods_dispatch = null;
				goods_discount = null;
				morder_price_min = 0.0;
				goods_unit = "";
				goods = null;
				sg = null;
			}
		}
		
		//搜索导航
		if(list!=null&&!list.isEmpty()&&category!=null&&!category.isEmpty()){
			Elements select = category.select("a");
			String cid = null;
			String cname = null;
			String name = null;
			StringBuilder sb = new StringBuilder();
			sb.append("<span class=\"cathome\">").append("<a href=\"").append("http://www.import-express.com").append("\">Home</a></span>");
			for(int i=0;i<select.size();i++){
				cid=DownloadMain.getSpiderContext(select.get(i).attr("href"), "category/\\d+/").replaceAll("\\D+", "").trim();
				cname = select.get(i).attr("title").replace("&amp;", "&");
				name = cname.replaceAll("\\s+", "%20").replaceAll("'", "%27").replaceAll("&", "%26");
				name = name.split("%20")[0].split("%27")[0];
				if(cid!=null&&!cid.isEmpty()&&name!=null&&!name.isEmpty()){
					sb.append("<span class=\"divider\">&gt;</span>");
					sb.append("<span>").append("<a href=\"").append("/cbtconsole/goodsTypeServerlet?keyword=")
					  .append(name).append("&website=a&srt=order-desc&catid=").append(cid).append("\">").append(cname).append("</a></span>");
				}
			}
			String keyw = category.select("strong[class=active]").text().replace("&quot", "\"");
			if(keyw==null||keyw.isEmpty()){
				Elements soans = category.select("span");
				keyw = "\""+soans.get(soans.size()-1).text()+"\"";
			}
			sb.append("<span class=\"divider\">&gt;</span>").append("<span class=\"activekey\">").append(keyw).append("</span>");
		    
			String cunt = category.select("strong[class=search-count]").text();
			if(cunt!=null&&!cunt.isEmpty()){
				sb.append("&nbsp;&nbsp;<span class=\"catrseault\">").append(cunt).append(" results</span>");
			}
			sg = new SearchGoods();
			sg.setKey_type("categps");
			sg.setKey_url(sb.toString());
			list.add(sg);
		}
		long end = new Date().getTime();
		LOG.warn("parseExSearch get aliexpress keyword：" + (end-start));
		return list;
	}
	
	/**parse yiwugou 商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseYiSearch(String turl, int cycle){
		int tem = cycle;
		String page=null;
		String url = turl.replaceAll("(&amp;)+", "&");
		url = TypeUtils.modefindUrl(url, 0);
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		if(url!=null&&!url.isEmpty()&&Pattern.compile("(http://.+)").matcher(url).matches()){
			page = DownloadMain.getJsoup(url,0,null);
			while(page==null||page.isEmpty()){
				if(tem<2){
					tem++;
					page = DownloadMain.getJsoup(url,0,null);
				}else{
					break;
				}
			}
		}else{
			LOG.warn("The url is unvalid");
		}
		if(page!=null&&!page.isEmpty()&&!"httperror".equals(page)){
			//解析搜索url  
			Document doc_s = Jsoup.parse(page);
			//获取element  
			Element body_s = doc_s.body(); 
			SearchGoods sg;
			String goods_image = null;
			String image_url = null;
			String name_url = null;
			String goods_name = null;
			String goods_minOrder = null;
			String goods_price = null;
			String goods_solder = null;
			String goods_dispatch = null;
			String goods_fee = null;
			String goods_shop = null;
			//获取element
			Elements Goods = body_s.select("div[class=product_inf]");
			if(list!=null){
				list.clear();
			}
			if(Goods!=null&&!Goods.isEmpty()){
				StringBuilder sb = new StringBuilder();
				for(Element goods:Goods){
					
					//获取商品图片
					goods_image = DownloadMain.getSpiderContext(goods.select("p[class=imgsize]").select("img").toString(),"(?:src=\")(.*?)(\")");
					//获取商品图片附带的商品链接
					image_url = DownloadMain.getSpiderContext(goods.select("p[class=imgsize]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
					//获取商品名称
					goods_name = goods.select("ul[class=pro]").select("li[class=fontbold font14px fontbluelink]").text();
					//获取商品名称附带的链接
					name_url = DownloadMain.getSpiderContext(goods.select("ul[class=pro]").select("li[class=fontbold font14px fontbluelink]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
					
					goods_price = goods.select("font[class=fontfc7215 fontbold]").text().replace("RMB", "").replace("¥", "").trim();
					
					if("Price Negotiable".equals(goods_price)){
						goods_price = null;
					}
					if(goods_price!=null){
						sg = new SearchGoods();
						//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
						sb.delete(0, sb.length());
						sb.append("http://en.yiwugou.com/");
						if(image_url.length() == 0){
							sb.append(name_url);
						}else{
							sb.append(image_url);
						}
						sg.setGoods_url(TypeUtils.encodeGoods(sb.toString()));
						sg.setGoods_image(goods_image);
						sg.setGoods_name(goods_name);
						sg.setGoods_minOrder(goods_minOrder);
						sg.setGoods_price(goods_price);
						sg.setGoods_solder(goods_solder);
						sg.setGoods_dispatch(goods_dispatch);
						sg.setGoods_free(goods_fee);
						sg.setGoods_shop(goods_shop);
						sg.setKey_type("goods");
						list.add(sg);
					}
					goods_fee = null;
					goods_shop = null;
					goods_image = null;
					image_url = null;
					name_url = null;
					goods_name = null;
					goods_minOrder = null;
					goods_price = null;
					goods_solder = null;
					goods_dispatch = null;
					sg = null;
				}
			}
		}
		return list;
	}
	
	/**parse Alibaba  商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseAliSearchA(Element body_s, Boolean key){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image = null;
		String image_url = null;
		String name_url = null;
		String goods_name = null;
		String goods_minOrder = null;
		String goods_price = null;
		String goods_solder = null;
		String goods_dispatch = null;
		
		Elements Goods = body_s.select("div[data-role=item]");//2015.12.02
		if(Goods.isEmpty()){
			Goods = body_s.select("div[class=item-main]");
			if(Goods.isEmpty()){
				Goods = body_s.select("div[class=f-icon m-item]");
			}
			if(Goods.isEmpty()){
				Goods = body_s.select("div[class=item-main util-clearfix]");
			}
		}
		if(list!=null){
			list.clear();
		}
		if(Goods!=null&&!Goods.isEmpty()){
			Elements img = null;
			for(Element goods:Goods){
				sg = new SearchGoods();
				//获取商品图片
				img = goods.select("div[class=image]");
				if(img==null||img.isEmpty()){
					img = goods.select("div[class=item-img]");
				}
				if(img==null||img.isEmpty()){
					img = goods.select("div[class=util-valign img-wrap]");
				}
				if(img!=null){
					goods_image = DownloadMain.getSpiderContext(img.select("img").toString(),"(?:src=\")(.*?)(\")");
					//获取商品图片附带的商品链接
					image_url = DownloadMain.getSpiderContext(img.select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				}
				//获取商品名称
				goods_name = goods.select("h2[class=title]").text();
				//获取商品名称附带的链接
				name_url = DownloadMain.getSpiderContext(goods.select("h2[class=title]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				if(goods_name.isEmpty()){
					goods_name = goods.select("h3[class=title]").text();
					name_url = DownloadMain.getSpiderContext(goods.select("h3[class=title]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				}
				//获取商品最小订购数量
				Elements minO = goods.select("div[class=ids-attr]");
				if(!minO.isEmpty()){
					goods_minOrder = minO.get(0).select("div[class=value]").text();
				}else{
					minO = goods.select("div[class=min-order]");
					if(minO!=null&&!minO.isEmpty()){
						minO = goods.select("div[class=pmo]");
					}
					if(minO!=null&&!minO.isEmpty()){
						goods_minOrder = minO.select("div[class=min-order]").text().replaceAll("(\\(*\\s*Min. Order\\s*\\)*)", "");
						goods_price = minO.select("div[class=price]").text().replace("(", "").replace("FOB", "").replace(")", "")
								.replace("Price", "").replace("US", "").replace("$", "").trim();
					}else{
						minO = goods.select("div[class=attr]");
						for(Element m:minO){
							if(Pattern.compile("(Min. Order)").matcher(m.text()).find()){
								goods_minOrder = m.text().replaceAll("(\\(*\\s*Min. Order\\s*\\)*)", "");
							}else if(Pattern.compile("(Price)").matcher(m.text()).find()&&Pattern.compile("($)").matcher(m.text()).find()){
								goods_price  = DownloadMain.getSpiderContext( m.text(), "(\\d+\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
							}
						}
					}
				}
				//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
				if(image_url!=null&&image_url.length() == 0){
					sg.setGoods_url(TypeUtils.encodeGoods(name_url));
				}else{
					sg.setGoods_url(TypeUtils.encodeGoods(image_url));
				}
				if(goods_price!=null){
					int index = goods_price.length();
					if(index>0){
						int endIndex = goods_price.indexOf("/");
						goods_price = endIndex > 0?goods_price.substring(0, endIndex):goods_price;
					}
				}
				sg.setGoods_image(goods_image);
				sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
				sg.setGoods_minOrder(goods_minOrder);
				sg.setGoods_price(goods_price);
				sg.setGoods_solder(goods_solder);
				sg.setGoods_dispatch(goods_dispatch);
				sg.setKey_type("goods");
				sg = modefindPrice(sg,key);
				list.add(sg);
				goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				goods_solder = null;
				goods_dispatch = null;
				img = null;
				sg = null;
			}
		}
		LOG.warn("parseAliSearchA get alibaba keyword：" + (new Date().getTime()-start));
		return list;
	}
	
	/**parse Alibaba supplier商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseAliSearchS(Element body_s, String url, Boolean key){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image;
		String image_url;
		String name_url;
		String goods_name;
		String goods_minOrder = null;
		String tem_minOrder = null;
		String goods_price = null;
		String st = url.split("com/")[0];
		
		Elements goods = body_s.select("div[class=list-item util-clearfix");
		if(list!=null){
			list.clear();
		}
		if(goods!=null&&!goods.isEmpty()){
			int len = goods.size();
			Elements select;
			String text;
			for(int i=0;i<len;i++){
				sg = new SearchGoods();
				//获取商品图片
				goods_image = DownloadMain.getSpiderContext(goods.get(i).select("div[class=product-img savm]").toString(),"(?:data-src=\")(.*?)(\")");
				//获取商品图片附带的商品链接
				image_url = DownloadMain.getSpiderContext(goods.get(i).select("div[class=product-img savm]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品名称
				goods_name = goods.get(i).select("div[class=product-title]").text();
				//获取商品名称附带的链接
				name_url = DownloadMain.getSpiderContext(goods.get(i).select("div[class=product-title]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品最小订购数量
				select = goods.get(i).select("div[class=product-order-info ellipsis]");
				for(int j=0;j<select.size();j++){
					text = select.get(j).text();
					if(Pattern.compile("(min.*order)").matcher(text.toLowerCase()).find()){
						tem_minOrder = select.get(j).select("span").text();
						goods_minOrder = select.get(j).text().replace(tem_minOrder, "").trim();
						if(goods_minOrder!=null){
							goods_minOrder = DownloadMain.getSpiderContext(goods_minOrder, "(\\d+\\s*([pP]ieces*)*([sS]ets*)*)");
						}
					}else if(Pattern.compile("(US.*$.*)").matcher(text).find()){
						//获取商品价格
						goods_price = text.replace("US", "").replaceAll("\\s+", "").replace("$", "").trim();
					}
				}
				//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
				if(image_url.length() == 0){
					sg.setGoods_url(TypeUtils.encodeGoods(st+"com"+name_url));
				}else{
					sg.setGoods_url(TypeUtils.encodeGoods(st+"com"+image_url));
				}
				sg.setGoods_image(goods_image);
				sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
				sg.setGoods_minOrder(goods_minOrder);
				sg.setGoods_price(goods_price);
				sg.setGoods_solder(null);
				sg.setGoods_dispatch(null);
				sg.setKey_type("goods");
				sg = modefindPrice(sg,key);
				list.add(sg);
				
				goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				tem_minOrder = null;
				goods_price = null;
				sg = null;
			}
			long end = new Date().getTime();
			LOG.warn("parseAliSearchS get alibaba store：" + (end-start));
			if(list!=null&&!list.isEmpty()){
				list.addAll(preUrl(0, body_s, "", url,false));
			}
		}
		return list;
	}
	
	/**parse Alibaba promotion商品搜索链接，获取搜索到的商品列表
	 * http://www.alibaba.com/satin-phone-promotion.html
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseAliSearchM(Element body_s, Boolean key){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image;
		String image_url;
		String name_url;
		String goods_name;
		String goods_minOrder;
		String goods_price;
		
		Elements goods = body_s.select("div[class=productlist");
		if(list!=null){
			list.clear();
		}
		if(goods!=null&&!goods.isEmpty()){
			int len = goods.size();
			for(int i=0;i<len;i++){
				sg = new SearchGoods();
				//获取商品图片
				goods_image = DownloadMain.getSpiderContext(goods.get(i).select("div[class=img]").toString(),"(?:src=\")(.*?)(\")");
				//获取商品图片附带的商品链接
				image_url = DownloadMain.getSpiderContext(goods.get(i).select("div[class=img]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品名称
				goods_name = goods.get(i).select("div[class=title]").text();
				//获取商品名称附带的链接
				name_url = DownloadMain.getSpiderContext(goods.get(i).select("div[class=title]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
				//获取商品最小订购数量
				goods_minOrder = goods.get(i).select("div[class=mini-order]").select("span").text();
				//获取商品价格
				goods_price = goods.get(i).select("div[class=fob-price]").select("span").text().replace("US", "").replace("$", "").trim();
				
				//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
				if(image_url.length() == 0){
					sg.setGoods_url(TypeUtils.encodeGoods(name_url));
				}else{
					sg.setGoods_url(TypeUtils.encodeGoods(image_url));
				}
				
				sg.setGoods_image(goods_image);
				sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
				sg.setGoods_minOrder(goods_minOrder);
				sg.setGoods_price(goods_price);
				sg.setGoods_solder(null);
				sg.setGoods_dispatch(null);
				sg.setKey_type("goods");
				sg = modefindPrice(sg,key);
				list.add(sg);
				
				goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				sg = null;
			}
			long end = new Date().getTime();
			LOG.warn("parseAliSearchM get alibaba keyword：" + (end-start));
		}
		return list;
	}
	/**parse Alibaba  wholesale商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseAliSearchW(Element body_s, Boolean key){
		long start = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image;
		String image_url;
		String name_url;
		String goods_name;
		String goods_minOrder;
		String goods_price;
		String goods_solder;
		String goods_dispatch;
		String min_tem ;
        Elements goods = body_s.select("div[class=wrapper util-clearfix]");
        if(list!=null){
        	list.clear();
        }
        DownloadMain.write2file(body_s.toString(), "C:/Users/abc/Desktop/se2.txt");
        if(goods==null||goods.isEmpty()){
        	goods = body_s.select("div[class=m-wholesale-gallery-product-item]");
        }
        if(goods==null||goods.isEmpty()){
        	goods = body_s.select("div[class=m-wholesale-product-item]");
        }
        if(goods!=null&&!goods.isEmpty()){
        	int len = goods.size();
        	Elements img_element = null;
        	for(int i=0;i<len;i++){
        		sg = new SearchGoods();
        		img_element = goods.get(i).select("div[class=image]");
        		img_element = img_element.isEmpty()?goods.get(i).select("div[class=item-img]"):img_element;
        		img_element = img_element.isEmpty()?goods.get(i).select("div[class=item-sub]"):img_element;
        		//获取商品图片
        		goods_image = DownloadMain.getSpiderContext(img_element.toString(),"(?:src=\")(.*?)(\">)");
        		//获取商品图片附带的商品链接
        		image_url = DownloadMain.getSpiderContext(img_element.select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
        		//获取商品名称
        		goods_name = goods.get(i).select("h3[class=title]").text();
        		//获取商品名称附带的链接
        		name_url = DownloadMain.getSpiderContext(goods.get(i).select("h3[class=title]").select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
        		if(goods_name.isEmpty()){
        			goods_name = goods.get(i).select("h2[class=title]").text();
        			name_url = DownloadMain.getSpiderContext(goods.get(i).select("h2[class=title]")
        					.select("a[href$=.html]").toString(), "(?:href=\")(.*?)(\")");
        		}
        		//获取商品最小订购数量
        		goods_minOrder = goods.get(i).select("div[class=order]").select("strong").text();
        		if(goods_minOrder.isEmpty()){
        			min_tem = goods.get(i).select("div[class=order]").select("span").text();
        			goods_minOrder = goods.get(i).select("div[class=order]").text().replace(min_tem, "").trim();
        		}
        		Elements prop_item = goods.get(i).select("div[class=prop-item]");
        		if(prop_item.size()>0){
        			goods_minOrder = goods_minOrder.isEmpty()?prop_item.get(0).text().replace("MOQ:", ""):goods_minOrder;
        		}
        		prop_item = null;
        		//获取商品价格
        		goods_price = goods.get(i).select("div[class=price]").select("strong").text().replace("US", "").replace("$", "").trim();
        		if(goods_price.isEmpty()){
        			goods_price = goods.get(i).select("span[class=latest]").text().replace("US", "").replace("$", "").trim();
        		}
        		if(goods_price.isEmpty()){
        			goods_price = goods.get(i).select("div[class=price ladder-price]").select("strong").text().replace("US", "").replace("$", "").trim();
        		}
        		goods_price = DownloadMain.getSpiderContext(goods_price, "(\\d+\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
        		//获取商品已售出数量
        		goods_solder = goods.get(i).select("div[class=sold]").text();
        		goods_solder = goods_solder.length() > 5?goods_solder.substring(6):"0";
        		//若搜索结果列表上的商品图片没有附带商品链接，就使用商品名称上附带的链接
        		if(image_url.length() == 0){
        			sg.setGoods_url(TypeUtils.encodeGoods(name_url));
        		}else{
        			sg.setGoods_url(TypeUtils.encodeGoods(image_url));
        		}
        		goods_dispatch = goods.get(i).select("div[class=qualitysupplier]").select("span").text();	
        		sg.setGoods_image(goods_image);
        		sg.setGoods_name(SearchUtils.nameVert(goods_name,26));
        		sg.setGoods_minOrder(goods_minOrder);
        		sg.setGoods_price(goods_price);
        		sg.setGoods_solder(goods_solder);
        		sg.setGoods_dispatch(goods_dispatch);
        		sg.setKey_type("goods");
				sg = modefindPrice(sg,key);
        		list.add(sg);
        		goods_image = null;
				image_url = null;
				name_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				goods_solder = null;
				goods_dispatch = null;
				min_tem = null;
				sg = null;
        	}
        }
        LOG.warn("parseAliSearchW get alibaba keyword:" + (new Date().getTime()-start));
		return list;
	}
	
	/**parse Taobao商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseTBSearch(Element body_s, String  page){
//		long st = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image = null;
		String goods_name = null;
		String goods_url = null;
		String goods_minOrder = null;
		String goods_price = null;
		String goods_solder = null;
		String goods_dispatch = null;
		String goods_fee = null;
		String pic;
		Elements elements = body_s.select("div[class=item-box st-itembox]");
		if(elements!=null&&!elements.isEmpty()){
			for(Element e:elements){
				sg = new SearchGoods();
				pic = e.select("p[class=pic-box]").toString();
				goods_image = DownloadMain.getSpiderContext(pic, "(?:data-ks-lazyload=\")(.*?)(?:\")");
				goods_url = DownloadMain.getSpiderContext(pic, "(?:a href=\")(.*?)(?:\")");
				goods_name = e.select("h3[class=summary]").select("a").text();
				goods_price = e.select("div[class=col price g_price g_price-highlight]").text();
				goods_fee = "";
				goods_solder = e.select("div[class=col end dealing]").text();
				
				sg.setGoods_image(goods_image);
				if(Pattern.compile("(&amp)").matcher(goods_name).find()){
					goods_name = goods_name.replaceAll("amp;", "").replace("&", "").replaceAll("hellip;", "");
				}
	        	sg.setGoods_name(goods_name);
				sg.setGoods_url(TypeUtils.encodeGoods(goods_url));
	        	sg.setGoods_minOrder(goods_minOrder);
	        	sg.setGoods_price(goods_price);
	        	sg.setGoods_free(goods_fee);
	        	sg.setGoods_solder(goods_solder);
	        	sg.setGoods_dispatch(goods_dispatch);
	        	sg.setKey_type("goods");
				if(!goods_name.isEmpty()&&!goods_url.isEmpty()){
					list.add(sg);
				}
				goods_image = null;
				goods_url = null;
				goods_url = null;
				goods_name = null;
				goods_minOrder = null;
				goods_price = null;
				goods_solder = null;
				goods_dispatch = null;
				sg = null;
			}
		
		}else{
			
			List<String> re_list = DownloadMain.getSpiderContextList1("(?:\"nid\":)(.*?)(?:user_id)", page);
			if(re_list!=null&&!re_list.isEmpty()){
				for(String l:re_list){
					sg = new SearchGoods();
					goods_image = DownloadMain.getSpiderContext(l, "(?:\"pic_url\":\")(.*?)(?:\",)");
					goods_name = DownloadMain.getSpiderContext(l, "(?:\"raw_title\":\")(.*?)(?:\",)");
					goods_url = DownloadMain.getSpiderContext(l, "(?:\"detail_url\":\")(.*?)(?:\",)");
					goods_price = DownloadMain.getSpiderContext(l, "(?:\"view_price\":\")(.*?)(?:\",)");
					goods_fee = DownloadMain.getSpiderContext(l, "(?:\"view_fee\":\")(.*?)(?:\",)");
					goods_solder = DownloadMain.getSpiderContext(l, "(?:\"view_sales\":\")(.*?)(?:\",)");
					if(!goods_url.isEmpty()&&!"http:".equals(goods_url.subSequence(0, 5))){
						goods_url = "http:"+goods_url;
					}
					sg.setGoods_image(goods_image.replace("\\/", "/"));
					sg.setGoods_name(SearchUtils.dencodeUnicode(goods_name.replace("\\/", "/")));
					sg.setGoods_url(TypeUtils.encodeGoods(goods_url.replace("\\/", "/")));
					sg.setGoods_minOrder(goods_minOrder);
					sg.setGoods_price(goods_price.isEmpty()?"":"￥"+goods_price);
					sg.setGoods_free(goods_fee);
					sg.setGoods_solder(SearchUtils.dencodeUnicode(goods_solder).replace("�˸���", ""));
					sg.setGoods_dispatch(goods_dispatch);
					sg.setKey_type("goods");
					list.add(sg);
					goods_image = null;
					goods_url = null;
					goods_url = null;
					goods_name = null;
					goods_minOrder = null;
					goods_price = null;
					goods_solder = null;
					goods_dispatch = null;
					sg = null;
				}
			}
		}
//		long ed = new Date().getTime();
//		LOG.warn("解析淘宝搜索 :"+(ed-st));
		page = null;
		return list;
		
	}
	
	/**parse Taobao商品搜索链接，获取搜索到的商品列表
	 * @param searchUrl  搜索链接
	 * @return list   搜索到的商品列表
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> parseTMSearch(Element body_s){
//		long st = new Date().getTime();
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods sg;
		String goods_image = null;
		String goods_name = null;
		String goods_url = null;
		String goods_minOrder = null;
		String goods_price = null;
		String goods_solder = null;
		String goods_dispatch = null;
		String goods_fee = null;
		String img_page = "";
		
//		long st1 = new Date().getTime();
        Elements goods = body_s.select("div[class=product]");
        if(list!=null){
        	list.clear();
        }
        if(goods!=null&&!goods.isEmpty()){
        	for(Element g:goods){
        		sg = new SearchGoods();
        		img_page = g.select("div[class=productImg-wrap]").toString();
        		goods_image = DownloadMain.getSpiderContext(img_page, "(?:data-ks-lazyload=\")(.*?)(?:\">)");
        		if(goods_image.isEmpty()){
        			goods_image = DownloadMain.getSpiderContext(g.select("img").toString(), "(?:src=\")(.*?)(?:\">)");
        		}
        		goods_name = g.select("p[class=productTitle]").text();
        		goods_url = DownloadMain.getSpiderContext(g.select("p[class=productTitle]").toString(), "(?:href=\")(.*?)(?:\")");
        		goods_url = goods_url.isEmpty()?DownloadMain.getSpiderContext(g.select("div[class=productAlbum]").toString(), "(?:href=\")(.*?)(?:\")"):goods_url;
        		goods_price = g.select("p[class=productPrice]").text().replace("RMB", "").trim();
        		goods_fee = null;
        		goods_solder = g.select("p[class=productStatus]").select("em").text();
        		
        		sg.setGoods_image(goods_image);
        		sg.setGoods_name(goods_name);
        		sg.setGoods_url(TypeUtils.encodeGoods(goods_url));
        		sg.setGoods_minOrder(goods_minOrder);
        		sg.setGoods_price(goods_price);
        		sg.setGoods_free(goods_fee);
        		sg.setGoods_solder(goods_solder);
        		sg.setGoods_dispatch(goods_dispatch);
        		sg.setKey_type("goods");
        		list.add(sg);
        		goods_image = null;
        		goods_url = null;
        		goods_url = null;
        		goods_name = null;
        		goods_minOrder = null;
        		goods_price = null;
        		goods_solder = null;
        		goods_dispatch = null;
        		sg = null;
        	}
        	
        }
//		long ed = new Date().getTime();
//		LOG.warn("解析天猫搜索关键字 :"+(ed-st));
		return list;
		
	}
	
	 /**获取alibaba里类别等信息
	 * @param url
	 * @return
	 * @throws Throwable
	 */
	public static ArrayList<SearchGoods> RelatedData(String url, Element body_s, String page, CatPvdBean bean, boolean page_flag){
		 ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		 int flag = 6;
		 if(Pattern.compile("(alibaba)").matcher(url).find()){
			 flag = 0;//alibaba
		 }else if(Pattern.compile("(aliexpress)").matcher(url).find()){
			 flag = 1;//aliexpress
		 }else if(Pattern.compile("(taobao)").matcher(url).find()){
			 flag = 2;//tb
		 }else if(Pattern.compile("(tmall)").matcher(url).find()){
			 flag = 3;//tmall
		 }else if(Pattern.compile("(yiwugou)").matcher(url).find()){
			 flag = 4;//yiwugou
		 }
		 	 ArrayList<SearchGoods> searches = RelatedSearches(flag,body_s,page);
			 ArrayList<SearchGoods> reca = RelatedCategories(flag,body_s,bean);
			 ArrayList<SearchGoods> type = RelatedType(flag,body_s,url,page,bean);
			 ArrayList<SearchGoods> preUrl = preUrl(flag,body_s,page,url,page_flag);
			 if(searches!=null&&!searches.isEmpty()){
				 list.addAll(searches);
			 }
			 if(reca!=null&&!reca.isEmpty()){
				 list.addAll(reca);
			 }
			 if(type!=null&&!type.isEmpty()){
				 list.addAll(type);
			 }
			 if(preUrl!=null&&!preUrl.isEmpty()){
				 list.addAll(preUrl);
			 }
			 page = null;
			 searches = null;
			 reca = null;
			 type = null;
			 preUrl = null;
			 
		return list;
		 
	 }	
	
	
	 /**获取搜索结果中 Related Searches
		 * @param url
		 * @return
		 * @throws Throwable
	*/
	public static ArrayList<SearchGoods> RelatedSearches(int flag, Element body_s, String page){
		 ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		 SearchGoods cate;
		 String key_name=null;
		 String key_url="";
		 Elements rsearches;
		 Elements elements=null ;
		 if(flag==0){
			//wholesale
			 rsearches = body_s.select("div[class=related]");
			 elements = rsearches.select("a");
			 if(rsearches.isEmpty()){
				//alibaba
				 rsearches = body_s.select("div[class=ui-searchbar-related]");//alibaba2
				 elements = rsearches.select("a");
			 }
		 }else if(flag==1){
			 //aliexpress
			 rsearches = body_s.select("div[id=pop-search]");
			 elements = rsearches.select("a");
			 
		 }else if(flag==2){
			 rsearches = body_s.select("dl[class=supplement-relate]");//TB
			 elements = rsearches.select("a");
		 }
		 
		 if(list!=null){
			 list.clear();
		 }
		 if(elements!=null&&!elements.isEmpty()){
			 for(Element e:elements){
				 if(flag==0){
					 //alibaba
					 key_url = DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")");
				 }else if(flag==1){
					 //aliexpress
					 key_url = DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")").replaceAll("&amp;", "&")+"&g=y";
				 }else if(flag==2){//TB
					 key_url ="http://s.taobao.com" + DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")") + "&style=grid&tab=all&bcoffset=4&s=0";
				 }
				 if(!Pattern.compile("(javascript)").matcher(key_url).find()){
					 cate = new SearchGoods();
					 cate.setKey_type("Related Searches");
					 cate.setKey_name(SearchUtils.decodeString(e.text()));
					 cate.setKey_url(TypeUtils.encodeSearch(key_url));
					 if(flag==1){
						 cate.setKey_url("&catid=0"+TypeUtils.encodeSearch(key_url));
					 }
					 list.add(cate);
					 cate = null;
				 }
				 key_url = null;
			 }
		 }else  if(flag==2){//tb通过正则匹配得方式获取
			 //TB
			 String ssearch = DownloadMain.getSpiderContext(page, "(?:rsKeywords\":)(.*?)(?:\"\\],\")")+"\"";
			 List<String> listt = DownloadMain.getSpiderContextList1("(?:\")(.*?)(?:\")", ssearch);
			 String pre =  "http://s.taobao.com/search?q=";
			 String beh  = "&style=grid&tab=all&bcoffset=4&s=0";
			 for(int i=0;i<listt.size();i++){
				 cate = new SearchGoods();
				 key_name = listt.get(i);
				 key_url =pre + key_name +"&amp;rs=up&amp;rsclick=" +(i+1) + beh;
				 cate.setKey_type("Related Searches");
				 cate.setKey_name(key_name);
				 cate.setKey_url(TypeUtils.encodeSearch(key_url));
				 list.add(cate);
				 key_name = null;
				 key_url = null;
				 cate = null; 
			 }
			 listt = null;
			 ssearch = null;
		}
		 return list;
	 }	
	
	
	 
	 /**获取alibaba搜索结果中 Related Categories
	 * @param url
	 * @return
	 * @throws Throwable
	 */
	public static ArrayList<SearchGoods> RelatedCategories(int flag, Element body_s, CatPvdBean bean){
		 ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		 SearchGoods cate;
		 String key_type;
		 String key_url;
		 if(flag == 0){
			 //alibaba
			 Elements rsearches = body_s.select("div[class=m-categories m-categories-related]").select("ul[class=categorys]");
			 Elements elements = null;
			 if(rsearches.isEmpty()){
				 Elements select = body_s.select("div[class=col-sub]").select("div[class=box cate]");
				 rsearches = body_s.select("div[class=col-sub]").select("div[class=box]");
				 if(!select.isEmpty()){
					 rsearches.addAll(select);
				 }
				 if(rsearches!=null&&!rsearches.isEmpty()){
					 for(Element r:rsearches){
						 if(Pattern.compile("(Categories)").matcher(r.select("div[class=title]").text()).find()){
							 elements = r.select("a");
						 }
					 }
				 }else{
					 rsearches = body_s.select("div[class=cate]").select("div[class=box attrs-box]");
					 elements = rsearches.select("a");
					 if(rsearches==null||rsearches.isEmpty()){
						 rsearches = body_s.select("div[class=cate]").select("div[class=box]");
						 elements = rsearches.select("a");
					 }
				 }
			 }else{
				 elements = rsearches.select("li");
			 }
			 if(list!=null){
				 list.clear();
			 }
			 if(elements!=null&&!elements.isEmpty()){
				 for(Element e:elements){
					 if(!Pattern.compile("(View more)").matcher(e.text()).find()){
						 cate = new SearchGoods();
						 key_url = DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")");
						 if(!Pattern.compile("(javascript)").matcher(key_url).find()){
							 cate.setKey_type("Categories");
							 cate.setKey_name(e.text());
							 cate.setKey_url(TypeUtils.encodeSearch(key_url));
							 list.add(cate);
						 }
					 }
					 key_url = null;
					 cate = null;
				 }
			 }
			 Elements curr = rsearches.select("ul[class=items mitems]").select("li[class=item selected]");
			 if(curr!=null&&!curr.isEmpty()){
				 cate = new SearchGoods();
				 cate.setKey_type("Categories");
				 cate.setKey_name(curr.text());
				 cate.setKey_url(TypeUtils.encodeSearch("voiddiov"));
				 list.add(cate);
				 cate = null;
			 }
			 curr = null;
			 rsearches = null;
			 elements = null;
		 }else if(flag == 1){
			 //aliexpress
			 Elements categ = body_s.select("div[id=refine-category-list]").select("dl");
			 key_type = categ.size()>0?categ.get(0).select("dt[class=cate-title]").text():"";
			 if(Pattern.compile("(Categories)").matcher(key_type).find()){
				 Elements categories = categ.get(0).select("a");
				 if(categories!=null&&!categories.isEmpty()){
					 String catidlist = "";
					 String key_id = "";
					 for(Element c:categories){
						 cate = new SearchGoods();
						 key_url = c.attr("href");
						 key_id = c.attr("qrdata");
						 if(!Pattern.compile("(javascript)").matcher(key_url).find()){
							 if(GetFilterUtils.getCatidFilter(key_id)){
								 cate.setKey_type(key_type);
								 cate.setKey_name(c.text());
								 cate.setKey_url("&catid="+key_id);//+TypeUtils.encodeSearch(key_url)
								 list.add(cate);
							 }
							 catidlist = !key_id.isEmpty()?catidlist+key_id+",":catidlist;
						 } 
						 key_id = null;
						 key_url = null;
						 cate = null;
					 }
					 if(!catidlist.isEmpty()&&bean!=null){
						 ICatPvdDao dao = new CatPvdDao();
						 ArrayList<CatPvdBean> query = dao.query(bean.getKeyword(), bean.getCatid());
						 bean.setCatidlist(catidlist);
						 if(query!=null&&!query.isEmpty()){
							 dao.updateCList(bean);
						 }else{
							 dao.add(bean);
						 }
					 }
				 }
				 categories = null;
			 }
			 Elements curr = categ.select("span[class=current-cate]");
			 if(curr!=null&&!curr.isEmpty()){
				 cate = new SearchGoods();
				 cate.setKey_type(key_type);
				 cate.setKey_name(curr.text());
				 cate.setKey_url(TypeUtils.encodeSearch("voiddiov"));
				 list.add(cate);
				 cate = null;
			 }
			 key_type = null;
			 categ = null;
			 curr = null;
		 }
		 return list;
	 }
	
	/**获取alibaba搜索结果中 Related Type
	 * @param url
	 * @return
	 * @throws Throwable
	 */
	public static ArrayList<SearchGoods> RelatedType(int flag, Element body_s, String url, String page, CatPvdBean bean){
		
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods cate;
		String key_type;
		String key_name;
		String key_url;
		String key_img;
		Elements rsearches;
		Elements elements = null;
		String clear = "";
		if(list!=null){
			list.clear();
		}
		if(flag==0){
			/****************************alibaba start*********************************************/
			rsearches = body_s.select("div[class=m-categories m-categories-attributes]");
			if(rsearches==null||rsearches.isEmpty()){
				rsearches = body_s.select("div[id=J-product-attribute-refine]").select("div[class=m-attribute-filter m-attribute-filter-vertical]");
			}
			if(rsearches==null||rsearches.isEmpty()){
				//1.parseAliSearchW类型
				rsearches = body_s.select("div[class=col-sub]").select("div[id=J-m-refine]").select("div[class=box]");
				if(rsearches!=null&&!rsearches.isEmpty()){
					for(Element r:rsearches){
						if(!Pattern.compile("(Categories)").matcher(r.select("div[class=title]").text()).find()){
							elements = r.select("li[class=item]");
							elements.addAll(r.select("li[class=item selected]"));
							for(Element e:elements){
								if(!Pattern.compile("(View more)").matcher(e.text()).find()){
									cate  = new SearchGoods();
									key_type = r.select("div[class=title]").text();
									key_name = e.text();
									clear = e.select("a").text();
									if(!key_name.isEmpty()&&!clear.isEmpty()){
										key_name = "×".equals(clear)?key_name.replace("×", "")+"&nbsp;"+clear:key_name;
									}
									key_url = DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")");
									if(!Pattern.compile("(javascript:)").matcher(key_url).find()){
										cate.setKey_type(key_type);
										cate.setKey_name(key_name);
										cate.setKey_url(TypeUtils.encodeSearch(key_url));
										list.add(cate);
									}
								}
								key_type = null;
								key_url = null;
								key_name = null;
								cate = null;
							}}
						}
				}
			}else{
				//2.parseAliSearchA类型
				for(Element r:rsearches){
					elements = r.select("li");
					if(elements==null||elements.isEmpty()){
						elements = r.select("div[class=attr-item]");
					}
					for(Element e:elements){
						if(!Pattern.compile("(View more)").matcher(e.text()).find()){
							cate = new SearchGoods();
							key_type = r.select("h4").text();
							key_type = key_type.isEmpty()?r.select("span[class=box-title]").select("em").text():key_type;
							key_name = e.select("span[class=ui2-checkbox-customize-txt]").text();
							key_name = key_name.isEmpty()?e.text():key_name;
							clear = e.select("a").text();
							if(!key_name.isEmpty()&&!clear.isEmpty()){
								key_name = "X".equals(clear)?key_name.substring(1)+"&nbsp;"+clear:key_name;
							}
							key_url = DownloadMain.getSpiderContext(e.toString(), "(?:location.href=')(.*?)(?:';)").replace("\\x2f", "/").replace("\\x2d", "-");
							key_url = key_url.isEmpty()?DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")"):key_url;
							if(!Pattern.compile("(javascript)").matcher(key_url).find()){
								cate.setKey_type(key_type);
								cate.setKey_name(key_name);
								cate.setKey_url(TypeUtils.encodeSearch(key_url));
								list.add(cate);
							}}
						 key_type = null;
						 key_url = null;
						 key_name = null;
						 cate = null;
					}}}
		/****************************alibaba start*********************************************/	
	}else if(flag==1){
		/****************************aliexpress start*********************************************/	
		rsearches = body_s.select("dl[class=attribute-item]");
		Elements clears = body_s.select("div[id=clear-all-refine]");
		String catid = "0";
		if(clears!=null&&!clears.isEmpty()){
			cate = new SearchGoods();
			String a = clears.select("a").text();
			key_name = clears.text().replace(a, "")+"&nbsp;"+a;
			key_url = DownloadMain.getSpiderContext(clears.toString(), "(?:href=\")(.*?)(?:\")");
			catid = DownloadMain.getSpiderContext(key_url, "(CatId=\\d+)").replaceAll("\\D+", "");
			cate.setKey_type("");
			cate.setKey_name(key_name);
			cate.setKey_url("&catid="+catid+TypeUtils.encodeSearch(key_url));
			list.add(cate);
			key_url = null;
			key_name = null;
			cate = null;
			catid = "0";
		}
		Elements selected = body_s.select("dl[class=refine-selected]");
		if(selected!=null&&!selected.isEmpty()){
			for(Element s:selected){
				elements = s.select("a");
				for(Element e:elements){
					if(!Pattern.compile("(View more)").matcher(e.text()).find()){
						cate = new SearchGoods();
						key_type = s.select("dt").text();
						key_name = e.text()+ "&nbsp;"+"X";
						key_url = e.attr("href");
						if(!Pattern.compile("(javascript)").matcher(key_url).find()){
							cate.setKey_type(key_type);
							cate.setKey_name(key_name);
							catid = DownloadMain.getSpiderContext(key_url, "(CatId=\\d+)").replaceAll("\\D+", "");
							cate.setKey_url("&catid="+catid+TypeUtils.encodeSearch(key_url));
							list.add(cate);
							catid = "0";
						}
						key_type = null;
						key_url = null;
						key_name = null;
						cate = null;
					}}}
		}
		if(rsearches!=null&&!rsearches.isEmpty()){
			String pvidlist = "";
			String pdlist = null;
			PvidBean pvbean = null;
			IPvidDao pdao = new PvidDao();
			for(Element r:rsearches){
				elements = r.select("a");
				for(Element e:elements){
					if(!Pattern.compile("(View more)").matcher(e.text()).find()){
						cate = new SearchGoods();
						key_type = r.select("dt").text();
						key_name = e.text().trim();
						key_url = DownloadMain.getSpiderContext(e.toString(), "(?:href=\")(.*?)(?:\")");
						key_img = DownloadMain.getSpiderContext(e.select("img[class=lazy-load-img]").toString(), "(?:src=\")(.*?)(?:\")");
						if(!Pattern.compile("([A-Za-z0-9]+)").matcher(key_name).find()){
							key_name = DownloadMain.getSpiderContext(e.toString(), "(?:title=\")(.*?)(?:\")");
						}
						if(!Pattern.compile("(javascript)").matcher(key_url).find()){
							if(key_name!=null&&!key_name.isEmpty()){
								cate.setKey_type(key_type);
								cate.setKey_name(key_name);
								pvbean = new PvidBean();
								pvbean.setName(key_type);
								pvbean.setValue(key_name);
								if(key_img!=null&&!key_img.isEmpty()){
									cate.setKey_img(key_img);
									pvbean.setImg(key_img);
								}
								pdlist = e.attr("qrdata");
								if(pdlist!=null&&!pdlist.isEmpty()){
									catid = pdlist.substring(0,pdlist.indexOf("_"));
									pdlist = pdlist.substring(pdlist.indexOf("_")+1);
									pvidlist = pvidlist+pdlist+",";
									//记录规格名  规格值  以及 规格id至ali_pvidlist
									pvbean.setPvid(pdlist);
									if(pdao.queryExsis(pdlist)==0){
										pdao.add(pvbean);
									}
								}
								cate.setKey_url("&catid="+catid+TypeUtils.encodeSearch(key_url));
								list.add(cate);
								pdlist = null;
								pvbean = null;
								catid = "0";
							}
						}
						key_type = null;
						key_url = null;
						key_name = null;
						cate = null;
			}}}
			//记录所有规格id保存至ali_catpvd
			if(!pvidlist.isEmpty()&&bean!=null){
			 ICatPvdDao dao = new CatPvdDao();
			 ArrayList<CatPvdBean> query = dao.query(bean.getKeyword(), bean.getCatid());
			 bean.setPvidlist(pvidlist);;
			 if(query!=null&&!query.isEmpty()){
				 String plist = query.get(0).getPvidlist();
				 if(plist==null||plist.isEmpty()){
					 dao.updatePList(bean);
				 }
			 }else{
				 dao.add(bean);
			 }
			 query = null;
		 }
		}
	/****************************aliexpress end*********************************************/	
	}else if(flag==2){//TB
		String q = DownloadMain.getSpiderContext(url, "(?:q=)(.*?)(?:&)");
		if(!q.isEmpty()){
			String  pre = "http://s.taobao.com/search?tab=all&q="+q+"&style=grid&cps=yes&rsclick=1&";
			rsearches = body_s.select("div[class=nav-category]");
			Elements element = body_s.select("div[class=nav-category   show-2-line]");
			Elements elem = body_s.select("div[class=nav-category  navhackHasmore]");
			if(!element.isEmpty()){
				rsearches.addAll(element);
			}
			if(!elem.isEmpty()){
				rsearches.addAll(elem);
			}
			Elements ppath = null;
			String cat = "";
			String value = "";
			if(rsearches!=null&&!rsearches.isEmpty()){
				for(Element rs:rsearches){
					elements = rs.select("li");
					ppath = rs.select("ul");
					cat = DownloadMain.getSpiderContext(ppath.toString(), "(?:data-param-type=\")(.*?)(?:\" data-pid)");
					key_type = rs.select("h4").text();
					for(Element e:elements){
						cate = new SearchGoods();
						key_name = e.text();
						if("cat".equals(cat)){
							value = DownloadMain.getSpiderContext(e.toString(), "(?:data-param-value=\")(.*?)(?:\")");
							key_url = pre+cat+"="+value;
						}else{
							value = DownloadMain.getSpiderContext(e.toString(), "(?:data-ppath=\")(.*?)(?:\")");
							key_url = pre+cat+"="+value.replace(":", "%3A");
						}
						cate.setKey_type(key_type);
						cate.setKey_name(key_name);
						cate.setKey_url(TypeUtils.encodeSearch(key_url));
						list.add(cate);
						key_type = null;
						key_url = null;
						key_name = null;
						cate = null;
					}
				}
			}else{
				//TB无节点匹配
				if(rsearches!=null&&!rsearches.isEmpty()){
					for(Element rs:rsearches){
						elements = rs.select("li");
						ppath = rs.select("ul");
						cat = DownloadMain.getSpiderContext(ppath.toString(), "(?:data-param-type=\")(.*?)(?:\" data-pid)");
						key_type = rs.select("h4").text();
						for(Element e:elements){
							cate = new SearchGoods();
							key_name = e.text();
							if("cat".equals(cat)){
								value = DownloadMain.getSpiderContext(e.toString(), "(?:data-param-value=\")(.*?)(?:\")");
								key_url = pre+cat+"="+value;
							}else{
								value = DownloadMain.getSpiderContext(e.toString(), "(?:data-ppath=\")(.*?)(?:\")");
								key_url = pre+cat+"="+value.replace(":", "%3A");
							}
							cate.setKey_type(key_type);
							cate.setKey_name(key_name);
							cate.setKey_url(TypeUtils.encodeSearch(key_url));
							list.add(cate);
							key_url = null;
							key_name = null;
							cate = null;
						}
						key_type = null;
						}}}
				}
		}
		return list;
	}
	
	
	/**获取搜索结果页数的链接
	 * @param searchUrl
	 * @return
	 * @throws Throwable 
	 */
	public static ArrayList<SearchGoods> preUrl(int uflag, Element body_s, String page, String url, boolean page_flag){
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		SearchGoods cate ;
		String num=null;
		String url_page="test";
		List<String> tepp;
		String ts = "";
		Elements pre_url;
		Elements cmount;
		Element amount;
		String st = url.split("com/")[0];
		if(uflag==0){
			//alibaba
			int flag = 0;
			pre_url = body_s.select("div[id=pagination]").select("div[id=J-pager]");//.select("a");
			cmount = pre_url.select("span[class=ui-pagination-active]");
			amount = body_s.select("div[id=pagination]").select("input[id=J-total-page]").last();
			if(pre_url.isEmpty()){
				pre_url = body_s.select("div[class=navi]");//.select("a");
				flag = 1;
				if(pre_url.isEmpty()){
					pre_url = body_s.select("div[class=ui2-pagination-pages]");//.select("a");
					amount = pre_url.select("span[class=disable]").last();
					cmount = pre_url.select("span[class=current]");
					Elements next = pre_url.select("span[class=next disable]");
					if(!next.isEmpty()){
						amount = cmount.get(0);
					}else if(amount==null){
						Elements nouts = pre_url.select("a");
						if(nouts.size()>1){
							amount = nouts.get(nouts.size()-2);
						}
						nouts = null;
					}
					next = null;
				}
				if(pre_url.isEmpty()){
					pre_url = body_s.select("div[id=pagination-wrapper]");//.select("a");
					amount = pre_url.select("li").last();
					cmount = pre_url.select("li[class=ui-pagination-active]");
				}
				if(pre_url.isEmpty()){
					flag=2;
					pre_url = body_s.select("div[class=ui-pagination-navi util-left]");//.select("a");
					cmount = pre_url.select("span[class=ui-pagination-active]");
					Elements next = pre_url.select("span[class=ui-pagination-next ui-pagination-disabled]");
					if(next.isEmpty()){
						Elements amounts = pre_url.select("a");
						amount = amounts.size()>2?amounts.get(amounts.size()-2):null;
						amounts = null;
					}else{
						if(amount==null){
							amount = cmount.get(0);
						}
					}
					next = null;
				}
			}
			if(list!=null){
				list.clear();
			}
			if(amount!=null&&!amount.toString().isEmpty()){
				String amount_text = "";
				if(amount.text().isEmpty()){
					amount_text = DownloadMain.getSpiderContext(amount.toString(), "(?:value=\")(.*?)(?:\")");
				}else{
					amount_text = amount.text();
				}
				amount_text = DownloadMain.getSpiderContext(amount_text, "(\\d+)");
				if(!amount_text.isEmpty()){
					cate = new SearchGoods();
					cate.setKey_type("page amount");
					cate.setKey_url(url_page);
					cate.setKey_name(amount_text);
					list.add(cate);
					cate = null;
				}
				amount = null;
			}else{
				Elements ele = body_s.select("script");
				int index = ele.toString().indexOf("total");
				String mont = index>0?ele.toString().substring(index, index+8)+"ednd":"";
				mont = DownloadMain.getSpiderContext(mont, "(\\d+)");
				if(!mont.isEmpty()){
					cate = new SearchGoods();
					cate.setKey_type("page amount");
					cate.setKey_name(mont);
					cate.setKey_url(url_page);
					list.add(cate);
					cate = null;
				}
				ele = null;
				mont = null;
			}
			
			if(cmount!=null&&!cmount.toString().isEmpty()){
				String current = DownloadMain.getSpiderContext(cmount.text(), "(\\d+)");
				if(!current.isEmpty()){
					cate = new SearchGoods();
					cate.setKey_type("page current");
					cate.setKey_name(cmount.text());
					cate.setKey_url(url_page);
					list.add(cate);
					cate = null;
					cmount = null;
				}
			}
			int len = pre_url.size();
			for(int j=0;j<len;j++){
				cate = new SearchGoods();
				if(flag==1){
					num = pre_url.get(j).toString().replace("href=\"", "href=\"http://www.alibaba.com/");
					num = num.replace("/www.alibaba.com/wholesaler.alibaba.com/","/wholesaler.alibaba.com/");
				}else if(flag==2){
					num = pre_url.get(j).toString().replaceAll("href=\"\\s*", "href=\""+st+"com");
				}else{
					num = pre_url.get(j).toString();
				}
				tepp = DownloadMain.getSpiderContextList1("(?:href=\")(.*?)(?:\\s*\")", num);
				String t = null;
				for(int k=tepp.size()-1;k>0;k--){
					t = tepp.get(k);
					if(!Pattern.compile("(javascript)").matcher(t).find()){
						ts = t.replaceAll("(http\\://www.alibaba.com/)+", "http://www.alibaba.com/");
						num = num.replace(t, TypeUtils.encodeSearch(ts));
					}else{
						num = num.replace("href=\""+t+"\"", "");
					}
					t = null;
				}
				cate.setKey_type("next page");
				cate.setKey_name(num);
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				tepp = null;
				num = null;
			}
		}else if(uflag==3){//tmall
			pre_url = body_s.select("div[class=ui-page-wrap]").select("b[class=ui-page-num]");
			if(list!=null){
				list.clear();
			}
			Elements mount = body_s.select("input[name=totalPage]");
			if(mount!=null&&!mount.toString().isEmpty()){
				String text = DownloadMain.getSpiderContext(mount.toString(), "(?:value=\")(.*?)(?:\")");
				text = DownloadMain.getSpiderContext(text, "(\\d+)");
				if(!text.isEmpty()){
					cate = new SearchGoods();
					cate.setKey_type("page amount");
					cate.setKey_name(text);
					cate.setKey_url(url_page);
					list.add(cate);
					cate = null;
				}
			}
			cmount = body_s.select("b[class=ui-page-cur]");
			if(cmount!=null&&!cmount.toString().isEmpty()){
				cate = new SearchGoods();
				cate.setKey_type("page current");
				cate.setKey_name(cmount.text());
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
			}
			
			int len = pre_url.size();
			for(int j=0;j<len;j++){
				cate = new SearchGoods();
				num = pre_url.get(j).toString().replaceAll("href=\"", "href=\"http://list.tmall.com/search_product.htm");
				cate.setKey_type("next page");
				cate.setKey_name(num.replace("&lt;", "").replace("&gt;", ""));
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
			}
		}else if(uflag==2){
			//tb
			if(list!=null){
				list.clear();
			}
			Elements mount = body_s.select("span[class=page-info]");
			if(mount!=null&&!mount.text().isEmpty()){
				cate = new SearchGoods();
				cate.setKey_type("page amount");
				cate.setKey_name(mount.text().split("/")[1]);
//				cate.setKey_name(amount);
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				cate = new SearchGoods();
				cate.setKey_type("page current");
				cate.setKey_name(mount.text().split("/")[0]);
//				cate.setKey_name(current);
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				cate = new SearchGoods();
				cate.setKey_type("next page");
				cate.setKey_name("TB");
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
			}else{
				String amountt = DownloadMain.getSpiderContext(page, "(?:\"totalPage\":)(.*?)(?:\",)");
				String current = DownloadMain.getSpiderContext(page, "(?:\"currentPage\":)(.*?)(?:\",)");
				cate = new SearchGoods();
				cate.setKey_type("page amount");
//				cate.setKey_name(mount.text().split("/")[1]);
				cate.setKey_name(amountt);
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				cate = new SearchGoods();
				cate.setKey_type("page current");
//				cate.setKey_name(mount.text().split("/")[0]);
				cate.setKey_name(current);
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				cate = new SearchGoods();
				cate.setKey_type("next page");
				cate.setKey_name("TB");
				cate.setKey_url(url_page);
				list.add(cate);
				cate = null;
				amountt = null;
				current = null;
			}
		}else if(uflag==1){
			//aliexpress
			pre_url = body_s.select("div[class=ui-pagination-navi util-left]");
			cmount = pre_url.select("span[class=ui-pagination-active]");
			Elements amountt = body_s.select("span[id=pagination-max]");
			Elements page_select = pre_url.get(0).children();
			Elements  result_page = new Elements();
			if(page_flag){
				int current_num = Integer.parseInt(cmount.get(0).text())-6;
				for(int i=0;i<page_select.size();i++){
					Element p = page_select.get(i);
					String href = p.attr("href");
					if(!href.isEmpty()){
						p.attr("href", "&more=2"+TypeUtils.encodeSearch(href));
					}
					String text = p.text().replaceAll("\\D+", "").trim();
					if(!text.isEmpty()){
						int text_num = Integer.parseInt(text)-6;
						if(text_num>0){
							p.text(""+text_num);
							result_page.add(p);
						}
					}else{
						if(current_num==1&&(p.text().equals("Previous")||p.text().equals("Pre"))){
							p.removeAttr("href");
							p.tagName("span");
						}
						result_page.add(p);
					}
				}
			}else{
				for(Element p:page_select){
					String href = p.attr("href");
					if(!href.isEmpty()){
						p.attr("href", TypeUtils.encodeSearch(href));
					}
					result_page.add(p);
				}
			}
			num = result_page.toString().replaceAll(">\\s*<", "><span>&nbsp;&nbsp;</span><");
			num = num.toString().replaceAll("&amp;", "&");
			cate = new SearchGoods();
			cate.setKey_type("next page");
			cate.setKey_name(num);
			cate.setKey_url(url_page);
			list.add(cate);
			cate = null;
			//当前页
			cate = new SearchGoods();
			cate.setKey_type("page current");
			String cmout_page = cmount.text();
			cate.setKey_name(cmout_page);
			cate.setKey_url(url_page);
			list.add(cate);
			//总页数
			cate = new SearchGoods();
			cate.setKey_type("page amount");
			String amout_page = amountt.text().isEmpty()?cmount.text():amountt.text();
			cate.setKey_name(amout_page);
			cate.setKey_url(url_page);
			list.add(cate);
			cate = null;
			num = null;
			tepp = null;
			cmount = null;
			amountt = null;
			pre_url = null;
		}else if(uflag==4){
			//yiwugou
		}
		url_page=null;
		tepp = null;
		pre_url = null;
		cmount = null;
		amount = null;
		st = null;
		return list;
	}
	
	
	/**特殊商品价格
	 * @param goods
	 * @param key
	 * @return
	 */
	private static SearchGoods modefindPrice(SearchGoods goods, Boolean key){
		if(goods!=null){
			//扭扭车价格校正
			if(key||Pattern.compile("(electric.*scooter)|(scooter)").matcher(goods.getGoods_name().toLowerCase()).find()){
				String goods_price = goods.getGoods_price();
				if(goods_price!=null&&goods_price.indexOf("-")<0){
					goods_price = DownloadMain.getSpiderContext(goods_price, "\\d+\\.*\\d*");
					if(!goods_price.isEmpty()){
						Double price = Double.valueOf(goods_price);
						if(price>60&&price<170){
							goods.setGoods_price("170.00");
							goods.setGoods_url("&p=1"+goods.getGoods_url());
						}
					}
				}
			}
		}
		return goods;
	}
	
	/**产品名字
	 * @param name
	 * @return
	 */
	private static String modefindName(String name){
		if(name!=null){
			name = name.toLowerCase().replace("pandora", "");
		}
		return name;
	}
}
