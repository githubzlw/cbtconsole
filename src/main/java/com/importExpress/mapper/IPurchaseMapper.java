package com.importExpress.mapper;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.pojo.StraightHairPojo;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.pojo.*;
import com.cbt.website.bean.PrePurchasePojo;
import com.cbt.website.bean.PurchasesBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IPurchaseMapper {
	/**
	 * 订单采购详情页面数据查询
	 * @param pagesize
	 * @param admin
	 * @param user
	 * @param orderno
	 * @param good
	 * @param date
	 * @param dayy
	 * @param states
	 * @param unpaid
	 * @param orderid_no_array
	 * @param goodsid
	 * @param goodname
	 * @return
	 */
	public List<Map<String, String>> findPageByCondition(@Param("startindex") int startindex, @Param("pagesize") int pagesize,
                                                         @Param("admin") int admin, @Param("user") int user, @Param("orderno") String orderno,
                                                         @Param("good") int good, @Param("date") String date, @Param("dayy") String dayy,
                                                         @Param("states") int states, @Param("unpaid") int unpaid, @Param("orderid_no_array") String orderid_no_array,
                                                         @Param("goodsid") String goodsid, @Param("goodname") String goodname);

	/**
	 * 获取订单信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getOrders(@Param("orderNo") String orderNo);

	/**
	 * 获取订单备注
	 * @param orderNo
	 * @param goodsdataid
	 * @param goodid
	 * @return
	 */
	public OrderProductSource ShowRmark(@Param("orderNo") String orderNo, @Param("goodsdataid") int goodsdataid, @Param("goodid") int goodid, @Param("odid") String odid);
	public String getUserName(@Param("adminid") int adminid);
	/**
	 * 采购详情查询山沟替换日志
	 * @param map
	 * @return
	 */
	public List<ChangeGoodsLogPojo> getDetailsChangeInfo(Map<String,String> map);
	/**
	 * 通过id查询采购账号名称
	 * @return
	 */
	public String queryBuyCount(@Param("admuserid") int admuserid);
	/**
	 * 获取需要确认货源的商品信息
	 * @param orderid
	 * @param adminid
	 * @return
	 */
	public List<Map<String,String>> getAllNewDatas(@Param("orderid") String orderid, @Param("adminid") int adminid);

	/**
	 * 获取订单的商品号
	 * @param orderid
	 * @return
	 */
	public List<String> getGoodsisList(@Param("orderid") String orderid, @Param("adminid") int adminid);

	public int updateLockInventory(Map<String, String> map);

	public int updateInventory(Map<String, String> map);

	public String getGoodsid(Map<String, String> map);

	public int updateCustomSstockFlag(Map<String, String> map);

	public int updateDetailsRemark(Map<String, String> map);
	/**
	 * 获取某个采购的订单采购商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getPurchaseCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	
	/**
	 * 检查采购数量是否正确
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int checkPurchaseCount(@Param("orderid") String orderid);
	
	/**
	 * 获取某个采购的订单分配商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getFpCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取该订单采购与销售的沟通
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getGoodsInfo(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	List<String> getNoShipInfoOrder(Map<String, String> map);
	/**
	 * 超过1天未发货
	 * @param map
	 * @return
	 */
	public String getNotShipped(Map<String, Object> map);
	/**
	 *发货3天未入库
	 * @param map
	 * @return
	 */
	public String getShippedNoStorage(Map<String, Object> map);
	//当日分配采购种类
	public String getDistributionCount(Map<String, Object> map);
	//获得采购数量
	public String getCgCount(Map<String, Object> map);
	//获得实际采购数量
	public String getSjCgCount(Map<String, Object> map);
	OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map);
	//点了采购确认
	List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map);
	List<PurchasesBean> getFpOrderDetails(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取入库没有匹配到商品的订单
	 * @Title getNoMatchOrderByTbShipno
	 * @Description TODO
	 * @param map
	 * @return
	 * @return OrderInfoCountPojo
	 */
	OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map);
	/**
	 * 获取验货有问题数量
	 * @param orderid
	 * @return
	 */
	List<String> getProblem(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	//当月分配采购种类
	public String getfpCount(Map<String, Object> map);
	//获得每月采购数量cjc 1-11
	public String getMCgCount(Map<String, Object> map);
	/**
	 * 获取某个采购的订单入库商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getStorageCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	public List<String> getAllGoodsisList(@Param("orderid") String orderid, @Param("adminid") int adminid);
	/**
	 * 采购前置页面数据条数
	 * @param map
	 * @return
	 */
	List<String> getPrePurchaseCount(Map<String, Object> map);
	/**
	 * 采购前置页面数据获取
	 * @param map
	 * @return
	 */
	List<PrePurchasePojo> getPrePurchase(Map<String, Object> map);
	/**
	 * 获取已经验货无误商品
	 * @param orderid
	 * @return
	 */
	int getChecked(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	public String getTbOrderid(@Param("shipno") String shipno);
	/**
	 * 根据入库未匹配到商品的订单查询订单信息
	 * @Title getPrePurchaseForTB
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PrePurchasePojo>
	 */
	List<PrePurchasePojo> getPrePurchaseForTB(Map<String, Object> map);
	/**
	 * 根据入库未匹配到商品的订单查询订单信息数量
	 * @Title getPrePurchaseForTBCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PrePurchasePojo>
	 */
	List<PrePurchasePojo> getPrePurchaseForTBCount(Map<String, Object> map);

	public String getTbOrderId(@Param("orderid") String orderid, @Param("odid") String odid);

	public int updateTaoBaoOrder(Map<String, String> map);

	public int insertTaoBaoOrder(Map<String, String> map);

	public int updateSourceState(Map<String, String> map);

	public int insertRecords(Map<String, String> map);

	public List<Map<String,String>> getAlCgGoodsInfos(@Param("orderid") String orderid, @Param("adminid") int adminid);

	public int getIsSendEmail(@Param("orderid") String orderid);

	public List<String> getIdList(@Param("orderid") String orderid, @Param("odid") String odid);

	public int udpateLog(@Param("adminid") int adminid, @Param("orderid") String orderid, @Param("odid") String odid, @Param("msg") String msg);

	public int insertLog(@Param("adminid") int adminid, @Param("orderid") String orderid, @Param("odid") String odid, @Param("msg") String msg);

	public int updateQxSourceState(@Param("orderid") String orderid, @Param("odid") String odid);

	public String getDpOrderInfo(@Param("orderid") String orderid);

	public int updateDpOrderState(@Param("dropshipid") String dropshipid);

	/**
	 * 插入发送邮件记录
	 * @param orderid
	 * @return
	 */
	public int insertPurchaseEmail(@Param("orderid") String orderid);

	public int updateOrderInfoState(@Param("orderid") String orderid);

	public  int updateCgSourceState(@Param("orderid") String orderid, @Param("odid") String odid, @Param("adminid") int adminid);

	/**
	 * 更新货源状态
	 * @param orderid
	 * @param odid
	 * @return
	 */
	public int updateOrderProSource(@Param("orderid") String orderid, @Param("odid") String odid);

	/**
	 * 更新details状态
	 * @param orderid
	 * @param odid
	 * @return
	 */
	public int updateDetailsState(@Param("orderid") String orderid, @Param("odid") String odid);
	/**
	 * 根据订单号获取国际运费
	 * @param orderNo
	 * @return
	 */
	public Map<String,String> getAllFreightByOrderid(@Param("orderNo") String orderNo);

	/**
	 *  获取其他货源
	 * @param orderid
	 * @param goodsid
	 * @return
	 */
	public List<Map<String,String>> getOtherSources(@Param("orderid") String orderid, @Param("odid") String odid);

	/**
	 * 根据产品ID查询店铺信息
	 * @param shop_id
	 * @return
	 */
	public Map<String,String> getShopIdByGoodsPid(@Param("shop_id") String shop_id);

	/**
	 * 判断订单是否被取消
	 * @param orderNo
	 * @return
	 */
	public int checkOrder(@Param("orderNo") String orderNo);

	/**
	 * 查询商品状态
	 * @param orderNo
	 * @param odid
	 * @return
	 */
	public int checkGoods(@Param("orderNo") String orderNo, @Param("odid") String odid);

	/**
	 * 判断商品是否有过变更
	 * @param orderNo
	 * @param odid
	 * @return
	 */
	public String checkOrderChange(@Param("orderNo") String orderNo, @Param("odid") String odid);

	/**
	 * 获取采购人名称
	 * @param buyerId
	 * @return
	 */
	public String getUserbyID(@Param("buyerId") String buyerId);

	public int getOpsCount(@Param("orderNo") String orderNo, @Param("odid") int odid);

	/**
	 * 新增前先删除货源
	 * @param orderNo
	 * @param od_id
	 * @return
	 */
	public int deleteSource(@Param("orderNo") String orderNo, @Param("od_id") String od_id);

	/**
	 * 录入货源信息
	 * @return
	 */
	public int insertSource(@Param("admid") int admid, @Param("userid") int userid, @Param("orderNo") String orderNo, @Param("goodsid") int goodid, @Param("goodsdataid") int goodsdataid,
                            @Param("goods_p_url") String goods_p_url
            , @Param("goodsurl") String goodsurl, @Param("newValue") String newValue, @Param("googsimg") String googsimg, @Param("goodsprice") String goodsprice, @Param("oldValue") String oldValue
            , @Param("goodstitle") String goodstitle, @Param("googsnumber") int googsnumber, @Param("purchaseCount") int purchaseCount,
                            @Param("od_id") int od_id, @Param("itemid") String itemid);

	/**
	 * 更新order_details的状态
	 * @param orderNo
	 * @param odid
	 * @return
	 */
	public int updateOrderDetails(@Param("orderNo") String orderNo, @Param("odid") int odid);

	public int updateSourceInfo(@Param("admid") int admid, @Param("goodsid") int goodsid, @Param("orderNo") String orderNo, @Param("od_id") int od_id);

	public Map<String,String> getOrderDtails(@Param("odid") int odid);

	public int getOrderChangeState(Map<String, String> map);

	/**
	 * 获取货源数量
	 * @param map
	 * @return
	 */
	public int getSourceCount(Map<String, String> map);

	public int getGoodsSourceCount(Map<String, String> map);

	/**
	 * 根据产品ID查询店铺信息
	 * @param sid
	 * @return
	 */
	public Map<String,String> getLevelBySid(@Param("sid") String sid);

	public int updateGoodsChangeState(Map<String, String> map);

	public int updateGoodsOfSource(Map<String, String> map);

	/**
	 * 删除货源记录
	 * @param map
	 * @return
	 */
	public int deleteSource(Map<String, String> map);

	/**
	 * 插入货源记录
	 * @param map
	 * @return
	 */
	public int insertOrderProductSource(Map<String, String> map);

	/**
	 * 更新货源信息
	 * @param map
	 * @return
	 */
	public int updateOrderProductSource(Map<String, String> map);

	public int updateDetails(Map<String, String> map);

	/**
	 * 获取插入的货源信息id
	 * @param map
	 * @return
	 */
	public int getOrderProductSource(Map<String, String> map);

	/**
	 * 记录插入货源记录
	 * @param map
	 * @return
	 */
	public int addRecodChangeSourceLog(Map<String, String> map);

	/**
	 * 查询goods_source记录
	 * @param map
	 * @return
	 */
	public Map<String,String> queryGoodsSource(Map<String, String> map);

	/**
	 * 更新goods_source表
	 * @param map
	 * @return
	 */
	public int updateGoodsSource(Map<String, String> map);

	/**
	 * 查询订单详情信息
	 * @param map
	 * @return
	 */
	public OrderDetailsBean queryOrderDetails(Map<String, String> map);

	/**
	 * 插入goods_source记录
	 * @param map
	 * @return
	 */
	public int insertGoodsSource(Map<String, String> map);

	/**
	 * 查询订单详情
	 * @param map
	 * @return
	 */
	public List<OrderDetailsBean> getOrderDeatails(Map<String, String> map);

	/**
	 * 查询产品表中的店铺名称
	 * @param map
	 * @return
	 */
	public String queryShopId(Map<String, String> map);

	/**
	 * 查询货源记录
	 * @param
	 * @return
	 */
	public int queryOrderProductSource(@Param("orderid") String orderid, @Param("odid") String odid);

	public int insertOrderSource(Map<String, String> map);

	public int updateBuyUrl(Map<String, String> map);

	/**
	 * 更新货源表的店铺名称
	 * @param map
	 * @return
	 */
	public int updateOrderProduct(Map<String, String> map);

	/**
	 * 根据产品ID查询店铺信息
	 * @param sid
	 * @return
	 */
	public Map<String,String> getLevelBySids(@Param("sid") String sid);

	public int updateOrderDetailsFlag(@Param("flag") int flag, @Param("orderNo") String orderNo, @Param("odid") String odid);

	/**
	 * 查询该商品是否有使用库存
	 * @param odid
	 * @return
	 */
	public String getUseInventory(@Param("odid") int odid);

	public List<Map<String,String>> getTaoBaoInfoByShipno(@Param("shipnos") String shipnos);

	public List<String> getBhShopId(@Param("orderNo") String orderNo, @Param("goodsid") String goodsid);

	public String supplierScoringLevel(@Param("shopId") String shopId);

	public String getEsPrice(@Param("orderNo") String orderNo, @Param("odid") String odid);

	/**
	 * 获取广东直发列表数据
	 * @return
	 */
	public List<StraightHairPojo> straightHairList();

	public int updateState(@Param("orderNo") String orderNo, @Param("goodsid") String goodsid);

	public Map<String,Integer> queryState(@Param("orderNo") String orderNo);
	//添加补货记录
	int addReplenishmentRecord(Map<String, Object> map);
	//查询补货记录
	List<Replenishment_RecordPojo> getIsReplenishments(Map<String, Object> map);
	//查询线下采购记录
	List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String, Object> map);
	//补货
	int insertOrderReplenishment(Map<String, Object> map);
	public int updateOrderInfo(@Param("orderNo") String orderNo);
	//补货订单按钮状态改变
	int updateReplenishmentState(Map<String, Object> map);
	public int updateGoodsCommunicationInfo(@Param("orderNo") String orderNo, @Param("admin") int admin);

	/**
	 * 根据订单号查询客户ID和新旧订单状态
	 * @param orderNo
	 * @return
	 */
	Map<String,Object> queryUserIdAndStateByOrderNo(@Param("orderNo") String orderNo);

	List<Map<String,Object>> getComfirmedSourceGoods();

	int updateAutoOrderFlag(@Param("idsList") List<Integer> idsList);
	
	public List<Map<String,Object>> getSizeChart(@Param("catid")String  catid);
	public List<Map<String,Object>> loadCategoryName(@Param("catid")String catid);
	public List<Map<String,Object>> getSizeChart_add(@Param("catid")String  catid);
	public List<Map<String,Object>> loadCategoryName_add(@Param("catid")String catid);
	public List<Map<String,Object>> getSizeChartPidInfo();
	public int updateSizeChart(@Param("imgname")String imgname,@Param("localpath")String localpath,@Param("rowid")int rowid);
	public int updateSizeChart_add(@Param("imgname")String imgname,@Param("localpath")String localpath,@Param("rowid")int rowid);
	public int updateSizeChartUpload(@Param("rowidArray")List<Integer> rowidArray);
	public int updateSizeChartById(@Param("rowidArray")List<Integer> rowidArray,@Param("userid")int userid);
	public int updateSizeChartById_add(@Param("rowidArray")List<Integer> rowidArray,@Param("userid")int userid);

    @Select("SELECT apply_time as applyTime,apply_user as applyUser FROM return_display WHERE customer_info=#{orderNo} AND item=#{goods_pid} ORDER BY apply_time DESC LIMIT 1")
	returndisplay getApplyTime(@Param("orderNo") String orderNo, @Param("goods_pid") String goods_pid);

}
