package com.cbt.website.service;

import com.cbt.bean.GoodsCheckBean;
import com.cbt.util.Utility;
import com.cbt.website.bean.AliExpressTop240Bean;
import com.cbt.website.bean.GoodsSource;
import com.cbt.website.dao.AliExpress240Dao;
import com.cbt.website.dao.IAliExpress240Dao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AliExpress240Sercive implements IAliExpress240Sercive {

	private IAliExpress240Dao dao = new AliExpress240Dao();
	/*@Override
	public int saveAliExpress240(
			List<AliExpressTop240Bean> aliExpressTop240Beans, String keyword, String typeid, int results_typeid) {
		return dao.saveAliExpress240(aliExpressTop240Beans,  keyword,  typeid, results_typeid);
	}*/

	@Override
	public List<AliExpressTop240Bean> getAliExpress240s(String typeId,
                                                        String keyword) {
		// TODO Auto-generated method stub
		return dao.getAliExpress240s(typeId, keyword);
	}

	@Override
	public int saveSearch240_type(String keyword,String typeid,int pagenum) {
		// TODO Auto-generated method stub
		return dao.saveSearch240_type(keyword, typeid, pagenum);
	}

	@Override
	public List<String[]> getSearch240_type(List<String[]> search240_type) {
		// TODO Auto-generated method stub
		return dao.getSearch240_type(search240_type);
	}
	
	
	@Override
	public ArrayList<AliExpressTop240Bean> getAliExpress240(int results_typeid, String sort, int page) {
		
		return dao.getAliExpress240(results_typeid, sort,page);
	}

	@Override
	/**查询关键词和类别是否存在数据集合
	 * @param keyword
	 * @param typeid
	 * @return
	 */
	public HashMap<String, String> getSearch240_typeCount(String keyword,String typeid) {
		
		return dao.getSearch240_typeCount(keyword, typeid);
	}
	


	@Override
	public List<String[]> getSearch240_type() {
		return dao.getSearch240_type();
	}

	@Override
	public List<GoodsCheckBean> getGoodsCheckBeans(int id) {
		return dao.getGoodsCheckBeans(id);
	}

	@Override
	public String saveGoodsSourch(int search_number, int typeId, String datainfo) {
		String res = "";
		try {
			List<GoodsSource> goodsSources = new ArrayList<GoodsSource>();
			List<Integer> gids = new ArrayList<Integer>();//从有变无货源
			List<Integer> gids_ = new ArrayList<Integer>();//从无变有货源
			JSONArray ja = JSONArray.fromObject(datainfo.replace("\r\n","\\r\\n"));
			for (int i = 0; i < ja.size(); i++) {
				JSONObject json = (JSONObject) ja.get(i);
				int gid = json.getInt("gid");
				String url = json.getString("url");
				String purl = json.getString("purl");
				String img = json.getString("img");
				String price = json.getString("price");
				String name = json.getString("name");
				String sourceType = json.getString("sourceType");
				int state = json.getInt("state");//原纪录是否有货源
				if(!Utility.getStringIsNull(url)){//无货源商品
					gids.add(gid);
				}else{
					GoodsSource goodsSource = new GoodsSource();
					goodsSource.setGoodsId(0);
					goodsSource.setGoodsImg(img);
					goodsSource.setGoodsName(name);
					goodsSource.setGoodsPrice(price);
					goodsSource.setGoodsPurl(purl);
					goodsSource.setGoodsUrl(url);
					goodsSource.setSourceType(sourceType);
					goodsSource.setOrderDesc("TB".equals(sourceType)?10:50);
					goodsSources.add(goodsSource);
					if(state == 1){
						gids_.add(gid);
					}
				}
			}
			System.out.println(datainfo);
			//修改tab_top240results_type表的search_number有货商品数量examinetime审查时间
			dao.upSearch240_type(search_number, typeId);
			//修改tab_top240results表state无货源0
			if(gids.size()>0){
				dao.upSearch240(gids, 1);
			}
			//修改tab_top240results表state有货源1
			if(gids_.size()>0){
				dao.upSearch240(gids_, 0);
			}
			//增加goods_source数据
			int sult = dao.addGoodsSource(goodsSources);
			res = sult > 0 ? "保存成功" : "保存失败";
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	@Override
	public int addGoodsSource(GoodsSource bean, int typeId, int state, int gid) {
		if(state == 1){
			dao.upSearch240_type_number(typeId);
		}
		int res = dao.addGoodsSource(bean);
		List<Integer> gids_ = new ArrayList<Integer>();//从无变有货源
		gids_.add(gid);
		dao.upSearch240(gids_, 0);
		return res;
	}

}
