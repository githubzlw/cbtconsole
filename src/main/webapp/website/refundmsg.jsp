<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.processes.servlet.Currency"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtprogram/img/mathematics1.ico" type="image/x-icon"/>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>

<style type="text/css">
body{
width: 100%;
}
</style>
<script type="text/javascript">

$(function(){
	var orderno = '${param.orderno}';
	var paramread = '${param.state}';
	$("#paramread").val(paramread);
	$("#orderno").val(orderno);
	
})

function fnget(){
	var state=$("#paramread").val();
	var orderno=$("#orderno").val();
	if(orderno==''&&state==''){
		return ;
	}
	
	window.location="/cbtconsole/refundss/messages?state="+state+"&orderno="+orderno;
}

</script>

<title>退款消息中心</title>
</head>

<body  class="bodym">
<br><br>
<div align="center" style="width: 1000px;margin: 0 auto;">
<div style="font-size: 25px;">退款消息</div>
<br>
<div class="paramdv">
消息状态:
<select id="paramread">
<option value="0" selected="selected">全部</option>
<option value="N">未读</option>
<option value="Y">已读</option>
</select>
&nbsp;&nbsp;&nbsp;&nbsp;订单号:<input value="" id="orderno">
&nbsp;&nbsp;&nbsp;&nbsp;<input value="查询" type="button" class="btn btn-primary btn-sm" onclick="fnget()">

&nbsp;&nbsp;&nbsp;&nbsp;操作人:<span>${admName }</span>
</div>
<br>
<div class="bodycsd">


<table style="table-layout: fixed;word-wrap:break-word;" class="table table-bordered  table-hover definewidth m10">
<tr style="background-color:#D2BBB5">
<td>#</td>
<td>订单号</td>
<td>内容</td>
<td>时间</td>
<td>状态</td>
</tr>
<c:forEach items="${msgList}" var="msg">
<tr>
<td>${msg.id }</td>
<td>${msg.orderNo }</td>
<td>${msg.sendContent }</td>
<td>${msg.createTime }</td>
<td>${msg.isRead=='N' ?'未读':'已读'}
&nbsp;&nbsp;(<a href="/cbtconsole/refundss/searchByParam?userid=0&username=&appdate=&agreeTime=&status=-3&startrow=1&type=-1&rid=${msg.refundId}&mid=${msg.id}">打开</a>)</td>
</tr>

</c:forEach>
</table>
<c:if test="${msgflag==1}">
<input type="hidden" value="${refundid }" id="refundid">
<input type="hidden" value="${msgid }" id="msgid">
<input type="hidden" value="${admName }" id="admName">

<script type="text/javascript">
var msg = $('#msgid').val();
var refundid = $('#refundid').val();
var admName = $('#admName').val();
window.location.href="/cbtconsole/refundss/searchByParam?&status=-3&startrow=1&type=-1&rid="+refundid+"&mid="+msg+"&deal="+admName;
</script>
</c:if>


</div>

</div>
</body>
</html>