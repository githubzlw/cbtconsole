package com.cbt.parse.servlet;

import com.cbt.parse.bean.SearchGoods;
import com.cbt.parse.driver.YiWuDriver;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Servlet implementation class YiWu
 */
public class YiWu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YiWu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String keyword = request.getParameter("keyword");
		String fkey = request.getParameter("fkey");
		String website = request.getParameter("website");
		JSONArray jsonArray = JSONArray.fromObject("[]");
		if(keyword!=null&&!"undefined".equals(keyword)){
			keyword=URLEncoder.encode(keyword, "utf-8");
			keyword = keyword.replaceAll("%20+", " ");
			
			if(fkey!=null&&!"undefined".equals(fkey)){
				fkey=URLEncoder.encode(fkey, "utf-8");
				fkey = fkey.replaceAll("%20+", " ").replaceAll("%3B+", ";");
			}
			if(website.equals("y")){
				ArrayList<SearchGoods> storeSearch = null;
				YiWuDriver driver = new YiWuDriver();
				storeSearch = driver.searchFilter(keyword, fkey,false);
				if(storeSearch!=null&&!storeSearch.isEmpty()){
					if(storeSearch!=null&&!storeSearch.isEmpty()){
						jsonArray = JSONArray.fromObject(storeSearch);
					}
					storeSearch = null;
				}
			}
		}
		
		request.setAttribute("responsetext", jsonArray);
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/yiwu.jsp");
		homeDispatcher.forward(request, response);
		jsonArray = null;
		
	}

}
