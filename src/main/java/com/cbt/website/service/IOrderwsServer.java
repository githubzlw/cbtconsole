package com.cbt.website.service;

import com.cbt.bean.*;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.website.bean.PaymentConfirm;
import com.cbt.website.bean.QualityResult;
import com.cbt.website.bean.TabTransitFreightinfoUniteOur;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderwsServer {

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
	public int checkOrderState(String orderNo, String isDropshipOrder1);

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
	public List<QualityResult> openCheckResult(Map<String, String> map);
	/**
	 * 获取订单信息
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<OrderBean> getOrders(int userID, int state, Date startdate, Date enddate, String username, String email,
                                     String orderno, String phone, int page, int admuserid, int buyid, int showUnpaid, int status);

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
	 * 获取订单信息
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
	 * 修改订单详情中 每个产品的状态
	 *
	 * @param orderDatailId,state
	 *            订单详情ID,订单状态
	 */
	public int upOrderDeatail(int orderDatailId, int state, String orderNo, int userId);

	/**
	 * 获取订单详情
	 *
	 * @param orderNo订单号
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
	public TaoBaoOrderInfo getShipStatusInfo(String tb_1688_itemid, String last_tb_1688_itemid, String time, String admName, String shipno, int offline_purchase, String orderid, int goodsid);

	/**
	 * 补货订单生成
	 */
	public int addOrderDetails(String goodsid, String count, String newOrderid, String orderid, int admuserid);

	/**
	 * 获取订单的采购人
	 * @param orderid
	 * @return
	 */
	public int addAutoAdmuser(String orderid);

	public int addOrderInfo(String orderid, String newOrderid, int length);

	/**
	 * 判断该新订单是否取消
	 *
	 * @param orderid
	 * @return
	 */
	public int judgeOrderState(String orderid);

	public boolean deleteOrderInfo(String newOrderid);

	/**
	 * 订单支付确认信息
	 */
	public PaymentConfirm queryForPaymentConfirm(String orderNo);

	/**
	 * 查询该用户是否为黑名单
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
	public List<String> getOrderNos(int userid, String orderid);

	/**
	 * 增加订单中的到货信息
	 *
	 * @param orderNo,
	 *            【实际国际运费】【客服评论：运费计算细节】【客服评论：其他】
	 *
	 */
	public int upOrder(int userId, String orderNo, String actual_ffreight, String custom_discuss_other,
                       Date transport_time, String actual_weight, int orderState, String actual_volume, Date expect_arrive_date,
                       String actual_allincost, String remaining_price, double order_ac, String service_fee,
                       String domestic_freight, String mode_transport, double actual_freight_c_, float exchange_rate,
                       float applicable_credit);

	/**
	 * 增加订单详情中的到货信息
	 *
	 * @param orderDatailId,【收到的货物照片（上传）】实际重量
	 *            实际体积【实际采购价格】 订单详情ID,订单状态
	 */
	public int upOrderDeatail(int orderDatailId, String filepath, String weight, String volume, String actual_price,
                              String actual_freight, String file_upload);

	/**
	 * 录入 货代相关信息 ylm
	 *
	 * @param 订单好,
	 *            快递跟踪单
	 *
	 */
	public int saveForwarder(Forwarder forwarder);

	/**
	 * ylm 获取用户对订单评价信息
	 *
	 * @param orderNo
	 *            订单号
	 * @return
	 */
	public Evaluate getEvaluate(String orderNo);

	/**
	 * 查询 货代相关信息
	 *
	 * @param 订单好,
	 *            快递跟踪单
	 *
	 */
	public Forwarder getForwarder(String orderNo);

	/**
	 * 查询 替换产品信息
	 *
	 * @param 订单号
	 *
	 */
	public int getChangegooddata(String orderNo);

	/**
	 * 后台 修改客户订单要求
	 *
	 * @param orderNo
	 * @param goodId
	 * @param oldInfo
	 * @param newInfo
	 * @param changeType
	 * @return
	 */
	public String updateOrderChange(String orderNo, int goodId, String oldInfo, String newInfo, int changeType);

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
	 * 获取 修改订单内容的信息
	 *
	 * @param orderNo
	 * @param goodId
	 * @param changeType
	 * @param lastNum
	 * @return
	 */
	public Map<String, Object> getOrderChange(String orderNo, int goodId, int changeType, int lastNum);

	/**
	 * 获取 修改订单内容的信息
	 *
	 * @param orderNo
	 * @param goodId
	 * @param changeType
	 * @param lastNum
	 * @return
	 */
	public List<Object[]> getOrderChanges(String orderNo, int lastNum);

	/**
	 * 订单发送确定消息
	 *
	 * @param isDropship
	 * @param orderNo1
	 */
	public String sendCutomers(String serverName, int port, String orderNo, int whichOne, int isDropship,
                               String orderNo1);

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
	public AdvanceOrderBean getAdvanceBean(String orderid);

	/**
	 * 获取一个订单支付的每笔金额 ylm
	 *
	 * @param orderNo
	 *            用户ID,订单状态
	 */
	public List<Map<String, String>> getOrdersPays(String orderNo);

	/**
	 * 修改用户数量和金额 ylm
	 *
	 * @param type
	 *            修改类型（0数量，1价格）
	 *
	 */
	public int updateGoods(int type, int goodsid, String value);

	/**
	 * 释放该订单占用的库存
	 * @param orderNo
	 * whj 2017-05-15
	 */
	public void cancelInventory(String orderNo);

	/**
	 * 系统后台取消订单 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public int closeOrder(String orderNo);

	/**
	 * 判断取消订单是否是测试订单
	 * @param orderid
	 * @return
	 */
	public boolean checkTestOrder(String orderid);
	/**
	 * 将该订单中未占用库存的商品添加到库存表中
	 * @param orderNo
	 * whj 2017-05-15
	 */
	public void cancelInventory1(String orderNo);

	/**
	 * 查询邮费申请折扣 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public List<Object[]> getGoodpostage(int userid, int page);

	/**
	 * 查询邮费申请折扣条数 ylm
	 *
	 * @param orderNo订单号
	 *
	 */
	public int getGoodpostageNumber(int userid);

	/**
	 * 单件商品确认货源 ylm
	 *
	 * @param orderNo订单号，orderdetailid订单详情id，purchase_confirmation确认人员
	 *
	 */
	public int upOrderPurchase(int orderdetailid, String orderNo, String purchase_confirmation);

	/**
	 * 取消单件商品采购状态 wanyang
	 *
	 * @param orderNo订单号，orderdetailid订单详情id
	 *
	 */
	public int cancelOrderPurchase(int orderdetailid, String orderNo);

	/**
	 * 取得物流公司名称 zlw
	 *
	 * @param
	 *
	 */
	public List<CodeMaster> getLogisticsInfo();

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
	 * 发送出货邮件
	 *
	 * @param orderNo
	 *
	 */
	public int sendShipment(String orderNo, int userid, String email, String expect_arrive_time);

	/**
	 * 新增运费减免记录
	 *
	 * @param orderNo
	 *
	 */
	public int addOrder_reductionfreight(String orderNo, double price);

	public String getOrdersEvaluateByOrderNo(String orderNo);

	public List<OrderBean> getOrderChangeState(String orderNo);

	public String queryCountryNameByOrderNo(String orderNo);

	public int saveOrderProfit(String orderid, String esProfit, String acProfit,String endProfit);

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
	 * 查询 同一个人是否有不同的账号
	 */
	public List<Integer> getRepeatUserid(int id);

	/**
	 * 修改采购人
	 *
	 * @param odid
	 * @param admuserid
	 * @return
	 */
	public int changeBuyer(int odid, int admuserid);

	/**
	 * 整单更改采购
	 *
	 * @param orderid
	 * @return
	 */
	public int changeOrderBuyer(String orderid, int admuserid, String goodsids);

	/**
	 * 查询客户 订单国家 lyb
	 */
	public List<Map<String, String>> getCustCountry(String orderno);

	// 查询商品的采购人员 lyb
	public List<Map<String, String>> getBuyerByOrderNo(String orderno);

	public List<Admuser> getAllBuyer();

	public OrderBean getChildrenOrders(String orderNo);

	List<OrderDetailsBean> getChildrenOrdersDetails(String orderNo);

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
	 * 根据订单号查询是否是dropship主订单号
	 *
	 * @param userId
	 *            用户id
	 * @param mainOrderNo
	 *            主订单号
	 * @param childOrderNo
	 *            子订单号
	 * @param totalPrice
	 *            商品总价
	 * @param freight
	 *            运费
	 * @param weight
	 *            重量
	 * @return
	 */
	public int closeDropshipOrder(int userId, String mainOrderNo, String childOrderNo, float totalPrice, float orderAc, float extraFreight,
                                  float weight) throws Exception;

	/**
	 * 根据dropship主订单号取消所有子订单
	 *
	 * @param mainOrderNo
	 * @return
	 * @throws Exception
	 */
	public int closeDropshipOrderByMainOrderNo(String mainOrderNo) throws Exception;

	/**
	 * 根据订单号判断dropship订单是否都取消,都取消后更新主订单状态到取消
	 *
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public int updateMainOrderByDropship(int userId, String mainOrderNo, String orderNo) throws Exception;

	/**
	 *
	 * @Title queryGoodsPriceFromDetails
	 * @Description 根据订单查询详情中商品的总和
	 * @param orderNo
	 * @return float
	 */
	public float queryGoodsPriceFromDetails(String orderNo);
	/**
	* @Title: getFreightInfo
	* @Description: TODO(根据中文国家名字返回该国家运费信息)
	* @param @param countryNameCn
	* @param @return    设定文件
	* @return List<TabTransitFreightinfoUniteOur>    返回类型
	* @throws
	 */
    public TabTransitFreightinfoUniteOur getFreightInfo(String countryNameCn, int isEub);
    /**
    * @Title: getFreightByWeight
    * @Description: TODO(根据重量获取运费)
    * @param @param fo
    * @param @param weight
    * @param @return    设定文件
    * @return double    返回类型
    * @throws
     */
    public double  getFreightByWeight(TabTransitFreightinfoUniteOur fo, double weight);


	/**
	 * 根据订单哈查询线上存在通知消息
	 * @param orderNo
	 * @return
	 */
	int isExistsMessageByOrderNo(String orderNo);

        
}
