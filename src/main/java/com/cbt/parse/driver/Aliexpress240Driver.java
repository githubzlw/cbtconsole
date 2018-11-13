package com.cbt.parse.driver;

import com.cbt.parse.bean.ParamBean;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.website.bean.AliExpressTop240Bean;
import com.cbt.website.service.AliExpress240Sercive;
import com.cbt.website.service.IAliExpress240Sercive;

import java.util.ArrayList;

public class Aliexpress240Driver {
	
	/**本地有货源的商品搜索集合
	 * @return
	 */
	public ArrayList<SearchGoods> getSearchGoods(int results_typeid,String keyword,String typeid,String sort,String page){
		IAliExpress240Sercive  ali_240_service = new AliExpress240Sercive();
		ArrayList<SearchGoods>  list = new ArrayList<SearchGoods>();
		page = page==null?"1":page.replaceAll("\\D+", "").trim();
		page = page.isEmpty()?"1":page;
		ArrayList<AliExpressTop240Bean> aliExpress240s = ali_240_service.getAliExpress240(results_typeid,sort,Integer.parseInt(page));
		int aliExpress240s_num = aliExpress240s.size();
		SearchGoods search_goods = null;
		int total = aliExpress240s_num==0?0:aliExpress240s.get(0).getTotal();
		for(int i=0;i<aliExpress240s_num;i++){
			AliExpressTop240Bean bean = aliExpress240s.get(i);
			search_goods = new SearchGoods();
			search_goods.setGoods_url(bean.getAliexpress_url());
			search_goods.setGoods_solder(""+bean.getSales());
			search_goods.setGoods_free(bean.getGfree());
			search_goods.setGoods_name(bean.getGname());
			search_goods.setGoods_price(bean.getPrice());
			search_goods.setGoods_image(bean.getGimgurl());
			search_goods.setGoods_minOrder(bean.getMinOrder());
			search_goods.setKey_type("goods");
			list.add(search_goods);
		}
		ParamBean  param = new ParamBean();
		total = total%40==0?total/40:total/40+1;
		param.setAmount(total);
		param.setCatid(typeid);
		param.setCom("goodsTypeServerlet");
		param.setCurrent(Integer.parseInt(page));
		param.setKeyword(keyword);
		sort = "0".equals(sort)?"bbPrice-asc":"order-desc";
		param.setSort(sort);
		param.setWebsite("a");
		list.addAll(SearchEngine.page(param));
		list.addAll(SearchEngine.category(keyword, typeid));
		return list;
	}

}
