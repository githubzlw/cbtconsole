package com.cbt.parse.driver;

import com.cbt.bean.ShippingBean;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.parse.bean.CnEnDaoBean;
import com.cbt.parse.bean.OneSixExpressBean;
import com.cbt.parse.bean.TypeBean;
import com.cbt.parse.dao.CatidFilterDao;
import com.cbt.parse.dao.CnEnDao;
import com.cbt.parse.dao.ImgFileDao;
import com.cbt.parse.dao.OneSixExpressDao;
import com.cbt.parse.daoimp.ICatidFilterDao;
import com.cbt.parse.daoimp.IOneSixExpressDao;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**用于数据更新，（例如1688数据更新）
 * @author abc
 * {
 * 例如1688数据更新
 *(1)每2周全面更新（轮流）
 *(2)如果 对于每个 客户访问的网页，如果 是 4天之前更新，就在数据库中 标记为要 立刻更新。 
 *(3)每天定时对 标记了“立刻更新”的产品 进行更新。
 *(4) 更新的 时候，如果发现 图片没变化 就不要再下载图片
 *}
 */
public class DataUpdateDriver {
	ICatidFilterDao catFilterDao = new CatidFilterDao();
	DecimalFormat format = new DecimalFormat("#0.00");
	IZoneServer zs= new ZoneServer();//运费
	ImgFileDao  imgdao = new ImgFileDao();
	
	/**
	 * 更新标记的数据
	 * valid:0为全部更新
	 */
	public void  updateRemark(int valid){
		IOneSixExpressDao  oneSixExpressDao = new OneSixExpressDao();
		ArrayList<OneSixExpressBean> datas = oneSixExpressDao.getAllData(valid);
		ISpiderServer spider = new SpiderServer();
		Map<String, Double> maphl = spider.getExchangeRate();
		Double rmb = maphl.get("RMB");
		//翻译
		CnEnDao dao = new CnEnDao();
		ArrayList<CnEnDaoBean> query = dao.query();
		int datas_num = datas.size();
		String goods_url = null;
		String catid = null;
		int id = 0;
		for(int i=0;i<10;i++){
			System.out.println("---------------------"+i+"------------");
			goods_url = datas.get(i).getUrl();
			catid = datas.get(i).getCatid1();
			id = datas.get(i).getId();
			OneSixExpressBean bean = updateOneSix(goods_url,catid,rmb,query,id);
			System.out.println("url---------"+goods_url);
			if(bean==null||bean.getValid()==0){
				//数据失效
				oneSixExpressDao.updateValid(bean.getUrl(), bean.getValid());
			}else if(bean!=null&&bean.getValid()!=0){
				//数据信息更新
				oneSixExpressDao.update(bean);
			}
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		}
	}
	
