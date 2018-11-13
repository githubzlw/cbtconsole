package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.ISearchCacheDao;
import com.cbt.util.Cache;
import com.cbt.util.Utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCacheDao implements ISearchCacheDao {

	
	@Override
	public List<Map<String, Object>> searchGoodsByCache(String key,int param_id,int sort,String page,int sold){
		List<Map<String, Object>> searchResult=null;
		List<Map<String, Object>> result=Cache.get(key);
		try {
			if(result != null && result.size()>0){
				String table_name = "";
				for(Map<String, Object> map : result){
					int paramid=Integer.parseInt(Utility.formatObject(map.get("param_id"))) ;
					if(paramid==param_id){
						table_name=Utility.formatObject(map.get("table_name"));
					}
				}
				if((!table_name.isEmpty())&&queryTable(table_name)>0){
					String sql="select * from "+table_name +" where goods_flag=1 and seller_flag=1 "
							+ "and (online_flag=1 or (online_flag=0 and goods_sold>"+(2*sold)+"))";
					if(sort==3){
						sql=sql+" order by goods_sold desc";
					}else if(sort==1){
						sql=sql+" order by goods_price asc";
					}
					sql=sql+" limit "+((Integer.parseInt(page)-1)*40)+",40";
					System.out.println("cache-sql:"+sql);
					searchResult=search(sql);
					return searchResult;
				}
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResult;
	}
	
	@Override
	public int searchGoodsCountByCache(String key,int param_id,int sold){
		// TODO Auto-generated method stub
		int count=0;
		List<Map<String, Object>> searchResult=null;
		List<Map<String, Object>> result=Cache.get(key);
		try {
			if(result != null && result.size()>0){
				String table_name = "";
				for(Map<String, Object> map : result){
					int paramid=Integer.parseInt( Utility.formatObject(map.get("param_id"))) ;
					if(paramid==param_id){
						table_name=Utility.formatObject(map.get("table_name"));
					}
				}
				if(table_name.isEmpty()||queryTable(table_name)==0){
					return 0;
				}
				String sql="select count(*) as count from  "+table_name+" where goods_flag=1 and seller_flag=1"
						+ " and (online_flag=1 or (online_flag=0 and goods_sold>"+sold+"))";
				searchResult=search(sql);
				if(searchResult != null && searchResult.size()>0){
					count=Integer.parseInt(searchResult.get(0).get("count")+"");
				}
				return count;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public List<Map<String, Object>> search(String sql) {
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
			e.printStackTrace();;
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
	public int update(String sql) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
		} catch (Exception e) {
			e.printStackTrace();;
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
	public int create(String tableName) {
		// TODO Auto-generated method stub
		String sql="CREATE TABLE `"+tableName+"` ("+
				   "`id` int(11) NOT NULL AUTO_INCREMENT,"+
				   "`goods_name` varchar(500)  COMMENT '商品名称',"+
				   "`goods_price` varchar(255)  COMMENT '商品价格',"+
				   "`goods_img` varchar(500)  COMMENT '商品图片',"+
				   "`goods_url` varchar(500) UNIQUE  COMMENT '商品链接',"+
				   "`goods_sold` int(11) DEFAULT '0' COMMENT '销量',"+
				   "`goods_morder` int(11) DEFAULT '1' COMMENT '最小订量',"+
				   "`goods_free` int(2) DEFAULT '0' COMMENT '商品免邮',"+
				   "`online_flag` int(2)  COMMENT '店家在线  0-不在线  1-在线',"+
				   "`seller_flag` int(2)  COMMENT '店家黑名单   0-黑名单 1-白名单',"+
				   "`goods_flag` int(2)  COMMENT '商品标志  1-数据有效  0-数据无效',"+
				   "`goods_similar` int(5)  COMMENT '商品类似  0-不存在任何相似商品  ',"+
				   "`createtime` datetime  COMMENT '入库时间',"+
				   "PRIMARY KEY (`id`)"+
				   ") ENGINE=MEMORY AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		return update(sql);
	}
	@Override
	public int create2(String tableName) {
		// TODO Auto-generated method stub
		String sql="CREATE TABLE `"+tableName+"` ("+
				"`id` int(11) NOT NULL AUTO_INCREMENT,"+
				"`param_id` int(11)  COMMENT 'param的id',"+
				"`goods_name` varchar(500)  COMMENT '商品名称',"+
				"`goods_price` varchar(255)  COMMENT '商品价格',"+
				"`goods_img` varchar(500)  COMMENT '商品图片',"+
				"`goods_url` varchar(500)  COMMENT '商品链接',"+
				"`goods_sold` int(11) DEFAULT '0' COMMENT '销量',"+
				"`goods_morder` int(11) DEFAULT '1' COMMENT '最小订量',"+
				"`goods_free` int(2) DEFAULT '0' COMMENT '商品免邮',"+
				"`online_flag` int(2)  COMMENT '店家在线  0-不在线  1-在线',"+
				"`seller_flag` int(2)  COMMENT '店家黑名单   0-黑名单 1-白名单',"+
				"`goods_flag` int(2)  COMMENT '商品标志  1-数据有效  0-数据无效',"+
				"`goods_similar` int(5)  COMMENT '商品类似  0-不存在任何相似商品  ',"+
				"`createtime` datetime  COMMENT '入库时间',"+
				"PRIMARY KEY (`id`)"+
				") ENGINE=MEMORY AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		return update(sql);
	}

	@Override
	public void insertNewTb(List<Map<String, Object>> list, String tablename) {
		// TODO Auto-generated method stub
		String sql="insert into "+tablename+" (goods_name,goods_price,goods_img,goods_url,goods_sold,goods_morder"
				+ ",goods_free,online_flag,seller_flag,goods_flag,goods_similar,createtime)"
				+"values( ?,?,?,?,?,?,?,?,?,?,?,now()) ON DUPLICATE KEY UPDATE goods_url=VALUES(goods_url)";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			for(Map<String, Object> map : list){
				stmt.setString(1, Utility.formatObject(map.get("goods_name")));
				stmt.setString(2, Utility.formatObject(map.get("goods_price")));
				stmt.setString(3, Utility.formatObject(map.get("goods_img")));
				stmt.setString(4, Utility.formatObject(map.get("goods_url")));
				stmt.setInt(5, Integer.parseInt(Utility.formatObject(map.get("goods_sold"))));
				stmt.setInt(6, Integer.parseInt(Utility.formatObject(map.get("goods_morder"))));
				stmt.setInt(7, Integer.parseInt(Utility.formatObject(map.get("goods_free"))));
				stmt.setInt(8, Integer.parseInt(Utility.formatObject(map.get("online_flag"))));
				stmt.setInt(9, Integer.parseInt(Utility.formatObject(map.get("seller_flag"))));
				stmt.setInt(10, Integer.parseInt(Utility.formatObject(map.get("goods_flag"))));
				stmt.setInt(11, Integer.parseInt(Utility.formatObject(map.get("goods_similar"))));
				stmt.addBatch();
			}
			stmt.executeBatch();
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
	@Override
	public void insertNewTb2(List<Map<String, Object>> list, String tablename,String param_id) {
		// TODO Auto-generated method stub
//		String sql="insert into "+tablename+" (param_id,goods_name,goods_price,goods_img,goods_url,goods_sold,goods_morder"
//				+ ",goods_free,online_flag,seller_flag,goods_flag,goods_similar,createtime)"
//				+ "select ?,?,?,?,?,?,?,?,?,?,?,?,now() from "+tablename+" where "
//				+ "(select count(*) from "+tablename+"  where goods_url =? and param_id=?)<1 limit 0,1";
				
				
//		String sql2="update "+tablename+" set param_id=(select check_id_by_url(?,?)) where goods_url =?";*/
		
		String sql="insert into "+tablename+" (param_id,goods_name,goods_price,goods_img,goods_url,goods_sold,goods_morder"
				+ ",goods_free,online_flag,seller_flag,goods_flag,goods_similar,createtime) values(?,?,?,?,?,?,?,?,?,?,?,?,now())"
				+" ON DUPLICATE KEY UPDATE goods_url=VALUES(goods_url),param_id=VALUES(param_id) ";
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			for(Map<String, Object> map : list){
				stmt.setString(1, param_id);
				stmt.setString(2, Utility.formatObject(map.get("goods_name")));
				stmt.setString(3, Utility.formatObject(map.get("goods_price")));
				stmt.setString(4, Utility.formatObject(map.get("goods_img")));
				stmt.setString(5, Utility.formatObject(map.get("goods_url")));
				stmt.setInt(6, Integer.parseInt(Utility.formatObject(map.get("goods_sold"))));
				stmt.setInt(7, Integer.parseInt(Utility.formatObject(map.get("goods_morder"))));
				stmt.setInt(8, Integer.parseInt(Utility.formatObject(map.get("goods_free"))));
				stmt.setInt(9, Integer.parseInt(Utility.formatObject(map.get("online_flag"))));
				stmt.setInt(10, Integer.parseInt(Utility.formatObject(map.get("seller_flag"))));
				stmt.setInt(11, Integer.parseInt(Utility.formatObject(map.get("goods_flag"))));
				stmt.setInt(12, Integer.parseInt(Utility.formatObject(map.get("goods_similar"))));
				stmt.setString(13, param_id);
				stmt.setString(14, Utility.formatObject(map.get("goods_url")));
				stmt.addBatch();
			}
			stmt.executeBatch();
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

	@Override
	public int delete(String tableName) {
		String sql="DROP TABLE `"+tableName;
		return update(sql);
	}

	/**查询表是否存在
	 * @date 2016年3月1日
	 * @author abc
	 * @param tableName
	 * @return  
	 */
	@Override
	public int queryTable(String tableName) {
		String sql = "select count(TABLE_NAME) from INFORMATION_SCHEMA.TABLES where TABLE_NAME=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tableName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("count(TABLE_NAME)");
			}
		} catch (Exception e) {
			e.printStackTrace();;
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
	
	

	

}
