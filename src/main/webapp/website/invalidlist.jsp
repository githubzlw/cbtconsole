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
<script type="text/javascript">
function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){
		if(parseInt(page)<2){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}else if(obj==-2){
		page=1;
	}
	
	$("#page").val(page);
	var type = $("#type").val();
	window.location.href="/cbtconsole/invalidgc/ilist?page="+page+"&type="+type
}

$(document).ready(function(){
	var type = '${type}';
	$("#type").val(type);
	
});


</script>

</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<div style="font-size: 25px;color: black;">
	<c:if test="${type==1 }">查看已屏蔽商店</c:if>
	<c:if test="${type==0 }">查看已屏蔽产品</c:if>
	</div>
	<br>
	<div align="right" style="">
	<select id="type" style="font-size: 25px;width: 100px;color: #6a6aff;font-weight: bold;"  onchange="fnjump(-2)">
	<option value="1">商店</option>
	<option value="0">产品</option>
	</select>
	</div>
	<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table" >
	<tr align="center"  bgcolor="#DAF3F5" >
			<td style="width: 1000px;" >链接</td>
			
			<td style="width: 200px;">时间</td>
	</tr>
	
	<c:forEach items="${urlList}" var="list" varStatus="index" >
	<tr  bgcolor="#FFF7FB">
	<td id="user_${list.id}"><a href="${list.url}" target="_blank">${list.url}</a></td>
	<td id="order_${list.id}">${list.createtime}&nbsp;&nbsp;
	<c:if test="${type==0}"><a href="https://www.import-express.com/spider/detail?${list.aliUrl}" target="_blank">验证</a>
	</c:if></td>
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