package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.bean.NoGoodsPushBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoGoodsPushDao implements INoGoodsPushDao {

	//查询近10-20天 无货源商品信息
	@Override
	public List<NoGoodsPushBean> selectNoGoods() {
		List<NoGoodsPushBean> list = new ArrayList<NoGoodsPushBean>();
		String sql  = "SELECT od.car_url, od.car_img, goodsname from order_details  od   INNER join  orderinfo  oi on  od.orderid=oi.order_no where "+
				"orderpaytime>=DATE_ADD(CURRENT_DATE,INTERVAL -20 DAY) and  orderpaytime<=DATE_ADD(CURRENT_DATE,INTERVAL -10 DAY) and oi.state in (1,2,3,4,5)"+
				"and od.id not in (SELECT od_id from order_product_source where order_product_source.addtime>=DATE_ADD(CURRENT_DATE,INTERVAL -20 DAY)  ) GROUP BY goodsname,car_url order by car_url";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				NoGoodsPushBean good = new NoGoodsPushBean();
				good.setCarUrl(rs.getString("car_url"));
				good.setImg(rs.getString("car_img"));
				good.setGoodsName(rs.getString("goodsname"));
				list.add(good);
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return list;
	}
	
	/**
	 * 推送商品
	 */
	@Override
	public int pushGoods(String carUrl){
		int i = 0;
		String sql = "update goodsdata_write set valid=0 where url='"+carUrl+"'";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			i = pst.executeUpdate();
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return i;
	}

}
