package com.cbt.website.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cbt.warehouse.util.StringUtil;

import com.cbt.bean.OrderAddress;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.Payment;
import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.pojo.Admuser;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Utility;
import com.cbt.warehouse.pojo.Dropshiporder;

public class OrderSplitDaoImpl implements IOrderSplitDao {
//	private static final Log   SLOG = (Log) LogFactory.getLog("source");

	@Override
	public List<OrderDetailsBean> getOrdersDetails_split(String orderNo) {
		// String sql="select
		// od.id,od.yourorder,od.goodsprice,od_total_weight,od_bulk_volume,purchase_state,goodsid,discount_ratio,state
		// from order_details od where od.state != 2 and od.orderid=? ";
		String sql = "select od.id,od.userid,od.yourorder,od.goodsname,od.orderid,od.delivery_time,"
				+ "od.car_img,od.car_type,od.car_url,od.goodsprice,od_total_weight,od_bulk_volume,"
				+ "purchase_state,goodsid,discount_ratio,state,od.fileupload from order_details od where od.state != 2 and od.orderid=? ";
		//查询线上订单商品信息拆单时 王宏杰 2018-09-29
		Connection conn = DBHelper.getInstance().getConnection2();
//		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> spiderlist = new ArrayList<OrderDetailsBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderDetailsBean odb = new OrderDetailsBean();
				odb.setId(rs.getInt("od.id"));
				odb.setYourorder(rs.getInt("od.yourorder"));
				odb.setGoodsprice(rs.getString("od.goodsprice"));
				odb.setActual_weight(Utility.getStringIsNull(rs.getString("od_total_weight"))
						? rs.getString("od_total_weight") : "0");
				odb.setActual_volume(
						Utility.getStringIsNull(rs.getString("od_bulk_volume")) ? rs.getString("od_bulk_volume") : "0");
				odb.setPurchase_state(rs.getInt("purchase_state"));
				odb.setGoodsid(rs.getInt("goodsid"));
				odb.setDiscount_ratio(rs.getDouble("discount_ratio"));
				odb.setState(rs.getInt("state"));
				odb.setUserid(rs.getInt("od.userid"));
				odb.setGoodsname(rs.getString("od.goodsname"));
				odb.setOrderid(rs.getString("od.orderid"));
				odb.setDelivery_time(rs.getString("od.delivery_time"));
				odb.setCar_img(rs.getString("od.car_img"));
				odb.setCar_type(rs.getString("od.car_type"));
				odb.setCar_url(rs.getString("od.car_url"));
				odb.setFileupload(rs.getString("od.fileupload"));
				spiderlist.add(odb);
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
		return spiderlist;
	}

	/**
	 * 判断拆订单是否为测试订单
	 * @param odid
	 * @return
	 */
	@Override
	public boolean checkTestOrder(String odid) {
		boolean flag=true;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql="SELECT od.id FROM order_details od INNER JOIN orderinfo oi ON od.orderid=oi.order_no " +
					"LEFT JOIN admin_r_user a ON oi.user_id=a.userid WHERE adminid='18' AND od.id="+odid+"";
			stmt=conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				flag=false;
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return flag;
	}

