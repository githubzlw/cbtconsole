package com.cbt.website.dao;

import com.cbt.bean.AutoOrderBean;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderPaymentBean;
import com.cbt.pojo.AddBalanceInfo;
import com.cbt.pojo.RechangeRecord;
import com.cbt.refund.bean.RefundBean;
import com.cbt.website.bean.*;

import java.util.List;
import java.util.Map;

public interface PaymentDaoImp {

    public List<OrderPaymentBean> getOrderPayList(int userid, int page);

    public List<PaymentBean> query(PayCheckBean bean);

    /**
     * 根据参数查询符合条件的总数
     *
     * @param bean
     * @return
     */
    public int queryTotalNum(PayCheckBean bean);

    // 查询所有订单 核对款项
    public List<PaymentBean> queryAll(PayCheckBean bean);

    // 查询所有订单 核对款项
    public List<PayInfoBean> getPaymentForCheck(int page);

    /**
     * 获取用户付款总金额(Paypal & Wire transfer)
     *
     * @param userid
     * @return
     */
    public double getPayUserid(int userid);

    public Map<String, Double> getPaysUserid(int userid);

    /**
     * 获取用户付款总金额(Paypal & Wire transfer & 余额)
     *
     * @param userid
     * @return
     */
    public double getPayAllUserid(int userid);

    /**
     * 获取用户付款总金额(Paypal & Wire transfer)
     *
     * @param userid
     * @return
     */
    public Map<String, String> getPayByUserids(List<Integer> list);

    /**
     * 获取用户paypal申诉(Paypal & Wire transfer)
     *
     * @param userid
     * @return
     */
    public Map<String, String> getPaypalByUserids(List<Integer> list);

    /**
     * 获取用户paypal申诉(Paypal & Wire transfer)
     *
     * @param userid
     * @return
     */
    public double getPaypalByUserid(int userid);

    /**
     * 根据用户ID/订单号/交易编码查找支付记录对象
     *
     * @param userid
     * @param orderid
     * @param paymentid
     * @return
     */
    public PaymentBean getPayment(String userid, String orderid, String paymentid);

    /**
     * 后台生成订单
     *
     * @param payment
     * @return
     * @date 2016年10月27日
     * @author abc
     */
    public int addPayment(PaymentBean payment);

    public int AddPayment(String userid);

    /**
     * 获取指定userid的订单数
     *
     * @param userid
     * @return
     */
    public int getCountOrders(int userid);

    public double getAllTotalMoney(PayCheckBean bean);

    /**
     * 添加订单生成记录
     *
     * @param userid     用户id
     * @param orderid    订单号
     * @param orderAdmin 操作人员
     * @param invoice    发票
     * @return
     * @date 2016年10月28日
     * @author abc
     */
    public int addOrderNote(String userid, String orderid, String orderAdmin, String invoice);

    /**
     * 添加订单生成记录
     *
     * @param userid       用户id
     * @param orderid      订单号
     * @param paymentAdmin 进账记录操作人
     * @return
     * @date 2016年10月28日
     * @author abc
     */
    public int addPaymentNote(String userid, String orderid, String paymentAdmin);

    /**
     * 更新订单记录
     *
     * @param userid     用户id
     * @param orderid    订单号
     * @param orderAdmin 订单操作人
     * @param invoice    发票
     * @return
     * @date 2016年10月28日
     * @author abc
     */
    public int updateOrderNote(String userid, String orderid, String orderAdmin, String invoice);

    /**
     * 更新进账记录
     *
     * @param userid       用户id
     * @param orderid      订单号
     * @param paymentAdmin 进账操作人
     * @param isPayment    是否支付 0-未支付 1-支付
     * @return
     * @date 2016年10月28日
     * @author abc
     */
    public int updatePaymentNote(String userid, String orderid, String paymentAdmin, int isPayment);

    /**
     * @param userid  用户id
     * @param orderid 订单号
     * @return
     * @date 2016年10月28日
     * @author abc
     */
    public int countPaymentInvoiceByorderuser(String userid, String orderid);

    /**
     * 获取订单invoice
     *
     * @param orderid 订单号
     * @return
     * @date 2016年11月8日
     * @author abc
     */
    public String getFileByOrderid(String orderid);

    /**
     * 自生成订单列表
     *
     * @param orderid 订单号
     * @param userid  用户id
     * @param page    页
     * @return
     * @date 2016年11月18日
     * @author abc
     */
    public List<AutoOrderBean> getOrderList(String orderid, String userid, int page);

    /**
     * 取消销售自生成进账记录
     *
     * @param pid payment的id
     * @return
     * @date 2016年11月21日
     * @author abc
     */
    public int cancelPayment(String pid);

    /**
     * 根据参数查询支付统计数据
     *
     * @param bean
     * @return
     */
    public List<PaymentStatistics> queryPaymentStatistics(PayCheckBean bean);

    /**
     * 根据参数查询支付统计数据数量
     *
     * @param bean
     * @return
     */
    public int queryPaymentStatisticsCount(PayCheckBean bean);

    /**
     * 根据订单号查询支付详情数据
     *
     * @param orderNo
     * @return
     */
    public List<PaymentDetails> queryPaymentDetails(String orderNo);

    /**
     * @param orderNo
     * @return PaymentDetails
     * @Title queryBalancePayment
     * @Description 根据订单号查询余额支付信息
     */
    public PaymentDetails queryBalancePayment(String orderNo);


    public List<Map<String, Object>> payDetailByUserid(int userid, int page);

    /**
     * 根据客户ID查询订单详情
     *
     * @param userId
     * @return
     */
    List<OrderBean> queryOrderInfoByUserId(int userId);

    /**
     * 根据客户ID查询余额变更记录信息
     *
     * @param userId
     * @return
     */
    List<RechangeRecord> queryRechangeRecordByUserId(int userId);

    /**
     * 根据客户ID查询余额补偿信息
     *
     * @param userId
     * @return
     */
    List<AddBalanceInfo> quertAddBalanceInfoByUserId(int userId);

    /**
     * 根据客户ID查询退款信息
     *
     * @param userId
     * @return
     */
    List<RefundBean> quertRefundByUserId(int userId);


    /**
     * 查询所有支付订单信息
     * @param userId
     * @param type 0全部 1最近一个月 2最近两个月 3最近三个月
     * @return
     */
    List<PaymentBean> queryPayMentInfoByUserId(int userId, int type);

    /**
     * 根据退款ID和客户ID查询退款信息
     *
     * @param rfId
     * @param userId
     * @return
     */
    RefundBean queryRefundBeanById(int rfId, int userId);


    /**
     * 查询后台取消的订单退余额信息
     * @param orderNo
     * @return
     */
    RechangeRecord querySystemCancelOrder(String orderNo);


}
