package com.cbt.pay.dao;

import com.cbt.bean.SpiderBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SpidersDao implements ISpidersDao{

	@Override
	public List<SpiderBean> getSelectedItem(int userid, String itemid, int state) {
		// TODO Auto-generated method stub
		
		String sql = "select id,itemId,shopId,googs_color,freight,googs_img,goods_title,googs_number,googs_price,remark,googs_seller,googs_size,goods_url,delivery_time,norm_least,goodsdata_id,guid,free_shopping_company,free_sc_days,state,freight_free,goods_class,feeprice,total_weight,bulk_volume,perWeight,extra_freight,source_url,currency,pWprice,goods_type from goods_car where userid=? and id in ("+itemid.replace("@", ",")+") ";
		/*if(state==0){
			sql =  sql+" and state!=1";
		}*/
		sql += " order by freight_free desc,free_shopping_company";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setUserId(userid);
				spider.setId(rs.getInt("id"));
				spider.setItemId(rs.getString("itemId"));
				spider.setShopId(rs.getString("shopId"));
				spider.setColor(rs.getString("googs_color"));
				spider.setFreight(rs.getString("freight"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				String googs_price = rs.getString("googs_price");
				spider.setPrice(googs_price);
				spider.setRemark(rs.getString("remark"));
				spider.setSeller(rs.getString("googs_seller"));
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
				spider.setDelivery_time(rs.getString("delivery_time"));
				spider.setNorm_least(rs.getString("norm_least"));
				spider.setGoodsdata_id(rs.getInt("goodsdata_id"));
				spider.setGuId(rs.getString("guid"));
				spider.setFree_shopping_company(rs.getString("free_shopping_company"));
				spider.setFree_sc_days(rs.getString("free_sc_days"));
				spider.setFreight_free(rs.getInt("freight_free"));
				spider.setFeeprice(rs.getString("feeprice"));
				spider.setSource_url(rs.getInt("source_url"));
				spider.setGoods_class(rs.getInt("goods_class"));
				spider.setTotal_weight(rs.getString("total_weight"));
				spider.setBulk_volume(rs.getString("bulk_volume"));
				spider.setPerWeight(rs.getString("perWeight"));
				spider.setExtra_freight(rs.getDouble("extra_freight"));
				spider.setCurrency(rs.getString("currency"));
				spider.setpWprice(rs.getString("pWprice"));
				spider.setTypes(rs.getString("goods_type"));
				spiderlist.add(spider);
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
		
		return spiderlist;

	}

	@Override
	public List<Object[]> getSelectedItemPrice(int userid, String itemid,
			int state) {
		String sql = "select id,googs_number,googs_price,state,freight_free,goods_class,extra_freight from goods_car where userid=? and id in ("+itemid.replace("@", ",")+")";
		
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Object[] obj = {rs.getInt("id"),rs.getInt("googs_number"),rs.getString("googs_price"),rs.getInt("state"),rs.getInt("freight_free"),rs.getInt("goods_class"),rs.getDouble("extra_freight")};
				spiderlist.add(obj);
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
		
		return spiderlist;
	}

	@Override
	public List<String[]> getOrderInfo(String orderNo) {
		
		String sql = "select  order_no,product_cost,address,address2,phoneNumber,zipcode,(select country from zone where oa.country = zone.id) Country,statename,street,recipients,create_time,pay_price_tow,currency,mode_transport from order_address oa ,orderinfo o where oa.orderno=o.order_no and  oa.orderNo=?";
		if(orderNo.indexOf(",") > -1){
			orderNo = "'" + orderNo.replaceAll(",", "','") + "'";
			sql = "select  order_no,product_cost,address,address2,phoneNumber,zipcode,(select country from zone where oa.country = zone.id) Country,statename,street,recipients,create_time,pay_price_tow,currency,mode_transport from order_address oa ,orderinfo o where oa.orderno=o.order_no and  oa.orderNo in ("+orderNo+")";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<String[]> info = new ArrayList<String[]>();
		try {
			stmt = conn.prepareStatement(sql);
			if(orderNo.indexOf(",") == -1){
				stmt.setString(1, orderNo);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String[] obj = new String[14];
				obj[0] = rs.getString("product_cost");
				obj[1] = rs.getString("address");
				obj[2] = rs.getString("address2");
				obj[3] = rs.getString("phoneNumber");
				obj[4] = rs.getString("zipcode");
				obj[5] = rs.getString("Country");
				obj[6] = rs.getString("street");
				obj[7] = rs.getString("recipients");
				obj[8] = rs.getString("create_time");
				obj[9] = Utility.getStringIsNull(rs.getString("pay_price_tow"))?rs.getString("pay_price_tow"):"0";
				obj[10] = rs.getString("currency");
				String mode_transport = rs.getString("mode_transport");
				obj[11] = mode_transport == null ? "":mode_transport;
				obj[12] = rs.getString("statename");
				obj[13] = rs.getString("order_no");
				info.add(obj);
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
		
		return info;
	}

	@Override
	public List<Object[]> getOrderDetails(String orderNo) {
		String sql = "select orderid,yourorder,goodsprice,goodsname,oa.delivery_time,car_url,car_img,car_type,(select img from goods_typeimg where oa.goodsid= goods_id) goods_typeimg from order_details oa  where  orderid=?";
		if(orderNo.indexOf(",") > -1){
			orderNo = "'" + orderNo.replaceAll(",", "','") + "'";
			sql = "select orderid,yourorder,goodsprice,goodsname,oa.delivery_time,car_img,yourorder,car_type,(select img from goods_typeimg where oa.goodsid = goods_id) goods_typeimg from order_details oa where  orderid in ("+orderNo+")";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Object[]> spiderlist = new ArrayList<Object[]>();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		try {
			stmt = conn.prepareStatement(sql);
			if(orderNo.indexOf(",") == -1){
				stmt.setString(1, orderNo);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String orderid = rs.getString("orderid");
				int number = rs.getInt("yourorder");
				String price = rs.getString("goodsprice");
				String delivery_time = rs.getString("delivery_time");
				String googs_img = rs.getString("car_img");
				String yourorder = rs.getString("yourorder");
				String goods_type = rs.getString("car_type");
				String goods_typeimg = rs.getString("goods_typeimg");
				if(Utility.getStringIsNull(price)){
					double sprice = Double.parseDouble(price);
					Object[] obj = {rs.getString("goodsname"),df.format(number*sprice),delivery_time,googs_img,yourorder,goods_type==null?"":goods_type,goods_typeimg==null?"":goods_typeimg,orderid};
					spiderlist.add(obj);
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
		
		return spiderlist;
	}
}
