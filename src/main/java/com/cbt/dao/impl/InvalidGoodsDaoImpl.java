package com.cbt.dao.impl;

import com.cbt.bean.IntensveBean;
import com.cbt.bean.InvalidUrlBean;
import com.cbt.dao.InvalidGoodsDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvalidGoodsDaoImpl implements InvalidGoodsDao {

	@Override
	public int addFilterGoodsUrl(String goodsUuid,String goodsPid) {
		String sql = "select  count(1) as num from filter_data_goods where goods_pid=?";
		String sql2 = "insert into filter_data_goods (goods_uuid,goods_pid,create_time)"
				+ " values(?,?,now()) ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		int result = 0;
		PreparedStatement stmt = null;
//		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodsPid);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("num");
			}
			
			if(result == 0){
//				stmt2 = conn.prepareStatement(sql2);
//				stmt2.setString(1, goodsUuid);
//				stmt2.setString(2, goodsPid);
//				result = stmt2.executeUpdate();

				List<String> lstValues = new ArrayList<String>();
				lstValues.add(goodsUuid);
				lstValues.add(goodsPid);

				String runSql = DBHelper.covertToSQL(sql2,lstValues);
				result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

			}else{
				result = -2;
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
//			if (stmt2 != null) {
//				try {
//					stmt2.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	@Override
	public int addFilterStoreUrl(String storeUrl,String storeId) {
		storeUrl = storeUrl.startsWith("//") ? "http:"+storeUrl : storeUrl;
		String sql = "select  count(1) as num from filter_data_store where store_url=?";
		String sql2 = "insert into filter_data_store (store_url,store_id,create_time)"
				+ " values(?,?,now()) ";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		int result = 0;
		PreparedStatement stmt = null;
//		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, storeUrl);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("num");
			}
			
			if(result == 0){
//				stmt2 = conn.prepareStatement(sql2);
//				stmt2.setString(1, storeUrl);
//				stmt2.setString(2, storeId);
//				result = stmt2.executeUpdate();

				List<String> lstValues = new ArrayList<String>();
				lstValues.add(storeUrl);
				lstValues.add(storeId);

				String runSql = DBHelper.covertToSQL(sql2,lstValues);
				result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

			}else{
				result = -2;
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
//			if (stmt2 != null) {
//				try {
//					stmt2.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public int updateGoodsUrl(String goodsUuid,String goodsPid) {
		//线上表不存在不需要改
		String sql0 = "update custom_benchmark_ready set valid=3 where pid=?";
		String sql1 = "update off_shelf_list set shelf_flag=3 where goods_pid=?";
//		String sql = "update goods_data_new set goods_valid=6 where goods_pid=?";
		String sql2 = "update custom_benchmark_ready_cloud set valid=3 where pid=?";
		
//		String sqlinsert = "insert into goods_data_new (goods_name,goods_url,goods_freeflag,"
//				+ "goods_valid,goods_uuid,goods_pid,goods_freight,goods_posttime,"
//				+ "goods_method,goods_sellprice,create_time) values (?,?,?,?,?,?,?,?,?,?,now()) ";
		Connection conn = DBHelper.getInstance().getConnection2();
		int rs = 0;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			
			if(goodsUuid.startsWith("D")){
				stmt = conn.prepareStatement(sql0);
				stmt.setString(1,goodsPid);
				rs = stmt.executeUpdate();
			}else if(goodsUuid.startsWith("N")){
				stmt = conn.prepareStatement(sql2);
				stmt.setString(1,goodsPid);
				rs = stmt.executeUpdate();
				
			}else if(goodsUuid.startsWith("A")){
				stmt1 = conn.prepareStatement(sql1);
				stmt1.setString(1,goodsPid);
				rs = stmt1.executeUpdate();
//				if(rs == 0){
//					stmt = conn.prepareStatement(sql);
//					stmt.setString(1,goodsPid);
//					rs = stmt.executeUpdate();
//					if(rs == 0){
//						stmt2 = conn.prepareStatement(sqlinsert);
//						stmt2.setString(1,"Oops, the product has been taken off shelf ");
//						stmt2.setString(2,"http://www.aliexpress.com/item/a/"+goodsPid+".html");
//						stmt2.setInt(3,1);
//						stmt2.setInt(4,6);
//						stmt2.setString(5,goodsUuid);
//						stmt2.setString(6,goodsPid);
//						stmt2.setString(7, "0.00");
//						stmt2.setString(8, "9-15");
//						stmt2.setString(9, "ePacket");
//						stmt2.setString(10, "");
//						rs = stmt2.executeUpdate();
//					}
//				}
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public List<InvalidUrlBean> getInvalidUrls(int page, int type) {
		
		
		String sql = "select sql_calc_found_rows distinct *from filter_data_goods order by create_time desc limit ?,40 ";
		if(type == 1){
			sql = "select sql_calc_found_rows distinct *from filter_data_store order by create_time desc limit ?,40 ";
		}
		
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		int count  = 0;
		List<InvalidUrlBean> list = new ArrayList<InvalidUrlBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,page);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows(); ");
			rs2 = stmt2.executeQuery();
			while(rs2.next()){
				count = rs2.getInt("found_rows()");
			}
			String aliUrl="";
			while(rs.next()){
				InvalidUrlBean bean = new InvalidUrlBean();
				bean.setCreatetime(rs.getString("create_time"));
				String filter = "";
				String uuid = "";
				if(type == 1){
					filter = rs.getString("store_url");
				}else{
					uuid = rs.getString("goods_uuid");
					filter = rs.getString("goods_pid");
					aliUrl = "&source=" + uuid+ "&item="+filter;
				}
				if(filter == null || filter.isEmpty()){
					continue;
				}
				if(type == 0){
					bean.setUrl(uuid.startsWith("A")? "http://www.aliexpress.com/item/a/"+filter+".html" : "http://detail.1688.com/offer/"+filter+".html");
				}else{
					bean.setUrl(filter);
				}
				
				bean.setAliUrl(aliUrl);
				bean.setCount(count);
				bean.setId(rs.getInt("id"));
				list.add(bean);
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
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	@Override
	public List<IntensveBean> getIntensve() {
		
		
		String sql = "select a.word,a.cid,a.valid,a.catid,b.category from intensve a,ali_category b where a.catid=b.cid order by a.cid asc";
		Connection conn = DBHelper.getInstance().getConnection2();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<IntensveBean> list = new ArrayList<IntensveBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				IntensveBean bean = new IntensveBean();
				bean.setCatid(rs.getString("catid"));
				bean.setKeyword(rs.getString("word"));
				int type = rs.getInt("cid");
				bean.setStatus(rs.getInt("valid") == 1 ? "启用" : "不启用");
				bean.setType(type == 1 ? "品牌产品" : type == 2 ? "词+类别屏蔽" : "类别屏蔽");
				bean.setCategory(rs.getString("category"));
				list.add(bean);
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

}
