<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ page import="com.cbt.bean.StorageLocationBean" %>
<%@ page import="java.util.*" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
</head>
<body>
<% 
List<StorageLocationBean> slblist = (List<StorageLocationBean>)request.getAttribute("storageLocationpageList");

String orderids = "";
for (int i=0; i<slblist.size(); i++){
	if(orderids.indexOf(slblist.get(i).getOrderid())== -1){
		orderids +=  slblist.get(i).getOrderid()+ ",";
	}
	
}
orderids = orderids.substring(0,orderids.length()-1);
String[] osArray = orderids.split(",");

%>
	 <form action="">
	 <div align="center">
	 		<h1>入库</h1>
	 		<%
	 			for(int j=0; j<osArray.length; j++){
	 		%>
	 			<div>订单账号<%=osArray[j] %></div>
	 			<table border="1" align="center" class="gridtable">
	 			<tr>
	 			<td>商品id</td>
	 			<td>图片</td>
	 			<td>库位号</td>	
	 			<td>入库时间</td>
	 			
		 	<%
		 			for(int i=0; i<slblist.size(); i++){
		 				StorageLocationBean storageLocation = slblist.get(i);
		 				if(storageLocation.getOrderid().equals(osArray[j]))
		 				{
		 	%>
			 			</tr>
				 			<td><%=storageLocation.getGoodid() %></td>  
				 			<td><img src="<%=storageLocation.getCar_img() %>"/></td>  
				 			<td><%=storageLocation.getPosition() %></td>
				 			<td><%=storageLocation.getCreatetime() %></td>
			 			</tr>
	 		<%
		 				}
	 				}
		 	%>
		 		</table>
		 		
		 	<%
	 			} 
	 		%>
	 		<div> 当前第   1   页    总共   1   页    总共   <%=slblist.size() %>  条记录	首页    上一页    下一页    点击此处  跳转至    
   页    尾页</div>
	 	</div>
	 </form>
</body>
</html>