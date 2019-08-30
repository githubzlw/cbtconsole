package com.cbt.filter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;

public class FilterChainDefinitionMapBuilder {
	public LinkedHashMap<String, String> buildFilterChainDefinitionMap(){
		AdmUserDao admuserDao = new AdmUserDaoImpl();
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("/css/**", "anon");
		map.put("/source/**", "anon");
		map.put("/img/**", "anon");
		map.put("/jquery-easyui-1.5.2/**", "anon");
		map.put("/js/**", "anon");
		map.put("/userLogin/checkUserInfo.do", "anon");
		map.put("/userLogin/loginOut.do", "logout");
		
		List<Map<String,String>> allAnth = admuserDao.getAllAnth();
		for(Map<String,String> m : allAnth) {
			String roll = map.get(m.get("url"));
			if(StringUtil.isNotBlank(roll)) {
				roll = roll.replace("]", "") + ","+m.get("roleType")+"]";
			}
			map.put(m.get("url"), roll);
		}
		map.put("/**", "authc");
		
		return map;
	}
}
