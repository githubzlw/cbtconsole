package com.cbt.processes.service;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.processes.dao.ISpiderDao;
import com.cbt.processes.dao.SpiderDao;
import com.cbt.processes.utils.Processes;
import com.cbt.util.Utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiderServer implements ISpiderServer {
	ISpiderDao dao = new SpiderDao();
	public static int cartNumber = 0;
	private static final Log LOG = LogFactory.getLog(SpiderServer.class);
	@Override
	public int addGoogs_car(SpiderBean spider) {
		int res = dao.addGoogs_car(spider);
		if(Utility.getStringIsNull(spider.getImg_type())){
			dao.addGoogs_carTypeimg(res, spider.getImg_type());
		}
		return res;
	}
	
	@Override
	public Map<String, String> upGoogs_car(String guid,int spiderid,int number,int userid,String sessionId,Map<String, Double> mms){
		SpiderBean spider = dao.getGoogs_carsId(guid, 0,sessionId,userid);
		String price = spider.getPrice();
		boolean isPwprice = Utility.getStringIsNull(spider.getpWprice());
		double mm = mms.get(spider.getCurrency())/mms.get("USD");
		if(isPwprice){
			price = Processes.getWPrice(spider.getpWprice(), number, mm, 0);
		}
		spider.setBulk_volume(ParseGoodsUrl.calculateVolume(number, spider.getWidth(), spider.getSeilUnit(), spider.getGoodsUnit()));
		spider.setTotal_weight(ParseGoodsUrl.calculateWeight(number,spider.getPerWeight(), spider.getSeilUnit(), spider.getGoodsUnit()));
		dao.upGoogs_car(guid, spiderid, number, userid,sessionId,price, spider.getBulk_volume() , spider.getTotal_weight());
		Map<String, String> map = new HashMap<String, String>();
		map.put("price", price);
		map.put("number", number+"");
		map.put("totalweight", spider.getTotal_weight());
		map.put("totalvalume", spider.getBulk_volume());
		return map;
	}
	
	@Override
	public int upGoogs_car(String guid,int spiderid,int number,int userid,String sessionId,String price1,String totalvalume,String totalweight){
		return dao.upGoogs_car(guid, spiderid, number, userid, sessionId, price1, totalvalume, totalweight);
	}

	@Override
	public int delGoogs_car(String[] goodsid, String guid) {
		int res = dao.delGoogs_car(goodsid,guid);
		return res;
	}

	@Override
	public int addURL(String userName, String url, int fruit) {
		int res = dao.addURL(userName, url,fruit);
		return res;
	}
 

	@Override
	public int upGoogs_car(int goodsid,String title, String freight, String remark) {
		int res = dao.upGoogs_car(goodsid, title, freight, remark);
		return res;
	}
	

	@Override
	public Map<String, Double> getExchangeRate() {
		Map<String, Double> er = dao.getExchangeRate();
		return er;
	}

	@Override
	public int LoginGoogs_car(String sessionId, int userId,String currency,Map<String, Double> map) {
		//获取sessionid的购物车，根据sessionid中的链接、颜色、size、userId查询是否存在该商品，不存在-修改商品（删除sessionid，添加userid）。存在-修改商品数量&根据sessionid中的购物车ID删除商品
		List<SpiderBean> spider_s = dao.getGoogs_cars(sessionId, 0, 0);
		int res = 0;
		double ma = map.get(currency);
		DecimalFormat df = new DecimalFormat("#0.##");
		for (int i = 0; i < spider_s.size(); i++) {
			SpiderBean spider = spider_s.get(i);
			SpiderBean spiderdb = dao.getGoogs_cars(spider.getItemId(), userId,spider.getUrl(),spider.getSize(),spider.getColor(),spider.getSessionId(),spider.getTypes());
			if(spiderdb != null){
				//存在-根据spiderdb中的ID修改商品数量&根据sessionid中的购物车ID删除商品
					int number = spiderdb.getNumber()+spider.getNumber();
					String price = spiderdb.getPrice();
					boolean isPwprice = Utility.getStringIsNull(spider.getpWprice());
					//double mm = ma/map.get(spider.getCurrency());
					if(isPwprice){
						price = Processes.getWPrice(spiderdb.getpWprice(), number,1, 0);
					}
					
					spider.setBulk_volume(ParseGoodsUrl.calculateVolume(number, spider.getWidth(), spider.getSeilUnit(), spider.getGoodsUnit()));
					spider.setTotal_weight(ParseGoodsUrl.calculateWeight(number,spider.getPerWeight(), spider.getSeilUnit(), spider.getGoodsUnit()));
					dao.upGoogs_car("",spiderdb.getId(), number, userId,sessionId, price, spider.getBulk_volume() , spider.getTotal_weight());
					String [] word = {spiderdb.getId()+""};
					dao.delGoogs_car(word,"");
					res = 1;
			}else{
				//不存在-根据购物车ID修改商品（删除sessionid，添加userid）
				String price = spider.getPrice();
				double mm = ma/map.get(spider.getCurrency());
				String sprice = df.format(Double.parseDouble(price)*mm);
				dao.upGoogs_car(spider.getId(), userId, spider.getPrice());
			}
		}
		return res;
	}


	@Override
	public int getGoogs_carNum(int userId, String sessionId) {
		int num = dao.getGoogs_carNum(userId, sessionId);
		return num;
	}


	@Override
	public List<SpiderBean> getGoogs_cars(String sessionId, int userId,int preshopping) {
		List<SpiderBean> lis = new ArrayList<SpiderBean>();
		lis = dao.getGoogs_cars(sessionId,userId,preshopping);
		cartNumber =lis.size();
		return lis;
	}


	@Override
	public int upGoodsprice(int goodsid, String price,String sessionId,int userid) {
		return dao.upGoodsprice(goodsid, price, sessionId, userid);
	}


	@Override
	public List<SpiderBean> getInquiryGoods(String[] goodsid) {
		List<SpiderBean> list = null;
		if(goodsid != null){
			list = dao.getInquiryGoods(goodsid);
		}
		return list;
	}


	@Override
	public List<CollectionBean> getCollection(int userid) {
		return dao.getCollection(userid);
	}


	@Override
	public int deleteCollection(String ids,int userid) {
		// TODO Auto-generated method stub
		return dao.deleteCollection(ids,userid);
	}


	@Override
	public int addCollection(CollectionBean collection) {
		int res = dao.getCollection(collection.getUrl(), collection.getUserid());
		if(res > 0){
			return 2;
		}else{
			return dao.addCollection(collection);
		}
	}

	@Override
	public int saveGoodsEmail(int userid, String session, String email) {
		int res = dao.saveGoodsEmail(userid, session, email);
		/*if(res>0){
			IUserServer userServer = new UserServer();
			userServer.upUserApplicableCredit(userid, 50);
		}*/
		return res;
	}

	@Override
	public int upExpreeType(String goodsId, String expreeType, String days,
			int countryId) {
		return dao.upExpreeType(goodsId, expreeType, days, countryId);
	}

	@Override
	public int getCollection(String url,int userid) {
		return dao.getCollection(url, userid);
	}

	@Override
	public int delCollectionByUrl(String url, int userid) {
		// TODO Auto-generated method stub
		return dao.delCollectionByUrl(url,userid);
	}

	@Override
	public int upGoogs_carFree(List<SpiderBean> spider) {
		int res = dao.upGoogs_car(spider);
		return res;
	}

}
