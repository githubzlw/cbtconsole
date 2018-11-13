<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>验货图片列表</title>
</head>
<body>
	<table width="800px;" cellspacing="1" border="1" align="center">
		<c:if test="${!empty picturePathList}">
			<c:set var="i" value="1" />
			<c:forEach var="pic" items="${picturePathList }">
				<c:if test="${i % 5 == 1 }">
					<tr>
				</c:if>
				<td><p>订单号：${pic.orderid }<br>商品号：${pic.goodid }<br>商品名称：${pic.goods_name }</p>
					<%-- <img alt="到货图片" src="WebsiteServlet?action=outputPicture&className=ExpressTrackServlet&pic=${pic.picturepath }"> --%>
					<img alt="到货图片" src="http://s154x36606.iok.la:8084/${pic.picturepath }">
				</td>
				<c:if test="${i % 5 == 0 }">
					</tr>
				</c:if>
				<c:set var="i" value="${i+1 }" />
			</c:forEach>
		</c:if>
    </table>
</body>
</html>