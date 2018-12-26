package com.importExpress.service;

import com.alibaba.trade.param.AlibabaTradeFastCreateOrderResult;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.pojo.ChangeGoodsLogPojo;
import com.cbt.warehouse.pojo.OfflinePurchaseRecordsPojo;
import com.cbt.warehouse.pojo.OrderInfoCountPojo;
import com.cbt.warehouse.pojo.Replenishment_RecordPojo;
import com.cbt.website.bean.PrePurchasePojo;
import com.cbt.website.bean.PurchaseGoodsBean;
import com.cbt.website.bean.PurchasesBean;
import com.cbt.website.dao2.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IPurchaseService {
	/**
	 * 订单采购详情页面数据查询
	 * @param pagenum
	 * @param orderid
	 * @param admid
	 * @param userid
	 * @param orderno
	 * @param goodid
	 * @param date
	 * @param days
	 * @param state
	 * @param unpaid
	 * @param pagesize
	 * @param orderid_no_array
	 * @param goodsid
	 * @param goodname
	 * @param orderarrs
	 * @param search_state
	 * @return
	 */
	public Page findPageByCondition(String pagenum, String orderid, String admid, String userid, String orderno, String goodid, String date,
                                    String days, String state, int unpaid, int pagesize, String orderid_no_array, String goodsid, String goodname, String orderarrs, String search_state);
	//当月分配采购种类
	public String getfpCount(Map<String, Object> map);
	List<PurchasesBean> getFpOrderDetails(String orderid,String admuserid);

	/**
	 * 超过1天未发货
	 * @param map
	 * @return
	 */
	public String getNotShipped(Map<String, Object> map);
	/**
	 * 获取某个采购的订单分配商品数量
	 * @param orderid
	 * @param admuserid
	 * @return
	 */
	int getFpCount(String orderid, String admuserid);
	/**
	 *发货3天未入库
	 * @param map
	 * @return
	 */
	public String getShippedNoStorage(Map<String, Object> map);
	//获取当日采购分配种类
	public String getDistributionCount(Map<String, Object> map);
	//获得采购数量
	public String getCgCount(Map<String, Object> map);
	//获得实际采购数量
	public String getSjCgCount(Map<String, Object> map);
	//获得每月采购数量
	public String getMCgCount(Map<String, Object> map);
	//获取疑似货源贴错
	OrderInfoCountPojo getOrderInfoCountNoitemid(Map<String, Object> map);
	//点了采购确认
	List<PurchasesBean> getOrderInfoCountItemid(Map<String, Object> map);
	List<String> getNoShipInfoOrder(Map<String, String> map);
	/**
	 * 获取入库没有匹配商品的订单
	 * @Title getNoMatchOrderByTbShipno
	 * @Description TODO
	 * @param map
	 * @return
	 * @return OrderInfoCountPojo
	 */
	OrderInfoCountPojo getNoMatchOrderByTbShipno(Map<String, Object> map);

	/**
	 * 通过id查询采购账号名称
	 * @return
	 */
	public String queryBuyCount(int admuserid);

	/**
	 * 采购详情查询山沟替换日志
	 * @param map
	 * @return
	 */
	public List<ChangeGoodsLogPojo> getDetailsChangeInfo(Map<String,String> map);

	/**
	 * 获取订单备注
	 * @param orderNo
	 * @param goodsdataid
	 * @param goodid
	 * @return
	 */
	public OrderProductSource ShowRmark(String orderNo, int goodsdataid, int goodid, String odid);

	/**
	 * 全部取消货源
	 * @param orderid
	 * @param adminid
	 * @return
	 */
	public String allQxQrNew(String orderid, int adminid);
	//补货
	int insertOrderReplenishment(Map<String, Object> map);
	//添加补货记录
	int addReplenishmentRecord(Map<String, Object> map);
	//查询补货记录
	List<Replenishment_RecordPojo> getIsReplenishments(Map<String, Object> map);
	//查询线下采购记录
	List<OfflinePurchaseRecordsPojo> getIsOfflinepurchase(Map<String, Object> map);
	//补货按钮状态改变
	int updateReplenishmentState(Map<String, Object> map);
	/**
	 *采购是否使用库存
	 * @param map
	 * @return
	 */
	public int useInventory(Map<String, String> map);

	/**
	 * 一键取消采购
	 * @param orderid
	 * @param adminid
	 * @return
	 */
	public String allQxcgQrNew(String orderid, int adminid);

	/**
	 * 原链接订单信息录入
	 * @param map
	 * @return
	 */
	public int insertSources(Map<String, String> map);
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
	 * 采购前置页面数据条数
	 * @param map
	 * @return
	 */
	List<String> getPrePurchaseCount(Map<String, Object> map);
	/**
	 * 采购前置页面数据曾现
	 * @param map
	 * @return
	 */
	List<PrePurchasePojo> getPrePurchase(Map<String, Object> map);
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
	 *一键确认采购
	 * @param orderid
	 * @param adminid
	 * @return
	 */
	public String allcgqrQrNew(String orderid, int adminid);
	/**
	 public String allcgqrQrNew();

	 /**
	 * 全部确认货源
	 * @param orderid
	 * @param adminid
	 * @return
	 */
	public List<Map<String,String>> allQrNew(String orderid, int adminid);

	public String getUserName(int adminid);

	/**
	 * 查询订单信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getOrders(String orderNo);

	/**
	 * 根据订单号获取国际运费
	 * @param orderNo
	 * @return
	 */
	public Double getAllFreightByOrderid(String orderNo);

	/**
	 * 货源确认
	 * @param userid
	 * @param orderNo
	 * @param od_id
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
	 * @param child_order_no
	 * @param isDropshipOrder
	 * @return
	 */
	public int PurchaseComfirmTwoHyqr(int userid, String orderNo, int od_id, int goodid, int goodsdataid, int admid,
                                      String goodsurl, String googsimg, String goodsprice, String goodstitle, int googsnumber, String oldValue,
                                      String newValue, int purchaseCount, String child_order_no, String isDropshipOrder);

	/**
	 * 获取采购人名称
	 * @param buyerId
	 * @return
	 */
	public String getUserbyID(String buyerId);

	/**
	 * 判断订单是否被取消
	 * @param orderNo
	 * @return
	 */
	public int checkOrder(String orderNo, String odid);

	/**
	 * 获取其他货源
	 * @param orderNo
	 * @param odid
	 * @param goods_url
	 * @return
	 */
	public String getOtherSources(String orderNo, String odid, String goods_url);

	/**
	 * 录入货源信息
	 * @param map
	 */
	public void AddRecource(Map<String, String> map);


	/**
	 * 根据订单号查询客户ID和新旧订单状态
	 * @param orderNo
	 * @return
	 */
	Map<String,Object> queryUserIdAndStateByOrderNo(String orderNo);

	public List<Map<String, Object>> getComfirmedSourceGoods();

	public AlibabaTradeFastCreateOrderResult generateOrdersByShopId(String app_key, String sec_key, String access_taken, List<PurchaseGoodsBean> beanList);

	public List<String> sendPost(String url, String param);

	int updateAutoOrderFlag(List<Integer> idsList);

	List<Map<String, Object>> getSizeChart(String  catid);

	List<Map<String, Object>> loadCategoryName(String catid);
	public int updateSizeChart(String imgname,String localpath,int rowid);
	public int updateSizeChartById(List<Integer> rowidArray,int userid);

}
