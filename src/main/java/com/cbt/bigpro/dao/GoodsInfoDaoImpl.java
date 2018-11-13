package com.cbt.bigpro.dao;

import com.cbt.bigpro.bean.BigGoodsArea;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodsInfoDaoImpl implements GoodInfoDao {

	@Override
	public BigGoodsArea getGoodsInfo(String id) {
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		ResultSet rs3 = null;
		ResultSet rs3_1 = null;
		ResultSet rs4 = null;
 		PreparedStatement stmt = null;
 		PreparedStatement stmt3 = null;
 		PreparedStatement stmt3_1 = null;
 		PreparedStatement stmt4 = null;
 		BigGoodsArea temp=new BigGoodsArea();
 		int  count = 0;
 		//首先根据商品id 查询1688表
 		String  sql_1 ="select count(*) as num from importcsv.custom_goods  where pid = ? or url =?";
 		try{
 			stmt3 = conn.prepareStatement(sql_1);
 			stmt3.setString(1, id);
 			stmt3.setString(2, id);
			rs3 = stmt3.executeQuery();
			String catid1 =null;
			String  catid = null;
			//如果存在,
			while(rs3.next()){
				count = rs3.getInt("num");
			}
			if(count>=1){
				String sql_3 ="select *  from importcsv.custom_goods  where pid = ? or url =?";
				stmt3_1 = conn.prepareStatement(sql_3);
				stmt3_1.setString(1, id);
				stmt3_1.setString(2, id);
				rs3_1 = stmt3_1.executeQuery();
				while(rs3_1.next()){
					temp.setGoodsId(rs3_1.getString("pid"));
					temp.setKeyWord(rs3_1.getString("keyword"));
					temp.setTitle(rs3_1.getString("enname"));
					temp.setImg(rs3_1.getString("img"));
					temp.setLocalpath(rs3_1.getString("localpath"));
					temp.setPrice(Double.parseDouble(rs3_1.getString("price")));
					temp.setGoodsurl(rs3_1.getString("url"));
					temp.setCatid1(rs3_1.getString("catid1"));
					catid1 =rs3_1.getString("catid1");
				}
				String sql_2 = "select weightcut from csv_weight_cut2 where postcategryId =? ";
				stmt4 = conn.prepareStatement(sql_2);
				stmt4.setInt(1, Integer.parseInt(catid1));
				rs4  = stmt4.executeQuery();
				while(rs4.next()){
					temp.setWeight(rs4.getString("weightcut"));
				}
			}else{
				//如果不存在1688数据 ,则查询goodsdata表 
		 		String sql ="select gd.pID,gd.sPrice,gd.img,gd.url,gd.name,gd.perWeight from  "+
		 		"goodsdata as gd  where gd.pID =? or gd.url= ?";
		 			stmt = conn.prepareStatement(sql);
		 			stmt.setString(1, id);
		 			stmt.setString(2, id);
					rs = stmt.executeQuery();
					while(rs.next()){
						temp.setGoodsId(rs.getString("pID"));
						temp.setPrice(rs.getDouble("sPrice"));
						temp.setImg(rs.getString("img"));
						temp.setGoodsurl(rs.getString("url"));
						temp.setTitle(rs.getString("name"));
						temp.setWeight(rs.getString("perWeight"));
					}
			}} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
			finally {
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
		return temp;
	}

}
