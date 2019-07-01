package com.cbt.orderinfo.dao;

import com.cbt.bean.*;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Inventory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public interface OrderinfoMapper {
	/**
	 * 订单数量
	 * @return
	 */
	public int getOrdersCount(Map<String,String> paramMap);
	/**
	 * 订单信息
	 * @return
	 */
	public List<Map<String, String>> getOrders(@Param("userID") int userID, @Param("state") int state, @Param("startdate") Date startdate, @Param("enddate") Date enddate, @Param("email") String email,
                                               @Param("orderno") String orderno, @Param("startpage") int startpage, @Param("page") int page, @Param("admuserid") int admuserid, @Param("buyid") int buyid, @Param("showUnpaid") int showUnpaid, @Param("type") String type, @Param("status") int status);
	//订单留言的订单
	public List<Map<String, String>> getOrders1(@Param("userID") int userID, @Param("state") int state, @Param("startdate") Date startdate, @Param("enddate") Date enddate, @Param("email") String email,
                                                @Param("orderno") String orderno, @Param("startpage") int startpage, @Param("page") int page, @Param("admuserid") int admuserid, @Param("buyid") int buyid, @Param("showUnpaid") int showUnpaid, @Param("type") String type, @Param("status") int status);
	/**
	 * 判断该订单是否有验货疑问
	 * @param orderid
	 * @return
	 * whj 2017-04-26
	 */
	public String getProblem(@Param("orderid") String orderid);
	/**
	 * 批量查询缓存订单信息
	 * @param list
	 * @return
	 */
	public int insertOrderinfoCache(List<Map<String, String>> list);
	/**
	 * 刷新订单预估运费
	 * @return
	 */
	public List<OrderBean> getFlushOrderFreightOrder();
	public List<OrderBean> getAllOrderInfo();
	int deleteFlagByOrder(@Param("orderNo") String orderNo);

	int insertFlagByOrderid(@Param("orderNo") String orderNo);
	/**
	 * 刷新取消订单或商品取消的预估运费
	 * @return
	 */
	public List<OrderBean> getFlushOrderFreightOrderCancel();
	/**
	 * 查询没有预估运费的订单数
	 * @return
	 */
	public int getAllOrderinfoFreight();
	/**
	 * 获取需要刷新预估国际运费的订单
	 * @return
	 */
	public List<OrderBean> getAllNoFreight();
	/**
	 * 获取订单号
	 * @return
	 */
	public String getOrderNo();
	/**
	 * 获取汇率
	 * @return
	 */
	public String getExchangeRate();

	/**
	 * 获取国家
	 * @return
	 */
	public String getCountryById(@Param("country") String country);

	public String getOtherCountry(@Param("userid") String userid);

	/**
	 * 用户添加订单记录
	 * @return
	 */
	public int addOrderInfo(@Param("list1") List<OrderBean> list1, @Param("address") int address);
	/**
	 * 添加订单详情信息
	 * @param orderdetails
	 * @return
	 */
	public int addOrderDetail(@Param("orderdetails") List<OrderDetailsBean> orderdetails);
	/**
	 * 新增地址
	 * @param addressMap
	 * @return
	 */
	public int addOrderAddress(Map<String, String> addressMap);
	public UserBean getUserFromIdForCheck(@Param("userId") int userId);


	public int getPayMentCount(PaymentBean payment);

	public int updatePayMent(PaymentBean payment);

	public int insertPayMent(PaymentBean payment);
	/**
	 * 获取用户地址
	 * @return
	 */
	public List<Address> getUserAddr(@Param("userId") int userId);

	/**
	 * 添加订单生成记录
	 * @param userid
	 * @param orderno
	 * @param dealMan
	 * @return
	 */
	public int addPaymentNote(@Param("userid") String userid, @Param("orderno") String orderno, @Param("dealMan") String dealMan, @Param("upfile") String upfile);
	public int countPaymentInvoiceByorderuser(@Param("userid") String userid, @Param("orderno") String orderno);
	/**
	 * 更新进账记录
	 * @param userid
	 * @param orderno
	 * @param dealMan
	 * @return
	 */
	public int updatePaymentNote(@Param("userid") String userid, @Param("orderno") String orderno, @Param("dealMan") String dealMan, @Param("upfile") String upfile);

	public List<OrderDetailsBean> getCatidDetails(@Param("orderid") String orderid);

	public Map<String,String> getRemarkInspetion(Map<String, String> map);
	public String getPositionByBarcode(@Param("barcode") String barcode, @Param("type") String type);
	int insertIdRelationtable(Map<String, String> map);
	int getIdRelationtable(Map<String, String> map);
	int updateIdRationtable(Map<String, String> map);
	int updateOrderReState(Map<String, String> map);
	int updateBuyState(Map<String, String> map);
	int updateState(Map<String, String> map);
	int updateOrderSourceState(Map<String, String> map);
	public int getAdmNameByShipno(Map<String, String> map);
	public List<TabTransitFreightinfoUniteNew> selectByExample(TabTransitFreightinfoUniteNewExample example);
	public int updateIdrelationtable(Map<String, String> map);

	public int updateStorageProblemOrder(Map<String, String> map);
	/**
	 * 更改采购人
	 * @param map
	 * @return
	 */
	public int changeBuyer(Map<String, String> map);

	/**
	 * 查询是否为DP订单
	 * @param map
	 * @return
	 */
	public int queyIsDropshipOrder(Map<String, String> map);

	/**
	 * 根据spec_id关联抓取订单的id
	 * @param map
	 * @return
	 */
	public String getTypeNameByOdid(Map<String, String> map);

	/**
	 * 查询入库对应的淘宝订单
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getTaobaoInfoByOrderid(Map<String, String> map);
	public int updateOrderDetails(Map<String, String> map);

	public int getDtailsState(Map<String, String> map);
	public int updateDropshiporder(Map<String, String> map);

	/**
	 * 查询DP所有子单是否到库
	 * @param map
	 * @return
	 */
	public int getAllChildOrderState(Map<String, String> map);

	/**
	 * 更新orderinfo的state=2
	 * @param map
	 * @return
	 */
	public int updateOrderInfoState(Map<String, String> map);

	public Map<String,String> queryData(Map<String, String> map);

	public int insertGoodsInventory(Map<String, String> map);

	public int getDetailsState(Map<String, String> map);
	/**
	 * 查询商品是否分配过采购人
	 * @param map
	 * @return
	 */
	public int queryGoodsDis(Map<String, String> map);

	/**
	 * 记录扫描日子
	 * @param shipno
	 * @param admName
	 * @return
	 */
	public int insertScanLog(@Param("shipno") String shipno, @Param("admName") String admName);

	/**
	 * 根据运单号获取淘宝订单信息
	 * @param shipno
	 * @return
	 */
	public List<Tb1688OrderHistory> getGoodsData(@Param("shipno") String shipno);

	public String getAdminid(@Param("shipno") String shipno);

	public List<Map<String,String>> getOrderDataOne(@Param("shipno") String shipno);

	/**
	 * 获取产品的店铺Id
	 * @param goodsPid
	 * @return
	 */
	public String getShopId(@Param("goodsPid") String goodsPid);
	public List<Map<String,String>> getOrderData(@Param("shipno") String shipno, @Param("adminid") int adminid);
	public List<OrderDetailsBean> getAllCancelDetails(Map<String, String> map);
	public String getShopCustomId(@Param("goodsPid") String goodsPid);
	/**
	 * 更新商品的采购人
	 * @param map
	 * @return
	 */
	public int updateGoodsDis(Map<String, String> map);

	public int updateIdrelationtableFlag(Map<String, String> map);

	public int getInCount(Map<String, String> map);
	public int udpateStorage(Map<String, String> map);
	public int updateDetailsShipno(Map<String, String> map);
	public int updateOrderState(Map<String, String> map);
	public int updateOrderRe(Map<String, String> map);
	public int updateOrderSource(Map<String, String> map);
	public String getGoodsPrice(Map<String, String> map);

	/**
	 * 查询入库明细记录
	 * @param map
	 * @return
	 */
	public Map<String,String> getGoodsInventory(Map<String, String> map);
	/**
	 * 更改订单销售和采购
	 * @param map
	 * @return
	 */
	public int updateAdminUser(Map<String, String> map);

	/**
	 * 新增用户销售人
	 * @param map
	 * @return
	 */
	public int insertAdminUser(Map<String, String> map);

	public int updateChecked(Map<String, String> map);

	public int updateSourceState(Map<String, String> map);

	public Map<String,String> getInspetionMap(Map<String, String> map);

	public int updateDetails(Map<String, String> map);

	public int updateRelationTable(Map<String, String> map);

	/**
	 * 验货无误成功，判断该订单是否全部到库并且验货无误
	 * @param orderid
	 * @return
	 */
	public int checkOrderState(@Param("orderid") String orderid);

	/**
	 * 如果该商品验货是有录入库存则做想应的减少
	 * @param map
	 * @return
	 */
	public Map<String,String> getInventoryMap(Map<String, String> map);

	/**
	 * 查询库存信息
	 * @param map
	 * @return
	 */
	public Inventory getInventoryInfo(Map<String, String> map);

	public int updateInventory(Map<String, String> map);

	public int updateInventoryFlag(Map<String, String> map);

	public int updateUutboundDetails(Map<String, String> map);

	/**
	 * 查询订单的销售
	 * @param map
	 * @return
	 */
	public int queryAdmin(Map<String, String> map);

	/**
	 * 获取销售人名称
	 * @param map
	 * @return
	 */
	public String getAdmName(Map<String, String> map);
	/**
	 *1688采购订单建议退货管理记录
	 * @param id
	 * @param orderid
	 * @return
	 */
	public int orderReturn(Map<String, String> map);

	/**
	 * 判断销售是否为采销一体账户
	 * @param map
	 * @return
	 */
	public int queryAllSales(Map<String, String> map);

	/**
	 * 判断订单是否分配过采销员
	 * @param map
	 * @return
	 */
	public int queryOrderBuyer(Map<String, String> map);

	/**
	 * 更改订单的采销
	 * @param map
	 * @return
	 */
	public int updateOrderBuyer(Map<String, String> map);

	/**
	 * 新增采销人
	 * @param map
	 * @return
	 */
	public int insertOrderBuyer(Map<String, String> map);

	/**
	 * 获取DP订单详情
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> getChildrenOrdersDetails(@Param("orderNo") String orderNo);
	public String getAllFreightByOrderid(@Param("orderNo") String orderNo);
	/**
	 * 获取采购人
	 * @return
	 */
	public List<com.cbt.pojo.Admuser> getAllBuyer();
	public OrderBean getChildrenOrders(@Param("orderid") String orderid);
	/**
	 * 订单管理页面查询
	 * @return
	 */
	public List<Map<String, String>> getOrderManagementQuery(Map<String,String> paramMap);

	public List<Map<String,String>> getAllOrderCatid(@Param("orderids") String orderids);
	/**
	 * 查询订单状态为支付失败或者order_pending的订单
	 * @return
	 */
	public List<Map<String, String>> getorderPending(@Param("orderIds") List<String> orderIds);

	/**
	 * 根据订单号获取订单的支付状态
	 * @param orderNo
	 * @return
	 */
	public Payment getPayTypeForOrderNo(@Param("orderNo") String orderNo);
	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> getOrdersDetails(@Param("orderNo") String orderNo);

	/**
	 * 根据订单号获取订单变更信息
	 * @param orderNo
	 * @return
	 */
	public List<OrderChange> getOrderChange(@Param("orderNo") String orderNo);
	/**
	 * 获取一个订单支付的每笔金额
	 * @param orderNo
	 * @return
	 */
	public List<Map<String, String>> getOrdersPays(@Param("orderNo") String orderNo, @Param("flag") int flag);
	/**
	 * 获取物流公司名称
	 * @return
	 */
	public List<CodeMaster> getLogisticsInfo();
	/**
	 * 获取同一客户还没出运的订单
	 * @return
	 */
	public List<String> getOrderNos(@Param("userId") int userId, @Param("orderNo") String orderNo);
	
	/**
	 * 获取对同地址不同账号客户
	 * @return
	 */
	public List<String> getSameAdrDifAccount(@Param("userId") int userId, @Param("address") String address,
			@Param("street") String street,@Param("zipCode") String zipCode,@Param("country") String country,
			@Param("city") String city,@Param("recipients") String  recipients);
	
	/**
	 * dp订单实际支付金额
	 * @param orderNo
	 * @return
	 */
	public double getAcPayPrice(@Param("orderNo") String orderNo);
	/**
	 * 获取订单运输方式
	 * @param orderNo
	 * @return
	 */
	public String getModeTransport(@Param("orderNo") String orderNo);
	/**
	 * 查看项目对应邮件内容
	 * @param orderNo
	 * @return
	 */
	public List<EmailReceive1> getall(@Param("orderNo") String orderNo);
	/**
	 * 获取用户评价信息
	 * @param orderNo
	 * @return
	 */
	public Evaluate getEvaluate(@Param("orderNo") String orderNo);
	/**
	 * 获取商品物流信息
	 * @param tb1688Itemid
	 * @param admName
	 * @return
	 */
	public TaoBaoOrderInfo getShipStatusInfo(@Param("itmeid") String tb1688Itemid, @Param("createtime") String createtime, @Param("admName") String admName);

	public TaoBaoOrderInfo getShipStatusInfos(@Param("itemid") String itemid, @Param("itemids") String itemids, @Param("createtime") String createtime, @Param("admName") String admName);

	public TaoBaoOrderInfo getOrderReplenishment(@Param("tb1688Itemid") String tb1688Itemid, @Param("lastTb1688Itemid") String lastTb1688Itemid, @Param("confirmTime") String confirmTime,
                                                 @Param("admName") String admName, @Param("shipno") String shipno,
                                                 @Param("offlinePurchase") int offlinePurchase, @Param("orderId") String orderId, @Param("goodsId") int goodsId);

	public TaoBaoOrderInfo getShipStatusInfoFor(@Param("tb1688Itemid") String tb1688Itemid, @Param("lastTb1688Itemid") String lastTb1688Itemid, @Param("confirmTime") String confirmTime,
                                                @Param("admName") String admName, @Param("shipno") String shipno,
                                                @Param("offlinePurchase") int offlinePurchase, @Param("orderId") String orderId, @Param("goodsId") int goodsId);
	/**
	 * 获取采购账号名称
	 * @param admuserId
	 * @return
	 */
	public String queryBuyCount(@Param("admuserId") int admuserId);
	/**
	 * 查询最近半年的订单号
	 * @return
	 */
	public List<String> getOrderIdList();
	/**
	 * 更改购物车表的商品备注为已读
	 * @param orderNo
	 * @return
	 */
	public int updateGoodsCarMessage(@Param("orderNo") String orderNo);
	/**
	 * 根据订单好查询订单信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getOrder(@Param("orderNo") String orderNo);
	/**
	 * 查询货代相关信息
	 * @param orderNo
	 * @return
	 */
	public Forwarder getForwarder(@Param("orderNo") String orderNo);
	/**
	 * 根据订单号获取出库信息
	 * @param orderNo
	 * @return
	 */
	public List<ShippingBean> getShipPackmentInfo(@Param("orderNo") String orderNo);

	/**
	 * 获取订单的预估运费
	 * @param orderNo
	 * @return
	 */
	public Double getEstimatefreight(@Param("orderNo") String orderNo);
	/**
	 * 获取订单产品的重量
	 * @param orderNo
	 * @return
	 */
	public double getAllWeight(@Param("orderNo") String orderNo);
	/**
	 * 获取订单的上传文件
	 * @param orderNo
	 * @return
	 */
	public String getFileByOrderid(@Param("orderNo") String orderNo);

	/**
	 * 取消自生成订单
	 * @param orderNo
	 * @return
	 */
	public int cancelOrder(@Param("orderNo") String orderNo);
	/**
	 * 取消生成的进账记录
	 * @param pid
	 * @return
	 */
	public int cancelPayment(@Param("pid") String pid);
	/**
	 * 自生成订单列表
	 * @param orderid
	 * @param userid
	 * @param page
	 * @return
	 */
	public List<AutoOrderBean> getOrderList(@Param("orderid") String orderid, @Param("userid") String userid, @Param("page") String page);

	public int getCounts();
	/**
	 * 获取运费公式信息
	 * @param countryNameCn
	 * @param isEub
	 * @return
	 */
	public TabTransitFreightinfoUniteOur getFreightInfo(@Param("countryNameCn") String countryNameCn, @Param("isEub") int isEub);
	/**
	 * 更新订单的预估金额
	 * @param orderNo
	 * @param freight
	 * @return
	 */
	public int updateFreight(@Param("orderNo") String orderNo, @Param("freight") String freight);

	/**
	 * 更新订单预估国际运费
	 * @param orderNo
	 * @param freight
	 * @return
	 */
	public int updateFreightForOrder(@Param("orderNo") String orderNo, @Param("freight") String freight, @Param("esBuyPrice") String esBuyPrice);

	/**
	 * 获取纯采购和采销一体账户信息
	 * @return
	 */
	public List<Admuser> getBuyerAndAll();
	/**
	 * 根据订单号和商品号获取工厂信息
	 * @param orderNo
	 * @param goodsid
	 * @return
	 */
	public List<String> getBhShopId(@Param("orderNo") String orderNo, @Param("goodsid") int goodsid);

	public List<Map<String,String>> getOrderRemark(@Param("orderNo") String orderNo);

	public String getCatid(@Param("goodscatid") String goodscatid);
	/**
	 * 获取支付时间或到账订单的订单号
	 * @return
	 */
	public List<String> getOrderIds(@Param("admuserid") int admuserid);
	/**
	 * 根据客户订单获取邮箱地址
	 * @param orderNo
	 * @return
	 */
	public String getUserEmailByOrderNo(@Param("orderNo") String orderNo);
	/**
	 * 订单详情改价等操作提醒客户个人中心
	 * @param orderNo
	 * @return
	 */
	public int updateOrderinfoUpdateState(@Param("orderNo") String orderNo);
	/**
	 * 查看该订单是否发送过质检信息邮件
	 * @param orderNo
	 * @return
	 */
	public int checkRecord(@Param("orderNo") String orderNo);

	/**
	 * 记录该订单已发送质检信息邮件
	 * @param orderNo
	 * @return
	 */
	public int insertEmailRecord(@Param("orderNo") String orderNo);
	/**
	 * 统计订单每个状态的数量
	 *
	 * @return
	 * 		订单状态值，状态对应数量值
	 */
	public List<Map<String, Integer>> getOrdersState(@Param("admuserid") int admuserid);

	/**
	 * 获取纯销售和采销一体账户信息
	 * @return
	 */
	public List<ConfirmUserInfo> getAllSalesAndBuyer();

	/**
	 * 根据订单号查询客户ID
	 * @param orderNo
	 * @return
	 */
	int queryUserIdByOrderNo(@Param("orderNo") String orderNo);
	List<Orderinfo> getAllOrderShippingMehtodIsNull();
	int updateOrderinfomodeTransport(@Param("modeTransport") String modeTransport, @Param("orderNo") String orderNo);
	public String getDropshipOrderNoList(String orderNo);

    void updateBusiess(Map<String, String> map);
    int updateOrderDetailsState(@Param("odid")String odid, @Param("orderid") String orderid);
    @Update("UPDATE goods_list_search SET goods_state=1 WHERE pid=#{goods_pid}")
    int UpdateGoodsState(@Param("goods_pid") String goods_pid);
    @Insert("insert into goods_list_search(pid,goods_state) values (#{goods_pid},1)")
	int InserGoodsState(@Param("goods_pid")String goods_pid);
    @Select("SELECT itemid as goods_pid from taobao_1688_order_history WHERE orderid=#{tbOrderId}")
	List<String> FindAllGoodsPid(@Param("tbOrderId") String tbOrderId);

    String getSampleschoice(@Param("orderNo") String orderNo);

}