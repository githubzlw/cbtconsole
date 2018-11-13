package com.cbt.dc.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.service.SearchUtils;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DcDaoImpl implements DcDao {

	String urll = null;
	String pricee = null;
	int goodssumm = 0;
	
	@Override
	public void changeBuyGoods(String url, String price, int goodssum) {
		urll = url;
		pricee = price;
		goodssumm = goodssum;
		// 先判断商品是否被买过
		String sql = "select count(*) as num from buy_goods where url = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		int res = 0;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			while (rs.next()) {
				res = rs.getInt("num");
			}
			if (res == 0) {
				//没有被买过
				this.addBuyGoods();
			} else {
				//已经被买过
				this.updateBuyGoods();
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

	}
	
	//没有被买过进入addBuyGoods()方法
	public void addBuyGoods() {
		String sql = "insert into buy_goods(url,goodID,category,count,name,imgurl,price,title,buysum,mOrder,pUnit,createTime,updateTime)"
				+ "select g.url,g.id,g.category,1,g.name,g.img, ? ,g.title, ? ,g.mOrder,g.pUnit,now(),now() from goodsdata as g ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,pricee );
			stmt.setInt(2,goodssumm );
			int result = stmt.executeUpdate();
			if (result == 1) {
				rs = stmt.getGeneratedKeys();
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
	}
	
	//已经被买过进入updateBuyGoods()方法
	public void updateBuyGoods() {
		String sql = "update buy_goods set count = count+1,buysum = buysum + ? ,price = ? ,updateTime = now() where url = ? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, goodssumm );
			stmt.setString(2, pricee );
			stmt.setString(3, urll );
			stmt.executeUpdate();
		}catch (Exception e) {
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
	}
	
	//获取热销商品
	@Override
	public ArrayList<SearchGoods> getPopProducts(String keywords,String catList, String catid) {
				
		ArrayList<SearchGoods> bgbList = new ArrayList<SearchGoods>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		//System.out.print("keywords:"+keywords+ "catList:"+catList +"catid:"+catid);
		String sql = null;
		String catKey = null;
		if((catid==null||catid.isEmpty())&&(keywords==null||keywords.isEmpty())&&(catList==null||catList.isEmpty())){
			sql = "select * from buy_goods where valid=1 order by buysum desc limit 12";
			try {
				stmt = conn.prepareStatement(sql);
			} catch (SQLException e) { e.printStackTrace(); }
		} else {
			if(catid!=null&&!catid.isEmpty()){
				sql = "select * from buy_goods where match(category) against ( ? ) and valid=1 order by buysum desc limit 12";
				catKey = catid;
				try {
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, catKey);
				} catch (SQLException e) { e.printStackTrace(); }
			} else {
				if(keywords!=null&&!keywords.isEmpty()){
					keywords = keywords.replace(","," +");
					catKey = "+"+keywords;
						if(catList!=null&&!catList.isEmpty()){
							catList = catList.replace(","," ");
							sql = "select * from buy_goods where match(category) against ( ? IN BOOLEAN MODE ) and valid=1 union select * from buy_goods where match(title) against ( ? IN BOOLEAN MODE ) and valid=1 order by buysum desc limit 12 ";
							try {
								stmt = conn.prepareStatement(sql);
								stmt.setString(1, catList);
								stmt.setString(2, catKey);
							} catch (SQLException e) { e.printStackTrace(); }
						} else {
							sql = "select * from buy_goods where match(title) against ( ? IN BOOLEAN MODE ) and valid=1 order by buysum desc limit 12";
							try {
								stmt = conn.prepareStatement(sql);
								stmt.setString(1, catKey);
							} catch (SQLException e) { e.printStackTrace(); }
						}
					}else{
						if(catList!=null&&!catList.isEmpty()){
							catList = catList.replace(","," ");
							sql = "select * from buy_goods where match(category) against ( ? IN BOOLEAN MODE  ) and valid=1 order by buysum desc limit 12";
							try {
								stmt = conn.prepareStatement(sql);
								stmt.setString(1, catList);
							} catch (SQLException e) { e.printStackTrace(); }
						}
					}
				}
		}
		try {
			rs = stmt.executeQuery();
			if(bgbList!=null){
				bgbList.clear();
			}
			while (rs.next()) {
				String imgurl = rs.getString("imgurl");
				if(imgurl!=null&&!imgurl.isEmpty()){
					SearchGoods bgb = new SearchGoods();
					bgb.setGoods_url(TypeUtils.encodeGoods(rs.getString("url")));
					bgb.setGoods_name(SearchUtils.nameVert(rs.getString("name"), 26));
					String[] imgu = null;
					imgurl = imgurl.replace("["," ");
					imgurl = imgurl.replace("]"," ");
					imgu = imgurl.split(",");
					bgb.setGoods_image(imgu[0]);
					bgb.setGoods_price(rs.getString("pUnit")+rs.getString("price"));
					bgb.setGoods_solder(rs.getString("buysum"));
					bgb.setGoods_minOrder(rs.getString("morder"));
					bgb.setKey_type("goods");
					bgbList.add(bgb);
					bgb = null;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		  finally {
			if (rs != null) {
				try { rs.close(); } 
				catch (SQLException e) {e.printStackTrace();}
			}
			if (stmt != null) {
				try { stmt.close(); }
				catch (SQLException e) {e.printStackTrace();}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return bgbList;
	}
	
}
