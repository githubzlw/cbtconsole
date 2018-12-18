package com.cbt.website.dao2;

import com.cbt.bean.CodeMaster;
import com.cbt.bean.OrderDatailsNew;
import com.cbt.bean.OrderProductSource;
import com.cbt.pojo.StraightHairPojo;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.warehouse.pojo.ChangeGoodsLogPojo;
import com.cbt.warehouse.pojo.PreferentialPrice;
import com.cbt.website.bean.*;

import java.util.List;
import java.util.Map;

public interface PurchaseDao {

	public int splitPage();

	/**
	 * 广东直发国内包裹到了后更改商品状态
	 * @param orderid
	 * @param goodsid
	 * @return
	 */
	public int updateState(String orderid, String goodsid);

	/**
	 * 获取广东直发列表数据
	 * @return
	 */
	public List<StraightHairPojo> StraightHairList();
	public List<AdminUserBean> getAllAdmUser();

	public List<OrderProductSource> getAllGoodsids(int adminid);

	public int purchaseConfirmation(String string, int admid, String orderNo, int goodid);
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
	 * 根据条件查询、
	 *
	 * @param startindex
	 * @param pagesize
	 * @param admin
	 * @param user
	 * @param orderno
	 * @param good
	 * @param date
	 * @param day
	 * @param stat
	 * @return
	 */
	public List<PurchasesBean> getPurchaseByXXX(int startindex, int pagesize, int admin, int user, String orderno,
                                                int good, String date, int day, int stat, int unpaid, String orderid_no_array, String goodsid,
                                                String goodname, String orderarrs, String search_state);

	/**
	 * 标记该订单销售发送消息已读
	 * @param orderid
	 * @param admid
	 * @return
	 */
	public int updateGoodsCommunicationInfo(String orderid, int admid);

	/**
	 * 录入货源
	 *
	 * @param admid
	 * @param userid
	 * @param goodsdataid
	 * @param goods_url
	 * @param googs_img
	 * @param goodsprice
	 * @param goods_title
	 * @param googsnumber
	 * @param orderNo
	 * @param goodid
	 * @param price
	 * @param resource
	 * @param buycount
	 * @param issuree
	 */
	public void AddRecource(String type, int admid, int userid, int goodsdataid, String goods_url, String googs_img,
                            double goodsprice, String goods_title, int googsnumber, String orderNo, int od_id, int goodid, double price,
                            String resource, int buycount, String reason, String currency, String pname, String cGoodstypee,
                            String issuree, String shop_id, String state_flag, String straight_address);

	public void AddRecource127(String type, int admid, int userid, int goodsdataid, String goods_url, String googs_img,
                               double goodsprice, String goods_title, int googsnumber, String orderNo, int od_id, int goodid, double price,
                               String resource, int buycount, String reason, String currency, String pname);

	// 添加无货源记录
	public void AddNoGS(String goods_url, String goods_type, String userid);

	/**
	 * 录入备注
	 *
	 * @param orderNo
	 * @param goodid
	 * @param remark
	 */
	public void AddRmark(String orderNo, int gooddataid, String remark, int goodid);

	public OrderProductSource ShowRmark(String orderNo, int gooddataid, int goodid);

	/**
	 * 其他货源
	 *
	 * @param orderNo
	 * @param goodid
	 * @return
	 */
	public String getOtherSources(String orderNo, int goodid, String goods_url);

	/**
	 * 获取用户订单地址
	 *
	 * @param userid
	 * @return
	 */
	public String getAddressByOrderID(String orderNo);

	/**
	 * 出货
	 *
	 * @param orderno
	 */
	public void OutPortNow(String orderno, String wuliuNumber, String transport, String userid);

	public void OutPortNow127(String orderno, String wuliuNumber, String transport, String userid);

	/**
	 * 客户ID，出货订单
	 *
	 * @return
	 */
	public List<outIdBean> findOutId(int uid, String str);

	public List<outIdBean> findOutIdTwo(int uid);

	/**
	 * 获取用户Email
	 *
	 * @param userid
	 * @return
	 */
	public String getEmail(int userid);

	/**
	 * 取消采购
	 *
	 * @param orderNo
	 * @param goodsid
	 * @param adminid
	 * @return
	 */
	public int PurchaseComfirmOne(String orderNo, int goodsid, int adminid);

	public int PurchaseComfirmOne127(String orderNo, int goodsid, int adminid);

	/**
	 * 获取采购商品的批量优惠价格
	 * @param orderid
	 * @param goodsid
	 * @return
	 * @author whj
	 */
	public List<PreferentialPrice> queryPreferentialPrice(String orderid, int goodsid, String goods_p_url);

