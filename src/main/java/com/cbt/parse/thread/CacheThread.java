package com.cbt.parse.thread;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.dao.AliSearchCache;
import com.cbt.parse.daoimp.IAliSearchCache;
import com.cbt.parse.driver.SearchCacheDriver;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.GetFilterUtils;
import com.cbt.parse.service.ParseSearchUrl;
import com.cbt.parse.service.TypeUtils;
import com.cbt.util.Utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**	对于 主板，手机等等特定类别， 我们 用人工挑选的 方式 （小尹刚做好的）。  但是 增加一个 选项：  不显示我们没筛选的产品。
	
	对于 服装，饰品，珠宝  都只显示销售量 超过 20 的 产品 (具体数字会按类别而不同）	
	搜索时 先检查这个搜索 我们缓存过没有， 和原来的 区别是  这个 缓存结果 是 从数据库中 来的	
		这个缓存结果里面 有 根据 销量  和 是否 有 silimar product 而做 过滤		
		这个 “搜索结果缓存” 在 不断 增加的 同时，每周自动到 aliexpress 处进行更新	
														
		如果 这个 搜索 以前没缓存过， 当场从  AliExpress 抓取，并过滤 和形成页面。  
															
		一旦启动缓存，就在 把 后面 3页的内容都进行 分析和缓存。不要等用户点击
					
		假设缓存了 4页的产品，最后筛选下来的 只有 2页半 就不再提供其他产品。 （将来这里会接上 1688的产品）
						
			按销量排序的 情况下， 可以 每下载一页就分析一页，如果 这一页 的商品已经不满足要求了，就不用缓存下一页了。
			
			按 “价格 和 best match” 的 情况下， 可以 一下子 缓存 3页，并且在 客户点击第2页时，自动再多缓存3页，以此类推。		（具体一次缓存多少页，我没仔细考虑，你们定）
		
		保存的 搜索结果中 还要 记录此 商店是否 旺旺在线，如果 一个商品 旺旺不在线，那么 展示她 需要 的 销量是  最小值*2													
		保存的 搜索结果中 还要 记录此 商店是否 有 相似款 （搜索时用 group similar product），如果 相似款>2，那么 即使 销量不达标, 也可以展示													
		
		在搜索结果 中，如果 有商品属于我们以前发现 找不到的产品，就自动不显示		产品黑名单					
		在搜索结果 中，如果 有商品属于我们 觉得有问题的 卖家，就自动不显示			卖家黑名单
							
 * @author abc   2016-2-24
 *
 */
public class CacheThread extends Thread {
	private  final Log LOG = LogFactory.getLog(CacheThread.class);
	private SearchCacheDriver cacheDriver = new SearchCacheDriver();
	private String keyword;//搜索关键
	private String catid;//类别id
	private int sort;//排序
	private String sort_str;
	private IAliSearchCache dao = new AliSearchCache();
	private int user_page;
	private int sold;//限定销量
	private int param_id;
	
	
	
	public CacheThread() {
		
	}
	public CacheThread(String keyword,String catid,String sort_str,int param_id,int user_page,int sold){
		this.keyword = keyword;
		this.catid = catid;
		this.user_page = user_page;
		this.sold = sold;
		sort_str = "order-desc".equals(sort_str)?"total_tranpro_desc":sort_str;
		sort_str = "bbPrice-asc".equals(sort_str)?"price-asc":sort_str;
		this.sort_str = sort_str;
		int sort_flag = "total_tranpro_desc".equals(sort_str)?3:0;
		sort_flag = "price-asc".equals(sort_str)?1:sort_flag;
		this.sort = sort_flag;
		this.param_id = param_id;
		LOG.warn("sort_str-"+sort_str+"+user_page-"+user_page+"+keyword-"+keyword+"+catid-"+catid+"+sort_flag-"+sort_flag);
	}
	
	@Override
	public synchronized void run() {
		param_id = param_id==0?dao.addParam(keyword, catid, sort):param_id;
		LOG.warn("run-paramid:"+param_id);
		Map<String, Object> param = dao.getParam(param_id);
		int param_cache_page = Integer.parseInt(Utility.formatObject(param.get("param_cache_page")));
		//用户点击的页码 已经缓存过，则返回，不继续缓存数据
		if(user_page*3<param_cache_page){
			LOG.warn("----------return----user_page*3<param_cache_page-------");
			return ;
		}	
		int param_cache_flag = Integer.parseInt(Utility.formatObject(param.get("param_cache_flag")));
		//param参数存在，且缓存标志位1才能继续缓存数据
		if( param_id!=0&&param_cache_flag==1){
			ArrayList<SearchGoods> saveCache = getGoodsList(keyword, sort_str, catid);
			if(saveCache.size()>0){
				//数据保存成功之后刷新缓存数据
				saveDatas(saveCache,param_cache_page,param_id,sold);
			}else{
				dao.updateParamCacheFlag(0, param_id);
			}
		}
	}
	
	
	/**保存商品集合数据到datas表中 ---销量排序
	 * 2016年2月26日
	 * abc
	 * user_page  用户点击的页码数（可能为缓存数据的页码数）
	 * param_cache_page  已经缓存的页码
	 */
	private void saveDatas(ArrayList<SearchGoods> list,int param_cache_page,int param_id,int sold){
		ArrayList<SearchGoods> list_result = new ArrayList<SearchGoods>();
		int list_size = list.size();
		boolean  flag = false;//终止缓存标志，若当前页面的所有商品数据均不满足条件(即flag=false)则终止缓存( 即param_cache_flag=0)
		String page_next = "";
		int amount = 0;
		//当前页数据                                  list是第一页的aliexpress数据集合
		for(int j=0;j<list_size;j++){
			SearchGoods goods = list.get(j);
			if(sort==3&&user_page==1&&"goods".equals(goods.getKey_type())&&
					(Integer.parseInt(goods.getGoods_solder())>(sold-1)||Integer.parseInt(goods.getGoods_similar())>2)){
				goods.setGoods_free(goods.getGoods_free().isEmpty()?"0":"1");
				goods.setSeller_flag(1);
				goods.setValid(1);
				goods.setGoods_name(goods.getGoods_name().replace("+", " "));
				goods.setGoods_url(TypeUtils.decodeGoods(goods.getGoods_url().replace("&s=y", "")));
				list_result.add(goods);
				flag = true;
			}
			if("next page".equals(goods.getKey_type())){
				page_next = goods.getKey_name();//页码html
			}else if("page amount".equals(goods.getKey_type())){
				amount = Integer.parseInt(goods.getKey_name().trim());//总页数
			}
		}
		//用户点击页数已经超过aliexpress总页数
		if(user_page>amount){
			LOG.warn("----------return----user_page>amount-------");
			return ;
		}
		page_next  = page_next.split("ui-pagination-active")[1];
		List<String> page_list = DownloadMain.getSpiderContextList1("(?:href=\")(.*?)(?:\")", page_next);
		//价格或“best match”情况下，若param_cache_page>1 需要清空list集合
		if(sort!=3&&param_cache_page>1){
			list.clear();
		}
		//需要缓存多少页数据cache_page
		int cache_page = page_list.size()>3?3:page_list.size();
		//缓存分页数据   从第二页开始
		for(int i=0;i<cache_page;i++){
			String page_url = page_list.get(i);
			page_url = TypeUtils.decodeSearch(page_url);
			if(page_url==null||page_url.isEmpty()){
				break;
			}
			//根据已经缓存的页码数继续向后缓存
			int page = param_cache_page+1+i;//缓存页码
			if(amount+1>page){
				page_url = page_url.replaceAll("&page=\\d+", "&page="+page);
				LOG.warn("page_url:"+page_url);
				ArrayList<SearchGoods> list_page = ParseSearchUrl.parseSearch(page_url, 0, keyword, catid, false);
				if(sort==3){//按销量排序的 情况下,可以 每下载一页就分析一页，如果 这一页 的商品已经不满足要求了,就不用缓存下一页了
					flag = false;
					int list_page_size = list_page.size();
					for(int j=0;j<list_page_size;j++){
						SearchGoods goods = list_page.get(j);
						//保存的 搜索结果中 还要 记录此 商店是否 有 相似款 （搜索时用 group similar product），如果 相似款>2，那么 即使 销量不达标, 也可以展示
						if("goods".equals(goods.getKey_type())&&
								(Integer.parseInt(goods.getGoods_solder())>(sold-1)||Integer.parseInt(goods.getGoods_similar())>2)){
							goods.setGoods_free(goods.getGoods_free().isEmpty()?"0":"1");
							goods.setSeller_flag(1);
							goods.setGoods_name(goods.getGoods_name().replace("+", " "));
							goods.setGoods_url(TypeUtils.decodeGoods(goods.getGoods_url().replace("&s=y", "")));
							goods.setValid(1);
							list_result.add(goods);
							flag = true;
						}
					}
				}else{
					//按 “价格 和 best match” 的 情况下,可以 一下子 缓存 3页,并且在 客户点击第2页时,自动再多缓存3页
					list.addAll(list_page);
				}
			}else{
				//所有页码都缓存了，则终止缓存 缓存标志更改为0
				dao.updateParamCacheFlag(0, param_id);
			}
			//更新param的缓存页码param_cache_page
			dao.updateParamCachePage(page, param_id);
			if(sort==3&&!flag){//按销量排序的情况下 当出现一页的所有数据均不满足的时候，则终止缓存 缓存标志更改为0
				dao.updateParamCacheFlag(0, param_id);
				break;
			}
		}
		
		
		//三页数据一起解析  按 “价格 和 best match” 的 情况下， 可以 一下子 缓存 3页
		if(sort!=3){
			list_size = list.size();
			for(int j=0;j<list_size;j++){
				SearchGoods goods = list.get(j);
				//保存的 搜索结果中 还要 记录此 商店是否 有 相似款 （搜索时用 group similar product），如果 相似款>2，那么 即使 销量不达标, 也可以展示
				if("goods".equals(goods.getKey_type())&&
						(Integer.parseInt(goods.getGoods_solder())>(sold-1)||Integer.parseInt(goods.getGoods_similar())>2)){
					
					goods.setGoods_free(goods.getGoods_free().isEmpty()?"0":"1");
					goods.setSeller_flag(1);
					goods.setGoods_name(goods.getGoods_name().replace("+", " "));
					goods.setGoods_url(TypeUtils.decodeGoods(goods.getGoods_url().replace("&s=y", "")));
					goods.setValid(1);
					list_result.add(goods);
				}
			}
		}
		if(param_id!=0&&!list_result.isEmpty()){
			//批量添加数据到data
			int dataCount = dao.addData(list_result, param_id);
			//更改param缓存数据数量dataCount
			dao.updateParamDataCount(dataCount, param_id);
			if(dataCount>400){//限制商品数量 商品数量超过一定数量 则终止缓存 缓存标志更改为0
				dao.updateParamCacheFlag(0, param_id);
			}
			LOG.warn("----------saveSearchCacheData----success-------");
		}
	}
	
	
	/**拼搜索链接，获取页码，数据集合
	 * @date 2016年2月29日
	 * @author abc
	 * @param keyword
	 * @param sort
	 * @param catid
	 * @return  
	 */
	private ArrayList<SearchGoods> getGoodsList(String keyword,String sort,String catid){
		sort = "order-desc".equals(sort)?"total_tranpro_desc":sort;
		sort = "bbPrice-asc".equals(sort)?"price_asc":sort;
		String minprice = "price_asc".equals(sort)?GetFilterUtils.priceFilter(catid):"";
		String url = "http://www.aliexpress.com/wholesale?CatId="+catid+
				"&site=glo&groupsort=1&shipCountry=US&shipFromCountry=cn&g=y&SearchText="+keyword+
				"&minPrice="+minprice+"&maxPrice=&minQuantity=&maxQuantity=&SortType="+sort+"&similar_style=yes";
		url=url.replaceAll("\\s+", "%20").replaceAll("(\\+)+", "%20");
		ArrayList<SearchGoods> list = ParseSearchUrl.parseSearch(url, 0, keyword, catid, false);
		return list;
	}
	
	/**更新缓存
	 * 2016年2月26日
	 * abc
	 *//*
	private void updateCache(ArrayList<SearchGoods> datas,String table_name,int param_id){
		//创建或更新当前 搜索词+类别+排序 的内存表
		int datas_size = datas.size();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object>  map = null;
		boolean  flag = false;
		for(int i=0;i<datas_size;i++){
			SearchGoods goods = datas.get(i);
			map = new HashMap<String, Object>();
			map.put("goods_name", goods.getGoods_name());
			map.put("goods_price", goods.getGoods_price());
			map.put("goods_img", goods.getGoods_image());
			map.put("goods_url", goods.getGoods_url());
			map.put("goods_sold", goods.getGoods_solder());
			map.put("goods_morder", goods.getGoods_minOrder());
			map.put("goods_free", goods.getGoods_free());
			map.put("online_flag", goods.getSeller_online());
			map.put("seller_flag", goods.getSeller_flag());
			map.put("goods_flag", goods.getValid());
			map.put("goods_similar", goods.getGoods_similar());
			list.add(map);
		}
		try {
			ISearchCacheDao  cache = new SearchCacheDao();
			if(table_name.isEmpty()||cache.queryTable(table_name)==0){
				//内存表不存在，则创建内存表
				table_name = param_id+"_"+TypeUtils.getTime().replaceAll("[-(\\:)(\\s+)]", "").trim();
				cache.create(table_name);
				dao.updateParamTabName(table_name, param_id);
				flag = true;
				LOG.warn("create memory table----"+table_name);
			}
			//向内存表插入数据
			cache.insertNewTb(list, table_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){//若新创建了内存表则要更新缓存key
			cacheDriver.saveCache(false);
		}
	}*/
}
