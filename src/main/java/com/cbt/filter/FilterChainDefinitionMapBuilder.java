package com.cbt.filter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;

public class FilterChainDefinitionMapBuilder {
	public LinkedHashMap<String, String> buildFilterChainDefinitionMap(){
		AdmUserDao admuserDao = new AdmUserDaoImpl();
		LinkedHashMap<String, String> map = new LinkedHashMap<>(1024);
		map.put("/css/**", "anon");
		map.put("/source/**", "anon");
		map.put("/img/**", "anon");
		map.put("/jquery-easyui-1.5.2/**", "anon");
		map.put("/js/**", "anon");
		map.put("/userLogin/checkUserInfo.do", "anon");
		map.put("/userLogin/loginOut.do", "logout");
		
		// xxx.html* = anon （未登录可以访问）  xxx.html* =authc （必须登录才能访问 ）  xxx.html* = perms[权限] （需要特定权限才能访问）  
//		xxx.html* = roles[角色] （需要特定角色才能访问 ） 
		
		List<Map<String,String>> allAnth = admuserDao.getAllAnth();
		for(Map<String,String> m : allAnth) {
			String url = m.get("url");
			url = url.replace("?colorFlag=ccff9a", "");
			url = url.startsWith("http") ? url : "/cbtconsole"+url;
			map.put(url, "perms[admin|req:"+m.get("authId")+"]");
		}
		map.put("/**", "authc");
		return map;
	}
}
