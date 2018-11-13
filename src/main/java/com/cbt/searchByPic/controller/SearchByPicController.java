package com.cbt.searchByPic.controller;

import com.cbt.bean.SearchIndex;
import com.cbt.bean.SearchResults;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.parse.service.StrUtils;
import com.cbt.searchByPic.bean.CustomerRequireBean;
import com.cbt.searchByPic.service.SearchByPicService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 图片搜索2.0  8小时后台
 * @author admin
 *
 */
@Controller
@RequestMapping("/SearchByPicController")
public class SearchByPicController {

	@Autowired
	private SearchByPicService searchByPicService;
	
	@Autowired
	private  com.cbt.util.JspToPDF  jspToPDF ; 
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SearchByPicController.class);
	
	/**
	 * 获取客户信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getKeyWordsList")
	@ResponseBody
	public ModelAndView getKeyWordsList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView("prokwResult");
		String page = request.getParameter("page");
		try {
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page = "1";
			}
			List<CustomerRequireBean> selectAll = searchByPicService.selectAll(Integer.parseInt(page));
			
			int count = selectAll.size()>0?selectAll.get(0).getCount():0;
			
			count = count%40==0?count/40:count/40+1;
			
			mv.addObject("total", count);
			mv.addObject("current", page);
			mv.addObject("items", selectAll);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return mv;
		
	}
	
	/**index管理页面
	 * @date 2016年7月13日
	 * @author abc
	 * @param keywords
	 * @param indexid
	 * @return  
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryIndex(String page, String indexId) {
		ModelAndView mv = new ModelAndView("proKwmana");
		try {
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page = "1";
			}
			if(StrUtils.isNullOrEmpty(indexId)){
				indexId="0";
			}
			List<SearchIndex> selectAll = searchByPicService.selectList(Integer.parseInt(page),Integer.parseInt(indexId));
			
			int count = selectAll.size()>0?selectAll.get(0).getCount():0;
			
			count = count%40==0?count/40:count/40+1;
			
			mv.addObject("total", count);
			mv.addObject("current", page);
			mv.addObject("items", selectAll);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return mv;
	}
	
	/**展现搜索结果
	 * @date 2016年7月13日
	 * @author abc
	 * @param indexid
	 * @param page
	 * @return  
	 */
	@RequestMapping(value = "/resultlist", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchByPrice(String indexid, String page, String minprice, String maxprice) {
		ModelAndView mv = new ModelAndView("proKwResultDetail");
		mv.addObject("indexid", indexid);
		try {
			//index_id错误
			if(StrUtils.isNullOrEmpty(indexid)||!StrUtils.isMatch(indexid, "(\\d+)")){
				return mv;
			}
			//分页page默认值
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page = "1";
			}
			//
			mv.addObject("minprice", minprice);
			if(StrUtils.isNullOrEmpty(minprice)||!StrUtils.isMatch(minprice, "(\\d+\\.*\\d*)")){
				minprice = "0";
			}
			
			mv.addObject("maxprice", maxprice);
			if(StrUtils.isNullOrEmpty(maxprice)||!StrUtils.isMatch(maxprice, "(\\d+\\.*\\d*)")){
				maxprice = "0";
			}
			
			//查询search_results_temp
			List<SearchResults> selectByIndexId =
					searchByPicService.selectByIndexIdAndPrice(Integer.parseInt(indexid),
							Integer.parseInt(page),Double.valueOf(minprice),Double.valueOf(maxprice));
			
			int count = selectByIndexId.size()>0?selectByIndexId.get(0).getCount():0;
			
			count = count%80==0?count/80:count/80+1;
			//总页数
			mv.addObject("total", count);
			//当前页码
			mv.addObject("current", page);
			mv.addObject("items", selectByIndexId);
			
		} catch (Exception e) {
			LOG.warn("",e);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/correct", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView correctIndex(String indexid, String id, String email, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("proKwmess");
		request.getSession().setAttribute("customEmail",email);
		try {
			CustomerRequireBean Bean = searchByPicService.selectByPrimaryKey(Integer.parseInt(id));
			if(StrUtils.isNotNullEmpty(indexid)){
				SearchIndex searchIndex = searchByPicService.selectKeyWords(Integer.parseInt(indexid));
				mv.addObject("searchIndex", searchIndex);
			}
			mv.addObject("index", Bean);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return mv;
	}
	
	/**提交
	 * @date 2016年7月13日
	 * @author abc
	 * @param request
	 * @param response
	 * @return  
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView submitIndex(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("proKwmess");
		String results = "";
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String id = request.getParameter("id");
			String indexid = request.getParameter("indexid");
			String en_name = request.getParameter("en");
			String en_name1 = request.getParameter("enone");
			String en_name2 = request.getParameter("entwo");
			String en_name3 = request.getParameter("enthree");
			String cn_name = request.getParameter("cn");
			String cn_name1 = request.getParameter("cnone");
			String cn_name2 = request.getParameter("cntwo");
			String cn_name3 = request.getParameter("cnthree");
			String keywords = request.getParameter("keywords");
			String detail_catid = request.getParameter("decatid");
			String min_price = request.getParameter("minprice");
			String max_price = request.getParameter("maxprice");
			keywords = keywords!=null?keywords.replace("，", ","):keywords;
			String translation_catid = request.getParameter("trcatid");
			detail_catid = detail_catid!=null?detail_catid.replace("，", ","):detail_catid;
			if(StrUtils.isNullOrEmpty(id)||!StrUtils.isMatch(id, "(\\d+)")){
				return mv;
			}
			CustomerRequireBean Bean = searchByPicService.selectByPrimaryKey(Integer.parseInt(id));
			mv.addObject("index", Bean);
			if(StrUtils.isNullOrEmpty(en_name)){
				mv.addObject("results", "表单提交不能为空");
				return mv;
			}
			if(!StrUtils.isMatch(min_price, "(\\d+)")){
				min_price = "";
			}
			if(!StrUtils.isMatch(max_price, "(\\d+)")){
				max_price = "";
			}
			
			SearchIndex searchIndex = new SearchIndex();
			searchIndex.setMinPrice(min_price);
			searchIndex.setMaxPrice(max_price);
			searchIndex.setCnName(cn_name);
			searchIndex.setCnNameOne(cn_name1);
			searchIndex.setCnNameTwo(cn_name2);
			searchIndex.setCnNameThree(cn_name3);
			searchIndex.setEnName(en_name);
			searchIndex.setEnNameOne(en_name1);
			searchIndex.setEnNameTwo(en_name2);
			searchIndex.setEnNameThree(en_name3);
			searchIndex.setTranslationCatid(translation_catid);
			searchIndex.setDetailCatid(detail_catid);
			searchIndex.setKeywords(keywords);
			searchIndex.setCreateTime(new Date());
			searchIndex.setCustomId(Integer.parseInt(id));
			//indexid 不存在 添加  存在则更新
			if(StrUtils.isNullOrEmpty(indexid)||!StrUtils.isMatch(indexid, "(\\d+)")){
				int index_id = searchByPicService.insertSelective(searchIndex);
				//更新custom_search
				searchByPicService.updateCustomByIndexId(Integer.parseInt(id),index_id);
				if(index_id>0){
					//调用外部接口，开启数据抓取程序
					results = "提交成功";
				}else if(index_id<0){
					results = "已经存在,请重新提交";
				}else{
					results = "提交失败";
				}
				
			}else{
				searchIndex.setFlag(0);
				searchIndex.setId(Integer.parseInt(indexid));
				int updateByPrimaryKey = searchByPicService.updateByPrimaryKey(searchIndex);
				if(updateByPrimaryKey>0){
					results = "更新成功";
				}else{
					results = "更新失败";
				}
			}
		} catch (Exception e) {
			LOG.error("",e);
			results = "程序发生错误，请通知开发人员";
		}
		mv.addObject("results", results);
		return mv;
	}
	
	
	/**增加屏蔽关键词
	 * @date 2016年7月13日
	 * @author abc
	 * @param keywords
	 * @param indexid
	 * @return  
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateByIndex(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("proKwResultDetail");
		ArrayList<SearchResults> results = new ArrayList<SearchResults>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String indexid = request.getParameter("indexid");
			mv.addObject("indexid", indexid);
			String keywords = request.getParameter("keywords");
			keywords = keywords.replace("，", ",");
//			String keywords = new String(keywords.getBytes("ISO-8859-1"),"UTF-8");
			mv.addObject("keywords", keywords);
			
			if(StrUtils.isNullOrEmpty(indexid)||!StrUtils.isMatch(indexid, "(\\d+)")){
				return mv;
			}
			String page = request.getParameter("page");
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page = "1";
			}
			
			//search_index的id
			int id = Integer.parseInt(indexid);
			//获取屏蔽关键词
			SearchIndex selectByPrimaryKey = searchByPicService.selectKeyWords(id);
			String keywords2 = selectByPrimaryKey.getKeywords();
			//更新屏蔽关键词
			if(StrUtils.isNotNullEmpty(keywords)&&StrUtils.isNotNullEmpty(keywords2)){
				if(!StrUtils.isFind(","+keywords2+",", "(,"+keywords+",)")){
					keywords = keywords+","+keywords2;
					selectByPrimaryKey.setKeywords(keywords);
				}
			}
			searchByPicService.updateByPrimaryKey(selectByPrimaryKey);
			//屏蔽关键词新增加后要立刻影响 过滤
			mv.addObject("current", page);
			int paged = Integer.parseInt(page);
			List<SearchResults> selectByIndexId = searchByPicService.selectByIndexId(id,paged);
			int count = selectByIndexId.size()>0?selectByIndexId.get(0).getCount():0;
			int count_page = count%80==0?count/80:count/80+1;
			String pids = "";
			keywords = keywords.replace(",", ")|(");
			keywords = "("+keywords+")";
			for(SearchResults result:selectByIndexId){
				String goods_name = result.getGoodsName();
				if(result.getGoodsValid()!=0&&Pattern.compile(keywords).matcher(goods_name).find()){
					pids = pids+result.getGoodsPid()+",";
					result.setGoodsValid(0);
				}
				results.add(result);
			}
			mv.addObject("items", results);
			//剩余页码的数据屏蔽
			for(int i=1;i<count_page+1;i++){
				if(i==paged){
					continue;
				}
				selectByIndexId = searchByPicService.selectByIndexId(id,i);
				for(SearchResults result:selectByIndexId){
					String goods_name = result.getGoodsName();
					if(result.getGoodsValid()==0){
						continue;
					}
					if(Pattern.compile(keywords).matcher(goods_name).find()){
						pids = pids+result.getGoodsPid()+",";
					}
				}
			}
			//屏蔽掉的商品状态更改
			if(pids.endsWith(",")){
				pids = pids.substring(0, pids.length()-1);
			}
			searchByPicService.updateValidByPids(id, pids,0);
			searchByPicService.updateValidByPids1(id, pids,0);
			
//			//重新计算分页
//			int re_count = pids.split(",").length;
//			re_count= count-re_count;
//			count_page = re_count%80==0?re_count/80:re_count/80+1;
			
			mv.addObject("total", count_page);
			
		} catch (Exception e) {
			LOG.error("",e);
		}
		return mv;
	}
 	
 	/**翻译结果页面
	 * @date 2016年7月13日
	 * @author abc
	 * @param indexId
	 * @param pids
	 * @return  
	 */
	@RequestMapping(value = "/retrans", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView translation(String indexid, String page) {
		ModelAndView mv = new ModelAndView("proKwtrans");
		try {
			mv.addObject("indexid", indexid);
			if(StrUtils.isNullOrEmpty(indexid)||!StrUtils.isMatch(indexid, "(\\d+)")){
				return mv;
			}
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page = "1";
			}
			mv.addObject("current", page);
			int id = Integer.parseInt(indexid);
			SearchIndex searchIndex = searchByPicService.selectKeyWords(id);
			//翻译类目
			String translation_catid = searchIndex.getTranslationCatid();
			if(translation_catid!=null){
				translation_catid = "1".equals(translation_catid)?"服装&鞋":translation_catid;
				translation_catid = "2".equals(translation_catid)?"消费电子及配件":translation_catid;
				translation_catid = "3".equals(translation_catid)?"办公用品":translation_catid;
				translation_catid = "4".equals(translation_catid)?"家庭用品,家具&园艺":translation_catid;
				translation_catid = "5".equals(translation_catid)?"首饰":translation_catid;
				translation_catid = "6".equals(translation_catid)?"手表":translation_catid;
				translation_catid = "7".equals(translation_catid)?"箱包":translation_catid;
				translation_catid = "8".equals(translation_catid)?"母婴用品":translation_catid;
				translation_catid = "9".equals(translation_catid)?"运动户外":translation_catid;
				translation_catid = "10".equals(translation_catid)?"美妆":translation_catid;
				translation_catid = "11".equals(translation_catid)?"汽配&摩托":translation_catid;
				translation_catid = "12".equals(translation_catid)?"玩具":translation_catid;
				translation_catid = "13".equals(translation_catid)?"灯具":translation_catid;
			}
			boolean flag = false;
			mv.addObject("translationcatid", translation_catid);
			mv.addObject("aliexpresscatid", searchIndex.getAliexpressCatid());
			//根据index_id  分页查询数据
			List<SearchResults> insertByTemPids = searchByPicService.selectResultByIndexId(id, Integer.parseInt(page));
			if(insertByTemPids==null||insertByTemPids.isEmpty()){
				insertByTemPids = searchByPicService.selectByIndexId(id, Integer.parseInt(page));
				flag  = true;
			}
			//总页数
			Integer count = insertByTemPids.size()>0?insertByTemPids.get(0).getCount():0;
			count = count%80==0? count/80:count/80+1;
			mv.addObject("total", count);
			
			List<SearchResults> results = new ArrayList<SearchResults>();
			List<SearchResults> results_save = new ArrayList<SearchResults>();
			String goodsTypeEn = null;
			String goodsDetailEn = null;
			DecimalFormat df=new DecimalFormat("#0.##");
			 double tradePrice = 0.0;
			for(SearchResults result:insertByTemPids){
				
				//重新翻译
				if(flag){
					//翻译产品名称
					String goods_name = ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsName());
					String first_words = goods_name.split("(\\s+)")[0];
					
					String end_name = first_words.length()>1?
							first_words.substring(0,1).toUpperCase()+first_words.substring(1):first_words;
					
					goods_name = goods_name.replace(first_words, end_name);
					result.setGoodsNameEn(goods_name);
					//翻译产品规格
					goodsTypeEn =  ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsType());
					result.setGoodsTypeEn(goodsTypeEn);
					//翻译产品明细
					goodsDetailEn =  ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsDetail());
					result.setGoodsDetailEn(goodsDetailEn);
					results_save.add(result);
					
				}else{
					goodsTypeEn = result.getGoodsTypeEn();
					goodsDetailEn = result.getGoodsDetailEn();
					if(StrUtils.isNotNullEmpty(result.getTrade())){
						 String trade = result.getTrade().substring(result.getTrade().indexOf("交")+1);
						 //如果trade 包含 万
						 if(trade.contains("万")){
							 char[]  cc= trade.toCharArray();
								int t=0;
								for(int i=0;i<cc.length;i++){
									 String gg= String.valueOf(cc[i]);
									 if(!gg.matches("\\d+")){
										 t=i;
										 break; 
									 }
								}
							trade = trade.substring(0, t);
						   tradePrice = Double.parseDouble(df.format(Double.parseDouble(trade)*10000/6.5));
							
						 }else{
							 // 人民币除以汇率 
							tradePrice = Double.parseDouble(df.format(Double.parseDouble(trade.substring(0, trade.indexOf("元")))/6.5));
						 }
						 trade = "30 Day Sale:"+tradePrice+" USD";
						 result.setTrade(trade);
					}
				}
				
				//前台页面显示规格
				
				//前台页面显示明细
				
				
				result.setGoodsTypeEn(goodsTypeEn);
				result.setGoodsDetailEn(goodsDetailEn);
				results.add(result);
			}
			 if(flag){
				for(int i=1;i<count;i++){
					if(i==Integer.parseInt(page)){
						continue;
					}
					insertByTemPids = searchByPicService.selectByIndexId(id, Integer.parseInt(page));
					for(SearchResults result:insertByTemPids){
						//翻译产品名称
						String goods_name = ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsName());
						String first_words = goods_name.split("(\\s+)")[0];
						
						String end_name = first_words.length()>1?
								first_words.substring(0,1).toUpperCase()+first_words.substring(1):first_words;
						
						goods_name = goods_name.replace(first_words, end_name);
						result.setGoodsNameEn(goods_name);
						//翻译产品规格
						goodsTypeEn =  ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsType());
						result.setGoodsTypeEn(goodsTypeEn);
						//翻译产品明细
						goodsDetailEn =  ParseGoodsUrl.tranlationText(translation_catid,result.getGoodsDetail());
						result.setGoodsDetailEn(goodsDetailEn);
						results_save.add(result);
					}
				}
				searchByPicService.saveListByTempTable(results_save);
				//更新翻译时间
				searchByPicService.updateTranslationTime(id);
			}  
			//翻译结果数据
			mv.addObject("items", results);
			
		} catch (Exception e) {
			LOG.error("",e);
		}
		return mv;
	}
	
	
	/**保存搜索结果
	 * @date 2016年7月13日
	 * @author abc
	 * @param indexId
	 * @param pids
	 * @return  
	 */
	@RequestMapping(value = "/saveGoods", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String save(String indexId,String pids,String page) {
		int result = 0;
		try {
			//index_id错误
			if(StrUtils.isNullOrEmpty(indexId)||!StrUtils.isMatch(indexId, "(\\d+)")){
				result =  -3;
			}
			if(StrUtils.isNullOrEmpty(pids)){
				pids = "";
			}else{
				pids = pids.substring(0,pids.length()-1);
				pids = "("+pids.replace(",", ")|(")+")";
			}
			if(StrUtils.isNullOrEmpty(page)||!StrUtils.isMatch(page, "(\\d+)")){
				page =  "1";
			}
			int id = Integer.parseInt(indexId);
			//获取翻译词库
			SearchIndex selectByPrimaryKey = searchByPicService.selectKeyWords(id);
			String translation_catid = selectByPrimaryKey.getTranslationCatid();
			translation_catid = StrUtils.isNullOrEmpty(translation_catid)?"0":translation_catid;
			String aliexpressCatid = selectByPrimaryKey.getAliexpressCatid();
			aliexpressCatid = StrUtils.isNullOrEmpty(aliexpressCatid)?"0":aliexpressCatid;
			
			int startD = Integer.parseInt(page);
			List<SearchResults> selectByIndexId = searchByPicService.selectByIndexId(id,startD);
			int count = selectByIndexId.size()>0?selectByIndexId.get(0).getCount():0;
			count = count%80==0?count/80:count/80+1;
			List<SearchResults> list = new ArrayList<SearchResults>();
			String unValidPids = "";
			String ValidPids = "";
			//翻译当前页面的产品
			for(SearchResults index:selectByIndexId){
				//当前页面的商品未被选中的话就不保存到search_result中
				if(!StrUtils.isMatch(index.getGoodsPid(), pids)){
					if(index.getGoodsValid()!=0){
						unValidPids = unValidPids+index.getGoodsPid()+",";
					}
					continue;
				}
				if(index.getGoodsValid()==0){
					ValidPids = ValidPids+index.getGoodsPid()+",";
				}
				index.setGoodsValid(1);
				//翻译产品名称,首字母大写
				String goods_name = ParseGoodsUrl.tranlationText(translation_catid,index.getGoodsName());
				
				String first_words = goods_name.split("(\\s+)")[0];
				
				String end_name = first_words.length()>1?
						first_words.substring(0,1).toUpperCase()+first_words.substring(1):first_words;
				
				goods_name = goods_name.replace(first_words, end_name);
				
				index.setGoodsNameEn(goods_name);
				
				//翻译产品规格
				String goods_type =  ParseGoodsUrl.tranlationText(translation_catid,index.getGoodsType());
				index.setGoodsTypeEn(goods_type);
				
				//翻译产品明细
				String goods_detail =  ParseGoodsUrl.tranlationText(translation_catid,index.getGoodsDetail());
				index.setGoodsDetailEn(goods_detail);
				
				//aliexpress类别id
				index.setAliexpressCatid(aliexpressCatid);
				//销量、最小订量
				String trade = index.getTrade();
				if(StrUtils.isNotNullEmpty(trade)&&index.getGoodsSold()==0){
					String str = StrUtils.matchStr(trade, "(成交.*)").replace("成交", "").trim();
					String total = StrUtils.matchStr(str, "(\\d+\\.*\\d*)");
					double sold = 0.0;
					Double price = index.getGoodsPrice();
					if(str.indexOf("万元")>-1){
						sold = Double.valueOf(total)*10000/price;
					}else{
						sold = Double.valueOf(total)/price;
					}
					String str_sold =String.valueOf(sold); 
					str_sold = str_sold.substring(0, str_sold.indexOf(".")); 
					index.setGoodsSold(Integer.valueOf(str_sold));
				}else{
					index.setGoodsSold(0);
				}
				index.setGoodsMorder(1);
				
				list.add(index);
			}
			//保存到search_results表
			int insertByTemPids = searchByPicService.saveListByTempTable(list);
			pids = pids.replace(")|(", ",").replace("(", "").replace(")", "").trim();
			
			//二次操作，有可能unchecked的商品在第一次的时候已经保存到search_result表中了，需要更新一下状态
			//更新search_result_temp产品状态
			searchByPicService.updateValidByPids1(id, unValidPids,0);
			searchByPicService.updateValidByPids1(id, ValidPids,1);
			//更新search_result产品状态
			searchByPicService.updateValidByPids(id, unValidPids,0);
			searchByPicService.updateValidByPids(id, ValidPids,1);
			
			if(insertByTemPids>0){
				 result =  count>startD?count:startD;
			}else{
				result =  -1;
			}
		} catch (Exception e) {
			LOG.error("",e);
			result = -2;
		}
		return String.valueOf(result);
	}
	
	//发送邮件给客户  
	@RequestMapping(value = "/JspToPDF")
	@ResponseBody
	public ModelAndView JspToPDF(String indexId, String page, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("proKwEmail");
		SearchIndex searchIndex = searchByPicService.selectKeyWords(Integer.parseInt(indexId));
		mv.addObject("customId", searchIndex.getCustomId());
		List<SearchResults> insertByTemPids = searchByPicService.selectResultByIndexId(Integer.parseInt(indexId), Integer.parseInt(page));
		StringBuffer html = new StringBuffer();
		html.append("<div class='eiresul'>");
		DecimalFormat df=new DecimalFormat("#0.##");
		 double tradePrice = 0.0;
		for(SearchResults bean: insertByTemPids){
			String eName = bean.getGoodsNameEn();
			double price = bean.getGoodsPrice();
			String img = bean.getGoodsImg();
			String trade ="";
			if(StrUtils.isNotNullEmpty(bean.getTrade())){
				  trade = bean.getTrade().substring(bean.getTrade().indexOf("交")+1);
				 //如果trade 包含 万
				 if(trade.contains("万")){
					 char[]  cc= trade.toCharArray();
						int t=0;
						for(int i=0;i<cc.length;i++){
							 String gg= String.valueOf(cc[i]);
							 if(!gg.matches("\\d+")){
								 t=i;
								 break; 
							 }
						}
					trade = trade.substring(0, t);
				   tradePrice = Double.parseDouble(df.format(Double.parseDouble(trade)*10000/6.5));
				 }else{
					 // 人民币除以汇率 
					tradePrice = Double.parseDouble(df.format(Double.parseDouble(trade.substring(0, trade.indexOf("元")))/6.5));
				 }
				 trade = "30 Day Sale:"+tradePrice+" USD";
			}
			html.append("<li class='eireli'><div class='eirelidcon'>"+
					"<div class='eireimg'>"+
						"<a href=''>"+
							"<img width='250px' height='250px' class='rimgai' src='"+img+"'>"+
						"</a>"+
					"</div>"+
					"<p class='eritxt'>"+
						"<label for='' style='height: 50px;  width: 50px;  display: inline-block;  top:-81px;  left:12px;  clear:both;'></label>"+ 
						"<span class='eirpname' title=''>"+eName+"</span>"+
					"</p>"+
					"<p class='eritxt'>"+
						"<span class='eirprice'>\\$"+price+"</span>"+
						"<font size='2px'>"+trade+"</font>"+
					"</p></div></li>");
			
		} 
		
		html.append("</div>");
		String content = html.toString();
		String rootPath = request.getServletContext().getRealPath("/");	
		String  filePath = rootPath+"JspToHtml/protrans.html"; 
		String templateContent=""; 
		FileInputStream fileinputstream = new FileInputStream(filePath);// 读取模板文件   
		int lenght = fileinputstream.available();   
		byte bytes[] = new byte[lenght];   
		fileinputstream.read(bytes);   
		fileinputstream.close();   
		templateContent = new String(bytes);   
		System.out.print(templateContent);
		templateContent=templateContent.replaceAll("1aa11",content);   
		System.out.print(templateContent);   
		  
		// 根据时间得文件名   
		Calendar calendar = Calendar.getInstance();   
		String autoNum = String.valueOf(calendar.getTimeInMillis());
		String fileame = autoNum+".html";   
		fileame = rootPath+ "JspToHtml/" + fileame;// 生成的html文件保存路径。   
		FileOutputStream fileoutputstream = new FileOutputStream(fileame);// 建立文件输出流   
		System.out.print("文件输出路径:");   
		System.out.print(fileame);   
		byte tag_bytes[] = templateContent.getBytes();   
		fileoutputstream.write(tag_bytes);   
		fileoutputstream.close(); 
		
		//直接转pdf
		 String  Info =  jspToPDF.readFile(fileame, "UTF-8");
		 jspToPDF.doConversion2(Info, "D:\\PDF\\"+autoNum+".pdf");
		 
		 //删除html 
		 File file = new  File(fileame);
		 file.delete();
		 
		 //将客户信息写入邮件页面
		 String customEmail = (String) request.getSession().getAttribute("customEmail");
		 mv.addObject("customEmail",customEmail );
		 String Enclosure = autoNum+".pdf";
		 mv.addObject("Enclosure", Enclosure);
		 //给定固定的url  
		 String url = "http://192.168.1.206:10001/cbtconsole/SearchByPicController/retrans?indexid=27&page=1";
		 mv.addObject("url", url); 
		 
		return  mv;
	}
	
	
	/**同步数据至线上数据库
	 * @date 2016年7月13日
	 * @author abc
	 * @param indexId
	 * @param pids
	 * @return  
	 */
	@RequestMapping(value = "/syncdata", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String syncData(String indexid) {
		String result = "同步完成";
		try {
			DataSourceSelector.restore();
			if(StrUtils.isNullOrEmpty(indexid)||!StrUtils.isMatch(indexid, "(\\d+)")){
				result = "同步错误";
			}else{
				Integer indexId = Integer.valueOf(indexid);
				//取出本地数据库的index
				SearchIndex index = searchByPicService.selectKeyWords(indexId);
				//取出本地数据库的results
				List<SearchResults> resultsList = searchByPicService.selectByIndexIdAll(indexId);
				if(resultsList!=null&&!resultsList.isEmpty()){
					for(SearchResults results:resultsList){
						results.setDownLoadFlag(0);
						results.setDownLoadImg(null);
					}
					//切换数据源
					//同步index到线上index
					DataSourceSelector.set("dataSource127hop");
					int index_sync = 0 ;
					if(searchByPicService.selectKeyWords(indexId)==null){
						index_sync = searchByPicService.insertSelective(index);
					}else{
						index_sync = searchByPicService.updateByPrimaryKey(index);
					}
					//先删除已有的数据，再同步数据
					searchByPicService.deleteByIndexId(indexId);
					int result_sync = searchByPicService.saveListByTempTable(resultsList);
					//数据库还原
					DataSourceSelector.restore();
					String index_mess = index_sync==0?"index同步失败,":"";
					//保存成功后，更新状态
					String result_mess = "";
					if(result_sync>0){
						int updateSyncFlag = searchByPicService.updateSyncFlag(indexId, 1);
						result_mess = updateSyncFlag!=0?"产品数据同步成功":"产品数据同步成功,index的sync状态更新失败";
					}else{
						result_mess = "产品数据同步失败";
					}
					result = index_mess+result_mess;
				}else{
					result = "请先保存并翻译数据，再同步";
				}
			}
		} catch (Exception e) {
			LOG.error("",e);
			result = "程序发生错误";
		}finally{
			//数据库还原
			DataSourceSelector.restore();
		}
		return result;
	}
	
	
}

