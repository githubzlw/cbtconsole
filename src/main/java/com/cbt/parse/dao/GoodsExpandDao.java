package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.GoodsExpandBean;
import com.cbt.parse.bean.SqlBean;
import com.cbt.parse.daoimp.IGoodsExpandDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GoodsExpandDao implements IGoodsExpandDao {

	@Override
	public int addAll(GoodsExpandBean bean) {
		int result = 0;
		String sql = "insert goodsdata_expand_ex (cid,pid,url,time,catid1,catid2,catid3,catid4,catid5,catid6,noteurl,"
				+ "img,name,minprice,maxprice,morder,sell,punit,gunit,pvid,sid) "
				+ "value(?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getCid());
			stmt.setString(2, bean.getPid());
			stmt.setString(3, bean.getUrl());
			stmt.setString(4, bean.getCatid1());
			stmt.setString(5, bean.getCatid2());
			stmt.setString(6, bean.getCatid3());
			stmt.setString(7, bean.getCatid4());
			stmt.setString(8, bean.getCatid5());
			stmt.setString(9, bean.getCatid6());
			stmt.setString(10, bean.getNoteurl());
			stmt.setString(11, bean.getImg());
			stmt.setString(12, bean.getName());
			stmt.setDouble(13, bean.getMinprice());
			stmt.setDouble(14, bean.getMaxprice());
			stmt.setInt(15, bean.getMorder());
			stmt.setInt(16, bean.getSell());
			stmt.setString(17, bean.getPunit());
			stmt.setString(18, bean.getGunit());
			stmt.setString(19, bean.getPvid());
			stmt.setString(20, bean.getSid());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int updateAll(GoodsExpandBean bean) {
		int result = 0;
		String sql = "update goodsdata_expand_ex set noteurl=?,cid=?,pid=?,time=now(),catid1=?,catid2=?,catid3=?,catid4=?,catid5=?,catid6=?,"
				+ "img=?,name=?,minprice=?,maxprice=?,morder=?,sell=?,punit=?,gunit=?,pvid=?,sid=? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getNoteurl());
			stmt.setString(2, bean.getCid());
			stmt.setString(3, bean.getPid());
			stmt.setString(4, bean.getCatid1());
			stmt.setString(5, bean.getCatid2());
			stmt.setString(6, bean.getCatid3());
			stmt.setString(7, bean.getCatid4());
			stmt.setString(8, bean.getCatid5());
			stmt.setString(9, bean.getCatid6());
			stmt.setString(10, bean.getImg());
			stmt.setString(11, bean.getName());
			stmt.setDouble(12, bean.getMinprice());
			stmt.setDouble(13, bean.getMaxprice());
			stmt.setInt(14, bean.getMorder());
			stmt.setInt(15, bean.getSell());
			stmt.setString(16, bean.getPunit());
			stmt.setString(17, bean.getGunit());
			stmt.setString(18, bean.getPvid());
			stmt.setString(19, bean.getSid());
			stmt.setString(20, bean.getUrl());
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int updateValid(String url, int valid) {
		int result = 0;
		String sql = "update goodsdata_expand_ex set valid=?,time=now() where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setString(2, url);
			
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int addNote(String noteurl,String url) {
		int result = 0;
		String sql = "insert goodsdata_expand_ex (noteurl,url,time) value(?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, noteurl);
			stmt.setString(2,url);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public int updateNote(String noteurl,String url) {
		int result = 0;
		String sql = "update goodsdata_expand_ex set noteurl=? where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,noteurl);
			stmt.setString(2, url);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
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
	public ArrayList<HashMap<String, String>> queryNote(String url) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> bean = null;
		String sql = "select distinct url,noteurl,time from goodsdata_expand_ex where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			while(rs.next()){
				bean = new HashMap<String, String>();
				bean.put("url", rs.getString("url"));
				bean.put("noteurl", rs.getString("noteurl"));
				bean.put("time", rs.getString("time"));
				list.add(bean);
				bean = null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	@Override
	public ArrayList<GoodsExpandBean> queryExist(String url) {
		ArrayList<GoodsExpandBean> list = new ArrayList<GoodsExpandBean>();
		GoodsExpandBean bean = null;
		String sql = "select distinct catid1,noteurl,name from goodsdata_expand_ex where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			while(rs.next()){
				bean = new GoodsExpandBean();
				bean.setCatid1(rs.getString("catid1"));
				bean.setNoteurl(rs.getString("noteurl"));
				bean.setName(rs.getString("name"));
				list.add(bean);
				bean = null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	/**用于搜索商品集合
	 * @return
	 */
	@Override
	public ArrayList<GoodsExpandBean> querySearch(ArrayList<SqlBean> bean) {
		ArrayList<GoodsExpandBean> list = new ArrayList<GoodsExpandBean>();
		String sql = "select sql_calc_found_rows distinct valid,name,url,minprice,maxprice,punit,gunit,img,morder,sell from goodsdata_expand_ex where valid=1 ";
		
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		GoodsExpandBean rsTree = null;
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		String para = null;
		int bean_num = bean.size();
		int name_index = 0;
		int index = 0;
		int total = 0;
		for(int i=0;i<bean_num;i++){
			para = bean.get(i).getPara();
			if("name".equals(para)){
				if(name_index==0){
					sb.append("and match (name) AGAINST (? IN BOOLEAN MODE)");
				}
				name_index++;
			}else if("cid".equals(para)){
				sb.append(" and (catid1=? or catid2=? or catid3=? or catid4=? or catid5=? or catid6=? )");
			}else if("pid".equals(para)){
				sb.append(" and match (pvid) against (? in boolean mode)");
			}else if("price".equals(para)){
				sb.append(" order by ").append("minprice").append(" "+bean.get(i).getValue());
			}else if("solder".equals(para)){
				sb.append(" order by ").append("sell").append(" "+bean.get(i).getValue());
			}else if("minq".equals(para)){
				sb.append(" and ").append("morder ").append("> ").append("?");
			}else if("maxq".equals(para)){
				sb.append(" and ").append("morder ").append("< ").append("?");
			}else if("price1".equals(para)){
				sb.append(" and ").append("minprice ").append("> ").append("?");
			}else if("price2".equals(para)){
				sb.append(" and ").append("minprice ").append("< ").append("?");
			}else if("page".equals(para)){
				sb.append(" limit ").append("?").append(",40 ");
			}
		}
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sb.toString());
			StringBuffer wd = new StringBuffer();
			for(int i=0;i<bean_num;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(i<name_index){
						wd.append(" +").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, wd.toString());
					}
				}else if("cid".equals(para)){
					String value = bean.get(i).getValue();
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, value);
				}else if("pid".equals(para)){
					String value = "";
					String[] values =  bean.get(i).getValue().split(",");
					for(int j=0;j<values.length;j++){
						value = value+" +"+values[j];
					}
					index++;
					stmt.setString(index, value);
				}else if("minq".equals(para)){
					index++;
					stmt.setInt(index, Integer.valueOf(bean.get(i).getValue()));
				}else if("maxq".equals(para)){
					index++;
					stmt.setInt(index, Integer.valueOf(bean.get(i).getValue()));
				}else if("price1".equals(para)){
					index++;
					stmt.setString(index, bean.get(i).getValue());
				}else if("price2".equals(para)){
					index++;
					stmt.setString(index, bean.get(i).getValue());
				}else if("page".equals(para)){
					index++;
					stmt.setInt(index, (Integer.valueOf(bean.get(i).getValue())-1)*40);
				}
			}
			stmt2 = conn.prepareStatement("select found_rows();");
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			while(rs2.next()){
				total = rs2.getInt("found_rows()");		
			}
			while(rs.next()){
				rsTree = new GoodsExpandBean();
				rsTree.setTotal(total);
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setName( rs.getString("name"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setMinprice( rs.getDouble("minprice"));
				rsTree.setMaxprice( rs.getDouble("maxprice"));
				rsTree.setPunit( rs.getString("punit"));
				rsTree.setImg(rs.getString("img"));
				rsTree.setMorder( rs.getInt("morder"));
				rsTree.setSell(rs.getInt("sell"));
				rsTree.setGunit(rs.getString("gunit"));
				list.add(rsTree);
				rsTree = null;
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
			if(stmt2!=null){
				try {
					stmt2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(rs2!=null){
				try {
					rs2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	/* 配合goodsdata表
	 */
	public ArrayList<GoodsExpandBean> queryDate(String url) {
		ArrayList<GoodsExpandBean> list = new ArrayList<GoodsExpandBean>();
		String sql = "select distinct catid1,catid2,catid3,catid4,catid5,catid6,noteurl,url "
				+ "from goodsdata_expand_ex where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		GoodsExpandBean bean =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			while(rs.next()){
				bean = new GoodsExpandBean();
				bean.setCatid1(rs.getString("catid1"));
				bean.setCatid2(rs.getString("catid2"));
				bean.setCatid3(rs.getString("catid3"));
				bean.setCatid4(rs.getString("catid4"));
				bean.setCatid5(rs.getString("catid5"));
				bean.setCatid6(rs.getString("catid6"));
				bean.setNoteurl(rs.getString("noteurl"));
				bean.setUrl(rs.getString("url"));
				list.add(bean);
				bean = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	public ArrayList<String> queryTest(String page) {
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select url from goodsdata_expand_ex where catid1 is null and "
				+ "(catid2 is not null or catid3 is not null or catid4 is "
				+ "not null or catid5 is not null or catid6 is not null) where ?,100";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

}
