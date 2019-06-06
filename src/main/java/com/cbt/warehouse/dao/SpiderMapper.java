package com.cbt.warehouse.dao;

import java.util.List;
import java.util.Map;

import com.importExpress.pojo.SpiderNewBean;
import org.apache.ibatis.annotations.Param;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;
import com.cbt.warehouse.pojo.TypeAvgWeightBean;

import ceRong.tools.bean.SearchLog;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SpiderMapper {

	public Integer getMaxGoodsdata(String url);

	/**
	 * 添加商品到购物车
	 *
	 * @param spider
	 * 	购物车信息
	 */
	public int addGoogs_car(SpiderBean spider);

	public Integer addGoogs_carTypeimg(@Param("goodsId")int goodsId, @Param("img")String img);

	//	public Integer batchAddGoogs_car(Map<String, List<SpiderBean>> map);
	public Integer batchAddGoogs_car( @Param("userId") int userId, @Param("spdMap") List<SpiderNewBean> map);

	public int upGoogs_car_state(@Param("spiderIds")List<String> spiderIds, @Param("state")int state);

	/*public SpiderBean getGoogs_carsId(@Param("guid")String guid, @Param("id")int id, @Param("sessionId")String sessionId, @Param("userId")int userId,int s);*/

	/*public int upGoogs_car(@Param("guid")String guid, @Param("spiderId")int spiderId, @Param("number")int number, @Param("userid")int userid,
			@Param("sessionId")String sessionId, @Param("price")String price, @Param("totalvalume")String totalvalume, @Param("totalweight")String totalweight);*/

	public int delTblPreshoppingcarInfo(@Param("goodsid")String[] goodsid);

	/**
	 * 记录用户搜索过的连接
	 *
	 * @param spider
	 * 	购物车信息
	 */
	public Integer addURL(@Param("userName")String userName, @Param("url")String url,@Param("state")int state);

	/**
	 * 修改购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogsCar(@Param("guid")String[] guid,@Param("title")String title, @Param("freight")String freight, @Param("remark")String remark);

	/**
	 * 获取汇率
	 *
	 * @param
	 */
	public List<Map<String, Object>> getExchangeRate();

	public Double getExchangeRateByCountry(String country);

	/**
	 * 获取购物车商品的数量
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  int getGoogs_carNum(@Param("userId")int userId,@Param("sessionId")String sessionId);

	public  int upCustom(@Param("sessionId")String sessionId,@Param("userId")int userId, @Param("email")String email,@Param("rfq")String rfq,@Param("custype")String custype);

	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID ,是否优惠商品
	 */
	public List<Map<String, Object>> getGoogs_cars(@Param("sessionid")String sessionid,@Param("userid")int userid,@Param("preshopping")int preshopping,@Param("type") int type);

	/**
	 * 修改购物车商品价格
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoodsprice(@Param("goodsid")int goodsid,@Param("price")String price,@Param("sessionid")String sessionid,@Param("userid")int userid);

	/**
	 * ylm
	 * 询价支付过渡界面
	 */
	public List<SpiderBean> getInquiryGoods(@Param("goodsid")String[] goodsid);

	/**
	 * ylm
	 * 获取用户收藏的商品
	 */
	public List<CollectionBean> getCollectionByUserid(int userid);

	/**
	 * ylm
	 * 删除用户收藏的商品
	 */
	public int deleteCollection(@Param("ids")String[] ids,@Param("userid")int userid);

	/**
	 * ylm
	 * 查询用户是否收藏了的该商品
	 */
	public Integer getCollectionId(@Param("url")String url,@Param("userid")int userid);

	/**
	 * ylm
	 * 增加用户收藏的商品
	 */
	public int addCollection(CollectionBean collection);

	public int updateGoodsEmailByUserid(@Param("userid")int userid, @Param("email")String email);

	public int updateGoodsEmailBySessionid(@Param("sessionid")String sessionid, @Param("email")String email);


	/**
	 * ylm
	 * 修改商品的运输方式
	 */
	public int upExpreeType(@Param("goodsIds")String goodsIds, @Param("expreeType")String expreeType, @Param("days")String days, @Param("countryId")int countryId);

	/**
	 *
	 * 根据url删除用户收藏的商品
	 */
	public int delCollectionByUrl(@Param("url")String url, @Param("userid")int userid);

	public Integer batchUpGoogs_car(@Param("spiders")List<SpiderBean> spiders);


	public SpiderBean getGoogsCar(@Param("itemId")String itemId, @Param("userId")int userId, @Param("url")String url,
								  @Param("size")String size, @Param("color")String color, @Param("sessionId")String sessionId, @Param("types")String types);

	/**
	 * 删除购物车批量优惠申请商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car_preferential(@Param("userId") int userId,@Param("sesisonId") String sesisonId,@Param("preferential") String preferential);
	/**
	 * 删除购物车商品
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int delGoogs_car(@Param("goodsid")String[] goodsid,@Param("guid")String[] guid,@Param("userId") int userId,@Param("sesisonId") String sesisonId);

	/**
	 * 修改购物车商品价格
	 *
	 * @param spiderId
	 * 		购物车ID
	 */
	public int upGoogs_car_price(@Param("map_price")Map<String, String> map_price);
	/********************************************com.cbt.pay.dao ************************************************************************/

	/**
	 * wanyang
	 *
	 * @param spider
	 * 获取购物车商品的其他信息（交期，订量等等）
	 */
	public  List<SpiderBean> getSelectedItem(@Param("userid")int userid,@Param("itemid")String[] itemid);

	/**
	 * 获取批量优惠申请信息
	 *
	 * @param userid
	 * @param itemid
	 *
	 */
	public List<SpiderBean> getPreferentialInfo(@Param("userid")int userid,@Param("itemid")String itemid);

	public List<SpiderBean> getShippingInfo(@Param("userid")int userid,@Param("itemid")String itemid);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取购物车商品的其他信息（价格，订量等等）
	 */
	public List<Map<String, Object>> getSelectedItemPrice(@Param("userid")int userid,@Param("itemid")String[] itemid);

	public List<Map<String, Object>> getPreferentialPrice(@Param("userid")int userid,@Param("itemid")String itemid);

	public List<SpiderBean> getPreferential(@Param("userid")int userid,@Param("itemid")String itemid);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取订单信息，地址，交期
	 */
	public List<Map<String, String>> getOrderInfo(@Param("orderNo")String[] orderNo);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取订单详情信息，产品名称，总价
	 */
	public List<Map<String, Object>> getOrderDetails(@Param("orderNo")String[] orderNo);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取购物车商品的ID和数量\价格、是否免邮
	 */
	public List<Map<String, Object>> getGid_numbers(@Param("userId")int userId, @Param("sessionId")String sessionId,@Param("itemid")String[] itemid);

	/**
	 * wanyang
	 *
	 * @param spider
	 * 获取购物车商品信息（类别总金额，类别名称）
	 */
	public List<Object[]> getSelectedItemVat(@Param("itemid")String[] itemid);
	/********************************************----- ************************************************************************/

	/**
	 * yang_tao
	 * 更新goods_car表数据
	 * @param spiders
	 * @return
	 *//*
	public int updateGoodsCar(@Param("spiders")List<SpiderBean> spiders) throws Exception;*/
	public int up_carPrice(@Param("spiders")List<SpiderBean> spiders,@Param("userId") int userId,@Param("sessionId") String sessionId,@Param("type") String type);
	public int login_carUser(@Param("spiderIds")List<Integer> spiderIds,@Param("userId") int userId);

	/**
	 * ylm
	 *
	 * @param spider
	 * 获取类别平均重量
	 */
	public List<TypeAvgWeightBean> getCatid_Filter_AvgWeight();

	public List<Map<String, Object>> getGoodsInfoByOrderNo(@Param("orderNo") String orderNo, @Param("goods_id") String[] goods_id);
	/**
	 * 搜索页面记录日志 qiqing 2018/04/11
	 */
	public int saveTheSearchLogOnSearchPage(SearchLog seaLog);
	public int updateTheSearchLogOnSearchPage(@Param("rowid") int rowid, @Param("productShowIdList") String productShowIdList);
	public int saveTheClickCountOnSearchPage(@Param("goods_id") String goodsPid, @Param("searchmd5") String searchMD5, @Param("searchusermd5") String searchUserMD5);

	public List<Map<String, Object>> getGoodsInfoByUserid(@Param("orderNo") String orderNo);
    @Select("SELECT id FROM order_details WHERE shipno=#{shipno} AND checked=1")
    List<String> FindOdidByShipno(@Param("shipno") String shipno);
    @Update("UPDATE order_details SET checked=0,yourorder=0 WHERE id=#{id}")
	int updataCheckedById(@Param("id") String id);
    @Update("update id_relationtable SET itemqty=0 WHERE odid=#{id}")
    int updataIqById(@Param("id") String id);

    @Update("update orderinfo set del = 1 where order_no=#{orderno}")
    int delOrderinfo(@Param("orderno") String orderno);

}
