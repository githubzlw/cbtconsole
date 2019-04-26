package com.cbt.website.dao;

import com.cbt.bean.*;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.report.vo.UserBehaviorBean;
import com.cbt.report.vo.UserBehaviorDetails;
import com.cbt.warehouse.pojo.Shipment;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.bean.QualityResult;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;
import com.cbt.website.bean.UserBehavior;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ylm 后台订单管理
 */
public interface IOrderwsDao {

	/**
	 * 获取订单信息 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<OrderBean> getOrders(int userID, int state, Date startdate, Date enddate, String username, String email,
	                                 String orderno, String phone, int startpage, int endpage, int admuserid, int buyid, int showUnpaid,
	                                 int status);
	/**
	 * 根据1688pid获取ali  pid
	 * @Title getAliPid
	 * @Description TODO
	 * @param goods_pid
	 * @return
	 * @return String
	 */
	public String getAliPid(String goods_pid);
	/**
	 * 获取订单条数 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public int getOrdersPage(int userID, int state, Date date, String username, String email, String orderno,
	                         String phone);

	/**
	 * 查询订单是否取消 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public int iscloseOrder(String orderNo);

	/**
	 * 获取订单状态
	 *
	 * @date 2016年11月16日
	 * @author abc
	 * @param userid
	 * @param orderno
	 * @return
	 */
	public OrderBean getStateByOrderNo(int userid, String orderno);
	/**
	 * 整单取消时判断线上订单状态
	 * @param orderNo
	 * @return
	 */
	public int checkOrderState(String orderNo,String isDropshipOrder1);
	/**
	 * 采购页面查询订单支付金额等信息
	 * @param orderNo
	 * @return
	 */
	public Map<String,String> queryOrderAmount(String orderNo);
	/**
	 * 根据订单号获取ipn付款信息
	 * @param orderNo
	 * @return
	 */
	public int getIpnPaymentStatus(String orderNo);
	/**
	 * 获取商品验货是商品的质检信息
	 * @param map
	 * @return
	 */
	public List<QualityResult> openCheckResult(Map<String,String> map);
	/**
	 * 获取所有的采购人员信息
	 *
	 * @return
	 */
	public List<Admuser> getAllBuyer();

	/**
	 * 修改采购人
	 *
	 * @param odid
	 * @param admuserid
	 * @return
	 */
	public int changeBuyer(int odid, int admuserid);

	public int changeOrderBuyer(String orderid, int admuserid,String odids);

	public int addOrderDetails(String goodsid, String count, String newOrderid, String orderid,int admuserid);

	public int addOrderInfo(String orderid, String newOrderid, int length);

	/**
	 * 获取订单的采购人
	 * @param orderid
	 * @return 采购人ID
	 *whj 2017-05-02
	 */
	public int addAutoAdmuser(String orderid);

	public boolean deleteOrderInfo(String newOrderid);

	public int judgeOrderState(String orderid);

	/**
	 * 获取订单信息 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public OrderBean getOrders(String orderNo);
	/**
	 * 获取订单出运
	 * @param orderNo
	 * @return
	 */
	public double getEstimatefreight(String orderNo);
	/**
	 * 获取产品表中的重量
	 * @param orderNo
	 * @return
	 */
	public double getAllWeight(String orderNo);
	/**
	 * 获取订单出运信息
	 * @param orderid
	 * @return
	 */
	public ShippingBean getShipPackmentInfo(String orderid);
	/**
	 * 获取订单信息
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public List<OrderBean> getListOrders(String orderNo);

	/**
	 * 订单支付确认信息
	 */
	public PaymentConfirm queryForPaymentConfirm(String orderNo);

	/**
	 * 查看该用户是否为黑名单
	 * @param orderid
	 * @return
	 */
	public int isTblack(String userName);

	/**
	 * 关联订单 = 同一客户，还没出货的 订单 ylm
	 *
	 * @param userid
	 *            用户ID
	 */
	public List<String> getOrderNos(int userid,String orderid);

