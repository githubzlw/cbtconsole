package com.cbt.dao.impl;

import com.cbt.dao.KeyWordSearchDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Cache;
import com.cbt.util.Utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWordSearchDaoImpl implements KeyWordSearchDao{

	@Override
	public List<Map<String, Object>> searchByKeyWords(String keywords, String title) throws Exception {
		// TODO Auto-generated method stub
		String goodsSql="select * from goodsinfo as a  "+
				        " where (match(title) AGAINST ('+"+title+"' IN BOOLEAN MODE)  "+
				        " or match(keywords) AGAINST ('+"+keywords+"' IN BOOLEAN MODE))"+								
						" order by a.solder desc limit 0,40";	
		List<Map<String, Object>> list=search(goodsSql);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> searchGoodsByCache(String keywords, String price1,String price2,String minq,String maxq,
			   String sort,String cid,String page,String pid) throws Exception{
		if(cid!=null&&"0".equals(cid)){
			cid = "";
		}
		List<Map<String, Object>> searchResult=null;
		List<Map<String, Object>> result=Cache.get(keywords);
		if(result != null && result.size()>0){
				StringBuilder sb=new StringBuilder();
				for(Map<String, Object> map : result){
					int id=Integer.parseInt(Utility.formatObject(map.get("id")));
					sb.append(id+",");
				}
				
				String ids=sb.toString().substring(0, sb.toString().length()-1);
				
				String sql="select * from goodsinfo as a where a.id in("+ids+") ";
				
				if(price1 != null && !"".equals(price1)){
					sql=sql+" and price1>="+price1;
				}
				
				if(price2 != null && !"".equals(price2)){
					sql=sql+" and price1<="+price2;
				}
				
				if(minq != null && !"".equals(minq)){
					sql=sql+" and morder>="+minq;
				}
				
				if(maxq != null && !"".equals(maxq)){
					sql=sql+" and morder<="+maxq;
				}
				
				if(cid != null && !"".equals(cid)){
					sql=sql+" and typeid="+cid;
				}
				
				sql=sql+" limit "+((Integer.parseInt(page)-1)*40)+",40";
				searchResult=search(sql);
				return searchResult;
		}	
		return searchResult;
	}

	@Override
	public List<Map<String, Object>> search(String sql) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		Map<String, Object> data = null;
		try {
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				ResultSetMetaData rsmd = stmt.getMetaData();
				int columnCount = rsmd.getColumnCount();
				while(rs.next()){
				    data = new HashMap<String, Object>();
				    for (int i = 1; i <= columnCount; i++) {
	                    data.put(rsmd.getColumnLabel(i), rs.getObject(rsmd
	                            .getColumnLabel(i)));
	                }
				    list.add(data);
				}				
		} catch (Exception e) {
			throw e;
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
	public int updateSimilarwords(String zc, String tyc, String time) throws Exception {
		// TODO Auto-generated method stub
		String sql="update similarwords set words='"+tyc+"',valid=1,time='"+time+"' where keywords='"+zc+"'";
		return update(sql);
	}

	@Override
	public int insertSimilarwords(String zc, String tyc, String time) throws Exception {
		// TODO Auto-generated method stub
		String sql="insert into similarwords(keywords,words,valid,time)value('"+zc+"','"+tyc+"',1,'"+time+"')";
		return update(sql);
	}

	@Override
	public int updateKwsynonymstb(String zc, String tablename) throws Exception {
		// TODO Auto-generated method stub
		String sql="update kwsynonymstb set tablename='"+tablename+"' where keywords='"+zc+"'";
		return update(sql);
	}

	@Override
	public int insertKwsynonymstb(String zc, String tablename) throws Exception {
		// TODO Auto-generated method stub
		String sql="insert into kwsynonymstb(keywords,tablename)value('"+zc+"','"+tablename+"')";
		return update(sql);
	}

	@Override
	public List<Map<String, Object>> selectGoodsInfo(String tyc) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder("select * from goodsinfo as a where 1=1 ");
		String[] tycs = tyc.split(",");
		for(int i=0; i<tycs.length; i++){
			if(i == 0){
				sb.append(" or ( match(title) AGAINST ('+"+tycs[i]+"' IN BOOLEAN MODE) ");
			}else{
				sb.append(" or match(title) AGAINST ('+"+tycs[i]+"' IN BOOLEAN MODE) ");
			}
			
			if(i == tycs.length-1){
				sb.append(" )");
			}
		}						
		return search(sb.toString());
	}

	@Override
	public int update(String sql) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
		} catch (Exception e) {
			throw e;
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
	public int create(String tablename) throws Exception {
		// TODO Auto-generated method stub
		String sql="CREATE TABLE `"+tablename+"` ("+
				   "`id` int(11) NOT NULL AUTO_INCREMENT,"+
				   "`price1` varchar(9) DEFAULT '0' COMMENT '价格一',"+
				   "`price2` varchar(9) DEFAULT '0',"+
				   "`morder` int(11) DEFAULT '1' COMMENT '最小订量单位',"+
				   "`sortid` varchar(255) DEFAULT NULL,"+
				   "`pid` varchar(1000) DEFAULT NULL,"+
				   "`typeid` int(11) DEFAULT NULL,"+
				   "PRIMARY KEY (`id`)"+
				   ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;";
		return update(sql);
	}

	@Override
	public void insertNewTb(List<Map<String, Object>> list, String tablename) throws Exception {
		// TODO Auto-generated method stub
		String sql="insert into "+tablename+"(price1,price2,morder,sortid,pid,typeid)value(?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			for(Map<String, Object> map : list){
				stmt.setString(1, Utility.formatObject(map.get("price1")));
				stmt.setString(2, Utility.formatObject(map.get("price2")));
				stmt.setInt(3, Integer.parseInt(Utility.formatObject(map.get("morder"))));
				stmt.setString(4, Utility.formatObject(map.get("sortid")));
				stmt.setString(5, Utility.formatObject(map.get("pid")));
				stmt.setInt(6, Integer.parseInt(Utility.formatObject(map.get("typeid"))));
				stmt.addBatch();
			}
			int[] result=stmt.executeBatch();
		} catch (Exception e) {
			throw e;
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

	@Override
	public int searchGoodsCountByCache(String keywords,
			String price1, String price2, String minq, String maxq,
			String sort, String cid, String page, String pid) throws Exception {
		// TODO Auto-generated method stub
		int count=0;
		List<Map<String, Object>> searchResult=null;
		List<Map<String, Object>> result=Cache.get(keywords);
		if(result != null && result.size()>0){
				StringBuilder sb=new StringBuilder();
				for(Map<String, Object> map : result){
					int id=Integer.parseInt(Utility.formatObject(map.get("id")));
					sb.append(id+",");
				}
				
				String ids=sb.toString().substring(0, sb.toString().length()-1);
				
				String sql="select count(*) as count from goodsinfo as a where a.id in("+ids+") ";
				
				if(price1 != null && !"".equals(price1)){
					sql=sql+" and price1>="+price1;
				}
				
				if(price2 != null && !"".equals(price2)){
					sql=sql+" and price1<="+price2;
				}
				
				if(minq != null && !"".equals(minq)){
					sql=sql+" and minq>="+minq;
				}
				
				if(maxq != null && !"".equals(maxq)){
					sql=sql+" and minq<="+maxq;
				}
				
				if(cid != null && !"".equals(cid)){
					sql=sql+" and typeid="+cid;
				}

				searchResult=search(sql);
				if(searchResult != null && searchResult.size()>0){
					count=Integer.parseInt(searchResult.get(0).get("count")+"");
				}
				return count;
		}	
		return count;
	}

}
