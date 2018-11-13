package com.cbt.parse.service;

import com.cbt.parse.dao.CatidFilterDao;
import com.cbt.parse.dao.FilterDataDao;
import com.cbt.parse.dao.IntensveDao;
import com.cbt.parse.dao.KeysPageDao;
import com.cbt.parse.daoimp.ICatidFilterDao;
import com.cbt.parse.daoimp.IFilterDataDao;
import com.cbt.parse.daoimp.IKeysPageDao;

import java.util.ArrayList;
import java.util.HashMap;

public class GetFilterUtils {
	private static ICatidFilterDao catidFilter_dao = new CatidFilterDao();
	private static IntensveDao intensve_dao = new IntensveDao();
	
	/**指定关键词搜索进入二级静态页面
	 * @param keywords
	 * @return
	 */
	public static String pageFilter(String keywords){
		if(keywords!=null&&!keywords.isEmpty()){
			IKeysPageDao pd = new KeysPageDao();
			String pageurl = pd.querry(keywords);
			return pageurl;
		}
		return null;
	}
	
	/**类别过滤（指定的类别选择）(例如指定类别进入1688搜索)
	 * @param catid
	 * @param type
	 * @return
	 */
	public static boolean  catidFilter(String catid){
		Boolean  result = false;
		if(catid!=null&&!catid.isEmpty()){
			String list = catidFilter_dao.queryCidFilter(catid);
			if(list!=null){
				result = true;
			}
		}
		return result;
	}
	/**类别过滤
	 * catid的过滤类型 
	 * 1-为指定类别搜索跳转到1688； 
	 * 2-为指定类别默认重量；
	 * 3-为既跳转1688又指定重量；
	 * 4-为指定类别设定最小价格用于价格排序；
	 * 5-关键词过滤到1688；
	 * 6-指定类别销量限定；
	 * 7-指定类别跳转aliexpress240；
	 * 8-缓存搜索数据
	 * @param catid
	 * @param type
	 * @return
	 */
	public static boolean  catidFilter(String catid,int type){
		if(catid!=null&&!catid.isEmpty()&&catidFilter_dao.getCatidFilter(catid, type)>0){
			return true;
		}
		return false;
	}
	
	/**关键词过滤（指定的关键词选择）(例如指定关键词进入1688搜索)
	 * @param catid
	 * @param type
	 * @return
	 */
	public static boolean  keywordFilter(String keyword,String catid){
		if(catid!=null&&"0".equals(catid)){
			if(catidFilter_dao.queryKeyWordsFilter(catid, keyword)>0){
				return true;
			}
		}
		return false;
	}
	
	/**指定类别价格排序最小价格
	 * @param catid
	 * @return
	 */
	public static String priceFilter(String catid){
		if(catid!=null){
			String list = catidFilter_dao.queryPriceFilter(catid);
			return list==null||list.isEmpty()?"":list;
		}
		return "";
	}
	/**指定类别销量限定值
	 * @param catid
	 * @return
	 */
	public static int soldFilter(String catid){
		if(catid==null||catid.isEmpty()||"0".equals(catid)){
			return 1;
		}
		return catidFilter_dao.querySoldFilter(catid);
	}
	
	/**过滤指定的商店链接（即屏蔽商店商品）
	 * @param url
	 * @return
	 */
	public static boolean  storeFilter(String url){
		if(url!=null){
			IFilterDataDao dao = new FilterDataDao();
			if(dao.getStoreFilter(url)>0){
				return false;
			}
		}
		return true;
	}
	
	/**过滤某一类商品
	 * @param goods
	 * @return
	 */
	public static GoodsBean goodsFilter(GoodsBean goods){
		if(goods!=null&&!goods.isEmpty()&&goods.getCid()==7){
			IntensveDao id = new IntensveDao();
			ArrayList<HashMap<String, String>> list = id.getCatidFilters();
			String catid = null;
			String words = null;
			String catid1 = goods.getCatid1();
			String catid2 = goods.getCatid2();
			String catid3 = goods.getCatid3();
			String catid4 = goods.getCatid4();
			String catid5 = goods.getCatid5();
			String catid6 = goods.getCatid6();
			for(int i=0;i<list.size();i++){
				words = list.get(i).get("word");
				if(words==null||words.isEmpty()){
					catid = list.get(i).get("catid");
					if((catid1!=null&&catid.equals(catid1))||
					   (catid2!=null&&catid.equals(catid2))||
					   (catid3!=null&&catid.equals(catid3))||
					   (catid4!=null&&catid.equals(catid4))||
					   (catid5!=null&&catid.equals(catid5))||
					   (catid6!=null&&catid.equals(catid6)))
					{
						goods.setValid(4);
						break;
					}
				}
			}
		}
		return goods;
	}
	
	
	/**屏蔽指定类别
	 * @param catid
	 * @return
	 */
	public static boolean  getCatidFilter(String catid){
		if(catid!=null&&!catid.isEmpty()){
			if(intensve_dao.getCatidFilter(catid)!=0){
				return false;
			}
		}
		return true;
	}

}
