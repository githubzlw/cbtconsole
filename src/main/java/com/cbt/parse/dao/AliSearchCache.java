package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.daoimp.IAliSearchCache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AliSearchCache implements IAliSearchCache {

	@Override
	public int addParam(String keyword,String catid,int sort) {
		int result = 0;
		String sql="insert ali_search_cache_param (param_key,param_catid,"
				+ "param_sort,createtime) values(?,?,?,now());";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, catid);
			stmt.setInt(3, sort);
			stmt.executeUpdate();
			stmt2 = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			rs = stmt2.executeQuery();
			if(rs.next()){
				result = rs.getInt("LAST_INSERT_ID()");
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
			if(stmt2!=null){
				try {
					stmt2.close();
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

	@Override
	public int updateParam(String keyword,String catid,int sort,int param_id) {
		int result = 0;
		String sql="update ali_search_cache_param set param_key=?,param_catid=?,"
				+ "param_sort=?,createtime=now()  where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, catid);
			stmt.setInt(3, sort);
			stmt.setInt(4, param_id);
			stmt.executeUpdate();
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
	public int getParamId(String keyword,String catid,int sort) {
		int result = 0;
		String sql="select id from ali_search_cache_param where param_key=? and param_catid=? "
				+ "and param_sort=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		 ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, catid);
			stmt.setInt(3, sort);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("id");
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

	@Override
	public int addData(ArrayList<SearchGoods> list, int param_id) {
		int result = 0;
		String sql="insert into ali_search_cache_datas (param_id,goods_name,goods_price,goods_img,goods_url,"
				+ "goods_sold,goods_morder,goods_free,online_flag,seller_flag,goods_similar,createtime) "
				+ "select ?,?,?,?,?,?,?,?,?,?,?,now() from ali_search_cache_datas where "
				+ "(select count(*) from ali_search_cache_datas  where goods_url =?)<1 limit 0,1";
		String sql2="update ali_search_cache_datas set param_id=(select check_id_by_url(?,?)) where goods_url =?";
		
		String sql3="select count(*) from ali_search_cache_datas where match(param_id) against(? in boolean mode) "
				+ "and goods_flag=1 and seller_flag=1";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			stmt3 = conn.prepareStatement(sql3);
			stmt3.setString(1, "'"+param_id+"'");
			int list_size = list.size();
			for(int i=0;i<list_size;i++){
				SearchGoods bean = list.get(i);
				stmt.setString(1, String.valueOf(param_id));
				stmt.setString(2, bean.getGoods_name());
				stmt.setString(3, bean.getGoods_price());
				stmt.setString(4, bean.getGoods_image());
				stmt.setString(5, bean.getGoods_url());
				stmt.setInt(6, Integer.parseInt(bean.getGoods_solder()));
				stmt.setInt(7, Integer.parseInt(bean.getGoods_minOrder()));
				stmt.setInt(8, Integer.parseInt(bean.getGoods_free()));
				stmt.setInt(9, Integer.parseInt(bean.getSeller_online()));
				stmt.setInt(10,bean.getSeller_flag());
				stmt.setInt(11, Integer.parseInt(bean.getGoods_similar()));
				stmt.setString(12, bean.getGoods_url());
				stmt.addBatch();
				stmt2.setString(1, bean.getGoods_url());
				stmt2.setString(2, String.valueOf(param_id));
				stmt2.setString(3, bean.getGoods_url());
				stmt2.addBatch();
			}
			stmt.executeBatch();
			stmt2.executeBatch();
			rs = stmt3.executeQuery();
			if(rs.next()){
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
			if(stmt2!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt3!=null){
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
	@Override
	public int updateData(ArrayList<SearchGoods> list) {
		int result = 0;
		String sql="update ali_search_cache_datas set goods_name=?,goods_price=?,"
				+ "goods_sold=?,goods_morder=?,goods_flag=?,createtime=now()  where goods_url =?";
		
//		String sql2="delete  from  table where 字段1=? (...and 字段n=?)";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
//		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
//			stmt2 = conn.prepareStatement(sql2);
			int list_size = list.size();
			for(int i=0;i<list_size;i++){
				SearchGoods bean = list.get(i);
				stmt.setString(1, bean.getGoods_name());
				stmt.setString(2, bean.getGoods_price());
				stmt.setInt(3, Integer.parseInt(bean.getGoods_solder()));
				stmt.setInt(4, Integer.parseInt(bean.getGoods_minOrder()));
				stmt.setInt(5, bean.getValid());
				stmt.setString(6, bean.getGoods_url());
				stmt.addBatch();
			}
			stmt.executeBatch();
//			stmt2.executeUpdate();
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
			/*if(stmt2!=null){
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return result;
	}

	@Override
	public ArrayList<SearchGoods> getDatas(String param_id,int sort,int sold,String page) {
		ArrayList<SearchGoods>  list = new ArrayList<SearchGoods>();
		SearchGoods sg = null;
		String sql="select sql_calc_found_rows distinct *from ali_search_cache_datas where match(param_id) AGAINST (? IN BOOLEAN MODE) and "
				+ "goods_flag=1 and seller_flag=1 and (online_flag=1 or (online_flag=0 and goods_sold>?))";
		if(sort==3){
			sql +=" order by goods_sold desc";
		}else if(sort==1){
			sql +=" order by goods_price asc";
		}
		sql=sql+" limit "+((Integer.parseInt(page)-1)*40)+",40";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement("select found_rows();");
			stmt.setString(1, "+"+param_id);
			stmt.setInt(2, 2*sold);
			rs = stmt.executeQuery();
			rs2 = stmt2.executeQuery();
			int total = 0 ;
			if(rs2.next()){
				total = rs2.getInt("found_rows()");
			}
			while(rs.next()){
				sg = new SearchGoods();
				sg.setTotal(total);
				sg.setGoods_name(rs.getString("goods_name"));
				sg.setGoods_image(rs.getString("goods_img"));
				sg.setGoods_price(rs.getString("goods_price"));
				sg.setGoods_url(rs.getString("goods_url"));
				sg.setGoods_solder(rs.getString("goods_sold"));
				sg.setGoods_minOrder(rs.getString("goods_morder"));
				sg.setGoods_free(rs.getString("goods_free"));
				sg.setSeller_online(rs.getString("online_flag"));
				sg.setSeller_flag(rs.getInt("seller_flag"));
				sg.setValid(rs.getInt("goods_flag"));
				sg.setGoods_similar(rs.getString("goods_similar"));
				sg.setCreatetime(rs.getString("createtime"));
				list.add(sg);
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
	public ArrayList<SearchGoods> getDatasLast(String param_id,String time) {
		ArrayList<SearchGoods>  list = new ArrayList<SearchGoods>();
		SearchGoods sg = null;
		String sql="select *from ali_search_cache_datas where match(param_id) AGAINST (? IN BOOLEAN MODE) and"
				+ "  goods_flag=1 and seller_flag=1 and createtime<time";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, param_id);
			stmt.setString(2, time);
			rs = stmt.executeQuery();
			while(rs.next()){
				sg = new SearchGoods();
				sg.setCreatetime(rs.getString("createtime"));
				sg.setGoods_free(rs.getString("goods_free"));
				sg.setGoods_image(rs.getString("goods_img"));
				sg.setGoods_image(rs.getString("goods_morder"));
				sg.setGoods_name(rs.getString("goods_name"));
				sg.setGoods_price(rs.getString("goods_price"));
				sg.setGoods_similar(rs.getString("goods_similar"));
				sg.setGoods_solder(rs.getString("goods_sold"));
				sg.setGoods_url(rs.getString("goods_url"));
				sg.setSeller_flag(rs.getInt("seller_flag"));
//				sg.setId(rs.getInt("id"));
				sg.setValid(rs.getInt("goods_flag"));
				sg.setSeller_online(rs.getString("online_flag"));
				list.add(sg);
				
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
	public int updateParamCachePage(int page, int param_id) {
		int result = 0;
		String sql="update ali_search_cache_param set param_cache_page=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, page);
			stmt.setInt(2, param_id);
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
	public int updateParamCacheFlag(int flag, int param_id) {
		int result = 0;
		String sql="update ali_search_cache_param set param_cache_flag=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, flag);
			stmt.setInt(2, param_id);
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
	public int getParamCacheFlag(int param_id) {
		int result =-1;
		String sql="select param_cache_flag from ali_search_cache_param where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, param_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("param_cache_flag");
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

	@Override
	public int getParamCachePage(int param_id) {
		int result =0;
		String sql="select param_cache_page from ali_search_cache_param where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, param_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("param_cache_page");
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

	@Override
	public int updateParamDataCount(int datas_count,int param_id) {
		int result = 0;
		String sql="update ali_search_cache_param set datas_count=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, datas_count);
			stmt.setInt(2, param_id);
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
	public int getDataCount(int param_id) {
		int result =0;
		String sql="select count(*) from ali_search_cache_datas where match(param_id) against(? in boolean mode) "
				+ "and goods_flag=1 and seller_flag=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "'"+param_id+"'");
			rs = stmt.executeQuery();
			if(rs.next()){
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

	@Override
	public int getParamDataCount(int param_id) {
		int result =0;
		String sql="select datas_count from ali_search_cache_param where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, param_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("datas_count");
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

	@Override
	public Map<String, Object> getParam(int param_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql="select param_cache_flag,param_cache_page,param_catid,datas_count,table_name "
				+ "from ali_search_cache_param where id=? and valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, param_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				map.put("param_cache_flag", rs.getInt("param_cache_flag"));
				map.put("param_cache_page", rs.getInt("param_cache_page"));
				map.put("datas_count", rs.getInt("datas_count"));
				map.put("table_name", rs.getString("table_name"));
				map.put("param_catid", rs.getString("param_catid"));
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
		return map;
	}
	@Override
	public ArrayList<Map<String, Object>> getParamAll() {
		ArrayList<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String sql="select * from ali_search_cache_param where valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				map.put("param_id", rs.getString("id"));
				map.put("param_cache_flag", rs.getInt("param_cache_flag"));
				map.put("param_cache_page", rs.getInt("param_cache_page"));
				map.put("datas_count", rs.getInt("datas_count"));
				map.put("table_name", rs.getString("table_name"));
				list.add(map);
				map = null;
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
	public int updateParamTabName(String table_name,int param_id) {
		int result = 0;
		String sql="update ali_search_cache_param set table_name=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, table_name);
			stmt.setInt(2, param_id);
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
	public String getParamTabName(int param_id) {
		String result =null;
		String sql="select table_name from ali_search_cache_param where id=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, param_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				result = rs.getString("table_name");
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

	@Override
	public ArrayList<Map<String, String>> getParamLatest(String time) {
		String sql="select id,table_name,param_key,param_catid,param_sort from ali_search_cache_param where createtime<? and valid=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, time);
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("param_id", rs.getString("id"));
				map.put("table_name", rs.getString("table_name"));
				map.put("param_key", rs.getString("param_key"));
				map.put("param_catid", rs.getString("param_catid"));
				map.put("param_sort", rs.getString("param_sort"));
				list.add(map);
				map = null;
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
	public int addLog() {
		int result = 0;
		String sql="insert into ali_search_cache_log (param_id,goods_name,goods_price,goods_img,goods_url,"
				+ "goods_sold,goods_morder,goods_free,online_flag,seller_flag,goods_flag,goods_similar,createtime) "
				+ "(select param_id,goods_name,goods_price,goods_img,goods_url,"
				+ "goods_sold,goods_morder,goods_free,online_flag,seller_flag,goods_flag,goods_similar,now() "
				+ "from ali_search_cache_datas where goods_flag=0 or seller_flag=0)";
		
		String sql2="delete  from  ali_search_cache_datas where goods_flag=0  or seller_flag=0";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			stmt.executeUpdate();
			stmt2.executeUpdate();
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
			if(stmt2!=null){
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return result;
	}

	@Override
	public int deleteDatas() {
		int result = 0;
		String sql="delete  from  ali_search_cache_datas where goods_flag=0  or seller_flag=0";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
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
