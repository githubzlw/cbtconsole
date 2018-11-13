package com.cbt.controller;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.parse.service.StrUtils;
import com.cbt.website.bean.UserBehavior;
import com.importExpress.service.BehaviorRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value="/userstatisitcs")
public class UserStatisitcsController {

	@Autowired
	private BehaviorRecordService behaviorRecordService;
	
	
	/**客户对应的行为轨迹
	 * @date 2018年4月2日
	 * @author user4
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException  
	 */
	@RequestMapping(value="/userbehavior")
	public String getUserBehavior(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strUserid = request.getParameter("userid");
		int userid=StrUtils.isNum(strUserid) ? Integer.parseInt(strUserid) : 0;
		String view_dtime= request.getParameter("dateTime");
		String view_date_time = null;
		if(StringUtils.isNotBlank(view_dtime)){
			view_date_time= view_dtime+" 00:00:00";
		}
		String strPage = request.getParameter("page");
		int page = StrUtils.isNum(strPage) ? Integer.parseInt( strPage ) : 1;
		int pagesize = 40;
		try{
			DataSourceSelector.set("dataSource127hop");
			List<UserBehavior> list= behaviorRecordService.recordUserBehavior(userid, page, pagesize, view_date_time);
			int total = behaviorRecordService.recordUserBehaviorCount(userid, view_date_time);
			int totalPage = total % pagesize == 0? total / pagesize : total / pagesize + 1;
			request.setAttribute("view_dtime", view_dtime);
			request.setAttribute("userid", userid);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.setAttribute("totalCount", total);
			request.setAttribute("totalPage", totalPage);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		return "user_timeline";
	}
	
	
	
	
	

}
