package com.cbt.website.dao;

import com.cbt.bean.GoodsCheckBean;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;
import com.cbt.website.bean.AliExpressTop240Bean;
import com.cbt.website.bean.GoodsSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AliExpress240Dao implements IAliExpress240Dao {

	/*@Override
	public int saveAliExpress240(
			List<AliExpressTop240Bean> aliExpressTop240Beans,String keyword,String typeid, int results_typeid) {
		String sql = "insert into tab_top240results(results_typeid,sort,aliexpress_url,img,gname,price,sales,minOrder,freight_free,gimgurl,createtime) values(?,?,?,?,?,?,?,?,?,?,now());";
		String sql2 = "insert into goodsdatacheck(id,goodsname,url,imgurl,imgpath,price) select 0,?,?,?,?,? from goodsdatacheck where  not exists  (select url from goodsdatacheck  where url =?  )limit 0,1  ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		int res = 0;
		try {	
			stmt2 = conn.prepareStatement(sql2);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < aliExpressTop240Beans.size(); i++) {
				AliExpressTop240Bean aliExpressTop240Bean = aliExpressTop240Beans.get(i);
				stmt.setInt(1, results_typeid);
				stmt.setString(2, aliExpressTop240Bean.getSort());
				stmt.setString(3, aliExpressTop240Bean.getAliexpress_url());
				stmt.setString(4, aliExpressTop240Bean.getImg());
				stmt.setString(5, aliExpressTop240Bean.getGname());
				stmt.setString(6, aliExpressTop240Bean.getPrice());
				stmt.setInt(7, aliExpressTop240Bean.getSales());
				stmt.setString(8, aliExpressTop240Bean.getMinOrder());
				stmt.setString(9, aliExpressTop240Bean.getGfree());
				stmt.setString(10, aliExpressTop240Bean.getGimgurl());
				stmt.addBatch();
				stmt2.setString(1, aliExpressTop240Bean.getGname());
				stmt2.setString(2, aliExpressTop240Bean.getAliexpress_url());
				stmt2.setString(3, "[" + aliExpressTop240Bean.getGimgurl() + "]");
				stmt2.setString(4, aliExpressTop240Bean.getGimgurl());
				stmt2.setString(5, aliExpressTop240Bean.getPrice());
				stmt2.setString(6, aliExpressTop240Bean.getAliexpress_url());
				stmt2.addBatch();
			}
			int[] ress = stmt.executeBatch();
			stmt2.executeBatch();
			res = ress.length;
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
	}*/

	@Override
	public List<AliExpressTop240Bean> getAliExpress240s(String typeId,
			String keyword) {
		String sql = "select id,keyword,typeid,sort,aliexpress_url,img,gname,price,sales from tab_top240results gc where ";
		if(Utility.getStringIsNull(typeId)){
			sql += " typeid=? ";
		}else if(Utility.getStringIsNull(keyword)){
			sql += " keyword like '%?%' ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<AliExpressTop240Bean> aliExpressTop240Beans = new ArrayList<AliExpressTop240Bean>();
		try {
			stmt = conn.prepareStatement(sql);
			if(Utility.getStringIsNull(typeId)){
				stmt.setString(1, typeId);
			}else if(Utility.getStringIsNull(keyword)){
				stmt.setString(1, keyword);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				AliExpressTop240Bean aliExpressTop240Bean = new AliExpressTop240Bean();
				aliExpressTop240Bean.setId(rs.getInt("id"));
				aliExpressTop240Bean.setKeyword(rs.getString("keyword"));
				aliExpressTop240Bean.setTypeid(rs.getString("typeid"));
				aliExpressTop240Bean.setSort(rs.getString("sort"));
				aliExpressTop240Bean.setAliexpress_url(rs.getString("aliexpress_url"));
				aliExpressTop240Bean.setImg(rs.getString("img"));
				aliExpressTop240Bean.setGname(rs.getString("gname"));
				aliExpressTop240Bean.setPrice(rs.getString("price"));
				aliExpressTop240Bean.setSales(rs.getInt("sales"));
				aliExpressTop240Beans.add(aliExpressTop240Bean);
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
		
		return aliExpressTop240Beans;
	}
	@Override
	public ArrayList<AliExpressTop240Bean> getAliExpress240(int results_typeid,String sort,int page) {
		String sql = "select sql_calc_found_rows distinct  id,sort,aliexpress_url,gimgurl,minOrder,gname,price,sales,freight_free"
				+ " from tab_top240results  where results_typeid=? and sort=? and state=0 limit ?,40";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int total = 0;
		ArrayList<AliExpressTop240Bean> aliExpressTop240Beans = new ArrayList<AliExpressTop240Bean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, results_typeid);
			stmt.setString(2, sort);
			stmt.setInt(3, (page-1)*40);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				AliExpressTop240Bean aliExpressTop240Bean = new AliExpressTop240Bean();
				aliExpressTop240Bean.setTotal(total);
				aliExpressTop240Bean.setId(rs.getInt("id"));
				aliExpressTop240Bean.setSort(rs.getString("sort"));
				aliExpressTop240Bean.setAliexpress_url(rs.getString("aliexpress_url"));
				aliExpressTop240Bean.setGimgurl(rs.getString("gimgurl"));
				aliExpressTop240Bean.setGname(rs.getString("gname"));
				aliExpressTop240Bean.setPrice(rs.getString("price"));
				aliExpressTop240Bean.setSales(rs.getInt("sales"));
				aliExpressTop240Bean.setGfree(rs.getString("freight_free"));
				aliExpressTop240Bean.setMinOrder(rs.getString("minOrder"));
				aliExpressTop240Beans.add(aliExpressTop240Bean);
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
		
		return aliExpressTop240Beans;
	}

	@Override
	public int saveSearch240_type(String keyword,String typeid,int pagenum) {
		String sql = "insert into tab_top240results_type(keyword,typeid,pagenum,createtime,state) values(?,?,?,now(),1);";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, keyword);
			stmt.setString(2, typeid);
			stmt.setInt(3, pagenum);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if(rs.next()){
				res=rs.getInt(1);
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
	public List<String[]> getSearch240_type(List<String[]> search240_type) {
		String sql = "select keyword,typeid,examinetime from tab_top240results_type gc where ";
		for (int i = 0; i < search240_type.size(); i++) {
			sql += " ( keyword=? and typeid=? ) ";
			if(i != search240_type.size()-1){
				sql += " or ";
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			int j = 1;
			for (int i = 0; i < search240_type.size(); i++) {
				stmt.setString(j, search240_type.get(i)[0]);
				stmt.setString(j+1, search240_type.get(i)[1]);
				j = j+2;
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String keyword = rs.getString("keyword");
				String typeid = rs.getString("typeid");
				for (int i = 0; i < search240_type.size(); i++) {
					if(keyword.equals(search240_type.get(i)[0]) && typeid.equals(search240_type.get(i)[1])){
						search240_type.remove(i);
					}
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
		return search240_type;
	}

	@Override
	public HashMap<String, String> getSearch240_typeCount(String keyword, String typeid) {
		String sql = "select  *from tab_top240results_type where keyword=? and typeid=? limit 0,1;";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> res = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, typeid);
			rs = stmt.executeQuery();
			if(rs.next()){
				 res = new HashMap<String, String>();
				res.put("pagenum", rs.getString("pagenum"));
				res.put("search_number", rs.getString("search_number"));
				res.put("results_typeid", rs.getString("id"));
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
	public List<String[]> getSearch240_type() {
		String sql = "select id,keyword,typeid,examinetime,createtime,search_number from tab_top240results_type";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String[]>  list = new ArrayList<String[]>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String keyword = rs.getString("keyword");
				String typeid = rs.getString("typeid");
				String examinetime = rs.getString("examinetime");
				String createtime = rs.getString("createtime");
				String search_number = rs.getString("search_number");
				int id = rs.getInt("id");
				String[] results_type= {keyword,typeid,createtime,examinetime,id+"",search_number};
				list.add(results_type);
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
		return list;
	}

	@Override
	public int upSearch240(List<Integer> ids, int type) {
		String sql = "update tab_top240results set state=? where id=?";
		for (int i = 1; i < ids.size(); i++) {
			sql += " or id=? ";
		}
		System.out.println(sql);
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, type);
			stmt.setInt(2, ids.get(0));
			for (int i = 1; i < ids.size(); i++) {
				stmt.setInt(2+i, ids.get(i));
				System.out.println(2+i);
			}
			res = stmt.executeUpdate();
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
	public List<GoodsCheckBean> getGoodsCheckBeans(int id) {
		String sql ="select rs.id,rs.gimgurl,rs.state ";
		sql = sql +",gdc.id as id,rs.gname,rs.aliexpress_url as url,imgurl,imgpath,tbimg,tburl,rs.price,tbimg1,tburl1,tbimg2,tburl2,tbimg3,tburl3,tbprice,tbprice1,tbprice2,tbprice3, ";
		sql = sql +"tbname,tbname1,tbname2,tbname3,least(imgcheck0,imgcheck1,imgcheck2,imgcheck3) as minImgCheck,least(tbprice,tbprice1,tbprice2,tbprice3) as minPrice,style0,style1,style2,style3,goodsstyle, ";
		sql = sql +"imgcheck0,imgcheck1,imgcheck2,imgcheck3,goodsstyle  ";
		sql = sql +"from tab_top240results rs  left join goodsdatacheck gdc on rs.aliexpress_url = gdc.url  ";
		sql = sql +"where results_typeid=? ";
		String sql2= "select goods_url,goods_purl,goods_img_url,goods_price,goods_name from goods_source where goods_url = ? order by updatetime desc";
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		conn = DBHelper.getInstance().getConnection();
		List<GoodsCheckBean> gsfList = new ArrayList<GoodsCheckBean>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs1 = stmt.executeQuery();
			while (rs1.next()) {
					List<GoodsCheckBean> aliSourceList = new ArrayList<GoodsCheckBean>();
					GoodsCheckBean gfb1 = new GoodsCheckBean();
					gfb1.setpId(rs1.getInt("rs.id"));
					gfb1.setGoodsName(rs1.getString("rs.gname"));
					gfb1.setUrl(rs1.getString("url"));
					gfb1.setImgpath(rs1.getString("rs.gimgurl"));
					gfb1.setPrice(rs1.getString("price"));
					gfb1.setTbImg(rs1.getString("tbimg"));
					gfb1.setTbUrl(rs1.getString("tburl"));
					gfb1.setTbprice(rs1.getString("tbprice"));
					gfb1.setTbImg1(rs1.getString("tbimg1"));
					gfb1.setTbUrl1(rs1.getString("tburl1"));
					gfb1.setTbprice1(rs1.getString("tbprice1"));
					gfb1.setTbImg2(rs1.getString("tbimg2"));
					gfb1.setTbUrl2(rs1.getString("tburl2"));
					gfb1.setTbprice2(rs1.getString("tbprice2"));
					gfb1.setTbImg3(rs1.getString("tbimg3"));
					gfb1.setTbUrl3(rs1.getString("tburl3"));
					gfb1.setTbprice3(rs1.getString("tbprice3"));
					gfb1.setMinImgCheck(rs1.getInt("minImgCheck"));
					gfb1.setMinPrice(rs1.getString("minPrice"));
					gfb1.setTbName(rs1.getString("tbname"));
					gfb1.setTbName1(rs1.getString("tbname1"));
					gfb1.setTbName2(rs1.getString("tbname2"));
					gfb1.setTbName3(rs1.getString("tbname3"));
					gfb1.setStyle0(rs1.getString("style0"));
					gfb1.setStyle1(rs1.getString("style1"));
					gfb1.setStyle2(rs1.getString("style2"));
					gfb1.setStyle3(rs1.getString("style3"));
					gfb1.setAlStyle(rs1.getString("goodsstyle"));
					gfb1.setImgCheck0(rs1.getInt("imgcheck0"));
					gfb1.setImgCheck1(rs1.getInt("imgcheck1"));
					gfb1.setImgCheck2(rs1.getInt("imgcheck2"));
					gfb1.setImgCheck3(rs1.getInt("imgcheck3"));
					gfb1.setAligSourceUrl(rs1.getInt("state")+"");//是否有货源
					stmt2 = conn.prepareStatement(sql2);
					stmt2.setString(1,rs1.getString("url"));
					rs2 = stmt2.executeQuery();
					while(rs2.next()){
						GoodsCheckBean gfb2 = new GoodsCheckBean();
						gfb2.setAligSourceUrl(rs2.getString("goods_url"));
						gfb2.setAliSourceUrl(rs2.getString("goods_purl"));
						gfb2.setAliSourceImgUrl(rs2.getString("goods_img_url"));
						gfb2.setAliSourcePrice(rs2.getString("goods_price"));
						gfb2.setAliSourceName(rs2.getString("goods_name"));
						aliSourceList.add(gfb2);
					}
					gfb1.setAliSourceList(aliSourceList);
					gsfList.add(gfb1);
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
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return gsfList;
	}

	@Override
	public int upSearch240_type(int number, int gid) {
		String sql = "update tab_top240results_type set search_number=?,examinetime=now() where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, number);
			stmt.setInt(2, gid);
			res = stmt.executeUpdate();
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
	public int addGoodsSource(List<GoodsSource> goodsSource) {
		int result  = 0;
		String sql = "insert into goods_source (goodsdataid,goods_url,goods_purl,goods_img_url,"
				+ "goods_price,goods_name,sourceType,orderDesc,updatetime) "
				+ "values (?,?,?,?,?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < goodsSource.size(); i++) {
				GoodsSource bean = goodsSource.get(i);
				stmt.setInt(1, bean.getGoodsId());
				stmt.setString(2, bean.getGoodsUrl());
				stmt.setString(3, bean.getGoodsPurl());
				stmt.setString(4, bean.getGoodsImg());
				stmt.setDouble(5, Double.valueOf(bean.getGoodsPrice()));
				stmt.setString(6, bean.getGoodsName());
				stmt.setString(7, bean.getSourceType());
				stmt.setInt(8, bean.getOrderDesc());
				stmt.addBatch();
			}
			int[] results = stmt.executeBatch();
			result = results.length;
		} catch (Exception e) {
			e.printStackTrace();
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
	public int addGoodsSource(GoodsSource bean) {
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
			e.printStackTrace();
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
	public int upSearch240_type_number(int gid) {
		String sql = "update tab_top240results_type set search_number=search_number+1 where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gid);
			res = stmt.executeUpdate();
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
	public int upSearch240_img(String url, String img) {
		String sql = "update tab_top240results set img=? where aliexpress_url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, img);
			stmt.setString(2, url);
			res = stmt.executeUpdate();
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
}
