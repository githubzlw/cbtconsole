package com.cbt.website.dao;

import com.cbt.bean.OrderBean;
import com.cbt.bean.Payment;
import com.cbt.jdbc.DBHelper;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.util.BigDecimalUtil;
import com.cbt.warehouse.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfoImpl implements OrderInfoDao {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderInfoImpl.class);

	/**
	 * 更新订单状态
	 */
	@Override
	public void updateOrderStatu(Integer userId, String orderNo)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"update orderinfo set state = '5' where 1 = 1 and state = '0' ");
		if (orderNo != null) {
			sql.append("and order_no = '" + orderNo + "' ");
		}
		if (userId != null) {
			sql.append("and user_id = " + userId + " ");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql.toString(),
					Statement.RETURN_GENERATED_KEYS);
			int result = stmt.executeUpdate();
//			LOG.debug("---------updateOrderPayPrice:" + result);
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
	public int updateOrderinfoIpnAddress(String orderNo) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		Statement stmtMm = null;
		ResultSet rs = null;
		int row=12;
		String shorthand="";
		try {
			String orderNos = orderNo.indexOf("_") > -1 ? orderNo.split("_")[0] : orderNo;
			String sql = "select paytype from payment where orderid='" + orderNos + "' and paystatus=1 and paytype<>0";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return 1;
			}
			OrderBean orderBean = getOrderInfo(orderNo, null);
			if (orderBean.getIsDropshipOrder() > 0) {
				sql = "SELECT IFNULL(z.shorthand,'') as shorthand FROM order_address oa INNER JOIN zone z ON REPLACE(oa.country,' ','')=z.id " +
						"OR REPLACE(oa.country,' ','')=REPLACE(z.country,' ','') WHERE oa.orderNo like '" + orderNo + "%' limit 1";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					shorthand = rs.getString("shorthand");
				}
				if (StringUtil.isNotBlank(shorthand)) {
					stmtMm = conn.createStatement();
					sql = "update orderinfo set ipnaddress='" + shorthand + "' where order_no='" + orderNo + "'";
					stmtMm.addBatch(sql);
					sql = "update dropshiporder a,(SELECT IFNULL(z.shorthand,'') as shorthand,oa.orderNo FROM order_address oa INNER JOIN zone z " +
							"ON REPLACE(oa.country,' ','')=z.id OR REPLACE(oa.country,' ','')=REPLACE(z.country,' ','')) b " +
							"set a.ipnaddress = b.shorthand where a.parent_order_no='" + orderNo + "' and b.orderNo = a.child_order_no";
					stmtMm.addBatch(sql);
					row = stmt.executeBatch().length;
				}
			} else {
				sql = "SELECT IFNULL(z.shorthand,'') as shorthand FROM order_address oa INNER JOIN zone z ON REPLACE(oa.country,' ','')=z.id OR REPLACE(oa.country,' ','')=REPLACE(z.country,' ','') WHERE oa.orderNo='" + orderNos + "'";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if (rs.next()) {
					shorthand = rs.getString("shorthand");
				}
				if (StringUtil.isNotBlank(shorthand)) {
					sql = "update orderinfo set ipnaddress='" + shorthand + "' where order_no='" + orderNo + "'";
					stmt = conn.prepareStatement(sql);
					row = stmt.executeUpdate();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeStatement(stmtMm);
			DBHelper.getInstance().closeConnection(conn);
		}
		return row;
	}

	@Override
	public OrderBean getOrderInfo(String orderNo, String userId)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"select * from orderinfo where 1 = 1 ");
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("and order_no = '" + orderNo + "' ");
		}
		if (userId != null && !"".equals(userId)) {
			sql.append("and user_id = '" + userId + "' ");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OrderBean order = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				order = new OrderBean();
				order.setVatBalance(rs.getDouble("vatbalance"));
				order.setId(rs.getInt("orderid"));
				order.setOrderNo(rs.getString("order_no"));
				order.setUserid(rs.getInt("user_id"));
				order.setDeliveryTime(rs.getInt("delivery_time"));
				order.setPackage_style(rs.getInt("packag_style"));
				order.setMode_transport(rs.getString("mode_transport"));
				order.setService_fee(rs.getString("service_fee"));
				order.setProduct_cost(rs.getString("product_cost"));
				order.setDomestic_freight(rs.getString("domestic_freight"));
				order.setForeign_freight(rs.getString("foreign_freight"));
				order.setActual_allincost(rs.getDouble("actual_allincost"));
				order.setPay_price(rs.getDouble("pay_price"));
				order.setPay_price_tow(rs.getString("pay_price_tow"));
				order.setPay_price_three(rs.getString("pay_price_three"));
				order.setActual_ffreight(rs.getString("actual_ffreight"));
				order.setRemaining_price(rs.getDouble("remaining_price"));
				order.setActual_volume(rs.getString("actual_volume"));
				order.setActual_weight(rs.getString("actual_weight"));
				order.setCustom_discuss_other(rs
						.getString("custom_discuss_other"));
				// order.setCustom_discuss_fright(rs.getString("custom_discuss_fright"));
				order.setTransport_time(rs.getString("transport_time"));
				order.setState(rs.getInt("state"));
				order.setCancel_obj(rs.getInt("cancel_obj"));
				order.setExpect_arrive_time(rs.getString("expect_arrive_time"));
				order.setArrive_time(rs.getString("arrive_time"));
				order.setCreatetime(rs.getString("create_time"));
				order.setClient_update(rs.getInt("client_update"));
				order.setServer_update(rs.getInt("server_update"));
				order.setIp(rs.getString("ip"));
				order.setOrder_ac(rs.getDouble("order_ac"));
				order.setPurchase_number(rs.getInt("purchase_number"));
				order.setDetails_number(rs.getInt("details_number"));
				// order.setIpnaddress(rs.getString("ipnaddress"));
				order.setCurrency(rs.getString("currency"));
				order.setDiscount_amount(rs.getDouble("discount_amount"));
				// order.setPurchasedays(rs.getString("purchase_days"));
				order.setActual_lwh(rs.getString("actual_lwh"));
				order.setActual_weight_estimate(rs
						.getDouble("actual_weight_estimate"));
				order.setActual_freight_c(rs.getDouble("actual_freight_c"));
				order.setExtra_freight(rs.getDouble("extra_freight"));
				order.setIsDropshipOrder(rs.getInt("isDropshipOrder"));
				order.setShare_discount(rs.getDouble("share_discount"));//分享折扣
				order.setExtra_discount(rs.getDouble("extra_discount"));//手动优惠 1/19
				
				order.setProcessingfee(rs.getDouble("processingfee"));
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
		return order;
	}

	@Override
	public int addPaymentConfirm(String orderNo, String confirmname,
			String confirmtime, String paytype, String tradingencoding,
			String wtprice, int userId) throws Exception {
		String confirmsql = "select * from where paymentconfirm where 1=1 and orderno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = conn.prepareStatement(confirmsql);
			stmt.setString(1, orderNo);
			result = stmt.executeQuery();
			if (result.next()) {
				String UpdateConfirmsql = "update paymentconfirm set confirmtime = ? where orderno = ?";
				PreparedStatement updateStmt = null;
				updateStmt = conn.prepareStatement(UpdateConfirmsql);
				updateStmt.setString(1, confirmtime);
				updateStmt.setString(2, orderNo);
				updateStmt.executeUpdate();
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

		return 0;
	}

	@Override
	public int confirmOrder(String orderNo, String confirmname,
			String confirmtime, String paytype, String tradingencoding,
			String wtprice, int userId) throws Exception {
		String sql = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) values(?,?,?,?,?)";
		if ("1".equals(paytype) || paytype == "1") {
			IPaymentDao payDao = new PaymentDao();
			Payment pay = new Payment();
			pay.setUserid(userId);// 添加用户id
			pay.setOrderid(orderNo);
			pay.setPaystatus(5);// 添加付款状态
			pay.setPaymentid(tradingencoding);// 添加付款流水号（paypal返回的）
			pay.setPayment_amount(Float.parseFloat(wtprice.replaceAll(
					"[^0-9/.]", "")));// 添加付款金额（paypal返回的）
			pay.setPayment_cc(wtprice.replaceAll("[^(A-Za-z)]", ""));// 添加付款币种（paypal返回的）
			pay.setPaySID(tradingencoding);
			pay.setPaytype(paytype);
			payDao.addPayment(pay);
			// 更新订单状态
			// OrderInfoDao orderDao = new OrderInfoImpl();
			try {
				this.updateOrderStatu(userId, orderNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderNo);
			stmt.setString(2, confirmname);
			stmt.setString(3, confirmtime);
			stmt.setString(4, paytype);
			stmt.setString(5, tradingencoding);
			res = stmt.executeUpdate();
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
		return res;
	}
	
	@Override
	public int addpayment(String orderNo, String paytype,
			String tradingencoding, int userId) throws Exception {
		
		return 0;
	}

	@Override
	public int updateOrder(OrderBean order, String payPrice, boolean isPayFreight) throws Exception {
		double remainning = 0.00;
		StringBuffer sql = null;
		if (payPrice != null || "".equals(payPrice)) {
			sql = new StringBuffer(
					"update orderinfo set pay_price = '"+payPrice+"', ");			
		}
		if (isPayFreight) {
			remainning = (Double.parseDouble(order.getProduct_cost()) - order.getDiscount_amount() - order.getOrder_ac() + Double.parseDouble(order.getActual_ffreight())) - Double.parseDouble(payPrice);
			sql.append("pay_price_tow = '"+order.getActual_ffreight()+"', ");
		}else{
			remainning = (Double.parseDouble(order.getProduct_cost()) - order.getDiscount_amount()) - Double.parseDouble(payPrice);
		}
		sql.append("state = '5', remaining_price = "+String.format("%.2f", remainning)+" where 1 = 1 and state = '0' ");
		
		
		if (order.getOrderNo() != null) {
			sql.append("and order_no = '" + order.getOrderNo() + "' ");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql.toString());
			result = stmt.executeUpdate();
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
		return result;
	}
 	@Override
	public String[] getPayMentInfo(String orderNo){
		StringBuffer sql = new StringBuffer("SELECT pay_info from pay_result_info");
		if (orderNo != null) {
			sql.append(" where  orderid = '" +orderNo+ "' ");
		}
		sql.append(" limit 1;");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String[] payinfo = new String[2];
		try {
			List<com.paypal.api.payments.Payment> list = new ArrayList<com.paypal.api.payments.Payment>();
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				String paymentinfo = rs.getString("pay_info");
//				payment = JSON.parseObject("[" + rs.getString("pay_info") + "]",com.paypal.api.payments.Payment.class);
				JSONObject jsonObject = JSONArray.fromObject("[" + paymentinfo + "]")
						.getJSONObject(0)
						.getJSONArray("transactions")
						.getJSONObject(0)
						.getJSONArray("related_resources")
						.getJSONObject(0)
						.getJSONObject("sale");

				//JSONObject jsonObject = JSONArray.fromObject("[" + paymentinfo + "]").getJSONObject(0);
				//double total = jsonObject.getDouble("amount");
				//payinfo[1] = String.valueOf(BigDecimalUtil.truncateDouble(total,2));

				String saled = jsonObject.getString("id");
				String total = jsonObject.getJSONObject("amount").getString("total");
				payinfo[0] = saled;

				payinfo[1] = total;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeConnection(conn);
		}
		return  payinfo;
 }

	@Override
	public Map<String, Object> queryPaymentInfoByOrderNo(String orderNo) {
		String sql = "select a.pay_info,b.paytype,DATE_FORMAT(b.createtime,'%Y-%m-%d %H:%i:%s') as createtime from pay_result_info a,payment b " +
				"where a.orderid = b.orderid and b.paytype in(0,1,5) and a.orderid = ? limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1,orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int payType = rs.getInt("paytype");
				String paymentInfo = rs.getString("pay_info");
				resultMap.put("payTime", rs.getString("createtime"));
				if (payType == 0 || payType == 1) {
					JSONObject jsonObject = JSONArray.fromObject("[" + paymentInfo + "]")
							.getJSONObject(0)
							.getJSONArray("transactions")
							.getJSONObject(0)
							.getJSONArray("related_resources")
							.getJSONObject(0)
							.getJSONObject("sale");
					resultMap.put("payType", "paypal");
					resultMap.put("saleId", jsonObject.getString("id"));
					resultMap.put("payAmount", jsonObject.getJSONObject("amount").getString("total"));
				} else if (payType == 5) {
					JSONObject jsonObject = JSONArray.fromObject("[" + paymentInfo + "]").getJSONObject(0);
					double total = jsonObject.getDouble("amount");
					resultMap.put("payType", "stripe");
					resultMap.put("saleId", jsonObject.getString("id"));
					resultMap.put("payAmount", BigDecimalUtil.truncateDouble(total, 2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return resultMap;
	}

	@Override
	public boolean checkIsOldPayPal(String orderNo) {
		String sql = "select ipnInfo from ipn_info where orderNo = ? and paymentStatus = 1 limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isOld = false;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String ipnInfo = rs.getString(1);
				if (StringUtils.isNotBlank(ipnInfo)) {
					if (ipnInfo.contains("=584JZVFU6PPVU")) {
						isOld = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return isOld;
	}

}
