package com.cbt.dao.impl;

import com.cbt.bean.BalanceBean;
import com.cbt.dao.AdditionalBalanceDao;
import com.cbt.jdbc.DBHelper;
import com.mysql.jdbc.Statement;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdditionalBalanceDaoImpl implements AdditionalBalanceDao {

	@Override
	public int insert(BalanceBean bean) {
		String sql = "insert additional_balance"
				+ "(userid,money,admin,remark,createtime,orderid,comid) "
				+ "values(?,?,?,?,now(),?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bean.getUserId());
			stmt.setDouble(2, bean.getMoney());
			stmt.setString(3, bean.getAdmin());
			stmt.setString(4, bean.getRemark());
			stmt.setString(5, bean.getOrderid());
			stmt.setString(6, bean.getComplainid());
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
	public int insertForRefund(BalanceBean bean) {
		String sql = "insert additional_balance"
				+ "(userid,money,admin,remark,createtime,orderid,comid,valid) "
				+ "values(?,?,?,?,now(),?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, bean.getUserId());
			stmt.setDouble(2, bean.getMoney());
			stmt.setString(3, bean.getAdmin());
			stmt.setString(4, bean.getRemark());
			stmt.setString(5, bean.getOrderid());
			stmt.setString(6, bean.getComplainid());
			stmt.setInt(7, bean.getValid());
			stmt.executeUpdate();
			generatedKeys = stmt.getGeneratedKeys();
			while(generatedKeys.next()){
				rs = generatedKeys.getInt(1);
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
		return rs;
	}

	@Override
	public List<BalanceBean> getBalanceByUserId(Integer userId,int page) {
		String sql = "select sql_calc_found_rows userid,money,admin,remark,createtime,type,valid,orderid "
				+ " from additional_balance where valid=1 and userid=? order by createtime desc limit ?,20";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		List<BalanceBean> list = new ArrayList<BalanceBean>();
		ResultSet rs = null;
		ResultSet rs2 = null;
		int page_total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.setInt(2, (page-1)*20);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if (rs2.next()) {
				page_total = rs2.getInt("found_rows()");
			}
			
			BalanceBean balanceBean = null;
			while(rs.next()){
				int userid = rs.getInt("userid");
				int type = rs.getInt("type");
				double money = rs.getDouble("money");
				String admin = rs.getString("admin");
				String remark = rs.getString("remark");
				Date date = rs.getDate("createtime");
				balanceBean = new BalanceBean(userid, money, remark, admin, type, date);
				balanceBean.setOrderid(rs.getString("orderid"));
				balanceBean.setCountTotal(page_total);
				list.add(balanceBean);
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
			if (stmt2 != null) {
				try {
					stmt2.close();
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
		return list;
	}

	@Override
	public double getMoneyAmount(int userId) {
		String sql = "select sum(money) as amount from additional_balance where "
				+ "valid=1 and type=0 and userid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double  amount = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while(rs.next()){
				amount = rs.getDouble("amount");
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
		return amount;
	}

	@Override
	public Map<String, String> getBalanceByUserIds(
			List<Integer> userIds) {
		String sql = "select sum(money) as money,userid from additional_balance "
				  + "where valid=1 and type=0 and userid in (";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(Integer l:userIds){
				sql = sql+l+",";
			}
			sql = sql.endsWith(",") ? sql.substring(0, sql.length()-1):sql;
			sql = sql+") group by userid";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("userid"), rs.getString("money"));
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
		return map;
	}
	@Override
	public int updateStateById(int id, int valid) {
		String sql = "update additional_balance set valid=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setInt(2, id);
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
	public double getMoneyAmountByCid(String cid) {
		String sql = "select sum(money) as money from additional_balance "
				  + "where valid=1  and type=0 and comid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cid);
			rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getDouble("money");
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
		return result;
	}
	@Override
	public Map<String, Double> getMoneyAmountByCids(String cidList) {
		String sql = "select sum(money) as money,comid from additional_balance "
				  + "where valid=1 and type=0 and comid in ("+cidList+") group by comid";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("comid"), rs.getDouble("money"));
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
		return map;
	}

}
