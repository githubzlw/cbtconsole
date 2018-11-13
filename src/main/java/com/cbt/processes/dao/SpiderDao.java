package com.cbt.processes.dao;

import com.cbt.bean.CollectionBean;
import com.cbt.bean.SpiderBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiderDao implements ISpiderDao {

	@Override
	public int addGoogs_car(SpiderBean spider) {
		String sql = "insert goods_car(userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,datatime,itemId,shopId,norm_least,pWprice,sessionid,true_shipping,delivery_time,freight_free,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,per_weight,free_shopping_company,free_sc_days,goodsdata_id,preferential,deposit_rate,guid,goods_type,feeprice,currency,goods_class,extra_freight,source_url) values(?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(select max(id) from goodsdata where url = ?),?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, spider.getUserId());
			stmt.setString(2, spider.getUrl());
			stmt.setString(3, spider.getName());
			stmt.setString(4, spider.getSeller());
			stmt.setString(5, spider.getImg_url());
			stmt.setString(6, spider.getPrice());
			stmt.setInt(7, spider.getNumber());
			stmt.setString(8, spider.getSize());
			stmt.setString(9, spider.getColor());
			stmt.setString(10, spider.getFreight());
			stmt.setString(11, spider.getRemark());
			stmt.setString(12, spider.getItemId());
			stmt.setString(13, spider.getShopId());
			stmt.setString(14, spider.getNorm_least());
			stmt.setString(15, spider.getpWprice());
			stmt.setString(16, spider.getSessionId());
			stmt.setString(17, spider.getTrue_shipping());
			stmt.setString(18, spider.getDelivery_time());
			stmt.setInt(19, spider.getFreight_free());
			stmt.setString(20, spider.getWidth());
			stmt.setString(21, spider.getPerWeight());
			stmt.setString(22, spider.getSeilUnit());
			stmt.setString(23, spider.getGoodsUnit());
			stmt.setString(24, spider.getBulk_volume());
			stmt.setString(25, spider.getTotal_weight());
			stmt.setString(26, spider.getWeight());
			stmt.setString(27, spider.getFree_shopping_company());
			stmt.setString(28, spider.getFree_sc_days());
			stmt.setString(29, spider.getUrl());
			stmt.setInt(30, spider.getPreferential());
			stmt.setInt(31, spider.getDeposit_rate());
			stmt.setString(32, spider.getGuId());
			stmt.setString(33, spider.getTypes());
			stmt.setString(34, spider.getFeeprice());
			stmt.setString(35, spider.getCurrency());
			stmt.setInt(36, spider.getGoods_class());
			stmt.setDouble(37, spider.getExtra_freight());
			stmt.setInt(38, spider.getSource_url());
			int result = stmt.executeUpdate();
			 if (result == 1) {
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						googds_id = rs.getInt(1);
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
	 
		return googds_id;
	}

	@Override
	public int addGoogs_car(List<SpiderBean> spiders, String uCurrency, Map<String, Double> maphl) {
		String sql = "insert goods_car(userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,datatime,itemId,shopId,norm_least,pWprice,sessionid,true_shipping,delivery_time,freight_free,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,per_weight,free_shopping_company,free_sc_days,goodsdata_id,preferential,deposit_rate,guid,goods_type,currency) values(?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(select max(id) from goodsdata where url = ?),?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		ResultSet rs = null;
		try {
			double ma = maphl.get(uCurrency);
			DecimalFormat df=new DecimalFormat("#0.##");
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < spiders.size(); i++) {
				SpiderBean spider = spiders.get(i);
			stmt.setInt(1, spider.getUserId());
			stmt.setString(2, spider.getUrl());
			stmt.setString(3, spider.getName());
			stmt.setString(4, spider.getSeller());
			stmt.setString(5, spider.getImg_url());
			double mm = ma/maphl.get(spider.getCurrency());
			String sprice = df.format(Double.parseDouble(spider.getPrice())*mm);
			stmt.setString(6, sprice);
			stmt.setInt(7, spider.getNumber());
			stmt.setString(8, spider.getSize());
			stmt.setString(9, spider.getColor());
			stmt.setString(10, spider.getFreight());
			stmt.setString(11, spider.getRemark());
			stmt.setString(12, spider.getItemId());
			stmt.setString(13, spider.getShopId());
			stmt.setString(14, spider.getNorm_least());
			stmt.setString(15, spider.getpWprice());
			stmt.setString(16, spider.getSessionId());
			stmt.setString(17, spider.getTrue_shipping());
			stmt.setString(18, spider.getDelivery_time());
			stmt.setInt(19, spider.getFreight_free());
			stmt.setString(20, spider.getWidth());
			stmt.setString(21, spider.getPerWeight());
			stmt.setString(22, spider.getSeilUnit());
			stmt.setString(23, spider.getGoodsUnit());
			stmt.setString(24, spider.getBulk_volume());
			stmt.setString(25, spider.getTotal_weight());
			stmt.setString(26, spider.getWeight());
			stmt.setString(27, spider.getFree_shopping_company());
			stmt.setString(28, spider.getFree_sc_days());
			stmt.setString(29, spider.getUrl());
			stmt.setInt(30, spider.getPreferential());
			stmt.setDouble(31, spider.getDeposit_rate());
			stmt.setString(32, spider.getGuId());
			stmt.setString(33, spider.getTypes());
			stmt.setString(34, uCurrency);
			 stmt.addBatch();
			}
			int[] results = stmt.executeBatch();
			result = results.length;
			
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
	public int upGoogs_car(String guid,int spiderId,int number,int userid,String sessionId, String price, String bulk_volume,String total_weight) {
		String sql = "update goods_car set googs_number = ?  ,  bulk_volume = ? , total_weight = ?, googs_price = ? where  state != 1 and sessionId = ? ";
		if(userid != 0){
			sql = "update goods_car set googs_number = ?  ,  bulk_volume = ? , total_weight = ?, googs_price = ? where state != 1 and userid = ? ";
		}
		if(price.equals("")){
			if(userid != 0){
				sql = "update goods_car set googs_number = ? ,  bulk_volume = ? , total_weight = ? where state != 1 and userid = ? ";
			}else{
				sql = "update goods_car set googs_number = ? ,  bulk_volume = ? , total_weight = ? where state != 1 and sessionId = ? ";
			}
			
		}
		if(spiderId == 0){
			sql += " and guid=?";
		}else{
			sql += " and id=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, number);
			stmt.setString(2, bulk_volume);
			stmt.setString(3, total_weight);
			int i = 4;
			if(!price.equals("")){
				stmt.setString(4, price);
				i++;
			}
			if(userid == 0){
				stmt.setString(i, sessionId);
				i++;
			}else{
				stmt.setInt(i, userid);
				i++;
			}
			if(spiderId == 0){
				stmt.setString(i, guid);
				i++;
			}else{
				stmt.setInt(i, spiderId);
				i++;
			}
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
	
	@Override
	public int delGoogs_car(String spiderId, int userid, String sessionId) {
		String sql = "delete from goods_car where id = ? and state=0";
		 
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, spiderId);
			 
			 res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public List<SpiderBean> getGoogs_cars(String sessionId, int userId, int preshopping) {
		String sql;
		if(preshopping == 0){
			sql = "select id,itemId,catid,shopId,userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,datatime,norm_least,delivery_time,freight_free,bulk_volume,total_weight,width,perWeight,seilUnit,goodsUnit,per_weight,goods_email,free_shopping_company,free_sc_days,preferential,deposit_rate,guid,goods_type,feeprice,goods_class,pWprice,(select img from goods_typeimg where gc.id = goods_id) goods_typeimg,extra_freight,source_url,currency from goods_car gc where ";
		}else{
			sql = "select gc.id,itemId,catid,shopId,gc.userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,gc.remark,datatime,norm_least,delivery_time,freight_free,bulk_volume,total_weight,width,perWeight,seilUnit,goodsUnit,per_weight,goods_email,free_shopping_company,free_sc_days,preferential,deposit_rate,guid,goods_type,feeprice,goods_class,pWprice,(select img from goods_typeimg where gc.id = goods_id) goods_typeimg,extra_freight,source_url,currency,ifnull(pre.flag,1) preflag from goods_car gc left join tbl_preshoppingcar_info pre on  pre.goods_car_id=gc.id where ";
		}
		if(userId != 0){
			sql += " gc.userid=? ";
		}else{
			sql += " sessionid=? ";
		}
		sql += " and (state=0 or state=3) order by freight_free desc,free_shopping_company desc,datatime desc";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0){
				stmt.setInt(1, userId);
			}else{
				stmt.setString(1, sessionId);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setUserId(userId);
				spider.setId(rs.getInt("id"));
				spider.setGoods_catid(rs.getString("catid"));
				spider.setItemId(rs.getString("itemId"));
				spider.setShopId(rs.getString("shopId"));
				spider.setColor(rs.getString("googs_color"));
				spider.setFreight(rs.getString("freight"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setRemark(rs.getString("remark"));
				spider.setSeller(rs.getString("googs_seller")); 
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
				spider.setNorm_least(rs.getString("norm_least"));
				spider.setDelivery_time(rs.getString("delivery_time"));
				spider.setFreight_free(rs.getInt("freight_free"));
				spider.setBulk_volume(rs.getString("bulk_volume"));
				spider.setTotal_weight(rs.getString("total_weight"));
				spider.setWidth(rs.getString("width"));
				spider.setPerWeight(rs.getString("perWeight"));
				spider.setSeilUnit(rs.getString("seilUnit"));
				spider.setGoodsUnit(rs.getString("goodsUnit"));
				spider.setWeight(rs.getString("per_weight"));
				spider.setGoods_email(rs.getString("goods_email"));
				spider.setFree_shopping_company(rs.getString("free_shopping_company"));
				spider.setFree_sc_days(rs.getString("free_sc_days"));
				spider.setPreferential(rs.getInt("preferential"));
				spider.setDeposit_rate(rs.getInt("deposit_rate"));
				spider.setGuId(rs.getString("guid"));
				spider.setTypes(rs.getString("goods_type"));
				String feeprice = rs.getString("feeprice");
				spider.setFeeprice(feeprice);
				String googs_price = rs.getString("googs_price");
				spider.setPrice(googs_price);
				spider.setExtra_freight(rs.getDouble("extra_freight"));
				spider.setSource_url(rs.getInt("source_url"));
				spider.setImg_type(rs.getString("goods_typeimg"));
				spider.setGoods_class(rs.getInt("goods_class"));
				spider.setSource_url(rs.getInt("source_url"));
				spider.setCurrency(rs.getString("currency"));
				spider.setpWprice(rs.getString("pWprice"));
				int preflag = 1;
				if(preshopping == 1){
					preflag = rs.getInt("preflag");
				}
				spider.setPreshopping(preflag);
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
	public List<SpiderBean> getGoogs_carsFreeprice(String guid) {
		String[] guids = guid.split("@");
		String sql = "select id,feeprice,googs_price from goods_car where state=0 and freight_free=1 and guid=? ";
		for (int i = 1; i < guids.length; i++) {
			sql += " or guid=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < guids.length; i++) {
				stmt.setString(i+1, guids[i]);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setId(rs.getInt("id"));
				spider.setFeeprice(rs.getString("feeprice"));
				spider.setPrice(rs.getString("price"));
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
	public SpiderBean getGoogs_cars(String itemId, int userId, String url, String size, String color, String sessionId, String types) {
		String sql = "select id,userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,datatime,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,pWprice,preferential,guid,currency from goods_car where state=0 and googs_size=? and googs_color=? and goods_type=?   and  goods_url = ? ";
		if(userId != 0){
			sql = sql + " and  userid = ?";
		}else{
			sql = sql + " and  sessionid = ?";
		}
		sql += " having min(id) order by id desc "; 
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		SpiderBean spider = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, size);
			stmt.setString(2, color);
			stmt.setString(3, url);
			stmt.setString(4, types);
			if(userId != 0){
				stmt.setInt(5, userId);
			}else{
				stmt.setString(5, sessionId);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				spider = new SpiderBean();
				spider.setUserId(userId);
				spider.setId(rs.getInt("id"));
				spider.setColor(rs.getString("googs_color"));
				spider.setFreight(rs.getString("freight"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setSeller(rs.getString("googs_seller"));
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
				spider.setWidth(rs.getString("width"));
				spider.setPerWeight(rs.getString("perWeight"));
				spider.setSeilUnit(rs.getString("seilUnit"));
				spider.setGoodsUnit(rs.getString("goodsUnit"));
				spider.setBulk_volume(rs.getString("bulk_volume"));
				spider.setTotal_weight(rs.getString("total_weight"));
				spider.setpWprice(rs.getString("pWprice"));
				spider.setPreferential(rs.getInt("preferential"));
				spider.setGuId(rs.getString("guid"));
				spider.setCurrency(rs.getString("currency"));
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
		
		return spider;
	}

	@Override
	public SpiderBean getGoogs_carsId(String guid, int id, String sessionId, int userId) {
		String sql = "select id,userid,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,googs_size,googs_color,freight,remark,datatime,pWprice,width,perWeight,seilUnit,goodsUnit,bulk_volume,total_weight,preferential,guid,currency from goods_car where 1=1 ";
		 if(id == 0){
			 sql += " and guid=?";
		 }else{
			 sql += " and id=?";
		 }
		 if(userId == 0){
			 sql += " and sessionId=?";
		 }else {
			 sql += " and userid=?";
		 }
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		SpiderBean spider = null;
		try {
			stmt = conn.prepareStatement(sql);
			 if(id == 0){
				stmt.setString(1, guid);
			 }else{
				stmt.setInt(1, id);
			 }
			 if(userId == 0){
				stmt.setString(2, sessionId);
			 }else {
				stmt.setInt(2, userId);
			 }
			rs = stmt.executeQuery();
			if (rs.next()) {
				spider = new SpiderBean();
				spider.setUserId(userId);
				spider.setId(rs.getInt("id"));
				spider.setColor(rs.getString("googs_color"));
				spider.setFreight(rs.getString("freight"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setSeller(rs.getString("googs_seller"));
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
				spider.setpWprice(rs.getString("pWprice"));
				spider.setWidth(rs.getString("width"));
				spider.setPerWeight(rs.getString("perWeight"));
				spider.setSeilUnit(rs.getString("seilUnit"));
				spider.setGoodsUnit(rs.getString("goodsUnit"));
				spider.setBulk_volume(rs.getString("bulk_volume"));
				spider.setTotal_weight(rs.getString("total_weight"));
				spider.setPreferential(rs.getInt("preferential"));
				spider.setGuId(rs.getString("guid"));
				spider.setCurrency(rs.getString("currency"));
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
		
		return spider;
	}
	
	@Override
	public int addURL(String userName,String url,int fruit) {
		String sql = "insert url(username,url,state,date) values(?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userName);
			stmt.setString(2, url);
			stmt.setInt(3, fruit);
			stmt.executeUpdate();
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
		return googds_id;
	}

	@Override
	public int upGoogs_car(int goodsid,String title, String freight, String remark) {
		String sql = "update goods_car set goods_title=? ,freight=?,remark=? where id = ? and state=0";
		Connection conn = DBHelper.getInstance().getConnection();
		
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, title); 
			stmt.setString(2, freight);
			stmt.setString(3, remark); 
			stmt.setInt(4, goodsid); 
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int upGoogs_car(List<SpiderBean> spider) {
		if(spider.size()<1)return 0;
		String sql = "update goods_car set googs_price=?,freight_free=0 where guid = ? and state=0";
		Connection conn = DBHelper.getInstance().getConnection();
		
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < spider.size(); i++) {
				stmt.setString(1, spider.get(i).getPrice()); 
				stmt.setString(2,  spider.get(i).getGuId());
				stmt.addBatch();
			}
			int[] ress = stmt.executeBatch();
			res = ress.length;
		}catch (Exception e) {
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
	 
	@Override
	public int delGoogs_car_s(String shopID, int userid, String sessionId) {
		String sql = "delete from goods_car where shopID = ? ";
		if(userid != 0){
			sql += " and userid = ?";
		}else{
			sql += " and sessionId = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, shopID);
			if(userid != 0){
				stmt.setInt(2, userid);
			}else{
				stmt.setString(2, sessionId);
			}
			 res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public Map<String, Double> getExchangeRate() {
		String sql = "select id,country,exchange_rate from exchange_rate";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("country"), rs.getDouble("exchange_rate"));
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
		
		return map;
	}

	
	
	/*������Ʒ���ʼ�״̬*/
	public  int updateFlag(String itemid){
		String sql = "update goods_car set googs_number = ?,googs_seller = ?,goods_title=?,googs_price=?,freight=?,remark=?,url=? where id = ? and state = 0 and userid = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int getGoogs_carNum(int userId, String sessionId) {
		String sql = "select count(id) from goods_car where state=0 ";
		if(userId != 0){
			sql +=" and userid=?";
		}else{
			sql += " and sessionid = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);

			if(userId != 0){
				stmt.setInt(1, userId);
			}else{
				stmt.setString(1, sessionId);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
		return res;
	}

	@Override
	public int upGoogs_car(int cartId, int userid,String price) {
		String sql = "update goods_car set userid = ?,googs_price = ? where id = ? and state = 0";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, price);
			stmt.setInt(3, cartId);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int delGoogs_car(String[] goodsid, String guid) {
		String sql = "update goods_car set state = 9 where state != 1 ";
		if(Utility.getStringIsNull(guid)){
			sql += "  and guid = ? ";
		}else{
			sql += "  and id = ? ";
			for (int i = 1; i < goodsid.length; i++) {
				sql += "  or id = ? ";
			}
		}
		String sql2 = "update tbl_preshoppingcar_info set flag = 1 where goods_car_id=? ";
		for (int i = 1; i < goodsid.length; i++) {
			sql2 += "  or goods_car_id = ? ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			if(Utility.getStringIsNull(guid)){
				stmt.setString(1, guid);
			}else{
				for (int i = 0; i < goodsid.length; i++) {
					stmt.setInt(i+1, Integer.parseInt(goodsid[i]));
					stmt2.setInt(i+1, Integer.parseInt(goodsid[i]));
				}
			}
			 res = stmt.executeUpdate();
			 if(res > 0){
				 stmt2.executeUpdate();
			 }
		}catch (Exception e) {
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
			DBHelper.getInstance().closeConnection(conn);
	}
		return res;
	}

	@Override
	public int upGoogs_car_state(List<String> spiderId, int state) {
		String sql = "update goods_car set state = ? where ";
		for (int i = 0; i < spiderId.size(); i++) {
			if(i == spiderId.size()-1){
				sql += " id = ? ";
			}else{
				sql += "  id = ? or ";
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, state);
			for (int i = 0; i < spiderId.size(); i++) {
				stmt.setInt(i+2, Integer.parseInt(spiderId.get(i)));
			}
			
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int upGoodsprice(int goodsid, String price,String sessionId,int userid) {
		String sql = "update goods_car set googs_price = ? where id = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		if(userid != 0){
			sql += " and userid=?"; 
		}else{
			sql += " and sessionid=?"; 
		}
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, price);
			stmt.setInt(2, goodsid);
			if(userid != 0){
				stmt.setInt(3, userid);
			}else{
				stmt.setString(3, sessionId);
			}
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public List<SpiderBean> getInquiryGoods(String[] goodsid) {
		String sql = "select id,goods_url,goods_title,googs_img,googs_price,googs_number,googs_size,googs_color,remark from goods_car where state=0 and id=? ";
		for (int i = 0; i < goodsid.length-1; i++) {
			sql += " or id=? ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(goodsid[0]));
			for (int i = 1; i < goodsid.length; i++) {
				stmt.setInt(i+1, Integer.parseInt(goodsid[i]));
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setId(rs.getInt("id"));
				spider.setColor(rs.getString("googs_color"));
				spider.setImg_url(rs.getString("googs_img"));
				spider.setName(rs.getString("goods_title"));
				spider.setNumber(rs.getInt("googs_number"));
				spider.setPrice(rs.getString("googs_price"));
				spider.setRemark(rs.getString("remark"));
				spider.setSize(rs.getString("googs_size"));
				spider.setUrl(rs.getString("goods_url"));
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
	public List<CollectionBean> getCollection(int userid) {
		String sql = "select id,url,price,img,createtime,titile,moq from collection where userid=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<CollectionBean> spiderlist = new ArrayList<CollectionBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CollectionBean collection = new CollectionBean();
				collection.setId(rs.getInt("id"));
				collection.setUrl(rs.getString("url"));
				collection.setPrice(rs.getString("price"));
				collection.setImg(rs.getString("img"));
				collection.setCreatetime(rs.getString("createtime"));
				collection.setTitile(rs.getString("titile"));
				collection.setMoq(rs.getString("moq"));
				spiderlist.add(collection);
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
	public int deleteCollection(String ids, int userid) {
		String sql = "delete from collection where id in ("+ids+") and userid = ?";
		 
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, ids);
			stmt.setInt(1, userid);
			 res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int addCollection(CollectionBean collection) {
		String sql = "insert collection(userid,titile,url,price,img,createtime,moq) values(?,?,?,?,?,now(),?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, collection.getUserid());
			stmt.setString(2, collection.getTitile());
			stmt.setString(3, collection.getUrl());
			stmt.setString(4, collection.getPrice());
			stmt.setString(5, collection.getImg());
			stmt.setString(6, collection.getMoq());
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

	@Override
	public int getCollection(String url, int userid) {
		String sql = "select id from collection where url=? and userid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setInt(2, userid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getInt("id");
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
		
		return res;
	}

	@Override
	public int saveGoodsEmail(int userid, String session, String email) {
		String sql = "";
		if(session == null){
			sql = "update goods_car set goods_email=? where userid=?";
		}else{
			sql = "update goods_car set goods_email=? where sessionid=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			if(session == null){
				stmt.setInt(2, userid);
			}else{
				stmt.setString(2, session);
			}
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
	public static void main(String[] args) {
		String string = "sdf@df@";
		String[] ss = string.split("@");
		for (int i = 0; i < ss.length; i++) {
			System.err.println(ss[i]);
		}
	}
	@Override
	public int upExpreeType(String goodsId, String expreeType, String days,int countryId) {
			String sql = "update goods_car set shopping_company = ? , sc_days = ? ,  countryid = ? where id in ("+goodsId.replaceAll("@", ",")+")";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			int res = 0;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, expreeType);
				stmt.setString(2, days);
				stmt.setInt(3, countryId);
				String[] goods = goodsId.split(",");
				for (int i = 0; i < goods.length; i++) {
					stmt.setInt(4+i, Integer.parseInt(goods[i]));
				}
				res = stmt.executeUpdate();
			}catch (Exception e) {
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

	@Override
	public int delCollectionByUrl(String url, int userid) {
		// TODO Auto-generated method stub
		String sql = "delete from collection where url = ? and userid = ?";	 
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			stmt.setInt(2, userid);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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

	@Override
	public int addGoogs_carTypeimg(int gid, String img) {
		String sql = "insert goods_typeimg(goods_id,img) values(?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(2, img);
			stmt.setInt(1, gid);
			stmt.executeUpdate();
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
		return googds_id;
	}

}
