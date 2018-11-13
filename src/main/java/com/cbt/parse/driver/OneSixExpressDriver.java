package com.cbt.parse.driver;

import com.cbt.parse.bean.*;
import com.cbt.parse.dao.*;
import com.cbt.parse.daoimp.*;
import com.cbt.parse.service.*;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.util.Redis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class OneSixExpressDriver {
	private final Log LOG = LogFactory.getLog(OneSixExpressDriver.class);
	int count = 0;
	IOneSixExpressDao adao_1688 = new OneSixExpressDao();
	ArrayList<OneSixExpressBean> bean_list = new ArrayList<OneSixExpressBean>();
	
	/**
	 * 商店商品抓取
	 */
	public void storeSave(String[] urls){
		String url = null;
		//商店链接集合
		String path1 = null;
		String path2 = null;
		int urls_num = urls.length;
		Map<String, String> cookie = null;
		try {
//		   cookie = DownloadMain.loginHtml("login.1688.com", 443, "https://login.1688.com/member/signin.htm", "3053568516@qq.com","3053568516lizhanjun");
//		   System.out.println("cookie:"+cookie.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int j=0;j<urls_num;j++){
			url = urls[j];
			//http://binlishijie.1688.com/?spm=0.0.0.0.YDDYdG
			//http://binlishijie.1688.com/page/offerlist.htm?spm=a2615.2177701.0.0.uIpUvg	
			if(!Pattern.compile("(/page/offerlist)").matcher(url).find()){
				url = url.substring(0,url.indexOf(".com/"))+".com/page/offerlist.htm";
			}
			System.out.println("store--"+url);
			//redis存值
			Redis.hset("dataload", "store_url", url);
			path1  = "j="+j;
//			Element body = DriverEngine.pageBody2(url, 0, 0,cookie);
			Element body = DriverEngine.pageBody(url, 0, 0);
			if(body!=null){
				//System.out.println(body.toString());
				String count = body.select("em[class=page-count]").text();
				int total = count.isEmpty()?0:Integer.valueOf(count);
				if(total==0){
					System.out.println("error------------");
					Redis.hset("dataload", "store_url", url+"--fail");
				}
				String page_url = url;
				int begin = 1;
				//分页解析商品集合
				for(int i=begin;i<total+1;i++){
					if(i!=1){
						page_url = url+"?&pageNum="+i;
					}
					path2  = path1+"--i="+i;
					pageStore(page_url,path2);
					page_url = null;
					path2 = null;
				}
			}else {
				System.out.println("body:"+body);
			}
			path1 = null;
		}
	}
	
	/**商店分页商品
	 * @param url
	 * @param path
	 */
	private void pageStore(String url,String path){
		Element body = DriverEngine.pageBody(url, 0, 0);
		if(body!=null){
			Elements count = body.select("ul[class=offer-list-row]").select("li");
			int total = count.isEmpty()?0:count.size();
			String img = null;
			String name = null;
			String price = null;
			String purl = null;
			OneSixExpressBean bean = null;
			int  query = 0;
			for(int i=0;i<total;i++){
				purl = count.get(i).select("div[class=image]").select("a").attr("href");
				System.out.println("purl:"+purl);
				//链接？之后的信息去除
				if(purl!=null&&purl.indexOf("?")>0){
					purl = purl.substring(0,purl.indexOf("?"));
				}
				//查询当前商品链接是否已经在库
				query = adao_1688.queryExsis(purl);
				if(query==0){
					//搜索页面显示的图片
					img = count.get(i).select("div[class=image]").select("img").attr("data-lazy-load-src");
					if(img.isEmpty()){
						img = count.get(i).select("div[class=image]").select("img").attr("src");
					}
					if(img!=null&&!img.isEmpty()&&!"http:".equals(img.subSequence(0, 5))){
						img = "http:"+img;
					}
					//搜索页面显示的商品名称（中文）
					name = count.get(i).select("div[class=title]").text();
					//搜索页面显示的商品价格
					price = count.get(i).select("div[class=price]").select("em").text().replaceAll("\\s+", "").trim();
					price = DownloadMain.getSpiderContext(price, "\\d+\\.*\\d*");
					bean = new OneSixExpressBean();
					price = price.isEmpty()?"0":price;
					System.out.println("price:"+price);
					bean.setName(name);
					bean.setPrice(price);
					bean.setImg(img);
					bean.setUrl(purl);
					//爬取1688商品单页其他数据信息
					bean = getGoods(bean, purl);
					System.out.println("img:"+img);
					if(bean!=null){
						int add = adao_1688.add(bean);
						Redis.hset("dataload", "product_count_url_flag",purl+"-count-" +String.valueOf(i+1)+"-flag-"+add);
					}else{
						Redis.hset("dataload", "product_count_url_flag",purl+"-count-" +String.valueOf(i+1)+"-flag-"+4);
					}
				}else{
					//redis存值
					Redis.hset("dataload", "product_count_url_flag", purl+"-count-" +String.valueOf(i+1)+"-flag-"+3);
				}
				System.out.println(path+"--goods="+i);
				img = null;
				name = null;
				price = null;
				purl = null;
			}
		}
	}
	
	
/**爬取1688商品单页其他数据信息
 * @param bean
 * @param url
 * @return
 */
private OneSixExpressBean getGoods(OneSixExpressBean bean, String url){
	if(bean!=null&&url!=null&&!url.isEmpty()){
		//抓取1688商品单页信息
		GoodsBean goods = ParseGoodsUrl.parseGoodsw(url, 0);
		if(goods!=null&&!goods.isEmpty()){
			//批发价格
			ArrayList<String> wprice = goods.getpWprice();
			if(wprice!=null){
				bean.setWprice(wprice.toString());
			}
			//图片集合
			ArrayList<String> imgs = goods.getpImage();
			if(imgs!=null){
				bean.setImgs(imgs.toString());
			}
			//商品明细
			HashMap<String, String> detail = goods.getpInfo();
			if(detail!=null){
				bean.setDetail(detail.toString());
			}
			//商品详情
			bean.setInfo(goods.getInfo_ori());
			//商品规格
			ArrayList<TypeBean> type = goods.getType();
			if(type!=null){
				bean.setType(type.toString());
			}
			//货币单位
			bean.setPunit(goods.getpPriceUnit());
			//商品售卖单位
			bean.setGunit(goods.getpGoodsUnit());
			//商品重量
			bean.setWeight(goods.getWeight());
			//商品id
			bean.setPid(goods.getpID());
			//商店id
			bean.setSid(goods.getsID());
			bean.setCatid1(goods.getCatid1());
			bean.setCatid2(goods.getCatid2());
			String[] imgSize = goods.getImgSize();
			if(imgSize!=null){
				bean.setImgsize(imgSize[0]+"+"+imgSize[1]);
			}
			//最小订量
			String morder = goods.getMinOrder();
			morder = morder!=null?morder.replaceAll("\\D+", "").trim():"1";
			System.out.println(morder);
			bean.setMorder(morder);
			//已经售出数量
			String sold = goods.getSell();
			if(!sold.isEmpty()){
				sold = sold.replaceAll("\\D+", "");
			}else{
				sold = "0";
			}
			bean.setSold(sold);
		}else{
			bean = null;
		}
	}
	
	return bean;
	
}


/**搜索商品集合
 * @param keyword
 * @param srt
 * @param minprice
 * @param maxprice
 * @param minq
 * @param maxq
 * @param catid
 * @param pvid
 * @param page
 * @return
 */
public ArrayList<SearchGoods>  searchDriver(String keyword,String srt,String minprice,String maxprice,String minq,String maxq,String catid,String pvid,String page){
	ArrayList<SearchGoods> list_search = new ArrayList<SearchGoods>();
	if(keyword!=null&&!keyword.isEmpty()){
		IOneSixExpressDao dao = new OneSixExpressDao();
		page = page==null?"1":page;
		page = page.replaceAll("\\D+", "");
		page = page.isEmpty()?"1":page;
		//搜索参数设置
		ParamBean param = new ParamBean();
		param.setCurrent(Integer.valueOf(page));
		param.setKeyword(keyword);
		param.setMinq(minq);//最小订量区间最小值
		param.setMaxq(maxq);//最小订量区间最大值
		param.setMinprice(minprice);//价格区间最低价
		param.setMaxprice(maxprice);//价格区间最高价
		param.setSort(srt);//排序
		param.setCatid(catid);//类别
		param.setPvid(pvid);//类型pvid
		param.setValid("0");//valid
		//搜索满足条件的商品集合
		ArrayList<SqlBean> sql = SearchEngine.pate(param, null);
//		Long st= new Date().getTime();
		ArrayList<OneSixExpressBean> list = dao.querySearch(sql);
//		LOG.warn("search sql:"+(new Date().getTime()-st));
		int list_num = list.size();
		SearchGoods sg = null;
		OneSixExpressBean gb = null;
		String price = null;
		int total = 1;
		//设置返回页数
		if(list!=null&&!list.isEmpty()){
			total = list.get(0).getTotal();
			total = total%40==0?total/40:total/40+1;
			total = total>263?263:total;
		}
		String entype = null;
		int color_num = 0;
		for(int i=0;i<list_num;i++){
			gb = list.get(i);
			sg = new SearchGoods();
			sg.setGoods_name(SearchUtils.nameVert(gb.getEnname(), 26));
			sg.setGoods_url(TypeUtils.encodeGoods(gb.getUrl().replace(".htm", "/local1688.htm")));
			price =gb.getPrice();
			if(price!=null&&!price.isEmpty()&&!"0.00".equals(price)&&!"0.0".equals(price)&&!"0".equals(price)){
				sg.setGoods_price(price);
			}
			sg.setGoods_image(/*gb.getImg()*/setPath(gb.getImg()));
			sg.setGoods_minOrder(gb.getMorder());
			sg.setGoods_solder(gb.getSold());
			sg.setKey_type("goods");
			sg.setGoods_free("Free Shipping");
			//规格参数
			entype = gb.getEntype();
			if(entype!=null&&!entype.isEmpty()){
				entype = entype.replace("[", "").replace("]", "").trim();
				String[] types_s = entype.split(",\\s+");
				for(int j=0;j<types_s.length;j++){
					if(Pattern.compile("([cC]olor)").matcher(types_s[j]).find()){
						color_num++;
					}
				}
			}
			sg.setGoods_colors(color_num+"");
			list_search.add(sg);
			gb = null;
			sg = null;
			price = null;
			color_num = 0;
		}
		
		if(list_search!=null&&!list_search.isEmpty()){
			//分页信息
			param.setAmount(total);
			param.setWebsite("o");
			param.setCom("goodsTypeServerlet");
			ArrayList<SearchGoods> page_list = SearchEngine.page(param);
			list_search.addAll(page_list);
		}
		Long t= new Date().getTime();
		//类别信息
		ArrayList<SearchGoods> cat_list = category(sql,dao,catid);
		list_search.addAll(cat_list);
		//规格信息
		ArrayList<SearchGoods> type_list = ptype(catid,pvid,keyword);
		list_search.addAll(type_list);
		LOG.warn("search sql category and type:"+(new Date().getTime()-t));
	}
	return list_search;
}

/**图片链接替换为ftp图片
 * @param imgurl
 * @return
 */
private  String setPath(String imgurl){
	String path = null;
	if(imgurl!=null&&!imgurl.isEmpty()){
		String tem_path = DownloadMain.getSpiderContext(imgurl, "https*://.*com/");
		String replace = tem_path.replace("http://", "").replace(".com", "")
				         .replace("https://", "").replace(".", "/");
		path = "http://www.china-clothing-wholesale.com/img/importexpress/"+imgurl.replace(tem_path, replace).replace("/", "_");
//		path = "http://198.38.82.146/img/importexpress/"+imgurl.replace(tem_path, replace).replace("/", "_");
	}
	return path;
}


/**搜索商品集合
 * @param keyword
 * @param srt
 * @param minprice
 * @param maxprice
 * @param minq
 * @param maxq
 * @param catid
 * @param pvid
 * @param page
 * @return
 */
public ArrayList<SearchGoods>  searchWebsite(String keyword,String srt,String minprice,String maxprice,String minq,String maxq,String catid,String pvid,String page){
	ArrayList<SearchGoods> list_search = new ArrayList<SearchGoods>();
	if(keyword!=null&&!keyword.isEmpty()){
		IOneSixExpressDao dao = new OneSixExpressDao();
		DecimalFormat format = new DecimalFormat("#0.00");
		ISpiderServer spider = new SpiderServer();
		Map<String, Double> maphl = spider.getExchangeRate();
		Double rmb = maphl.get("RMB");
		String minp = minprice.isEmpty()?minprice:format.format(Double.valueOf(minprice)*rmb);
		String maxp = maxprice.isEmpty()?maxprice:format.format(Double.valueOf(maxprice)*rmb);
		page = page==null?"1":page;
		page = page.replaceAll("\\D+", "");
		page = page.isEmpty()?"1":page;
		ParamBean param = new ParamBean();
		param.setCurrent(Integer.valueOf(page));
		param.setKeyword(keyword);
		param.setMinq(minq);
		param.setMaxq(maxq);
		param.setMinprice(minp);
		param.setMaxprice(maxp);
		param.setSort(srt);
		param.setCatid(catid);
		param.setPvid(pvid);
		ArrayList<SqlBean> sql = SearchEngine.pate(param, null);
		ArrayList<OneSixExpressBean> list = dao.querySearchWbsite(sql);
		int list_num = list.size();
		SearchGoods sg = null;
		OneSixExpressBean gb = null;
		double price = 0.0;
		int total = 1;
		if(list!=null&&!list.isEmpty()){
			total = list.get(0).getTotal();
			total = total%40==0?total/40:total/40+1;
		}
		String morder = null;
		for(int i=0;i<list_num;i++){
			gb = list.get(i);
			sg = new SearchGoods();
			sg.setGoods_name(gb.getName());
			sg.setGoods_url(gb.getUrl()+"&site=o");
			price = Double.valueOf(gb.getPrice());
			price = "RMB".equals(gb.getPunit())?price/rmb:price;
			if(price!=0){
				sg.setGoods_price(format.format(price));
			}
			sg.setGoods_image(gb.getImg());
			int morders = Integer.valueOf(gb.getMorder());
			morder = morders+" "+gb.getGunit();
			morder = morders>1? morder+"s":morder;
			sg.setGoods_minOrder(morder);
			sg.setGoods_solder(gb.getSold());
			sg.setKey_type("goods");
			list_search.add(sg);
			gb = null;
			sg = null;
			price = 0.0;
		}
		if(list_search!=null&&!list_search.isEmpty()){
			//分页信息
			param.setAmount(total);
			param.setWebsite("o");
			param.setMinprice(minprice);
			param.setMaxprice(maxprice);
			param.setCom("goodswebsite");
			ArrayList<SearchGoods> page_list = SearchEngine.page(param);
			list_search.addAll(page_list);
		}
	}
	return list_search;
}

/**category
 * @param sql
 * @param dao
 * @param catid
 * @return
 */
private ArrayList<SearchGoods> category(ArrayList<SqlBean> sql, IOneSixExpressDao dao, String catid){
	ArrayList<SearchGoods> cat_list = new ArrayList<SearchGoods>();
	ArrayList<String> list = new ArrayList<String>();
	IAliCategoryDao idao = new AliCategoryDao();
	OneSixExpressBean bean = null;
	String catid6 = null;
	String catid5 = null;
	String catid4 = null;
	String catid3 = null;
	SearchGoods sg = null;
	
	String catid2_p = null;
	String catid3_p = null;
	String catid4_p = null;
	String catid5_p = null;
	String catid6_p = null;
	int index = 0;
	ArrayList<OneSixExpressBean> cate = new ArrayList<OneSixExpressBean>();
	if(catid!=null&&!catid.isEmpty()&&!"0".equals(catid)){
		OneSixExpressBean parent = dao.queryCateParent(catid);
		if(parent!=null){
			catid2_p = parent.getCatid2();
			catid3_p = parent.getCatid3();
			catid4_p = parent.getCatid4();
			catid5_p = parent.getCatid5();
			catid6_p = parent.getCatid6();
			index = catid2_p!=null&&catid.equals(catid2_p)?1:index;
			index = catid3_p!=null&&catid.equals(catid3_p)?2:index;
			index = catid4_p!=null&&catid.equals(catid4_p)?3:index;
			index = catid5_p!=null&&catid.equals(catid5_p)?4:index;
			index = catid6_p!=null&&catid.equals(catid6_p)?5:index;
			cate = dao.queryCate(sql,0);
		}else{
			cate = dao.queryCate(sql,6);
		}
	}else{
		cate = dao.queryCate(sql,6);
	}
	//类别归纳
	int cate_size = cate.size();
	for(int i=0;i<cate_size;i++){
		bean = cate.get(i);
		catid6 = bean.getCatid6();
		if(catid6!=null){
			catid5 = bean.getCatid5();
			catid4 = bean.getCatid4();
			catid3 = bean.getCatid3();
			if(i==0&&index!=0){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("All category");
				sg.setKey_url("&catid=0");
				cat_list.add(sg);
			}
			//默认进入未选择任何类别
			if((index==0||!catid6.equals(catid6_p))&&!list.contains(catid6)){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name(idao.getCname(catid6));
				sg.setKey_url("&catid="+catid6);
				cat_list.add(sg);
				list.add(catid6);
			}
			
			if(index==5&&catid6.equals(catid)){
				//选择的catid是最高父类catid6
				if(!list.contains(catid6)){
					sg = new SearchGoods();
					sg.setKey_type("Related Categories");
					sg.setKey_name("&lt;"+idao.getCname(catid));
					sg.setKey_url("&k0=id&k1=ov&k2=vo&k3=di");
					cat_list.add(sg);
					list.add(catid);
				}
				if(catid5!=null&&!list.contains(catid5)){
					sg = new SearchGoods();
					sg.setKey_type("Related Categories");
					sg.setKey_name("&nbsp;&nbsp;"+idao.getCname(catid5));
					sg.setKey_url("&catid="+catid5);
					cat_list.add(sg);
					list.add(catid5);
				}
		  }else if(index==4&&catid6.equals(catid6_p)){
			//选择的catid是父类catid5
			 if(!list.contains(catid)){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&lt;"+idao.getCname(catid6));
				sg.setKey_url("&catid="+catid6);
				cat_list.add(sg);
				list.add(catid6);
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&nbsp;&lt;"+idao.getCname(catid));
				sg.setKey_url("&k0=id&k1=ov&k2=vo&k3=di");
				cat_list.add(sg);
				list.add(catid);
			}
			if(catid.equals(catid5)&&catid4!=null&&!list.contains(catid4)){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&nbsp;&nbsp;"+idao.getCname(catid4));
				sg.setKey_url("&catid="+catid4);
				cat_list.add(sg);
				list.add(catid4);
			}
		}else if(index==3&&catid6.equals(catid6_p)){
			//选择的catid是父类catid4
			if(catid4!=null&&catid4.equals(catid)){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&lt;"+idao.getCname(catid6));
				sg.setKey_url("&catid="+catid6);
				cat_list.add(sg);
				list.add(catid6);
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&nbsp;&lt;"+idao.getCname(catid5));
				sg.setKey_url("&catid="+catid5);
				cat_list.add(sg);
				list.add(catid5);
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&nbsp;&nbsp;&lt;"+idao.getCname(catid));
				sg.setKey_url("&k0=id&k1=ov&k2=vo&k3=di");
				cat_list.add(sg);
				list.add(catid);
			}
			if(catid4!=null&&catid.equals(catid4)&&catid3!=null&&!list.contains(catid3)){
				sg = new SearchGoods();
				sg.setKey_type("Related Categories");
				sg.setKey_name("&nbsp;&nbsp;&nbsp;"+idao.getCname(catid3));
				sg.setKey_url("&catid="+catid3);
				cat_list.add(sg);
				list.add(catid3);
			}
		}
		}
	}
	return cat_list;
}


/**pvid
 * @param catid
 * @param pvid
 * @param keyword
 * @return
 */
private ArrayList<SearchGoods> ptype(String catid,String pvid,String keyword){
	ArrayList<SearchGoods> cat_list = new ArrayList<SearchGoods>();
	IOnePvidDao dao = new OnePvidDao();
	SearchGoods sg = null;
	String type = null;
	ArrayList<String> type_list = new ArrayList<String>();
	ArrayList<String> clear_list = new ArrayList<String>();
	IPvidDao  idao = new PvidDao();
	if(pvid!=null&&!pvid.isEmpty()&&!"0".equals(pvid)){
		String[] pvids = pvid.split(",");
		ArrayList<PvidBean> pvids_list = null;
		String clear_pvid = null;
		int pvids_length = pvids.length;
		for(int i=0;i<pvids_length;i++){
			if(i==0){
				sg = new SearchGoods();
				sg.setKey_type("Active Filters");
				sg.setKey_name("Clear All");
				sg.setKey_url("&catid="+catid);
				cat_list.add(sg);
				sg= null;
			}
			pvids_list = idao.query(pvids[i]);
			if(pvids_list.size()>0){
				sg = new SearchGoods();
				sg.setKey_name(pvids_list.get(0).getValue().replace("X", "x")+"&nbsp;&nbsp;X");
				type_list.add(pvids_list.get(0).getName());
				clear_pvid = pvid.replace(pvids[i], "");
				if(clear_pvid!=null){
					int clear_pvid_length  = clear_pvid.length();
					if(clear_pvid_length>0&&clear_pvid.substring(0, 1).equals(",")){
						clear_pvid = clear_pvid.substring(1);
					}
					if(clear_pvid!=null&&clear_pvid_length>1&&clear_pvid.substring(clear_pvid_length-1).equals(",")){
						clear_pvid = clear_pvid.substring(0,clear_pvid_length-1);
					}
					if(clear_pvid.isEmpty()){
						sg.setKey_url("&catid="+catid);
					}else{
						sg.setKey_url("&catid="+catid+"&pid="+clear_pvid);
					}
					sg.setKey_type(pvids_list.get(0).getName());
					if(!clear_list.contains(pvids_list.get(0).getName())){
						clear_list.add(pvids_list.get(0).getName());
					}
					cat_list.add(sg);
				}
				clear_pvid = null;
			}
		}
	}
	
	ArrayList<PvidBean> list = dao.query(catid, keyword);
	int list_num = list.size();
	for(int i=0;i<list_num;i++){
		type = list.get(i).getName();
//		System.out.println("type:"+type);
		if(!type_list.contains(type)){
			type_list.add(type);
		}
		if(type_list.size()>=10){
			break;
		}
		if(!clear_list.contains(type)){
			sg = new SearchGoods();
			sg.setKey_name(list.get(i).getValue());
			if(pvid!=null&&!pvid.isEmpty()&&!"0".equals(pvid)){
				sg.setKey_url("&catid="+catid+"&pid="+pvid+","+list.get(i).getPvid());
			}else{
				sg.setKey_url("&catid="+catid+"&pid="+list.get(i).getPvid());
			}
			sg.setKey_img(list.get(i).getImg());
			sg.setKey_type(type);
			cat_list.add(sg);
			sg = null;
		}
	}
	return cat_list;
}

/**商店商品集合
 * @param sid
 * @param page
 * @return
 */
public ArrayList<SearchGoods>  storeDriver(String sid,String page){
	ArrayList<SearchGoods> list  =new ArrayList<SearchGoods>();
	if(sid!=null&&!sid.isEmpty()){
		IOneSixExpressDao dao = new OneSixExpressDao();
		DecimalFormat format = new DecimalFormat("#0.00");
		ISpiderServer spider = new SpiderServer();
		Map<String, Double> maphl = spider.getExchangeRate();
		Double rmb = maphl.get("RMB");
		page = page==null?"1":page;
		page = DownloadMain.getSpiderContext(page, "(\\d+)");
		page = page.isEmpty()?"1":page;
		ArrayList<OneSixExpressBean> store_list = dao.queryStore(sid,Integer.valueOf(page));
		int list_num = store_list.size();
		SearchGoods sg = null;
		OneSixExpressBean gb = null;
		double price = 0.0;
		String morder = null;
		int total = list_num>0?store_list.get(0).getTotal():1;
		total = total%40==0?total/40:(total/40)+1;
		for(int i=0;i<list_num;i++){
			gb = store_list.get(i);
			sg = new SearchGoods();
			sg.setGoods_name(SearchUtils.nameVert(gb.getEnname(), 26));
			sg.setGoods_url("&site=o"+TypeUtils.encodeGoods(gb.getUrl()));
			price = Double.valueOf(gb.getPrice());
			if("RMB".equals(gb.getPunit())){
			    price = price/rmb;
			}
			if(price!=0){
				price = price*1.4;
				sg.setGoods_price(format.format(price));
			}
			sg.setGoods_image(setPath(gb.getImg()));
			int morders = Integer.valueOf(gb.getMorder());
			morder = morders+" "+gb.getGunit();
			morder = morders>1? morder+"s":morder;
			sg.setGoods_minOrder(morder);
			sg.setGoods_solder(gb.getSold());
			sg.setKey_type("goods");
			list.add(sg);
			gb = null;
			sg = null;
			price = 0.0;
		}
		if(list!=null&&!list.isEmpty()){
			ParamBean page_bean = new ParamBean();
			page_bean.setAmount(total);
			page_bean.setCurrent(Integer.valueOf(page));
			page_bean.setWebsite("o");
			page_bean.setSid(sid);
			page_bean.setCom("goodsTypeServerlet");
			ArrayList<SearchGoods> page_list = SearchEngine.page(page_bean);
			list.addAll(page_list);
		}
	}
	return list;
}

/**商品单页数据
 * @param url
 * @param pid
 * @return
 */
public GoodsBean goodsDriver(String url, String pid){
	GoodsBean goods = new GoodsBean();
	goods.setValid(0);
	IOneSixExpressDao dao = new OneSixExpressDao();
	OneSixExpressBean oneSixBean = dao.queryGoods(url, pid);
	if(oneSixBean!=null){
		goods.setValid(oneSixBean.getValid());
		if(oneSixBean.getValid()!=0){
			if(!TypeUtils.isNew(oneSixBean.getCreatetime(), 4.0)){
				//dao.updateValid(url, 5);
			}
			goods.setpName(oneSixBean.getEnname());//商品名称（英文）
			goods.setCid(1688);//设置商品来源1688数据
			goods.setpID(oneSixBean.getPid());//商品id
			goods.setpUrl(oneSixBean.getUrl().replace(".htm", "/local1688.htm"));//商品链接（结尾处加local1688便于商品单页解析）
			goods.setsID(oneSixBean.getSid());//
			goods.setSupplierUrl("&website=o&sid="+oneSixBean.getSid());//商店链接
			goods.setpPriceUnit("$");//货币单位
			goods.setpGoodsUnit(oneSixBean.getGunit());//商品售卖单位
			String info_ori = oneSixBean.getInfo();//商品详情图片
			if(info_ori!=null){
				String ori_info = "";
				Elements infos = Jsoup.parse(info_ori).body().select("img");
				String attr = null;
				for(int i=0;i<infos.size();i++){
					attr = infos.get(i).attr("src");
					//去除gif以及babidou的图片
					if(attr!=null&&attr.length()>3&&!"gif".equals(attr.substring(attr.length()-3))
							&&!Pattern.compile("(\\.babidou.com)").matcher(attr).find()){
						attr = setPath(attr);
						ori_info +="<img src=\""+attr+"\">";
					}
					attr = null;
				}
				goods.setInfo_ori(ori_info);
			}
			
			goods.setMinOrder(oneSixBean.getMorder());//商品最小订量
			
			String weight = oneSixBean.getWeight();
			if(weight==null||weight.isEmpty()){
				//类别对应默认重量
				String catid = oneSixBean.getCatid1();
				if(catid!=null&&!catid.isEmpty()){
					ICatidFilterDao idao = new CatidFilterDao();
					String list = idao.queryWeightFilter(catid);
					weight = list!=null?list+"kg":"0.268kg";
				}else{
					weight = "0.268kg";
				}
			}
			goods.setWeight(weight);//商品重量
			goods.setPerWeight(weight);//均重
			goods.setSellUnits(oneSixBean.getGunit());//售卖单位
			goods.setCom("sql");
			//规格参数
			String types = oneSixBean.getEntype();
			if(types!=null&&!types.isEmpty()){
				types = types.replace("[", "").replace("]", "").trim();
				ArrayList<TypeBean> typeList = new ArrayList<TypeBean>();
				String[] types_s = types.split(",\\s+");
				TypeBean tybean = null;
				String[] names = null;
				String typename;
				for(int i=0;i<types_s.length;i++){
					if(!types_s[i].isEmpty()){
						tybean = new TypeBean();
						String[] type = types_s[i].split("\\+#\\s+");
						for(int j=0;j<type.length;j++){
							if(Pattern.compile("(id=)").matcher(type[j]).find()){
								names = type[j].split("id=");
								typename = names.length>1?names[1]:"";
								tybean.setId(typename.trim());
								typename = null;
								names = null;
							}else if(Pattern.compile("(type=)").matcher(type[j]).find()){
								names = type[j].split("type=");
								typename = names.length>1?names[1]:"";
								typename = "适well-fittinghigh".equals(typename)?"Fit height":typename;
								tybean.setType(typename);
								typename = null;
								names = null;
							}else if(Pattern.compile("(value=)").matcher(type[j]).find()){
								names = type[j].split("value=");
								typename = names.length>1?names[1]:"";
								tybean.setValue(typename.trim());
								typename = null;
								names = null;
							}else if(Pattern.compile("(img=)").matcher(type[j]).find()){
								names = type[j].split("img=");
								typename = names.length>1?names[1]:"";
								tybean.setImg(setPath(typename));
								typename = null;
								names = null;
							}
						}
						typeList.add(tybean);
						tybean = null;
					}
				}
				goods.setType(typeList);
			}
			//批发价
			String wprice = oneSixBean.getWprice();
			if(wprice!=null&&!wprice.isEmpty()&&!"[]".equals(wprice)){
				wprice = wprice.replace("[", "").replace("]", "").trim();
				String[] prices = wprice.split(",\\s+");
				ArrayList<String> wList = new ArrayList<String>();
				for(int i=0;i<prices.length;i++){
					if(!prices[i].isEmpty()){
						wList.add(prices[i]);
					}
				}
				goods.setpWprice(wList);
				wList = null;
				goods.setFeeprice(oneSixBean.getFeeprice());//运费
				goods.setFprice(oneSixBean.getFprice());//非免邮价格
				if(oneSixBean.getFeeprice()!=null){
					goods.setFree("1");//免邮标志
					goods.setMethod(oneSixBean.getMethod());//免邮运输方式
					goods.setTime(oneSixBean.getPosttime());//免邮快递时间
				}
			}
			wprice = null;
			//图片
			String img = oneSixBean.getImgs();
			if(img!=null&&!img.isEmpty()){
				img = img.replace("[", "").replace("]", "").trim();
				String[] imgs = img.split(",\\s+");
				ArrayList<String> imgList = new ArrayList<String>();
				for(int i=0;i<imgs.length;i++){
					if(!imgs[i].isEmpty()){
						if(imgs[i].substring(imgs[i].length()-1).equals(".")){
							imgList.add(setPath(imgs[i])+"310x310.jpg");
						}else{
							imgList.add(setPath(imgs[i]));
						}
					}
				}
				goods.setpImage(imgList);
				imgList = null;
			}
			img = null;
			//detail
			String detail = oneSixBean.getEndetail();
			if(detail!=null&&!detail.isEmpty()){
				detail = detail.replace("{", "").replace("}", "").trim();
				HashMap<String, String> detailList = new HashMap<String, String>();
				String[] details = detail.split(",*\\s*\\d+=");
				String detail_text = null;
				for(int i=0;i<details.length;i++){
					detail_text = details[i].trim();
					if(!detail_text.isEmpty()&&!Pattern.compile("(suggesting\\s*price)").matcher(detail_text.toLowerCase()).find()){
						detailList.put(i+"",detail_text);
					}
				}
				goods.setpInfo(detailList);
				detailList=  null;
				details = null;
			}
			detail = null;
			////类别gps
			getCateGps(goods, oneSixBean);
		}
	}
	return goods;
}

/**商品各级类别导航html
 * @param goods
 * @param oneSixBean
 * @return
 */
private GoodsBean getCateGps(GoodsBean goods, OneSixExpressBean oneSixBean){
	if(goods!=null&&oneSixBean!=null){
		//类别gps
		StringBuilder sb = new StringBuilder();
		sb.append("<span><a href=\"http://www.import-express.com\"><img src=\"/cbtconsole/img/shopcar/newicon/iconfont-home.png\"></a></span>");
		String catid = null;
		String catid_n = null;
		String catid_name = null;
		IAliCategoryDao catDao = new AliCategoryDao();
		String catid_parent = "0";
		catid = oneSixBean.getCatid6();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			goods.setCatid6(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s1\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		catid = oneSixBean.getCatid5();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid5(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s2\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		catid = oneSixBean.getCatid4();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid4(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s3\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		catid = oneSixBean.getCatid3();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid3(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s4\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		catid = oneSixBean.getCatid2();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid2(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s5\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		catid = oneSixBean.getCatid1();
		if(catid!=null&&!catid.isEmpty()){
			catid_n = catDao.getCname(catid);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid1(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\"/cbtconsole/goodsTypeServerlet?keyword=")
				.append(catid_name)
				.append("&website=o&srt=default&catid=").append(catid).append("\">").append("<span id=\"catid_s6\" data_id=\"").append(catid).append("\">")
				.append(catid_n).append("</span></a></span>");
				count++;
			}
			catid = null;
			catid_n = null;
			catid_name = null;
		}
		if(count>0){
			goods.setCategps(sb.toString());
		}
	}
	return goods;
}

}
