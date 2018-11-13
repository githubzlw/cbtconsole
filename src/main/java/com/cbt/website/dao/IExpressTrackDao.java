package com.cbt.website.dao;

import com.cbt.bean.BuyAliGoodsBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.website.bean.ExpressTrackInfo;
import com.cbt.website.bean.SearchResultInfo;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wanyang
 * 订单号及对应的单号入库过程的数据库操作
 */
public interface IExpressTrackDao {
	/**
	 * 查询库位号位置
	 * @param id 库位号条码
	 * @return
	 */
	public String getPosition(String id);	
	
	public List<Map<String, String>> getOrderManagementQuery(int userID, int state, String startdate, String enddate, String email,
                                                             String orderno, int startpage, int page, int admuserid, int buyid, int showUnpaid, String type, int status, String paymentid);

	/**
	 * 查询订单支付为pending订单
	 * @return
	 */
	public List<Map<String, String>> getorderPending();
	/**
	 * 查询库位号位置
	 * @param id 淘宝商品id
	 * @return
	 */
	public String getPositionByGoodId(String goodId);



	/**
	 * 查询库位号位置
	 * @param id 淘宝订单id
	 * @return  淘宝订单id已经存库的所有位置
	 */
	public String getPositionByOrderId(String orderId);
	/**
	 * 向单号和订单号对应关系表插入一条记录
	 * wanyang
	 * orderid:订单号	，relationorderid:关联单号	 type:关联单号类型
	 */
	public int insert(String orderid, String relationorderid, int type);
	/**
	 * 根据快递跟踪号查询到对应的订单商品信息
	 * expresstrackid:运单号
	 * wanyang
	 *
	 */
	public List<ExpressTrackInfo> search(String express_trackid);
	/**
	 * 更新货物的到货状态
	 * expresstrackid:运单号，orderid:订单号，goodsid：商品id，status：到货状态
	 * wanyang
	 */
	public int updateArrivalStatus(String expresstrackid, String orderid, String goodsid, int status);
	/**
	 * 更新仓库的到货状态
	 * orderid:订单号,ordercount：订单有效商品数,inwarehousecount：订单入库商品数,warehouseid:库位号
	 * wanyang
	 */
	public int updateWarehouseId(String orderid, int ordercount, int inwarehousecount, String warehouseid);
	/**
	 * 货物状态为数量不够时更新货物的到货数量
	 * expresstrackid:运单号，orderid:订单号，goodsid：商品id，goodsaddrivecount：到货数量
	 * wanyang
	 */
	public int updateArriveCount(String expresstrackid, String orderid, String goodsid, int goodsaddrivecount);

	/********************新修改逻辑***********************/
	/**
	 * 根据运单号查出当前快递单下面的订单(根据未出货的淘宝链接显示)
	 * wanyang
	 * 2015-11-24
	 */
	public List<SearchResultInfo> getOrder(String expresstrackid, String checked);
	public List<SearchResultInfo> getCheckInfo(String expresstrackid);
	/**
	 * 一键确认或取消入库
	 * @param shipno
	 * @param type
	 * @return
	 */
	public int allTrack(String shipno, String type, String barcode, String userid, String userName, String tbOrderId, int admid);
	/**
	 * 根据公司订单反查淘宝订单
	 * orderid 公司订单号
	 */
	public List<TaoBaoOrderInfo> getTaoBaoInfo(String orderid);
	/**
	 * 查询早期库存放商品最少的库位
	 * short_term 是否为早期库
	 */
	public String getBarcode(int short_term, int admid, String orderid);

	public String getInventoryRecords(String orderid);
	/**
	 * 根据订单号获取该订单是那种类型的订单
	 * @Title getOrderType
	 * @Description TODO
	 * @param orderid
	 * @return
	 * @return String
	 */
	public String getOrderType(String orderid);
	/**
	 * 根据运单号查出当前快递单下面的订单(补货)
	 * zsl
	 * 2016-06-06
	 */
	public List<SearchResultInfo> getReplenishResultInfo(String expresstrackid);
	/**
	 * 确认并移库完成操作
	 * @param map
	 * whj 2017-05-03
	 */
	public int confirmMoveLibrary(Map<String, String> map);
	/**
	 * 使用库存时发现库存有误修改当前锁定库存操作
	 * @param map
	 * @return  影响的数据库列行数
	 */
	public int updateLockInventory(Map<String, String> map);
	/**
	 * 仓库移库是库存有误记录
	 * @param map
	 * @return
	 * whj 2017-08-22
	 */
	public int insertInventoryWrong(Map<String, String> map);
	/**
	 * 确认使用部分库存并移库完成操作
	 * @param map
	 * whj 2017-05-03
	 */
	public int confirmMoveLittleLibrary(Map<String, String> map);
	/**
	 * 根据运单号查出当前快递单下面的订单(亚马逊)
	 * zsl
	 * 2016-07-27
	 */
	public List<SearchResultInfo> getAmazonResultInfo(String expresstrackid);
	/**
	 * 亚马逊商品入库
	 * zsl
	 * 2016-07-27
	 */
	public int updateAmazonstatus(String orderid, long goodid, int status, String goodurl, String barcode, String userid, String userName,
                                  String tbOrderId, String shipno, String itemid, String warehouseRemark, String storeName);
	/**
	 * 亚马逊商品取消入库
	 * zsl
	 * 2016-07-27
	 */
	public int cancelAmazonstatus(String orderid, String itemid, long goodid, String warehouseRemark);

