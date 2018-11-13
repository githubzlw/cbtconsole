package com.cbt.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 过滤jsp访问
 */
public class SaveIpFilter implements Filter {

    /**
     * Default constructor. 
     */
    public SaveIpFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest hrequest = (HttpServletRequest) request;
//		String ip = Utility.getIpAddress(hrequest);
//		IIPSelectDao ipSelectDao = new IPSelectDao();
//		ArrayList<HashMap<String, String>> list = ipSelectDao.querry(ip);
//		if (list.size() == 0) {
//			ipSelectDao.add(ip);
//		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
