package com.cbt.website.dao;

import com.cbt.bean.Payment;
import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.util.GetConfigureInfo;
import com.cbt.website.bean.PaymentConfirm;
import com.importExpress.utli.NotifyToCustomerUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class PaymentConfirmDaoImpl implements PaymentConfirmDao {

	@Override
	public Map<String, Object> selectPaymentConfirm(String orderno) {
		// TODO Auto-generated method stub
        String sql="select * from paymentconfirm where orderno='"+orderno+"'";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			stmt = conn.prepareStatement(sql);	
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put("id", rs.getInt("id"));
				map.put("orderno", rs.getString("orderno"));
				map.put("confirmname", rs.getString("confirmname"));
				map.put("confirmtime", rs.getString("confirmtime"));
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
		return map;
	}

	@Override
	public int insertPaymentConfirm(String orderNo, String confirmname, String confirmtime, 
			                     String paytype, String tradingencoding, String wtprice, 
			                     int userId) {
		OrderInfoDao orderDao = new OrderInfoImpl();
		int res = 0;
		res=orderDao.updateOrderinfoIpnAddress(orderNo);
		if(res == 12){
			return res;
		}
		String sql = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) SELECT ?,?,?,?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM paymentconfirm WHERE orderno='"+orderNo+"')";
		if("1".equals(paytype)|| paytype == "1"){
			IPaymentDao payDao = new PaymentDao();
			Payment pay = new Payment();
			pay.setUserid(userId);// 添加用户id
			pay.setOrderid(orderNo);
			pay.setPaystatus(5);// 添加付款状态
			pay.setPaymentid(tradingencoding);// 添加付款流水号（paypal返回的）
			pay.setPayment_amount(Float.parseFloat(wtprice.replaceAll("[^0-9/.]", "")));// 添加付款金额（paypal返回的）
			pay.setPayment_cc(wtprice.replaceAll("[^(A-Za-z)]", ""));// 添加付款币种（paypal返回的）
			pay.setPaySID(tradingencoding);
			pay.setPaytype(paytype);
			payDao.addPayment(pay);
			//更新订单状态
			try {
				orderDao.updateOrderStatu(userId, orderNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		//Connection conn1 = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		//PreparedStatement stmt1 = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, orderNo);
			stmt.setString(2, confirmname);
			stmt.setString(3, confirmtime);
			stmt.setString(4, paytype);
			stmt.setString(5, tradingencoding);
			res = stmt.executeUpdate();	
			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String sqlStr = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) "
						+ "values('"+orderNo+"','"+confirmname+"','"+confirmtime+"','"+paytype+"','"+(tradingencoding==null ? "":tradingencoding)+"')";
				SaveSyncTable.InsertOnlineDataInfo(userId, orderNo, "确认到账详情", "paymentconfirm", sqlStr);
			} else{
				/*stmt1 = conn1.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				stmt1.setString(1, orderNo);
				stmt1.setString(2, confirmname);
				stmt1.setString(3, confirmtime);
				stmt1.setString(4, paytype);
				stmt1.setString(5, tradingencoding);
				res = stmt1.executeUpdate();*/

                StringBuffer sqlBf = new StringBuffer();
                sqlBf.append("insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) select ");
                sqlBf.append("'" + orderNo + "' as order_no,");
                sqlBf.append("'" + confirmname + "' as confirmname,");
                sqlBf.append("'" + confirmtime + "' as confirmtime,");
                sqlBf.append("'" + paytype + "' as paytype,");
                sqlBf.append("'" + (tradingencoding == null ? "": tradingencoding ) + "' as tradingencoding");
                sqlBf.append(" FROM DUAL WHERE NOT EXISTS (SELECT * FROM paymentconfirm WHERE orderno='" + orderNo + "')");

                ////使用MQ更新远程
                NotifyToCustomerUtil.sendSqlByMq(sqlBf.toString());
                ////订单状态修改，通知客户
                NotifyToCustomerUtil.confimOrderPayment(userId, orderNo);
				res = 1;
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
			/*if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
			DBHelper.getInstance().closeConnection(conn);
			//DBHelper.getInstance().closeConnection(conn1);
		}
		return res;
	}

	@Override
	public PaymentConfirm getPaymentConfirmBean(String orderNo) {
		String sql = "select * from paymentconfirm where orderno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PaymentConfirm paymentconfirm = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				paymentconfirm = new PaymentConfirm();
				paymentconfirm.setOrderno(rs.getString("orderno"));
				paymentconfirm.setConfirmtime(rs.getString("confirmtime"));
				paymentconfirm.setConfirmname(rs.getString("confirmname"));
				paymentconfirm.setPaytype(rs.getString("paytype"));
				paymentconfirm.setPaymentid(rs.getString("paymentid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return paymentconfirm;
	}

	@Override
	public int addPaymentConfirm(PaymentConfirm paymentconfirm) {
		String sql = "insert into paymentconfirm(orderno,confirmname,confirmtime,paytype,paymentid) values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, paymentconfirm.getOrderno());
			stmt.setString(2, paymentconfirm.getConfirmname());
			stmt.setString(3, paymentconfirm.getConfirmtime());
			stmt.setString(4, paymentconfirm.getPaytype());
			stmt.setString(5, paymentconfirm.getPaymentid());
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
	public int updatePaymentConfirm(PaymentConfirm paymentconfirm) {
		String sql = "update paymentconfirm set confirmname = ?,confirmtime = ?,paytype = ?,paymentid = ? where orderno = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, paymentconfirm.getConfirmname());
			stmt.setString(2, paymentconfirm.getConfirmtime());
			stmt.setString(3, paymentconfirm.getPaytype());
			stmt.setString(4, paymentconfirm.getPaymentid());
			stmt.setString(5, paymentconfirm.getOrderno());
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
	
	

}
