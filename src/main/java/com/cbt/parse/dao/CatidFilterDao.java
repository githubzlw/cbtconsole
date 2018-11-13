package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.CatidFilterBean;
import com.cbt.parse.daoimp.ICatidFilterDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**1688转aliexpress过滤
 * @author abc
 * catid的过滤类型   type值为：
 * 1-为指定类别搜索跳转到1688； 
 * 2-为指定类别默认重量；
 * 3-为既跳转1688又指定重量；
 * 4-为指定类别设定最小价格用于价格排序；
 * 5-关键词过滤到1688；
 * 6-指定类别销量限定；
 * 7-指定类别跳转aliexpress240；
 *
 */
public class CatidFilterDao implements ICatidFilterDao {
	
	public int add(CatidFilterBean bean){
		int result = 0;
		String sql = "insert catid_filter (catid,type,minprice,createtime) value (?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getCatid());
			stmt.setInt(2, bean.getType());
			stmt.setDouble(3, bean.getMinPrice());
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
	
	public ArrayList<CatidFilterBean> query(String catid, int type){
		ArrayList<CatidFilterBean> list = new ArrayList<CatidFilterBean>();
		String sql = "select minprice,weight,catid,type,valid from catid_filter where catid=? and type=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		CatidFilterBean bean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			stmt.setInt(2, type);
			set = stmt.executeQuery();
			while(set.next()){
				bean = new CatidFilterBean();
				bean.setCatid(set.getString("catid"));
				bean.setType(set.getInt("type"));
				bean.setWeight(set.getString("weight"));
				bean.setValid(set.getInt("valid"));
				bean.setMinPrice(set.getDouble("minprice"));
				list.add(bean);
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public String queryPriceFilter(String catid){
		String list = null;
		String sql = "select minprice from catid_filter where type=4 and valid=1 and catid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getString("minprice");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public String queryWeightFilter(String catid){
		String list = null;
		String sql = "select weight from catid_filter where valid=1 and type=3 and catid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getString("weight");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public String queryCidFilter(String catid){
		String list = null;
		String sql = "select catid from catid_filter where (type=1 or type=3) and valid=1 and catid=?  ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getString("catid");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public int queryKeyWordsFilter(String catid,String keywords){
		int list = 0;
		String sql = "select count(*) from catid_filter where type=5 and valid=1 and catid=? and keywords=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getInt("count(*)");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	/**指定类别
	 * @param catid
	 * @return
	 */
	@Override
	public int getCatidFilter(String catid,int type) {
		int list = 0;
		String sql = "select count(*) from catid_filter where type=? and valid=1 and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			stmt.setInt(2, type);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getInt("count(*)");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	
	
	@Override
	public int querySoldFilter(String catid) {
		int list = 1;
		String sql = "select sold from catid_filter where type=8 and valid=1 and catid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			set = stmt.executeQuery();
			if(set.next()){
				list = set.getInt("sold");
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	

}