	/**
	 * 数据更新
	 * @param url
	 * @param catid
	 * @param rmb
	 * @param query
	 * @return
	 */
	private OneSixExpressBean updateOneSix(String url, String catid, double rmb, ArrayList<CnEnDaoBean> query, int id){
		OneSixExpressBean bean = null;
		if(url!=null){
			//获取基本数据
			GoodsBean goods = ParseGoodsUrl.parseGoodsw(url, 0);
			bean = new OneSixExpressBean();
			bean.setUrl(url);
			if(goods==null||goods.isEmpty()){
				bean.setValid(0);
			}else{
				bean.setValid(1);
				HashMap<String, String> detail = goods.getpInfo();//明细
				if(detail!=null){
					bean.setDetail(detail.toString());
					//英文
					bean.setEndetail(trant(detail.toString(), query, 1));
				}
				ArrayList<TypeBean> type = goods.getType();//规格
				if(type!=null){
					bean.setType(type.toString());
					//英文
					bean.setEntype(trant(type.toString(), query, 2));
				}
				String name = goods.getpName();//名称
				if(name!=null){
					bean.setName(name);
					//英文
					bean.setEnname(trant(name, query, 0));
				}
				
				String weight = goods.getWeight();//重量
				//类别均重
				if(weight==null||weight.isEmpty()){
					weight = catFilterDao.queryWeightFilter(catid);
					//类别对应默认重量
					if(weight==null||weight.isEmpty()){
						weight = "0.268";
					}
				}else{
					weight = DownloadMain.getSpiderContext(weight, "(\\d+\\.*\\d*)");
				}
				if(weight==null||weight.isEmpty()){
					weight = "0.268";
				}
				bean.setWeight(weight);
				ArrayList<String> wprice = goods.getpWprice();//价格
				if(wprice!=null){
					//调整价格//调整//非免邮价格//快递方式//快递时间
					bean.setWprice(wprice.toString());
					bean = updatePrice1688(bean, rmb);
				}
				//已经售出数量
				String sold = goods.getSell();
				sold = sold.isEmpty()?"0":sold.replaceAll("\\D+", "");
				bean.setSold(sold);
				String minOrder = goods.getMinOrder();
				System.out.println("minOrder:"+minOrder);
				
				minOrder = minOrder==null?"1":minOrder;
				minOrder = minOrder.replaceAll("(\\D+)", "").trim();
				minOrder = minOrder.isEmpty()?"1":minOrder;
				bean.setMorder(minOrder);
				//图片集合
				ArrayList<String> image = goods.getpImage();
				System.out.println("image:"+image);
				if(image!=null&&!image.isEmpty()){
					for(int k=0;k<image.size();k++){
						String img = image.get(k);
						if(!Pattern.compile("(\\.310x310\\.jpg)").matcher(img).find()){
							img = img + "310x310.jpg";
						}
						updateImgs(img, url, id);//某些不存在的图片需要下载
					}
					if(!Pattern.compile("(\\.310x310\\.jpg)").matcher(image.get(0)).find()){
						bean.setImg(image.get(0)+"310x310.jpg");
					}else{
						bean.setImg(image.get(0));
					}
					bean.setImgs(image.toString());
				}
				//详情
				String info_ori = goods.getInfo_ori();
				List<String> list = DownloadMain.getSpiderContextList1("(?:src=\")(.*?)(?:\")", info_ori);
				String info_img = null;
				for(int k=0;k<list.size();k++){
					info_img = list.get(k);
					if(Pattern.compile("(\\.gif)").matcher(info_img).find()){
						info_ori = info_ori.replace(info_img, "");
					}
					updateImgs(list.get(k), url, id);
				}
				bean.setInfo(info_ori);//某些不存在的图片需要下载
			}
		}
		return bean;
	}
	
	/**更新的 时候，如果发现 图片没变化 就不要再下载图片
	 * @param img
	 */
	private void updateImgs(String img,String url,int id){
		//通过imgurl检索数据表imgfile是否存在，不存在就下载图片（其中去除babidou、 .gif图片）
		if(img!=null&&img.length()>6&&!Pattern.compile("(\\.gif)|(babidou)|(lazyload)").matcher(img).find()){
			img = img.replace(".jpg_.webp", ".jpg");
			if(imgdao.query(img, url).size()==0){
				String path = "F:/1688-update/"+ImgDownloadDriver.getPath(img);
				if(ImgDownload.execute(img, path)){
					//imgdao.add(img, path, url, id);
				}
			}
		}
	}
	
