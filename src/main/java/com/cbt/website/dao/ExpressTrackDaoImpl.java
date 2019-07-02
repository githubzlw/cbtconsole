package com.cbt.website.dao;

import com.cbt.bean.*;
import com.cbt.change.util.ChangeRecordsDao;
import com.cbt.change.util.OnlineOrderInfoDao;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ExpressTrackInfo;
import com.cbt.website.bean.OrderWarehouseInfo;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.SearchTaobaoInfo;
import com.cbt.website.util.Utility;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ExpressTrackDaoImpl implements IExpressTrackDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ExpressTrackDaoImpl.class);
	private final static org.slf4j.Logger MQLOG = LoggerFactory.getLogger("mq");
	// 添加的备注对内
	IOrderwsDao orderwsdao = new OrderwsDao();

	public int insert(String orderid, String relationorderid, int type) {
		String sql = "";
		if (type != 3) {
			sql = "insert into orderid_r_trackingid(orderid,relation_orderid,type,status,createtime) values(?,?,?,?,now())";
		} else {
			sql = "insert into orderid_r_trackingid(orderid,express_trackid,type,status,createtime) values(?,?,?,?,now())";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, relationorderid);
			stmt.setInt(3, type);
			stmt.setInt(4, 0);
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public List<ExpressTrackInfo> search(String express_trackid) {
		String sql = "select  t1.orderid,t1.express_trackid,t1.relation_orderid,t1.type,t1.status  from orderid_r_trackingid t1 left join order_details t2 on t1.orderid = t2.orderid where express_trackid=? group by t1.orderid;";
		String sql1 = "select t2.id,t2.goods_title,t2.googs_price,t2.googs_number,t2.goods_type,t2.goodsUnit,t2.currency,replace(substring(SUBSTRING_INDEX(t3.img,',',1),2),']','') as googs_img from order_details t1 left join goods_car t2  on t1.goodsid = t2.id "
				+ "left join goodsdata t3 on t3.id=t2.goodsdata_id where t1.orderid=?;";
		String sql2 = "select count(*) as count from order_details where orderid=? and state!=-1;";
		String sql3 = "select count(*) as count from goods_arrivalstatus where orderid=? and arrivalstatus=1";
		String sql4 = "select ifnull(arrivalstatus,0) as arrivalstatus from goods_arrivalstatus where orderid=? and goodsid=?";
		String sql5 = "select * from orderwarehouseinfo where OrderId=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null;
		PreparedStatement stmt = null, stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null;
		List<ExpressTrackInfo> res = new ArrayList<ExpressTrackInfo>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, express_trackid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ExpressTrackInfo temp = new ExpressTrackInfo();
				List<String> goodsImgList = new ArrayList<String>();
				List<String> goodsIdList = new ArrayList<String>();
				List<String> goodsNameList = new ArrayList<String>();
				List<String> goodsTypeList = new ArrayList<String>();
				List<String> goodsPriceList = new ArrayList<String>();
				List<Integer> goodsQuantityList = new ArrayList<Integer>();
				OrderWarehouseInfo info = new OrderWarehouseInfo();

				temp.setExpressTrackId(rs.getString("express_trackid"));
				temp.setExpressTrackOrderid(rs.getString("orderid"));
				temp.setExpressTrackRelationId(rs.getString("relation_orderid"));
				temp.setType(rs.getInt("type"));
				temp.setStatus(rs.getInt("status"));
				/* 获取订单里的商品id和商品图片imgurl */
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1, rs.getString("orderid"));
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					goodsImgList.add(rs1.getString("googs_img"));
					goodsIdList.add(rs1.getString("id"));
					goodsNameList.add(rs1.getString("goods_title"));
					if (rs1.getString("goods_type") == null) {
						goodsTypeList.add("");
					} else {
						if (rs1.getString("goods_type").indexOf(",") > 0) {
							goodsTypeList.add(rs1.getString("goods_type")
									.replace(",", "@"));
						}
					}

					goodsPriceList.add(rs1.getString("googs_price") + " "
							+ rs1.getString("currency") + "/"
							+ rs1.getString("goodsUnit"));
					goodsQuantityList.add(Integer.parseInt(rs1
							.getString("googs_number")));
				}
				int[] goodsArrivalStatus = new int[goodsIdList.size()];
				/* 获取订单里的有效商品总数 */
				stmt2 = conn.prepareStatement(sql2);
				stmt2.setString(1, rs.getString("orderid"));
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					info.setOrderCount(rs2.getInt("count"));
				}
				/* 获取订单里的已到货的商品数 */
				stmt3 = conn.prepareStatement(sql3);
				stmt3.setString(1, rs.getString("orderid"));
				rs3 = stmt3.executeQuery();
				while (rs3.next()) {
					info.setInWarehouseCount(rs3.getInt("count"));
				}
				/* 获取商品的到货状态 */
				stmt4 = conn.prepareStatement(sql4);
				for (int i = 0; i < goodsIdList.size(); i++) {
					stmt4.setString(1, rs.getString("orderid"));
					stmt4.setString(2, goodsIdList.get(i));
					rs4 = stmt4.executeQuery();
					while (rs4.next()) {
						goodsArrivalStatus[i] = rs4.getInt("arrivalstatus");
					}
				}
				/* 获取库位号 */
				stmt5 = conn.prepareStatement(sql5);
				stmt5.setString(1, rs.getString("orderid"));
				rs5 = stmt5.executeQuery();
				while (rs5.next()) {
					info.setWarehouseId(rs5.getString("WarehouseId"));
				}

				temp.setGoodsImgurl(goodsImgList);
				temp.setGoodsId(goodsIdList);
				temp.setGoodsName(goodsNameList);
				temp.setGoodsType(goodsTypeList);
				temp.setGoodsPrice(goodsPriceList);
				temp.setGoodsQuantity(goodsQuantityList);
				temp.setWarehouseInfo(info);
				temp.setArrivalstatus(goodsArrivalStatus);
				res.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public int updateArrivalStatus(String expresstrackid, String orderid,
	                               String goodsid, int status) {
		String sql = "{call update_arrivalstatus(?,?,?,?)}";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		CallableStatement cstmt = null;
		int res = 0;
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, expresstrackid);
			cstmt.setString(2, orderid);
			cstmt.setString(3, goodsid);
			cstmt.setInt(4, status);
			res = cstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public int updateWarehouseId(String orderid, int ordercount,
	                             int inwarehousecount, String warehouseid) {
		// TODO Auto-generated method stub
		String sql = "insert into orderwarehouseinfo(OrderId,OrderCount,InWarehouseCount,WarehouseId,createtime) values(?,?,?,?,now())";
		String sql1 = "update goods_arrivalstatus set warehouseid =? where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0, rs1 = 0;
		PreparedStatement stmt = null, stmt1 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, ordercount);
			stmt.setInt(3, inwarehousecount);
			stmt.setString(4, warehouseid);
			rs = stmt.executeUpdate();

			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, warehouseid);
			stmt1.setString(2, orderid);
			rs1 = stmt1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int updateArriveCount(String expresstrackid, String orderid,
	                             String goodsid, int goodsaddrivecount) {
		String sql = "{call update_arrivalcount(?,?,?,?)}";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		CallableStatement cstmt = null;
		int res = 0;
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, expresstrackid);
			cstmt.setString(2, orderid);
			cstmt.setString(3, goodsid);
			cstmt.setInt(4, goodsaddrivecount);
			res = cstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	/**
	 * 一键确认或取消入库
	 *
	 * @param shipno
	 * @param type
	 * @return
	 */
	public int allTrack(String shipno, String type, String barcode,
	                    String userid, String userName, String tbOrderId, int admid) {
		int row = 0;
		String sql = "";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		int adminid = 520;
		try {
			if ("1".equals(type)) {
				sql = "SELECT ta.adminid FROM tb_1688_accounts ta INNER JOIN taobao_1688_order_history t ON ta.account=t.username WHERE t.shipno='"
						+ shipno + "' LIMIT 1";
				stmt = conn.prepareStatement(sql);
				rs1 = stmt.executeQuery();
				if (rs1.next()) {
					adminid = rs1.getInt("adminid");
				}
				sql = "{call getOrderData(?,?)}";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, shipno);
				stmt.setInt(2, adminid);
				rs1 = stmt.executeQuery();
				while (rs1.next()) {
					String orderid = rs1.getString("orderid");
					long goodid = Long.parseLong(rs1.getString("goodsid"));
					String goodurl = rs1.getString("goods_url");
					row = updateGoodStatus(orderid, goodid, 1, goodurl,
							barcode, userid, userName, tbOrderId, shipno, "",
							"", "1", 0);
				}
			} else if ("0".equals(type)) {
				sql = "select orderid,goodsid from order_details where checked=0 and shipno='"
						+ shipno + "'";
				stmt = conn.prepareStatement(sql);
				rs1 = stmt.executeQuery();
				while (rs1.next()) {
					String orderid = rs1.getString("orderid");
					long goodid = Long.parseLong(rs1.getString("goodsid"));
					row = updatecanceltatus(orderid, goodid, "1", "", "0",
							admid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public List<SearchResultInfo> getCheckInfo(String expresstrackid) {
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sql = "";
		Set set=new HashSet();
		Connection conn = DBHelper.getInstance().getConnection();
		// 本地化链接
		List<SearchResultInfo> info = new ArrayList<SearchResultInfo>();
		List<SearchTaobaoInfo> taobaoinfoList = new ArrayList<SearchTaobaoInfo>();
		try{
			sql="SELECT DISTINCT ops.*,id.position as goodsPOSITION,id.goodstatus as goodstatus,oi.isDropshipOrder,od.checked,od.remark AS odremark,od.goods_pid,od.seilUnit,gc.seilUnit gcUnit," +
					"(SELECT GROUP_CONCAT(DISTINCT orderid) FROM taobao_1688_order_history WHERE shipno='"+expresstrackid+"') taobaoOrderid, SPACE(500) orderPOSITION,id.position as  goodsPOSITION," +
					"0  AS ordercount,0 AS buycnt,(select ifnull(CONCAT(SUBSTRING_INDEX(goods_typeimg.img,'.jpg',1),'.jpg'),'')  from goods_typeimg where goods_id=od.goodsid) as img," +
					"od.car_img as  od_goods_img_url,od.car_type,id.goodstatus, (SELECT orderRemark FROM orderinfo WHERE orderinfo.order_no = od.orderid) as orderRemark,od.goodscatid" +
					" FROM order_details od  INNER JOIN order_product_source ops ON od.orderid=ops.orderid AND od.goodsid=ops.goodsid " +
					" LEFT JOIN goods_car gc ON ops.goodsid=gc.id  LEFT JOIN paymentconfirm pf ON ops.orderid=pf.orderno " +
					" INNER JOIN orderinfo oi ON od.orderid=oi.order_no INNER JOIN id_relationtable id ON od.orderid=id.orderid AND od.goodsid=id.goodid AND id.is_delete <> 1 " +
					"WHERE (od.shipno='"+expresstrackid+"' OR od.re_shipnos LIKE CONCAT('%"+expresstrackid+"%' )  ) AND od.checked=0 AND od.state=1 ORDER BY pf.confirmtime";
			stmt=conn.prepareStatement(sql);
			rs1=stmt.executeQuery();
			while(rs1.next()){
				SearchResultInfo searchresultinfo = new SearchResultInfo();
				String resultTaobaoItemId = null;
				resultTaobaoItemId = rs1.getString("tb_1688_itemid");
				String car_type = rs1.getString("car_type");
				String types1 = "";
				if (car_type.indexOf("<") > -1
						&& car_type.indexOf(">") > -1) {
					for (int j = 1; j < 4 && car_type.indexOf("<") > -1
							&& car_type.indexOf(">") > -1; j++) {
						types1 = car_type.substring(car_type.indexOf("<"),
								car_type.indexOf(">") + 1);
						car_type = car_type.replace(types1, "");
					}
				}
				searchresultinfo.setStrcar_type(changetypeName(car_type));
				searchresultinfo.setTbOrderIdPositions(rs1
						.getString("orderPOSITION"));
				searchresultinfo.setGoodstatus(rs1.getString("goodstatus"));
				searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
				searchresultinfo.setUserid(rs1.getInt("userid"));
				searchresultinfo.setOrdercount(rs1.getInt("ordercount"));
				searchresultinfo.setOrderbuycount(rs1.getInt("buycnt"));
				searchresultinfo.setGoods_pid(rs1.getString("goods_pid"));
				if(rs1.getInt("isDropshipOrder")==3){
					sql = "select goods_pid,shop_id from ali_info_data where 1688_pid='"+rs1.getString("goods_pid")+"' limit 1";
					Connection conn4 = DBHelper.getInstance().getConnection4();
					stmt = conn4.prepareStatement(sql);
					rs2 = stmt.executeQuery();
					if (rs2.next()) {
						searchresultinfo.setShop_id(rs2.getString("shop_id"));
					}
				}else{
					List<String> shop_id_b=new ArrayList<String>();
					String shop_id="";
					shop_id=rs1.getString("shop_id");
					if(StringUtils.isStrNull(shop_id) || "-".equals(shop_id)){
						shop_id="0000";
					}else if(!StringUtils.isStrNull(shop_id) && shop_id.contains("//") && shop_id.contains(".")){
						shop_id=shop_id.split("\\//")[1].split("\\.")[0];
					}
					if(rs1.getInt("is_replenishment")==1){
						shop_id_b=getBhShopId(rs1.getString("orderid"),rs1.getInt("goodsid"));
						searchresultinfo.setBh_shop_id(shop_id_b);
					}
					searchresultinfo.setShop_id(shop_id);
				}
				//获取验货商品的最大类别ID 王宏杰
				String catid="";
				String goodscatid=rs1.getString("goodscatid");
				sql="SELECT func_get_split_string(path,',',1) AS catid FROM 1688_category WHERE category_id='"+goodscatid+"'";
				stmt=conn.prepareStatement(sql);
				rs2=stmt.executeQuery();
				if(rs2.next()){
					catid=rs2.getString("catid");
				}
				searchresultinfo.setCatid(catid);
				searchresultinfo.setIsDropshipOrder(rs1.getString("isDropshipOrder"));
				String orderid = rs1.getString("orderid");
				searchresultinfo.setTaobao_orderid(rs1
						.getString("taobaoOrderid"));
				searchresultinfo.setOrderid(rs1.getString("orderid"));
				set.add(rs1.getString("orderid"));
				searchresultinfo.setGoodsid(rs1.getInt("goodsid"));
				searchresultinfo
						.setPosition(rs1.getString("goodsPOSITION"));
				searchresultinfo.setOrderRemark(rs1
						.getString("orderRemark"));// 订单备注
				searchresultinfo.setGoodsdataid(rs1.getInt("goodsdataid"));
				searchresultinfo.setGoods_name(rs1.getString("goods_name"));
				searchresultinfo.setGoods_url(rs1.getString("goods_url"));
				searchresultinfo.setGoods_p_url(rs1
						.getString("goods_p_url"));
				searchresultinfo.setGoods_img_url(rs1
						.getString("od_goods_img_url").replace("80x80","400x400").replace("60x60","400x400"));
				if(StringUtil.isNotBlank(rs1.getString("img"))){
					searchresultinfo.setImg(Utility.ImgMatch(rs1.getString("img").replace("60x60","400x400").replace("80x80","400x400")));
				}else{
					searchresultinfo.setImg(Utility.ImgMatch(""));
				}
				searchresultinfo.setCurrency(rs1.getString("currency"));
				searchresultinfo.setGoods_price(rs1
						.getDouble("goods_price"));
				searchresultinfo.setGoods_p_price(rs1
						.getDouble("goods_p_price"));
				searchresultinfo.setPurchase_state(rs1
						.getInt("purchase_state"));
				searchresultinfo.setUsecount(rs1.getInt("usecount"));
				searchresultinfo.setBuycount(rs1.getInt("buycount"));
				PaymentDaoImp paymentDao = new PaymentDao();
				String fileByOrderid = paymentDao.getFileByOrderid(rs1.getString("orderid"));
				if (fileByOrderid == null || fileByOrderid.length() < 10) {
					searchresultinfo.setInvoice("");
				} else {
					searchresultinfo.setInvoice("<button><a style='color:red' target='_blank' href='/cbtconsole/autoorder/show?orderid="+rs1.getString("orderid")+"'>查看invoice</a></button>");
				}
				searchresultinfo
						.setOdRemark(rs1.getString("odremark") == null || "".equals(rs1.getString("odremark")) ? "无"
								: rs1.getString("odremark"));
				String mk = "";
				StringBuffer sbf = new StringBuffer();
				if (rs1.getString("bargainRemark") != null
						&& !(rs1.getString("bargainRemark")).equals("")) {
					mk = "砍价情况:" + rs1.getString("bargainRemark") + " ;";
					sbf.append(mk);
				}
				if (rs1.getString("deliveryRemark") != null
						&& !(rs1.getString("deliveryRemark")).equals("")) {
					mk = "交期偏长:" + rs1.getString("deliveryRemark") + " ;";
					sbf.append(mk);
				}
				if (rs1.getString("colorReplaceRemark") != null
						&& !(rs1.getString("colorReplaceRemark"))
						.equals("")) {
					mk = "颜色替换:" + rs1.getString("colorReplaceRemark")
							+ " ;";
					sbf.append(mk);
				}
				if (rs1.getString("sizeReplaceRemark") != null
						&& !(rs1.getString("sizeReplaceRemark")).equals("")) {
					mk = "尺寸替换:" + rs1.getString("sizeReplaceRemark")
							+ " ;";
					sbf.append(mk);
				}
				if (rs1.getString("orderNumRemarks") != null
						&& !(rs1.getString("orderNumRemarks")).equals("")) {
					mk = "订量问题:" + rs1.getString("orderNumRemarks") + " ;";
					sbf.append(mk);
				}
				if (rs1.getString("questionsRemarks") != null
						&& !(rs1.getString("questionsRemarks")).equals("")) {
					mk = "疑问备注:" + rs1.getString("questionsRemarks") + " ;";
					sbf.append(mk);
				}
				if (rs1.getString("unquestionsRemarks") != null
						&& !(rs1.getString("unquestionsRemarks"))
						.equals("")) {
					mk = "无疑问备注:" + rs1.getString("unquestionsRemarks")
							+ " ;";
					sbf.append(mk);
				}
				if (rs1.getString("againRemarks") != null
						&& !(rs1.getString("againRemarks")).equals("")) {
					mk = "再次备注:" + rs1.getString("againRemarks") + " ;";
					sbf.append(mk);
				}
				if(rs1.getInt("isDropshipOrder")==3){
					sbf.append("该订单为采样订单");
				}
				searchresultinfo.setRemark(sbf.toString());
				sbf.setLength(0);
				searchresultinfo.setImgList(taobaoinfoList);
				List<Object[]> orderremark = orderwsdao.getOrderRemark(orderid);
				searchresultinfo.setOrderremark(orderremark);
				searchresultinfo.setChecked(rs1.getInt("checked"));
				searchresultinfo.setSeilUnit(rs1.getString("seilUnit")==null || "".equals(rs1.getString("seilUnit"))?"无":rs1.getString("seilUnit"));
				searchresultinfo.setGcUnit(rs1.getString("gcUnit")==null || "".equals(rs1.getString("gcUnit"))?"无":rs1.getString("gcUnit"));
				info.add(searchresultinfo);
			}
			info.get(0).setOrder_num(set.size());
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs1);
			DBHelper.getInstance().closeResultSet(rs2);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return info;
	}

	@Override
	public List<SearchResultInfo> getOrder(String expresstrackid, String checked) {

		// String sql = "SELECT DISTINCT os.* ,lgi.orderid taobaoOrderid,
		// SPACE(200) orderPOSITION,SPACE(50) goodsPOSITION,0 AS ordercount,0 AS
		// buycnt,SPACE(400) img,SPACE(400) od_goods_img_url,SPACE(300)
		// car_type,"
		// +" SPACE(10) goodstatus,SPACE(200) orderRemark"
		// +" FROM order_product_source os INNER JOIN (SELECT DISTINCT
		// itemid,orderid FROM taobao_1688_order_history WHERE shipno =
		// '"+expresstrackid+"') lgi ON lgi.itemid = os.tb_1688_itemid LEFT JOIN
		// paymentconfirm pf ON "
		// +" os.orderid=pf.orderno"
		// +" WHERE os.purchase_state IN (2,3,5,6,7,9) AND TO_DAYS(NOW()) -
		// TO_DAYS(os.addtime) <= 60 ORDER BY pf.confirmtime;";
		String sql = "";
		Connection conn = DBHelper.getInstance().getConnection();
		CallableStatement cstmt = null;
		PreparedStatement stmt = null;
		int rs = 0;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		// Connection conn = DBHelper.getInstance().getConnection();
		// 本地化链接
		List<SearchResultInfo> info = new ArrayList<SearchResultInfo>();
		List<SearchTaobaoInfo> taobaoinfoList = new ArrayList<SearchTaobaoInfo>();
		int adminid = 520;
		try {
			sql = "SELECT ta.adminid FROM tb_1688_accounts ta INNER JOIN taobao_1688_order_history t ON ta.account=t.username WHERE t.shipno='"
					+ expresstrackid + "' and ta.del=1 LIMIT 1";
			stmt = conn.prepareStatement(sql);
			rs1 = stmt.executeQuery();
			if (rs1.next()) {
				adminid = rs1.getInt("adminid");
			}
			if ("1".equals(checked)) {
				sql = "{call getOrderData_1(?)}";
			} else {
				sql = "{call getOrderData(?,?)}";
			}
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, expresstrackid);
			if (!"1".equals(checked)) {
				stmt.setInt(2, adminid);
			}
			rs1 = stmt.executeQuery();

			// stmt = conn.prepareStatement(sql);
			// stmt.setString(1, expresstrackid);
			// rs1 = stmt.executeQuery();
			if (true) {
				Set set=new HashSet();
				while (rs1.next()) {
					SearchResultInfo searchresultinfo = new SearchResultInfo();
					String resultTaobaoItemId = null;
					resultTaobaoItemId = rs1.getString("tb_1688_itemid");
					String car_type = rs1.getString("car_type");
					String types1 = "";
					if (car_type.indexOf("<") > -1
							&& car_type.indexOf(">") > -1) {
						for (int j = 1; j < 4 && car_type.indexOf("<") > -1
								&& car_type.indexOf(">") > -1; j++) {
							types1 = car_type.substring(car_type.indexOf("<"),
									car_type.indexOf(">") + 1);
							car_type = car_type.replace(types1, "");
						}
					}
					searchresultinfo.setStrcar_type(changetypeName(car_type));
					searchresultinfo.setTbOrderIdPositions(rs1
							.getString("orderPOSITION"));
					searchresultinfo.setGoodstatus(rs1.getString("goodstatus"));
					searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
					searchresultinfo.setUserid(rs1.getInt("userid"));
					searchresultinfo.setOrdercount(rs1.getInt("ordercount"));
					searchresultinfo.setOrderbuycount(rs1.getInt("buycnt"));
					searchresultinfo.setGoods_pid(rs1.getString("goods_pid"));
					if(rs1.getInt("isDropshipOrder")==3 && "1".equals(checked)){
						sql = "select goods_pid,shop_id from ali_info_data where 1688_pid='"+rs1.getString("goods_pid")+"' limit 1";
						Connection conn4 = DBHelper.getInstance().getConnection4();
						stmt = conn4.prepareStatement(sql);
						rs2 = stmt.executeQuery();
						if (rs2.next()) {
							searchresultinfo.setShop_id(rs2.getString("shop_id"));
						}
						DBHelper.getInstance().closeResultSet(rs2);
						DBHelper.getInstance().closePreparedStatement(stmt);
						DBHelper.getInstance().closeConnection(conn4);
					}else{
						List<String> shop_id_b=new ArrayList<String>();
						String shop_id="";
						shop_id=rs1.getString("shop_id");
						if(StringUtils.isStrNull(shop_id) || "-".equals(shop_id)){
							shop_id="0000";
						}else if(!StringUtils.isStrNull(shop_id) && shop_id.contains("//") && shop_id.contains(".")){
							shop_id=shop_id.split("\\//")[1].split("\\.")[0];
						}
						if(rs1.getInt("is_replenishment")==1){
							shop_id_b=getBhShopId(rs1.getString("orderid"),rs1.getInt("goodsid"));
							searchresultinfo.setBh_shop_id(shop_id_b);
						}
						searchresultinfo.setShop_id(shop_id);
					}
					//获取验货商品的最大类别ID 王宏杰
					String catid="";
					if("1".equals(checked)){
						String goodscatid=rs1.getString("goodscatid");
						sql="SELECT func_get_split_string(path,',',1) AS catid FROM 1688_category WHERE category_id='"+goodscatid+"'";
						stmt=conn.prepareStatement(sql);
						rs2=stmt.executeQuery();
						if(rs2.next()){
							catid=rs2.getString("catid");
						}
					}
					searchresultinfo.setCatid(catid);
					searchresultinfo.setIsDropshipOrder(rs1.getString("isDropshipOrder"));
					String orderid = rs1.getString("orderid");
					searchresultinfo.setTaobao_orderid(rs1
							.getString("taobaoOrderid"));
					searchresultinfo.setOrderid(rs1.getString("orderid"));
					set.add(rs1.getString("orderid"));
					searchresultinfo.setGoodsid(rs1.getInt("goodsid"));
					searchresultinfo
							.setPosition(rs1.getString("goodsPOSITION"));
					searchresultinfo.setOrderRemark(rs1
							.getString("orderRemark"));// 订单备注
					searchresultinfo.setGoodsdataid(rs1.getInt("goodsdataid"));
					searchresultinfo.setGoods_name(rs1.getString("goods_name"));
					searchresultinfo.setGoods_url(rs1.getString("goods_url"));
					searchresultinfo.setGoods_p_url(rs1
							.getString("goods_p_url"));
					searchresultinfo.setGoods_img_url(rs1
							.getString("od_goods_img_url").replace("80x80","400x400").replace("60x60","400x400"));
					searchresultinfo.setImg(Utility.ImgMatch(rs1
							.getString("img").replace("60x60","400x400").replace("80x80","400x400")));
					searchresultinfo.setCurrency(rs1.getString("currency"));
					searchresultinfo.setGoods_price(rs1
							.getDouble("goods_price"));
					searchresultinfo.setGoods_p_price(rs1
							.getDouble("goods_p_price"));
					searchresultinfo.setPurchase_state(rs1
							.getInt("purchase_state"));
					searchresultinfo.setUsecount(rs1.getInt("usecount"));
					searchresultinfo.setBuycount(rs1.getInt("buycount"));
					PaymentDaoImp paymentDao = new PaymentDao();
					String fileByOrderid = paymentDao.getFileByOrderid(rs1.getString("orderid"));
					if (fileByOrderid == null || fileByOrderid.length() < 10) {
						searchresultinfo.setInvoice("");
					} else {
						searchresultinfo.setInvoice("<button><a style='color:red' target='_blank' href='/cbtconsole/autoorder/show?orderid="+rs1.getString("orderid")+"'>查看invoice</a></button>");
					}
					searchresultinfo
							.setOdRemark(rs1.getString("odremark") == null || "".equals(rs1.getString("odremark")) ? "无"
									: rs1.getString("odremark"));
					String mk = "";
					StringBuffer sbf = new StringBuffer();
					if (rs1.getString("bargainRemark") != null
							&& !(rs1.getString("bargainRemark")).equals("")) {
						mk = "砍价情况:" + rs1.getString("bargainRemark") + " ;";
						sbf.append(mk);
					}
					if (rs1.getString("deliveryRemark") != null
							&& !(rs1.getString("deliveryRemark")).equals("")) {
						mk = "交期偏长:" + rs1.getString("deliveryRemark") + " ;";
						sbf.append(mk);
					}
					if (rs1.getString("colorReplaceRemark") != null
							&& !(rs1.getString("colorReplaceRemark"))
							.equals("")) {
						mk = "颜色替换:" + rs1.getString("colorReplaceRemark")
								+ " ;";
						sbf.append(mk);
					}
					if (rs1.getString("sizeReplaceRemark") != null
							&& !(rs1.getString("sizeReplaceRemark")).equals("")) {
						mk = "尺寸替换:" + rs1.getString("sizeReplaceRemark")
								+ " ;";
						sbf.append(mk);
					}
					if (rs1.getString("orderNumRemarks") != null
							&& !(rs1.getString("orderNumRemarks")).equals("")) {
						mk = "订量问题:" + rs1.getString("orderNumRemarks") + " ;";
						sbf.append(mk);
					}
					if (rs1.getString("questionsRemarks") != null
							&& !(rs1.getString("questionsRemarks")).equals("")) {
						mk = "疑问备注:" + rs1.getString("questionsRemarks") + " ;";
						sbf.append(mk);
					}
					if (rs1.getString("unquestionsRemarks") != null
							&& !(rs1.getString("unquestionsRemarks"))
							.equals("")) {
						mk = "无疑问备注:" + rs1.getString("unquestionsRemarks")
								+ " ;";
						sbf.append(mk);
					}
					if (rs1.getString("againRemarks") != null
							&& !(rs1.getString("againRemarks")).equals("")) {
						mk = "再次备注:" + rs1.getString("againRemarks") + " ;";
						sbf.append(mk);
					}
					if(rs1.getInt("isDropshipOrder")==3){
						sbf.append("该订单为采样订单");
					}
					searchresultinfo.setRemark(sbf.toString());
					sbf.setLength(0);
					searchresultinfo.setImgList(taobaoinfoList);
					List<Object[]> orderremark = orderwsdao.getOrderRemark(orderid);
					searchresultinfo.setOrderremark(orderremark);
					searchresultinfo.setChecked(rs1.getInt("checked"));
					searchresultinfo.setSeilUnit(rs1.getString("seilUnit")==null || "".equals(rs1.getString("seilUnit"))?"无":rs1.getString("seilUnit"));
					searchresultinfo.setGcUnit(rs1.getString("gcUnit")==null || "".equals(rs1.getString("gcUnit"))?"无":rs1.getString("gcUnit"));
					info.add(searchresultinfo);
				}
				//一个1688包裹对应的采购订单数量
				info.get(0).setOrder_num(set.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);

		}
		return info;
	}

	public static List<String> getBhShopId(String orderid,int goodsid){
		List<String> shop_ids=new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql="SELECT distinct shop_id FROM order_replenishment WHERE orderid=? AND goodsid=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, goodsid);
			rs=stmt.executeQuery();
			while(rs.next()){
				String shop_id=rs.getString("shop_id");
				shop_id=shop_id.split("\\//")[1].split("\\.")[0];
				shop_ids.add(shop_id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(stmt!=null){
					stmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				DBHelper.getInstance().closeConnection(conn);
			}
		}
		return shop_ids;
	}

	@Override
	public int confirmMoveLittleLibrary(Map<String, String> map) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String goods_pid="";
		String sql = "SELECT od.*,i.remaining,li.lock_inventory_amount,i.inventory_amount,i.new_inventory_amount,i.flag as lock_flag,i.new_remaining,li.lock_remaining,li.id liid,i.id iid FROM lock_inventory li inner join inventory i on li.in_id=i.id INNER JOIN order_details od on li.od_id=od.id where od.goodsid='"
				+ map.get("goodsid")
				+ "' AND od.orderid='"
				+ map.get("orderid") + "' and li.is_use=1";
		SendMQ sendMQ = null;
		try {
			sendMQ = new SendMQ();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int remaining = rs.getInt("lock_remaining");// 使用的库存数量
				goods_pid=rs.getString("goods_pid");
				// 增加入库记录
				sql = "insert into id_relationtable (orderid,goodid,goodurl,goodstatus,goodarrivecount,createtime,barcode,position,username,userid,itemqty) "
						+ "select '"
						+ map.get("orderid")
						+ "','"
						+ map.get("goodsid")
						+ "','"
						+ rs.getString("car_url")
						+ "',1,'"
						+ remaining
						+ "',now(),'"
						+ map.get("barcode")
						+ "','"
						+ getPosition(map.get("barcode"))
						+ "','"
						+ map.get("userName")
						+ "','"
						+ rs.getInt("userid")
						+ "','"
						+ remaining
						+ "' from dual where not exists (select * from id_relationtable where orderid='"
						+ map.get("orderid")
						+ "' and goodid='"
						+ map.get("goodsid") + "')";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				// 库存状态变为已确认
				sql = "update lock_inventory set flag=1 where id="
						+ rs.getInt("liid") + "";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				// 减少库存
				if (rs.getInt("lock_flag")==1) {
					sql = "update inventory set new_remaining="
							+ (rs.getInt("new_remaining") - rs.getInt("lock_remaining")) + ","
							+ "new_inventory_amount='"+(rs.getDouble("new_inventory_amount")-rs.getDouble("lock_inventory_amount"))+"' where id="
							+ rs.getInt("iid") + "";
				} else {
					sql = "update inventory set remaining="
							+ (rs.getInt("remaining") - rs.getInt("lock_remaining")) + ","
							+ "inventory_amount='"+(rs.getDouble("inventory_amount")-rs.getDouble("lock_inventory_amount"))+"' where id="
							+ rs.getInt("iid") + "";
				}
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				sql = "update orderinfo set state=1 where order_no='" + map.get("orderid")+ "'";
				sendMQ.sendMsg(new RunSqlModel(sql));
				sql = "update orderinfo set state=1 where order_no='" + map.get("orderid")+ "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				sql = "update order_details set inventory_remark='库存不足,需补购【"
						+ (Integer.valueOf(map.get("purchaseCount").toString()) - Integer
						.valueOf(map.get("remaining").toString()))
						+ "】件' where orderid='" + map.get("orderid")
						+ "' and goodsid='" + map.get("goodsid") + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				//改变库存标识为没有库存
				sql="select can_remaining inventory from where id='"+rs.getInt("iid")+"'";
				stmt = conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				if(rs.next()){
					if(rs.getInt("can_remaining")>=0){
						//如果库存锁定后可用库存为0则更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
					}else{
						//如果库存锁定后可用库存为0则更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"0");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			sendMQ.closeConn();
		}
		return row;
	}
	/**
	 * 仓库移库是库存有误记录
	 * whj 2017-08-22
	 */
	@Override
	public int insertInventoryWrong(Map<String, String> map) {
		int row=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="";
		try{
			sql = "SELECT i.remaining,i.new_remaining,i.goods_url,i.sku,i.id,li.od_id odid FROM inventory i INNER JOIN lock_inventory li ON i.id=li.in_id WHERE li.od_id='"
					+ map.get("od_id") + "' and is_delete=0";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				sql="insert into inventroy_wrong (username,createtime,barcode,goods_url,sku,amount,remark) values(?,now(),?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, map.get("userName"));
				stmt.setString(2, map.get("barcode"));
				stmt.setString(3, rs.getString("goods_url"));
				stmt.setString(4, rs.getString("sku"));
				stmt.setInt(5, Integer.valueOf(map.get("old_remaining"))-Integer.valueOf(map.get("all_remaining")));
				stmt.setString(6, map.get("remark"));
				row=stmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	/**
	 * 使用库存时发现库存有误修改当前锁定库存操作
	 *
	 * @param map
	 * @return 影响的数据库列行数
	 */
	@Override
	public int updateLockInventory(Map<String, String> map) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		int new_remaining = 0;
		String goods_pid="";
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
		SendMQ sendMQ = null;
		try {
			sendMQ = new SendMQ();
			// 修改库存数量
			String sql = "SELECT i.goods_pid,i.remaining,i.new_remaining,i.remark,i.id,i.goods_p_price,li.od_id odid FROM inventory i INNER JOIN lock_inventory li ON i.id=li.in_id WHERE li.od_id='"
					+ map.get("od_id") + "' and is_delete=0";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				new_remaining = rs.getInt("new_remaining");
				goods_pid=rs.getString("goods_pid");
				String goods_p_price=rs.getString("goods_p_price").indexOf(",")>-1?rs.getString("goods_p_price").split(",")[0]:rs.getString("goods_p_price");
				String remark=rs.getString("remark")==null?"":rs.getString("remark");
				if(map.get("remark")!=null && !"".equals(map.get("remark")) && remark!=null && !"".equals(remark)){
					remark=remark+"</br>"+sdf.format(date)+";"+map.get("userName")+";"+map.get("remark");
				}else if((remark==null || "".equals(remark)) && (map.get("remark")!=null && !"".equals(map.get("remark")))){
					remark=sdf.format(date)+";"+map.get("userName")+";"+map.get("remark");
				}
				if (new_remaining > 0) {
					sql = "update inventory set new_remaining="
							+ map.get("all_remaining") + ",remark='"+remark+"',can_remaining="
							+ map.get("all_remaining") + ",new_inventory_amount='"+(Integer.valueOf(map.get("all_remaining"))*Double.valueOf(goods_p_price))+"' where id="
							+ rs.getInt("id") + "";
				} else {
					sql = "update inventory set remaining="
							+ map.get("all_remaining") + ",remark='"+remark+"',can_remaining="
							+ map.get("all_remaining") + ",inventory_amount='"+(Integer.valueOf(map.get("all_remaining"))*Double.valueOf(goods_p_price))+"' where id="
							+ rs.getInt("id") + "";
				}
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				if (Integer.valueOf(map.get("all_remaining")) == 0) {
					sql = "update lock_inventory set is_delete=1,lock_inventory_amount=0 where in_id="
							+ rs.getInt("id") + " and flag=0";
					stmt = conn.prepareStatement(sql);
					row = stmt.executeUpdate();
					sql = "update order_details set inventory_remark='库存为0,请重新采购' where id="
							+ rs.getInt("odid") + "";
					stmt = conn.prepareStatement(sql);
					row = stmt.executeUpdate();
					//释放锁定库存后更新线上产品表。预上线表。后台产品表库存标识为无库存
					//本地
					sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
					//预上线表
					Connection conn28 = DBHelper.getInstance().getConnection8();
					sql="update custom_benchmark_ready_newest set is_stock_flag=0 where pid='"+goods_pid+"'";
					stmt = conn28.prepareStatement(sql);
					stmt.executeUpdate();
					DBHelper.getInstance().closeConnection(conn28);
					//线上表
//                    sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
//                    sendMQ.sendMsg(new RunSqlModel(sql));
					GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"0");
				} else {
					// 根据锁定库存数量时间来重新分配可使用库存量
					sql = "SELECT li.od_id,od.yourorder,od.seilUnit,li.in_id FROM lock_inventory li INNER JOIN order_details od ON li.od_id=od.id WHERE li.in_id="
							+ rs.getInt("id")
							+ " AND li.flag=0 and li.is_delete=0 and od.state<2 ORDER BY li.id";
					stmt = conn.prepareStatement(sql);
					rs = stmt.executeQuery();
					while (rs.next()) {
						String str = rs.getString("seilUnit").trim();
						StringBuffer bf = new StringBuffer();
						if (str != null && !"".equals(str)) {
							for (int i = 0; i < str.length(); i++) {
								if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
									bf.append(str.charAt(i));
								}
							}
						}
						int od_id = rs.getInt("od_id");
						int yourorder = rs.getInt("yourorder")
								* Integer
								.valueOf(bf.toString().length() > 0 ? bf
										.toString() : "1");// 该商品需要采购的数量
						String sql1 = "select can_remaining,id from inventory where id="
								+ rs.getInt("in_id") + "";
						stmt1 = conn.prepareStatement(sql1);
						rs1 = stmt1.executeQuery();
						if (rs1.next() && rs1.getInt("can_remaining") > 0) {
							int can_remaining = rs1.getInt("can_remaining");
							sql1 = "select od.*,li.in_id from order_details od inner join lock_inventory li on od.id=li.od_id where od.id="
									+ od_id + "";
							stmt1 = conn.prepareStatement(sql1);
							rs1 = stmt1.executeQuery();
							if (rs1.next()) {
								map.put("goodsid", rs1.getString("goodsid"));
								map.put("orderid", rs1.getString("orderid"));
								map.put("purchaseCount",
										rs1.getString("yourorder"));
								map.put("remaining",
										String.valueOf(can_remaining));
								if (can_remaining >= yourorder) {
									// 库存足够
									sql = "update lock_inventory set lock_inventory_amount="+(Double.valueOf(goods_p_price)*yourorder)+",lock_remaining="
											+ yourorder
											+ " where od_id="
											+ od_id + "";
									stmt = conn.prepareStatement(sql);
									row = stmt.executeUpdate();
									sql = "update inventory set can_remaining="
											+ (can_remaining - yourorder)
											+ " where id="
											+ rs1.getInt("in_id") + "";
									stmt = conn.prepareStatement(sql);
									row = stmt.executeUpdate();
								} else {
									// 库存不够
									sql = "update inventory set can_remaining=0 where id="
											+ rs1.getInt("in_id") + "";
									stmt = conn.prepareStatement(sql);
									row = stmt.executeUpdate();
									sql = "update lock_inventory set lock_inventory_amount="+(Double.valueOf(goods_p_price)*yourorder)+",lock_remaining="
											+ can_remaining
											+ " where od_id="
											+ od_id + "";
									stmt = conn.prepareStatement(sql);
									row = stmt.executeUpdate();
								}
							}
						}
					}
					//查询该商品剩余可用库存
					String sql1 = "select can_remaining,id from inventory where id="
							+ rs.getInt("in_id") + "";
					stmt1 = conn.prepareStatement(sql1);
					rs1 = stmt1.executeQuery();
					if(rs1.next() && rs1.getInt("can_remaining") > 0){
						//释放锁定库存后更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
					}else{
						//释放锁定库存后更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+rs1.getString("goods_pid")+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"0");
					}
				}
			}
			sendMQ.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
			// updateLockInventory(map);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			sendMQ.closeConn();
		}
		return row;
	}

	@Override
	public int confirmMoveLibrary(Map<String, String> map) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn1 = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String goods_pid="";
		String sql = "SELECT od.*,i.remaining,i.new_remaining,i.inventory_amount,i.new_inventory_amount,li.lock_inventory_amount,i.flag as lock_flag,i.can_remaining,li.id liid,li.lock_remaining,i.id iid FROM lock_inventory li inner join inventory i on li.in_id=i.id INNER JOIN order_details od on li.od_id=od.id where od.goodsid='"
				+ map.get("goodsid")
				+ "' AND od.orderid='"
				+ map.get("orderid") + "' and li.is_use=1";
		try {
			SendMQ sendMQ=new SendMQ();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				goods_pid=rs.getString("goods_pid");
				// 增加入库记录
				sql = "insert into id_relationtable (orderid,goodid,goodurl,goodstatus,goodarrivecount,createtime,barcode,position,username,userid,itemqty) "
						+ "select '"
						+ map.get("orderid")
						+ "','"
						+ map.get("goodsid")
						+ "','"
						+ rs.getString("car_url")
						+ "',1,'"
						+ map.get("purchaseCount")
						+ "',now(),'"
						+ map.get("barcode")
						+ "','"
						+ getPosition(map.get("barcode"))
						+ "','"
						+ map.get("userName")
						+ "','"
						+ rs.getInt("userid")
						+ "','"
						+ rs.getInt("lock_remaining")
						+ "' from dual where not exists (select * from id_relationtable where orderid='"
						+ map.get("orderid")
						+ "' and goodid='"
						+ map.get("goodsid") + "')";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				// 锁定库存状态改变
				sql = "update lock_inventory set flag=1 where id='"
						+ rs.getInt("liid") + "'";
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				// 库存减少
				if (rs.getInt("lock_flag")==1) {
					sql = "update inventory set new_remaining="
							+ (rs.getInt("new_remaining") - rs.getInt("lock_remaining")) + ","
							+ "new_inventory_amount='"+(rs.getDouble("new_inventory_amount")-rs.getDouble("lock_inventory_amount"))+"' where id='"
							+ rs.getInt("iid") + "'";
				} else {
					sql = "update inventory set remaining="
							+ (rs.getInt("remaining") - rs.getInt("lock_remaining")) + ","
							+ "inventory_amount='"+(rs.getDouble("inventory_amount")-rs.getDouble("lock_inventory_amount"))+"' where id='"
							+ rs.getInt("iid") + "'";
				}
				stmt = conn.prepareStatement(sql);
				row = stmt.executeUpdate();
				String sql2 = "select isDropshipOrder from orderinfo where order_no='"
						+ map.get("orderid") + "'";
				stmt = conn1.prepareStatement(sql2);
				rs = stmt.executeQuery();
				if (rs.next()) {
					int isDropshipOrder = rs.getInt("isDropshipOrder");
					if (isDropshipOrder == 1) {
						sql2 = "update order_details set state=1,checked=1 where orderid='"
								+ map.get("orderid")
								+ "' and goodsid='"
								+ map.get("goodsid") + "'";
						sendMQ.sendMsg(new RunSqlModel(sql2));
						sql2 = "SELECT DISTINCT COUNT(b.id)-SUM(b.state) as counts FROM order_details o INNER JOIN order_details b ON o.dropshipid=b.dropshipid WHERE o.orderid='"
								+ map.get("orderid")
								+ "' AND o.goodsid='"
								+ map.get("goodsid") + " and b.state<2";
						stmt = conn1.prepareStatement(sql2);
						rs = stmt.executeQuery();
						if (rs.next() && rs.getInt("counts") == 0) {
							sql2 = "update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"
									+ map.get("orderid")
									+ "' and goodsid='"
									+ map.get("goodsid") + "')";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}else{
							sql2 = "update dropshiporder set state=1 where child_order_no=(select dropshipid from order_details where orderid='"
									+ map.get("orderid")
									+ "' and goodsid='"
									+ map.get("goodsid") + "')";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
						// 判断主单下所有的子单是否到库
						sql2 = "SELECT  COUNT(orderid)*2-SUM(state) as countss FROM dropshiporder WHERE parent_order_no='"
								+ map.get("orderid") + "'";
						stmt = conn1.prepareStatement(sql2);
						rs = stmt.executeQuery();
						if (rs.next() && rs.getInt("countss") == 0) {
							sql2 = "update orderinfo set state=2 where order_no='"
									+ map.get("orderid") + "'";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}else{
							sql2 = "update orderinfo set state=1 where order_no='"
									+ map.get("orderid") + "'";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
						sql2 = "update order_details set checked=1,inventory_remark='库存充足' where orderid='"
								+ map.get("orderid")
								+ "' and goodsid='"
								+ map.get("goodsid") + "'";
						stmt = conn.prepareStatement(sql2);
						stmt.executeUpdate();
					} else {
						// 非dropshi订单
						sql2 = "update order_details set state=1,checked=1,purchase_state=3 where orderid='"
								+ map.get("orderid")
								+ "' and goodsid='"
								+ map.get("goodsid") + "'";
						sendMQ.sendMsg(new RunSqlModel(sql2));
						sql2 = "SELECT (COUNT(id) - SUM(state)) as orderdetails_count FROM order_details WHERE state<2 AND orderid='"
								+ map.get("orderid") + "'";
						stmt = conn1.prepareStatement(sql2);
						rs = stmt.executeQuery();
						if (rs.next() && rs.getInt("orderdetails_count") == 0) {
							sql2 = "UPDATE orderinfo SET state=2 WHERE order_no='"
									+ map.get("orderid") + "'";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}else{
							sql2 = "UPDATE orderinfo SET state=1 WHERE order_no='"
									+ map.get("orderid") + "'";
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
						sql2 = "update order_details set checked=1,inventory_remark='库存充足' where orderid='"
								+ map.get("orderid")
								+ "' and goodsid='"
								+ map.get("goodsid") + "'";
						stmt = conn.prepareStatement(sql2);
						stmt.executeUpdate();
					}
				}
				//改变库存标识为没有库存
				sql="select can_remaining inventory from where id='"+rs.getInt("iid")+"'";
				stmt = conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				if(rs.next()){
					if(rs.getInt("can_remaining")>=0){
						//如果库存锁定后可用库存为0则更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
					}else{
						//如果库存锁定后可用库存为0则更新线上产品表。预上线表。后台产品表库存标识为无库存
						//本地
						sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn.prepareStatement(sql);
						stmt.executeUpdate();
						//预上线表
						Connection conn28 = DBHelper.getInstance().getConnection8();
						sql="update custom_benchmark_ready_newest set is_stock_flag=0 where pid='"+goods_pid+"'";
						stmt = conn28.prepareStatement(sql);
						stmt.executeUpdate();
						DBHelper.getInstance().closeConnection(conn28);
						//线上表
//                        sql="update custom_benchmark_ready set is_stock_flag=0 where pid='"+goods_pid+"'";
//                        sendMQ.sendMsg(new RunSqlModel(sql));
						GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"0");
					}
				}
			}
//            sendMQ.closeConn();
		} catch (Exception e) {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn1);
		}
		return row;
	}

	@Override
	public List<SearchResultInfo> getReplenishResultInfo(String expresstrackid) {
		String sql = "SELECT DISTINCT rep_state purchase_state,ir.POSITION goodsPOSITION,od.id AS odid,od.remark AS odremark,ir.goodstatus goodstatus,od.seilUnit AS gcUnit, " +
				" lgi.itemid, o.userid,o.orderid,o.goodsid,o.rep_state goodsdataid, o.goods_title goods_name,o.goods_url,od.car_type,od.checked,od.seilUnit, o.goods_p_url," +
				" CONCAT(SUBSTRING_INDEX(od.car_img,'.jpg',1),'.jpg') goods_img_url,  CONCAT(SUBSTRING_INDEX(od.car_img,'.jpg',1),'.jpg') img,o.goods_price,o.goods_price goods_p_price," +
				" od.yourorder usecount,o.buycount,o.remark,orderstate.ordercount,orderstate.buycnt ,(SELECT GROUP_CONCAT(DISTINCT(POSITION),',') FROM id_relationtable ir WHERE ir.orderid=o.orderid " +
				" AND ir.is_delete <> 1) AS POSITION ,o.goods_type FROM order_replenishment o  " +
				" INNER JOIN (SELECT DISTINCT  itemid FROM taobao_1688_order_history WHERE shipno = ?) lgi " +
				" ON lgi.itemid = o.tb_1688_itemid  INNER JOIN order_details od ON od.id=o.od_id AND od.orderid=o.orderid  INNER JOIN (SELECT orderid,COUNT(id) ordercount,SUM(state) buycnt FROM order_details" +
				"  WHERE order_details.state<2 GROUP BY orderid )  orderstate   ON  orderstate.orderid=o.orderid LEFT JOIN id_relationtable ir ON ir.odid = o.od_id AND ir.orderid=o.orderid  AND ir.is_delete =0   " +
				" WHERE  ir.is_replenishment=1 AND o.rep_state=0 AND TO_DAYS(NOW()) - TO_DAYS(o.createtime) <= 60 ORDER BY o.orderid;";
		// Connection conn = DBHelper.getInstance().getConnection();
		// 本地化链接
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		List<SearchResultInfo> info = new ArrayList<SearchResultInfo>();
		List<SearchTaobaoInfo> taobaoinfoList = new ArrayList<SearchTaobaoInfo>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, expresstrackid);
			rs1 = stmt.executeQuery();
			if (!rs1.wasNull()) {
				while (rs1.next()) {
					SearchResultInfo searchresultinfo = new SearchResultInfo();
					String resultTaobaoItemId = null;
					resultTaobaoItemId = rs1.getString("itemid");
					String car_type = rs1.getString("car_type");
					searchresultinfo.setStrcar_type(changetypeName(car_type));
					// 所有的仓库位置changetypeName
					searchresultinfo.setTbOrderIdPositions(rs1
							.getString("position"));
					searchresultinfo.setOdid(rs1.getString("odid"));
					searchresultinfo.setGoodstatus(rs1.getString("goodstatus"));
					searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
					searchresultinfo.setUserid(rs1.getInt("userid"));
					searchresultinfo.setOrdercount(rs1.getInt("ordercount"));
					searchresultinfo.setOrderbuycount(rs1.getInt("buycnt"));
					// 用于查询对内备注
					String orderid = rs1.getString("orderid");
					searchresultinfo.setOrderid(rs1.getString("orderid"));
					searchresultinfo.setGoodsid(rs1.getInt("goodsid"));
					PaymentDaoImp paymentDao = new PaymentDao();
					String fileByOrderid = paymentDao.getFileByOrderid(rs1.getString("orderid"));
					if (fileByOrderid == null || fileByOrderid.length() < 10) {
						searchresultinfo.setInvoice("");
					} else {
						searchresultinfo.setInvoice("<button><a style='color:red' target='_blank' href='/cbtconsole/autoorder/show?orderid="+rs1.getString("orderid")+"'>查看invoice</a></button>");
					}
					// 该件商品是否已存仓库
					// searchresultinfo.setPosition(this.getPositionByGoodId(searchresultinfo.getGoodsid()+""));
					searchresultinfo
							.setPosition(rs1.getString("goodsPOSITION"));
					searchresultinfo.setGoodsdataid(rs1.getInt("goodsdataid"));
					searchresultinfo.setGoods_name(rs1.getString("goods_name"));
					searchresultinfo.setGoods_url(rs1.getString("goods_url"));
					searchresultinfo.setGoods_p_url(rs1
							.getString("goods_p_url"));
					searchresultinfo.setGoods_img_url(rs1
							.getString("goods_img_url"));
					searchresultinfo.setImg(Utility.ImgMatch(rs1
							.getString("img")));
					searchresultinfo.setGoods_price(rs1
							.getDouble("goods_price"));
					searchresultinfo.setGoods_p_price(rs1
							.getDouble("goods_p_price"));
					searchresultinfo.setPurchase_state(rs1
							.getInt("purchase_state"));
					searchresultinfo.setUsecount(rs1.getInt("usecount"));
					searchresultinfo.setGoodsType(rs1.getString("goods_type"));
					searchresultinfo.setBuycount(rs1.getInt("buycount"));
					searchresultinfo.setRemark(rs1.getString("remark"));
					searchresultinfo.setGcUnit(rs1.getString("gcUnit")==null || "".equals(rs1.getString("gcUnit"))?"无":rs1.getString("gcUnit"));
					searchresultinfo
							.setOdRemark(rs1.getString("odremark") == null || "".equals(rs1.getString("odremark")) ? "无"
									: rs1.getString("odremark"));
					// 备注对内
					List<Object[]> orderremark = orderwsdao
							.getOrderRemark(orderid);
					searchresultinfo.setOrderremark(orderremark);
					searchresultinfo.setImgList(taobaoinfoList);
					searchresultinfo.setChecked(rs1.getInt("checked"));
					searchresultinfo.setSeilUnit(rs1.getString("seilUnit")==null || "".equals(rs1.getString("seilUnit"))?"无":rs1.getString("seilUnit"));
					info.add(searchresultinfo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return info;
	}

	// 亚马逊商品扫描
	@Override
	public List<SearchResultInfo> getAmazonResultInfo(String expresstrackid) {
		String sql = "SELECT DISTINCT o.storeName,ir.position goodsPOSITION,o.productNumbers usecount,productNumbers buycount,o.url goods_url,o.orderNo orderid,"
				+ "mabangSum.ordercount ordercount,mabangSum.buycnt buycnt,'用户id' userid,'商品id goodsid','商品图片' goods_p_url,'规格图片' goods_img_url, o.productName goods_name,o.sku car_type,o.productNumbers,"
				+ "(CASE WHEN o.state = 0 THEN '未到货' WHEN o.state = 1 THEN '已入库'  WHEN o.state = 2 THEN '已入库 但货有问题' else '状态错误' END) goodstatus,o.productUnitPrice ,o.productTotalAmount,o.item_id itemid,"
				+ "  (SELECT GROUP_CONCAT(DISTINCT(POSITION),',') FROM id_relationtable ir WHERE ir.orderid=o.orderNo  AND ir.is_delete <> 1) AS position "
				+ "  FROM mabangShipment o  "
				+ "INNER JOIN (SELECT o.orderNo,COUNT(o.orderNo) ordercount,COUNT(CASE WHEN o.state = 1 THEN 1 END) buycnt FROM mabangShipment o GROUP BY o.orderNo  )  mabangSum   ON  mabangSum.orderNo=o.orderNo "
				+ "  INNER JOIN (SELECT DISTINCT  itemid FROM taobao_1688_order_history WHERE shipno = ?) lgi ON lgi.itemid = o.item_id  "
				+ " LEFT JOIN id_relationtable ir ON ir.orderid = o.orderNo AND ir.itemid = o.item_id AND ir.is_delete <> 1"
				+ " WHERE 1 = 1 AND TO_DAYS(NOW()) - TO_DAYS(o.createtime) <= 60 ORDER BY o.orderNo;";
		// Connection conn = DBHelper.getInstance().getConnection();
		// 本地化链接
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		List<SearchResultInfo> info = new ArrayList<SearchResultInfo>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, expresstrackid);
			rs1 = stmt.executeQuery();
			if (!rs1.wasNull()) {
				while (rs1.next()) {
					SearchResultInfo searchresultinfo = new SearchResultInfo();
					String resultTaobaoItemId = null;
					resultTaobaoItemId = rs1.getString("itemid");
					String car_type = rs1.getString("car_type");
					searchresultinfo.setStoreName(rs1.getString("storeName"));
					searchresultinfo.setStrcar_type(changetypeName(car_type));
					// 所有的仓库位置
					searchresultinfo.setTbOrderIdPositions(rs1
							.getString("position"));
					searchresultinfo.setGoodstatus(rs1.getString("goodstatus"));
					searchresultinfo.setTaobao_itemid(resultTaobaoItemId);
					// searchresultinfo.setUserid(rs1.getInt("userid"));
					searchresultinfo.setOrdercount(rs1.getInt("ordercount"));
					searchresultinfo.setOrderbuycount(rs1.getInt("buycnt"));
					searchresultinfo.setOrderid(rs1.getString("orderid"));
					// searchresultinfo.setGoodsid(rs1.getInt("goodsid"));
					// 商品库位
					searchresultinfo
							.setPosition(rs1.getString("goodsPOSITION"));
					// searchresultinfo.setGoodsdataid(rs1.getInt("goodsdataid"));
					searchresultinfo.setGoods_name(rs1.getString("goods_name"));
					searchresultinfo.setGoods_url(rs1.getString("goods_url"));
					searchresultinfo.setGoods_p_url(rs1
							.getString("goods_p_url"));
					searchresultinfo.setGoods_img_url(rs1
							.getString("goods_img_url"));
					// searchresultinfo.setImg(Utility.ImgMatch(rs1.getString("img")));
					searchresultinfo.setGoods_price(rs1
							.getDouble("productUnitPrice"));
					searchresultinfo.setGoods_p_price(rs1
							.getDouble("productTotalAmount"));
					searchresultinfo.setUsecount(rs1.getInt("usecount"));
					searchresultinfo.setBuycount(rs1.getInt("buycount"));
					info.add(searchresultinfo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return info;
	}

	// 处理规格名称
	private String changetypeName(String _strType) {
		if (_strType == null || _strType.isEmpty()) {
			return "";
		}
		String strType = "";

		for (String strTp : _strType.split(",")) {

			strType = strType + " " + strTp.split("@")[0];
		}

		return strType.replace("undefined", "");
	}

	/**
	 * 验货取消
	 */
	@Override
	public int updatecancelChecktatus(final String orderid, final long goodid,
	                                  final String repState, String warehouseRemark, final String count,
	                                  final int adminId,String barcode,final String cance_inventory_count,String seiUnit,final String weight) {
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement cstmtsqlOrderDetails = null;
		PreparedStatement cstmtsqlOrderInfo = null;
		int rs1 = 0;
		ResultSet rs=null;
		// 更新订单明细状态为到库未验货
		String sqlOrderDetails = "";
		try {
//			cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
//			cstmtsqlOrderDetails.setString(1, orderid);
//			cstmtsqlOrderDetails.setLong(2, goodid);
//			rs1 += cstmtsqlOrderDetails.executeUpdate();
			sqlOrderDetails="select itemqty,weight from id_relationtable  WHERE orderid=? AND goodid=? AND is_delete=0 and is_replenishment=1";
			cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
			cstmtsqlOrderDetails.setString(1, orderid);
			cstmtsqlOrderDetails.setLong(2, goodid);
			rs=cstmtsqlOrderDetails.executeQuery();
			int old_itemqty=0;
			double weights=0.00;
			if(rs.next()){
				old_itemqty=rs.getInt("itemqty");
				weights=rs.getDouble("weight")-Double.parseDouble(weight);
				sqlOrderDetails = "UPDATE id_relationtable SET itemqty=?,goodstatus=1,weight="+weights+" WHERE orderid=? AND goodid=? AND is_delete=0 and is_replenishment=1";
				cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
				int count_=old_itemqty-Integer.valueOf(count);
				if(count_<0){
					count_=0;
				}
				cstmtsqlOrderDetails.setInt(1, count_);
				cstmtsqlOrderDetails.setString(2, orderid);
				cstmtsqlOrderDetails.setLong(3, goodid);
				rs1 += cstmtsqlOrderDetails.executeUpdate();
			}
			sqlOrderDetails = "UPDATE order_details t SET t.checked = 0,t.state=1,purchase_state=3 WHERE t.orderid = ? AND t.goodsid = ?;";
			cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
			cstmtsqlOrderDetails.setString(1, orderid);
			cstmtsqlOrderDetails.setLong(2, goodid);
			rs1 += cstmtsqlOrderDetails.executeUpdate();
			sqlOrderDetails = "UPDATE order_product_source SET purchase_state=4 WHERE orderid=? AND goodsid=?";
			cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
			cstmtsqlOrderDetails.setString(1, orderid);
			cstmtsqlOrderDetails.setLong(2, goodid);
			rs1 += cstmtsqlOrderDetails.executeUpdate();
			//如果该商品验货是有录入库存则做想应的减少
			sqlOrderDetails="SELECT ifnull(od.yourorder,1),od.car_type,od.goods_pid,od.car_urlMD5,ops.goods_p_price,ifnull(gc.seilUnit,1) "
					+ "FROM order_details od "
					+ "inner join order_product_source ops on od.id=ops.od_id "
					+ "left JOIN goods_car gc ON od.goodsid=gc.id "
					+ "WHERE od.orderid=? AND od.goodsid=?";
			cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
			cstmtsqlOrderDetails.setString(1, orderid);
			cstmtsqlOrderDetails.setLong(2, goodid);
			rs=cstmtsqlOrderDetails.executeQuery();
			if(rs.next()){
				String sku=rs.getString("car_type");
				String car_urlMD5=rs.getString("car_urlMD5");
				String goods_pid=rs.getString("goods_pid");
				String goods_p_price=rs.getString("goods_p_price");
				//库存减少的数量
				int inventory_count=Integer.valueOf(cance_inventory_count);
				if(inventory_count>0){
					//库存减少
					sqlOrderDetails="select * from inventory where sku=? and car_urlMD5=? and goods_pid=? and barcode=?";
					cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
					cstmtsqlOrderDetails.setString(1, StringUtils.isStrNull(sku)?"":sku.trim());
					cstmtsqlOrderDetails.setString(2, car_urlMD5);
					cstmtsqlOrderDetails.setString(3, goods_pid);
					cstmtsqlOrderDetails.setString(4, barcode);
					rs=cstmtsqlOrderDetails.executeQuery();
					if(rs.next()){
						int flag=rs.getInt("flag");
						if(flag==1){
							//盘点过
							double amount=rs.getDouble("new_inventory_amount")-(inventory_count/Integer.valueOf(seiUnit))*Double.valueOf(goods_p_price);
							sqlOrderDetails="update inventory set new_remaining=?,can_remaining=?,new_inventory_amount=?,updatetime=now() where sku=? and car_urlMD5=? and goods_pid=? and barcode=?";
							cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
							cstmtsqlOrderDetails.setInt(1, rs.getInt("new_remaining")-inventory_count<0?0:(rs.getInt("new_remaining")-inventory_count));
							cstmtsqlOrderDetails.setInt(2, rs.getInt("can_remaining")-inventory_count<0?0:(rs.getInt("can_remaining")-inventory_count));
							cstmtsqlOrderDetails.setDouble(3, amount);
							cstmtsqlOrderDetails.setString(4, sku.trim());
							cstmtsqlOrderDetails.setString(5, car_urlMD5);
							cstmtsqlOrderDetails.setString(6, goods_pid);
							cstmtsqlOrderDetails.setString(7, barcode);
							cstmtsqlOrderDetails.executeUpdate();
						}else{
							//未盘点
							double amount=rs.getDouble("inventory_amount")-(inventory_count/Integer.valueOf(seiUnit))*Double.valueOf(goods_p_price);
							sqlOrderDetails="update inventory set remaining=?,can_remaining=?,inventory_amount=?,updatetime=now() where sku=? and car_urlMD5=? and goods_pid=? and barcode=?";
							cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
							cstmtsqlOrderDetails.setInt(1, rs.getInt("remaining")-inventory_count<0?0:(rs.getInt("remaining")-inventory_count));
							cstmtsqlOrderDetails.setInt(2, rs.getInt("can_remaining")-inventory_count<0?0:(rs.getInt("can_remaining")-inventory_count));
							cstmtsqlOrderDetails.setDouble(3, amount);
							cstmtsqlOrderDetails.setString(4, StringUtils.isStrNull(sku)?"":sku.trim());
							cstmtsqlOrderDetails.setString(5, car_urlMD5);
							cstmtsqlOrderDetails.setString(6, goods_pid);
							cstmtsqlOrderDetails.setString(7, barcode);
							cstmtsqlOrderDetails.executeUpdate();
						}
					}
					//库存明细相应减少
					sqlOrderDetails="update inventory_details set inventory_acount=inventory_acount-?,invetory_amount=invetory_amount-? where sku=? and car_urlMD5=? and goods_pid=? and inventory_barcode=? order by id desc limit 1";
					cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
					cstmtsqlOrderDetails.setInt(1, inventory_count<0?0:inventory_count);
					cstmtsqlOrderDetails.setDouble(2, Double.valueOf(goods_p_price));
					cstmtsqlOrderDetails.setString(3, StringUtils.isStrNull(sku)?"":sku.trim());
					cstmtsqlOrderDetails.setString(4, car_urlMD5);
					cstmtsqlOrderDetails.setString(5, goods_pid);
					cstmtsqlOrderDetails.setString(6, barcode);
					cstmtsqlOrderDetails.executeUpdate();
					//删除验货时的记录storage_outbound_details
					sqlOrderDetails="update storage_outbound_details set is_delete=1 where goodsid=? and orderid=? order by id desc limit 1";
					cstmtsqlOrderDetails = conn1.prepareStatement(sqlOrderDetails);
					cstmtsqlOrderDetails.setLong(1, goodid);
					cstmtsqlOrderDetails.setString(2, orderid);
					cstmtsqlOrderDetails.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (cstmtsqlOrderDetails != null) {
					cstmtsqlOrderDetails.close();
				}
				if (cstmtsqlOrderInfo != null) {
					cstmtsqlOrderInfo.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBHelper.getInstance().closeConnection(conn1);
		return rs1;
	}

	@Override
	public int delPics(Map<String, String> map) {
		int row=0;
		Connection conn27 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql="";
		try{
			SendMQ sendMQ=new SendMQ();
			sql="update inspection_picture set isdelete=1 where orderid=? and goods_id=? and pic_path=?";
			stmt=conn27.prepareStatement(sql);
			stmt.setString(1,map.get("orderid"));
			stmt.setString(2,map.get("goodsid"));
			stmt.setString(3,map.get("picPath"));
			row=stmt.executeUpdate();
			if(row>0){
				sendMQ.sendMsg(new RunSqlModel("update inspection_picture set isdelete=1 where orderid='"+map.get("orderid")+"' and goods_id='"+map.get("goodsid")+"' and pic_path='"+map.get("picPath")+"'"));
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn27);
			DBHelper.getInstance().closePreparedStatement(stmt);
		}
		return row;
	}

	/**
	 * 获取该订单主单图片
	 *
	 * @param orderid
	 * @return 图片路径
	 */
	@Override
	public String getImgFiles(String orderid) {
		String imgs = "";
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select localImgPath from orderinfo where order_no=?";
		try {
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				imgs = rs.getString("localImgPath");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn1);
		}
		return imgs;
	}

	/**
	 * 删除上传的图片
	 *
	 * @param imgPath
	 * @param orderid
	 * @return
	 */
	@Override
	public int delUploadImage(String imgPath, String orderid) {
		int row = 0;
//        Connection conn = DBHelper.getInstance().getConnection2();
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			SendMQ sendMQ=new SendMQ();
			sql = "select ftpPicPath,localImgPath from orderinfo where order_no=?";
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String localImgPath = rs.getString("localImgPath");
				if (localImgPath != null && !"".equals(localImgPath)
						&& localImgPath.indexOf("&") > -1) {
					StringBuffer bf = new StringBuffer();
					String files[] = localImgPath.split("&");
					for (int i = 0; i < files.length; i++) {
						if (!files[i].equals(imgPath)) {
							bf.append(files[i]).append("&");
						}
					}
					localImgPath = bf.toString().substring(0,
							bf.toString().length() - 1);
				} else if (localImgPath != null && !"".equals(localImgPath)
						&& localImgPath.length() > 1) {
					if (localImgPath.equals(imgPath)) {
						localImgPath = "";
					}
				}
				String ftpPicPath = rs.getString("ftpPicPath");
				if (ftpPicPath != null && !"".equals(ftpPicPath)
						&& ftpPicPath.indexOf("&") > -1) {
					StringBuffer bf = new StringBuffer();
					String files[] = ftpPicPath.split("&");
					for (int i = 0; i < files.length; i++) {
						if (!files[i].contains(imgPath)) {
							bf.append(files[i]).append("&");
						}
					}
					ftpPicPath = bf.toString().substring(0,
							bf.toString().length() - 1);
				} else if (ftpPicPath != null && !"".equals(ftpPicPath)
						&& ftpPicPath.length() > 1) {
					if (ftpPicPath.contains(imgPath)) {
						ftpPicPath = "";
					}
				}
				sql = "update orderinfo set ftpPicPath=?,localImgPath=? where order_no=?";
				stmt = conn1.prepareStatement(sql);
				stmt.setString(1, ftpPicPath);
				stmt.setString(2, localImgPath);
				stmt.setString(3, orderid);
				row = stmt.executeUpdate();
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set ftpPicPath='"+ftpPicPath+"',localImgPath='"+localImgPath+"' where order_no='"+orderid+"'"));
			}
			sendMQ.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
//            DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn1);
		}
		return row;
	}

	/**
	 * 将上传图片路径存入orderinfo中
	 *
	 * @param ftpPath
	 * @param orderid
	 * @param localImgPath
	 * @return
	 */
	@Override
	public int saveImgPathForInfo(String ftpPath, String orderid,
	                              String localImgPath) {
		int row = 0;
//        Connection conn = DBHelper.getInstance().getConnection2();// 仓库不用
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			SendMQ sendMQ=new SendMQ();
			sql = "select ftpPicPath,localImgPath from orderinfo where order_no=?";
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String ftpPicPaths = rs.getString("ftpPicPath");
				String localImgPaths = rs.getString("localImgPath");
				if (ftpPicPaths != null && !"".equals(ftpPicPaths)
						&& ftpPicPaths.length() > 0) {
					ftpPicPaths = ftpPicPaths + "&" + ftpPath;
				} else {
					ftpPicPaths = ftpPath;
				}
				if (localImgPaths != null && !"".equals(localImgPaths)
						&& localImgPaths.length() > 0) {
					localImgPaths = localImgPaths + "&" + localImgPath;
				} else {
					localImgPaths = localImgPath;
				}
				sql = "update orderinfo set ftpPicPath=?,localImgPath=? where order_no=?";
				stmt = conn1.prepareStatement(sql);
				stmt.setString(1, ftpPicPaths);
				stmt.setString(2, localImgPaths);
				stmt.setString(3, orderid);
				row = stmt.executeUpdate();
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set ftpPicPath='"+ftpPicPaths+"',localImgPath='"+localImgPaths+"' where order_no='"+orderid+"'"));
			}
			sendMQ.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn1);
		}
		return row;
	}

	/**
	 * 保存图片路径
	 * path 本地图片路径
	 * ftpPath 图片服务器存放路径
	 */
	@Override
	public int saveImgPath(String path, String orderid, String odid,String ftpPath,boolean success) {
		int row = 0;
//        Connection conn = DBHelper.getInstance().getConnection2();// 仓库不用
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "UPDATE order_details t SET t.picturepath = ? WHERE t.orderid = ? AND t.id = ?";
		try {
			SendMQ sendMQ = new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("UPDATE order_details t SET t.picturepath = '"+(success?ftpPath:path)+"' WHERE t.orderid = '"+orderid+"' AND t.id = '"+odid+"'"));
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, success?ftpPath:path);
			stmt.setString(2, orderid);
			stmt.setString(3, odid);
			row = stmt.executeUpdate();
			sql = "update id_relationtable set picturepath=? where orderid=? and odid=?";
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, path);
			stmt.setString(2, orderid);
			stmt.setString(3, odid);
			row = stmt.executeUpdate();
			sql="insert into inspection_picture(pid,orderid,odid,pic_path,createtime) select goods_pid,orderid,id,'"+ftpPath+"',now() from order_details where orderid=? and id=?";
			stmt = conn1.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, odid);
			row = stmt.executeUpdate();
			sendMQ.sendMsg(new RunSqlModel("insert into inspection_picture(pid,orderid,odid,pic_path,createtime) " +
					"select goods_pid,orderid,id,'"+ftpPath+"',now() from order_details where orderid='"+orderid+"' and id='"+odid+"'"));
			sendMQ.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn1);
		}
		return row;
	}

	/**
	 * 入库取消
	 */
	@Override
	public int updatecanceltatus(final String orderid, final long goodid,
	                             final String repState, String warehouseRemark, final String count,
	                             final int adminId) {
//        Connection conn = DBHelper.getInstance().getConnection2();// 仓库不用
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement cstmtsqlOrderDetails = null;
		PreparedStatement cstmtsqlOrderInfo = null;
		PreparedStatement cstmtsqlIdRelationtable1 = null;
		PreparedStatement cstmtsqlOrderDetails1 = null;
		PreparedStatement cstmtsqlOrderInfo1 = null;
		PreparedStatement cstmtsqlOrderProductSource1 = null;
		PreparedStatement cstmtsqlOrderReplenishment1 = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int rs1 = 0;
		// 直接删除 标识记为已经删除
		String sqlIdRelationtable = "UPDATE id_relationtable t SET t.is_delete = 1 WHERE t.orderid = ? AND t.goodid = ?;";
		// 更新订单明细状态为购买中
		String sqlOrderDetails = "UPDATE order_details t SET t.state = 0,t.shipno=0 WHERE t.orderid = ? AND t.goodsid = ?;";
		// 更新订单状态为购买中
		String sqlOrderInfo = "UPDATE orderinfo t SET t.state = 1 WHERE t.order_no = ?;";
		// 更新货品来源表状态
		String sqlOrderProductSource = "UPDATE order_product_source t SET t.purchase_state = 3 WHERE t.orderid = ? AND t.goodsid = ?;";
		// 更新补货订单状态
		String sqlOrderReplenishment = "UPDATE order_replenishment t SET t.rep_state = 0 WHERE t.orderid = ? AND t.goodsid = ?;";
		// 释放库位
		String queryLocation = "SELECT count(id) as counts FROM id_relationtable WHERE orderid =? AND is_delete = 0";
		String canceLocation = "update storage_location set acount=0 where barcode=(select barcode from id_relationtable where orderid=? limit 1)";
		try {
			SendMQ sendMQ = new SendMQ();
			cstmtsqlIdRelationtable1 = conn1
					.prepareStatement(sqlIdRelationtable);
			cstmtsqlIdRelationtable1.setString(1, orderid);
			cstmtsqlIdRelationtable1.setLong(2, goodid);
			rs1 += cstmtsqlIdRelationtable1.executeUpdate();
			stmt = conn1.prepareStatement(queryLocation);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			if (rs.next() && rs.getInt("counts") == 0) {
				stmt = conn1.prepareStatement(canceLocation);
				stmt.setString(1, orderid);
				stmt.executeUpdate();
			}
			cstmtsqlOrderDetails1 = conn1.prepareStatement(sqlOrderDetails);
			cstmtsqlOrderDetails1.setString(1, orderid);
			cstmtsqlOrderDetails1.setLong(2, goodid);
			rs1 += cstmtsqlOrderDetails1.executeUpdate();
			cstmtsqlOrderInfo1 = conn1.prepareCall(sqlOrderInfo);
			cstmtsqlOrderInfo1.setString(1, orderid);
			rs1 += cstmtsqlOrderInfo1.executeUpdate();
			if ("0".equals(repState)) {
				cstmtsqlOrderReplenishment1 = conn1
						.prepareStatement(sqlOrderReplenishment);
				cstmtsqlOrderReplenishment1.setString(1, orderid);
				cstmtsqlOrderReplenishment1.setLong(2, goodid);
				rs1 += cstmtsqlOrderReplenishment1.executeUpdate();
			} else {
				cstmtsqlOrderProductSource1 = conn1
						.prepareStatement(sqlOrderProductSource);
				cstmtsqlOrderProductSource1.setString(1, orderid);
				cstmtsqlOrderProductSource1.setLong(2, goodid);
				rs1 += cstmtsqlOrderProductSource1.executeUpdate();
			}
			// 更新线上状态
			sqlOrderDetails = "UPDATE order_details t SET t.state = 0 WHERE t.orderid = ? AND t.goodsid = ?;";
			rs1 +=1;
			sendMQ.sendMsg(new RunSqlModel("UPDATE order_details t SET t.state = 0 WHERE t.orderid = '"+orderid+"' AND t.goodsid = '"+goodid+"'"));
			sendMQ.sendMsg(new RunSqlModel("UPDATE orderinfo t SET t.state = 1 WHERE t.order_no = '"+orderid+"'"));
			sendMQ.closeConn();
			rs1 +=1;

			String sql1 = "select goods_p_price from order_product_source where goodsid=? and orderid=?";
			stmt = conn1.prepareStatement(sql1);
			stmt.setInt(1, (int) goodid);
			stmt.setString(2, orderid);
			rs = stmt.executeQuery();
			String buyPrice = "0";
			if (rs.next()) {
				buyPrice = rs.getString("goods_p_price");
			}
			// 取消入库明细记录whj
			sql1 = "select * from goods_inventory where goodsid=?";
			stmt = conn1.prepareStatement(sql1);
			stmt.setLong(1, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sql1 = "insert into goods_inventory (goods_url,inGoodNum,inGoodTime,remark,itemid,barcode,goodsname,sku,itemprice) values(?,?,now(),?,?,?,?,?,?)";
				stmt = conn1.prepareStatement(sql1);
				stmt.setString(1, rs.getString("goods_url"));
				stmt.setInt(2, 0 - Integer.valueOf(count));
				stmt.setString(3, "取消入库");
				stmt.setString(4, rs.getString("itemid"));
				stmt.setString(5, rs.getString("barcode"));
				stmt.setString(6, rs.getString("goodsname"));
				stmt.setString(7, rs.getString("sku"));
				stmt.setString(8, rs.getString("itemprice"));
				int row = stmt.executeUpdate();
				if (row > 0) {
					System.out
							.println("-----------------------记录取消入库明细成功------------------");
					sql1 = "select * from inventory where goods_url=? and sku=? and barcode=?";
					stmt = conn1.prepareStatement(sql1);
					stmt.setString(1, rs.getString("goods_url"));
					stmt.setString(2, rs.getString("sku"));
					stmt.setString(3, rs.getString("barcode"));
					rs = stmt.executeQuery();
					if (rs.next()) {
						sql1 = "update inventory set add_goods=?,remaining=?,noInCount=?,inventory_amount=? where goods_url=? and sku=? and barcode=?";
						stmt = conn1.prepareStatement(sql1);
						stmt.setInt(1,
								rs.getInt("add_goods") - Integer.valueOf(count));
						stmt.setInt(2,
								rs.getInt("remaining") - Integer.valueOf(count));
						stmt.setInt(3,
								rs.getInt("noInCount") + Integer.valueOf(count));
						stmt.setInt(
								4,
								rs.getInt("inventory_amount")
										- Integer.valueOf(buyPrice)
										* Integer.valueOf(count));
						stmt.setString(5, rs.getString("goods_url"));
						stmt.setString(6, rs.getString("sku"));
						stmt.setString(7, rs.getString("barcode"));
						row = stmt.executeUpdate();
						if (row > 0) {
							System.out.println("更新库存表成功");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cstmtsqlOrderDetails != null) {
					cstmtsqlOrderDetails.close();
				}
				if (cstmtsqlOrderInfo != null) {
					cstmtsqlOrderInfo.close();
				}
				if (cstmtsqlIdRelationtable1 != null) {
					cstmtsqlIdRelationtable1.close();
				}
				if (cstmtsqlOrderDetails1 != null) {
					cstmtsqlOrderDetails1.close();
				}
				if (cstmtsqlOrderInfo1 != null) {
					cstmtsqlOrderInfo1.close();
				}
				if (cstmtsqlOrderProductSource1 != null) {
					cstmtsqlOrderProductSource1.close();
				}
				if (cstmtsqlOrderReplenishment1 != null) {
					cstmtsqlOrderReplenishment1.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn1);
		}
		return rs1;
	}

	@Override
	public String qyeryShipno(String orderid, String goodsid) {
		String shipno = "0";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "select shipno from taobao_1688_order_history where importOrderid=? and importGoodsid=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, goodsid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				shipno = rs.getString("shipno");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return shipno;
	}

	public void updateOfflinePurchase(String orderid, String goodsid) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "update order_product_source set offline_purchase=1 where orderid=? and goodsid=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, goodsid);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBHelper.getInstance().closeConnection(conn);
	}

	@Override
	/**
	 * 通过id查询采购账号名称
	 *
	 * @param admuserid
	 *            用户id
	 * @return 采购账号名称 2017-04-14 whj
	 */
	public String queryBuyCount(int admuserid) {
		String name = "";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "select account from tb_1688_accounts where adminid=? and del=1";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, admuserid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				name = rs.getString("account");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return name;
	}

	@Override
	public OrderProductSource queryOps(String orderid, String goodsid) {
		OrderProductSource ops = new OrderProductSource();
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "select ops.confirm_userid,ops.goods_price,ops.buycount,ops.goods_p_url,ops.goods_name,od.car_type,od.car_img from order_product_source ops inner join order_details od on ops.od_id=od.id where ops.orderid=? and ops.goodsid=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, goodsid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ops.setConfirmUserid(rs.getInt("confirm_userid"));
				ops.setCar_type(rs.getString("car_type"));
				ops.setGoodsPrice(rs.getDouble("goods_price"));
				ops.setBuycount(rs.getInt("buycount"));
				ops.setGoodsPUrl(rs.getString("goods_p_url"));
				ops.setGoodsName(rs.getString("goods_name"));
				ops.setCar_img(rs.getString("car_img"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return ops;
	}

	/**
	 * 强制入库
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰 2016-10-09
	 */
	@Override
	public int mandatoryPut(String tbOrderId, String shipno, String itemid,
	                        String barcode, String itemprice, String sku, String userid,
	                        String username, String itemqty, String remark) {
		ResultSet rs = null;
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql = "select id from id_relationtable where tborderid=? and taobaospec=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tbOrderId);
			stmt.setString(2, sku);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sql = "delete from id_relationtable where tborderid=? and taobaospec=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, tbOrderId);
				stmt.setString(2, sku);
				stmt.execute();
			} // else{
			String positon = "";
			sql = "SELECT POSITION  FROM storage_location WHERE barcode=?;";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, barcode);
			rs = stmt.executeQuery();
			if (rs.next()) {
				positon = rs.getString("POSITION");
			}
			sql = "INSERT INTO id_relationtable(barcode,position,userid,username,tborderid,taobaospec,taobaoprice,shipno,itemid,itemqty,warehouse_remark,createtime) values(?,?,?,?,?,?,?,?,?,?,?,now());";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, barcode);
			stmt.setString(2, positon);
			stmt.setString(3, userid);
			stmt.setString(4, username);
			stmt.setString(5, tbOrderId);
			stmt.setString(6, sku);
			stmt.setString(7, itemprice);
			stmt.setString(8, shipno);
			stmt.setString(9, itemid);
			stmt.setString(10, itemqty);
			stmt.setString(11, remark);
			row = stmt.executeUpdate();
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public int updateGoodStatus(final String orderid, final long goodid,
	                            final int status, final String goodurl, final String barcode,
	                            final String userid, final String userName, final String tbOrderId,
	                            final String shipno, final String itemid,
	                            final String warehouseRemark, final String repState,
	                            final int goodCount) {
		Map<String, String> map = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		Connection conn = DBHelper.getInstance().getConnection();
		int res = 0;
		CallableStatement cstmt = null;
		ResultSet rs1 = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = "";
		String remark = "";
		int old_itemqty=0;
		try {
			sql = "select warehouse_remark,itemqty from id_relationtable where orderid=? and goodid=? and is_replenishment=1";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setLong(2, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				remark = rs.getString("warehouse_remark") + warehouseRemark;
				old_itemqty=rs.getInt("itemqty");
			} else {
				remark = warehouseRemark;
			}
			old_itemqty=goodCount+old_itemqty;
			sql = "{call update_idrelationtable_test(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			// 修改本地数据库
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, orderid);
			cstmt.setLong(2, goodid);
			cstmt.setInt(3, status);
			cstmt.setString(4, goodurl);
			cstmt.setString(5, barcode);
			cstmt.setString(6, userid);
			cstmt.setString(7, userName);
			cstmt.setString(8, tbOrderId);
			cstmt.setString(9, shipno);
			cstmt.setString(10, itemid);
			cstmt.setString(11, repState);
			cstmt.setString(12, remark);
			cstmt.setString(13, String.valueOf(old_itemqty));// 入库时数量未0验货时才录入数量
			rs1 = cstmt.executeQuery();
			if (!rs1.wasNull()) {
				while (rs1.next()) {
					res = rs1.getInt("cnt");
				}
			}
			PreparedStatement stmt2 = null;
			try {
				SendMQ sendMQ = new SendMQ();
				sql="update storage_problem_order set flag=1 where shipno='"+shipno+"'";
				stmt2 = conn.prepareStatement(sql);
				stmt2.executeUpdate();
				String sql2 = "select isDropshipOrder from orderinfo where order_no='"
						+ orderid + "'";
				stmt2 = conn.prepareStatement(sql2);
				rs = stmt2.executeQuery();
				if (rs.next()) {
					int isDropshipOrder = rs.getInt("isDropshipOrder");
					if (isDropshipOrder == 1) {
						sql2 = "update order_details set state=1 where orderid='"
								+ orderid + "' and goodsid='" + goodid + "'";
						stmt2 = conn.prepareStatement(sql2);
						stmt2.executeUpdate();
						sendMQ.sendMsg(new RunSqlModel(sql2));
						sql2 = "SELECT DISTINCT COUNT(b.id)-SUM(b.state) as counts FROM order_details o INNER JOIN order_details b ON o.dropshipid=b.dropshipid WHERE o.orderid='"
								+ orderid
								+ "' AND o.goodsid='"
								+ goodid
								+ "' and b.state<2";
						stmt2 = conn.prepareStatement(sql2);
						rs = stmt2.executeQuery();
						if (rs.next() && rs.getInt("counts") == 0) {
							sql2 = "update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"
									+ orderid
									+ "' and goodsid='"
									+ goodid
									+ "')";
							stmt2 = conn.prepareStatement(sql2);
							stmt2.executeUpdate();
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
						// 判断主单下所有的子单是否到库
						sql2 = "SELECT  COUNT(orderid)*2-SUM(state) as countss FROM dropshiporder WHERE parent_order_no='"
								+ orderid + "'";
						stmt2 = conn.prepareStatement(sql2);
						rs = stmt2.executeQuery();
						if (rs.next() && rs.getInt("countss") == 0) {
							sql2 = "update orderinfo set state=2 where order_no='"
									+ orderid + "'";
							stmt2 = conn.prepareStatement(sql2);
							stmt2.executeUpdate();
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
					} else {
						// 非dropshi订单
						sql2 = "update order_details set state=1 where orderid='"
								+ orderid + "' and goodsid='" + goodid + "'";
						stmt2 = conn.prepareStatement(sql2);
						stmt2.executeUpdate();
						sendMQ.sendMsg(new RunSqlModel(sql2));
						sql2 = "SELECT (COUNT(id) - SUM(state)) as orderdetails_count FROM order_details WHERE state<2 AND orderid='"
								+ orderid + "'";
						stmt2 = conn.prepareStatement(sql2);
						rs = stmt2.executeQuery();
						if (rs.next() && rs.getInt("orderdetails_count") == 0) {
							sql2 = "UPDATE orderinfo SET state=2 WHERE order_no='"
									+ orderid + "'";
							stmt2 = conn.prepareStatement(sql2);
							stmt2.executeUpdate();
							sendMQ.sendMsg(new RunSqlModel(sql2));
						}
					}
				}
				sendMQ.closeConn();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("====================================================更新【"
						+ orderid
						+ "】订单的【goodid】入库状态错误===============================");
			} finally {
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				if (status != 1) {
					System.out.println("--------------------开始记录商品入库--------------------");
					String sql1 = "SELECT od.goods_pid,ops.buycount AS buycount,ops.goods_name as goods_name,ops.goods_p_price AS goods_p_price,od.car_type AS car_type,ops.goods_p_url AS goods_p_url,od.goodscatid AS cid,od.car_img AS car_img FROM order_product_source ops INNER JOIN order_details od ON ops.orderid=od.orderid and od.goodsid=ops.goodsid  WHERE ops.goodsid=? AND ops.orderid=?";
					stmt = conn.prepareStatement(sql1);
					stmt.setInt(1, (int) goodid);
					stmt.setString(2, orderid);
					rs = stmt.executeQuery();
					String goodName = "";
					String car_type = "";
					String goods_p_price = "";
					String goods_p_url = "";
					String goodscatid = "0";
					String cid = "";
					String car_img = "";
					String goods_pid="";//销售商品的编号
					int buycount = 0;
					if (rs.next()) {
						buycount = rs.getInt("buycount");//采购数量
						goodName = rs.getString("goods_name");//商品销售名称
						car_type = rs.getString("car_type");//商品规格
						goods_p_price = rs.getString("goods_p_price");//采购价格
						goods_p_url = rs.getString("goods_p_url");//采购链接
//						goodscatid = rs.getString("category");//商品类别
						cid = rs.getString("cid");//类别ID
						car_img = rs.getString("car_img");//商品图片
						goods_pid=rs.getString("goods_pid");
					}
					// 新增商品入库明细记录
					sql1 = "insert into goods_inventory_whj (orderid,goodsid,createtime,counts,goods_pid,admName,barcode,type,remark) " +
							"values('"+orderid+"',"+goodid+",now(),'"+goodCount+"','"+goods_pid+"','"+userName+"','"+barcode+"','1','"+remark+"')";
					stmt = conn.prepareStatement(sql1);
					int row = stmt.executeUpdate();
					if (row > 0) {
						System.out.println("-----------------------结束记录商品入库------------------");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		System.out.println(System.currentTimeMillis() - start);
		return res;
	}

	@Override
	public int pictureagain(String orderid, long goodid) {
		String sql = "update tmp_table set isinsert=1 where orderid=? and goodid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setLong(2, goodid);
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	/**
	 * 商品多余库存存入库存表中
	 * @param barcode 库存库位
	 * @param inventory_count  库存数量
	 * @param orderid  订单号
	 * @param odid  商品号
	 * @return
	 */
	@Override
	public int addInventory(String barcode, String inventory_count,
	                        String orderid, String odid,String storage_count,String when_count,String admName,String unit) {
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn28 = DBHelper.getInstance().getConnection8();
		Connection aliconn =DBHelper.getInstance().getConnection5();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="";
		int row=0;
		int in_id=0000;
		try{
			//查询库存原商品信息
			sql="select od.id,od.goods_pid,ltrim(od.car_type) as car_type,od.car_urlMD5,od.goodsname,od.goodscatid,od.car_img,ops.goods_p_price,"
					+ "ops.goods_p_url from order_details od inner join order_product_source ops on od.id=ops.od_id and od.orderid=ops.orderid where od.orderid=? and od.id=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, odid);
			rs=stmt.executeQuery();
			if(rs.next()){
				String goods_pid=rs.getString("goods_pid");
				String sku=rs.getString("car_type");
				String car_urlMD5=rs.getString("car_urlMD5");
				String goodsname=rs.getString("goodsname");
				String goodscatid=rs.getString("goodscatid");
				String car_img=rs.getString("car_img");
				String goods_p_price=rs.getString("goods_p_price");
				String goods_p_url=rs.getString("goods_p_url");
				int od_id=rs.getInt("id");
				//当次库存金额
				double amount=Double.valueOf(goods_p_price)*(Integer.valueOf(inventory_count)/Integer.valueOf(unit));
				BigDecimal b=new BigDecimal(amount);
				amount = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				String category="";
				sql="select * from inventory where goods_pid=? and sku=? and barcode=? and car_urlMD5=?";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, goods_pid);
				stmt.setString(2, StringUtils.isStrNull(sku)?"":sku);
				stmt.setString(3, barcode);
				stmt.setString(4, car_urlMD5);
				rs=stmt.executeQuery();
				if(rs.next()){
					int flag=rs.getInt("flag");
					int new_remaining=rs.getInt("new_remaining");
					int can_remaining=rs.getInt("can_remaining");
					double new_inventory_amount=rs.getDouble("new_inventory_amount");
					int remaining=rs.getInt("remaining");
					double inventory_amount=rs.getDouble("inventory_amount");
					String old_goods_p_url=rs.getString("goods_p_url");
					String old_buy_price=rs.getString("goods_p_price");
					in_id=rs.getInt("id");
					old_buy_price=old_buy_price.contains(goods_p_price)?old_buy_price:old_buy_price+","+goods_p_price;
					if(flag==1){
						//已经盘点过库存
						sql="update inventory set goods_p_price=?,new_remaining=?,can_remaining=?,new_inventory_amount=?,updatetime=now(),goods_p_url=? where goods_pid=? and sku=? and barcode=? and car_urlMD5=?";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, old_buy_price);
						stmt.setInt(2, new_remaining+Integer.valueOf(inventory_count));
						stmt.setInt(3, can_remaining+Integer.valueOf(inventory_count));
						stmt.setDouble(4, new_inventory_amount+amount);
						stmt.setString(5, StringUtils.isStrNull(old_goods_p_url)?goods_p_url:(old_goods_p_url+","+goods_p_url));
						stmt.setString(6, goods_pid);
						stmt.setString(7, StringUtils.isStrNull(sku)?"":sku);
						stmt.setString(8, barcode);
						stmt.setString(9, car_urlMD5);
					}else{
						//没有盘点过库存
						sql="update inventory set goods_p_price=?,remaining=?,can_remaining=?,inventory_amount=?,updatetime=now(),goods_p_url=? where goods_pid=? and sku=? and barcode=? and car_urlMD5=?";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, old_buy_price);
						stmt.setInt(2, remaining+Integer.valueOf(inventory_count));
						stmt.setInt(3, can_remaining+Integer.valueOf(inventory_count));
						stmt.setDouble(4, inventory_amount+amount);
						stmt.setString(5, StringUtils.isStrNull(old_goods_p_url)?goods_p_url:(old_goods_p_url+","+goods_p_url));
						stmt.setString(6, goods_pid);
						stmt.setString(7, StringUtils.isStrNull(sku)?"":sku);
						stmt.setString(8, barcode);
						stmt.setString(9, car_urlMD5);
					}
					row=stmt.executeUpdate();
				}else{
					//新增库存记录
					sql = "insert into inventory(goods_url,remaining,can_remaining,good_name,sku,barcode,goodscatid,inventory_amount,car_img,goods_pid,car_urlMD5,createtime,goods_p_price,goods_p_url) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, "https://www.import-express.com/spider/detail?&source="+car_urlMD5+"&item="+goods_pid+"");
					stmt.setInt(2, Integer.valueOf(inventory_count));
					stmt.setInt(3,Integer.valueOf(inventory_count));
					stmt.setString(4, goodsname);
					stmt.setString(5, StringUtils.isStrNull(sku)?"":sku);
					stmt.setString(6, barcode);
					stmt.setString(7, category);
					stmt.setDouble(8, amount);
					stmt.setString(9, car_img);
					stmt.setString(10, goods_pid);
					stmt.setString(11, car_urlMD5);
					stmt.setString(12, goods_p_price);
					stmt.setString(13, goods_p_url);
					row=stmt.executeUpdate();
					sql="select id from inventory where goods_pid=? and sku=? and barcode=? and car_urlMD5=?";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, goods_pid);
					stmt.setString(2, StringUtils.isStrNull(sku)?"":sku);
					stmt.setString(3, barcode);
					stmt.setString(4, car_urlMD5);
					rs=stmt.executeQuery();
					if(rs.next()){
						in_id=rs.getInt("id");
					}
				}
				//库存关联入库记录 插入storage_outbound_details记录
				sql="insert into storage_outbound_details (orderid,odid,in_id,storage_count,type,createtime,admName,when_count,add_inventory) values("
						+ "'"+orderid+"','"+odid+"',"+in_id+","+storage_count+",1,now(),'"+admName+"',"+when_count+","+inventory_count+")";
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate();
				//如果采用打分为1,2分时则不更新线上库存标识
				sql="select quality from supplier_product where goods_pid='"+goods_pid+"'";
				stmt=aliconn.prepareStatement(sql);
				rs=stmt.executeQuery();
				if(!rs.next() || rs.getDouble("quality")>2){
					// 同步1、更新预上线表  2、线上产品表  3.后台产品表中的库存标识
					//本地
					sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
//                    //预上线表
//                    sql="update custom_benchmark_ready_newest set is_stock_flag=1 where pid='"+goods_pid+"'";
//                    stmt = conn28.prepareStatement(sql);
//                    stmt.executeUpdate();
//                    //线上表
//                    sql="update custom_benchmark_ready set is_stock_flag=1 where pid='"+goods_pid+"'";
//                    SendMQ sendMQ = new SendMQ();
//                    sendMQ.sendMsg(new RunSqlModel(sql));
//                    sendMQ.closeConn();
					GoodsInfoUpdateOnlineUtil.stockToOnlineByMongoDB(goods_pid,"1");
				}
				//记录库存存入记录，用来统计当月库存金额
				sql="insert into inventory_details (createtime,od_id,inventory_acount,inventory_price,invetory_amount,inventory_barcode,goods_pid,sku,car_urlMD5) values(now(),?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, od_id);
				stmt.setString(2, inventory_count);
				stmt.setString(3, goods_p_price);
				stmt.setDouble(4, amount);
				stmt.setString(5, barcode);
				stmt.setString(6, goods_pid);
				stmt.setString(7, StringUtils.isStrNull(sku)?"":sku);
				stmt.setString(8, car_urlMD5);
				stmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn28);
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(aliconn);
		}
		return row;
	}

	// 根据库位条形码获得仓库位置
	@Override
	public String getPosition(String id) {
		String str = "";
		String sql = "select position from storage_location where barcode=?";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				str = rs.getString("position");
				// System.out.println(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return str;
	}

	@Override
	public List<Map<String, String>> getorderPending() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet prs = null;
		String sql="";
		try{
			sql="SELECT GROUP_CONCAT(\"'\",p.orderid,\"'\") as orderids FROM payment p inner join orderinfo oi on p.orderid=oi.order_no WHERE p.id IN(SELECT MAX(id) FROM payment GROUP BY orderid) AND p.paystatus=0 and oi.state=0   ORDER BY p.id DESC";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next() && StringUtil.isNotBlank(rs.getString("orderids"))){
				String orderids=rs.getString("orderids");
				sql="SELECT  o.order_no,o.mode_transport,o.orderid AS id,o.delivery_time ,o.currency,o.user_id,IFNULL(aru.admName,'') adminname," +
						"IFNULL(aru.adminid,-1) adminid,cancel_obj,o.actual_ffreight,o.product_cost,o.state,user.name username,user.email email, " +
						"''AS buyer, SPACE(2000) AS countrys, '' address, '' address2,'' statename,'' phoneNumber,'' street,'' zipcode, 1 paystatus, " +
						"o.packag_style AS changenum ,(select createtime from payment where orderid=o.order_no limit 1) as paytime," +
						"(CASE WHEN pay_price_three='-999' THEN 3 WHEN IFNULL(pay_price_three,0)>0 THEN 2 ELSE 0 END ) paytype," +
						"(CASE (IFNULL(pay_price,0)-IFNULL(pay_price_three,0))>0 WHEN TRUE THEN (IFNULL(pay_price,0)-IFNULL(pay_price_three,0)) ELSE 0 END ) service_fee ," +
						"DATE_FORMAT(IFNULL(orderpaytime,o.create_time),'%Y-%c-%d %H:%i:%s') AS createtime ,o.orderRemark,pay_price_three,isDropshipOrder, SPACE(200) AS preferential_applications," +
						"SPACE(200) AS purchase,SPACE(200) AS deliver,SPACE(200) AS message_read,SPACE(2000) AS orderremarks,SPACE(200) AS checked,SPACE(200) AS countOd," +
						"SPACE(200) AS no_checked,SPACE(200) AS details_number,SPACE(200) AS number_of_warehouses,SPACE(200) AS purchase_number,SPACE(200) AS oc_c,SPACE(200) AS order_count " +
						"FROM orderinfo o INNER JOIN USER ON o.user_id=user.id LEFT JOIN admin_r_user aru ON aru.userid=o.user_id where o.order_no in ("+orderids+")";
				stmt=conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				while (rs != null && rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("order_no", rs.getString("order_no"));
					map.put("mode_transport", rs.getString("mode_transport"));
					map.put("paytime", rs.getString("paytime")==null || "null".equals(rs.getString("paytime"))?"-":rs.getString("paytime"));
					map.put("id", rs.getString("id"));
					map.put("delivery_time", rs.getString("delivery_time"));
					map.put("currency", rs.getString("currency"));
					map.put("user_id", rs.getString("user_id"));
					map.put("adminname", rs.getString("adminname"));
					map.put("adminid", rs.getString("adminid"));
					map.put("cancel_obj", rs.getString("cancel_obj"));
					map.put("actual_ffreight", rs.getString("actual_ffreight"));
					map.put("product_cost", rs.getString("product_cost"));
					map.put("state", rs.getString("state"));
					map.put("username", rs.getString("username"));
					map.put("email", rs.getString("email"));
					map.put("buyer", rs.getString("buyer"));
					map.put("countrys", rs.getString("countrys"));
					map.put("address", rs.getString("address"));
					map.put("address2", rs.getString("address2"));
					map.put("statename", rs.getString("statename"));
					map.put("phoneNumber", rs.getString("phoneNumber"));
					map.put("street", rs.getString("street"));
					map.put("zipcode", rs.getString("zipcode"));
					map.put("paystatus", rs.getString("paystatus"));
					map.put("changenum", rs.getString("changenum"));
					map.put("paytype", rs.getString("paytype"));
					map.put("service_fee", rs.getString("service_fee"));
					map.put("createtime", rs.getString("createtime"));
					map.put("orderRemark", rs.getString("orderRemark"));
					map.put("pay_price_three", rs.getString("pay_price_three"));
					map.put("isDropshipOrder", rs.getString("isDropshipOrder"));
					map.put("preferential_applications",
							rs.getString("preferential_applications"));
					map.put("purchase", rs.getString("purchase"));
					map.put("deliver", rs.getString("deliver"));
					map.put("message_read", rs.getString("message_read"));
					map.put("orderremarks", rs.getString("orderremarks"));
					map.put("allFreight", "0.00");
					map.put("checked", rs.getString("checked"));
					map.put("countOd", rs.getString("countOd"));
					map.put("details_number", rs.getString("details_number"));
					map.put("number_of_warehouses",
							rs.getString("number_of_warehouses"));
					map.put("purchase_number", rs.getString("purchase_number"));
					map.put("oc_c", rs.getString("oc_c"));
					map.put("order_count", rs.getString("order_count"));
					map.put("no_checked", rs.getString("no_checked"));
					sql="SELECT GROUP_CONCAT(paytype) as paytype,if(paystatus=2,4,paystatus) as paystatus FROM payment WHERE orderid='"+rs.getString("order_no")+"' and (paystatus=2 or paystatus=0) order by id desc limit 1";
					pstmt=conn.prepareStatement(sql);
					prs=pstmt.executeQuery();
					String tp="支付错误";
					String paystatus="";
					if(prs.next()){
						String paytype=prs.getString("paytype");
						if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
							StringBuilder types=new StringBuilder();
							String [] t=paytype.split(",");
							for(int i=0;i<t.length;i++){
								types.append(StringUtil.getPayType(t[i]));
								if(i != t.length-1){
									types.append(",");
								}
							}
							tp=types.toString();
						}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
							tp=StringUtil.getPayType(paytype);
						}
						paystatus=prs.getString("paystatus");
					}
					map.put("paytypes",tp);
					map.put("paytype",paystatus);
					list.add(map);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closeResultSet(prs);
			DBHelper.getInstance().closePreparedStatement(pstmt);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public List<Map<String, String>> getOrderManagementQuery(int userID,
	                                                         int state, String startdate, String enddate, String email,
	                                                         String orderno, int startpage, int page, int admuserid, int buyid,
	                                                         int showUnpaid, String type, int status,String paymentid) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		String sql = "call order_management_query(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			CallableStatement cst = conn.prepareCall(sql);
			cst.setString(1, StringUtils.isStrNull(orderno)?"":orderno);
			cst.setInt(2, state);
			cst.setString(3, StringUtils.isStrNull(type)?"":type);
			cst.setInt(4, userID);
			cst.setInt(5, admuserid);
			cst.setInt(6, showUnpaid);
			cst.setInt(7, buyid);
			cst.setString(8,StringUtils.isStrNull(startdate)?"":startdate);
			cst.setString(9,StringUtils.isStrNull(enddate)?"":enddate);
			cst.setString(10,StringUtils.isStrNull(email)?"":email);
			cst.setInt(11, status);
			cst.setInt(12, startpage);
			cst.setInt(13, page);
			cst.setString(14, paymentid);
			System.out.println("call order_management_query('"+(StringUtils.isStrNull(orderno)?"":orderno)+"','"+state+"','"+(StringUtils.isStrNull(type)?"":type)+"'"
					+ ","+userID+","+admuserid+","+showUnpaid+","+buyid+",'"+(StringUtils.isStrNull(startdate)?"":startdate)+"','"+(StringUtils.isStrNull(enddate)?"":enddate)+"'"
					+ ",'"+(StringUtils.isStrNull(email)?"":email)+"',"+status+","+startpage+","+page+",'"+paymentid+"')");
			boolean hadResults = cst.execute();
			if (hadResults) {
				rs = cst.getResultSet();
				while (rs != null && rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("order_no", rs.getString("order_no"));
					map.put("mode_transport", rs.getString("mode_transport"));
					map.put("paytime", rs.getString("paytime")==null || "null".equals(rs.getString("paytime"))?"-":rs.getString("paytime"));
					map.put("id", rs.getString("id"));
					map.put("delivery_time", rs.getString("delivery_time"));
					map.put("currency", rs.getString("currency"));
					map.put("user_id", rs.getString("user_id"));
					map.put("adminname", rs.getString("adminname"));
					map.put("adminid", rs.getString("adminid"));
					map.put("cancel_obj", rs.getString("cancel_obj"));
					map.put("actual_ffreight", rs.getString("actual_ffreight"));
					map.put("product_cost", rs.getString("product_cost"));
					map.put("state", rs.getString("state"));
					map.put("username", rs.getString("username"));
					map.put("email", rs.getString("email"));
					map.put("buyer", rs.getString("buyer"));
					map.put("countrys", rs.getString("countrys"));
					map.put("address", rs.getString("address"));
					map.put("address2", rs.getString("address2"));
					map.put("statename", rs.getString("statename"));
					map.put("phoneNumber", rs.getString("phoneNumber"));
					map.put("street", rs.getString("street"));
					map.put("zipcode", rs.getString("zipcode"));
					map.put("paystatus", rs.getString("paystatus"));
					map.put("changenum", rs.getString("changenum"));
					map.put("paytype", rs.getString("paytype"));
					map.put("service_fee", rs.getString("service_fee"));
					map.put("createtime", rs.getString("createtime"));
					map.put("orderRemark", rs.getString("orderRemark"));
					map.put("pay_price_three", rs.getString("pay_price_three"));
					map.put("isDropshipOrder", rs.getString("isDropshipOrder"));
					map.put("preferential_applications",
							rs.getString("preferential_applications"));
					map.put("purchase", rs.getString("purchase"));
					map.put("deliver", rs.getString("deliver"));
					map.put("message_read", rs.getString("message_read"));
					map.put("orderremarks", rs.getString("orderremarks"));
					map.put("allFreight", rs.getString("allFreight"));
					map.put("checked", rs.getString("checked"));
					map.put("countOd", rs.getString("countOd"));
					map.put("details_number", rs.getString("details_number"));
					map.put("number_of_warehouses",
							rs.getString("number_of_warehouses"));
					map.put("purchase_number", rs.getString("purchase_number"));
					map.put("oc_c", rs.getString("oc_c"));
					map.put("order_count", rs.getString("order_count"));
					map.put("no_checked", rs.getString("no_checked"));
					String paytype=rs.getString("paytypes");
					String tp="支付错误";
					if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")>-1){
						StringBuilder types=new StringBuilder();
						String [] t=paytype.split(",");
						for(int i=0;i<t.length;i++){
							types.append(StringUtil.getPayType(t[i]));
							if(i != t.length-1){
								types.append(",");
							}
						}
						tp=types.toString();
					}else if(StringUtil.isNotBlank(paytype) && paytype.indexOf(",")<=-1){
						tp=StringUtil.getPayType(paytype);
					}
					map.put("ref_orderid",rs.getString("ref_orderid"));
					map.put("paytypes",tp);
					list.add(map);
				}
			}
			cst.close();
			cst = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}


	// 根据商品id查询位置
	@Override
	public String getPositionByGoodId(String goodId) {
		String str = "";
		String sql = "select position from id_relationtable where  goodid=? and is_delete <> 1  LIMIT 1";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				str = rs.getString("position");
				// System.out.println(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return str;
	}

	// 根据订单id查询所有位置
	@Override
	public String getPositionByOrderId(String orderId) {
		String str = "";
		String sql = "select replace(position, ',,', ',') position from ( select GROUP_CONCAT(position) position from ( select distinct position from id_relationtable ir where  orderid=?  and ir.is_delete <> 1) a )b";
		ResultSet rs = null;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				str += rs.getString("position") + ",";
				// System.out.println(str);
			}
			if (str.length() > 1) {
				str = str.substring(0, str.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return str;
	}

	@Override
	public int updatePicturePath(String orderid, String goodid,
	                             String picturePath) {
		String sql = "update id_relationtable ir set picturepath=?, createtime=now() where orderid=? and goodid=?  and ir.is_delete <> 1";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, picturePath);
			stmt.setString(2, orderid);
			stmt.setString(3, goodid);
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public List<Map<String, String>> getPicturePathList(String uid) {
		String sql = "select t1.orderid, goodid, picturepath, goods_name from id_relationtable t1, order_product_source t2 where goodsid = goodid and t1.userid = ?  and t1.is_delete <> 1";
		// sql += " and DATE_FORMAT(createtime,'%Y-%d-%m') =
		// DATE_FORMAT(NOW(),'%Y-%d-%m')";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", rs.getString("t1.orderid"));
				map.put("goodid", rs.getString("goodid"));
				map.put("picturepath", rs.getString("picturepath"));
				map.put("goods_name", rs.getString("goods_name"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 查询一周内有问题订单号
	 */
	@Override
	public List<Map<String, String>> getIdRelationtable(int startNum, int endNum) {
		// String sql = "SELECT * FROM id_relationtable t WHERE t.goodstatus <>
		// 1 AND t.createtime >= CURDATE()-7 AND t.createtime <CURDATE()+1";
		String sql = "SELECT DISTINCT  ir.goodstatus,ir.warehouse_remark,of.user_id,ir.orderid,ir.position,ir.createtime,ir.goodid,ir.picturepath FROM id_relationtable ir "
				+ "LEFT JOIN orderinfo of ON of.order_no = ir.orderid "
				+ "WHERE 1=1 AND ir.is_delete != 1 AND ir.goodstatus <> 1 AND ir.createtime >= DATE_SUB(CURDATE(),INTERVAL 7 DAY) AND ir.createtime < DATE_ADD(CURDATE(),INTERVAL 1 DAY) "
				+ "ORDER BY ir.createtime LIMIT ?,?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, startNum);
			stmt.setInt(2, endNum);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", rs.getString("orderid"));
				map.put("goodstatus", rs.getString("goodstatus"));
				map.put("warehouseRemark", rs.getString("warehouse_remark"));
				map.put("goodid", rs.getString("goodid"));
				map.put("picturepath", rs.getString("picturepath"));
				map.put("userId", rs.getString("user_id"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 获取最近没有验货没有拍照商品
	 */
	@Override
	public List<OrderDetailsBean> getNoPictureGoods(String orderid,String goodsid,int page) {
		List<OrderDetailsBean> list=new ArrayList<OrderDetailsBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int count=0;
		try{
			String sql1="select count(od.id) as counts from order_details od inner join orderinfo o on od.orderid=o.order_no "
					+ "inner join admin_r_user a on a.userid=o.user_id "
					+ "where (od.picturepath is null or od.picturepath='') and od.checked=1  and o.state>0 and o.state<6 "
					+ "and a.adminid<>'18' and od.state<2 and od.id>222364 and od.goodsid<>'1400' ";
			String sql="select od.orderid,od.goodsid,od.car_url,od.car_img,od.goodsname from order_details od inner join orderinfo o on od.orderid=o.order_no "
					+ "inner join admin_r_user a on a.userid=o.user_id "
					+ "where (od.picturepath is null or od.picturepath='') and od.checked=1  and o.state>0 and o.state<6 "
					+ "and a.adminid<>'18' and od.state<2 and od.id>222364 and od.goodsid<>'1400' ";

			if(!StringUtils.isStrNull(orderid)){
				sql+=" and od.orderid='"+orderid+"'";
				sql1+=" and od.orderid='"+orderid+"'";
			}
			if(!StringUtils.isStrNull(goodsid)){
				sql+=" and od.goodsid='"+goodsid+"'";
				sql1+=" and od.goodsid='"+goodsid+"'";
			}
			stmt=conn.prepareStatement(sql1);
			rs=stmt.executeQuery();
			if(rs.next()){
				count=rs.getInt("counts");
			}
			sql+="order by od.id desc limit "+page+",24";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				OrderDetailsBean o=new OrderDetailsBean();
				o.setOrderid(rs.getString("orderid"));
				o.setGoodsid(rs.getInt("goodsid"));
				o.setCar_img(rs.getString("car_img"));
				o.setGoodsname(rs.getString("goodsname"));
				o.setCar_url(rs.getString("car_url"));
				o.setBuycount(count);
				list.add(o);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	/**
	 * 根据订单号和商品编号关联入库
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰 2016-10-10
	 */
	public int updateMatch(String orderid, String goodsid, String sku,
	                       String itemid, String tborderid, int state) {
		int row = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM order_product_source WHERE orderid=? AND goodsid=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, goodsid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sql = "update order_details set checked=1 WHERE orderid=? AND goodsid=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, orderid);
				stmt.setString(2, goodsid);
				stmt.executeUpdate();
				sql = "update id_relationtable set orderid=?,goodid=?,goodurl=?,goodstatus=? where tborderid=? and itemid=? and taobaospec=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, rs.getString("orderid"));
				stmt.setInt(2, rs.getInt("goodsid"));
				stmt.setString(3, rs.getString("goods_url"));
				stmt.setInt(4, 1);
				stmt.setString(5, tborderid);
				stmt.setString(6, itemid);
				stmt.setString(7, sku);
				row = stmt.executeUpdate();
				if (row > 0) {
					row = this.updateGoodStatus(rs.getString("orderid"),
							Long.valueOf(rs.getInt("goodsid")), state,
							rs.getString("goods_url"), "",
							String.valueOf(rs.getInt("userid")), "", tborderid,
							"", itemid, "", "1", 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	/**
	 * 查询20条快递单号匹配不了货源的淘宝信息商品
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author 王宏杰
	 */
	public List getNoMatchGoods(String expresstrackid, String username) {
		List allList = new ArrayList();
		// 本地化链接
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			String sql1 = "select distinct t.shipno,i.taobaospec from taobao_1688_order_history t INNER JOIN id_relationtable i on t.itemid=i.itemid where t.sku=i.taobaospec and t.orderid=i.tborderid and i.goodid is null";
			if (null != expresstrackid && !expresstrackid.equals("")) {
				sql1 += " and t.shipno='" + expresstrackid + "'";
			}
			if (null != username && !username.equals("")) {
				sql1 += " and t.username='" + username + "' ";
			}
			sql1 += " order by t.id desc LIMIT 20";
			stmt1 = conn.prepareStatement(sql1);
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				List<Tb1688OrderHistory> orderList = new ArrayList<Tb1688OrderHistory>();
				String sql = "SELECT t.id,t.tbOr1688,t.orderid,t.orderdate,t.seller,t.totalprice,t.orderstatus,t.itemname,t.itemid,t.itemprice,"
						+ "t.itemqty,t.sku,t.shipno,t.shipper,t.shipstatus,t.itemurl,t.imgurl,t.username,t.creatTime FROM  taobao_1688_order_history t where t.shipno=? and t.sku=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, rs1.getString("shipno"));
				stmt.setString(2, rs1.getString("taobaospec"));
				rs = stmt.executeQuery();
				if (!rs.wasNull()) {
					while (rs.next()) {
						Tb1688OrderHistory order = new Tb1688OrderHistory();
						order.setId(rs.getInt(1));
						order.setTbOr1688(rs.getInt(2));
						order.setOrderid(rs.getString(3));
						order.setOrderdate(rs.getDate(4));
						order.setSeller(rs.getString(5));
						order.setTotalprice(rs.getString(6));
						order.setOrderstatus(rs.getString(7));
						order.setItemname(rs.getString(8));
						order.setItemid(rs.getString(9));
						order.setItemprice(rs.getDouble(10));
						order.setItemqty(rs.getString(11));
						order.setSku(rs.getString(12));
						order.setShipno(rs.getString(13));
						order.setShipper(rs.getString(14));
						order.setShipstatus(rs.getString(15));
						order.setItemurl(rs.getString(16));
						order.setImgurl(rs.getString(17));
						order.setUsername(rs.getString(18));
						order.setCreatTime(rs.getDate(19));
						orderList.add(order);
					}
				}
				allList.add(orderList);
			}

			// String sql = "SELECT
			// t.id,t.tbOr1688,t.orderid,t.orderdate,t.seller,t.totalprice,t.orderstatus,t.itemname,t.itemid,t.itemprice,"
			// +
			// "t.itemqty,t.sku,t.shipno,t.shipper,t.shipstatus,t.itemurl,t.imgurl,t.username,t.creatTime
			// FROM taobao_1688_order_history t "
			// + "LEFT JOIN order_product_source o ON t.itemid=o.tb_1688_itemid
			// WHERE t.shipno=? and t.shipno<>'' AND t.paytreasureid IS NOT NULL
			// AND o.tb_1688_itemid "
			// + "IS NULL OR o.tb_1688_itemid ='' AND t.itemid<>'' LIMIT 20";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return allList;
	}

	@Override
	public void insertScanLog(String shipno, String admName) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			String sql = "insert into scan_parcel_log_info (shipno,username,createtime) select '"
					+ shipno
					+ "','"
					+ admName
					+ "',now() from dual where not exists (select * from scan_parcel_log_info where shipno='"
					+ shipno + "')";
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	/**
	 * 根据快递单号到taobao_1688_order_history表中查找淘宝数据信息
	 */
	public List<Tb1688OrderHistory> getGoodsData(String expresstrackid) {
		String sql = "select id,tbOr1688,orderid,orderdate,seller,totalprice,orderstatus,itemname,itemid,itemprice,itemqty,sku,shipno,shipper"
				+ ",shipstatus,itemurl,imgurl,username,creatTime from taobao_1688_order_history where shipno=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double prices=0.00;
		List<Tb1688OrderHistory> orderList = new ArrayList<Tb1688OrderHistory>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, expresstrackid);
			rs = stmt.executeQuery();
			if (!rs.wasNull()) {
				while (rs.next()) {
					Tb1688OrderHistory order = new Tb1688OrderHistory();
					order.setId(rs.getInt(1));
					order.setTbOr1688(rs.getInt(2));
					if(rs.getInt(2)==2){
						prices+=Double.valueOf(rs.getString(6));
					}
					order.setOrderid(rs.getString(3));
					order.setOrderdate(rs.getDate(4));
					order.setSeller(rs.getString(5));
//					order.setTotalprice(rs.getString(6));
					order.setOrderstatus(rs.getString(7));
					order.setItemname(rs.getString(8));
					order.setItemid(rs.getString(9));
					order.setItemprice(rs.getDouble(10));
					order.setItemqty(rs.getString(11));
					order.setSku(rs.getString(12));
					order.setShipno(rs.getString(13));
					order.setShipper(rs.getString(14));
					order.setShipstatus(rs.getString(15));
					order.setItemurl(rs.getString(16));
					order.setImgurl(rs.getString(17).replace(".80x80","").replace(".60x60",""));
					order.setUsername(rs.getString(18));
					order.setCreatTime(rs.getDate(19));
					orderList.add(order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		if(orderList.size()>0){
			BigDecimal bg = new BigDecimal(prices);
			double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			orderList.get(0).setTotalprice(String.valueOf(f1));
		}
		return orderList;
	}

	// 亚马逊商品取消入库
	@Override
	public int cancelAmazonstatus(String orderid, String itemid, long goodid,
	                              String warehouseRemark) {
		// 直接删除 标识记为已经删除
		String sqlIdRelationtable = "UPDATE id_relationtable t SET t.is_delete = 1 WHERE t.orderid = ? AND t.itemid = ?;";
		// 更新亚马逊商品状态为0未到
		String sqlMabangshipment = "UPDATE mabangShipment t SET t.state = 0 WHERE t.orderNo = ? AND t.item_id = ?;";
		// Connection conn = DBHelper.getInstance().getConnection();
		//
		Connection conn1 = DBHelper.getInstance().getConnection();
		PreparedStatement cstmtsqlIdRelationtable = null;
		PreparedStatement cstmtsqlMabangshipment = null;
		int rs1 = 0;
		try {
			/*
			 * cstmtsqlIdRelationtable =
			 * conn.prepareStatement(sqlIdRelationtable);
			 * cstmtsqlIdRelationtable.setString(1, orderid);
			 * cstmtsqlIdRelationtable.setString(2, itemid);
			 * rs1=cstmtsqlIdRelationtable.executeUpdate();
			 *
			 * cstmtsqlMabangshipment =
			 * conn.prepareStatement(sqlMabangshipment);
			 * cstmtsqlMabangshipment.setString(1, orderid);
			 * cstmtsqlMabangshipment.setString(2, itemid);
			 * rs1=cstmtsqlMabangshipment.executeUpdate();
			 */
			// 本地库更新
			PreparedStatement cstmtsqlIdRelationtable1 = null;
			PreparedStatement cstmtsqlMabangshipment1 = null;
			try {
				cstmtsqlIdRelationtable1 = conn1
						.prepareStatement(sqlIdRelationtable);
				cstmtsqlIdRelationtable1.setString(1, orderid);
				cstmtsqlIdRelationtable1.setString(2, itemid);
				cstmtsqlIdRelationtable1.executeUpdate();

				cstmtsqlMabangshipment1 = conn1
						.prepareStatement(sqlMabangshipment);
				cstmtsqlMabangshipment1.setString(1, orderid);
				cstmtsqlMabangshipment1.setString(2, itemid);
				cstmtsqlMabangshipment1.executeUpdate();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					if (cstmtsqlIdRelationtable1 != null) {
						cstmtsqlIdRelationtable1.close();
					}
					if (cstmtsqlMabangshipment1 != null) {
						cstmtsqlMabangshipment1.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// DBHelper.getInstance().closeConnection(conn);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (cstmtsqlIdRelationtable != null) {
					cstmtsqlIdRelationtable.close();
				}
				if (cstmtsqlMabangshipment != null) {
					cstmtsqlMabangshipment.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn1);
		}

		return rs1;
	}

	// 亚马逊商品入库
	@Override
	public int updateAmazonstatus(String orderid, long goodid, int status,
	                              String goodurl, String barcode, String userid, String userName,
	                              String tbOrderId, String shipno, String itemid,
	                              String warehouseRemark, String storeName) {
		/*
		 * String sqlCount =
		 * "SELECT * FROM id_relationtable WHERE orderid= ? AND itemid= ? AND is_delete <> 1"
		 * ; String sql =
		 * "INSERT INTO id_relationtable(orderid,goodid,goodstatus,goodurl,barcode,picturepath,createtime,POSITION,userid,username,tborderid,warehouse_remark) "
		 * +
		 * "VALUES(order_id,good_id,good_status,good_url,bar_code,CONCAT(DATE_FORMAT(NOW(),'%Y-%m'),'/',order_id,'_',good_id,'.jpg'),NOW(),"
		 * + "position_v,userid_v,username_v,tborderid_v ,warehouseRemark);";
		 * String sqlupdateMabangShipment =
		 * "UPDATE mabangShipment t SET t.state=1 WHERE orderNo= ? AND item_id= ?;"
		 * ;
		 */
		final String sql = "{call update_amazon_idrelationtable(?,?,?,?,?,?,?,?,?,?,?,?)}";
		// Connection conn = DBHelper.getInstance().getConnection();
		Connection conn1 = DBHelper.getInstance().getConnection();
		int res = 0;
		CallableStatement cstmt = null;
		CallableStatement cstmt1 = null;
		ResultSet rs1 = null;
		try {
			/*
			 * cstmt = conn.prepareCall(sql); cstmt.setString(1, orderid);
			 * cstmt.setLong(2, goodid); cstmt.setInt(3, status);
			 * cstmt.setString(4, goodurl); cstmt.setString(5, barcode); //新增
			 * 用户id 用户名 淘宝订单id cstmt.setString(6, userid); cstmt.setString(7,
			 * userName); cstmt.setString(8, tbOrderId); //快递单号
			 * cstmt.setString(9, shipno); //itemid cstmt.setString(10, itemid);
			 * // cstmt.setString(11, storeName); //入库备注
			 * cstmt.setString(12,warehouseRemark); rs1=cstmt.executeQuery(); if
			 * (!rs1.wasNull()) { while (rs1.next()) { res=rs1.getInt("cnt"); }
			 * }
			 */
			// 跟新本地库
			try {
				cstmt1 = conn1.prepareCall(sql);
				cstmt1.setString(1, orderid);
				cstmt1.setLong(2, goodid);
				cstmt1.setInt(3, status);
				cstmt1.setString(4, goodurl);
				cstmt1.setString(5, barcode);
				// 新增 用户id 用户名 淘宝订单id
				cstmt1.setString(6, userid);
				cstmt1.setString(7, userName);
				cstmt1.setString(8, tbOrderId);
				// 快递单号
				cstmt1.setString(9, shipno);
				// itemid
				cstmt1.setString(10, itemid);
				//
				cstmt1.setString(11, storeName);
				// 入库备注
				cstmt1.setString(12, warehouseRemark);
				rs1 = cstmt1.executeQuery();
				if (!rs1.wasNull()) {
					while (rs1.next()) {
						res = rs1.getInt("cnt");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (cstmt1 != null) {
					try {
						cstmt1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				DBHelper.getInstance().closeConnection(conn1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public List<BuyAliGoodsBean> getAliGoodsData(String admuserid) {
		List<BuyAliGoodsBean> list = new ArrayList<BuyAliGoodsBean>();
		String sql = "SELECT od.car_img,od.car_type,od.goodsid,ops.goods_name,ops.buycount,ops.userid,ops.confirm_userid,ops.orderid,ops.goods_url FROM order_product_source ops INNER JOIN order_details od ON ops.goodsid=od.goodsid and ops.orderid=od.orderid "
				+ "WHERE LOCATE('aliexpress',ops.goods_p_url)>0 AND ops.purchase_state=3 "
				+ " AND od.state=0 ORDER BY ops.confirm_time";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			// stmt.setString(1, admuserid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				BuyAliGoodsBean bab = new BuyAliGoodsBean();
				bab.setBuyCount(rs.getInt("buycount"));
				bab.setCarImg(rs.getString("car_img"));
				bab.setCarType(rs.getString("car_type"));
				bab.setGoodsid(rs.getInt("goodsid"));
				bab.setGoodsName(rs.getString("goods_name"));
				bab.setOrderid(rs.getString("orderid"));
				bab.setGoodsUrl(rs.getString("goods_url"));
				bab.setUserid(rs.getInt("userid"));
				bab.setConfirmUserid(rs.getString("confirm_userid"));
				list.add(bab);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public List<TaoBaoOrderInfo> getTaoBaoInfo(String orderid) {
		List<TaoBaoOrderInfo> list = new ArrayList<TaoBaoOrderInfo>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			// String sql="SELECT DISTINCT t.orderid AS
			// orderid,DATE_FORMAT(id.createtime, '%Y-%m-%d') AS
			// createtime,id.barcode AS barcode,t.orderstatus AS orderstatus,"
			// +"(SELECT COUNT(id) FROM order_details WHERE
			// orderid='"+orderid+"')-(SELECT COUNT(op.id) FROM
			// order_product_source op INNER JOIN order_details od ON
			// od.id=op.od_id WHERE op.orderid=od.orderid AND
			// od.orderid='"+orderid+"' AND od.state<>2 AND op.purchase_state IN
			// (2,3,4,6,7,8)) AS noBuy "
			// +"FROM order_product_source ops INNER JOIN
			// taobao_1688_order_history t ON ops.tb_1688_itemid=t.itemid LEFT
			// JOIN id_relationtable id ON t.orderid=id.tborderid WHERE "
			// +"ops.orderid='"+orderid+"' AND t.orderdate>(SELECT orderpaytime
			// FROM orderinfo WHERE order_no='"+orderid+"') AND
			// t.orderstatus<>'交易关闭' AND t.orderstatus<>'退款成功' AND
			// t.orderstatus<>'退款中'";
			String sql = "SELECT distinct id.odid,t.orderid AS orderid,DATE_FORMAT(id.createtime, '%Y-%m-%d') AS createtime,id.barcode AS barcode,t.orderstatus AS orderstatus,"
					+ "(SELECT COUNT(id) FROM order_details WHERE orderid='"
					+ orderid
					+ "')-(SELECT COUNT(op.id) FROM order_product_source op INNER JOIN order_details od ON od.id=op.od_id and od.orderid=op.orderid WHERE op.orderid=od.orderid AND od.orderid='"
					+ orderid
					+ "' AND od.state<>2 AND op.purchase_state IN (2,3,4,6,7,8)) AS noBuy"
					+ " FROM taobao_1688_order_history t INNER JOIN id_relationtable id ON t.orderid=id.tborderid"
					+ " WHERE id.orderid='" + orderid + "'";
			sql += "OR id.orderid IN (SELECT order_no FROM orderinfo WHERE state IN (1, 2) AND user_id = (SELECT user_id FROM orderinfo WHERE order_no ='"
                    + orderid + "')) LIMIT 1";  // 同用户未出库订单在同一库位
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaoBaoOrderInfo tb = new TaoBaoOrderInfo();
				tb.setOrderid(rs.getString("orderid"));
				tb.setStorageTime(rs.getString("createtime") != null ? rs
						.getString("createtime") : "无");// 入库时间
				tb.setBarcode(rs.getString("barcode") != null ? rs
						.getString("barcode") : "无");
				tb.setOrderstatus(rs.getString("orderstatus"));
				tb.setTotalqty(rs.getInt("noBuy"));
				tb.setOdid(rs.getString("odid"));
				StringBuffer bf = new StringBuffer();
				String sql1 = "SELECT GROUP_CONCAT(DISTINCT ops.orderid) as orderids FROM order_product_source ops INNER JOIN taobao_1688_order_history tb ON ops.tb_1688_itemid=tb.itemid WHERE tb.orderid='"
						+ rs.getString("orderid")
						+ "' AND ops.purchase_state IN (2,3,4,6,7,8)";
				stmt1 = conn.prepareStatement(sql1);
				rs1 = stmt1.executeQuery();
				String orderids = "";
				if (rs1.next()) {
					orderids = rs1.getString("orderids");
				}
				if (null != orderids && orderids.contains(",")) {
					String[] id = orderids.split(",");
					for (int i = 0; i < id.length; i++) {
						if (!id[i].equals(orderid)) {
							bf.append("," + id[i]);
						}
					}
				}
				tb.setItemname(bf.toString().length() > 0 ? bf.toString()
						.substring(1, bf.length()) : "无");
				list.add(tb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}

	@Override
	public String getBarcode(int short_term, int admid, String orderid) {
		String code = "无";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "";
			if (admid == 32) {
				sql = "SELECT barcode FROM storage_location WHERE 1=1 and barcode<>'SHCR006003001' ";
				sql += " and is_company=1 and acount=0 and is_use=0 limit 1";
			} else {
				sql = "select count(id) as counts from order_details where orderid='"
						+ orderid + "'";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				int counts = 0;
				if (rs.next()) {
					counts = rs.getInt("counts");
				}
				sql = "SELECT barcode FROM storage_location WHERE 1=1 and barcode<>'SHCR006003001' and is_use=0 AND LEFT(POSITION,4)<>'CR-8' AND LEFT(POSITION,4)<>'CR-9' AND LEFT(POSITION,5)<>'CR-10' ";
				if (counts >= 40) {
					sql += " and is_company=2 and acount=0 and region in (7,8) limit 1";
				} else if (counts <= 10) {
					sql += " and is_company=2 and acount=0 and row=1 limit 1";
				} else {
					sql += " and is_company=2 and acount=0 and region in (1,2,3,4,5,6) and row<>1  limit 1";
				}
			}
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				code = rs.getString("barcode");
			} else if (admid != 32) {
				sql = "SELECT barcode FROM storage_location WHERE is_company=2 and barcode<>'SHCR006003001' and is_use=0 AND LEFT(POSITION,4)<>'CR-8' AND LEFT(POSITION,4)<>'CR-9' AND LEFT(POSITION,5)<>'CR-10' and acount=0 limit 1";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					code = rs.getString("barcode");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return code;
	}

	@Override
	public int checkOrderState(String orderid) {
		int row=0;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			String sql="SELECT SUM(od.state) AS rkCount,SUM(od.checked) as yhCount,COUNT(od.id) as odCount FROM orderinfo oi INNER JOIN order_details od ON oi.order_no=od.orderid " +
					"WHERE oi.state>0 AND oi.state<6 AND od.state<2 AND oi.order_no=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,orderid);
			rs=stmt.executeQuery();
			if(rs.next()){
				int a=rs.getInt("rkCount");
				int b=rs.getInt("yhCount");
				int c=rs.getInt("odCount");
				if(a == b && b == c){
					row=1;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			LOG.error("",e);
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public int updateTbstatus(final String orderid, final long goodid,
	                          final int status, final String goodurl, final String barcode,
	                          final String userid, final String userName, final String tbOrderId,
	                          final String shipno, final String itemid,
	                          final String warehouseRemark, final String repState,
	                          final int goodCount,final String weight) {
		String sql1 = "update order_details set checked =1,shipno=? where orderid=? and goodsid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs1 = 0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		try {
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setString(1, shipno);
			stmt1.setString(2, orderid);
			stmt1.setInt(3, (int) goodid);
			rs1 = stmt1.executeUpdate();
			sql1 = "update order_product_source set purchase_state=4 where goodsid='"
					+ goodid + "' and orderid='" + orderid + "'";
			stmt1 = conn.prepareStatement(sql1);
			rs1 = stmt1.executeUpdate();
			String remark = "";
			double weights=Double.parseDouble(weight);
			sql1 = "select warehouse_remark,weight from id_relationtable where orderid=? and goodid=?";
			stmt = conn.prepareStatement(sql1);
			stmt.setString(1, orderid);
			stmt.setLong(2, goodid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				remark = rs.getString("warehouse_remark") + warehouseRemark;
				weights+=rs.getDouble("weight");
			} else {
				remark = warehouseRemark;
			}
			sql1 = "update id_relationtable set weight="+weights+",goodstatus=1,itemqty="
					+ goodCount + ",warehouse_remark='" + remark
					+ "' where goodid='" + goodid + "' and orderid='" + orderid
					+ "' and is_replenishment=1 and is_delete=0";
			stmt1 = conn.prepareStatement(sql1);
			rs1 = stmt1.executeUpdate();
			sql1 = "select isDropshipOrder from orderinfo where order_no='"+ orderid + "'";
			stmt2 = conn.prepareStatement(sql1);
			rs = stmt2.executeQuery();
			if (rs.next()) {
				int str = rs.getInt("isDropshipOrder");
				if (str == 1) {
					sql1 = "SELECT DISTINCT COUNT(b.id)-SUM(b.state) as counts FROM order_details o INNER JOIN order_details b ON o.dropshipid=b.dropshipid WHERE o.orderid='"
							+ orderid
							+ "' AND o.goodsid='"
							+ goodid
							+ "' and b.state<2";
					stmt2 = conn.prepareStatement(sql1);
					rs = stmt2.executeQuery();
					if (rs.next() && rs.getInt("counts") == 0) {
						sql1 = "update dropshiporder set state=2 where child_order_no=(select dropshipid from order_details where orderid='"
								+ orderid + "' and goodsid='" + goodid + "')";
						stmt2 = conn.prepareStatement(sql1);
						stmt2.executeUpdate();
					}
					// 判断主单下所有的子单是否到库
					sql1 = "SELECT  COUNT(orderid)*2-SUM(state) as countss FROM dropshiporder WHERE parent_order_no='"
							+ orderid + "'";
					stmt2 = conn.prepareStatement(sql1);
					rs = stmt2.executeQuery();
					if (rs.next() && rs.getInt("countss") == 0) {
						sql1 = "update orderinfo set state=2 where order_no='"
								+ orderid + "'";
						stmt2 = conn.prepareStatement(sql1);
						stmt2.executeUpdate();
					}
				} else {
					sql1 = "SELECT (COUNT(*) - SUM(checked)) as orderdetails_count FROM order_details WHERE state<2 AND orderid='"
							+ orderid + "'";
					stmt2 = conn.prepareStatement(sql1);
					rs = stmt2.executeQuery();
					if (rs.next() && rs.getInt("orderdetails_count") == 0) {
						sql1 = "UPDATE orderinfo SET state=2 WHERE order_no='"
								+ orderid + "'";
						stmt3 = conn.prepareStatement(sql1);
						stmt3.executeUpdate();
					}
				}
			}
			System.out.println("--------------------开始记录商品入库--------------------");
			if (repState != null && "0".equals(repState)) {
				sql1 = "SELECT ops.buycount AS buycount,ops.goods_title AS goods_name,ops.goods_price AS goods_p_price,od.car_type AS car_type,ops.goods_p_url AS goods_p_url,od.goods_pid,od.goodscatid AS cid,od.car_img AS car_img FROM order_replenishment ops INNER JOIN order_details od ON ops.goodsid=od.goodsid and ops.orderid=od.orderid  WHERE ops.goodsid=? AND ops.orderid=?";
			} else {
				sql1 = "SELECT ops.buycount AS buycount,ops.goods_name AS goods_name,ops.goods_p_price AS goods_p_price,od.car_type AS car_type,ops.goods_p_url AS goods_p_url,od.goods_pid,od.goodscatid AS cid,od.car_img AS car_img FROM order_product_source ops INNER JOIN order_details od ON ops.goodsid=od.goodsid and ops.orderid=od.orderid  WHERE ops.goodsid=? AND ops.orderid=?";
			}
			stmt = conn.prepareStatement(sql1);
			stmt.setInt(1, (int) goodid);
			stmt.setString(2, orderid);
			rs = stmt.executeQuery();
			String goodName = "";
			String car_type = "";
			String goods_p_price = "0.00";
			String goods_p_url = "";
			String goodscatid = "0";
			String cid = "";
			String car_img = "";
			String goods_pid="";//销售商品的编号
			int buycount = 0;
			if (rs.next()) {
				buycount = rs.getInt("buycount");
				goodName = rs.getString("goods_name");
				car_type = rs.getString("car_type");
				goods_p_price = rs.getString("goods_p_price");
				goods_p_url = rs.getString("goods_p_url");
//				goodscatid = rs.getString("category");
				cid = rs.getString("cid");
				car_img = rs.getString("car_img");
				goods_pid=rs.getString("goods_pid");
			}
			// 新增商品入库明细记录
			sql1 = "insert into goods_inventory_whj (orderid,goodsid,createtime,counts,goods_pid,admName,barcode,type,remark) " +
					"values('"+orderid+"',"+goodid+",now(),'"+goodCount+"','"+goods_pid+"','"+userName+"','"+barcode+"','1','"+remark+"')";
			stmt = conn.prepareStatement(sql1);
			int row = stmt.executeUpdate();
			if (row > 0) {
				System.out.println("-----------------------记录入库成功------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt3 != null) {
				try {
					stmt3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs1;
	}

	@Override
	public String getOrderType(String orderid) {
		String type="1";
		Connection con = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = "select isDropshipOrder from orderinfo where order_no='"+orderid+"'";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				type=rs.getString("isDropshipOrder");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
		}
		return type;
	}

	@Override
	public String getInventoryRecords(String orderid) {
		StringBuffer bf = new StringBuffer();
		Connection con = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = "SELECT orderid,odid,sum(itemqty) as itemqty FROM id_relationtable WHERE orderid='"
				+ orderid + "' and is_replenishment=1 and is_delete=0 group by odid";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bf.append(rs.getString("orderid") + ","
						+ rs.getString("odid") + ","
						+ rs.getString("itemqty") + ";");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
		}
		return bf.toString().length() > 0 ? bf.toString().substring(0,
				bf.length() - 1) : "";
	}

	private void insertOrderInfoChangeRecords(String orderNo,
	                                          int operationType, int adminId) {

		Orderinfo orderinfo = new Orderinfo();
		try {

			// 插入成功，插入信息放入更改记录表中
			OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
			orderinfo = infoDao.queryOrderInfoByOrderNo(orderNo);
			if (orderinfo != null) {
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao
						.insertOrderChange(orderinfo, adminId, operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}

		} catch (Exception e) {
			e.getStackTrace();
			if (orderinfo != null) {
				LOG.error("订单[订单号：" + orderinfo.getOrderNo()
						+ "]更改，插入更改数据失败，修改状态为：" + operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}
		}
	}


	private void insertDetailsChangeRecords(String orderNo, int goodsid,
	                                        int operationType, int adminId) {
		OrderDetailsBean orderDetails = new OrderDetailsBean();
		try {
			// 插入成功，插入信息放入更改记录表中
			OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
			orderDetails = infoDao.queryOrderDetailsByOrderNoAndGoodsid(
					orderNo, goodsid);
			if (orderDetails != null) {
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao.insertOrderDetailsChange(orderDetails,
						operationType, adminId);
			} else {
				LOG.error("订单详情[订单号:" + orderNo + ",商品id：" + goodsid
						+ "]获取数据失败，插入更改记录取消");
			}

		} catch (Exception e) {
			if (orderDetails != null) {
				LOG.error("订单详情[订单号:" + orderDetails.getId() + ",商品id："
						+ orderDetails.getGoodsid() + "]更改，插入更改数据失败，修改状态为："
						+ operationType);
			} else {
				LOG.error("订单详情[订单号:" + orderNo + ",商品id：" + goodsid
						+ "]获取数据失败，插入更改记录取消");
			}
		}
	}

}
