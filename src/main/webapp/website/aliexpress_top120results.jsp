<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>关键字搜索结果列表</title>
</head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<body>
	<div align="center">
		<h2>关键字搜索结果列表</h2>
		<table border="1">
			<tr>
				<td>搜索关键词</td>
				<td>类别ID</td>
				<td>上次下载时间</td>
				<td>上次审查时间 </td>
				<td>有货商品数量</td>
				<td></td>
			</tr>
		<c:forEach items="${results}" var="search">
			<tr>
				<td>${search[0]}</td>
				<td>${search[1]}</td>
				<td>${search[2]}</td>
				<td>${search[3]}</td>
				<td>${search[5]}</td>
				<td><a target="_blank" href="/cbtconsole/WebsiteServlet?action=show_aliexpress_results_tb&className=Aliexpress_top120&id=${search[4]}">查看</a></td>
			</tr>
		</c:forEach>
		</table>
	</div>
</body>
</html>