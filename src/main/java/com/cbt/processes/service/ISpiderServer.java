package com.cbt.processes.service;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;

import ceRong.tools.bean.SearchLog;

import java.util.List;
import java.util.Map;

public interface ISpiderServer {

	/**
	 * 添加商品到购物车
	 * 
	 * @param spider
	 * 	购物车信息
	 */
	public int addGoogs_car(SpiderBean spider);

	/**
	 * 用户未登录时购物车存放的物品转入至用户购物车
	 * 
	 * @param spider
	 * 	购物车信息
	 */
	public int LoginGoogs_car(String sessionId, int userId, String currency, Map<String, Double> map);

	/**
	 * 修改购物车的非免邮标志和价格
	 *
	 * @param spider
	 * 	购物车信息
	 */
	public int upGoogs_carFree(List<SpiderBean> spider);


	/**
	 * 删除购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car(String[] goodsid, String guid);

	/**
	 * 修改购物车商品数量
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	/*public Map<String, String> upGoogs_car(String guid,int spiderId,int number,int userid,String sessionId);*/

	/**
	 * 修改购物车商品数量
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(String guid, int spiderid, int number, int userid, String sessionId, String price, String totalvalume, String totalweight);

	/**
	 * 修改购物车商品数量
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public Map<String, String> upGoogs_car(String guid, int spiderid, int number, int userid, String sessionId, Map<String, Double> map);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car(int goodsid, String title, String freight, String remark);

	/**
	 * 修改购物车商品价格
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoodsprice(int goodsid, String price, String sessionId, int userid);


	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID ,是否优惠商品
	 */
	public List<SpiderBean> getGoogs_cars(String session, int userId, int preshopping);

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

	/**
	 * 获取购物车商品的数量
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  int getGoogs_carNum(int userId, String sessionId);

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
	 * 删除用户收藏的商品
	 */
	public int deleteCollection(String id, int userid);

	/**
	 *
	 * 根据url删除用户收藏的商品
	 */
	public int delCollectionByUrl(String url, int userid);

	/**
	 * ylm
	 * 增加用户收藏的商品
	 */
	public int addCollection(CollectionBean collection);
	/**
	 * ylm
	 * 查询用户是否收藏了的该商品
	 */
	public int getCollection(String url, int userid);

	/**
	 * ylm
	 * 保存email
	 */
	public int saveGoodsEmail(int userid, String session, String email);
	
	/**
	 * ylm
	 * 修改商品的运输方式
	 */
	public int upExpreeType(String goodsId, String expreeType, String days, int countryId);

	public int saveTheSearchLogOnSearchPage(SearchLog seaLog);

	public int saveTheClickCountOnSearchPage(String goodsPid, String searchMD5, String searchUserMD5);

	List<String> FindOdidByShipno(String shipno);

	int updataCheckedById(String id);

}
