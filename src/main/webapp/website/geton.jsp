<%@page import="java.net.URLEncoder"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@page import="com.cbt.util.WebCookie"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>  
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/login.css">
<title>Import Express</title>
<script type="text/javascript">
<% String[] userinfo = WebCookie.getUser(request);
%> 
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-61939258-1', 'auto');
	  ga('send', 'pageview');
	  ga('set', '&uid', <%=userinfo != null ? userinfo[0] : "0"%>);
//facebook login-----end */
function loginSubmit(){
	var userName = $.trim($("#userName").val());
	var password = $.trim($("#password").val());
	//var loginCode = $("#loginCode").val().trim();
	if(userName == ""){
		$("#errorInfo").html("Please enter the username");
		$("#errorInfo").css("display","block");
		$("#errorInfo").css("color","red");
		return ;
	}else if(password == ""){
		$("#errorInfo").html("Please enter the password");
		$("#errorInfo").css("display","block");
		$("#errorInfo").css("color","red");
		return ;
	/*  }else if(loginCode.length != 4){
		$("#errorInfo").html("验证码错误");
		$("#errorInfo").css("display","block");
		return ;   */
	}
	if('${param.flag}' == 'dialog'){
		var remember=$("#remember").val();
		$.post("/cbtconsole/cbt/cbtlogin/verify",{userName:userName,password:password,remember:remember},function(result){
		    if(result.code == 0){
		    	parent.close();
		    }else{
		    	$("#errorInfo").text(result.msg);
		    } 
		});
	}else{
		$("#form").submit();
	}	
}
function loadimage(){
	document.getElementById("loginCheckCode").src = "/cbtconsole/processesServlet?action=getLoginImage&className=ImageServlet&"+Math.random(); 
}
function fnFocuss(val){
	$(val).css("background","url(/cbtconsole/img/login_b2.jpg) no-repeat");
}
function fnMover(val){
	$(val).css("background","url(/cbtconsole/img/login_b1.jpg) no-repeat");
}
</script>

</head>
<body >
   <form action="/cbtconsole/WebsiteServlet?action=login&className=Login" id="form" method="post">
   <% String purl=request.getParameter ("purl");
   if(purl != null && !"".equals(purl)){
	   purl = new String(purl.getBytes("iso-8859-1"),"GB2312");
	   purl = URLEncoder.encode(purl,"UTF-8");
   }%>
   	<input type="hidden" name="purl" value="<%=purl%>">
   	<input type="hidden" name="uid" value="${param.uid}">
   	<input type="hidden" name="gbid" value="${gbid }">
   	<input type="hidden" name="pre" value="${param.pre }">
   	<c:if test="${fn:contains(pageContext.request.queryString, 'redirect=')}">
   	   <input type="hidden" name="redirect" flag="${pageContext.request.requestURL} " value="${fn:replace(pageContext.request.queryString, 'redirect=','') } ">
   	</c:if>
   	   <div class="top">
        <div class="logo">
                <a href="http://www.import-express.com/" target="_blank"><img width="280px;" height="38px;" alt="logo" src="/cbtconsole/img/logo.png"></a></div>
        <div class="T_right">
            <a href="http://www.import-express.com/"  target="blank" >Home</a><!-- <span>|</span><a href="#">Help Center</a> -->
        </div>
    </div>
    <div class="login"  >
        
      <div class="dl"> 
            <h2>Sign In</h2>
            <div class="go">
            <%
        		String name = WebCookie.cookieValue(request, "userName");
            	String pass = "";
            	String saveuser = WebCookie.cookieValue(request, "saveuser");
            	if(saveuser != null){
            		pass = WebCookie.cookieValue(request, "pass_req");
            	}
            %>
                <br/><span id="errorInfo" style="float: right;width: 281px;color:red;">${code != null ? code:'' }</span>
                <table id="txtTable">
                    <tbody>
                    <tr> 
                        <td class="l" height="46px">
                            User ID:
                        </td>
                        <td rowspan="1">
                            <input style="border-radius:4px;border: 1px solid #8FC3E2;height: 21px;" name="userName" id="userName" value="<%=name!=null?name:"" %>" type="text" onfocus="this.className='on';this.style.borderColor='#E3B696'" onblur="this.className='';this.style.borderColor='#8FC3E2'"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l" height="46px;">
                            User Name:
                        </td>
                        <td>
                            <input  style="border-radius:4px;border: 1px solid #8FC3E2;height: 21px;" value="<%=pass!=null?pass.split("@")[0]:"" %>" name="password" id="password" type="text" onfocus="this.className='on';this.style.borderColor='#E3B696'" onblur="this.className='';this.style.borderColor='#8FC3E2'" maxlength="30">
                        </td>
                    </tr>
                     <tr><td></td>
                    	<td colspan="1" align="left"><div><input style="width: 30px;border:0 none;" id="remember" name="remember" checked="checked" type="checkbox">Stay Signed In </div></td>
                    </tr>
                    
                    <tr>
                    	<td colspan="2" align="center"><a href="/cbtconsole/cbt/forgot.jsp"  target="_blank">Find your password.</a></td>
                    </tr>
                    <tr>
                    	<td height="59px;" colspan="2" align="center">
                    		<%--lzj start --%>
                    		  <img onclick="location='https://www.facebook.com/dialog/oauth/?client_id=<%=AppConfig.APP_ID%>&redirect_uri=http://116.228.150.218:8099/CbtTest/FacebookCallback&scope=email&display=page&expires=0&purl=<%=purl%>'" src="/cbtconsole/img/facebook.png">
                    		 <%--lzj end --%>
                    	</td>
                    </tr>
                   <%--  <tr id="checkCodeTr">
                        <td class="l">
                            验证码
                        </td>
                        <td>
                            <input  class="y" style="width: 120px;" name="loginCode" id="loginCode" type="text" onfocus="this.className='y on'" onblur="this.className='y'" maxlength="4">
                            <img id="loginCheckCode"   onclick="loadimage();"  src="/cbtconsole/processesServlet?action=getLoginImage&className=ImageServlet"  title="点击图片刷新" alt="验证码" style="vertical-align:middle;cursor:pointer;" onclick="" border="0">
                        </td>
                    </tr> --%>
                    
                </tbody></table>
                <div class="join clearfix">
                    <input name="submitBtn" id="submitBtn1" onmousemove="this.className= 'jionh' " onmouseout="this.className=''"    type="button" onclick="loginSubmit();" value="Sign In"  >&nbsp;&nbsp;&nbsp;&nbsp;
                    <input name="submitBtn" id="submitBtn"  onmousemove="this.className= 'jionh' " onmouseout="this.className=''" type="button" onclick="window.open('/cbtconsole/cbt/register.jsp?purl=<%=purl%>')" value="Join Free"  >
                </div>
            </div>
        </div>
        <div class="get_back">
        	<a href="javascript:history.back(-1)">&laquo; back</a>
        </div>
    </div>
   
    </form>
  

</body>
</html>