package com.cbt.parse.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.ShippingBean;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.parse.bean.GoodsDaoBean;
import com.cbt.parse.bean.Set;
import com.cbt.parse.bean.SupplierGoods;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.dao.AliCategoryDao;
import com.cbt.parse.dao.GoodsDao;
import com.cbt.parse.daoimp.IAliCategoryDao;
import com.cbt.parse.daoimp.IGoodsDao;
import com.cbt.parse.driver.DriverInterface;
import com.cbt.parse.thread.GoodsThread;
import com.cbt.util.AppConfig;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**商品信息解析模块
 * @author abc
 *
 */
public class ParseGoodsUrl {
	private final static String PATE = "([hH]ot\\s*[sS]ale,*!*\\:*)|([fF][rR][eE]*\\s*[sS][hH][iI][pP]+[iI][nN][gG],*!*\\:*)|(\\d*%*\\s*[bB]rand\\s*[nN]ew,*!*\\:*)"
							+ "|([tT]op\\s*[rR]ated,*!*\\:*)|([lL]owest\\s*[pP]rice,*!*\\:*)|(\\(*[sS]hip\\s*[fF]rom\\s*[uU][sS]\\)*,*!*\\:*)"
							+ "|([hH]igh\\s*[qQ]uality,*!*\\:*)|(2015,*!*\\:*)|([pP][aA][nN][dD][oO][rR][aA])";
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ParseGoodsUrl.class);
	private static IGoodsDao gd = new GoodsDao();
	private static String t_url = "http://116.228.150.218:8083/translation/gettrans?catid=0&cntext=";
	
	

	
	
	
	/**商品数据 信息
	 * @param urls
	 * @param username
	 * @param cycle
	 * @return
	 */
	public static GoodsBean parseGoods2(String urls, String com_ip, String imgurl){
		GoodsBean goods = null;
		DriverInterface driver = new DriverInterface();
		if(urls == null || urls.isEmpty() || !StrUtils.isFind(urls, "(http.*com)")){
			goods = new GoodsBean();
			goods.setValid(1);
			return goods;
		}
		if(urls.indexOf("/cido") > -1){
			String pid = urls.substring(urls.indexOf("/pid=")+5);
			goods = driver.goods(null, pid, 8);
			goods = modefiGoodsData(goods);
		}else{
			goods = parseGoods(urls, 0,com_ip);
//			goods = parseGoodsw(urls, 0);
			
//			goods = GetFilterUtils.goodsFilter(goods);
		}
		return goods;
	}
	
	/**中文商品翻译
	 * @date 2016年6月17日
	 * @author abc
	 * @param goods
	 * @return  
	 */
	public static GoodsBean modefideTrans(GoodsBean goods){
		if(goods==null){
			return goods;
		}
		//商品名称翻译
		String name = tranlationText(goods.getpName());
		name = name.length()>1?name.substring(0, 1).toUpperCase()+name.substring(1):name;
		goods.setpName(name);
		//明细翻译
		HashMap<String,String> getpInfo = goods.getpInfo();
		if(getpInfo!=null){
			HashMap<String, String> detailList = new HashMap<String, String>();
			int getpInfo_length = getpInfo.size();
			for(int i=0;i<getpInfo_length;i++){
				String cntext = getpInfo.get(i+"");
				if(StrUtils.isFind(cntext, "品牌")){
					continue;
				}
				cntext = tranlationText(cntext);
				cntext = cntext.replaceAll("\\s+\\:\\s+", ":");
				if(cntext.length()>0){
					int index = cntext.indexOf(":");
					cntext = cntext.substring(0, 1).toUpperCase()+
							cntext.substring(1, index+1)+
							cntext.substring(index+1, index+2).toUpperCase()+
							cntext.substring(index+2);
					detailList.put(i+"", cntext);
				}
			}
			goods.setpInfo(detailList);
			detailList=  null;
		}
		//颜色、尺寸规格翻译
		ArrayList<TypeBean> types = goods.getType();
		if(types!=null){
			ArrayList<TypeBean> typesList =  new ArrayList<TypeBean>();
			//获取尺寸信息
			for(TypeBean type:types){
				type.setType(tranlationText(type.getType()));
				type.setValue(tranlationText(type.getValue()));
				typesList.add(type);
			}
			goods.setType(typesList);
		}
		goods.setpGoodsUnit("piece");
	return goods;
}
	
	/**调用远程翻译接口
	 * @date 2016年6月17日
	 * @author abc
	 * @param text
	 * @return  
	 */
	public static String tranlationText(String text){
		if(text==null){
			return "";
		}
		text = text.replace("：", ":");
		text = StrUtils.urlEncoder(text);
		text = DownloadMain.getContentClient(t_url+text,null);
		text = StrUtils.urlDecoder(text);
		return text;
	}
	/**调用远程翻译接口
	 * @date 2016年6月17日
	 * @author abc
	 * @param text
	 * @return  
	 */
	public static String tranlationText(String catid,String text){
		if(text==null){
			return "";
		}
		if(catid==null||catid.isEmpty()){
			catid = "0";
		}
		String t_url = "http://116.228.150.218:8083/translation/gettrans?catid="+catid+"&cntext=";
		text = text.replace("：", ":");
		text = StrUtils.urlEncoder(text);
		text = DownloadMain.getContentClient(t_url+text,null);
		text = StrUtils.urlDecoder(text);
		return text;
	}
	
	/**通过pid  cid 查询商品数据(goodsdata表)
	 * @param pid
	 * @param cid
	 * @return
	 */
	public static GoodsBean goodsDriver(String pid, String cid){
		IGoodsDao gd = new GoodsDao();
	   return dao2Bean(gd.queryPid(pid, cid));
	}
	
	
	/**没有代理服务器  获取url商品数据
	 * @param url
	 * @param userName
	 * @return
	 * @throws Throwable
	 */
	public static GoodsBean parseGoodsw(String urls, int cycle){
//		long s = new Date().getTime();
		String url = TypeUtils.modefindUrl(urls,1);
		GoodsBean goods = new GoodsBean();
		goods.setValid(1);
		goods.setpUrl(url);
		//The url is unvalid
		if(url==null||!Pattern.compile("(https*://.+)").matcher(url).matches()){
			return goods;
		}
		
		int tem = cycle;
		Set set = new Set();
//		String page = DownloadMain.getJsoup("https://login.1688.com/member/signin.htm",1,set);
		String page = DownloadMain.getJsoup(url,1,set);
		//通过连接抓取图片
		
		//System.err.println("page:"+page);
		//DownloadMain.write2file(page, "C:/Users/user5/Desktop/aligoods.html");
		//System.out.println(page);
		if(page!=null && StrUtils.isFind(page, "(Join free now)")){
			page = null;
		}
		//获取失败重试  最多3次
		while((page==null||page.isEmpty())&&tem<3){
			tem++;
			page = DownloadMain.getJsoup(url,1,set);
		}
		//获取页面失败
		if(page==null||page.isEmpty()||StrUtils.isFind(page, "(Join free now)")){
//			LOG.warn("page--------isEmpty---");
			return goods;
		}
		//请求出现错误
		if("httperror".equals(page)){
			goods.setValid(0);
			return goods;
		}
		//JSoup解析HTML生成document
		Document doc = Jsoup.parse(page);
		page = null;
		//获取element
		Element body = doc.body();
		String url_title = doc.title();
		//商品下架
		String text = body.select("div[class=shadow-text]").select("h1").text();
		Elements available = body.select("div[id=no-longer-available]");
		String detail_404 = body.select("div[class=m-detail-404]").text();
		String no_found = "Page Not Found - Aliexpress.com";
		if("Oops!".equals(text)||no_found.equals(url_title)||available.size()>0||!detail_404.isEmpty()){
			goods.setValid(0);
			doc = null;
			body = null;
			return goods;
		}
		//根据url 来获取数据类型
		int type = TypeUtils.getType(url);
		//解析商品链接数据，获取商品信息
		goods = spiderFromDoc(goods,type, body,doc.head());
		goods.setTitle(url_title);
		goods.setIp(set.getIp());
		goods.setValid(1);
		goods.setCid(type);
		//数据校正（价格中存在特殊字符、重量、体积数据、默认交期情况、商品名称）
		goods = modefiGoodsData(goods);
		//taobao 、1688  商品 翻译
		if(goods.getCid() == 18 || goods.getCid() == 19 || goods.getCid() == 5){
			goods.setpWprice(null);
//			goods = modefideTrans(goods);
			String weight = goods.getWeight();
			if(weight==null||weight.isEmpty()){
				goods.setWeight("1kg");
				goods.setWeight("1");
			}
		}
		
		body = null;
		doc = null;
		url_title = null;
		set = null;
		return goods;
	}

	
	
	/**获取此商品供应商提供的其他商品列表（alibaba商品）
	 * @param url
	 * @return
	 */
	public static List<SupplierGoods> parseSupUrl(Element body){
        Elements products = body.select("div[class=more_from_supplier]").select("div[class=item-wrap]");
        if(products==null||products.isEmpty()){
        	products = body.select("div[class=more_from_supplier]").select("div[class=item-wrap j-item]");
        }
        if(products==null||products.isEmpty()){
        	return null;
        }
        List<SupplierGoods> list = new ArrayList<SupplierGoods>();
        SupplierGoods sg = null;
        String sg_name = null;
        String sg_img = null;
        String sg_url = null;
        String sg_price = null;
        String sg_min = null;
        String spurl = null;
        for(Element p:products){
        	sg = new SupplierGoods();
        	sg_name = p.select("div[class=title]").text();
        	sg_price = p.select("div[class=price]").text();
        	sg_min = p.select("div[class=minorder]").text();
        	spurl = p.select("div[class=iwrap]").select("a").attr("href");
        	sg_img = p.select("img").attr("image-src");
        	sg_url = TypeUtils.encodeGoods(spurl);
        	if(sg_img.isEmpty()||sg_name.isEmpty()||sg_url.isEmpty()){
        		continue;
        	}
        	sg_name = sg_name.replaceAll("/", "/ ").replaceAll("-", "- ").replaceAll(",\\s*", ",").trim();
        	sg.setG_img(sg_img);
        	sg.setG_min(sg_min);
        	sg.setG_name(sg_name);
        	sg.setG_price(sg_price);
        	sg.setG_url(sg_url);
        	list.add(sg);
        	sg = null;
        }
		return list;
		
	}
	
	/**根据网址分类解析网页数据
	 * @param url
	 * @param element
	 * @throws Throwable 
	 */
	public static GoodsBean spiderFromDoc(GoodsBean p, int type, Element  element, Element  head){
		GoodsBean temp = p;
		OtherParse otherParse = new OtherParse();
		AliParse aliParse = new AliParse();
		p = null;
		switch (type) {
        case 0:
        	LOG.warn("....search no goods....");
        	break;
		case 1:
			String title = aliParse.productCategory(element);
			String regex = "(Wholesale).*";
			String regex2 = "(Home).*";
			if((temp.getpUrl()!=null && temp.getpUrl().indexOf("Wholesaler") > -1)||
					aliParse.getKey(element)||title.matches(regex)){
				//Wholesale Product 产品
				LOG.warn("...insert type getType_wh...");
				temp = aliParse.getTypeWh(temp,element);
				//temp.setSupplier(parseSupUrl(element));	
			}else if(title.matches(regex2)){
				//一般产品
				LOG.warn("...insert type getType_default...");
				temp = aliParse.getTypeDefault(temp,element);
			}else if(element.select("div[class=deal-wrapper]")!=null){
				temp = aliParse.getTypeAny(temp,element);
			}else{
				LOG.warn("...insert alibaba error...");
			}
			//temp.setRelate(RelateSearch.searchRelate(element));
			title = null;
			regex = null;
			regex2 = null;
			break;
		case 2://xinxiuli
			LOG.warn("...get en.alibaba data...");
			temp = aliParse.getTypeEn(temp,element);
//			temp.setRelate(RelateSearch.searchRelate(element));
			break;
		case 3://bestway020.en
			LOG.warn("...get bestway020.alibaba data...");
			temp = aliParse.getTypeBest(temp,element);
			break;
		case 4://www.dhgate.com
			LOG.warn("...get dhgate data...");
			temp = otherParse.getFromDhgate(temp,element);
			break;
		case 5://item.taobao.com
			LOG.warn("...gat taobao data...");
			temp = otherParse.getFromTaobao(temp,element,head);
			break;
		case 6://ny.yiwubuy.com
			LOG.warn("...get yiwubuy data...");
			temp = otherParse.getFromYiwubuy(temp,element);
			break;
		case 7://www.aliexpress.com
			temp = otherParse.getFromAliexpress(temp,element);
			break;
		case 8://www.globalsources.com
			LOG.warn("...get globalsources data...");
			temp = otherParse.getFromGlobal(temp, element);
			break;
		case 9://en.jd.com
			LOG.warn("...get jd data...");
			temp = otherParse.getFromJD(temp,element);
			break;
		case 10://hero-tech.en.made-in-china.com
			LOG.warn("...get hero-tech data...");
			temp = otherParse.getFromHero(temp,element);
			break;
		case 11://www.made-in-china.com
			LOG.warn("...get made-in-china data...");
			temp = otherParse.getFromMade(temp,element);
			break;
		case 12://www.lightinthebox.com
			LOG.warn("...get lightinthebox data...");
			temp = otherParse.getFromLight(temp,element);
			break;
		case 13://www.ebay.com
			LOG.warn("...get ebay data...");
			temp = otherParse.getFromEbay(temp,element);
			break;
		case 14://www.tiny.com
			LOG.warn("...get ecvv data...");
			temp = otherParse.getFromEcvv(temp,element);
			break;
		case 15://www.dx.com
			LOG.warn("...get dx data...");
			temp = otherParse.getFromDx(temp,element);
			break;
		case 16://www.tiny.com
			LOG.warn("...get tinydeal data...");
			temp = otherParse.getFromTiny(temp,element);
			break;
		case 17://www.amazon.com
			LOG.warn("...get amazon data...");
			temp = otherParse.getFromAmazon(temp,element);
			break;
		case 18://detail.tmall.com
			LOG.warn("...get tmall data...");
			temp = otherParse.getFromTmall(temp,element);
			break;
		case 19://detail.1688.com
			LOG.warn("...get 1688 data...");
			temp = aliParse.getType16(temp,element);
			break;
		case 20://www.hktdc.com
			LOG.warn("...get hktdc data...");
			temp = otherParse.getFromhktdc(temp,element);
			break;
		case 21://www.eelly.com
			LOG.warn("...get eelly data...");
			temp = otherParse.getYiLian(temp, element);
			break;
		}
		return temp;
	}
	
	/**根据实际情况修改数据
	 * @param goods
	 * @return
	 */
	public static GoodsBean modefiGoodsData(GoodsBean goods){
		if(goods==null||goods.isEmpty()){
			return goods;
		}
		//名称
		String g_name = goods.getpName();
		if(g_name!=null&&Pattern.compile(PATE).matcher(g_name).find()){
			g_name = g_name.replaceAll(PATE, "").replaceAll("\\s+", " ").trim();
		}
		goods.setpName(g_name);
		//运费 详情请求
		String org = goods.getInfo_ori();
		if(org==null||org.isEmpty()){
			goods.setFlag("0");
		}
		String free = goods.getFree();
		String method = goods.getMethod();
		if(free==null||method==null||method.isEmpty()){
			if(goods.getpUrl()!=null&&Pattern.compile("(aliexpress)").matcher(goods.getpUrl()).find()){
				goods.setFree("2");
			}
		}
		//统一商品单位形式
		String qUnit = goods.getpGoodsUnit();
		qUnit = qUnit!=null?qUnit.replaceAll("(\\d+)", "").trim():qUnit;
		String minOrder = goods.getMinOrder();
		if(qUnit!=null&&!qUnit.isEmpty()){
			qUnit = qUnit.replaceAll("\\s*\\((s)*(es)*\\)*", "").trim();
			if(!Pattern.compile("^[\u4E00-\u9FFF]+$").matcher(qUnit).matches()){
				if(Pattern.compile("([bB]ox)").matcher(qUnit).find()){
					qUnit = "box";
				}else{
					int index = qUnit.indexOf("/");
					qUnit = index>0?qUnit.substring(0, index):qUnit;
					String sub = qUnit.length()>0?qUnit.substring(qUnit.length()-1):"";
					qUnit = "s".equals(sub)?qUnit.substring(0,qUnit.length()-1):qUnit;
				}
			}
			minOrder = StrUtils.matchStr(minOrder, "(\\d+)");
		}else{
			qUnit = "piece";
			minOrder = minOrder!=null?StrUtils.matchStr(minOrder, "(\\d+)"):"1";
			minOrder = minOrder.isEmpty()?"1":minOrder;
		}
		minOrder = minOrder==null?"1":minOrder.replaceAll("(\\D+)", "");
		minOrder = minOrder.isEmpty()?"1":minOrder;
		goods.setpGoodsUnit(qUnit);
		goods.setMinOrder(minOrder);
		qUnit = null;
		minOrder = null;
		//重量  大小
		String weight = goods.getWeight();
		String t_weight = null;
		if(weight!=null&&!weight.isEmpty()){
			weight = weight.trim();
			String w_pat = "(\\d+\\.*\\d+\\s*(([kK][gG])|([gG])))";
			weight = StrUtils.matchStr(weight, w_pat);
			
			String ceil = StrUtils.matchStr(weight, "(\\d+\\.*\\d*)").replaceAll("\\.+", ".").trim();
			double d_weight = ceil.isEmpty()?0:Double.valueOf(ceil.trim()) ;
			if(Pattern.compile("(g)|(G)").matcher(weight.replace(ceil, "").trim()).matches()){
				d_weight = d_weight/1000.0;
			}
			t_weight =d_weight==0? "":d_weight+"kg";
			goods.setWeight(t_weight);
		}
		//体积大小
		String width = goods.getWidth();
		String t_width = null;
		if(width!=null&&!width.isEmpty()){
			String width_pat = "((\\d+\\.*\\d*\\s*([cC][mM])*\\s*(x|X|\\*)\\s*)*"
					+ "(\\d*\\.*\\d*\\s*([cC][mM])*))";
			width = StrUtils.matchStr(width, width_pat);
			t_width = width;
			goods.setWidth(width);
			goods.setExtra_freight(getExtraFreight(width));
		}
		if(t_weight!=null&&t_width!=null){
			if(Pattern.compile("(((1|0\\.1|0\\.01)\\s*([cC][mM])*([xX\\*]))*((1|0\\.1|0\\.01)\\s*))").matcher(t_width).matches()){
				if(Pattern.compile("((1|0\\.1|0\\.01)(([kK][gG])|([gG])))").matcher(t_weight).matches()){
					goods.setWeight("");
					goods.setWidth("");
				}
			}
		}
		goods.setPerWeight(getPerWeight(weight, goods.getSellUnits(), goods.getpGoodsUnit()));
		weight = null;
		width = null;
		
		
		//商品的pid
		String pid = goods.getpID();
		pid = SpiderParse.getPidByUrl(goods.getpUrl(), goods.getCid(),pid);
		goods.setpID(pid);
		//交期
		String pTime = goods.getpTime();
		pTime = pTime==null?"7-12":pTime;
		pTime = StrUtils.matchStr(pTime, "(\\d+-*\\d*)");
		pTime = pTime.isEmpty()?"7-12":pTime;
		//交期统一加上3天  最低3天
		String[] pTime_split = pTime.split("-");
		if(Integer.parseInt(pTime_split[0])<3){
			pTime = "3";
			pTime = pTime_split.length>1?pTime+"-"+ pTime_split[1]:pTime;
		}
		
		/*pTime = String.valueOf(Integer.parseInt(pTime_split[0])+3);
		if(pTime_split.length>1){
			pTime +="-"+ String.valueOf(Integer.parseInt(pTime_split[1])+3);
		}*/
		goods.setpTime(pTime);
		
		//价格
		String sprice = goods.getpSprice();
		if(sprice!=null){
			sprice = sprice.replaceAll(",", "");
			sprice = StrUtils.matchStr(sprice, "(\\d+\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
			goods.setpSprice(sprice);
		}
		String oprice = goods.getpOprice();
		if(oprice!=null){
			oprice = oprice.replaceAll(",", "");
			oprice = StrUtils.matchStr(oprice, "(\\d+\\.*\\d*\\s*-*\\s*\\d*\\.*\\d*)");
			goods.setpOprice(oprice);
		}
		//天猫  淘宝价格
		if((goods.getCid()==5||goods.getCid()==18)&&pid!=null){
			String price_url = "https://show.re.taobao.com/feature.htm?cb=jQuery&auction_ids="+pid+
					"&feature_names=feedbackCount%2CpromoPrice&from=taobao_search";
			String client = DownloadMain.getContentClient(price_url, null);
			String price = StrUtils.matchStr(client, "(promoPrice\"\\:\"\\d+\\.*\\d*)");
			price = StrUtils.matchStr(price, "(\\d+\\.*\\d*)");
			if(!price.isEmpty()){
				goods.setpOprice(goods.getpSprice());
				goods.setpSprice(price);
			}
		}
		//type排序
		/*ArrayList<TypeBean> type = goods.getType();
		if(type!= null&&!type.isEmpty()){
			HashSet<TypeBean> h  =   new  HashSet<TypeBean>(type);  
			if(h!=null){
				type.clear();     
				type.addAll(h); 
				if(type!=null&&!type.isEmpty()){
					Collections.sort(type, new SortByTypeMin());
				}
				goods.setType(type);
			}
		}*/
		
		//扩展数据
		goods = modefiExprand(goods);
		//重量设置默认值----2016-4-7
//		String goods_weight = goods.getWeight();
		/*if(goods_weight==null||goods_weight.isEmpty()){
			String catid = goods.getCatid1();
			catid = catid==null||catid.isEmpty()?goods.getCatid2():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid3():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid4():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid5():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid6():catid;
			goods_weight = GetFilterUtils.weightFilter(catid);
			goods_weight = goods_weight==null?"0":goods_weight;
			goods.setWeight(goods_weight);
			goods.setPerWeight(goods_weight);
			goods.setSellUnits(goods.getpGoodsUnit());
			goods.setWidth("");
		}*/
		return goods;
	}
	
	/**goodsdata 扩展数据修正
	 * @param goods
	 * @return
	 */
	private static GoodsBean modefiExprand(GoodsBean goods){
		if(goods==null||goods.isEmpty()||goods.getCid()!=7){
			return goods;
		}
		String cat = goods.getCategory();
		if(cat==null||cat.isEmpty()){
			return goods;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<span><a href=\"http://www.import-express.com\"><img src=\""+
				AppConfig.path+"/img/shopcar/newicon/iconfont-home.png\"></a></span>");
		IAliCategoryDao catDao = new AliCategoryDao();
		String catid = null;
		String catid_n = null;
		String catid_name = null;
		String[] cates = cat.split("\\^")[0].split(">");
		int cates_num = cates.length;
		int count = 0 ;
		String catid_parent = "0";
		if(cates_num>4){
			catid_n = cates[cates_num-5];
			catid = catDao.getCid(catid_n,catid_parent);
			
			goods.setCatid6(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s1\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
		}
		if(cates_num>3){
			catid_n = cates[cates_num-4];
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid5(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s2\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
		}
		if(cates_num>2){
			catid_n = cates[cates_num-3];
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid4(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s3\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
		}
		
		if(cates_num>1){
			catid_n = cates[cates_num-2];
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid3(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s4\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
		}
		if(cates_num>0){
			catid_n = cates[cates_num-1];
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid2(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s5\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
				catid_parent = "0".equals(catid_parent)?catid:catid_parent;
			}
			catid = null;
			catid_n = null;
		}
		if(cat.indexOf("^")!=-1){
			catid_n = cat.substring(cat.indexOf("^")+2);
			catid = catDao.getCid(catid_n,catid_parent);
			goods.setCatid1(catid);
			if(catid_n!=null&&!catid_n.isEmpty()&&catid!=null&&!catid.isEmpty()&&!"0".equals(catid.trim())){
				catid_name = catid_n.replaceAll("&(amp;)*", "%26").replaceAll("\\s+", "%20").replaceAll("'", "%27");
				catid_name = catid_name.split("%20")[0].split("%27")[0];
				sb.append("&gt;").append("<span><a href=\""+AppConfig.path+"/goodslist?keyword=")
				.append(catid_name).append("&website=a&srt=order-desc&catid=")
				.append(catid).append("\">").append("<span id=\"catid_s6\" data_id=\"")
				.append(catid).append("\">").append(catid_n).append("</span></a></span>");
				count++;
			}
			catid = null;
			catid_n = null;
		}
		if(count>0){
			goods.setCategps(sb.toString());
		}
		return goods;
	}
	
	/**获取商品数据(sql & web)
	 * @param url
	 * @return
	 */
	public static GoodsBean parseGoods(String urls, int cycle, String com_ip){
		String url = TypeUtils.modefindUrl(urls,1);
		if(url==null||!Pattern.compile("(https*://.+)").matcher(url).matches()){
			LOG.warn("The url is unvalid");
			return null;
		}
		//查询goodsdata表
		GoodsDaoBean data = gd.queryData("goodsdata",url);
		
		/***************数据库没有数据，则解析数据，并添加数据到数据库***************/
		if(data==null){
			GoodsBean goods = parseGoodsw(url,cycle);
			goods.setCom("web");
			return goods;
		}
		
		
		String valid = data.getValid();
		Boolean isNew = TypeUtils.isNew(data.getTime(),3.0);
		
		/*******************数据库已有最新数据，从数据库取数据***************/
		if((valid!=null&&"2".equals(valid))||(isNew&&"1".equals(valid))){
			return dao2Bean(data);
		}
		
		return dao2Bean(data);
		
		/***************runer 测试商品添加数据库*******正式库没有这种状态********/
			
		/********数据库已有数据，但不是最新的，重新解析数据，并更新数据库数据********/
		/*if(valid!=null&&"1".equals(valid)&&!isNew){
			GoodsBean goods = parseGoodsw(url,cycle);
			goods.setCom("web");
			return goods;
		}*/
		
		/********数据库已有数据，其他情况********/
//		GoodsBean goods = new GoodsBean();
//		goods.setValid(0);
//		goods.setpUrl(url);
//		goods.setCom("sql");
//		return goods;
	}
	
	
	/**货源获取商品数据
	 * @return
	 */
	public static GoodsBean parseGoodsWbsite(String urls, String name, String price, ArrayList<TypeBean> type, String goodsCarId){
		IGoodsDao gd = new GoodsDao();
		String url = TypeUtils.modefindUrl(urls,1);
		if(url==null||!Pattern.compile("(https*://.+)").matcher(url).matches()){
			LOG.warn("The url is unvalid");
			return null;
		}
		if(url.indexOf("aliexpress") > -1 ){
			return null;
		}
		GoodsBean goods = parseGoodsw(url,0);
		ArrayList<String> getpImage = goods.getpImage();
		if(getpImage.size() == 0){
			getpImage= gd.getImages(goodsCarId);
//			getpImage.add("https://cbu01.alicdn.com/img/ibank/2018/940/040/8665040049_1232941264.400x400.jpg");
//			goods = contentClient(url);
			
		}
		goods.setCom("web");
		GoodsBean t_goods = newGoods(goods);
		if(t_goods!=null){
			t_goods.setpName(name);
			t_goods.setTitle(name);
			t_goods.setpSprice(price);
			t_goods.setpInfo(null);
			t_goods.setType(type);
			t_goods.setpWprice(null);
			t_goods.setpPriceUnit("RMB");
			t_goods.setpGoodsUnit("piece");
			t_goods.setValid(2);
			new GoodsThread(t_goods,true).start();
		}
		t_goods = null;
		return goods;
	}
	
	public static GoodsBean contentClient(String url) {
		
		String requestUrl  = "http://192.168.1.48:8881/sourceparse/source?goodsurl=" + url;
		String contentClient = DownloadMain.getContentClient(requestUrl, IpSelect.ip());
		GoodsBean goods = new GoodsBean();
		if(StringUtils.isNotBlank(contentClient)){
//			contentClient = contentClient.replaceAll("null", "");
			JSONObject fromObject = JSONObject.parseObject(contentClient);
			goods = JSONObject.parseObject(fromObject.getString("getpgoods"),GoodsBean.class);
			if(goods == null){
				return goods;
			}

			ArrayList<String> pImage = (ArrayList<String>) JSONArray.parseArray(fromObject.getString("getpImage"),String.class);
			goods.setpImage(pImage);

			HashMap<String, String> pInfo = JSONObject.parseObject(fromObject.getString("getpInfo"), HashMap.class);
			goods.setpInfo(pInfo);

			ArrayList<TypeBean> ptype = (ArrayList<TypeBean>) JSONArray.parseArray(fromObject.getString("getptype"),TypeBean.class);
			goods.setType(ptype);

			ArrayList<String> pWprice = (ArrayList<String>) JSONArray.parseArray(fromObject.getString("getpWprice"),String.class);
			goods.setpWprice(pWprice);

			ArrayList<String> pDetail = (ArrayList<String>) JSONArray.parseArray(fromObject.getString("getpDetail"),String.class);
			goods.setpDetail(pDetail);
			
			String[] pimgSize = new String[2];
			JSONArray imgSize = JSONArray.parseArray(fromObject.getString("imgSize"));
			if(imgSize.size() == 2){
				for(int i=0;i<2;i++){
					pimgSize[i] = imgSize.get(i).toString();
				}
				goods.setImgSize(pimgSize);
			}
			
			System.err.println(goods);
			
		}
		return goods;
		
	}
	/**GoodsBean数据复制
	 * @param goods
	 * @return
	 */
	private static GoodsBean newGoods(GoodsBean goods){
		if(goods==null){
			return null;
		}
		GoodsBean t_goods = new GoodsBean();
		t_goods.setCid(goods.getCid());
		t_goods.setApiItemDesc(goods.getApiItemDesc());
		t_goods.setbPrice(goods.getbPrice());
		t_goods.setCategory(goods.getCategory());
		t_goods.setFlag(goods.getFlag());
		t_goods.setFprice(goods.getFprice());
		t_goods.setFree(goods.getFree());
		t_goods.setImgSize(goods.getImgSize());
		t_goods.setInfo_ori(goods.getInfo_ori());
		t_goods.setInfourl(goods.getInfourl());
		t_goods.setMethod(goods.getMethod());
		t_goods.setMinOrder(StrUtils.matchStr(goods.getMinOrder(), "(\\d+)"));
		t_goods.setPackages(goods.getPackages());
		t_goods.setpDescription(goods.getpDescription());
//		t_goods.setpDetail(goods.getpDetail());
		t_goods.setPerWeight(goods.getPerWeight());
		t_goods.setpFreight(goods.getpFreight());
		t_goods.setpFreightChange(goods.getpFreightChange());
		t_goods.setpGoodsUnit(goods.getpGoodsUnit());
		t_goods.setpID(goods.getpID());
		t_goods.setpImage(goods.getpImage());
		t_goods.setpInfo(goods.getpInfo());
		t_goods.setpName(goods.getpName());
		t_goods.setpOprice(goods.getpOprice());
		t_goods.setpPriceUnit(goods.getpPriceUnit());
		t_goods.setpSprice(goods.getpSprice());
		t_goods.setpTime(goods.getpTime());
		t_goods.setpUrl(goods.getpUrl());
		t_goods.setpWprice(goods.getpWprice());
		t_goods.setSell(goods.getSell());
		t_goods.setSellUnits(goods.getSellUnits());
		t_goods.setsID(goods.getsID());
		t_goods.setSkuProducts(goods.getSkuProducts());
		t_goods.setSupplierUrl(goods.getSupplierUrl());
		t_goods.setTitle(goods.getTitle());
		t_goods.setTime(goods.getTime());
		t_goods.setType(goods.getType());
		t_goods.setWeight(goods.getWeight());
		t_goods.setWidth(goods.getWidth());
		t_goods.setFeeprice(goods.getFeeprice());
		t_goods.setNoteUrl(goods.getNoteUrl());
		t_goods.setCatid1(goods.getCatid1());
		t_goods.setCatid2(goods.getCatid2());
		t_goods.setCatid3(goods.getCatid3());
		t_goods.setCatid4(goods.getCatid4());
		t_goods.setCatid5(goods.getCatid5());
		t_goods.setCatid6(goods.getCatid6());
		t_goods.setCategps(goods.getCategps());
		t_goods.setPvid(goods.getPvid());
		t_goods.setDtime(goods.getDtime());
		return t_goods;
	}
	
	/**GoodsDaoBean  to GoodsBean
	 * @param map
	 * @return
	 */
	public static GoodsBean dao2Bean(GoodsDaoBean map){
		if(map==null){
			return null;
		}
		GoodsBean goods = new GoodsBean();
		goods.setDtime(map.getDtime());
		goods.setValid(1);
		goods.setpID(map.getpID());
		goods.setCid(Integer.valueOf(map.getcID()));
		//商品名称
		String g_name = map.getName();
		if(g_name!=null&&Pattern.compile(PATE).matcher(g_name).find()){
			g_name = g_name.replaceAll(PATE, "").replaceAll("\\s+", " ").trim();
		}
		goods.setpName(g_name);
		goods.setId(map.getId());
		String url = map.getUrl();
		goods.setpUrl(url);
		goods.setpOprice(map.getoPrice());
		goods.setpSprice(map.getsPrice());
		//获取批发价格
		String wprice  = map.getwPrice();
		if(wprice!=null&&!wprice.isEmpty()){
			wprice = wprice.replace("[", "").replace("]", "").trim();
			String[] prices = wprice.split(",\\s+");
			ArrayList<String> wList = new ArrayList<String>();
			for(int i=0;i<prices.length;i++){
				if(prices[i].isEmpty()){
					continue;
				}
				wList.add(prices[i]);
			}
			if(wList!=null&&!wList.isEmpty()){
				goods.setpWprice(wList);
			}
			wList = null;
		}
		wprice  =null;
		goods.setMinOrder(StrUtils.matchStr(map.getmOrder(), "(\\d+)"));
		goods.setpPriceUnit(map.getpUnit());
		goods.setpGoodsUnit(map.getgUnit());
		//获取图片集
		String img = map.getImg();
		if(img!=null&&!img.isEmpty()){
			img = img.replace("[", "").replace("]", "").trim();
			String[] imgs = img.split(",\\s+");
			ArrayList<String> imgList = new ArrayList<String>();
			for(int i=0;i<imgs.length;i++){
				if(imgs[i].isEmpty()){
					continue;
				}
				imgList.add(imgs[i]);
			}
			goods.setpImage(imgList);
			imgList = null;
		}
		img = null;
		//获取图片尺寸
		String imgSize = map.getImgSize();
		if(imgSize!=null&&!imgSize.isEmpty()){
			String[] split = imgSize.split("\\+");
			if(split.length==2){
				goods.setImgSize(split);
			}
			split = null;
		}
		imgSize = null;
		
		goods.setSellUnits(map.getSellUnit());
		goods.setExtra_freight(getExtraFreight(map.getWidth()));
		goods.setWidth(map.getWidth());
		String cat = map.getCategory();
		goods.setCategory(cat);
		goods.setsID(map.getsID());
		goods.setTitle(map.getTitle());
		goods.setSupplierUrl(map.getsUrl());
		
		//获取supplierGoods
		/*String sGoods = map.getsGoods();
		if(sGoods!=null&&!sGoods.isEmpty()){
			sGoods = sGoods.replace("[", "").replace("]", "").trim();
			String[] sGood = sGoods.split(",\\s+");
			ArrayList<SupplierGoods> goodsList = new ArrayList<SupplierGoods>();
			String goods_tem;
			String[] goods_tems;
			for(int i=0;i<sGood.length;i++){
				if(!sGood[i].isEmpty()){
					SupplierGoods sg = new SupplierGoods();
					String[] s_goods = sGood[i].split("\\+#\\s+");
					for(int j=0;j<s_goods.length;j++){
						if(Pattern.compile("(g_name=)").matcher(s_goods[j]).find()){
							goods_tems = s_goods[j].split("g_name=");
							goods_tem = goods_tems.length>1?goods_tems[1]:"";
							sg.setG_name(goods_tem);
							goods_tem = null;
						}else if(Pattern.compile("(g_img=)").matcher(s_goods[j]).find()){
							goods_tems = s_goods[j].split("g_img=");
							goods_tem = goods_tems.length>1?goods_tems[1]:"";
							sg.setG_img(goods_tem);
							goods_tem = null;
						}else if(Pattern.compile("(g_price=)").matcher(s_goods[j]).find()){
							goods_tems = s_goods[j].split("g_price=");
							goods_tem = goods_tems.length>1?goods_tems[1]:"";
							sg.setG_price(goods_tem);
							goods_tem = null;
						}else if(Pattern.compile("(g_min=)").matcher(s_goods[j]).find()){
							goods_tems =  s_goods[j].split("g_min=");
							goods_tem = goods_tems.length>1?goods_tems[1]:"";
							sg.setG_min(goods_tem);
							goods_tem = null;
						}else if(Pattern.compile("(g_url=)").matcher(s_goods[j]).find()){
							goods_tems = s_goods[j].split("g_url=");
							goods_tem = goods_tems.length>1?goods_tems[1]:"";
							sg.setG_url(goods_tem);
							goods_tem = null;
						}
					}
					goodsList.add(sg);
					sg = null;
				}
			}
			if(goodsList!=null&&!goodsList.isEmpty()){
				goods.setSupplier(goodsList);
			}
			goodsList = null;
		}
		sGoods = null;*/
		
		//获取尺寸信息
		String types = map.getTypes();
		if(types!=null&&!types.isEmpty()){
			types = types.replace("[", "").replace("]", "").trim();
			ArrayList<TypeBean> typeList = new ArrayList<TypeBean>();
			String[] types_s = types.split(",\\s+");
			TypeBean bean = null;
			String[] tems = null;
			String tem = null;
			for(int i=0;i<types_s.length;i++){
				if(types_s[i].isEmpty()){
					continue;
				}
				bean = new TypeBean();
				String[] type = types_s[i].split("\\+#\\s+");
				for(int j=0;j<type.length;j++){
					if(Pattern.compile("(id=)").matcher(type[j]).find()){
						tems = type[j].split("id=");
						tem = tems.length>1?tems[1]:"";
						bean.setId(tem);
					}else if(Pattern.compile("(type=)").matcher(type[j]).find()){
						tems = type[j].split("type=");
						tem = tems.length>1?tems[1]:"";
						bean.setType(tem);
					}else if(Pattern.compile("(value=)").matcher(type[j]).find()){
						tems = type[j].split("value=");
						tem = tems.length>1?tems[1]:"";
						bean.setValue(tem);
					}else if(Pattern.compile("(img=)").matcher(type[j]).find()){
						tems = type[j].split("img=");
						tem = tems.length>1?tems[1]:"";
						bean.setImg(tem);
					}
				}
				typeList.add(bean);
			}
			goods.setType(typeList);
		}
		types  =null;
		
		//获取商品详细图文信息
		String info = map.getInfo();
		String packages = map.getPackages();
		goods.setPackages(packages);
		
		if(info!=null&&!info.isEmpty()){
			info = info.replaceAll("=======", "");
			if(packages!=null){
				info = new StringBuffer().append(info).append("<div style=\"clear:both;\"></div><br/><br/>").append(packages).toString();
			}
			List<String> list = StrUtils.matchStrList("(?:width\\:)(.*?)(?:px)", info);
			String width = null;
			for(int i=0;i<list.size();i++){
				width = StrUtils.matchStr(list.get(i), "\\d+\\.*\\d*");
				if(Pattern.compile("(9\\d{2}\\.*\\d*)").matcher(width).matches()){
					info = info.replaceAll("width\\:\\s*"+width+"\\s*px;", "");
				}
				else if(Pattern.compile("(\\d{4,}\\.*\\d*)").matcher(width).find()){
					info = info.replaceAll("width\\:\\s*"+width+"\\s*px;", "");
				}
			}
			info = info.replace("http://www.aliexpress.com", "");
			info = info.replaceAll("(width\\s*=\"\\s*\\d{4,}\\.*\\d*\")", "");
		}
	    goods.setInfo_ori(info);
	    if(info==null||info.isEmpty()){
			goods.setFlag("0");
		}
		info = null;
		
		//获取商品detail信息
		String detail = map.getDetail();
		if(detail!=null&&!detail.isEmpty()){
			detail = detail.replace("{", "").replace("}", "").trim();
			HashMap<String, String> detailList = new HashMap<String, String>();
			String[] details = detail.split(",\\s+\\d+=");
			for(int i=0;i<details.length;i++){
				if(details[i].isEmpty()){
					continue;
				}
				if(i==0){
					detailList.put(i+"", details[i].substring(3));
				}else{
					detailList.put(i+"", details[i]);
				}
			}
			goods.setpInfo(detailList);
			detailList=  null;
			details = null;
		}
		detail = null;
		
		//获取商品单页底部相关搜索关键词
		/*String relate = map.getData();
		if(relate!=null&&!relate.isEmpty()){
			relate = relate.replace("[", "").replace("]", "").trim();
			ArrayList<SearchWordBean> relateList = new ArrayList<SearchWordBean>();
			String[] redata = relate.split(",\\s+\\{");
			SearchWordBean swb = null;
			for(int i=0;i<redata.length;i++){
				if(!redata[i].isEmpty()){
					String[] sb = redata[i].replace("{", "").replace("}", "").split(";");
					if(sb.length==4){
						swb = new SearchWordBean();
						swb.setWebsite(sb[0].split("[=:]")[1].replaceAll("^\"*|\"*$", ""));
						swb.setType(sb[1].split("[=:]")[1].replaceAll("^\"*|\"*$", ""));
						swb.setUrl(sb[2].split("[=:]")[1].replaceAll("^\"*|\"*$", ""));
						swb.setName(sb[3].split("[=:]")[1].replaceAll("^\"*|\"*$", ""));
						relateList.add(swb);
					}
					swb = null;
					sb  = null;
				}
			}
			if(relateList!=null&&!relateList.isEmpty()){
				goods.setRelate(relateList);
			}
			relateList=  null;
			redata = null;
		}
		relate  =null;*/
		
		//获取已经售出数量
		goods.setSell(map.getSell());
		
		//免邮标志
		String free = map.getFree();
		goods.setFree(free);
		//除去免邮运费后价格
		String fprice = map.getfPrice();
		goods.setFprice(fprice);
		
		//免邮的快递方式
		String method = map.getMethod();
		goods.setMethod(method);
		//免邮快递运输时间
		String posttime = map.getPosttime();
		goods.setTime(posttime);
		//免邮运费
		goods.setFeeprice(map.getFeePrice());
		goods.setInfourl(map.getInfourl());
		if(free!=null&&!"3".equals(free)&&(posttime==null||method==null||posttime.isEmpty()||method.isEmpty())){
			if(Pattern.compile("(aliexpress)").matcher(url).find()){
				LOG.warn("set ajx to querry fee");
				goods.setFree("2");
			}
		}
		//bulk价格
		goods.setbPrice(map.getBprice());
		//规格价格数据
		goods.setSkuProducts(map.getSku());
		
		//类别导航
		goods = modefiExprand(goods);
		
		//数据来源
		goods.setCom("sql");
		
		//重量设置默认值----2016-4-7
		/*String goods_weight = map.getWeight();
		if(goods_weight==null||goods_weight.isEmpty()){
			String catid = goods.getCatid1();
			catid = catid==null||catid.isEmpty()?goods.getCatid2():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid3():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid4():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid5():catid;
			catid = catid==null||catid.isEmpty()?goods.getCatid6():catid;
			goods_weight = GetFilterUtils.weightFilter(catid);
			goods_weight = goods_weight==null?"0":goods_weight;
			goods.setWeight(goods_weight);
			goods.setPerWeight(goods_weight);
			goods.setSellUnits(goods.getpGoodsUnit());
			goods.setWidth("");
		}else{
			goods.setWeight(goods_weight);
			goods.setPerWeight(map.getPerWeight());
		}*/
		//交期
		String pTime = map.getpTime();
		pTime = pTime==null?"7-12":pTime;
		pTime = StrUtils.matchStr(pTime, "(\\d+-*\\d*)");
		pTime = pTime.isEmpty()?"7-12":pTime;
		//交期统一加上3天  最低3天
		String[] pTime_split = pTime.split("-");
		if(Integer.parseInt(pTime_split[0])<3){
			pTime = "3";
			pTime = pTime_split.length>1?pTime+"-"+ pTime_split[1]:pTime;
		}
		goods.setpTime(pTime);
		return goods;
	}
	
	
	/**获取商品详情
	 * @param infourl
	 * @return
	 */
	public static String getInfo(String infourl,String ip){
		String info = null;
		if(infourl==null||infourl.isEmpty()){
			return null;
		}
		if(infourl.indexOf("http:") == -1){
			infourl = "http:"+infourl;
		}
		info = OtherParse.getInfo(infourl,ip);
		
		if(info==null||info.isEmpty()){
			LOG.warn("get goods info from web fail-infourl:"+infourl);
			return info;
		}
		if(info.contains("<!DOCTYPE html>")){
			return "";
		}
		info = info.replaceAll("=======", "").replaceAll(">\\s*\\\\", ">");
		info = info.lastIndexOf("';")>0?info.substring(0, info.lastIndexOf("';")):info;
		String pat = StrUtils.matchStr(info, "(?:strong><span style=\"m.*>)(.*?)(?:</span></strong>)");
		String replace = pat.replaceAll("&nbsp;", " ");
		if(replace.length()>300){
			int size = replace.length()/2-1;
			replace = new StringBuffer().append(replace.substring(0,size)).append(" ")
					.append(replace.substring(size)).toString();
		}
		info = info.replace(pat, replace);
		List<String> list = StrUtils.matchStrList("(?:width\\:)(.*?)(?:px)", info);
		String width = null;
		for(int i=0;i<list.size();i++){
			width = StrUtils.matchStr(list.get(i), "\\d+\\.*\\d*");
			if(Pattern.compile("(9\\d{2}\\.*\\d*)").matcher(width).matches()){
				info = info.replaceAll("width\\:\\s*"+width+"\\s*px;", "");
			}else if(Pattern.compile("(\\d{4,}\\.*\\d*)").matcher(width).find()){
				info = info.replaceAll("width\\:\\s*"+width+"\\s*px;", "");
			}
		}
		info = info.replace("http://www.aliexpress.com", "");
		info = info.replaceAll("(width\\s*=\"\\s*\\d{4,}\\.*\\d*\")", "");
//		if(info!=null&&info.length()<60000){
//			IGoodsDao  gd = new GoodsDao();
//			gd.updateInfo(info,infourl);
//			gd = null;
//		}
		return info;
	}
	
	private static String fee(HttpServletRequest request, String url, String pid, String sprice, String weight, String volum, String pWight){
		String result = null;
		url = TypeUtils.modefindUrl(url,1);
		if(pid==null||pid.isEmpty()){
			return "3";
		}
		//商品的非免邮价格
		String fprice = null;
		pid = StrUtils.matchStr(pid, "(\\d+)").trim();
		//运费请求链接
		String fre_url = "http://freight.aliexpress.com/ajaxFreightCalculateService.htm?callback=jQuery&productid="+pid+"&country=US&count=1&sendGoodsCountry=CN";
		//运费链接返回内容
		String content = DownloadMain.getContentClient(fre_url,null);
		//运费
		String pFreight = StrUtils.matchStr(content, "(?:localPriceFormatStr\":\")(.*?)(?:\",)")
							.replace("US", "").replace("$", "").replace(",", "").trim();
		//快递方式
		String method = StrUtils.matchStr(content, "(?:companyDisplayName\":\")(.*?)(?:\",)");
		//快递时间
		String posttime = StrUtils.matchStr(content, "(?:time\":\")(.*?)(?:\",)");
		//所有运费集合
		List<String> fees = StrUtils.matchStrList("(?:localTotalFreight\":\")(.*?)(?:\")",content);
		//最低运费
		String minFee = getMinFee(fees,volum,pWight);
		DecimalFormat format = new DecimalFormat("#0.00");
		String sprice1 = null;
		String sprice2 = null;
		//计算商品的非免邮价格
		//①最低运费 > 商品价格的25%    则非免邮价格为 商品价格*75%
		//②最低运费 <= 商品价格的25%  则非免邮价格为 商品价格 - 最低运费*70%
		if(minFee!=null&&!minFee.isEmpty()){
			Double double_minFeeprice = Double.valueOf(minFee.replaceAll("\\s+", "").trim());
			if(sprice!=null&&!sprice.isEmpty()){
				sprice = sprice.replaceAll("\\s+", "").trim();
				sprice1 = sprice;
				String fprice2 = null;
				//价格区间（最高价格）
				String[] split_sprice = sprice.split("-");
				if(split_sprice.length>1){
					sprice2 = StrUtils.matchStr(split_sprice[1].replaceAll("\\s+", "").trim(), "\\d+\\.*\\d*");
					if(!sprice2.isEmpty()){
						Double double_sprice2 = Double.valueOf(sprice2);
						Double priced2 = 0.0;
						if(double_minFeeprice>double_sprice2*0.25){
							priced2 = double_sprice2*0.75;
						}else{
							priced2 = double_sprice2-(double_minFeeprice*0.7);
						}
						fprice2 = priced2>0?String.valueOf(format.format(priced2)):null;
						sprice1 = sprice.split("-")[0].replaceAll("\\s+", "").trim();
					}
				}
				//价格（若是价格区间则为价格最低价格）
				sprice1 = StrUtils.matchStr(sprice1, "\\d+\\.*\\d*");
				if(!sprice1.isEmpty()){
					Double double_sprice1 = Double.valueOf(sprice1);
					Double priced1 = 0.0;
					if(double_minFeeprice>double_sprice1*0.25){
						priced1 = double_sprice1*0.75;
					}else{
						priced1 = double_sprice1-(double_minFeeprice*0.7);
					}
					fprice = priced1>0?String.valueOf(format.format(priced1)):null;
					if(fprice!=null&&fprice2!=null&&!fprice2.isEmpty()){
						fprice = fprice +" - "+fprice2;
					}
				}else{
					fprice = null;
				}
			}else{
				fprice = null;
			}
		}else{
			fprice = null;
		}
		
		if(pFreight==null||pFreight.isEmpty()){
//			LOG.warn("result-update free pFreight(no free):"+pFreight);
			return "3";
		}
		//商品免邮
		result = "1"+"@";
		//商品本来不是免邮
		Double  double_sprice1 = sprice1!=null&&!sprice1.isEmpty()?Double.valueOf(sprice1):0;//商品价格
		Double  double_pFreight = Double.valueOf(pFreight);//运费
		//校正的商品免邮价格
		String  p_sprice = null;
		//若商品价格大于20，且商品运费大于价格的50%
		//取消商品价格大于20的条件-------2016-4-8  孙经理提出
		if(/*p_sprice1>20&&*/double_pFreight>double_sprice1*0.5){
			//商品价格修正为  商品价格+运费
			double double_sprice_1 = double_sprice1+double_pFreight;
			p_sprice = format.format(double_sprice_1);
			//非免邮价格为修正后的价格的75%
			fprice = format.format(double_sprice_1*0.75);
			if(sprice2!=null&&!sprice2.isEmpty()){
				Double p_sprice2 = Double.valueOf(sprice2);
				Double p_sprices = p_sprice2+double_pFreight;
				p_sprice += "-"+format.format(p_sprices);
				fprice += " - "+format.format(p_sprices*0.75);
			}
			minFee = pFreight;
			posttime = "20-45";
			method = "China Post SAL";
		}
		if(!method.isEmpty()){
			result +=method+"@"+posttime;
		}
		if(fprice!=null&&!fprice.isEmpty()){
			result +="@"+fprice;
		}
		if(minFee!=null&&!minFee.isEmpty()){
			result +="@"+minFee;
		}
		if(p_sprice!=null&&!p_sprice.isEmpty()){
			result +="@"+p_sprice;
		}
		int updateFree = 0;
		if("0.00".equals(pFreight)){
		}else if(/*(p_sprice1>20||p_sprice1==20)&&*/double_pFreight>double_sprice1*0.5){
			//取消商品价格大于20的条件-------2016-4-8  孙经理要求的
			GoodsDaoBean bean = new GoodsDaoBean();
			bean.setUrl(url);
			bean.setsPrice(p_sprice);
			bean.setMethod(method);
			bean.setPosttime(posttime);
			bean.setFeePrice(minFee);
			bean.setfPrice(fprice);
			bean.setFree("1");
		}else{
			result = "3";
		}
		if(updateFree==0){
			LOG.warn("querry sql free goods url:"+url);
			LOG.warn("result-free_url:"+fre_url);
			LOG.warn("result-method:"+method);
			LOG.warn("result-pid_tem:"+pid);
			LOG.warn("result-pFreight:"+pFreight);
			LOG.warn("result-posttime:"+posttime);
			LOG.warn("result-update free:"+result);
		}
		pFreight = null;
		fre_url = null;
		content = null;
		method = null;
		posttime = null;
		return result;
	}
	
	
	
	/**取最小运费（不为零）
	 * @param fees
	 * @param weights
	 * @param volums
	 * @param pweights
	 * @return
	 */
	private static String getMinFee(List<String> fees,String volums,String pweights){
		DecimalFormat format = new DecimalFormat("#0.00");
		Double result = 0.0;
		if(fees!=null&&!fees.isEmpty()){
			Double price =0.0;
			Double fee = 0.0;
			int dex = 0;
			for(int i=0;i<fees.size();i++){
				fee = Double.valueOf(fees.get(i).replaceAll("\\s+", "").trim());
				if(fee==0.0){
					continue;
				}
				if(price==0.0&&dex==0){
					price = fee;
					dex++;
				}
				price = fee<price?fee:price;
			}
			result = price;
		}
		if(result==0&&volums!=null&&pweights!=null){
			IZoneServer zs= new ZoneServer();
//			weights = StrUtils.matchStr(weights, "\\d+\\.*\\d*");
//			weights = weights.isEmpty()?"0":weights;
			volums = StrUtils.matchStr(volums, "\\d+\\.*\\d*");
			pweights = StrUtils.matchStr(pweights, "\\d+\\.*\\d*");
			volums = volums.isEmpty()?"0":volums;
			pweights = pweights.isEmpty()?"0":pweights;
			
//			float weight = Float.valueOf(weights);
			float volum = Float.valueOf(volums);
			float perweight = Float.valueOf(pweights);
			List<ShippingBean> list=zs.getShippingList(43, perweight, volum,perweight , 16);
			double price = 0f;
			double result2;
			for(int i=0;i<list.size();i++){
				price = i==0?list.get(0).getResult():price;
				result2 = list.get(i).getResult();
				price = result2<price?result2:price;
			}
			LOG.warn("ZoneServer--getShippingList:"+price);
			result = (double) price;
		}
		return format.format(result);
	}
	
	/**获取运费  快递方式   快递时间等
	 * @param urls
	 * @param pid
	 * @param saleprice
	 * @param weight
	 * @param volum
	 * @param pWight
	 * @return
	 */
	public static String getFee(HttpServletRequest request, String urls, String pid, String saleprice, String weight, String volum, String pWight){
		String result = null;
		result = fee(request,urls, pid,saleprice,weight,volum,pWight);
		int num = 0;
		while((result==null||result.isEmpty())&&num<1){
			result = fee(request,urls, pid,saleprice,weight,volum,pWight);
			num++;
			LOG.warn("get free again:"+num+"-"+result);
		}
		return result;
	}

	/**计算重量
	 * @param count  购买数量
	 * @param perWeight单位重量
	 * @param seilUnit重量计算单位
	 * @param goodsUnit购买商品单位
	 * @return
	 */
	public static String  calculateWeight(int count,String perWeight,String seilUnit,String goodsUnit){
		String toWeight = "0";
		Double tem_count  = (double) count;
		if(count<1||perWeight==null||perWeight.isEmpty()){
			return toWeight;
		}
		String tem_seilUnit = seilUnit.trim().toLowerCase();
		String tem_goodsUnit = goodsUnit.trim().toLowerCase();
		String tem_perWeight = perWeight.toLowerCase();
		String ceil = StrUtils.matchStr(tem_perWeight, "(\\d+\\.*\\d*)").trim();
		double weight = ceil.isEmpty()?0:Double.valueOf(ceil.trim()) ;
		double total_weight=0.0;
		
		if(Pattern.compile("(g)").matcher(tem_perWeight.replace(ceil, "").trim()).matches()){
			weight = weight/1000.0;
		}
		if(seilUnit!=null&&goodsUnit!=null){
			String unit;
			double num;
			if(Pattern.compile("(lot)").matcher(tem_seilUnit).find()){
				String re_pat = StrUtils.matchStr(tem_seilUnit, "(\\d*\\s*[lL]ot)");
				String tem_seil = tem_seilUnit.replace(re_pat, "").trim();
				unit = StrUtils.matchStr(tem_seil, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				if(Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
					total_weight = tem_count * weight;
				}else{
					total_weight = tem_count * weight/num;
				}
				re_pat = null;
				tem_seil = null;
			}else if(Pattern.compile("(single)").matcher(tem_seilUnit).find()){
				if(!Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
					total_weight = tem_count * weight;
				}else{
					total_weight = tem_count * weight;
				}
			}else if(Pattern.compile("("+tem_goodsUnit+")").matcher(tem_seilUnit).find()){
				String tem_unit = StrUtils.matchStr(tem_seilUnit, "(\\d+\\s*"+tem_goodsUnit+")");
				unit = StrUtils.matchStr(tem_unit, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				total_weight = tem_count * weight/num;
				tem_unit = null;
			}else{
				unit = StrUtils.matchStr(tem_seilUnit, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				total_weight = tem_count * weight/num;
			}
			unit = null;
		}
		toWeight = String.valueOf(total_weight);
		if(toWeight.indexOf("E")>0){
			String tem_toWeight = toWeight.substring(0,toWeight.indexOf("E"));
			if(toWeight.indexOf("-")>0&&tem_toWeight.indexOf(".")>0){
				tem_toWeight = StrUtils.matchStr(tem_toWeight, "(\\d+\\.\\d{1,3})");
			}
			toWeight = tem_toWeight + toWeight.substring(toWeight.indexOf("E"));
			tem_toWeight = null;
		}else if(toWeight.indexOf(".")>0){
			toWeight = StrUtils.matchStr(toWeight, "(\\d+\\.\\d{1,3})");
		}
		tem_seilUnit = null;
		tem_goodsUnit = null;
		tem_perWeight = null;
		ceil = null;
		
		return toWeight;
	}
	
	/**计算体积
	 * @param count  购买数量
	 * @param perWeight单位体积
	 * @param seilUnit体积计算单位
	 * @param goodsUnit购买商品单位
	 * @return
	 */
	public static String  calculateVolume(int count,String perVolum,String seilUnit,String goodsUnit){
		String toVolume = "0";
		if(count>0&&perVolum!=null&&!perVolum.isEmpty()){
			String tem_seilUnit = seilUnit.trim().toLowerCase();
			String tem_goodsUnit = goodsUnit.trim().toLowerCase();
			String tem_perVolum = perVolum.toLowerCase();
			double total_volume=0.0;
			double volume = 0.0;
			if(tem_perVolum.indexOf("x")>0||tem_perVolum.indexOf("X")>0||tem_perVolum.indexOf("*")>0){
				String[] split = tem_perVolum.split("[xX\\*]");
				String width;
				String verte;
				String height;
				if(split.length>2){
					width = StrUtils.matchStr(split[0], "(\\d+\\.*\\d*)");
					verte = StrUtils.matchStr(split[1], "(\\d+\\.*\\d*)");
					height = StrUtils.matchStr(split[2], "(\\d+\\.*\\d*)");
					if(!width.isEmpty()&&!height.isEmpty()&&!verte.isEmpty()){
						volume = Double.valueOf(width)*
								Double.valueOf(height)*
								Double.valueOf(verte);
					}else{
						volume = 0;
					}
				}else if(split.length>1){
					width = StrUtils.matchStr(split[0], "(\\d+\\.*\\d*)");
					verte = StrUtils.matchStr(split[1], "(\\d+\\.*\\d*)");
					if(!width.isEmpty()&&!verte.isEmpty()){
						volume = Double.valueOf(width)*
								       Double.valueOf(verte);
					}else{
						volume = 0;
					}
				}else{
					width = StrUtils.matchStr(tem_perVolum, "(\\d+\\.*\\d*)");
					volume = width.isEmpty()?0:Double.valueOf(width);
				}
			}else{
				String volume_tem = StrUtils.matchStr(tem_perVolum, "(\\d+\\.*\\d*)");
				volume = volume_tem.isEmpty()?0:Double.valueOf(volume_tem);
			}
			
			
			if(tem_seilUnit!=null&&tem_goodsUnit!=null){
				String unit;
				double num;
				if(Pattern.compile("(lot)").matcher(tem_seilUnit).find()){
					String re_pat = StrUtils.matchStr(tem_seilUnit, "(\\d*\\s*[lL]ot)");
					tem_seilUnit = tem_seilUnit.replace(re_pat, "").trim();
					unit = StrUtils.matchStr(tem_seilUnit, "(\\d+)");
					num = unit.isEmpty()?1:Double.valueOf(unit);
					if(Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
						total_volume = count * volume;
					}else{
						total_volume = count * volume/num;
					}
				}else if(Pattern.compile("(single)").matcher(tem_seilUnit).find()){
					if(!Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
						total_volume = count * volume;
					}else{
						
						
					}
				}else if(Pattern.compile("("+tem_goodsUnit+")").matcher(tem_seilUnit).find()){
					String tem_unit = StrUtils.matchStr(tem_seilUnit, "(\\d+\\s*"+tem_goodsUnit+")");
					unit = StrUtils.matchStr(tem_unit, "(\\d+)");
					num = unit.isEmpty()?1:Double.valueOf(unit);
					total_volume = count * volume/num;
				}else{
					unit = StrUtils.matchStr(tem_seilUnit, "(\\d+)");
					num = unit.isEmpty()?1:Double.valueOf(unit);
					total_volume = count * volume/num;
				}
			}
			toVolume = String.valueOf(total_volume/1000000);
			if(toVolume.indexOf("E")>0){
				String tem_toVolume = toVolume.substring(0,toVolume.indexOf("E"));
				if(toVolume.indexOf("-")>0){
					if(tem_toVolume.indexOf(".")>0){
						tem_toVolume = StrUtils.matchStr(tem_toVolume, "(\\d+\\.\\d{1,3})");
					}
				}
				toVolume = tem_toVolume + toVolume.substring(toVolume.indexOf("E"));
			}else if(toVolume.indexOf(".")>0){
				toVolume = StrUtils.matchStr(toVolume, "(\\d+\\.\\d{1,3})");
			}
		}
		return toVolume;
	}
	
	/**获取单位重量
	 * @param perWeight
	 * @param seilUnit
	 * @param goodsUnit
	 * @return
	 */
	public static String  getPerWeight(String perWeight,String seilUnit,String goodsUnit){
		String toWeight = "";
		if(perWeight==null||perWeight.isEmpty()){
			return toWeight;
		}
		String tem_seilUnit = seilUnit==null?null:seilUnit.trim().toLowerCase();
		String tem_goodsUnit = goodsUnit==null?null:goodsUnit.trim().toLowerCase();
		String tem_perWeight = perWeight==null?null:perWeight.toLowerCase();
		String ceil = StrUtils.matchStr(tem_perWeight, "(\\d+\\.*\\d*)").replaceAll("\\.+", ".").trim();
		double weight = ceil.isEmpty()?0:Double.valueOf(ceil.trim()) ;
		double total_weight=0.0;
		if(Pattern.compile("(g)").matcher(tem_perWeight.replace(ceil, "").trim()).matches()){
			weight = weight/1000.0;
		}
		if(tem_seilUnit!=null&&tem_goodsUnit!=null){
			String unit;
			double num;
			if(Pattern.compile("(lot)").matcher(tem_seilUnit).find()){
				String re_pat = StrUtils.matchStr(tem_seilUnit, "(\\d*\\s*[lL]ot)");
				String tem_seil = tem_seilUnit.replace(re_pat, "").trim();
				unit = StrUtils.matchStr(tem_seil, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				if(Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
					total_weight = weight;
				}else{
					total_weight = weight/num;
				}
			}else if(Pattern.compile("(single)").matcher(tem_seilUnit).find()){
				if(!Pattern.compile("(lot)|(carton)").matcher(tem_goodsUnit).find()){
					total_weight = weight;
				}else{
					total_weight = weight;
				}
			}else if(Pattern.compile("("+tem_goodsUnit+")").matcher(tem_seilUnit).find()){
				String tem_unit = StrUtils.matchStr(tem_seilUnit, "(\\d+\\s*"+tem_goodsUnit+")");
				unit = StrUtils.matchStr(tem_unit, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				total_weight = weight/num;
			}else{
				unit = StrUtils.matchStr(tem_seilUnit, "(\\d+)");
				num = unit.isEmpty()?1:Double.valueOf(unit);
				total_weight = weight/num;
			}
		}
		toWeight = String.valueOf(total_weight);
		if(toWeight.indexOf("E")>0){
			String tem_toWeight = toWeight.substring(0,toWeight.indexOf("E"));
			if(toWeight.indexOf("-")>0){
				if(tem_toWeight.indexOf(".")>0){
					tem_toWeight = StrUtils.matchStr(tem_toWeight, "(\\d+\\.\\d{1,3})");
				}
			}
			toWeight = tem_toWeight + toWeight.substring(toWeight.indexOf("E"));
		}else if(toWeight.indexOf(".")>0){
			toWeight = StrUtils.matchStr(toWeight, "(\\d+\\.\\d{1,3})");
		}
		return toWeight;
	}
	
	/**中文版规格获取
	 * @param goods
	 * @return
	 */
	public  static ArrayList<TypeBean> filterYiWu(String  url){
		ArrayList<TypeBean> list = new ArrayList<TypeBean>();
		if(url!=null&&!url.isEmpty()){
			url = url.replace("http://en.yiwugou.com/", "http://www.yiwugou.com/");
			String page = DownloadMain.getJsoup(url,1,null);
			if(page!=null&&!page.isEmpty()){
				if(!"httperror".equals(page)){
					//JSoup解析HTML生成document
					Document doc = Jsoup.parse(page);
					page = null;
					//获取element
					Element body = doc.body();
					
					TypeBean bean = null;
					Elements type = body.select("li[class=mt10  pro-attr-one pro-shux01]");
					Elements type2 = body.select("li[class=mt10  pro-attr-one pro-shux03]");
					if(type2!=null&&!type2.isEmpty()){
						type.addAll(type2);
					}
					Elements types = null;
					Elements types2 = null;
					String typen = null;
					for(int i=0;i<type.size();i++){
						typen = type.get(i).select("span[class=titattr]").text();
						types = type.get(i).select("a");
						types2 = type.get(i).select("em");
						if(types2!=null&&!types2.isEmpty()){
							types.addAll(types2);
						}
						for(int j=0;j<types.size();j++){
							bean = new TypeBean();
							bean.setType(typen);
							bean.setValue(types.get(j).text());
							list.add(bean);
						}
					}
					doc = null;
					body = null;
				}
			}
		}
		return list;
	}
	
	
	
	/**yiwugou商品名称废话去除
	 * @param name
	 * @return
	 */
	public static String filterName(String name){
		if(name!=null){
			String[] names = new String[]{
					"made\\s+in\\s+china","fashionable","arrival","full\\s+of",
					"charming","advanced","listing","name\\s+brand","brand","top","alibaba",
					"stock","designer","fancy","supper\\s+quality","price","best","china",
					"chinese","Latest","patent","product","our","new","selling","sales",
					 "sale","Special","wholesale","The","\\d+\\s+dollar","two\\s+dollar",
					 "with","premium","factory","direct","3C\\s+certification","custom","boutique",
					"free\\s+shipping","quality","high\\s+quality","tailor-made","general",
					"merchandise","manufacturers","high quality","factory\\s+outlets","taobao",
					"creative","hot","into","accessories","2015","2016","2013","2014","2012",
					"dollar store","stylish","authentic","import","export","imported","exported",
					"genuine","supplies","simple","fashion","elegant","famous","local","lovely",
					"yiwu","off-the-shelf","bulk","all","original","priced","like","hot\\s+cakes",
					"specializing","excellence","Elite","special\\s+offer","professional","exotic","yuyao",
					"to sell","ningbo","shanghai","shenzhen","guangzhou","also","are","called","giant\\s+cheap",
					"most","practical","of","certified","Korean","simulation","fine","sourcing","year","shop",
					 "yuan","commodity","small","according","two\\s+generation","second\\s+generation",
					 "three\\s+generation","third\\s+generation","multifunctional","multi-functional",
					 "version","within","American","Japanese","two\\s+yuan","2\\s+yuan","please\\s+contact",
					 "Wangwang","qq\\s*\\:*\\s*\\d+","phone\\s*\\d+",
					 "contact\\s+telephone\\s+number\\s*\\:*\\s*\\d+-*\\d*",
					 //要去掉 引号，#,括号，中文括号 等特殊字符
					 "\"","#","\\(","\\)",",","\\.","\\:","\\?","\\\\","'","$","@","&",
			};
			int names_num = names.length;
			name = name.toLowerCase();
			for(int i=0;i<names_num;i++){
				name = name.replaceAll(names[i].toLowerCase(), "").trim();
			}
		}
		return name;
	}
	
	
	/**aliexpress用户反馈
	 * @param url
	 * @return
	 */
	public static String getNote(String url){
		if(url!=null&&!url.isEmpty()){
			if(url.indexOf("http:")==-1){
				url = "http:"+url;
			}
			url = url.replaceAll("&amp;", "&");
			String content = DownloadMain.getContentClient(url,null);
			if(content!=null&&!content.isEmpty()){
				Document doc = Jsoup.parse(content);
				content = null;
				//获取element
				Element body = doc.body();
				doc = null;
				Elements tbody = body.select("div[class=main-content]").select("tbody");
				body = null;
				Elements trs = tbody.select("tr");
				StringBuilder notes = new StringBuilder();
				tbody = null;
				String note = null;
				int trs_num = trs.size();
				Element tr = null;
				for(int i=0;i<trs_num;i++){
					tr = trs.get(i);
					note = tr.select("div[class=right feedback]").select("span").text();
					if(note==null||note.length()<21){
						continue;
					}
					note = tr.toString().replaceAll("No&nbsp;Feedback&nbsp;Score", "");
					List<String> list = StrUtils.matchStrList("(?:ownerMemberId)(.*?)(?:buyer)",note);
					for(int j=0;j<list.size();j++){
						note = note.replace("href=\"http://feedback.aliexpress.com/display/detail.htm?ownerMemberId"+list.get(j)+"buyer\"", "");
					}
					notes.append(note);
					note = null;
					tr = null;
				}
				trs = null;
				return notes.toString().replace("<tr>", "<tr class=\"feedbacktr\">");
			}
			return null;
		}
		return null;
	}
	
	/**根据商品长宽高(任意一边大于1米就收取)，收取额外的运费
	 * @param volum
	 * @return
	 */
	public static float  getExtraFreight(String volume){
		if(volume==null||volume.isEmpty()){
			return 0f;
		}
		//volume格式
		String[] volumes = volume.replace("X", "x").replace("*", "x").split("x");
		String tem_width = null;
		String tem_unit = null;
		for(int i=0;i<volumes.length;i++){
			tem_width = StrUtils.matchStr(volumes[i].trim(), "\\d+\\.*\\d*");
			tem_unit = volumes[i].trim().replace(tem_width, "").replace("\\s+", "").toLowerCase().trim();
			tem_width = tem_width.isEmpty()?"0":tem_width;
			if("cm".equals(tem_unit)&&Float.valueOf(tem_width)>100){
				return 50f;
			}else if("m".equals(tem_unit)&&Float.valueOf(tem_width)>1){
				return 50f;
			}else if(Float.valueOf(tem_width)>1){
				return 50f;
			}
		}
		return 0f;
	}
	
	/**商品重量、体积数据异步请求
	 * @date 2016年3月23日
	 * @author abc
	 * @param url
	 * @return  
	 */
	public static GoodsBean getWeight(String url){
		if(url==null||!Pattern.compile("(https*://.+)").matcher(url).matches()){
			return null;
		}
		GoodsBean goodsBean = parseGoodsw(url, 0);
		return goodsBean;
	}
	
	
	public static GoodsBean getGoodsData(String url){
		GoodsDaoBean goods = new GoodsDaoBean();
		goods = gd.queryData("goodsdata",url);
		return dao2Bean(goods);
	}
	
	
}
