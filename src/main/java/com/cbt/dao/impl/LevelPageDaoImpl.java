package com.cbt.dao.impl;

import com.cbt.bean.LevelPageBean;
import com.cbt.dao.LevelPageDao;
import com.cbt.jdbc.DBHelper;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LevelPageDaoImpl implements LevelPageDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(LevelPageDaoImpl.class);

	@Override
	public List<LevelPageBean> getList(int page) {
		String sql = "select sql_calc_found_rows *from level_page where 1=1  ";
		
		sql = sql + "limit "+((page-1)*40)+",40";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int count = 0;
		List<LevelPageBean> list = new ArrayList<LevelPageBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if(rs2.next()){
				count = rs2.getInt("found_rows()");
			}
			
			LevelPageBean bean = null;
			while(rs.next()){
				bean = new LevelPageBean();
				bean.setCount(count);
				bean.setCatid(rs.getString("catid"));
				bean.setName(rs.getString("name"));
				bean.setPage(rs.getString("page"));
				bean.setValid(rs.getInt("valid"));
				bean.setId(rs.getInt("id"));
				bean.setCreateTime(rs.getString("createtime"));
				list.add(bean);
			}
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public int update(LevelPageBean bean) {

		String sql = "update level_page set name=?,page=?,valid=?,catid=?,"
				+ "createtime=now() where 1=1 and id=? ";
		
		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getName());
			stmt.setString(2, bean.getPage());
			stmt.setInt(3, bean.getValid());
			stmt.setString(4, bean.getCatid());
			stmt.setInt(5, bean.getId());
			rs = stmt.executeUpdate();
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int insert(LevelPageBean bean) {
		String sql = "insert into level_page(name,page,catid,valid,createtime) "
				+ "value (?,?,?,1,now()) ";
		
		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getName());
			stmt.setString(2, bean.getPage());
			stmt.setString(3, bean.getCatid());
			rs = stmt.executeUpdate();
			
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int delete(int id) {

		return 0;
	}

}
