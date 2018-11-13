package com.cbt.dao.impl;

import com.cbt.dao.ZoneDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.warehouse.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ZoneDaoImpl implements ZoneDao {

	@Override
	public String getCountryById(String id,String userid) {
		String result = "";
		String sql = "select country  from zone where id=? or country=?";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("country");
			}
			if(StringUtil.isBlank(result)){
				sql="SELECT z.country FROM order_address ad " +
					" INNER JOIN orderinfo oi ON ad.orderNo=oi.order_no " +
					" INNER JOIN zone z ON z.id=ad.country OR z.country=ad.country" +
					" WHERE oi.user_id="+userid+" ORDER BY ad.id DESC LIMIT 1";
				stmt=conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				if(rs.next()){
					result = rs.getString("country");
				}
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
		return result;
	}

}
