package com.cbt.warehouse.dao;

import com.cbt.bean.CustomGoodsBean;
import com.cbt.warehouse.pojo.HotSellingCategory;
import com.cbt.warehouse.pojo.HotSellingGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HotGoodsMapper {

	/**
	 * 查询所有热卖区类别
	 * 
	 * @return
	 */
	public List<HotSellingCategory> queryForList();

	/**
	 * 修改单个热卖区类别数据
	 * 
	 * @param category
	 * @return
	 */
	public int updateHotSellingCategory(HotSellingCategory category);

	/**
	 * 根据热卖商品类别id获取热卖类别的商品
	 * 
	 * @param hotSellingCategoryId
	 * @return
	 */
	public List<HotSellingGoods> queryByHotSellingCategory(@Param("hotCategoryId") int hotCategoryId, @Param("hotType") int hotType);

	/**
	 * 新增热卖商品
	 * 
	 * @param hsGoods
	 * @return
	 */
	public int insertHotSellingGoods(HotSellingGoods hsGoods);

	/**
	 * 
	 * @Title deleteGoodsByPid
	 * @Description 根据热卖类别id和pid删除商品
	 * @param categoryId
	 *            : 热卖类别id
	 * @param goodsPid
	 * @return int
	 */
	public int deleteGoodsByPid(@Param("categoryId") int categoryId, @Param("goodsPid") String goodsPid);

	/**
	 * 修改热卖商品
	 * 
	 * @param hsGoods
	 * @return
	 */
	public int updateHotSellingGoods(HotSellingGoods hsGoods);

	/**
	 * 根据热卖商品id删除临时表数据
	 * 
	 * @param hsGoodsId
	 * @return
	 */
	public int deleteHotSellingGoodsTmp(int hsGoodsId);

	/**
	 * 调用存储过程获取热门商品的类别以及类别下的商品
	 * 
	 * @return
	 */
	public void genHotCategoryAndGoods();

	/**
	 * 获取存储过程下生成的商品，临时表的商品数据
	 * 
	 * @return
	 */
	public List<HotSellingGoods> queryForByTmp();

	/**
	 * 设置有效商品Pid
	 * 
	 * @param hsGoodsId
	 * @param goodsPid
	 * @return
	 */
	public int updateHotSellingGoodsPid(@Param("hsGoodsId") int hsGoodsId, @Param("goodsPid") String goodsPid);

	/**
	 * 设置商品valid标识为1，为有效商品
	 * 
	 * @return
	 */
	public int updateHotSellingGoodsValid();

	/**
	 * 更新商品图片链接，去掉小图后缀
	 * 
	 * @param hsGoodsId
	 * @param goodsImgUrl
	 * @return
	 */
	public int updateHotSellingGoodsImgUrl(@Param("hsGoodsId") int hsGoodsId, @Param("goodsImgUrl") String goodsImgUrl);

	/**
	 * 删除无图片链接的商品
	 * 
	 * @param hsGoodsId
	 * @param goodsImgUrl
	 * @return
	 */
	public int deleteHotSellingGoodsByImgUrl();

	/**
	 * 查询需要插入到线上的类别数据，category_tmp
	 * 
	 * @return
	 */
	public List<HotSellingCategory> queryInsertCategory();

	/**
	 * 查询需要插入到线上的类别商品数据，goods_tmp
	 * 
	 * @return
	 */
	public List<HotSellingGoods> queryInsertGoods();

	/**
	 * 批量插入类别数据到电商线上
	 * 
	 * @param ctLst
	 * @return
	 */
	public int insertCategoryToOnLine(List<HotSellingCategory> ctLst);

	/**
	 * 批量插入类别商品数据到电商线上
	 * 
	 * @param gdLst
	 * @return
	 */
	public int insertGoodsToOnLine(List<HotSellingGoods> gdLst);

	/**
	 * 更新本地类别插入标识，insert_flag =0
	 * 
	 * @return
	 */
	public int updateInsertCategory();

	/**
	 * 更新本地类别商品插入标识，insert_flag =0
	 * 
	 * @return
	 */
	public int updateInsertGoods();

	/**
	 * 更新表的关联关系Category-Goods
	 * 
	 * @return
	 */
	public int updateRelationship();

	/**
	 * 根据pid获取1688商品信息
	 * 
	 * @param pid
	 * @return
	 */
	public CustomGoodsBean queryFor1688Goods(String pid);

	/**
	 * 
	 * @Title checkExistsGoods
	 * @Description 查询类别下的商品个数
	 * @param categoryId
	 * @param goodsPid
	 * @return
	 * @return boolean
	 */
	public int queryExistsGoodsCount(@Param("categoryId") int categoryId, @Param("goodsPid") String goodsPid);

	/**
	 * 
	 * @Title insertHotGoodsUse
	 * @Description 插入 正在使用的热卖商品表
	 * @param goodsPid
	 * @return
	 * @return boolean
	 */
	public boolean insertHotGoodsUse(String goodsPid);

	/**
	 * 
	 * @Title useHotGoodsByState
	 * @Description 是否使用热卖商品
	 * @pidsMap
	 * @return
	 * @return int
	 */
	public int useHotGoodsByState(@Param("pidsMap") Map<String, String> pidsMap, @Param("categoryId") int categoryId);
	
	/**
	 * 
	 * @Title deleteHotUseGoodsByPid 
	 * @Description 删除可用热卖商品数据
	 * @param goodsPid
	 * @return
	 * @return int
	 */
	public int deleteHotUseGoodsByPid(String goodsPid);

}