	/**
	 * 获取用户总付款金额 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public Object[] getOrdersPay(String orderNo);

	/**
	 * 获取用户总付款金额 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public double getOrdersPayUserid(int userid);

	public double getOrdersCancelUserid(int userid);

	/**
	 * 获取用户总付款金额 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public Map<String, String> getOrdersPayByUserids(List<Integer> list);

	/**
	 * 获取一个订单支付的每笔金额 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public List<Map<String, String>> getOrdersPays(String orderNo);

	/**
	 * 修改订单详情中 每个产品的状态 ylm
	 *
	 * @param orderDatailId,state
	 *            订单详情ID,订单状态
	 */
	public int upOrderDeatail(int orderDatailId, int state);

	/**
	 * 修改订单详情中 每个产品的状态 ylm
	 *
	 * @param orderDatailId,state
	 *            订单ID,订单状态
	 */
	public int upOrderDeatailstate(String ordeId, int state);

	/**
	 * 获取订单详情 ylm
	 *
	 * @param orderNo
	 * @param state订单状态
	 */
	public List<OrderDetailsBean> getOrdersDetails(String orderNo);

	/**
	 * 根据订单号获取国际运费
	 * @Title getAllFreightByOrderid
	 * @Description TODO
	 * @param orderid
	 * @return
	 * @return double
	 */
	public double getAllFreightByOrderid(String orderid);
	/**
	 * 更改购物车表的商品备注为已读
	 * @param orderNo
	 */
	public void updateGoodsCarMessage(String orderNo);
	/**
	 * 获取商品的物流信息
	 * @param tb_1688_itemid
	 * @param last_tb_1688_itemid
	 * @param time  确认采购时间
	 * @param admName  采购人
	 * @return  商品物流信息
	 */
	public TaoBaoOrderInfo getShipStatusInfo(String tb_1688_itemid,String last_tb_1688_itemid, String time, String admName,String shipno,int offline_purchase,String orderid,int goodsid);

	/**
	 * 增加订单详情中的到货信息 ylm
	 *
	 * @param orderDatailId,【收到的货物照片（上传）】实际重量
	 *            实际体积【实际采购价格】,国内运费 订单详情ID,订单状态
	 */
	public int upOrderDeatail(int orderDatailId, String file, String weight, String volume, String actual_price,
	                          String actual_freight, String file_upload);

	public int upOrder(String orderNo, String actual_ffreight, String custom_discuss_other, int state, Date date,
	                   String actual_weight, String actual_volume, Date expect_arrive_date, String actual_allincost,
	                   double remaining_price_d, double order_ac, String service_fee, String domestic_freight,
	                   String mode_transport, double actual_freight_c_, float payPrice);

	/**
	 * ylm 录入 货代相关信息
	 *
	 * @param 订单好,
	 *            快递跟踪单
	 *
	 */
	public int saveForwarder(Forwarder forwarder);

	/**
	 * 查询 货代相关信息 ylm
	 *
	 * @param 订单好,
	 *            快递跟踪单
	 *
	 */
	public Forwarder getForwarder(String orderNo);

	public int getChangegooddata(String orderNo);

	/**
	 * 修改 货代相关信息 ylm
	 *
	 * @param 订单好,
	 *            快递跟踪单
	 *
	 */
	public int upForwarder(Forwarder forwarder);

	/**
	 * ylm 获取用户对订单评价信息
	 *
	 * @param orderNo
	 *            订单号
	 * @return
	 */
	public Evaluate getEvaluate(String orderNo);

	/**
	 * 服务器端修改订单问题
	 *
	 * @param orderNo
	 * @param goodId
	 * @param oldInfo
	 * @param newInfo
	 * @param changeType
	 * @return
	 */
	public int updateOrderChange(String orderNo, int goodId, String oldInfo, String newInfo, int changeType);

	/**
	 * 后台 问题解决了
	 *
	 * @param orderNo
	 * @param goodId
	 * @param changeType
	 * @return
	 */
	public int upOrderChangeResolve(String orderNo, int goodId, int changeType);

	/**
	 * 获取订单改变信息
	 *
	 * @param orderNo
	 * @param goodId
	 * @param changeType
	 * @param lastNum
	 * @return
	 */
	public Map<String, Object> getOrderChange(String orderNo, int goodId, int changeType, int lastNum);

	/**
	 * 获取订单改变信息
	 *
	 * @param orderNo
	 * @param goodId
	 * @param changeType
	 * @param lastNum
	 * @return
	 */
	public List<Object[]> getOrderChanges(String orderNo, int lastNum);

