package com.cbt.orderinfo.service;

import com.cbt.bean.*;
import com.cbt.email.entity.EmailReceive1;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.service.TabTransitFreightinfoUniteNewExample;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderinfoService {

	public List<Map<String, String>> getOrders(int userID, int state, Date startdate, Date enddate, String email,
                                               String orderno, int startpage, int page, int admuserid, int buyid, int showUnpaid, String type, int status);
	/**
	 * 判断该订单是否有验货疑问
	 * @param orderid
	 * @return
	 * whj 2017-04-26
	 */
	public String getProblem(String orderid);

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

	int deleteFlagByOrder(String orderNo);

	int insertFlagByOrderid(String orderNo);

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
	public String getCountryById(String country, String userid);

	/**
	 * 用户添加订单记录
	 * @return
	 */
	public int addOrderInfo(List<OrderBean> OrderBean, int address);

	/**
	 * 添加订单详情信息
	 * @param orderdetails
	 * @return
	 */
	public int addOrderDetail(List<OrderDetailsBean> orderdetails);

	/**
	 * 新增地址
	 * @param addressMap
	 * @return
	 */
	public int addOrderAddress(Map<String, String> addressMap);

	/**
	 * 后台生成订单号
	 * @param payment
	 * @return
	 */
	public int addPayment(PaymentBean payment);

	/**
	 * 获取用户地址
	 * @return
	 */
	public List<Address> getUserAddr(int userId);

	/**
	 * 添加订单生成记录
	 * @param userid
	 * @param orderno
	 * @param dealMan
	 * @return
	 */
	public int addPaymentNote(String userid, String orderno, String dealMan, String upfile);

	public int countPaymentInvoiceByorderuser(String userid, String orderno);

	/**
	 * 更新进账记录
	 * @param userid
	 * @param orderno
	 * @param dealMan
	 * @return
	 */
	public int updatePaymentNote(String userid, String orderno, String dealMan, String upfile);

	public UserBean getUserFromIdForCheck(int userId);

	public List<OrderDetailsBean> getCatidDetails(String orderid);

	public List<TabTransitFreightinfoUniteNew> selectByExample(TabTransitFreightinfoUniteNewExample example);

	/**
	 * 商品入库操作
	 * @param map
	 * @return
	 */
	public int updateGoodStatus(Map<String, String> map);

	public List<OrderDetailsBean> getAllCancelDetails(Map<String, String> map);

	/**
	 * 一键确认或取消入库
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> allTrack(Map<String, String> map);

	/**
	 * 更改订单销售和采购
	 * @param map
	 * @return
	 */
	public int addUser(Map<String, String> map);

	/**
	 *1688采购订单建议退货管理记录
	 * @param map
	 * @return
	 */
	public int orderReturn(Map<String, String> map);

	/**
	 * 取消入库
	 * @param map
	 * @return
	 */
	public int updatecanceltatus(Map<String, String> map);

	/**
	 *验货无误
	 * @param map
	 * @return
	 */
	public int updateTbstatus(Map<String, String> map);

	/**
	 * 验货无误成功，判断该订单是否全部到库并且验货无误
	 * @param orderid
	 * @return
	 */
	public int checkOrderState(String orderid);

	/**
	 * 取消验货
	 * @param map
	 * @return
	 */
	public int updatecancelChecktatus(Map<String, String> map);


	/**
	 * 更改采购人
	 * @param map
	 * @return
	 */
	public int changeBuyer(Map<String, String> map);

	/**
	 * 记录扫描日子
	 * @param shipno
	 * @param admName
	 * @return
	 */
	public int insertScanLog(String shipno, String admName);

	/**
	 * 根据运单号获取淘宝订单信息
	 * @param shipno
	 * @return
	 */
	public List<Tb1688OrderHistory> getGoodsData(String shipno);

	/**
	 * 查询需要入库的商品信息
	 * @param shipno
	 * @param checked
	 * @return
	 */
	public List<SearchResultInfo> getOrder(String shipno, String checked);

	/**
	 * 获取采购人
	 * @return
	 */
	public List<com.cbt.pojo.Admuser> getAllBuyer();

	public OrderBean getChildrenOrders(String orderNo);

	public String getAllFreightByOrderid(String orderNo);

	/**
	 * 获取DP订单详情
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo);


	/**
	 * 查询订单状态为支付失败或者order_pending的订单
	 * @return
	 */
	public List<Map<String, String>> getorderPending();

	/**
	 * 根据客户订单获取邮箱地址
	 * @param orderNo
	 * @return
	 */
	public String getUserEmailByOrderNo(String orderNo);

	/**
	 * 订单详情改价等操作提醒客户个人中心
	 * @param orderNo
	 * @return
	 */
	public int updateOrderinfoUpdateState(String orderNo);

	/**
	 * 查看该订单是否发送过质检信息邮件
	 * @param orderNo
	 * @return
	 */
	public int checkRecord(String orderNo);

	/**
	 * 记录该订单已发送质检信息邮件
	 * @param orderNo
	 * @return
	 */
	public int insertEmailRecord(String orderNo);

	/**
	 * 根据订单号查询订单详情
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> getOrdersDetails(String orderNo);

	/**
	 * 更改购物车表的商品备注为已读
	 * @param orderNo
	 * @return
	 */
	public int updateGoodsCarMessage(String orderNo);

	/**
	 * 查询最近半年的订单号
	 * @return
	 */
	public List<String> getOrderIdList();

	/**
	 * 获取采购账号名称
	 * @param admuserId
	 * @return
	 */
	public String queryBuyCount(int admuserId);

	/**
	 * 获取商品物流信息
	 * @param tb1688Itemid
	 * @param lastTb1688Itemid
	 * @param confirmTime
	 * @param admName
	 * @param shipno
	 * @param offlinePurchase
	 * @param orderId
	 * @param goodsId
	 * @return
	 */
	public TaoBaoOrderInfo getShipStatusInfo(String tb1688Itemid, String lastTb1688Itemid, String confirmTime, String admName, String shipno, int offlinePurchase, String orderId, int goodsId);

	/**
	 * 获取一个订单支付的每笔金额
	 * @param orderNo
	 * @return
	 */
	public List<Map<String, String>> getOrdersPays(String orderNo);

	/**
	 * 获取物流公司名称
	 * @return
	 */
	public List<CodeMaster> getLogisticsInfo();

	/**
	 * 获取同一客户还没出运的订单
	 * @return
	 */
	public List<String> getOrderNos(int userId, String orderNo);

	/**
	 * 获取对同地址不同账号客户
	 * @return
	 */
	public List<String> getSameAdrDifAccount(int userId, String address, String street, String zipCode,
			String country, String city, String recipients);
	
	
	/**
	 * dp订单实际支付金额
	 * @param orderNo
	 * @return
	 */
	public double getAcPayPrice(String orderNo);

	/**
	 * 查看项目对应邮件内容
	 * @param orderNo
	 * @return
	 */
	public List<EmailReceive1> getall(String orderNo);

	/**
	 * 获取订单运输方式
	 * @param orderNo
	 * @return
	 */
	public String getModeTransport(String orderNo);

	/**
	 * 获取用户评价信息
	 * @param orderNo
	 * @return
	 */
	public Evaluate getEvaluate(String orderNo);

	/**
	 * 查询货代相关信息
	 * @param orderNo
	 * @return
	 */
	public Forwarder getForwarder(String orderNo);

	/**
	 * 根据订单好查询订单信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getOrders(String orderNo);

	/**
	 * 获取纯采购和采销一体账户信息
	 * @return
	 */
	public List<Admuser> getBuyerAndAll();

	/**
	 * 获取运费公式信息
	 * @param countryNameCn
	 * @param isEub
	 * @return
	 */
	public TabTransitFreightinfoUniteOur getFreightInfo(String countryNameCn, int isEub);
	public double getFreightFee(String allFreight, OrderBean orderInfo);

	/**
	 * 更新订单预估国际运费
	 * @param orderNo
	 * @param freight
	 * @return
	 */
	public int updateFreightForOrder(String orderNo, String freight, String esBuyPrice);

	/**
	 * 更新订单的预估金额
	 * @param orderNo
	 * @param freight
	 * @return
	 */
	public int updateFreight(String orderNo, String freight);



	/**
	 * 根据订单号获取出库信息
	 * @param orderNo
	 * @return
	 */
	public List<ShippingBean> getShipPackmentInfo(String orderNo);

	/**
	 * 获取订单的预估运费
	 * @param orderNo
	 * @return
	 */
	public Double getEstimatefreight(String orderNo);

	/**
	 * 获取订单产品的重量
	 * @param orderNo
	 * @return
	 */
	public double getAllWeight(String orderNo);

	/**
	 * 获取订单的上传文件
	 * @param orderNo
	 * @return
	 */
	public String getFileByOrderid(String orderNo);

	/**
	 * 自生成订单列表
	 * @param orderid
	 * @param userid
	 * @param page
	 * @return
	 */
	public List<AutoOrderBean> getOrderList(String orderid, String userid, String page);

	/**
	 * 取消自生成订单
	 * @param orderid
	 * @return
	 */
	public int cancelOrder(String orderid);

	/**
	 * 取消生成的进账记录
	 * @param pid
	 * @return
	 */
	public int cancelPayment(String pid);

	/**
	 * 订单管理页面查询
	 * @return
	 */
	public List<Map<String, String>> getOrderManagementQuery(Map<String,String> paramMap);
	//有订单留言的订单
	public List<Map<String, String>> getOrders1(int userID, int state, Date startdate, Date enddate, String email,
                                                String orderno, int startpage, int page, int admuserid, int buyid, int showUnpaid, String type, int status);
	public int getOrdersCount(Map<String,String> paramMap);
	
	/**
	 * 统计订单每个状态的数量
	 * 
	 * @return
	 * 		订单状态值，状态对应数量值
	 */
	public List<Map<String, Integer>> getOrdersState(int admuserid);

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
	int queryUserIdByOrderNo(String orderNo);

	List<Orderinfo> getAllOrderShippingMehtodIsNull();
	int getCountryIdByName(String countryNameEn);

	int updateOrderinfomodeTransport(String modeTransport, String orderNo);

	Boolean UpdateGoodsState(String goods_pid);

	Boolean UpdateAllGoodsState(String tbOrderId);

    boolean getSampleschoice(String orderNo);
}