	/**
	 * 更新免邮价格String posttime_d,String method_d,float feeprice_d
	 */
	private OneSixExpressBean updatePrice1688(OneSixExpressBean bean, double rmb){
		String wprice = null;
		String price = null;
		double feeprice = 0.0f;
		String fprice = "";
		String tem_price = null;
		String sql_wprice = null;
		String sql_price = null;
		String sql_feeprice = null;
		String posttime = null;
		String method = null;
		String weight = bean.getWeight();//重量
		List<ShippingBean> list2 = zs.getShippingList(43, Float.valueOf(weight), 0.0f,Float.valueOf(weight) , 16);
		feeprice = 0.0f;
		double result2 = 0.0f;
		for(int j=0;j<list2.size();j++){
			feeprice = j==0?list2.get(0).getResult():feeprice;
			posttime = j==0?list2.get(0).getDays():posttime;
			method = j==0?list2.get(0).getName():method;
			result2 = list2.get(j).getResult();
			if(result2<feeprice){
				feeprice = result2;
				posttime = list2.get(j).getDays();
				method = list2.get(j).getName();
			}
		}
		sql_feeprice = format.format(feeprice);
		//wprice
		wprice = bean.getWprice();
		System.out.println("wprice:"+wprice);
		if(wprice!=null&&!wprice.isEmpty()){
			wprice = wprice.replace("[", "").replace("]", "").trim();
			String[] prices = wprice.split(",\\s+");
			ArrayList<String> wList = new ArrayList<String>();
			tem_price = null;
			String str_price = null;
			System.out.println("feeprice:"+feeprice);
//			System.out.println("prices.length:"+prices.length);
			double double_ten_price = 0.0;
			for(int j=0;j<prices.length;j++){
				str_price = DownloadMain.getSpiderContext(prices[j], "((≥)*\\d+-*\\d*\\s*RMB\\s*\\d*)");
				if(!str_price.isEmpty()){
					tem_price = DownloadMain.getSpiderContext(str_price, "(RMB\\s*\\d*)").replaceAll("RMB\\s*", "").trim();
					price = tem_price;//最后一个批发价格就是搜索页面显示的价格
					double_ten_price = Double.valueOf(tem_price);
					if(double_ten_price>0&&double_ten_price<40){
						tem_price = format.format(((Double.valueOf(double_ten_price)/rmb)*1.4)+feeprice);
					}else if(double_ten_price>=40&&double_ten_price<100){
						tem_price = format.format(((Double.valueOf(double_ten_price)/rmb)*1.35)+feeprice);
					}else if(double_ten_price>=100&&double_ten_price<200){
						tem_price = format.format(((Double.valueOf(double_ten_price)/rmb)*1.3)+feeprice);
					}else if(double_ten_price>=200&&double_ten_price<400){
						tem_price = format.format(((Double.valueOf(double_ten_price)/rmb)*1.25)+feeprice);
					}else if(double_ten_price>=400){
						tem_price = format.format(((Double.valueOf(double_ten_price)/rmb)*1.2)+feeprice);
					}
					sql_price = tem_price;//最后一个批发价格就是搜索页面显示的价格  调整后的价格
					str_price = DownloadMain.getSpiderContext(str_price, "(.*RMB)").replace("RMB", "$").trim();
					wList.add(str_price+tem_price);
					if(j==0){
						fprice = fprice.isEmpty()?format.format(Double.valueOf(tem_price)-feeprice):fprice;
					}
				}
			}
			sql_wprice = wList.toString();
			wList = null;
		}
		wprice = null;
		fprice = fprice.isEmpty()?"0":fprice;
		bean.setFprice(fprice);
		bean.setFeeprice(sql_feeprice);
		sql_price = sql_price==null?"0":sql_price;
		sql_price = sql_price.isEmpty()?"0":sql_price;
		bean.setPrices(sql_price);
		price = price==null?"0":price;
		price = price.isEmpty()?"0":price;
		bean.setPrice(price);
		bean.setPosttime(posttime);
		bean.setMethod(method);
		bean.setWprices(sql_wprice);
		System.out.println("price:"+price);
		System.out.println("prices:"+sql_price);
		System.out.println("sql_feeprice:"+sql_feeprice);
		System.out.println("sql_fprice:"+fprice);
		System.out.println("sql_posttime:"+posttime);
		System.out.println("sql_method:"+method);
		method = null;
		posttime = null;
		return bean;
	}
	
	
	/**汉英翻译
	 * @param ch
	 * @return
	 */
	private  String trant(String ch, ArrayList<CnEnDaoBean> query, int flag){
		if(ch==null||query==null){
			return "";
		}
		int query_num = query.size();
		ch = flag==0?replaceName(ch):ch;
		for(int i=0;i<query_num;i++){
			ch = ch.replace(query.get(i).getCntext(), " "+query.get(i).getEntext()+" ").trim();
		}
		ch = ch.replaceAll("（", "(").replaceAll("）", ")").replaceAll("［", "[")
				.replaceAll("！", "!").replaceAll("？", "?").replaceAll("］", "]")
				.replaceAll("＜", "<").replaceAll("？", "?").replaceAll("。", "")
				.replaceAll("：", ":").replaceAll("，", ",").replaceAll("、", "");
		ch = ch.replaceAll("[\\一-\\龥]+", " ").replaceAll("［］", "");
		if(flag==0){
			ch = ch.toLowerCase().trim().replaceAll("\\s*wish/*\\s*"," ").replaceAll("\\s*\\.*ebay/*\\.*\\s*"," ")
					.replaceAll("\\s*aliexpress/*\\s*"," ")
					.replaceAll("\\s*amozon/*\\s*"," ").replaceAll("\\s*amazon/*\\s*"," ").replaceAll("\\s*\\.*eaby/*\\.*\\s*"," ")
					.replaceAll("\\s*dhgate/*\\s*"," ").replaceAll("\\s*\\.*sbay/*\\.*\\s*"," ").replaceAll("\\s*\\.*eba/*\\.*\\s*"," ")
					.replaceAll("\\s*2015\\s*year\\s+"," ").replaceAll("\\s*2014\\s*year\\s+"," ").replaceAll("\\s*2015\\s+"," ")
					.replaceAll("\\s*2014\\s+", " ").replaceAll("\\s*15\\s*year\\s+", " ").replaceAll("\\s*2016\\s*year\\s+", " ").replaceAll("「.*」", " ").replaceAll("\\(.*\\)", " ").replaceAll("!", " ")
					.replaceAll("\\s*2013\\s+", " ")
					.replaceAll("\\s*2013\\s*year\\s+", " ")
					.replaceAll("\\[.*\\]", "")
					.replaceAll("\\s+", " ").trim();
		}else if(flag==1){//明细翻译去除品牌  零售价格
			String detail_parse = ch;
			if(detail_parse!=null&&!detail_parse.isEmpty()){
				detail_parse = detail_parse.replace("{", "").replace("}", "").trim();
				HashMap<String, String> detailList = new HashMap<String, String>();
				String[] details = detail_parse.split(",\\s+\\d+=");
				String detail_text = null;
				String[] tem_detail = null;
				int index = 0;
				String tem1 = null;
				String tem2 = null;
				for(int k=0;k<details.length;k++){
					detail_text = details[k].trim();
					if(!detail_text.isEmpty()){
						if(k==0&&detail_text.length()>3){
							detail_text = detail_text.substring(3);
						}
						tem_detail = detail_text.split(":");
						if(tem_detail.length>1){
							tem1 = tem_detail[0].replaceAll("\\s+", " ").trim();
							if(!"brand".equals(tem1.toLowerCase().trim())&&!Pattern.compile("(suggesting\\s*price)").matcher(tem1.toLowerCase()).find()){
								tem2 = tem_detail[1].replaceAll("\\s+", " ").trim();
								if((!tem1.isEmpty())&&(!tem2.isEmpty())){
									detail_text = tem1.substring(0,1).toUpperCase()+tem1.substring(1)
											+":"+ tem2.substring(0,1).toUpperCase()+tem2.substring(1);
									detailList.put(index+"",detail_text);
									index++;
								}
							}
						}
					}
				}
				if(detailList!=null){
					ch = detailList.toString();
				}
				details = null;
			}
		}
		return ch;
	}
	
