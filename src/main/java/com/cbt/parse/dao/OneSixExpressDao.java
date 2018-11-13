package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.OneSixExpressBean;
import com.cbt.parse.bean.SqlBean;
import com.cbt.parse.daoimp.IOneSixExpressDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OneSixExpressDao implements IOneSixExpressDao {

	@Override
	public int add(OneSixExpressBean bean) {
		int result = 0;
		String sql = "insert onesixexpress_goods (url,name,sold,morder,price,img,cid1,cid2,"
				+ "wprice,imgs,detail,info,type,gunit,punit,weight,createtime,sid,pid,imgsize) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getUrl());
			stmt.setString(2, bean.getName());
			stmt.setInt(3, Integer.valueOf(bean.getSold()));
			stmt.setInt(4, Integer.valueOf(bean.getMorder()));
			stmt.setDouble(5, Double.valueOf(bean.getPrice()));
			stmt.setString(6,bean.getImg());
			stmt.setString(7, bean.getCatid1());
			stmt.setString(8, bean.getCatid2());
//			stmt.setString(9, bean.getSimilar());
			stmt.setString(9, bean.getWprice());
			stmt.setString(10, bean.getImgs());
			stmt.setString(11, bean.getDetail());
			stmt.setString(12, bean.getInfo());
			stmt.setString(13, bean.getType());
			stmt.setString(14, bean.getGunit());
			stmt.setString(15, bean.getPunit());
			stmt.setString(16, bean.getWeight());
			stmt.setString(17, bean.getSid());
			stmt.setString(18, bean.getPid());
			stmt.setString(19, bean.getImgsize());
//			stmt.setString(21, bean.getPvid());
//			stmt.setString(22, bean.getSame());
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
	public int update(OneSixExpressBean bean) {
		int result = 0;
		String sql = "update onesixexpress_goods_copy set name=?,enname=?,type=?,entype=?,detail=?,endetail=?,"
				+ "price=?,prices=?,wprice=?,wprices=?,fprice=?,feeprice=?,posttime=?,method=?,"
				+ "img=?,imgs=?,weight=?,sold=?,morder=?,info=?,"
				+ "createtime=now(),valid=1 where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getName());
			stmt.setString(2, bean.getEnname());
			stmt.setString(3, bean.getType());
			stmt.setString(4, bean.getEntype());
			stmt.setString(5, bean.getDetail());
			stmt.setString(6, bean.getEndetail());
			stmt.setDouble(7, Double.valueOf(bean.getPrice()));
			stmt.setDouble(8, Double.valueOf(bean.getPrices()));
			stmt.setString(9, bean.getWprice());
			stmt.setString(10, bean.getWprices());
			stmt.setDouble(11, Double.valueOf(bean.getFprice()));
			stmt.setDouble(12, Double.valueOf(bean.getFeeprice()));
			stmt.setString(13, bean.getPosttime());
			stmt.setString(14, bean.getMethod());
			stmt.setString(15,bean.getImg());
			stmt.setString(16, bean.getImgs());
			stmt.setString(17, bean.getWeight());
			stmt.setInt(18, Integer.valueOf(bean.getSold()));
			stmt.setInt(19, Integer.valueOf(bean.getMorder()));
			stmt.setString(20, bean.getInfo());
			stmt.setString(21, bean.getUrl());
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
	public int updateValid(String url,int valid) {
		int result = 0;
		String sql = "update onesixexpress_goods_copy set valid=?,createtime=now() where url=?";
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
	public int queryExsis(String url) {
		int result = 0;
		String sql = "select count(*) from onesixexpress_goods where url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getInt("count(*)");
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
		return result;
	}
	/**用于搜索商品集合
	 * @return
	 */
	@Override
	public ArrayList<OneSixExpressBean> querySearch(ArrayList<SqlBean> bean) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select sql_calc_found_rows distinct *from onesixexpress_goods where id<90001 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		OneSixExpressBean rsTree = null;
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
					sb.append(" and match (enname) AGAINST (? IN BOOLEAN MODE)");
				}
				name_index++;
			}else if("pid".equals(para)){
				sb.append(" and match (pvids) against (? in boolean mode)");
			}else if("valid".equals(para)){
				sb.append(" and valid!=0");
			}else if("cid".equals(para)){
				sb.append(" and (catid1=? or catid2=? or catid3=? or catid4=? or catid5=? or catid6=?)");
			}else if("minq".equals(para)){
				sb.append(" and ").append("morder ").append("> ").append("?");
			}else if("maxq".equals(para)){
				sb.append(" and ").append("morder ").append("< ").append("?");
			}else if("price1".equals(para)){
				sb.append(" and ").append("prices ").append("> ").append("?");
			}else if("price2".equals(para)){
				sb.append(" and ").append("prices ").append("< ").append("?");
			}else if("price".equals(para)){
				sb.append(" order by ").append("prices").append(" "+bean.get(i).getValue());
			}else if("solder".equals(para)){
				sb.append(" order by ").append("sold").append(" "+bean.get(i).getValue());
			}else if("page".equals(para)){
				sb.append(" limit ").append("?").append(",40 ");
			}
		}
		try {
			System.out.println(sb.toString());
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
				}else if("pid".equals(para)){
					String value = "";
					String[] values =  bean.get(i).getValue().split(",");
					for(int j=0;j<values.length;j++){
						value = value+" +"+values[j];
					}
					index++;
					stmt.setString(index, value);
				}else if("cid".equals(para)){
					String value = bean.get(i).getValue();
					index++;
					stmt.setString(index, value);
					index++;
					stmt.setString(index, bean.get(i).getValue());
					index++;
					stmt.setString(index, bean.get(i).getValue());
					index++;
					stmt.setString(index, bean.get(i).getValue());
					index++;
					stmt.setString(index, bean.get(i).getValue());
					index++;
					stmt.setString(index, bean.get(i).getValue());
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
				rsTree = new OneSixExpressBean();
				rsTree.setTotal(total);
				rsTree.setEnname( rs.getString("enname"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setPrice( String.valueOf(rs.getDouble("prices")));
				rsTree.setImg(rs.getString("img"));
				rsTree.setMorder( String.valueOf(rs.getInt("morder")));
				rsTree.setSold(String.valueOf(rs.getInt("sold")));
				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setEntype(rs.getString("entype"));
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
	/**用于搜索商品集合
	 * @return
	 */
	@Override
	public ArrayList<OneSixExpressBean> querySearchWbsite(ArrayList<SqlBean> bean) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select sql_calc_found_rows distinct name,url,price,wprice,punit,gunit,img,"
				+ "morder,sold from onesixexpress_goods where id<90001 and valid!=0 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		OneSixExpressBean rsTree = null;
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
				sb.append(" and catid3=? ");
			}else if("pid".equals(para)){
				sb.append(" and match (pvid) against (? in boolean mode)");
			}else if("price".equals(para)){
				sb.append(" order by ").append("price").append(" "+bean.get(i).getValue());
			}else if("solder".equals(para)){
				sb.append(" order by ").append("sold").append(" "+bean.get(i).getValue());
			}else if("minq".equals(para)){
				sb.append(" and ").append("morder ").append("> ").append("?");
			}else if("maxq".equals(para)){
				sb.append(" and ").append("morder ").append("< ").append("?");
			}else if("price1".equals(para)){
				sb.append(" and ").append("price ").append("> ").append("?");
			}else if("price2".equals(para)){
				sb.append(" and ").append("price ").append("< ").append("?");
			}else if("page".equals(para)){
				sb.append(" limit ").append("?").append(",40 ");
			}
		}
		try {
			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sb.toString());
			StringBuffer wd = new StringBuffer();
			for(int i=0;i<bean_num;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(i<name_index){
						wd.append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, wd.toString());
					}
				}else if("cid".equals(para)){
//					String value = bean.get(i).getValue();
//					index++;
//					stmt.setString(index, value);
					index++;
					stmt.setString(index, bean.get(i).getValue());
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
				rsTree = new OneSixExpressBean();
				rsTree.setTotal(total);
				rsTree.setName( rs.getString("name"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setPrice( String.valueOf(rs.getDouble("price")));
				rsTree.setImg(rs.getString("img"));
				rsTree.setMorder( String.valueOf(rs.getInt("morder")));
				rsTree.setSold(String.valueOf(rs.getInt("sold")));
				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setPunit(rs.getString("punit"));
				rsTree.setWprice(rs.getString("wprice"));
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
	public ArrayList<OneSixExpressBean> queryCate(ArrayList<SqlBean> bean,int catid_index) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select distinct catid1,catid2,catid3,catid4,catid5,catid6 from onesixexpress_goods where  valid!=0 and id<90001  ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		String para = null;
		int bean_num = bean.size();
		int name_index = 0;
		int index = 0;
		for(int i=0;i<bean_num;i++){
			para = bean.get(i).getPara();
			if("name".equals(para)){
				if(name_index==0){
					sb.append("and match (enname) AGAINST (? IN BOOLEAN MODE)");
				}
				name_index++;
			}
		}
		if(catid_index!=0){
			sb.append(" group by catid" + catid_index);
		}
		sb.append(" order by catid6 desc");
		
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
				}
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setCatid1( rs.getString("catid1"));
				rsTree.setCatid2(rs.getString("catid2"));
				rsTree.setCatid3( rs.getString("catid3"));
				rsTree.setCatid4(rs.getString("catid4"));
				rsTree.setCatid5(rs.getString("catid5"));
				rsTree.setCatid6(rs.getString("catid6"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public OneSixExpressBean queryCateParent(String catid) {
		String sql = "select catid1,catid2,catid3,catid4,catid5,catid6 from onesixexpress_goods where catid1=? or catid2=? or catid3=? "
				+ " or catid4=? or catid5=? or catid6=? limit 0,1 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			stmt.setString(2, catid);
			stmt.setString(3, catid);
			stmt.setString(4, catid);
			stmt.setString(5, catid);
			stmt.setString(6, catid);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setCatid1( rs.getString("catid1"));
				rsTree.setCatid2(rs.getString("catid2"));
				rsTree.setCatid3( rs.getString("catid3"));
				rsTree.setCatid4(rs.getString("catid4"));
				rsTree.setCatid5(rs.getString("catid5"));
				rsTree.setCatid6(rs.getString("catid6"));
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
		return rsTree;
	}
	@Override
	public ArrayList<OneSixExpressBean> queryStore(String sid,int page) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select sql_calc_found_rows distinct enname,url,price,wprice,punit,gunit,img,"
				+ "morder,sold from onesixexpress_goods where  id<90001 and valid!=0 and sid=? limit ?,40";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;
		OneSixExpressBean rsTree = null;
		int total = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sid);
			stmt.setInt(2, (page-1)*40);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			while(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setTotal(total);
				rsTree.setEnname( rs.getString("enname"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setPrice( String.valueOf(rs.getDouble("price")));
				rsTree.setImg(rs.getString("img"));
				rsTree.setMorder( String.valueOf(rs.getInt("morder")));
				rsTree.setSold(String.valueOf(rs.getInt("sold")));
				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setPunit(rs.getString("punit"));
				rsTree.setWprice(rs.getString("wprice"));
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
		return list;
	}
	@Override
	public OneSixExpressBean queryGoods(String url,String pid) {
		String sql = "select distinct createtime,catid1,catid2,catid3,catid4,catid5,catid6,valid,enname,url,wprices,punit,gunit,imgs,imgsize,"
				+ "morder,sold,endetail,entype,info,weight,pid,sid,feeprice,fprice,posttime,method from "
				+ "onesixexpress_goods where id<90001 and url=? ";
		if(pid!=null&&!pid.isEmpty()){
			sql = "select distinct createtime,catid1,catid2,catid3,catid4,catid5,catid6,valid,enname,url,wprices,punit,gunit,imgs,imgsize,"
					+ "morder,sold,endetail,entype,info,weight,pid,sid,feeprice,fprice,posttime,method from "
					+ "onesixexpress_goods where id<90001 and pid=?  ";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sql);
			if(pid!=null&&!pid.isEmpty()){
				stmt.setString(1, pid);
			}else{
				stmt.setString(1, url);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setEnname( rs.getString("enname"));
				rsTree.setUrl(rs.getString("url"));
				rsTree.setImgs(rs.getString("imgs"));
				rsTree.setImgsize(rs.getString("imgsize"));
				rsTree.setMorder( String.valueOf(rs.getInt("morder")));
				rsTree.setSold(String.valueOf(rs.getInt("sold")));
				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setPunit(rs.getString("punit"));
				rsTree.setWprice(rs.getString("wprices"));
				rsTree.setEndetail(rs.getString("endetail"));
				rsTree.setEntype(rs.getString("entype"));
				rsTree.setInfo(rs.getString("info"));
				rsTree.setWeight(rs.getString("weight"));
				rsTree.setPid(rs.getString("pid"));
				rsTree.setSid(rs.getString("sid"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setCatid1(rs.getString("catid1"));
				rsTree.setCatid2(rs.getString("catid2"));
				rsTree.setCatid3(rs.getString("catid3"));
				rsTree.setCatid4(rs.getString("catid4"));
				rsTree.setCatid5(rs.getString("catid5"));
				rsTree.setCatid6(rs.getString("catid6"));
				rsTree.setFeeprice(rs.getString("feeprice"));
				rsTree.setFprice(rs.getString("fprice"));
				rsTree.setPosttime(rs.getString("posttime"));
				rsTree.setMethod(rs.getString("method"));
				rsTree.setCreatetime(rs.getString("createtime"));
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
		return rsTree;
	}
	
	public ArrayList<OneSixExpressBean> getAllData(int valid) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select * from onesixexpress_goods_copy where id<90646";//90647
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
			if(valid!=0){
				sql +=" and valid=?";
			}else{
				sql +=" and valid!=0";
			}
			stmt = conn.prepareStatement(sql);
			if(valid!=0){
				stmt.setInt(1, valid);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setUrl(rs.getString("url"));
				rsTree.setId(rs.getInt("id"));
				rsTree.setName( rs.getString("name"));
				rsTree.setEnname( rs.getString("enname"));
				rsTree.setDetail(rs.getString("detail"));
				rsTree.setEndetail(rs.getString("endetail"));
				rsTree.setType(rs.getString("type"));
				rsTree.setEntype(rs.getString("entype"));
				rsTree.setSold(rs.getString("sold"));
				rsTree.setMorder(rs.getString("morder"));
				rsTree.setPrice(rs.getString("price"));
				rsTree.setImg(rs.getString("img"));
				rsTree.setCatid1(rs.getString("catid1"));
				rsTree.setCatid2(rs.getString("catid2"));
				rsTree.setCatid3(rs.getString("catid3"));
				rsTree.setCatid4(rs.getString("catid4"));
				rsTree.setCatid5(rs.getString("catid5"));
				rsTree.setCatid6(rs.getString("catid6"));
				rsTree.setPvid(rs.getString("pvids"));
				rsTree.setSid(rs.getString("sid"));
				rsTree.setWprice(rs.getString("wprice"));
				rsTree.setImgs(rs.getString("imgs"));
				rsTree.setInfo(rs.getString("info"));
				rsTree.setWeight(rs.getString("weight"));
				rsTree.setPid(rs.getString("pid"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setPunit(rs.getString("punit"));
				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setType(rs.getString("type"));
				rsTree.setCreatetime(rs.getString("createtime"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	//以下用于数据校正
	public ArrayList<OneSixExpressBean> queryImg(int id1,int id2) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select id,url,img,imgs,imgsize,info from onesixexpress_goods where id>?";//90647
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		if(id2!=0&&id2>id1){
			sql = sql+" and id<?";
		}
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id1);
			if(id2!=0&&id2>id1){
				stmt.setInt(2, id2);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setUrl(rs.getString("url"));
				rsTree.setId(rs.getInt("id"));
//				rsTree.setName( rs.getString("name"));
//				rsTree.setEnname( rs.getString("enname"));
//				rsTree.setSold(rs.getString("sold"));
//				rsTree.setMorder(rs.getString("morder"));
//				rsTree.setPrice(rs.getString("price"));
				rsTree.setImg(rs.getString("img"));
//				rsTree.setCatid1(rs.getString("catid1"));
//				rsTree.setCatid2(rs.getString("catid2"));
//				rsTree.setPvid(rs.getString("pvids"));
//				rsTree.setSame(rs.getString("same"));
//				rsTree.setSid(rs.getString("sid"));
//				rsTree.setSimilar(rs.getString("similar"));
//				rsTree.setWprice(rs.getString("wprice"));
				rsTree.setImgsize(rs.getString("imgsize"));
				rsTree.setImgs(rs.getString("imgs"));
//				rsTree.setEndetail(rs.getString("endetail"));
//				rsTree.setEntype(rs.getString("entype"));
				rsTree.setInfo(rs.getString("info"));
//				rsTree.setWeight(rs.getString("weight"));
//				rsTree.setPid(rs.getString("pid"));
//				rsTree.setValid(rs.getInt("valid"));
//				rsTree.setPunit(rs.getString("punit"));
//				rsTree.setGunit(rs.getString("gunit"));
//				rsTree.setDetail(rs.getString("detail"));
//				rsTree.setType(rs.getString("type"));
//				rsTree.setCreatetime(rs.getString("createtime"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public ArrayList<OneSixExpressBean> queryCatid() {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select catid3 from onesixexpress_goods where id<90001 group by catid3 ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
//				rsTree.setUrl(rs.getString("url"));
//				rsTree.setName( rs.getString("name"));
//				rsTree.setEnname( rs.getString("enname"));
//				rsTree.setSold(rs.getString("sold"));
//				rsTree.setMorder(rs.getString("morder"));
//				rsTree.setPrice(rs.getString("price"));
//				rsTree.setImg(rs.getString("img"));
				rsTree.setCatid3(rs.getString("catid3"));
//				rsTree.setCatid2(rs.getString("catid2"));
//				rsTree.setPvid(rs.getString("pvids"));
//				rsTree.setSame(rs.getString("same"));
//				rsTree.setSid(rs.getString("sid"));
//				rsTree.setSimilar(rs.getString("similar"));
//				rsTree.setWprice(rs.getString("wprice"));
//				rsTree.setImgsize(rs.getString("imgsize"));
//				rsTree.setImgs(rs.getString("imgs"));
//				rsTree.setEndetail(rs.getString("endetail"));
//				rsTree.setEntype(rs.getString("entype"));
//				rsTree.setInfo(rs.getString("info"));
//				rsTree.setWeight(rs.getString("weight"));
//				rsTree.setPid(rs.getString("pid"));
//				rsTree.setValid(rs.getInt("valid"));
//				rsTree.setPunit(rs.getString("punit"));
//				rsTree.setGunit(rs.getString("gunit"));
//				rsTree.setDetail(rs.getString("detail"));
//				rsTree.setType(rs.getString("type"));
//				rsTree.setCreatetime(rs.getString("createtime"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public ArrayList<OneSixExpressBean> queryweight(String catid1) {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select weight,catid3 from onesixexpress_goods  where id<90001 and catid3=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid1);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
//				rsTree.setUrl(rs.getString("url"));
//				rsTree.setName( rs.getString("name"));
//				rsTree.setEnname( rs.getString("enname"));
//				rsTree.setSold(rs.getString("sold"));
//				rsTree.setMorder(rs.getString("morder"));
//				rsTree.setPrice(rs.getString("price"));
//				rsTree.setImg(rs.getString("img"));
				rsTree.setCatid3(rs.getString("catid3"));
//				rsTree.setCatid2(rs.getString("catid2"));
//				rsTree.setPvid(rs.getString("pvids"));
//				rsTree.setSame(rs.getString("same"));
//				rsTree.setSid(rs.getString("sid"));
//				rsTree.setSimilar(rs.getString("similar"));
//				rsTree.setWprice(rs.getString("wprice"));
//				rsTree.setImgsize(rs.getString("imgsize"));
//				rsTree.setImgs(rs.getString("imgs"));
//				rsTree.setEndetail(rs.getString("endetail"));
//				rsTree.setEntype(rs.getString("entype"));
//				rsTree.setInfo(rs.getString("info"));
				rsTree.setWeight(rs.getString("weight"));
//				rsTree.setPid(rs.getString("pid"));
//				rsTree.setValid(rs.getInt("valid"));
//				rsTree.setPunit(rs.getString("punit"));
//				rsTree.setGunit(rs.getString("gunit"));
//				rsTree.setDetail(rs.getString("detail"));
//				rsTree.setType(rs.getString("type"));
//				rsTree.setCreatetime(rs.getString("createtime"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	public ArrayList<OneSixExpressBean> queryTest() {
		ArrayList<OneSixExpressBean> list = new ArrayList<OneSixExpressBean>();
		String sql = "select * from onesixexpress_goods where id < 90646";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		OneSixExpressBean rsTree = null;
		try {
//			System.out.println(sb.toString());
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new OneSixExpressBean();
				rsTree.setUrl(rs.getString("url"));
				rsTree.setName( rs.getString("name"));
//				rsTree.setEnname( rs.getString("enname"));
//				rsTree.setSold(rs.getString("sold"));
//				rsTree.setMorder(rs.getString("morder"));
				rsTree.setPrice(rs.getString("price"));
//				rsTree.setImg(rs.getString("img"));
//				rsTree.setCatid1(rs.getString("catid1"));
//				rsTree.setCatid2(rs.getString("catid2"));
//				rsTree.setCatid3(rs.getString("catid3"));
				rsTree.setPvid(rs.getString("pvids"));
//				rsTree.setSame(rs.getString("same"));
//				rsTree.setSid(rs.getString("sid"));
//				rsTree.setSimilar(rs.getString("similar"));
				rsTree.setWprice(rs.getString("wprice"));
//				rsTree.setImgsize(rs.getString("imgsize"));
//				rsTree.setImgs(rs.getString("imgs"));
//				rsTree.setEndetail(rs.getString("endetail"));
//				rsTree.setEntype(rs.getString("entype"));
//				rsTree.setInfo(rs.getString("info"));
//				rsTree.setWeight(rs.getString("weight"));
//				rsTree.setPid(rs.getString("pid"));
//				rsTree.setValid(rs.getInt("valid"));
//				rsTree.setPunit(rs.getString("punit"));
//				rsTree.setGunit(rs.getString("gunit"));
				rsTree.setDetail(rs.getString("detail"));
//				rsTree.setType(rs.getString("type"));
//				rsTree.setCreatetime(rs.getString("createtime"));
				rsTree.setFeeprice(rs.getString("feeprice"));
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	
	public int updateTest(String value,String url,String param){
		int result = 0;
		String sql = "update onesixexpress_goods set "+param+"=? where url=?";
//		System.out.println(sql);
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, value);
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
	
	public int updateCat(String value,String param){
		int result = 0;
		String sql = "update onesixexpress_goods set catid3=? where catid1=? or catid2=?";
//		System.out.println(sql);
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, value);
			stmt.setString(2, param);
			stmt.setString(3, param);
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
	/**
	 * @param value
	 * @param param
	 * @return
	 */
	public int updatePrice(String price,String wprice,String feeprice,String fprice,String posttime,String method,String url){
		int result = 0;
//		String sql = "update onesixexpress_goods set prices=?,wprices=?,feeprice=?,fprice=?,posttime=?,method=?  where url=?";
		String sql = "update onesixexpress_goods set prices=?,wprices=?,fprice=?  where url=?";
//		System.out.println(sql);
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, Double.valueOf(price));
			stmt.setString(2, wprice);
//			stmt.setDouble(3, Double.valueOf(feeprice));
			stmt.setDouble(3, Double.valueOf(fprice));
//			stmt.setString(5, posttime);
//			stmt.setString(6, method);
			stmt.setString(4, url);
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

}
