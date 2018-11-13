package com.cbt.service;

import com.cbt.bean.*;
import com.cbt.pojo.Admuser;
import com.cbt.website.util.JsonResult;

import java.util.List;

public interface SingleGoodsService {

	/**
	 * 
	 * @Title saveGoods
	 * @Description 新增商品信息
	 * @param goodsUrl
	 * @param adminId
	 * @param goodsWeight
	 * @param drainageFlag
	 * @return
	 * @return JsonResult
	 */
	public JsonResult saveGoods(String goodsUrl, int adminId, double goodsWeight, int drainageFlag, int goodsType, String aliPid, String aliPrice);

	/**
	 *
	 * @Title queryDealGoods
	 * @Description 查询所有已处理的数据的主图、规格图、橱窗图和详情图的数据
	 * @return 商品数据集合
	 * @return List<CustomGoodsPublish>
	 */
	public List<CustomOnlineGoodsBean> queryDealGoods();

	/**
	 *
	 * @Title queryForList
	 * @Description 根据条件查询数据
	 * @param queryPm
	 * @return 数据集合
	 * @return List<SameTypeGoodsBean>
	 */
	public List<SameTypeGoodsBean> queryForList(SingleQueryGoodsParam queryPm);

	/**
	 *
	 * @Title queryForListCount
	 * @Description 查询结果集总数
	 * @param queryPm
	 * @return 总数
	 * @return int
	 */
	public int queryForListCount(SingleQueryGoodsParam queryPm);

	/**
	 *
	 * @Title queryAllAdmin
	 * @Description 查询所有的管理员数据
	 * @return 数据集合
	 * @return List<Admuser>
	 */
	public List<Admuser> queryAllAdmin();

	/**
	 *
	 * @Title deleteGoodsByPid
	 * @Description 根据pid删除数据
	 * @param pid
	 * @return
	 * @return boolean
	 */
	public boolean deleteGoodsByPid(String pid);


	/**
	 * 分页查询跨境商品信息
	 * @param goodsCheck
	 * @return
	 */
	List<SingleGoodsCheck> queryCrossBorderGoodsForList(SingleGoodsCheck goodsCheck);

	/**
	 * 跨境商品信息总数
	 * @param goodsCheck
	 * @return
	 */
	int queryCrossBorderGoodsForListCount(SingleGoodsCheck goodsCheck);

	/**
	 * 更新跨境商品信息
	 * @param goodsCheck
	 * @return
	 */
	int updateSingleGoodsCheck(SingleGoodsCheck goodsCheck);

	/**
	 * 插入通过的商品到单个商品里面去
	 * @param pid
	 * @return
	 */
	int insertIntoSingleGoodsByIsCheck(String pid);

	/**
	 * 批量更新跨境商品信息
	 * @param goodsList
	 * @return
	 */
	int batchUpdateSingleGoodsCheck(List<SingleGoodsCheck> goodsList);

	/**
	 * 查询跨境商品的类别信息
	 * @param queryPm
	 * @return
	 */
	List<CategoryBean> queryCateroryList(SingleGoodsCheck queryPm);


	/**
	 * 设置跨境商品的搜索图
	 * @param pid
	 * @param imgUrl
	 * @return
	 */
	int setMainImgByPid(String pid, String imgUrl);


	/**
	 * 设置跨境商品店铺的搜索图
	 * @param shopId
	 * @param imgUrl
	 * @return
	 */
	int setMainImgByShopId(String shopId, String imgUrl);

}
