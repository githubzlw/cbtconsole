package com.cbt.warehouse.service;

import com.cbt.pojo.page.Page;
import com.cbt.warehouse.pojo.SupplierProductsBean;
import com.cbt.warehouse.pojo.SupplierScoringBean;
import com.importExpress.pojo.QueAns;

import java.util.List;
import java.util.Map;

public interface SupplierScoringService {
	/**
	 * 查询所有供应商列表
	 * @param pagesize 
	 * @param start 
	 * @param level 
	 * @param shop_id 
	 * @Title queryList 
	 * @Description TODO
	 * @return
	 * @return List<SupplierScoring>
	 */
	public Page<SupplierScoringBean> queryList(int start, int pagesize, String shop_id, String level, String quality,
                                               String services, String authorizedFlag, boolean flag, String userid,
                                               String categoryName, Integer salesShop, String nowdate1, String nowdate2,int siteFlag,int validFlag);

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
	 * 根据shop_id查询该供应商下对应的产品 若goodsPid不为null则说明只查询一个产品
	 * @Title queryProductsByShopId
	 * @Description TODO
	 * @param shop_id
	 * @param goodsPid
	 * @param integer
	 * @return
	 * @return List<SupplierProducts>
	 */
	public List<SupplierProductsBean> queryProductsByShopId(String shop_id, String goodsPid, int userId);
	/**
	 * 保存或者修改产品分数信息
	 * @Title saveOrupdateProductScord
	 * @Description TODO
	 * @param supplierProductsBean
	 * @return void
	 */
	public void saveOrupdateProductScord(
            SupplierProductsBean supplierProductsBean);

	/**
	 * 供应商产品打分页面修改产品评论备注
	 * @param id
	 * @param reamrk
	 * @return
	 */
	public int updateRemark(String id, String reamrk,String newQuality);
	/**
	 * 查询打过分的产品信息
	 * @Title searchProductListByShopId
	 * @Description TODO
	 * @param shopId
	 * @return
	 * @return List<SupplierProductsBean>
	 */
	public List<SupplierProductsBean> searchProductListByShopId(String shopId);
	/**
	 * 查询供应商列表中是否存在该打过分的数据
	 * @Title searchOneScoringByShopId
	 * @Description TODO
	 * @param shopId
	 * @return
	 * @return SupplierScoringBean
	 */
	public SupplierScoringBean searchOneScoringByShopId(String shopId);

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
	public void insertOrupdateScoringScoring(SupplierScoringBean scoringBean);

	/**
	 * 商品评分管理页面查看该产品问答信息
	 * @param pid
	 * @return
	 */
	public List<QueAns> lookQuestion(String pid);
	/**
	 * 查询当前用户的打打分信息
	 * @Title queryOneProductsUserId
	 * @Description TODO
	 * @param shop_id
	 * @param goodsPid
	 * @param goodsPid
	 * @param id
	 * @return
	 * @return SupplierProductsBean
	 */
	public List<SupplierProductsBean> queryOneProductsUserId(String shop_id, String goodsPid);
	/**
	 *
	 * @Title queryByProductScoreId
	 * @Description TODO
	 * @param goodsPid
	 * @param id
	 * @return
	 * @return SupplierProductsBean
	 */
	public SupplierProductsBean queryByProductScoreId(String goodsPid,
                                                      Integer id);
	/**
	 * 查询该用户对该店铺打过的分数
	 * @Title searchUserScoringByShopId
	 * @Description TODO
	 * @param shop_id
	 * @param integer
	 * @return
	 * @return SupplierScoringBean
	 */
	public SupplierProductsBean searchUserScoringByShopId(String shop_id, Integer integer);
	/**
	 * 保存或者修改店铺打分
	 * @Title saveOrupdateSupplierScoringScord
	 * @Description TODO
	 * @param supplierScoringBean
	 * @param id
	 * @param admName
	 * @return void
	 */
	public void saveOrupdateSupplierScoringScord(
            SupplierScoringBean supplierScoringBean, Integer id, String admName);

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
	 * 判断该店铺是否已算过平均分
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
	 * 查询该供应商的所有打分信息
	 * @Title queryAllScoresSupplier
	 * @Description TODO
	 * @param shop_id
	 * @return
	 * @return List<SupplierProductsBean>
	 */
	public List<SupplierProductsBean> querySupplierAllScoresSupplier(String shop_id);
	/**
	 * 修改库存协议
	 * @Title saveOrUpdateInven 
	 * @Description TODO
	 * @param bean
	 * @return void
	 */
	public void saveOrUpdateInven(SupplierScoringBean bean);

    List<SupplierProductsBean> queryWarehouseRemarkByShopId(String shopId);
}
