package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.HotWordBean;
import com.cbt.parse.daoimp.IHotWordDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotWordDao implements IHotWordDao {

	@Override
	public int add(HotWordBean bean) {
		int result = 0;
		String sql = "insert hotsale (hotwords,url,img,minprice,maxprice,morder,punit,gunit,time,wprice,name,pid,info,weight,pweight,width) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getHotwords());
			stmt.setString(2, bean.getUrl());
			stmt.setString(3, bean.getImg());
			stmt.setDouble(4, bean.getMinprice());
			stmt.setDouble(5, bean.getMaxprice());
			stmt.setInt(6, bean.getMorder());
			stmt.setString(7, bean.getPunit());
			stmt.setString(8, bean.getGunit());
			stmt.setString(9, TypeUtils.getTime());
			stmt.setString(10, bean.getWprice());
			stmt.setString(11, bean.getName());
			stmt.setString(12, bean.getPid());
			stmt.setString(13, bean.getInfo());
			stmt.setString(14, bean.getWeight());
			stmt.setString(15, bean.getPweight());
			stmt.setString(16, bean.getWidth());
			
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int update(HotWordBean bean) {
		int result = 0;
		String sql = "update hotsale set img=?,wprice=?,name=?,pid=?,info=?,weight=?,pweight=?,width=?,minprice=?,maxprice=?,"
				+ "morder=?,punit=?,gunit=?,hotwords=?,time=? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getImg());
			stmt.setString(2, bean.getWprice());
			stmt.setString(3, bean.getName());
			stmt.setString(4, bean.getPid());
			stmt.setString(5, bean.getInfo());
			stmt.setString(6, bean.getWeight());
			stmt.setString(7, bean.getPweight());
			stmt.setString(8, bean.getWidth());
			stmt.setDouble(9, bean.getMinprice());
			stmt.setDouble(10, bean.getMaxprice());
			stmt.setInt(11, bean.getMorder());
			stmt.setString(12, bean.getPunit());
			stmt.setString(13, bean.getGunit());
			stmt.setString(14, bean.getHotwords());
			stmt.setString(15, TypeUtils.getTime());
			stmt.setString(16, bean.getUrl());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int updateValid(String url,int valid) {
		int result = 0;
		String sql = "update hotsale set valid=? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setString(2,url);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public ArrayList<HotWordBean> querySearch(String hotwords) {
		ArrayList<HotWordBean> list = new ArrayList<HotWordBean>();
		String sql = "select name,img,url,minprice,maxprice,valid,morder,gunit,punit from hotsale where match(hotwords) against (? in boolean mode) and valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs  = null;
		HotWordBean bean = null;
		if(conn!=null){
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, " +"+hotwords);
				rs = stmt.executeQuery();
				while(rs.next()){
					bean = new HotWordBean();
					bean.setGunit(rs.getString("gunit"));
					bean.setImg(rs.getString("img"));
					bean.setMinprice(rs.getDouble("minprice"));
					bean.setMaxprice(rs.getDouble("maxprice"));
					bean.setValid(rs.getInt("valid"));
					bean.setMorder(rs.getInt("morder"));
					bean.setPunit(rs.getString("punit"));
					bean.setUrl(rs.getString("url"));
					bean.setName(rs.getString("name"));
					list.add(bean);
					bean = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
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
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public HotWordBean queryGoods(String url,String pid) {
		String sql = "select name,img,url,wprice,valid,morder,gunit,punit,type,info,weight from hotsale where url=?";
		if(pid!=null&&!pid.isEmpty()){
			sql = "select name,img,url,wprice,valid,morder,gunit,punit,type,info,weight from hotsale where pid=?";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs  = null;
		HotWordBean bean = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(pid!=null&&!pid.isEmpty()){
				stmt.setString(1, pid);
			}else{
				stmt.setString(1, url);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				bean = new HotWordBean();
				bean.setGunit(rs.getString("gunit"));
				bean.setImg(rs.getString("img"));
				bean.setValid(rs.getInt("valid"));
				bean.setMorder(rs.getInt("morder"));
				bean.setPunit(rs.getString("punit"));
				bean.setUrl(rs.getString("url"));
				bean.setWprice(rs.getString("wprice"));
				bean.setInfo(rs.getString("info"));
				bean.setType(rs.getString("type"));
				bean.setWeight(rs.getString("weight"));
				bean.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return bean;
	}
	
	
	
	public int queryExsis(String hotwords) {
		String sql = "select count(*) from hotsale where match(hotwords) against (? in boolean mode) and valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs  = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, " +"+hotwords);
			rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

}
