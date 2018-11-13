package com.cbt.searchByPic.dao;

import com.cbt.bean.SearchResults;
import com.cbt.jdbc.DBHelper;
import com.cbt.jdbc.RemoteHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SynchDataDaoImpl implements SynchDataDao {

	@Override
	public List<SearchResults> selectInfoByIndexId(int parseInt) {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
 		PreparedStatement stmt = null;
 		ResultSet  res =null;
 		List<SearchResults>  list  = new  ArrayList<SearchResults>();
 		String sql ="select * from  search_results where index_id = ?";
 	 		try {
 				stmt = conn.prepareStatement(sql);
 				stmt.setInt(1, parseInt);
 				res = stmt.executeQuery();
 				while(res.next()){
 					SearchResults bean = new SearchResults();
 					bean.setId(res.getInt("id"));
 					bean.setAliexpressCatid(res.getString("aliexpress_catid")); 
 					bean.setIndexId(res.getInt("index_id")); 
 					bean.setGoodsPid(res.getString("goods_pid")); 
 					bean.setGoodsUrl(res.getString("goods_url")); 
 					bean.setGoodsName(res.getString("goods_name"));
 					bean.setGoodsNameEn(res.getString("goods_name_en")); 
 					bean.setGoodsPrice(res.getDouble("goods_price")); 
 					bean.setGoodsPriceRe(res.getDouble("goods_price_re")); 
 					bean.setGoodsMorder(res.getInt("goods_morder")); 
 					bean.setGoodsSold(res.getInt("goods_sold")); 
 					bean.setGoodsImg(res.getString("goods_img")); 
 					bean.setDownLoadImg(res.getString("download_img")); 
 					bean.setDownLoadFlag(res.getInt("download_flag")); 
 					bean.setFactoryName(res.getString("factory_name"));
 					bean.setFactoryId(res.getString("factory_id")); 
 					bean.setFactoryUrl(res.getString("factory_url")); 
 					bean.setTrade(res.getString("trade")); 
 					bean.setCreateTime(res.getDate("create_time"));
 					bean.setGoodsValid(res.getInt("goods_valid"));
 					bean.setGoodsType(res.getString("goods_type"));
 					bean.setGoodsTypeEn(res.getString("goods_type_en"));
 					bean.setGoodsDetail(res.getString("goods_detail"));
 					bean.setGoodsDetailEn(res.getString("goods_detail_en"));
 					bean.setGoodsInfo(res.getString("goods_info")); 
 					bean.setGoodsWeight(res.getString("goods_weight"));
 					bean.setUpdateFlag(res.getInt("update_flag"));
 					list.add(bean);
 				}
 				
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	 		finally {
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

	@Override
	public int insertInfo(List<SearchResults> list, int indexId) {
		// TODO Auto-generated method stub
		Connection conn = RemoteHelper.getConnection();
 		PreparedStatement stmt = null;
 		PreparedStatement stmt_1 = null;
 		ResultSet  res_1 =null;
 		int  res = 0 ;
 		int count = 0 ;
 		try {
 			String sql_1 = "select count(*) as num from search_results where index_id = ?";
 			stmt_1 = conn.prepareStatement(sql_1);
 			stmt_1.setInt(1, indexId);
 			res_1 = stmt_1.executeQuery();
 			while(res_1.next()){
 				count = res_1.getInt("num");
 			}
 			if(count==0){
			for(SearchResults bean:list){
				String sql ="insert into search_results (aliexpress_catid,index_id,goods_pid,goods_url,goods_name,goods_name_en,goods_price"
						+ ",goods_morder,goods_sold,goods_img,factory_name,factory_id,factory_url,trade,create_time,goods_valid"
						+ ",goods_type,goods_type_en,goods_detail,goods_detail_en,goods_info,goods_weight,update_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, bean.getAliexpressCatid());
				stmt.setInt(2, bean.getIndexId());
				stmt.setString(3, bean.getGoodsPid());
				stmt.setString(4, bean.getGoodsUrl());
				stmt.setString(5, bean.getGoodsName());
				stmt.setString(6, bean.getGoodsNameEn());
				stmt.setDouble(7, bean.getGoodsPrice());
				stmt.setInt(8, bean.getGoodsMorder());
				stmt.setInt(9, bean.getGoodsSold());
				stmt.setString(10, bean.getGoodsImg());
				stmt.setString(11, bean.getFactoryName());
				stmt.setString(12, bean.getFactoryId());
				stmt.setString(13, bean.getFactoryUrl());
				stmt.setString(14, bean.getTrade());
				stmt.setString(15, String.valueOf(bean.getCreateTime()));
				stmt.setInt(16, bean.getGoodsValid());
				stmt.setString(17, bean.getGoodsType());
				stmt.setString(18, bean.getGoodsTypeEn());
				stmt.setString(19, bean.getGoodsDetail());
				stmt.setString(20, bean.getGoodsDetailEn());
				stmt.setString(21, bean.getGoodsInfo());
				stmt.setString(22, bean.getGoodsWeight());
				stmt.setInt(23, bean.getUpdateFlag());
				res = stmt.executeUpdate();
			}
 			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
 		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			RemoteHelper.returnConnection(conn);
	}
 		
 		return  res ; 
	}
}
