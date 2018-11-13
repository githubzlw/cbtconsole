package com.cbt.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylm
 *页面跳转
 */
public class SendRedirectPage {

	public static String sendPage(HttpServletRequest request, int userid){
		String pagestate="",itemid="";
		int j=0;
		if(WebCookie.cookie(request, "pageState") != null){
			pagestate=WebCookie.cookie(request, "pageState");
			if(pagestate.indexOf(":") > -1){
				j=Integer.parseInt(pagestate.split(":")[0]);
				itemid=pagestate.split(":")[1];
			}
		}
		String advance = "";
		if(j>7){
			advance = "&advance=1";
			j = j-4;
		}
		if(j == 1 || j == 5){
			return "/paysServlet?action=getSelectedItem&className=RequireInfoServlet"+"&userId="+userid+"&itemId="+itemid+"&state=1";
		}else if(j == 2){
			return "cbt/Settlement.jsp"+advance;
		}else if(j == 3){
			return "cbt/paysuccess.jsp";
		}else if(j == 4){
			return "/cbt/orderinfonew/loginCart?flag=1&userId="+userid+"&itemId="+itemid+advance;
		}else if(j == 6){
			return "/paysServlet?action=getSelectedItemrequiredinfo&className=RequireInfoServlet&userId="+userid+"&itemId="+itemid+advance+"&state=1";
		}else if(j == 7){
			return "/cbt/orderinfonew/loginCart?flag=1&userId="+userid+"&itemId="+itemid+"&FreeShipping=1";
		}else{
			return "/processesServlet?action=getCenter&className=IndividualServlet";
		}
	}
	 
}
