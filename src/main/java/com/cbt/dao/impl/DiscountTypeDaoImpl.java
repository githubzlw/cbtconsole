/**
 * 
 */
package com.cbt.dao.impl;

import com.cbt.dao.IDiscountTypeDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 *
 */
@Component
public class DiscountTypeDaoImpl implements IDiscountTypeDao {

	/**
	 * 类别折扣上限查询
	 */
	@Override
	public List<HashMap<String, String>> queryForAliCategory(String cid,String category) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT ac.cid,ac.path,ac.category,ac.time,dt.max_discount FROM ali_category ac LEFT JOIN discount_type dt ON dt.cid = ac.cid where 1=1 ";
		if(cid != null && cid.length()>0){
			sql = sql+" and ac.cid = ?";
		}
		if(category != null && category.length()>0){
			sql = sql+" and ac.category like ?;";
		}
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String>  map = null;
		try {
			stmt = conn.prepareStatement(sql);
			int index = 1;
			if(cid != null && cid.length()>0){
				stmt.setString(index, cid);
				index++;
			}
			if(category != null && category.length()>0){
				stmt.setString(index, "%"+category+"%");
			}
			rs = stmt.executeQuery();
			if(rs!= null){
				while(rs.next()){
					map = new HashMap<String, String>();
					map.put("cid", rs.getString("cid"));
					map.put("category", rs.getString("category"));
					map.put("maxDiscount", rs.getString("max_discount"));
					list.add(map);
				}
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

	/**
	 * 类别折扣上限修改
	 */
	@Override
	public int updateDiscountTypeByCid(String cid,String category,double maxDiscount) {
		String sql = "select count(*) count from discount_type where cid = ?";
		String sql1 = "INSERT INTO discount_type(cid,category,max_discount) VALUE(?,?,?);";
		String sql2 = "UPDATE discount_type SET max_discount = ? WHERE cid = ?;";
		Connection con = DBHelper.getInstance().getConnection();
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		int rows = 0;
		int rs1 = 1;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cid);
			rs = stmt.executeQuery();
			if (!rs.wasNull()) {
				while (rs.next())
				{
					rows=rs.getInt("count");
				}
			}
			if(rows>0){//大于0就说明数据存在则更新
//				stmt2 = conn.prepareStatement(sql2);
//				stmt2.setDouble(1, maxDiscount);
//				stmt2.setString(2, cid);
//				rs1 = stmt2.executeUpdate();

				List<String> lstValues = new ArrayList<String>();
				lstValues.add(String.valueOf(maxDiscount));
				lstValues.add(String.valueOf(cid));
				String runSql = DBHelper.covertToSQL(sql2,lstValues);
				rs1 = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

				//同时 修改本地
				ps2 = con.prepareStatement(sql2);
				ps2.setDouble(1, maxDiscount);
				ps2.setString(2, cid);
				ps2.executeUpdate();
			}else{//否则插入数据
				stmt1 = conn.prepareStatement(sql1);
//				stmt1.setString(1, cid);
//				stmt1.setString(2, category);
//				stmt1.setDouble(3, maxDiscount);
//				rs1 = stmt1.executeUpdate();
				List<String> lstValues = new ArrayList<String>();
				lstValues.add(String.valueOf(cid));
				lstValues.add(String.valueOf(category));
				lstValues.add(String.valueOf(maxDiscount));
				String runSql = DBHelper.covertToSQL(sql1,lstValues);
				rs1 = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

				//同时修改本地
				ps1 = con.prepareStatement(sql1);
				ps1.setString(1, cid);
				ps1.setString(2, category);
				ps1.setDouble(3, maxDiscount);
				ps1.executeQuery();
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
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps2!=null){
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(con);
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs1;
	}

}
