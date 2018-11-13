<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>Drop Ship Apply List</title>
<script type="text/javascript">
	 function tosearch(){
			var uid = $("#userCategory").val();
			if(isNaN(uid)){
				alert("请输入数字");
				return false;
			}else{$("#query_form").submit();}
	};
</script>
</head>
<body>
<form action="dropshiplist.do" id="query_form" method="post">
	<%-- <div>
	<div align="center">
		<h3>Drop Ship Apply List</h3>
		<table>
				user_category:<input type="text" id="userCategory" value="${userCategory}"name="userCategory" >
				<td><a class="btn btn-light btn-xs" onclick="tosearch();"title="查询">查询</a></td>
		</table> 
	</div> --%>
	<div>
		<div align="center">
			<h3>Drop Ship Apply List</h3>
		</div>
	 
			<table id="table" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
				<Tr>
					<th>序号</th>
					<!-- <th>id</th> -->
					<th>user_id</th>
					<th>email</th>
					<th>website</th>
					<th>phone_number</th>
					<th>introduce</th>
					<th>time</th>
				</Tr>
				<c:forEach items="${ds }" var="ds" varStatus="i">
					<c:forEach items="${ds.dropShippApplys }" var="drop" >
						<Tr>
							<td>${i.index+1 }</td>
							<%-- <td>${drop.id}</td> --%>
							<td>${drop.userId}</td>
							<td>${drop.email}</td>
							<td>${drop.website}</td>
							<td>${drop.phoneNumber}</td>
							<td>${drop.introduce}</td>
							<td><fmt:formatDate value="${drop.time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</Tr>
					</c:forEach>
				</c:forEach>
			</table>
			<br/>
		<%-- 	<div align="center">${pager }</div> --%>
		</div>
	</div>
	</form>
		

	
</body>
</html>