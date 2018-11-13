package com.cbt.processes.dao;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;

import java.util.List;
import java.util.Map;

/**
 * @author ylm
 *
 */
public interface ISpiderDao {

	/**
	 * 添加商品到购物车
	 * 
	 * @param spider
	 * 	购物车信息
	 */
	public int addGoogs_car(SpiderBean spider);
	
	/**
	 * 添加商品类别图片到购物车
	 * 
	 * @param spider
	 * 	购物车信息
	 */
	public int addGoogs_carTypeimg(int gid, String img);
	
	/**
	 * 优惠申请的商品转至购物车
	 * 
	 * @param spider
	 * 	购物车信息
	 */
	public int addGoogs_car(List<SpiderBean> spider, String uCurrency, Map<String, Double> maphl);


	/**
	 * 修改购物车商品状态
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car_state(List<String> spiderId, int state);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(String guid, int spiderId, int number, int userid, String sessionId, String price, String bulk_volume, String total_weight);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(int goodsid, String title, String freight, String remark);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(List<SpiderBean> spider);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(int cartId, int userid, String price);



	/**
	 * 修改购物车商品价格
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoodsprice(int goodsid, String price, String sessionId, int userid);

	/**
	 * 删除购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car(String spiderId, int userid, String sessionId);

	/**
	 * 删除购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car(String[] goodsid, String guid);

	/**
	 * 删除购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car_s(String shopID, int userid, String sessionId);

	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public List<SpiderBean> getGoogs_carsFreeprice(String guid);

	/**
	 * 获取购物车商品的数量
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  int getGoogs_carNum(int userId, String sessionId);
	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  List<SpiderBean> getGoogs_cars(String sessionId, int userId, int preshopping);
	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public SpiderBean getGoogs_cars(String spiderId, int userId, String url, String size, String color, String sessionId, String types);

	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public SpiderBean getGoogs_carsId(String guid, int id, String sessionId, int userId);


	/**
	 * 记录用户搜索过的连接
	 *
	 * @param spider
	 * 	购物车信息
	 */
	public int addURL(String userName, String url, int fruit);


	/**
	 * 获取汇率
	 *
	 * @param
	 */
	public Map<String, Double> getExchangeRate();

	public  int updateFlag(String itemid);

	/**
	 * ylm
	 * 询价支付过渡界面
	 */
	public List<SpiderBean> getInquiryGoods(String[] goodsid);

	/**
	 * ylm
	 * 获取用户收藏的商品
	 */
	public List<CollectionBean> getCollection(int userid);

	/**
	 * ylm
	 * 查询是否已经添加收藏
	 */
	public int getCollection(String url, int userid);

	/**
	 * ylm
	 * 删除用户收藏的商品
	 */
	public int deleteCollection(String ids, int userid);

	/**
	 * ylm
	 * 增加用户收藏的商品
	 */
	public int addCollection(CollectionBean collection);

	/**
	 * ylm
	 * 购物车保存email
	 */
	public int saveGoodsEmail(int userid, String session, String email);
	
	/**
	 * ylm
	 * 修改商品的运输方式
	 */
	public int upExpreeType(String goodsId, String expreeType, String days, int countryId);
	
	/**
	 * 删除收藏品
	 * 
	 * @param url
	 * 
	 */
	public int delCollectionByUrl(String url, int userid);
	
	
	
}
