package com.cbt.parse.servlet;

import com.cbt.dc.service.DcServerImpl;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.driver.Aliexpress240Driver;
import com.cbt.parse.driver.DriverInterface;
import com.cbt.parse.driver.SearchCacheDriver;
import com.cbt.parse.service.*;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.util.AppConfig;
import com.cbt.util.WebCookie;
import com.cbt.website.service.AliExpress240Sercive;
import com.cbt.website.service.IAliExpress240Sercive;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * sj
 */
public class GoodsTypeServerlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(GoodsTypeServerlet.class);
       
    public GoodsTypeServerlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long stt = new Date().getTime();
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		Boolean web = false;
		try {
/***************************参数设置 start**************************************/
			//关键词
			String keyword = req.getParameter("keyword");
			if(keyword==null||"undefined".equals(keyword)){
				keyword = "";
			}else{
				keyword = keyword.replaceAll("\\s+", " ").trim();
				keyword = keyword.length()>1&&keyword.substring(0,1).equals("<")?keyword.substring(1):keyword;
				keyword=URLEncoder.encode(keyword, "utf-8");/*链接关键字搜索需要对特殊字符：空格进行转码*/
			}
			LOG.warn("Search1: keyword="+keyword);
			//最小价格
			String price1 = req.getParameter("price1");
			price1=price1==null?"":DownloadMain.getSpiderContext(price1, "(\\d+\\.*\\d*)");
			
			//最大价格
			String price2 = req.getParameter("price2");
			price2=price2==null?"":DownloadMain.getSpiderContext(price2, "(\\d+\\.*\\d*)");
			//最小订量
			String minq = req.getParameter("minq");
			minq  = minq==null?"":minq.replaceAll("\\D+", "").trim();
			
			//最大大量
			String maxq = req.getParameter("maxq");
			maxq  = maxq==null?"":maxq.replaceAll("\\D+", "").trim();
			
			//特殊商品直接跳转至二级页面(扭扭车专题页面)20160217 delete
			/*if(maxq.isEmpty()&&minq.isEmpty()&&price1.isEmpty()&&price2.isEmpty()){
				String page_url = GetFilterUtils.pageFilter(keyword);
				if(page_url!=null&&!page_url.isEmpty()){
					resp.sendRedirect(AppConfig.ip+page_url);
					return ;
				}
			}*/
			
			//类别catid
			String catid = req.getParameter("catid");
			if(catid==null||"undefined".equals(catid)||catid.isEmpty()||!Pattern.compile("(\\d+)").matcher(catid).matches()){
				catid = "0";
			}
			//首页二级页
			String index = req.getParameter("ind");//首页二级页面静态生成页面标志
			index = index!=null&&!"1".equals(index)?null:index;
			
			//“类似商品”参数
			String similar = req.getParameter("simiar");//相似商品
			similar =similar==null||"undefined".equals(similar)? "":similar.trim();
			
			//类型pvid
			String pid = req.getParameter("pid");
			if(pid==null||"undefined".equals(pid)||pid.isEmpty()){
				pid = "0";
			}
			//页数
			String pages = req.getParameter("page");
			pages = pages==null?"1":DownloadMain.getSpiderContext(pages, "(\\d+)");
			pages = pages.isEmpty()?"1":pages;
			
			//搜索排序
			String sort = req.getParameter("srt");
			if(sort==null||"undefind".equals(sort)||sort.isEmpty()){
				sort = "order-desc";
			}
			//类别限定价格排序的最小价格---2016-1-14 add
			if("bbPrice-asc".equals(sort)||"bbPrice-desc".equals(sort)){
				price1 =price1.isEmpty()? GetFilterUtils.priceFilter(catid):price1;
				price1 = price1==null?"":price1;
				req.setAttribute("minprice", price1);
			}
			//是否为商店商品搜索
			String store = req.getParameter("store");
			if(store==null||"undefind".equals(store)||store.isEmpty()){
				store = "0";
			}
			//搜索链接
			String encodeUrl = null;
			String keyurl = req.getParameter("keyurl");//远程连接
			String k0 = req.getParameter("k0");//
			String k1 = req.getParameter("k1");//
			String k2 = req.getParameter("k2");//
			String k3 = req.getParameter("k3");//
			if(k0!=null&&k1!=null&&k2!=null&&k3!=null
			   &&!k0.isEmpty()&&!k1.isEmpty()&&!k2.isEmpty()&&!k3.isEmpty()
			   &&!"undefined".equals(k0)&&!"undefined".equals(k1)
			   &&!"undefined".equals(k2)&&!"undefined".equals(k3)){
					encodeUrl = "http://"+TypeUtils.decodeUrl(k2+k0+k3+k1);;
			 }
			if(keyurl==null||keyurl.isEmpty()||"undefined".equals(keyurl)){
				keyurl = encodeUrl;
			}
			if(keyword.isEmpty()&&(keyurl==null||keyurl.isEmpty())){//关键词为空 直接跳转到首页
				resp.sendRedirect("http://www.import-express.com/");
				return ;
			}
			//来源网站
			String website = req.getParameter("website");
			if(website==null||"undefined".equals(website)||website.isEmpty()){
				website = "a";
			}
			keyword = "a".equals(website)?SearchUtils.largeWord(keyword.replaceAll("(\\+)+", "+")):keyword;
			//商店id
			String sid = req.getParameter("sid");
			if(sid!=null&&("undefined".equals(sid)||sid.isEmpty())){
				sid = null;
			}
			//如果是商店的链接  跳转到store.jsp
			if("1".equals(store)||(keyurl!=null&&Pattern.compile("(aliexpress.*/store/)|(.*alibaba.*/productlist)").matcher(keyurl).find())){
				web = true;
				store(keyurl, sid,pages,website,req, resp);
			}
			
			//1688跳转到web获取搜索数据
			String more = req.getParameter("more");
			if(more!=null&&("undefined".equals(more)||more.isEmpty())){
				more = null;
			}
			//静态页面保存路径
			String statice_path;
			if(Pattern.compile("(import-express)").matcher(AppConfig.ips).find()){
				statice_path="/usr/local/apache2/htdocs/Cbt/";
			}else{
				statice_path = "E:/workdesk/soft/apache-tomcat-6.0.43/webapps/Cbt/";
			}
			//取得Application对象     过滤屏蔽词汇
			ServletContext application=this.getServletContext();
			Object attribute_words = application.getAttribute("words");
			String inwords = attribute_words!=null?attribute_words.toString():null;
			String intensave = SearchUtils.chenckKey(catid,keyword,inwords);
/**********************************参数设置 end*****************************************************************************/
			if(!web){
				if("0".equals(intensave)){
					/********************************local search***start*************************************************************************/
					ArrayList<SearchGoods> goods = null;
					if(more==null&&("o".equals(website)/*||GetFilterUtils.catidFilter(catid)*/)){
						//本地数据库读取1688
						web = false;
						DriverInterface driver = new DriverInterface();
						goods = driver.search(7, keyword, price1, price2, minq, maxq, sort, catid, pages, pid);
						LOG.warn("search product from sql");
						if(goods!=null&&!goods.isEmpty()){
							req.setAttribute("responsetext", JSONArray.fromObject(goods));
						}
						goods = null;
						req.setAttribute("site", "o");
					/********************************local search***end*************************************************************************/
					}else{
						web = false;
						Boolean page_flag = true;//为true则直接从网站抓取搜索数据
						String priceFilter = GetFilterUtils.priceFilter(catid);
						/********************************2016-2-17***add*****本地有货源数据aliexpress240topresult start******************/
						if((more==null||!"2".equals(more))&&"a".equals(website)&&price2.isEmpty()&&minq.isEmpty()&&maxq.isEmpty()
								&&(("order-desc".equals(sort)&&price1.isEmpty())||("bbPrice-asc".equals(sort)&&price1.equals(priceFilter)))
								&&(keyurl==null||keyurl.isEmpty())){
							//1.类别过滤，
							//(1).指定的类别走人工挑选的方式获取商品集合aliexpress240数据      2016-2-29
							if(GetFilterUtils.catidFilter(catid, 7)){
								//进入本地有货源数据aliexpress240topresult
								IAliExpress240Sercive ali_240_service = new AliExpress240Sercive();
								HashMap<String, String> map_count = ali_240_service.getSearch240_typeCount(keyword, catid);
								if(map_count!=null){
									int search_number = Integer.parseInt(map_count.get("search_number"));
									if(search_number!=0){
										int pagenum = Integer.parseInt(map_count.get("pagenum"));
										if(pagenum>6){//原网站数据页数小于7的话，就不显示view_more(view_more==true即转到aliexpress搜索)
											req.setAttribute("view_more", "f");
										}
										Aliexpress240Driver aliexpress240 = new Aliexpress240Driver();
										String aliexpress240_sort = "bbPrice-asc".equals(sort)?"0":"1";
										ArrayList<SearchGoods> searchGoods = aliexpress240.getSearchGoods(
												Integer.parseInt(map_count.get("results_typeid")),keyword, catid, aliexpress240_sort, pages);
										if(searchGoods!=null&&searchGoods.size()>0){
											req.setAttribute("responsetext", JSONArray.fromObject(searchGoods));
											page_flag = false;
										}
									}
								}
							}else /*if(GetFilterUtils.catidFilter(catid,8))*/{
								//搜索缓存数据-----------searchcache-----2016-2-29
								SearchCacheDriver  driver = new SearchCacheDriver();
								ArrayList<SearchGoods> list = driver.saveSearchCache(keyword, catid, sort, pages);
								System.out.println(list);
								page_flag = true;
								if(list!=null){
									page_flag = false;
									list=SearchUtils.class_discount(list, null, keyword, req, catid);
									req.setAttribute("responsetext", JSONArray.fromObject(list));
								}
							}
						}
						/********************************2016-2-17***add********本地有货源数据aliexpress240topresult end********************************/
						
						if(page_flag){//走web获取数据
							page_flag = more!=null&&"2".equals(more)?true:false;
							//搜索词远程获取数据
//							web = true;
							/****************************************返回客户端静态页面 start*******************************/
							SaveHtml savehtml = new SaveHtml();
							String flag = savehtml.searchHtml(catid,keyword, price1, price2, minq, maxq,website, sort, keyurl,statice_path,index);
							if(flag!=null&&!flag.isEmpty()){
								String[] split = flag.split("Cbt");
								if(split.length>1){
									flag = split[1].replaceAll("\\\\", "/");
									resp.sendRedirect(AppConfig.ips.replace("/cbtconsole", "/Cbt")+flag);
									LOG.warn("page redirect ..."+flag);
								}else{
									resp.sendRedirect("http://www.import-express.com/");
									LOG.warn("1.error:file is not exists");
								}
								split = null;
								return ;
							/**************************************返回客户端静态页面   end**********************************/
							}else{
								System.out.println("-----------------------44-------------------");
								//web获取数据
								Map<String, Object> map = webForLocal(catid,keyword,price1,price2,minq,maxq,website,sort,keyurl,req,page_flag);
								if(map == null){
									resp.sendRedirect("http://www.import-express.com/");
									return;
								}else{
									req.setAttribute("responsetext", map.get("responsetext") == null ? "[]" : JSONArray.fromObject(map.get("responsetext")));
								}
							}
						}
					}
				}else{
					web = false;
					req.setAttribute("check_result", intensave);
				}
			}
		} catch (Exception e1) {
			req.setAttribute("responsetext", JSONArray.fromObject("[]"));
			web = false;
		}finally{
			if(!web){
				req.setAttribute("goodsnum", "4");
				req.setAttribute("goodsnum", "4");
				RequestDispatcher homeDispatcher = req.getRequestDispatcher("cbt/Product.jsp");
				homeDispatcher.forward(req, resp);
			}
		}
		LOG.warn("GoodsTypeServerlet time "+(new Date().getTime()-stt));
	}
	
	
	/**商店商品搜索
	 * @throws Exception 
	 */
	private void store(String storeUrl, String sid, String page, String website, HttpServletRequest req, HttpServletResponse resp) throws Exception{
		try{
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/html");
			JSONArray jsonArray = JSONArray.fromObject("[]");
			ArrayList<SearchGoods> storeSearch = null;
			if(storeUrl!=null&&!storeUrl.isEmpty()&&GetFilterUtils.storeFilter(storeUrl)){
				storeSearch = ParseSearchUrl.parseSearch(storeUrl, 0,null,null,false);
			}else if(sid!=null&&!sid.isEmpty()&&website!=null&&!website.isEmpty()){
				DriverInterface driver = new DriverInterface();
				if("o".equals(website)){
					storeSearch =driver.store(sid,7,page);
				}
			}
			if(storeSearch!=null&&!storeSearch.isEmpty()){
				jsonArray = JSONArray.fromObject(storeSearch);
				storeSearch = null;
			}
			req.setAttribute("responsetext", jsonArray);
			javax.servlet.RequestDispatcher homeDispatcher = req.getRequestDispatcher("cbt/store.jsp");
			homeDispatcher.forward(req, resp);
			jsonArray = null;
		}catch(Exception e){
			throw e;
		}
	}	
		
	
	/**远程获取数据
	 * @para
	 * @throws IOException
	 * @throws Exception
	 */
	public Map<String, Object> web(String catid, String keyword, String price1, String price2, String minq, String maxq, String website, String sort,
                                   String keyurl, HttpServletRequest req, boolean page_flag) throws Exception{
		long st = new Date().getTime();
		try{		
			LOG.warn("search product from web");
			StringBuilder sbtem = new StringBuilder();
			String search = null;
			ArrayList<SearchGoods> goods = new ArrayList<SearchGoods>() ;
/**************不同网站来源 兼容排序参数  start***********************************************/
			if("a".equals(website)){
				if("bbPrice-asc".equals(sort)){
					sort = "price_asc";
				}else if("bbPrice-desc".equals(sort)){
					sort = "price_desc";
				}else if("order-desc".equals(sort)){
					sort = "total_tranpro_desc";
				}
			}else if("r".equals(website)){
				if("bbPrice-asc".equals(sort)){
					sort = "price_asc";
				}else if("bbPrice-desc".equals(sort)){
					sort = "price-desc";
				}else if("order-desc".equals(sort)){
					sort = "sale-desc";
				}
			}
/*****************不同网站来源 兼容排序参数  end***********************************************/
			sbtem = new StringBuilder();
			LOG.warn("price1:"+price1+"-price2:"+price2+"-minq:"+minq+"-maxq:"+maxq+"-website:"+website);
			boolean pflag = false;
			if(keyurl==null||keyurl.isEmpty()||"undefined".equals(keyurl)){
				if(keyword!=null&&!keyword.isEmpty()){
					/*链接关键字搜索需要对特殊字符：空格进行转码*/
					if("r".equals(website)){
						sbtem.delete(0, sbtem.length());
						search = sbtem.append("http://s.taobao.com/search?q=").append(keyword).append("&js=1&style=grid&stats_click=search_radio_all%253A1&tab=all&bcoffset=-1&s=0").toString();
					}
					else if("t".equals(website)){
						sbtem.delete(0, sbtem.length());
						search = sbtem.append("http://list.tmall.com/search_product.htm?q=").append(keyword).append("&type=p&vmarket=&spm=3.7396704.a2227oh.d100&from=mallfp..pc_1_searchbutton").toString();
					}
					else if("h".equals(website)){
						sbtem.delete(0, sbtem.length());
						sbtem.append("http://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=").append(keyword);
						search = maxq.isEmpty()?sbtem.toString():sbtem.append("&atm=&f0=y&moqf=MOQF&moqt=MOQT").append(maxq).toString();
					}
					else if("a".equals(website)){
						sbtem.delete(0, sbtem.length());
						search=sbtem.append("http://www.aliexpress.com/wholesale?CatId=").append(catid).append("&site=glo&groupsort=1&shipCountry=US&shipFromCountry=cn&g=y&SearchText=").append(keyword)
								.append("&minPrice=").append(price1).append("&maxPrice=").append(price2).append("&minQuantity=").append(minq).append("&maxQuantity=").append(maxq).append("&SortType=").append(sort).toString();	
					}
					else if("w".equals(website)){
						sbtem.delete(0, sbtem.length());
						sbtem.append("http://wholesaler.alibaba.com/wholesale/search/").append(keyword).append("/1.html");
						if(minq.isEmpty()&&maxq.isEmpty()&&price1.isEmpty()&&price2.isEmpty()){
							pflag = true;
						}else{
							sbtem.append("?sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq);
						}
						if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
							sbtem = pflag?sbtem.append("?"):sbtem;
							sbtem.append("&sort=").append(sort);
						}
						search = sbtem.toString();
					}
					/*--------keyurl is empty------end*/
				}else{
					LOG.warn("keyword is empty");
					return null;
				}
			}else{
				/*--------keyurl is not empty------start*/
				search = URLDecoder.decode(keyurl.replaceAll("%\\+", "+"), "utf-8").replaceAll("&amp;", "&").trim();
				pflag = true;
				if(website.equals("w")){
					/*--------website is w------start*/
					if(!(minq.isEmpty()&&maxq.isEmpty()&&price1.isEmpty()&&price2.isEmpty())){
						pflag = false;
						if(Pattern.compile("(SearchText)").matcher(search).find()){
							sbtem.delete(0, sbtem.length());
							search = sbtem.append(search).append("&sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq).toString();
						}else{
							String tem = search.indexOf("html")>0?search.substring(0, search.indexOf("html"))+"html":search;
							sbtem.delete(0, sbtem.length());
							search =sbtem.append(tem).append("?_csrf_token_=7tigt4thuu5y&sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq).toString();
							tem  =null;
						}
						if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
							sbtem.delete(0, sbtem.length());
							search =  sbtem.append(search).append("&sort=").append(sort).toString();
						}
					}else{
						search = search.indexOf("&sort")>0?search.substring(0, search.indexOf("&sort")):search;
						if(Pattern.compile("\\?").matcher(search).find()){
							pflag = false;
						}
						if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
							sbtem.delete(0, sbtem.length());
							sbtem.append(search);
							sbtem = pflag?sbtem.append("?"):sbtem;
							sbtem.append("&sort=").append(sort);
							search = sbtem.toString();
						}
					}
					/*--------website is w------end*/	
				}else if(website.equals("r")){
					if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
						search = new StringBuilder().append(search).append("&sort=").append(sort).toString();
					}
				}else if(website.equals("h")){
					/*--------website is h------start*/	
					String[] split = search.split("/");
					String end = split.length>0?split[split.length-1]:"";
					String moqt = "";
					if(Pattern.compile("(MOQT)").matcher(search).find()){
						moqt = search.split("MOQT")[1].replace("/", "").replace(end, "").trim();
					}
					if(!maxq.trim().equals(moqt)){
						String c_cid=DownloadMain.getSpiderContext(search, "(?:CID)(.*?)(?:\\D)");
						String conti;
						String[] splits = end.split("MOQT");
						if(splits.length>0){
							end = splits[0];
							conti =splits.length>1? splits[1]:"";
						}else{
							conti =end.length()>13?end.substring(end.length()-13):"";
						}
						String refine_attr_value = end.replace("CID"+c_cid, "").replace("--", "").replace(".html", "").trim();
						conti  = conti.replace("--", "").replace(".html", "").trim();
						conti = DownloadMain.getSpiderContext(conti, "(\\D+)");
						sbtem.delete(0, sbtem.length());
						search = sbtem.append("http://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=")
								.append(keyword).append("&c=CID").append(c_cid.trim()).append("&refine_attr_value=").append(refine_attr_value.trim())
								.append("&conti=").append(conti).append("&atm=&f0=y&moqf=MOQF&moqt=MOQT").append(maxq).toString();
						c_cid = null;
						conti = null;
						splits = null;
						refine_attr_value = null;
					}
					split = null;
					end = null;
					moqt = null;	
					/*--------website is h------end*/
				}else if(website.equals("a")){
					/*--------website is a------start*/
					String[] split = search.split("&");
					String sortType = "default";
					String maxQuantity = "";
					String minQuantity = "";
					String minPrice = "";
					String maxPrice = "";
					String page = "";
					//url如果以.html结尾的话，需要加?site=glo&shipCountry=us&g=y&tag=
					if(search.length()-5>0){
						if(Pattern.compile("(.html)").matcher(search.substring(search.length()-5)).find()){
							sbtem.delete(0, sbtem.length());
							search = sbtem.append(search).append("?site=glo&shipCountry=us&g=y&tag=").toString();
						}
						if(!Pattern.compile("(&g=)").matcher(search).find()){
							sbtem.delete(0, sbtem.length());
							search = sbtem.append(search).append("&g=y").toString();
						}else if(Pattern.compile("(&g=)").matcher(search).find()){
							search = search.replace("&g=n", "&g=y");
						}
					}
					
					for(int i=1;i<split.length;i++){
						if(Pattern.compile("(SortType=)").matcher(split[i]).find()){
							sortType = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(maxQuantity=)").matcher(split[i]).find()){
							maxQuantity = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(minQuantity=)").matcher(split[i]).find()){
							minQuantity = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(minPrice=)").matcher(split[i]).find()){
							minPrice = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(maxPrice=)").matcher(split[i]).find()){
							maxPrice = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(page=)").matcher(split[i]).find()){
							page = split[i].trim();
						}
					}
					sbtem.delete(0, sbtem.length());
					search = sbtem.append(search).append("&SortType=").append(sort).append("&minQuantity=").append(minq)
							.append("&maxQuantity=").append(maxq).append("&minPrice=").append(price1).append("&maxPrice=").append(price2).toString();
					if(!minq.isEmpty()&&(minQuantity==null||minQuantity.isEmpty())){
						minQuantity = "0";
					}
					if(!(sortType.equals(sort)&&minQuantity.equals(minq)&&maxQuantity.equals(maxq)&&minPrice.equals(price1)&&maxPrice.equals(price2))){
						search = page.isEmpty()?search:search.replace("&"+page, "");
					}
					
					if(!Pattern.compile("(&groupsort=1)").matcher(search).find()){
						search +="&groupsort=1";
					}
					search = search.replaceAll("shipCountry=[a-zA-Z][a-zA-Z]", "shipCountry=us").trim();
					split = null;
					sortType = null;
					maxQuantity = null;
					minQuantity = null;
					minPrice = null;
					maxPrice = null;
					page = null;
					/*--------website is a------end*/
				}
				/*--------keyurl is not empty------end*/
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if(search!=null){
				search=search.replaceAll("\\s+", "%20").replaceAll("(\\+)+", "%20");
				LOG.warn("search2: url="+search);
/********************aliexpressde 搜索类别id***start****用于类别混批折扣，类别id记录*便于wholesale（elly网数据）商品数据加载*******************/
				String cid = DownloadMain.getSpiderContext(search, "(CatId=\\d+)").replaceAll("CatId=", "").replaceAll("\\D+", "").trim();
				cid = cid.isEmpty()?DownloadMain.getSpiderContext(search, "(/category/\\d+/)").replaceAll("/category/", "").replaceAll("/", "").trim():cid;
				if("0".equals(cid.trim())||cid.isEmpty()){
					cid = catid;
				}
/*********************************aliexpressde 搜索类别id***end************************************************************/
				/*获取关键字搜索结果,得到商品列表*/
				goods = ParseSearchUrl.parseSearch(search,0,keyword,cid,page_flag);
				if(goods!=null&&!goods.isEmpty()){
					//折扣
					//goods = SearchUtils.discount(goods);
					goods = SearchUtils.class_discount(goods, null, keyword, req, cid);
					map.put("responsetext", goods);
				}
				goods = null;
			}
			//关闭义乌购商品---2016-1-27
			/*if(keyword!=null&&LocalSDriver.filterHotWord(keyword)){
				req.setAttribute("goodsnum", "3");
			}else{
			}*/
//			map.put("responsetext", jsonArray);
//			req.setAttribute("goodsnum", "4");
//			req.setAttribute("responsetext", jsonArray);
//			javax.servlet.RequestDispatcher homeDispatcher = req.getRequestDispatcher("cbt/Product.jsp");
			/*if(TypeUtils.checkAgent(req.getHeader( "USER-AGENT" ))){
				homeDispatcher = req.getRequestDispatcher("cbt/mproduct.jsp");
			}*/
//			homeDispatcher.forward(req, resp);
			search = null;
			LOG.warn("get datas from web: "+(new Date().getTime()-st));
			return map;
		}catch(Exception e){
			throw e;
		}
	}
	

	
	/**远程获取数据
	 * @para
	 * @throws IOException
	 * @throws Exception
	 */
	private Map<String, Object> webForLocal(String catid, String keyword, String price1, String price2, String minq, String maxq, String website, String sort,
                                            String keyurl, HttpServletRequest req, boolean page_flag) throws Exception{
		long st = new Date().getTime();
		try{
			if("a".equals(website)&&keyurl!=null&&!keyurl.isEmpty()){
				//缓存数据
				String pages =  DownloadMain.getSpiderContext(keyurl,"(&page=\\d+)").replace("&page=", "").trim();
				if(!pages.isEmpty()){
					System.out.println("pages:"+pages);
					SearchCacheDriver  driver = new SearchCacheDriver();
					ArrayList<SearchGoods> list = driver.saveSearchCache(keyword, catid, sort, pages);
					if(list!=null){
						Map<String, Object> map = new HashMap<String, Object>();
						String cid = DownloadMain.getSpiderContext(keyurl, "(CatId=\\d+)").replaceAll("CatId=", "").replaceAll("\\D+", "").trim();
						cid = cid.isEmpty()?DownloadMain.getSpiderContext(keyurl, "(/category/\\d+/)").replaceAll("/category/", "").replaceAll("/", "").trim():cid;
						cid = "0".equals(cid.trim())||cid.isEmpty()?catid:cid;
						list = SearchUtils.class_discount(list, null, keyword, req, cid);
						map.put("responsetext", list);
						return map;
					}
				}
			}
			LOG.warn("search product from web");
			StringBuilder sbtem = new StringBuilder();
			String search = null;
			ArrayList<SearchGoods> goods = new ArrayList<SearchGoods>() ;
/**************不同网站来源 兼容排序参数  start***********************************************/
			if("a".equals(website)||"r".equals(website)){
				if("bbPrice-asc".equals(sort)){
					sort = "price_asc";
				}else if("bbPrice-desc".equals(sort)){
					sort = "price_desc";
				}else if("order-desc".equals(sort)){
					sort = "a".equals(website)?"total_tranpro_desc":"sale-desc";
				}
			}
/*****************不同网站来源 兼容排序参数  end***********************************************/
			sbtem = new StringBuilder();
			LOG.warn("price1:"+price1+"-price2:"+price2+"-minq:"+minq+"-maxq:"+maxq+"-website:"+website);
			boolean pflag = false;
			if(keyurl==null||keyurl.isEmpty()||"undefined".equals(keyurl)){
				sbtem.delete(0, sbtem.length());
				if("r".equals(website)){
					sbtem.append("http://s.taobao.com/search?q=").append(keyword).append("&js=1&style=grid&stats_click=search_radio_all%253A1&tab=all&bcoffset=-1&s=0");
				}
				else if("t".equals(website)){
					sbtem.append("http://list.tmall.com/search_product.htm?q=").append(keyword).append("&type=p&vmarket=&spm=3.7396704.a2227oh.d100&from=mallfp..pc_1_searchbutton");
				}
				else if("h".equals(website)){
					sbtem.append("http://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=").append(keyword);
					sbtem = maxq.isEmpty()?sbtem:sbtem.append("&atm=&f0=y&moqf=MOQF&moqt=MOQT").append(maxq);
				}
				else if("a".equals(website)){
					sbtem.append("http://www.aliexpress.com/wholesale?CatId=").append(catid).append("&site=glo&groupsort=1&shipCountry=US&shipFromCountry=cn&g=y&SearchText=").append(keyword);
					if(page_flag){
						sbtem.append("&page=7");
					}
				    sbtem.append("&minPrice=").append(price1).append("&maxPrice=").append(price2).append("&minQuantity=").append(minq).append("&maxQuantity=").append(maxq).append("&SortType=").append(sort);	
				}
				else if("w".equals(website)){
					sbtem.append("http://wholesaler.alibaba.com/wholesale/search/").append(keyword).append("/1.html");
					if(minq.isEmpty()&&maxq.isEmpty()&&price1.isEmpty()&&price2.isEmpty()){
						pflag = true;
					}else{
						sbtem.append("?sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq);
					}
					if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
						sbtem = pflag?sbtem.append("?"):sbtem;
						sbtem.append("&sort=").append(sort);
					}
				}
				search = sbtem.toString();
				/*--------keyurl is empty------end*/
			}else{
				/*--------keyurl is not empty------start*/
				search = URLDecoder.decode(keyurl.replaceAll("%\\+", "+"), "utf-8").replaceAll("&amp;", "&").trim();
				pflag = true;
				if(website.equals("w")){
					/*--------website is w------start*/
					if(!(minq.isEmpty()&&maxq.isEmpty()&&price1.isEmpty()&&price2.isEmpty())){
						pflag = false;
						sbtem.delete(0, sbtem.length());
						if(Pattern.compile("(SearchText)").matcher(search).find()){
							sbtem.append(search).append("&sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq);
						}else{
							String tem = search.indexOf("html")>0?search.substring(0, search.indexOf("html"))+"html":search;
							sbtem.append(tem).append("?_csrf_token_=7tigt4thuu5y&sp=").append(price1).append("&ep=").append(price2).append("&sm=").append(minq).append("&em=").append(maxq);
							tem  =null;
						}
						
						if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
							sbtem.append("&sort=").append(sort);
						}
						search = sbtem.toString();
					}else{
						search = search.indexOf("&sort")>0?search.substring(0, search.indexOf("&sort")):search;
						if(Pattern.compile("\\?").matcher(search).find()){
							pflag = false;
						}
						if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
							sbtem.delete(0, sbtem.length());
							sbtem.append(search);
							sbtem = pflag?sbtem.append("?"):sbtem;
							sbtem.append("&sort=").append(sort);
							search = sbtem.toString();
						}
					}
					/*--------website is w------end*/	
				}else if(website.equals("r")){
					if(sort!=null&&!sort.isEmpty()&&!"undefined".equals(sort)&&!"default".equals(sort)){
						search += "&sort="+sort;
					}
				}else if(website.equals("h")){
					/*--------website is h------start*/	
					String[] split = search.split("/");
					String end = split.length>0?split[split.length-1]:"";
					String moqt = "";
					if(Pattern.compile("(MOQT)").matcher(search).find()){
						moqt = search.split("MOQT")[1].replace("/", "").replace(end, "").trim();
					}
					if(!maxq.trim().equals(moqt)){
						String c_cid=DownloadMain.getSpiderContext(search, "(?:CID)(.*?)(?:\\D)");
						String conti;
						String[] splits = end.split("MOQT");
						if(splits.length>0){
							end = splits[0];
							conti =splits.length>1? splits[1]:"";
						}else{
							conti =end.length()>13?end.substring(end.length()-13):"";
						}
						String refine_attr_value = end.replace("CID"+c_cid, "").replace("--", "").replace(".html", "").trim();
						conti  = conti.replace("--", "").replace(".html", "").trim();
						conti = DownloadMain.getSpiderContext(conti, "(\\D+)");
						sbtem.delete(0, sbtem.length());
						search = sbtem.append("http://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=")
								.append(keyword).append("&c=CID").append(c_cid.trim()).append("&refine_attr_value=").append(refine_attr_value.trim())
								.append("&conti=").append(conti).append("&atm=&f0=y&moqf=MOQF&moqt=MOQT").append(maxq).toString();
						c_cid = null;
						conti = null;
						splits = null;
						refine_attr_value = null;
					}
					split = null;
					end = null;
					moqt = null;	
					/*--------website is h------end*/
				}else if(website.equals("a")){
					/*--------website is a------start*/
					String[] split = search.split("&");
					String sortType = "default";
					String maxQuantity = "";
					String minQuantity = "";
					String minPrice = "";
					String maxPrice = "";
					String page = "";
					//url如果以.html结尾的话，需要加?site=glo&shipCountry=us&g=y&tag=
					if(search.length()-5>0){
						if(Pattern.compile("(.html)").matcher(search.substring(search.length()-5)).find()){
							search += "?site=glo&shipCountry=us&g=y&tag=";
						}else{
							if(!Pattern.compile("(&g=)").matcher(search).find()){
								search += "&g=y";
							}else if(Pattern.compile("(&g=)").matcher(search).find()){
								search = search.replace("&g=n", "&g=y");
							}
						}
					}
					
					for(int i=1;i<split.length;i++){
						if(Pattern.compile("(SortType=)").matcher(split[i]).find()){
							sortType = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(maxQuantity=)").matcher(split[i]).find()){
							maxQuantity = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(minQuantity=)").matcher(split[i]).find()){
							minQuantity = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(minPrice=)").matcher(split[i]).find()){
							minPrice = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(maxPrice=)").matcher(split[i]).find()){
							maxPrice = split[i].split("=")[1].trim();
							search = search.replace("&"+split[i], "");
						}else if(Pattern.compile("(page=)").matcher(split[i]).find()){
							page = split[i].trim();
						}
					}
					sbtem.delete(0, sbtem.length());
					search = sbtem.append(search).append("&SortType=").append(sort).append("&minQuantity=").append(minq)
							.append("&maxQuantity=").append(maxq).append("&minPrice=").append(price1).append("&maxPrice=").append(price2).toString();
					if(!minq.isEmpty()&&(minQuantity==null||minQuantity.isEmpty())){
						minQuantity = "0";
					}
					if(!(sortType.equals(sort)&&minQuantity.equals(minq)&&maxQuantity.equals(maxq)&&minPrice.equals(price1)&&maxPrice.equals(price2))){
						search = page.isEmpty()?search:search.replace("&"+page, "");
					}
					
					if(!Pattern.compile("(&groupsort=1)").matcher(search).find()){
						search +="&groupsort=1";
					}
					search = search.replaceAll("shipCountry=[a-zA-Z][a-zA-Z]", "shipCountry=us").trim();
					split = null;
					sortType = null;
					maxQuantity = null;
					minQuantity = null;
					minPrice = null;
					maxPrice = null;
					page = null;
					/*--------website is a------end*/
				}
				/*--------keyurl is not empty------end*/
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if(search!=null){
				search=search.replaceAll("\\s+", "%20").replaceAll("(\\+)+", "%20");
				LOG.warn("search2: url="+search);
				/********************aliexpressde 搜索类别id***start****用于类别混批折扣，类别id记录*便于wholesale（elly网数据）商品数据加载*******************/
				String cid = DownloadMain.getSpiderContext(search, "(CatId=\\d+)").replaceAll("CatId=", "").replaceAll("\\D+", "").trim();
				cid = cid.isEmpty()?DownloadMain.getSpiderContext(search, "(/category/\\d+/)").replaceAll("/category/", "").replaceAll("/", "").trim():cid;
				if("0".equals(cid.trim())||cid.isEmpty()){
					cid = catid;
				}
				/*********************************aliexpressde 搜索类别id***end************************************************************/
				/*获取关键字搜索结果,得到商品列表*/
				goods = ParseSearchUrl.parseSearch(search,0,keyword,cid,page_flag);
				if(goods!=null&&!goods.isEmpty()){
					//折扣
					//goods = SearchUtils.discount(goods);
					goods = SearchUtils.class_discount(goods, null, keyword, req, cid);
					map.put("responsetext", goods);
				}
				goods = null;
			}
			search = null;
			LOG.warn("get datas from web: "+(new Date().getTime()-st));
			return map;
		}catch(Exception e){
			LOG.warn(e);
			throw e;
		}
	}
	
	 
	/**wholesale products
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected  void getYiWu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/html");
			String keyword = req.getParameter("keyword");
			if(keyword==null||"undefind".equals(keyword)||keyword.isEmpty()){
				keyword = "";
			}
			/*String cid = req.getParameter("cat");
			String pid = null;
			if(cid!=null&&("undefind".equals(cid)||cid.isEmpty())){
				cid = null;
			}
			String catname = req.getParameter("catname");
			if(catname!=null&&("undefind".equals(catname)||catname.isEmpty())){
				catname = null;
			}*/
			keyword = keyword.replaceAll("\\s+", "+")
							 .replaceAll("%20", "+");
			JSONArray jsonArray = JSONArray.fromObject("[]");
			/*获取关键字搜索结果,得到商品列表*/
			/*if(cid!=null){
				cid = DownloadMain.getSpiderContext(cid, "(&k0.*&k1.*k2.*&k3.*)");
				if(!cid.isEmpty()){
					catname = DownloadMain.getSpiderContext(cid, "(&cat=.*)")
							.replaceAll("&cat=", "").replace("<", "")
							.replaceAll("%20", " ");
					cid = cid.replaceAll("&cat=.*", "").trim();
				}
				String href = TypeUtils.decodeSearch(cid);
				cid = DownloadMain.getSpiderContext(href, "(CatId=\\d+)").replaceAll("CatId=", "").replaceAll("\\D+", "").trim();
				pid = DownloadMain.getSpiderContext(href, "(pvId=\\d+-*\\d*-*\\d*)").replace("pvId=", "").trim();
				if(cid.isEmpty()){
					cid = DownloadMain.getSpiderContext(href, "(/category/\\d+/)").replaceAll("/category/", "").replaceAll("/", "").trim();
				}
				if("0".equals(cid.trim())||cid.isEmpty()){
					cid = null;
				}
			}*/
			DriverInterface driver = new DriverInterface();
			ArrayList<SearchGoods> goods = driver.search(5, keyword, null, null, null, null, null, null, null, null);
			if(goods!=null&&!goods.isEmpty()){
				//折扣
				//goods = SearchUtils.discount(goods);
				jsonArray = JSONArray.fromObject(goods);
			}
			goods = null;
			PrintWriter out = resp.getWriter();
			out.print(jsonArray);
			out.flush();
			out.close();
			jsonArray = null;
	}
	
	/**hot sale  product
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected  void getHotSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String keyword = req.getParameter("keyword");
		if(keyword==null||"undefind".equals(keyword)||keyword.isEmpty()){
			keyword = "";
		}
		String catname = req.getParameter("catname");
		if(catname!=null){
			if(catname.indexOf("^")>0){
				catname = catname.substring(catname.indexOf("^")+2);
			}else if(catname.indexOf("&cat=")>0){
				catname = DownloadMain.getSpiderContext(catname, "(&cat=.*)")
						.replaceAll("&cat=", "").replace("<", "")
						.replaceAll("%20", " ").replace("%27", "'");
			}else{
				catname = null;
			}
		}
		if(catname!=null&&("undefind".equals(catname)||catname.isEmpty())){
			catname = null;
		}
		keyword = keyword.replaceAll("\\s+", " ")
				.replaceAll("%20", " ");
		JSONArray jsonArray = JSONArray.fromObject("[]");
		
		LOG.warn("getHotSale:keyword="+keyword+"----catname="+catname);
		DcServerImpl driver = new DcServerImpl();
		ArrayList<SearchGoods> goods = driver.getPopProducts(keyword, catname, null);
		String goods_price = null;
		String prices = null;
		String punit = null;
		DecimalFormat format = new DecimalFormat("#0.00");
		ISpiderServer spider = new SpiderServer();
		Map<String, Double> maphl = spider.getExchangeRate();
		Double rmb = 1.0;
		for(int i=0;i<goods.size();i++){
			goods_price = goods.get(i).getGoods_price();
			if(goods_price!=null&&!goods_price.isEmpty()){
				prices = DownloadMain.getSpiderContext(goods_price, "(\\d+\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
				punit = goods_price.replace(prices, "");
				if("RMB".equals(punit)){
					rmb = maphl.get("RMB");
					if(prices!=null&&!prices.isEmpty()){
						int index = prices.indexOf("-");
						if(index>0){
							prices = format.format(Double.valueOf(prices.substring(0, index).trim())/rmb)+"-"
									+format.format(Double.valueOf(prices.substring(index+1).trim())/rmb);
						}else{
							prices = format.format(Double.valueOf(prices)/rmb);
						}
					}
				}
				goods.get(i).setGoods_price(prices);
			}
			prices = null;
			goods_price = null;
		}
		if(goods!=null&&!goods.isEmpty()){
			//折扣10%
			//goods = SearchUtils.discount(goods);
			jsonArray = JSONArray.fromObject(goods);
		}
		goods = null;
		PrintWriter out = resp.getWriter();
		out.print(jsonArray);
		out.flush();
		out.close();
		jsonArray = null;
	}
	
	/**商店商品搜索
	 * @throws IOException 
	 * @throws Exception 
	 */
	protected void getStoreSale(HttpServletRequest req, HttpServletResponse resp) throws IOException{
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/html");
			String storeUrl = req.getParameter("storeUrl");
			String sid = null;
			String website = null;
			JSONArray jsonArray = JSONArray.fromObject("[]");
			ArrayList<SearchGoods> storeSearch = null;
			if(storeUrl!=null&&!Pattern.compile("(k0.*k1.*k2.*k3.*)").matcher(storeUrl).find()){
				String[] split = storeUrl.split("&");
				for(int i=0;i<split.length;i++){
					if(Pattern.compile("(sid=)").matcher(split[i]).find()){
						sid = split[i].replace("sid=", "").trim();
					}else if(Pattern.compile("(website=)").matcher(split[i]).find()){
						website = split[i].replace("website=", "").trim();
					}
				}
				storeUrl =  null;
			}
			if(storeUrl!=null&&!storeUrl.isEmpty()){
				storeUrl = TypeUtils.decodeSearch(storeUrl);
				if(GetFilterUtils.storeFilter(storeUrl)){
					storeSearch = ParseSearchUrl.parseSearch(storeUrl, 0,null,null,false);
				}
			}else if(sid!=null&&!sid.isEmpty()&&website!=null&&!website.isEmpty()){
				DriverInterface driver = new DriverInterface();
				if("o".equals(website)){
					storeSearch =driver.store(sid,7,"1");
				}
			}
			if(storeSearch!=null&&!storeSearch.isEmpty()){
				jsonArray = JSONArray.fromObject(storeSearch);
				storeSearch = null;
			}
			PrintWriter out = resp.getWriter();
			out.print(jsonArray);
			out.flush();
			out.close();
			jsonArray = null;
	}
	
	/**收藏
	 * @param request
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected  void getCollected(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String purl = request.getParameter("url");
		ISpiderServer is = new SpiderServer();
		String[] user =  WebCookie.getUser(request);
		int userId = 0;
		String userName = "游客";
    	if(user != null){
    		userId = Integer.parseInt(user[0]);
        	userName = user[1];
        	request.setAttribute("username", userName);
        	if(user.length == 3){
        		request.setAttribute("email", user[2]);
        	}
    	}
	 
	 if(!Pattern.compile("(&u0=)").matcher(purl.substring(0, 5)).find()){
		 purl = TypeUtils.encodeGoods(purl);
	 }
	 int collectionFlag=is.getCollection(purl, userId);
	   PrintWriter out = resp.getWriter();
		out.print(collectionFlag);
		out.flush();
		out.close();
	}
	
	/**session
	 * @param request
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected  void setVal(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		String val = request.getParameter("val");
		if(val==null||val.isEmpty()||"undefind".equals(val)){
			val = "a";
		}
		request.getSession().setAttribute("val", val);
		PrintWriter out = resp.getWriter();
		out.print("1");
		out.flush();
		out.close();
	}
	
	
}

