package com.cbt.website.dao;

import com.cbt.bean.goodsCarPresale;
import com.cbt.website.bean.TblPreshoppingcarInfo;
import com.cbt.website.userAuth.bean.Admuser;

import java.util.List;

public interface shoppingCartDao {

	/**
	 * 查找购物车所有商品信息
	 * @param userid
	 * @return
	 */
	public List<Object[]> getAllUserShopCarList(int userid, Admuser admuser, int beginPage, int pageSize, int isorder, int status);
//	public List<Object[]> getAllUserShopCarList(int userid);

	/**
	 * 获取购物车数目总条数
	 * @param userid
	 * @param beginPage
	 * @param pageSize
	 * @return
	 */
	public int getAllUserShopCarListCount(int userid);

	/**
	 * 根据用户查找用户购物车相似的商品
	 * @param userid
	 * @return
	 */
	public List<Object[]> getShopCarListFromUser(int userid, String goodcarid);

	/**
	 * 根据选择的购物车id查询购物车商品信息
	 * @param userid
	 * @param goodcarid
	 * @return
	 */
	public List<Object[]> getSelectShopCarListFromUser(int userid, String goodcarid);

	/**
	* @Title: 购物车营销 修改价格
	 */
	public int updateGoodsCarPrice(List<Integer> carid, List<Double> price);

	/**
	 * 根据用户查找用户购物车中已购买过的货源
	 * @param userid
	 * @return
	 */
	public List<Object[]> getPurchasedSupplyFromUserId(int userid);


	/**
	 * 根据商品ID获取已购买货源
	 * @param gooddataid
	 * @return
	 */
	public List<Object[]> getPurchasedSupplyFromGoodDataUrl(int userid, Double rate);

	/**
	 * 发客户发送购物车优惠邮件
	 * @param userid
	 * @param caridList
	 * @param priceList
	 * @return
	 */
	public String showEmail(int userid, String caridStr, String seleteStr, String priceStr, String emailTitle, String emailRemark, int action);


	/**
	 * 保存预购物记录
	 * @param preshoppingcar
	 * @return
	 */
	public int savePreshoppingcarInfo(TblPreshoppingcarInfo preshoppingcar);

	public void saveEmailSentMessage(String userid, String goodscarid, String admName);

	public int updateWeight(int carid, double weight, int nunber);

	/**
	 * 获取购物车营销记录
	 * @param userId
	 */
	public List<goodsCarPresale> getGoodsCarPresale(int userId);

	/**
	 * 删除未营销的购物车数据
	 * @param userId
	 * @param goodscarid
	 * @return
	 */
	public int deleteUnMarketData(int userId, String goodscarid, String countList, String oldprice, String newprice, String reduPrice) throws Exception;
	
}