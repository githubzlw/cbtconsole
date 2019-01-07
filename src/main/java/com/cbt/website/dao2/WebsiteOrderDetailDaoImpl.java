package com.cbt.website.dao2;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebsiteOrderDetailDaoImpl implements IWebsiteOrderDetailDao {

	@Override
	public float PurchaseAmountOfOrder(String orderid) {
		// TODO Auto-generated method stub
//		String sql="select oc.orderNo,round(sum(oc.oldValue*od.yourorder),2) as amount from order_change oc left join order_details od on oc.orderNo = od.orderid and oc.goodId=od.goodsid where ropType=6 and del_state=0 and orderid=? group by oc.orderNo";
//		String sql="select oc.orderid orderNo,round(sum(oc.goods_p_price*od.yourorder),2) as amount from order_product_source oc left join order_details od on oc.orderid = od.orderid and oc.goodsid=od.goodsid where oc.purchase_state>0 and oc.del=0 and od.orderid=? group by oc.orderid";
		String sql="select sum(goods_p_price*buycount) amount from order_product_source where orderid=? and purchase_state > 0;";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		float res=0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			rs = stmt.executeQuery();
			while(rs.next()){
				res=rs.getFloat("amount");
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
	public int websiteUpdateOrderState(String orderid,int state) {
		// TODO Auto-generated method stub
		String sql="update orderinfo set state=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int res=0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			stmt.setString(2, orderid);
			res = stmt.executeUpdate();
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
	public int websiteUpdateOrderPrice(String orderid,double price) {
		// TODO Auto-generated method stub
		String sql="update orderinfo set extra_discount=? where order_no=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int res=0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1,price);
			stmt.setString(2, orderid);
			res = stmt.executeUpdate();
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
	public int updateOrderStateLog(String orderid, int newState,int oldState, String remark,int adminId) {
		// TODO Auto-generated method stub
		String sql = "insert into orderinfo_state_log(order_no,new_state,old_state,remark,admin_id)" +
				"values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, newState);
			stmt.setInt(3, oldState);
			stmt.setString(4, remark);
			stmt.setInt(5,adminId);
			res = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

	@Override
	public boolean deleteRechangeRecord(int userId, String orderid) {
		// TODO Auto-generated method stub
		String sql = "delete from recharge_record  where userid = ? and remark_id = ? and type = 1 and remark like '%system closeOrder%'";
		Connection connAws = DBHelper.getInstance().getConnection2();
		Connection conn27 = DBHelper.getInstance().getConnection();
		PreparedStatement stmtAws = null;
		PreparedStatement stmt27 = null;
		int rs = 0;
		try {
			connAws.setAutoCommit(false);
			conn27.setAutoCommit(false);

			stmtAws = connAws.prepareStatement(sql);
			stmtAws.setInt(1, userId);
			stmtAws.setString(2, orderid);

			stmt27 = conn27.prepareStatement(sql);
			stmt27.setInt(1, userId);
			stmt27.setString(2, orderid);
			rs = stmtAws.executeUpdate();
			if (rs > 0) {
				rs = stmt27.executeUpdate();
				if (rs > 0) {
					connAws.commit();
					conn27.commit();
				} else {
					connAws.rollback();
					conn27.rollback();
				}
			} else {
				connAws.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closePreparedStatement(stmtAws);
			DBHelper.getInstance().closePreparedStatement(stmt27);
			DBHelper.getInstance().closeConnection(connAws);
			DBHelper.getInstance().closeConnection(conn27);
		}
		return rs > 0;
	}

}
