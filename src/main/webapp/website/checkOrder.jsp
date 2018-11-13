<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tableExport.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#F4F5FF;
}
.evenrowcolor{
	background-color:#FFF4F7;
}




/* 字体样式 */
body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;} 
p.thicker {font-weight: 900}


</style>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<script type="text/javascript">

//查询 提交
function aSubmit(){
	document.getElementById("idForm").submit();
}

//清空查询条件
function cleText(){
//	document.getElementById("idname").value="";
	$('input[type=text]').val("");
}
</script>

</head>
<body style="background-color : #F4FFF4;">
<form id="idForm" action="/cbtconsole/takeGoods/checkOrder.do" method="get">
	<div align="center" >
		<div><H1>查看抓取订单关联项目号</H1></div>
		<div>                                                   
			淘宝/1688订单号:<input class="querycss" style="width : 160px;" id="orderid" name="orderid" value="${orderid}" type="text"/>
			<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
			<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
			
		</div>
		<div>
		 <table>
		 <p>对应的项目号:</p>
		  <c:forEach items="${orderList}" var="order" varStatus="s">
				<tr><td><a target='_blank' href="/cbtconsole/PurchaseServlet?action=getPurchaseByXXX&className=Purchase&pagenum=1&goodsid=&orderid_no_array=&orderid=0&admid=999&userid=&orderno=${order}&goodid=&date=&days=&state=&unpaid=0&pagesize=50&goodname=">${order }</a></td></tr>		
		  </c:forEach>
		 </table>
		</div>
	</div>
	</form>
	
</body>
</html>