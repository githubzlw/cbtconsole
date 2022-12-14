package com.cbt.processes.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;
import com.cbt.parse.service.ParseGoodsUrl;
import com.cbt.processes.dao.ISpiderDao;
import com.cbt.processes.dao.SpiderDao;
import com.cbt.processes.utils.Processes;
import com.cbt.util.StrUtils;
import com.cbt.util.Utility;
import com.cbt.warehouse.dao.InventoryMapper;
import com.cbt.warehouse.dao.SpiderMapper;
import com.cbt.warehouse.service.InventoryService;

import ceRong.tools.bean.SearchLog;

@Service
public class SpiderServer implements ISpiderServer {
	ISpiderDao dao = new SpiderDao();
	public static int cartNumber = 0;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SpiderServer.class);
	
	@Autowired
	private InventoryMapper inventoryMapper;
	@Autowired
	private SpiderMapper spiderMapper;
	@Autowired
	private InventoryService inventoryService;
	
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
	
	/******???????????????????????????????????????????????????????????????????????????************************/
	@Override
	public int saveTheSearchLogOnSearchPage(SearchLog seaLog){
		if("0".equals(seaLog.getSaveFlag())){
			spiderMapper.saveTheSearchLogOnSearchPage(seaLog);
			return seaLog.getId();
		}else{
			return spiderMapper.updateTheSearchLogOnSearchPage(seaLog.getId(),seaLog.getProductShowIdList());
		}
	}
	@Override
	public int saveTheClickCountOnSearchPage(String goodsPid,String searchMD5,String searchUserMD5){
		return spiderMapper.saveTheClickCountOnSearchPage(goodsPid,searchMD5,searchUserMD5);
	}

	@Override
	public List<String> FindOdidByShipno(String shipno) {
		List<String> list=new ArrayList<>();
		try {
			list = this.spiderMapper.FindOdidByShipno(shipno);
			return list;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int updataCheckedById(String id) {

		int ret=0;
		try {
			ret = this.spiderMapper.updataCheckedById(id);
			ret=this.spiderMapper.updataIqById(id);
			//????????????
			Map<String, Object> det = inventoryMapper.getInventoryDetailByOdid(Integer.parseInt(id));
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderid",StrUtils.object2Str(det.get("orderno")));
			map.put("odid",StrUtils.object2Str(det.get("od_id")));
			//??????????????????
			map.put("specid",StrUtils.object2Str(det.get("goods_specid")));
			map.put("skuid",StrUtils.object2Str(det.get("good_skuid")));
			map.put("tbskuid",StrUtils.object2Str(det.get("goods_p_skuid")));
			map.put("tbspecid",StrUtils.object2Str(det.get("goods_p_specid")));
			map.put("seiUnit","1");
			String cance_inventory_count = StrUtils.object2NumStr(det.get("goods_number"));
			map.put("cance_inventory_count",cance_inventory_count);
			//????????????????????????????????????????????????????????????
			inventoryService.cancelInventory(map);
			return ret;
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}

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
		//??????sessionid?????????????????????sessionid????????????????????????size???userId???????????????????????????????????????-?????????????????????sessionid?????????userid????????????-??????????????????&??????sessionid???????????????ID????????????
		List<SpiderBean> spider_s = dao.getGoogs_cars(sessionId, 0, 0);
		int res = 0;
		double ma = map.get(currency);
		DecimalFormat df = new DecimalFormat("#0.##");
		for (int i = 0; i < spider_s.size(); i++) {
			SpiderBean spider = spider_s.get(i);
			SpiderBean spiderdb = dao.getGoogs_cars(spider.getItemId(), userId,spider.getUrl(),spider.getSize(),spider.getColor(),spider.getSessionId(),spider.getTypes());
			if(spiderdb != null){
				//??????-??????spiderdb??????ID??????????????????&??????sessionid???????????????ID????????????
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
				//?????????-???????????????ID?????????????????????sessionid?????????userid???
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
	@Override
	public  int delOrderinfo(String orderno){
		return spiderMapper.delOrderinfo(orderno);
	}
}
