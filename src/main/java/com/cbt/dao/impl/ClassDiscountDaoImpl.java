package com.cbt.dao.impl;

import com.cbt.bean.DiscountBean;
import com.cbt.dao.ClassDiscountDao;
import com.cbt.jdbc.DBHelper;

import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassDiscountDaoImpl implements ClassDiscountDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ClassDiscountDaoImpl.class);

	@Override
	public List<DiscountBean> getDiscount(String catid,String price,String desopite) {
		String sql = "select *from class_discount where 1=1 ";
		if(catid!=null&&!catid.isEmpty()){
			sql +=" and cid=? ";
		}
		if(price!=null&&!price.isEmpty()){
			sql +=" and price=? ";
		}
		if(desopite!=null&&!desopite.isEmpty()){
			sql +=" and deposit_rate=? ";
		}
		
		sql += " order by classtype asc,price desc";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<DiscountBean> list = new ArrayList<DiscountBean>();
		try {
			stmt = conn.prepareStatement(sql);
			int index=1;
			if(catid!=null&&!catid.isEmpty()){
				stmt.setString(index, catid);
				index ++;
			}
			if(price!=null&&!price.isEmpty()){
				stmt.setInt(index, Integer.valueOf(price));
				index ++;
			}
			if(desopite!=null&&!desopite.isEmpty()){
				stmt.setDouble(index, Double.valueOf(desopite));
				index ++;
			}
			rs = stmt.executeQuery();
			DiscountBean bean = null;
			while(rs.next()){
				bean = new DiscountBean();
				bean.setCatid(rs.getString("cid"));
				bean.setClassName(rs.getString("classname"));
				bean.setClassType(rs.getInt("classtype"));
				bean.setShowName(rs.getString("showname"));
				bean.setDepositRate(rs.getDouble("deposit_rate"));
				bean.setId(rs.getInt("id"));
				bean.setPrice(rs.getInt("price"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public int add(DiscountBean bean) {
		String sql = "insert into class_discount (showname,classname,price,"
				+ "deposit_rate,cid,classtype) values(?,?,?,?,?,?) ";
		
//		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
//		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, bean.getShowName());
//			stmt.setString(2, bean.getClassName());
//			stmt.setInt(3, bean.getPrice());
//			stmt.setDouble(4, bean.getDepositRate());
//			stmt.setString(5, bean.getCatid());
//			stmt.setInt(6, bean.getClassType());
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(bean.getShowName());
			lstValues.add(bean.getClassName());
			lstValues.add(String.valueOf(bean.getPrice()));
			lstValues.add(String.valueOf(bean.getDepositRate()));
			lstValues.add(bean.getCatid());
			lstValues.add(String.valueOf(bean.getClassType()));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			LOG.error("",e);
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					LOG.error("",e);
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int delete(int id) {
		String sql = "delete from class_discount where id=?";
		
//		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
//		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1,id);
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(id));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			LOG.error("",e);
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					LOG.error("",e);
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int update(DiscountBean bean) {
		String sql = "update class_discount set showname=?,classname=?,price=?,"
				+ " deposit_rate=?,cid=?,classtype=? where id=?";
		
//		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
//		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, bean.getShowName());
//			stmt.setString(2, bean.getClassName());
//			stmt.setInt(3, bean.getPrice());
//			stmt.setDouble(4, bean.getDepositRate());
//			stmt.setString(5, bean.getCatid());
//			stmt.setInt(6, bean.getClassType());
//			stmt.setInt(7, bean.getId());
//			rs = stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(bean.getShowName());
			lstValues.add(bean.getClassName());
			lstValues.add(String.valueOf(bean.getPrice()));
			lstValues.add(String.valueOf(bean.getDepositRate()));
			lstValues.add(bean.getCatid());
			lstValues.add(String.valueOf(bean.getClassType()));
			lstValues.add(String.valueOf(bean.getId()));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			LOG.error("",e);
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					LOG.error("",e);
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public int isExsis(String catid) {
		String sql = "select count(*) as c from  class_discount where cid=?";
		
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			rs = stmt.executeQuery();
			while(rs.next()){
				result  =rs.getInt("c");
			}
			
		} catch (Exception e) {
			LOG.error("isExsis",e);
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

}
