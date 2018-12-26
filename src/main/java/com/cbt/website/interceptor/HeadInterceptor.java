package com.cbt.website.interceptor;

import com.cbt.util.*;
import com.cbt.warehouse.ctrl.HotGoodsCtrl;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.bean.AuthInfo;
import net.sf.json.JSONArray;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeadInterceptor implements Filter {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HeadInterceptor.class);

	private String loginPage;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpSession session = httpServletRequest.getSession();
		// 获取当前路径
		String servletPath = httpServletRequest.getServletPath();
		String urlPath = httpServletRequest.getRequestURL().toString();
		String queryString = httpServletRequest.getQueryString();
		String requestUrl = null;
		if (queryString != null) {
			requestUrl = servletPath + "?" + queryString;
		} else {
			requestUrl = servletPath;
		}

		String userauthJson = "";
		String admuserJson = "";
		Admuser admuser = null;
		try {
			String sessionId = session.getId();
			// 从redis中获取数据
			admuserJson = Redis.hget(sessionId, "admuser");
			userauthJson = Redis.hget(sessionId, "userauth");
			admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		} catch (Exception e) {
			logger.error("doFilter",e);
			throw e;
		}
		try {

			if (urlPath != null && urlPath.contains("cbtconsole/www")) {
				httpServletResponse.sendRedirect(AppConfig.ip);
			} else {
				// 判断是否是管理台菜单请求
				if (Cache.getCacheUrl(requestUrl)) {
					// 判断是否已经登录或session是否过期
					if (admuser == null && "/website/".equals(servletPath)) {
						httpServletResponse.sendRedirect(AppConfig.ip + "/website" + loginPage);
					} else {
						if (userauthJson == null || "".equals(userauthJson)) {
							String result = "{\"status\":false,\"message\":\"对不起,请先登录再进行操作\"}";
							WebTool.writeJson(result, httpServletResponse);
						} else {
							List<AuthInfo> authlist = new ArrayList<AuthInfo>();
							JSONArray jsonArray = JSONArray.fromObject(userauthJson);// 把String转换为json
							authlist = JSONArray.toList(jsonArray, AuthInfo.class);// 这里的t是Class<T>
							if (authlist == null || authlist.size() == 0) {
								String result = "{\"status\":false,\"message\":\"对不起,请先登录再进行操作\"}";
								WebTool.writeJson(result, httpServletResponse);
							} else {
								boolean isCheck = false;
								for (int i = 0; i < authlist.size(); i++) {
									if (requestUrl.equals(authlist.get(i).getUrl())) {
										isCheck = true;
										break;
									}
								}
								authlist = null;
								if (isCheck) {
									chain.doFilter(request, response);
								} else {
									String result = "{\"status\":false,\"message\":\"您无权限操作\"}";
									WebTool.writeJson(result, httpServletResponse);
								}
							}
						}
					}

				} else {// 如果没登录且访问后台页面或者session过期在menu页面刷新都转到登录页面

					if (admuser == null && "/website/".equals(servletPath)
							|| admuser == null && ("/website/main_menu.jsp".equals(servletPath) || "/website/user_profit.jsp".equals(servletPath))) {
						logger.warn("1如果没登录且访问后台页面或者session过期在menu页面刷新都转到登录页面");
						logger.info("requestUrl:[{}]",requestUrl);
						logger.info("Cache.getAllAuth():[{}]",Cache.getAllAuth());
						httpServletResponse.sendRedirect(AppConfig.ip + "/website" + loginPage);
					} else if (admuser != null && "/website/".equals(servletPath)) {
						logger.warn("2如果没登录且访问后台页面或者session过期在menu页面刷新都转到登录页面");
						logger.info("requestUrl:[{}]",requestUrl);
						logger.info("Cache.getAllAuth():[{}]",Cache.getAllAuth());
						httpServletResponse.sendRedirect(AppConfig.ip + "/website/main_menu.jsp");
					} else {
						chain.doFilter(request, response);
					}
				}

			}

		} catch (Exception e) {
			logger.error("doFilter",e);
			String result = "{\"status\":false,\"message\":\"请重新登录进行操作\"}";
			WebTool.writeJson(result, httpServletResponse);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		String jspStr = filterConfig.getInitParameter("loginPage");
		if (jspStr == null || "".equals(jspStr)) {
			this.loginPage = "/main_login.jsp";
		} else {
			this.loginPage = jspStr;
		}

	}

}
