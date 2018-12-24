package com.cbt.util;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.userAuth.bean.AuthInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache {

	private static final ConcurrentMap<String, List<Map<String, Object>>> cache = new ConcurrentHashMap<String, List<Map<String, Object>>>();

	// 管理台菜单缓存
	private static final List<AuthInfo> urlList = new ArrayList<AuthInfo>();

	/**获取缓存
	 * @date 2016年3月1日
	 * @author abc
	 * @param key
	 * @return  
	 */
	public static List<Map<String, Object>> get(String key) {
		List<Map<String, Object>> resultList = null;
		if (!cache.isEmpty()) {
			for (Entry<String, List<Map<String, Object>>> entry : cache
					.entrySet()) {
				String k = entry.getKey();
				if (k.indexOf(key.toLowerCase()) >= 0) {
					resultList = entry.getValue();
					break;
				}
			}
		}
		return resultList;
	}

	/**保存缓存
	 * @date 2016年3月1日
	 * @author abc
	 * @param key
	 * @param list  
	 */
	public static void save(String key, List<Map<String, Object>> list) {
		cache.putIfAbsent(key, list);
	}
	
	/**更新缓存
	 * @date 2016年3月1日
	 * @author abc
	 * @param key
	 * @param list  
	 */
	public static void replace(String key, List<Map<String, Object>> list) {
		cache.replace(key, list);
	}
	

	/**
	 * 判断菜单缓存中是否存在该菜单
	 * 
	 * @param url
	 * @return
	 */
	public static boolean getCacheUrl(String url) {
		for (int i = 0; i < urlList.size(); i++) {
			AuthInfo authinfo = urlList.get(i);
			if (url.equals(authinfo.getUrl())) {
				return true;
			}
		}
		return false;
	}

    /**
     * 清除菜单中缓存
     *
     */
    public static void clearCache() {
        urlList.clear();
    }

	public static void saveCache(AuthInfo auth) {
		urlList.add(auth);
	}

	public static List<AuthInfo> getAllAuth() throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Connection conn=null;
		try{
			if(urlList.size()<=0){
				conn = DBHelper.getInstance().getConnection();
				String sql="SELECT authId,authName FROM tbl_auth_info";
				stmt=conn.prepareStatement(sql);
				rs=stmt.executeQuery();
				while(rs.next()){
					AuthInfo a=new AuthInfo();
					a.setAuthId(rs.getInt("authId"));
					a.setAuthName(rs.getString("authName"));
					urlList.add(a);
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeResultSet(rs);
		}
		return urlList;
	}

}
