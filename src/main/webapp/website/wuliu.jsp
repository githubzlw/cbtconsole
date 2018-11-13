<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${ctx }/css/warehousejs/Tracker.css">
<script type="text/javascript" src="${ctx }/js/jquery-1.10.2.js"></script>
<title>物流详情</title>
<style type="text/css">

</style>
</head>
<script type="text/javascript">
</script>

<body>  
	<div class="tab-wrap">
	<div class="tab-sp" >Express Logistics Information</div>
	         <span style=" float:left;width:800px; margin-left:20px;padding-bottom:10px;"><b>Tracking Number:</b>${trackBean.expressno } <b style="margin-left:50px;"> Transportation:</b>${trackBean.company}</span>
	         <table class="tab-main" >
	           <tr class="tab-tr1">
	             <td class="tab-tr2 ">Time</td>
	             <td  class="tab-tr3 ">Location And Tracking Progress</td>
	           </tr>
	           <c:if test="${trackBean.errorInfo!= null}">
	           	 <tr>
		           	<td></td>
		           	<td>${trackBean.errorInfo}</td>
		         </tr>
	           </c:if>
	           <c:if test="${trackBean.errorInfo== null}">
	             <c:forEach var="bean"  items="${trackBean.tList}">
	                <tr>
			           	<td>${bean.createtime}</td>
			           	<td>${bean.context}</td>
		             </tr>
	             </c:forEach>
	           </c:if>
	         </table>
	</div>
</body>
</html>