	/**
	 * 通过订单好 获取用户bean
	 *
	 * @param orderNo
	 * @return
	 */
	public UserBean getUserBeanByOrderNo(String orderNo);

	/**
	 * 修改订单状态
	 *
	 * @param orderNo
	 * @param orderStatus
	 *            (state)状态（-1-等待付款,0-购买中，1-产品买不到或失效，2-产品买了还没到我们仓库，3-
	 *            产品买了并已经到我们仓库,4出运中，5完结，6-等待第二次付款）
	 * @return
	 */
	public int updateOrderStatus(String orderNo, int orderStatus);

	/**
	 * 修改订单状态为2-已全部到仓库
	 *
	 * @param orderNo
	 * @return
	 */
	public int updateOrderStatus(String orderNo);

	/**
	 * 确认并告知客户更改信息
	 *
	 * @param orderNo
	 * @return
	 */
	public int updateOrderChangeStatus(String orderNo);

	/**
	 * 获取告知客户更改信息的内容，发邮件用
	 *
	 * @param orderNo
	 * @return
	 */
	public List<Integer> getOrderChangeStatus(String orderNo);

	/**
	 * 服务器 修改 客户 服务器 更新状态
	 *
	 * @param orderNo
	 * @return
	 */
	public int updateClinetAndServerUpdateState(String orderNo, int clientUpdate, int serverUpdate);

	/**
	 * 方法描述:根据订单详情id查询单个订单详情信息 author: lizhanjun date:2015年4月15日
	 *
	 * @param id
	 * @return
	 */
	public OrderDetailsBean getById(int id);

	/**
	 * 查询用户在确认价格中操作confirm
	 *
	 * @param orderNo
	 * @return 商品ID,类型，最新值
	 */
	public List<Object[]> getOrderChanges(String orderNo);

	/**
	 * ylm 修改预订单内容表 订单号，回答，运费，关税
	 *
	 * @param
	 */
	public int upQuestions(String orderid, String answer, String freight, String tariffs);

	/**
	 * ylm 查询预订单内容表是否存在
	 *
	 * @param
	 */
	public String getAdvance(String orderid);

	/**
	 * ylm 查询预订单内容表是否存在
	 *
	 * @param
	 */
	public AdvanceOrderBean getAdvanceBean(String orderid);

	/**
	 * ylm 添加预订单内容表
	 *
	 * @param
	 */
	public int addAdvance(String orderid, String freight, String tariffs);

	/**
	 * 修改用户数量和金额 ylm
	 *
	 * @param type
	 *            修改类型（0数量，1价格）
	 *
	 */
	public int updateGoods(int type, int goodsid, String value);

	/**
	 * 黑名单邮件获取ip
	 *
	 * @param
	 */
	public Set<String> getOrderIpByUserId(String userId);

	/**
	 * 系统后台取消订单 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public int closeOrder(String orderNo);

	/**
	 * 判断取消订单是否为测试订单
	 * @param orderid
	 * @return
	 */
	public boolean checkTestOrder(String orderid);
	/**
	 * 将该订单中未占用库存的商品添加到库存表中
	 * @param orderNo
	 * whj 2018-05-04
	 */
	public void cancelInventory1(String orderNo);

	/**
	 * 释放该订单占用的库存
	 * @param orderNo
	 */
	public void cancelInventory(String orderNo);

	/**
	 * 查询邮费申请折扣 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public List<Object[]> getGoodpostage(int userid, int page, int endpage);

	/**
	 * 查询邮费申请折扣条数 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public int getGoodpostageNumber(int userid);

	/**
	 * 确认单件商品货源 ylm
	 *
	 * @param orderNo订单号，orderdetailid订单详情id，purchase_confirmation确认人员
	 *
	 */
	public int upOrderPurchase(int orderdetailid, String orderNo, String purchase_confirmation);

	/**
	 * 获取订单备注内容 wanyang
	 *
	 * @param orderid
	 *            订单号
	 *
	 */
	public List<Object[]> getOrderRemark(String orderid);

	/**
	 * 添加订单备注内容 wanyang
	 *
	 * @param orderid
	 *            订单号
	 *
	 */
	public int addOrderRemark(String orderid, String orderremark, int remarkuserid, int type);

