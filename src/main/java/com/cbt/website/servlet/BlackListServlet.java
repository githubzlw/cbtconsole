package com.cbt.website.servlet;

import com.cbt.bean.BlackList;
import com.cbt.bean.SpiderBean;
import com.cbt.util.SplitPage;
import com.cbt.util.Utility;
import com.cbt.website.dao.*;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 黑名单管理
 */
public class BlackListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BlackListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		String page=request.getParameter("page");
		String url=request.getParameter("url");
		BlackListDao blackListDao=new BlackListDaoImpl();
		BlackList blackList = new BlackList();
		Map<String,Object> map=new HashMap<String,Object>();
		if("blacklist".equals(action)){
			String email=request.getParameter("email");
//			blackList.setEmail(email);
			int total=blackListDao.getBlackListPageCount(blackList);
			List<BlackList> list=blackListDao.getBlackListPage(blackList,Integer.parseInt(page), 20);
			request.setAttribute("list", list);
			SplitPage.buildPager(request, total, 20, Integer.parseInt(page));
			request.getRequestDispatcher("website/blacklist.jsp").forward(request, response);
		}else if("addblacklist".equals(action)){
			response.sendRedirect("website/addblacklist.jsp");
		}else if("delblacklist".equals(action)){
			String ids=request.getParameter("ids");
			int result=blackListDao.delBlackList(ids);
			if(result > 0){
				map.put("code", "1");
				map.put("msg", "操作成功");
			}else{
				map.put("code", "0");
				map.put("msg", "操作失败");
			}
			String resultJSON=JSONArray.fromObject(map).toString();
			out.print(resultJSON);
			out.flush();
			out.close();
		}else if("modifyblacklist".equals(action)){
			String id=request.getParameter("id");
			Integer iid=Integer.parseInt(id);
			blackList.setId(iid);
			blackList=blackListDao.getBlackList(blackList);
			request.setAttribute("blackList", blackList);
			request.getRequestDispatcher("website/modifyblacklist.jsp").forward(request, response);
		}else if("check".equals(action)){
			/*Cookie[] coks = request.getCookies();
			String username = "";
			for (int i = 0; i < coks.length; i++) {
				String name = coks[i].getName();
				String value = coks[i].getValue();
				if ("userName".equals(name)) {
					username = coks[i].getValue();
					break;
				}
			}
			UserDao us = new UserDaoImpl();
			String email = us.getUserEmailByUserName(username);
			blackList.setEmail(email);*/
			String ip=Utility.getIpAddress(request);
//			blackList.setUserip(ip);
			boolean flag = blackListDao.getBlackListCount(blackList);
			if (flag) {
				map.put("code", "1");
				map.put("msg", "该用户不属于黑名单");
			} else {
				map.put("code", "0");
				map.put("msg", "该用户属于黑名单");
			}
			String resultJSON=JSONArray.fromObject(map).toString();
			out.print(resultJSON);
		}else if("blackgoods".equals(action)){
			List<SpiderBean> list=blackListDao.getBlackgoods();
			request.setAttribute("blackgoodsList", list);
			request.getRequestDispatcher("website/blackgoods.jsp").forward(request, response);
		}else if("addblackgoods".equals(action)){
			blackListDao.addBlackgoods(url);
			List<SpiderBean> list=blackListDao.getBlackgoods();
			request.setAttribute("blackgoodsList", list);
			request.getRequestDispatcher("website/blackgoods.jsp").forward(request, response);
		}
	}
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String action=request.getParameter("action");
		Map<String,Object> map=new HashMap<String,Object>();
		BlackListDao blackListDao=new BlackListDaoImpl();
		BlackList blackList=new BlackList();	
		if("addblacklist".equals(action)){
			String email=request.getParameter("email");
			String userip=request.getParameter("userip");
			String username=request.getParameter("username");
			UserDao userDao=new UserDaoImpl();
			String userId=userDao.getUserIdByEmail(email);
			IOrderwsDao iorderwsDao=new OrderwsDao();
			Set<String> ipSet=iorderwsDao.getOrderIpByUserId(userId);	
//			blackList.setEmail(email);
			blackList.setUsername(username);
			if(userip != null && !"".equals(userip)){
				ipSet.add(userip);
			}
			blackList.setCreatetime(Utility.format(new Date(), Utility.datePattern1));
			if(ipSet != null && ipSet.size()==0){
				ipSet.add("");
			}
			int[] result=blackListDao.addBlackList(blackList, ipSet);	
			if(result.length > 0){
				map.put("code", "1");
				map.put("msg", "操作成功");
			}else{
				map.put("code", "0");
				map.put("msg", "操作失败");
			}							
		}else if("modifyblacklist".equals(action)){
			String email=request.getParameter("email");
			String userip=request.getParameter("userip");
			String id=request.getParameter("id");
			blackList.setId(Integer.parseInt(id));
//			blackList.setEmail(email);
//			blackList.setUserip(userip);
			int result=blackListDao.modifyBlackList(blackList);
			if(result == 1){
				map.put("code", "1");
				map.put("msg", "操作成功");
			}else{
				map.put("code", "0");
				map.put("msg", "操作失败");
			}
		}else{			
			map.put("code", "0");
			map.put("msg", "操作失败");
		}
		String resultJSON=JSONArray.fromObject(map).toString();
		out.print(resultJSON);
		out.flush();
		out.close();
	}

}
