package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.CnEnDaoBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CnEnDao {
	
	public int add(String cntext,String entext,int cnlength,int psort,String ptype ){
		int result = 0;
		String sql="insert cn_en_trant (cntext,entext,cnlength,psort,ptype) value(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cntext);
			stmt.setString(2, entext);
			stmt.setInt(3, cnlength);
			stmt.setInt(4, psort);
			stmt.setString(5, ptype);
			
			result = stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		}
		
	   DBHelper.getInstance().closeConnection(conn);
		return result;
	}
	public int update(String cntext,String entext,int cnlength,int psort,String ptype ){
		int result = 0;
		String sql="update cn_en_trant set entext=?,cnlength=?,psort=?,ptype=? where cntext=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			System.out.println("cntext:"+cntext);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, entext);
			stmt.setInt(2, cnlength);
			stmt.setInt(3, psort);
			stmt.setString(4, ptype);
			stmt.setString(5, cntext);
			
			result = stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		}
		
		DBHelper.getInstance().closeConnection(conn);
		return result;
	}
	
	public int queryExsis(String cntext){
		int result = 0;
		String sql = "select id from cn_en_trant where cntext=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cntext);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	   DBHelper.getInstance().closeConnection(conn);
		
		return result;
	}
	
	public ArrayList<CnEnDaoBean> query(){
		ArrayList<CnEnDaoBean> list = new ArrayList<CnEnDaoBean>();
		CnEnDaoBean bean = null;
		String sql = "select cntext,entext,cnlength,psort from cn_en_trant order by cnlength desc";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new CnEnDaoBean();
				bean.setCntext(rs.getString("cntext"));
				bean.setEntext(rs.getString("entext"));
				bean.setCnlength( rs.getInt("cnlength"));
				bean.setSport(rs.getInt("psort"));
				list.add(bean);
				bean = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	   DBHelper.getInstance().closeConnection(conn);
		return list;
	}

}