	/**
	 * 取消单件商品采购状态 wanyang
	 *
	 * @param orderNo订单号，orderdetailid订单详情id，purchase_confirmation确认人员
	 *
	 */
	public int cancelOrderPurchase(int orderdetailid, String orderNo);

	/**
	 * 获取更新价格后的总价 wanyang
	 *
	 */
	public List<String> getNewTotalPrice(String orderNo);

	/**
	 * 将用户绑定相应的负责人，方便查看订单 wanyang
	 *
	 */
	// public int addUserInCharge(int userid,String username,String
	// useremail,int adminid,String admName);
	/**
	 * 修改用户绑定相应的负责人，方便查看订单 sj
	 *
	 */
	public int updateUserInCharge(int userid, int adminid, String admName);

	/**
	 * 修改用户绑定相应的负责人，方便查看订单 sj
	 *
	 */
	public int queryUserInCharge(int userid);

	/**
	 * 注册客户数量 wanyang
	 *
	 */
	public int getCountofRegisterUser(String date);

	/**
	 * 昨天进入的用户总数量 wanyang
	 *
	 */
	public int getCountofUserEnterinsite(String date);

	/**
	 * 当日有搜索但没添加购物车的客户 wanyang
	 *
	 */
	public int getCountofSearchNotAdd(String date);

	/**
	 * 有添加购物车的客户数量 （以前就注册过的客户，当日完成注册的客户，未注册的客户） wanyang
	 *
	 */
	public int getCountofAddGoodscar(String date);

	/**
	 * 当日添加购物车，但未完成注册的客户（ 只有一件商品的 ） wanyang
	 *
	 */
	public int getCountofAddNotRegister_1(String date);

	/**
	 * 当日添加购物车，但未完成注册的客户（有多件商品在购物车中的） wanyang
	 *
	 */
	public int getCountofAddNotRegister_2(String date);

	/**
	 * 当日添加购物车，完成注册但没输入地址的客户 wanyang
	 *
	 */

	public int getCountofRegisterNotAddr(String date);

	/**
	 * 当日添加购物车，在支付界面点了支付按钮，但在PayPal没完成支付的客户 wanyang
	 *
	 */
	public int getCountofPaypal(String date);

	/**
	 * 线上新下单的客户数量 wanyang
	 *
	 */
	public int getCountofNeworder(String date);

	/**
	 * 注册客户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofRegister(String date);

	/**
	 * 进入网站的用户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofEnterinsite(String date);

	/**
	 * 进入当日有搜索但没添加购物车的客户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofSearchNotAdd(String date);

	/**
	 * 有添加购物车的客户数量 （未注册的客户）信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofAddGoodscar(String date);

	/**
	 * 当日添加购物车，但未完成注册的客户（ 只有一件商品的 ）信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofAddNotRegister_1(String date);

	/**
	 * 当日添加购物车，但未完成注册的客户（多件商品的 ）信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofAddNotRegister_2(String date);

	/**
	 * 当日添加购物车，完成注册但没输入地址的客户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofRegisterNotAddr(String date);

	/**
	 * 当日添加购物车，在支付界面点了支付按钮，但在PayPal没完成支付的客户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofPaypal(String date);

	/**
	 * 线上新下单的客户信息及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofNeworder(String date);

	/**
	 * 记录用户轨迹行为 wanyang
	 *
	 */
	public List<UserBehavior> recordUserBehavior(int userid, int page, int pagesize, String view_date_time);

	/**
	 * 有添加购物车的客户数量 （当日完成注册的客户） wanyang
	 *
	 */
	public int getCountofAddGoodscar_1(String date);

	/**
	 * 有添加购物车的客户信息（当日完成注册的客户）及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofAddGoodscar_1(String date);

	/**
	 * 有添加购物车的客户信息（以前就注册过的客户）及购物车产品数量 wanyang
	 *
	 */
	public int getCountofAddGoodscar_2(String date);

	/**
	 * 有添加购物车的客户信息（以前就注册过的客户）及购物车产品数量 wanyang
	 *
	 */
	public List<Object> getUserofAddGoodscar_2(String date);

