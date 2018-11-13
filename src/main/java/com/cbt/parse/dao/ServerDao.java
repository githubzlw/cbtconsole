package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.IServerDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerDao implements IServerDao {
	
	/**更新代理服务器的error内容
	 * @param ip
	 * @param port
	 * @param status
	 * @param error
	 * @return
	 */
	public  int addError(String ip,String port,String status,String error) {
		int result = 0;
		if(!ip.isEmpty()){
			String sql = "update server set error=?,status=? where ip=? and port=?";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, error);
				stmt.setString(2, status);
				stmt.setString(3, ip);
				stmt.setString(4, port);
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
		}
		return result;
	}
	/**更新代理服务器的status状态
	 * @param ip
	 * @param status
	 * @param error
	 * @return
	 */
	public  int updateStatus(String ip,String  status,String error) {
		int result = 0;
		if(!ip.isEmpty()){
			String sql = "update server set status=?,error=? where ip=?";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, error);
				stmt.setString(3, ip);
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
		}
		return result;
	}
	/**更新代理服务器的valid状态
	 * @param ip
	 * @param valid
	 * @return
	 */
	public  int updateValid(String ip,int  valid) {
		int result = 0;
		if(!ip.isEmpty()){
			String sql = "update server set valid=? where ip=?";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, valid);
				stmt.setString(2, ip);
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
		}
		return result;
	}
	
	public  int addServer(String ip,String port,String status,int valid,String error) {
		int result = 0;
		if(!ip.isEmpty()&&!port.isEmpty()){
			String sql = "insert server(ip,port,status,valid,error) values(?,?,?,?,?)";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, ip.trim());
				stmt.setString(2, port.trim());
				stmt.setString(3, status);
				stmt.setInt(4, valid);
				stmt.setString(5, error);
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
		}
		return result;
	}
	
	public int deleteServer(String ip) {
		int result = 0;
		if(!ip.isEmpty()){
			String sql = "delete from server where(ip=?)";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, ip);
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
		}
		return result;
	}
	
	/**查询数据，符合status状态为“active”的代理服务器
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryData() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from server where status=\"active\"";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("ip", rs.getString("ip"));
				rsTree.put("port", rs.getString("port"));
				rsTree.put("status", rs.getString("status"));
				rsTree.put("error", rs.getString("error"));
				list.add(rsTree);
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
		return list;
	}
	/**查询数据库所有数据
	 * @return
	 */
	public ArrayList<HashMap<String, String>> query() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from server";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("ip", rs.getString("ip"));
				rsTree.put("port", rs.getString("port"));
				rsTree.put("valid", rs.getString("valid"));
				rsTree.put("status", rs.getString("status"));
				rsTree.put("error", rs.getString("error"));
				list.add(rsTree);
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
		return list;
	}
	/**查询数据库，符合valid状态为“1”（即代理服务器有效）
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryValid() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from server where valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("ip", rs.getString("ip"));
				rsTree.put("port", rs.getString("port"));
				rsTree.put("valid", rs.getString("valid"));
				list.add(rsTree);
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
		return list;
	}
	
	/**查询数据库，符合valid状态为“1”（即代理服务器有效）
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryip(String ip,String port) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from server where ip=? and port=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("ip", rs.getString("ip"));
				rsTree.put("port", rs.getString("port"));
				list.add(rsTree);
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
		return list;
	}
	
	
	

}
