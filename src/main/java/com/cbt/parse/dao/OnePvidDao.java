package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.PvidBean;
import com.cbt.parse.daoimp.IOnePvidDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class OnePvidDao implements IOnePvidDao {

	@Override
	public int add(PvidBean bean) {
		String sql = "insert one_catid_pvid (sort,catid,param,value,pvid,img,createtime) values (?,?,?,?,?,?,now())";
		int result = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bean.getSort());
			stmt.setString(2, bean.getCatid());
			stmt.setString(3, bean.getName());
			stmt.setString(4, bean.getValue());
			stmt.setString(5, bean.getPvid());
			stmt.setString(6, bean.getImg());
			result  =stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	@Override
	public int update(PvidBean bean) {
		String sql = "update one_catid_pvid set catid=CONCAT_WS(',',catid,?) where param=? and value=?";
		int result = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getCatid());
			stmt.setString(2, bean.getName());
			stmt.setString(3, bean.getValue());
			result  =stmt.executeUpdate();
		} catch (Exception e) {
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public ArrayList<PvidBean> query(String catid, String keyword) {
		String sql = "select param,pvid,img,value,catid from one_catid_pvid ";
		if(catid!=null&&!"0".equals(catid)){
			sql += "where match(catid) against (? in boolean mode)";
		}
		sql += " order by sort desc"; 
		ArrayList<PvidBean>  list = new ArrayList<PvidBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PvidBean bean = null;
		try {
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			if(catid!=null&&!"0".equals(catid)){
				stmt.setString(1, " +"+catid);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setName(rs.getString("param"));
				bean.setValue(rs.getString("value"));
				bean.setPvid(rs.getString("pvid"));
				bean.setImg(rs.getString("img"));
				bean.setCatid(rs.getString("catid"));
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	public ArrayList<PvidBean> getParamGroup(String catid) {
		String sql = "select param,pvid,img,value,catid from one_catid_pvid ";
		if(catid!=null&&!"0".equals(catid)){
			sql += "where match(catid) against (? in boolean mode)";
		}
		sql += " group by param order by sort desc"; 
		ArrayList<PvidBean>  list = new ArrayList<PvidBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PvidBean bean = null;
		try {
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			if(catid!=null&&!"0".equals(catid)){
				stmt.setString(1, " +"+catid);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setName(rs.getString("param"));
				bean.setValue(rs.getString("value"));
				bean.setPvid(rs.getString("pvid"));
				bean.setImg(rs.getString("img"));
				bean.setCatid(rs.getString("catid"));
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public ArrayList<PvidBean> queryAll() {
		String sql = "select param,pvid,img,value,catid from one_catid_pvid where sort=0 group by param   order by id asc ";
		ArrayList<PvidBean>  list = new ArrayList<PvidBean>();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PvidBean bean = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setName(rs.getString("param"));
				bean.setValue(rs.getString("value"));
				bean.setPvid(rs.getString("pvid"));
				bean.setImg(rs.getString("img"));
				bean.setCatid(rs.getString("catid"));
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public ArrayList<PvidBean> queryExsis(String param, String value) {
		String sql = "select distinct *from one_catid_pvid where param=? and value=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<PvidBean>  list = new ArrayList<PvidBean>();
		PvidBean bean = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, param);
			stmt.setString(2, value);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setCatid(rs.getString("catid"));
				bean.setPvid(rs.getString("pvid"));
				list.add(bean);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

}
