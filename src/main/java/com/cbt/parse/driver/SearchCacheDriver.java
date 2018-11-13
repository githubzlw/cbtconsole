package com.cbt.parse.driver;

import com.cbt.parse.bean.ParamBean;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.dao.AliSearchCache;
import com.cbt.parse.daoimp.IAliSearchCache;
import com.cbt.parse.service.*;
import com.cbt.parse.thread.CacheThread;
import com.cbt.util.Cache;
import com.cbt.util.Utility;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**	对于 主板，手机等等特定类别， 我们 用人工挑选的 方式 （小尹刚做好的）。  但是 增加一个 选项：  不显示我们没筛选的产品。 
 * 														
	对于 服装，饰品，珠宝  都只显示销售量 超过 20 的 产品 (具体数字会按类别而不同）	
														
	搜索时 先检查这个搜索 我们缓存过没有， 和原来的 区别是  这个 缓存结果 是 从数据库中 来的	
		这个缓存结果里面 有 根据 销量  和 是否 有 silimar product 而做 过滤		
		这个 “搜索结果缓存” 在 不断 增加的 同时，每周自动到 aliexpress 处进行更新	
														
		如果 这个 搜索 以前没缓存过， 当场从  AliExpress 抓取，并过滤 和形成页面。  													
		一旦启动缓存，就在 把 后面 3页的内容都进行 分析和缓存。不要等用户点击
					
		假设缓存了 4页的产品，最后筛选下来的 只有 2页半 就不再提供其他产品。 （将来这里会接上 1688的产品）
						
			按销量排序的 情况下， 可以 每下载一页就分析一页，如果 这一页 的商品已经不满足要求了，就不用缓存下一页了。
			
			按 “价格 和 best match” 的 情况下， 可以 一下子 缓存 3页，并且在 客户点击第2页时，自动再多缓存3页，以此类推（具体一次缓存多少页，我没仔细考虑，你们定）
		
		保存的 搜索结果中 还要 记录此 商店是否 旺旺在线，如果 一个商品 旺旺不在线，那么 展示她 需要 的 销量是  最小值*2	
														
		保存的 搜索结果中 还要 记录此 商店是否 有 相似款 （搜索时用 group similar product），如果 相似款>2，那么 即使 销量不达标, 也可以展示													
		
		在搜索结果 中，如果 有商品属于我们以前发现 找不到的产品，就自动不显示		产品黑名单					
		在搜索结果 中，如果 有商品属于我们 觉得有问题的 卖家，就自动不显示			卖家黑名单
							
 * @author abc   2016-2-24
 *
 */
public class SearchCacheDriver {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SearchCacheDriver.class);
	private static IAliSearchCache param_dao = new AliSearchCache();
	
	/**缓存读取数据，若缓存中没有，则后台启动缓存保存
	 * @date 2016年3月1日
	 * @author abc
	 * @param keyword
	 * @param catid
	 * @param sort
	 * @param page
	 * @return  
	 */
	public ArrayList<SearchGoods> saveSearchCache(String keyword,String catid,String str_sort,String page){
		int sold = GetFilterUtils.soldFilter(catid);
		if(sold==0){
			return null;
		}
		//(2).指定类别进入销量限定方式获取商品集合
		try {
			ArrayList<SearchGoods> list = null;
			int int_sort = "order-desc".equals(str_sort)?3:0;
			int_sort = "bbPrice-asc".equals(str_sort)?1:int_sort;
			int paramId = param_dao.getParamId(keyword, catid, int_sort);//检查缓存
			if(paramId==0){ //(2.1)不存在缓存，即走aliexpress，并后台启动数据分析
				new CacheThread(keyword, catid,str_sort,paramId,Integer.parseInt(page),sold).start();
				System.out.println("---------1111------------------------");
				return null;
			}
			//存在缓存,中则从本地获取数据集合
			ArrayList<SearchGoods> datas = param_dao.getDatas(String.valueOf(paramId), int_sort, sold, page);
			int datas_size = datas.size();
			int amount = datas_size>0?datas.get(0).getTotal():0;
			list = new ArrayList<SearchGoods>();
			SearchGoods  goods = null;
			for(int i=0;i<datas_size;i++){
				SearchGoods map = datas.get(i);
				goods = new SearchGoods();
				int free = Integer.parseInt(Utility.formatObject(map.getGoods_free()));
				if(free==1){
					goods.setGoods_free("Free Shipping");
					goods.setGoods_url("&s=y"+TypeUtils.encodeGoods(Utility.formatObject(map.getGoods_url())));
				}else{
					goods.setGoods_free("");
					goods.setGoods_url(TypeUtils.encodeGoods(Utility.formatObject(map.getGoods_url())));
				}
				goods.setGoods_solder(Utility.formatObject(map.getGoods_solder()));
				goods.setGoods_price(Utility.formatObject(map.getGoods_price()));
				goods.setGoods_name(SearchUtils.nameVert(Utility.formatObject(map.getGoods_name()), 26));
				goods.setGoods_minOrder(Utility.formatObject(map.getGoods_minOrder()));
				goods.setGoods_image(Utility.formatObject(map.getGoods_image()));
				goods.setKey_type("goods");
				list.add(goods);
			}
			//页码  类别
			if(!list.isEmpty()){
				ParamBean bean = new ParamBean();
				bean.setKeyword(keyword);
				bean.setSort(str_sort);
				bean.setCatid(catid);
				amount = amount%40==0?amount/40:amount/40+1;
				bean.setAmount(amount);
				bean.setCurrent(Integer.parseInt(page));
				bean.setWebsite("a");
				bean.setCom("goodsTypeServerlet");
				list.addAll(SearchEngine.page(bean));
				list.addAll(SearchEngine.category(keyword, catid));
			}
			list = datas_size==0?null:list;
			//继续缓存其他页的数据
			new CacheThread(keyword, catid,str_sort,paramId,Integer.parseInt(page),sold).start();
			LOG.warn("search from cache");
			return list;
		} catch (Exception e) {
			LOG.warn("",e);
			return null;
		}
	}
	
	
	
	/**每周自动进行更新时间标签超过一周的商品数据
	 * @date 2016年3月1日
	 * @author abc  
	 */
	public  void updateSearchCache(){
		LOG.warn("update search cache start........");
		//获取param表时间标签超过一周的goods_url
		String time = TypeUtils.getTimeBefore(7.0);
		ArrayList<Map<String, String>> paramLatest = param_dao.getParamLatest(time);
		int paramLatest_size = paramLatest.size();
		//根据param_id查询data表获取商品的goods_url
		ArrayList<SearchGoods> list = new ArrayList<SearchGoods>();
		//data表中没有失效的商品集合
		ArrayList<SearchGoods> updata_data_list = new ArrayList<SearchGoods>();
		SearchGoods  goods = null;
		for(int i=0;i<paramLatest_size;i++){
			list.clear();
			updata_data_list.clear();
			String param_id = paramLatest.get(i).get("param_id");
			LOG.warn("param_id:"+param_id);
			ArrayList<SearchGoods> datas = param_dao.getDatasLast(param_id,time);
			int datas_size = datas.size();
			String goods_url = null;
			//根据goods_url获取此商品最新信息，并更新data表
			for(int j=0;j<datas_size;j++){
				goods = datas.get(j);
				goods_url = goods.getGoods_url();
				GoodsBean goodsBean = ParseGoodsUrl.parseGoodsw(goods_url, 0);
				if(goodsBean!=null){
					int valid = goodsBean.getValid();
					goods.setValid(valid);
					if(valid!=0){//数据有效的情况下更新goods值
						goods.setGoods_price(goodsBean.getpSprice());
						goods.setGoods_solder(goodsBean.getSell());
						goods.setGoods_minOrder(goodsBean.getMinOrder());
						updata_data_list.add(goods);
						LOG.warn(param_id+"---goodsdata---"+j);
					}
				}else{
					goods.setValid(0);
				}
				list.add(goods);
			}
			param_dao.updateData(list);//更新data表
			//重新构建
			int int_param_id = Integer.parseInt(param_id);
			int updata_data_list_size = updata_data_list.size();
			param_dao.updateParamDataCount(updata_data_list_size, int_param_id);
			if(updata_data_list_size<400){
				param_dao.updateParamCacheFlag(1, int_param_id);
			}
			//更新param的时间戳
			param_dao.updateParam(paramLatest.get(i).get("param_key"), 
					paramLatest.get(i).get("param_catid"), Integer.valueOf(paramLatest.get(i).get("param_sort")), int_param_id);
		}
		//data表中goods_flag标志为0的迁移到另一个表中,并删除data表失效商品数据
		param_dao.addLog();
		LOG.warn("update search cache end........");
	}
	
	
	
	/**缓存 key 
	 * @date 2016年3月1日
	 * @author abc
	 * @param
	 * 若为true则初始化缓存key  若为false则更新缓存key 
	 */
	public  void saveCache(boolean init){
		//获取所有的param（关键词keyword、类别catid、排序sort）
		ArrayList<Map<String,Object>> paramAll = param_dao.getParamAll();
		List<Map<String, Object>>  cache_list = new ArrayList<Map<String, Object>>();
		int paramAll_size = paramAll.size();
		Map<String, Object> param_map = null;
		for(int i=0;i<paramAll_size;i++){
			param_map = new HashMap<String, Object>();
			param_map.put("param_id", paramAll.get(i).get("param_id"));
			cache_list.add(param_map);
		}
		if(init){
			Cache.save("search_cache", cache_list);//保存缓存key
			LOG.warn("initCache-search_cache");
		}else{
			Cache.replace("search_cache", cache_list);//更新缓存key
			LOG.warn("updateCache-search_cache");
		}
	}
	
	
	public static void main(String[] args) {
//		new CacheThread("tree","121","order_desc",3).start();
		
		SearchCacheDriver cacheDriver = new SearchCacheDriver();
		cacheDriver.updateSearchCache();
	}
	
	

}
