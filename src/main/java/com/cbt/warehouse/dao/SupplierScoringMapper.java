package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.SupplierProductsBean;
import com.cbt.warehouse.pojo.SupplierScoringBean;
import com.importExpress.pojo.QueAns;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface SupplierScoringMapper {
	/**
	 * 查询供应商列表
	 * @param level 
	 * @param shop_id 
	 * @Title querySupplierScoringList 
	 * @Description TODO
	 * @return
	 * @return List<SupplierScoring>
	 */
	List<SupplierScoringBean> querySupplierScoringByPage(@Param("startindex") int startindex,
                                                         @Param("pagesize") int pagesize, @Param("shop_id") String shop_id, @Param("level") String level, @Param("quality") String quality, @Param("qualitys") String qualitys,
                                                         @Param("services") String services, @Param("servicess") String servicess, @Param("authorized") String authorized,
                                                         @Param("flag") boolean flag, @Param("userid") String userid,@Param("categoryName") String categoryName,
                                                         @Param("salesShop") Integer salesShop, @Param("nowdate1") String nowdate1, @Param("nowdate2") String nowdate2);

	/**
	 * 判断该店铺是否为精品店铺
	 * @param shopId
	 * @return
	 */
	public SupplierScoringBean getShoptype(@Param("shopId") String shopId);
	/**
	 * 查询总数
	 * @param level
	 * @param shop_id
	 * @Title querySupplierRecord
	 * @Description TODO
	 * @return
	 * @return int
	 */
	int querySupplierRecord(@Param("shop_id") String shop_id, @Param("level") String level, @Param("quality") String quality,
                            @Param("qualitys") String qualitys, @Param("services") String services, @Param("servicess") String servicess,
                            @Param("authorized") String authorized, @Param("flag") boolean flag, @Param("userid") String userid,
                            @Param("categoryName") String categoryName, @Param("salesShop") Integer salesShop,
                            @Param("nowdate1") String nowdate1, @Param("nowdate2") String nowdate2);
	/**
	 * 合作过的供应商
	 * @return
	 */
	public int getCooperatedCount();

	/**
	 * 优质供应商
	 * @return
	 */
	public int getHighCount();

	/**
	 * 黑名单供应商
	 * @return
	 */
	public int getBlacklistCount();
	/**
	 * 普通供应商
	 * @return
	 */
	public int getOrdinaryCount();
	/**
	 * 根据shop_id查询该供应商下对应的产品,没打分也可以
	 * @Title queryProductsByShopId
	 * @Description TODO
	 * @param shop_id
	 * @param userId
	 * @return
	 * @return List<SupplierProducts>
	 */
	List<SupplierProductsBean> queryProductsByShopId(@Param("shop_id") String shop_id, @Param("goodsPid") String goodsPid, @Param("userId") int userId);
	/**
	 * 新增或者修改
	 * @Title saveOrupdateProductScord
	 * @Description TODO
	 * @param supplierProductsBean
	 * @return void
	 */
	void saveOrupdateProductScord(@Param("supplierProductsBean") SupplierProductsBean supplierProductsBean);
	/**
	 * 供应商产品打分页面修改产品评论备注
	 * @param id
	 * @param remark
	 * @return
	 */
	public int updateRemark(@Param("id") String id, @Param("remark") String remark,@Param("newQuality") String newQuality);
	/**
	 * 查询打过分数的产品信息
	 * @Title searchProductListByShopId
	 * @Description TODO
	 * @param shopId
	 * @return
	 * @return List<SupplierProductsBean>
	 */
	List<SupplierProductsBean> searchProductListByShopId(@Param("shop_id") String shopId);
	/**
	 * 查询供应商列表中是否存在该打过分的数据
	 * @Title searchOneScoringByShopId
	 * @Description TODO
	 * @param shopId
	 * @return
	 * @return SupplierScoringBean
	 */
	SupplierScoringBean searchOneScoringByShopId(@Param("shop_id") String shopId);
	/**
	 * 查询店铺评分信息
	 * @param map
	 * @return
	 */
	public List<SupplierProductsBean> getAllShopInfo(Map<String, String> map);

	/**
	 * 查询产品评分信息
	 * @param map
	 * @return
	 */
	public List<SupplierProductsBean> getAllShopGoodsInfo(Map<String, String> map);
	/**
	 * 查询产品评分信息
	 * @param map
	 * @return
	 */
	public List<SupplierProductsBean> getAllShopGoodsInfoList(Map<String, String> map);
	/**
	 * 保存或者更新供应商表中的数据
	 * @Title insertOrupdateScoringScoring
	 * @Description TODO
	 * @param scoringBean
	 * @return void
	 */
	void insertOrupdateScoringScoring(@Param("scoringBean") SupplierScoringBean scoringBean);
	/**
	 * 商品评分管理页面查看该产品问答信息
	 * @param pid
	 * @return
	 */
	public List<QueAns> lookQuestion(@Param("pid") String pid);
	/**
	 * 查询店铺打分信息
	 * @Title querySuppliseScoring
	 * @Description TODO
	 * @param shop_id
	 * @return
	 * @return SupplierScoringBean
	 */
	SupplierScoringBean querySuppliseScoring(@Param("shop_id") String shop_id);
	/**
	 *  查询当前用户的打打分信息(包括单一商品)
	 * @param goodsPid
	 */
	List<SupplierProductsBean> queryOneProductsUserId(@Param("shop_id") String shop_id, @Param("goodsPid") String goodsPid);
	/**
	 *
	 * @Title queryByProductScoreId
	 * @Description TODO
	 * @param goodsPid
	 * @param userId
	 * @return
	 * @return SupplierProductsBean
	 */
	SupplierProductsBean queryByProductScoreId(@Param("goodsPid") String goodsPid, @Param("userId") Integer userId);
	/**
	 *
	 * @Title searchUserScoringByShopId
	 * @Description TODO
	 * @param shop_id
	 * @param userId
	 * @return
	 * @return SupplierScoringBean
	 */
	SupplierProductsBean searchUserScoringByShopId(@Param("shop_id") String shop_id, @Param("userId") Integer userId);
	/**
	 * 保存或者修改店铺打分
	 * @Title saveOrupdateSupplierScoringScord
	 * @Description TODO
	 * @param supplierScoringBean
	 * @param id
	 * @param admName
	 * @return void
	 */
	void saveOrupdateSupplierScoringScord(
            @Param("supplierScoringBean") SupplierScoringBean supplierScoringBean, @Param("userId") Integer id, @Param("userName") String userName);

	/**
	 * 记录当次打分信息
	 * @param map
	 * @return
	 */
	public int saveSupplierProduct(Map<String, String> map);

	/**
	 * 首次不需要算店铺平均分直接插入信息
	 * @param map
	 * @return
	 */
	public int saveSupplierScoring(Map<String, String> map);

	/**
	 * 更新店铺平均质量分数
	 * @param map
	 * @return
	 */
	public int updateSupplierScoring(Map<String, String> map);

	/**
	 *判断该店铺是否已算过平均分
	 * @param map
	 * @return
	 */
	public List<SupplierScoringBean> getSupplierScoring(Map<String, String> map);

	/**
	 * 查询该店铺打分的总质量分
	 * @param map
	 * @return
	 */
	public List<SupplierProductsBean> getAllShopScoring(Map<String, String> map);

	/**
	 * 获取该店铺已经打分的信息
	 * @param map
	 * @return
	 */
	public List<SupplierProductsBean> SupplierProductsBean(Map<String, String> map);
	/**
	 * 查询该供应商的所有打分信息
	 * @Title queryAllScoresSupplier
	 * @Description TODO
	 * @param shop_id
	 * @return
	 * @return List<SupplierProductsBean>
	 */
	List<SupplierProductsBean> querySupplierAllScoresSupplier(@Param("shop_id") String shop_id);
	/**
	 * 库存协议
	 * @Title saveOrUpdateInven
	 * @Description TODO
	 * @param bean
	 * @return void
	 */
	void saveOrUpdateInven(@Param("bean") SupplierScoringBean bean);
	/**
	 * 修改ali_indo_data下的天数
	 * @Title updateAliInfoDataDays
	 * @Description TODO
	 * @param returnDays
	 * @param shopId
	 * @return void
	 */
	void updateAliInfoDataDays(@Param("returnDays") int returnDays, @Param("shopId") String shopId);

    List<SupplierProductsBean> queryWarehouseRemarkByShopId(@Param("shopId") String shopId);
}