	/********************ajax获取商品信息***********************/
	/**
	 * 根据运单号查出当前快递单下面的订单(根据未出货的淘宝链接显示)
	 */
	public List<Tb1688OrderHistory> getGoodsData(String expresstrackid);

	/**
	 * 记录快递包裹扫描记录
	 * @param shipno
	 * @param admName
	 */
	public void insertScanLog(String shipno, String admName);

	/********************ajax获取商品信息***********************/
	/**
	 * 查询10天以内没到库的原链接购买商品
	 */
	public List<BuyAliGoodsBean> getAliGoodsData(String admuserid);
	/**
	 * 查询20条快递单号匹配不了货源的淘宝信息商品
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰
	 */
	public List getNoMatchGoods(String expresstrackid, String username);
	/**
	 * 根据订单号和商品编号关联入库
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰
	 * 2016-10-10
	 */
	public int updateMatch(String orderid, String goodsid, String sku, String itemid, String tborderid, int state);

	public List<OrderDetailsBean> getNoPictureGoods(String orderid, String goodsid, int page);
	/**
	 * 查询一周内有问题订单号
	 */
	public List<Map<String, String>> getIdRelationtable(int startNum, int endNum);
	/**
	 * 强制入库
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰
	 * 2016-10-09
	 */
	public int mandatoryPut(String tbOrderId, String shipno, String itemid, String barcode, String itemprice, String sku, String userid, String username, String itemqty, String remark);
	/**
	 * 修改商品的到货状态
	 * wanyang
	 * 2015-11-25
	 */
	public int updateGoodStatus(String orderid, long goodid, int status,
                                String goodurl, String barcode, String userid, String userName, String tbOrderId, String shipno,
                                String itemid, String warehouseRemark, String repState, int count);

	/**
	 * 查询货源信息
	 * @param orderid
	 * @param goodsid
	 * @return
	 */
	public OrderProductSource queryOps(String orderid, String goodsid);

	/**
	 * 通过id查询采购账号名称
	 * @param admuserid  用户id
	 * @return  采购账号名称
	 * 2017-04-14 whj
	 */
	public String queryBuyCount(int admuserid);

	public void updateOfflinePurchase(String orderid, String goodsid);

	/**
	 * 根据订单号和商品编号获取快递单号
	 * @param orderid
	 * @param goodsid
	 * 2017-04-13  whj
	 * @return
	 */
    public String qyeryShipno(String orderid, String goodsid);
//	public int updateAliGoodStatus(String orderid, long goodid,
//			String goodurl,String barcode ,String userid, String userName,String shipno,String warehouseRemark,int count);

	/**
	 * 入库取消
	 */
	public int updatecanceltatus(String orderid, long goodid, String repState, String warehouseRemark, String count, int adminId);

	/**
	 * 将上传图片路径存入order_details中
	 * @param path
	 * @param orderid
	 * @param goodsid
	 * @return
	 */
	public int saveImgPath(String path, String orderid, String odid, String ftpPath, boolean success);

	/**
	 * 将上传图片路径存入orderinfo中
	 * @param ftpPath
	 * @param orderid
	 * @param localImgPath
	 * @return
	 */
	public int saveImgPathForInfo(String ftpPath, String orderid, String localImgPath);

	/**
	 * 删除上传的图片
	 * @param imgPath
	 * @param orderid
	 * @return
	 */
	public int delUploadImage(String imgPath, String orderid);

	/**
	 * 获取该订单主单图片
	 * @param orderid
	 * @return  图片路径
	 */
	public String getImgFiles(String orderid);

	/**
	 * 验货图片双击删除
	 * @param map
	 * @return
	 */
	public int delPics(Map<String, String> map);
	/**
	 * 验货取消
	 */
	public int updatecancelChecktatus(String orderid, long goodid, String repState, String warehouseRemark, String count,
                                      int adminId, String barcode, String cance_inventory_count, String seiUnit, String weight);

	public int updateTbstatus(String orderid, long goodid, int status,
                              String goodurl, String barcode, String userid, String userName, String tbOrderId, String shipno, String itemid, String warehouseRemark, String repState, int count, String weight);

	/**
	 * 判断订单是否全部到库并且验货无误
	 * @param orderid
	 * @return
	 */
	public int checkOrderState(String orderid);
	/**
	 * 重拍图片修改状态
	 * wanyang
	 * 2016-01-12
	 */
	public int pictureagain(String orderid, long goodid);

	/**
	 * 商品多余库存存入库存表中
	 * @param barcode 库存库位
	 * @param inventory_count  库存数量
	 * @param orderid  订单号
	 * @param goodsid  商品号
	 * @return
	 */
	public int addInventory(String barcode, String inventory_count, String orderid, String odid, String storage_count,
                            String when_count, String admName, String unit);
	
	/**
	 * 更新图片地址
	 * @param orderid
	 * @param goodid
	 * @param picturePath
	 * @return
	 */
	public int updatePicturePath(String orderid, String goodid, String picturePath);
	
	/**
	 * 得到图片地址列表
	 * @param uid
	 * @return
	 */
	public List<Map<String, String>> getPicturePathList(String uid);
	

	

		
}
