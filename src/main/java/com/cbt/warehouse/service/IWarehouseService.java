package com.cbt.warehouse.service;

import com.cbt.bean.*;
import com.cbt.bean.OrderBean;
import com.cbt.pojo.BuyerCommentPojo;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.pojo.*;
import com.cbt.warehouse.pojo.ClassDiscount;
import com.cbt.website.bean.*;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IWarehouseService {
	public outIdBean findOutId(Integer uid);
	public OrderAddress getAddressByOrderID(String orderNo);
	public List<PurchaseDetailsBean> getPurchaseDetails(String orderNo);
	public List<PurchaseBean> getOutByID(String userid, String ordernolist);
	public void callResult(Map<String, String> param);
	public void callUpdateIdrelationtable(Map<String, String> param);
	public void insertStorage_location(String a, String b, String c, String d, String e);

	//获得用户id对饮销售email账号密码
	AdmuserPojo getAdmuserSendMailInfo(Map<String, Object> map);
	//运费报表总记录
	List<OrderFeePojo> getOrderfeeFreight(Map<String, Object> map, HttpServletRequest request);
	int getOrderfeeFreightCount(Map<String, Object> map);
	List<JcexPrintInfo> getJcexPrintInfoPlck(Map<String, Object> map);
	List<JcexPrintInfo> getJcexPrintInfoPlckCount(Map<String, Object> map);
	int updateRemainingPrice(Map<String, String> map);
	int updateODPState(Map<String, String> map);
	int updateOpsState(Map<String, String> map);
	int updateOrderinfoNumber(Map<String, String> map);
	int updateOrderinfoState(Map<String, String> map);
	int updateChildOrderinfoState(Map<String, String> map);
	int GetSetOrdrerState(Map<String, String> map);
	int updateOrder(Map<String, String> map);
	int updateorderDetailsState(Map<String, String> map);
	int getOdIsState(Map<String, String> map);
	int updateSendMail(Map<String, String> map);
	int saveClothingData(Map<String, String> map);
	int saveWeight(Map<String, String> map);
	int saveWeightFlag(String pid);

	/**
	 * 产品单页提问图片路径保存
	 * @param id
	 * @param path
	 * @return
	 */
	int updateQuestPicPath(String id, String path);
	/**
	 * 采购页面商品拿样
	 * @param pids
	 * @return
	 */
	List<SampleGoodsBean> getSampleGoods(String[] pids);

	/**
	 * 客户下单商品查询同款信息
	 * @param pids
	 * @return
	 */
	List<SampleGoodsBean> getLiSameGoods(String[] pids, String[] shopIds, String[] pIds);

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
	 * 商品采购后取消退货时产品的库存的也一并取消
	 * @param map
	 * @return
	 */
	int refundGoods(Map<String, String> map);
	/**
	 * 保存采购质量评论内容
	 * @param map
	 * @return
	 */
	int saveQualityData(Map<String, String> map);

	/**
	 * 月用户利润统计获取平均汇率
	 * @param map
	 * @return
	 */
	Map<String,String> getExchange(Map<String, String> map);

	/**
	 *人为编辑过的产品在搜索结果中被呈现次数
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
	 * 验货图片关联验货商品
	 * @param map
	 * @return
	 */
	int insInsp(Map<String,String> map);
	/**
	 * 新增验货图片上传-验货编辑
	 * @param pid
	 * @return
	 */
	int insertInspectionPicture(String pid, String picPath);
	/**
	 * 删除验货图片路径在order_details
	 * @param map
	 * @return
	 */
	int delInPic(Map<String, String> map);
	//申报信息中文
	List<SbxxPojo> getSbxxList(Map<String, String> map);
	//添加申报信息
	int insertSbxx(Map<String, String> map);
	//批量修改出货包裹
	int batchUpdateShippingPackage(List<Map<String, String>> list);

	//出库页面订单的所有包裹
	List<ShippingPackage> getShippingPackageById(Map<String, String> map);
	//出库的时候修改包裹信息
	int bgUpdate(List<Map<String, String>> list);

	int xlsbatch(List<Map<String, String>> list);
	//包裹列表
	List<ShippingPackage> getPackageInfoList(Map<String, String> map);

	int updateGoodsDistribution(Map<String, String> map);

	//获得采购数量
	public String getCgCount(Map<String, Object> map);
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
	public int updateStateCategory(Map<String, String> map);

	/**
	 * 修改搜索词对应的优先类别
	 * @param map
	 * @return
	 */
	public int editKeyword(Map<String, String> map);
	/**
	 * 新增搜索词对应的优先类别
	 * @param map
	 * @return
	 */
	public int addKeyword(Map<String, String> map);
	//获得每月采购数量
	public String getMCgCount(Map<String, Object> map);
	//获取当日采购分配种类
	public String getDistributionCount(Map<String, Object> map);


	//获得实际采购数量
	public String getSjCgCount(Map<String, Object> map);
	//添加订单采购商品备注
	public int insertOrderRemark(Map<String, Object> map);

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
	/**
	 * 出运运费预警时信息录入
	 * @param map
	 * @return
	 */
	public int insertWarningInfo(Map<String, String> map);
	int addGoodsInventory(Map<String, String> map);
	//获得订单问题描述
	String getOrderProblem(Map<String, Object> map);

	//包裹信息
	ShippingPackage getPackageInfo(Map<String, String> map);

	//查询所有待出库订单
	List<StorageLocationBean> getAllOutboundorder(Map<Object, Object> map);

	List<StorageLocationBean> allLibrary(Map<Object, Object> map);

	List<TaoBaoOrderInfo> getPurchaseOrderDetails(Map<Object, Object> map);

	int updateIsRead(String orderid);

	List<String> getInfos(int admid);

	List<Tb1688Account> getAllBuy();

	void updateMessageY(String ids);

	List<TaoBaoOrderInfo> getAllCount(Map<Object, Object> map);

	//查询库位信息
	List<LocationManagementInfo>  getLocationManagementInfo(Map<Object, Object> map);

	List<LocationManagementInfo> getLocationManagementInfoByOrderid(String orderid);

	/**
	 * 根据入库时运单号获取销售订单号
	 * @param shipno
	 * @return
	 */
	List<String> getSaleOrderid(String shipno);
	String getOrderIdByBarcode(String barcode);

	/**
	 * 查询入库，验货等数量
	 * @param map
	 * @return
	 */
	List<StorageInspectionLogPojo> getStorageInspectionLogInfo(Map<String, String> map);
	int getMid();

	int getShortTerm();

	int getAmounts(String orderid);

	int getAcounts(String orderid);

	LocationManagementInfo getTaoBaoInfos(String orderid);

	List<OrderDetailsBean> getOrderDetailsInfo(Map<Object, Object> map);

	List<LocationTracking> getLocationTracking(Map<Object, Object> map);

	int searchCount1();

	int searchCount(Map<Object, Object> map);

	int noInspection();

	public String getCreateTime(String barcode);

	List<String> allLibraryCount();

	/**
	 * 有待确认使用库存的商品
	 * @return  待确认商品库存数量
	 */
	int getStockOrderInfo();

	List<LocationManagementInfo> getCheckOrders(String orderids, int num);

	//重置库位
	int resetLocation(Map<String, String> map);

	int updateFlag(String id,String type);
	int updatebackEmail(String id,String email);
	int addBackUser(String email,String ip,String userName);
	int updateState(Map<String, String> map);

	int insertRemark(Map<String, String> map);

	/**
	 * 1688订单退货状态更改
	 * @param map
	 * @return
	 */
	int updateTbState(Map<String, String> map);
	int queryOrderState(Map<String, String> map);

	int updateOrderState(Map<String, String> map);

	int updateAllDetailsState(Map<String, String> map);

	void inertLocationTracking(Map<String, String> map);

	//查询商品原链接
	public List<Map<String,String>> getGoodsCar(Map<String, Object> map);

	/**
	 * 根据产品1688ID获取信息
	 * @Title getAllGoodsInfos
	 * @Description TODO
	 * @param goods_pids
	 * @return
	 * @return List<AliInfoDataBean>
	 */
	public List<CustomGoodsBean> getAllGoodsInfos(String goods_pids);
	/**
	 * 生成采样订单信息
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
	public OrderDetailsBean getOldDetails(String pid);
	public OrderDetailsBean getCustomBeack(String pid);
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
	public int getOrderDetailsExit(String goods_pid, String car_type);
	/**
	 * 分配采样订单采购
	 * @Title insertGd
	 * @Description TODO
	 * @param list
	 * @return void
	 */
	public void insertGd(List<OrderDetailsBean> list);
	int updateGdOdid();
	/**
	 * 根据订单号查询采样订单详情
	 * @Title getOrderDetailsByOrderid
	 * @Description TODO
	 * @param orderid
	 * @return
	 * @return List<OrderDetailsBean>
	 */
	public List<OrderDetailsBean> getOrderDetailsByOrderid(String orderid);
	/**
	 * 更新28cross_border-custom_benchmark_ready_newest-为2samplingStatus
	 * @Title updateCrossBorder
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateCrossBorder(String goods_pid);
	/**
	 * 更新线上crossshop-custom_benchmark_ready-为2samplingStatus
	 * @Title updateShop
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateShop(String goods_pid);
	/**
	 * 更新alidata中ali_info_data表字段是否采样（sampl_flag）为1
	 * @Title updateSamplFlag
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateSamplFlag(String goods_pid, int count);
	/**
	 * 更新27crossshop-custom_benchmark_ready-为2samplingStatus
	 * @Title updateCrossShopr
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return int
	 */
	public int updateCrossShopr(String goods_pid);

	//库位
	public List<StorageLocationBean> getAllStorageLocationByPage(int startNum, int endNum);

	//StorageLocationBean
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
	 * @param admuserid
	 * @return
	 */
	public List<String> getBuyerName(String admuserid);

	/**
	 * 获取采购账号
	 * @param admuserid
	 * @return
	 */
	public String getBuyerNames(String admuserid);

	/**
	 * 查询采购 所有昨天实际采购的订单
	 * @param map
	 * @return   采购昨天确认实际采购的订单数量集合
	 * whj 2017-05-11
	 */
	List<String> getsourceValidationForBuy(Map<String, Object> map);

	//出货验证
	List<StorageLocationBean> getOrderInfoInspection(Map<String, Object> map, String ip);
	List<StorageLocationBean> getOrderInfoInspectionall(Map<String, Object> map, String ip);
	int getCountOrderInfoInspection(Map<String, Object> map);

	List<Forwarder> getForwarderplck(Map<String, Object> map);
	int getCountForwarderplck(Map<String, Object> map);
	void getShippingPackCost(Forwarder forwarder);
	int updateExperssNoPlck(Map<String, Object> map);
	//验货插入数据
	int insertOrderfeeFromOrderInfo(Map<String, Object> map, int dropshipFlag);

	//根据订单查询收货地址
	String getOrderidAddress(Map<String, Object> map);

	//根据订单查询下单时间
	String getOrderCreateTime(Map<String, Object> map, @Param("dropshipFlag") int dropshipFlag);

	/**
	 * 若拆单了，在该清单的最后要提醒客户还有部分东西在另一个包裹中，并列出哪些还准备发货，哪些取消了
	 * @return
	 */
	int getUndeliveredOrder(String orderId);

	//查询出库打印信息
	List<OrderInfoPrint> getOrderidPrinInfo(Map<String, Object> map);

	//根据订单id删除order_fee表数据 delteFromOrderFeeByOrderid
	int delteFromOrderFeeByOrderid(Map<String, Object> map);

	//已出货列表
	List<Forwarder> getForwarder(Map<String, Object> map);
	int getCountForwarder(Map<String, Object> map);

	//取消合并
	int updateFromOrderFeeByOrderid(Map<String, Object> map);

	//修改快递单号
	int updateExperssNo(Map<String, Object> map);
	//本来差钱但是现在已经把差的钱补齐的订单
	List<OrderInfoPojo> getNotMoneyOrderinfo(Map<String, Object> map);

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
	 * 编辑页面获取验货图片数量
	 * @Title queryPictureInfos
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<SearchResultInfo>
	 */
	int queryPictureInfosCount(Map<String, String> map);
	//修改订单欠费金额1
	int updateOrderinfoAll(Map<String, Object> map);
	//修改订单欠费金额2
	int updateOrderinfo(Map<String, Object> map);

	//获得出货号
	String getShipmentno();

	//批量插入出货包裹
	int batchInsertSP(List<Map<String, String>> list);

	//删除出货
	int deleteShippingPackage(Map<String, String> map);

	int selectShippingPackage(Map<String, String> map);
	//根据订单查询地址
	public String getOrderAddress(Map<String, Object> map);

	//获取国家
	List<OrderFeePojo> getFpxCountryCode();
	//获得数量最多的3张图片
	List<Map<String,String>> getMaxImg(Map<String, String> map);
	//获得种类和数量
	List<Map<String,String>> getCntSum(Map<String, String> map);
	//其他出货方式
	List<OrderFeePojo> getCodemaster();
	//4px运输方式
	List<OrderInfoPojo> getFpxProductCode();
	//修改orderfee
	int updateOrderFeeByOrderid(Map<String, Object> map);
	//出库和待出库数量
	List<OrderInfoPojo> getOutCount();
	//获取所有金额
	OrderInfoPojo getPaymentFy(Map<String, Object> map);

	//*****************************************综合采购**********************************************
	//淘宝商品快递单号，和状态
	List<Logisticsinfo> getlogisticsidAndState(Map<String, Object> map);
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
	 * 获取商品编号根据商品号和订单号
	 * @Title getRefundGoodsPid
	 * @Description TODO
	 * @param goodsid
	 * @param orderid
	 * @return
	 * @return List<String>
	 */
	List<String> getRefundGoodsPid(String goodsid, String orderid);

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
	//淘宝订单信息
	Logisticsinfo getlogisticsinfo(Map<String, Object> map);

	//OrderProductSurc 商品信息
	List<OrderProductSurcePojo> getlogisticsAndOrderProductSurc(Map<String, Object> map);

	//查询对应hscode
	String getHsCode(Map<String, Object> map);


	//****************************************库存*************************************************
	List<OrderInfoPrint> getIdrelationtable(Map<String, Object> map);
	int getCountIdrelationtable(Map<String, Object> map);

	//根据不同的状态获得订单的数量
	OrderInfoCountPojo getOrderInfoCountByState(Map<String, Object> map);

	//获取疑似货源贴错
	OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map);
	/**
	 * 获取入库没有匹配商品的订单
	 * @Title getNoMatchOrderByTbShipno
	 * @Description TODO
	 * @param map
	 * @return
	 * @return OrderInfoCountPojo
	 */
	OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map);
	List<String> getNoShipInfoOrder(Map<String, String> map);
	//点了采购确认
	List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map);

	//查询用户信息
	List<UserInfo> getUserInfoForPrice(Map<String, Object> map);

	/**
	 * 1688采购订单建议退货管理
	 * @param goodsid
	 * @return
	 */
	List<TaoBaoOrderInfo> getBuyReturnManage(String goodsid, int page, String state, String startTime, String endTime);

	/**
	 * 1688采购订单建议退货管理
	 * @param goodsid
	 * @return
	 */
	int getBuyReturnManageCount(String goodsid, String state, String startTime, String endTime);

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
	 * 获取店铺产品信息
	 * @param map
	 * @return
	 */
	List<ShopManagerPojo> getShopManagerDetailsList(Map<String, Object> map);

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
	 * 供应商的采购历史
	 * @param map
	 * @return
	 */
	public List<ShopManagerPojo> getShopBuyLogInfo(Map<String, String> map);
	/**
	 * 供应商的采购历史
	 * @param map
	 * @return
	 */
	public int getShopBuyLogInfoCount(Map<String, String> map);

	/**
	 * 搜索词优先类别
	 * @param map
	 * @return
	 */
	List<ShopManagerPojo> getPriorityCategory(Map<String, Object> map);
	/**
	 * 搜索词优先类别
	 * @param map
	 * @return
	 */
	int getPriorityCategoryCount(Map<String, Object> map);
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
	 * 查询采样退样信息
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
	 * 获取所有采购人
	 * @return
	 */
	List<com.cbt.pojo.AdmuserPojo> getAllBuyer(int id);
	/**
	 * 添加采样商品备注
	 * @Title addSampleRemark
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int addSampleRemark(Map<String, Object> map);
	/**
	 * 删除手工录入的替换货源
	 * @Title deleteSource
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int deleteSource(Map<String, Object> map);
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
	 *更新类别的最低价格最高价
	 * @param map
	 * @return
	 */
	int updateCatePrice(Map<String, String> map);

	/**
	 * 获取采购评论商品的前置信息
	 * @param map
	 * @return
	 */
	BuyerCommentPojo getBuyerCommentPojo(Map<String, String> map);

	/**
	 * 采购添加商品评论
	 * @param map
	 * @return
	 */
	int saveCommentContent(Map<String, String> map);
	/**
	 * 获取验货时商品的重量
	 * @return
	 */
	SearchResultInfo getWeight();
	/**
	 * 记录入库扫描没有匹配到商品淘宝
	 * @Title insertStorageProblemOrder
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	int insertStorageProblemOrder(Map<String, Object> map);
	//根据1688pid获取ali pid
	String getAliPid(String goods_pid);
	List<Inventory> getHavebarcode();
	/**
	 * 人工赋能统计数据查询
	 * @Title getPurchaseSamplingStatistics
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PurchaseSamplingStatisticsPojo>
	 */
	List<PurchaseSamplingStatisticsPojo> getPurchaseSamplingStatistics(Map<String, Object> map);

	public List<Inventory> getAllUser();

	/**
	 * 人为编辑过的产品销售业绩
	 * @param map
	 * @return
	 */
	List<PurchaseSamplingStatisticsPojo> salesPerformance(Map<String, Object> map);
	List<PurchaseSamplingStatisticsPojo> salesPerformanceCount(Map<String, Object> map);

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
	 * 清洗质量
	 * @param map
	 * @return
	 */
	public List<PurchaseSamplingStatisticsPojo> getCleaningQuality(Map<String, String> map);

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
	public List<PurchaseSamplingStatisticsPojo> getCleaningQualityCount(Map<String, Object> map);
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
	 * 获取店铺产品信息数量
	 * @param map
	 * @return
	 */
	int getShopManagerListDetailsCount(Map<String, Object> map);

	/**
	 * 采购前置页面数据曾现
	 * @param map
	 * @return
	 */
	List<PrePurchasePojo> getPrePurchase(Map<String, Object> map);
	/**
	 * 根据入库未匹配到的订单号查询订单信息
	 * @Title getPrePurchaseForTB
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PrePurchasePojo>
	 */
	List<PrePurchasePojo> getPrePurchaseForTB(Map<String, Object> map);
	/**
	 * 根据入库未匹配到的订单号查询订单信息数量
	 * @Title getPrePurchaseForTB
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<PrePurchasePojo>
	 */
	List<PrePurchasePojo> getPrePurchaseForTBCount(Map<String, Object> map);
	/**
	 * 获取某个采购的订单分配商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getFpCount(String orderid, String admuserid);
	/**
	 * 采购前置页面数据条数
	 * @param map
	 * @return
	 */
	List<String> getPrePurchaseCount(Map<String, Object> map);

	/**
	 * 更改店铺状态
	 * @param map
	 * @return
	 */
	int updateShopState(Map<String, Object> map);

	List<UserInfo> getUserInfoForPriceCount(Map<String, Object> map);



	//获得淘宝状态
	Tb1688Pojo getTbState(Map<String, Object> map);

	//问卷调查统计
	List<AllProblemPojo> getAllProblem(Map<String, Object> map);
	int getTotalNumber(Map<String, Object> map);
	List<String> getAllProposal(Map<String, Object> map);

	//补货
	int insertOrderReplenishment(Map<String, Object> map);
	//采购补货时将录入的店铺ID添加到28库中
	int insertShopId(Map<String, Object> map);

	//补货按钮状态改变
	int updateReplenishmentState(Map<String, Object> map);

	//添加补货记录
	int addReplenishmentRecord(Map<String, Object> map);
	//是否存在补货
	List<OrderReplenishmentPojo> getIsReplenishment(Map<String, Object> map);
	//查询补货记录
	List<Replenishment_RecordPojo> getIsReplenishments(Map<String, Object> map);
	//获取采样Log
	List<DisplayBuyInfo> displayBuyLog(Map<String, Object> map);
	//查询线下采购记录
	List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String, Object> map);
	//根据产品ID获取工厂和工厂级别
	Map<String,String> getCompanyInfo(String goods_pid);
	//申报信息
	int insertDeclareinfo(Map<String, Object> map);

	//jcex打印信息
	List<JcexPrintInfo> getJcexPrintInfo(Map<String, Object> map);

	//写入导出数据
	int updateDeclareinfoByOrderid(Map<String, Object> map);

	List<Integer> queryUser();

	//多货源
	int delteOrderReplenishment(Map<String, Object> map);
	//根据userid 获取用户信息
	public String getUserName(String userid);
	//根据orderid 插入goods_inventory 表数据

	//同时根据orderid 将查询的数据返回
	public List<GoodsInventory>  addGoodsInvent(String orderid);

	//出库列表新添一条记录
	public int insertSp(String shipmentno);
	//根据orderno 查询数据
	public int selectOrderFeeByOrderid(Map<String, Object> map);
	//获取dropship子单信息
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
	//出库  更新orderinfo状态
	public int upOrderInfo(Map<String, Object> map);
	public int upId_relationtable(Map<String, Object> map);
	public int upOrderFee(Map<String, Object> map);
	public int upWaraingState(Map<String, Object> map);

	/**
	 * 出库审核通过时判断该订单是否有合并出运订单
	 * @param orderId
	 * @return
	 */
	public String getMegerOrder(String orderId);
	public List<String> getOrderFeeOrderNO(String orderno);
	//更新dropship子单状态
	public int updateDropshipState(Map<String, Object> map);
	//判断主单状态
	public String  checkState(String orderno);

	public int updateMainDropshipState(String mainOrder);
	//入库插入信息
	int insertId_relationtable(Map<String, String> map);

	public int updateBarcodeByOrderNo(String orderid);

	/**
	 * 根据用户订单号获取发送邮件需要的用户邮箱等信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getUserOrderInfoByOrderNo(String orderNo);

	//入库备注
	public void treasuryNote(String orderNo, int goodsid, String remarkContent);

	/**
	 * 强制更新疑问验货为验货无误
	 * @param orderNo
	 * @param goodsid
	 * @return
	 * whj 2017-04-25
	 */
	public int updateCheckForNote(String orderNo, int goodsid);
	/**
	 * 删除订单内的商品
	 * @param orderNo
	 * @param goodId
	 * @return {删除标志，订单状态}
	 */
	public Integer[] deleteOrderGoods(String orderNo, int goodId, int userid,
                                      int purchase_state,
                                      List<ClassDiscount> cdList,
                                      HttpServletResponse response);
	public List<OrderBean> getOrderInfo(int parseInt, String orderNo);

	public int insertBuluInfo(List<Map<String, Object>> bgList);

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
	public int getOrderDetailsStateByOrderNoAndGoodsid(String orderNo, int odid);

	/**
	 * 根据关联的订单号  更新货源表 商品状态
	 * @param orderid
	 */
	public int updateOrderSourceState(String orderid);

	/**
	 * 将出运的订单 商品在授权表中标记
	 * @param orderid
	 * @return
	 */
	public int checkAuthorizedFlag(String orderid);

	/**
	 * 更新补录订单状态
	 * @param bgList
	 * @return
	 */
	public int updateBuluOrderState(List<Map<String, Object>> bgList);

	/**
	 * 出库时增加出库明细
	 * @param orderid
	 * @return
	 */
	int insertGoodsInventory(String orderid);

	/**
	 * 通过订单编号查询订单的入库找和出库照等
	 * @param remarks
	 * @return
	 */
	public Map<String, Object> getDetailsByRemarks(String remarks);
	
	public void insertChangeLog(Map<String, Object> map);
}
 