	private String replaceName(String text){
		String[]  filerName = new String[]{
				"价格好商量、绝对全网最低,好评多多、绝对让亲们满意,能拍下都有大量现货哈,当天拍下拍下、当天发出",
				"何以笙箫默唐嫣赵默笙明星同款","国投一楼BF-档 - 时尚优品","女人街二楼C档 - 诗曼莎",
				"一手货源网络分销招代理","何.*以.*笙.*箫.*默","日本女装第一品牌润卡","国大楼-A档 - 如意纺",
				"普通代理100元保证金","金牌代理200元保证金","钻石代理300元保证金","交纳保证金联系客服","拍下提交后店主改价",
				"包退换实拍代发批发","绝对让亲们满意","能拍下都有大量现货哈","华伊阁杭派精品","日韩欧美外贸女装","绝对全网最低",
				"当天拍下拍下","当天发出","[高清印花 与 主图最像]","价格好商量","同款林志玲唐嫣","好评多多","（柚子外贸女装）",
				"微信代发实拍","依之韵女人秀","速卖通米兰站","雪儿·嘉年华","欧美品牌剪标","毛领孔雀夫人","华伊阁杭派","元源制衣厂",
				"衣贝子服饰厂","依美汇制衣厂","素裂帛鸣旗舰店","格格恋杭派精品","江南红雪儿",
				"宝华 三楼 -A档","侧缝中国制造标","不接爱中差评","厂家直批原创","衣桌尔壹号店","依唯雅.莎露","柚子外贸女装",
				"布谷村原创","国民女神高圆圆","宇福新思路","爱琴海网络","艾美斯汤迪","中国好声音","包实拍代发","美时尚女裤",
				"美时尚女款","[广周服饰]","[不退不换]","[芳菲小镇]","淘呀淘商城","天使之佳话","衣优丶衣秀","娅丽丹妮娜",
				"随风 Denim","菲悦 · 东方 ","雨田靓衣坊","乖乖实拍店","火爆靓佳人","韩依艾俪®","莉泽.贝妮","莉泽。贝妮",
				"俊蓉好心情","星空下的猫","聚美依坊","江南诗语","丰衣主色","小香官网","风领都秀","朵朵精品","风姿秀衣","风姿秀逸",
				"沣涵菓缘","凤盈好彩","概念色彩","雪悦菲杨","雅柏莉思","雅静靓衣","雅兰诗芸","娅柏莉思","娅丽丹年","姚家铺子",
				"一田设计","衣衫情缘","（假一赔十）","假一赔十","衣尚尽美","衣香满经","衣路相随","芳菲小镇","雅酷衣衣","亦酷亦雅",
				"诚招代理","一件代发","辛唐米娜","蓝色倾情","杰西&莱","素裂帛鸣","当天发货","拍下改价","厂家直批","厂家批发",
				"韩都部落","出彩四季","印象女人","滢赢e站","优美衣然","怪物大学","优品伧库","伊藤千佳","依雪佳儿","依韵女秀",
				"蜜诗曼迪","木槿花开","羽娜伊莎","衣阁里拉","自由呼吸","柚子美衣","拉夏贝尔","旗下品牌","姿色·凡","依瑄贝妮",
				"德利华茵","迪曼缔丝","非常物语","衣博服饰","原创街拍","贰零衣爱","环朱哥哥","卡拉迪克","迪尼格瑞","真强菲儿",
				"芷墨服饰","质尚衣阁","中瑞尚品","圆梦天使","雨儿飘飘","雨林一漫","年金玉林","春妮公主","贝林皇后","米可芭娜",
				"[试卖]","辛德瑞拉","沐沐胡同","裙角飞扬","叮当制造","林依雅妮","厂家供货","丽人一身","艾雅壹诺","柚子商城",
				"自主品牌","潮搭坊】","衣香满经","衣满经经","衣路相随","衣阁里拉","孕涵妈咪","真强菲尔","巴拉维那","萧瑶天姿",
				"英子佳俊","亿莱尼雅","莎度勋浠","圆梦蒂娇","致帛尤品","伊曼迪儿","伊贝莎丽","阳光元素","爱俐思娜","贝尔丝丹",
				"馨妮贝尔","纤衣楚楚","水墨佳人","秋水青阁","倾城依轩","金荣世界","韩都衣库","菲悦东方","百斯特诺","波西塔诺",
				"丹琦迪子","韩社衣讯","华煜靓依","江南雨季","豪族女郎","雅兰诗芸","依妃尼迪","益赢卓啦","贝蕾酷儿","城市衣柜",
				"甜蜜女孩","薇薇新依","温婉依人","西子湖畔","纤姿秀逸","欣欣衫魅","素笔时尚","最美年华","魅莎丽影","思俪兰卡",
				"欣缘木子","鑫冉久久","星梦同珍","雄鹰战士","皇家爱衣","蔷薇衣语","万人之裳","侨顿牧歌","秋水青阁","秋水伊人",
				"莎度勋浠","圣雪佳人","盛达莱妮","时尚潮牌","水彩飞雪","丝蒂雅文","荣盛之星","塞外凤凰","三标齐全","皮尔仙妮",
				"难舍钰依","妮可伊伊","欧妮思曼","欧尼思曼","欧莎俪影","盼盼驿站","千琪娇美","东孜先森","千叶贝贝","魅力女王",
				"玛丽公馆","魅力小雪","曼琳秋伊","萌诺衣族","美超秀彩","曼诗娅菲","美人依就","梦特奈尔","咪咪笛笛","米可芭娜",
				"慕菲伊人","玛丽匡特","玛丝爱汀","凌家小妹","龙腾时尚","洛诗欧妮","蜜淘香衣","蜜套香衣","摩登丛林","卡拉迪克",
				"兰蔻卓雅","自由天使","浪漫空间","丽莎迪露","丽新时尚","【.*】","可儿俏娃","批发C区","可可二店","酷艺辉扬",
				"俪亨国际","恋上婉约","恋尚婉约","海牛凤仪","韩芭比乐","韩创服饰","拉夏贝尔","艾欧严选","韩美纤衣","红火丹妮",
				"慧川卉美","几何部落","金梦娜菲","佳韵惠美","锦华秀逸","君品衣人","恒伴服装","花之里予","韩国品牌","丽迪雅",
				"佳璐贝尔","静洁依秀","加速时尚","拉夏家的","米可巴娜","卡迪娇娃","简约佳人","诚招微信","京东一布","靓一代",
				"卡丹妮","卡丹尼","金丰色","金斯曼","金玉林","金慧乔","杰茜莱","杰依曼","嘉依贝诺","金帝卡蒙","九爱伊田",
				"晶雪儿","聚宝绒","吉之朗","极企鹅","季诗雨","卡伦曼","卡司弗","佳豪欣","假日猫","娇莉莎","丽姿奴","靓得好",
				"简如衣","朗曼姿","蓝熙绮","微信\\d+","慧姿阁","金多多","骏马王","蓝黛门","酷百度","酷裙坊","酷衣族","招微商",
				"珞咖鹭","落丝雨","海马城","麦酷酷","包代发","蛮女孩","曼秋伊","幕帛莎","莱妮茵","玛伊蔓","幕泊莎","亮雄丽",
				"妙衣阁","美杰斯","梦之雪","蒙戈莱","欧维斯","蒙莎特","梦佳佳","美之芝","莱斯顿","牵动力","钱衣族","卡轩伊",
				"派啦帝","欧炫雅","欧美人","欧蔓莎","欧百惠","欧迪妮","欧妃美","素配雅","茜可可","千利红","千绿湖","卡西玫",
				"欧斐雅","杰西家","欧格菲","妮蕾迪","曼娅奴","女人街","水边缘","卡玛娅","葡萄家","欧依度","欧喆娅","冰雅纯",
				"色女郎","三沫沫","三叶草","赛雨丽","麦昆羊","十三行","摩安珂","三木记","舒雪儿","诗雨阁","盛维斯","狂牛仕",
				"萨利文","艳丽达", "柏尓果","菁菁秀","衣神家","贝勒川","雨纯秀","金大地","诗柏妮","诗恩娜","圣雅姿","珂曼莎",
				"贵丽人","鑫兴阁","四季青","假日猫","大东门","雪凌美","羊咩咩","绒丝奴","深依度","圣可诗","鑫菲露","蕾切尔",
				"如梦令","情湘愿","情依族","秋惠依","皇梓浩","轩衣林","雪冰绒","馨乃馨","鑫菲妍","高圆圆","巧曼语","韩袭人",
				"孝之恩","吸引力","吾维依","薇西妮","薇伊嘉","唯丽绚","太平鸟","淘裤吧","新巴黎","欣怡阁","小淑女","可尔奇",
				"淘香衣","淘衣酷","威伦蒂","图巴拉","薇尔登","加哩果","珍黎诗","知名度","小叶子", "禾尔美","恒雪龙","邦森娜",
				"钰芙思","依之御","依纳婷","衣之秀","卓霖秀","雨尔丽","优意诗","伊漫莎","好依典","好羽菲","赵黙笙","堡狮龙",
				"轩衣林","奥戴尔","维尔斯","二千家","阿朵家","冰晶族","百家好","潮搭坊","赵丽颖","招微商","招待你","碧诱狐",
				"芙莉情","尤曼丝","衣之吻","衣之韵","依贝妍","依韵儿","依杰斯","依迪诗","傲.尚","澳兰雪","佰思汇","春枝俏",
				"依贵族","宜织红","意心兰","罗志祥 ","东岂邦","尚贵华","柏图利","莎郎海","柏芙澜","帛泊莎","裁缝佬","巴宝莉",
				"家爱衣","众宇琦","追梦鸟","桌霖秀","卓品依","樟枝怡","月月约","云世尚","碧佳纯","玉织秀","雨思佳","衣加依",
				"艳丽达","羽诗琪","雨纯秀","诱导力","拍下改","招代理","陈梓童","小琳儿","姗姗贝","川尚楹","川裳楹","玉织绣",
				"衣加衣","富妈咪 ","不退换","布谷村","蓝波薇","美颜子","民族韵","伊可人","川秀楹","东城淘","丹妮宇","林志玲",
				"哎呦嘿","都拉米","随意门","家爱衣","衣丽奇","衣名升","伊尚纤","伊思萌","丹妮兰","蒂文特","蝶贝儿","杰西莱",
				"伊韵儿","衣贝子","伊菲特","伊乐雅","伊丽娜","伊莉娜","伊娜妃","野酷魅","村上春","大莉德","袋鼠兜","奥代尔",
				"胭脂扣","吆呵女","雅莉谷","雅衣堂","雪思盾","轩衣林","小琳儿","四季缘","朵兰春","凡维得","凡依卉","云之狄",
				"好丰采","天瑞卡","奥黛丽","倩朵娜","千绿湖","靓衣淘","莎菲儿","绣芙绒","范特希","朵维思","东林狼","豪都风",
				"西子漾","午夜花","尚可儿","咎三行","少女坊","昕宇心","雅琪娜","雅思梦","菲曼琪","菲裳好","老照片","朵莱莉",
				"雅特诺","雅丽雅","雪花玲","雪伦诗","雪天使","雪羽格","莹欣儿","蜓中蜓","菲思雨","迪笛欧","枫之玲","欧佰利",
				"玉织秀","潮流坊","嫒俏裳","艾蜜爾","爱卫儿","牵引力","蕴诗坊","奥可丹","高米缔","戈诗琪","戈詩琪","衣之恋",
				"野·井","艾思妮","艾思琪","艾依阁","爱淘家","艾億得","梵轩格","艾安琪","歌莉娅","歌依坊","第六屏","百年好",
				"奥诺丝","安博尔","安妮娅","芭迪欧","哈丽秀","百卡鸟","格调坊","爱如依","格丹蒙","欧时力","欧尚奈","艾珍妮",
				"红伊纯","旗舰店","花园街","华之韵","周扬清","平安园","大博金","百家好","韩武纪","风雨花","枫之奴","韩小姐",
				"杨子珊","杨丞琳","周冬雨","周笔畅","韩国馆","格外红","格姿廊","古铭洋","贵妃子","韩酷儿","欧洲站","韩魅儿",
				"韩雅轩","韩衣秀","好妈咪","好尼奥","田木澜","张慧雯","逗当家","丹尼宇","嘉莉华","潮 搭坊","\\[\\]","卉希",
				"斓蝶","海秋","涵绮","涵旭","韩都","韩序","合美","荷莎","恒泰","华誉","服饰","画形","惠儿","周迅","谷度",
				"佳莱","简伊","姣薇","嬌崙","菁品","精品","静柔","绢纺","珺怡","米羞","名仕","陌阡","目善","国大","成功",
				"乐乐","李李","李宁","曼莎","美涵","美美","辰子","媚唇","蒙戈","卡多","兰开","兰诺","蓝丘","初语","大雅",
				"娜美","耐克","欧韩","欧美","欧一","蓬歌 ","骐谷","旗下","轻素","慕悦","衣乐","穆南","邦笛","寻美","潮流",
				"瑞丽","润杨","三有","尚品","盛隆","诗荣","竖笔","双琴","丝敏","纳兰","平派","爱唯","黛纹","衣源","雅芙",
				"思思","苏杭","天逸","通牌","外贸","微兆","维娓 ","希晨","熙熙","图腾","素笔","凯儿","伊人","服饰","意诗",
				"仙子","小儿","新颖","鑫海","秀丽","泫雅","雅利","一族","伊尚","主调","騏谷","蓬歌","之家","玉兰","盈+",
				"衣优","衣秀","衣源","依彩","亿发","弈丽","羿丽","樱丰","海澜","博航","布厘","帝琪","朵而","莲伦","旎月",
				"应季","迂墨","雨蝶","禹研","唐嫣","原单","嘉美","远晨","早市","鸿海","皇牌","集团","浪莎","范秀","飞飞",
				"着西","微信","真的","卓秀","姿丹","杨幂","秭又","紫凌","电话","苏玛","鑫磊","达蒂","秀丽","朵朵","朵客",
				"孙俪","包邮","批发","同款","珈美","代理","免费","蓬歌","野井","独秀","艾格","艾凸","上市","杨幂","上式",
				"雨兮","唛头","佳钰","姣薇","艾欧","奢点","益青","颁奖","古妞","翱翔","奥雅","百优","冰冰","禾森","圆通",
				"孝恩","鑫蕊","精品","微兆","苏玛","丝蔓","泫雅","薇馨","海锦","德兴","地素","官网","多咪","阁绫","阁綾",
				"裂帛","梦凯","奋斗","云阁","娇媛","简爱","欧家","西宽","范*哲","傲徒","兰蔻","一手", "佳钰","非常","风韵",
				"时力","钰佳","杰西","拉家","城画","奥文", "嘉美","菁菁","郑爽","热卖","衣舍","扬基","品尚","楚","京","欣",
				"鑫","皇","鸿","伊","密","琳","富","厂家","家","【","DH","【",
		};
		String[]  replace_str = new String[]{
				"礼包臀:包臀","复股:复古","礼蓬蓬裙:蓬蓬裙","礼连衣裙:连衣裙",
		};
		if(text!=null){
			if(text.subSequence(0, 1).equals("年")){
				text  =text.substring(1);
			}
			int filerName_num = filerName.length;
			for(int i=0;i<filerName_num;i++){
				text = text.replaceAll(filerName[i], "");
			}
			int replace_str_num = replace_str.length;
			String[] split = null;
			for(int i=0;i<replace_str_num;i++){
				split = replace_str[i].split(":");
				text = text.replace(split[0], split[1]);
			}
		}
		return text;
	}
}
