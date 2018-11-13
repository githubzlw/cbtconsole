package com.cbt.website.dao2;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;

public class feeCount {

	//4PX运费计算预估
	public String fpxFee(String country){
		String country_code="";
        String[] cty = country.split("@");
        String sql = "select country_code from fpx_country_code where r_zone like ?";
        Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+cty[cty.length-3]+"%");
			rs = stmt.executeQuery();
			if(rs.next()){
				country_code = rs.getString("country_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return country_code;
	}
	
	//原飞航运费计算预估
	public String yfhFee(String countrycode){
		String area="";
		String sql = "select area_name from fpx_country_code where country_code=?";
        Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, countrycode);
			rs = stmt.executeQuery();
			if(rs.next()) {
				area = rs.getString("area_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return area;
	}
	
	public String getYFHFee(String contry,String weight, String currency, Map<String, Double> maphl){
		double excrate = maphl.get(currency) / maphl.get("RMB");
		String fee = "#";
		double feee = 0;
		String trans_days = "";  //交期天数
		String fir_weight = ""; //首重重量
		double fir_fee = 0;    //首重单价
		double sec_fee = 0;   //续重单价
		String sql = "select * from yfh_fee where area=? and min_weight<=? and max_weight>=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,contry);
			stmt.setString(2,weight);
			stmt.setString(3,weight);
			rs = stmt.executeQuery();
			if(rs.next()){
				fir_weight = rs.getString("first_weight");
				fir_fee = rs.getDouble("first_fee") * excrate;
				sec_fee = rs.getDouble("second_fee") * excrate;
				trans_days = rs.getString("trans_days");
				if(!fir_weight.equals("0")){
					if(Double.parseDouble(weight)<=0.5){
						feee = fir_fee;
					} else {
						feee = Math.ceil((Double.parseDouble(weight)-0.5)/0.5)*sec_fee + fir_fee;
					}
				} else {
					feee = Math.ceil(Double.parseDouble(weight))*sec_fee;
				}
	        	DecimalFormat df = new DecimalFormat("0.00");
				fee = df.format(feee)+"#"+trans_days;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return fee;
	}
	
	
}
