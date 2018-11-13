<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>屏蔽商品管理页面</title>
</head>
<script type="text/javascript">
function addBlackgoods(){
	window.location.href="/cbtconsole/BlackListServlet?action=addblackgoods&url="+document.getElementById("goodsurl").value;
}
</script>
<body>
<div align="center">
    <div class="top">
        <span>屏蔽商品链接列表</span>
    </div>
    <div style="height: 10px">
    </div>
    <div class="select">
        商品链接:<input type="text" id="goodsurl" value="" style="width:700px"/>
        <input type="button" onclick="addBlackgoods()" value="添加"/><br>
    </div>
	<div>
		<table border="1">
		        <tr> 
			        <th>Goods Data Id</th> 
			        <th>商品名称</th> 
			        <th>商品链接</th> 
			    </tr> 
				<c:forEach items="${blackgoodsList}" var="list">     
				      <tr>   
				          <td>${list.goodsdata_id}</td>
				          <td>${list.name}</td>
				          <td>${list.url}</td>
				      </tr>     
				</c:forEach> 
		</table> 
	</div>
</div>
</body>
</html>