	/**
	 * 获取订单类别和价格 ylm
	 *
	 */
	public List<Object[]> getOrderClass(String orderNo);

	/**
	 * 增加订单优惠变更记录 ylm
	 *
	 *//*
	 * public int addDiscountChenge(String orderNo,String sprice,String
	 * price,String info);
	 */

	public List<CodeMaster> getLogisticsInfo();

	/**
	 * 交期预警列表 author:wanyang 2015-11-03
	 */
	public List<Object[]> getDeliveryWarningList(int adminid, int userid);

	/**
	 * 交期预警添加采购天数 author:wanyang 2015-11-05
	 */
	public int updatePurchaseDays(String orderid, int days);

	/**
	 * 更新订单状态for销售人员 orderid:订单id，state：状态 author:wanyang 2015-11-06
	 */
	public int updateOrderState(String orderid, int state);

	/**
	 * 根据用户ID查询确认价格中订单号 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<String> getOrdersNos(int userID);

	/**
	 * 查询订单号是否存在
	 *
	 * @param orderNo
	 *
	 */
	public int existOrders(String orderNo);

	/**
	 * 运费抵扣
	 *
	 * @param orderNo
	 *
	 */
	public OrderBean getOrder_remainingPrice(String orderNo);

	/**
	 * 运费抵扣
	 *
	 * @param orderNo
	 *
	 */
	public int upOrder_remainingPrice(String orderNo, double remainingPrice, double order_ac);

	/**
	 * 新增运费减免记录
	 *
	 * @param orderNo
	 *
	 */
	public int addOrder_reductionfreight(String orderNo, double price);

	/**
	 * 修改运费减免记录
	 *
	 * @param orderNo
	 *
	 */
	public int upOrder_reductionfreight(String orderNo, double price, String remark);

	/**
	 * 获取原始运费
	 *
	 * @param orderNo
	 *
	 */
	public double getOrder_reductionfreight(String orderNo);

	/**
	 * yue 获取超过2天或者超过五天的未支付订单
	 *
	 * @return
	 */
	public List<Object[]> CheckUnpaidOrder();

	public String getOrdersEvaluateByOrderNo(String orderNo);

	public List<Shipment> validateShipmentList(List<Shipment> list, String uuid);

	public List<OrderBean> getOrderChangeState(String orderNo);

	public String queryCountryNameByOrderNo(String orderNo);

	public List<Integer> getRepeatUserid(int id);

	public List<Map<String, String>> getCustCountry(String orderno);

	public List<Map<String, String>> getBuyerByOrderNo(String orderno);

	public OrderBean getChildrenOrders(String orderNo);

