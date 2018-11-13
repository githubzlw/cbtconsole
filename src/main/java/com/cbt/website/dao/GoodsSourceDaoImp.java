package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.bean.GoodsSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodsSourceDaoImp implements IGoodsSourceDao {


	@Override
	public int add(GoodsSource bean) {
		int result  = 0;
		String sql = "insert into goods_source (goodsdataid,goods_url,goods_purl,goods_img_url,"
				+ "goods_price,goods_name,sourceType,orderDesc,updatetime) "
				+ "values (?,?,?,?,?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bean.getGoodsId());
			stmt.setString(2, bean.getGoodsUrl());
			stmt.setString(3, bean.getGoodsPurl());
			stmt.setString(4, bean.getGoodsImg());
			stmt.setDouble(5, Double.valueOf(bean.getGoodsPrice()));
			stmt.setString(6, bean.getGoodsName());
			stmt.setString(7, bean.getSourceType());
			stmt.setInt(8, bean.getOrderDesc());
			
			result = stmt.executeUpdate();
		} catch (Exception e) {
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public int queryExsis(String sourceurl,String url) {
		int result  = 0;
		String sql = "select count(*) from goods_source where goods_purl = ? and goods_url =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sourceurl);
			stmt.setString(2, url);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count(*)");
			}
			
		} catch (Exception e) {
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
	
	@Override
	public String getGoodsDataImg(String url) {
		
		String strImg  ="";
		String sql = "select img from goodsdata where url = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				String gdimg = rs.getString("img");
				if(gdimg!=null && !gdimg.isEmpty()){
					gdimg = gdimg.replace("[", "").replace("]", "").trim();
					String[] gdimgs = gdimg.split(",\\s+");
					if(gdimgs[0].indexOf(".jpg")>1 || gdimgs[0].indexOf(".png")>1 || gdimgs[0].indexOf(".gif")>1){
						strImg=gdimgs[0];
					}else{
						strImg=gdimgs[0]+"jpg";
					}
				}
			}
			
		} catch (Exception e) {
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
		return strImg;
	}
	
	

	@Override
	public int deleteGoodsSource(String goodsUrl,String goodsPurl) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		sql = "delete from goods_source where goods_url=? and goods_purl=? ";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, goodsUrl);
			stmt.setString(2, goodsPurl);
			result = stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	
	@Override
	public int delGoodsDataImg(String url) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		sql = "delete from goodsdata where url=? ";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			result = stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	
	@Override
	public int updateChaImg(String sourceurl,String url,String pImg) {
		
		String sql = "update goods_source set goods_img_url=?  where goods_purl = ? and goods_url =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pImg);
			System.out.print("更新"+pImg);
			stmt.setString(2, sourceurl);
			stmt.setString(3, url);
			res = stmt.executeUpdate();
			
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	

}
