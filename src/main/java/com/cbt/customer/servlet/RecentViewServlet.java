package com.cbt.customer.servlet;

import com.cbt.bean.RecentViewBean;
import com.cbt.customer.dao.IRencentViewDao;
import com.cbt.customer.dao.RencentViewDaoImpl;
import com.cbt.customer.service.IRecentViewService;
import com.cbt.customer.service.RencentViewServiceImpl;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecentViewServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  public static List<RecentViewBean> getRencent(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html");
    List<RecentViewBean> list = new ArrayList<RecentViewBean>();
    String value = WebCookie.cookie(request, "rencentView");
    if (value != null) {
        IRencentViewDao dao = new RencentViewDaoImpl();
        list = dao.findCent(value);
    }
    return list;
  }

  public static void main(String[] args) {
	String slString = "http://192.168.1.212:8080/cbtconsole/goodsTypeServerlet?keyword=cd&srt=order-desc";
	System.out.println(slString.split("keyword=")[1].split("&")[0]);
	
}
  public static void saveCookie(HttpServletRequest request, HttpServletResponse response, RecentViewBean rvb)
  {
    if (!Utility.getStringIsNull(rvb.getPid())) {
      return;
    }

    IRecentViewService service = new RencentViewServiceImpl();
    service.addRecode(rvb);
    String coName = "rencentView";
    String value = WebCookie.cookie(request, "rencentView");
    StringBuffer sb = new StringBuffer();
    sb.append(rvb.getPid());
    if (Utility.getStringIsNull(value))
    {
        String[] ids = value.split(",");
        int length = ids.length > 6 ? 6 : ids.length;
        for (int i = 0; i < length; i++) {
          if (!ids[i].equals(rvb.getPid())) {
            sb.append(",").append(ids[i]);
          }
        }
        Cookie cookie = new Cookie(coName, sb.toString());
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }else{
        Cookie cookie = new Cookie(coName, sb.toString());
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }
  }
}