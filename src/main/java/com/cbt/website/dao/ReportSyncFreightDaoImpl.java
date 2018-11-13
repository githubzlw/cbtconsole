package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportSyncFreightDaoImpl implements IReportSyncFreightDao {

	/**
	 * 获取未填入重量和运费的 运单号 
	 */
	@Override
	public List<String> getExpressno(String flag) {
		Connection conn = DBHelper.getInstance().getConnection();
//		String sql = "select DISTINCT expressno from shipping_package where expressno is not null and expressno <> ''  ";
//		//flag为new 查新数据，为old查旧数据
//		if(flag.equals("new")){
//			sql += " and shipmentno is not null and ( settleWeight is null or settleWeight=0.00 or totalPrice is null or totalPrice=0.00)";
//		}else if(flag.equals("old")){
//			sql += " and shipmentno is null and ( settleWeight is null or settleWeight=0.00 or totalPrice is null or totalPrice=0.00)";
//		}
		String sql ="select DISTINCT expressno from shipping_package where expressno is not null and expressno <> '' and ( settleWeight is null or settleWeight=0.00 or totalPrice is null or totalPrice=0.00)";
		List<String> list = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getString("expressno")!=null && !"".equals(rs.getString("expressno"))){
					list.add(rs.getString("expressno"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	/**
	 * 通过运单号到shipment中获取重量和运费总合
	* @Title: getFreightByExpressno 
	 */
	@Override
	public List<Object[]> getFreightByExpressno(List<String> list, String flag){
		List<Object[]> list_price = new ArrayList<Object[]>();
		if(list.size()==0 || list==null){
			return null;
		}
		String sql = "";
		if(flag.equals("new")){
			sql = "select orderNo,settleWeight,totalPrice from shipment where 1=1 "; //查新数据
		}else{
			sql = "select orderNo,settleWeight,totalPrice from shipment_backup where 1=1 "; //查旧数据
		}
		if(list.size()==1){
			sql += "and  orderNo=?";
		}else{
			for(int i=0;i<list.size();i++){
				if(i==0){
					sql += " and (  orderNo=?";
				}else if(i==(list.size()-1)){
					sql += " or orderNo=? )";
				}else{
					sql +=" or orderNo=?";
				}
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			for(int i=0;i<list.size();i++){
				pst.setString(i+1,list.get(i));
			}
			rs = pst.executeQuery();
			while(rs.next()){
				Object[] obj = new Object[3];
				obj[0] = rs.getString("orderNo");
				if(rs.getString("settleWeight")!=null && !"".equals(rs.getString("settleWeight"))){
					obj[1] = rs.getDouble("settleWeight");
				}else{
					obj[1]=0.00;
				}
				if(rs.getString("totalPrice")!=null && !"".equals(rs.getString("totalPrice"))){
					obj[2] = rs.getDouble("totalPrice");
				}else{
					obj[2]=0.00;
				}
				list_price.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list_price;
	}
	
	//填充 重量和运费
	@Override
	public int updateWeightFreight(List<Object[]> list){
		int res =0;
		if(list.size()==0 || list==null){
			return -1;
		}
		String sql = "update shipping_package set settleWeight=?,totalPrice=? where expressno=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			for(int i=0;i<list.size();i++){
				pst.setDouble(1, Double.parseDouble(list.get(i)[1].toString()));
				pst.setDouble(2, Double.parseDouble(list.get(i)[2].toString()));
				pst.setString(3, list.get(i)[0].toString());
				pst.addBatch();
			}
			int[] result = pst.executeBatch();
			res = result.length;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}

}
