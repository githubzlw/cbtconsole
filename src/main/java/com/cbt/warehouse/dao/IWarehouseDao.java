package com.cbt.warehouse.dao;

import com.cbt.bean.*;
import com.cbt.bean.OrderBean;
import com.cbt.pojo.BuyerCommentPojo;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.pojo.*;
import com.cbt.website.bean.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IWarehouseDao {
	public outIdBean findOutId(@Param("uid") Integer uid);
	public OrderAddress getAddressByOrderID(@Param("orderNo") String orderNo);
	public List<PurchaseDetailsBean> getPurchaseDetails(@Param("orderNo") String orderNo);
	public List<PurchaseBean> getOutByID(String userid, String sql);
	public void callResult(Map<String, String> param);
	public void callUpdateIdrelationtable(Map<String, String> param);
	public void insertStorage_location(String a, String b, String c, String d, String e);

	/**
	 * 判断该商品是否存在该张验货图片
	 * @param map
	 * @return
	 */
	int checnInspIsExit(Map<String,String> map);

	int insertInspPath(Map<String,String> map);
	/**
	 * 保存验货时商品重量
	 * @param map
	 * @return
	 */
	int saveWeight(Map<String, String> map);
	/**
	 * 采购页面商品拿样
	 * @param pids
	 * @return
	 */
	List<SampleGoodsBean> getSampleGoods(@Param("pids") String[] pids);
	/**
	 * 产品单页提问图片路径保存
	 * @param id
	 * @param path
	 * @return
	 */
	int updateQuestPicPath(@Param("id") String id, @Param("path") String path);
	/**
	 * 客户下单商品查询同款信息
	 * @param pids
	 * @return
	 */
	List<SampleGoodsBean> getLiSameGoods(@Param("pids") String[] pids, @Param("shopIds") String[] shopIds, @Param("pIds") String[] pIds);

	/**
	 * 获取建议采样结果集 根据店铺id和剔除的pids
	 * @param map
	 * @return
	 */
	List<SampleGoodsBean> getRecommdedSameGoods(Map<String, String> map);

	/**
	 * 根据订单号获取非取消商品的Pid
	 * @param map
	 * @return
	 */
	String getAllOrderPid(Map<String, String> map);

	/**
	 * 检查该商品是否有取消订单产生的库存
	 * @param map
	 * @return
	 */
	Inventory checkGoodsInvengory(Map<String, String> map);

	/**
	 * 取消商品退货库存相应减少
	 * @param map
	 * @return
	 */
	int updateInventory(Map<String, String> map);

	/**
	 * 更改入库记录中状态为退货
	 * @param map
	 * @return
	 */
	int updateIsRefund(Map<String, String> map);
	/**
	 * 根据订单号和商品号获取商品信息
	 * @param map
	 * @return
	 */
	OrderDetailsBean getOrderDetails(Map<String, String> map);

	/**
	 * 记录采购质量评论变更记录
	 * @param map
	 * @return
	 */
	int saveQualityDataLog(Map<String, String> map);
	/**
	 * 月用户利润统计获取平均汇率
	 * @param map
	 * @return
	 */
	Map<String,String> getExchange(Map<String, String> map);
	/**
	 *产品在搜索结果中被呈现次数
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getPresentationsData(Map<String, String> map);
	/**
	 * 人为编辑过的产品页面被打开总数
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getOpenCountData(Map<String, String> map);
	/**
	 * 人为编辑过的产品被加购物车总数
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getAddCarData(Map<String, String> map);
	/**
	 * 人为编辑过的产品被购买总数
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getGoodsBuyData(Map<String, String> map);
	/**
	 * 人为编辑过的产品总销售额
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getGoodsSalesAmountData(Map<String, String> map);
	/**
	 * 人为编辑过的产品被取消总数
	 * @param map
	 * @return
	 */
	public OrderInfoCountPojo getCancelData(Map<String, String> map);
	/**
	 * 保存采购质量评论内容
	 * @param map
	 * @return
	 */
	int saveQualityData(Map<String, String> map);
	/**
	 * 获取采购质量评论id
	 * @param map
	 * @return
	 */
	int queryQeId(Map<String, String> map);
	/**
	 * 更新采购质量评论内容
	 * @param map
	 * @return
	 */
	int updateQualityData(Map<String, String> map);
	/**
	 *获取商品质量评论
	 * @param map
	 * @return
	 */
	String getQualityEvaluation(Map<String, String> map);
	/**
	 * 删除验货图片路径在order_details
	 * @param map
	 * @return
	 */
	int delInPic(Map<String, String> map);
	/**
	 * 新增验货图片上传-验货编辑
	 * @param pid
	 * @return
	 */
	int insertInspectionPicture(@Param("pid") String pid, @Param("picPath") String picPath);
	/**
	 * 停用验货图片
	 * @param map
	 * @return
	 */
	int disabled(Map<String, String> map);
	/**
	 * 验货页面增加评论
	 * @param map
	 * @return
	 */
	int insertEvaluation(Map<String, String> map);

	/**
	 * 查询是否商品pid是否存在产品评论
	 * @param map
	 * @return
	 */
	String queryQevId(Map<String, String> map);

	/**
	 *更新产品评论
	 * @param map
	 * @return
	 */
	int updateEvaluation(Map<String, String> map);

	/**
	 * 添加评论日志
	 * @param map
	 * @return
	 */
	int insertEvaluationLog(Map<String, String> map);
	/**
	 * 获取验货的商品重量
	 * @return
	 */
	public SearchResultInfo getWeight();
	public SearchResultInfo getGoodsWeight(@Param("pid") String pid);
	public SearchResultInfo updateGoodsWeightFlag(@Param("pid") String pid);

	/**
	 * 获取该商品上一次发货的订单国家
	 * @param pid
	 * @return
	 */
	public String getBatckInfo(@Param("pid") String pid);
	//getAllStorageLocationByPage
	public List<StorageLocationBean> getAllStorageLocationByPage(int startNum, int endNum);
	//获得采购数量
	public String getCgCount(Map<String, Object> map);
	//当日分配采购种类
	public String getDistributionCount(Map<String, Object> map);
	/**
	 * 最近30天 产生的 库存损耗
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getLossInventory();
	/**
	 * 最近30天 产生的 库存删除
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getDeleteInventory();
	/**
	 * 最近30天销售掉的库存
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getSaleInventory();
	/**
	 * 库存管理页面统计最近30天新产生的库存
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getNewInventory();
	//当月分配采购种类
	public String getfpCount(Map<String, Object> map);
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
	/**
	 * 新增搜索词对应的优先类别
	 * @param map
	 * @return
	 */
	public int addKeyword(Map<String, String> map);
	/**
	 * 修改搜索词对应的优先类别
	 * @param map
	 * @return
	 */
	public int editKeyword(Map<String, String> map);
	public int updateStateCategory(Map<String, String> map);
	//获得每月采购数量cjc 1-11
	public String getMCgCount(Map<String, Object> map);
	//获得实际采购数量
	public String getSjCgCount(Map<String, Object> map);
	//添加订单采购商品备注
	public int insertOrderRemark(Map<String, Object> map);
	//功能订单采购备注
	public int updateOrderRemark(Map<String, Object> map);
	//查询某个订单备注是否存在
	public int findOrderRemark(Map<String, Object> map);
	/**
	 *出库审核验证商品号是否为该订单商品
	 * @param map
	 * @return
	 */
	public int checkedGoods(Map<String, String> map);
	/**
	 * 采购对采购商品 授权操作
	 * @param map
	 * @return
	 */
	public int productAuthorization(Map<String, String> map);

	public int insertAuthorizedFlag(Map<String, String> map);

	public int updateAuthorizedFlag(Map<String, String> map);

	/**
	 * 检查该商品是否已被授权
	 * @param map
	 * @return
	 */
	public int checkIsExit(Map<String, String> map);
	/**
	 * 出运运费预警时信息录入
	 * @param map
	 * @return
	 */
	public int insertWarningInfo(Map<String, String> map);
	//获得出货号
	String getShipmentno();

	//包裹信息
	ShippingPackage getPackageInfo(Map<String, String> map);

	//出库页面订单的所有包裹
	List<ShippingPackage> getShippingPackageById(Map<String, String> map);

	//添加申报信息
	int insertSbxx(Map<String, String> map);
	int updateRemainingPrice(Map<String, String> map);
	int updateOrderDetailsState(Map<String, String> map);
	int updateODPState(Map<String, String> map);
	int updateSendMail(Map<String, String> map);
	int insertId_relationtable(Map<String, String> map);



	int updateOpsState(Map<String, String> map);
	int updateOrderinfoNumber(Map<String, String> map);
	int updateOrderinfoState(Map<String, String> map);
	int updateChildOrderinfoState(Map<String, String> mapc);
	int GetSetOrdrerState(Map<String, String> map);
	int updateOrder(Map<String, String> map);
	int updateorderDetailsState(Map<String, String> map);
	int getOdIsState(Map<String, String> map);

	int addGoodsInventory(Map<String, String> map);

	//申报信息中文
	List<SbxxPojo> getSbxxList(Map<String, String> map);

	//包裹列表
	List<ShippingPackage> getPackageInfoList(Map<String, String> map);

	//删除出货
	int deleteShippingPackage(Map<String, String> map);


	int updateGoodsDistribution(Map<String, String> map);

	//获得数量最多的3张图片
	List<Map<String,String>> getMaxImg(Map<String, String> map);
	//获得种类和数量
	List<Map<String,String>> getCntSum(Map<String, String> map);

	int selectShippingPackage(Map<String, String> map);

	//批量插入出货包裹
	int batchInsertSP(List<Map<String, String>> list);

	//批量修改出货包裹
	int batchUpdateShippingPackage(List<Map<String, String>> list);

	//出库的时候修改包裹信息
	int bgUpdate(List<Map<String, String>> list);

	int xlsbatch(List<Map<String, String>> list);

	//根据订单查询地址
	public String getOrderAddress(Map<String, Object> map);

	//查询商品原链接
	public List<Map<String,String>> getGoodsCar(Map<String, Object> map);
	//根据1688产品ID获取信息
	public List<CustomGoodsBean> getAllGoodsInfos(@Param("goods_pids") String goods_pids);
	/**
	 * 生成采样订单
	 * @Title insertOrderInfo
	 * @Description TODO
	 * @param oi
	 * @return
	 * @return int
	 */
	public int insertOrderInfo(Orderinfo oi);
	/**
	 * 根据pid获取下单信息
	 * @param pid
	 * @return
	 */
	public OrderDetailsBean getOldDetails(@Param("goods_pid") String goods_pid);
	public OrderDetailsBean getCustomBeack(@Param("goods_pid") String goods_pid);
	/**
	 * 生成采样订单明细
	 * @Title insertOrderDetails
	 * @Description TODO
	 * @param list
	 * @return
	 * @return int
	 */
	public int insertOrderDetails(List<OrderDetailsBean> list);
	/**
	 * 判断需要生成的采样详情是否已存在
	 * @Title getOrderDetailsExit
	 * @Description TODO
	 * @param goods_pid
	 * @param car_type
	 * @return
	 * @return int
	 */
	public int getOrderDetailsExit(@Param("goods_pid") String goods_pid, @Param("car_type") String car_type);
	/**
	 * 更新28cross_border-custom_benchmark_ready_newest-为2samplingStatus
	 * @Title updateCrossBorder
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateCrossBorder(@Param("goods_pid") String goods_pid);
	/**
	 * 更新线上crossshop-custom_benchmark_ready-为2samplingStatus
	 * @Title updateShop
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateShop(@Param("goods_pid") String goods_pid);
	/**
	 * 更新alidata中ali_info_data表字段是否采样（sampl_flag）为1
	 * @Title updateSamplFlag
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateSamplFlag(@Param("goods_pid") String goods_pid, @Param("count") int count);
	/**
	 * 更新27crossshop-custom_benchmark_ready-为2samplingStatus
	 * @Title updateCrossShopr
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateCrossShopr(@Param("goods_pid") String goods_pid);
	/**
	 * 根据订单号查询采样订单详情
	 * @Title getOrderDetailsByOrderid
	 * @Description TODO
	 * @param orderid
	 * @return
	 * @return List<OrderDetailsBean>
	 */
	public List<OrderDetailsBean> getOrderDetailsByOrderid(@Param("orderid") String orderid);
	//采样订单分配采购
	public void insertGd(List<OrderDetailsBean> list);
	public int updateGdOdid();
	//获得用户id对饮销售email账号密码
	AdmuserPojo getAdmuserSendMailInfo(Map<String, Object> map);
	//运费报表总记录
	List<OrderFeePojo> getOrderfeeFreight(Map<String, Object> map);
	int getOrderfeeFreightCount(Map<String, Object> map);

	//查询对应hscode
	String getHsCode(Map<String, Object> map);


	//商品库位对照
	List<StorageLocationBean> getOrderinfoPage(Map<String, Object> map);
	int getCountOrderinfo(Map<String, Object> map);
	/**
	 * 查询采购 所有昨天确认采购的订单
	 * @param map
	 * @return   采购昨天确认采购的订单数量集合
	 * whj 2017-05-11
	 */
	List<String> getsourceValidation(Map<String, Object> map);

	/**
	 * 获取采购账号
	 */
	public List<String> getBuyerName(@Param("admuserid") String admuserid);

	/**
	 * 获取采购账号
	 */
	public String getBuyerNames(@Param("admuserid") String admuserid);

	/**
	 * 查询采购 所有昨天确认采购的订单
	 * @param map
	 * @return   采购昨天确认采购的订单数量集合
	 * whj 2017-05-11
	 */
	List<String> getsourceValidationForBuy(Map<String, Object> map);


	//出库验证
	List<StorageLocationBean> getOrderInfoInspection(Map<String, Object> map);
	List<StorageLocationBean> getOrderInfoInspectionall(Map<String, Object> map);
	int getCountOrderInfoInspection(Map<String, Object> map);

	//验货插入数据
	int insertOrderfeeFromOrderInfo(Map<String, Object> map);

	//根据订单查询收货地址
	String getOrderidAddress(Map<String, Object> map);

	//根据订单查询下单时间
	String getOrderCreateTime(Map<String, Object> map);
	/**
	 * 若拆单了，在该清单的最后要提醒客户还有部分东西在另一个包裹中，并列出哪些还准备发货，哪些取消了
	 * @return
	 */
	int getUndeliveredOrder(@Param("orderid") String orderid);

	//查询出库打印信息
	List<OrderInfoPrint> getOrderidPrinInfo(Map<String, Object> map);

     List<OrderInfoPrint> getDropshipOrderidPrinInfo(Map<String, Object> map);

	//根据订单id删除order_fee表数据 delteFromOrderFeeByOrderid
	int delteFromOrderFeeByOrderid(Map<String, Object> map);


	//多货源
	int delteOrderReplenishment(Map<String, Object> map);


	//已出货列表
	List<Forwarder> getForwarder(Map<String, Object> map);
	int getCountForwarder(Map<String, Object> map);

	List<Forwarder> getForwarderplck(Map<String, Object> map);
	int getCountForwarderplck(Map<String, Object> map);



	//取消合并
	int updateFromOrderFeeByOrderid(Map<String, Object> map);

	//修改快递单号
	int updateExperssNo(Map<String, Object> map);


	int updateExperssNoPlck(Map<String, Object> map);
	//修改订单欠费金额1
	int updateOrderinfoAll(Map<String, Object> map);
	//修改订单欠费金额2
	int updateOrderinfo(Map<String, Object> map);


	//正式出库*****************************
	//获取所有出库列表
	List<OrderFeePojo> getOrderFee(Map<String, Object> map);

	//获取单个出库订单的详细信息
	OrderInfoPojo getOutOrderInfo(Map<String, Object> map);
	/**
	 * 获取产品的视频ID
	 * @param map
	 * @return
	 */
	OrderDetailsBean queryVideo(Map<String, String> map);
	/**
	 * 更新产品单页视频ID
	 * @param map
	 * @return
	 */
	int updateCustomVideoUrl(Map<String, String> map);
	/**
	 * 编辑页面获取验货图片
	 * @Title queryPictureInfos
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<SearchResultInfo>
	 */
	List<SearchResultInfo> queryPictureInfos(Map<String, String> map);
	/**
	 * 根据商品号查找验货图片地址
	 * @Title getPicturePath
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return String
	 */
    List<SearchResultInfo> getPicturePath(@Param("goods_pid") String goods_pid);
	/**
	 * 编辑页面获取验货图片数量
	 * @Title queryPictureInfos
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<SearchResultInfo>
	 */
	int queryPictureInfosCount(Map<String, String> map);

	//获取国家
	List<OrderFeePojo> getFpxCountryCode();
	//其他出货方式
	List<OrderFeePojo> getCodemaster();
	//出库和待出库数量
	List<OrderInfoPojo> getOutCount();

	//本来差钱但是现在已经把差的钱补齐的订单
	List<OrderInfoPojo> getNotMoneyOrderinfo(Map<String, Object> map);


	//获取所有金额
	OrderInfoPojo getPaymentFy(Map<String, Object> map);

	//4px运输方式
	List<OrderInfoPojo> getFpxProductCode();
	//修改orderfee
	int updateOrderFeeByOrderid(Map<String, Object> map);

	//*************************************综合采购************************************
	//淘宝商品快递单号，和状态
	List<Logisticsinfo> getlogisticsidAndState(Map<String, Object> map);

	//淘宝订单信息
	Logisticsinfo getlogisticsinfo(Map<String, Object> map);

	//OrderProductSurc 商品信息
	List<OrderProductSurcePojo> getlogisticsAndOrderProductSurc(Map<String, Object> map);
	/**
	 * 手动录入退样运单号
	 * @Title refundShipnoEntry
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int refundShipnoEntry(Map<String, Object> map);
	/**
	 * 根据采购订单ID获取是否已经退过货
	 * @Title getState
	 * @Description TODO
	 * @param map
	 * @return
	 * @return String
	 */
	String getState(Map<String, Object> map);
	/**
	 * 获取采样订单验货时的数量
	 * @Title getStoragCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return String
	 */
	int getStoragCount(Map<String, Object> map);
	/**
	 * 退样时库存减少
	 * @Title updateInventoryCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int updateInventoryCount(Map<String, Object> map);
	/**
	 * 更新货源表的退货标识refund_flag
	 * @Title updateRefundFlag
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int updateRefundFlag(Map<String, Object> map);
	/**
	 * 获取商品编号根据商品号和订单号
	 * @Title getRefundGoodsPid
	 * @Description TODO
	 * @param goodsid
	 * @param orderid
	 * @return
	 * @return List<String>
	 */
	List<String> getRefundGoodsPid(@Param("goodsid") String goodsid, @Param("orderid") String orderid);
	//库存列表
	List<OrderInfoPrint> getIdrelationtable(Map<String, Object> map);
	int getCountIdrelationtable(Map<String, Object> map);


	//获得订单问题描述
	String getOrderProblem(Map<String, Object> map);


	//采购头部信息*******************
	//根据不同的状态获得订单的数量
	OrderInfoCountPojo getOrderInfoCountByState(Map<String, Object> map);

	OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map);
	/**
	 * 获取入库没有匹配到商品的订单
	 * @Title getNoMatchOrderByTbShipno
	 * @Description TODO
	 * @param map
	 * @return
	 * @return OrderInfoCountPojo
	 */
	OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map);
	List<UserInfo> getUserInfoForPrice(Map<String, Object> map);
	List<String> getNoShipInfoOrder(Map<String, String> map);
    //点了采购确认
    List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map);
	/**
	 * 1688采购订单建议退货管理
	 * @param goodsid
	 * @return
	 */
	List<TaoBaoOrderInfo> getBuyReturnManage(@Param("goodsid") String goodsid, @Param("page") int page, @Param("state") String state, @Param("startTime") String startTime, @Param("endTime") String endTime);

	/**
	 * 1688采购订单建议退货管理
	 * @param goodsid
	 * @return
	 */
	int getBuyReturnManageCount(@Param("goodsid") String goodsid, @Param("state") String state, @Param("startTime") String startTime, @Param("endTime") String endTime);
	/**
	 * 获取店铺信息
	 * @param map
	 * @return
	 */
	List<ShopManagerPojo> getShopManagerList(Map<String, Object> map);

	/**
	 * 获取店铺信息数量
	 * @param map
	 * @return
	 */
	int getShopManagerListCount(Map<String, Object> map);
	/**
	 * 查询采样退样信息
	 * @Title searchRefundSample
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<RefundSamplePojo>
	 */
	List<RefundSamplePojo> searchRefundSample(Map<String, Object> map);
	/**
	 * 查询28 alidata库中产品的退货日期
	 * @Title searchRefundSample
	 * @Description TODO
	 * @param pids
	 * @return
	 * @return Map<String,String>
	 */
	Map<String, String> searchReturnDatas(@Param("pids") String pids);
	/**
	 * 查询工厂
	 * @Title getShopIdForPid
	 * @Description TODO
	 * @param goods_pids
	 * @return
	 * @return Map<String,String>
	 */
	List<Map<String, String>> getShopIdForPid(@Param("goods_pids") String goods_pids);
	/**
	 * 查询采样退样信息数量
	 * @Title searchRefundSample
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<RefundSamplePojo>
	 */
	List<RefundSamplePojo> searchRefundSampleCount(Map<String, Object> map);
	/**
	 * 查询需要退样的采购订单信息
	 * @Title searchRefundOrder
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<TaoBaoOrderInfo>
	 */
	List<TaoBaoOrderInfo> searchRefundOrder(Map<String, Object> map);
	/**
	 * 查询需要退样的采购订单信息数量
	 * @Title searchRefundOrderCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<TaoBaoOrderInfo>
	 */
	List<TaoBaoOrderInfo> searchRefundOrderCount(Map<String, Object> map);
	/**
	 * 获取店铺产品信息
	 * @param map
	 * @return
	 */
	List<ShopManagerPojo> getShopManagerDetailsList(Map<String, Object> map);
	/**
	 * 采样月退款数据统计
	 * @Title searchMonthlyRefund
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<TaoBaoOrderInfo>
	 */
	List<TaoBaoOrderInfo> searchMonthlyRefund(Map<String, Object> map);
	/**
	 * 采样月退款数据条数
	 * @Title searchMonthlyRefundCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<TaoBaoOrderInfo>
	 */
	List<TaoBaoOrderInfo> searchMonthlyRefundCount(Map<String, Object> map);
	/**
	 * 搜索词优先类别
	 * @param map
	 * @return
	 */
	List<ShopManagerPojo> getPriorityCategory(Map<String, Object> map);

	/**
	 * 根据类别id获取类别名称
	 * @param category
	 * @return
	 */
	public String getEnName(@Param("category") String category);
	/**
	 * 搜索词优先类别
	 * @param map
	 * @return
	 */
	int getPriorityCategoryCount(Map<String, Object> map);
	/**
	 * 供应商的采购历史
	 * @param map
	 * @return
	 */
	public List<ShopManagerPojo> getShopBuyLogInfo(Map<String, String> map);

	/**
	 * 根据店铺ID获取店铺评价
	 * @param map
	 * @return
	 */
	public List<ShopManagerPojo> getShopSupplier(Map<String, String> map);
	/**
	 * 供应商的采购历史
	 * @param map
	 * @return
	 */
	public int getShopBuyLogInfoCount(Map<String, String> map);
	/**
	 * 黑名单信息查询
	 * @param map
	 * @return
	 */
	public List<BlackList> getUserBackList(Map<String,String> map);
	/**
	 * 黑名单信息查询
	 * @param map
	 * @return
	 */
	public List<BlackList> getUserBackListCount(Map<String,String> map);
	/**
	 * 获取所有采购人
	 * @return
	 */
	List<com.cbt.pojo.AdmuserPojo> getAllBuyer(@Param("admuserid") int admuserid);
	/**
	 * 添加采样商品反馈
	 * @Title addSampleRemark
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int addSampleRemark(Map<String, Object> map);
	/**
	 * 删除手工录入的替换商品
	 * @Title deleteSource
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int deleteSource(Map<String, Object> map);
	/**
	 *更新类别的最低价格最高价
	 * @param map
	 * @return
	 */
	int updateCatePrice(Map<String, String> map);
	/**
	 * 更新采购价格
	 * @Title updatePrice
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int updatePrice(Map<String, Object> map);
	/**
	 * 采购添加商品评论
	 * @param map
	 * @return
	 */
	int saveCommentContent(Map<String, String> map);
	/**
	 * 获取采购评论商品的前置信息
	 * @param map
	 * @return
	 */
	BuyerCommentPojo getBuyerCommentPojo(Map<String, String> map);
	/**
	 * 记录扫描入库没有匹配到商品的淘宝订单
	 * @Title insertStorageProblemOrder
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int insertStorageProblemOrder(Map<String, Object> map);
	/**
	 * 保存服装类验货质检结果
	 * @param map
	 * @return
	 */
	int saveClothingData(Map<String, String> map);
	/**
	 * 人工赋能统计数据查询
	 * @Title getPurchaseSamplingStatistics
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PurchaseSamplingStatisticsPojo>
	 */
	List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatistics(Map<String, Object> map);
	public List<PurchaseSamplingStatisticsPojo> salesPerformanceCount(Map<String, Object> map);
	/**
	 * 产品被购买详情
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetails(Map<String, String> map);
	/**
	 * 产品被购买详情
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> salesPerformanDetailsCount(Map<String, String> map);
	/**
	 * 疑似重量有问题产品详情
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> weightProblemDetails(Map<String, String> map);
	public List<PurchaseSamplingStatisticsPojo> weightProblemDetailsCount(Map<String, String> map);
	/**
	 *月销售商品努力报表
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsList(Map<String,String> map);
	/**
	 *月销售商品努力报表
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> monthSalesEffortsListCount(Map<String,String> map);

	/**
	 * 清洗质量
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> getCleaningQuality(Map<String, String> map);
	public List<PurchaseSamplingStatisticsPojo> getCleaningQualityCount(Map<String, Object> map);
	public List<Inventory> getAllUser();
	/**
	 * 人为编辑过的产品销售业绩
	 * @param map
	 * @return
	 */
	List<PurchaseSamplingStatisticsPojo> salesPerformance(Map<String, Object> map);

	/**
	 * 截止到今天人为编辑过的页面总数
	 * @param map
	 * @return
	 */
	public String getEditCount(Map<String, Object> map);

	/**
	 * 产品在搜索结果中被呈现次数
	 * @param pid
	 * @return
	 */
	public String getPresentations(@Param("pid") String pid, @Param("updateTime") String updateTime);

	/**
	 * 产品页面被打开次数
	 * @param pid
	 * @return
	 */
	public String getOpenCount(@Param("pid") String pid, @Param("updateTime") String updateTime);

	/**
	 * 产品被加购物车次数
	 * @param pid
	 * @param updateTime
	 * @return
	 */
	public String getAddCarCount(@Param("pid") String pid, @Param("updateTime") String updateTime);

	/**
	 * 产品被购买次数
	 * @param pid
	 * @param updateTime
	 * @return
	 */
	public Map<String,String> getBuyCount(@Param("pid") String pid, @Param("updateTime") String updateTime);

	/**
	 * 产品被取消次数
	 * @param pid
	 * @param updateTime
	 * @return
	 */
	public String getCancelCount(@Param("pid") String pid, @Param("updateTime") String updateTime);
	/**
	 * 获取库存销售额
	 * @Title getInventorySaleAmount
	 * @Description TODO
	 * @param pids
	 * @return
	 * @return String
	 */
	String getInventorySaleAmount(@Param("pids") String pids);
	/**
	 * 总库存货值
	 * @Title getAllInventoryAmount
	 * @Description TODO
	 * @param pid
	 * @return
	 * @return String
	 */
	String getAllInventoryAmount(@Param("pids") String pids);
	/**
	 * 采样商品销售额
	 * @Title getSalesAmount
	 * @Description TODO
	 * @param pid
	 * @return
	 * @return String
	 */
	String getSalesAmount(@Param("pids") String pids);
	/**
	 * 货源采购额
	 * @Title getSourceAmount
	 * @Description TODO
	 * @param pid
	 * @return
	 * @return String
	 */
	String getSourceAmount(@Param("pids") String pids);
	/**
	 * 查询最近15天的库存货值
	 * @Title getInventoryAamountOne
	 * @Description TODO
	 * @param pid
	 * @return
	 * @return String
	 */
	String getInventoryAamountOne(@Param("pids") String pids);
	/**
	 * 查询最近30天的库存货值
	 * @Title getInventoryAamountOne
	 * @Description TODO
	 * @param pid
	 * @return
	 * @return String
	 */
	String getInventoryAamountTwo(@Param("pids") String pids);
	/**
	 * 查询28库alidata 中ali_info_data中产品的供应商数量
	 * @Title getCompanyCount
	 * @Description TODO
	 * @param pids
	 * @return
	 * @return int
	 */
	String getCompanyCount(@Param("pids") String pids);
	/**
	 * 人工赋能统计数量查询
	 * @Title getPurchaseSamplingStatisticsCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PurchaseSamplingStatisticsPojo>
	 */
	List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatisticsCount(Map<String, Object> map);
	/**
	 * 根据1688 PID获取ali PID
	 * @Title getAliPid
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return String
	 */
	String getAliPid(@Param("goods_pid") String goods_pid);
	/**
	 * 获取有库存的库位信息
	 * @Title getHavebarcode
	 * @Description TODO
	 * @return
	 * @return List<Inventory>
	 */
	List<Inventory> getHavebarcode();
	/**
	 * 获取店铺产品信息数量
	 * @param map
	 * @return
	 */
	int getShopManagerListDetailsCount(Map<String, Object> map);
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

	/**
	 * 采购前置页面数据获取
	 * @param map
	 * @return
	 */
	List<PrePurchasePojo> getPrePurchase(Map<String, Object> map);
	/**
	 * 采购前置页面数据条数
	 * @param map
	 * @return
	 */
	List<String> getPrePurchaseCount(Map<String, Object> map);
	/**
	 * 获取某个采购的订单分配商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getFpCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);

	List<PurchasesBean> getFpOrderDetails(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取某个采购的订单入库商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getStorageCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);

	/**
	 * 判断用户邮箱是否为黑名单
	 * @param email
	 * @return
	 */
	int getBackList(@Param("email") String email);

	/**
	 * 判断支付邮箱是否为黑名单
	 * @param payName
	 * @return
	 */
	int getPayBackList(@Param("payName") String payName);
	/**
	 * 获取某个采购的订单采购商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getPurchaseCount(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取该订单采购与销售的沟通
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getGoodsInfo(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取已经验货无误商品
	 * @param orderid
	 * @return
	 */
	int getChecked(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取订单商品数量
	 * @param orderid
	 * @return
	 */
	int getCountOd(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 获取验货有问题数量
	 * @param orderid
	 * @return
	 */
	List<String> getProblem(@Param("orderid") String orderid, @Param("admuserid") String admuserid);
	/**
	 * 更改店铺状态
	 * @param map
	 * @return
	 */
	int updateShopState(Map<String, Object> map);

	List<UserInfo> getUserInfoForPriceCount(Map<String, Object> map);

	String getGname(@Param("gid") int gid);

	//获得淘宝状态
	Tb1688Pojo getTbState(Map<String, Object> map);

	List<ConfirmUserInfo> getAllAdmuser();


	//问卷调查统计
	List<AllProblemPojo> getAllProblem(Map<String, Object> map);
	int getTotalNumber(Map<String, Object> map);
	List<String> getAllProposal(Map<String, Object> map);


	//补货
	int insertOrderReplenishment(Map<String, Object> map);
	//补货订单按钮状态改变
	int updateReplenishmentState(Map<String, Object> map);
	//将采购补货时录入的店铺ID添加到28库中
	int insertShopId(Map<String, Object> map);
	//是否存在补货
	List<OrderReplenishmentPojo> getIsReplenishment(Map<String, Object> map);
	//添加补货记录
	int addReplenishmentRecord(Map<String, Object> map);
	//查询补货记录
	List<Replenishment_RecordPojo> getIsReplenishments(Map<String, Object> map);
	//获取采样记录
	public List<DisplayBuyInfo> displayBuyLog(Map<String, Object> map);
	//查询线下采购记录
	List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String, Object> map);
	//根据1688产品获取工厂和级别
	Map<String, String> getCompanyInfo(@Param("goods_pid") String goods_pid);
	//申报信息
	int insertDeclareinfo(Map<String, Object> map);

	//jcex打印信息
	List<JcexPrintInfo> getJcexPrintInfo(Map<String, Object> map);
	List<JcexPrintInfo> getJcexPrintInfoPlck(Map<String, Object> map);
	List<JcexPrintInfo> getJcexPrintInfoPlckCount(Map<String, Object> map);
	//写入导出数据
	int updateDeclareinfoByOrderid(Map<String, Object> map);

	List<Integer> queryUser();
	//根据userid  获取用户信息
	public String getUserName(String userid);

	public void addGoodsInvent(String orderid);

	public List<GoodsInventory> selectInventByOrderId(String orderid);

	//出库列表新添一条记录
	public int insertSp(String shipmentno);
	//根据orderno 查询数据
	public List<String> selectOrderFeeByOrderid(Map<String, Object> map);
	//获取dropship订单信息
	public List<StorageLocationBean> getDropShipOrderInfoInspection(
            Map<String, Object> map);
	//获取dropship订单信息
	public List<StorageLocationBean> getDropShipOrderInfoInspectionall(
            Map<String, Object> map);
	//
	public int getCountDropShipOrderInfoInspection(Map<String, Object> map);

	public List<String> getOrderNoList(Map<String, Object> map);
	/**
	 * 获取shipping_package 的id
	 * @Title getSpId
	 * @Description TODO
	 * @param orderids
	 * @return
	 * @return String
	 */
	public String getSpId(String orderids);
	//dropship  包裹列表
	public List<ShippingPackage> getDropshipPackageInfoList(Map<String, String> map);
	// dropship 出库列表
	public List<Forwarder> getDropshipForwarderplck(Map<String, Object> map);
	//dropship 出库列表 统计
	public int getDropshipCountForwarderplck(Map<String, Object> map);
	public int upOrderInfo(Map<String, Object> map);
	public int upId_relationtable(Map<String, Object> map);
	public int upOrderFee(Map<String, Object> map);
	public int upWaraingState(Map<String, Object> map);
	public List<String> getOrderFeeOrderNO(String orderno);
	//更新dropship子单状态
	public int updateDropshipState(Map<String, Object> map);
	/**
	 * 出库审核通过时判断该订单是否有合并出运订单
	 * @param orderid
	 * @return
	 */
	public String getMegerOrder(@Param("orderid") String orderid);
	public int selectCountByparent_order_no(String orderno);
	public int selectSumByState(String orderno);
	//更新dropship主单状态
	public int updateMainDropshipState(String mainOrder);
	//根据子单查主单好
	public String selectDropshipOrderNo(String orderno);
	//查询库位信息
	public List<LocationManagementInfo> getLocationManagementInfo(Map<Object, Object> map);

	public List<LocationManagementInfo> getLocationManagementInfoByOrderid(@Param("orderid") String orderid);
	//重置库位
    public int resetLocation(Map<String, String> map);
	public int updateFlag(@Param("id") String id,@Param("type") String type);
    public int searchCount(Map<Object, Object> map);
	public int updatebackEmail(@Param("id") String id,@Param("email") String email);
    public void inertLocationTracking(Map<String, String> map);
	public int addBackUser(@Param("email") String email,@Param("ip") String ip,@Param("userName") String userName);
    public List<LocationTracking> getLocationTracking(Map<Object, Object> map);

    public int searchCount1();

	public String getCreateTime(@Param("barcode") String barcode);

    public List<String> noInspection();

    public List<LocationManagementInfo> getCheckOrders(@Param("orderids") String orderids, @Param("num") int num);

    public List<StorageLocationBean> getAllOutboundorder(Map<Object, Object> map);

    public List<OrderDetailsBean> getOrderDetailsInfo(Map<Object, Object> map);

    public int updateState(Map<String, String> map);

    public int getShortTerm();

    public int getMid();

    public String getOrderIdByBarcode(String barcode);
	/**
	 * 根据入库时运单号获取销售订单号
	 * @param shipno
	 * @return
	 */
	List<String> getSaleOrderid(@Param("shipno") String shipno);
	/**
	 * 查询入库，验货等数量
	 * @param map
	 * @return
	 */
	List<StorageInspectionLogPojo> getStorageInspectionLogInfo(Map<String, String> map);
    public LocationManagementInfo getTaoBaoInfos(String orderid);

    public int getAmounts(String orderid);

    public int getAcounts(String orderid);

    public List<TaoBaoOrderInfo> getPurchaseOrderDetails(Map<Object, Object> map);

    public List<String> getInfos(@Param("admid") int admid);

    public int updateIsRead(@Param("orderid") String orderid);

    public List<Tb1688Account> getAllBuy();

    public void updateMessageY(@Param("ids") String ids);
	/**
	 * 根据用户订单号获取发送邮件需要的用户邮箱等信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getUserOrderInfoByOrderNo(@Param("orderNo") String orderNo);
	public int updateBarcodeByOrderNo(List<String> list);

    public List<TaoBaoOrderInfo> getAllCount(Map<Object, Object> map);

    public int queryOrderState(Map<String, String> map);

    public int updateOrderState(Map<String, String> map);

    public int updateAllDetailsState(Map<String, String> map);

    public int insertRemark(Map<String, String> map);

    public List<String> allLibraryCount();

    /**
	 * 有待确认使用库存的商品
	 * @return  待确认商品库存数量
	 */
    public int getStockOrderInfo();

    public List<StorageLocationBean> allLibrary(Map<Object, Object> map);

	public List<String> checkOrders(@Param("list1") List<StorageLocationBean> list1, @Param("flag") int flag);

	public void treasuryNote(@Param("orderNo") String orderNo, @Param("goodsid") int goodsid, @Param("remarkContent") String remarkContent);
	public int updateCheckForNote(@Param("orderNo") String orderNo, @Param("goodsid") int goodsid);
	public int getDelOrder(String orderNo);
	public void updateOrderChange2State(@Param("orderNo") String orderNo, @Param("goodId") int goodId,
                                        @Param("ropType") Integer ropType);
//	public void updateOrderChange2State(Map<String,Object> map);
	public Map<String, Object> getOrderinfoByOrderNo(String orderNo);
	public List<OrderDetailsBean> getDelOrderByOrderNoAndStatus(@Param("orderNo") String orderNo,
                                                                @Param("goodId") int goodId);
	public int updateOrderDetails2StateByGoodId(@Param("orderNo") String orderNo, @Param("goodId") int goodId);
	public Map<String, Object> getBalance_currency(@Param("userid") int userid);

	public void updateOrderPayPrice1(@Param("userid") int userid, @Param("order_no") String order_no,
                                     @Param("pay_price") String pay_price, @Param("ipnAddress") Object object, @Param("discount_amount") double discount_amount,
                                     @Param("share_discount") double share_discount, @Param("product_cost") double product_cost, @Param("remaining_price") double available_r,
                                     @Param("cashback") double cashback);
	public void addRechargeRecord(RechargeRecord rr);
	public void upUserPrice(@Param("userId") int userid, @Param("price") double available_ru, @Param("acprice") double order_ac);
	public void updateAcceptPriceByOrderNo(@Param("orderNo") String orderNo, @Param("payPrice") String pay_price);
	public void upOrderPurchase(@Param("purchase_state") int purchase_state, @Param("orderNo") String orderNo, @Param("state") int i);
	public void delMessageByOrderid(@Param("orderNo") String orderNo, @Param("count") String count);
	public int updateAcceptShareByOrderNo(@Param("orderNo") String orderNo);
	public Integer checkUpOrderState(String orderNo);
	public List<OrderBean> getOrderInfoByOrder_noAndUserid(@Param("userId") int userid, @Param("order_no")
            String[] split);

	public int insertBuluInfo(List<Map<String, Object>> bgList);
	public List<Dropshiporder> selectByUserIdAndParentOrderNo(@Param("userId") Integer userId, @Param("parentOrderNo") String parentOrderNo);
	public Map<String, Object> getOrdersPay(String orderNo);

	/**
	 * 根据订单号查询订单状态
	 * @param orderNo
	 * @return
	 */
	public String getOrderInfoStateByOrderNo(String orderNo);

	/**
	 * 根据订单号和商品id查询订单详情的状态
	 * @param orderNo
	 * @param odid
	 * @return
	 */
	public int getOrderDetailsStateByOrderNoAndGoodsid(@Param("orderNo") String orderNo, @Param("odid") int odid);
	
	/**
	 * 根据关联的订单号 更新货源表状态
	 * @param orderid
	 * @return
	 */
	public int updateOrderSourceState(@Param("orderid") String orderid);

	/**
	 * 获取订单的所有出运商品
	 * @param orderid
	 * @return
	 */
	public List<String> getAllGoodsPidsByOrderNo(@Param("orderid") String orderid);
	/**
	 * 出库时增加出库明细
	 * @param orderid
	 * @return
	 */
	int insertGoodsInventory(String orderid);
	/**
	 * 根据订单号获取barcode 
	 * @param orderid
	 * @return
	 */
	public List<String> selectBarcideByOrderNo(String orderid);
	
	public int updateBuluOrderState(List<Map<String, Object>> bgList);
	
	public int updateBuluDropShipOrderState(List<Map<String, Object>> list);
	
	public List<Map<String, Object>> getOrderinfoByOrderNoIn(List<String> remarksList);
	
	public List<Map<String, Object>> getOrderDetailsByOrderidIn(List<String> remarksList);
	
	public void insertChangeLog(Map<String, Object> map);
} 
