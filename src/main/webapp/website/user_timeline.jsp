<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>用户行为操作信息</title>
<script type="text/javascript">
function find(){
	$("#sb_form").submit();
}
function fnpagePrevious(){
	var page = parseInt($("#currentPage").html());
	if(page > 1){
		page = page - 1;	
	}else{
		return ;
	}
	$("#jump").val(page);
	find();
}
function fnpageNext(){
	var page = parseInt($("#currentPage").html());
	var totalPage = parseInt($("#count").html());
	if(page < totalPage){
	   page=page+1;
	}else{
		retrun ;
	}
	$("#jump").val(page);
	find();
}
//重置
$(function(){
	$("#reset").click(function(){
		$("#uid").val('');
		$("#view_date_time").val('');
	})
	
})
//数据导出
function exportExcel() {
	var str = "";
	var tab = $("#userbehavior tr").length;
	if (tab < 0) {
		alert("无数据导出");
		return;
	}
	for (var i = 0; i < tab; i++) {
		var tr = $("#userbehavior tr").eq(i);
		var td = tr.find("td").length;
		for (var j = 0; j < td - 1; j++) {
			var html = tr.find("td").eq(j).html();
			if (j == 1) {
				html = "'" + html;
			}
			str += html + ",";
		}
		str += "\n";
	}
	str = encodeURIComponent(str);
	var uri = 'data:text/csv;charset=utf-8,\ufeff' + str;
	var downloadLink = document.createElement("a");
	downloadLink.href = uri;
	downloadLink.download = "order.csv";
	document.body.appendChild(downloadLink);
	downloadLink.click();
	document.body.removeChild(downloadLink);
}
</script>
</head>
<body >
<div align="center">
	<form action="/cbtconsole/userstatisitcs/userbehavior" id="sb_form">
	<div>用户行为轨迹信息</div><p>
	<div>
	  ID搜索:<input type="text" id="uid" value="${userid}" name="userid">
	  访问时间:<input id="view_date_time" name="dateTime"
		readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="${view_date_time }"/>
	  <button type="button" onclick="find();">搜索</button>
	  <button type="button" id="reset">重置</button> <font color="red">(id为0,表示用户未登陆时访问)</font>
	</div>
	
	
	<div>
    	<table id="userbehavior" border="1" style="margin-top:20px;width:1000px;text-align: center">
    	<tr>
    	<td style="word-wrap:break-word;word-break:break-all;width:182px">用户id</td>
    	<td style="word-wrap:break-word;word-break:break-all;width:182px">访问关键字</td>
    	<td style="word-wrap:break-word;word-break:break-all;width:300px">访问链接</td>
    	<td style="word-wrap:break-word;word-break:break-all;width:300px">用户操作</td>
    	<td style="word-wrap:break-word;word-break:break-all;width:200px">访问时间</td>
    	</tr>
    	
    	<c:forEach var="bean" items="${list }">
    	<tr>
		<td style="word-wrap:break-word;word-break:break-all;width:182px">${bean.userid }</td>
		<td style="word-wrap:break-word;word-break:break-all;width:182px">${bean.keywords}</td>
		<td style="word-wrap:break-word;word-break:break-all;width:300px"><a href="${bean.view_url}">${bean.view_url}</a></td>
		<td style="word-wrap:break-word;word-break:break-all;width:300px">${bean.action}</td>
		<td style="word-wrap:break-word;word-break:break-all;width:200px">${bean.view_date_time}</td>
    	</tr>
    	</c:forEach>
    	</table>
    </div>
    <div id="page">
      <span>&nbsp;&nbsp;总条数：<font id="counto">${totalCount}</font>&nbsp;&nbsp;总页数：<font
				id="count">${totalPage }</font></span>&nbsp;&nbsp;当前页:<font id="currentPage">${page }</font>	  
		<button onclick="fnpagePrevious()">上一页</button>&nbsp;<button  onclick="fnpageNext()">下一页</button>
		&nbsp;<input id="jump" type="text" name="page" value="${page }">
			<button onclick="find()">转至</button>
		<button onclick="exportExcel()" style="margin-top: 18px;">导出本页</button>
	</div>
	</form>
</div>
    
</body>
</html>