	/**
	 * 添加采购商品的批量优惠价格
	 * @param map
	 * @return
	 * @author whj
	 */
	public int addPreferentialPrice(Map<Object, Object> map);

	/**
	 * 修改采购商品的批量优惠价格
	 * @param map
	 * @return 影响的行数
	 */
	public int updatePreferentialPrice(Map<Object, Object> map);

	// 取消货源
	public int PurchaseComfirmOneQxhy(String orderNo, int odid, int adminid);

	/**
	 * 确认货源
	 *
	 * @param userid
	 * @param orderNo
	 * @param goodid
	 * @param goodsdataid
	 * @param admid
	 * @param goodsurl
	 * @param googsimg
	 * @param goodsprice
	 * @param goodstitle
	 * @param googsnumber
	 * @param oldValue
	 * @param newValue
	 * @param purchaseCount
	 */
	public int PurchaseComfirmTwo(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
                                  String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
                                  String newValue, int purchaseCount);

	public int PurchaseComfirmTwo_crossshop(int userid, String orderNo, int od_id, int goodid, int goodsdataid,
                                            int admid, String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber,
                                            String oldValue, String newValue, int purchaseCount);

	public int PurchaseComfirmTwo127(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
                                     String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
                                     String newValue, int purchaseCount);

	public int insertSources(Map<String, String> map);
	/**
	 * 申请线下采购付款
	 * @param map
	 * @return
	 */
	public int OfflinePaymentApplication(Map<String, String> map);
	/**
	 * 确认广东直发
	 * @Title determineStraighthair
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	public int determineStraighthair(Map<Object, Object> map);

	/**
	 * 采购使用使用库存
	 * @param map
	 * @return
	 */
	public int useInventory(Map<String, String> map);

	public String getUserName(String name);
	/**
	 * 本链接采样信息录入
	 * @Title insertDateToAliInfoData
	 * @Description TODO
	 * @param od_id
	 * @return
	 * @return int
	 */
	public int insertDateToAliInfoData(String od_id, String name);
	/**
	 * 保存商品沟通备注信息
	 * @param map  订单号、商品号、备注信息
	 * @author 王宏杰  2017-07-26
	 */
	public String saveRepalyContent(Map<String, String> map);

	// 货源确认
	public int PurchaseComfirmTwoHyqr(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
                                      String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
                                      String newValue, int purchaseCount, String child_order_no, String isDropshipOrder);
	/**
	 * 一键确认货源
	 * @param orderNo  订单号
	 * @return  影响的行数
	 */
	public String allQrNew(String orderNo, int id);
	/**
	 * 一键取消货源
	 * @param orderNo  订单号
	 * @return  影响的行数
	 */
	public String allQxQrNew(String orderNo, int id);

	/**
	 * 一键取消采购
	 * @param orderNo  订单号
	 * @return  影响的行数
	 */
	public String allQxcgQrNew(String orderNo, int id);
	/**
	 * 一键确认采购
	 * @param orderNo  订单号
	 * @return  影响的行数
	 */
	public String allcgqrQrNew(String orderNo, int id);
	// 货源确认
	public int PurchaseComfirmTwoHyqr127(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
                                         String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
                                         String newValue, int purchaseCount);

	public List<PurchaseBean> getOutByID(int userid, String str);

	public List<outIdBean> getOutNowId();

	public List<outIdBean> getOutNowIdTwo();

	public String getUserbyID(String admid);
	
	public int getBuyId(String orderid, String goodsid);

	public UserOrderDetails getUserDetails(String orderno);

	public int findOrderService(String ordernolist);

	public int YiRuKu(String url, String orderno, int goodid);

	public int YiRuKu127(String url, String orderno, int goodid);

	public void ProblemGoods(String url, String orderno, int goodid);

	public String getYFHTurnOrder(String yfhOrder);

	public List<CodeMaster> getCodeMaster();

	public OrderPayDetails getDetailsByOrder(String adminid, String uid, String order);

	// 查询订单商品
	public List<OrderDatailsNew> getOrderdataelsNew(String order);

	public List<CountryCodeBean> getCountryCode();

	public List<ProductCodeBean> getProductCode();

	public int saveOrderFee(OrderFeeDetails ofd);

	/**
	 * 替换商品的原产品
	 * 
	 * @param orderno
	 * @param goodsid
	 * @return
	 */
	public String getOriginalGoodsUrl(String orderno, int goodsid);

	/**
	 * 根据参数在货源表中添加再次备注信息
	 * 
	 * @param orderNo
	 * @param od_id
	 * @param goodsid
	 * @param remarkContent
	 */
	public void notePurchaseAgain(String orderNo, int od_id, int goodsid, String remarkContent);

	public UserOrderDetails getUserAddr(String orderid)  throws Exception;

}
