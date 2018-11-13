<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Invoice</title>
<style type="text/css">
body{width: 900px;margin: 0 auto;}
.body{width: 900px;height:auto;}
</style>
</head>
<body class="body">

<div>
<c:forEach items="${list}" var="bean" varStatus="state">
	<img style="-webkit-user-select: none" src="/cbtconsole/autoorder/show?orderid=${orderid}&index=${state.index}">	

</c:forEach>
</div>
</body>
</html>