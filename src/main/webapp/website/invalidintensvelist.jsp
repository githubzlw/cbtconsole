<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>屏蔽链接列表</title>
<style type="text/css">
body{width: 1200px;margin: 0 auto;}
.body{width: 1200px;height:auto;}
.table1 td{width: 300px;}
.table td{width: 300px;}
/* .error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.error4{background-color:#00FFFF;} */
a{
	cursor: pointer;
	text-decoration:underline;
}
.btn{    color: #fff;
    background-color: #5db5dc;
    border-color: #2e6da4;    
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;}


</style>


</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<div style="font-size: 25px;color: black;">
	查看已屏蔽关键词/类别
	</div>
	<br>
	<div align="right" style="">
	</div>
	<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table" >
	<tr align="center"  bgcolor="#DAF3F5" >
			<td style="width: 200px;" >关键词</td>
			<td style="width: 200px;" >类别</td>
			<td style="width: 200px;" >类别名称</td>
			<td style="width: 200px;">类型</td>
			<td style="width: 200px;">状态</td>
	</tr>
	
	<c:forEach items="${urlList}" var="list" varStatus="index" >
	<tr  bgcolor="#FFF7FB">
			<td style="width: 200px;" >${list.keyword}</td>
			<td style="width: 200px;" >${list.catid}</td>
			<td style="width: 200px;" >${list.category}</td>
			<td style="width: 200px;">${list.type}</td>
			<td style="width: 200px;">${list.status}</td>
	
	</tr>
	
	</c:forEach>
	
	</table>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${totalpage}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${currentpage}<em>/</em> ${totalpage}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${currentpage}" style="height: 26px;">
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		</div>
</div>
</body>
</html>