	public List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo);

	/**
	 * 根据订单号查询是否是dropship订单
	 *
	 * @param orderNo
	 *            订单号
	 * @return
	 */
	public int isDropshipOrder(String orderNo) throws Exception;

	/**
	 *
	 * @param orderNo
	 * @return
	 */
	public String queryMainOrderByDropship(String orderNo) throws Exception;

	/**
	 * 根据订单号查询dropship的主订单是否已取消
	 *
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public int isCloseDropshipOrder(String orderNo) throws Exception;

	/**
	 * 根据主订单号查询dropship的主订单是否已取消
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public int isCloseByDropshipMainOrder(String mainOrderNo) throws Exception;

	/**
	 * 取消dropship子订单
	 *
	 * @param orderNo
	 *            订单号
	 * @return
	 */
	public int closeDropshipOrder(int userId,String orderNo) throws Exception;

	/**
	 * 根据dropship主订单号取消所有子订单
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public int closeDropshipOrderByMainOrderNo(String mainOrderNo) throws Exception;

	/**
	 * 根据dropship主订单号取消所有关联商品
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public int closeDropshipOrderGoodsByMainOrderNo(String mainOrderNo) throws Exception;

	/**
	 * 根据订单号判断dropship订单是否都取消
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public boolean checkDropshipIsCancel(String mainOrderNo) throws Exception;

	/**
	 * 根据dropship订单号,设置主订单号的状态为取消状态
	 *
	 * @return
	 */
	public int closeOrderByDropshipOrder(int userId,String orderNo) throws Exception;

	/**
	 * 根据dropship子订单号取消所有关联的商品
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public int closeOrderGoodsByDropshipOrder(int userId,String orderNo) throws Exception;

	/**
	 * 减去dropship主订单的商品总价和运费
	 *
	 * @param mainOrderNo
	 * @param totalPrice
	 * @param orderAc
	 * @return
	 */
	public int updateMainOrderNoTotalPriceAndFreight(int userId,String mainOrderNo, float totalPrice, float orderAc,float extraFreight, float weight)
			throws Exception;

	/**
	 *
	 * @Title queryGoodsPriceFromDetails
	 * @Description 根据订单查询详情中商品的总和
	 * @param orderNo
	 * @return float
	 */
	public float queryGoodsPriceFromDetails(String orderNo);

	TabTransitFreightinfoUniteOur getFreightInfo(String orderNo,int isEub);

	/**
	 *
	 * @Title statisticsRegisterUser
	 * @Description 新注册用户统计
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsRegisterUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryRegisterUserDetails
	 * @Description 分组显示新注册用户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryRegisterUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsAddFirstAddress
	 * @Description 当日录入收货地址的用户,过滤掉 已经有一个地址，录入更多地址的情况。 也就是说 要统计第一次录入地址的用户
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsAddFirstAddress(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryAddFirstAddressDetails
	 * @Description 分组显示当日录入收货地址的用户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryAddFirstAddressDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsAddCarWithNoRegisterUser
	 * @Description 有添加购物车的客户数量 （未注册的客户）
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsAddCarWithNoRegisterUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryAddCarWithNoRegisterUserDetails
	 * @Description 分组显示有添加购物车的客户 （未注册的客户）
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryAddCarWithNoRegisterUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsAddCarWithHasRegisterUser
	 * @Description 有添加购物车的客户数量 （有注册过的客户）
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsAddCarWithHasRegisterUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryAddCarWithHasRegisterUserDetails
	 * @Description 分组显示有添加购物车的客户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryAddCarWithHasRegisterUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsAddCarWithOldUser
	 * @Description 有添加购物车的客户数量 （老客户 （以前购买过））
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsAddCarWithOldUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryAddCarWithOldUserDetails
	 * @Description 分组显示有添加购物车的客户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryAddCarWithOldUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsMakeOrderAllUser
	 * @Description 当日下单的 总客户
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsMakeOrderAllUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryMakeOrderAllUserDetails
	 * @Description 分组显示当日下单的 总客户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryMakeOrderAllUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsMakeOrderNewUser
	 * @Description 当日下单的 新客户数量
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsMakeOrderNewUser(String beginDate,String endDate, int ipFlag);

	/**
	 *
	 * @Title queryMakeOrderNewUserDetails
	 * @Description 分组显示当日下单的 新客户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryMakeOrderNewUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *
	 * @Title statisticsPayOrderUser
	 * @Description 当日付款按钮的用户数量
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return int
	 */
	int statisticsPayOrderUser(String beginDate,String endDate, int ipFlag);

	/**
	 * 产品单页浏览总次数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	int statisticsRecentView(String beginDate,String endDate, int ipFlag);
	/**
	 *
	 * @Title queryPayOrderUserDetails
	 * @Description 分组显示当日付款按钮的用户
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 * @return List<UserBehaviorDetails>
	 */
	List<UserBehaviorDetails> queryPayOrderUserDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 * 分组显示浏览次数最大的50个产品
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 */
	List<UserBehaviorDetails> queryUserRecentView(String beginDate,String endDate,int startNum,int offSet, int ipFlag);

	/**
	 *Pay按钮点击独特人数
	 * @param beginDate
	 * @param endDate
	 * @param startNum
	 * @param offSet
	 * @return
	 */
	List<UserBehaviorDetails> queryUserPayLogDetails(String beginDate,String endDate,int startNum,int offSet, int ipFlag);
	/**
	 *
	 * @Title queryExistsBehaviorData
	 * @Description 查询已经存在的单日统计信息
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @return List<UserBehaviorBean>
	 */
	List<UserBehaviorBean> queryExistsBehaviorData(String beginDate,String endDate);

	/**
	 * Pay按钮点击独特人数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	int queryUserPayLog(String beginDate,String endDate, int ipFlag);

    int queryBehaviorRecord(String beginDate, String endDate, int ipFlag);
}
