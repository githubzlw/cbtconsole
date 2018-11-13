package com.cbt.customer.dao;

import com.cbt.bean.RecentViewBean;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RencentViewDaoImpl implements IRencentViewDao{

	@Override
	public int addRecode(RecentViewBean rvb) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		sql = "insert into recent_view(pid,pname,purl,img_url,price,min_order,sessionid,uid,ip,view_date_year,view_date_month,view_date_day,view_date_time,keywords,seachwords) values(?,?,?,?,?,?,?,?,?,year(now()),month(now()),day(now()),curtime(),?,?)";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, rvb.getPid());
			stmt.setString(2, rvb.getPname());
			stmt.setString(3, rvb.getPurl());
			stmt.setString(4, rvb.getImgUrl());
			stmt.setString(5, rvb.getPrice());
			stmt.setString(6, rvb.getMinOrder());
			stmt.setString(7, rvb.getSessionid());
			stmt.setInt(8, rvb.getUid());
			stmt.setString(9, rvb.getIp());
			stmt.setString(10, rvb.getKeywords());
			stmt.setString(11, rvb.getSeachwords());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
			stmt.close();
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
	public RecentViewBean findByPid(String pid) {
			RecentViewBean rvb = null;
			String sql = " select * from recent_view where pid = ?";
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			conn = DBHelper.getInstance().getConnection();
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, pid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					rvb = new RecentViewBean();
					rvb.setId(rs.getInt("id"));
					rvb.setPid(rs.getString("pid"));
		    		rvb.setPname(rs.getString("pname"));
		    		rvb.setPurl(rs.getString("purl"));
		    		rvb.setImgUrl(rs.getString("img_url"));
		    		rvb.setPrice(rs.getString("price"));
		    		rvb.setMinOrder(rs.getString("min_order"));
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
		return rvb;
	}

	@Override
	public List<RecentViewBean> findAll(int start, int page_size) {
		List<RecentViewBean> rvbs = new ArrayList<RecentViewBean>();;
		String sql = "select * from recent_view order by id desc limit ?,?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(1, page_size);
			rs = stmt.executeQuery();
			while (rs.next()) {
				RecentViewBean rvb = new RecentViewBean();
				rvb.setId(rs.getInt("id"));
				rvb.setPid(rs.getString("pid"));
	    		rvb.setPname(rs.getString("pname"));
	    		rvb.setPurl(rs.getString("purl"));
	    		rvb.setImgUrl(rs.getString("img_url"));
	    		rvb.setPrice(rs.getString("price"));
	    		rvb.setMinOrder(rs.getString("min_order"));
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
	return rvbs;
	}
	
	@Override
	 public List<RecentViewBean> findCent(String ids)
	  {
	    List rvbs = new ArrayList();
	    String sql = "select * from recent_view where pid in(" + ids + ") group by pid order by id";
//	    System.out.println(sql + ids);
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    conn = DBHelper.getInstance().getConnection();
	    try {
	      stmt = conn.prepareStatement(sql);

	      rs = stmt.executeQuery();
	      while (rs.next()) {
	        RecentViewBean rvb = new RecentViewBean();
	        rvb = new RecentViewBean();
	        rvb.setId(rs.getInt("id"));
	        rvb.setPid(rs.getString("pid"));
	        rvb.setPname(rs.getString("pname"));
	        rvb.setPurl(rs.getString("purl"));
	        rvb.setImgUrl(rs.getString("img_url"));
	        rvb.setPrice(rs.getString("price"));
	        rvb.setMinOrder(rs.getString("min_order"));
	        rvbs.add(rvb);
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

	    return rvbs;
	  }

}