	@Override
	public List<Object[]> getOrdersDetails(String[] orderNos) {
		String sql = "select od.orderid,od.id,od.yourorder,od.goodsprice,goodsname,od.car_img AS googs_img,od.car_type AS goods_type,od.car_img goods_typeimg  from order_details od where od.state!=2  and ";
		if (orderNos.length == 2) {
			sql += "( od.orderid=? or od.orderid=? )";
		} else {
			sql += " od.orderid=? ";
		}
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		DecimalFormat df = new DecimalFormat("######0.00");
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNos[0]);
			if (orderNos.length == 2) {
				stmt.setString(2, orderNos[1]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String price = rs.getString("od.goodsprice");
				int number = rs.getInt("od.yourorder");
				double sprice = 0;
				if (Utility.getStringIsNull(price)) {
					sprice = Double.parseDouble(price);
				}
				String googs_img = rs.getString("googs_img");
				if (googs_img == null || "".equals(googs_img)) {
					googs_img = "";
				} else {
					if (googs_img.indexOf(".jpg") != googs_img.lastIndexOf(".jpg")) {
						googs_img = googs_img.substring(0, googs_img.indexOf(".jpg") + ".jpg".length());
					} else if (googs_img.indexOf("32x32") > -1) {
						googs_img = googs_img.replace("32x32", "400x400");
					} else if (googs_img.indexOf("60x60") > -1) {
						googs_img = googs_img.replace("60x60", "400x400");
					}
				}
				Object[] objects = { rs.getString("od.orderid"), rs.getInt("od.id"), number, df.format(number * sprice),
						rs.getString("goodsname"), googs_img, rs.getString("goods_type"),
						rs.getString("goods_typeimg") };
				googs_img = null;
				spiderlist.add(objects);
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
		return spiderlist;
	}

	@Override
	public void addOrderInfoLog(String orderno, String orderinfo) {
		String sql = "insert orderinfo_log(orderno,orderinfo,createtime) values(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderno);
			stmt.setString(2, orderinfo);
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	}

	@Override
	public List<OrderBean> getOrders(String[] orderno) {
		String sql = null;
		// sql = "select
		// order_no,user_id,product_cost,state,delivery_time,service_fee,ip,mode_transport,create_time,details_number,pay_price_three,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,order_ac,pay_price,purchase_number,server_update,client_update
		// from orderinfo where order_no=? ";
		sql = "select order_no,user_id,product_cost,state,delivery_time,service_fee,ip,mode_transport,create_time,"
				+ "details_number,pay_price_three,foreign_freight, "
				+ "(case when  orderinfo.state=-1 then (select payment.payment_amount from payment where orderid=order_no and paystatus=1 limit 1) else orderinfo.pay_price end  ) pay_price,"
				+ "pay_price_tow,currency,actual_ffreight, discount_amount,order_ac,pay_price,purchase_number,server_update,client_update,"
				+ "coupon_discount,extra_discount,grade_discount,share_discount,discount_amount,cashback,extra_freight,vatbalance,actual_lwh,processingfee "
				+ "from orderinfo where orderinfo.order_no= ?";
		for (int i = 1; i < orderno.length; i++) {
			sql += " or order_no=?";
		}
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<OrderBean> obs = new ArrayList<OrderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderno[0]);
			for (int i = 1; i < orderno.length; i++) {
				stmt.setString(i + 1, orderno[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderBean ob = new OrderBean();
				ob.setUserid(rs.getInt("user_id"));
				ob.setOrderNo(rs.getString("order_no"));
				ob.setProduct_cost(rs.getString("product_cost"));
				ob.setCreatetime(rs.getString("create_time"));
				ob.setState(rs.getInt("state"));
				ob.setMode_transport(rs.getString("mode_transport"));
				ob.setService_fee(rs.getString("service_fee"));
				String foreign_freight_ = rs.getString("foreign_freight");
				ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
				ob.setActual_ffreight(rs.getString("actual_ffreight"));
				ob.setService_fee(rs.getString("service_fee"));
				ob.setIp(rs.getString("ip"));
				ob.setPurchase_number(rs.getInt("purchase_number"));
				ob.setDetails_number(rs.getInt("details_number"));
				ob.setCurrency(rs.getString("currency"));
				ob.setPay_price_three(rs.getString("pay_price_three"));
				ob.setPay_price_tow(rs.getString("pay_price_tow"));
				ob.setPay_price(rs.getDouble("pay_price"));
				int delivery_time = rs.getInt("delivery_time");
				ob.setOrder_ac(rs.getDouble("order_ac"));
				ob.setDiscount_amount(rs.getDouble("discount_amount"));
				ob.setDeliveryTime(delivery_time);
				ob.setServer_update(rs.getInt("server_update"));
				ob.setClient_update(rs.getInt("client_update"));
				ob.setCoupon_discount(rs.getDouble("coupon_discount"));
				ob.setExtra_discount(rs.getDouble("extra_discount"));
				ob.setGradeDiscount(rs.getFloat("grade_discount"));
				ob.setShare_discount(rs.getDouble("share_discount"));
				ob.setDiscount_amount(rs.getDouble("discount_amount"));
				ob.setCashback(rs.getDouble("cashback"));
				ob.setExtra_freight(rs.getDouble("extra_freight"));
				ob.setVatBalance(rs.getDouble("vatbalance"));
				ob.setActual_lwh(rs.getString("actual_lwh"));
				ob.setProcessingfee(rs.getDouble("processingfee"));
				obs.add(ob);
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

		return obs;
	}




	@Override
	public int addInventory(String odid,String remark) {
		int row=0;
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null,rs1=null;
		PreparedStatement stmt = null;
		String sql="";
		try{
			sql="SELECT od.id,od.orderid,od.goodsid,od.car_url,od.seilUnit,od.goodsname,od.car_type,id.barcode,ca.en_name,ops.goods_p_price,od.car_img,od.goods_pid,od.car_urlMD5,"
					+ " ops.usecount,ops.goods_p_url,ops.goodsid "
					+" FROM order_details od INNER JOIN order_product_source ops ON od.id=ops.od_id"
					+" INNER JOIN id_relationtable id ON od.id=id.odid"
					+" LEFT JOIN 1688_category ca ON od.goodscatid=ca.category_id"
					+" WHERE od.id="+odid+" AND od.checked=1 AND id.goodstatus=1 limit 1";
			stmt=conn.prepareStatement(sql);
			System.out.println("sql==="+sql);
			rs=stmt.executeQuery();
			if(rs.next()){
				System.out.println("----------------开始做库存存入-------------------");
				sql="select * from inventory where goods_pid=? and sku=? and car_urlMD5=? and barcode=?";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, rs.getString("goods_pid"));
				stmt.setString(2, rs.getString("car_type"));
				stmt.setString(3, rs.getString("car_urlMD5"));
				stmt.setString(4, rs.getString("barcode"));
				rs1=stmt.executeQuery();
				if(rs1.next()){
					//存在该商品库存则更新记录
					int flag=rs1.getInt("flag");
					if(flag==1){
						//已盘点过
						sql="update inventory set new_remaining=?,can_remaining=?,new_inventory_amount=?,goods_p_url=?,goods_p_price=?,od_id=?,updatetime=now(),remark=?"
								+ "where goods_pid=? and sku=? and car_urlMD5=? and barcode=?";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, String.valueOf(rs1.getInt("new_remaining")+rs.getInt("usecount")));
						stmt.setString(2,  String.valueOf(rs1.getInt("can_remaining")+rs.getInt("usecount")));
						stmt.setString(3, String.valueOf(rs1.getDouble("new_inventory_amount")+rs.getInt("usecount")*rs.getDouble("goods_p_price")));

					}else{
						//未盘点库存
						sql="update inventory set remaining=?,can_remaining=?,inventory_amount=?,goods_p_url=?,goods_p_price=?,od_id=?,updatetime=now(),remark=?"
								+ "where goods_pid=? and sku=? and car_urlMD5=? and barcode=?";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, String.valueOf(rs1.getInt("remaining")+rs.getInt("usecount")));
						stmt.setString(2,   String.valueOf(rs1.getInt("can_remaining")+rs.getInt("usecount")));
						stmt.setString(3, String.valueOf(rs1.getDouble("inventory_amount")+rs.getInt("usecount")*rs.getDouble("goods_p_price")));
					}
					stmt.setString(4, rs1.getString("goods_p_url")+","+rs.getString("goods_p_url"));
					stmt.setString(5, rs1.getString("goods_p_price")+","+rs.getString("goods_p_price"));
					stmt.setString(6, rs.getString("id"));
					stmt.setString(7, (StringUtil.isBlank(rs1.getString("remark"))?"":rs1.getString("remark"))+rs.getString("orderid")+"-"+rs.getString("goodsid")+remark);
					stmt.setString(8, rs.getString("goods_pid"));
					stmt.setString(9, rs.getString("car_type"));
					stmt.setString(10, rs.getString("car_urlMD5"));
					stmt.setString(11, rs.getString("barcode"));
					row=stmt.executeUpdate();
					sql="insert into storage_outbound_details (orderid,goodsid,in_id,type,admName,createtime,add_inventory,storage_count) values(?,?,?,?,?,now(),?,?)";
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, rs.getString("orderid"));
					stmt.setString(2, rs.getString("goodsid"));
					stmt.setString(3, rs1.getString("id"));
					stmt.setString(4, "3");
					stmt.setString(5, "订单销售");
					stmt.setString(6, rs.getString("usecount"));
					stmt.setString(7, rs.getString("usecount"));
					stmt.executeUpdate();
				}else{
					//不存在该商品库存则新增
					sql="insert into inventory (goods_url,remaining,can_remaining,good_name,sku,barcode,goods_p_url,goodscatid,"
							+ "inventory_amount,car_img,goods_p_price,od_id,goods_pid,createtime,car_urlMD5,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, rs.getString("car_url"));//goods_url
					stmt.setString(2, rs.getString("usecount"));//remaining
					stmt.setString(3, rs.getString("usecount"));//can_remaining
					stmt.setString(4, rs.getString("goodsname"));//good_name
					stmt.setString(5, rs.getString("car_type"));//sku
					stmt.setString(6, rs.getString("barcode"));//barcode
					stmt.setString(7, rs.getString("goods_p_url"));//goods_p_url
					stmt.setString(8, rs.getString("en_name"));//goodscatid
					stmt.setString(9, String.valueOf(rs.getInt("usecount")*rs.getDouble("goods_p_price")));//inventory_amount
					stmt.setString(10, rs.getString("car_img"));//car_img
					stmt.setString(11, rs.getString("goods_p_price"));//goods_p_price
					stmt.setString(12, rs.getString("id"));//od_id
					stmt.setString(13, rs.getString("goods_pid"));//goods_pid
					stmt.setString(14, rs.getString("car_urlMD5"));//car_urlMD5
					stmt.setString(15, rs.getString("orderid")+"-"+rs.getString("goodsid")+remark);//remark
					row=stmt.executeUpdate();
					sql="select * from inventory where goods_pid=? and sku=? and car_urlMD5=? and barcode=?";
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, rs.getString("goods_pid"));
					stmt.setString(2, rs.getString("car_type"));
					stmt.setString(3, rs.getString("car_urlMD5"));
					stmt.setString(4, rs.getString("barcode"));
					rs1=stmt.executeQuery();
					if(rs1.next()){
						sql="insert into storage_outbound_details (orderid,goodsid,in_id,type,admName,createtime,add_inventory,storage_count) values(?,?,?,?,?,now(),?,?)";
						stmt=conn.prepareStatement(sql);
						stmt.setString(1, rs.getString("orderid"));
						stmt.setString(2, rs.getString("goodsid"));
						stmt.setString(3, rs1.getString("id"));
						stmt.setString(4, "3");
						stmt.setString(5, "订单销售");
						stmt.setString(6, rs.getString("usecount"));
						stmt.setString(7, rs.getString("usecount"));
						stmt.executeUpdate();
					}
				}
			}else{
				System.out.println("----------------没有入库存入-------------------");
			}
		}catch(Exception e){
//			SLOG.info("取消订单转变库存失败【"+odid+"】");
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(rs1!=null){
					rs1.close();
				}
				if(stmt!=null){
					stmt.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public OrderBean getOrders(String orderno) {
		// String sql = "select
		// order_no,user_id,product_cost,packag_style,state,delivery_time,service_fee,ip,mode_transport,create_time,"
		// +
		// "details_number,pay_price_three,remaining_price,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,"
		// + "order_ac,pay_price,purchase_number,server_update,client_update,"
		// + "(select max(order_no) from orderinfo where order_no like
		// '"+orderno+"%') maxSplitOrder,"
		// + "(select min(createtime) from payment where orderid=order_no and
		// paystatus=1 ) paytime "
		// + "from orderinfo where order_no=?";
		// --cjc20161105
		String sql = "select order_no,user_id,product_cost,packag_style,state,delivery_time,service_fee,ip,mode_transport,create_time,"
				+ "details_number,actual_lwh,processingfee,pay_price_three,remaining_price,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,"
				+ "order_ac,pay_price,purchase_number,server_update,client_update,cashback,extra_freight,share_discount,extra_discount,coupon_discount,grade_discount,exchange_rate,"
				+ "(select max(order_no) from orderinfo where order_no like '" + orderno
				+ "%' and locate('_SP',order_no) =0 and  isDropshipOrder < 2) maxSplitOrder,"
				+ "(select min(createtime) from payment where orderid=order_no and paystatus=1 ) paytime "
				+ "from orderinfo where order_no=? ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OrderBean ob = new OrderBean();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderno);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ob.setUserid(rs.getInt("user_id"));
				ob.setOrderNo(rs.getString("order_no"));
				ob.setProduct_cost(rs.getString("product_cost"));
				ob.setCreatetime(rs.getString("create_time"));
				ob.setState(rs.getInt("state"));
				ob.setMode_transport(rs.getString("mode_transport"));
				ob.setPackage_style(rs.getInt("packag_style"));
				// ob.setService_fee(rs.getString("service_fee"));多了一个
				// 11.02//--cjc20161102
				ob.setService_fee(rs.getString("service_fee"));
				String foreign_freight_ = rs.getString("foreign_freight");
				ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
				ob.setActual_ffreight(rs.getString("actual_ffreight"));
				ob.setIp(rs.getString("ip"));
				ob.setPurchase_number(rs.getInt("purchase_number"));
				ob.setDetails_number(rs.getInt("details_number"));
				ob.setCurrency(rs.getString("currency"));
				ob.setPay_price_three(rs.getString("pay_price_three"));
				ob.setPay_price_tow(rs.getString("pay_price_tow"));
				ob.setPay_price(rs.getDouble("pay_price"));
				int delivery_time = rs.getInt("delivery_time");
				ob.setOrder_ac(rs.getDouble("order_ac"));
				ob.setDiscount_amount(rs.getDouble("discount_amount"));
				ob.setDeliveryTime(delivery_time);
				ob.setServer_update(rs.getInt("server_update"));
				ob.setClient_update(rs.getInt("client_update"));
				ob.setMaxSplitOrder(rs.getString("maxSplitOrder"));
				ob.setDetails_pay(rs.getString("paytime"));
				ob.setRemaining_price(rs.getDouble("remaining_price"));
				ob.setCashback(rs.getDouble("cashback"));// 获得10美元减免--cjc20161105
				ob.setExtra_freight(rs.getDouble("extra_freight"));// 获取额外运费的金额，就是加急的运费的金额；<一般要求在5天之内到的需要付额外的费用>--cjc20161105
				ob.setShare_discount(rs.getDouble("share_discount"));// 分享折扣--cjc2017.1.6
				ob.setExtra_discount(rs.getDouble("extra_discount"));// 手动优惠拆单--cjc2017.1.19coupon_discount
				ob.setCoupon_discount(rs.getDouble("coupon_discount"));// 返单优惠 2.20
				ob.setGradeDiscount(rs.getFloat("grade_discount"));
				ob.setExchange_rate(rs.getString("exchange_rate"));
				ob.setActual_lwh(rs.getString("actual_lwh"));
				ob.setProcessingfee(rs.getDouble("processingfee"));
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

		return ob;
	}

	@Override
	public void addMessage_error(String email, String error, String info) {
		String sql = "insert message_error(email,error,info,createtime) values(?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, email);
			stmt.setString(2, error);
			stmt.setString(3, info);
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	}

	@Override
	public List<Object[]> getMessage_error(String time, String endtime, int page, int endpage) {
		String sql = "select id,email,error,info,createtime from message_error where createtime>=? and createtime<=? limit ?, ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, time);
			stmt.setString(2, endtime);
			stmt.setInt(3, page);
			stmt.setInt(4, endpage);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object[] objects = { rs.getInt("id"), rs.getString("email"), rs.getString("error"),
						rs.getString("info"), rs.getString("createtime") };
				spiderlist.add(objects);
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
		return spiderlist;
	}

	@Override
	public int getMessage_error(String time, String endtime) {
		String sql = "select count(id) counts from message_error where ";
		if (Utility.getStringIsNull(time)) {
			sql += " createtime>?";
		}
		if (Utility.getStringIsNull(endtime)) {
			if (Utility.getStringIsNull(time)) {
				sql += " and ";
			}
			sql += " createtime<? ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			int i = 1;
			if (Utility.getStringIsNull(time)) {
				stmt.setString(i, time);
				i++;
			}
			if (Utility.getStringIsNull(endtime)) {
				stmt.setString(i, endtime);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getInt("counts");
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
		return res;
	}

	@Override
	public int splitOrder(OrderBean ob, OrderBean orderBeannew, String odId_now, String selSplitArr, Payment pay,
	                      int state, double order_ac, double balance_pay, String gId_last, String adminUserName, String pay_time,
	                      List<OrderDetailsBean> updateOrderDetaildList, List<OrderDetailsBean> insertOrderDetaildList,
	                      String splitGoodsId) {
		// 修改拆分后原订单orderinfo
		String sql_up = "UPDATE orderinfo SET product_cost=?,pay_price=?,pay_price_tow=?,pay_price_three=?,actual_ffreight=?,service_fee=?,order_ac=?,details_number=?,foreign_freight=?,discount_amount=?,purchase_number=0,state=?,purchase_number=?,extra_freight=?,remaining_price=?,packag_style=0,share_discount=?,extra_discount=?,coupon_discount=?  WHERE order_no = ?";
		// 更新本地orderinfo主单的替换标志
		String sql_packag_style = "update orderinfo set packag_style=0 where order_no=?";
		// 修改拆分后的订单详情
		String sql_od = "update order_details set orderid=?   where id=?";
		String sql_od1 = "update order_details set orderid=?,state =-1  where id=?";
		// 复制订单对应的地址到新订单
		String sql_address = "insert into order_address select null,AddressID,?,Country,statename,address,address2,phoneNumber,zipcode,Adstatus,Updatetimr,admUserID,street,recipients,flag from order_address  where orderno=?  limit 1";
		// 复制订单对应的确认人员到新订单
		String sql_paymentconfirm = "insert into paymentconfirm select null, ?,confirmname,confirmtime,paytype,paymentid from paymentconfirm where orderno=?";
		// 新增拆分订单
		String sql_adod_new = "insert orderinfo(order_no,user_id,product_cost,state,address_id,delivery_time,service_fee,ip,mode_transport,create_time,details_number,pay_price_three,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,order_ac,purchase_number,server_update,client_update,remaining_price,packag_style,extra_freight,share_discount,extra_discount,coupon_discount) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// order_change表修改
		// String sql_change = "update order_change oc inner join order_details
		// od on od.goodsid=oc.goodid set oc.orderno=od.orderid where
		// od.orderid=?";
		// 取消插入一条，出货不插
		if (gId_last.length() > 0) {
			gId_last = gId_last.substring(0, gId_last.length() - 1);
		}
		String sql_change = "update `order_change` set orderno=? where orderno=? and goodid in (" + gId_last + ")"; // gId_last是字符串，使用预编译有可能报错
		String sql_change_insert = "INSERT INTO `order_change`(orderNo,goodId,ropType,status)VALUES(?,?,?,1)";
		// 更新入库表
		// 不用更新入库数量（原有订单）
		String sql_id_relationtable = "update id_relationtable set orderid=? where orderid=? and goodid in (" + gId_last
				+ ")"; // gId_last是字符串，使用预编译有可能报错
		// 当前台有输入数量拆分时，且商品到货数量> 拆分后原始订单内商品数量时，更新原始订单商品到货数量 为拆分后原始订单剩余数量；商品到货数量>
		// 拆分后原始订单内商品数量时 到货数量为当前数量
		String sql_id_relationtable_update = "update id_relationtable t set t.`itemqty` = ? where t.`itemqty` >= ? and t.`orderid` = ? and t.`goodid` = ?";
		String sql_id_relationtable_update2 = "UPDATE id_relationtable t SET t.`itemqty` = ? WHERE t.`itemqty` < ? AND t.`orderid` = ? AND t.`goodid` =?	";
		// 当前台有输入拆分数量时，插入新订单内商品到货数量（总到货数量 — 原始订单内到货数量）
		String sql_id_relationtable_insert = "INSERT INTO id_relationtable SELECT NULL,?,?,goodurl,goodstatus,goodproblem,goodarrivecount,picturepath,createtime,barcode,tborderid,`position`,shipno,username,userid,state,itemid,is_delete,store_name,taobaospec,warehouse_remark,taobaoprice,? FROM id_relationtable WHERE orderid = '"
				+ ob.getOrderNo() + "' AND goodid = ?";
		// payment表拆分
		String sql_pay = "insert payment(userid,orderid,paymentid,orderdesc,username,paystatus,payment_amount,payment_cc,createtime,paySID,payflag,paytype) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		// 复制采购人员到拆分订单
		String sql_orderbuy = "insert into order_buy select null, ?,buyuser,buyid,time from order_buy where orderid=?";
		// 复制订单对应的备注到新订单
		// 更新货源记录
		String sql_source = "update order_product_source ops set ops.orderid=? where od_id=?";
		// 更新留在老订单中的货源记录
		String sql_source_update = "update order_product_source ops set ops.buycount = ?,ops.usecount = ?  where od_id=?";
		// String sql_source_insert = "INSERT INTO order_product_source SELECT
		// NULL,?,adminid,userid,`addtime`,'"+orderBeannew.getOrderNo()+"',confirm_userid,confirm_time,goodsid,goodsdataid,goods_url,goods_img_url,goods
		// FROM order_product_source WHERE od_id = ? ";
		String sql_changegooddata = "update changegooddata cgd  set cgd.orderno=? where goodscarid=?";
		// 更新原有orderdetail,这里还有什么别的字段??无
		String sql_od_old = "UPDATE order_details t SET t.`yourorder` = ? WHERE t.`id` = ?";
		// 前台填写了数量后，插如记录到orderdetail
		String sql_od_new = "INSERT INTO order_details (userid,goodsid,goodsdata_id,goodsname,orderid,delivery_time,yourorder,car_url,car_type,car_img,goodsprice,checkprice_fee,checkproduct_fee,state,fileupload,freight_free,purchase_state,goods_class,extra_freight,od_bulk_volume,od_total_weight,discount_ratio,flag,buy_for_me,checked,seilUnit) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		// String sql_od_new_temp = "INSERT INTO `order_details`
		// (userid,goodsid,goodsdata_id,goodsname,orderid,delivery_time,yourorder,car_url,car_type,car_img,goodsprice,checkprice_fee,checkproduct_fee,state,fileupload,freight_free,purchase_state,goods_class,extra_freight,od_bulk_volume,od_total_weight,discount_ratio,flag,buy_for_me,checked,shipno,seilUnit)
		// VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
		// ?, ?, ?, ?, ?, ?)";
		// odId_last拆出来的没有填写数量详情ID
		String[] odIds = odId_now.split(",");
		for (int i = 1; i < odIds.length; i++) {
			sql_od += " or id=? ";
			sql_od1 += " or id=? ";
			sql_source += " or od_id=? ";
		}
		String[] gIds = gId_last.split(",");
		for (int i = 1; i < gIds.length; i++) {
			sql_changegooddata += " or id=? ";
		}
		// 如果是拆单退款，修改用户余额和用户变更表
		String sql_userprice = "update user set available_m=available_m+(?),applicable_credit=applicable_credit+(?)  where id =?";

		String sql_recharge = "insert into recharge_record(userid,price,type,remark,datatime,adminuser,usesign,balanceAfter) "
				+ "values(?,?,?,?,now(),?,0,(select available_m from user  where id=?))";
		// 存储过程校对订单状态
		String sql_checkupdate_ordersate = "{call checkupdate_ordersate(?)}";
		// 商品分配到新订单号
		if (odId_now.length() > 0) {
			odId_now = odId_now.substring(0, odId_now.length() - 1);
		}
		String sql_goods_distribution = "update goods_distribution set orderid=? where orderid=? and odid in ("
				+ odId_now + ")";
		String sql_googd_distribution_insert = "INSERT INTO `goods_distribution` SELECT NULL,?,?,goodsid,admuserid,createtime,distributionid,goodsdataid,goods_url,goodscatid,iscomplete from goods_distribution WHERE orderid ='"
				+ ob.getOrderNo() + "' AND odid = ?";
		// 为防止orderInfo状态修改不成功，所有操作成功后再次修改状态
		String sql_oi_state = "update orderinfo set state=? where order_no=?";

		Connection conn2 = DBHelper.getInstance().getConnection2();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt_packag_style = null;
		PreparedStatement stmt_od = null;
		PreparedStatement stmt_od2 = null;
		PreparedStatement stmt_address = null;
		PreparedStatement stmt_paymentconfirm = null;
		PreparedStatement stmt_adod_new = null;
		PreparedStatement stmt_change = null;
		PreparedStatement stmt_change2 = null;
		PreparedStatement stmt_id_relationtable = null;
		PreparedStatement stmt_pay = null;
		PreparedStatement stmt_orderbuy = null;
		PreparedStatement stmt_source = null;
		PreparedStatement stmt_changegooddata = null;
		PreparedStatement stmt_userprice = null;
		PreparedStatement stmt_recharge = null;
		PreparedStatement stmt_oi_state = null;
		PreparedStatement stmt_oi_state2 = null;
		PreparedStatement stmt_goods_distribution = null;
		PreparedStatement stmt_goods_distribution_insert = null;
		PreparedStatement stmt_od_old = null;
		PreparedStatement stmt_od_new = null;
		PreparedStatement stmt_change_insert = null;
		PreparedStatement stmt_id_relationtable_update = null;
		PreparedStatement stmt_id_relationtable_insert = null;
		PreparedStatement stmt_id_relationtable_update2 = null;
		PreparedStatement stmt_source_update = null;
		ResultSet rs = null;
		int res = 0;
		try {
			String orderNew = orderBeannew.getOrderNo();
			conn.setAutoCommit(false);
			conn2.setAutoCommit(false);
			// 更新原有订单orderinfo
			stmt = conn2.prepareStatement(sql_up);
			stmt.setString(1, ob.getProduct_cost());
			// 原订单的价格。
			stmt.setDouble(2, ob.getPay_price());
			stmt.setString(3, ob.getPay_price_tow());
			stmt.setString(4, ob.getPay_price_three());
			stmt.setString(5, ob.getActual_ffreight());
			stmt.setString(6, ob.getService_fee());
			stmt.setDouble(7, ob.getOrder_ac());
			stmt.setInt(8, ob.getDetails_number());
			stmt.setString(9, ob.getForeign_freight());
			stmt.setDouble(10, ob.getDiscount_amount());
			// int s =ob.getState();
			// System.out.println(s);
			stmt.setInt(11, ob.getState());
			stmt.setInt(12, ob.getPurchase_number());
			stmt.setDouble(13, ob.getExtra_freight());// 原订单需要支付的加急运费--cjc20161105
			stmt.setDouble(14, ob.getRemaining_price());
			stmt.setDouble(15, ob.getShare_discount());
			stmt.setDouble(16, ob.getExtra_discount());
			stmt.setDouble(17, ob.getCoupon_discount());
			;
			stmt.setString(18, ob.getOrderNo());
			res = stmt.executeUpdate();

			String sqlStrOne = "update orderinfo set product_cost=" + ob.getProduct_cost() + ",pay_price="
					+ ob.getPay_price() + ",pay_price_tow=" + ob.getPay_price() + ",pay_price_three="
					+ ob.getPay_price_three() + "," + " actual_ffreight=" + ob.getActual_ffreight() + ",service_fee="
					+ ob.getService_fee() + ",order_ac=" + ob.getOrder_ac() + ",details_number="
					+ ob.getDetails_number() + ",foreign_freight=" + ob.getForeign_freight() + "," + " discount_amount="
					+ ob.getForeign_freight() + ",purchase_number=0,state=" + ob.getState() + ",purchase_number="
					+ ob.getPurchase_number() + ",extra_freight=" + ob.getExtra_freight() + "," + " remaining_price="
					+ ob.getRemaining_price() + ",packag_style=0,share_discount=" + ob.getShare_discount()
					+ ",extra_discount=" + ob.getShare_discount() + ",coupon_discount=" + ob.getCoupon_discount() + "  "
					+ " where order_no = '" + ob.getOrderNo() + "'";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),ob.getOrderNo()
			// , "拆单1", "orderinfo",sqlStrOne);
			// 更新本地主单的替换标识
			stmt_packag_style = conn.prepareStatement(sql_packag_style);
			stmt_packag_style.setString(1, ob.getOrderNo());
			stmt_packag_style.executeUpdate();
			// 更新线上
			stmt_od_new = conn2.prepareStatement(sql_od_new, Statement.RETURN_GENERATED_KEYS);
			;
			if (state != 0) {
				// 二次出货
				stmt_od = conn2.prepareStatement(sql_od);
				stmt_od.setString(1, orderNew);
				// 若已经输入了数量，则可以不用更新
				boolean flag = false;
				for (int i = 0; i < odIds.length; i++) {
					if (!selSplitArr.contains(odIds[i])) {
						stmt_od.setString(i + 2, odIds[i]);
						flag = true;
					}
				}
				if (flag) {
					res = stmt_od.executeUpdate();
				}
			} else {
				// 退货 orderdetail state = -1()
				stmt_od = conn2.prepareStatement(sql_od1);
				stmt_od.setString(1, orderNew);
				boolean flag = false;
				for (int i = 0; i < odIds.length; i++) {
					if (!selSplitArr.contains(odIds[i])) {
						stmt_od.setString(i + 2, odIds[i]);
						flag = true;
					}
				}
				if (flag) {
					res = stmt_od.executeUpdate();
				}
				// stmt_od_new = conn2.prepareStatement(sql_od_new_temp);
			}
			// 更新原有订单商品orderdetail，货源表order_product_source老订单商品采购数量以及购买量
			stmt_od_old = conn2.prepareStatement(sql_od_old);
			stmt_source_update = conn.prepareStatement(sql_source_update);
			for (OrderDetailsBean re : updateOrderDetaildList) {
				stmt_od_old.setInt(1, re.getYourorder());
				stmt_od_old.setInt(2, re.getId());
				stmt_od_old.addBatch();
				// 拆单默认采购数量大于等与老订单商品数量
				stmt_source_update.setInt(1, re.getYourorder());
				stmt_source_update.setInt(2, re.getYourorder());
				stmt_source_update.setInt(3, re.getId());
				stmt_source_update.addBatch();
			}
			stmt_od_old.executeBatch();
			stmt_source_update.executeBatch();
			HashMap map = new HashMap();
			HashMap map1 = new HashMap();
			map.put("orderid", orderNew);
			String goodsid_ = "";
			for (int i = 0; i < odIds.length; i++) {
				stmt_od.setString(i + 2, odIds[i]);
				map1.put("id", odIds[i]);// 覆盖了？？
				if (i == odIds.length - 1) {
					goodsid_ += odIds[i];
				} else {
					goodsid_ += odIds[i] + ",";

				}
			}
			String ccc = "update order_details set orderid=" + orderNew + "   where id in (" + goodsid_.trim() + ")";
			// String ccc = ParamToSqlUtil.genSql("order_details", 1, map,
			// map1);
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单2", "order_details",ccc);
			// System.out.println("拆单2"+ccc);
			// 更新本地
			if (state != 0) {
				stmt_od2 = conn.prepareStatement(sql_od);
				stmt_od2.setString(1, orderNew);
				boolean flag = false;
				for (int i = 0; i < odIds.length; i++) {
					if (!selSplitArr.contains(odIds[i])) {
						stmt_od2.setString(i + 2, odIds[i]);
						flag = true;
					}

				}
				if (flag) {
					res = stmt_od2.executeUpdate();
				}
			} else {
				stmt_od2 = conn.prepareStatement(sql_od1);
				stmt_od2.setString(1, orderNew);
				boolean flag = false;
				for (int i = 0; i < odIds.length; i++) {
					if (!selSplitArr.contains(odIds[i])) {
						stmt_od2.setString(i + 2, odIds[i]);
						flag = true;
					}
				}
				if (flag) {
					res = stmt_od2.executeUpdate();
				}
			}
			// 新增地址与order
			stmt_address = conn2.prepareStatement(sql_address, Statement.RETURN_GENERATED_KEYS);
			stmt_address.setString(1, orderNew);
			stmt_address.setString(2, ob.getOrderNo());
			res = stmt_address.executeUpdate();
			String sqlStrTwo = "insert into order_address select null,AddressID,'" + orderNew + "',Country,"
					+ "statename,address,address2,phoneNumber,zipcode,Adstatus,Updatetimr,admUserID,"
					+ "	street,recipients,flag from order_address  where orderno='" + ob.getOrderNo() + "'  limit 1";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单3", "order_address",sqlStrTwo);
			int addressId = 0;
			if (res == 1) {
				rs = stmt_address.getGeneratedKeys();
				if (rs.next()) {
					addressId = rs.getInt(1);
				}
			}

			stmt_paymentconfirm = conn.prepareStatement(sql_paymentconfirm, Statement.RETURN_GENERATED_KEYS);
			stmt_paymentconfirm.setString(1, orderNew);
			stmt_paymentconfirm.setString(2, ob.getOrderNo());
			res = stmt_paymentconfirm.executeUpdate();

			String sqlStrThree = "insert into paymentconfirm select null, '" + orderNew + "',"
					+ "	confirmname,confirmtime,paytype,paymentid from paymentconfirm " + "where orderno='"
					+ ob.getOrderNo() + "'";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单4", "paymentconfirm",sqlStrThree);

			stmt_adod_new = conn2.prepareStatement(sql_adod_new, Statement.RETURN_GENERATED_KEYS);
			stmt_adod_new.setString(1, orderNew);
			stmt_adod_new.setInt(2, orderBeannew.getUserid());
			stmt_adod_new.setString(3, orderBeannew.getProduct_cost());
			stmt_adod_new.setInt(4, orderBeannew.getState());
			stmt_adod_new.setInt(5, addressId);
			stmt_adod_new.setInt(6, orderBeannew.getDeliveryTime());
			// 得到服务费 应该在原订单
			stmt_adod_new.setString(7, orderBeannew.getService_fee());
			stmt_adod_new.setString(8, orderBeannew.getIp());
			stmt_adod_new.setString(9, orderBeannew.getMode_transport());
			stmt_adod_new.setString(10, orderBeannew.getCreatetime());
			stmt_adod_new.setInt(11, orderBeannew.getDetails_number());
			stmt_adod_new.setString(12, orderBeannew.getPay_price_three());
			stmt_adod_new.setString(13, orderBeannew.getForeign_freight());
			stmt_adod_new.setString(14, orderBeannew.getPay_price() + "");
			stmt_adod_new.setString(15, orderBeannew.getPay_price_tow() + "");
			stmt_adod_new.setString(16, orderBeannew.getCurrency());
			stmt_adod_new.setString(17, orderBeannew.getActual_ffreight());
			stmt_adod_new.setDouble(18, orderBeannew.getDiscount_amount());
			stmt_adod_new.setDouble(19, orderBeannew.getOrder_ac());
			stmt_adod_new.setInt(20, orderBeannew.getPurchase_number());
			stmt_adod_new.setInt(21, orderBeannew.getServer_update());
			stmt_adod_new.setInt(22, orderBeannew.getClient_update());
			stmt_adod_new.setDouble(23, orderBeannew.getRemaining_price());
			stmt_adod_new.setInt(24, orderBeannew.getPackage_style());
			stmt_adod_new.setDouble(25, orderBeannew.getExtra_freight());
			stmt_adod_new.setDouble(26, orderBeannew.getShare_discount());
			stmt_adod_new.setDouble(27, orderBeannew.getExtra_discount());
			stmt_adod_new.setDouble(28, orderBeannew.getCoupon_discount());
			res = stmt_adod_new.executeUpdate();

			// 插入新的orderdetail
			// 先记录老的orderdetailId
			List<Integer> orderDetailList = new ArrayList<Integer>();
			for (OrderDetailsBean re : insertOrderDetaildList) {
				orderDetailList.add(re.getId());
				stmt_od_new.setInt(1, re.getUserid());
				stmt_od_new.setInt(2, re.getGoodsid());
				stmt_od_new.setInt(3, re.getGoodsdata_id());
				stmt_od_new.setString(4, re.getGoodsname());
				stmt_od_new.setString(5, orderNew);
				stmt_od_new.setString(6, re.getDelivery_time());
				stmt_od_new.setInt(7, re.getYourorder());
				stmt_od_new.setString(8, re.getCar_url());
				stmt_od_new.setString(9, re.getCar_type());
				stmt_od_new.setString(10, re.getCar_img());
				stmt_od_new.setString(11, re.getGoodsprice());
				stmt_od_new.setInt(12, re.getCheckprice_fee());
				stmt_od_new.setInt(13, re.getCheckproduct_fee());
				// stmt_od_new.setString(15,
				// String.valueOf(re.getActual_price()));
				// stmt_od_new.setString(16,
				// String.valueOf(re.getActual_freight()));
				// stmt_od_new.setString(17,
				// String.valueOf(re.getActual_weight()));
				// stmt_od_new.setString(18, re.getActual_volume());
				if (state == 0) {
					stmt_od_new.setInt(14, re.getState());
				} else {
					stmt_od_new.setInt(14, -1);
				}
				stmt_od_new.setString(15, re.getFileupload());
				// stmt_od_new.setInt(21, re.getOd_state());
				// stmt_od_new.setString(22, re.getPaytime());
				// stmt_od_new.setString(23, re.getCreatetime());
				stmt_od_new.setInt(16, re.getFreight_free());
				stmt_od_new.setInt(17, re.getPurchase_state());
				stmt_od_new.setInt(18, re.getGoods_class());
				stmt_od_new.setDouble(19, re.getExtra_freight());
				stmt_od_new.setString(20, String.valueOf(re.getOd_bulk_volume()));
				stmt_od_new.setString(21, String.valueOf(re.getOd_total_weight()));
				stmt_od_new.setDouble(22, re.getDiscount_ratio());
				stmt_od_new.setInt(23, re.getFlag());
				stmt_od_new.setInt(24, re.getBuy_for_me());
				stmt_od_new.setInt(25, re.getChecked());
				stmt_od_new.setString(26, re.getSeilUnit());
				// stmt_od_new.setString(27, re.getpic);
				stmt_od_new.addBatch();
			}
			int[] iS = stmt_od_new.executeBatch();

			List<Integer> newDetailId = new ArrayList<Integer>();
			ResultSet rsKey = stmt_od_new.getGeneratedKeys();
			while (rsKey != null && rsKey.next()) {
				rsKey.getInt(1);
				newDetailId.add(rsKey.getInt(1));
			}

			// 查询老的
			String sqlStrFour = "insert orderinfo(order_no,user_id,product_cost,state,address_id,delivery_time,"
					+ "service_fee,ip,mode_transport,create_time,details_number,pay_price_three,foreign_freight,"
					+ "pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,order_ac,purchase_number,"
					+ "server_update,client_update,remaining_price,packag_style,extra_freight,share_discount,"
					+ "extra_discount,coupon_discount) values('" + orderNew + "','" + orderBeannew.getUserid() + "','"
					+ orderBeannew.getProduct_cost() + "','" + orderBeannew.getState() + "','" + addressId + "','"
					+ orderBeannew.getDeliveryTime() + "','" + orderBeannew.getService_fee() + "','"
					+ orderBeannew.getIp() + "','" + orderBeannew.getMode_transport() + "','"
					+ orderBeannew.getCreatetime() + "','" + orderBeannew.getDetails_number() + "','"
					+ orderBeannew.getPay_price_three() + "','" + orderBeannew.getForeign_freight() + "','"
					+ orderBeannew.getPay_price() + "','" + orderBeannew.getPay_price_tow() + "','"
					+ orderBeannew.getCurrency() + "','" + orderBeannew.getActual_ffreight() + "','"
					+ orderBeannew.getDiscount_amount() + "','" + orderBeannew.getOrder_ac() + "','"
					+ orderBeannew.getPurchase_number() + "','" + orderBeannew.getServer_update() + "','"
					+ orderBeannew.getClient_update() + "','" + orderBeannew.getRemaining_price() + "','"
					+ orderBeannew.getPackage_style() + "','" + orderBeannew.getExtra_freight() + "','"
					+ orderBeannew.getShare_discount() + "','" + orderBeannew.getExtra_discount() + "','"
					+ orderBeannew.getCoupon_discount() + "')";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单5", "orderinfo",sqlStrFour);
			// System.out.println("拆单5"+sqlStrFour);
			// 线上更新
			stmt_change = conn2.prepareStatement(sql_change);
			stmt_change.setString(1, orderNew);
			stmt_change.setString(2, ob.getOrderNo());
			if (!"".equals(gId_last)) {
				stmt_change.executeUpdate();
			}
			// 本地更新
			stmt_change2 = conn.prepareStatement(sql_change);
			stmt_change2.setString(1, orderNew);
			stmt_change2.setString(2, ob.getOrderNo());
			if (!"".equals(gId_last)) {
				stmt_change2.executeUpdate();
			}
			// 拆单取消时，向order_change表插入记录
			String[] splitGoods = splitGoodsId.split(",");
			if (state == 0) {
				stmt_change_insert = conn2.prepareStatement(sql_change_insert);
				for (int j = 0; j < splitGoods.length; j++) {
					stmt_change_insert.setString(1, orderNew);
					stmt_change_insert.setInt(2, Integer
							.parseInt((!splitGoods[j].equals("") && splitGoods[j] != null) ? splitGoods[j] : "0"));
					stmt_change_insert.setInt(3, 4);
					stmt_change_insert.addBatch();
				}
				stmt_change_insert.executeBatch();
			}

			String sqlStrFive = "update order_change set orderno='" + orderNew + "' where orderno='" + ob.getOrderNo()
					+ "' and goodid in ('" + gId_last + "')";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单6", "order_change",sqlStrFive);
			// System.out.println("拆单6"+sqlStrFive);
			// 更新id_relationtable表 入库表 2/17
			stmt_id_relationtable = conn.prepareStatement(sql_id_relationtable);
			stmt_id_relationtable.setString(1, orderNew);
			stmt_id_relationtable.setString(2, ob.getOrderNo());
			if (!"".equals(gId_last)) {
				stmt_id_relationtable.executeUpdate();
			}
			// 批量更新原始订单商品到货数量
			stmt_id_relationtable_update = conn.prepareStatement(sql_id_relationtable_update);
			stmt_id_relationtable_update2 = conn.prepareStatement(sql_id_relationtable_update2);
			stmt_id_relationtable_insert = conn.prepareStatement(sql_id_relationtable_insert);
			PreparedStatement stmt_get_goodarrivecount = conn
					.prepareStatement("SELECT t.`itemqty` FROM id_relationtable t WHERE t.`orderid` = '"
							+ ob.getOrderNo() + "' AND t.`goodid` = ?");
			boolean insertFlag = false;
			for (int j = 0; j < splitGoods.length; j++) {
				// 判断到货数量和原始订单拆分后商品数量
				// 获取原始到货数量
				stmt_get_goodarrivecount.setString(1, splitGoods[j]);
				ResultSet rest = stmt_get_goodarrivecount.executeQuery();
				String goodArrivecount = "";
				while (rest.next()) { // 循环遍历查询结果集
					goodArrivecount = rest.getString("itemqty");
				}
				// 获取拆分原始订单的orderdetail 数量
				int originalOrderbuy = 0;
				for (OrderDetailsBean detail : updateOrderDetaildList) {
					if (splitGoods[j].equals(String.valueOf(detail.getGoodsid()))) {
						originalOrderbuy = detail.getYourorder();
						break;
					}
				}
				// 已到数量> 原始订单留下商品数量
				if (!"".equals(goodArrivecount)) {
					insertFlag = true;
					if (Integer.parseInt(goodArrivecount) >= originalOrderbuy) {
						stmt_id_relationtable_update.setString(1, String.valueOf(originalOrderbuy));// 拆分后订单商品数量
						stmt_id_relationtable_update.setString(2, String.valueOf(originalOrderbuy)); // 拆分后订单商品数量
						stmt_id_relationtable_update.setString(3, ob.getOrderNo());
						stmt_id_relationtable_update.setString(4, splitGoods[j]);
						stmt_id_relationtable_update.addBatch();
						// 批量插入
						stmt_id_relationtable_insert.setString(1, orderBeannew.getOrderNo());
						stmt_id_relationtable_insert.setString(2, splitGoods[j]);
						stmt_id_relationtable_insert.setString(3,
								String.valueOf((Integer.parseInt(goodArrivecount) - originalOrderbuy)));
						stmt_id_relationtable_insert.setString(4, splitGoods[j]);
						stmt_id_relationtable_insert.addBatch();
					} else {
						stmt_id_relationtable_update2.setString(1, goodArrivecount);// 商品到货数量
						stmt_id_relationtable_update2.setString(2, String.valueOf(originalOrderbuy)); // 拆分后订单商品数量
						stmt_id_relationtable_update2.setString(3, ob.getOrderNo());
						stmt_id_relationtable_update2.setString(4, splitGoods[j]);
						stmt_id_relationtable_update2.addBatch();
						// 批量插入
						stmt_id_relationtable_insert.setString(1, orderBeannew.getOrderNo());
						stmt_id_relationtable_insert.setString(2, splitGoods[j]);
						stmt_id_relationtable_insert.setString(3, "0");
						stmt_id_relationtable_insert.setString(4, splitGoods[j]);
						stmt_id_relationtable_insert.addBatch();
					}
				}
			}
			stmt_id_relationtable_update.executeBatch();
			stmt_id_relationtable_update2.executeBatch();
			if (insertFlag) {
				stmt_id_relationtable_insert.executeBatch();
			}
			// payment表拆分
			stmt_pay = conn2.prepareStatement(sql_pay);
			stmt_pay.setInt(1, pay.getUserid());
			stmt_pay.setString(2, pay.getOrderid());
			stmt_pay.setString(3, pay.getPaymentid());
			stmt_pay.setString(4, pay.getOrderdesc());
			stmt_pay.setString(5, pay.getUsername());
			stmt_pay.setInt(6, pay.getPaystatus());
			stmt_pay.setFloat(7, pay.getPayment_amount());
			stmt_pay.setString(8, pay.getPayment_cc());
			stmt_pay.setString(9, pay_time);
			stmt_pay.setString(10, pay.getPaySID());
			stmt_pay.setString(11, pay.getPayflag());
			stmt_pay.setString(12, pay.getPaytype());
			stmt_pay.executeUpdate();

			String sqlStrSix = "insert payment(userid,orderid,paymentid,orderdesc,username,paystatus,"
					+ "payment_amount,payment_cc,createtime,paySID,payflag,paytype) values(" + pay.getUserid() + ",'"
					+ pay.getOrderid() + "','" + pay.getPaymentid() + "','" + pay.getOrderdesc() + "','"
					+ pay.getUsername() + "'," + pay.getPaystatus() + "," + pay.getPayment_amount() + "," + "'"
					+ pay.getPayment_cc() + "','" + pay_time + "','" + pay.getPaySID() + "','" + pay.getPayflag()
					+ "','" + pay.getPaytype() + "')";
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单7","order_change", sqlStrSix);
			// System.out.println("拆单7"+sqlStrSix);
			stmt_orderbuy = conn.prepareStatement(sql_orderbuy);
			stmt_orderbuy.setString(1, orderNew);
			stmt_orderbuy.setString(2, ob.getOrderNo());
			stmt_orderbuy.executeUpdate();

			stmt_source = conn.prepareStatement(sql_source);
			stmt_source.setString(1, orderNew);
			for (int i = 0; i < odIds.length; i++) {
				stmt_source.setString(i + 2, odIds[i]);
			}
			stmt_source.executeUpdate();
			//
			stmt_changegooddata = conn.prepareStatement(sql_changegooddata);
			stmt_changegooddata.setString(1, orderNew);
			for (int i = 0; i < gIds.length; i++) {
				stmt_changegooddata.setString(i + 2, gIds[i]);
			}
			stmt_changegooddata.executeUpdate();
			//
			if (order_ac != 0 || balance_pay != 0) {
				stmt_userprice = conn2.prepareStatement(sql_userprice);
				stmt_userprice.setInt(3, orderBeannew.getUserid());
				stmt_userprice.setDouble(1, balance_pay);
				stmt_userprice.setDouble(2, order_ac);
				stmt_userprice.executeUpdate();

				String sqlStrSeven = "update user set available_m=available_m+(" + balance_pay + "),"
						+ "applicable_credit=applicable_credit+(" + order_ac + ")  where id =" + ob.getUserid();
				// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
				// ob.getOrderNo(),"拆单8","user",sqlStrSeven);
				stmt_recharge = conn2.prepareStatement(sql_recharge);
				stmt_recharge.setInt(1, ob.getUserid());
				stmt_recharge.setDouble(2, balance_pay);
				stmt_recharge.setInt(3, 1);
				stmt_recharge.setString(4, "add: splitOrder:" + orderNew);
				stmt_recharge.setString(5, adminUserName);
				stmt_recharge.setInt(6, ob.getUserid());
				stmt_recharge.executeUpdate();

				String sqlStrEight = "insert into recharge_record(userid,price,type,remark,datatime,adminuser,usesign,balanceAfter) "
						+ "values(" + ob.getUserid() + "," + balance_pay + "," + 1 + ",'" + "add: splitOrder:"
						+ orderNew + "',now()," + "'" + ob.getUserid() + "',0,(select available_m from user where id="
						+ ob.getUserid() + "))";
				// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
				// ob.getOrderNo(), "拆单9", "orderinfo",sqlStrEight);
			} else {
				CallableStatement cstmtNew = null;
				cstmtNew = conn2.prepareCall(sql_checkupdate_ordersate);
				cstmtNew.setString(1, orderBeannew.getOrderNo());
				cstmtNew.executeUpdate();
			}
			CallableStatement cstmtold = null;
			cstmtold = conn2.prepareCall(sql_checkupdate_ordersate);
			cstmtold.setString(1, ob.getOrderNo());
			cstmtold.executeUpdate();

			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单10", "orderinfo","{call
			// checkupdate_ordersate('"+ob.getOrderNo()+"')}");
			// 再次修改订单状态
			// 原单
			stmt_oi_state = conn2.prepareStatement(sql_oi_state);
			// int s1=ob.getState();
			// System.out.println(s1);
			stmt_oi_state.setInt(1, ob.getState());
			stmt_oi_state.setString(2, ob.getOrderNo());
			stmt_oi_state.executeUpdate();
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单11", "orderinfo","update orderinfo set
			// state="+ob.getState()+" where order_no='"+ob.getOrderNo()+"'");
			// 新单
			stmt_oi_state2 = conn2.prepareStatement(sql_oi_state);
			// int s2=ob.getState();
			// System.out.println(s2);
			stmt_oi_state2.setInt(1, orderBeannew.getState());
			stmt_oi_state2.setString(2, orderNew);
			stmt_oi_state2.executeUpdate();
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单12", "orderinfo","update orderinfo set
			// state="+orderBeannew.getState()+" where
			// order_no='"+orderNew+"'");
			// goods_distribution商品分给新订单
			stmt_goods_distribution = conn.prepareStatement(sql_goods_distribution);
			stmt_goods_distribution_insert = conn.prepareStatement(sql_googd_distribution_insert);
			stmt_goods_distribution.setString(1, orderNew);
			stmt_goods_distribution.setString(2, ob.getOrderNo());
			if (!"".equals(odId_now)) {
				stmt_goods_distribution.executeUpdate();
			}
			// 批量插入到goods_distribution,order_product_source
			for (int m = 0; m < newDetailId.size(); m++) {
				// goods_distribution
				stmt_goods_distribution_insert.setString(1, orderBeannew.getOrderNo());
				stmt_goods_distribution_insert.setInt(2, newDetailId.get(m));
				stmt_goods_distribution_insert.setInt(3, orderDetailList.get(m));
				stmt_goods_distribution_insert.addBatch();
				// order_product_source更新
				// stmt_source_update.setInt(1, newDetailId.get(m));
				// stmt_source_update.setInt(2, orderDetailList.get(m));
			}
			stmt_goods_distribution_insert.executeBatch();
			// stmt_source_update.executeBatch();
			conn.commit();
			conn2.commit();
			// HashMap map = new HashMap();
			// HashMap map1 = new HashMap();
			// map.put("orderid", orderNew);
			// for (int i = 0; i < odIds.length; i++) {
			// stmt_od.setString(i+2, odIds[i]);
			// map1.put("id", odIds[i]);
			// }
			// String ccc = ParamToSqlUtil.genSql("order_details", 1, map,
			// map1);
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单", "order_details",ccc);
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单", "orderinfo",
			// "insert
			// orderinfo(order_no,user_id,product_cost,state,address_id,delivery_time,service_fee,ip,mode_transport,create_time,details_number,pay_price_three,foreign_freight,pay_price,pay_price_tow,currency,actual_ffreight,discount_amount,order_ac,purchase_number,server_update,client_update,remaining_price,packag_style,extra_freight,share_discount,extra_discount,coupon_discount)
			// values("
			// + ""+orderBeannew.getUserid()+",
			// "+orderBeannew.getProduct_cost()+", "+orderBeannew.getState()+",
			// "+orderBeannew.getDeliveryTime()+",
			// "+orderBeannew.getService_fee()+", "+orderBeannew.getIp()+",
			// "+orderBeannew.getMode_transport()+",
			// "+orderBeannew.getCreatetime()+",
			// "+orderBeannew.getDetails_number()+",
			// "+orderBeannew.getPay_price_three()+",
			// "+orderBeannew.getForeign_freight()+",
			// "+orderBeannew.getPay_price()+""+",
			// "+orderBeannew.getPay_price_tow()+""+",
			// "+orderBeannew.getCurrency()+",
			// "+orderBeannew.getActual_ffreight()+",
			// "+orderBeannew.getDiscount_amount()+",
			// "+orderBeannew.getOrder_ac()+",
			// "+orderBeannew.getPurchase_number()+",
			// "+orderBeannew.getServer_update()+",
			// "+orderBeannew.getClient_update()+",
			// "+orderBeannew.getRemaining_price()+",
			// "+orderBeannew.getPackage_style()+",
			// "+orderBeannew.getExtra_freight()+",
			// "+orderBeannew.getShare_discount()+",
			// "+orderBeannew.getExtra_discount()+",
			// "+orderBeannew.getCoupon_discount()+","
			// + ")");
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单", "orderinfo","update order_change set
			// orderno='"+orderNew+"' where orderno='"+ob.getOrderNo()+"' and
			// goodid in ("+gId_last+")");
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单", "orderinfo","{call
			// checkupdate_ordersate('"+ob.getOrderNo()+"')}");
			// SaveSyncTable.InsertOnlineDataInfo(ob.getUserid(),
			// ob.getOrderNo(), "拆单", "orderinfo","update orderinfo set
			// state="+ob.getState()+" where order_no='"+ob.getOrderNo()+"'");
			// 更新用户余额
		} catch (Exception e) {
			if (conn != null || conn2 != null)
			// if( conn2!=null)
			{
				try {
					conn.rollback();
					conn2.rollback();
					res = 0;
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			}
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
			if (stmt_packag_style != null) {
				try {
					stmt_packag_style.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_od != null) {
				try {
					stmt_od.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_od2 != null) {
				try {
					stmt_od2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_address != null) {
				try {
					stmt_address.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_adod_new != null) {
				try {
					stmt_adod_new.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_change != null) {
				try {
					stmt_change.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_change2 != null) {
				try {
					stmt_change2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_paymentconfirm != null) {
				try {
					stmt_paymentconfirm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_pay != null) {
				try {
					stmt_pay.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_orderbuy != null) {
				try {
					stmt_orderbuy.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_source != null) {
				try {
					stmt_source.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_changegooddata != null) {
				try {
					stmt_changegooddata.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_userprice != null) {
				try {
					stmt_userprice.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_recharge != null) {
				try {
					stmt_recharge.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_oi_state != null) {
				try {
					stmt_oi_state.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_oi_state2 != null) {
				try {
					stmt_oi_state2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt_goods_distribution != null) {
				try {
					stmt_goods_distribution.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn2);
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	public double getExchangeRate(int userId, String currency) {
		String sql = "select (select  exchange_rate from exchange_rate where country=(select currency from user where user.id=?))/(select exchange_rate from exchange_rate where country=?)    exchange";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double exchangeRate = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.setString(2, currency);
			rs = stmt.executeQuery();
			if (rs.next()) {
				exchangeRate = rs.getDouble("exchange");
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
		return exchangeRate;
	}

	@Override
	public List<OrderProductSource> getOrderProductSource(String orderid, String[] orderdtlIds) {
		String sql = "SELECT * FROM order_product_source WHERE orderid = ? ";// and
		// od_id
		// =
		// ?
		// ";
		// AND od_id IN ?
		if (orderdtlIds.length == 1) {
			sql += " and od_id=?";
		} else if (orderdtlIds.length > 1) {
			for (int i = 0; i < orderdtlIds.length; i++) {
				if (i == 0) {
					sql += " and ( od_id=? ";
				} else if (i == orderdtlIds.length - 1) {
					sql += " or od_id=? )";
				} else {
					sql += " or od_id = ? ";
				}
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderProductSource> OrderProductSourceList = new ArrayList<OrderProductSource>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			for (int i = 0; i < orderdtlIds.length; i++) {
				stmt.setString(i + 2, orderdtlIds[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				OrderProductSource orderProductSource = new OrderProductSource();
				orderProductSource.setPurchaseState(rs.getInt("purchase_state"));
				orderProductSource.setBuycount(rs.getInt("buycount"));
				orderProductSource.setUsecount(rs.getInt("usecount"));
				orderProductSource.setOrderid(rs.getString("orderid"));
				orderProductSource.setOdId(rs.getInt("od_id"));

				OrderProductSourceList.add(orderProductSource);
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
		return OrderProductSourceList;
	}

	public List<Integer> getOrderDetailPurchaseByOdids(String odids) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select purchase_state from order_details where id in (" + odids + ")";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				int state = rs.getInt("purchase_state");
				list.add(state);
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
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 根据orderNo,goodsid查询出需要拆分的订单
	 *
	 * @param orderNo
	 * @param goodsid
	 * @return
	 */
	@Override
	public OrderDetailsBean getDropShipOrdersDetails(String orderNo, String goodsid) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select id,userid,goodsid,goodsdata_id,goodsname,car_url,car_img,car_type,orderid,dropshipid,delivery_time,yourorder,goodsprice,goodsfreight,");
		sqlBuffer.append(
				"checkproduct_fee,checkprice_fee,actual_freight,actual_price,actual_weight,actual_volume,state,fileupload,od_state,createtime,");
		sqlBuffer.append(
				"paytime,freight_free,purchase_state,purchase_time,purchase_confirmation,remark,sprice,goods_class,extra_freight,buy_for_me,");
		sqlBuffer.append(
				"od_bulk_volume,od_total_weight,discount_ratio,flag,goodscatid,isAuto,isFeight,checked from order_details where orderid=? and id=? and state !=2 ");
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OrderDetailsBean orderDetailsBean = null;
		try {
			stmt = conn.prepareStatement(sqlBuffer.toString());
			stmt.setString(1, orderNo);
			stmt.setString(2, goodsid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				orderDetailsBean = new OrderDetailsBean();
				orderDetailsBean.setId(rs.getInt("id"));
				orderDetailsBean.setUserid(rs.getInt("userid"));
				orderDetailsBean.setGoodsid(rs.getInt("goodsid"));
				orderDetailsBean.setGoodsname(rs.getString("goodsname"));
				orderDetailsBean.setCar_url(rs.getString("car_url"));
				orderDetailsBean.setCar_img(rs.getString("car_img"));
				orderDetailsBean.setCar_type(rs.getString("car_type"));
				orderDetailsBean.setOrderid(rs.getString("orderid"));
				orderDetailsBean.setDropshipid(rs.getString("dropshipid"));
				orderDetailsBean.setDelivery_time(rs.getString("delivery_time"));
				orderDetailsBean.setYourorder(rs.getInt("yourorder"));
				orderDetailsBean.setGoodsprice(rs.getString("goodsprice"));
				orderDetailsBean.setGoods_freight(rs.getString("goodsfreight"));
				orderDetailsBean.setCheckproduct_fee(rs.getInt("checkproduct_fee"));
				orderDetailsBean.setCheckprice_fee(rs.getInt("checkprice_fee"));
				orderDetailsBean.setActual_freight(rs.getDouble("actual_freight"));
				orderDetailsBean.setActual_price(rs.getDouble("actual_price"));
				orderDetailsBean.setActual_weight(rs.getString("actual_weight"));
				orderDetailsBean.setActual_volume(rs.getString("actual_volume"));
				orderDetailsBean.setState(rs.getInt("state"));
				orderDetailsBean.setFileupload(rs.getString("fileupload"));
				orderDetailsBean.setOd_state(rs.getInt("od_state"));
				orderDetailsBean.setPaytime(rs.getString("paytime"));
				orderDetailsBean.setFreight_free(rs.getInt("freight_free"));
				orderDetailsBean.setPurchase_state(rs.getInt("purchase_state"));
				orderDetailsBean.setPurchase_time(rs.getString("purchase_time"));
				orderDetailsBean.setPurchase_confirmation(rs.getString("purchase_confirmation"));
				orderDetailsBean.setSprice(rs.getString("sprice"));
				orderDetailsBean.setGoods_class(rs.getInt("goods_class"));
				orderDetailsBean.setExtra_freight(rs.getDouble("extra_freight"));
				orderDetailsBean.setBuy_for_me(rs.getInt("buy_for_me"));
				orderDetailsBean.setOd_bulk_volume(rs.getString("od_bulk_volume"));
				orderDetailsBean.setOd_total_weight(rs.getDouble("od_total_weight"));
				orderDetailsBean.setDiscount_ratio(rs.getDouble("discount_ratio"));
				orderDetailsBean.setFlag(rs.getInt("flag"));
				orderDetailsBean.setGoodscatid(rs.getString("goodscatid"));
				orderDetailsBean.setIsAuto(rs.getInt("isAuto"));
				orderDetailsBean.setIsFeight(rs.getInt("isFeight"));
				orderDetailsBean.setChecked(rs.getInt("checked"));
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
		return orderDetailsBean;
	}

	/**
	 * 得到地址信息(DropShip拆单)
	 */
	@Override
	public OrderAddress getOrderAddressByOrderNo(String orderNo) {
		String sql = "select id ,addressID,orderNo,country ,statename,address,address2,phoneNumber,zipcode,Adstatus,Updatetimr,admUserID,street,recipients,flag from  order_address where orderNo=? ";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OrderAddress orderAddress = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				orderAddress = new OrderAddress();
				orderAddress.setId(rs.getInt("id"));
				orderAddress.setAddressId(rs.getString("addressID"));
				orderAddress.setOrderNo(rs.getString("orderNo"));
				orderAddress.setCountry(rs.getString("country"));
				orderAddress.setStatename(rs.getString("statename"));
				orderAddress.setAddress(rs.getString("address"));
				orderAddress.setAddress2(rs.getString("address2"));
				orderAddress.setPhoneNumber(rs.getString("phoneNumber"));
				orderAddress.setZipcode(rs.getString("zipcode"));
				orderAddress.setAdstatus(rs.getString("Adstatus"));
				orderAddress.setUpdatetimr(rs.getString("Updatetimr"));
				orderAddress.setAdmUserID(rs.getString("admUserID"));
				orderAddress.setStreet(rs.getString("street"));
				orderAddress.setRecipients(rs.getString("recipients"));
				orderAddress.setFlag(rs.getInt("flag"));
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
		return orderAddress;
	}

	/**
	 * 添加订单地址信息(DropShip拆单)
	 */
	@Override
	public void addOrderAddress(OrderAddress orderAddress) {
		String sql = "insert order_address(AddressID,orderNo,Country,statename,address,address2,phoneNumber,zipcode,Adstatus,Updatetimr,admUserID,street,recipients,flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		try {
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlTwo = "insert order_address(AddressID,orderNo,Country,statename,address,address2,phoneNumber,"
						+ "zipcode,Adstatus,Updatetimr,admUserID,street,recipients,flag) values(" + orderAddress.getId()
						+ ",'" + orderAddress.getOrderNo() + "','" + orderAddress.getCountry() + "','"
						+ orderAddress.getStatename() + "','" + orderAddress.getAddress() + "','"
						+ orderAddress.getAddress2() + "','" + orderAddress.getPhoneNumber() + "','"
						+ orderAddress.getZipcode() + "','" + orderAddress.getAdstatus() + "','"
						+ orderAddress.getUpdatetimr() + "','" + orderAddress.getAdmUserID() + "','"
						+ orderAddress.getStreet() + "','" + orderAddress.getRecipients() + "',"
						+ orderAddress.getFlag() + ")";
				SaveSyncTable.InsertOnlineDataInfo(0, orderAddress.getOrderNo(), "dropship拆单", "order_address", sqlTwo);
			} else {
				stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(1, orderAddress.getId());
				stmt.setString(2, orderAddress.getOrderNo());
				stmt.setString(3, orderAddress.getCountry());
				stmt.setString(4, orderAddress.getStatename());
				stmt.setString(5, orderAddress.getAddress());
				stmt.setString(6, orderAddress.getAddress2());
				stmt.setString(7, orderAddress.getPhoneNumber());
				stmt.setString(8, orderAddress.getZipcode());
				stmt.setString(9, orderAddress.getAdstatus());
				stmt.setString(10, orderAddress.getUpdatetimr());
				stmt.setString(11, orderAddress.getAdmUserID());
				stmt.setString(12, orderAddress.getStreet());
				stmt.setString(13, orderAddress.getRecipients());
				stmt.setInt(14, orderAddress.getFlag());
				stmt.executeUpdate();
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
	}

	/**
	 * 更新OrderDetails 中的dropshipid
	 */
	@Override
	public void updateOrderDetails(OrderDetailsBean orderDetailsBean) {
		String sql = "update order_details set dropshipid =? where orderid = ? and id = ?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		try {
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlStr = "update order_details set dropshipid ='" + orderDetailsBean.getDropshipid()
						+ "' where orderid = '" + orderDetailsBean.getOrderid() + "' and id = "
						+ orderDetailsBean.getGoodsid();
				SaveSyncTable.InsertOnlineDataInfo(0, orderDetailsBean.getOrderid(), "dropship拆单", "order_details",
						sqlStr);
			} else {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, orderDetailsBean.getDropshipid());
				stmt.setString(2, orderDetailsBean.getOrderid());
				stmt.setLong(3, orderDetailsBean.getGoodsid());
				stmt.executeUpdate();
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
	}

	/**
	 * 根据orderNo 获取DropShip Order信息
	 */
	@Override
	public List<Dropshiporder> getDropShipOrderList(String parentOrderNo) {

		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select orderid,parent_order_no,child_order_no,user_id,address_id,delivery_time,packag_style,mode_transport,service_fee,product_cost,domestic_freight,");
		sqlBuffer.append(
				"foreign_freight,actual_allincost,pay_price,pay_price_tow,pay_price_three,actual_ffreight,remaining_price,actual_volume,actual_weight,custom_discuss_other,");
		sqlBuffer.append(
				"custom_discuss_fright,transport_time,state,cancel_obj,expect_arrive_time,arrive_time,create_time,client_update,server_update,ip,order_ac,purchase_number,");
		sqlBuffer.append(
				"ipnaddress,currency,discount_amount,purchase_days,actual_lwh,actual_weight_estimate,actual_freight_c,");
		sqlBuffer.append(
				"extra_freight,order_show,packag_number,orderRemark,orderpaytime,cashback,details_number from dropshiporder where parent_order_no= ?");

		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Dropshiporder> dropShipList = new ArrayList<Dropshiporder>();
		try {
			stmt = conn.prepareStatement(sqlBuffer.toString());
			stmt.setString(1, parentOrderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Dropshiporder dropshiporder = new Dropshiporder();
				dropshiporder.setOrderid(rs.getInt("orderid"));
				dropshiporder.setParentOrderNo(rs.getString("parent_order_no"));
				dropshiporder.setChildOrderNo(rs.getString("child_order_no"));
				dropshiporder.setUserId(rs.getInt("user_id"));
				dropshiporder.setAddressId(rs.getInt("address_id"));
				dropshiporder.setDeliveryTime(rs.getString("delivery_time"));
				dropshiporder.setPackagStyle(rs.getInt("packag_style"));
				dropshiporder.setModeTransport(rs.getString("mode_transport"));
				dropshiporder.setServiceFee(rs.getString("service_fee"));
				dropshiporder.setProductCost(rs.getString("product_cost"));
				dropshiporder.setDomesticFreight(rs.getString("domestic_freight"));
				dropshiporder.setForeignFreight(rs.getString("foreign_freight"));
				dropshiporder.setActualAllincost(rs.getDouble("actual_allincost"));
				dropshiporder.setPayPrice(rs.getString("pay_price"));
				dropshiporder.setPayPriceTow(rs.getString("pay_price_tow"));
				dropshiporder.setPayPriceThree(rs.getString("pay_price_three"));
				dropshiporder.setActualFfreight(rs.getString("actual_ffreight"));
				dropshiporder.setRemainingPrice(rs.getDouble("remaining_price"));
				dropshiporder.setActualVolume(rs.getString("actual_volume"));
				dropshiporder.setActualWeight(rs.getString("actual_weight"));
				dropshiporder.setCustomDiscussOther(rs.getString("custom_discuss_other"));
				dropshiporder.setCustomDiscussFright(rs.getString("custom_discuss_fright"));
				dropshiporder.setTransportTime(rs.getDate("transport_time"));
				dropshiporder.setState(rs.getString("state"));
				dropshiporder.setCancelObj(rs.getInt("cancel_obj"));
				dropshiporder.setExpectArriveTime(rs.getDate("expect_arrive_time"));
				dropshiporder.setArriveTime(rs.getDate("arrive_time"));
				dropshiporder.setCreateTime(rs.getDate("create_time"));
				dropshiporder.setClientUpdate(rs.getInt("client_update"));
				dropshiporder.setServerUpdate(rs.getInt("server_update"));
				dropshiporder.setIp(rs.getString("ip"));
				dropshiporder.setOrderAc(rs.getDouble("order_ac"));
				dropshiporder.setPurchaseNumber(rs.getInt("purchase_number"));
				dropshiporder.setDetailsNumber(rs.getInt("details_number"));
				dropshiporder.setIpnaddress(rs.getString("ipnaddress"));
				dropshiporder.setCurrency(rs.getString("currency"));
				dropshiporder.setDiscountAmount(rs.getDouble("discount_amount"));
				dropshiporder.setPurchaseDays(rs.getInt("purchase_days"));
				dropshiporder.setActualLwh(rs.getString("actual_lwh"));
				dropshiporder.setActualWeightEstimate(rs.getDouble("actual_weight_estimate"));
				dropshiporder.setActualFreightC(rs.getDouble("actual_freight_c"));
				dropshiporder.setExtraFreight(rs.getDouble("extra_freight"));
				dropshiporder.setOrderShow(rs.getInt("order_show"));
				dropshiporder.setPackagNumber(rs.getInt("packag_number"));
				dropshiporder.setOrderremark(rs.getString("orderRemark"));
				dropshiporder.setOrderpaytime(rs.getDate("orderpaytime"));
				dropshiporder.setCashback(rs.getDouble("cashback"));
				dropShipList.add(dropshiporder);
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
		return dropShipList;
	}

	/**
	 * 插入Dropshiporder记录(DropShip 拆单)
	 */
	@Override
	public void insertDropShiporder(Dropshiporder dropshiporder) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"insert dropshiporder(parent_order_no,child_order_no,user_id,address_id,delivery_time,packag_style,mode_transport,service_fee,product_cost,domestic_freight,foreign_freight,actual_allincost,");
		sqlBuffer.append(
				"pay_price,pay_price_tow,pay_price_three,actual_ffreight,remaining_price,actual_volume,actual_weight,custom_discuss_other,custom_discuss_fright,transport_time,state,cancel_obj,expect_arrive_time,arrive_time,");
		sqlBuffer.append(
				"create_time,client_update,server_update,ip,order_ac,purchase_number,ipnaddress,currency,discount_amount,purchase_days,actual_lwh,actual_weight_estimate,actual_freight_c,extra_freight,order_show,");
		sqlBuffer.append(
				"packag_number,orderRemark,orderpaytime,details_number,cashback) VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		try {
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlTwo = "insert dropshiporder(parent_order_no,child_order_no,user_id,address_id,delivery_time,"
						+ "packag_style,mode_transport,service_fee,product_cost,domestic_freight,foreign_freight,"
						+ "actual_allincost,pay_price,pay_price_tow,pay_price_three,actual_ffreight,remaining_price,"
						+ "actual_volume,actual_weight,custom_discuss_other,custom_discuss_fright,transport_time,state,"
						+ "cancel_obj,expect_arrive_time,arrive_time,create_time,client_update,server_update,ip,order_ac,"
						+ "purchase_number,ipnaddress,currency,discount_amount,purchase_days,actual_lwh,"
						+ "actual_weight_estimate,actual_freight_c,extra_freight,order_show,packag_number,orderRemark,"
						+ "orderpaytime,details_number,cashback) values('" + dropshiporder.getParentOrderNo() + "','"
						+ dropshiporder.getChildOrderNo() + "'," + dropshiporder.getUserId() + ","
						+ dropshiporder.getAddressId() + ", '" + dropshiporder.getDeliveryTime() + "',"
						+ dropshiporder.getPackagStyle() + ",'" + dropshiporder.getModeTransport() + "','"
						+ dropshiporder.getServiceFee() + "','" + dropshiporder.getProductCost() + "','"
						+ dropshiporder.getDomesticFreight() + "','" + dropshiporder.getForeignFreight() + "','"
						+ dropshiporder.getActualAllincost() + "','" + dropshiporder.getPayPrice() + "','"
						+ dropshiporder.getPayPriceTow() + "','" + dropshiporder.getPayPriceThree() + "','"
						+ dropshiporder.getActualFfreight() + "','" + dropshiporder.getRemainingPrice() + "','"
						+ dropshiporder.getActualVolume() + "','" + dropshiporder.getActualWeight() + "','"
						+ dropshiporder.getCustomDiscussOther() + "','" + dropshiporder.getCustomDiscussFright() + "','"
						+ dropshiporder.getTransportTime() + "','" + dropshiporder.getState() + "',"
						+ dropshiporder.getCancelObj() + ",'" + dropshiporder.getExpectArriveTime() + "','"
						+ dropshiporder.getArriveTime() + "','" + dropshiporder.getCreateTime() + "',"
						+ dropshiporder.getClientUpdate() + "," + dropshiporder.getServerUpdate() + ",'"
						+ dropshiporder.getIp() + "','" + dropshiporder.getState() + "','" + dropshiporder.getOrderAc()
						+ "'," + dropshiporder.getPurchaseNumber() + ",'" + dropshiporder.getIpnaddress() + "','"
						+ dropshiporder.getCurrency() + "','" + dropshiporder.getDiscountAmount() + "',"
						+ dropshiporder.getPurchaseDays() + ",'" + dropshiporder.getActualLwh() + "','"
						+ dropshiporder.getActualWeightEstimate() + "','" + dropshiporder.getActualFreightC() + "','"
						+ dropshiporder.getExtraFreight() + "'," + dropshiporder.getOrderShow() + ","
						+ dropshiporder.getPackagNumber() + ",'" + dropshiporder.getOrderAc() + "','"
						+ dropshiporder.getOrderremark() + "','" + dropshiporder.getOrderpaytime() + "',"
						+ dropshiporder.getDetailsNumber() + ",'" + dropshiporder.getCashback() + "')";
				SaveSyncTable.InsertOnlineDataInfo(dropshiporder.getUserId(), dropshiporder.getParentOrderNo(),
						"dropship拆单", "dropshiporder", sqlTwo);
			} else {
				stmt = conn.prepareStatement(sqlBuffer.toString(), Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, dropshiporder.getParentOrderNo());
				stmt.setString(2, dropshiporder.getChildOrderNo());
				stmt.setInt(3, dropshiporder.getUserId());
				stmt.setInt(4, dropshiporder.getAddressId());
				stmt.setString(5, dropshiporder.getDeliveryTime());
				stmt.setInt(6, dropshiporder.getPackagStyle());
				stmt.setString(7, dropshiporder.getModeTransport());
				stmt.setString(8, dropshiporder.getServiceFee());
				stmt.setString(9, dropshiporder.getProductCost());
				stmt.setString(10, dropshiporder.getDomesticFreight());
				stmt.setString(11, dropshiporder.getForeignFreight());
				stmt.setDouble(12, dropshiporder.getActualAllincost());

				stmt.setString(13, dropshiporder.getPayPrice());
				stmt.setString(14, dropshiporder.getPayPriceTow());
				stmt.setString(15, dropshiporder.getPayPriceThree());
				stmt.setString(16, dropshiporder.getActualFfreight());
				stmt.setDouble(17, dropshiporder.getRemainingPrice());
				stmt.setString(18, dropshiporder.getActualVolume());
				stmt.setString(19, dropshiporder.getActualWeight());
				stmt.setString(20, dropshiporder.getCustomDiscussOther());
				stmt.setString(21, dropshiporder.getCustomDiscussFright());
				stmt.setDate(22, (Date) dropshiporder.getTransportTime());
				stmt.setString(23, dropshiporder.getState());
				stmt.setInt(24, dropshiporder.getCancelObj());
				stmt.setDate(25, (Date) dropshiporder.getExpectArriveTime());
				stmt.setDate(26, (Date) dropshiporder.getArriveTime());

				stmt.setDate(27, (Date) dropshiporder.getCreateTime());
				stmt.setInt(28, dropshiporder.getClientUpdate());
				stmt.setInt(29, dropshiporder.getServerUpdate());
				stmt.setString(30, dropshiporder.getIp());
				stmt.setDouble(31, dropshiporder.getOrderAc());
				stmt.setInt(32, dropshiporder.getPurchaseNumber());
				stmt.setString(33, dropshiporder.getIpnaddress());
				stmt.setString(34, dropshiporder.getCurrency());
				stmt.setDouble(35, dropshiporder.getDiscountAmount());
				stmt.setInt(36, dropshiporder.getPurchaseDays());
				stmt.setString(37, dropshiporder.getActualLwh());
				stmt.setDouble(38, dropshiporder.getActualWeightEstimate());
				stmt.setDouble(39, dropshiporder.getActualFreightC());
				stmt.setDouble(40, dropshiporder.getExtraFreight());
				stmt.setInt(41, dropshiporder.getOrderShow());
				stmt.setInt(42, dropshiporder.getPackagNumber());
				stmt.setString(43, dropshiporder.getOrderremark());
				stmt.setDate(44, (Date) dropshiporder.getOrderpaytime());
				stmt.setInt(45, dropshiporder.getDetailsNumber());
				stmt.setDouble(46, dropshiporder.getCashback());
				stmt.executeUpdate();
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
	}

	/**
	 * 更新DropShiporder信息
	 */
	@Override
	public void updateDropShiporder(Dropshiporder dropshiporder) {
		String sql = "update dropshiporder set product_cost=?,foreign_freight=?,pay_price=?,pay_price_tow=?, details_number=?,actual_weight_estimate=? where child_order_no=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		try {
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String syncSql = "update dropshiporder set product_cost='" + dropshiporder.getProductCost() + "',"
						+ "	foreign_freight='" + dropshiporder.getForeignFreight() + "',pay_price='"
						+ dropshiporder.getPayPrice() + "'" + ",pay_price_tow='" + dropshiporder.getPayPriceTow()
						+ "',details_number=" + dropshiporder.getDetailsNumber() + "" + ",actual_weight_estimate="
						+ dropshiporder.getActualWeightEstimate() + " where child_order_no='"
						+ dropshiporder.getChildOrderNo() + "'";
				SaveSyncTable.InsertOnlineDataInfo(0, dropshiporder.getChildOrderNo(), "dropship拆单", "dropshiporder",
						syncSql);
			} else {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, dropshiporder.getProductCost());
				stmt.setString(2, dropshiporder.getForeignFreight());
				stmt.setString(3, dropshiporder.getPayPrice());
				stmt.setString(4, dropshiporder.getPayPriceTow());
				stmt.setInt(5, dropshiporder.getDetailsNumber());
				stmt.setDouble(6, dropshiporder.getActualWeightEstimate()==null ? 0:dropshiporder.getActualWeightEstimate());
				stmt.setString(7, dropshiporder.getChildOrderNo());
				stmt.executeUpdate();
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
	}

	/**
	 * 根据OrderNo获取订单列表信息(用来判断Drop Ship订单拆分是否只有一个子订单)
	 *
	 * @param orderNo
	 * @return
	 */
	@Override
	public List<OrderDetailsBean> getOrdersDetailsList(String orderNo,int noCancel) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select id,userid,goodsid,goodsdata_id,goodsname,car_url,car_img,car_type,orderid,dropshipid,delivery_time,yourorder,goodsprice,goodsfreight,");
		sqlBuffer.append(
				"checkproduct_fee,checkprice_fee,actual_freight,actual_price,actual_weight,actual_volume,state,fileupload,od_state,createtime,");
		sqlBuffer.append(
				"paytime,freight_free,purchase_state,purchase_time,purchase_confirmation,remark,sprice,goods_class,extra_freight,buy_for_me,");
		sqlBuffer.append(
				"od_bulk_volume,od_total_weight,discount_ratio,flag,goodscatid,isAuto,isFeight,checked from order_details where orderid=?");
		if(noCancel > 0){
			sqlBuffer.append(
					" and dropshipid not in(select child_order_no from dropshiporder where parent_order_no = ? and state in(-1,6))");
		}
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> detailsList = new ArrayList<OrderDetailsBean>();
		OrderDetailsBean orderDetailsBean = null;
		try {
			stmt = conn.prepareStatement(sqlBuffer.toString());
			stmt.setString(1, orderNo);
			if(noCancel > 0){
				stmt.setString(2, orderNo);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				orderDetailsBean = new OrderDetailsBean();
				orderDetailsBean.setId(rs.getInt("id"));
				orderDetailsBean.setUserid(rs.getInt("userid"));
				orderDetailsBean.setGoodsid(rs.getInt("goodsid"));
				orderDetailsBean.setGoodsname(rs.getString("goodsname"));
				orderDetailsBean.setCar_url(rs.getString("car_url"));
				orderDetailsBean.setCar_img(rs.getString("car_img"));
				orderDetailsBean.setCar_type(rs.getString("car_type"));
				orderDetailsBean.setOrderid(rs.getString("orderid"));
				orderDetailsBean.setDropshipid(rs.getString("dropshipid"));
				orderDetailsBean.setDelivery_time(rs.getString("delivery_time"));
				orderDetailsBean.setYourorder(rs.getInt("yourorder"));
				orderDetailsBean.setGoodsprice(rs.getString("goodsprice"));
				orderDetailsBean.setGoods_freight(rs.getString("goodsfreight"));
				orderDetailsBean.setCheckproduct_fee(rs.getInt("checkproduct_fee"));
				orderDetailsBean.setCheckprice_fee(rs.getInt("checkprice_fee"));
				orderDetailsBean.setActual_freight(rs.getDouble("actual_freight"));
				orderDetailsBean.setActual_price(rs.getDouble("actual_price"));
				orderDetailsBean.setActual_weight(rs.getString("actual_weight"));
				orderDetailsBean.setActual_volume(rs.getString("actual_volume"));
				orderDetailsBean.setState(rs.getInt("state"));
				orderDetailsBean.setFileupload(rs.getString("fileupload"));
				orderDetailsBean.setOd_state(rs.getInt("od_state"));
				orderDetailsBean.setPaytime(rs.getString("paytime"));
				orderDetailsBean.setFreight_free(rs.getInt("freight_free"));
				orderDetailsBean.setPurchase_state(rs.getInt("purchase_state"));
				orderDetailsBean.setPurchase_time(rs.getString("purchase_time"));
				orderDetailsBean.setPurchase_confirmation(rs.getString("purchase_confirmation"));
				orderDetailsBean.setSprice(rs.getString("sprice"));
				orderDetailsBean.setGoods_class(rs.getInt("goods_class"));
				orderDetailsBean.setExtra_freight(rs.getDouble("extra_freight"));
				orderDetailsBean.setBuy_for_me(rs.getInt("buy_for_me"));
				orderDetailsBean.setOd_bulk_volume(rs.getString("od_bulk_volume"));
				orderDetailsBean.setOd_total_weight(rs.getDouble("od_total_weight"));
				orderDetailsBean.setDiscount_ratio(rs.getDouble("discount_ratio"));
				orderDetailsBean.setFlag(rs.getInt("flag"));
				orderDetailsBean.setGoodscatid(rs.getString("goodscatid"));
				orderDetailsBean.setIsAuto(rs.getInt("isAuto"));
				orderDetailsBean.setIsFeight(rs.getInt("isFeight"));
				orderDetailsBean.setChecked(rs.getInt("checked"));
				detailsList.add(orderDetailsBean);
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
		return detailsList;
	}

	/**
	 * 更新DropShipOrder 表的状态
	 */
	@Override
	public int updateDropShipOrderStates(String dropShipOrderId) {
		String checkupdatedropship_orderstates = "{call checkupdatedropship_orderstates(?)}";
		Connection conn = DBHelper.getInstance().getConnection2();
		CallableStatement callSta = null;
		int res = 0;
		try {
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlStr = "{call checkupdatedropship_orderstates('" + dropShipOrderId + "')}";
				SaveSyncTable.InsertOnlineDataInfo(0, dropShipOrderId, "dropship拆单", "dropshiporder", sqlStr);
			} else {
				callSta = conn.prepareCall(checkupdatedropship_orderstates);
				callSta.setString(1, dropShipOrderId);
				callSta.executeUpdate();
			}
			res = 1;

		} catch (SQLException e) {
			res = 0;
			e.printStackTrace();
		} finally {
			if (callSta != null) {
				try {
					callSta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public int getOrderdetailYouorder(String orderId) {
		// StringBuffer sqlBuffer=new StringBuffer();
		String sql = "SELECT t.`yourorder`,t.`id` FROM `order_details` t WHERE t.`id` = ?";
		// sqlBuffer.append("SELECT t.`yourorder`,t.`id` FROM `order_details` t
		// WHERE t.`id` = ?");
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int yourOrder = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				yourOrder = rs.getInt("yourorder");
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
		return yourOrder;
	}

	@Override
	public boolean orderSplitByOrderNo(int userId, String orderNoOld, String orderNoNew, int updateGoodsId,
	                                   int updateGoodsNum) {

		Connection conn2 = DBHelper.getInstance().getConnection2();
		String sql = "call order_split_by_orderno(?,?,?,?) ;";
		CallableStatement cStmt = null;
		ResultSet rs = null;
		try {
			if (GetConfigureInfo.openSync()) {
				String sqlStr = "{call order_split_by_orderno('" + orderNoOld + "','" + orderNoNew + "',"
						+ updateGoodsId + "," + updateGoodsNum + ")}"; // 执行SQL体
				SaveSyncTable.InsertOnlineDataInfo(userId, orderNoOld, "拆单,新订单:" + orderNoNew, "orderinfo", sqlStr);
				return true;
			} else {
				cStmt = conn2.prepareCall(sql);
				cStmt.setString(1, orderNoOld);
				cStmt.setString(2, orderNoNew);
				cStmt.setInt(3, updateGoodsId);
				cStmt.setInt(4, updateGoodsNum);
				return cStmt.execute();
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
			if (cStmt != null) {
				try {
					cStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn2);
		}
		return false;
	}

	@Override
	public int insertIntoPayment(int userId, String nwOrderNo, String oldOrderNo) {
		Connection conn2 = DBHelper.getInstance().getConnection2();
		String sql = "INSERT into payment "
				+ "( userid, orderid, paymentid, payment_amount, payment_cc, orderdesc, username, "
				+ "paystatus, createtime , paySID, payflag, paytype,  payment_other  ,paymentno,transaction_fee ) "
				+ "select  userid, '" + nwOrderNo + "', paymentid,  (select max(pay_price) from orderinfo "
				+ "where order_no='" + nwOrderNo + "'), payment_cc, orderdesc, username, paystatus, NOW(),"
				+ " paySID, payflag, 3 , 3,paymentno,0 from payment  where  payment.orderid='" + oldOrderNo
				+ "' limit 1;";
		Statement stmt = null;
		try {
			if (GetConfigureInfo.openSync()) {
				SaveSyncTable.InsertOnlineDataInfo(userId, nwOrderNo, "拆单支付", "payment", sql);
			} else {
				stmt = conn2.createStatement();
				stmt.execute(sql);
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
			DBHelper.getInstance().closeConnection(conn2);
		}
		return 0;
	}

	@Override
	public int cancelNewOrder(int userId, String nwOrderNo) {

		Connection conn2 = DBHelper.getInstance().getConnection2();
		// recharge_record表插入
		String querySql = "select max(pay_price) from orderinfo where order_no='" + nwOrderNo + "'";

		Statement stmtQr = null;
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			stmtQr = conn2.createStatement();
			rs = stmtQr.executeQuery(querySql);
			if (rs.next()) {
				String available_mStr = rs.getString(1);
				float available_m = 0;
				if (available_mStr != null && !"".equals(available_mStr)) {
					available_m = Float.valueOf(available_mStr);
				}
				System.out.println("当前客户:" + userId + ",新订单支付金额:" + available_m);
				String sqlRcRd = "insert into recharge_record(userid,price,type,remark,remark_id,datatime,usesign,"
						+ "currency,balanceAfter) select user_id,pay_price,1 as type,"
						+ "concat('add: system closeOrder:',order_no) as remark,"
						+ "order_no,sysdate(),0 as usesign,currency,cast(((select available_m from user where id = "
						+ userId + ") + " + available_m + ") as DECIMAL(11,2)) as balanceAfter"
						+ " from orderinfo where order_no = '" + nwOrderNo + "'";
				String sqlOdIf = "update orderinfo set state = '-1' where order_no = '" + nwOrderNo + "'";
				String sqlOdDs = "update order_details set state = -1 where orderid = '" + nwOrderNo + "'";
				if (GetConfigureInfo.openSync()) {
					SaveSyncTable.InsertOnlineDataInfo(userId, nwOrderNo, "拆单1取消插入余额变更记录", "recharge_record", sqlRcRd);
					SaveSyncTable.InsertOnlineDataInfo(userId, nwOrderNo, "拆单2取消修改订单状态", "orderinfo", sqlOdIf);
					SaveSyncTable.InsertOnlineDataInfo(userId, nwOrderNo, "拆单3取消修改详情状态", "order_details", sqlOdDs);
					count = 4;
				} else {
					conn2.setAutoCommit(false);
					stmt = conn2.createStatement();
					// stmt.addBatch(sqlRcRd);
					stmt.addBatch(sqlOdIf);
					stmt.addBatch(sqlOdDs);
					int[] res = stmt.executeBatch();
					count = res.length;
					if (count > 0) {
						conn2.commit();
					} else {
						conn2.rollback();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtQr != null) {
				try {
					stmt.close();
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
			DBHelper.getInstance().closeConnection(conn2);
		}
		return count;
	}

	@Override
	public OrderBean getOrderInfo(String orderNo) {
		String sql = "select order_no,user_id,product_cost,state,delivery_time,service_fee,ip,"
				+ "mode_transport,create_time,details_number,pay_price_three,foreign_freight, "
				+ "(case when  orderinfo.state=-1 then (select ifnull(sum( payment.payment_amount),0) from payment where orderid=order_no and paystatus=1) "
				+ "else orderinfo.pay_price end  ) pay_price,pay_price_tow,currency,actual_ffreight, "
				+ "discount_amount,order_ac,pay_price,purchase_number,server_update,client_update from orderinfo "
				+ "where orderinfo.order_no= ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OrderBean ob = new OrderBean();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ob.setUserid(rs.getInt("user_id"));
				ob.setOrderNo(rs.getString("order_no"));
				ob.setProduct_cost(rs.getString("product_cost"));
				ob.setCreatetime(rs.getString("create_time"));
				ob.setState(rs.getInt("state"));
				ob.setMode_transport(rs.getString("mode_transport"));
				ob.setService_fee(rs.getString("service_fee"));
				String foreign_freight_ = rs.getString("foreign_freight");
				ob.setForeign_freight(Utility.getStringIsNull(foreign_freight_) ? foreign_freight_ : "0");
				ob.setActual_ffreight(rs.getString("actual_ffreight"));
				ob.setService_fee(rs.getString("service_fee"));
				ob.setIp(rs.getString("ip"));
				ob.setPurchase_number(rs.getInt("purchase_number"));
				ob.setDetails_number(rs.getInt("details_number"));
				ob.setCurrency(rs.getString("currency"));
				ob.setPay_price_three(rs.getString("pay_price_three"));
				ob.setPay_price_tow(rs.getString("pay_price_tow"));
				ob.setPay_price(rs.getDouble("pay_price"));
				int delivery_time = rs.getInt("delivery_time");
				ob.setOrder_ac(rs.getDouble("order_ac"));
				ob.setDiscount_amount(rs.getDouble("discount_amount"));
				ob.setDeliveryTime(delivery_time);
				ob.setServer_update(rs.getInt("server_update"));
				ob.setClient_update(rs.getInt("client_update"));
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

		return ob;
	}

	@Override
	public List<Object[]> queryLocalOrderDetails(String orderNo) {
		String sql = "select od.orderid,od.id,od.yourorder,od.goodsprice,goodsname,googs_img,goods_type,(select img from goods_typeimg where gc.id = goods_id) goods_typeimg from order_details od,goods_car gc where od.state!=2 and  gc.id=od.goodsid and ";
		sql += " od.orderid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		DecimalFormat df = new DecimalFormat("######0.00");
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String price = rs.getString("od.goodsprice");
				int number = rs.getInt("od.yourorder");
				double sprice = 0;
				if (Utility.getStringIsNull(price)) {
					sprice = Double.parseDouble(price);
				}
				String googs_img = rs.getString("googs_img");
				if (googs_img == null || "".equals(googs_img)) {
					googs_img = "";
				} else {
					if (googs_img.indexOf(".jpg") != googs_img.lastIndexOf(".jpg")) {
						googs_img = googs_img.substring(0, googs_img.indexOf(".jpg") + ".jpg".length());
					} else if (googs_img.indexOf("32x32") > -1) {
						googs_img = googs_img.replace("32x32", "400x400");
					} else if (googs_img.indexOf("60x60") > -1) {
						googs_img = googs_img.replace("60x60", "400x400");
					}
				}
				Object[] objects = { rs.getString("od.orderid"), rs.getInt("od.id"), number, df.format(number * sprice),
						rs.getString("goodsname"), googs_img, rs.getString("goods_type"),
						rs.getString("goods_typeimg") };
				googs_img = null;
				spiderlist.add(objects);
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
		return spiderlist;
	}

	@Override
	public boolean checkAndUpdateOrderState(String oldOrder, String newOrder) {
		Connection conn2 = DBHelper.getInstance().getConnection2();
		String sql1 = "call checkupdate_ordersate(?)";
		String sql2 = "call checkupdate_ordersate(?)";
		CallableStatement cStmt1 = null;
		CallableStatement cStmt2 = null;
		try {
			if (GetConfigureInfo.openSync()) {
				String sqlStr1 = "{call checkupdate_ordersate('" + oldOrder + "')}"; // 执行SQL体
				String sqlStr2 = "{call checkupdate_ordersate('" + newOrder + "')}"; // 执行SQL体
				SaveSyncTable.InsertOnlineDataInfo(0, oldOrder, "拆单,更新原订单状态", "orderinfo", sqlStr1);
				SaveSyncTable.InsertOnlineDataInfo(0, newOrder, "拆单,更新新订单状态", "orderinfo", sqlStr2);
				return true;
			} else {
				cStmt1 = conn2.prepareCall(sql1);
				cStmt1.setString(1, oldOrder);
				cStmt1.execute();
				cStmt2 = conn2.prepareCall(sql2);
				cStmt2.setString(1, newOrder);
				cStmt2.execute();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cStmt1 != null) {
				try {
					cStmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cStmt2 != null) {
				try {
					cStmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn2);
		}
		return false;

	}

	@Override
	public int updateWarehouseInfo(String oldOrder, String newOrder, List<Integer> goodsIds) {

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		String sql = "update id_relationtable set orderid =? where orderid = ? and goodid in(";
		PreparedStatement pStmt = null;
		for (int i = 0; i < goodsIds.size(); i++) {
			sql += "?";
		}
		sql += ")";
		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, newOrder);
			pStmt.setString(2, oldOrder);
			for (int i = 0; i < goodsIds.size(); i++) {
				pStmt.setInt(i + 3, goodsIds.get(i));
			}
			rs = pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pStmt != null) {
				try {
					pStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	/**
	 * 拆单取消后取消的商品如果有使用库存则还原库存
	 */
	@Override
	public void cancelInventory(String[] ids) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			for (String id : ids) {
				sql = "SELECT i.goods_p_price,od.car_img,i.barcode as i_barcode,ir.barcode,od.orderid,od.goodsid,li.in_id,li.id,li.od_id,i.remaining,li.lock_remaining,i.can_remaining,i.flag,li.flag as li_flag FROM lock_inventory li "
						+ "INNER JOIN order_details od ON li.od_id=od.id LEFT JOIN inventory i ON li.in_id=i.id left join id_relationtable ir on od.orderid=ir.orderid and od.goodsid=ir.goodid WHERE li.is_delete=0 AND od.id="
						+ id + "";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					int flag = rs.getInt("li_flag");
					String goods_p_price = rs.getString("goods_p_price");
					if (goods_p_price.indexOf(",") > -1) {
						goods_p_price = goods_p_price.split(",")[0];
					}
					if (flag == 0) {
						// 如果商品还没有移库，只需把锁定的库存释放回去
						sql = "update inventory set can_remaining=(" + rs.getInt("can_remaining")
								+ rs.getInt("lock_remaining") + ") where id=" + rs.getInt("in_id") + "";
						stmt = conn.prepareStatement(sql);
					} else {
						// 商品已移库，把移库结果曾现给仓库，等待仓库移库
						sql = "insert into cancel_goods_inventory (orderid,goodsid,order_barcode,inventory_qty,inventory_barcode,od_id,car_img,createtime,in_id,i_flag,goods_p_price) values(?,?,?,?,?,?,?,now(),?,?,?)";
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, rs.getString("orderid"));
						stmt.setString(2, rs.getString("goodsid"));
						stmt.setString(3, rs.getString("barcode"));
						stmt.setInt(4, rs.getInt("lock_remaining"));
						stmt.setString(5, rs.getString("i_barcode"));
						stmt.setString(6, rs.getString("od_id"));
						stmt.setString(7, rs.getString("car_img"));
						stmt.setInt(8, rs.getInt("in_id"));
						stmt.setInt(9, rs.getInt("flag"));
						stmt.setDouble(10, Double.valueOf(goods_p_price));
					}
					stmt.executeUpdate();
					sql = "update lock_inventory set is_delete=1 where id=" + rs.getInt("id") + "";
					stmt = conn.prepareStatement(sql);
					stmt.executeUpdate();
				}
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
	}

	@Override
	public List<Object[]> getOrderDetails(String[] orderNos) {
		String sql = "select od.orderid,od.id,od.yourorder,od.goodsprice,od.goodsname ,REPLACE(od.car_img,'60x60','400x400') as googs_img," +
				"od.car_type as goods_type,(select img from goods_typeimg where od.goodsid = goods_id) goods_typeimg " +
				"from order_details od where ";
		if (orderNos.length == 2) {
			sql += "( od.orderid=? or od.orderid=? )";
		} else {
			sql += " od.orderid=? ";
		}
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Object[]> odIdlist = new ArrayList<Object[]>();
		DecimalFormat df = new DecimalFormat("######0.00");
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNos[0]);
			if (orderNos.length == 2) {
				stmt.setString(2, orderNos[1]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String price = rs.getString("od.goodsprice");
				int number = rs.getInt("od.yourorder");
				double sprice = 0;
				if (Utility.getStringIsNull(price)) {
					sprice = Double.parseDouble(price);
				}
				String googs_img = rs.getString("googs_img");
				if (googs_img == null || "".equals(googs_img)) {
					googs_img = "";
				} else {
					if (googs_img.indexOf(".jpg") != googs_img.lastIndexOf(".jpg")) {
						googs_img = googs_img.substring(0, googs_img.indexOf(".jpg") + ".jpg".length());
					} else if (googs_img.indexOf("32x32") > -1) {
						googs_img = googs_img.replace("32x32", "400x400");
					} else if (googs_img.indexOf("60x60") > -1) {
						googs_img = googs_img.replace("60x60", "400x400");
					}
				}
				Object[] objects = { rs.getString("od.orderid"), rs.getInt("od.id"), number, df.format(number * sprice),
						rs.getString("goodsname"), googs_img, rs.getString("goods_type"),
						rs.getString("goods_typeimg") };
				googs_img = null;
				odIdlist.add(objects);
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
		return odIdlist;
	}

	@Override
	public boolean orderSplitNomal(OrderBean orderBean, String orderNoOld, String orderNoNew, List<Integer> goodsIds) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement ppStamt = null;

		try {
			// 复制订单信息做订单前的数据保存信息
			// 1.订单拆单前，做数据保存信息
			// 2.复制订单信息充当临时订单
			// 3.统计拆单商品所有的原始价格，支付价格之和，给出预期结果，保存数据库
			// 4.执行拆单操作
			// 5.执行完成后，给出执行的结果并保存数据库
			// 6.如果是取消商品进余额，则调用统一接口进行客户余额变更

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ppStamt != null) {
				try {
					ppStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return false;
	}

	@Override
	public boolean addOrderInfoAndPaymentLog(String orderNo, Admuser admuser, int flag) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement ppOrStamt = null;
		PreparedStatement ppPyStamt = null;
		int count = 0;
		String orderSql = "insert into orderinfo_split_log(order_no,user_id,service_fee,product_cost,"
				+ "domestic_freight,foreign_freight,actual_allincost,pay_price,pay_price_tow,"
				+ "pay_price_three,actual_ffreight,remaining_price,state,order_ac,currency,"
				+ "share_discount,discount_amount,actual_freight_c,extra_freight,"
				+ "orderpaytime,cashback,extra_discount,coupon_discount,admin_id,is_new) "
				+ "select order_no,user_id,service_fee,product_cost,"
				+ "domestic_freight,foreign_freight,actual_allincost,pay_price,pay_price_tow,"
				+ "pay_price_three,actual_ffreight,remaining_price,state,order_ac,currency,"
				+ "share_discount,discount_amount,actual_freight_c,extra_freight,orderpaytime,"
				+ "cashback,extra_discount,coupon_discount," + admuser.getId() + " as admin_id," + flag + " as is_new"
				+ " from orderinfo where order_no = ?";
		String paymentSql = "insert into orderinfo_split_payment(userid,orderid,payment_amount,payment_cc,admin_id,is_new)"
				+ " select userid,orderid,payment_amount,payment_cc," + admuser.getId() + " as admin_id,0 as is_new"
				+ " from payment where orderid = ?";

		System.err.println("orderSql : " + orderSql);
		System.err.println("paymentSql : " + paymentSql);
		try {
			conn.setAutoCommit(false);
			ppOrStamt = conn.prepareStatement(orderSql);
			ppOrStamt.setString(1, orderNo);
			count = ppOrStamt.executeUpdate();
			if (count > 0) {
				if (flag == 0) {
					count = 0;
					ppPyStamt = conn.prepareStatement(paymentSql);
					ppPyStamt.setString(1, orderNo);
					count = ppPyStamt.executeUpdate();
				}
				if (count > 0) {
					conn.commit();
				} else {
					conn.rollback();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		} finally {
			if (ppOrStamt != null) {
				try {
					ppOrStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ppPyStamt != null) {
				try {
					ppPyStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return count > 0;
	}

	@Override
	public boolean saveOrderInfoLogByList(List<OrderBean> orderBeans, Admuser admuser) {

		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement ppStamt = null;
		boolean isSuccess = false;
		String sql = "insert into orderinfo_split_log(order_no,user_id,service_fee,product_cost,"
				+ "domestic_freight,foreign_freight,actual_allincost,pay_price,pay_price_tow,"
				+ "pay_price_three,actual_ffreight,remaining_price,state,order_ac,currency,"
				+ "share_discount,discount_amount,actual_freight_c,extra_freight,"
				+ "cashback,extra_discount,coupon_discount,admin_id) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			conn.setAutoCommit(false);
			ppStamt = conn.prepareStatement(sql);
			for (OrderBean odb : orderBeans) {
				int index = 1;
				ppStamt.setString(index++, odb.getOrderNo());
				ppStamt.setInt(index++, odb.getUserid());
				ppStamt.setString(index++, odb.getService_fee());
				ppStamt.setString(index++, odb.getProduct_cost());
				ppStamt.setString(index++, odb.getDomestic_freight());
				ppStamt.setString(index++, odb.getForeign_freight());
				ppStamt.setDouble(index++, odb.getActual_allincost());
				ppStamt.setDouble(index++, odb.getPay_price());
				ppStamt.setString(index++, odb.getPay_price_tow());
				ppStamt.setString(index++, odb.getPay_price_three());
				ppStamt.setString(index++, odb.getActual_ffreight());
				ppStamt.setDouble(index++, odb.getRemaining_price());
				ppStamt.setInt(index++, odb.getState());
				ppStamt.setDouble(index++, odb.getOrder_ac());
				ppStamt.setString(index++, odb.getCurrency());
				ppStamt.setDouble(index++, odb.getShare_discount());
				ppStamt.setDouble(index++, odb.getDiscount_amount());
				ppStamt.setDouble(index++, odb.getActual_freight_c());

				ppStamt.setDouble(index++, odb.getExtra_freight());
				ppStamt.setDouble(index++, odb.getCashback());
				ppStamt.setDouble(index++, odb.getExtra_discount());
				ppStamt.setDouble(index++, odb.getCoupon_discount());
				ppStamt.setInt(index++, admuser.getId());
				ppStamt.addBatch();
			}

			isSuccess = ppStamt.executeBatch().length == orderBeans.size();
			if (isSuccess) {
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		} finally {
			if (ppStamt != null) {
				try {
					ppStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return isSuccess;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean newOrderSplitFun(OrderBean orderBeanTemp, OrderBean odbeanNew,
	                                List<OrderDetailsBean> nwOrderDetails,String state, int isSplitNum) {

		String orderNoOld = orderBeanTemp.getOrderNo();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		Connection localConn = DBHelper.getInstance().getConnection();
		Statement ppStamt = null;
		Statement wStamt = null;
		Statement localStamt = null;
		boolean isSuccess = false;
		// 远程执行SQL队列
		List<String> remoteSqlList = new ArrayList<String>();
		StringBuffer insertOrderSql = new StringBuffer();
		// 插入新的订单信息SQL
		insertOrderSql.append("insert into orderinfo")
				.append("(order_no,user_id,address_id,delivery_time,packag_style,mode_transport,service_fee,product_cost,")
				.append("domestic_freight,foreign_freight,actual_allincost,pay_price,pay_price_tow,pay_price_three,actual_ffreight,")
				.append("remaining_price,actual_volume,actual_weight,custom_discuss_other,custom_discuss_fright,transport_time,")
				.append("state,cancel_obj,expect_arrive_time,arrive_time,create_time,client_update,server_update,ip,order_ac,")
				.append("purchase_number,details_number,ipnaddress,currency,discount_amount,purchase_days,actual_lwh,")
				.append("actual_weight_estimate,actual_freight_c,extra_freight,order_show,packag_number,orderRemark,")
				.append("orderpaytime,cashback,isDropshipOrder,share_discount,extra_discount,coupon_discount,grade_discount,exchange_rate,processingfee)")
				.append(" select '" + odbeanNew.getOrderNo() + "' as order_no,")
				.append("user_id,address_id,delivery_time,packag_style,mode_transport,'0' as service_fee,'")
				.append(odbeanNew.getProduct_cost() + "' as product_cost,")
				.append("'0' as domestic_freight,'0' as foreign_freight,0 as actual_allincost," + odbeanNew.getPay_price())
				.append(" as pay_price,'0' as pay_price_tow,'0' as pay_price_three,'0' as actual_ffreight,")
				.append("0 as remaining_price,'0' as actual_volume,'0' as actual_weight,'0' as custom_discuss_other,"
						+ "'0' as custom_discuss_fright,transport_time,")
				.append("state,cancel_obj,expect_arrive_time,arrive_time,create_time,client_update,server_update,ip,0 as order_ac,")
				.append("0 as purchase_number,"+ odbeanNew.getOrderDetail().size() +" as details_number,ipnaddress,currency," + odbeanNew.getDiscount_amount())
				.append(" as discount_amount,purchase_days,"+odbeanNew.getActual_lwh()+" as actual_lwh,")
				.append("actual_weight_estimate,0 as actual_freight_c,"+odbeanNew.getExtra_freight()+" as extra_freight,order_show,0 as packag_number,orderRemark,")
				.append("orderpaytime," + odbeanNew.getCashback() + " as cashback,isDropshipOrder," + odbeanNew.getShare_discount()
						+ " as share_discount,")
				.append(odbeanNew.getExtra_discount() + " as extra_discount," + odbeanNew.getCoupon_discount())
				.append(" as coupon_discount," + odbeanNew.getGradeDiscount() + " as grade_discount,"+odbeanNew.getExchange_rate()+","+odbeanNew.getProcessingfee()+" as processingfee")
				.append(" from orderinfo where order_no = '" + orderNoOld + "'");

		remoteSqlList.add(insertOrderSql.toString());
		insertOrderSql = null;
		// 更新原订单的商品总价，支付金额和折扣金额
		String updateOrSql = "update orderinfo set product_cost='" + orderBeanTemp.getProduct_cost() + "',pay_price="
				+ orderBeanTemp.getPay_price() + ",discount_amount=" + orderBeanTemp.getDiscount_amount()
				+ ",share_discount=" + orderBeanTemp.getShare_discount() + ",extra_discount="
				+ orderBeanTemp.getExtra_discount() + ",coupon_discount=" + orderBeanTemp.getCoupon_discount()
				+ ",grade_discount=" + orderBeanTemp.getGradeDiscount() + ",cashback=" + orderBeanTemp.getCashback()
				+ ",extra_freight=" + orderBeanTemp.getExtra_freight()
				+ ",actual_lwh=" + orderBeanTemp.getActual_lwh()
				+ ",processingfee=" + orderBeanTemp.getProcessingfee()
				+ " where order_no = '"
				+ orderBeanTemp.getOrderNo() + "'";
		remoteSqlList.add(updateOrSql);
		updateOrSql = null;
		// 删除订单地址SQL
		String deleteAddressSql = "delete from order_address  where orderNo= '" + odbeanNew.getOrderNo() + "'";
		remoteSqlList.add(deleteAddressSql);
		deleteAddressSql = null;
		// 复制原订单地址信息并更新订单号到新订单
		String insertAddressSql = "insert into  order_address"
				+ "(AddressID, orderNo, Country, statename, address, address2, phoneNumber, zipcode, Adstatus, Updatetimr, "
				+ "admUserID, street, recipients) select  AddressID, '" + odbeanNew.getOrderNo()
				+ "' as orderNo, Country, statename, address, address2, phoneNumber, zipcode, Adstatus, Updatetimr, "
				+ "admUserID, street, recipients from order_address where orderNo='" + orderNoOld + "'";
		remoteSqlList.add(insertAddressSql);
		insertAddressSql = null;

		// 补充新的支付信息
		String deletePaymentSql = "delete from payment where payment.orderid='" + odbeanNew.getOrderNo() + "'";
		remoteSqlList.add(deletePaymentSql);
		deletePaymentSql = null;
		String insertPaymentSql = "insert into payment(userid,orderid,paymentid,payment_amount,"
				+ "payment_cc,orderdesc,username,paystatus,createtime,paySID,payflag,paytype,"
				+ "payment_other,paymentno,transaction_fee) select userid,'" + odbeanNew.getOrderNo()
				+ "' as orderno_new,paymentid,'" + odbeanNew.getPay_price()
				+ "' as payprice_new,payment_cc,'order split' as orderdesc,username,"
				+ "paystatus,NOW(),paySID,payflag,3 as paytype,3 as payment_other,paymentno,"
				+ "0 as transaction_fee from payment where orderid='" + orderNoOld + "' limit 1";
		remoteSqlList.add(insertPaymentSql);
		insertPaymentSql = null;

		// 本地执行SQL队列
		List<String> localSqlList = new ArrayList<String>();

		String idRelationSql ="update id_relationtable set orderid='" + odbeanNew.getOrderNo() + "' where orderid='"
				+ orderNoOld + "' and goodid in(" ;
		String goodsDistributionSql ="update goods_distribution set orderid='" + odbeanNew.getOrderNo() + "' where orderid='"
				+ orderNoOld + "' and goodsid in(" ;
		String goodsCommunicationSql ="update goods_communication_info set orderid='" + odbeanNew.getOrderNo() + "' where orderid='"
				+ orderNoOld + "' and goodsid in(" ;
		//将拆单后的订单号和商品号数据保存到split_details表中，用定时任务去执行采购商品退回操作  王宏杰2018-05-14
		String insert_split_details="insert into split_details(new_orderid,goodsid) values";
		String tempSql ="";

		String goodsDistributionSqlBegin = "insert into goods_distribution(orderid,odid,goodsid,admuserid,goodscatid,goods_pid,goods_url,createtime) " +
                "select '"+odbeanNew.getOrderNo()+"' as orderid,odid,goodsid,admuserid,goodscatid,goods_pid,goods_url,now() " +
                "from goods_distribution where orderid = '"+orderNoOld +"' and odid in(";
        String goodsDistributionSqlEnd="";
		for (OrderDetailsBean oddsb : nwOrderDetails) {
		    goodsDistributionSqlEnd += "," + oddsb.getId();

			insert_split_details +="(";

			if(isSplitNum == 0){
				// 正常普通订单商品
				// 更新order_details表原订单号到新的订单号
				String orderDetails = "update order_details set orderid='" + odbeanNew.getOrderNo() + "' where orderid='"
						+ orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				remoteSqlList.add(orderDetails);
			}else{
				// 数量拆单商品
				// 插入新的订单详情
				String orderDetails = "insert into order_details(userid,goodsid,goodsdata_id, goodsname, orderid,dropshipid, " +
						"delivery_time, yourorder,car_url, car_type, car_img,goodsprice, goodsfreight, checkprice_fee, checkproduct_fee, " +
						"actual_price,actual_freight,actual_weight, actual_volume, state,fileupload, od_state, paytime," +
						"createtime, freight_free, purchase_state, purchase_time, purchase_confirmation, remark," +
						"sprice, goods_class, extra_freight, od_bulk_volume, od_total_weight, discount_ratio, " +
						"flag, goodscatid, isAuto,buy_for_me, checked, " +
						"seilUnit, picturepath, car_urlMD5,goods_pid," +
						"bizPriceDiscount, isFreeShipProduct, shopCount)" +
						" select userid,goodsid,goodsdata_id, goodsname, '"+odbeanNew.getOrderNo()+"' as orderid,dropshipid, " +
						"delivery_time, "+oddsb.getYourorder()+" as yourorder,car_url, car_type, car_img,goodsprice, goodsfreight, " +
						"checkprice_fee, checkproduct_fee," +
						"actual_price,actual_freight,actual_weight, actual_volume, state,fileupload, od_state, paytime," +
						"createtime, freight_free, purchase_state, purchase_time, purchase_confirmation, remark," +
						"sprice, goods_class, extra_freight, od_bulk_volume, od_total_weight, discount_ratio," +
						"flag, goodscatid, isAuto,buy_for_me, checked,seilUnit, picturepath, " +
						"car_urlMD5,goods_pid,bizPriceDiscount, isFreeShipProduct, shopCount " +
						" from order_details where orderid='" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				String oldOdSql = "update order_details set yourorder = yourorder - "+ oddsb.getYourorder()
                        +" where orderid='" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				remoteSqlList.add(orderDetails);
				remoteSqlList.add(oldOdSql);
			}

			// 更新order_change表信息
			String updateChange = "update order_change set orderNo='" + odbeanNew.getOrderNo() + "' where orderNo='"
					+ orderNoOld + "' and goodid=" + oddsb.getGoodsid();
			remoteSqlList.add(updateChange);
			//采购补货的订单号更新
			String orderReplenishment = "update order_replenishment set orderid = '"+ odbeanNew.getOrderNo() +"' where orderid = '"
					+ orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
			//localSqlList.add(orderReplenishment);


			if(isSplitNum > 0) {
				// 数量拆单的单独处理
				// 商品沟通备注数据
				String insertGoodsCommunicationInfoSql = "insert into goods_communication_info(context,is_read,send_id,accept_id," +
						"create_time,orderid,goodsid,odid) " +
						" select context,is_read,send_id,accept_id,create_time,'"+odbeanNew.getOrderNo() +"' as orderid,goodsid, 0 as odid " +
						" from goods_communication_info where orderid = '" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				localSqlList.add(insertGoodsCommunicationInfoSql);
				// 新增货源数据
				String insertOrderProductSql = "insert into order_product_source(od_id,adminid,userid,addtime,orderid,confirm_userid,confirm_time,goodsid,goodsdataid,goods_url," +
						"goods_p_url,last_goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,usecount,buycount,currency," +
						"goods_p_name,bargainRemark,deliveryRemark,colorReplaceRemark,sizeReplaceRemark,orderNumRemarks,questionsRemarks," +
						"unquestionsRemarks,purchase_state,tb_1688_itemid,last_tb_1688_itemid,purchasetime,old_shopid,tborderid) " +
						" select 0 as od_id,adminid,userid,addtime,'" + odbeanNew.getOrderNo() + "' as orderid,confirm_userid,confirm_time," +
						"goodsid,goodsdataid,goods_url,goods_p_url,last_goods_p_url,goods_img_url,goods_price,goods_p_price,goods_name,"
						+ oddsb.getYourorder() + " as usecount," + oddsb.getYourorder() + " as buycount,currency," +
						"goods_p_name,bargainRemark,deliveryRemark,colorReplaceRemark,sizeReplaceRemark,orderNumRemarks,questionsRemarks," +
						"unquestionsRemarks,purchase_state,tb_1688_itemid,last_tb_1688_itemid,purchasetime,old_shopid,tborderid from order_product_source " +
						" where orderid = '" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				localSqlList.add(insertOrderProductSql);

				String updateOrderProductSql1 = "update order_product_source set usecount = usecount -" + oddsb.getYourorder()
						+ " where orderid = '" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid() + " and usecount > 0";
				String updateOrderProductSql2 = "update order_product_source set buycount = buycount -" + oddsb.getYourorder()
						+ " where orderid = '" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid() + " and buycount > 0";
				localSqlList.add(updateOrderProductSql1);
				localSqlList.add(updateOrderProductSql2);

				// 入库
				String insertIdRelationSql = "insert into id_relationtable(orderid,goodid,goodstatus,goodurl,barcode,picturepath,createtime," +
						"POSITION,userid,username,tborderid,warehouse_remark,shipno,is_replenishment,itemqty,itemid,odid)" +
						" select '"+odbeanNew.getOrderNo()+"',goodid,goodstatus,goodurl,barcode," +
						"CONCAT(DATE_FORMAT(NOW(),'%Y-%m'),'/','"+odbeanNew.getOrderNo()+"','_',goodid,'.jpg'),createtime," +
						"POSITION,userid,username,tborderid,warehouse_remark,shipno,is_replenishment,"
						+oddsb.getYourorder()+" as itemqty,itemid,0 as odid " +
						"from id_relationtable " + " where orderid = '" + orderNoOld + "' and goodid=" + oddsb.getGoodsid();
				localSqlList.add(insertIdRelationSql);
				String updateRelationSql = "update id_relationtable set itemqty = itemqty - " + oddsb.getYourorder()
						+ " where orderid='"+orderNoOld+"' and goodid=" + oddsb.getGoodsid() + " and itemqty > 0";
				localSqlList.add(updateRelationSql);
			}else {
				//更新order_product_source表数据
				String order_product_source_sql = "update order_product_source set orderid='" + odbeanNew.getOrderNo() + "' where orderid = '" + orderNoOld + "' and goodsid=" + oddsb.getGoodsid();
				localSqlList.add(order_product_source_sql);
			}

			// 替换拆单的入库商品单号
			tempSql += "," + oddsb.getGoodsid();
			insert_split_details += "'"+oddsb.getGoodsid()+"','"+odbeanNew.getOrderNo()+"'),";
//			insert_split_details += ")";
			updateChange = null;
			String inspection_pictureSql = "update inspection_picture set orderid = '"+ odbeanNew.getOrderNo()
					+"' where orderid = '"+ orderNoOld  +"' and goods_id="+oddsb.getGoodsid()+"";
			if(isSplitNum == 0){
				remoteSqlList.add(inspection_pictureSql);
			}
			localSqlList.add(inspection_pictureSql);
		}
		insert_split_details=insert_split_details.substring(0,insert_split_details.length()-1)+";";

		if(isSplitNum == 0){
			idRelationSql += tempSql.substring(1) + ")";
			localSqlList.add(idRelationSql);
			goodsDistributionSql += tempSql.substring(1) + ")";
			localSqlList.add(goodsDistributionSql);
			goodsCommunicationSql += tempSql.substring(1) + ")";
			localSqlList.add(goodsCommunicationSql);
		}

		//localSqlList.add(insert_split_details);

        if(isSplitNum > 0){
            // 采购分配的
            localSqlList.add(goodsDistributionSqlBegin + goodsDistributionSqlEnd.substring(1) + ")");
        }

		try {
			remoteConn.setAutoCommit(false);
			localConn.setAutoCommit(false);
			ppStamt = remoteConn.createStatement();
			System.err.println("exSql begin");
			for (String exSql : remoteSqlList) {
				System.err.println(exSql + ";");
				ppStamt.addBatch(exSql);
			}

			isSuccess = ppStamt.executeBatch().length > 0;
			if(isSuccess){
				wStamt = localConn.createStatement();
				for(String loSql : localSqlList){
					wStamt.addBatch(loSql);
				}
				//ppStamt.executeUpdate(localSql);
				isSuccess = wStamt.executeBatch().length > 0;
				if(isSuccess){
				    remoteConn.commit();
				    localConn.commit();
                }else{
				    remoteConn.rollback();
				    localConn.rollback();
                }
				//拆单取消时记录拆单的商品
				if("0".equals(state)){
				    wStamt.clearBatch();
					wStamt = localConn.createStatement();
					wStamt.executeUpdate(insert_split_details);
				}
			}else{
				remoteConn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
			try {
				remoteConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				localConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (ppStamt != null) {
				try {
					ppStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (wStamt != null) {
				try {
					wStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (localStamt != null) {
				try {
					localStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(localConn);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return isSuccess;
	}

	@Override
	public int isExistsMessageByOrderNo(String orderNo) {
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement ppStamt = null;
		ResultSet rs = null;
		String querySql = " select count(0) from order_change where orderNo = ? and ropType <> 6 and del_state = 0";
		int count = 0;
		try{
			ppStamt = conn.prepareStatement(querySql);
			ppStamt.setString(1,orderNo);
			rs = ppStamt.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(ppStamt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return  count;
	}

	@Override
	public boolean updateDropShipDate(String oldDropShopId, String newDropShopId, String parentOrderNo,float orderAc,float extraFreight) {
		Connection conn = DBHelper.getInstance().getConnection2();
		Statement ppStamt = null;
		ResultSet rs = null;
		String updateOldSql = "update dropshiporder set product_cost = (select sum(yourorder*goodsprice) from order_details " +
				"where orderid= '" + parentOrderNo + "' and dropshipid = '" + oldDropShopId + "'), " +
				"pay_price = (select sum(yourorder*goodsprice) from order_details where orderid= '" + parentOrderNo + "' " +
				"and dropshipid = '" + oldDropShopId + "') - " + (orderAc + extraFreight)  + ",order_ac = order_ac - "
				+ orderAc + ",extra_freight = extra_freight - " + extraFreight + " where parent_order_no = '" + parentOrderNo
				+ "' and child_order_no = '" + oldDropShopId + "'";

		String updateNewSql = "update dropshiporder set product_cost = (select sum(yourorder*goodsprice) from order_details " +
				"where orderid= '" + parentOrderNo + "' and dropshipid = '" + newDropShopId + "'), " +
				"pay_price = (select sum(yourorder*goodsprice) from order_details where orderid= '" + parentOrderNo + "' " +
				"and dropshipid = '" + newDropShopId + "') + " + (orderAc + extraFreight) + ",order_ac = "
				+ orderAc + ",extra_freight = " + extraFreight + " where parent_order_no = '" + parentOrderNo
				+ "' and child_order_no = '" + newDropShopId + "'";

		String updateParentSql = "update orderinfo a,(select (count(id)-sum(state)) as orderdetails_count,orderid from " +
				"order_details where state < 2 and orderid='" + parentOrderNo + "') b " +
				"set a.state = 2 where a.order_no = b.orderid and b.orderdetails_count = 0;";

		int count = 0;
		try {
			conn.setAutoCommit(false);
			ppStamt = conn.createStatement();
			ppStamt.addBatch(updateOldSql);
			ppStamt.addBatch(updateNewSql);
			ppStamt.addBatch(updateParentSql);
			count = ppStamt.executeBatch().length;
			if (count > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			DBHelper.getInstance().rollbackConnection(conn);
		} finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closeStatement(ppStamt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return count > 0;
	}

	@Override
	public int updateGoodsCommunicationInfo(String oldOrder, String newOrder, List<Integer> odIds) {

		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		String sql = "update goods_communication_info set orderid =? where orderid = ? and odid = ?";
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
			for (int i = 0; i < odIds.size(); i++) {
				pStmt.setString(1, newOrder);
				pStmt.setString(2, oldOrder);
				pStmt.setInt(3, odIds.get(i));
				pStmt.addBatch();
			}
			rs = pStmt.executeBatch().length;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closePreparedStatement(pStmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

}
