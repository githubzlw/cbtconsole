package com.cbt.parse.driver;

import com.cbt.parse.bean.*;
import com.cbt.parse.dao.*;
import com.cbt.parse.daoimp.*;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.SearchUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**管理本地数据库商品数据搜索
 * @author abc
 *
 */
public class LocalSDriver {
	private DecimalFormat format = new DecimalFormat("#0.00");
	
	
	public static boolean filterHotWord(String hot_word){
		if(hot_word!=null){
			hot_word  = hot_word.replaceAll("(\\++)|(\\s+)|((%20)+)", " ").toLowerCase();
			IHotWordDao dao = new HotWordDao();
			return dao.queryExsis(hot_word)>0;
		}
		return false;
	}
	
	
	/**热搜词集合商品
	 * @return
	 */
	public ArrayList<SearchGoods> hotWordsSearchDriver(String hot_words){
		ArrayList<SearchGoods> list_hot = new ArrayList<SearchGoods>();
		hot_words  = hot_words.replaceAll("(\\++)|(\\s+)|((%20)+)", " ");
		SearchGoods bean = null;
		HotWordBean hot = null;
		int morder = 0;
		Double minprice = 0.0;
		Double maxprice = 0.0;
		ISpiderServer spider = new SpiderServer();
		Map<String, Double> maphl = spider.getExchangeRate();
		Double rmb = 1.0;
		IHotWordDao dao = new HotWordDao();
		ArrayList<HotWordBean> list = dao.querySearch(hot_words);
		int list_size = list.size();
		Random random = new Random();
		int index = list_size-20>0?random.nextInt(list_size-20):0;
		random = null;
		for(int i=index;i<list_size;i++){
			hot = list.get(i);
			if(hot.getName()!=null&&hot.getValid()==1){
				bean= new SearchGoods();
				bean.setGoods_image(hot.getImg());
				morder = hot.getMorder();
				if(morder>1){
					bean.setGoods_minOrder(morder+" "+hot.getGunit()+"s");
				}else{
					bean.setGoods_minOrder(morder+" "+hot.getGunit());
				}
				bean.setGoods_name(SearchUtils.nameVert(hot.getName(), 26));
				minprice = hot.getMinprice();
				maxprice = hot.getMaxprice();
				if("RMB".equals(hot.getPunit())){
					rmb = maphl.get("RMB");
				}else{
					rmb = 1.0;
				}
				if(maxprice!=0){
					bean.setGoods_price(format.format(minprice/rmb)+"-"+format.format(maxprice/rmb));
				}else{
					bean.setGoods_price(format.format(minprice/rmb));
				}
				bean.setGoods_url(TypeUtils.encodeGoods(hot.getUrl().replace(".htm", "/localyiwu.htm")));
				list_hot.add(bean);
				bean = null;
				hot = null;
				minprice = 0.0;
				maxprice = 0.0;
				morder = 0;
			}
		}
		return list_hot;
	}
	/**googs_expand_ex.sql集合商品
	 * @return
	 */
	public ArrayList<SearchGoods> searchDriver(String keyword, String minprice, String maxprice, String minq, String maxq, String page, String srt, String catid, String pvid){
		ArrayList<SearchGoods> list_search = new ArrayList<SearchGoods>();
		if(keyword!=null&&!keyword.isEmpty()){
			IGoodsExpandDao dao = new GoodsExpandDao();
			ParamBean param = new ParamBean();
			param.setKeyword(keyword);
			param.setMinq(minq);
			param.setMaxq(maxq);
			param.setMinprice(minprice);
			param.setMaxprice(maxprice);
			param.setSort(srt);
			param.setCatid(catid);
			param.setPvid(pvid);
			ArrayList<SqlBean> sql = SearchEngine.pate(param, null);
			ArrayList<GoodsExpandBean> list = dao.querySearch(sql);
			int list_num = list.size();
			SearchGoods sg = null;
			GoodsExpandBean gb = null;
			double minprice2 = 0.0;
			double maxprice2 = 0.0;
			String price = null;
			Double rmb = 1.0;
			int total = 1;
			if(list!=null&&!list.isEmpty()){
				total = list.get(0).getTotal();
				total = total%40==0?total/40:total/40+1;
				total= total>263?263:total;
			}
			String morder = null;
			for(int i=0;i<list_num;i++){
				gb = list.get(i);
				sg = new SearchGoods();
				sg.setGoods_name(SearchUtils.nameVert(gb.getName(), 26));
				sg.setGoods_url(TypeUtils.encodeGoods(gb.getUrl()));
				minprice2 = gb.getMinprice();
				maxprice2 = gb.getMaxprice();
				DecimalFormat format = new DecimalFormat("#0.00");
				if("RMB".equals(gb.getPunit())){
				    ISpiderServer spider = new SpiderServer();
				    Map<String, Double> maphl = spider.getExchangeRate();
				    rmb = maphl.get("RMB");
				    minprice2 = minprice2/rmb;
				    maxprice2 = maxprice2/rmb;
				}
				if(minprice2!=0){
					price = format.format(minprice2);
					if(maxprice2!=0){
						price = price+"-"+format.format(maxprice2);
					}
				}
				sg.setGoods_price(price);
				sg.setGoods_image(gb.getImg());
				int morders = gb.getMorder();
				morder = morders+" "+gb.getGunit();
				morder = morders>1? morder+"s":morder;
				sg.setGoods_minOrder(morder);
				sg.setGoods_solder(gb.getSell()+"");
				sg.setKey_type("goods");
				list_search.add(sg);
				gb = null;
				sg = null;
				price = null;
				minprice2 = 0.0;
				maxprice2 = 0.0;
			}
			if(list_search!=null&&!list_search.isEmpty()){
				param.setAmount(total);
				param.setCurrent(Integer.valueOf(page));
				param.setWebsite("a");
				param.setCom("goodstest");
				ArrayList<SearchGoods> page_list = SearchEngine.page(param);
				list_search.addAll(page_list);
			}
			ArrayList<SearchGoods> cat = category(keyword, catid,pvid);
			list_search.addAll(cat);
		}
		return list_search;
	}
	
