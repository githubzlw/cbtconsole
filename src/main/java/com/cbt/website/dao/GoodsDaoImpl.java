package com.cbt.website.dao;

import com.cbt.bean.Goods;
import com.cbt.bean.SpiderBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Util;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoodsDaoImpl implements IGoodsDao {

	@Override
	public List<Goods> getGoodsList() {
		String sql = "select id,goodName,keywords,moneyType,price,address from goodslist ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<Goods> goodsList = new ArrayList<Goods>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Goods goods=new Goods();
				goods.setId(rs.getInt("id"));
				goods.setGoodName(rs.getString("goodName"));
				goods.setKeywords(rs.getString("keywords"));
				goods.setPrice(rs.getDouble("price"));
				goods.setAddress(rs.getString("address"));
				goodsList.add(goods);
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
		return goodsList;
	}

	@Override
	public List<Goods> getConditionList(String condition) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<Goods> goodsList = new ArrayList<Goods>();
		try {
			String sql1 = "select id,goodName,keywords,moneyType,price,address from goodslist where keywords like '%"+condition+"%'";
			stmt = conn.prepareStatement(sql1);
			rs = stmt.executeQuery();
			StringBuffer ids=new StringBuffer();
			ids.append("0");
			while (rs.next()) {
				Goods goods=new Goods();
				int id = rs.getInt("id");
				ids.append(",").append(id);
				goods.setId(id);
				goods.setGoodName(rs.getString("goodName"));
				goods.setKeywords(rs.getString("keywords"));
				goods.setPrice(rs.getDouble("price"));
				goods.setAddress(rs.getString("address"));
				goodsList.add(goods);
			}
			String sql2 = "select id,goodName,keywords,moneyType,price,address from goodslist where goodName like '%"+condition+"%' and id not in("+ids.toString()+")";
			stmt = conn.prepareStatement(sql2);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Goods goods=new Goods();
				goods.setId(rs.getInt("id"));
				goods.setGoodName(rs.getString("goodName"));
				goods.setKeywords(rs.getString("keywords"));
				goods.setPrice(rs.getDouble("price"));
				goods.setAddress(rs.getString("address"));
				goodsList.add(goods);
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
		return goodsList;
	}

	@Override
	public List<Goods> getOrderList(int order, String condition) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<Goods> goodsList = new ArrayList<Goods>();
		try {
			String sql1 = "";
			if(order==0){
				sql1="select id,goodName,keywords,moneyType,price,address from goodslist where keywords like '%"+condition+"%' or goodName like '%"+condition+"%' order by price asc";
			}else{
				sql1="select id,goodName,keywords,moneyType,price,address from goodslist where keywords like '%"+condition+"%' or goodName like '%"+condition+"%' order by price desc";
			}
			stmt = conn.prepareStatement(sql1);
			rs = stmt.executeQuery();
			StringBuffer ids=new StringBuffer();
			ids.append("0");
			while (rs.next()) {
				Goods goods=new Goods();
				int id = rs.getInt("id");
				ids.append(",").append(id);
				goods.setId(id);
				goods.setGoodName(rs.getString("goodName"));
				goods.setKeywords(rs.getString("keywords"));
				goods.setPrice(rs.getDouble("price"));
				goods.setAddress(rs.getString("address"));
				goodsList.add(goods);
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
		return goodsList;
}

	@Override
	public List<Goods> getSearchScopeList(int order, double minprice, double maxprice, String condition) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<Goods> goodsList = new ArrayList<Goods>();
		try {
			String sql1 = "";
			if(order==0){
				if(minprice>0&&maxprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%') and price>="+minprice+" and price<="+maxprice+" order by price asc";
				}else if(minprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%') and price>="+minprice+"  order by price asc";
				}else if(maxprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%') and price<="+minprice+"  order by price asc";
				}
			}else{
				if(minprice>0&&maxprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%' ) and price>="+minprice+" and price<="+maxprice+" order by price desc";
				}else if(minprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%' ) and price>="+minprice+" order by price desc";
				}else if(maxprice>0){
					sql1="select id,goodName,keywords,moneyType,price,address from goodslist where (keywords like '%"+condition+"%' or goodName like '%"+condition+"%' ) and price<="+maxprice+" order by price desc";
				}
			}
			stmt = conn.prepareStatement(sql1);
			rs = stmt.executeQuery();
			StringBuffer ids=new StringBuffer();
			ids.append("0");
			while (rs.next()) {
				Goods goods=new Goods();
				int id = rs.getInt("id");
				ids.append(",").append(id);
				goods.setId(id);
				goods.setGoodName(rs.getString("goodName"));
				goods.setKeywords(rs.getString("keywords"));
				goods.setPrice(rs.getDouble("price"));
				goods.setAddress(rs.getString("address"));
				goodsList.add(goods);
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
		return goodsList;
	}

	@Override
	public List<SpiderBean> getSpiderBeans(int userid, String email) {
		int uid = 0;
		double discount = 1;
		String sqlid = "select u.id,g.discount from user u,grade_discount g where 1=1 and u.grade=g.gid ";
		if(userid>0){
			sqlid +=" and u.id="+userid;
		}
		if(email!=null && !"".equals(email)){
			sqlid  += "and u.email="+email;
		}

		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null,stmt2 = null;
		ResultSet rs = null,rs2= null;
		List<SpiderBean> spiderlist = new ArrayList<SpiderBean>();
		try{
			stmt = conn.prepareStatement(sqlid);
			rs = stmt.executeQuery();
			while(rs.next()){
				uid = Integer.parseInt(rs.getString("id"));
				discount = 1-rs.getDouble("discount");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
		}
		
		String sql = "select g.id,g.notfreeprice,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,pWprice,googs_size,googs_color, "
				   + " freight,remark,datatime,norm_least,delivery_time,freight_free,g.currency,goods_type,g.goods_class,"
				   + " IFNULL(tab_class.showname,'-') as showname,IFNULL(ts.price,0.0) as rate_price,IFNULL(tab_class.deposit_rate1,0) "
				   + " as deposit_rates,ROUND(googs_price * googs_number,2 )as ceil_money ,tab_class.class_money "
				   + ", ifnull(width,0) as width ,ifnull(perWeight,'') as perWeight, seilUnit, goodsUnit from goods_car g  left join ("
				   + " select class_money,classtype,showname, min(case  when price>class_money then 1   when  class_money>price then deposit_rate end)  deposit_rate1    from "
				   + " (select cast(sum(t.googs_price * t.googs_number) as DECIMAL(10,2)) class_money,t.goods_class "
				   + " from goods_car t  where 1=1  and  t.userid =?   and (state=0 or state=3)"
				   + " group by t.goods_class"
				   + " )  altble  left join class_discount  "
				   + " on  class_discount.classtype=altble.goods_class " 
				   + " group by class_money,showname,classtype "
				   + ") tab_class "
				   + " on  g.goods_class=tab_class.classtype"
				   + " left join class_discount ts  on  tab_class.classtype=ts.classtype and tab_class.deposit_rate1=ts.deposit_rate "
				   + " where userid =?   and (state=0 or state=3)";
		
		
		
//		StringBuilder sql = new StringBuilder();
//		sql.append("select g.id,goods_url,goods_title,googs_seller,googs_img,googs_price,googs_number,pWprice,googs_size,googs_color, ");
//		sql.append(" freight,remark,datatime,norm_least,delivery_time,freight_free,g.currency,goods_type,g.goods_class, ");
//		sql.append(" IFNULL(c.showname,'-') as showname,IFNULL(c.price,0.0) as rate_price,IFNULL(c.deposit_rate,0) as deposit_rates,ROUND(googs_price * googs_number,2 )as ceil_money ,tab_class.class_money ");
//		sql.append(", ifnull(width,0) as width ,ifnull(perWeight,'') as perWeight, seilUnit, goodsUnit from goods_car g   LEFT JOIN (select *from class_discount  group by classtype) as  c on g.goods_class=c.classtype "); 
//		sql.append(" inner join (select cast(sum(t.googs_price * t.googs_number) as DECIMAL(10,2)) class_money,t.goods_class ");
//		sql.append(" from goods_car t  where 1=1  and  t.userid =?   and (state=0 or state=3) ");
//		sql.append(" group by t.goods_class)  tab_class  on tab_class.goods_class=g.goods_class ");
//		sql.append(" where 1=1  and   g.userid =?  and (state=0 or state=3) ");
//		sql.append(" order by goods_class,g.id desc ");
		try {
			stmt2 = conn.prepareStatement(sql);
			stmt2.setInt(1, uid);
			stmt2.setInt(2, uid);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				SpiderBean spider = new SpiderBean();
				spider.setUserId(uid);
				spider.setId(rs2.getInt("id"));
				spider.setNotfreeprice(rs2.getDouble("notfreeprice"));
				spider.setColor(rs2.getString("googs_color"));
				spider.setFreight(rs2.getString("freight"));
				spider.setImg_url(rs2.getString("googs_img"));
				spider.setName(rs2.getString("goods_title"));
				spider.setNumber(rs2.getInt("googs_number"));
				spider.setPrice(rs2.getString("googs_price"));
				spider.setRemark(rs2.getString("remark"));
				spider.setSeller(rs2.getString("googs_seller")); 
				spider.setSize(rs2.getString("googs_size"));
				spider.setUrl(rs2.getString("goods_url"));
				spider.setNorm_least(rs2.getString("norm_least"));
				spider.setDelivery_time(rs2.getString("delivery_time"));
				spider.setFreight_free(rs2.getInt("freight_free"));
				spider.setCreatetime(rs2.getString("datatime"));
				spider.setpWprice(rs2.getString("pWprice"));
				spider.setCurrency(Util.currencyChange(rs2.getString("currency")));
				spider.setGrade_discount(discount);
				spider.setGoods_type(rs2.getString("goods_type"));
				spider.setShowname((rs2.getString("showname")));
				spider.setDeposit_rates(Double.parseDouble(rs2.getString("deposit_rates")));
				spider.setRate_price(Double.parseDouble(rs2.getString("rate_price")));
				spider.setCeil_money(Double.parseDouble(rs2.getString("ceil_money")));
				spider.setClass_money(Double.parseDouble(rs2.getString("class_money")));
				spider.setWidth(rs2.getString("width"));
				spider.setPerWeight(rs2.getString("perWeight"));
				spider.setSeilUnit(rs2.getString("seilUnit"));
				spider.setGoodsUnit(rs2.getString("goodsUnit"));
				spiderlist.add(spider);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		
		return spiderlist;
	}

	@Override
	public int updateVolumeOrWeight(String f, int id, String vw, String total) {
		int i =0;
		Connection con  = DBHelper.getInstance().getConnection();
		// Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		String sql = "";
		if(f.equals("0")){
			sql = "update goods_car set width=?, bulk_volume=? where id=?";
			try {
				ps1 = con.prepareStatement(sql);
				ps1.setString(1, vw);
				ps1.setString(2, total);
				ps1.setInt(3, id);
				
				/*ps2 = conn.prepareStatement(sql);
				ps2.setString(1, vw);
				ps2.setString(2, total);
				ps2.setInt(3, id);*/

				List<String> listValues = new ArrayList<>();
				listValues.add(String.valueOf(vw));
				listValues.add(String.valueOf(total));
				listValues.add(String.valueOf(id));
				String runSql = DBHelper.covertToSQL(sql, listValues);
				String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
				int countRs = 0;
				if(StringUtils.isBlank(rsStr)){
					countRs = Integer.valueOf(rsStr);
				}
				
				i = ps1.executeUpdate();

				// i+=ps2.executeUpdate();
				i+= countRs;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps1!=null){
					try {
						ps1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(ps2!=null){
					try {
						ps2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}else{
			sql = "update goods_car set perWeight=?,per_weight=?,total_weight=? where id=?";
			try {
				ps1 = con.prepareStatement(sql);
				ps1.setString(1, vw);
				ps1.setString(2, vw);
				ps1.setString(3, total);
				ps1.setInt(4, id);
				
				/*ps2 = conn.prepareStatement(sql);
				ps2.setString(1, vw);
				ps2.setString(2, vw);
				ps2.setString(3, total);
				ps2.setInt(4, id);*/

				List<String> listValues = new ArrayList<>();
				listValues.add(String.valueOf(vw));
				listValues.add(String.valueOf(vw));
				listValues.add(String.valueOf(total));
				listValues.add(String.valueOf(id));
				String runSql = DBHelper.covertToSQL(sql, listValues);
				String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
				int countRs = 0;
				if(StringUtils.isBlank(rsStr)){
					countRs = Integer.valueOf(rsStr);
				}
				
				i = ps1.executeUpdate();
				// i+=ps2.executeUpdate();
				i+= countRs;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps1!=null){
					try {
						ps1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(ps2!=null){
					try {
						ps2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		DBHelper.getInstance().closeConnection(con);
		// DBHelper.getInstance().closeConnection(conn);
		return i;
	}
	 
}
