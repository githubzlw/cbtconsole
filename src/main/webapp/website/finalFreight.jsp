<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>前三个月的运费</title>
<link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
<style type="text/css">
table.altrowstable { 
    margin-top:10px;
    width:1200px;                     
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
</style>
<script type="text/javascript"  src="${pageContext.request.contextPath}/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
function check(createtime){
	$.ajax({
		type:"post",
		url:'/cbtconsole/shipment/check',
		data:{createTime:createtime},
		dataType:'json',
		success:function(res){
			if(res>1){
				alert("验证成功!");
				window.location.reload();
			}
		}
	})
}
</script>
</head>
<body>
<h1 style="text-align:center;">前三个月的运费统计</h1>

<div style="margin-left:350px;margin-top:80px;">
<a href='/cbtconsole/shipment/exportAllFreight'  target="_blank"  class="btn btn-success" >导出Excel</a>
  <table  class="altrowstable">
      <tr style="background-color:#FFECE4;">
         <th>月份</th>
         <th>JCEX</th>
         <th>邮政</th>
         <th>原飞航</th>
         <th>其他</th>
         <th>总运费金额</th>
         <th>是否验证</th>
         <th></th>
      </tr>
     <c:forEach  var="freight"   items="${finalList }">
         <tr >
             <td>${freight.createTime }</td>
             <td>${freight.jecxFreight }</td>
             <td>${freight.emsintenFreight}</td>
             <td>${freight.yfhFreight }</td>
             <td>${freight.otherFreight }</td>
             <td>${freight.totalprice }</td>
             <td>
               <c:if test="${freight.passFlag==1 }">
                  <span>已经验证</span>
               </c:if>
               <c:if test="${freight.passFlag==0 }">
                  <span><font color="red">还未验证</font></span>
               </c:if>
             </td>
             <td><input type="button"  name="check"  value="验证"  onclick="check('${freight.createTime }')"></td>
         </tr>
     </c:forEach>
  </table>

</div>
</body>
</html>