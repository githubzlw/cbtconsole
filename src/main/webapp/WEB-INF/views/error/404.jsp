<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%response.setStatus(200);%>

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
	<!-- <p><a href="<c:url value="/apa/index.html"/>">返回首页</a></p> -->
	<a href="<c:url value="http://www.import-express.com"/>" class="error404a1">
       <p class="error404logo"><img src="/cbtconsole/img/logo.png"/></p>
       <p class="error4p">Sorry , <b>The page you requested can not be found! :(</b></p>
       <b class="error4b">Back to homepage now</b>
        <img src="/cbtconsole/img/error/404.jpg"/>
    </a>
</body>
</html>