package com.cbt.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.bean.Admuser;

public class LoginFilter extends OncePerRequestFilter{
	private static Set<String> anon = new HashSet<>();
	static {
		anon.add("/cbtconsole/");
		anon.add("/cbtconsole");
		anon.add("/cbtconsole/website/main_login.jsp");
		anon.add("/cbtconsole/userLogin/checkUserInfo.do");
		anon.add("");
		
//		/css/** = anon
//		/img/** = anon
//		/js/** = anon
//		/source/** = anon
//		/jquery-easyui-1.5.2/** = anon
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		Admuser adm;
		if(!anon.contains(uri)) {
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			if(adm == null) {
				response.sendRedirect("/cbtconsole/website/main_login.jsp");
				return;
			}
		}
		//验证权限
		
		
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("LoginFilter --doFilterInternal filterChain.doFilter error,url is "+uri+"，error content：",e);
		}
		
	}

}