	private ArrayList<SearchGoods> category(String keyword, String catid, String pvid){
		ArrayList<SearchGoods> list_search = new ArrayList<SearchGoods>();
		ICatPvdDao dao = new CatPvdDao();
		ArrayList<CatPvdBean> list = dao.query(keyword, catid);
		if(list!=null&&!list.isEmpty()){
			String catids = list.get(0).getCatidlist();
			String pvids = list.get(0).getPvidlist();
			SearchGoods sg = null;
			//类别
			if(catids!=null&&!catids.isEmpty()){
				IAliCategoryDao cate = new AliCategoryDao();
				String[] catis_s = catids.split(",");
				String catid_name = null;
				String catid_id = null;
				for(int i=0;i<catis_s.length;i++){
					catid_id = catis_s[i];
					if(!catid_id.isEmpty()){
						catid_name = cate.getCname(catid_id);
						if(catid_name!=null&&!catid_name.isEmpty()){
							sg = new SearchGoods();
							sg.setKey_type("Related Categories");
							sg.setKey_name(catid_name);
							if(!catid_id.equals(catid)){
								sg.setKey_url("&catid="+catid_id);
							}else{
								System.out.println("--------");
								sg.setKey_url("&catid="+catid_id+"&k0=id&k1=ov&k2=vo&k3=di");
							}
							list_search.add(sg);
							sg= null;
						}
						catid_name = null;
					}
					catid_id = null;
				}
				catid_name = cate.getCname(catid);
				if(catid_name!=null&&!catid_name.isEmpty()){
					sg = new SearchGoods();
					sg.setKey_type("Related Categories");
					sg.setKey_name(catid_name);
					sg.setKey_url("&k0=id&k1=ov&k2=vo&k3=di");
					list_search.add(sg);
					sg= null;
				}
				catid_name = null;
			}
			
			//类型
			if(pvids!=null&&!pvids.isEmpty()){
				IPvidDao pvidao = new PvidDao();
				String[] pvids_s = pvids.split(",");
				ArrayList<PvidBean> pvid_name = null;
				String pvid_id = null;
				int pvids_s_length = pvids_s.length;
				String type_name = null;
				if(pvid!=null&&"0".equals(pvid)){
					pvid = null;
				}
				//已经选取的pvid
				ArrayList<String> type_id = new ArrayList<String>();
				if(pvid!=null&&!pvid.isEmpty()){
					String[] pvid_s = pvid.split(",");
					int pvid_s_length = pvid_s.length;
					String pvid_s_tem  =null;
					for(int j=0;j<pvid_s_length;j++){
						pvid_s_tem = pvid_s[j];
						if(!pvid_s_tem.isEmpty()){
							pvid_name = pvidao.query(pvid_s_tem);
							if(pvid_name!=null&&!pvid_name.isEmpty()){
								type_name = pvid_name.get(0).getName();
								if(type_name!=null&&!type_id.contains(type_name)){
									type_id.add(type_name);
								}
								if(j==0){
									sg = new SearchGoods();
									sg.setKey_type("Active Filters");
									sg.setKey_name("Clear All");
									sg.setKey_url("");
									list_search.add(sg);
									sg= null;
								}
								sg = new SearchGoods();
								sg.setKey_type(type_name);
								sg.setKey_name(pvid_name.get(0).getValue()+"&nbsp;&nbsp;X");
								pvid_s_tem = pvid.replace(pvid_s_tem, "").replaceAll(",+", ",");
								int pvid_s_tem_length = pvid_s_tem.length();
								if(pvid_s_tem_length>1&&pvid_s_tem.substring(0,1).equals(",")){
									pvid_s_tem = pvid_s_tem.substring(1);
								}
								if(pvid_s_tem_length>1&&pvid_s_tem.substring(pvid_s_tem_length-1).equals(",")){
									pvid_s_tem = pvid_s_tem.substring(0,pvid_s_tem_length-1);
								}
								pvid_s_tem = pvid_s_tem.isEmpty()?"0":pvid_s_tem;
								sg.setKey_url("&catid="+catid+"&pid="+pvid_s_tem);
								sg.setKey_img(pvid_name.get(0).getImg());
								list_search.add(sg);
								sg= null;
							}
							pvid_s_tem = null;
							pvid_name = null;
							type_name = null;
						}
					}
				}
				ArrayList<String> type = new ArrayList<String>();
				for(int i=0;i<pvids_s_length;i++){
					pvid_id = pvids_s[i];
					if(!pvid_id.isEmpty()){
						pvid_name = pvidao.query(pvid_id);
						if(pvid_name!=null&&!pvid_name.isEmpty()){
							type_name = pvid_name.get(0).getName();
							if(!type_id.contains(type_name)){
								if(type_name!=null&&type.size()<10&&!type.contains(type_name)){
									type.add(type_name);
								}
								if(type.size()>9&&!type.contains(type_name)){
									break;
								}else{
									sg = new SearchGoods();
									sg.setKey_type(type_name);
									sg.setKey_name(pvid_name.get(0).getValue());
									if(pvid!=null&&!pvid.isEmpty()){
										sg.setKey_url("&catid="+catid+"&pid="+pvid+","+pvid_id);
									}else{
										sg.setKey_url("&catid="+catid+"&pid="+pvid_id);
									}
									sg.setKey_img(pvid_name.get(0).getImg());
									list_search.add(sg);
									sg= null;
								}
							}
						}
					}
				}
				type = null;
				type_id = null;
			}
		}
		return list_search;
	}
	
	
	/**热搜词商品单页搜索
	 * @param hot_words
	 * @return
	 */
	public GoodsBean hotWordsGoodsDriver(String url, String pid){
		GoodsBean goods_bean = null;
		IHotWordDao dao = new HotWordDao();
		HotWordBean hot_bean = dao.queryGoods(url, pid);
		if(hot_bean!=null&&hot_bean.getValid()==1){
			goods_bean = new GoodsBean();
			goods_bean.setpUrl(hot_bean.getUrl().replace(".htm", "/localyiwu.htm"));
			goods_bean.setpName(hot_bean.getName());
			String info = hot_bean.getInfo();
			if(info!=null){
				List<String> list = DownloadMain.getSpiderContextList1( "(?:href=\")(.*?)(?:\")",info);
				for(int i=0;i<list.size();i++){
					info = info.replace(list.get(i), "");
					info = info.replace("href=\"\"", "");
				}
				goods_bean.setInfo_ori(info);
			}
			int morder = hot_bean.getMorder();
			String pPriceUnit = hot_bean.getPunit();
			String pGoodsUnit = hot_bean.getGunit();
			//图片集合
			String img =  hot_bean.getImg();
			if(img!=null&&!img.isEmpty()){
				img = img.replace("[", "").replace("]", "").trim();
				String[] imgs = img.split(",\\s+");
				ArrayList<String> imgList = new ArrayList<String>();
				for(int i=0;i<imgs.length;i++){
					if(!imgs[i].isEmpty()){
						imgList.add(imgs[i]);
					}
				}
				if(imgList!=null&&!imgList.isEmpty()){
					goods_bean.setpImage(imgList);
				}
				imgList = null;
			}
			img = null;
			//批发价格
			String wprice = hot_bean.getWprice();
			if(wprice!=null&&!wprice.isEmpty()){
				wprice = wprice.replace("[", "").replace("]", "").trim();
				String[] prices = wprice.split(",\\s+");
				ArrayList<String> wList = new ArrayList<String>();
				for(int i=0;i<prices.length;i++){
					if(!prices[i].isEmpty()){
						wList.add(prices[i]);
					}
				}
				if(wList!=null&&!wList.isEmpty()){
					goods_bean.setpWprice(wList);
				}
				wList = null;
			}
			wprice  =null;
			goods_bean.setMinOrder(morder+"");
			goods_bean.setpPriceUnit(pPriceUnit);
			goods_bean.setpGoodsUnit(pGoodsUnit);
			goods_bean.setpID(hot_bean.getPid());
			goods_bean.setWeight(hot_bean.getWeight());
			goods_bean.setPerWeight(hot_bean.getWeight());
		}
		return goods_bean;
	}
	

}
