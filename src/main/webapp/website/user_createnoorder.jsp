<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>商品未生成订单的用户信息</title>
<script type="text/javascript">
var page;
var action;
var pagetotal=${pagetotal};
$(function(){
	$("#counto").html('${usercount_1}');
	$("#count").html('${pagetotal}');
	var url=location.search;
	page=url.split('&page=')[1];
	$('#page').html(page);
	action=url.split('action=')[1].split('&page=')[0];
});
function fnpagePrevious(){
	if(page>1){
		page=parseInt(page)-1;
	}
	window.location.href="/cbtconsole/BlackListServlet?action="+action+"&page="+page;
}
function fnpageNext(){
	if(page<pagetotal){
		page=parseInt(page)+1;
	}
	window.location.href="/cbtconsole/BlackListServlet?action="+action+"&page="+page;
}
</script>
</head>
<body>
<div align="center">
    <div class="manager">
        <a href="/cbtconsole/BlackListServlet?action=userinfo_1&page=1">最近两个月</a>
        <a href="/cbtconsole/BlackListServlet?action=userinfo_2&page=1">最近一个月</a>
        <a href="/cbtconsole/BlackListServlet?action=userinfo_3&page=1">最近十五天</a>
    </div>
	<div>
		<table border="1">
		        <tr>  
			        <th>用户id</th> 
			        <th>用户名</th> 
			        <th>用户邮箱</th> 
			        <th>创建时间</th>
			        <th>总金额</th>  
			    </tr> 
				<c:forEach items="${userlist_1}" var="list" varStatus="status">     
				      <tr>  
				          <td>${list.id}</td>
				          <td>${list.name}</td>
				          <td>${list.email}</td>
				          <td>${list.createtime}</td>
				          <td>${total_1[status.index]}</td>
				      </tr>     
				</c:forEach> 
		</table> 
	</div>
	<div class="pages" id="pages">	  
		<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="count"></font></span>&nbsp;&nbsp;当前页<font id="page"></font><button onclick="fnpagePrevious()">上一页</button>&nbsp;<button  onclick="fnpageNext()">下一页</button>
	</div>
</div>
</body>
</html>