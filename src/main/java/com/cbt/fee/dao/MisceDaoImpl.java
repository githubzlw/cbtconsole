package com.cbt.fee.dao;

import com.cbt.bean.State;
import com.cbt.bean.StateZip;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MisceDaoImpl implements IMisceDao {

	@Override
	public Map<String, StateZip> getStateZipMap(String state) {
		String sql = "select state,zipcode,vl_freight,wg_freight,deliverytime from statezip where state=? ";
//		其他州你暂时都用 90210
		String sql2 = "select state,zipcode,vl_freight,wg_freight,deliverytime from statezip where zipcode='90210' ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, StateZip> resultMap = new HashMap<String, StateZip>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, state);
			rs = stmt.executeQuery();
			while (rs.next()) {
				StateZip stateZip=new StateZip();
				stateZip.setState(rs.getString("state"));
				String zipCode = rs.getString("zipcode");
				stateZip.setZipCode(zipCode);
				stateZip.setVolumeFreight(rs.getFloat("vl_freight"));
				stateZip.setWeightFreight(rs.getFloat("wg_freight"));
				stateZip.setDeliveryTime(rs.getFloat("deliverytime"));
				resultMap.put(zipCode, stateZip);
			}
			if(resultMap.size()<1){
				stmt=conn.prepareStatement(sql2);
				rs=stmt.executeQuery();
				if(rs.next()){
					StateZip stateZip=new StateZip();
					stateZip.setState(rs.getString("state"));
					String zipCode = rs.getString("zipcode");
					stateZip.setZipCode(zipCode);
					stateZip.setVolumeFreight(rs.getFloat("vl_freight"));
					stateZip.setWeightFreight(rs.getFloat("wg_freight"));
					stateZip.setDeliveryTime(rs.getFloat("deliverytime"));
					resultMap.put(zipCode, stateZip);
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

		return resultMap;
	}
	@Override
	public StateZip getStateZip(String destinationport){
		String sql = "select state,zipcode,vl_freight,wg_freight,deliverytime from statezip where state=? limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StateZip stateZip= new StateZip();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, destinationport);
			rs = stmt.executeQuery();
			if (rs.next()) {
				stateZip.setState(rs.getString("state"));
				String zipCode = rs.getString("zipcode");
				stateZip.setZipCode(zipCode);
				stateZip.setVolumeFreight(rs.getFloat("vl_freight"));
				stateZip.setWeightFreight(rs.getFloat("wg_freight"));
				stateZip.setDeliveryTime(rs.getFloat("deliverytime"));
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
		
		return stateZip;
	}

	@Override
	public float getFreightClass(float pcf) {
		String sql = "select pcfclass from pondpercubicfoot where pcfmin<? and pcfmax>=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		float result=0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setFloat(1, pcf);
			stmt.setFloat(2, pcf);
			rs=stmt.executeQuery();
			if(rs.next()){
				result=rs.getFloat("pcfclass");
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
		return result;
	}

	@Override
	public Map<String, State> getStateList(String state) {
		String sql = "select stateabb,statename,zipcode,destinationport from state where stateabb=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, State> resultMap = new HashMap<String, State>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, state);
			rs = stmt.executeQuery();
			while (rs.next()) {
				State states=new State();
				states.setStateAbb(rs.getString("stateabb"));
				states.setStateName(rs.getString("statename"));
				String zipcode = rs.getString("zipcode");
				if(com.cbt.util.Utility.getStringIsNull(zipcode)){
					if(zipcode.trim().equals("N/A")){
						continue;
					}
				}
				states.setZipCode(zipcode);
				String destinationport = rs.getString("destinationport");
				states.setDestinationPort(destinationport);
//				if(Utility.getStringIsNull(destinationport)){
				resultMap.put(zipcode, states);
//				}
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

		return resultMap;
	}

}
