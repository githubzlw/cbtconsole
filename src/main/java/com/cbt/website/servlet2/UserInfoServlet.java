package com.cbt.website.servlet2;

import com.cbt.website.bean.UserInfo;
import com.cbt.website.server.GoodsServer;
import com.cbt.website.server.IGoodsServer;
import com.cbt.website.util.Utility;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户信息
 */
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String userid = request.getParameter("userid");
		String stateDate = request.getParameter("stateDate");
		String endDate = request.getParameter("endDate");
		String date = request.getParameter("date");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String recipients=request.getParameter("recipients");
		String recipientsaddress=request.getParameter("recipientsaddress");
		String paymentusername=request.getParameter("paymentusername");
		String paymentid=request.getParameter("paymentid");
		String paymentemail=request.getParameter("paymentemail");
		String conutry=request.getParameter("conutry");
		String admUserId=request.getParameter("admUser");
		String vip=request.getParameter("vip");
		IGoodsServer server = new GoodsServer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		try {
			userInfos = server.getUserInfoForPrice(conutry,admUserId,vip,Utility.getStringIsNull(userid)?Integer.parseInt(userid):0,Utility.getStringIsNull(stateDate)?sdf.parse(stateDate):null,Utility.getStringIsNull(endDate)?sdf.parse(endDate):null,Utility.getStringIsNull(date)?sdf.parse(date):null,name,email,recipients,recipientsaddress,paymentusername,paymentid,paymentemail);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(userInfos));
		out.flush();
		out.close();
	}

}
