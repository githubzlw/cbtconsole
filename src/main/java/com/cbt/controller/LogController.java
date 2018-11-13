package com.cbt.controller;

import com.cbt.log.dao.SaveLogToMysql;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping(value = "/cbt/lz")
public class LogController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(KeyWordsController.class);
	
	@RequestMapping(value = "/savelz", method = RequestMethod.POST)
	public String savelog(HttpServletRequest request, HttpServletResponse response,
                          String keywords, String seachwords, String view_url, String device, String placement) throws IOException{
		 HttpSession session = request.getSession();
		Object  view_url_count_ = session.getAttribute("view_url_count");//页面计数
		 int view_url_count = 1;
		if(view_url_count_ != null){
			view_url_count = Integer.parseInt(view_url_count_.toString())+1;
			session.setAttribute("view_url_count", view_url_count);
		}else{
			session.setAttribute("view_url_count", view_url_count);
		}
		 String[] userinfo = WebCookie.getUser(request);
		 int userid = 0;
		 if(userinfo != null){
			 userid = Integer.parseInt(userinfo[0]);
		 }
		 SaveLogToMysql.insert(userid, session.getId(), keywords, seachwords, Utility.getIpAddress(request), view_url, view_url_count, "", "",device,placement);
		 return "savelog";
	}
	
	@RequestMapping(value = "/slz", method = RequestMethod.POST)
	public String slz(HttpServletRequest request, HttpServletResponse response, String action, String pruduct_url, String url) throws IOException{
		 HttpSession session = request.getSession();
		Object  view_url_count_ = session.getAttribute("view_url_count");//页面计数
		 int view_url_count = 1;
		if(view_url_count_ != null){
			view_url_count = Integer.parseInt(view_url_count_.toString())+1;
			session.setAttribute("view_url_count", view_url_count);
		}else{
			session.setAttribute("view_url_count", view_url_count);
		}
		 String[] userinfo = WebCookie.getUser(request);
		 int userid = 0;
		 if(userinfo != null){
			 userid = Integer.parseInt(userinfo[0]);
		 }
		 SaveLogToMysql.insert(userid, session.getId(), "", "", Utility.getIpAddress(request), url, view_url_count, action, pruduct_url,"","");
		 return "savelog";
	}
}
