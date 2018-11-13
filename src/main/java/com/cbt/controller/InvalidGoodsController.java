package com.cbt.controller;

import com.cbt.bean.IntensveBean;
import com.cbt.bean.InvalidUrlBean;
import com.cbt.parse.service.StrUtils;
import com.cbt.parse.service.TypeUtils;
import com.cbt.service.InvalidGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *屏蔽 店铺+产品
 *@author abc
 *@date 2016年11月29日
 *
 */
@Controller
@RequestMapping(value = "/invalidgc")
public class InvalidGoodsController {
	@Autowired
	private InvalidGoodsService invalidGoodsService;
	
	
	/**屏蔽商店
	 * @date 2016年11月28日
	 * @author abc
	 * @param surl
	 * @return  
	 */
	@RequestMapping(value="/istore",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String invalidStore(String surl){
		if(surl==null||surl.isEmpty()){
			return "-1";
		}
		surl = surl.trim();
		if(surl.length()>500||StrUtils.isFind(surl, "(\\s)")){
			return "-1";
		}
		
		//链接简单处理
		System.err.println("surl----1----"+surl);
		//https://www.aliexpress.com/store/1866225?spm=2114.10010108.100005.1.H9BQNu
		if(surl.indexOf("/store/")>-1){
			surl = surl.indexOf("?")>-1?surl.substring(0, surl.indexOf("?")):surl;
		}else{
			return "-1";
		}
		
		if(surl.indexOf("/all-wholesale-products")>-1){
			surl = surl.replace("/all-wholesale-products", "");
			surl = surl.replace(".html", "");
		}
		
		surl = surl.startsWith("https:")?surl.replace("https:", ""):surl;
		surl = surl.startsWith("http://")?surl.replace("http://", "//"):surl;
		
		System.err.println("surl----2----"+surl);
		surl = surl.replace("http:////", "http://");
		
		String storeId = StrUtils.matchStr(surl, "/\\d+").replaceAll("\\D+","");
		int addFilterStoreUrl = invalidGoodsService.addFilterStoreUrl(surl, storeId);
		if(addFilterStoreUrl < 0 ){
			return "-2";
		}else{
			return addFilterStoreUrl > 0 ? "1" : "0";
		}
	}
	/**屏蔽产品
	 * @date 2016年11月28日
	 * @author abc
	 * @param surl 产品链接
	 * @return  
	 */
	@RequestMapping(value="/igoods",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String invalidGoods(String text){
		if(text==null||text.isEmpty()){
			return  "-1";
		}
		if(text.indexOf("/store/")>-1){
			return "-1";
		}
		String result = "";
		String[] texts = text.indexOf("https://")>-1?text.split("https://"):text.split("http://");
		for(String gurl:texts){
			System.err.println("gurl--------------"+gurl);
			gurl = gurl.replace("&amp;", "&").trim();
			
			String encodeSearch = "";
			String source = "";
			String item = "";
			if(gurl.indexOf("import-express") == -1){
				encodeSearch = gurl.indexOf("?")>-1?gurl.substring(0, gurl.indexOf("?")):gurl;
				encodeSearch = encodeSearch.startsWith("http://")?encodeSearch:"http://"+encodeSearch;
				encodeSearch = encodeSearch.replace("https://", "http://");
				item = StrUtils.matchStr(encodeSearch, "(/\\d+\\.htm)").replaceAll("\\D+", "").trim();
				source = TypeUtils.compatibleUrl("", item, encodeSearch);
			}else{
				String[] params = gurl.split("&");
				if(StrUtils.isFind(gurl, "(&u0=.*&u1=.*&u2=.*&u3=.*)")){
					String u0 = "";
					String u1 = "";
					String u2 = "";
					String u3 = "";
					for(String param:params){
						if(param.indexOf("u0=") > -1){
							u0 = "&" + param;
						}else if(param.indexOf("u1=") > -1){
							u1 = "&" + param;
						}else if(param.indexOf("u2=") > -1){
							u2 = "&" + param;
						}else if(param.indexOf("u3=") > -1){
							u3 = "&" + param;
						}
					}
					encodeSearch = TypeUtils.decodeGoods(u0+u1+u2+u3);
					item = StrUtils.matchStr(encodeSearch, "(/\\d+\\.htm)").replaceAll("\\D+", "").trim();
					source = TypeUtils.compatibleUrl("", item, encodeSearch);
							
				}else if(StrUtils.isFind(gurl, "(&{0,1}source=.*&item=.*)")|| StrUtils.isFind(gurl, "(&{0,1}item=.*&source=.*)")){
					for(String param:params){
						if(param.indexOf("source=") > -1){
							source = param.replace("source=", "");
						}else if(param.indexOf("item=") > -1){
							item = param.replace("item=", "");
						}
					}
				}else if(StrUtils.isFind(gurl, "(www\\.import-express\\.com/goodsinfo/.*)")){
					//https://www.import-express.com/goodsinfo/accept-royal-emperor-adhere-to-cheese-cheese-stuffed-filling-brail-1536478858901.html
					int index = gurl.lastIndexOf("-");
					item = gurl.substring(index+1).replaceAll("(\\.html)","");
					if(StringUtils.isNotBlank(item)){
						String type = item.substring(0,1);
						item = item.substring(1);
						if("1".equals(type)){
							source = TypeUtils.itemIDToUUID(item, "D") ;
						}else if("2".equals(type)){
							source = TypeUtils.itemIDToUUID(item, "A") ;
						}else if("3".equals(type)){
							source = TypeUtils.itemIDToUUID(item, "N") ;
						}else{
							source = TypeUtils.itemIDToUUID(item, "D") ;
						}
					}
				}
			}
			if(StringUtils.isBlank(source) || StringUtils.isBlank(item)){
				continue;
			}
			//屏蔽数据表filter_data_goods
			int addFilterGoodsUrl = invalidGoodsService.addFilterGoodsUrl(source, item);
			//屏蔽线上产品表goodsdata
			invalidGoodsService.updateGoodsUrl(source,item);
			
			result = addFilterGoodsUrl > 0? "1":"0";
			result = addFilterGoodsUrl < 0? "-2":result;
		}
		return result;
	}
	
	
	/**屏蔽列表
	 * @date 2016年11月28日
	 * @author abc
	 * @param page 页码
	 * @param type 类型 1-商店  0-产品
	 * @return  
	 */
	@RequestMapping(value="/ilist",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ModelAndView getList(String page, String type){
		if(page==null||page.isEmpty()||!StrUtils.isMatch(page, "(\\d+)")){
			page = "1";
		}
		if(type==null||type.isEmpty()||!StrUtils.isMatch(type, "(\\d+)")){
			type = "1";
		}
		
		ModelAndView mv = new ModelAndView("invalidlist");
		
		List<InvalidUrlBean> invalidUrls = invalidGoodsService.getInvalidUrls(Integer.valueOf(page), Integer.valueOf(type));
		int total = 1;
		if(invalidUrls!=null&&!invalidUrls.isEmpty()){
			total = invalidUrls.get(0).getCount();
			total  = total%40 == 0 ? total/40 : total/40+1;
		}
		mv.addObject("currentpage", page);
		mv.addObject("totalpage", total);
		
		mv.addObject("type", type);
		mv.addObject("urlList", invalidUrls);
		
		return mv;
	}
	
	
	/**屏蔽列表
	 * @date 2016年11月28日
	 * @author abc
	 * @param page 页码
	 * @param type 类型 1-商店  0-产品
	 * @return  
	 */
	@RequestMapping(value="/intensve",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ModelAndView getIntensveList(String page, String type){
		if(page==null||page.isEmpty()||!StrUtils.isMatch(page, "(\\d+)")){
			page = "1";
		}
		if(type==null||type.isEmpty()||!StrUtils.isMatch(type, "(\\d+)")){
			type = "1";
		}
		
		ModelAndView mv = new ModelAndView("invalidintensvelist");
		
		List<IntensveBean> invalidUrls = invalidGoodsService.getIntensve();
		mv.addObject("urlList", invalidUrls);
		
		return mv;
	}
	
	
	
}