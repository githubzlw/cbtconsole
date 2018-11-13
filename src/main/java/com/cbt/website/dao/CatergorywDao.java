package com.cbt.website.dao;

import com.cbt.bean.Eightcatergory;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatergorywDao implements ICatergorywDao {

	@Override
	public List<Eightcatergory> getCatergory(String id,String catergory,Integer type) {
		StringBuffer sql = new StringBuffer("select id,row,catergory,minorder,unit,price,url,imgurl,productname from eightcatergory  where 1 = 1 ");
		if (type != null) {
			sql.append("and valid = "+type+" ");
		}		
		if (id != null && !"".equals(id)) {
			sql.append("and id = "+ id);
		} 
		if (catergory != null && !"".equals(catergory)) {
			sql.append(" and catergory = '"+catergory+"' ");
		}
		sql.append(" order by id asc");
		Connection conn = DBHelper.getInstance().getConnection();
		List<Eightcatergory> home = new ArrayList<Eightcatergory>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				Eightcatergory temp=new Eightcatergory();
				temp.setId(rs.getInt("id"));
				temp.setRow(rs.getInt("row"));
				temp.setCatergory(rs.getString("catergory"));
				temp.setMinorder(rs.getInt("minorder"));
				temp.setUnit(rs.getString("unit"));
				temp.setPrice(rs.getFloat("price"));
				temp.setUrl(rs.getString("url"));
				temp.setImgurl(rs.getString("imgurl"));
				temp.setProductname(rs.getString("productname"));
				home.add(temp);
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
		return home;
	}

	@Override
	public int upCatergory(Eightcatergory catergory) {
		String sql ="update   eightcatergory set minorder=?,unit=?,price=?,url=?,imgurl=?,productname=?,catergory=?,id=?   where row=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, catergory.getMinorder());
			stmt.setString(2, catergory.getUnit());
			stmt.setFloat(3, catergory.getPrice());
			stmt.setString(4, catergory.getUrl());
			stmt.setString(5, catergory.getImgurl());
			stmt.setString(6, catergory.getProductname());
			stmt.setString(7, catergory.getCatergory());
			stmt.setInt(8, catergory.getId());
			stmt.setInt(9, catergory.getRow());
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
	public int addCatergory(Eightcatergory catergory) {
		String sql ="insert into eightcatergory(minorder,unit,price,url,imgurl,productname,id,catergory)  values(?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, catergory.getMinorder());
			stmt.setString(2, catergory.getUnit());
			stmt.setFloat(3, catergory.getPrice());
			stmt.setString(4, catergory.getUrl());
			stmt.setString(5, catergory.getImgurl());
			stmt.setString(6, catergory.getProductname());
			stmt.setInt(7, catergory.getId());
			stmt.setString(8, catergory.getCatergory());
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
	public List<Object> getCategoryList() {
		String sql ="select distinct catergory from eightcatergory";
		 
		Connection conn = DBHelper.getInstance().getConnection();
		List<Object> cateList = new ArrayList<Object>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				cateList.add(rs.getString("catergory"));
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
		return cateList;
	}

	@Override
	public int deleteCatergory(Integer row) {
		String sql = "delete from eightcatergory where row = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, row);//状态改为已回复
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
}
