package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.SqlBean;
import com.cbt.parse.bean.YiWuBean;
import com.cbt.parse.daoimp.IYiWuDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class YiWuDao implements IYiWuDao {

	@Override
	public int addDate(YiWuBean bean) {
		int result = 0;
		String sql = "insert yiwu(pname,purl,sname,cname,minprice,maxprice,pdesc,morder,utime,snumber,"
				+ "img,time,pid,type,wprice,info,punit,gunit,cid)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getProductName());
			stmt.setString(2, bean.getProductUrl());
			stmt.setString(3, bean.getStoreName());
			stmt.setString(4, bean.getCategoryName());
			stmt.setDouble(5, Double.valueOf(bean.getProductMinPrice()));
			stmt.setDouble(6, Double.valueOf(bean.getProductMaxPrice()));
			stmt.setString(7, bean.getProductDesc());
			stmt.setInt(8, Integer.valueOf(bean.getMinorder()));
			stmt.setString(9, bean.getUpdateTime());
			stmt.setString(10, bean.getStoreNumber());
			stmt.setString(11, bean.getImgs());
			stmt.setString(12, TypeUtils.getTime());
			stmt.setString(13, bean.getProductId());
			stmt.setString(14, bean.getType());
			stmt.setString(15, bean.getwPrice());
			stmt.setString(16, bean.getInfo());
			stmt.setString(17, bean.getpUnit());
			stmt.setString(18, bean.getgUnit());
			stmt.setString(19, bean.getCategory_id());
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
	public int updateDate(YiWuBean bean) {
		int result = 0;
		String sql = "update yiwu set pname=?,sname=?,cname=?,minprice=?,maxprice=?,pdesc=?,"
				+ "morder=?,utime=?,snumber=?,img=?,time=?,pid=?,type=?,wprice=?,info=?,"
				+ "punit=?,gunit=?,cid=? where purl=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getProductName());
			stmt.setString(2, bean.getStoreName());
			stmt.setString(3, bean.getCategoryName());
			stmt.setDouble(4, Double.valueOf(bean.getProductMinPrice()));
			stmt.setDouble(5, Double.valueOf(bean.getProductMaxPrice()));
			stmt.setString(6, bean.getProductDesc());
			stmt.setInt(7, Integer.valueOf(bean.getMinorder()));
			stmt.setString(8, bean.getUpdateTime());
			stmt.setString(9, bean.getStoreNumber());
			stmt.setString(10, bean.getImgs());
			stmt.setString(11, TypeUtils.getTime());
			stmt.setString(12, bean.getProductId());
			stmt.setString(13, bean.getType());
			stmt.setString(14, bean.getwPrice());
			stmt.setString(15, bean.getInfo());
			stmt.setString(16, bean.getpUnit());
			stmt.setString(17, bean.getgUnit());
			stmt.setString(18, bean.getCategory_id());
			stmt.setString(19, bean.getProductUrl());
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
	public int updateValid(String url,int valid) {
		int result = 0;
		String sql = "update yiwu set time=?,valid=? where purl=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, TypeUtils.getTime());
			stmt.setInt(2, valid);
			stmt.setString(3, url);
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
	public ArrayList<YiWuBean> querry(String url, String pid) {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		String sql = "select img,morder,pdesc,pname,purl,sname,"
				+ "snumber,pid,valid,type,info,wprice,punit,gunit from yiwu where purl=?";
		if(pid!=null&&!pid.isEmpty()){
			sql = "select img,morder,pdesc,pname,purl,sname,"
					+ "snumber,pid,valid,type,info,wprice,punit,gunit from yiwu where pid=?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(url!=null&&!url.isEmpty()){
				stmt.setString(1, url);
			}else if(pid!=null&&!pid.isEmpty()){
				stmt.setString(1, pid);
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setImgs(rs.getString("img"));
				rsTree.setMinorder(String.valueOf(rs.getInt("morder")));
				rsTree.setProductDesc(rs.getString("pdesc"));
				rsTree.setProductName(rs.getString("pname"));
				rsTree.setProductUrl(rs.getString("purl"));
				rsTree.setStoreName(rs.getString("sname"));
				rsTree.setStoreNumber(rs.getString("snumber"));
				rsTree.setProductId(rs.getString("pid"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setType(rs.getString("type"));
				rsTree.setInfo(rs.getString("info"));
				rsTree.setwPrice(rs.getString("wprice"));
				rsTree.setpUnit(rs.getString("punit"));
				rsTree.setgUnit(rs.getString("gunit"));
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
	@Override
	public ArrayList<YiWuBean> querryStore(String sid) {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		String sql = "select pname,img,minprice,maxprice,morder,purl,valid,gunit from yiwu where snumber=? and valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sid);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setImgs(rs.getString("img"));
				rsTree.setMinorder(String.valueOf(rs.getInt("morder")));
				rsTree.setProductName(rs.getString("pname"));
				rsTree.setProductMinPrice(String.valueOf(rs.getDouble("minprice")));
				rsTree.setProductMaxPrice(String.valueOf(rs.getDouble("maxprice")));
				rsTree.setProductUrl(rs.getString("purl"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setgUnit(rs.getString("gunit"));
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
	@Override
	public ArrayList<YiWuBean> querrySQL(ArrayList<SqlBean> bean) {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("select pname,img,minprice,maxprice,morder,purl,valid,gunit from yiwu where ");
		int count = bean!=null?bean.size():0;
		String para = null;
		int name_index = 0;
		int cid_index = 0;
		for(int i=0;i<count;i++){
			para = bean.get(i).getPara();
			if("name".equals(para)){
				if(name_index==0){
					sb.append("match").append("(pname) AGAINST (").append("?")
					.append(" IN BOOLEAN MODE) ");
				}
				name_index++;
			}else if("cid".equals(para)){
				 String[] split = bean.get(i).getValue().split(",");
				 cid_index = split.length;
				 for(int j=0;j<cid_index;j++){
					 if(i!=0&&j==0){
						 sb.append(" and ");
						 if(cid_index>1){
							 sb.append(" ( ");
						 }
					 }
					 if(j!=0){
						 sb.append(" or ");
					 }
					 sb.append(" cid=?");
					 if(cid_index>1&&j==cid_index-1){
						 sb.append(" ) ");
					 }
					 
				 }
				
			}else if("cname".equals(para)){
				if(i!=0){
					sb.append(" and ");
				}
				sb.append("cname=?");
			}else if("price".equals(para)){
				sb.append(" order by ").append("minprice").append(" "+bean.get(i).getValue());
			}else if("solder".equals(para)){
				sb.append(" order by ").append("sold").append(" "+bean.get(i).getValue());
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
//		sb.append(" group by snumber ");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			String sql = sb.toString();
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			sb.delete(0, sb.length());
			int index=0;
			for(int i=0;i<count;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(i<name_index){
						sb.append(" +").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, "'"+sb.toString()+"'");
					}
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
				}else if("cid".equals(para)){
					 String[] split = bean.get(i).getValue().split(",");
					 cid_index = split.length;
					 for(int j=0;j<cid_index;j++){
						 index++;
						 stmt.setString(index,split[j]);
					 }
					
				}else if("cname".equals(para)){
					index++;
					stmt.setString(index,bean.get(i).getValue());
				}
			}
			
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setImgs(rs.getString("img"));
				rsTree.setMinorder(String.valueOf(rs.getInt("morder")));
				rsTree.setProductName(rs.getString("pname"));
				rsTree.setProductMinPrice(String.valueOf(rs.getDouble("minprice")));
				rsTree.setProductMaxPrice(String.valueOf(rs.getDouble("maxprice")));
				rsTree.setProductUrl(rs.getString("purl"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setgUnit(rs.getString("gunit"));
				list.add(rsTree);
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
	public ArrayList<YiWuBean> querryAll(ArrayList<SqlBean> bean, String aid, String pid) {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("select pname,img,minprice,maxprice,morder,purl,valid,gunit from yiwu where valid=1 and ");
		int count = bean!=null?bean.size():0;
		String para = null;
		int name_index = 0;
		if(bean!=null&&!bean.isEmpty()){
			for(int i=0;i<count;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(name_index==0){
						sb.append("match").append("(pname) AGAINST (").append("?")
						.append(" IN BOOLEAN MODE) ");
					}
					name_index++;
				}
			}
			if(aid!=null&&!aid.isEmpty()){
				if(pid!=null&&!pid.isEmpty()){
					sb.append(" and ((cnl=? or cnm=?) and cns like ?) limit 0,80");
				}else{
					sb.append(" and (cnl=? or cnm=? or cns like ?) limit 0,80");
				}
			}
		}else{
			if(aid!=null&&!aid.isEmpty()){
				if(pid!=null&&!pid.isEmpty()){
					sb.append(" (cnl=? or cnm=?) and cns like ? limit 0,80");
				}else{
					sb.append(" cnl=? or cnm=? or cns like ? limit 0,80");
				}
			}
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			String sql = sb.toString();
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			sb.delete(0, sb.length());
			if(bean!=null&&!bean.isEmpty()){
				for(int i=0;i<count;i++){
					para = bean.get(i).getPara();
					if("name".equals(para)){
						if(i<name_index){
							sb.append(" +").append(bean.get(i).getValue());
						}
						if(i==name_index-1){
							stmt.setString(1, "'"+sb.toString()+"'");
						}
					}
				}
				if(aid!=null&&!aid.isEmpty()){
					stmt.setString(2,aid);
					stmt.setString(3,aid);
					if(pid!=null&&!pid.isEmpty()){
						stmt.setString(4,"%"+pid+"%");
					}else{
						stmt.setString(4,"%"+aid+"%");
					}
				}
				
			}else{
				if(aid!=null&&!aid.isEmpty()){
					stmt.setString(1,aid);
					stmt.setString(2,aid);
					if(pid!=null&&!pid.isEmpty()){
						stmt.setString(3,"%"+pid+"%");
					}else{
						stmt.setString(3,"%"+aid+"%");
					}
				}
			}
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setImgs(rs.getString("img"));
				rsTree.setMinorder(String.valueOf(rs.getInt("morder")));
				rsTree.setProductName(rs.getString("pname"));
				rsTree.setProductMinPrice(String.valueOf(rs.getDouble("minprice")));
				rsTree.setProductMaxPrice(String.valueOf(rs.getDouble("maxprice")));
				rsTree.setProductUrl(rs.getString("purl"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setgUnit(rs.getString("gunit"));
				list.add(rsTree);
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
	
	/**去除反关键字
	 * @param bean
	 * @return
	 */
	@Override
	public ArrayList<YiWuBean> querryFilter(ArrayList<SqlBean> bean) {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("select ctitle,pname,img,minprice,maxprice,morder,purl,valid,gunit from yiwu where ");
		int count = bean!=null?bean.size():0;
		String para = null;
		int name_index = 0;
//		int cid_index = 0;
		for(int i=0;i<count;i++){
			para = bean.get(i).getPara();
			if("name".equals(para)){
				if(name_index==0){
					sb.append("match").append("(pname) AGAINST (").append("?")
					.append(" IN BOOLEAN MODE) ");
				}
				name_index++;
			}else if("fkey".equals(para)){
				name_index++;
			}
			/*
			else if("cid".equals(para)){
				String[] split = bean.get(i).getValue().split(",");
				cid_index = split.length;
				for(int j=0;j<cid_index;j++){
					if(i!=0&&j==0){
						sb.append(" and ");
						if(cid_index>1){
							sb.append(" ( ");
						}
					}
					if(j!=0){
						sb.append(" or ");
					}
					sb.append(" cid=?");
					if(cid_index>1&&j==cid_index-1){
						sb.append(" ) ");
					}
				}
			}else if("cname".equals(para)){
				if(i!=0){
					sb.append(" and ");
				}
				sb.append("cname=?");
			}else if("price".equals(para)){
				sb.append(" order by ").append("minprice").append(" "+bean.get(i).getValue());
			}else if("solder".equals(para)){
				sb.append(" order by ").append("sold").append(" "+bean.get(i).getValue());
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
			*/
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			String sql = sb.toString();
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			sb.delete(0, sb.length());
			int index=0;
			for(int i=0;i<count;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(i<name_index){
						sb.append(" +").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, "'"+sb.toString()+"'");
					}
				}else if("fkey".equals(para)){
					if(i<name_index){
						sb.append(" -").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, "'"+sb.toString()+"'");
					}
					
				}
				/*
				else if("minq".equals(para)){
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
				}else if("cid".equals(para)){
					String[] split = bean.get(i).getValue().split(",");
					cid_index = split.length;
					for(int j=0;j<cid_index;j++){
						index++;
						stmt.setString(index,split[j]);
					}
					
				}else if("cname".equals(para)){
					index++;
					stmt.setString(index,bean.get(i).getValue());
				}
				*/
			}
			
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setImgs(rs.getString("img"));
				rsTree.setMinorder(String.valueOf(rs.getInt("morder")));
				rsTree.setProductName(rs.getString("ctitle")+"--"+rs.getString("pname"));
				rsTree.setProductMinPrice(String.valueOf(rs.getDouble("minprice")));
				rsTree.setProductMaxPrice(String.valueOf(rs.getDouble("maxprice")));
				rsTree.setProductUrl(rs.getString("purl"));
				rsTree.setValid(rs.getInt("valid"));
				rsTree.setgUnit(rs.getString("gunit"));
				list.add(rsTree);
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
	
	
	/**更改商品类别  对应于aliexpress的类别
	 * @param cnl
	 * @param cnm
	 * @param cns
	 * @param bean
	 * @return
	 */
	@Override
	public int updateC(String cnl,String cnm,String cns,ArrayList<SqlBean> bean){
		StringBuilder sb = new StringBuilder();
		sb.append("update yiwu set cnl=?,cnm=?,cns=CONCAT_WS(',',cns,?) where ");
		int count = bean!=null?bean.size():0;
		String para = null;
		int name_index = 0;
		for(int i=0;i<count;i++){
			para = bean.get(i).getPara();
			if("name".equals(para)){
				if(name_index==0){
					sb.append("match").append("(pname) AGAINST (").append("?")
					.append(" IN BOOLEAN MODE) ");
				}
				name_index++;
			}else if("fkey".equals(para)){
				name_index++;
			}
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			String sql = sb.toString();
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			sb.delete(0, sb.length());
			stmt.setString(1, cnl);
			stmt.setString(2, cnm);
			stmt.setString(3, cns);
			int index=3;
			for(int i=0;i<count;i++){
				para = bean.get(i).getPara();
				if("name".equals(para)){
					if(i<name_index){
						sb.append(" +").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, "'"+sb.toString()+"'");
					}
				}else if("fkey".equals(para)){
					if(i<name_index){
						sb.append(" -").append(bean.get(i).getValue());
					}
					if(i==name_index-1){
						index++;
						stmt.setString(index, "'"+sb.toString()+"'");
					}
				}
			}
			rs = stmt.executeUpdate();
			
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
		return rs;
	}
	
	/************************************以下查询函数正式库上不需要使用的*********************/
	
	public ArrayList<YiWuBean> querry() {
		ArrayList<YiWuBean> list = new ArrayList<YiWuBean>();
		String sql = "select purl from yiwu where id> 200000";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YiWuBean rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new YiWuBean();
				rsTree.setProductUrl(rs.getString("purl"));
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
	
	public int update(String url,String title,String cstore) {
		int result = 0;
		String sql = "update yiwu set ctitle=?,cstore=? where purl=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, title);
			stmt.setString(2, cstore);
			stmt.setString(3, url);
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
	
	
	
	
	
}
