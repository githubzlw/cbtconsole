package com.cbt.fee.dao;

import com.cbt.bean.DeliveryFee;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryFeeDao implements IDeliveryFee {

	@Override
	public List<DeliveryFee> getAllDeliveryFee() {
		// TODO Auto-generated method stub
		String sql = "select * from deliveryfee";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DeliveryFee> dflist = new ArrayList<DeliveryFee>();
		try {
			stmt = conn.prepareStatement(sql);
			rs =stmt.executeQuery();
			while(rs.next()){
				DeliveryFee fee= new DeliveryFee();
				fee.setId(rs.getInt("id"));
				fee.setDeliverymode(rs.getString("deliverymode"));
				fee.setExpecteddays(rs.getString("expecteddays"));
				fee.setWeight(rs.getFloat("weight"));
				fee.setDeliveryfee(rs.getFloat("deliveryfee"));
				dflist.add(fee);
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
		return dflist;
	}
	
}
