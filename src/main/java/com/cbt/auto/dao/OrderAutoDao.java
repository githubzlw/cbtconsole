package com.cbt.auto.dao;

import com.cbt.auto.ctrl.AutoToSalePojo;
import com.cbt.auto.ctrl.OrderAutoBean;
import com.cbt.auto.ctrl.PureAutoPlanBean;
import com.cbt.bean.AdmDsitribution;
import com.cbt.bean.OrderAutoDetail;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.pojo.GoodsDistribution;

import java.util.List;
import java.util.Map;

public interface OrderAutoDao {
	
    public List<OrderAutoBean> getAllOrderAutoInfo(String order);
     
     public int getGoodsDistributionCount(String carUrl);
    public int insertPaymentConfirm(String orderNo, String confirmname, String confirmtime, String paytype, String tradingencoding, String wtprice, int userId);
     public String getTwoCid(String goodsCatId);
     /**
      * 获取需要刷新退货日期的采购订单
      * @Title getAllOrderRefresh 
      * @Description TODO
      * @return
      * @return Map<String,String>
      */
     public Map<String,String> getAllOrderRefresh();
     /**
      * 获取为分配采购的订单数据
      * @Title getUnassignedOrders 
      * @Description TODO
      * @return
      * @return int
      */
     List<String> getUnassignedOrders();
     public Map<Integer, Integer> getAllBuyerCount();
    public Map<Integer,Integer> getLessSaler();
     /**
 	 * 根据订单详情的goodscatid获取商品类别
 	 * @param goodsCatId
 	 * @return 商品类别
 	 * @author 王宏杰
 	 * @ 2016-09-23
 	 */
     public String getAliCategoryType(String goodsCatId);
     
     public int insertDG(GoodsDistribution gd);
     
     /**
 	 * 库存锁定
 	 * @param orderNo  订单号
 	 * whj 2017-05-08
 	 */
 	public void inventoryLock(String orderNo);
    /**
     * 查询是否有客户同意替换的商品需要分配采购
     */
    public void queryChangeLogToAotu();
    /**
     * 获取订单paypal地址
     * @param orderNo
     * @return
     */
    public String getPayPalAddress(String orderNo);
    public int updateIsStockFlag();
     public List<AdmDsitribution> getAdmuserType(String time);
    /**
     * 根据用户订单号获取发送邮件需要的用户邮箱等信息
     * @param orderNo
     * @return
     */
     public OrderBean getUserOrderInfoByOrderNo(String orderNo);
     public int insertBGP(OrderAutoBean o, String time);

     public int insertPap(PureAutoPlanBean pap);

     public List<AdmDsitribution> getALLAdmDsitribution();

     public List<String> getAllAdmUser();

    public  List<String> getPureSalesAdmUser();

    public  List<String> getPurePurchaseAdmUser();

     public List<OrderAutoDetail> getOrderDetail(String goodid, String order);
    /**
     * 获取该订单中商品以前的分配情况
     * @param orderNo
     * @return
     */
    public Map<Integer,Integer> getMoreGoodsPid(String orderNo);
    /**
     * 获取最近7天采销员采购商品情况
     * @return
     */
    public Map<Integer,Integer> getSevenLeastAdmuser();
    /**
     * 分配销售
     * @param orderNo
     * @param adminid
     * @return
     */
    public int insertAdminUser(String orderNo, int adminid);
    /**
     * 判断该订单用户是否被分配过采销员
     * @param orderNo
     * @return
     */
    public int getAdminId(String orderNo);
     public int getMaxId();
     
     public List getAllOrderAuto(int maxId);
     
     /**
      * 查询没有录入货源时间的记录
      * @return 匹配的货源记录
      * 2017097-24 王宏杰
      */
     public List<OrderProductSource> getPreAutoSourceAddTime();
     
     public List getAllocatedOrder();
     
     /**
      * 根据还未分配的订单号查询商品个数
      * @param orderids
      * @return 商品数量
      * @author 王宏杰
      * @ 2016-09-26
      */
     public int getAllCount(String orderids);
     
     public Map getAdmAutoCount(String time2);
     /**
      * 自动分配销售
      * @author  王宏杰
      * 2016-10-10
      */
     public List<AutoToSalePojo> getOrderAutoToSale();
     
     public Map getBeforeNoComplete();
     
     public Map getNoCompleteCount();
     
     public int getExitGoodsPid(String goods_pid);
     
     public int getNewColudBuyer(String pid);
     
     public Map<Integer,List<String>> getAllProcurementExpertise();
     
     public Map<String,List<OrderAutoBean>> getItems(String orderid);
     
     public AdmDsitribution getAdmuserNoComplete(String admuserid);
     /**
      * 获取顶级类别ID
      * @Title getFirdstCid 
      * @Description TODO
      * @param id
      * @return
      * @return String
      */
     public String getFirdstCid(String id);
}
