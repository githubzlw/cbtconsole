package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.bean.OrderBuy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BuyUserDaoImp implements BuyUserDao {

	@Override
	public int add(String orderid, String buyuser, int buyid) {
		String sql = "insert order_buy (orderid,buyuser,buyid,time) values(?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, buyuser);
			stmt.setInt(3, buyid);
			result = stmt.executeUpdate();
		} catch (Exception e) {
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public int update(String orderid, String buyuser, int buyid) {
//		String sql = "update order_buy set buyuser=?,buyid=? where orderid=?";
		String sql = "delete from order_buy  where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, buyuser);
//			stmt.setInt(2, buyid);
			stmt.setString(1, orderid);
			result = stmt.executeUpdate();
		} catch (Exception e) {
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	@Override
	public ArrayList<OrderBuy> query(String orderid) {
		String sql = "select buyid,buyuser,orderid from order_buy where orderid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet result = null;
		ArrayList<OrderBuy> list = new  ArrayList<OrderBuy>();
		PreparedStatement stmt = null;
		OrderBuy buy = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			result = stmt.executeQuery();
			while(result.next()){
				buy = new OrderBuy();
				buy.setBuyId(result.getInt("buyid"));
				buy.setBuyUser(result.getString("buyuser"));
				buy.setOrderid(result.getString("orderid"));
				list.add(buy);
				buy = null;
			}
		} catch (Exception e) {
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

}
