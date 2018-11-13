<%@page import="com.cbt.util.WebCookie"%>
<%@ page import="com.cbt.refund.bean.RefundBean"%>
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
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">

.title{
	margin-left: 50%;
	color:red;
	font-size:2em;	
}

.mid{
    margin: 0 auto;
    width:  1500px;
    margin-top: 30px;
}

</style>

<title>退款报表</title>
</head>

<body >
<div class="mid">
	<p class="title" style="margin-top: 30px">退款报表</p>
</div>
	<div class="mid" align="center">
		
		<div style="margin-top: 20px">
		<a onclick="javascript:history.back(-1)" style="cursor: pointer;color:#00aff8;margin-left: -500px;">« Back</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>开始时间：</label>
				<input id="sdate" class="Wdate" style="width: 174px;height: 26px" type="text" value="${param.sdate }" onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-12-12',maxDate:'2020-12-20'})"/>
			
			
			<label>结束时间：</label>
			<input id="edate" class="Wdate" style="width: 174px;height: 26px" type="text" value="${param.edate }" onfocus="WdatePicker({skin:'whyGreen',minDate:'#F{$dp.$D(\'sdate\')}',maxDate:'2020-12-20'})"/>
			<label style="margin-left: 70px">退款原因：</label>
				<select style="height: 26px" id="reason">
					<option value="0">所有</option>
					<option value="1">产品无货</option>
					<option value="2">无法发货</option>
					<option value="3">交期延误</option>
					<option value="4">客户申请</option>
					<option value="5">其他原因</option>
				</select>
				<button class="btn btn-primary btn-sm" onclick="research()" style="margin-left: 35px">查询</button>
		</div>
		<div style="width:1500px;margin-top: 15px;">
			<table class="table table-bordered  table-hover definewidth m10" style="width: 1400px;">
			<tr>
			<td style="width: 200px;">申请时间</td>
			<td style="width: 100px;">退款金额</td>
			<td style="width: 200px;">退款状态</td>
			<td style="width: 200px;">客户账号</td>
			<td style="width: 200px;">PayPal邮箱</td>
			<td style="width: 500px;">原因</td>
			</tr>
			<c:forEach items="${list }" var="refund" >
			<tr>
			<td>${refund.apptime}</td>
			<td>
			 <em>${refund.appcount}&nbsp;${refund.currency}</em>
			<br>
			<em style="color:red;font-weight: bold;">${refund.account}&nbsp;${refund.currency}</em>
			</td>
			<td>
			<c:if test="${refund.status==0}">新申请</c:if>
			<c:if test="${refund.status==1}">销售同意退款</c:if>
			<c:if test="${refund.status==2}">完结</c:if>
			<c:if test="${refund.status==3}">管理员同意退款</c:if>
			<c:if test="${refund.status==-1}">销售驳回退款</c:if>
			<c:if test="${refund.status==-2}">用户取消</c:if>
			<c:if test="${refund.status==-3}">管理员驳回退款</c:if>
			<c:if test="${refund.status==-4}">退款失败</c:if>
			</td>
			
			<td>
			<em>用户id:<em style="color:#347936;font-weight: bold;">${refund.userid}</em></em>
			<br>
			<em>${refund.username}</em>
			<em></em>
			</td>
			<td>${refund.paypalname}</td>
			<td>${refund.remark}</td>
			</tr>
			</c:forEach>
			</table>
			
			<div>
					<a id="prepage">上一页</a>&nbsp;
					第<span id="pagenow">${pagenow }</span>页/共<span id="pagecount">${pagecount }</span>页&nbsp;
					<a id="nextpage">下一页</a>&nbsp;
					<input type="text" id="topage" value="${pagenow}" style="width:40px" onkeydown="if(event.keyCode==13){enterToJump()}"/>
					<input type="button" id="jumpToPage" value="Go" style="background-color:#337ab7;border-radius:3px;border: 1px solid transparent;color:#fff;"/>
		</div>
		
		</div>
		<br>
		<br>
	</div>
<script type="text/javascript">

var select = '${param.reason}';
$("#reason").val(select);

function research(){
	var stime = $("#sdate").val();
	var etime = $("#edate").val();
	var reason = $("#reason").val();
	var page = $("#topage").val();
	window.location="/cbtconsole/refundss/report?sdate="+stime+"&edate="+etime+"&reason="+reason+"&page="+page;
}

var startrow = 1;
$("#prepage").click(function(){
	var pagenow = parseInt($("#pagenow").text(), 10);
	if(pagenow==1){
		alert("当前是首页");
	}else{
		startrow=pagenow-1;
		$("#topage").val(startrow);
		research();
	}
});
$("#nextpage").click(function(){
	var pagenow = parseInt($("#pagenow").text(), 10);
	var pagecount = parseInt($("#pagecount").text(),10);
	if(pagenow==pagecount){
		alert("当前是尾页");
	}else{
		startrow=pagenow+1;
		$("#topage").val(startrow);
		research();
	}
});
$("#jumpToPage").click(function(){
	var pages = $("#topage").val();
	var pagecount = parseInt($("#pagecount").text(),10);
	if(pages==null){
		alert("请输入页码");
	}else if(isNaN(pages)){
		alert("请输入正确的页码");
	}else if(pages<1 || pages >pagecount){
		alert("页码超出范围");
	}else{
		startrow = pages;
		$("#topage").val(startrow);
		research();
	}
});

function enterToJump(){
	var pages = $("#topage").val();
	var pagecount = parseInt($("#pagecount").text(),10);
	if(pages==null){
		alert("请输入页码");
	}else if(isNaN(page)){
		alert("请输入正确的页码");
	}else if(pages<1 || pages >pagecount){
		alert("页码超出范围");
	}else{
		startrow = pages;
		$("#topage").val(startrow);
		research();
	}
}

</script>

</body>
</html>