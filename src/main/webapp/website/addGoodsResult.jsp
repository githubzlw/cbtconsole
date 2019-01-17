<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>添加对标商品执行结果</title>
</head>
<body>

<h1 style="text-align: center">添加对标商品执行结果</h1>

<br>
<br>
<c:if test="${result > 0}">
    <h1 style="text-align: center;color: green">${message}</h1>
</c:if>

<c:if test="${result == 0}">
    <h1 style="text-align: center;color: red">${message}</h1>
</c:if>

</body>
</html>
