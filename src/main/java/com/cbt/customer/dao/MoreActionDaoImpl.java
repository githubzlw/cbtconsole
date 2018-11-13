package com.cbt.customer.dao;

import ceRong.tools.bean.DorpDwonBean;
import com.cbt.bean.CustomOrderBean;
import com.cbt.bean.DropShipBean;
import com.cbt.bean.StockNearbyBean;
import com.cbt.jdbc.DBHelper;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoreActionDaoImpl implements IMoreActionDao{


	@Override
	public int addCustomOrder(CustomOrderBean cob) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "insert into customorder(user_id,user_name,email,quantity,comment,create_time,goods_url,goods_name,min_order_quantity,goods_price,currency) values(?,?,?,?,?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		try {
			Date parse = dateFormat.parse(dateFormat.format(now));
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, cob.getUserId());
			stmt.setString(2, cob.getUserName());
			stmt.setString(3, cob.getEmail());
			stmt.setString(4, cob.getQuantity());
			stmt.setString(5, cob.getComment());
			stmt.setTimestamp(6, new Timestamp(parse.getTime()));
			stmt.setString(7, cob.getPurl());
			stmt.setString(8, cob.getPname());
			stmt.setString(9, cob.getMinOrder());
			stmt.setDouble(10, new Double(cob.getFprice()));
			stmt.setString(11, cob.getCurrency());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public int addStockNearby(StockNearbyBean snb) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "insert into stocknearby(user_id,user_name,email,order_frequency,order_quantity,company_name,annual_turnover,create_time,";
		sql = sql + "goods_url,goods_name,min_order_quantity,goods_price,currency) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		try {
			Date parse = dateFormat.parse(dateFormat.format(now));
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, snb.getUserId());
			stmt.setString(2, snb.getUserName());
			stmt.setString(3, snb.getEmail());
			stmt.setString(4, snb.getOrderFrequency());
			stmt.setString(5, snb.getOrderQuantity());
			stmt.setString(6, snb.getCompanyName());
			stmt.setInt(7, snb.getAnnualTurnover());
			stmt.setTimestamp(8, new Timestamp(parse.getTime()));
			stmt.setString(9, snb.getPurl());
			stmt.setString(10, snb.getPname());
			stmt.setString(11, snb.getMinOrder());
			stmt.setDouble(12, new Double(snb.getFprice()));
			stmt.setString(13, snb.getCurrency());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public int addDropShip(DropShipBean dsb) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "insert into dropship(url,user_id,user_name,email,create_time,goods_name,min_order_quantity,goods_price,currency) values(?,?,?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		try {
			Date parse = dateFormat.parse(dateFormat.format(now));
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, dsb.getPurl());
			stmt.setInt(2, dsb.getUserId());
			stmt.setString(3, dsb.getUserName());
			stmt.setString(4, dsb.getEmail());
			stmt.setTimestamp(5, new Timestamp(parse.getTime()));
			stmt.setString(6, dsb.getPname());
			stmt.setString(7, dsb.getMinOrder());
			stmt.setDouble(8, new Double(dsb.getFprice()));
			stmt.setString(9, dsb.getCurrency());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	public List<StockNearbyBean> findAllStockNearby(int userId, String userName, int start, int end,String useremail) {
		List<StockNearbyBean> gbbList = new ArrayList<StockNearbyBean>();
		String sql = "select g.img,s.* from stocknearby s inner join goodsdata g on s.goods_url=g.url where 1=1 ";
		int i = 0;
		if(userId != 0) {
			sql +=" and s.user_id=?";
		}
		if(userName != null && !"".equals(userName)) {
			sql += " and s.user_name=?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and s.email=?";
		}
		sql += " order by s.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0) {
				i = i +1;
				stmt.setInt(1, userId);
			}
			if(userName != null && !"".equals(userName)) {
				i = i +1;
				stmt.setString(i, userName);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail);
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				StockNearbyBean gbb = new StockNearbyBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setUserName(rs.getString("user_name"));
				gbb.setOrderFrequency(rs.getString("order_frequency"));
				gbb.setOrderQuantity(rs.getString("order_quantity"));
				gbb.setCompanyName(rs.getString("company_name"));
				gbb.setAnnualTurnover(rs.getInt("annual_turnover"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setEmail(rs.getString("email"));
				gbb.setPurl(rs.getString("goods_url"));
				gbb.setPname(rs.getString("goods_name"));
				gbb.setMinOrder(rs.getString("min_order_quantity"));
				gbb.setFprice(rs.getString("goods_price"));
				gbb.setCurrency(rs.getString("currency"));
				gbb.setImg(rs.getString("img").replace("[", "").replace("]", ""));
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	@Override
	public List<DropShipBean> findAllDropShip(int userId, String userName, int start, int end,String useremail) {
		List<DropShipBean> gbbList = new ArrayList<DropShipBean>();
		String sql = "select g.img,d.* from dropship  d inner join goodsdata g on d.url=g.url where 1=1 ";
		int i = 0;
		if(userId != 0) {
			sql +=" and d.user_id=?";
		}
		if(userName != null && !"".equals(userName)) {
			sql += " and d.user_name=?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and d.email=?";
		}
		sql += " order by d.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			if(userId != 0) {
				i = i +1;
				stmt.setInt(1, userId);
			}
			if(userName != null && !"".equals(userName)) {
				i = i +1;
				stmt.setString(i, userName);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail);
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DropShipBean gbb = new DropShipBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setUserName(rs.getString("user_name"));
				gbb.setPurl(rs.getString("url"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setEmail(rs.getString("email"));
				gbb.setPname(rs.getString("goods_name"));
				gbb.setMinOrder(rs.getString("min_order_quantity"));
				gbb.setFprice(rs.getString("goods_price"));
				gbb.setCurrency(rs.getString("currency"));
				gbb.setImg(rs.getString("img").replace("[", "").replace("]", ""));
				gbbList.add(gbb);
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
		return gbbList;
	}

	@Override
	public List<CustomOrderBean> findAllCustomOrder(String startDate, String endDate, int start, int end, String useremail, String state) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
//		String sql = "select g.img,c.* from customorder c inner join goodsdata g on c.goods_url=g.url where 1=1 ";
		String sql = "select sql_calc_found_rows c.* from customorder c where 1=1 ";
		int i = 0;
		if(startDate != null && !"".equals(startDate)) {
			sql +=" and c.create_time>?";
		}
		if(endDate != null && !"".equals(endDate)) {
			sql += " and c.create_time<?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and c.email=?";
		}
		if(state!=null&&!state.equals("")){
			sql += " and c.state=?";
		}
		sql += " order by c.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			if(startDate != null && !"".equals(startDate)) {
				i = i +1;
				stmt.setString(1, startDate);
			}
			if(endDate != null && !"".equals(endDate)) {
				i = i +1;
				stmt.setString(i, endDate);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail.trim());
			}
			if(state!=null&&!state.equals("")){
				i = i +1;
				stmt.setInt(i, Integer.parseInt(state));
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setEmail(rs.getString("email"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setPurl(rs.getString("goods_url"));
				gbb.setQuantity(rs.getString("quantity"));
				gbb.setCustomChange(rs.getString("custom_changes"));
				gbb.setQuestions(rs.getString("questions"));
				gbb.setState(rs.getInt("state"));
				gbb.setTotal(total);
				
				
//				gbb.setUserName(rs.getString("user_name"));
//				gbb.setPname(rs.getString("goods_name"));
//				gbb.setMinOrder(rs.getString("min_order_quantity"));
//				gbb.setFprice(rs.getString("goods_price"));
//				gbb.setCurrency(rs.getString("currency"));
//				gbb.setImg(rs.getString("img").replace("[", "").replace("]", ""));
				
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	@Override
	public List<CustomOrderBean> findAllPriceMatch(String startDate, String endDate, int start, int end, String useremail, String state) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
		String sql = "select sql_calc_found_rows c.* from custompricematch c where 1=1 ";
		int i = 0;
		if(startDate != null && !"".equals(startDate)) {
			sql +=" and c.create_time>?";
		}
		if(endDate != null && !"".equals(endDate)) {
			sql += " and c.create_time<?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and c.email=?";
		}
		if(state!=null&&!state.equals("")){
			sql += " and c.state=?";
		}
		sql += " order by c.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			if(startDate != null && !"".equals(startDate)) {
				i = i +1;
				stmt.setString(1, startDate);
			}
			if(endDate != null && !"".equals(endDate)) {
				i = i +1;
				stmt.setString(i, endDate);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail.trim());
			}
			if(state!=null&&!state.equals("")){
				i = i +1;
				stmt.setInt(i, Integer.parseInt(state));
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setEmail(rs.getString("email"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setPurl(rs.getString("goods_url"));
				gbb.setOtherUrl(rs.getString("otherurl"));
				gbb.setFromPrice(rs.getString("fromprice"));
				gbb.setQuantity(rs.getString("quantity"));
				gbb.setState(rs.getInt("state"));
				gbb.setTotal(total);
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	@Override
	public List<CustomOrderBean> findAllNameYourPrice(String startDate, String endDate, int start, int end, String useremail, String state) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
		String sql = "select sql_calc_found_rows c.* from customprice c where 1=1 ";
		int i = 0;
		if(startDate != null && !"".equals(startDate)) {
			sql +=" and c.create_time>?";
		}
		if(endDate != null && !"".equals(endDate)) {
			sql += " and c.create_time<?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and c.email=?";
		}
		if(state!=null&&!state.equals("")){
			sql += " and c.state=?";
		}
		sql += " order by c.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			if(startDate != null && !"".equals(startDate)) {
				i = i +1;
				stmt.setString(1, startDate);
			}
			if(endDate != null && !"".equals(endDate)) {
				i = i +1;
				stmt.setString(i, endDate);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail.trim());
			}
			if(state!=null&&!state.equals("")){
				i = i +1;
				stmt.setInt(i, Integer.parseInt(state));
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setEmail(rs.getString("email"));
				gbb.setCreateTime(rs.getDate("create_time"));
				gbb.setPurl(rs.getString("goods_url"));
				
				gbb.setQuantity(rs.getString("quantity"));
				gbb.setTargetPrice(rs.getString("targetPrice"));
				gbb.setItem(rs.getString("item"));
				gbb.setComment(rs.getString("comments"));
				gbb.setState(rs.getInt("state"));
				gbb.setTotal(total);
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	@Override
	public List<CustomOrderBean> findAllCustomRfq(String startDate, String endDate, int start, int end, String useremail, String state) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
		String sql = "select sql_calc_found_rows c.* from customrfq c where 1=1 ";
		int i = 0;
		if(startDate != null && !"".equals(startDate)) {
			sql +=" and c.create_time>?";
		}
		if(endDate != null && !"".equals(endDate)) {
			sql += " and c.create_time<?";
		}
		if(useremail!=null&&!useremail.equals("")){
			sql += " and c.email=?";
		}
		if(state!=null&&!state.equals("")){
			sql += " and c.state=?";
		}
		sql += " order by c.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			if(startDate != null && !"".equals(startDate)) {
				i = i +1;
				stmt.setString(1, startDate);
			}
			if(endDate != null && !"".equals(endDate)) {
				i = i +1;
				stmt.setString(i, endDate);
			}
			if(useremail!=null&&!useremail.equals("")){
				i = i +1;
				stmt.setString(i, useremail.trim());
			}
			if(state!=null&&!state.equals("")){
				i = i +1;
				stmt.setInt(i, Integer.parseInt(state));
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
				gbb.setId(rs.getInt("id"));
				gbb.setUserId(rs.getInt("user_id"));
				gbb.setEmail(rs.getString("email"));
				gbb.setCreateTime(rs.getDate("create_time"));
				
				gbb.setPhoto(rs.getString("img_name"));
				gbb.setQuantity(rs.getString("order_quantity"));
				gbb.setProductName(rs.getString("product_name"));
				gbb.setComment(rs.getString("comment"));
				
				gbb.setState(rs.getInt("state"));
				gbb.setTotal(total);
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	
	
	@Override
	public List<CustomOrderBean> findSearchKey(String searchKey) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
		String sql = "select custom_1688_goods.name,custom_benchmark_ready.enname from custom_benchmark_ready ";
		sql +=" left join custom_1688_goods on custom_1688_goods.pid = custom_benchmark_ready.pid	where ";
		sql += " custom_1688_goods.name like '%"+searchKey+"%' and valid=1 ORDER BY RAND() limit 100 ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, searchKey);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
				gbb.setCatName(rs.getString("name"));
				gbb.setProductName(rs.getString("enname"));
				
				gbbList.add(gbb);
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
		return gbbList;
	}
	
	//大类索引取得
	@Override
	public List<DorpDwonBean> getLargeIndexInfo(){
		
		String sql = "select indexName,indexNameCn,upIndexName from importcsv.index_large ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		List<DorpDwonBean> logisticsList = new ArrayList<DorpDwonBean>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DorpDwonBean bean = new DorpDwonBean();
				bean.setIndexName(rs.getString("indexName"));
				bean.setIndexNameCn(rs.getString("indexNameCn"));
				bean.setUpIndexName(rs.getString("upIndexName"));
//				bean.setCreateTime(rs.getString("createTime"));
//				bean.setNameTime(rs.getString("indexName")+" "+rs.getString("createTime"));
				logisticsList.add(bean);
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
		return logisticsList;
	}
	
	@Override
	public List<CustomOrderBean> findAllCustomorSearch(String startDate, String endDate, int start, int end, String type) {
		List<CustomOrderBean> gbbList = new ArrayList<CustomOrderBean>();
//		String sql = "select g.img,c.* from customorder c inner join goodsdata g on c.goods_url=g.url where 1=1 ";
		String sql = "select sql_calc_found_rows a.img_name,b.indexNameCn,a.search_time,a.key_word from upload_search_info a  ";
		sql = sql +"left join importcsv.index_large b on a.type=b.indexName where 1=1 ";
		int i = 0;
		if(startDate != null && !"".equals(startDate)) {
			sql +=" and a.search_time>?";
		}
		if(endDate != null && !"".equals(endDate)) {
			sql += " and a.search_time<?";
		}
		if(type!=null&&!type.equals("")){
			sql += " and a.type=?";
		}
		sql += " order by a.id desc limit ?, ?";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			if(startDate != null && !"".equals(startDate)) {
				i = i +1;
				stmt.setString(1, startDate);
			}
			if(endDate != null && !"".equals(endDate)) {
				i = i +1;
				stmt.setString(i, endDate);
			}
			if(type!=null&&!type.equals("")){
				i = i +1;
				stmt.setString(i, type);
			}
			stmt.setInt(i+1, start);
			stmt.setInt(i+2, end);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				CustomOrderBean gbb = new CustomOrderBean();
//				gbb.setId(rs.getInt("id"));
//				gbb.setUserId(rs.getInt("user_id"));
//				gbb.setUserName(rs.getString("user_name"));
//				gbb.setQuantity(rs.getString("quantity"));
//				gbb.setComment(rs.getString("comment"));
//				gbb.setCreateTime(rs.getDate("create_time"));
//				gbb.setEmail(rs.getString("email"));
//				gbb.setPurl(rs.getString("goods_url"));
//				gbb.setPname(rs.getString("goods_name"));
//				gbb.setMinOrder(rs.getString("min_order_quantity"));
//				gbb.setFprice(rs.getString("goods_price"));
//				gbb.setCurrency(rs.getString("currency"));
//				gbb.setImg(rs.getString("img").replace("[", "").replace("]", ""));
				
				gbb.setImgName(rs.getString("img_name"));
				gbb.setIndexNameCn(rs.getString("indexNameCn"));
				gbb.setCreateTime(rs.getDate("search_time"));
				gbb.setKeyWord(rs.getString("key_word"));
				gbb.setTotal(total);
				
				gbbList.add(gbb);
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
			if (rs2 != null) {
				try {
					rs2.close();
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
		return gbbList;
	}
	
	
	
	@Override
	public int total(int userId, String userName, int start, int end,String useremail) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "select count(*) from stocknearby";
		sql +=" where 1=?";
		int i = 1;
		if(userId != 0) {
			sql +=" and user_id=?";
		}
		if(userName != null && !"".equals(userName)) {
			sql += " and user_name=?";
		}
		if(useremail != null && !"".equals(useremail)) {
			sql += " and email=?";
		}
		sql += " order by create_time desc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			if(userId != 0) {
				i = i +1;
				stmt.setInt(i, userId);
			}
			if(userName != null && !"".equals(userName)) {
				i = i +1;
				stmt.setString(i, userName);
			}
			if(useremail != null && !"".equals(useremail)) {
				i = i +1;
				stmt.setString(i, useremail);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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
	public int dropShiptotal(int userId, String userName, int start, int end,String useremail) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "select count(*) from dropship";
		sql +=" where 1=?";
		int i = 1;
		if(userId != 0) {
			sql +=" and user_id=?";
		}
		if(userName != null && !"".equals(userName)) {
			sql += " and user_name=?";
		}
		if(useremail != null && !"".equals(useremail)) {
			sql += " and email=?";
		}
		sql += " order by create_time desc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			if(userId != 0) {
				i = i +1;
				stmt.setInt(i, userId);
			}
			if(userName != null && !"".equals(userName)) {
				i = i +1;
				stmt.setString(i, userName);
			}
			if(useremail != null && !"".equals(useremail)) {
				i = i +1;
				stmt.setString(i, useremail);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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
	public int customOrdertotal(int userId, String userName, int start, int end,String useremail) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		sql = "select count(*) from customorder";
		sql +=" where 1=?";
		int i = 1;
		if(userId != 0) {
			sql +=" and user_id=?";
		}
		if(userName != null && !"".equals(userName)) {
			sql += " and user_name=?";
		}
		if(useremail != null && !"".equals(useremail)) {
			sql += " and email=?";
		}
		sql += " order by create_time desc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			if(userId != 0) {
				i = i +1;
				stmt.setInt(i, userId);
			}
			if(userName != null && !"".equals(userName)) {
				i = i +1;
				stmt.setString(i, userName);
			}
			if(useremail != null && !"".equals(useremail)) {
				i = i +1;
				stmt.setString(i, useremail);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
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
	public int updateCustomState(int userId,int type) {
		// TODO Auto-generated method stub
		String sql="";
		if(type==1){
			sql = "update customorder set state=1 where user_id=?";
		}else if(type==2){
			sql = "update custompricematch set state=1 where user_id=?";
		}else if(type==3){
			sql = "update customprice set state=1 where user_id=?";
		}else if(type==4){
			sql = "update customrfq set state=1 where user_id=?";
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
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
