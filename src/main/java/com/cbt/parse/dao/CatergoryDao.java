package com.cbt.parse.dao;

import com.cbt.bean.Eightcatergory;
import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.ICatergoryDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**八大类信息表操作类eightcatergory.sql
 * @author abc
 *
 */
public class CatergoryDao implements ICatergoryDao{
	
	/**添加一条数据信息
	 * @param id
	 * @param catergory
	 * @param productname
	 * @param minorder
	 * @param unit
	 * @param price
	 * @param url
	 * @param imgurl
	 * @param valid
	 * @return
	 */
	public  int addCatergory(int id,String catergory,String productname,
			int minorder,String unit,float price,String url,String imgurl,int valid) {
		int result = 0;
			String sql = "insert eightcatergory(id,catergory,productname,"
					+ "minorder,unit,price,url,imgurl,valid) "
					+ "values(?,?,?,?,?,?,?,?,?)";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, id);
				stmt.setString(2, catergory);
				stmt.setString(3, productname);
				stmt.setInt(4, minorder);
				stmt.setString(5, unit);
				stmt.setFloat(6, price);
				stmt.setString(7, url);
				stmt.setString(8, imgurl);
				stmt.setInt(9, valid);
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
	
	
	/**更新一条数据信息(更新url是否有效)
	 * @param ip
	 * @param status
	 * @param error
	 * @return
	 */
	public  int updateValid(String url,int valid) {
		int result = 0;
		String sql = "update eightcatergory set valid =? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setString(2, url);
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
	
	
	
	/**查询数据，符合valid状态为1的url
	 * @return
	 */
	public ArrayList<Eightcatergory> queryData() {
		ArrayList<Eightcatergory> list = new ArrayList<Eightcatergory>();
		String sql = "select *from eightcatergory where valid=1 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Eightcatergory rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new Eightcatergory();
				rsTree.setId(rs.getInt("id"));
				rsTree.setRow(rs.getInt("row"));
				rsTree.setCatergory(rs.getString("catergory"));
				rsTree.setProductname(rs.getString("productname"));
				rsTree.setImgurl(rs.getString("imgurl"));
				rsTree.setMinorder(rs.getInt("minorder"));
				rsTree.setPrice(rs.getFloat("price"));
				rsTree.setUnit(rs.getString("unit"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setValid(rs.getInt("valid"));
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
		String sql = "select *from eightcatergory where valid=1 ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("id", String.valueOf(rs.getInt("id")));
				rsTree.put("catergory", rs.getString("catergory"));
				rsTree.put("productname", rs.getString("productname"));
				rsTree.put("minorder", String.valueOf(rs.getInt("minorder")));
				rsTree.put("unit", rs.getString("unit"));
				rsTree.put("price", String.valueOf(rs.getFloat("price")));
				rsTree.put("url", rs.getString("url"));
				rsTree.put("imgurl", rs.getString("imgurl"));
				rsTree.put("valid", String.valueOf(rs.getInt("valid")));
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
