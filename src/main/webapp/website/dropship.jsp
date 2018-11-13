<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>Drop Ship</title>
<script type="text/javascript">
	var check = function(){
		var uid = $("#userid").val();
		if(isNaN(uid)){
			$("#ts").html("请输入数字");
			return false;
		}else{return true;}
	};
</script>
</head>
<body>
<div>
	<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br/>
	<div align="center">
	<form action="/cbtconsole/customerServlet?action=findAllDropShip&className=MoreActionServlet" onsubmit="return check();" method="post">
		<table>
			<tr>
				<td>userid:<input type="text" value="${userId }" id="userid" name="userId"><font id="ts" color="red"></font></td>
				<td>username:<input type="text" value="${userName }" id="username" name="userName"></td>
				<td>useremail:<input type="text" id="useremail" value="${useremail}"name="useremail" ></td>
				<td><input type="submit" value="查询"></td>
			</tr>
		</table> 
	</form>
	</div>
	<div>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>序号</th>
				<th>user_id</th>
				<th>user_name</th>
				<th>email</th>
				<th>goods_img</th>
				<th>goods_price</th>
				<th>currency</th>
				<th>min_order_quantity</th>
				<th>url</th>
				<th>create_time</th>
			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
			<Tr>
				<td>${i.index+1 }</td>
				<td>${gbb.userId }</td>
				<td>${gbb.userName }</td>
				<td>${gbb.email }</td>
				<td> <a target='_blank' href="${gbb.purl }" ><img  width='50px' title="${gbb.pname }" height='50px;' src="${gbb.img }" style='cursor: pointer;' ></a></td>
				<td>${gbb.fprice }</td>
				<td>${gbb.currency }</td>
				<td>${gbb.minOrder }</td>
				<td><a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&amp;url=${gbb.purl }" >${gbb.purl }</a></td>
				<td>${gbb.createTime }</td>
			</Tr>
			</c:forEach>
		</table>
		<br/>
		<div align="center">${pager }</div>
	</div>
</div>
</body>
</html>