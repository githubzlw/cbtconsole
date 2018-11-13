<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>

<%
	Throwable ex = null;
	if (exception != null) 
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
%>

<!DOCTYPE html>
<html>
<head>
	<title>China Wholesale Online, Buying Chinese Products</title>
	<style type="text/css">
     body,p,{margin:0;padding:0;}
	body{font-family: Verdana, Arial, Helvetica, sans-serif;}
	img{border:0 none;}
     .error404a1{display:block;width:950px;margin:0 auto;color:#000;text-decoration:none;padding-top:10px;}
	 .error404logo{border-bottom:1px solid #c9c9c9;padding-bottom:10px;}
	.error4p{margin:0 auto;width:625px;margin-top:30px;font-size:21px;}
	.error4b{font-size:15px;color:#0569cd;text-decoration:underline;width:465px;margin:0 auto;display:block;margin-top:8px;}
	</style>
</head>

<body>
	<!-- <h2>500 - 系统发生内部错误.</h2>
	<p><a href="<c:url value="/apa/index.html"/>">返回首页</a></p> -->
	<a href="<c:url value="http://www.import-express.com"/>" class="error404a1">
       <p class="error404logo"><img src="/cbtconsole/img/logo.png"/></p>
       <p class="error4p">Sorry , <b>Internal error</b></p>
       <b class="error4b">Back to homepage now</b>
        <img src="/cbtconsole/img/error/500.jpg"/>
    </a>
</body>
</html>